# Agent 1 Packet: Cache Core

## Scope

Own only:

- `rs-cache/src/main/java/io/github/ffakira/rsps/cache/*`
- `rs-cache/src/test/java/io/github/ffakira/rsps/cache/*`

## Objective

Take `rs-cache` from raw archive byte reading to usable archive container parsing.

## Tasks

1. Harden `CacheStoreReader`
- validate sector bounds more defensively
- support reading multiple known archives from store `0`
- keep behavior aligned with legacy `CacheFileStore`

2. Implement archive container parsing
- parse the six-byte compression header
- add decompression support for compressed archive containers
- expose a parsed archive container abstraction

3. Add parity tests
- compare raw store reads with legacy `CacheFileStore`
- compare parsed archive container payloads against legacy `Archive` entry access for one or more known archives

## Acceptance

- `./gradlew :rs-cache:test` passes
- at least one compressed archive path works
- no production dependency from `rs-cache` back to `:game-client`
