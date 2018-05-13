package com.cryptoexamples.java;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All in one example for hashing of a string in one method.
 * - SHA-512
 * - BASE64 encoding as representation for the byte-arrays
 * - UTF-8 encoding of String
 * - Exception handling
 */
public class ExampleHashInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleHashInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that should be authenticated by comparing the hash of it!";
    try {
      // Get MessageDigest Instance
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

      // CREATE HASH
      byte[] hashBytes = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));

      // CONVERT/ENCODE IN BASE64
      String hashString = Base64.getEncoder().encodeToString(hashBytes);

      LOGGER.log(Level.INFO, hashString);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }
}
