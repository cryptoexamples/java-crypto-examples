package com.cryptoexamples.java;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kai Mindermann on 12.05.2017.
 */

/**
 * All in one example for hashing of a string in one method;
 * Including
 * - SHA-512
 * - BASE64 encoding
 * - UTF-8 encoding of String
 * - exception handling
 */
public class ExampleHashInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleHashInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that should be authenticated by comparing the hash of it!";
    try {
      // https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

      // CREATE HASH
      byte[] hash = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));

      // CONVERT/ENCODE IN BASE64
      String hashString = new String(Base64.getEncoder().encode(hash),StandardCharsets.UTF_8);

      LOGGER.log(Level.INFO, hashString);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }
}
