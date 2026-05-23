package com.veyrmoor.client.core;

import com.veyrmoor.model.MovementMode;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.session.DisconnectNotice;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.input.MoveIntentMessage;
import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.input.PublicChatIntentMessage;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.ServerMessage;
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

  public void sendPublicChat(String text) {
    if (!clientCore.viewModel().loggedIn()) {
      return;
    }
    transport.send(new PublicChatIntentMessage(text));
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
    if (movementMode != MovementMode.RUN || steps.size() <= 1) {
      return List.copyOf(steps);
    }
    return collapseRunBursts(steps);
  }

  private List<QueuedMovementStep> collapseRunBursts(ArrayDeque<QueuedMovementStep> steps) {
    ArrayDeque<QueuedMovementStep> collapsedSteps = new ArrayDeque<>((steps.size() + 1) / 2);
    while (!steps.isEmpty()) {
      QueuedMovementStep firstStep = steps.pollFirst();
      QueuedMovementStep secondStep = steps.pollFirst();
      if (secondStep == null) {
        collapsedSteps.addLast(firstStep);
        continue;
      }
      collapsedSteps.addLast(new QueuedMovementStep(
          firstStep.deltaX() + secondStep.deltaX(),
          firstStep.deltaY() + secondStep.deltaY(),
          MovementMode.RUN
      ));
    }
    return List.copyOf(collapsedSteps);
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
