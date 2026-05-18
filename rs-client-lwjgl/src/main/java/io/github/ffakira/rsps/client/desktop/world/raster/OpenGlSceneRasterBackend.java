package io.github.ffakira.rsps.client.desktop.world.raster;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public final class OpenGlSceneRasterBackend implements SceneRasterBackend, AutoCloseable {

  private final OpenGlTexture[] sceneTextures;
  private final SceneMeshFacePassPlanner facePassPlanner;
  private final SceneTextureCoordinatePlanner textureCoordinatePlanner;
  private final SceneTextureAnimationPlanner textureAnimationPlanner;
  private final long animationStartNanos;

  // The native client now has an explicit raster boundary. Terrain already uses Gouraud batches,
  // static objects preserve texture intent from cache model faces, and this backend is where that
  // intent becomes actual textured triangles instead of leaking back into scene assembly.
  public OpenGlSceneRasterBackend(SceneTextureAssets sceneTextureAssets) {
    this.sceneTextures = createTextures(sceneTextureAssets);
    this.facePassPlanner = new SceneMeshFacePassPlanner();
    this.textureCoordinatePlanner = new SceneTextureCoordinatePlanner();
    this.textureAnimationPlanner = new SceneTextureAnimationPlanner();
    this.animationStartNanos = System.nanoTime();
  }

  @Override
  public void rasterize(SceneRenderQueue renderQueue) {
    if (renderQueue == null || renderQueue.isEmpty()) {
      return;
    }
    for (SceneRenderBatch renderBatch : renderQueue.batches()) {
      drawOpaqueBatch(renderBatch);
    }
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glDepthMask(false);
    for (SceneRenderBatch renderBatch : renderQueue.batches()) {
      drawTranslucentBatch(renderBatch);
    }
    glDepthMask(true);
    glDisable(GL_BLEND);
  }

  @Override
  public void close() {
    for (OpenGlTexture texture : sceneTextures) {
      if (texture != null) {
        texture.close();
      }
    }
  }

  private void drawOpaqueBatch(SceneRenderBatch renderBatch) {
    SceneMeshFacePassPlan passPlan = facePassPlanner.plan(renderBatch.mesh());
    if (!passPlan.hasOpaqueFaces()) {
      return;
    }
    if (shouldFallbackTexturedActorBatch(renderBatch)) {
      drawGouraudMesh(renderBatch.mesh(), passPlan.opaqueFaces());
      return;
    }
    switch (renderBatch.rasterMode()) {
      case FLAT -> drawFlatMesh(renderBatch.mesh(), passPlan.opaqueFaces());
      case GOURAUD -> drawGouraudMesh(renderBatch.mesh(), passPlan.opaqueFaces());
      case TEXTURED -> drawTexturedMesh(renderBatch.mesh(), passPlan.opaqueFaces());
    }
  }

  private void drawTranslucentBatch(SceneRenderBatch renderBatch) {
    SceneMeshFacePassPlan passPlan = facePassPlanner.plan(renderBatch.mesh());
    if (!passPlan.hasTranslucentFaces()) {
      return;
    }
    if (shouldFallbackTexturedActorBatch(renderBatch)) {
      drawGouraudMesh(renderBatch.mesh(), passPlan.translucentFaces());
      return;
    }
    switch (renderBatch.rasterMode()) {
      case FLAT -> drawFlatMesh(renderBatch.mesh(), passPlan.translucentFaces());
      case GOURAUD -> drawGouraudMesh(renderBatch.mesh(), passPlan.translucentFaces());
      case TEXTURED -> drawTexturedMesh(renderBatch.mesh(), passPlan.translucentFaces());
    }
  }

  private void drawFlatMesh(SceneTriangleMesh mesh, int[] faceIndices) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    glShadeModel(GL_FLAT);
    glBegin(GL_TRIANGLES);
    for (int faceIndex : faceIndices) {
      applyColor(mesh.faceColorA()[faceIndex], mesh.faceAlpha()[faceIndex]);
      emitVertex(mesh, mesh.faceVertexA()[faceIndex]);
      emitVertex(mesh, mesh.faceVertexB()[faceIndex]);
      emitVertex(mesh, mesh.faceVertexC()[faceIndex]);
    }
    glEnd();
  }

  private void drawGouraudMesh(SceneTriangleMesh mesh, int[] faceIndices) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    glShadeModel(GL_SMOOTH);
    glBegin(GL_TRIANGLES);
    for (int faceIndex : faceIndices) {
      drawGouraudFace(mesh, faceIndex);
    }
    glEnd();
  }

  private void drawTexturedMesh(SceneTriangleMesh mesh, int[] faceIndices) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    long elapsedAnimationNanos = System.nanoTime() - animationStartNanos;
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_ALPHA_TEST);
    glAlphaFunc(GL_GREATER, 0.02f);
    glShadeModel(GL_SMOOTH);
    int boundTextureId = -1;
    boolean drawing = false;
    for (int faceIndex : faceIndices) {
      int faceTextureId = mesh.faceTextureIds()[faceIndex];
      OpenGlTexture texture = resolveTexture(faceTextureId);
      float[] uv = texture == null ? null : textureAnimationPlanner.plan(
          faceTextureId,
          texture.height(),
          textureCoordinatePlanner.plan(mesh, faceIndex),
          elapsedAnimationNanos
      );
      if (texture == null || uv == null) {
        if (drawing) {
          glEnd();
          drawing = false;
          boundTextureId = -1;
        }
        glDisable(GL_TEXTURE_2D);
        drawSingleGouraudFace(mesh, faceIndex);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        continue;
      }
      if (!drawing || texture.id() != boundTextureId) {
        if (drawing) {
          glEnd();
        }
        glBindTexture(GL_TEXTURE_2D, texture.id());
        glBegin(GL_TRIANGLES);
        drawing = true;
        boundTextureId = texture.id();
      }
      int alpha = mesh.faceAlpha()[faceIndex];
      applyColor(mesh.faceColorA()[faceIndex], alpha);
      glTexCoord2f(uv[0], uv[1]);
      emitVertex(mesh, mesh.faceVertexA()[faceIndex]);
      applyColor(mesh.faceColorB()[faceIndex], alpha);
      glTexCoord2f(uv[2], uv[3]);
      emitVertex(mesh, mesh.faceVertexB()[faceIndex]);
      applyColor(mesh.faceColorC()[faceIndex], alpha);
      glTexCoord2f(uv[4], uv[5]);
      emitVertex(mesh, mesh.faceVertexC()[faceIndex]);
    }
    if (drawing) {
      glEnd();
    }
    glBindTexture(GL_TEXTURE_2D, 0);
    glDisable(GL_ALPHA_TEST);
    glDisable(GL_TEXTURE_2D);
  }

  private void drawSingleGouraudFace(SceneTriangleMesh mesh, int faceIndex) {
    glShadeModel(GL_SMOOTH);
    glBegin(GL_TRIANGLES);
    drawGouraudFace(mesh, faceIndex);
    glEnd();
  }

  private void drawGouraudFace(SceneTriangleMesh mesh, int faceIndex) {
    int alpha = mesh.faceAlpha()[faceIndex];
    applyColor(mesh.faceColorA()[faceIndex], alpha);
    emitVertex(mesh, mesh.faceVertexA()[faceIndex]);
    applyColor(mesh.faceColorB()[faceIndex], alpha);
    emitVertex(mesh, mesh.faceVertexB()[faceIndex]);
    applyColor(mesh.faceColorC()[faceIndex], alpha);
    emitVertex(mesh, mesh.faceVertexC()[faceIndex]);
  }

  private OpenGlTexture resolveTexture(int textureId) {
    if (textureId < 0 || textureId >= sceneTextures.length) {
      return null;
    }
    return sceneTextures[textureId];
  }

  private boolean shouldFallbackTexturedActorBatch(SceneRenderBatch renderBatch) {
    return renderBatch.kind() == SceneSubmissionKind.ACTOR && renderBatch.rasterMode() == SceneRasterMode.TEXTURED;
  }

  private void applyColor(int rgb, int alpha) {
    glColor4f(
        ((rgb >>> 16) & 0xff) / 255.0f,
        ((rgb >>> 8) & 0xff) / 255.0f,
        (rgb & 0xff) / 255.0f,
        Math.max(0, Math.min(255, alpha)) / 255.0f
    );
  }

  private void emitVertex(SceneTriangleMesh mesh, int vertexIndex) {
    glVertex3f(mesh.vertexX()[vertexIndex], mesh.vertexY()[vertexIndex], mesh.vertexZ()[vertexIndex]);
  }

  private OpenGlTexture[] createTextures(SceneTextureAssets sceneTextureAssets) {
    if (sceneTextureAssets == null) {
      return new OpenGlTexture[0];
    }
    ArgbImage[] sourceTextures = sceneTextureAssets.textures();
    OpenGlTexture[] textures = new OpenGlTexture[sourceTextures.length];
    for (int textureId = 0; textureId < sourceTextures.length; textureId++) {
      ArgbImage image = sourceTextures[textureId];
      if (image != null) {
        textures[textureId] = OpenGlTexture.fromArgbImage(image);
      }
    }
    return textures;
  }
}
