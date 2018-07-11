package com.cryptoexamples.java;

import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


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
  public void testStringEncryptionPasswordBasedMain() throws IOException {
    ExampleStringEncryptionPasswordBasedInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
    errContent.flush();
    assertTrue(ExampleStringEncryptionPasswordBasedInOneMethod.demonstratePasswordBasedSymmetricEncryption("plaintext",null));
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testStringEncryptionKeyBasedMain() {
    ExampleStringEncryptionKeyBasedInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testAsymmetricStringEncryptionMain() {
    ExampleAsymmetricStringEncryptionInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testFileEncryptionMain() {
    ExampleFileEncryptionInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted file content and original plain text are the same: true"));
  }

  @Test
  public void testHashMain() {
    ExampleHashInOneMethod.main(new String[1]);
    // uses string: "Text that should be authenticated by comparing the hash of it!"
    assertThat(errContent.toString(), containsString("jg0X629+SmdP0/LTHZV/3zXBrizM3/hptRZVIuTXSCtyaqAe0NB8KMld2qebBIXFS1yowCUpCPu93l/fPmKEXg=="));
  }

  @Test
  public void testSignatureMain() {
    ExampleSignatureInOneMethod.main(new String[1]);
    assertThat(errContent.toString(), containsString("Signature is correct: true"));
  }

}
