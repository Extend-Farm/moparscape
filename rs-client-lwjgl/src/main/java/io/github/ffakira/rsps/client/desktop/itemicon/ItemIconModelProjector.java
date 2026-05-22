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
  private static final int TRIG_SCALE = 1 << 16;
  private static final SceneTextureAssets EMPTY_TEXTURE_ASSETS = new SceneTextureAssets(new ArgbImage[0]);
  private static final int[] SINE = buildTrigTable(true);
  private static final int[] COSINE = buildTrigTable(false);

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

  List<ProjectedFace> project(int itemId) {
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
    return projectFaces(
        rawModelData,
        definition,
        projectedVertices.viewSpaceAxes(),
        normalSet
    );
  }

  List<ProjectedFace> projectWidgetPreview(int itemId, int zoomScale) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null || !definitionResolver.hasInventoryModel(definition) || zoomScale <= 0) {
      return List.of();
    }
    RawModelData rawModelData = rawModelRepository.loadModel(definition.inventoryAppearance().modelId());
    if (rawModelData == null || rawModelData.faceCount() == 0) {
      return List.of();
    }

    PreparedInventoryModel preparedInventoryModel = prepareInventoryModel(rawModelData, definition.inventoryAppearance());
    WidgetPreviewAppearance widgetPreviewAppearance = widgetPreviewAppearance(definition.inventoryAppearance(), zoomScale);
    ProjectedVertices projectedVertices = projectWidgetPreviewVertices(preparedInventoryModel, widgetPreviewAppearance);
    NormalSet normalSet = computeNormals(rawModelData, preparedInventoryModel.axes());
    return projectFaces(
        rawModelData,
        definition,
        projectedVertices.viewSpaceAxes(),
        normalSet
    );
  }

  private PreparedInventoryModel prepareInventoryModel(
      RawModelData rawModelData,
      ItemDefinition.InventoryAppearance inventoryAppearance
  ) {
    int vertexCount = rawModelData.vertexCount();
    int[] modelX = new int[vertexCount];
    int[] modelY = new int[vertexCount];
    int[] modelZ = new int[vertexCount];
    int modelHeight = 0;
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      int x = rawModelData.vertexX()[vertexIndex] * inventoryAppearance.resizeX() / 128;
      int y = rawModelData.vertexY()[vertexIndex] * inventoryAppearance.resizeY() / 128;
      int z = rawModelData.vertexZ()[vertexIndex] * inventoryAppearance.resizeZ() / 128;
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
    int pitchSin = SINE[angleIndex(inventoryAppearance.rotationX())];
    int pitchCos = COSINE[angleIndex(inventoryAppearance.rotationX())];
    int yawSin = SINE[angleIndex(inventoryAppearance.rotationY())];
    int yawCos = COSINE[angleIndex(inventoryAppearance.rotationY())];
    int rollSin = SINE[angleIndex(inventoryAppearance.rotationZ())];
    int rollCos = COSINE[angleIndex(inventoryAppearance.rotationZ())];
    // Match the classic item sprite camera math so icon footprints stay aligned with cache-era assets.
    int cameraY = (pitchSin * inventoryAppearance.zoom() >> 16)
        + preparedInventoryModel.modelHeight() / 2
        + inventoryAppearance.offsetY();
    int cameraZ = (pitchCos * inventoryAppearance.zoom() >> 16) + inventoryAppearance.offsetY();
    for (int vertexIndex = 0; vertexIndex < preparedInventoryModel.x().length; vertexIndex++) {
      int x = preparedInventoryModel.x()[vertexIndex];
      int y = preparedInventoryModel.y()[vertexIndex];
      int z = preparedInventoryModel.z()[vertexIndex];
      if (inventoryAppearance.rotationZ() != 0) {
        int rotatedX = y * rollSin + x * rollCos >> 16;
        y = y * rollCos - x * rollSin >> 16;
        x = rotatedX;
      }
      if (inventoryAppearance.rotationY() != 0) {
        int rotatedX = z * yawSin + x * yawCos >> 16;
        z = z * yawCos - x * yawSin >> 16;
        x = rotatedX;
      }
      x += inventoryAppearance.offsetX();
      y += cameraY;
      z += cameraZ;
      int projectedY = y * pitchCos - z * pitchSin >> 16;
      int depth = y * pitchSin + z * pitchCos >> 16;
      viewX[vertexIndex] = x;
      viewY[vertexIndex] = projectedY;
      viewZ[vertexIndex] = depth;
    }
    return new ProjectedVertices(new float[][]{viewX, viewY, viewZ});
  }

  private ProjectedVertices projectWidgetPreviewVertices(
      PreparedInventoryModel preparedInventoryModel,
      WidgetPreviewAppearance widgetPreviewAppearance
  ) {
    float[] viewX = new float[preparedInventoryModel.x().length];
    float[] viewY = new float[preparedInventoryModel.x().length];
    float[] viewZ = new float[preparedInventoryModel.x().length];
    int rotationSin = SINE[angleIndex(widgetPreviewAppearance.rotationX())];
    int rotationCos = COSINE[angleIndex(widgetPreviewAppearance.rotationX())];
    int tiltSin = SINE[angleIndex(widgetPreviewAppearance.rotationY())];
    int tiltCos = COSINE[angleIndex(widgetPreviewAppearance.rotationY())];
    int cameraY = (rotationSin * widgetPreviewAppearance.zoom() >> 16)
        + preparedInventoryModel.modelHeight() / 2;
    int cameraZ = rotationCos * widgetPreviewAppearance.zoom() >> 16;
    for (int vertexIndex = 0; vertexIndex < preparedInventoryModel.x().length; vertexIndex++) {
      int x = preparedInventoryModel.x()[vertexIndex];
      int y = preparedInventoryModel.y()[vertexIndex];
      int z = preparedInventoryModel.z()[vertexIndex];
      if (widgetPreviewAppearance.rotationY() != 0) {
        int rotatedX = z * tiltSin + x * tiltCos >> 16;
        z = z * tiltCos - x * tiltSin >> 16;
        x = rotatedX;
      }
      y += cameraY;
      z += cameraZ;
      int projectedY = y * rotationCos - z * rotationSin >> 16;
      int depth = y * rotationSin + z * rotationCos >> 16;
      viewX[vertexIndex] = x;
      viewY[vertexIndex] = projectedY;
      viewZ[vertexIndex] = depth;
    }
    return new ProjectedVertices(new float[][]{viewX, viewY, viewZ});
  }

  private List<ProjectedFace> projectFaces(
      RawModelData rawModelData,
      ItemDefinition definition,
      float[][] viewSpaceVertices,
      NormalSet normalSet
  ) {
    ArrayList<ProjectedFace> projectedFaces = new ArrayList<>(rawModelData.faceCount());
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      FaceSurface faceSurface = resolveFaceSurface(rawModelData, definition, viewSpaceVertices, normalSet, faceIndex);
      if (faceSurface == null) {
        continue;
      }
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      float averageDepth = (viewSpaceVertices[2][vertexA] + viewSpaceVertices[2][vertexB] + viewSpaceVertices[2][vertexC]) / 3.0f;
      int facePriority = rawModelData.facePriorities().length > faceIndex ? rawModelData.facePriorities()[faceIndex] : 0;
      TextureCoordinates textureCoordinates = faceSurface.textureCoordinates();
      List<ClippedVertex> clippedVertices = ItemIconRasterizer.clipTriangleToNearPlane(
          viewSpaceVertex(
              viewSpaceVertices,
              vertexA,
              faceSurface.colors().colorA(),
              textureCoordinates.ua(),
              textureCoordinates.va(),
              faceSurface.paletteShaded()
          ),
          viewSpaceVertex(
              viewSpaceVertices,
              vertexB,
              faceSurface.colors().colorB(),
              textureCoordinates.ub(),
              textureCoordinates.vb(),
              faceSurface.paletteShaded()
          ),
          viewSpaceVertex(
              viewSpaceVertices,
              vertexC,
              faceSurface.colors().colorC(),
              textureCoordinates.uc(),
              textureCoordinates.vc(),
              faceSurface.paletteShaded()
          )
      );
      for (int clippedIndex = 1; clippedIndex < clippedVertices.size() - 1; clippedIndex++) {
        ProjectedFace projectedFace = ItemIconRasterizer.projectedFace(
            clippedVertices.get(0),
            clippedVertices.get(clippedIndex),
            clippedVertices.get(clippedIndex + 1),
            faceSurface.alpha(),
            faceSurface.texture(),
            facePriority,
            averageDepth,
            faceIndex
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
    return new FaceSurface(colors, alpha, texture, textureCoordinates, faceMode < 2);
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

  private ClippedVertex viewSpaceVertex(
      float[][] viewSpaceVertices,
      int vertexIndex,
      int color,
      float textureU,
      float textureV,
      boolean paletteShaded
  ) {
    return new ClippedVertex(
        viewSpaceVertices[0][vertexIndex],
        viewSpaceVertices[1][vertexIndex],
        viewSpaceVertices[2][vertexIndex],
        color,
        textureU,
        textureV,
        paletteShaded
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
    return shadeHsl(colorHsl, vertexBrightness(inventoryAppearance, normalSet, vertexIndex), 0);
  }

  private int shadeFlatColor(
      int colorHsl,
      int renderType,
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int faceIndex
  ) {
    return shadeHsl(colorHsl, flatBrightness(inventoryAppearance, normalSet, faceIndex), renderType);
  }

  private int shadeTexturedVertexColor(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int vertexIndex
  ) {
    return texturedShadeValue(vertexBrightness(inventoryAppearance, normalSet, vertexIndex));
  }

  private int shadeTexturedFlatColor(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      NormalSet normalSet,
      int faceIndex
  ) {
    return texturedShadeValue(flatBrightness(inventoryAppearance, normalSet, faceIndex));
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

  private int texturedShadeValue(int brightness) {
    return 127 - clamp(brightness, 0, 127);
  }

  private static int[] buildTrigTable(boolean sine) {
    int[] table = new int[2048];
    for (int index = 0; index < table.length; index++) {
      double angle = index * (Math.PI * 2.0 / table.length);
      table[index] = (int) Math.round((sine ? Math.sin(angle) : Math.cos(angle)) * TRIG_SCALE);
    }
    return table;
  }

  private int angleIndex(int angleUnits) {
    return angleUnits & 2047;
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static WidgetPreviewAppearance widgetPreviewAppearance(
      ItemDefinition.InventoryAppearance inventoryAppearance,
      int zoomScale
  ) {
    int widgetZoom = inventoryAppearance.zoom() * 100 / zoomScale;
    return new WidgetPreviewAppearance(widgetZoom, inventoryAppearance.rotationX(), inventoryAppearance.rotationY());
  }

  private record FaceSurface(
      FaceColors colors,
      int alpha,
      ArgbImage texture,
      TextureCoordinates textureCoordinates,
      boolean paletteShaded
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
      int[] x,
      int[] y,
      int[] z,
      int modelHeight
  ) {

    private float[][] axes() {
      float[] axisX = new float[x.length];
      float[] axisY = new float[y.length];
      float[] axisZ = new float[z.length];
      for (int index = 0; index < x.length; index++) {
        axisX[index] = x[index];
        axisY[index] = y[index];
        axisZ[index] = z[index];
      }
      return new float[][]{axisX, axisY, axisZ};
    }
  }

  private record ProjectedVertices(
      float[][] viewSpaceAxes
  ) {
  }

  private record WidgetPreviewAppearance(
      int zoom,
      int rotationX,
      int rotationY
  ) {
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
