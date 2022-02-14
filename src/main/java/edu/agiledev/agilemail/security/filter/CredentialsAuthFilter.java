/*
 * CredentialsAuthenticationFilter.java
 *
 * Created on 2019-02-23, 7:47
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
package edu.agiledev.agilemail.security.filter;

import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.http.HttpHeaders;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.model.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权过滤器，负责提取证书并验证
 */
public class CredentialsAuthFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(CredentialsAuthFilter.class);

    private final TokenProvider tokenProvider;
    private final RequestMatcher requestMatcher;

    public CredentialsAuthFilter(RequestMatcher requestMatcher, TokenProvider tokenProvider) {
        this.requestMatcher = requestMatcher;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //TODO：检查
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!requestMatcher.matches(httpServletRequest) || authenticate(httpServletRequest)) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("text/html");
            httpServletResponse.getWriter().write("Unauthorized\n");
            httpServletResponse.getWriter().close();
            httpServletResponse.flushBuffer();
        }
    }

    private boolean authenticate(HttpServletRequest httpServletRequest) {
        try {
            final Credentials credentials = fetchCredentials(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(credentials);
            return true;
        } catch (Exception ex) {
            log.info("Couldn't authenticate request");
        }
        return false;
    }

    private Credentials fetchCredentials(HttpServletRequest httpServletRequest) {
        final String token = httpServletRequest.getHeader(HttpHeaders.AUTHENTICATION);
        if (StringUtils.hasText(token)) {
            throw new AuthenticationException("AgileMail credentials headers missing");
        }
        if (!tokenProvider.validateToken(token)) {
            throw new AuthenticationException("Authentication error");
        }
        Authentication authentication = tokenProvider.getAuthentication(token);
        return (Credentials) authentication;

    }

}
