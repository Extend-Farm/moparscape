package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.protocol.session.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class LoginGatewayActor extends MailboxActor<LoginGatewayMessage> {

  private final AccountRepository accountRepository;
  private final CharacterRepository characterRepository;
  private final CredentialVerifier credentialVerifier;
  private final ProtocolVersion acceptedProtocolVersion;
  private final String motd;
  private final WorldShardActor worldShardActor;

  public LoginGatewayActor(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      CredentialVerifier credentialVerifier,
      ProtocolVersion acceptedProtocolVersion,
      String motd,
      WorldShardActor worldShardActor
  ) {
    this.accountRepository = accountRepository;
    this.characterRepository = characterRepository;
    this.credentialVerifier = credentialVerifier;
    this.acceptedProtocolVersion = acceptedProtocolVersion;
    this.motd = motd;
    this.worldShardActor = worldShardActor;
  }

  @Override
  protected void onMessage(LoginGatewayMessage message) {
    switch (message) {
      case LoginGatewayMessage.HandleHandshakeMessage handleHandshakeMessage ->
          handleHandshake(handleHandshakeMessage);
      case LoginGatewayMessage.HandleLoginMessage handleLoginMessage -> handleLogin(handleLoginMessage);
    }
  }

  private void handleHandshake(LoginGatewayMessage.HandleHandshakeMessage message) {
    if (!acceptedProtocolVersion.equals(message.request().protocolVersion())) {
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage(
          "Unsupported protocol version: " + message.request().protocolVersion().value(),
          true
      ));
      return;
    }

    message.replyTo().tell(new SessionMessage.HandshakeCompletedMessage(
        new HandshakeAccepted(acceptedProtocolVersion, motd)
    ));
  }

  private void handleLogin(LoginGatewayMessage.HandleLoginMessage message) {
    var accountRecord = accountRepository.findByUsername(message.request().username()).orElse(null);
    if (accountRecord == null || !credentialVerifier.matches(accountRecord, message.request().password())) {
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage("Invalid credentials", false));
      return;
    }

    var characterSnapshot = characterRepository.loadByAccountId(accountRecord.id()).orElse(null);
    if (characterSnapshot == null) {
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage("No character found for account", false));
      return;
    }

    try {
      WorldShardAdmission admission = worldShardActor.enterCharacter(characterSnapshot).get(2, TimeUnit.SECONDS);
      message.replyTo().tell(new SessionMessage.LoginSucceededMessage(accountRecord, admission));
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage("Login interrupted", true));
    } catch (ExecutionException | TimeoutException exception) {
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage("World entry failed", true));
    }
  }
}
