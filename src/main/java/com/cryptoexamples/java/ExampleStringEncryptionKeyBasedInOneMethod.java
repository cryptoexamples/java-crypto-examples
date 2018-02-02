package com.cryptoexamples.java;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All in one example for encryption and decryption of a string in one method;
 * Including
 * - Random key generation using strong secure random number generator
 * - AES-256 authenticated encryption using GCM
 * - BASE64-encoding as representation for the byte-arrays
 * - Exception handling
 */
public class ExampleStringEncryptionKeyBasedInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleStringEncryptionKeyBasedInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";
    try {
      // GENERATE key
      // TODO key should only be generated once and then stored in a secure location.
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      // 256 bit requires unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
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

      //byte[] aad = "Additional authenticated not encrypted data".getBytes();
      //cipher.updateAAD(aad);

      byte[] byteCipher = cipher.doFinal(plainText.getBytes());
      // CONVERSION of raw bytes to BASE64 representation
      String cipherText = new String(Base64.getEncoder().encode(byteCipher));

      // DECRYPTION
      cipher.init(Cipher.DECRYPT_MODE, key, spec);
      //cipher.updateAAD(aad);
      byte[] decryptedCipher = cipher.doFinal(Base64.getDecoder().decode(cipherText));
      String decryptedCipherText = new String(decryptedCipher);

      LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException | InvalidAlgorithmParameterException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
