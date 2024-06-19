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

package com.cognizant.authapi.collector.service;

import com.cognizant.authapi.base.beans.JwtSecurityConstants;
import com.cognizant.authapi.base.error.ThrowException;
import com.cognizant.authapi.base.services.JwtTokenService;
import com.cognizant.authapi.collector.model.BaseConstants;
import com.cognizant.authapi.users.beans.Permission;
import com.cognizant.authapi.users.beans.Role;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.services.AccountService;
import com.cognizant.authapi.users.services.PermissionService;
import com.cognizant.authapi.users.services.RoleService;
import com.cognizant.authapi.users.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CollectorService - Services to create user , token ,roles, permission.
 *
 * @author Cognizant
 */
@Service
@Slf4j
@AllArgsConstructor
public class CollectorService {

    private UserService userService;
    private AccountService accountService;
    private RoleService roleService;
    private PermissionService permissionService;
    private JwtTokenService jwtTokenService;

    public Map<String, Object> generateCollectorToken(String email, int days) {
        User user = validateAndGenerateUser(email);
        Map<String, Object> map = jwtTokenService.generateToken(user, days);
        map.put(JwtSecurityConstants.COLLECTOR_TOKEN, map.remove(JwtSecurityConstants.AUTH_TOKEN));
        map.put(JwtSecurityConstants.TOKEN_EXPIRES_AT, map.remove(JwtSecurityConstants.EXPIRES_AT));
        return map;
    }

    private User validateAndGenerateUser(String email) {
        User getUser = userService.getUserByEmail(email).orElseGet(() -> {
            User user = userService.signUpUser(userService.generateUserDTO(email));
            Role role = roleService.getRole(BaseConstants.CHART_COLLECTOR).orElseGet(() -> {
                List<Permission> permissions = permissionService.getAllPermissionByIdLike(BaseConstants.CIQDASHBOARD_CHART);
                permissions.addAll(permissionService.getAllPermissionByIdLike(BaseConstants.CIQDASHBOARD_APP_STATUS));
                Role newRole = new Role();
                newRole.setName(BaseConstants.CHART_COLLECTOR);
                newRole.setPermissions(permissions);
                return roleService.addNewRole(newRole);
            });
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.getAccount().setRoles(roles);
            user.setActive(true);
            accountService.updateAccount(user.getAccount());
            return userService.updateUser(user);
        });
        List<Role> roleList = getUser.getAccount().getRoles().stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getName().equalsIgnoreCase(BaseConstants.CHART_COLLECTOR))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleList)) {
            throw new ThrowException(String.format(BaseConstants.USER_DOES_NOT_HAVE_THE_S_ROLE, BaseConstants.CHART_COLLECTOR));
        }
        return getUser;
    }
}
