# Agent 4 Packet: Client Login And World Bootstrap

## Scope

Own only:

- `rs-client-core/src/main/java/io/github/ffakira/rsps/client/core/*`
- `rs-client-core/src/test/java/io/github/ffakira/rsps/client/core/*`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/*`

## Objective

Move the new client from a shell into a login/world bootstrap flow.

## Tasks

1. Expand client state machine
- disconnected
- connecting
- handshaking
- login
- world loading
- in-world

2. Bind protocol events
- handshake accepted/rejected
- login accepted/rejected
- world snapshot/bootstrap
- movement updates

3. Improve LWJGL shell
- display status transitions clearly
- keep rendering/input shell thin
- avoid embedding gameplay logic in the window layer

4. Tests
- state transitions
- world load event handling

## Acceptance

- `./gradlew :rs-client-core:test :rs-client-lwjgl:compileJava` passes
- client state machine reaches login and world-loaded states in tests
