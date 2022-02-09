/*
 * IsotopeException.java
 *
 * Created on 2018-08-08, 17:19
 *
 * Copyright 2018 Marc Nuri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.agiledev.agilemail.exception;

import edu.agiledev.agilemail.pojo.ReturnCode;


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
