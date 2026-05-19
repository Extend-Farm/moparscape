package io.github.ffakira.rsps.client.desktop.world.raster;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;

final class OpenGlSceneShaderPipeline implements AutoCloseable {

  private static final String COMPATIBILITY_VERTEX_SHADER = """
      #version 120
      varying vec4 vertexColor;

      void main() {
        gl_Position = ftransform();
        vertexColor = gl_Color;
        gl_TexCoord[0] = gl_MultiTexCoord0;
      }
      """;

  private static final String COLOR_FRAGMENT_SHADER = """
      #version 120
      varying vec4 vertexColor;

      void main() {
        gl_FragColor = vertexColor;
      }
      """;

  private static final String TEXTURED_FRAGMENT_SHADER = """
      #version 120
      uniform sampler2D sceneTexture;
      varying vec4 vertexColor;

      void main() {
        vec4 texel = texture2D(sceneTexture, gl_TexCoord[0].st);
        if (texel.a <= 0.02) {
          discard;
        }
        gl_FragColor = texel * vertexColor;
      }
      """;

  private final int colorProgramId;
  private final int texturedProgramId;
  private final boolean available;

  private OpenGlSceneShaderPipeline(int colorProgramId, int texturedProgramId, boolean available) {
    this.colorProgramId = colorProgramId;
    this.texturedProgramId = texturedProgramId;
    this.available = available;
  }

  static OpenGlSceneShaderPipeline create() {
    try {
      int colorProgramId = linkProgram(COMPATIBILITY_VERTEX_SHADER, COLOR_FRAGMENT_SHADER);
      int texturedProgramId = linkProgram(COMPATIBILITY_VERTEX_SHADER, TEXTURED_FRAGMENT_SHADER);
      glUseProgram(texturedProgramId);
      int samplerLocation = glGetUniformLocation(texturedProgramId, "sceneTexture");
      if (samplerLocation >= 0) {
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(samplerLocation, 0);
      }
      glUseProgram(0);
      return new OpenGlSceneShaderPipeline(colorProgramId, texturedProgramId, true);
    } catch (RuntimeException exception) {
      System.err.println("Falling back to fixed-function world rendering: " + exception.getMessage());
      return new OpenGlSceneShaderPipeline(0, 0, false);
    }
  }

  void bindColorProgram() {
    if (!available) {
      return;
    }
    glUseProgram(colorProgramId);
  }

  void bindTexturedProgram() {
    if (!available) {
      return;
    }
    glUseProgram(texturedProgramId);
  }

  void unbind() {
    if (!available) {
      return;
    }
    glUseProgram(0);
  }

  @Override
  public void close() {
    if (!available) {
      return;
    }
    glDeleteProgram(colorProgramId);
    glDeleteProgram(texturedProgramId);
  }

  private static int linkProgram(String vertexSource, String fragmentSource) {
    int vertexShaderId = compileShader(GL_VERTEX_SHADER, vertexSource);
    int fragmentShaderId = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
    int programId = glCreateProgram();
    glAttachShader(programId, vertexShaderId);
    glAttachShader(programId, fragmentShaderId);
    glLinkProgram(programId);
    if (glGetProgrami(programId, GL_LINK_STATUS) != GL_TRUE) {
      String infoLog = glGetProgramInfoLog(programId).strip();
      glDeleteProgram(programId);
      throw new IllegalStateException("shader link failed: " + infoLog);
    }
    glDetachShader(programId, vertexShaderId);
    glDetachShader(programId, fragmentShaderId);
    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
    return programId;
  }

  private static int compileShader(int shaderType, String source) {
    int shaderId = glCreateShader(shaderType);
    glShaderSource(shaderId, source);
    glCompileShader(shaderId);
    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) != GL_TRUE) {
      String infoLog = glGetShaderInfoLog(shaderId).strip();
      glDeleteShader(shaderId);
      throw new IllegalStateException("shader compile failed: " + infoLog);
    }
    return shaderId;
  }
}
