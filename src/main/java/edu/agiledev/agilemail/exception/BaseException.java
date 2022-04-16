package edu.agiledev.agilemail.exception;

import edu.agiledev.agilemail.pojo.model.ReturnCode;


/**
 * 异常基类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/12
 */
public class BaseException extends RuntimeException {

//    private static final int MISCELLANEOUS_HTTP_WARN_CODE = 199;
//    private static final int MAX_HEADER_LENGTH = 500;

    private final ReturnCode code;

    public BaseException() {
        this(null);
    }

    public BaseException(String message) {
        this(ReturnCode.ERROR, message, null);
    }

    public BaseException(ReturnCode code, String message) {
        this(code, message, null);
    }

    public BaseException(String message, Throwable cause) {
        this(ReturnCode.ERROR, message, cause);
    }

    public BaseException(ReturnCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ReturnCode getCode() {
        return code;
    }

//    public final ResponseEntity<String> toFrontEndResponseEntity() {
//        final String message = getMessage();
//        final HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.WARNING, String.format("%s %s \"%s\"",
//                MISCELLANEOUS_HTTP_WARN_CODE, "-",
//                message == null ? "" :
//                        message.substring(0, Math.min(message.length(), MAX_HEADER_LENGTH))
//                                .replaceAll("[\\n\\r]", "")));
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        return new ResponseEntity<>(message, headers, getHttpStatus());
//    }

}
