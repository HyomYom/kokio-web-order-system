package com.kokio.userapi.utill;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecureAes256Util {

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String KEY = System.getenv("ENCRYPTION_KEY"); // Store key in environment variable
  private static final SecureRandom secureRandom = new SecureRandom();

  public static String encrypt(String text){
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
      IvParameterSpec ivParameterSpec = generateIv();
      log.info(ivParameterSpec.getIV().toString());

      cipher.init(Cipher.ENCRYPT_MODE,keySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(ivParameterSpec.getIV())+":"+Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      return null;
    }
  }
  public static String decrypt(String encryptedText){
    try {
      String[] parts = encryptedText.split(":");
      byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
      byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

      Cipher cipher = Cipher.getInstance(ALGORITHM);
      SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
      IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

      cipher.init(Cipher.DECRYPT_MODE ,keySpec, ivParameterSpec);
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
      return new String(decryptedBytes, StandardCharsets.UTF_8);



    } catch (Exception e){
      return null;
    }
  }
  private static IvParameterSpec generateIv(){
    byte[] iv = new byte[16];
    secureRandom.nextBytes(iv);
    log.info(iv.toString());
    return new IvParameterSpec(iv);
  }
}
