package com.cryptoexamples.java;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All in one example for encryption and decryption of a string in one method.
 * - Random password generation using strong secure random number generator
 * - Random salt generation
 * - Key derivation using PBKDF2 HMAC SHA-512,
 * - AES-256 authenticated encryption using GCM
 * - BASE64 encoding as representation for the byte-arrays
 * - UTF-8 encoding of Strings
 * - Exception handling
 */
public class ExampleStringEncryptionPasswordBasedInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleStringEncryptionPasswordBasedInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";
    try {
      String password = null;
      // GENERATE password (not needed if you have a password already)
      if(password == null || password.isEmpty()) {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        password = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
      }

      // GENERATE random salt (needed for PBKDF2)
      final byte[] salt = new byte[64];
      SecureRandom random = SecureRandom.getInstanceStrong();
      random.nextBytes(salt);

      // DERIVE key (from password and salt)
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      KeySpec passwordBasedEncryptionKeySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
      SecretKey secretKeyFromPBKDF2 = secretKeyFactory.generateSecret(passwordBasedEncryptionKeySpec);
      SecretKey key = new SecretKeySpec(secretKeyFromPBKDF2.getEncoded(), "AES");

      // GENERATE random nonce (number used once)
      final byte[] nonce = new byte[32];
      random.nextBytes(nonce);

      // ENCRYPTION
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(16 * 8, nonce);
      cipher.init(Cipher.ENCRYPT_MODE, key, spec);

      byte[] cipherTextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      // CONVERSION of raw bytes to BASE64 representation
      String cipherText = Base64.getEncoder().encodeToString(cipherTextBytes);

      // DECRYPTION
      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      byte[] decryptedCipherTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
      String decryptedCipherText = new String(decryptedCipherTextBytes, StandardCharsets.UTF_8);

      LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
    }
  }

}
