/*
 * CredentialsService.java
 *
 * Created on 2018-08-15, 21:46
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
package edu.agiledev.agilemail.security.service;

import edu.agiledev.agilemail.config.ApplicationConfiguration;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.http.HttpHeaders;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.model.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 提供身份认证相关服务
 */
@Service
@Slf4j
public class CredentialsAuthService {


    private final ApplicationConfiguration appConfig;

    private final TokenProvider tokenProvider;

    @Autowired
    public CredentialsAuthService(ApplicationConfiguration appConfig, TokenProvider tokenProvider) {
        this.appConfig = appConfig;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Parses {@link HttpServletRequest} for isotope credentials to decode them and return
     * a valid {@link Credentials} object.
     *
     * @param httpServletRequest from which to extract Isotope Credentials HttpHeaders
     * @return Credentials obtained and validated from httpServletRequest headers
     */
    public Credentials fromRequest(HttpServletRequest httpServletRequest) {
        final String token = httpServletRequest.getHeader(HttpHeaders.AUTHENTICATION);
        if (StringUtils.hasText(token)) {
            throw new AuthenticationException("AgileMail credentials headers missing");
        }
        if(!tokenProvider.validateToken(token)){
            throw new AuthenticationException("Authentication error");
        }
        Authentication authentication = tokenProvider.getAuthentication(token);
        return (Credentials) authentication;

    }


    public void refreshCredentials(Credentials oldCredentials, HttpServletResponse response) {
        String oldToken = tokenProvider.generateToken(oldCredentials);
        if (tokenProvider.validateToken(oldToken)) {
            response.setHeader(HttpHeaders.AUTHENTICATION, tokenProvider.refreshToken(oldToken));
        }

    }

//    private Credentials encrypt(String userId) {
//        final Credentials credentials = new Credentials();
//        // Add expiry date
//        credentials.setExpiryDate(ZonedDateTime.now(ZoneOffset.UTC).plus(Duration.ofMinutes(appConfig.getCredentialsDurationMinutes())));
//        // Perform encryption
//        credentials.setSalt(KeyGenerators.string().generateKey());
//        final TextEncryptor encryptor = Encryptors.text(appConfig.getEncryptionPassword(),
//                credentials.getSalt());
//        credentials.setEncrypted(encryptor.encrypt(userId));
//        return credentials;
//    }

//    private String decrypt(String token) throws IOException {
//        if (token == null || token.isEmpty()) {
//            throw new AuthenticationException("Missing encrypted credentials");
//        }
//        try {
//            final TextEncryptor encryptor = Encryptors.text(appConfig.getEncryptionPassword(), salt);
//            return encryptor.decrypt(token);
//        } catch (IllegalStateException ex) {
//            throw new AuthenticationException("Key or salt is not compatible with encrypted credentials" +
//                    " (Server has changed the password or user tampered with credentials.", ex);
//        }
//    }
}
