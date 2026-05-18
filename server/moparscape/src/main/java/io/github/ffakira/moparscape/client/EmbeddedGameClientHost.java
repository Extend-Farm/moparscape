package io.github.ffakira.moparscape.client;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.net.MalformedURLException;
import java.net.URL;

final class EmbeddedGameClientHost extends Applet {

  private final BufferedImage frameImage;
  private final Graphics frameGraphics;
  private final URL codeBaseUrl;

  EmbeddedGameClientHost(int width, int height, String serverAddress) {
    frameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    frameGraphics = frameImage.getGraphics();
    setPreferredSize(new Dimension(width, height));
    setSize(width, height);
    codeBaseUrl = resolveCodeBaseUrl(serverAddress);
  }

  LegacyFrameSnapshot snapshot() {
    int[] sourcePixels = ((DataBufferInt) frameImage.getRaster().getDataBuffer()).getData();
    int[] frameCopy = sourcePixels.clone();
    return new LegacyFrameSnapshot(frameImage.getWidth(), frameImage.getHeight(), frameCopy);
  }

  void disposeHost() {
    frameGraphics.dispose();
  }

  @Override
  public Graphics getGraphics() {
    return frameGraphics;
  }

  @Override
  public FontMetrics getFontMetrics(Font font) {
    return frameGraphics.getFontMetrics(font);
  }

  @Override
  public Image createImage(ImageProducer producer) {
    return Toolkit.getDefaultToolkit().createImage(producer);
  }

  @Override
  public boolean prepareImage(Image image, ImageObserver observer) {
    return Toolkit.getDefaultToolkit().prepareImage(image, -1, -1, observer);
  }

  @Override
  public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
    return Toolkit.getDefaultToolkit().prepareImage(image, width, height, observer);
  }

  @Override
  public URL getCodeBase() {
    return codeBaseUrl;
  }

  @Override
  public URL getDocumentBase() {
    return codeBaseUrl;
  }

  private static URL resolveCodeBaseUrl(String serverAddress) {
    GameServerEndpoint endpoint = GameServerEndpoint.fromConfiguredAddress(serverAddress);
    try {
      return endpoint.toCodeBaseUrl();
    } catch (MalformedURLException exception) {
      throw new IllegalArgumentException("Unable to resolve an embedded code base URL", exception);
    }
  }
}
