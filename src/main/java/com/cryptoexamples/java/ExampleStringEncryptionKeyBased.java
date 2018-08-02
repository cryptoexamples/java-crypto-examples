package com.cryptoexamples.java;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example for encryption and decryption of a string in one method.
 * - Random key generation using strong secure random number generator
 * - AES-256 authenticated encryption using GCM
 * - BASE64 encoding as representation for the byte-arrays
 * - UTF-8 encoding of Strings
 * - Exception handling
 */
public class ExampleStringEncryptionKeyBased {
  private static final Logger LOGGER = Logger.getLogger(ExampleStringEncryptionKeyBased.class.getName());

  /**
   * Demonstrational method that encrypts the plainText using a newly generated key.
   * @param plainText
   * @return true if encryption and decryption were successful, false otherwise
   */
  public static boolean demonstrateKeyBasedSymmetricEncryption(String plainText) {
    try {
      // GENERATE key
      // TODO key should only be generated once and then managed with a key manager/key store.
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(256);
      SecretKey key = keyGen.generateKey();

      // GENERATE random nonce (number used once)
      final byte[] nonce = new byte[32];
      SecureRandom random = SecureRandom.getInstanceStrong();
      random.nextBytes(nonce);

      // ENCRYPTION
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec spec = new GCMParameterSpec(16 * 8, nonce);
      cipher.init(Cipher.ENCRYPT_MODE, key, spec);

      byte[] byteCipher = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      // CONVERSION of raw bytes to BASE64 representation
      String cipherText = Base64.getEncoder().encodeToString(byteCipher);

      // DECRYPTION
      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      byte[] decryptedCipher = cipher.doFinal(Base64.getDecoder().decode(cipherText));
      String decryptedCipherText = new String(decryptedCipher, StandardCharsets.UTF_8);

      LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));
      return decryptedCipherText.compareTo(plainText) == 0;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException | InvalidAlgorithmParameterException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
      return false;
    }
  }

  public static void main(String[] args) {
    demonstrateKeyBasedSymmetricEncryption("Text that is going to be sent over an insecure channel and must be encrypted at all costs!");
  }
}
