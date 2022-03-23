package edu.agiledev.agilemail.utils;

import javax.mail.URLName;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 编码帮助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
public class EncodeUtil {

    public static String toBase64Id(URLName id) {
        return encode(id.toString());
    }

    public static URLName toUrl(String base64Id) {
        return new URLName(decode(base64Id));
    }

    private static String encode(String id) {
        return Base64.getUrlEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String encodedId) {
        return new String(Base64.getUrlDecoder().decode(encodedId), StandardCharsets.UTF_8);
    }
}
