/*
 * WebConfiguration.java
 *
 * Created on 2018-08-09, 7:55
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Stream;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * web相关配置
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer, AsyncConfigurer {

    public static final String IMAP_SERVICE_PROTOTYPE = "prototypeImapService";
    private static final String DEVELOPMENT_PROFILE = "dev";

    private final Environment environment;

    @Autowired
    public WebConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(getAsyncExecutor());
    }

    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        return executor;
    }


    /**
     * 解决跨域问题，我也不知道为什么原理，反正work了
     * 参考链接 https://stackoverflow.com/questions/40418441/spring-security-cors-filter
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        corsConfigurationSource.registerCorsConfiguration("/**",
//                new CorsConfiguration().applyPermitDefaultValues());
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigurationSource;
    }
//
//    @Bean(name = IMAP_SERVICE_PROTOTYPE)
//    @Scope(SCOPE_PROTOTYPE)
//    @Qualifier(IMAP_SERVICE_PROTOTYPE)
//    public ImapService imapService(
//            IsotopeApiConfiguration isotopeApiConfiguration, MailSSLSocketFactory mailSSLSocketFactory,
//            CredentialsService credentialsService) {
//
//        return new ImapService(isotopeApiConfiguration, mailSSLSocketFactory, credentialsService);
//    }
}
