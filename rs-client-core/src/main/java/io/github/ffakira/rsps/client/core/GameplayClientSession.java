package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.DisconnectNotice;
import io.github.ffakira.rsps.protocol.HandshakeRequest;
import io.github.ffakira.rsps.protocol.LoginRequest;
import io.github.ffakira.rsps.protocol.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.ActionSequenceIntentMessage;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameplayClientSession implements AutoCloseable {

  private static final long WALK_STEP_INTERVAL_NANOS = 120_000_000L;

  private final ClientCore clientCore;
  private final ClientTransport transport;
  private final String clientDescriptor;
  private final SceneAssetService sceneAssetService;
  private final Path workingDirectory;
  private final LongSupplier nanoClock;
  private final ArrayDeque<QueuedMovementStep> pendingMovementSteps = new ArrayDeque<>();
  private long nextMovementDispatchNanos;
  private boolean movementStepInFlight;
  private QueuedMovementStep inFlightStep;

  public GameplayClientSession(ClientCore clientCore, ClientTransport transport, String clientDescriptor) {
    this(clientCore, transport, clientDescriptor, new DefaultSceneAssetService(), Path.of("."), System::nanoTime);
  }

  GameplayClientSession(
      ClientCore clientCore,
      ClientTransport transport,
      String clientDescriptor,
      SceneAssetService sceneAssetService,
      Path workingDirectory
  ) {
    this(clientCore, transport, clientDescriptor, sceneAssetService, workingDirectory, System::nanoTime);
  }

  GameplayClientSession(
      ClientCore clientCore,
      ClientTransport transport,
      String clientDescriptor,
      SceneAssetService sceneAssetService,
      Path workingDirectory,
      LongSupplier nanoClock
  ) {
    this.clientCore = clientCore;
    this.transport = transport;
    this.clientDescriptor = clientDescriptor;
    this.sceneAssetService = sceneAssetService;
    this.workingDirectory = workingDirectory;
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
  }

  public List<ClientEvent> bootstrap() {
    SceneBootstrapAssets bootstrapAssets = sceneAssetService.loadInitialAssets(workingDirectory);
    return List.of(clientCore.bootstrap(bootstrapAssets));
  }

  public void connect() {
    transport.send(new HandshakeRequest(ProtocolVersion.current(), clientDescriptor));
  }

  public void login(String username, String password) {
    transport.send(new LoginRequest(username, password));
  }

  public void move(int deltaX, int deltaY, MovementMode movementMode) {
    if (!clientCore.viewModel().loggedIn()) {
      return;
    }
    pendingMovementSteps.clear();
    int remainingDeltaX = deltaX;
    int remainingDeltaY = deltaY;
    if (movementStepInFlight && inFlightStep != null) {
      remainingDeltaX -= inFlightStep.deltaX();
      remainingDeltaY -= inFlightStep.deltaY();
    }
    pendingMovementSteps.addAll(buildMovementPath(remainingDeltaX, remainingDeltaY, movementMode));
    nextMovementDispatchNanos = 0L;
    pumpMovement();
  }

  public void setActionSequence(int actionSequenceId) {
    if (!clientCore.viewModel().loggedIn()) {
      return;
    }
    transport.send(new ActionSequenceIntentMessage(actionSequenceId));
  }

  public void pumpMovement() {
    if (!clientCore.viewModel().loggedIn()) {
      clearQueuedMovement();
      return;
    }
    if (movementStepInFlight || pendingMovementSteps.isEmpty()) {
      return;
    }
    long now = nanoClock.getAsLong();
    if (now < nextMovementDispatchNanos) {
      return;
    }
    dispatchNextMovementStep(now);
  }

  public List<ClientEvent> accept(ServerMessage message) {
    WorldPoint previousPosition = clientCore.viewModel().localPlayerPosition();
    List<ClientEvent> events = clientCore.accept(message);
    if (!clientCore.viewModel().loggedIn()) {
      clearQueuedMovement();
      return events;
    }
    WorldPoint currentPosition = clientCore.viewModel().localPlayerPosition();
    if (movementStepInFlight && positionChanged(previousPosition, currentPosition)) {
      movementStepInFlight = false;
      inFlightStep = null;
    }
    return events;
  }

  public ClientViewModel viewModel() {
    return clientCore.viewModel();
  }

  @Override
  public void close() {
    clearQueuedMovement();
    if (clientCore.viewModel().loggedIn()) {
      transport.send(new DisconnectNotice("Client shutdown"));
    }
    transport.close();
  }

  private void dispatchNextMovementStep(long now) {
    QueuedMovementStep nextStep = pendingMovementSteps.pollFirst();
    if (nextStep == null) {
      return;
    }
    movementStepInFlight = true;
    inFlightStep = nextStep;
    nextMovementDispatchNanos = now + WALK_STEP_INTERVAL_NANOS;
    transport.send(new MoveIntentMessage(nextStep.deltaX(), nextStep.deltaY(), nextStep.movementMode()));
  }

  private List<QueuedMovementStep> buildMovementPath(int deltaX, int deltaY, MovementMode movementMode) {
    int absDeltaX = Math.abs(deltaX);
    int absDeltaY = Math.abs(deltaY);
    int stepCount = Math.max(absDeltaX, absDeltaY);
    if (stepCount <= 0) {
      return List.of();
    }
    ArrayDeque<QueuedMovementStep> steps = new ArrayDeque<>(stepCount);
    int stepX = Integer.compare(deltaX, 0);
    int stepY = Integer.compare(deltaY, 0);
    if (absDeltaX >= absDeltaY) {
      int error = absDeltaX / 2;
      for (int index = 0; index < absDeltaX; index++) {
        int stepDeltaY = 0;
        error -= absDeltaY;
        if (error < 0) {
          stepDeltaY = stepY;
          error += absDeltaX;
        }
        steps.addLast(new QueuedMovementStep(stepX, stepDeltaY, movementMode));
      }
    } else {
      int error = absDeltaY / 2;
      for (int index = 0; index < absDeltaY; index++) {
        int stepDeltaX = 0;
        error -= absDeltaX;
        if (error < 0) {
          stepDeltaX = stepX;
          error += absDeltaY;
        }
        steps.addLast(new QueuedMovementStep(stepDeltaX, stepY, movementMode));
      }
    }
    return List.copyOf(steps);
  }

  private boolean positionChanged(WorldPoint previousPosition, WorldPoint currentPosition) {
    if (previousPosition == null || currentPosition == null) {
      return previousPosition != currentPosition;
    }
    return previousPosition.x() != currentPosition.x()
        || previousPosition.y() != currentPosition.y()
        || previousPosition.plane() != currentPosition.plane();
  }

  private void clearQueuedMovement() {
    pendingMovementSteps.clear();
    nextMovementDispatchNanos = 0L;
    movementStepInFlight = false;
    inFlightStep = null;
  }

  private record QueuedMovementStep(int deltaX, int deltaY, MovementMode movementMode) {
  }
}
