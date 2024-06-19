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

package com.cognizant.authapi.base.security;

import com.cognizant.authapi.users.beans.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 *CustomPermissionEvaluator
 *
 * @author Cognizant
 */

@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Value("${app.permission.admin}")
    private String adminPermission;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {

        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege((UserPrincipal) auth.getPrincipal(), permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        if (StringUtils.isEmpty(targetId))
            return hasPrivilege((UserPrincipal) auth.getPrincipal(), permission.toString());
        else
            return hasResourceAndPrivilege((UserPrincipal) auth.getPrincipal(), targetId.toString(), permission.toString());
    }

    private boolean hasPrivilege(UserPrincipal auth, String permission) {
        SimpleGrantedAuthority permissionAuthority = new SimpleGrantedAuthority(permission);
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(adminPermission);

        return auth.getAuthorities().contains(permissionAuthority) || auth.getAuthorities().contains(adminAuthority);
    }

    private boolean hasResourceAndPrivilege(UserPrincipal auth, String targetId, String permission) {
        log.info("checking permission & targetId: " + permission + " & " + targetId);
        SimpleGrantedAuthority permissionAuthority = new SimpleGrantedAuthority(permission);
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(adminPermission);
        return auth.getAuthorities().contains(adminAuthority) ||
                (auth.getAccount().hasProject(targetId) && auth.getAuthorities().contains(permissionAuthority)) ||
                auth.getAccount().hasOwnProject(targetId);
    }
}