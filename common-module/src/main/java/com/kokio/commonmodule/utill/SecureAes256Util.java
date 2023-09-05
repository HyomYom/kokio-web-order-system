package com.kokio.commonmodule.utill;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecureAes256Util {


  private static final byte[] IV = new byte[16];
  private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String KEY_ALGORITHM = "AES";
  private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(IV);

  public static String encrypt(String secretKey, String text) {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), KEY_ALGORITHM);

      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IV_PARAMETER_SPEC);
      byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(IV_PARAMETER_SPEC.getIV()) + ":"
          + Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      return null;
    }
  }

  public static String decrypt(String secretKey, String encryptedText) {
    try {
      String[] parts = encryptedText.split(":");
      byte[] ivBytes = Base64.getDecoder().decode(parts[0]);
      byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), KEY_ALGORITHM);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IV_PARAMETER_SPEC);
      byte[] decrypted = cipher.doFinal(encryptedBytes);
      return new String(decrypted, StandardCharsets.UTF_8);

    } catch (Exception e) {
      return null;
    }
  }

}
