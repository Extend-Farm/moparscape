package io.github.ffakira.rsps.client.lwjgl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBEasyFont;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertexPointer;

final class ImmediateModeRenderer2d {

  private static final int TEXT_VERTEX_BUFFER_CAPACITY = 64 * 1024;

  private final ByteBuffer textVertexBuffer = BufferUtils.createByteBuffer(TEXT_VERTEX_BUFFER_CAPACITY);

  void drawTexturedQuad(OpenGlTexture texture, ScreenRect rect) {
    if (texture == null) {
      return;
    }
    drawCroppedTexturedQuad(texture, 0, 0, texture.width(), texture.height(), rect);
  }

  void drawCroppedTexturedQuad(OpenGlTexture texture, int sourceX, int sourceY, int sourceWidth, int sourceHeight, ScreenRect rect) {
    if (texture == null) {
      return;
    }
    float u0 = sourceX / (float) texture.width();
    float v0 = sourceY / (float) texture.height();
    float u1 = (sourceX + sourceWidth) / (float) texture.width();
    float v1 = (sourceY + sourceHeight) / (float) texture.height();
    beginTexturedAlpha();
    glBindTexture(GL_TEXTURE_2D, texture.id());
    glColor3f(1.0f, 1.0f, 1.0f);
    glBegin(GL_QUADS);
    glTexCoord2f(u0, v0);
    glVertex2f(rect.left(), rect.top());
    glTexCoord2f(u1, v0);
    glVertex2f(rect.left() + rect.width(), rect.top());
    glTexCoord2f(u1, v1);
    glVertex2f(rect.left() + rect.width(), rect.top() + rect.height());
    glTexCoord2f(u0, v1);
    glVertex2f(rect.left(), rect.top() + rect.height());
    glEnd();
    endTexturedAlpha();
  }

  void drawCroppedTexturedOval(OpenGlTexture texture, int sourceX, int sourceY, int sourceWidth, int sourceHeight, ScreenRect rect) {
    if (texture == null) {
      return;
    }
    float u0 = sourceX / (float) texture.width();
    float v0 = sourceY / (float) texture.height();
    float u1 = (sourceX + sourceWidth) / (float) texture.width();
    float v1 = (sourceY + sourceHeight) / (float) texture.height();
    float centerX = rect.left() + rect.width() * 0.5f;
    float centerY = rect.top() + rect.height() * 0.5f;
    float radiusX = rect.width() * 0.5f;
    float radiusY = rect.height() * 0.5f;
    float centerU = (u0 + u1) * 0.5f;
    float centerV = (v0 + v1) * 0.5f;
    float radiusU = (u1 - u0) * 0.5f;
    float radiusV = (v1 - v0) * 0.5f;
    int segments = 48;
    beginTexturedAlpha();
    glBindTexture(GL_TEXTURE_2D, texture.id());
    glColor3f(1.0f, 1.0f, 1.0f);
    glBegin(GL_TRIANGLE_FAN);
    glTexCoord2f(centerU, centerV);
    glVertex2f(centerX, centerY);
    for (int segment = 0; segment <= segments; segment++) {
      double angle = (Math.PI * 2.0 * segment) / segments - (Math.PI / 2.0);
      float cosine = (float) Math.cos(angle);
      float sine = (float) Math.sin(angle);
      glTexCoord2f(centerU + cosine * radiusU, centerV + sine * radiusV);
      glVertex2f(centerX + cosine * radiusX, centerY + sine * radiusY);
    }
    glEnd();
    endTexturedAlpha();
  }

  void drawQuad(float left, float top, float width, float height) {
    glBegin(GL_QUADS);
    glVertex2f(left, top);
    glVertex2f(left + width, top);
    glVertex2f(left + width, top + height);
    glVertex2f(left, top + height);
    glEnd();
  }

  void drawOutline(float left, float top, float width, float height) {
    glBegin(GL_LINE_LOOP);
    glVertex2f(left, top);
    glVertex2f(left + width, top);
    glVertex2f(left + width, top + height);
    glVertex2f(left, top + height);
    glEnd();
  }

  void drawText(float left, float top, String text, float red, float green, float blue) {
    if (text == null || text.isEmpty()) {
      return;
    }
    if (Math.max(1024, text.length() * 270) > textVertexBuffer.capacity()) {
      throw new IllegalArgumentException("Text is too large for the UI text buffer");
    }
    textVertexBuffer.clear();
    int quadCount = STBEasyFont.stb_easy_font_print(left, top, text, null, textVertexBuffer);
    glColor3f(red, green, blue);
    glEnableClientState(GL_VERTEX_ARRAY);
    glVertexPointer(2, GL_FLOAT, 16, textVertexBuffer);
    glDrawArrays(GL_QUADS, 0, quadCount * 4);
    glDisableClientState(GL_VERTEX_ARRAY);
  }

  void drawCenteredText(float centerX, float top, String text, float red, float green, float blue) {
    if (text == null || text.isEmpty()) {
      return;
    }
    float textWidth = STBEasyFont.stb_easy_font_width(text);
    drawText(centerX - textWidth / 2.0f, top, text, red, green, blue);
  }

  private void beginTexturedAlpha() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_TEXTURE_2D);
  }

  private void endTexturedAlpha() {
    glBindTexture(GL_TEXTURE_2D, 0);
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_BLEND);
  }
}
