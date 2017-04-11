/**
 * Created by Kai on 10.04.2017.
 */
class AESStringEncryptionOOTest extends groovy.util.GroovyTestCase {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  void setUp() {
  super.setUp()
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  void tearDown() {
    System.setOut(null);
    System.setErr(null);
  }

  void testMain() {
    AESStringEncryptionOO.main();
    assertEquals("Decrypted and original plain text are the same: true", outContent.toString());
  }

  void testEncrypt() {
    assertTrue(false);
  }

  void testDecrypt() {
    assertTrue(false);
  }

  void testGeneratePassword() {
    assertTrue(false);
  }

  void testGenerateSalt() {
    assertTrue(false);
  }

  void testGenerateNonce() {
    assertTrue(false);
  }
}
