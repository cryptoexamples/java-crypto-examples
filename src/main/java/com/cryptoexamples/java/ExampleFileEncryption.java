package com.cryptoexamples.java;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example for encryption and decryption of a file in one method.
 * - Random password generation using strong secure random number generator
 * - Random salt generation
 * - Key derivation using PBKDF2 HMAC SHA-512,
 * - AES-256 authenticated encryption using GCM
 * - BASE64-encoding as representation for the byte-arrays
 * - Exception handling
 */
public class ExampleFileEncryption {
  private static final Logger LOGGER = Logger.getLogger(ExampleFileEncryption.class.getName());

  /**
   * Demonstrational method that encrypts a file using a password (that is used to derive the required key).
   * @param fileName
   * @param plainText
   * @param password
   * @return true if encryption and decryption were successful, false otherwise
   */
  public static boolean demonstrateFileEncryption(String fileName, String plainText, String password) {
    try {
      // GENERATE password (not needed if you have a password already)
      if(password == null || password.isEmpty()) {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        password = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
      }

      // GENERATE random salt
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

      // SET UP CIPHER for encryption
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(16 * 8, nonce);
      cipher.init(Cipher.ENCRYPT_MODE, key, spec);

      // TODO store encryption parameters as authenticated data prepended to the file content

      // SET UP OUTPUT STREAM and write content of String
      try (
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        CipherOutputStream encryptedOutputStream = new CipherOutputStream(fileOutputStream, cipher);
        InputStream stringInputStream = new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8))
      ) {
        byte[] buffer = new byte[8192];
        int nread;
        while ((nread = stringInputStream.read(buffer)) > 0) {
          encryptedOutputStream.write(buffer, 0, nread);
        }
        encryptedOutputStream.flush();
      }

      // READ ENCRYPTED FILE
      StringBuilder stringBuilder = new StringBuilder();
      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      String decryptedCipherText;
      try (
        FileInputStream fileInputStream = new FileInputStream(fileName);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ) {
        byte[] buffer = new byte[8192];
        int nread;
        while ((nread = cipherInputStream.read(buffer)) > 0) {
          byteArrayOutputStream.write(buffer, 0, nread);
        }
        byteArrayOutputStream.flush();
        decryptedCipherText = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
      }

      LOGGER.log(Level.INFO, decryptedCipherText);
      LOGGER.log(Level.INFO,
              () -> String.format("Decrypted file content and original plain text are the same: %b",
                      decryptedCipherText.compareTo(plainText) == 0)
      );
      return decryptedCipherText.compareTo(plainText) == 0;
    } catch (NoSuchAlgorithmException |
            NoSuchPaddingException |
            InvalidKeyException |
            InvalidParameterException |
            InvalidAlgorithmParameterException |
            InvalidKeySpecException |
            IOException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
      return false;
    }
  }


  public static void main(String[] args) {
    demonstrateFileEncryption("encryptedFile.enc","Multiline text:\nMultiline text:\n",null );
  }

}
