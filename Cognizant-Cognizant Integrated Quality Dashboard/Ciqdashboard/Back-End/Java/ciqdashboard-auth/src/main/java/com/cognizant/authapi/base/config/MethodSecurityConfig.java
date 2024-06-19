
/*
 *  © [2021] Cognizant. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */



package com.cognizant.authapi.base.config;

import com.cognizant.authapi.base.security.CustomPermissionEvaluator;
import com.cognizant.authapi.users.beans.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * MethodSecurityConfig
 *
 * @author Cognizant
 */


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    @Value("${app.permission.admin:permission.admin}")
    private String adminPermission;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }

    @Bean("adminValidator")
    public Predicate<UserPrincipal> getAdminValidator() {
        return userPrincipal -> Objects.nonNull(userPrincipal.getAuthorities()) && userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).anyMatch(adminPermission::equalsIgnoreCase);
    }

}