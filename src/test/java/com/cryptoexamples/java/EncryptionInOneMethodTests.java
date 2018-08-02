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
  public void testStringEncryptionPasswordBased() throws IOException {
    ExampleStringEncryptionPasswordBased.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
    errContent.flush();
    assertTrue(ExampleStringEncryptionPasswordBased.demonstratePasswordBasedSymmetricEncryption("plaintext",null));
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testStringEncryptionKeyBased() throws IOException {
    ExampleStringEncryptionKeyBased.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
    errContent.flush();
    assertTrue(ExampleStringEncryptionKeyBased.demonstrateKeyBasedSymmetricEncryption("plaintext"));
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testAsymmetricStringEncryption() {
    ExampleAsymmetricStringEncryption.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted and original plain text are the same: true"));
  }

  @Test
  public void testFileEncryption() throws IOException {
    ExampleFileEncryption.main(new String[1]);
    assertThat(errContent.toString(), containsString("Decrypted file content and original plain text are the same: true"));
    errContent.flush();
    assertTrue(ExampleFileEncryption.demonstrateFileEncryption("file.enc", "plaintext", null));
    assertThat(errContent.toString(), containsString("Decrypted file content and original plain text are the same: true"));
  }

  @Test
  public void testHash() throws IOException {
    ExampleHash.main(new String[1]);
    // uses string: "Text that should be authenticated by comparing the hash of it!"
    assertThat(errContent.toString(), containsString("jg0X629+SmdP0/LTHZV/3zXBrizM3/hptRZVIuTXSCtyaqAe0NB8KMld2qebBIXFS1yowCUpCPu93l/fPmKEXg=="));
    errContent.flush();
    assertTrue(ExampleHash.demonstrateHash("plaintext"));
    assertThat(errContent.toString(), containsString("jg0X629+SmdP0/LTHZV/3zXBrizM3/hptRZVIuTXSCtyaqAe0NB8KMld2qebBIXFS1yowCUpCPu93l/fPmKEXg=="));
  }

  @Test
  public void testSignature() throws IOException {
    ExampleSignature.main(new String[1]);
    assertThat(errContent.toString(), containsString("Signature is correct: true"));
    errContent.flush();
    assertTrue(ExampleSignature.demonstrateSignature("plainText"));
    assertThat(errContent.toString(), containsString("Signature is correct: true"));
  }

}
