package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import java.util.ArrayList;
import java.util.List;

final class ItemIconModelProjector {

  private static final int LIGHT_VECTOR_X = -50;
  private static final int LIGHT_VECTOR_Y = -10;
  private static final int LIGHT_VECTOR_Z = -50;
  private static final int LIGHT_VECTOR_LENGTH = 71;
  private static final double HSL_PALETTE_GAMMA = 0.8D;
  private static final SceneTextureAssets EMPTY_TEXTURE_ASSETS = new SceneTextureAssets(new ArgbImage[0]);
  private static final int[] SHADED_HSL_PALETTE = buildShadedHslPalette(HSL_PALETTE_GAMMA);

  private final ItemDefinitionCatalog itemDefinitions;
  private final ItemIconDefinitionResolver definitionResolver;
  private final RawModelRepository rawModelRepository;
  private final SceneTextureAssets sceneTextureAssets;

  ItemIconModelProjector(
      ItemDefinitionCatalog itemDefinitions,
      RawModelRepository rawModelRepository,
      SceneTextureAssets sceneTextureAssets
  ) {
    this.itemDefinitions = itemDefinitions;
    definitionResolver = new ItemIconDefinitionResolver(itemDefinitions);
    this.rawModelRepository = rawModelRepository;
    this.sceneTextureAssets = sceneTextureAssets == null ? EMPTY_TEXTURE_ASSETS : sceneTextureAssets;
  }

  List<ItemIconRasterizer.ProjectedFace> project(int itemId) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null || !definitionResolver.hasInventoryModel(definition)) {
      return List.of();
    }
    RawModelData rawModelData = rawModelRepository.loadModel(definition.inventoryAppearance().modelId());
    if (rawModelData == null || rawModelData.faceCount() == 0) {
      return List.of();
    }

    PreparedInventoryModel preparedInventoryModel = prepareInventoryModel(rawModelData, definition.inventoryAppearance());
    ProjectedVertices projectedVertices = projectVertices(preparedInventoryModel, definition.inventoryAppearance());
    NormalSet normalSet = computeNormals(rawModelData, preparedInventoryModel.axes());
    return projectFaces(rawModelData, definition, projectedVertices.viewSpaceAxes(), normalSet);
  }

  private PreparedInventoryModel prepareInventoryModel(
      RawModelData rawModelData,
      ItemDefinition.InventoryAppearance inventoryAppearance
  ) {
    int vertexCount = rawModelData.vertexCount();
    float[] modelX = new float[vertexCount];
    float[] modelY = new float[vertexCount];
    float[] modelZ = new float[vertexCount];
    float scaleX = inventoryAppearance.resizeX() / 128.0f;
    float scaleY = inventoryAppearance.resizeY() / 128.0f;
    float scaleZ = inventoryAppearance.resizeZ() / 128.0f;
    float modelHeight = 0.0f;
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      float x = rawModelData.vertexX()[vertexIndex] * scaleX;
      float y = rawModelData.vertexY()[vertexIndex] * scaleY;
      float z = rawModelData.vertexZ()[vertexIndex] * scaleZ;
      modelX[vertexIndex] = x;
      modelY[vertexIndex] = y;
      modelZ[vertexIndex] = z;
      modelHeight = Math.max(modelHeight, -y);
    }
    return new PreparedInventoryModel(modelX, modelY, modelZ, modelHeight);
  }

  private ProjectedVertices projectVertices(
      PreparedInventoryModel preparedInventoryModel,
      ItemDefinition.InventoryAppearance inventoryAppearance
  ) {
    float[] viewX = new float[preparedInventoryModel.x().length];
    float[] viewY = new float[preparedInventoryModel.x().length];
    float[] viewZ = new float[preparedInventoryModel.x().length];
    float pitchRadians = angleRadians(inventoryAppearance.rotationX());
    float yawRadians = angleRadians(inventoryAppearance.rotationY());
    float rollRadians = angleRadians(inventoryAppearance.rotationZ());
    float pitchSin = (float) Math.sin(pitchRadians);
    float pitchCos = (float) Math.cos(pitchRadians);
    float yawSin = (float) Math.sin(yawRadians);
    float yawCos = (float) Math.cos(yawRadians);
    float rollSin = (float) Math.sin(rollRadians);
    float rollCos = (float) Math.cos(rollRadians);
    float cameraY = pitchSin * inventoryAppearance.zoom()
        + preparedInventoryModel.modelHeight() / 2.0f
        + inventoryAppearance.offsetY();
    float cameraZ = pitchCos * inventoryAppearance.zoom() + inventoryAppearance.offsetY();
    for (int vertexIndex = 0; vertexIndex < preparedInventoryModel.x().length; vertexIndex++) {
      float x = preparedInventoryModel.x()[vertexIndex];
      float y = preparedInventoryModel.y()[vertexIndex];
      float z = preparedInventoryModel.z()[vertexIndex];
      if (inventoryAppearance.rotationZ() != 0) {
        float rotatedX = y * rollSin + x * rollCos;
        y = y * rollCos - x * rollSin;
        x = rotatedX;
      }
      if (inventoryAppearance.rotationY() != 0) {
        float rotatedX = z * yawSin + x * yawCos;
        z = z * yawCos - x * yawSin;
        x = rotatedX;
      }
      x += inventoryAppearance.offsetX();
      y += cameraY;
      z += cameraZ;
      float projectedY = y * pitchCos - z * pitchSin;
      float depth = y * pitchSin + z * pitchCos;
      viewX[vertexIndex] = x;
      viewY[vertexIndex] = projectedY;
      viewZ[vertexIndex] = depth;
    }
    return new ProjectedVertices(new float[][]{viewX, viewY, viewZ});
  }

  private List<ItemIconRasterizer.ProjectedFace> projectFaces(
      RawModelData rawModelData,
      ItemDefinition definition,
      float[][] viewSpaceVertices,
      NormalSet normalSet
  ) {
    ArrayList<ItemIconRasterizer.ProjectedFace> projectedFaces = new ArrayList<>(rawModelData.faceCount());
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      FaceSurface faceSurface = resolveFaceSurface(rawModelData, definition, viewSpaceVertices, normalSet, faceIndex);
      if (faceSurface == null) {
        continue;
      }
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      TextureCoordinates textureCoordinates = faceSurface.textureCoordinates();
      List<ItemIconRasterizer.ClippedVertex> clippedVertices = ItemIconRasterizer.clipTriangleToNearPlane(
          viewSpaceVertex(viewSpaceVertices, vertexA, faceSurface.colors().colorA(), textureCoordinates.ua(), textureCoordinates.va()),
          viewSpaceVertex(viewSpaceVertices, vertexB, faceSurface.colors().colorB(), textureCoordinates.ub(), textureCoordinates.vb()),
          viewSpaceVertex(viewSpaceVertices, vertexC, faceSurface.colors().colorC(), textureCoordinates.uc(), textureCoordinates.vc())
      );
      for (int clippedIndex = 1; clippedIndex < clippedVertices.size() - 1; clippedIndex++) {
        ItemIconRasterizer.ProjectedFace projectedFace = ItemIconRasterizer.projectedFace(
            clippedVertices.get(0),
            clippedVertices.get(clippedIndex),
            clippedVertices.get(clippedIndex + 1),
            faceSurface.alpha(),
            faceSurface.texture()
        );
        if (projectedFace != null) {
          projectedFaces.add(projectedFace);
        }
      }
    }
    return projectedFaces;
  }

  private FaceSurface resolveFaceSurface(
      RawModelData rawModelData,
      ItemDefinition definition,
      float[][] viewSpaceVertices,
      NormalSet normalSet,
      int faceIndex
  ) {
    int alpha = normalizeFaceAlpha(rawModelData.faceAlpha()[faceIndex]);
    if (alpha <= 0) {
      return null;
    }
    int faceMode = rawModelData.faceRenderTypes()[faceIndex] & 3;
    FaceColors colors = resolveFaceColors(rawModelData, definition, normalSet, faceIndex, faceMode);
    ArgbImage texture = faceMode >= 2 ? resolveTexture(rawModelData.faceColorHsl()[faceIndex]) : null;
    TextureCoordinates textureCoordinates = TextureCoordinates.NONE;
    if (texture != null) {
      textureCoordinates = textureCoordinates(rawModelData, viewSpaceVertices, faceIndex);
      if (textureCoordinates == null) {
        texture = null;
        textureCoordinates = TextureCoordinates.NONE;
      }
    }
    return new FaceSurface(colors, alpha, texture, textureCoordinates);
  }

  private FaceColors resolveFaceColors(
      RawModelData rawModelData,
      ItemDefinition definition,
      NormalSet normalSet,
      int faceIndex,
      int faceMode
  ) {
    int vertexA = rawModelData.faceVertexA()[faceIndex];
    int vertexB = rawModelData.faceVertexB()[faceIndex];
    int vertexC = rawModelData.faceVertexC()[faceIndex];
    int sourceColorHsl = rawModelData.faceColorHsl()[faceIndex];
    return switch (faceMode) {
      case 0 -> gouraudFaceColors(definition, normalSet, sourceColorHsl, vertexA, vertexB, vertexC);
      case 1 -> flatFaceColors(definition, normalSet, rawModelData.faceRenderTypes()[faceIndex], sourceColorHsl, faceIndex);
      case 2 -> texturedVertexFaceColors(definition, normalSet, vertexA, vertexB, vertexC);
      default -> texturedFlatFaceColors(definition, normalSet, faceIndex);
    };
  }

  private FaceColors gouraudFaceColors(
      ItemDefinition definition,
      NormalSet normalSet,
      int sourceColorHsl,
      int vertexA,
      int vertexB,
      int vertexC
  ) {
    int recoloredHsl = recolor(sourceColorHsl, definition);
    return new FaceColors(
        shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexA),
        shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexB),
        shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexC)
    );
  }

  private FaceColors flatFaceColors(
      ItemDefinition definition,
      NormalSet normalSet,
      int renderType,
      int sourceColorHsl,
      int faceIndex
  ) {
    int recoloredHsl = recolor(sourceColorHsl, definition);
    return FaceColors.uniform(
        shadeFlatColor(
            recoloredHsl,
            renderType,
            definition.inventoryAppearance(),
            normalSet,
            faceIndex
        )
    );
  }

  private FaceColors texturedVertexFaceColors(
      ItemDefinition definition,
      NormalSet normalSet,
      int vertexA,
      int vertexB,
      int vertexC
  ) {
    return new FaceColors(
        shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexA),
        shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexB),
        shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexC)
    );
  }

  private FaceColors texturedFlatFaceColors(
      ItemDefinition definition,
      NormalSet normalSet,
      int faceIndex
  ) {
    return FaceColors.uniform(shadeTexturedFlatColor(definition.inventoryAppearance(), normalSet, faceIndex));
  }

  private ItemIconRasterizer.ClippedVertex viewSpaceVertex(
      float[][] viewSpaceVertices,
      int vertexIndex,
      int color,
      float textureU,
      float textureV
  ) {
    return new ItemIconRasterizer.ClippedVertex(
        viewSpaceVertices[0][vertexIndex],
        viewSpaceVertices[1][vertexIndex],
        viewSpaceVertices[2][vertexIndex],
        color,
        textureU,
        textureV
    );
  }

  private ArgbImage resolveTexture(int textureId) {
    return sceneTextureAssets.texture(textureId);
  }

  private TextureCoordinates textureCoordinates(RawModelData rawModelData, float[][] transformedVertices, int faceIndex) {
    int textureIndex = rawModelData.faceRenderTypes()[faceIndex] >> 2;
    if (textureIndex < 0 || textureIndex >= rawModelData.texturedFaceCount()) {
      return planarTextureCoordinates(rawModelData, transformedVertices, faceIndex);
    }
    float[] anchorA = vertex(transformedVertices, rawModelData.texturedFaceVertexA()[textureIndex]);
    float[] anchorB = vertex(transformedVertices, rawModelData.texturedFaceVertexB()[textureIndex]);
    float[] anchorC = vertex(transformedVertices, rawModelData.texturedFaceVertexC()[textureIndex]);
    Uv uvA = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexA()[faceIndex]));
    Uv uvB = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexB()[faceIndex]));
    Uv uvC = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexC()[faceIndex]));
    if (uvA == null || uvB == null || uvC == null) {
      return planarTextureCoordinates(rawModelData, transformedVertices, faceIndex);
    }
    return TextureCoordinates.of(uvA, uvB, uvC);
  }

  private Uv anchoredUv(float[] anchorA, float[] anchorB, float[] anchorC, float[] point) {
    float abX = anchorB[0] - anchorA[0];
    float abY = anchorB[1] - anchorA[1];
    float abZ = anchorB[2] - anchorA[2];
    float acX = anchorC[0] - anchorA[0];
    float acY = anchorC[1] - anchorA[1];
    float acZ = anchorC[2] - anchorA[2];
    float apX = point[0] - anchorA[0];
    float apY = point[1] - anchorA[1];
    float apZ = point[2] - anchorA[2];
    float dotAbAb = dot(abX, abY, abZ, abX, abY, abZ);
    float dotAbAc = dot(abX, abY, abZ, acX, acY, acZ);
    float dotAcAc = dot(acX, acY, acZ, acX, acY, acZ);
    float dotApAb = dot(apX, apY, apZ, abX, abY, abZ);
    float dotApAc = dot(apX, apY, apZ, acX, acY, acZ);
    float denominator = dotAbAb * dotAcAc - dotAbAc * dotAbAc;
    if (Math.abs(denominator) < 0.00001f) {
      return null;
    }
    float u = (dotAcAc * dotApAb - dotAbAc * dotApAc) / denominator;
    float v = (dotAbAb * dotApAc - dotAbAc * dotApAb) / denominator;
    return new Uv(u, v);
  }

  private TextureCoordinates planarTextureCoordinates(RawModelData rawModelData, float[][] transformedVertices, int faceIndex) {
    float[] vertexA = vertex(transformedVertices, rawModelData.faceVertexA()[faceIndex]);
    float[] vertexB = vertex(transformedVertices, rawModelData.faceVertexB()[faceIndex]);
    float[] vertexC = vertex(transformedVertices, rawModelData.faceVertexC()[faceIndex]);
    float edgeOneX = vertexB[0] - vertexA[0];
    float edgeOneY = vertexB[1] - vertexA[1];
    float edgeOneZ = vertexB[2] - vertexA[2];
    float edgeTwoX = vertexC[0] - vertexA[0];
    float edgeTwoY = vertexC[1] - vertexA[1];
    float edgeTwoZ = vertexC[2] - vertexA[2];
    float normalX = edgeOneY * edgeTwoZ - edgeOneZ * edgeTwoY;
    float normalY = edgeOneZ * edgeTwoX - edgeOneX * edgeTwoZ;
    float normalZ = edgeOneX * edgeTwoY - edgeOneY * edgeTwoX;
    ProjectionAxis projectionAxis = dominantAxis(normalX, normalY, normalZ);
    Uv uvA = projectedUv(vertexA, projectionAxis, normalX, normalY, normalZ);
    Uv uvB = projectedUv(vertexB, projectionAxis, normalX, normalY, normalZ);
    Uv uvC = projectedUv(vertexC, projectionAxis, normalX, normalY, normalZ);
    return TextureCoordinates.of(uvA, uvB, uvC);
  }

  private ProjectionAxis dominantAxis(float normalX, float normalY, float normalZ) {
    float absX = Math.abs(normalX);
    float absY = Math.abs(normalY);
    float absZ = Math.abs(normalZ);
    if (absY >= absX && absY >= absZ) {
      return ProjectionAxis.Y;
    }
    if (absX >= absZ) {
      return ProjectionAxis.X;
    }
    return ProjectionAxis.Z;
  }

  private Uv projectedUv(
      float[] vertex,
      ProjectionAxis projectionAxis,
      float normalX,
      float normalY,
      float normalZ
  ) {
    return switch (projectionAxis) {
      case X -> new Uv(normalX >= 0.0f ? vertex[2] : -vertex[2], vertex[1]);
      case Y -> new Uv(vertex[0], normalY >= 0.0f ? vertex[2] : -vertex[2]);
      case Z -> new Uv(normalZ >= 0.0f ? vertex[0] : -vertex[0], vertex[1]);
    };
  }

  private float dot(float ax, float ay, float az, float bx, float by, float bz) {
    return ax * bx + ay * by + az * bz;
  }

  private float[] vertex(float[][] transformedVertices, int vertexIndex) {
    return new float[]{
        transformedVertices[0][vertexIndex],
        transformedVertices[1][vertexIndex],
        transformedVertices[2][vertexIndex]
    };
  }

  private int recolor(int sourceColorHsl, ItemDefinition definition) {
    for (int index = 0; index < Math.min(definition.recolorSources().size(), definition.recolorTargets().size()); index++) {
      if (definition.recolorSources().get(index) == sourceColorHsl) {
        return definition.recolorTargets().get(index);
      }
    }
    return sourceColorHsl;
  }

  private NormalSet computeNormals(RawModelData rawModelData, float[][] transformedVertices) {
    int[] faceNormalX = new int[rawModelData.faceCount()];
    int[] faceNormalY = new int[rawModelData.faceCount()];
    int[] faceNormalZ = new int[rawModelData.faceCount()];
    int[] vertexNormalX = new int[rawModelData.vertexCount()];
    int[] vertexNormalY = new int[rawModelData.vertexCount()];
    int[] vertexNormalZ = new int[rawModelData.vertexCount()];
    int[] contributions = new int[rawModelData.vertexCount()];
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      float abX = transformedVertices[0][vertexB] - transformedVertices[0][vertexA];
      float abY = transformedVertices[1][vertexB] - transformedVertices[1][vertexA];
      float abZ = transformedVertices[2][vertexB] - transformedVertices[2][vertexA];
      float acX = transformedVertices[0][vertexC] - transformedVertices[0][vertexA];
      float acY = transformedVertices[1][vertexC] - transformedVertices[1][vertexA];
      float acZ = transformedVertices[2][vertexC] - transformedVertices[2][vertexA];
      float normalX = abY * acZ - abZ * acY;
      float normalY = abZ * acX - abX * acZ;
      float normalZ = abX * acY - abY * acX;
      float magnitude = (float) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
      if (magnitude <= 0.0001f) {
        magnitude = 1.0f;
      }
      int scaledNormalX = Math.round(normalX * 256.0f / magnitude);
      int scaledNormalY = Math.round(normalY * 256.0f / magnitude);
      int scaledNormalZ = Math.round(normalZ * 256.0f / magnitude);
      faceNormalX[faceIndex] = scaledNormalX;
      faceNormalY[faceIndex] = scaledNormalY;
      faceNormalZ[faceIndex] = scaledNormalZ;
      if ((rawModelData.faceRenderTypes()[faceIndex] & 1) != 0) {
        continue;
      }
      vertexNormalX[vertexA] += scaledNormalX;
      vertexNormalY[vertexA] += scaledNormalY;
      vertexNormalZ[vertexA] += scaledNormalZ;
      contributions[vertexA]++;
      vertexNormalX[vertexB] += scaledNormalX;
      vertexNormalY[vertexB] += scaledNormalY;
      vertexNormalZ[vertexB] += scaledNormalZ;
      contributions[vertexB]++;
      vertexNormalX[vertexC] += scaledNormalX;
      vertexNormalY[vertexC] += scaledNormalY;
      vertexNormalZ[vertexC] += scaledNormalZ;
      contributions[vertexC]++;
    }
    for (int vertexIndex = 0; vertexIndex < rawModelData.vertexCount(); vertexIndex++) {
      if (contributions[vertexIndex] != 0) {
        continue;
      }
      vertexNormalY[vertexIndex] = 256;
      contributions[vertexIndex] = 1;
    }
    return new NormalSet(
        faceNormalX,
        faceNormalY,
        faceNormalZ,
        vertexNormalX,
        vertexNormalY,
        vertexNormalZ,
        contributions
    );
  }

  private int normalizeFaceAlpha(int rawFaceAlpha) {
    return 255 - clamp(rawFaceAlpha, 0, 255);
  }

  private int shadeVertexColor(
      int colorHsl,
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int vertexIndex
  ) {
    return paletteRgb(shadeHsl(colorHsl, vertexBrightness(inventoryAppearance, normalSet, vertexIndex), 0));
  }

  private int shadeFlatColor(
      int colorHsl,
      int renderType,
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int faceIndex
  ) {
    return paletteRgb(shadeHsl(colorHsl, flatBrightness(inventoryAppearance, normalSet, faceIndex), renderType));
  }

  private int shadeTexturedVertexColor(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int vertexIndex
  ) {
    int brightness = clamp(vertexBrightness(inventoryAppearance, normalSet, vertexIndex), 0, 255);
    return (brightness << 16) | (brightness << 8) | brightness;
  }

  private int shadeTexturedFlatColor(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int faceIndex
  ) {
    int brightness = clamp(flatBrightness(inventoryAppearance, normalSet, faceIndex), 0, 255);
    return (brightness << 16) | (brightness << 8) | brightness;
  }

  private int vertexBrightness(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int vertexIndex
  ) {
    int denominator = lightAttenuation(inventoryAppearance) * Math.max(1, normalSet.vertexContributions()[vertexIndex]);
    int numerator = LIGHT_VECTOR_X * normalSet.vertexNormalX()[vertexIndex]
        + LIGHT_VECTOR_Y * normalSet.vertexNormalY()[vertexIndex]
        + LIGHT_VECTOR_Z * normalSet.vertexNormalZ()[vertexIndex];
    return ambient(inventoryAppearance) + numerator / Math.max(1, denominator);
  }

  private int flatBrightness(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int faceIndex
  ) {
    int numerator = LIGHT_VECTOR_X * normalSet.faceNormalX()[faceIndex]
        + LIGHT_VECTOR_Y * normalSet.faceNormalY()[faceIndex]
        + LIGHT_VECTOR_Z * normalSet.faceNormalZ()[faceIndex];
    int denominator = lightAttenuation(inventoryAppearance);
    return ambient(inventoryAppearance) + numerator / Math.max(1, denominator + denominator / 2);
  }

  private int ambient(ItemDefinition.InventoryAppearance inventoryAppearance) {
    return 64 + inventoryAppearance.ambient();
  }

  private int lightAttenuation(ItemDefinition.InventoryAppearance inventoryAppearance) {
    return ((768 + inventoryAppearance.contrast()) * LIGHT_VECTOR_LENGTH) >> 8;
  }

  private int shadeHsl(int colorHsl, int brightness, int renderType) {
    if ((renderType & 2) == 2) {
      return 127 - clamp(brightness, 0, 127);
    }
    int shadedLightness = brightness * (colorHsl & 0x7f) >> 7;
    shadedLightness = clamp(shadedLightness, 2, 126);
    return (colorHsl & 0xff80) + shadedLightness;
  }

  private int paletteRgb(int shadedHsl) {
    int paletteIndex = clamp(shadedHsl, 0, SHADED_HSL_PALETTE.length - 1);
    int rgb = SHADED_HSL_PALETTE[paletteIndex];
    return rgb == 0 ? 1 : rgb;
  }

  private static int[] buildShadedHslPalette(double gamma) {
    int[] palette = new int[0x10000];
    int paletteIndex = 0;
    for (int hueIndex = 0; hueIndex < 512; hueIndex++) {
      double hue = (hueIndex / 8) / 64.0D + 0.0078125D;
      double saturation = (hueIndex & 7) / 8.0D + 0.0625D;
      for (int lightnessIndex = 0; lightnessIndex < 128; lightnessIndex++) {
        double lightness = lightnessIndex / 128.0D;
        double red = lightness;
        double green = lightness;
        double blue = lightness;
        if (saturation != 0.0D) {
          double q = lightness < 0.5D
              ? lightness * (1.0D + saturation)
              : (lightness + saturation) - lightness * saturation;
          double p = 2.0D * lightness - q;
          red = paletteChannel(p, q, hue + 0.3333333333333333D);
          green = paletteChannel(p, q, hue);
          blue = paletteChannel(p, q, hue - 0.3333333333333333D);
        }
        int rgb = ((int) (red * 256.0D) << 16)
            + ((int) (green * 256.0D) << 8)
            + (int) (blue * 256.0D);
        rgb = gammaAdjust(rgb, gamma);
        palette[paletteIndex++] = rgb == 0 ? 1 : rgb;
      }
    }
    return palette;
  }

  private static double paletteChannel(double p, double q, double value) {
    double wrapped = value;
    if (wrapped > 1.0D) {
      wrapped--;
    }
    if (wrapped < 0.0D) {
      wrapped++;
    }
    if (6.0D * wrapped < 1.0D) {
      return p + (q - p) * 6.0D * wrapped;
    }
    if (2.0D * wrapped < 1.0D) {
      return q;
    }
    if (3.0D * wrapped < 2.0D) {
      return p + (q - p) * (0.6666666666666666D - wrapped) * 6.0D;
    }
    return p;
  }

  private static int gammaAdjust(int rgb, double gamma) {
    double red = Math.pow((rgb >>> 16) / 256.0D, gamma);
    double green = Math.pow(((rgb >>> 8) & 0xff) / 256.0D, gamma);
    double blue = Math.pow((rgb & 0xff) / 256.0D, gamma);
    return ((int) (red * 256.0D) << 16)
        | ((int) (green * 256.0D) << 8)
        | (int) (blue * 256.0D);
  }

  private float angleRadians(int angleUnits) {
    return (float) (angleUnits * (Math.PI * 2.0 / 2048.0));
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private record FaceSurface(
      FaceColors colors,
      int alpha,
      ArgbImage texture,
      TextureCoordinates textureCoordinates
  ) {
  }

  private record FaceColors(
      int colorA,
      int colorB,
      int colorC
  ) {

    private static FaceColors uniform(int color) {
      return new FaceColors(color, color, color);
    }
  }

  private record PreparedInventoryModel(
      float[] x,
      float[] y,
      float[] z,
      float modelHeight
  ) {

    private float[][] axes() {
      return new float[][]{x, y, z};
    }
  }

  private record ProjectedVertices(float[][] viewSpaceAxes) {
  }

  private record TextureCoordinates(
      float ua,
      float va,
      float ub,
      float vb,
      float uc,
      float vc
  ) {

    private static final TextureCoordinates NONE = new TextureCoordinates(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    private static TextureCoordinates of(Uv a, Uv b, Uv c) {
      return new TextureCoordinates(a.u(), a.v(), b.u(), b.v(), c.u(), c.v());
    }
  }

  private record Uv(float u, float v) {
  }

  private record NormalSet(
      int[] faceNormalX,
      int[] faceNormalY,
      int[] faceNormalZ,
      int[] vertexNormalX,
      int[] vertexNormalY,
      int[] vertexNormalZ,
      int[] vertexContributions
  ) {
  }

  private enum ProjectionAxis {
    X,
    Y,
    Z
  }
}
