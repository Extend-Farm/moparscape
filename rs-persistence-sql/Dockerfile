FROM gradle:8.10.2-jdk21 AS build

WORKDIR /workspace
COPY . .
RUN gradle --no-daemon :rs-persistence-sql:installDist

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/rs-persistence-sql/build/install/rs-persistence-sql/ /app/

ENTRYPOINT ["/app/bin/rs-persistence-sql"]
CMD ["migrate"]
