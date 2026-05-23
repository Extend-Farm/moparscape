package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.ProtocolVersion;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class LoginGatewayActor extends MailboxActor<LoginGatewayMessage> {

  private final AccountRepository accountRepository;
  private final CharacterRepository characterRepository;
  private final CredentialVerifier credentialVerifier;
  private final LoginAccountProvisioner loginAccountProvisioner;
  private final ProtocolVersion acceptedProtocolVersion;
  private final String motd;
  private final WorldShardActor worldShardActor;

  public LoginGatewayActor(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      CredentialVerifier credentialVerifier,
      LoginAccountProvisioner loginAccountProvisioner,
      ProtocolVersion acceptedProtocolVersion,
      String motd,
      WorldShardActor worldShardActor
  ) {
    this.accountRepository = accountRepository;
    this.characterRepository = characterRepository;
    this.credentialVerifier = credentialVerifier;
    this.loginAccountProvisioner = loginAccountProvisioner;
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
    var accountRecord = loadOrProvisionAccount(message.request().username(), message.request().password());
    if (accountRecord == null || !credentialVerifier.matches(accountRecord, message.request().password())) {
      message.replyTo().tell(new SessionMessage.LoginRejectedMessage("Invalid credentials", false));
      return;
    }

    var characterSnapshot = loadOrProvisionCharacter(accountRecord, message.request().password());
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

  private com.veyrmoor.persistence.AccountRecord loadOrProvisionAccount(
      String username,
      String password
  ) {
    var accountRecord = accountRepository.findByUsername(username).orElse(null);
    if (accountRecord != null) {
      return accountRecord;
    }
    if (!loginAccountProvisioner.provision(username, password)) {
      return null;
    }
    return accountRepository.findByUsername(username).orElse(null);
  }

  private com.veyrmoor.persistence.CharacterSnapshot loadOrProvisionCharacter(
      com.veyrmoor.persistence.AccountRecord accountRecord,
      String password
  ) {
    var characterSnapshot = characterRepository.loadByAccountId(accountRecord.id()).orElse(null);
    if (characterSnapshot != null) {
      return characterSnapshot;
    }
    if (!loginAccountProvisioner.provision(accountRecord.username(), password)) {
      return null;
    }
    return characterRepository.loadByAccountId(accountRecord.id()).orElse(null);
  }
}
