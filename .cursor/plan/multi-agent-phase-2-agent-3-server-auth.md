# Agent 3 Packet: Server Auth And Session

## Scope

Own only:

- `rs-server-runtime/src/main/java/io/github/ffakira/rsps/server/runtime/*`
- `rs-server-runtime/src/test/java/io/github/ffakira/rsps/server/runtime/*`
- `rs-persistence-api/src/main/java/io/github/ffakira/rsps/persistence/*`
- `rs-persistence-sql/src/main/java/io/github/ffakira/rsps/persistence/sql/*`
- `rs-protocol/src/main/java/io/github/ffakira/rsps/protocol/*`

## Objective

Turn the new runtime shell into a real login/session skeleton.

## Tasks

1. Define login/session actors
- session actor
- login gateway actor
- handoff into world shard actor

2. Stabilize protocol/login contracts
- handshake request/accept
- login success/failure
- movement command handoff

3. Persistence integration
- load account by username
- load character by account id
- keep persistence behind repository ports

4. Tests
- mailbox sequencing
- login success/failure paths
- persistence adapter smoke coverage where feasible

## Acceptance

- `./gradlew :rs-server-runtime:test :rs-persistence-sql:test` passes
- a headless runtime can authenticate and load a character snapshot in-process
