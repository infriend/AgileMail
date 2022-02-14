package edu.agiledev.agilemail.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 以AES算法为基础的加密解密工具类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/12
 */
@Slf4j
public class EncryptionUtil {

    private static final String PAT = "[0-9a-zA-Z]+";

    private static final String ENCODING = "UTF-8";
    private static final String AES = "AES";
    // "算法/模式/补码方式"
    private static final String CIPHER_PADDING = "AES/CBC/PKCS5Padding";

    private final String aesKey;
    //偏移量(CBC中使用，增强加密算法强度)
    private final String ivSeed;

    public EncryptionUtil(String aesKey, String ivSeed) throws IllegalAccessException {
        if (aesKey == null || aesKey.length() != 16)
            throw new IllegalAccessException("AesKey length should be 16!");
        if (!isAlphaNumeric(aesKey))
            throw new IllegalAccessException("AesKey should consists of only letters and numbers!");
        if (ivSeed == null)
            throw new IllegalAccessException("Iv seed should not be null!");
        this.aesKey = aesKey;
        this.ivSeed = ivSeed;
    }


    public String encrypt(@NonNull String content) {
        try {
            byte[] keyBytes = aesKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, AES);
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            IvParameterSpec iv = new IvParameterSpec(ivSeed.getBytes(ENCODING));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            log.info("AES_CBC encrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String decrypt(@NonNull String content) {
        try {
            byte[] bytes = aesKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES);
            IvParameterSpec iv = new IvParameterSpec(ivSeed.getBytes(ENCODING));
            Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] decodeBase64 = Base64.decodeBase64(content);
            byte[] decrypted = cipher.doFinal(decodeBase64);
            return new String(decrypted, ENCODING);
        } catch (Exception e) {
            log.info("AES_CBC decrypt exception:" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private static boolean isAlphaNumeric(String s) {
        Pattern p = Pattern.compile(PAT);
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
