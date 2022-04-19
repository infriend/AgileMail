/*
 * SecurityConfiguration.java
 *
 * Created on 2019-02-23, 7:35
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

import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.filter.CredentialsAuthFilter;
import edu.agiledev.agilemail.security.filter.CredentialsRefreshFilter;
import edu.agiledev.agilemail.security.service.CredentialsAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ACTUATOR_REGEX = "(/api)?/actuator/health";
    private static final String CONFIGURATION_REGEX = "(/api)?/v1/application/configuration";
    private static final String LOGIN_REGEX = "(/api)?/login";
    private static final String REGISTER_REGEX = "(/api)?/register";
    private final CredentialsAuthService authenticationService;
    private final TokenProvider tokenProvider;

    @Autowired
    public SecurityConfiguration(CredentialsAuthService authenticationService, TokenProvider tokenProvider) {
        this.authenticationService = authenticationService;
        this.tokenProvider = tokenProvider;
    }

    //配置Spring Security忽略的路径

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
//                .antMatchers(HttpMethod.OPTIONS, "/**")

                // allow anonymous resource requests
                .antMatchers(
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.json"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        final RequestMatcher negatedPublicMatchers = new NegatedRequestMatcher(new OrRequestMatcher(
                new RegexRequestMatcher(ACTUATOR_REGEX, "GET"),
                new RegexRequestMatcher(CONFIGURATION_REGEX, "GET"),
                new RegexRequestMatcher(LOGIN_REGEX, "POST"),
                new RegexRequestMatcher(REGISTER_REGEX, "POST")
        ));
        httpSecurity
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .requestMatchers(negatedPublicMatchers).authenticated()
                .and()
                .logout()
                .permitAll()
                .and()
                .addFilterAfter(new CredentialsAuthFilter(
                        negatedPublicMatchers, tokenProvider), BasicAuthenticationFilter.class)
                .addFilterAfter(new CredentialsRefreshFilter(tokenProvider), CredentialsAuthFilter.class);
    }
}
