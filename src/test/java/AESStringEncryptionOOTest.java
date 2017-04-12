import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Kai on 10.04.2017.
 */
class AESStringEncryptionOOTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @BeforeAll
  void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterAll
  void tearDown() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  void testMain() {
    AESStringEncryptionOO.main();
    assertEquals("Decrypted and original plain text are the same: true", outContent.toString());
  }
  @Test
  void testEncryptWithUmlaut() {
    try {
      String password = AESStringEncryptionOO.generatePassword();
      byte[] salt = AESStringEncryptionOO.generateSalt();
      byte[] nonce = AESStringEncryptionOO.generateNonce();

      String testString = "ä";

      String cipherText = AESStringEncryptionOO.encrypt(testString, password, salt, nonce);

      assertEquals(testString, AESStringEncryptionOO.decrypt(cipherText, password, salt, nonce));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
      assertTrue(false);
    }
  }

  @Test
  void testEncryptWithASCII() {
    try {
      String password = AESStringEncryptionOO.generatePassword();
      byte[] salt = AESStringEncryptionOO.generateSalt();
      byte[] nonce = AESStringEncryptionOO.generateNonce();

      String testString = "ASDF";

      String cipherText = AESStringEncryptionOO.encrypt(testString, password, salt, nonce);

      assertEquals(testString, AESStringEncryptionOO.decrypt(cipherText, password, salt, nonce));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
      assertTrue(false);
    }
  }


  @Test
  void testDecrypt() {
    assertTrue(false);
  }
  @Test
  void testGeneratePassword() {
    assertTrue(false);
  }
  @Test
  void testGenerateSalt() {
    assertTrue(false);
  }
  @Test
  void testGenerateNonce() {
    assertTrue(false);
  }
}
