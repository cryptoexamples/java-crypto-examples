package com.cryptoexamples.java;

import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;


/**
 * Created by Kai on 10.04.2017.
 */
public class EncryptionInOneMethodTests {
  private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @BeforeClass
  public static void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterClass
  public static void tearDown() {
    System.setOut(null);
    System.setErr(null);
  }

  @After
  public void resetOut() {
    outContent.reset();
    errContent.reset();
  }

  @Test
  public void testStringEncryptionMain() {
    ExampleStringEncryptionInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testFileEncryptionMain() {
    ExampleFileEncryptionInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted file content and original plain text are the same: true"));
  }

}
