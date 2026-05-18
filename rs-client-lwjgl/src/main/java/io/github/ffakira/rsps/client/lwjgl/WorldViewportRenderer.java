package io.github.ffakira.rsps.client.lwjgl;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

final class WorldViewportRenderer implements AutoCloseable {

  private final OpenGlSceneRasterBackend rasterBackend;

  WorldViewportRenderer(SceneTextureAssets sceneTextureAssets) {
    this.rasterBackend = new OpenGlSceneRasterBackend(sceneTextureAssets);
  }

  void render(ScreenRect sceneRect, int windowWidth, int windowHeight, WorldSceneRenderSubmission submission) {
    int viewportLeft = Math.round(sceneRect.left());
    int viewportTop = Math.round(sceneRect.top());
    int viewportWidth = Math.max(1, Math.round(sceneRect.width()));
    int viewportHeight = Math.max(1, Math.round(sceneRect.height()));
    int viewportBottom = windowHeight - (viewportTop + viewportHeight);

    glViewport(viewportLeft, viewportBottom, viewportWidth, viewportHeight);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glEnable(GL_MULTISAMPLE);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    float aspectRatio = viewportWidth / (float) viewportHeight;
    float nearPlane = 1.1f;
    float frustumTop = (float) (Math.tan(Math.toRadians(14.5)) * nearPlane);
    float frustumRight = frustumTop * aspectRatio;
    glFrustum(-frustumRight, frustumRight, -frustumTop, frustumTop, nearPlane, 96.0f);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    WorldCameraState cameraState = submission.cameraState();
    glTranslatef(0.0f, cameraState.screenOffsetY(), -cameraState.distance());
    glRotatef(cameraState.pitchDegrees(), 1.0f, 0.0f, 0.0f);
    glRotatef(cameraState.yawDegrees(), 0.0f, 1.0f, 0.0f);
    glTranslatef(-cameraState.focusX(), -cameraState.focusHeight(), -cameraState.focusY());

    // The native renderer is not yet at legacy triangle-submission parity. Until terrain, tile
    // models, and object meshes all guarantee consistent winding like the old software rasterizer,
    // OpenGL backface culling creates obvious holes in the scene. Depth stays enabled, but face
    // culling remains off for correctness.
    rasterBackend.rasterize(submission.renderQueue());

    glDisable(GL_DEPTH_TEST);
    glViewport(0, 0, windowWidth, windowHeight);
  }

  @Override
  public void close() {
    rasterBackend.close();
  }
}
