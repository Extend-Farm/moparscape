package com.veyrmoor.client.desktop.character;

import com.veyrmoor.cache.RawModelData;
import com.veyrmoor.client.desktop.world.raster.SceneRasterMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class CharacterLightingResolver {

  private static final int REFERENCE_LIGHT_AMBIENT = 64;
  private static final int REFERENCE_LIGHT_CONTRAST = 850;
  private static final int REFERENCE_LIGHT_X = -30;
  private static final int REFERENCE_LIGHT_Y = -50;
  private static final int REFERENCE_LIGHT_Z = -30;

  private static final int[][] APPEARANCE_COLOR_PALETTES = {
      {6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193},
      {8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239},
      {25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003},
      {4626, 11146, 6439, 12, 4758, 10270},
      {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}
  };
  private static final int[] SECONDARY_TORSO_PALETTE = {
      9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486
  };

  List<PreparedContributionLighting> prepareMergedLighting(
      List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
      int[] lookValues
  ) {
    MergedLightingModel mergedLightingModel = mergeLightingModel(preparedContributions);
    ReferenceLightingNormals normals = calculateReferenceLightingNormals(
        mergedLightingModel.vertexCount(),
        mergedLightingModel.faceVertexA(),
        mergedLightingModel.faceVertexB(),
        mergedLightingModel.faceVertexC(),
        mergedLightingModel.faceRenderTypes(),
        mergedLightingModel.modelX(),
        mergedLightingModel.modelY(),
        mergedLightingModel.modelZ()
    );
    ArrayList<PreparedContributionLighting> preparedLighting = new ArrayList<>(preparedContributions.size());
    int mergedFaceOffset = 0;
    for (int contributionIndex = 0; contributionIndex < preparedContributions.size(); contributionIndex++) {
      CharacterModelSourceBuilder.PreparedContribution preparedContribution = preparedContributions.get(contributionIndex);
      preparedLighting.add(
          prepareContributionLighting(
              preparedContribution,
              lookValues,
              normals,
              mergedLightingModel.vertexIndexByContribution().get(contributionIndex),
              mergedFaceOffset
          )
      );
      mergedFaceOffset += preparedContribution.contribution().rawModelData().faceCount();
    }
    return List.copyOf(preparedLighting);
  }

  private PreparedContributionLighting prepareContributionLighting(
      CharacterModelSourceBuilder.PreparedContribution preparedContribution,
      int[] lookValues,
      ReferenceLightingNormals normals,
      int[] mergedVertexIndexByLocalVertex,
      int mergedFaceOffset
  ) {
    RawModelData rawModelData = preparedContribution.contribution().rawModelData();
    int faceCount = rawModelData.faceCount();
    int[] faceColorA = new int[faceCount];
    int[] faceColorB = new int[faceCount];
    int[] faceColorC = new int[faceCount];
    SceneRasterMode[] faceRasterModes = new SceneRasterMode[faceCount];
    int[] faceTextureIds = new int[faceCount];
    int[] textureVertexA = new int[faceCount];
    int[] textureVertexB = new int[faceCount];
    int[] textureVertexC = new int[faceCount];
    for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
      int rawFaceType = rawModelData.faceRenderTypes()[faceIndex];
      int faceMode = rawFaceType & 3;
      int mergedFaceIndex = mergedFaceOffset + faceIndex;
      int mergedVertexA = mergedVertexIndexByLocalVertex[rawModelData.faceVertexA()[faceIndex]];
      int mergedVertexB = mergedVertexIndexByLocalVertex[rawModelData.faceVertexB()[faceIndex]];
      int mergedVertexC = mergedVertexIndexByLocalVertex[rawModelData.faceVertexC()[faceIndex]];
      faceRasterModes[faceIndex] = rasterModeForFace(rawFaceType);
      if (faceMode >= 2) {
        populateTexturedFaceLighting(
            faceIndex,
            rawFaceType,
            mergedFaceIndex,
            mergedVertexA,
            mergedVertexB,
            mergedVertexC,
            rawModelData,
            normals,
            faceColorA,
            faceColorB,
            faceColorC,
            faceTextureIds,
            textureVertexA,
            textureVertexB,
            textureVertexC
        );
        continue;
      }
      populateUntexturedFaceLighting(
          faceIndex,
          rawFaceType,
          mergedFaceIndex,
          mergedVertexA,
          mergedVertexB,
          mergedVertexC,
          rawModelData,
          preparedContribution.contribution(),
          normals,
          lookValues,
          faceColorA,
          faceColorB,
          faceColorC,
          faceTextureIds,
          textureVertexA,
          textureVertexB,
          textureVertexC
      );
    }
    return new PreparedContributionLighting(
        faceColorA,
        faceColorB,
        faceColorC,
        faceRasterModes,
        faceTextureIds,
        textureVertexA,
        textureVertexB,
        textureVertexC
    );
  }

  private void populateTexturedFaceLighting(
      int faceIndex,
      int rawFaceType,
      int mergedFaceIndex,
      int mergedVertexA,
      int mergedVertexB,
      int mergedVertexC,
      RawModelData rawModelData,
      ReferenceLightingNormals normals,
      int[] faceColorA,
      int[] faceColorB,
      int[] faceColorC,
      int[] faceTextureIds,
      int[] textureVertexA,
      int[] textureVertexB,
      int[] textureVertexC
  ) {
    faceTextureIds[faceIndex] = rawModelData.faceColorHsl()[faceIndex];
    int textureIndex = rawFaceType >> 2;
    if (textureIndex >= 0 && textureIndex < rawModelData.texturedFaceCount()) {
      textureVertexA[faceIndex] = rawModelData.texturedFaceVertexA()[textureIndex];
      textureVertexB[faceIndex] = rawModelData.texturedFaceVertexB()[textureIndex];
      textureVertexC[faceIndex] = rawModelData.texturedFaceVertexC()[textureIndex];
    } else {
      textureVertexA[faceIndex] = -1;
      textureVertexB[faceIndex] = -1;
      textureVertexC[faceIndex] = -1;
    }
    if ((rawFaceType & 1) == 0) {
      faceColorA[faceIndex] = referenceTexturedLightRgb(
          normals.vertexNormalX()[mergedVertexA],
          normals.vertexNormalY()[mergedVertexA],
          normals.vertexNormalZ()[mergedVertexA],
          normals.vertexMagnitude()[mergedVertexA]
      );
      faceColorB[faceIndex] = referenceTexturedLightRgb(
          normals.vertexNormalX()[mergedVertexB],
          normals.vertexNormalY()[mergedVertexB],
          normals.vertexNormalZ()[mergedVertexB],
          normals.vertexMagnitude()[mergedVertexB]
      );
      faceColorC[faceIndex] = referenceTexturedLightRgb(
          normals.vertexNormalX()[mergedVertexC],
          normals.vertexNormalY()[mergedVertexC],
          normals.vertexNormalZ()[mergedVertexC],
          normals.vertexMagnitude()[mergedVertexC]
      );
      return;
    }
    int flatTexturedColor = referenceTexturedFaceLightRgb(
        normals.faceNormalX()[mergedFaceIndex],
        normals.faceNormalY()[mergedFaceIndex],
        normals.faceNormalZ()[mergedFaceIndex]
    );
    faceColorA[faceIndex] = flatTexturedColor;
    faceColorB[faceIndex] = flatTexturedColor;
    faceColorC[faceIndex] = flatTexturedColor;
  }

  private void populateUntexturedFaceLighting(
      int faceIndex,
      int rawFaceType,
      int mergedFaceIndex,
      int mergedVertexA,
      int mergedVertexB,
      int mergedVertexC,
      RawModelData rawModelData,
      CharacterModelSourceBuilder.ModelContribution contribution,
      ReferenceLightingNormals normals,
      int[] lookValues,
      int[] faceColorA,
      int[] faceColorB,
      int[] faceColorC,
      int[] faceTextureIds,
      int[] textureVertexA,
      int[] textureVertexB,
      int[] textureVertexC
  ) {
    int resolvedFaceColor = resolveFaceColor(rawModelData.faceColorHsl()[faceIndex], lookValues, contribution);
    faceTextureIds[faceIndex] = -1;
    textureVertexA[faceIndex] = -1;
    textureVertexB[faceIndex] = -1;
    textureVertexC[faceIndex] = -1;
    if ((rawFaceType & 1) == 0) {
      faceColorA[faceIndex] = referenceLitHslRgb(
          resolvedFaceColor,
          rawFaceType,
          normals.vertexNormalX()[mergedVertexA],
          normals.vertexNormalY()[mergedVertexA],
          normals.vertexNormalZ()[mergedVertexA],
          normals.vertexMagnitude()[mergedVertexA]
      );
      faceColorB[faceIndex] = referenceLitHslRgb(
          resolvedFaceColor,
          rawFaceType,
          normals.vertexNormalX()[mergedVertexB],
          normals.vertexNormalY()[mergedVertexB],
          normals.vertexNormalZ()[mergedVertexB],
          normals.vertexMagnitude()[mergedVertexB]
      );
      faceColorC[faceIndex] = referenceLitHslRgb(
          resolvedFaceColor,
          rawFaceType,
          normals.vertexNormalX()[mergedVertexC],
          normals.vertexNormalY()[mergedVertexC],
          normals.vertexNormalZ()[mergedVertexC],
          normals.vertexMagnitude()[mergedVertexC]
      );
      return;
    }
    int flatFaceColor = referenceFlatLitHslRgb(
        resolvedFaceColor,
        rawFaceType,
        normals.faceNormalX()[mergedFaceIndex],
        normals.faceNormalY()[mergedFaceIndex],
        normals.faceNormalZ()[mergedFaceIndex]
    );
    faceColorA[faceIndex] = flatFaceColor;
    faceColorB[faceIndex] = flatFaceColor;
    faceColorC[faceIndex] = flatFaceColor;
  }

  private MergedLightingModel mergeLightingModel(List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions) {
    int maxVertexCount = 0;
    int faceCount = 0;
    for (CharacterModelSourceBuilder.PreparedContribution preparedContribution : preparedContributions) {
      RawModelData rawModelData = preparedContribution.contribution().rawModelData();
      maxVertexCount += rawModelData.vertexCount();
      faceCount += rawModelData.faceCount();
    }
    int[] mergedModelX = new int[maxVertexCount];
    int[] mergedModelY = new int[maxVertexCount];
    int[] mergedModelZ = new int[maxVertexCount];
    int[] faceVertexA = new int[faceCount];
    int[] faceVertexB = new int[faceCount];
    int[] faceVertexC = new int[faceCount];
    int[] faceRenderTypes = new int[faceCount];
    ArrayList<int[]> vertexIndexByContribution = new ArrayList<>(preparedContributions.size());
    int mergedVertexCount = 0;
    int mergedFaceCount = 0;
    for (CharacterModelSourceBuilder.PreparedContribution preparedContribution : preparedContributions) {
      RawModelData rawModelData = preparedContribution.contribution().rawModelData();
      int[] vertexIndexMap = new int[rawModelData.vertexCount()];
      // The reference player's Model merge reuses the first exact vertex match it finds before
      // accumulating normals, so seam lighting depends on this exact coordinate-based dedupe.
      for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
        int mergedVertexIndex = findMergedVertex(
            mergedModelX,
            mergedModelY,
            mergedModelZ,
            mergedVertexCount,
            preparedContribution.modelX()[vertexIndex],
            preparedContribution.modelY()[vertexIndex],
            preparedContribution.modelZ()[vertexIndex]
        );
        if (mergedVertexIndex < 0) {
          mergedVertexIndex = mergedVertexCount++;
          mergedModelX[mergedVertexIndex] = preparedContribution.modelX()[vertexIndex];
          mergedModelY[mergedVertexIndex] = preparedContribution.modelY()[vertexIndex];
          mergedModelZ[mergedVertexIndex] = preparedContribution.modelZ()[vertexIndex];
        }
        vertexIndexMap[vertexIndex] = mergedVertexIndex;
      }
      vertexIndexByContribution.add(vertexIndexMap);
      for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
        faceVertexA[mergedFaceCount] = vertexIndexMap[rawModelData.faceVertexA()[faceIndex]];
        faceVertexB[mergedFaceCount] = vertexIndexMap[rawModelData.faceVertexB()[faceIndex]];
        faceVertexC[mergedFaceCount] = vertexIndexMap[rawModelData.faceVertexC()[faceIndex]];
        faceRenderTypes[mergedFaceCount] = rawModelData.faceRenderTypes()[faceIndex];
        mergedFaceCount++;
      }
    }
    return new MergedLightingModel(
        mergedVertexCount,
        Arrays.copyOf(mergedModelX, mergedVertexCount),
        Arrays.copyOf(mergedModelY, mergedVertexCount),
        Arrays.copyOf(mergedModelZ, mergedVertexCount),
        faceVertexA,
        faceVertexB,
        faceVertexC,
        faceRenderTypes,
        List.copyOf(vertexIndexByContribution)
    );
  }

  private int findMergedVertex(
      int[] mergedModelX,
      int[] mergedModelY,
      int[] mergedModelZ,
      int mergedVertexCount,
      int modelX,
      int modelY,
      int modelZ
  ) {
    for (int vertexIndex = 0; vertexIndex < mergedVertexCount; vertexIndex++) {
      if (modelX == mergedModelX[vertexIndex]
          && modelY == mergedModelY[vertexIndex]
          && modelZ == mergedModelZ[vertexIndex]) {
        return vertexIndex;
      }
    }
    return -1;
  }

  private ReferenceLightingNormals calculateReferenceLightingNormals(
      int vertexCount,
      int[] faceVertexA,
      int[] faceVertexB,
      int[] faceVertexC,
      int[] faceRenderTypes,
      int[] modelX,
      int[] modelY,
      int[] modelZ
  ) {
    int faceCount = faceVertexA.length;
    int[] vertexNormalX = new int[vertexCount];
    int[] vertexNormalY = new int[vertexCount];
    int[] vertexNormalZ = new int[vertexCount];
    int[] vertexMagnitude = new int[vertexCount];
    int[] faceNormalX = new int[faceCount];
    int[] faceNormalY = new int[faceCount];
    int[] faceNormalZ = new int[faceCount];
    for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
      int vertexA = faceVertexA[faceIndex];
      int vertexB = faceVertexB[faceIndex];
      int vertexC = faceVertexC[faceIndex];
      int edgeOneX = modelX[vertexB] - modelX[vertexA];
      int edgeOneY = modelY[vertexB] - modelY[vertexA];
      int edgeOneZ = modelZ[vertexB] - modelZ[vertexA];
      int edgeTwoX = modelX[vertexC] - modelX[vertexA];
      int edgeTwoY = modelY[vertexC] - modelY[vertexA];
      int edgeTwoZ = modelZ[vertexC] - modelZ[vertexA];
      int normalX = edgeOneY * edgeTwoZ - edgeOneZ * edgeTwoY;
      int normalY = edgeOneZ * edgeTwoX - edgeOneX * edgeTwoZ;
      int normalZ = edgeOneX * edgeTwoY - edgeOneY * edgeTwoX;
      while (Math.abs(normalX) > 8192 || Math.abs(normalY) > 8192 || Math.abs(normalZ) > 8192) {
        normalX >>= 1;
        normalY >>= 1;
        normalZ >>= 1;
      }
      int magnitude = (int) Math.sqrt(normalX * (double) normalX + normalY * (double) normalY + normalZ * (double) normalZ);
      if (magnitude <= 0) {
        magnitude = 1;
      }
      normalX = normalX * 256 / magnitude;
      normalY = normalY * 256 / magnitude;
      normalZ = normalZ * 256 / magnitude;
      faceNormalX[faceIndex] = normalX;
      faceNormalY[faceIndex] = normalY;
      faceNormalZ[faceIndex] = normalZ;
      if ((faceRenderTypes[faceIndex] & 1) != 0) {
        continue;
      }
      vertexNormalX[vertexA] += normalX;
      vertexNormalY[vertexA] += normalY;
      vertexNormalZ[vertexA] += normalZ;
      vertexMagnitude[vertexA]++;
      vertexNormalX[vertexB] += normalX;
      vertexNormalY[vertexB] += normalY;
      vertexNormalZ[vertexB] += normalZ;
      vertexMagnitude[vertexB]++;
      vertexNormalX[vertexC] += normalX;
      vertexNormalY[vertexC] += normalY;
      vertexNormalZ[vertexC] += normalZ;
      vertexMagnitude[vertexC]++;
    }
    return new ReferenceLightingNormals(
        vertexNormalX,
        vertexNormalY,
        vertexNormalZ,
        vertexMagnitude,
        faceNormalX,
        faceNormalY,
        faceNormalZ
    );
  }

  private int resolveFaceColor(
      int faceColor,
      int[] lookValues,
      CharacterModelSourceBuilder.ModelContribution contribution
  ) {
    return applyAppearancePalettes(
        applyRecolors(faceColor, contribution.recolorSources(), contribution.recolorTargets()),
        lookValues
    );
  }

  private int applyRecolors(int faceColor, List<Integer> sources, List<Integer> targets) {
    for (int index = 0; index < sources.size() && index < targets.size(); index++) {
      if (sources.get(index) == faceColor) {
        return targets.get(index);
      }
    }
    return faceColor;
  }

  private int applyAppearancePalettes(int faceColor, int[] lookValues) {
    for (int paletteIndex = 0; paletteIndex < APPEARANCE_COLOR_PALETTES.length; paletteIndex++) {
      int[] palette = APPEARANCE_COLOR_PALETTES[paletteIndex];
      int replacementIndex = Math.min(Math.max(lookValues[paletteIndex + 1], 0), palette.length - 1);
      if (faceColor == palette[0]) {
        return palette[replacementIndex];
      }
      if (paletteIndex == 1 && faceColor == SECONDARY_TORSO_PALETTE[0]) {
        int secondaryIndex = Math.min(replacementIndex, SECONDARY_TORSO_PALETTE.length - 1);
        return SECONDARY_TORSO_PALETTE[secondaryIndex];
      }
    }
    return faceColor;
  }

  private int referenceLitHslRgb(
      int faceColorHsl,
      int rawFaceType,
      int normalX,
      int normalY,
      int normalZ,
      int normalMagnitude
  ) {
    return CharacterColorPalette.rgb(
        referenceLightHsl(faceColorHsl, rawFaceType, normalX, normalY, normalZ, normalMagnitude)
    );
  }

  private int referenceFlatLitHslRgb(
      int faceColorHsl,
      int rawFaceType,
      int normalX,
      int normalY,
      int normalZ
  ) {
    return CharacterColorPalette.rgb(referenceFlatLightHsl(faceColorHsl, rawFaceType, normalX, normalY, normalZ));
  }

  private int referenceTexturedLightRgb(int normalX, int normalY, int normalZ, int normalMagnitude) {
    return applyBrightness(0xffffff, clamp(referenceLightValue(normalX, normalY, normalZ, normalMagnitude), 2, 126));
  }

  private int referenceTexturedFaceLightRgb(int normalX, int normalY, int normalZ) {
    return applyBrightness(0xffffff, clamp(referenceFlatLightValue(normalX, normalY, normalZ), 2, 126));
  }

  private int referenceLightHsl(
      int faceColorHsl,
      int rawFaceType,
      int normalX,
      int normalY,
      int normalZ,
      int normalMagnitude
  ) {
    return referenceLight(faceColorHsl, referenceLightValue(normalX, normalY, normalZ, normalMagnitude), rawFaceType);
  }

  private int referenceFlatLightHsl(
      int faceColorHsl,
      int rawFaceType,
      int normalX,
      int normalY,
      int normalZ
  ) {
    return referenceLight(faceColorHsl, referenceFlatLightValue(normalX, normalY, normalZ), rawFaceType);
  }

  private int referenceLightValue(int normalX, int normalY, int normalZ, int normalMagnitude) {
    int denominator = referenceSpecular() * Math.max(1, normalMagnitude);
    return REFERENCE_LIGHT_AMBIENT
        + (REFERENCE_LIGHT_X * normalX + REFERENCE_LIGHT_Y * normalY + REFERENCE_LIGHT_Z * normalZ) / denominator;
  }

  private int referenceFlatLightValue(int normalX, int normalY, int normalZ) {
    int denominator = referenceSpecular() + (referenceSpecular() >> 1);
    return REFERENCE_LIGHT_AMBIENT
        + (REFERENCE_LIGHT_X * normalX + REFERENCE_LIGHT_Y * normalY + REFERENCE_LIGHT_Z * normalZ) / denominator;
  }

  private int referenceSpecular() {
    int lightMagnitude = (int) Math.sqrt(
        REFERENCE_LIGHT_X * (double) REFERENCE_LIGHT_X
            + REFERENCE_LIGHT_Y * (double) REFERENCE_LIGHT_Y
            + REFERENCE_LIGHT_Z * (double) REFERENCE_LIGHT_Z
    );
    return lightMagnitude * REFERENCE_LIGHT_CONTRAST >> 8;
  }

  private int referenceLight(int faceColorHsl, int light, int rawFaceType) {
    if ((rawFaceType & 2) == 2) {
      return clamp(light, 2, 126);
    }
    int lit = light * (faceColorHsl & 127) >> 7;
    return (faceColorHsl & 0xff80) + clamp(lit, 2, 126);
  }

  private SceneRasterMode rasterModeForFace(int rawFaceType) {
    return switch (rawFaceType & 3) {
      case 0 -> SceneRasterMode.GOURAUD;
      case 1 -> SceneRasterMode.FLAT;
      case 2, 3 -> SceneRasterMode.TEXTURED;
      default -> SceneRasterMode.FLAT;
    };
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

  private record MergedLightingModel(
      int vertexCount,
      int[] modelX,
      int[] modelY,
      int[] modelZ,
      int[] faceVertexA,
      int[] faceVertexB,
      int[] faceVertexC,
      int[] faceRenderTypes,
      List<int[]> vertexIndexByContribution
  ) {
  }

  private record ReferenceLightingNormals(
      int[] vertexNormalX,
      int[] vertexNormalY,
      int[] vertexNormalZ,
      int[] vertexMagnitude,
      int[] faceNormalX,
      int[] faceNormalY,
      int[] faceNormalZ
  ) {
  }
}
