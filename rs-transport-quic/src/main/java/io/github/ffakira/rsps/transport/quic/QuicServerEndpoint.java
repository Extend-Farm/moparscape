package io.github.ffakira.rsps.transport.quic;

import io.github.ffakira.rsps.protocol.ClientMessage;
import io.github.ffakira.rsps.protocol.ProtocolBinaryCodec;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import io.github.ffakira.rsps.server.runtime.PlayerSessionActor;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.quic.InsecureQuicTokenHandler;
import io.netty.handler.codec.quic.QuicServerCodecBuilder;
import io.netty.handler.codec.quic.QuicSslContext;
import io.netty.handler.codec.quic.QuicSslContextBuilder;
import io.netty.handler.codec.quic.QuicStreamChannel;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class QuicServerEndpoint implements AutoCloseable {

  private static final int MAX_FRAME_BYTES = 1_048_576;
  private static final long INITIAL_MAX_DATA_BYTES = 10_000_000L;
  private static final long INITIAL_MAX_STREAM_DATA_BYTES = 1_000_000L;
  private static final long INITIAL_MAX_STREAM_COUNT = 16L;
  private static final Duration IDLE_TIMEOUT = Duration.ofSeconds(30);

  private final MultiThreadIoEventLoopGroup eventLoopGroup;
  private final Channel datagramChannel;

  private QuicServerEndpoint(MultiThreadIoEventLoopGroup eventLoopGroup, Channel datagramChannel) {
    this.eventLoopGroup = eventLoopGroup;
    this.datagramChannel = datagramChannel;
  }

  public static QuicServerEndpoint start(InProcessServerRuntime runtime, QuicTransportConfiguration configuration) {
    QuicCertificateStore certificateStore =
        QuicCertificateStore.openServer(configuration.certificateDirectory(), configuration.host());
    MultiThreadIoEventLoopGroup eventLoopGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
    try {
      QuicSslContext sslContext = QuicSslContextBuilder.forServer(
              certificateStore.privateKeyFileHandle(),
              null,
              certificateStore.certificateFileHandle()
          )
          .applicationProtocols(configuration.applicationProtocol())
          .build();
      Channel datagramChannel = new Bootstrap()
          .group(eventLoopGroup)
          .channel(NioDatagramChannel.class)
          .handler(new QuicServerCodecBuilder()
              .sslContext(sslContext)
              .maxIdleTimeout(IDLE_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
              .initialMaxData(INITIAL_MAX_DATA_BYTES)
              .initialMaxStreamDataBidirectionalLocal(INITIAL_MAX_STREAM_DATA_BYTES)
              .initialMaxStreamDataBidirectionalRemote(INITIAL_MAX_STREAM_DATA_BYTES)
              .initialMaxStreamsBidirectional(INITIAL_MAX_STREAM_COUNT)
              .initialMaxStreamsUnidirectional(0)
              .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
              .handler(new ChannelInboundHandlerAdapter())
              .streamHandler(new ChannelInitializer<QuicStreamChannel>() {
                @Override
                protected void initChannel(QuicStreamChannel channel) {
                  QuicProtocolSession protocolSession = new QuicProtocolSession(channel);
                  PlayerSessionActor playerSessionActor = runtime.openSession(protocolSession);
                  channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_BYTES, 0, 4, 0, 4));
                  channel.pipeline().addLast(new LengthFieldPrepender(4));
                  channel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext context, ByteBuf message) {
                      byte[] payload = new byte[message.readableBytes()];
                      message.readBytes(payload);
                      ClientMessage clientMessage = ProtocolBinaryCodec.decodeClientMessage(payload);
                      playerSessionActor.accept(clientMessage);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext context) {
                      playerSessionActor.close();
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
                      playerSessionActor.close();
                      context.close();
                    }
                  });
                }
              })
              .build())
          .bind(new InetSocketAddress(configuration.bindHost(), configuration.port()))
          .syncUninterruptibly()
          .channel();
      return new QuicServerEndpoint(eventLoopGroup, datagramChannel);
    } catch (Exception exception) {
      eventLoopGroup.shutdownGracefully();
      throw new IllegalStateException(
          "Unable to start QUIC server on %s:%d".formatted(configuration.bindHost(), configuration.port()),
          exception
      );
    }
  }

  public void await() {
    datagramChannel.closeFuture().syncUninterruptibly();
  }

  @Override
  public void close() {
    try {
      datagramChannel.close().syncUninterruptibly();
    } finally {
      eventLoopGroup.shutdownGracefully();
    }
  }
}
