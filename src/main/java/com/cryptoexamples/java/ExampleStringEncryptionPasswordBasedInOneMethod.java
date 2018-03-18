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
 * All in one example for encryption and decryption of a string in one method;
 * Including
 * - Random password generation using strong secure random number generator
 * - Random salt generation
 * - Key derivation using PBKDF2 HMAC SHA-256,
 * - AES-256 authenticated encryption using GCM
 * - BASE64-encoding as representation for the byte-arrays
 * - Exception handling
 */
public class ExampleStringEncryptionPasswordBasedInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleStringEncryptionPasswordBasedInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";
    try {
      // GENERATE password (not needed if you have a password already)
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
      keyGen.init(256);
      String password = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());

      // GENERATE random salt (needed for PBKDF2)
      final byte[] salt = new byte[12];
      SecureRandom random = SecureRandom.getInstanceStrong();
      random.nextBytes(salt);

      // DERIVE key (from password and salt)
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
      KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(keyspec);
      SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");


      // GENERATE random nonce (number used once)
      final byte[] nonce = new byte[32];
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
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
