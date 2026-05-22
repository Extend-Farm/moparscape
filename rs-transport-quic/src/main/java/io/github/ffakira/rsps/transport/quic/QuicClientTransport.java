package io.github.ffakira.rsps.transport.quic;

import io.github.ffakira.rsps.client.core.ClientTransport;
import io.github.ffakira.rsps.protocol.ClientMessage;
import io.github.ffakira.rsps.protocol.ProtocolBinaryCodec;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
import io.netty.handler.codec.quic.QuicChannel;
import io.netty.handler.codec.quic.QuicClientCodecBuilder;
import io.netty.handler.codec.quic.QuicSslContext;
import io.netty.handler.codec.quic.QuicSslContextBuilder;
import io.netty.handler.codec.quic.QuicStreamChannel;
import io.netty.handler.codec.quic.QuicStreamType;
import io.netty.handler.ssl.util.FingerprintTrustManagerFactory;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.Duration;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HexFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class QuicClientTransport implements ClientTransport {

  private static final int MAX_FRAME_BYTES = 1_048_576;
  private static final long INITIAL_MAX_DATA_BYTES = 10_000_000L;
  private static final long INITIAL_MAX_STREAM_DATA_BYTES = 1_000_000L;
  private static final Duration IDLE_TIMEOUT = Duration.ofSeconds(30);

  private final MultiThreadIoEventLoopGroup eventLoopGroup;
  private final Channel datagramChannel;
  private final QuicChannel quicChannel;
  private final QuicStreamChannel streamChannel;
  private final AtomicBoolean closed = new AtomicBoolean();

  private QuicClientTransport(
      MultiThreadIoEventLoopGroup eventLoopGroup,
      Channel datagramChannel,
      QuicChannel quicChannel,
      QuicStreamChannel streamChannel
  ) {
    this.eventLoopGroup = eventLoopGroup;
    this.datagramChannel = datagramChannel;
    this.quicChannel = quicChannel;
    this.streamChannel = streamChannel;
  }

  public static QuicClientTransport connect(
      QuicTransportConfiguration configuration,
      Consumer<ServerMessage> inboundConsumer
  ) {
    QuicCertificateStore certificateStore = QuicCertificateStore.openClient(configuration.certificateDirectory());
    MultiThreadIoEventLoopGroup eventLoopGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
    Channel datagramChannel = null;
    QuicChannel quicChannel = null;
    QuicStreamChannel streamChannel = null;
    try {
      String pinnedFingerprint = pinnedFingerprint(certificateStore);
      QuicSslContext sslContext = QuicSslContextBuilder.forClient()
          .trustManager(FingerprintTrustManagerFactory.builder("SHA-256").fingerprints(pinnedFingerprint).build())
          .applicationProtocols(configuration.applicationProtocol())
          .build();
      datagramChannel = new Bootstrap()
          .group(eventLoopGroup)
          .channel(NioDatagramChannel.class)
          .handler(new QuicClientCodecBuilder()
              .sslContext(sslContext)
              .maxIdleTimeout(IDLE_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
              .initialMaxData(INITIAL_MAX_DATA_BYTES)
              .initialMaxStreamDataBidirectionalLocal(INITIAL_MAX_STREAM_DATA_BYTES)
              .build())
          .bind(0)
          .syncUninterruptibly()
          .channel();
      quicChannel = QuicChannel.newBootstrap(datagramChannel)
          .streamHandler(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext context) {
              context.close();
            }
          })
          .remoteAddress(new InetSocketAddress(configuration.host(), configuration.port()))
          .connect()
          .get(30, TimeUnit.SECONDS);
      streamChannel = quicChannel.createStream(
              QuicStreamType.BIDIRECTIONAL,
              new ChannelInitializer<QuicStreamChannel>() {
                @Override
                protected void initChannel(QuicStreamChannel channel) {
                  channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(MAX_FRAME_BYTES, 0, 4, 0, 4));
                  channel.pipeline().addLast(new LengthFieldPrepender(4));
                  channel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext context, ByteBuf message) {
                      byte[] payload = new byte[message.readableBytes()];
                      message.readBytes(payload);
                      inboundConsumer.accept(ProtocolBinaryCodec.decodeServerMessage(payload));
                    }
                  });
                }
              })
          .get(30, TimeUnit.SECONDS);
      return new QuicClientTransport(eventLoopGroup, datagramChannel, quicChannel, streamChannel);
    } catch (Exception exception) {
      closeQuietly(streamChannel);
      closeQuietly(quicChannel);
      closeQuietly(datagramChannel);
      eventLoopGroup.shutdownGracefully();
      throw new IllegalStateException(
          "Unable to connect QUIC transport to %s:%d".formatted(configuration.host(), configuration.port()),
          exception
      );
    }
  }

  @Override
  public void send(ClientMessage message) {
    if (closed.get() || !streamChannel.isActive()) {
      return;
    }
    streamChannel.writeAndFlush(Unpooled.wrappedBuffer(ProtocolBinaryCodec.encodeClientMessage(message)));
  }

  @Override
  public void close() {
    if (!closed.compareAndSet(false, true)) {
      return;
    }
    try {
      closeQuietly(streamChannel);
      closeQuietly(quicChannel);
      closeQuietly(datagramChannel);
    } finally {
      eventLoopGroup.shutdownGracefully();
    }
  }

  private static void closeQuietly(Channel channel) {
    if (channel != null) {
      channel.close().syncUninterruptibly();
    }
  }

  private static String pinnedFingerprint(QuicCertificateStore certificateStore) {
    try (var inputStream = Files.newInputStream(certificateStore.certificateFile())) {
      X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
          .generateCertificate(inputStream);
      byte[] fingerprint = MessageDigest.getInstance("SHA-256").digest(certificate.getEncoded());
      return HexFormat.of().withUpperCase().formatHex(fingerprint);
    } catch (Exception exception) {
      throw new IllegalStateException(
          "Unable to load pinned QUIC certificate %s".formatted(certificateStore.certificateFile()),
          exception
      );
    }
  }
}
