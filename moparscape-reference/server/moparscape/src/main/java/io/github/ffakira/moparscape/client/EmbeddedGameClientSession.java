package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.sign.SignLink;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public final class EmbeddedGameClientSession implements AutoCloseable {

  private final EmbeddedGameClientHost host;
  private final GameClient gameClient;

  EmbeddedGameClientSession(EmbeddedGameClientHost host, GameClient gameClient) {
    this.host = host;
    this.gameClient = gameClient;
  }

  public LegacyFrameSnapshot snapshotFrame() {
    return host.snapshot();
  }

  public boolean isLoggedIn() {
    return gameClient.aBoolean1157;
  }

  public LegacyLoginStatusSnapshot snapshotLoginStatus() {
    return gameClient.snapshotLegacyLoginStatus();
  }

  public void keyPressed(int keyCode, char keyChar) {
    gameClient.keyPressed(
        new KeyEvent(host, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, keyChar)
    );
  }

  public void keyReleased(int keyCode, char keyChar) {
    gameClient.keyReleased(
        new KeyEvent(host, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, keyChar)
    );
  }

  public void mouseMoved(int x, int y) {
    gameClient.mouseMoved(
        new MouseEvent(host, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false)
    );
  }

  public void mouseDragged(int x, int y) {
    gameClient.mouseDragged(
        new MouseEvent(host, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, x, y, 0, false)
    );
  }

  public void mousePressed(int x, int y, boolean metaDown) {
    int modifiers = metaDown ? InputEvent.BUTTON3_DOWN_MASK : InputEvent.BUTTON1_DOWN_MASK;
    int button = metaDown ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1;
    gameClient.mousePressed(
        new MouseEvent(host, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), modifiers, x, y, 1, metaDown, button)
    );
  }

  public void mouseReleased(int x, int y, boolean metaDown) {
    int modifiers = metaDown ? InputEvent.BUTTON3_DOWN_MASK : InputEvent.BUTTON1_DOWN_MASK;
    int button = metaDown ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1;
    gameClient.mouseReleased(
        new MouseEvent(host, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), modifiers, x, y, 1, metaDown, button)
    );
  }

  @Override
  public void close() {
    gameClient.method3(4747);
    GameClientCore.clearEmbeddedHost();
    SignLink.mainapp = null;
    host.disposeHost();
  }
}
