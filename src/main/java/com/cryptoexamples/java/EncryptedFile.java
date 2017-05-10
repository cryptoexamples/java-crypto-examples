package com.cryptoexamples.java;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Encapsulating class for saving a String encrypted in a file and to decrypt/retrieve it.
 *
 * Including
 * - random password generation,
 * - random salt generation,
 * - key derivation using PBKDF2 HMAC SHA-256,
 * - AES-256 authenticated encryption using GCM
 * - BASE64-encoding for the byte-arrays
 * // TODO store all encryption parameters (as authenticated data) prepended to the file content
 * // TODO use Cryptographic Message Snytax (https://tools.ietf.org/html/rfc5652)
 */
public class EncryptedFile implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(EncryptedFile.class.getName());

  /* 128, 120, 112, 104, or 96 @see NIST Special Publication 800-38D*/
  private static final int DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS = 128;

  private static final int DEFAULT_GCM_IV_NONCE_SIZE_BYTES = 12;
  private static final int DEFAULT_PBKDF2_ITERATIONS = 65536;
  private static final int DEFAULT_PBKDF2_SALT_SIZE_BYTES = 32;

  /* @see https://www.keylength.com/ */
  private static final int DEFAULT_AES_KEY_LENGTH_BITS = 256;
  private static final String DEFAULT_CIPHER = "AES";
  private static final String DEFAULT_CIPHERSCHEME = "AES/GCM/PKCS5Padding";
  private static final String DEFAULT_PBKDF2_SCHEME = "PBKDF2WithHmacSHA256";

  private int gcmAuthenticationTagSizeBits = DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS;
  private int gcmIvNonceSizeBytes = DEFAULT_GCM_IV_NONCE_SIZE_BYTES;
  private int pbkdf2Iterations = DEFAULT_PBKDF2_ITERATIONS;
  private int pbkdf2SaltSizeBytes = DEFAULT_PBKDF2_SALT_SIZE_BYTES;
  private int aesKeyLengthBits = DEFAULT_AES_KEY_LENGTH_BITS;
  private String cipher = DEFAULT_CIPHER;
  private String cipherscheme = DEFAULT_CIPHERSCHEME;
  private String pbkdf2Scheme = DEFAULT_PBKDF2_SCHEME;

  private byte[] nonce;
  private byte[] salt;
  private String cipherText;
  private String path;

  /**
   * Creates a new EncryptedFile object based on cipherText, nonce and salt.
   *
   * @param cipherText encrypted plaintext (generated from encrypt)
   * @param nonce      byte array, number used once (random) see gcmIvNonceSizeBytes
   * @param salt       random byte array to prevent rainbow table attacks on password lists
   */
  public EncryptedFile(String cipherText, String path, byte[] nonce, byte[] salt) {
    this.cipherText = cipherText;
    this.nonce = nonce;
    this.salt = salt;
    this.path = path;
  }

  /**
   * Initializes this EncryptedFile object with the provided parameters
   *
   * @param cipher
   * @param cipherscheme
   * @param gcmAuthenticationTagSizeBits
   * @param gcmIvNonceSizeBytes
   * @param pbkdf2Iterations
   * @param pbkdf2SaltSizeBytes
   * @param aesKeyLengthBits
   * @param pbkdf2Scheme
   */
  private EncryptedFile(String cipherText, String path, byte[] nonce, byte[] salt, String cipher, String cipherscheme, int gcmAuthenticationTagSizeBits, int gcmIvNonceSizeBytes, int pbkdf2Iterations, int pbkdf2SaltSizeBytes, int aesKeyLengthBits, String pbkdf2Scheme) {
    this.cipherText = cipherText;
    this.nonce = nonce;
    this.salt = salt;
    this.path = path;

    this.cipher = cipher;
    this.cipherscheme = cipherscheme;
    this.gcmAuthenticationTagSizeBits = gcmAuthenticationTagSizeBits;
    this.gcmIvNonceSizeBytes = gcmIvNonceSizeBytes;
    this.pbkdf2Iterations = pbkdf2Iterations;
    this.pbkdf2SaltSizeBytes = pbkdf2SaltSizeBytes;
    this.aesKeyLengthBits = aesKeyLengthBits;
    this.pbkdf2Scheme = pbkdf2Scheme;
  }

  /**
   * Creates a new empty EncryptedFile object
   */
  public EncryptedFile() {
    // uses default parameters, see initialization at the beginning.
  }

  /**
   * Generates a randomly filled byte array
   *
   * @param sizeInBytes length of the array in bytes
   * @return byte array containing random values
   * @throws NoSuchAlgorithmException
   */
  private static byte[] generateRandomArry(int sizeInBytes) throws NoSuchAlgorithmException {
    /* generate random salt */
    final byte[] salt = new byte[sizeInBytes];
    SecureRandom random = SecureRandom.getInstanceStrong();
    random.nextBytes(salt);
    return salt;
  }

  /**
   * Generates a random password.
   *
   * @param sizeInBytes length of the password in byte
   * @return Base64 encoded string with a random password
   * @throws NoSuchAlgorithmException
   */
  public static String generatePassword(int sizeInBytes) throws NoSuchAlgorithmException {
    return Base64.getEncoder().encodeToString(generateRandomArry(sizeInBytes));
  }

  private byte[] getNonce() {
    return this.nonce;
  }

  private byte[] getSalt() {
    return this.salt;
  }

  private String getCipherText() {
    return this.cipherText;
  }

  private String getPath() {
    return this.path;
  }

  /**
   * Encrypts the provided plainText using the provided password and stores it in a file
   *
   * @param plainText plaintext that should be encrypted
   * @param password  password which is used to generate the key
   * @param path path to a writeable file (may already exist)
   * @throws GeneralSecurityException
   */
  public void encrypt(String plainText, String password, String path) throws GeneralSecurityException {
    /* Derive the key*/
    SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkdf2Scheme);
    byte[] newSalt = generateRandomArry(pbkdf2SaltSizeBytes);
    KeySpec keyspec = new PBEKeySpec(password.toCharArray(), newSalt, pbkdf2Iterations, aesKeyLengthBits);
    SecretKey tmp = factory.generateSecret(keyspec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), cipher);

    Cipher myCipher = Cipher.getInstance(cipherscheme);
    byte[] newNonce = generateRandomArry(gcmIvNonceSizeBytes);
    GCMParameterSpec spec = new GCMParameterSpec(gcmAuthenticationTagSizeBits, newNonce);
    myCipher.init(Cipher.ENCRYPT_MODE, key, spec);

    // SET UP OUTPUT STREAM and write content of String
    try (
      FileOutputStream fileOutputStream = new FileOutputStream(path);
      CipherOutputStream encryptedOutputStream = new CipherOutputStream(fileOutputStream, myCipher);
      InputStream stringInputStream = new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8));
    ) {
      // write IV/nonce
      fileOutputStream.write(newNonce);

      // write salt
      fileOutputStream.write(newSalt);


      byte[] buffer = new byte[8192];
      while (stringInputStream.read(buffer) > 0) {
        encryptedOutputStream.write(buffer);
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      throw new SecurityException(e.getMessage(), e);
    }
  }

  /**
   * Decrypts the cipherText using the provided password.
   *
   * @param password password which is used to generate the key
   * @param path path to a previously encrypted file to be decrypted
   * @return plaintext
   * @throws GeneralSecurityException
   */
  public String decrypt(String password, String path ) throws GeneralSecurityException {

    // Read configuration from file

    byte[] nonce = new byte[gcmIvNonceSizeBytes];
    byte[] salt = new byte[pbkdf2SaltSizeBytes];

    try (
            FileInputStream fileInputStream = new FileInputStream(path);
    ) {
      fileInputStream.read(nonce);
      fileInputStream.read(salt);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      throw new SecurityException(e.getMessage(), e);
    }

    /* Derive the key*/
    SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkdf2Scheme);
    // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
    KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2Iterations, aesKeyLengthBits);
    SecretKey tmp = factory.generateSecret(keyspec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), cipher);

    Cipher myCipher = Cipher.getInstance(cipherscheme);
    GCMParameterSpec spec = new GCMParameterSpec(gcmAuthenticationTagSizeBits, nonce);

    myCipher.init(Cipher.DECRYPT_MODE, key, spec);

    // READ ENCRYPTED FILE
    StringBuilder stringBuilder = new StringBuilder();

    //cipher.updateAAD(aad);
    try (
      FileInputStream fileInputStream = new FileInputStream(path);
      CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, myCipher);
    ) {
      // offset the stream by the bytes already read previosly

      // TODO check if file has this bytes at least.
      byte[] skipped = new byte[gcmIvNonceSizeBytes+pbkdf2SaltSizeBytes];
      fileInputStream.read(skipped);

      byte[] buffer = new byte[8192];
      while (cipherInputStream.read(buffer) > 0) {
        stringBuilder.append(new String(buffer, StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      throw new SecurityException(e.getMessage(), e);
    }

    // TODO trim() should not be needed!
    String decryptedCipherText = stringBuilder.toString().trim();

    return new String(decryptedCipherText);
  }
}

