package io.github.ffakira.rsps.client.desktop.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

  void drawMaskedRotatedTexturedRows(
      OpenGlTexture texture,
      ScreenRect rect,
      int logicalWidth,
      int[] clipStarts,
      int[] clipWidths,
      float angleDegrees,
      float sourceCenterX,
      float sourceCenterY
  ) {
    if (texture == null || clipStarts.length == 0 || clipStarts.length != clipWidths.length) {
      return;
    }
    // The original client draws the compass as a rotated masked blit with row-by-row clip spans
    // from `mapback`. Recreating that contract here fixes the oversized square look from the
    // earlier generic oval draw path.
    float logicalHeight = clipStarts.length;
    float scaleX = rect.width() / logicalWidth;
    float scaleY = rect.height() / logicalHeight;
    float radians = (float) Math.toRadians(angleDegrees);
    float sine = (float) Math.sin(radians);
    float cosine = (float) Math.cos(radians);
    beginTexturedAlpha();
    glBindTexture(GL_TEXTURE_2D, texture.id());
    glColor3f(1.0f, 1.0f, 1.0f);
    glBegin(GL_QUADS);
    for (int row = 0; row < clipStarts.length; row++) {
      int topStart = clipStarts[row];
      int topWidth = clipWidths[row];
      if (topWidth <= 0) {
        continue;
      }
      int nextRow = Math.min(row + 1, clipStarts.length - 1);
      int bottomStart = row == clipStarts.length - 1 ? topStart : clipStarts[nextRow];
      int bottomWidth = row == clipStarts.length - 1 ? topWidth : clipWidths[nextRow];

      float[] topLeftUv = rotatedSpriteUv(
          topStart,
          row,
          logicalWidth,
          logicalHeight,
          cosine,
          sine,
          sourceCenterX,
          sourceCenterY,
          texture.width(),
          texture.height()
      );
      float[] topRightUv = rotatedSpriteUv(
          topStart + topWidth,
          row,
          logicalWidth,
          logicalHeight,
          cosine,
          sine,
          sourceCenterX,
          sourceCenterY,
          texture.width(),
          texture.height()
      );
      float[] bottomRightUv = rotatedSpriteUv(
          bottomStart + bottomWidth,
          row + 1,
          logicalWidth,
          logicalHeight,
          cosine,
          sine,
          sourceCenterX,
          sourceCenterY,
          texture.width(),
          texture.height()
      );
      float[] bottomLeftUv = rotatedSpriteUv(
          bottomStart,
          row + 1,
          logicalWidth,
          logicalHeight,
          cosine,
          sine,
          sourceCenterX,
          sourceCenterY,
          texture.width(),
          texture.height()
      );

      float topLeftX = rect.left() + topStart * scaleX;
      float topRightX = rect.left() + (topStart + topWidth) * scaleX;
      float bottomLeftX = rect.left() + bottomStart * scaleX;
      float bottomRightX = rect.left() + (bottomStart + bottomWidth) * scaleX;
      float topY = rect.top() + row * scaleY;
      float bottomY = rect.top() + (row + 1) * scaleY;

      glTexCoord2f(topLeftUv[0], topLeftUv[1]);
      glVertex2f(topLeftX, topY);
      glTexCoord2f(topRightUv[0], topRightUv[1]);
      glVertex2f(topRightX, topY);
      glTexCoord2f(bottomRightUv[0], bottomRightUv[1]);
      glVertex2f(bottomRightX, bottomY);
      glTexCoord2f(bottomLeftUv[0], bottomLeftUv[1]);
      glVertex2f(bottomLeftX, bottomY);
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
    drawText(left, top, text, red, green, blue, 1.0f);
  }

  void drawText(float left, float top, String text, float red, float green, float blue, float scale) {
    if (text == null || text.isEmpty()) {
      return;
    }
    if (Math.max(1024, text.length() * 270) > textVertexBuffer.capacity()) {
      throw new IllegalArgumentException("Text is too large for the UI text buffer");
    }
    textVertexBuffer.clear();
    int quadCount = STBEasyFont.stb_easy_font_print(0.0f, 0.0f, text, null, textVertexBuffer);
    transformTextVertices(quadCount * 4, left, top, scale);
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
    float textWidth = measureTextWidth(text);
    drawText(centerX - textWidth / 2.0f, top, text, red, green, blue);
  }

  float measureTextWidth(String text) {
    return measureTextWidth(text, 1.0f);
  }

  float measureTextWidth(String text, float scale) {
    if (text == null || text.isEmpty()) {
      return 0.0f;
    }
    return STBEasyFont.stb_easy_font_width(text) * scale;
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

  private void transformTextVertices(int vertexCount, float left, float top, float scale) {
    FloatBuffer floatBuffer = textVertexBuffer.asFloatBuffer();
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      int baseFloatIndex = vertexIndex * 4;
      floatBuffer.put(baseFloatIndex, left + floatBuffer.get(baseFloatIndex) * scale);
      floatBuffer.put(baseFloatIndex + 1, top + floatBuffer.get(baseFloatIndex + 1) * scale);
    }
  }

  private float[] rotatedSpriteUv(
      float sampleX,
      float sampleY,
      float logicalWidth,
      float logicalHeight,
      float cosine,
      float sine,
      float sourceCenterX,
      float sourceCenterY,
      int textureWidth,
      int textureHeight
  ) {
    float centeredX = sampleX - logicalWidth / 2.0f;
    float centeredY = sampleY - logicalHeight / 2.0f;
    float sourceX = sourceCenterX + centeredY * sine + centeredX * cosine;
    float sourceY = sourceCenterY + centeredY * cosine - centeredX * sine;
    float clampedX = clamp(sourceX, 0.0f, textureWidth - 1.0f);
    float clampedY = clamp(sourceY, 0.0f, textureHeight - 1.0f);
    return new float[]{
        clampedX / textureWidth,
        clampedY / textureHeight
    };
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
