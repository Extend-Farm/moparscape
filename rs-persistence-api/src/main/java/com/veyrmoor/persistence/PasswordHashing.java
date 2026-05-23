package com.veyrmoor.persistence;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHashing {

  private static final String FORMAT_PREFIX = "pbkdf2-sha256";
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final int DEFAULT_ITERATIONS = 210_000;
  private static final int SALT_BYTES = 16;
  private static final int KEY_LENGTH_BITS = 256;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();

  private PasswordHashing() {
  }

  public static String hashPassword(String password) {
    byte[] salt = new byte[SALT_BYTES];
    SECURE_RANDOM.nextBytes(salt);
    byte[] derivedKey = derive(password, salt, DEFAULT_ITERATIONS, KEY_LENGTH_BITS);
    return FORMAT_PREFIX
        + "$"
        + DEFAULT_ITERATIONS
        + "$"
        + BASE64_ENCODER.encodeToString(salt)
        + "$"
        + BASE64_ENCODER.encodeToString(derivedKey);
  }

  public static boolean matches(String storedPasswordHash, String presentedPassword) {
    if (storedPasswordHash == null || presentedPassword == null) {
      return false;
    }
    if (!storedPasswordHash.startsWith(FORMAT_PREFIX + "$")) {
      return MessageDigest.isEqual(
          storedPasswordHash.getBytes(StandardCharsets.UTF_8),
          presentedPassword.getBytes(StandardCharsets.UTF_8)
      );
    }

    String[] parts = storedPasswordHash.split("\\$");
    if (parts.length != 4) {
      return false;
    }
    int iterations = Integer.parseInt(parts[1]);
    byte[] salt = BASE64_DECODER.decode(parts[2]);
    byte[] expectedHash = BASE64_DECODER.decode(parts[3]);
    byte[] actualHash = derive(presentedPassword, salt, iterations, expectedHash.length * 8);
    return MessageDigest.isEqual(expectedHash, actualHash);
  }

  private static byte[] derive(String password, byte[] salt, int iterations, int keyLengthBits) {
    try {
      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLengthBits);
      try {
        return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec).getEncoded();
      } finally {
        keySpec.clearPassword();
      }
    } catch (GeneralSecurityException generalSecurityException) {
      throw new IllegalStateException("Failed to derive password hash", generalSecurityException);
    }
  }
}
