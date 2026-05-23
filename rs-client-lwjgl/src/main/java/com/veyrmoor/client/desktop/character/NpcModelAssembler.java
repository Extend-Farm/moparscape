package com.veyrmoor.client.desktop.character;

import com.veyrmoor.cache.RawModelData;
import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.client.desktop.world.object.WorldSceneObjectGeometry;
import com.veyrmoor.client.desktop.world.raster.SceneRasterMode;
import com.veyrmoor.content.NpcDefinition;
import com.veyrmoor.content.NpcDefinitionCatalog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NpcModelAssembler {

  private static final int REFERENCE_LIGHT_AMBIENT = 64;
  private static final int REFERENCE_LIGHT_X = -30;
  private static final int REFERENCE_LIGHT_Y = -50;
  private static final int REFERENCE_LIGHT_Z = -30;

  private final NpcDefinitionCatalog npcDefinitions;
  private final RawModelRepository rawModelRepository;
  private final Map<Integer, WorldSceneObjectGeometry> geometryByNpcId = new HashMap<>();

  public NpcModelAssembler(NpcDefinitionCatalog npcDefinitions, RawModelRepository rawModelRepository) {
    this.npcDefinitions = npcDefinitions;
    this.rawModelRepository = rawModelRepository;
  }

  public WorldSceneObjectGeometry assemble(int npcId) {
    return geometryByNpcId.computeIfAbsent(npcId, this::buildGeometry);
  }

  private WorldSceneObjectGeometry buildGeometry(int npcId) {
    NpcDefinition definition = npcDefinitions.require(npcId);
    List<Integer> modelIds = definition.modelIds();
    if (modelIds.isEmpty()) {
      return null;
    }
    RawModelData[] models = new RawModelData[modelIds.size()];
    int totalVertexCount = 0;
    int totalFaceCount = 0;
    for (int index = 0; index < modelIds.size(); index++) {
      models[index] = rawModelRepository.loadModel(modelIds.get(index));
      totalVertexCount += models[index].vertexCount();
      totalFaceCount += models[index].faceCount();
    }
    if (totalVertexCount == 0 || totalFaceCount == 0) {
      return null;
    }

    float[] vertexX = new float[totalVertexCount];
    float[] vertexY = new float[totalVertexCount];
    float[] vertexZ = new float[totalVertexCount];
    int[] faceVertexA = new int[totalFaceCount];
    int[] faceVertexB = new int[totalFaceCount];
    int[] faceVertexC = new int[totalFaceCount];
    int[] faceColorA = new int[totalFaceCount];
    int[] faceColorB = new int[totalFaceCount];
    int[] faceColorC = new int[totalFaceCount];
    int[] faceAlpha = new int[totalFaceCount];
    SceneRasterMode[] faceRasterModes = new SceneRasterMode[totalFaceCount];
    int[] faceTextureIds = new int[totalFaceCount];
    int[] textureVertexA = new int[totalFaceCount];
    int[] textureVertexB = new int[totalFaceCount];
    int[] textureVertexC = new int[totalFaceCount];
    int[] facePriorities = new int[totalFaceCount];

    int vertexOffset = 0;
    int faceOffset = 0;
    for (RawModelData model : models) {
      float[][] transformedVertices = transformVertices(model, definition);
      float[][] faceNormals = computeFaceNormals(model, transformedVertices);
      float[][] vertexNormals = computeVertexNormals(model, faceNormals);

      System.arraycopy(transformedVertices[0], 0, vertexX, vertexOffset, model.vertexCount());
      System.arraycopy(transformedVertices[1], 0, vertexY, vertexOffset, model.vertexCount());
      System.arraycopy(transformedVertices[2], 0, vertexZ, vertexOffset, model.vertexCount());

      for (int faceIndex = 0; faceIndex < model.faceCount(); faceIndex++) {
        int targetFaceIndex = faceOffset + faceIndex;
        int rawFaceType = faceTypeAt(model, faceIndex);
        int faceMode = rawFaceType & 3;
        int sourceColorHsl = recolor(model.faceColorHsl()[faceIndex], definition);
        int sourceVertexA = model.faceVertexA()[faceIndex];
        int sourceVertexB = model.faceVertexB()[faceIndex];
        int sourceVertexC = model.faceVertexC()[faceIndex];

        faceVertexA[targetFaceIndex] = vertexOffset + sourceVertexA;
        faceVertexB[targetFaceIndex] = vertexOffset + sourceVertexB;
        faceVertexC[targetFaceIndex] = vertexOffset + sourceVertexC;

        if ((rawFaceType & 1) == 0) {
          faceColorA[targetFaceIndex] = lightFaceVertex(
              definition,
              sourceColorHsl,
              rawFaceType,
              vertexNormals[0][sourceVertexA],
              vertexNormals[1][sourceVertexA],
              vertexNormals[2][sourceVertexA]
          );
          faceColorB[targetFaceIndex] = lightFaceVertex(
              definition,
              sourceColorHsl,
              rawFaceType,
              vertexNormals[0][sourceVertexB],
              vertexNormals[1][sourceVertexB],
              vertexNormals[2][sourceVertexB]
          );
          faceColorC[targetFaceIndex] = lightFaceVertex(
              definition,
              sourceColorHsl,
              rawFaceType,
              vertexNormals[0][sourceVertexC],
              vertexNormals[1][sourceVertexC],
              vertexNormals[2][sourceVertexC]
          );
        } else {
          int shadedFaceColor = lightFlatFace(
              definition,
              sourceColorHsl,
              rawFaceType,
              faceNormals[0][faceIndex],
              faceNormals[1][faceIndex],
              faceNormals[2][faceIndex]
          );
          faceColorA[targetFaceIndex] = shadedFaceColor;
          faceColorB[targetFaceIndex] = shadedFaceColor;
          faceColorC[targetFaceIndex] = shadedFaceColor;
        }

        faceAlpha[targetFaceIndex] = 255 - Math.max(0, Math.min(255, faceAlphaAt(model, faceIndex)));
        faceRasterModes[targetFaceIndex] = switch (faceMode) {
          case 1 -> SceneRasterMode.FLAT;
          case 2, 3 -> SceneRasterMode.TEXTURED;
          default -> SceneRasterMode.GOURAUD;
        };
        if (faceMode >= 2) {
          int textureIndex = rawFaceType >> 2;
          faceTextureIds[targetFaceIndex] = sourceColorHsl;
          textureVertexA[targetFaceIndex] = textureVertexAt(model.texturedFaceVertexA(), textureIndex, vertexOffset);
          textureVertexB[targetFaceIndex] = textureVertexAt(model.texturedFaceVertexB(), textureIndex, vertexOffset);
          textureVertexC[targetFaceIndex] = textureVertexAt(model.texturedFaceVertexC(), textureIndex, vertexOffset);
        } else {
          faceTextureIds[targetFaceIndex] = -1;
          textureVertexA[targetFaceIndex] = -1;
          textureVertexB[targetFaceIndex] = -1;
          textureVertexC[targetFaceIndex] = -1;
        }
        facePriorities[targetFaceIndex] = facePriorityAt(model, faceIndex);
      }

      vertexOffset += model.vertexCount();
      faceOffset += model.faceCount();
    }

    return new WorldSceneObjectGeometry(
        vertexX,
        vertexY,
        vertexZ,
        faceVertexA,
        faceVertexB,
        faceVertexC,
        faceColorA,
        faceColorB,
        faceColorC,
        faceAlpha,
        faceRasterModes,
        faceTextureIds,
        textureVertexA,
        textureVertexB,
        textureVertexC,
        facePriorities
    );
  }

  private float[][] transformVertices(RawModelData rawModelData, NpcDefinition definition) {
    int vertexCount = rawModelData.vertexCount();
    float[] worldX = new float[vertexCount];
    float[] worldY = new float[vertexCount];
    float[] worldZ = new float[vertexCount];
    float horizontalScale = definition.scaleXY() / 128.0f;
    float verticalScale = definition.scaleZ() / 128.0f;
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      worldX[vertexIndex] = rawModelData.vertexX()[vertexIndex] / 128.0f * horizontalScale;
      worldY[vertexIndex] = -(rawModelData.vertexY()[vertexIndex] / 128.0f * verticalScale);
      worldZ[vertexIndex] = rawModelData.vertexZ()[vertexIndex] / 128.0f * horizontalScale;
    }
    return new float[][]{worldX, worldY, worldZ};
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

  private float[][] computeVertexNormals(RawModelData rawModelData, float[][] faceNormals) {
    float[] vertexNormalX = new float[rawModelData.vertexCount()];
    float[] vertexNormalY = new float[rawModelData.vertexCount()];
    float[] vertexNormalZ = new float[rawModelData.vertexCount()];
    int[] contributions = new int[rawModelData.vertexCount()];
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      if ((faceTypeAt(rawModelData, faceIndex) & 1) != 0) {
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

  private int lightFaceVertex(
      NpcDefinition definition,
      int faceColorHsl,
      int rawFaceType,
      float normalX,
      float normalY,
      float normalZ
  ) {
    int lightValue = referenceLightValue(definition, normalX, normalY, normalZ);
    if ((rawFaceType & 2) == 2) {
      return applyBrightness(0xffffff, clamp(lightValue, 2, 126));
    }
    return CharacterColorPalette.rgb(referenceLight(faceColorHsl, lightValue, rawFaceType));
  }

  private int lightFlatFace(
      NpcDefinition definition,
      int faceColorHsl,
      int rawFaceType,
      float normalX,
      float normalY,
      float normalZ
  ) {
    int lightValue = referenceFlatLightValue(definition, normalX, normalY, normalZ);
    if ((rawFaceType & 2) == 2) {
      return applyBrightness(0xffffff, clamp(lightValue, 2, 126));
    }
    return CharacterColorPalette.rgb(referenceLight(faceColorHsl, lightValue, rawFaceType));
  }

  private int referenceLightValue(NpcDefinition definition, float normalX, float normalY, float normalZ) {
    return 64 + definition.ambient()
        + Math.round(
        (REFERENCE_LIGHT_X * normalX + REFERENCE_LIGHT_Y * normalY + REFERENCE_LIGHT_Z * normalZ)
            / Math.max(0.0001f, referenceSpecular(definition))
    );
  }

  private int referenceFlatLightValue(NpcDefinition definition, float normalX, float normalY, float normalZ) {
    float denominator = referenceSpecular(definition) * 1.5f;
    return 64 + definition.ambient()
        + Math.round(
        (REFERENCE_LIGHT_X * normalX + REFERENCE_LIGHT_Y * normalY + REFERENCE_LIGHT_Z * normalZ)
            / Math.max(0.0001f, denominator)
    );
  }

  private float referenceSpecular(NpcDefinition definition) {
    double lightMagnitude = Math.sqrt(
        REFERENCE_LIGHT_X * (double) REFERENCE_LIGHT_X
            + REFERENCE_LIGHT_Y * (double) REFERENCE_LIGHT_Y
            + REFERENCE_LIGHT_Z * (double) REFERENCE_LIGHT_Z
    );
    return (float) ((lightMagnitude * (850 + definition.contrast())) / 256.0);
  }

  private int referenceLight(int faceColorHsl, int light, int rawFaceType) {
    if ((rawFaceType & 2) == 2) {
      return clamp(light, 2, 126);
    }
    int lit = light * (faceColorHsl & 127) >> 7;
    return (faceColorHsl & 0xff80) + clamp(lit, 2, 126);
  }

  private int recolor(int colorHsl, NpcDefinition definition) {
    for (int index = 0; index < definition.recolorSources().size(); index++) {
      if (definition.recolorSources().get(index) == colorHsl && index < definition.recolorTargets().size()) {
        return definition.recolorTargets().get(index);
      }
    }
    return colorHsl;
  }

  private static int faceTypeAt(RawModelData rawModelData, int faceIndex) {
    return faceIndex < rawModelData.faceRenderTypes().length ? rawModelData.faceRenderTypes()[faceIndex] : 0;
  }

  private static int facePriorityAt(RawModelData rawModelData, int faceIndex) {
    return faceIndex < rawModelData.facePriorities().length ? rawModelData.facePriorities()[faceIndex] : 0;
  }

  private static int faceAlphaAt(RawModelData rawModelData, int faceIndex) {
    return faceIndex < rawModelData.faceAlpha().length ? rawModelData.faceAlpha()[faceIndex] : 0;
  }

  private static int textureVertexAt(int[] textureVertices, int textureIndex, int vertexOffset) {
    return textureIndex >= 0 && textureIndex < textureVertices.length
        ? vertexOffset + textureVertices[textureIndex]
        : -1;
  }

  private static int applyBrightness(int rgb, int brightness) {
    int red = ((rgb >>> 16) & 0xff) * brightness / 128;
    int green = ((rgb >>> 8) & 0xff) * brightness / 128;
    int blue = (rgb & 0xff) * brightness / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
