package com.cryptoexamples.java;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example for cryptographic signing of a string in one method.
 * - Generation of public and private RSA 4096 bit keypair
 * - SHA-512 with RSA
 * - BASE64 encoding as representation for the byte-arrays
 * - UTF-8 encoding of String
 * - Exception handling
 */
public class ExampleSignature {
  private static final Logger LOGGER = Logger.getLogger(ExampleSignature.class.getName());

  /**
   * Demonstrational method that signs the plainText using a newly generated keypair.
   * @param plainText
   * @return true if signing and verification were successful, false otherwise
   */
  public static boolean demonstrateSignature(String plainText) {
    try {
      // GENERATE NEW KEYPAIR
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      /* @see https://www.keylength.com/ */
      keyPairGenerator.initialize(4096);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      // INITIALIZE SIGNATURE WITH PRIVATE KEY
      Signature signature = Signature.getInstance("SHA512withRSA");
      signature.initSign(keyPair.getPrivate());
      signature.update(plainText.getBytes(StandardCharsets.UTF_8));

      // SIGN DATA/STRING
      String signatureForPlainTextString = Base64.getEncoder().encodeToString(signature.sign());
      LOGGER.log(Level.INFO, () -> String.format("Signature: %s", signatureForPlainTextString));

      // VERIFY JUST CREATED SIGNATURE USING PUBLIC KEY
      signature.initVerify(keyPair.getPublic());
      signature.update(plainText.getBytes(StandardCharsets.UTF_8));

      boolean isSignatureCorrect = signature.verify(Base64.getDecoder().decode(signatureForPlainTextString));
      LOGGER.log(Level.INFO, () -> String.format("Signature is correct: %b", isSignatureCorrect));
      return isSignatureCorrect;
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage());
      return false;
    }
  }

  public static void main(String[] args) {
    demonstrateSignature("Text that should be signed to prevent unknown tampering with its content.");

  }
}
