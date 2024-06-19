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

package com.cognizant.authapi.users.services;

import com.cognizant.authapi.users.beans.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *UserValidationService
 *
 * @author Cognizant
 */

@Service
public class UserValidationService {

    @Value("${app.permission.admin}")
    private String adminPermission;

    public boolean isAdmin() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(adminPermission);
        return userPrincipal.getAuthorities().contains(adminAuthority);
    }

    public UserPrincipal getLoggedInUser() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth instanceof UserPrincipal) {
            return (UserPrincipal) auth;
        } else {
            throw new AuthenticationCredentialsNotFoundException("user not signed in");
        }
    }

    public String getCurrentUserEmailId() {
        UserPrincipal loggedInUser = getLoggedInUser();
        return loggedInUser.getEmail();
    }

    public List<String> getCurrentUserProjectIds() {
        UserPrincipal loggedInUser = getLoggedInUser();
        return loggedInUser.getAccount().getProjectIds();
    }
}
