/*
 * CredentialsRefreshFilter.java
 *
 * Created on 2019-02-22, 11:21
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

import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 刷新过滤器，在请求完成之后更新token
 */
public class RefreshFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;

    @Autowired
    public RefreshFilter(AuthenticationService credentialsService) {
        this.authenticationService = credentialsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //TODO：检查
        final Object authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authenticationService.refreshCredentials((Credentials) authentication, (HttpServletResponse) response);
        }
        chain.doFilter(request, response);
    }
}
