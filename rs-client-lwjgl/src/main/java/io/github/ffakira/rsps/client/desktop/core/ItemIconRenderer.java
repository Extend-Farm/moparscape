package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTextureAssets;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ItemIconRenderer {

  private static final int ICON_SIZE = 32;
  private static final float ICON_CENTER = ICON_SIZE * 0.5f;
  private static final float INVENTORY_PROJECTION_SCALE = 512.0f;
  private static final float NEAR_PLANE = 1.0f;
  private static final int LIGHT_VECTOR_X = -50;
  private static final int LIGHT_VECTOR_Y = -10;
  private static final int LIGHT_VECTOR_Z = -50;
  private static final int LIGHT_VECTOR_LENGTH = 71;
  private static final double HSL_PALETTE_GAMMA = 0.8D;
  private static final int OUTLINE_RGB = 0x302020;
  private static final SceneTextureAssets EMPTY_TEXTURE_ASSETS = new SceneTextureAssets(new ArgbImage[0]);
  private static final int[] SHADED_HSL_PALETTE = buildShadedHslPalette(HSL_PALETTE_GAMMA);

  private final ItemDefinitionCatalog itemDefinitions;
  private final RawModelRepository rawModelRepository;
  private final SceneTextureAssets sceneTextureAssets;
  private final Map<Integer, ArgbImage> iconsByRenderableItemId = new HashMap<>();

  ItemIconRenderer(ItemDefinitionCatalog itemDefinitions, RawModelRepository rawModelRepository) {
    this(itemDefinitions, rawModelRepository, null);
  }

  ItemIconRenderer(
      ItemDefinitionCatalog itemDefinitions,
      RawModelRepository rawModelRepository,
      SceneTextureAssets sceneTextureAssets
  ) {
    this.itemDefinitions = itemDefinitions;
    this.rawModelRepository = rawModelRepository;
    this.sceneTextureAssets = sceneTextureAssets == null ? EMPTY_TEXTURE_ASSETS : sceneTextureAssets;
  }

  int iconKey(int itemId, int quantity) {
    return resolveRenderableItemId(itemId, quantity);
  }

  ArgbImage render(int itemId, int quantity) {
    int renderableItemId = resolveRenderableItemId(itemId, quantity);
    if (renderableItemId < 0) {
      return null;
    }
    return iconsByRenderableItemId.computeIfAbsent(renderableItemId, this::renderResolvedItemIcon);
  }

  private int resolveRenderableItemId(int itemId, int quantity) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null) {
      return -1;
    }
    for (int step = 0; step < 4; step++) {
      ItemDefinition stackVariant = stackVariant(definition, quantity);
      if (stackVariant != null && stackVariant.id() != definition.id()) {
        definition = stackVariant;
        continue;
      }
      if (definition.noted() && definition.noteLinkItemId() >= 0) {
        // Native inventory icons do not have the old paper-note composite yet. Until widget/sprite
        // decoding is ported, render noted items through their linked physical item model instead.
        ItemDefinition linkedDefinition = itemDefinitions.find(definition.noteLinkItemId()).orElse(null);
        if (linkedDefinition != null && linkedDefinition.id() != definition.id()) {
          definition = linkedDefinition;
          continue;
        }
      }
      break;
    }
    return definition.inventoryAppearance().modelId() >= 0 ? definition.id() : -1;
  }

  private ItemDefinition stackVariant(ItemDefinition definition, int quantity) {
    if (quantity <= 1) {
      return null;
    }
    ItemDefinition.StackVariant selectedVariant = null;
    for (ItemDefinition.StackVariant stackVariant : definition.stackVariants()) {
      if (quantity >= stackVariant.minimumQuantity()
          && (selectedVariant == null || stackVariant.minimumQuantity() >= selectedVariant.minimumQuantity())) {
        selectedVariant = stackVariant;
      }
    }
    if (selectedVariant == null) {
      return null;
    }
    return itemDefinitions.find(selectedVariant.itemId()).orElse(null);
  }

  private ArgbImage renderResolvedItemIcon(int itemId) {
    ItemDefinition definition = itemDefinitions.find(itemId).orElse(null);
    if (definition == null || definition.inventoryAppearance().modelId() < 0) {
      return null;
    }
    RawModelData rawModelData = rawModelRepository.loadModel(definition.inventoryAppearance().modelId());
    if (rawModelData == null || rawModelData.faceCount() == 0) {
      return null;
    }

    PreparedInventoryModel preparedInventoryModel = prepareInventoryModel(rawModelData, definition.inventoryAppearance());
    ProjectedVertices projectedVertices = projectVertices(preparedInventoryModel, definition.inventoryAppearance());
    NormalSet normalSet = computeNormals(rawModelData, preparedInventoryModel.axes());
    List<ProjectedFace> projectedFaces = projectFaces(
        rawModelData,
        definition,
        projectedVertices.viewSpaceAxes(),
        normalSet
    );
    if (projectedFaces.isEmpty()) {
      return null;
    }
    return rasterize(projectedFaces);
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

  private List<ProjectedFace> projectFaces(
      RawModelData rawModelData,
      ItemDefinition definition,
      float[][] viewSpaceVertices,
      NormalSet normalSet
  ) {
    ArrayList<ProjectedFace> projectedFaces = new ArrayList<>(rawModelData.faceCount());
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      int alpha = normalizeFaceAlpha(rawModelData.faceAlpha()[faceIndex]);
      if (alpha <= 0) {
        continue;
      }
      int vertexA = rawModelData.faceVertexA()[faceIndex];
      int vertexB = rawModelData.faceVertexB()[faceIndex];
      int vertexC = rawModelData.faceVertexC()[faceIndex];
      int faceMode = rawModelData.faceRenderTypes()[faceIndex] & 3;
      int sourceColorHsl = rawModelData.faceColorHsl()[faceIndex];
      int colorA;
      int colorB;
      int colorC;
      ArgbImage texture = null;
      float textureUa = 0.0f;
      float textureVa = 0.0f;
      float textureUb = 0.0f;
      float textureVb = 0.0f;
      float textureUc = 0.0f;
      float textureVc = 0.0f;
      switch (faceMode) {
        case 0 -> {
          int recoloredHsl = recolor(sourceColorHsl, definition);
          colorA = shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexA);
          colorB = shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexB);
          colorC = shadeVertexColor(recoloredHsl, definition.inventoryAppearance(), normalSet, vertexC);
        }
        case 1 -> {
          int recoloredHsl = recolor(sourceColorHsl, definition);
          int shadedFaceColor = shadeFlatColor(
              recoloredHsl,
              rawModelData.faceRenderTypes()[faceIndex],
              definition.inventoryAppearance(),
              normalSet,
              faceIndex
          );
          colorA = shadedFaceColor;
          colorB = shadedFaceColor;
          colorC = shadedFaceColor;
        }
        case 2 -> {
          colorA = shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexA);
          colorB = shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexB);
          colorC = shadeTexturedVertexColor(definition.inventoryAppearance(), normalSet, vertexC);
          texture = resolveTexture(sourceColorHsl);
        }
        default -> {
          int shadedFaceColor = shadeTexturedFlatColor(definition.inventoryAppearance(), normalSet, faceIndex);
          colorA = shadedFaceColor;
          colorB = shadedFaceColor;
          colorC = shadedFaceColor;
          texture = resolveTexture(sourceColorHsl);
        }
      }
      if (texture != null) {
        float[] uv = textureCoordinates(rawModelData, viewSpaceVertices, faceIndex);
        if (uv != null) {
          textureUa = uv[0];
          textureVa = uv[1];
          textureUb = uv[2];
          textureVb = uv[3];
          textureUc = uv[4];
          textureVc = uv[5];
        } else {
          texture = null;
        }
      }
      List<ClippedVertex> clippedVertices = clipTriangleToNearPlane(
          viewSpaceVertex(viewSpaceVertices, vertexA, colorA, textureUa, textureVa),
          viewSpaceVertex(viewSpaceVertices, vertexB, colorB, textureUb, textureVb),
          viewSpaceVertex(viewSpaceVertices, vertexC, colorC, textureUc, textureVc)
      );
      for (int clippedIndex = 1; clippedIndex < clippedVertices.size() - 1; clippedIndex++) {
        ProjectedFace projectedFace = projectedFace(
            clippedVertices.get(0),
            clippedVertices.get(clippedIndex),
            clippedVertices.get(clippedIndex + 1),
            alpha,
            texture
        );
        if (projectedFace != null) {
          projectedFaces.add(projectedFace);
        }
      }
    }
    return projectedFaces;
  }

  static List<ClippedVertex> clipTriangleToNearPlane(ClippedVertex vertexA, ClippedVertex vertexB, ClippedVertex vertexC) {
    ArrayList<ClippedVertex> inputVertices = new ArrayList<>(List.of(vertexA, vertexB, vertexC));
    ArrayList<ClippedVertex> clippedVertices = new ArrayList<>(4);
    ClippedVertex previousVertex = inputVertices.get(inputVertices.size() - 1);
    boolean previousInside = previousVertex.depth() > NEAR_PLANE;
    for (ClippedVertex currentVertex : inputVertices) {
      boolean currentInside = currentVertex.depth() > NEAR_PLANE;
      if (currentInside != previousInside) {
        clippedVertices.add(interpolateNearPlane(previousVertex, currentVertex));
      }
      if (currentInside) {
        clippedVertices.add(currentVertex);
      }
      previousVertex = currentVertex;
      previousInside = currentInside;
    }
    return clippedVertices;
  }

  private static ClippedVertex interpolateNearPlane(ClippedVertex fromVertex, ClippedVertex toVertex) {
    float depthDelta = toVertex.depth() - fromVertex.depth();
    if (Math.abs(depthDelta) < 0.00001f) {
      return new ClippedVertex(
          fromVertex.viewX(),
          fromVertex.viewY(),
          NEAR_PLANE,
          fromVertex.color(),
          fromVertex.textureU(),
          fromVertex.textureV()
      );
    }
    float interpolation = (NEAR_PLANE - fromVertex.depth()) / depthDelta;
    return new ClippedVertex(
        lerp(fromVertex.viewX(), toVertex.viewX(), interpolation),
        lerp(fromVertex.viewY(), toVertex.viewY(), interpolation),
        NEAR_PLANE,
        interpolateColor(fromVertex.color(), toVertex.color(), interpolation),
        lerp(fromVertex.textureU(), toVertex.textureU(), interpolation),
        lerp(fromVertex.textureV(), toVertex.textureV(), interpolation)
    );
  }

  private ClippedVertex viewSpaceVertex(
      float[][] viewSpaceVertices,
      int vertexIndex,
      int color,
      float textureU,
      float textureV
  ) {
    return new ClippedVertex(
        viewSpaceVertices[0][vertexIndex],
        viewSpaceVertices[1][vertexIndex],
        viewSpaceVertices[2][vertexIndex],
        color,
        textureU,
        textureV
    );
  }

  private ProjectedFace projectedFace(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC,
      int alpha,
      ArgbImage texture
  ) {
    if (vertexA.depth() <= NEAR_PLANE || vertexB.depth() <= NEAR_PLANE || vertexC.depth() <= NEAR_PLANE) {
      return null;
    }
    float ax = projectScreenX(vertexA.viewX(), vertexA.depth());
    float ay = projectScreenY(vertexA.viewY(), vertexA.depth());
    float bx = projectScreenX(vertexB.viewX(), vertexB.depth());
    float by = projectScreenY(vertexB.viewY(), vertexB.depth());
    float cx = projectScreenX(vertexC.viewX(), vertexC.depth());
    float cy = projectScreenY(vertexC.viewY(), vertexC.depth());
    float faceArea = signedArea(ax, ay, bx, by, cx, cy);
    if (Math.abs(faceArea) < 0.01f) {
      return null;
    }
    return new ProjectedFace(
        ax,
        ay,
        vertexA.depth(),
        bx,
        by,
        vertexB.depth(),
        cx,
        cy,
        vertexC.depth(),
        vertexA.color(),
        vertexB.color(),
        vertexC.color(),
        alpha,
        texture,
        vertexA.textureU(),
        vertexA.textureV(),
        vertexB.textureU(),
        vertexB.textureV(),
        vertexC.textureU(),
        vertexC.textureV()
    );
  }

  private float projectScreenX(float viewX, float depth) {
    return ICON_CENTER + viewX * INVENTORY_PROJECTION_SCALE / depth;
  }

  private float projectScreenY(float viewY, float depth) {
    return ICON_CENTER + viewY * INVENTORY_PROJECTION_SCALE / depth;
  }

  private ArgbImage rasterize(List<ProjectedFace> projectedFaces) {
    int[] pixels = new int[ICON_SIZE * ICON_SIZE];
    float[] depthBuffer = new float[ICON_SIZE * ICON_SIZE];
    Arrays.fill(depthBuffer, Float.POSITIVE_INFINITY);
    for (ProjectedFace face : projectedFaces) {
      rasterizeFace(face, pixels, depthBuffer);
    }
    applyOutline(pixels);
    return new ArgbImage(ICON_SIZE, ICON_SIZE, pixels);
  }

  private void rasterizeFace(ProjectedFace face, int[] pixels, float[] depthBuffer) {
    int minX = clamp((int) Math.floor(Math.min(face.ax(), Math.min(face.bx(), face.cx()))), 0, ICON_SIZE - 1);
    int maxX = clamp((int) Math.ceil(Math.max(face.ax(), Math.max(face.bx(), face.cx()))), 0, ICON_SIZE - 1);
    int minY = clamp((int) Math.floor(Math.min(face.ay(), Math.min(face.by(), face.cy()))), 0, ICON_SIZE - 1);
    int maxY = clamp((int) Math.ceil(Math.max(face.ay(), Math.max(face.by(), face.cy()))), 0, ICON_SIZE - 1);
    float area = signedArea(face.ax(), face.ay(), face.bx(), face.by(), face.cx(), face.cy());
    if (Math.abs(area) < 0.0001f) {
      return;
    }
    for (int pixelY = minY; pixelY <= maxY; pixelY++) {
      for (int pixelX = minX; pixelX <= maxX; pixelX++) {
        float sampleX = pixelX + 0.5f;
        float sampleY = pixelY + 0.5f;
        float weightA = signedArea(sampleX, sampleY, face.bx(), face.by(), face.cx(), face.cy()) / area;
        float weightB = signedArea(face.ax(), face.ay(), sampleX, sampleY, face.cx(), face.cy()) / area;
        float weightC = 1.0f - weightA - weightB;
        if (weightA < -0.001f || weightB < -0.001f || weightC < -0.001f) {
          continue;
        }
        float depth = face.az() * weightA + face.bz() * weightB + face.cz() * weightC;
        int pixelIndex = pixelY * ICON_SIZE + pixelX;
        if (depth >= depthBuffer[pixelIndex]) {
          continue;
        }
        depthBuffer[pixelIndex] = depth;
        float denominator = perspectiveInterpolationDenominator(face, weightA, weightB, weightC);
        float shadedRed = interpolateColorChannel(face, 16, weightA, weightB, weightC);
        float shadedGreen = interpolateColorChannel(face, 8, weightA, weightB, weightC);
        float shadedBlue = interpolateColorChannel(face, 0, weightA, weightB, weightC);
        int sourceAlpha = face.alpha();
        int red;
        int green;
        int blue;
        if (face.texture() != null) {
          float textureU = perspectiveCorrectInterpolate(
              face.textureUa(),
              face.textureUb(),
              face.textureUc(),
              face,
              weightA,
              weightB,
              weightC,
              denominator
          );
          float textureV = perspectiveCorrectInterpolate(
              face.textureVa(),
              face.textureVb(),
              face.textureVc(),
              face,
              weightA,
              weightB,
              weightC,
              denominator
          );
          int textureArgb = sampleTexture(face.texture(), textureU, textureV);
          int textureAlpha = (textureArgb >>> 24) & 0xff;
          if (textureAlpha <= 0) {
            continue;
          }
          red = modulate((textureArgb >>> 16) & 0xff, shadedRed);
          green = modulate((textureArgb >>> 8) & 0xff, shadedGreen);
          blue = modulate(textureArgb & 0xff, shadedBlue);
          sourceAlpha = sourceAlpha * textureAlpha / 255;
        } else {
          red = clamp(Math.round(shadedRed), 0, 255);
          green = clamp(Math.round(shadedGreen), 0, 255);
          blue = clamp(Math.round(shadedBlue), 0, 255);
        }
        if (sourceAlpha <= 0) {
          continue;
        }
        int argb = 0xff000000 | (red << 16) | (green << 8) | blue;
        pixels[pixelIndex] = sourceAlpha >= 255
            ? argb
            : blend(pixels[pixelIndex], argb, sourceAlpha);
      }
    }
  }

  private void applyOutline(int[] pixels) {
    int[] outlinedPixels = pixels.clone();
    for (int pixelY = 1; pixelY < ICON_SIZE - 1; pixelY++) {
      for (int pixelX = 1; pixelX < ICON_SIZE - 1; pixelX++) {
        int pixelIndex = pixelY * ICON_SIZE + pixelX;
        if (pixels[pixelIndex] != 0) {
          continue;
        }
        if (pixels[pixelIndex - 1] != 0
            || pixels[pixelIndex + 1] != 0
            || pixels[pixelIndex - ICON_SIZE] != 0
            || pixels[pixelIndex + ICON_SIZE] != 0) {
          outlinedPixels[pixelIndex] = 0xff000000 | OUTLINE_RGB;
        }
      }
    }
    System.arraycopy(outlinedPixels, 0, pixels, 0, pixels.length);
  }

  private ArgbImage resolveTexture(int textureId) {
    return sceneTextureAssets.texture(textureId);
  }

  private float[] textureCoordinates(RawModelData rawModelData, float[][] transformedVertices, int faceIndex) {
    int textureIndex = rawModelData.faceRenderTypes()[faceIndex] >> 2;
    if (textureIndex < 0 || textureIndex >= rawModelData.texturedFaceCount()) {
      return planarTextureCoordinates(rawModelData, transformedVertices, faceIndex);
    }
    float[] anchorA = vertex(transformedVertices, rawModelData.texturedFaceVertexA()[textureIndex]);
    float[] anchorB = vertex(transformedVertices, rawModelData.texturedFaceVertexB()[textureIndex]);
    float[] anchorC = vertex(transformedVertices, rawModelData.texturedFaceVertexC()[textureIndex]);
    float[] uvA = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexA()[faceIndex]));
    float[] uvB = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexB()[faceIndex]));
    float[] uvC = anchoredUv(anchorA, anchorB, anchorC, vertex(transformedVertices, rawModelData.faceVertexC()[faceIndex]));
    if (uvA == null || uvB == null || uvC == null) {
      return planarTextureCoordinates(rawModelData, transformedVertices, faceIndex);
    }
    return new float[]{uvA[0], uvA[1], uvB[0], uvB[1], uvC[0], uvC[1]};
  }

  private float[] anchoredUv(float[] anchorA, float[] anchorB, float[] anchorC, float[] point) {
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
    return new float[]{u, v};
  }

  private float[] planarTextureCoordinates(RawModelData rawModelData, float[][] transformedVertices, int faceIndex) {
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
    float[] uvA = projectedUv(vertexA, projectionAxis, normalX, normalY, normalZ);
    float[] uvB = projectedUv(vertexB, projectionAxis, normalX, normalY, normalZ);
    float[] uvC = projectedUv(vertexC, projectionAxis, normalX, normalY, normalZ);
    return new float[]{uvA[0], uvA[1], uvB[0], uvB[1], uvC[0], uvC[1]};
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

  private float[] projectedUv(
      float[] vertex,
      ProjectionAxis projectionAxis,
      float normalX,
      float normalY,
      float normalZ
  ) {
    return switch (projectionAxis) {
      case X -> new float[]{
          normalX >= 0.0f ? vertex[2] : -vertex[2],
          vertex[1]
      };
      case Y -> new float[]{
          vertex[0],
          normalY >= 0.0f ? vertex[2] : -vertex[2]
      };
      case Z -> new float[]{
          normalZ >= 0.0f ? vertex[0] : -vertex[0],
          vertex[1]
      };
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

  private float perspectiveInterpolationDenominator(ProjectedFace face, float weightA, float weightB, float weightC) {
    return weightA / face.az() + weightB / face.bz() + weightC / face.cz();
  }

  private float perspectiveCorrectInterpolate(
      float valueA,
      float valueB,
      float valueC,
      ProjectedFace face,
      float weightA,
      float weightB,
      float weightC,
      float denominator
  ) {
    if (Math.abs(denominator) < 0.00001f) {
      return valueA * weightA + valueB * weightB + valueC * weightC;
    }
    return ((valueA * weightA) / face.az() + (valueB * weightB) / face.bz() + (valueC * weightC) / face.cz())
        / denominator;
  }

  private float interpolateColorChannel(ProjectedFace face, int shift, float weightA, float weightB, float weightC) {
    return ((face.colorA() >>> shift) & 0xff) * weightA
        + ((face.colorB() >>> shift) & 0xff) * weightB
        + ((face.colorC() >>> shift) & 0xff) * weightC;
  }

  private int sampleTexture(ArgbImage texture, float u, float v) {
    float clampedU = clamp(u, 0.0f, 0.9999f);
    float clampedV = clamp(v, 0.0f, 0.9999f);
    int sampleX = clamp((int) (clampedU * texture.width()), 0, texture.width() - 1);
    int sampleY = clamp((int) (clampedV * texture.height()), 0, texture.height() - 1);
    return texture.pixels()[sampleY * texture.width() + sampleX];
  }

  private int modulate(int textureChannel, float lightChannel) {
    return clamp(Math.round(textureChannel * clamp(lightChannel, 0.0f, 255.0f) / 255.0f), 0, 255);
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

  private int blend(int destinationArgb, int sourceArgb, int sourceAlpha) {
    int inverseAlpha = 255 - sourceAlpha;
    int destinationRed = (destinationArgb >>> 16) & 0xff;
    int destinationGreen = (destinationArgb >>> 8) & 0xff;
    int destinationBlue = destinationArgb & 0xff;
    int sourceRed = (sourceArgb >>> 16) & 0xff;
    int sourceGreen = (sourceArgb >>> 8) & 0xff;
    int sourceBlue = sourceArgb & 0xff;
    int red = (sourceRed * sourceAlpha + destinationRed * inverseAlpha) / 255;
    int green = (sourceGreen * sourceAlpha + destinationGreen * inverseAlpha) / 255;
    int blue = (sourceBlue * sourceAlpha + destinationBlue * inverseAlpha) / 255;
    return 0xff000000 | (red << 16) | (green << 8) | blue;
  }

  private float signedArea(float ax, float ay, float bx, float by, float cx, float cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }

  private static float lerp(float from, float to, float interpolation) {
    return from + (to - from) * interpolation;
  }

  private static int interpolateColor(int fromColor, int toColor, float interpolation) {
    int red = Math.round(lerp((fromColor >>> 16) & 0xff, (toColor >>> 16) & 0xff, interpolation));
    int green = Math.round(lerp((fromColor >>> 8) & 0xff, (toColor >>> 8) & 0xff, interpolation));
    int blue = Math.round(lerp(fromColor & 0xff, toColor & 0xff, interpolation));
    return (red << 16) | (green << 8) | blue;
  }

  private float angleRadians(int angleUnits) {
    return (float) (angleUnits * (Math.PI * 2.0 / 2048.0));
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private record ProjectedFace(
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float cx,
      float cy,
      float cz,
      int colorA,
      int colorB,
      int colorC,
      int alpha,
      ArgbImage texture,
      float textureUa,
      float textureVa,
      float textureUb,
      float textureVb,
      float textureUc,
      float textureVc
  ) {
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

  static record ClippedVertex(
      float viewX,
      float viewY,
      float depth,
      int color,
      float textureU,
      float textureV
  ) {
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
