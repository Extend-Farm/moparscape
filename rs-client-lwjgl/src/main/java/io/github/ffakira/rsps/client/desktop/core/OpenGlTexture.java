package io.github.ffakira.rsps.client.desktop.core;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public final class OpenGlTexture implements AutoCloseable {

  public enum FilterMode {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR);

    private final int glValue;

    FilterMode(int glValue) {
      this.glValue = glValue;
    }
  }

  private final int id;
  private final int width;
  private final int height;
  private final ByteBuffer uploadBuffer;

  private OpenGlTexture(int id, int width, int height, ByteBuffer uploadBuffer) {
    this.id = id;
    this.width = width;
    this.height = height;
    this.uploadBuffer = uploadBuffer;
  }

  public static OpenGlTexture fromArgbImage(ArgbImage image) {
    return fromArgbImage(image, FilterMode.NEAREST);
  }

  public static OpenGlTexture fromArgbImage(ArgbImage image, FilterMode filterMode) {
    OpenGlTexture texture = create(image.width(), image.height(), filterMode);
    texture.update(image);
    return texture;
  }

  /**
   * Builds a texture with mipmaps and {@code GL_NEAREST_MIPMAP_LINEAR} minification. Used by the
   * world renderer for scene textures (water, stone, dirt) so the pixel-art look stays crisp at
   * close range but doesn't shimmer / moiré at distance, where adjacent tile pixels otherwise
   * collapse into the same texel. Magnification stays {@code GL_NEAREST} to preserve the legacy
   * sharp-pixel aesthetic when textures are sampled larger than 1:1.
   */
  public static OpenGlTexture fromArgbImageWithMipmaps(ArgbImage image) {
    OpenGlTexture texture = createWithMipmaps(image.width(), image.height());
    texture.update(image);
    glBindTexture(GL_TEXTURE_2D, texture.id());
    glGenerateMipmap(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, 0);
    return texture;
  }

  public static OpenGlTexture create(int width, int height) {
    return create(width, height, FilterMode.NEAREST);
  }

  public static OpenGlTexture create(int width, int height, FilterMode filterMode) {
    int textureId = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode.glValue);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode.glValue);
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGBA,
        width,
        height,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        (ByteBuffer) null
    );
    glBindTexture(GL_TEXTURE_2D, 0);
    return new OpenGlTexture(textureId, width, height, BufferUtils.createByteBuffer(width * height * 4));
  }

  private static OpenGlTexture createWithMipmaps(int width, int height) {
    int textureId = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGBA,
        width,
        height,
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        (ByteBuffer) null
    );
    glBindTexture(GL_TEXTURE_2D, 0);
    return new OpenGlTexture(textureId, width, height, BufferUtils.createByteBuffer(width * height * 4));
  }

  public int id() {
    return id;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public void update(ArgbImage image) {
    if (image.width() != width || image.height() != height) {
      throw new IllegalArgumentException("Texture dimensions do not match the uploaded image");
    }
    glBindTexture(GL_TEXTURE_2D, id);
    glTexSubImage2D(
        GL_TEXTURE_2D,
        0,
        0,
        0,
        image.width(),
        image.height(),
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        toByteBuffer(image, uploadBuffer)
    );
    glBindTexture(GL_TEXTURE_2D, 0);
  }

  @Override
  public void close() {
    glDeleteTextures(id);
  }

  private static ByteBuffer toByteBuffer(ArgbImage image, ByteBuffer buffer) {
    buffer.clear();
    for (int pixel : image.pixels()) {
      int alpha = (pixel >>> 24) & 0xff;
      int red = (pixel >>> 16) & 0xff;
      int green = (pixel >>> 8) & 0xff;
      int blue = pixel & 0xff;
      if (pixel != 0 && alpha == 0) {
        alpha = 0xff;
      }
      buffer.put((byte) red);
      buffer.put((byte) green);
      buffer.put((byte) blue);
      buffer.put((byte) alpha);
    }
    buffer.flip();
    return buffer;
  }
}
