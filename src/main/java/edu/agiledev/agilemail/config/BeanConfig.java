/*
 * SSLConfiguration.java
 *
 * Created on 2018-10-10, 18:52
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
package edu.agiledev.agilemail.config;

import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.security.CredentialsTokenProvider;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.utils.EncryptionUtil;
import edu.agiledev.agilemail.utils.SnowFlakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

/**
 * Created by Marc Nuri <marc@marcnuri.com> on 2018-10-10.
 */
@Configuration
public class BeanConfig {

    @Bean
    public MailSSLSocketFactory mailSSLSocketFactory() throws GeneralSecurityException {
        final MailSSLSocketFactory res = new MailSSLSocketFactory();
        res.setTrustAllHosts(true);
        return res;
    }

    @Bean
    public SnowFlakeIdGenerator getSnowFlakeIdGenerator(@Value("${dev.dataCenterId}") long dataCenterId,
                                                        @Value("${dev.machineId}") long machineId) {
        SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(dataCenterId, machineId);
        return idGenerator;
    }

    @Bean
    public EncryptionUtil getEncryptionUtil(@Value("${dev.security.aes.key}") String aesKey,
                                            @Value("${dev.security.aes.ivSeed}") String ivSeed) throws IllegalAccessException {
        return new EncryptionUtil(aesKey, ivSeed);
    }

    @Bean
    public TokenProvider getTokenProvider(@Value("${config.security.credentials.expiration}") long expiration,
                                          @Value("${config.security.credentials.refresh}") long refresh,
                                          @Value("${dev.security.jwt.key}") String secretKey) {
        CredentialsTokenProvider tokenProvider = new CredentialsTokenProvider(expiration, refresh, secretKey);
        return tokenProvider;
    }
}