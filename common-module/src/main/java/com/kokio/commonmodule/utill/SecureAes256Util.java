package com.kokio.commonmodule.utill;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecureAes256Util {


  private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

  public static String encrypt(SecretKey secretKeySpec, String text) {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      SecureRandom secureRandom = new SecureRandom();

      //AES 암호화에서는 IV의 크기가 128비트, 즉 16바이트여야 함
      byte[] iv = new byte[16];
      secureRandom.nextBytes(iv);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(ivParameterSpec.getIV()) + ":"
          + Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      return null;
    }
  }

  public static String decrypt(SecretKey secretKeySpec, String encryptedText) {
    try {
      String[] parts = encryptedText.split(":");
      byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
      byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decrypted = cipher.doFinal(encryptedBytes);
      return new String(decrypted, StandardCharsets.UTF_8);

    } catch (Exception e) {
      return null;
    }
  }

}
