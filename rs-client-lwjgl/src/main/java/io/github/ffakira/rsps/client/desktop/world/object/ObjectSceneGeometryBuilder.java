package io.github.ffakira.rsps.client.desktop.world.object;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.ObjectDefinition;
import java.util.ArrayList;
import java.util.List;

final class ObjectSceneGeometryBuilder {

  private static final float OBJECT_LIGHT_X = -0.35f;
  private static final float OBJECT_LIGHT_Y = 0.82f;
  private static final float OBJECT_LIGHT_Z = -0.44f;

  private final RawModelRepository rawModelRepository;

  // This builder owns the native object-model transform and shading contract so cache scene
  // loading stays focused on region assembly instead of also becoming the model/raster metadata
  // god class for static world objects.
  ObjectSceneGeometryBuilder(RawModelRepository rawModelRepository) {
    this.rawModelRepository = rawModelRepository;
  }

  WorldSceneObjectGeometry build(
      ObjectDefinition definition,
      int orientation,
      List<Integer> modelIds
  ) {
    if (modelIds.isEmpty()) {
      return null;
    }

    ArrayList<float[]> vertices = new ArrayList<>();
    ArrayList<int[]> faces = new ArrayList<>();
    ArrayList<Integer> faceColorA = new ArrayList<>();
    ArrayList<Integer> faceColorB = new ArrayList<>();
    ArrayList<Integer> faceColorC = new ArrayList<>();
    ArrayList<Integer> faceAlpha = new ArrayList<>();
    ArrayList<SceneRasterMode> faceRasterModes = new ArrayList<>();
    ArrayList<Integer> faceTextureIds = new ArrayList<>();
    ArrayList<Integer> textureVertexA = new ArrayList<>();
    ArrayList<Integer> textureVertexB = new ArrayList<>();
    ArrayList<Integer> textureVertexC = new ArrayList<>();
    int vertexOffset = 0;
    for (Integer modelId : modelIds) {
      RawModelData rawModelData = rawModelRepository.loadModel(modelId);
      float[][] transformedVertices = transformVertices(rawModelData, definition, orientation);
      float[][] vertexNormals = computeVertexNormals(rawModelData, transformedVertices);
      float[][] faceNormals = computeFaceNormals(rawModelData, transformedVertices);
      for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
        vertices.add(new float[]{
            transformedVertices[0][vertexIndex],
            transformedVertices[1][vertexIndex],
            transformedVertices[2][vertexIndex]
        });
      }
      for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
        int rawFaceType = rawModelData.faceRenderTypes()[faceIndex];
        int faceMode = rawFaceType & 3;
        SceneRasterMode rasterMode = switch (faceMode) {
          case 0 -> SceneRasterMode.GOURAUD;
          case 1 -> SceneRasterMode.FLAT;
          case 2, 3 -> SceneRasterMode.TEXTURED;
          default -> SceneRasterMode.FLAT;
        };
        int sourceColorHsl = rawModelData.faceColorHsl()[faceIndex];
        faces.add(new int[]{
            rawModelData.faceVertexA()[faceIndex] + vertexOffset,
            rawModelData.faceVertexB()[faceIndex] + vertexOffset,
            rawModelData.faceVertexC()[faceIndex] + vertexOffset
        });
        if (faceMode == 0 || faceMode == 2) {
          faceColorA.add(colorForFaceVertex(
              faceMode,
              sourceColorHsl,
              definition,
              vertexNormals[0][rawModelData.faceVertexA()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexA()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexA()[faceIndex]]
          ));
          faceColorB.add(colorForFaceVertex(
              faceMode,
              sourceColorHsl,
              definition,
              vertexNormals[0][rawModelData.faceVertexB()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexB()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexB()[faceIndex]]
          ));
          faceColorC.add(colorForFaceVertex(
              faceMode,
              sourceColorHsl,
              definition,
              vertexNormals[0][rawModelData.faceVertexC()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexC()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexC()[faceIndex]]
          ));
        } else {
          int shadedFaceColor = colorForFaceVertex(
              faceMode,
              sourceColorHsl,
              definition,
              faceNormals[0][faceIndex],
              faceNormals[1][faceIndex],
              faceNormals[2][faceIndex]
          );
          faceColorA.add(shadedFaceColor);
          faceColorB.add(shadedFaceColor);
          faceColorC.add(shadedFaceColor);
        }
        faceAlpha.add(255 - Math.max(0, Math.min(255, rawModelData.faceAlpha()[faceIndex])));
        faceRasterModes.add(rasterMode);
        if (faceMode >= 2) {
          int textureIndex = rawFaceType >> 2;
          faceTextureIds.add(sourceColorHsl);
          if (textureIndex >= 0 && textureIndex < rawModelData.texturedFaceCount()) {
            textureVertexA.add(rawModelData.texturedFaceVertexA()[textureIndex] + vertexOffset);
            textureVertexB.add(rawModelData.texturedFaceVertexB()[textureIndex] + vertexOffset);
            textureVertexC.add(rawModelData.texturedFaceVertexC()[textureIndex] + vertexOffset);
          } else {
            textureVertexA.add(-1);
            textureVertexB.add(-1);
            textureVertexC.add(-1);
          }
        } else {
          faceTextureIds.add(-1);
          textureVertexA.add(-1);
          textureVertexB.add(-1);
          textureVertexC.add(-1);
        }
      }
      vertexOffset += rawModelData.vertexCount();
    }

    return finalizeGeometry(
        vertices,
        faces,
        faceColorA,
        faceColorB,
        faceColorC,
        faceAlpha,
        faceRasterModes,
        faceTextureIds,
        textureVertexA,
        textureVertexB,
        textureVertexC
    );
  }

  boolean hasRenderableSourceModels(List<Integer> modelIds) {
    for (Integer modelId : modelIds) {
      RawModelData rawModelData = rawModelRepository.loadModel(modelId);
      if (rawModelData.vertexCount() > 0 || rawModelData.faceCount() > 0 || rawModelData.texturedFaceCount() > 0) {
        return true;
      }
    }
    return false;
  }

  private WorldSceneObjectGeometry finalizeGeometry(
      List<float[]> vertices,
      List<int[]> faces,
      List<Integer> faceColorA,
      List<Integer> faceColorB,
      List<Integer> faceColorC,
      List<Integer> faceAlpha,
      List<SceneRasterMode> faceRasterModes,
      List<Integer> faceTextureIds,
      List<Integer> textureVertexA,
      List<Integer> textureVertexB,
      List<Integer> textureVertexC
  ) {
    if (vertices.isEmpty() || faces.isEmpty()) {
      return null;
    }
    float[] vertexX = new float[vertices.size()];
    float[] vertexY = new float[vertices.size()];
    float[] vertexZ = new float[vertices.size()];
    for (int index = 0; index < vertices.size(); index++) {
      float[] vertex = vertices.get(index);
      vertexX[index] = vertex[0];
      vertexY[index] = vertex[1];
      vertexZ[index] = vertex[2];
    }
    int[] faceVertexA = new int[faces.size()];
    int[] faceVertexB = new int[faces.size()];
    int[] faceVertexC = new int[faces.size()];
    int[] meshFaceColorA = new int[faces.size()];
    int[] meshFaceColorB = new int[faces.size()];
    int[] meshFaceColorC = new int[faces.size()];
    int[] meshFaceAlpha = new int[faces.size()];
    SceneRasterMode[] meshFaceRasterModes = new SceneRasterMode[faces.size()];
    int[] meshFaceTextureIds = new int[faces.size()];
    int[] meshTextureVertexA = new int[faces.size()];
    int[] meshTextureVertexB = new int[faces.size()];
    int[] meshTextureVertexC = new int[faces.size()];
    for (int index = 0; index < faces.size(); index++) {
      int[] face = faces.get(index);
      faceVertexA[index] = face[0];
      faceVertexB[index] = face[1];
      faceVertexC[index] = face[2];
      meshFaceColorA[index] = faceColorA.get(index);
      meshFaceColorB[index] = faceColorB.get(index);
      meshFaceColorC[index] = faceColorC.get(index);
      meshFaceAlpha[index] = faceAlpha.get(index);
      meshFaceRasterModes[index] = faceRasterModes.get(index);
      meshFaceTextureIds[index] = faceTextureIds.get(index);
      meshTextureVertexA[index] = textureVertexA.get(index);
      meshTextureVertexB[index] = textureVertexB.get(index);
      meshTextureVertexC[index] = textureVertexC.get(index);
    }
    return new WorldSceneObjectGeometry(
        vertexX,
        vertexY,
        vertexZ,
        faceVertexA,
        faceVertexB,
        faceVertexC,
        meshFaceColorA,
        meshFaceColorB,
        meshFaceColorC,
        meshFaceAlpha,
        meshFaceRasterModes,
        meshFaceTextureIds,
        meshTextureVertexA,
        meshTextureVertexB,
        meshTextureVertexC
    );
  }

  static float[][] transformVertices(
      RawModelData rawModelData,
      ObjectDefinition definition,
      int orientation
  ) {
    int vertexCount = rawModelData.vertexCount();
    float[] worldX = new float[vertexCount];
    float[] worldY = new float[vertexCount];
    float[] worldZ = new float[vertexCount];
    float scaleX = definition.scaleX() / 128.0f;
    float scaleY = definition.scaleY() / 128.0f;
    float scaleZ = definition.scaleZ() / 128.0f;
    float translateX = definition.translateX() / 128.0f;
    float translateY = definition.translateY() / 128.0f;
    float translateZ = definition.translateZ() / 128.0f;

    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      float x = rawModelData.vertexX()[vertexIndex] / 128.0f * scaleX + translateX;
      // Cache model Y grows downward. Static objects must use the same world-up convention as the
      // actor assembler or tall props like fountains and pillars appear vertically flipped.
      float y = -(rawModelData.vertexY()[vertexIndex] / 128.0f * scaleY + translateY);
      float z = rawModelData.vertexZ()[vertexIndex] / 128.0f * scaleZ + translateZ;
      if (definition.mirrored()) {
        x = -x;
      }
      float rotatedX = x;
      float rotatedZ = z;
      for (int rotation = 0; rotation < (orientation & 3); rotation++) {
        float swap = rotatedX;
        rotatedX = rotatedZ;
        rotatedZ = -swap;
      }
      worldX[vertexIndex] = rotatedX;
      worldY[vertexIndex] = y;
      worldZ[vertexIndex] = rotatedZ;
    }
    return new float[][]{worldX, worldY, worldZ};
  }

  private int recolor(int colorHsl, ObjectDefinition definition) {
    for (int index = 0; index < definition.recolorSources().size(); index++) {
      if (definition.recolorSources().get(index) == colorHsl && index < definition.recolorTargets().size()) {
        return definition.recolorTargets().get(index);
      }
    }
    return colorHsl;
  }

  private float[][] computeFaceNormals(RawModelData rawModelData, float[][] transformedVertices) {
    float[] normalX = new float[rawModelData.faceCount()];
    float[] normalY = new float[rawModelData.faceCount()];
    float[] normalZ = new float[rawModelData.faceCount()];
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      float edgeOneX = transformedVertices[0][vertexB] - transformedVertices[0][vertexA];
      float edgeOneY = transformedVertices[1][vertexB] - transformedVertices[1][vertexA];
      float edgeOneZ = transformedVertices[2][vertexB] - transformedVertices[2][vertexA];
      float edgeTwoX = transformedVertices[0][vertexC] - transformedVertices[0][vertexA];
      float edgeTwoY = transformedVertices[1][vertexC] - transformedVertices[1][vertexA];
      float edgeTwoZ = transformedVertices[2][vertexC] - transformedVertices[2][vertexA];
      float crossX = edgeOneY * edgeTwoZ - edgeOneZ * edgeTwoY;
      float crossY = edgeOneZ * edgeTwoX - edgeOneX * edgeTwoZ;
      float crossZ = edgeOneX * edgeTwoY - edgeOneY * edgeTwoX;
      float magnitude = (float) Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
      if (magnitude <= 0.0001f) {
        magnitude = 1.0f;
      }
      normalX[faceIndex] = crossX / magnitude;
      normalY[faceIndex] = crossY / magnitude;
      normalZ[faceIndex] = crossZ / magnitude;
    }
    return new float[][]{normalX, normalY, normalZ};
  }

  private float[][] computeVertexNormals(RawModelData rawModelData, float[][] transformedVertices) {
    float[][] faceNormals = computeFaceNormals(rawModelData, transformedVertices);
    float[] vertexNormalX = new float[rawModelData.vertexCount()];
    float[] vertexNormalY = new float[rawModelData.vertexCount()];
    float[] vertexNormalZ = new float[rawModelData.vertexCount()];
    int[] contributions = new int[rawModelData.vertexCount()];
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      if ((rawModelData.faceRenderTypes()[faceIndex] & 1) != 0) {
        continue;
      }
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      float normalX = faceNormals[0][faceIndex];
      float normalY = faceNormals[1][faceIndex];
      float normalZ = faceNormals[2][faceIndex];
      vertexNormalX[vertexA] += normalX;
      vertexNormalY[vertexA] += normalY;
      vertexNormalZ[vertexA] += normalZ;
      contributions[vertexA]++;
      vertexNormalX[vertexB] += normalX;
      vertexNormalY[vertexB] += normalY;
      vertexNormalZ[vertexB] += normalZ;
      contributions[vertexB]++;
      vertexNormalX[vertexC] += normalX;
      vertexNormalY[vertexC] += normalY;
      vertexNormalZ[vertexC] += normalZ;
      contributions[vertexC]++;
    }
    for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
      if (contributions[vertexIndex] == 0) {
        vertexNormalX[vertexIndex] = 0.0f;
        vertexNormalY[vertexIndex] = 1.0f;
        vertexNormalZ[vertexIndex] = 0.0f;
        continue;
      }
      float magnitude = (float) Math.sqrt(
          vertexNormalX[vertexIndex] * vertexNormalX[vertexIndex]
              + vertexNormalY[vertexIndex] * vertexNormalY[vertexIndex]
              + vertexNormalZ[vertexIndex] * vertexNormalZ[vertexIndex]
      );
      if (magnitude <= 0.0001f) {
        magnitude = 1.0f;
      }
      vertexNormalX[vertexIndex] /= magnitude;
      vertexNormalY[vertexIndex] /= magnitude;
      vertexNormalZ[vertexIndex] /= magnitude;
    }
    return new float[][]{vertexNormalX, vertexNormalY, vertexNormalZ};
  }

  private int shadeObjectColor(ObjectDefinition definition, int baseRgb, float normalX, float normalY, float normalZ) {
    return applyBrightness(baseRgb, textureBrightness(definition, normalX, normalY, normalZ));
  }

  private int colorForFaceVertex(
      int faceMode,
      int sourceColorHsl,
      ObjectDefinition definition,
      float normalX,
      float normalY,
      float normalZ
  ) {
    if (faceMode >= 2) {
      return shadeObjectTexture(definition, normalX, normalY, normalZ);
    }
    int recoloredHsl = recolor(sourceColorHsl, definition);
    return shadeObjectColor(definition, hslToRgb(recoloredHsl), normalX, normalY, normalZ);
  }

  private int shadeObjectTexture(ObjectDefinition definition, float normalX, float normalY, float normalZ) {
    int brightness = textureBrightness(definition, normalX, normalY, normalZ);
    return (brightness << 16) | (brightness << 8) | brightness;
  }

  private int textureBrightness(ObjectDefinition definition, float normalX, float normalY, float normalZ) {
    float lightDot = normalX * OBJECT_LIGHT_X + normalY * OBJECT_LIGHT_Y + normalZ * OBJECT_LIGHT_Z;
    float amplitude = Math.max(28.0f, Math.min(92.0f, 74.0f + definition.contrast() * 0.55f));
    float base = 122.0f + definition.ambient();
    int minimumBrightness = 68;
    if (isFoliageObject(definition)) {
      // The 317 tree and bush canopies are textured foliage, not flat dark solids. Using the same
      // modulation range as hard-surface objects drives texture `8` close to black, so keep a
      // brighter floor and softer directional contrast for foliage.
      base += 28.0f;
      amplitude *= 0.52f;
      minimumBrightness = 112;
    }
    return clamp(Math.round(base + lightDot * amplitude), minimumBrightness, 228);
  }

  private boolean isFoliageObject(ObjectDefinition definition) {
    String lowercaseName = definition.name().toLowerCase();
    return lowercaseName.contains("tree")
        || lowercaseName.contains("bush")
        || lowercaseName.contains("hedge")
        || lowercaseName.contains("evergreen");
  }

  private int hslToRgb(int colorHsl) {
    float hue = ((colorHsl >> 10) & 0x3f) / 63.0f;
    float saturation = ((colorHsl >> 7) & 0x07) / 7.0f;
    float lightness = (colorHsl & 0x7f) / 127.0f;
    float q = lightness < 0.5f
        ? lightness * (1.0f + saturation)
        : lightness + saturation - lightness * saturation;
    float p = 2.0f * lightness - q;
    float red = hueToChannel(p, q, hue + 1.0f / 3.0f);
    float green = hueToChannel(p, q, hue);
    float blue = hueToChannel(p, q, hue - 1.0f / 3.0f);
    return (clamp(Math.round(red * 255.0f), 0, 255) << 16)
        | (clamp(Math.round(green * 255.0f), 0, 255) << 8)
        | clamp(Math.round(blue * 255.0f), 0, 255);
  }

  private float hueToChannel(float p, float q, float value) {
    float wrapped = value;
    if (wrapped < 0.0f) {
      wrapped += 1.0f;
    }
    if (wrapped > 1.0f) {
      wrapped -= 1.0f;
    }
    if (wrapped < 1.0f / 6.0f) {
      return p + (q - p) * 6.0f * wrapped;
    }
    if (wrapped < 0.5f) {
      return q;
    }
    if (wrapped < 2.0f / 3.0f) {
      return p + (q - p) * (2.0f / 3.0f - wrapped) * 6.0f;
    }
    return p;
  }

  private int applyBrightness(int rgb, int brightness) {
    int red = ((rgb >>> 16) & 0xff) * brightness / 128;
    int green = ((rgb >>> 8) & 0xff) * brightness / 128;
    int blue = (rgb & 0xff) * brightness / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
