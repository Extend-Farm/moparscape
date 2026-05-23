package com.veyrmoor.client.desktop.world.raster;

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
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;

final class OpenGlSceneShaderPipeline implements AutoCloseable {

  private static final String COMPATIBILITY_VERTEX_SHADER = """
      #version 120
      varying vec4 vertexColor;
      varying vec3 worldPosition;

      void main() {
        gl_Position = ftransform();
        vertexColor = gl_Color;
        worldPosition = gl_Vertex.xyz;
        gl_TexCoord[0] = gl_MultiTexCoord0;
      }
      """;

  private static final String TERRAIN_SHADE_FUNCTIONS = """
      float terrainHash(vec2 point) {
        return fract(sin(dot(point, vec2(127.1, 311.7))) * 43758.5453123);
      }

      float terrainNoise(vec2 point) {
        vec2 cell = floor(point);
        vec2 local = fract(point);
        vec2 blend = local * local * (3.0 - 2.0 * local);
        float southWest = terrainHash(cell);
        float southEast = terrainHash(cell + vec2(1.0, 0.0));
        float northWest = terrainHash(cell + vec2(0.0, 1.0));
        float northEast = terrainHash(cell + vec2(1.0, 1.0));
        return mix(
            mix(southWest, southEast, blend.x),
            mix(northWest, northEast, blend.x),
            blend.y
        );
      }

      float terrainShade(vec3 point, float strength) {
        if (strength <= 0.0) {
          return 1.0;
        }
        float macro = terrainNoise(point.xz * 0.18);
        float micro = terrainNoise(point.xz * 0.85 + vec2(point.y * 0.04, point.y * 0.02));
        float flow = sin(point.x * 0.33 + point.z * 0.21) * 0.5 + 0.5;
        float shade = 0.94;
        shade += (macro - 0.5) * 0.16;
        shade += (micro - 0.5) * 0.08;
        shade += (flow - 0.5) * 0.04;
        shade = clamp(shade, 0.82, 1.08);
        return mix(1.0, shade, strength);
      }
      """;

  private static final String COLOR_FRAGMENT_SHADER = """
      #version 120
      uniform float terrainShadeStrength;
      varying vec4 vertexColor;
      varying vec3 worldPosition;

      """ + TERRAIN_SHADE_FUNCTIONS + """

      void main() {
        vec4 shadedColor = vertexColor;
        shadedColor.rgb *= terrainShade(worldPosition, terrainShadeStrength);
        gl_FragColor = shadedColor;
      }
      """;

  private static final String TEXTURED_FRAGMENT_SHADER = """
      #version 120
      uniform sampler2D sceneTexture;
      uniform float terrainShadeStrength;
      varying vec4 vertexColor;
      varying vec3 worldPosition;

      """ + TERRAIN_SHADE_FUNCTIONS + """

      void main() {
        vec4 texel = texture2D(sceneTexture, gl_TexCoord[0].st);
        if (texel.a <= 0.02) {
          discard;
        }
        vec4 shadedColor = texel * vertexColor;
        shadedColor.rgb *= terrainShade(worldPosition, terrainShadeStrength);
        gl_FragColor = shadedColor;
      }
      """;

  private final int colorProgramId;
  private final int texturedProgramId;
  private final int colorTerrainShadeStrengthLocation;
  private final int texturedTerrainShadeStrengthLocation;
  private final boolean available;

  private OpenGlSceneShaderPipeline(
      int colorProgramId,
      int texturedProgramId,
      int colorTerrainShadeStrengthLocation,
      int texturedTerrainShadeStrengthLocation,
      boolean available
  ) {
    this.colorProgramId = colorProgramId;
    this.texturedProgramId = texturedProgramId;
    this.colorTerrainShadeStrengthLocation = colorTerrainShadeStrengthLocation;
    this.texturedTerrainShadeStrengthLocation = texturedTerrainShadeStrengthLocation;
    this.available = available;
  }

  static OpenGlSceneShaderPipeline create() {
    try {
      int colorProgramId = linkProgram(COMPATIBILITY_VERTEX_SHADER, COLOR_FRAGMENT_SHADER);
      int texturedProgramId = linkProgram(COMPATIBILITY_VERTEX_SHADER, TEXTURED_FRAGMENT_SHADER);
      int colorTerrainShadeStrengthLocation = glGetUniformLocation(colorProgramId, "terrainShadeStrength");
      int texturedTerrainShadeStrengthLocation = glGetUniformLocation(texturedProgramId, "terrainShadeStrength");
      glUseProgram(colorProgramId);
      if (colorTerrainShadeStrengthLocation >= 0) {
        glUniform1f(colorTerrainShadeStrengthLocation, 0.0f);
      }
      glUseProgram(texturedProgramId);
      int samplerLocation = glGetUniformLocation(texturedProgramId, "sceneTexture");
      if (samplerLocation >= 0) {
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(samplerLocation, 0);
      }
      if (texturedTerrainShadeStrengthLocation >= 0) {
        glUniform1f(texturedTerrainShadeStrengthLocation, 0.0f);
      }
      glUseProgram(0);
      return new OpenGlSceneShaderPipeline(
          colorProgramId,
          texturedProgramId,
          colorTerrainShadeStrengthLocation,
          texturedTerrainShadeStrengthLocation,
          true
      );
    } catch (RuntimeException exception) {
      System.err.println("Falling back to fixed-function world rendering: " + exception.getMessage());
      return new OpenGlSceneShaderPipeline(0, 0, -1, -1, false);
    }
  }

  void bindColorProgram(float terrainShadeStrength) {
    if (!available) {
      return;
    }
    glUseProgram(colorProgramId);
    if (colorTerrainShadeStrengthLocation >= 0) {
      glUniform1f(colorTerrainShadeStrengthLocation, terrainShadeStrength);
    }
  }

  void bindTexturedProgram(float terrainShadeStrength) {
    if (!available) {
      return;
    }
    glUseProgram(texturedProgramId);
    if (texturedTerrainShadeStrengthLocation >= 0) {
      glUniform1f(texturedTerrainShadeStrengthLocation, terrainShadeStrength);
    }
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
