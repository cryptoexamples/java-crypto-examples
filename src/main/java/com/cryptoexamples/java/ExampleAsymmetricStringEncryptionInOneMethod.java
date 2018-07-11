package com.cryptoexamples.java;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All in one example for asymmetric encryption and decryption of a string in one method.
 * - Generation of public and private RSA 4096 bit keypair
 * - AES-256 authenticated encryption using GCM
 * - BASE64 encoding as representation for the byte-arrays
 * - UTF-8 encoding of Strings
 * - Exception handling
 */
public class ExampleAsymmetricStringEncryptionInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleAsymmetricStringEncryptionInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";
    try {
      // GENERATE NEW KEYPAIR
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      /* @see https://www.keylength.com/ */
      keyPairGenerator.initialize(4096);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      // ENCRYPTION
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

      byte[] cipherTextBytes = cipher.doFinal(plainText.getBytes());

      // CONVERSION of raw bytes to BASE64 representation
      String cipherText = Base64.getEncoder().encodeToString(cipherTextBytes);

      // DECRYPTION
      cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
      byte[] decryptedCipherTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
      String decryptedCipherText = new String(decryptedCipherTextBytes);

      LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException  e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
    }
  }
}
