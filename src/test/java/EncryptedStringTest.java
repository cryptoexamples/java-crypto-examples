import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
      String testString = "Die heiße Zypernsonne quälte Max und Victoria ja böse auf dem Weg bis zur Küste";
      EncryptedString encryptedString = new EncryptedString().encrypt(testString, password);
      assertEquals(testString, encryptedString.decrypt(password));
    } catch (GeneralSecurityException e) {
      assertTrue(false);
    }
  }

  @Test
  public void testEncryptAndDecryptWithASCII() {
    try {
      String password = EncryptedString.generatePassword(32);
      String testString = "A quick movement of the enemy will jeopardize six gunboats.";
      EncryptedString encryptedString = new EncryptedString().encrypt(testString, password);
      assertEquals(testString, encryptedString.decrypt(password));
    } catch (GeneralSecurityException e) {
      assertTrue(false);
    }
  }
}
