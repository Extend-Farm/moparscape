package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.AnimationSequenceDefinition;
import java.util.Arrays;
import java.util.List;

final class CharacterGeometryBuilder {

  private final AnimationSequenceCatalog animationSequenceCatalog;
  private final CharacterFrameAnimator frameAnimator;

  CharacterGeometryBuilder(AnimationSequenceCatalog animationSequenceCatalog, CharacterFrameAnimator frameAnimator) {
    this.animationSequenceCatalog = animationSequenceCatalog;
    this.frameAnimator = frameAnimator;
  }

  WorldSceneObjectGeometry build(CharacterPreparedModel preparedModel, ActorAnimationState animationState) {
    if (preparedModel == null) {
      return null;
    }
    List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions = preparedModel.preparedContributions();
    CharacterGeometryBuffer geometryBuffer =
        new CharacterGeometryBuffer(expectedVertexCount(preparedContributions), expectedFaceCount(preparedContributions));
    List<CharacterFrameAnimator.AnimatedContribution> animatedContributions =
        resolveAnimatedContributions(preparedContributions, animationState);
    for (int contributionIndex = 0; contributionIndex < preparedContributions.size(); contributionIndex++) {
      CharacterModelSourceBuilder.PreparedContribution preparedContribution = preparedContributions.get(contributionIndex);
      PreparedContributionLighting preparedLighting = preparedModel.preparedLighting().get(contributionIndex);
      RawModelData rawModelData = preparedContribution.contribution().rawModelData();
      CharacterFrameAnimator.AnimatedContribution animatedContribution = valueAt(animatedContributions, contributionIndex);
      ContributionVertices contributionVertices = transformContribution(
          preparedContribution,
          animatedContribution,
          preparedModel.actorTransform()
      );
      if (shouldApplyProceduralPose(animatedContribution, animationState)) {
        // Procedural posing only fills the gap when no decoded cache frame is active for the
        // current movement/action state.
        CharacterWalkPoseAnimator.apply(
            contributionVertices.world().x(),
            contributionVertices.world().y(),
            contributionVertices.world().z(),
            preparedModel.actorBounds(),
            animationState
        );
      }
      geometryBuffer.appendLitFaces(
          rawModelData,
          preparedLighting,
          animatedContribution,
          contributionVertices
      );
    }
    return geometryBuffer.toGeometry();
  }

  private List<CharacterFrameAnimator.AnimatedContribution> resolveAnimatedContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
      ActorAnimationState animationState
  ) {
    if (frameAnimator == null || animationState == null || animationState.activeFrameId() < 0) {
      return List.of();
    }
    if (animationState.hasActionFrame()) {
      return defaultIfNull(frameAnimator.applyInterleaved(
          preparedContributions,
          animationState.actionFrameId(),
          animationState.movementFrameId(),
          resolveInterleaveOrder(animationState.actionSequenceId())
      ));
    }
    if (animationState.hasMovementFrame()) {
      return defaultIfNull(frameAnimator.apply(preparedContributions, animationState.movementFrameId()));
    }
    return List.of();
  }

  private int[] resolveInterleaveOrder(int actionSequenceId) {
    if (actionSequenceId < 0 || animationSequenceCatalog == null) {
      return null;
    }
    return animationSequenceCatalog.find(actionSequenceId)
        .map(AnimationSequenceDefinition::interleaveOrder)
        .orElse(null);
  }

  private WorldVertices applyActorTransform(int[] modelX, int[] modelY, int[] modelZ, CharacterActorTransform actorTransform) {
    float[] worldX = new float[modelX.length];
    float[] worldY = new float[modelY.length];
    float[] worldZ = new float[modelZ.length];
    for (int vertexIndex = 0; vertexIndex < worldX.length; vertexIndex++) {
      worldX[vertexIndex] = modelX[vertexIndex] / 128.0f * actorTransform.scale() + actorTransform.offsetX();
      worldY[vertexIndex] = -(modelY[vertexIndex] / 128.0f) * actorTransform.scale() + actorTransform.offsetY();
      worldZ[vertexIndex] = modelZ[vertexIndex] / 128.0f * actorTransform.scale() + actorTransform.offsetZ();
    }
    return new WorldVertices(worldX, worldY, worldZ);
  }

  private ContributionVertices transformContribution(
      CharacterModelSourceBuilder.PreparedContribution preparedContribution,
      CharacterFrameAnimator.AnimatedContribution animatedContribution,
      CharacterActorTransform actorTransform
  ) {
    int[] animatedModelX = animatedContribution == null ? preparedContribution.modelX() : animatedContribution.modelX();
    int[] animatedModelY = animatedContribution == null ? preparedContribution.modelY() : animatedContribution.modelY();
    int[] animatedModelZ = animatedContribution == null ? preparedContribution.modelZ() : animatedContribution.modelZ();
    // The merged player topology is defined by the prepared source meshes, then animated in
    // place. Re-keying seam collapse against per-frame coordinates makes the face index layout
    // drift across animations.
    return new ContributionVertices(
        preparedContribution.modelX(),
        preparedContribution.modelY(),
        preparedContribution.modelZ(),
        applyActorTransform(animatedModelX, animatedModelY, animatedModelZ, actorTransform)
    );
  }

  private int expectedVertexCount(List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions) {
    int expectedVertexCount = 0;
    for (CharacterModelSourceBuilder.PreparedContribution preparedContribution : preparedContributions) {
      expectedVertexCount += preparedContribution.contribution().rawModelData().vertexCount();
    }
    return expectedVertexCount;
  }

  private int expectedFaceCount(List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions) {
    int expectedFaceCount = 0;
    for (CharacterModelSourceBuilder.PreparedContribution preparedContribution : preparedContributions) {
      expectedFaceCount += preparedContribution.contribution().rawModelData().faceCount();
    }
    return expectedFaceCount;
  }

  private int normalizeFaceAlpha(int rawFaceAlpha) {
    return 255 - Math.max(0, Math.min(255, rawFaceAlpha));
  }

  private boolean shouldApplyProceduralPose(
      CharacterFrameAnimator.AnimatedContribution animatedContribution,
      ActorAnimationState animationState
  ) {
    return animatedContribution == null && animationState != null && !animationState.isIdle();
  }

  private static <T> T valueAt(List<T> values, int index) {
    return index >= 0 && index < values.size() ? values.get(index) : null;
  }

  private static <T> List<T> defaultIfNull(List<T> values) {
    return values == null ? List.of() : values;
  }

  private record WorldVertices(float[] x, float[] y, float[] z) {
  }

  private record ContributionVertices(int[] modelX, int[] modelY, int[] modelZ, WorldVertices world) {
  }

  private final class CharacterGeometryBuffer {
    private final int[] sourceVertexX;
    private final int[] sourceVertexY;
    private final int[] sourceVertexZ;
    private final float[] vertexX;
    private final float[] vertexY;
    private final float[] vertexZ;
    private final int[] faceVertexA;
    private final int[] faceVertexB;
    private final int[] faceVertexC;
    private final int[] faceColorA;
    private final int[] faceColorB;
    private final int[] faceColorC;
    private final int[] faceAlpha;
    private final SceneRasterMode[] faceRasterModes;
    private final int[] faceTextureIds;
    private final int[] textureVertexA;
    private final int[] textureVertexB;
    private final int[] textureVertexC;
    private final int[] facePriorities;
    private int vertexCursor;
    private int faceCursor;

    private CharacterGeometryBuffer(int expectedVertexCount, int expectedFaceCount) {
      this.sourceVertexX = new int[expectedVertexCount];
      this.sourceVertexY = new int[expectedVertexCount];
      this.sourceVertexZ = new int[expectedVertexCount];
      this.vertexX = new float[expectedVertexCount];
      this.vertexY = new float[expectedVertexCount];
      this.vertexZ = new float[expectedVertexCount];
      this.faceVertexA = new int[expectedFaceCount];
      this.faceVertexB = new int[expectedFaceCount];
      this.faceVertexC = new int[expectedFaceCount];
      this.faceColorA = new int[expectedFaceCount];
      this.faceColorB = new int[expectedFaceCount];
      this.faceColorC = new int[expectedFaceCount];
      this.faceAlpha = new int[expectedFaceCount];
      this.faceRasterModes = new SceneRasterMode[expectedFaceCount];
      this.faceTextureIds = new int[expectedFaceCount];
      this.textureVertexA = new int[expectedFaceCount];
      this.textureVertexB = new int[expectedFaceCount];
      this.textureVertexC = new int[expectedFaceCount];
      this.facePriorities = new int[expectedFaceCount];
    }

    private void appendLitFaces(
        RawModelData rawModelData,
        PreparedContributionLighting preparedLighting,
        CharacterFrameAnimator.AnimatedContribution animatedContribution,
        ContributionVertices contributionVertices
    ) {
      for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
        // The reference client lights the merged player model before any sequence deformation and
        // then reuses those lit colors while vertices animate. Re-lighting per frame changes the
        // silhouette and makes textured body parts treat texture ids like HSL colors.
        int sourceFaceAlpha = animatedContribution == null
            ? rawModelData.faceAlpha()[faceIndex]
            : animatedContribution.faceAlpha()[faceIndex];
        faceVertexA[faceCursor] = resolveVertexIndex(contributionVertices, rawModelData.faceVertexA()[faceIndex]);
        faceVertexB[faceCursor] = resolveVertexIndex(contributionVertices, rawModelData.faceVertexB()[faceIndex]);
        faceVertexC[faceCursor] = resolveVertexIndex(contributionVertices, rawModelData.faceVertexC()[faceIndex]);
        faceColorA[faceCursor] = preparedLighting.faceColorA()[faceIndex];
        faceColorB[faceCursor] = preparedLighting.faceColorB()[faceIndex];
        faceColorC[faceCursor] = preparedLighting.faceColorC()[faceIndex];
        faceAlpha[faceCursor] = normalizeFaceAlpha(sourceFaceAlpha);
        faceRasterModes[faceCursor] = preparedLighting.faceRasterModes()[faceIndex];
        faceTextureIds[faceCursor] = preparedLighting.faceTextureIds()[faceIndex];
        textureVertexA[faceCursor] = resolveOptionalTextureVertexIndex(
            contributionVertices,
            preparedLighting.textureVertexA()[faceIndex]
        );
        textureVertexB[faceCursor] = resolveOptionalTextureVertexIndex(
            contributionVertices,
            preparedLighting.textureVertexB()[faceIndex]
        );
        textureVertexC[faceCursor] = resolveOptionalTextureVertexIndex(
            contributionVertices,
            preparedLighting.textureVertexC()[faceIndex]
        );
        facePriorities[faceCursor] = facePriority(rawModelData, faceIndex);
        faceCursor++;
      }
    }

    private int facePriority(RawModelData rawModelData, int faceIndex) {
      return faceIndex < rawModelData.facePriorities().length ? rawModelData.facePriorities()[faceIndex] : 0;
    }

    private int resolveOptionalTextureVertexIndex(ContributionVertices contributionVertices, int sourceVertexIndex) {
      return sourceVertexIndex < 0 ? -1 : resolveVertexIndex(contributionVertices, sourceVertexIndex);
    }

    private int resolveVertexIndex(ContributionVertices contributionVertices, int sourceVertexIndex) {
      int sourceX = contributionVertices.modelX()[sourceVertexIndex];
      int sourceY = contributionVertices.modelY()[sourceVertexIndex];
      int sourceZ = contributionVertices.modelZ()[sourceVertexIndex];
      for (int existingVertexIndex = 0; existingVertexIndex < vertexCursor; existingVertexIndex++) {
        if (sourceVertexX[existingVertexIndex] == sourceX
            && sourceVertexY[existingVertexIndex] == sourceY
            && sourceVertexZ[existingVertexIndex] == sourceZ) {
          return existingVertexIndex;
        }
      }
      sourceVertexX[vertexCursor] = sourceX;
      sourceVertexY[vertexCursor] = sourceY;
      sourceVertexZ[vertexCursor] = sourceZ;
      vertexX[vertexCursor] = contributionVertices.world().x()[sourceVertexIndex];
      vertexY[vertexCursor] = contributionVertices.world().y()[sourceVertexIndex];
      vertexZ[vertexCursor] = contributionVertices.world().z()[sourceVertexIndex];
      return vertexCursor++;
    }

    private WorldSceneObjectGeometry toGeometry() {
      return new WorldSceneObjectGeometry(
          Arrays.copyOf(vertexX, vertexCursor),
          Arrays.copyOf(vertexY, vertexCursor),
          Arrays.copyOf(vertexZ, vertexCursor),
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
  }
}
