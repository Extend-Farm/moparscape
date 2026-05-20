package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.AnimationSequenceDefinition;
import java.util.ArrayList;
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
    List<CharacterFrameAnimator.AnimatedContribution> animatedContributions =
        resolveAnimatedContributions(preparedModel.preparedContributions(), animationState);
    int vertexOffset = 0;
    for (int contributionIndex = 0; contributionIndex < preparedModel.preparedContributions().size(); contributionIndex++) {
      CharacterModelSourceBuilder.PreparedContribution preparedContribution =
          preparedModel.preparedContributions().get(contributionIndex);
      PreparedContributionLighting preparedLighting = preparedModel.preparedLighting().get(contributionIndex);
      RawModelData rawModelData = preparedContribution.contribution().rawModelData();
      CharacterFrameAnimator.AnimatedContribution animatedContribution =
          animatedContributions == null ? null : animatedContributions.get(contributionIndex);
      float[][] transformedVertices = applyActorTransform(
          animatedContribution == null ? preparedContribution.modelX() : animatedContribution.modelX(),
          animatedContribution == null ? preparedContribution.modelY() : animatedContribution.modelY(),
          animatedContribution == null ? preparedContribution.modelZ() : animatedContribution.modelZ(),
          preparedModel.actorTransform()
      );
      if (animatedContribution == null && !animationState.isIdle()) {
        // Procedural posing only fills the gap when no decoded cache frame is active for the
        // current movement/action state.
        CharacterWalkPoseAnimator.apply(transformedVertices, preparedModel.actorBounds(), animationState);
      }
      appendContributionVertices(vertices, rawModelData.vertexCount(), transformedVertices);
      appendContributionFaces(
          faces,
          faceColorA,
          faceColorB,
          faceColorC,
          faceAlpha,
          faceRasterModes,
          faceTextureIds,
          textureVertexA,
          textureVertexB,
          textureVertexC,
          rawModelData,
          preparedLighting,
          animatedContribution,
          vertexOffset
      );
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

  private List<CharacterFrameAnimator.AnimatedContribution> resolveAnimatedContributions(
      List<CharacterModelSourceBuilder.PreparedContribution> preparedContributions,
      ActorAnimationState animationState
  ) {
    if (frameAnimator == null || animationState == null || animationState.activeFrameId() < 0) {
      return null;
    }
    if (animationState.hasActionFrame()) {
      return frameAnimator.applyInterleaved(
          preparedContributions,
          animationState.actionFrameId(),
          animationState.movementFrameId(),
          resolveInterleaveOrder(animationState.actionSequenceId())
      );
    }
    if (animationState.hasMovementFrame()) {
      return frameAnimator.apply(preparedContributions, animationState.movementFrameId());
    }
    return null;
  }

  private int[] resolveInterleaveOrder(int actionSequenceId) {
    if (actionSequenceId < 0 || animationSequenceCatalog == null) {
      return null;
    }
    return animationSequenceCatalog.find(actionSequenceId)
        .map(AnimationSequenceDefinition::interleaveOrder)
        .orElse(null);
  }

  private float[][] applyActorTransform(int[] modelX, int[] modelY, int[] modelZ, CharacterActorTransform actorTransform) {
    float[] worldX = new float[modelX.length];
    float[] worldY = new float[modelY.length];
    float[] worldZ = new float[modelZ.length];
    for (int vertexIndex = 0; vertexIndex < worldX.length; vertexIndex++) {
      worldX[vertexIndex] = modelX[vertexIndex] / 128.0f * actorTransform.scale() + actorTransform.offsetX();
      worldY[vertexIndex] = -(modelY[vertexIndex] / 128.0f) * actorTransform.scale() + actorTransform.offsetY();
      worldZ[vertexIndex] = modelZ[vertexIndex] / 128.0f * actorTransform.scale() + actorTransform.offsetZ();
    }
    return new float[][]{worldX, worldY, worldZ};
  }

  private void appendContributionVertices(List<float[]> vertices, int vertexCount, float[][] transformedVertices) {
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      vertices.add(new float[]{
          transformedVertices[0][vertexIndex],
          transformedVertices[1][vertexIndex],
          transformedVertices[2][vertexIndex]
      });
    }
  }

  private void appendContributionFaces(
      List<int[]> faces,
      List<Integer> faceColorA,
      List<Integer> faceColorB,
      List<Integer> faceColorC,
      List<Integer> faceAlpha,
      List<SceneRasterMode> faceRasterModes,
      List<Integer> faceTextureIds,
      List<Integer> textureVertexA,
      List<Integer> textureVertexB,
      List<Integer> textureVertexC,
      RawModelData rawModelData,
      PreparedContributionLighting preparedLighting,
      CharacterFrameAnimator.AnimatedContribution animatedContribution,
      int vertexOffset
  ) {
    for (int faceIndex = 0; faceIndex < rawModelData.faceCount(); faceIndex++) {
      faces.add(new int[]{
          rawModelData.faceVertexA()[faceIndex] + vertexOffset,
          rawModelData.faceVertexB()[faceIndex] + vertexOffset,
          rawModelData.faceVertexC()[faceIndex] + vertexOffset
      });
      // The reference client lights the merged player model before any sequence deformation and
      // then reuses those lit colors while vertices animate. Re-lighting per frame changes the
      // silhouette and makes textured body parts treat texture ids like HSL colors.
      faceColorA.add(preparedLighting.faceColorA()[faceIndex]);
      faceColorB.add(preparedLighting.faceColorB()[faceIndex]);
      faceColorC.add(preparedLighting.faceColorC()[faceIndex]);
      int sourceFaceAlpha = animatedContribution == null
          ? rawModelData.faceAlpha()[faceIndex]
          : animatedContribution.faceAlpha()[faceIndex];
      faceAlpha.add(normalizeFaceAlpha(sourceFaceAlpha));
      faceRasterModes.add(preparedLighting.faceRasterModes()[faceIndex]);
      faceTextureIds.add(preparedLighting.faceTextureIds()[faceIndex]);
      textureVertexA.add(offsetTextureVertex(preparedLighting.textureVertexA()[faceIndex], vertexOffset));
      textureVertexB.add(offsetTextureVertex(preparedLighting.textureVertexB()[faceIndex], vertexOffset));
      textureVertexC.add(offsetTextureVertex(preparedLighting.textureVertexC()[faceIndex], vertexOffset));
    }
  }

  private int offsetTextureVertex(int textureVertexIndex, int vertexOffset) {
    return textureVertexIndex < 0 ? -1 : textureVertexIndex + vertexOffset;
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

  private int normalizeFaceAlpha(int rawFaceAlpha) {
    return 255 - Math.max(0, Math.min(255, rawFaceAlpha));
  }
}
