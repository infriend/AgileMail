package edu.agiledev.agilemail.utils;

import javax.mail.URLName;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Description of the class
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
public class EncodeUtil {

    public static String toBase64Id(URLName id) {
        return encodeId(id.toString());
    }

    public static URLName toId(String base64Id) {
        return new URLName(decodeId(base64Id));
    }

    private static String encodeId(String id) {
        return Base64.getUrlEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeId(String encodedId) {
        return new String(Base64.getUrlDecoder().decode(encodedId), StandardCharsets.UTF_8);
    }
}
