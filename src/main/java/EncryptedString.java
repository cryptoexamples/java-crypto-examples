import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptedString {
  public int GCM_AUTHENTICATION_TAG_SIZE_BITS = 128;
  public int GCM_IV_NONCE_SIZE_BYTES = 12;
  public int PBKDF2_ITERATIONS = 65536;
  public int PBKDF2_SALT_SIZE_BYTES = 32;
  public int AES_KEY_LENGTH_BITS = 256;

  private byte[] nonce;
  private byte[] salt;
  private String cipherText;

  public EncryptedString(String cipherText, byte[] nonce, byte[] salt) {
    this.cipherText = cipherText;
    this.nonce = nonce;
    this.salt = salt;
  }

  public EncryptedString() {

  }

  public byte[] getNonce() {
    return this.nonce;
  }

  public byte[] getSalt() {
    return this.salt;
  }

  public String getCipherText() {
    return this.cipherText;
  }

  public static byte[] generateSalt(int sizeInBytes) throws NoSuchAlgorithmException {
    /* generate random salt */
    final byte[] salt = new byte[sizeInBytes];
    SecureRandom random = SecureRandom.getInstanceStrong();
    random.nextBytes(salt);
    return salt;
  }

  public byte[] generateNonce(int sizeInBytes) throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstanceStrong();
    final byte[] nonce = new byte[sizeInBytes];
    random.nextBytes(nonce);
    return nonce;
  }

  public EncryptedString encrypt(String plainText, String password) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
    /* Derive the key*/
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
    EncryptedString encryptedString = new EncryptedString();
    byte[] salt = generateSalt(PBKDF2_SALT_SIZE_BYTES);
    KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, AES_KEY_LENGTH_BITS);
    SecretKey tmp = factory.generateSecret(keyspec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
    byte[] nonce = generateNonce(GCM_IV_NONCE_SIZE_BYTES);
    GCMParameterSpec spec = new GCMParameterSpec(GCM_AUTHENTICATION_TAG_SIZE_BITS, nonce);
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);

    byte[] byteCipher = cipher.doFinal(plainText.getBytes());

    return new EncryptedString(new String(Base64.getEncoder().encode(byteCipher)),nonce, salt);
  }

  public String decrypt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    /* Derive the key*/
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
    KeySpec keyspec = new PBEKeySpec(password.toCharArray(), getSalt(), PBKDF2_ITERATIONS, AES_KEY_LENGTH_BITS);
    SecretKey tmp = factory.generateSecret(keyspec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
    GCMParameterSpec spec = new GCMParameterSpec(GCM_AUTHENTICATION_TAG_SIZE_BITS, getNonce());

    cipher.init(Cipher.DECRYPT_MODE, key, spec);

    byte[] decryptedCipher = cipher.doFinal(Base64.getDecoder().decode(getCipherText()));
    return new String(decryptedCipher);
  }

  public static String generatePassword(int sizeInBits) throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    // Needs unlimited strength policy files http://www.oracle.com/technetwork/java/javase/downloads
    keyGen.init(sizeInBits);
    return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
  }
}

