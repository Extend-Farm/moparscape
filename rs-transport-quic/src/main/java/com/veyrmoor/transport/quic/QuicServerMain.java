package com.veyrmoor.transport.quic;

import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.persistence.sql.PostgresAccountProvisioner;
import com.veyrmoor.persistence.sql.PostgresAccountRepository;
import com.veyrmoor.persistence.sql.PostgresCharacterRepository;
import com.veyrmoor.persistence.sql.SqlPersistenceEnvironment;
import com.veyrmoor.server.runtime.CharacterFileRepository;
import com.veyrmoor.server.runtime.InProcessServerRuntime;
import com.veyrmoor.server.runtime.LoginAccountProvisioner;
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
             new InProcessServerRuntime(
                 repositories.accountRepository(),
                 repositories.characterRepository(),
                 com.veyrmoor.server.runtime.CredentialVerifier.compatible(),
                 repositories.loginAccountProvisioner(),
                 com.veyrmoor.protocol.ProtocolVersion.current(),
                 "Welcome to the modern RSPS runtime"
             );
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
      PostgresAccountProvisioner accountProvisioner = new PostgresAccountProvisioner(configuration);
      return new RepositoryPair(
          new PostgresAccountRepository(configuration),
          new PostgresCharacterRepository(configuration),
          accountProvisioner::provisionForLogin
      );
    }
    CharacterFileRepository repository = new CharacterFileRepository(resolveCharactersDirectory(workingDirectory));
    return new RepositoryPair(repository, repository, LoginAccountProvisioner.disabled());
  }

  private static Path resolveCharactersDirectory(Path workingDirectory) {
    Path[] candidates = {
        workingDirectory.resolve("moparscape-reference/client/characters"),
        workingDirectory.resolve("../moparscape-reference/client/characters").normalize(),
        workingDirectory.resolve("client/characters"),
        workingDirectory.resolve("../client/characters").normalize()
    };
    for (Path candidate : candidates) {
      if (Files.isDirectory(candidate)) {
        return candidate;
      }
    }
    throw new IllegalStateException("Unable to locate the reference characters directory from " + workingDirectory);
  }

  private record RepositoryPair(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      LoginAccountProvisioner loginAccountProvisioner
  ) {
  }
}
