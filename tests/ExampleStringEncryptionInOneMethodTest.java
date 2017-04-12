import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Kai on 10.04.2017.
 */
public class ExampleStringEncryptionInOneMethodTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testMain() {
    ExampleStringEncryptionInOneMethod.main(new String[1]);
    assertEquals("Decrypted and original plain text are the same: true", outContent.toString());
  }

}
