package com.cryptoexamples.java;

import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO check if KeyStore should be used

/**
 * Object oriented example for encryption and decryption of a string;
 * Including
 * - Random password generation using strong secure random number generator
 * - random salt generation
 * - Key derivation using PBKDF2 HMAC SHA-256,
 * - AES-256 authenticated encryption using GCM
 * - BASE64-encoding as representation for the byte-arrays
 * - Exception handling
 */
public class ExampleEncryptedString {
  private static final Logger LOGGER = Logger.getLogger(ExampleEncryptedString.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";

    try {
      // GENERATE a password (if a password exists, use that).
      String password = EncryptedString.generatePassword(32);

      // ENCRYPTION
      EncryptedString encryptedString = new EncryptedString().encrypt(plainText, password);

      // DECRYPTION
      String decryptedCipherText = encryptedString.decrypt(password);

      LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));
    } catch (GeneralSecurityException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
