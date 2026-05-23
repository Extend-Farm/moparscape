package com.veyrmoor.transport.quic;

import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class QuicCertificateStore {

  private static final String CERTIFICATE_FILE_NAME = "server-cert.pem";
  private static final String PRIVATE_KEY_FILE_NAME = "server-key.pem";

  private final Path certificateDirectory;
  private final Path certificateFile;
  private final Path privateKeyFile;

  private QuicCertificateStore(Path certificateDirectory, Path certificateFile, Path privateKeyFile) {
    this.certificateDirectory = certificateDirectory;
    this.certificateFile = certificateFile;
    this.privateKeyFile = privateKeyFile;
  }

  static QuicCertificateStore openServer(Path certificateDirectory, String hostName) {
    try {
      Files.createDirectories(certificateDirectory);
      Path certificateFile = certificateDirectory.resolve(CERTIFICATE_FILE_NAME);
      Path privateKeyFile = certificateDirectory.resolve(PRIVATE_KEY_FILE_NAME);
      if (!Files.isRegularFile(certificateFile) || !Files.isRegularFile(privateKeyFile)) {
        writeSelfSignedCertificate(certificateFile, privateKeyFile, hostName);
      }
      return new QuicCertificateStore(certificateDirectory, certificateFile, privateKeyFile);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to initialize QUIC certificate directory " + certificateDirectory, exception);
    } catch (Exception exception) {
      throw new IllegalStateException("Unable to create local QUIC certificate material", exception);
    }
  }

  static QuicCertificateStore openClient(Path certificateDirectory) {
    Path certificateFile = certificateDirectory.resolve(CERTIFICATE_FILE_NAME);
    if (!Files.isRegularFile(certificateFile)) {
      throw new IllegalStateException(
          "Missing QUIC trust certificate %s. Start the QUIC server once to generate local certificate material."
              .formatted(certificateFile)
      );
    }
    return new QuicCertificateStore(certificateDirectory, certificateFile, certificateDirectory.resolve(PRIVATE_KEY_FILE_NAME));
  }

  Path certificateDirectory() {
    return certificateDirectory;
  }

  Path certificateFile() {
    return certificateFile;
  }

  Path privateKeyFile() {
    return privateKeyFile;
  }

  File certificateFileHandle() {
    return certificateFile.toFile();
  }

  File privateKeyFileHandle() {
    return privateKeyFile.toFile();
  }

  private static void writeSelfSignedCertificate(Path certificateFile, Path privateKeyFile, String hostName) throws Exception {
    SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate(hostName);
    Files.copy(selfSignedCertificate.certificate().toPath(), certificateFile, StandardCopyOption.REPLACE_EXISTING);
    Files.copy(selfSignedCertificate.privateKey().toPath(), privateKeyFile, StandardCopyOption.REPLACE_EXISTING);
    selfSignedCertificate.delete();
  }
}
