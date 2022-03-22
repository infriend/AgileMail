package edu.agiledev.agilemail.exception;

import edu.agiledev.agilemail.pojo.model.ReturnCode;

/**
 * 认证错误类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/9
 */
public class AuthenticationException extends BaseException {

    public enum Type {
        BLACKLISTED, IMAP, SMTP
    }

    public AuthenticationException(Type type) {
        this(String.format("Authentication Error: %s", type.name()));
    }

    public AuthenticationException(String message) {
        this(message, null);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(ReturnCode.ERROR, message, cause);
    }
}
