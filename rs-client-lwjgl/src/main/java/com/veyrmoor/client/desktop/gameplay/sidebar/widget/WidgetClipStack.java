package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glScissor;

import com.veyrmoor.client.desktop.render.common.ScreenRect;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import org.lwjgl.BufferUtils;

final class WidgetClipStack {

  private final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4);
  private final Deque<ClipEntry> clipStack = new ArrayDeque<>();

  void push(ScreenRect clipRect) {
    clipStack.push(new ClipEntry(clipRect));
    applyClipRect(clipRect);
  }

  void pop() {
    if (clipStack.isEmpty()) {
      applyClipRect(null);
      return;
    }
    clipStack.pop();
    applyClipRect(clipStack.isEmpty() ? null : clipStack.peek().clipRect());
  }

  void clear() {
    clipStack.clear();
    applyClipRect(null);
  }

  private void applyClipRect(ScreenRect clipRect) {
    if (clipRect == null) {
      glDisable(GL_SCISSOR_TEST);
      return;
    }
    int scissorWidth = Math.max(0, Math.round(clipRect.width()));
    int scissorHeight = Math.max(0, Math.round(clipRect.height()));
    if (scissorWidth <= 0 || scissorHeight <= 0) {
      glEnable(GL_SCISSOR_TEST);
      glScissor(0, 0, 0, 0);
      return;
    }
    viewportBuffer.clear();
    glGetIntegerv(GL_VIEWPORT, viewportBuffer);
    int viewportHeight = viewportBuffer.get(3);
    int scissorLeft = Math.round(clipRect.left());
    int scissorBottom = viewportHeight - Math.round(clipRect.top() + clipRect.height());
    glEnable(GL_SCISSOR_TEST);
    glScissor(scissorLeft, scissorBottom, scissorWidth, scissorHeight);
  }

  private record ClipEntry(ScreenRect clipRect) {
  }
}
