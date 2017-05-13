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
 * Created by Kai Mindermann on 2017-05-13.
 */

/**
 * All in one example for cryptographic signing of a string in one method;
 * Including
 * - Generation of public and private RSA keypair
 * - SHA-512 with RSA
 * - BASE64 encoding
 * - UTF-8 encoding of String
 * - exception handling
 */
public class ExampleSignatureInOneMethod {
  private static final Logger LOGGER = Logger.getLogger(ExampleSignatureInOneMethod.class.getName());

  public static void main(String[] args) {
    String plainText = "Text that should be signed to prevent unknown tampering with it's content.";
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
      byte[] signatureForPlainText = signature.sign();
      String signatureForPlainTextString = new String(Base64.getEncoder().encode(signatureForPlainText),StandardCharsets.UTF_8);

      // VERIFY JUST CREATED SIGNATURE USING PUBLIC KEY
      signature.initVerify(keyPair.getPublic());
      signature.update(plainText.getBytes(StandardCharsets.UTF_8));

      boolean isSignatureCorrect = signature.verify(signatureForPlainText);
      LOGGER.log(Level.INFO, () -> String.format("Signature is correct: %b", isSignatureCorrect));
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }
}
