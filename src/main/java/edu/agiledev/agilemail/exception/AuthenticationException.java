/*
 * AuthenticationException.java
 *
 * Created on 2018-08-18, 7:47
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
