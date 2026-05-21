package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Diagnostic harness for the world-coordinate / view-transform parity bug captured in
 * {@code .codex/plan/world-frame-parity.md}. This is intentionally read-only: it does not exercise
 * any production code path that would be affected by a fix. Instead it replicates the exact matrix
 * sequence used by {@link WorldViewportRenderer} (glTranslate / glRotatef in MODELVIEW order) and
 * traces a single probe vertex through every stage so the actual numbers can be inspected.
 *
 * <p>Each test pins the current (buggy) value with an assertion, plus a comment describing what the
 * legacy-faithful value would be. Once the world-frame refactor lands, the legacy comments become
 * the assertions and the current-value assertions are deleted.
 *
 * <p>Probe scenario: player stands on scene-local tile {@code (52, 52)} (the center of a 104x104
 * scene), camera at the default {@code yaw=0, pitch=22.5°}. We trace the corner of the tile directly
 * north of the player — scene-local {@code (52, 53)}, mesh-world {@code (52, 0, 53)} — through the
 * view transform and assert what camera-space coordinates result.
 */
class WorldFrameParityProbeTest {

  // Camera state mirrors the defaults from WorldSceneCameraPlanner so the probe matches what the
  // renderer would actually see at game launch.
  private static final float DEFAULT_PITCH_DEGREES = 22.5f;
  private static final float DEFAULT_YAW_DEGREES = 0.0f;
  private static final float DEFAULT_DISTANCE = WorldSceneCameraPlanner.orbitDistanceForPitchUnits(
      clampedPitchUnits(DEFAULT_PITCH_DEGREES)
  );
  private static final float DEFAULT_SCREEN_OFFSET_Y = 0.0f;
  private static final float PLAYER_LOCAL_X = 52.5f;
  private static final float PLAYER_LOCAL_Y = 52.5f;
  private static final float PLAYER_FOCUS_HEIGHT = 0.0f;

  private static final WorldCameraState DEFAULT_CAMERA = new WorldCameraState(
      DEFAULT_PITCH_DEGREES,
      DEFAULT_YAW_DEGREES,
      DEFAULT_DISTANCE,
      DEFAULT_SCREEN_OFFSET_Y,
      PLAYER_LOCAL_X,
      PLAYER_LOCAL_Y,
      PLAYER_FOCUS_HEIGHT
  );

  @Test
  void probe_northOfPlayer_endsUpBehindCameraInTheCurrentBuild() {
    // A tile one scene-local tile north of the player. In OSRS coords +Y is north, and the loader
    // preserves that mapping (localY = worldY - originY), so localY+1 is north. The mesh builder
    // places this vertex at world (52, 0, 53) — i.e. +Z direction relative to the player.
    float[] meshVertex = {52.0f, 0.0f, 53.0f};

    float[] cameraSpaceVertex = applyCurrentRendererModelview(meshVertex, DEFAULT_CAMERA);

    // CURRENT (buggy): the northern tile ends up at +Z direction in mesh, the renderer puts the
    // camera at -distance in +Z direction relative to focus, and the camera looks toward -Z. So a
    // tile at +Z direction = behind the camera. After the view transform, camera-space Z is
    // *greater than* the focus Z (which is at -distance). With distance ~10, focus is at z=-10
    // and a tile 1 unit north (+Z direction) ends up roughly at z=-9, i.e. CLOSER to the camera /
    // less in front. The pitch rotates that toward -Y_camera so it renders BELOW the player on
    // screen. In other words: north-of-player appears south-on-screen.
    assertThat(cameraSpaceVertex[2])
        .as("camera-space Z of north-of-player vertex (current build)")
        .isGreaterThan(-DEFAULT_DISTANCE);

    // LEGACY-FAITHFUL target: north-of-player should appear FURTHER from camera (more negative Z
    // in OpenGL camera space) than the player, so it renders ABOVE the player on screen. The
    // value should be roughly z = -(distance + cos(pitch)).
    // assertThat(cameraSpaceVertex[2]).isLessThan(-DEFAULT_DISTANCE);  // future correctness check
  }

  @Test
  void probe_eastOfPlayer_endsUpToTheRightInTheCurrentBuild() {
    // A tile one scene-local tile east of the player. localX maps directly to mesh-world +X, and
    // OpenGL camera-space +X is screen-right when yaw=0, so east-of-player should land at +X in
    // camera space.
    float[] meshVertex = {53.0f, 0.0f, 52.0f};

    float[] cameraSpaceVertex = applyCurrentRendererModelview(meshVertex, DEFAULT_CAMERA);

    // Current build: east-of-player vertex at camera-space +X ≈ 1.0, so it renders to the right of
    // the player on screen. This matches legacy. The east/west axis itself is fine at yaw=0.
    assertThat(cameraSpaceVertex[0])
        .as("camera-space X of east-of-player vertex")
        .isGreaterThan(0.0f);
  }

  @Test
  void prints_fullProbeTable_forCurrentRenderer() {
    System.out.println();
    System.out.println("=== WorldFrameParityProbe — current renderer (pre-fix) ===");
    System.out.printf("Camera: yaw=%.2f° pitch=%.2f° distance=%.3f screenOffsetY=%.3f focus=(%.2f, %.2f, %.2f)%n",
        DEFAULT_CAMERA.yawDegrees(), DEFAULT_CAMERA.pitchDegrees(), DEFAULT_CAMERA.distance(),
        DEFAULT_CAMERA.screenOffsetY(), DEFAULT_CAMERA.focusX(), DEFAULT_CAMERA.focusHeight(), DEFAULT_CAMERA.focusY());
    printProbe("focus (player)        ", new float[]{PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y});
    printProbe("north of player (+Z+1)", new float[]{PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y + 1.0f});
    printProbe("south of player (+Z-1)", new float[]{PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y - 1.0f});
    printProbe("east of player  (+X+1)", new float[]{PLAYER_LOCAL_X + 1.0f, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y});
    printProbe("west of player  (+X-1)", new float[]{PLAYER_LOCAL_X - 1.0f, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y});
    System.out.println();
    System.out.println("Interpretation:");
    System.out.println("  +cameraZ = behind camera (OpenGL looks -Z forward), -cameraZ = in front");
    System.out.println("  +cameraY = up on screen,                          -cameraY = down on screen");
    System.out.println("  +cameraX = right on screen");
    System.out.println();
    System.out.println("Expected for legacy parity (camera looks NORTH at yaw=0):");
    System.out.println("  north-of-player should have cameraZ < focus cameraZ  (further in front)");
    System.out.println("  south-of-player should have cameraZ > focus cameraZ  (closer / between cam and focus)");
  }

  private static void printProbe(String label, float[] meshVertex) {
    float[] cs = applyCurrentRendererModelview(meshVertex, DEFAULT_CAMERA);
    System.out.printf("  %s  mesh=(%5.1f, %5.1f, %5.1f)  →  camera=(%+7.3f, %+7.3f, %+8.3f)%n",
        label, meshVertex[0], meshVertex[1], meshVertex[2], cs[0], cs[1], cs[2]);
  }

  @Test
  void prints_fullProbeTable_forProposedFix() {
    System.out.println();
    System.out.println("=== WorldFrameParityProbe — proposed fix (negate mesh Z, flip focus Z translate) ===");
    System.out.printf("Camera: yaw=%.2f° pitch=%.2f° distance=%.3f screenOffsetY=%.3f focus=(%.2f, %.2f, %.2f)%n",
        DEFAULT_CAMERA.yawDegrees(), DEFAULT_CAMERA.pitchDegrees(), DEFAULT_CAMERA.distance(),
        DEFAULT_CAMERA.screenOffsetY(), DEFAULT_CAMERA.focusX(), DEFAULT_CAMERA.focusHeight(), DEFAULT_CAMERA.focusY());
    printProbeProposed("focus (player)        ", PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y);
    printProbeProposed("north of player (+Y+1)", PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y + 1.0f);
    printProbeProposed("south of player (+Y-1)", PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y - 1.0f);
    printProbeProposed("east of player  (+X+1)", PLAYER_LOCAL_X + 1.0f, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y);
    printProbeProposed("west of player  (+X-1)", PLAYER_LOCAL_X - 1.0f, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y);
    System.out.println();
    System.out.println("After fix, north-of-player should land at cameraY > focus cameraY (above on screen)");
    System.out.println("and cameraZ < focus cameraZ (further from camera).");
  }

  // Proposed fix:
  //   - mesh emission negates Z so +localY = -meshZ (north → -Z, lined up with OpenGL -Z forward)
  //   - view transform's focus translate uses +focusY instead of -focusY to compensate
  //   - rest of camera math unchanged (yaw/pitch/distance still apply the same way)
  private static void printProbeProposed(String label, float localX, float height, float localY) {
    // Mesh emission with Z negated:
    float meshX = localX;
    float meshY = height;
    float meshZ = -localY;

    // View transform with +focusY (instead of -focusY):
    float vx = meshX - DEFAULT_CAMERA.focusX();
    float vy = meshY - DEFAULT_CAMERA.focusHeight();
    float vz = meshZ + DEFAULT_CAMERA.focusY();  // <-- sign flipped here

    double yawRadians = Math.toRadians(-DEFAULT_CAMERA.yawDegrees());
    double cosYaw = Math.cos(yawRadians);
    double sinYaw = Math.sin(yawRadians);
    double rxYaw = vx * cosYaw + vz * sinYaw;
    double rzYaw = -vx * sinYaw + vz * cosYaw;
    vx = (float) rxYaw;
    vz = (float) rzYaw;

    double pitchRadians = Math.toRadians(DEFAULT_CAMERA.pitchDegrees());
    double cosPitch = Math.cos(pitchRadians);
    double sinPitch = Math.sin(pitchRadians);
    double ryPitch = vy * cosPitch - vz * sinPitch;
    double rzPitch = vy * sinPitch + vz * cosPitch;
    vy = (float) ryPitch;
    vz = (float) rzPitch;

    vy += DEFAULT_CAMERA.screenOffsetY();
    vz += -DEFAULT_CAMERA.distance();

    System.out.printf("  %s  localOSRS=(%5.1f, %5.1f)  →  mesh=(%5.1f, %5.1f, %5.1f)  →  camera=(%+7.3f, %+7.3f, %+8.3f)%n",
        label, localX, localY, meshX, meshY, meshZ, vx, vy, vz);
  }

  @Test
  void probe_focusVertex_lands_atOpticalCenterMinusDistance() {
    // The focus point itself (player position). After the modelview it should land at
    // (0, screenOffsetY, -distance) — that's the standard OpenGL "focus is at -distance in front
    // of camera" setup. This test confirms the camera placement math itself is structurally
    // correct; the parity bug is about which world direction +Z corresponds to, not about how the
    // camera sits relative to focus.
    float[] focusVertex = {PLAYER_LOCAL_X, PLAYER_FOCUS_HEIGHT, PLAYER_LOCAL_Y};

    float[] cameraSpaceVertex = applyCurrentRendererModelview(focusVertex, DEFAULT_CAMERA);

    assertThat(cameraSpaceVertex[0]).as("focus camera-space X").isCloseTo(0.0f, offset(1.0e-4f));
    assertThat(cameraSpaceVertex[1]).as("focus camera-space Y").isCloseTo(DEFAULT_SCREEN_OFFSET_Y, offset(1.0e-4f));
    assertThat(cameraSpaceVertex[2]).as("focus camera-space Z").isCloseTo(-DEFAULT_DISTANCE, offset(1.0e-4f));
  }

  // Replicates the modelview matrix that WorldViewportRenderer.render builds for the current
  // (pre-frame-fix) renderer:
  //   glLoadIdentity();
  //   glTranslatef(0, screenOffsetY, -distance);
  //   glRotatef(pitch, 1, 0, 0);
  //   glRotatef(-yaw, 0, 1, 0);
  //   glTranslatef(-focusX, -focusHeight, -focusY);
  // Matrix application order (column-vector convention): T_back * R_pitch * R_yaw * T_focus * v.
  private static float[] applyCurrentRendererModelview(float[] meshVertex, WorldCameraState camera) {
    float vx = meshVertex[0];
    float vy = meshVertex[1];
    float vz = meshVertex[2];

    // T_focus
    vx -= camera.focusX();
    vy -= camera.focusHeight();
    vz -= camera.focusY();

    // R_yaw applied with -yaw (current renderer)
    double yawRadians = Math.toRadians(-camera.yawDegrees());
    double cosYaw = Math.cos(yawRadians);
    double sinYaw = Math.sin(yawRadians);
    double rxYaw = vx * cosYaw + vz * sinYaw;
    double rzYaw = -vx * sinYaw + vz * cosYaw;
    vx = (float) rxYaw;
    vz = (float) rzYaw;

    // R_pitch applied with +pitch
    double pitchRadians = Math.toRadians(camera.pitchDegrees());
    double cosPitch = Math.cos(pitchRadians);
    double sinPitch = Math.sin(pitchRadians);
    double ryPitch = vy * cosPitch - vz * sinPitch;
    double rzPitch = vy * sinPitch + vz * cosPitch;
    vy = (float) ryPitch;
    vz = (float) rzPitch;

    // T_back
    vy += camera.screenOffsetY();
    vz += -camera.distance();

    return new float[]{vx, vy, vz};
  }

  private static org.assertj.core.data.Offset<Float> offset(float epsilon) {
    return org.assertj.core.data.Offset.offset(epsilon);
  }

  // Mirror of WorldSceneCameraPlanner's pitch-clamp / distance computation.
  private static int clampedPitchUnits(float pitchDegrees) {
    float unitsPerDegree = 2048.0f / 360.0f;
    int requested = Math.round(pitchDegrees * unitsPerDegree);
    return Math.max(128, Math.min(383, requested));
  }
}
