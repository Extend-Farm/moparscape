package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CharacterModelAssembler {

  private static final float ACTOR_LIGHT_X = -0.35f;
  private static final float ACTOR_LIGHT_Y = 0.82f;
  private static final float ACTOR_LIGHT_Z = -0.44f;
  private static final float ACTOR_FOOTPRINT = 0.66f;
  private static final float ACTOR_HEIGHT = 1.74f;

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

  private final CharacterModelSourceBuilder sourceBuilder;
  private final Map<String, PreparedCharacterModel> preparedModelByAppearanceKey = new HashMap<>();
  private final Map<String, WorldSceneObjectGeometry> geometryByAppearanceKey = new HashMap<>();
  private Float cachedMaleReferenceScale;
  private Float cachedFemaleReferenceScale;

  // This assembler mirrors the legacy appearance contract without importing legacy runtime code:
  // persisted look values are sex + palette choices, while the visible body comes from default
  // identity kits plus wearable item body models resolved from the native cache layer.
  public CharacterModelAssembler(
      ItemDefinitionCatalog itemDefinitions,
      IdentityKitDefinitionCatalog identityKitDefinitions,
      RawModelRepository rawModelRepository
  ) {
    this.sourceBuilder = new CharacterModelSourceBuilder(itemDefinitions, identityKitDefinitions, rawModelRepository);
  }

  public WorldSceneObjectGeometry assemble(BootstrapAppearance appearance, List<BootstrapItemSlot> equipment) {
    String cacheKey = buildCacheKey(appearance, equipment);
    return geometryByAppearanceKey.computeIfAbsent(
        cacheKey,
        ignored -> buildGeometry(preparedModelByAppearanceKey.computeIfAbsent(cacheKey, unused -> prepareModel(appearance, equipment)), ActorAnimationState.idle())
    );
  }

  public WorldSceneObjectGeometry assemble(
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState animationState
  ) {
    if (animationState == null || animationState.isIdle()) {
      return assemble(appearance, equipment);
    }
    String cacheKey = buildCacheKey(appearance, equipment);
    return buildGeometry(
        preparedModelByAppearanceKey.computeIfAbsent(cacheKey, unused -> prepareModel(appearance, equipment)),
        animationState
    );
  }

  private PreparedCharacterModel prepareModel(BootstrapAppearance appearance, List<BootstrapItemSlot> equipment) {
    int[] lookValues = sourceBuilder.normalizedLookValues(appearance);
    boolean female = lookValues[0] == 1;
    CharacterModelSourceBuilder.PreparedCharacterSource preparedCharacterSource = sourceBuilder.prepareSourceModel(lookValues, equipment);
    if (preparedCharacterSource == null) {
      return null;
    }
    ActorTransform actorTransform = resolveActorTransform(preparedCharacterSource.sourceBounds(), female);
    CharacterModelSourceBuilder.SourceBounds sourceBounds = preparedCharacterSource.sourceBounds();
    return new PreparedCharacterModel(
        lookValues,
        preparedCharacterSource.preparedContributions(),
        actorTransform,
        new ActorBounds(
            sourceBounds.minX() * actorTransform.scale() + actorTransform.offsetX(),
            sourceBounds.minY() * actorTransform.scale() + actorTransform.offsetY(),
            sourceBounds.minZ() * actorTransform.scale() + actorTransform.offsetZ(),
            sourceBounds.maxX() * actorTransform.scale() + actorTransform.offsetX(),
            sourceBounds.maxY() * actorTransform.scale() + actorTransform.offsetY(),
            sourceBounds.maxZ() * actorTransform.scale() + actorTransform.offsetZ()
        )
    );
  }

  private WorldSceneObjectGeometry buildGeometry(PreparedCharacterModel preparedCharacterModel, ActorAnimationState animationState) {
    if (preparedCharacterModel == null) {
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
    for (CharacterModelSourceBuilder.PreparedContribution preparedContribution : preparedCharacterModel.preparedContributions()) {
      RawModelData rawModelData = preparedContribution.contribution().rawModelData();
      float[][] transformedVertices = applyActorTransform(preparedContribution, preparedCharacterModel.actorTransform());
      if (!animationState.isIdle()) {
        CharacterWalkPoseAnimator.apply(transformedVertices, preparedCharacterModel.actorBounds(), animationState);
      }
      float[][] faceNormals = computeFaceNormals(rawModelData, transformedVertices);
      float[][] vertexNormals = computeVertexNormals(rawModelData, faceNormals);
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
        int sourceColorHsl = rawModelData.faceColorHsl()[faceIndex];
        int recoloredHsl = resolveFaceColor(sourceColorHsl, preparedCharacterModel.lookValues(), preparedContribution.contribution());
        int baseRgb = hslToRgb(recoloredHsl);
        faces.add(new int[]{
            rawModelData.faceVertexA()[faceIndex] + vertexOffset,
            rawModelData.faceVertexB()[faceIndex] + vertexOffset,
            rawModelData.faceVertexC()[faceIndex] + vertexOffset
        });
        if (faceMode == 0 || faceMode == 2) {
          faceColorA.add(shadeActorColor(
              baseRgb,
              vertexNormals[0][rawModelData.faceVertexA()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexA()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexA()[faceIndex]]
          ));
          faceColorB.add(shadeActorColor(
              baseRgb,
              vertexNormals[0][rawModelData.faceVertexB()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexB()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexB()[faceIndex]]
          ));
          faceColorC.add(shadeActorColor(
              baseRgb,
              vertexNormals[0][rawModelData.faceVertexC()[faceIndex]],
              vertexNormals[1][rawModelData.faceVertexC()[faceIndex]],
              vertexNormals[2][rawModelData.faceVertexC()[faceIndex]]
          ));
        } else {
          int shadedFaceColor = shadeActorColor(
              baseRgb,
              faceNormals[0][faceIndex],
              faceNormals[1][faceIndex],
              faceNormals[2][faceIndex]
          );
          faceColorA.add(shadedFaceColor);
          faceColorB.add(shadedFaceColor);
          faceColorC.add(shadedFaceColor);
        }
        faceAlpha.add(normalizeFaceAlpha(rawModelData.faceAlpha()[faceIndex]));
        faceRasterModes.add(rasterModeForFace(rawFaceType));
        if (faceMode >= 2) {
          // Textured legacy faces reuse the stored "face color" slot as a texture id and then
          // light the face through brightness rather than palette HSL. We preserve that contract
          // here so actor geometry can flow through the same textured raster path as static models.
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

  private String buildCacheKey(BootstrapAppearance appearance, List<BootstrapItemSlot> equipment) {
    StringBuilder builder = new StringBuilder();
    if (appearance != null) {
      for (Integer lookValue : appearance.lookValues()) {
        builder.append(lookValue).append(':');
      }
    }
    builder.append('|');
    for (BootstrapItemSlot equipmentSlot : equipment) {
      builder.append(equipmentSlot.slotIndex()).append('=').append(equipmentSlot.itemId()).append(';');
    }
    return builder.toString();
  }

  private ActorTransform resolveActorTransform(CharacterModelSourceBuilder.SourceBounds sourceBounds, boolean female) {
    float scale = referenceActorScale(female);
    float offsetX = -sourceBounds.centerX() * scale;
    float offsetZ = -sourceBounds.centerZ() * scale;
    float offsetY = -sourceBounds.minY() * scale;
    return new ActorTransform(scale, offsetX, offsetY, offsetZ);
  }

  private float referenceActorScale(boolean female) {
    if (female) {
      if (cachedFemaleReferenceScale == null) {
        cachedFemaleReferenceScale = computeReferenceActorScale(true);
      }
      return cachedFemaleReferenceScale;
    }
    if (cachedMaleReferenceScale == null) {
      cachedMaleReferenceScale = computeReferenceActorScale(false);
    }
    return cachedMaleReferenceScale;
  }

  private float computeReferenceActorScale(boolean female) {
    int[] referenceLookValues = sourceBuilder.normalizedLookValues(null);
    referenceLookValues[0] = female ? 1 : 0;
    CharacterModelSourceBuilder.PreparedCharacterSource referenceSource = sourceBuilder.prepareSourceModel(referenceLookValues, List.of());
    if (referenceSource == null) {
      return 1.0f;
    }
    CharacterModelSourceBuilder.SourceBounds sourceBounds = referenceSource.sourceBounds();
    float sourceWidth = Math.max(0.01f, sourceBounds.maxX() - sourceBounds.minX());
    float sourceDepth = Math.max(0.01f, sourceBounds.maxZ() - sourceBounds.minZ());
    float sourceHeight = Math.max(0.01f, sourceBounds.maxY() - sourceBounds.minY());
    return Math.min(ACTOR_FOOTPRINT / Math.max(sourceWidth, sourceDepth), ACTOR_HEIGHT / sourceHeight);
  }

  private float[][] applyActorTransform(CharacterModelSourceBuilder.PreparedContribution contribution, ActorTransform actorTransform) {
    float[] worldX = contribution.sourceX().clone();
    float[] worldY = contribution.sourceY().clone();
    float[] worldZ = contribution.sourceZ().clone();
    for (int vertexIndex = 0; vertexIndex < worldX.length; vertexIndex++) {
      worldX[vertexIndex] = worldX[vertexIndex] * actorTransform.scale() + actorTransform.offsetX();
      worldY[vertexIndex] = worldY[vertexIndex] * actorTransform.scale() + actorTransform.offsetY();
      worldZ[vertexIndex] = worldZ[vertexIndex] * actorTransform.scale() + actorTransform.offsetZ();
    }
    return new float[][]{worldX, worldY, worldZ};
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

  private SceneRasterMode rasterModeForFace(int rawFaceType) {
    return switch (rawFaceType & 3) {
      case 0 -> SceneRasterMode.GOURAUD;
      case 1 -> SceneRasterMode.FLAT;
      case 2, 3 -> SceneRasterMode.TEXTURED;
      default -> SceneRasterMode.FLAT;
    };
  }

  private int normalizeFaceAlpha(int rawFaceAlpha) {
    return 255 - clamp(rawFaceAlpha, 0, 255);
  }

  private int shadeActorColor(int baseRgb, float normalX, float normalY, float normalZ) {
    float lightDot = normalX * ACTOR_LIGHT_X + normalY * ACTOR_LIGHT_Y + normalZ * ACTOR_LIGHT_Z;
    int brightness = clamp(Math.round(122 + lightDot * 74.0f), 68, 228);
    return applyBrightness(baseRgb, brightness);
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

  private record PreparedCharacterModel(
      int[] lookValues,
      List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
      ActorTransform actorTransform,
      ActorBounds actorBounds
  ) {

    private PreparedCharacterModel {
      lookValues = lookValues.clone();
      preparedContributions = List.copyOf(preparedContributions);
    }
  }

  record ActorBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {

    float centerX() {
      return (minX + maxX) * 0.5f;
    }

    float centerZ() {
      return (minZ + maxZ) * 0.5f;
    }
  }

  private record ActorTransform(float scale, float offsetX, float offsetY, float offsetZ) {
  }
}
