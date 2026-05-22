package io.github.ffakira.rsps.transport.quic;

import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresAccountRepository;
import io.github.ffakira.rsps.persistence.sql.PostgresCharacterRepository;
import io.github.ffakira.rsps.persistence.sql.SqlPersistenceEnvironment;
import io.github.ffakira.rsps.server.runtime.CharacterFileRepository;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuicServerMain {

  private static final Logger log = LoggerFactory.getLogger(QuicServerMain.class);

  private QuicServerMain() {
  }

  public static void main(String[] args) {
    Path workingDirectory = Path.of(".").toAbsolutePath().normalize();
    QuicTransportConfiguration configuration = QuicTransportConfiguration.defaults(workingDirectory);
    RepositoryPair repositories = createRepositories(workingDirectory);
    try (InProcessServerRuntime runtime =
             new InProcessServerRuntime(repositories.accountRepository(), repositories.characterRepository());
         QuicServerEndpoint endpoint = QuicServerEndpoint.start(runtime, configuration)) {
      log.info(
          "QUIC runtime listening on {}:{} with local trust anchor {}",
          configuration.bindHost(),
          configuration.port(),
          configuration.certificateDirectory().resolve("server-cert.pem")
      );
      endpoint.await();
    }
  }

  private static RepositoryPair createRepositories(Path workingDirectory) {
    String persistenceMode = System.getenv("RSPS_PERSISTENCE_MODE");
    if ("postgres".equalsIgnoreCase(persistenceMode)) {
      var configuration = SqlPersistenceEnvironment.load();
      return new RepositoryPair(
          new PostgresAccountRepository(configuration),
          new PostgresCharacterRepository(configuration)
      );
    }
    CharacterFileRepository repository = new CharacterFileRepository(resolveCharactersDirectory(workingDirectory));
    return new RepositoryPair(repository, repository);
  }

  private static Path resolveCharactersDirectory(Path workingDirectory) {
    Path[] candidates = {
        workingDirectory.resolve("client/characters"),
        workingDirectory.resolve("../client/characters").normalize()
    };
    for (Path candidate : candidates) {
      if (Files.isDirectory(candidate)) {
        return candidate;
      }
    }
    throw new IllegalStateException("Unable to locate client/characters from " + workingDirectory);
  }

  private record RepositoryPair(AccountRepository accountRepository, CharacterRepository characterRepository) {
  }
}
