import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Kai on 12.04.2017.
 */
public class EncryptedStringTest {


  public void setUp() {

  }


  public void tearDown() {

  }
  @Test
  public void testEncryptAndDecryptWithUmlaut() {
    try {
      String password = EncryptedString.generatePassword(32);
      String testString = "ä";
      EncryptedString encryptedString = new EncryptedString().encrypt(testString, password);
      assertEquals(testString, encryptedString.decrypt(password));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
      assertTrue(false);
    }
  }

  @Test
  public void testEncryptAndDecryptWithASCII() {
    try {
      String password = EncryptedString.generatePassword(32);
      String testString = "ASDF";
      EncryptedString encryptedString = new EncryptedString().encrypt(testString, password);
      assertEquals(testString, encryptedString.decrypt(password));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
      assertTrue(false);
    }
  }
}
