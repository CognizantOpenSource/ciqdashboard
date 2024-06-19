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

package com.cognizant.authapi.users.util;

import com.cognizant.authapi.users.beans.Permission;
import com.cognizant.authapi.users.beans.Role;
import com.cognizant.authapi.users.dto.RoleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 *RoleUtil
 *
 * @author Cognizant
 */
@Component
public class RoleUtil {

    public RoleDTO convertToDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        roleDTO.setPermissionIds(
                role.getPermissions().stream().map(Permission::getId).collect(toList())
        );
        return roleDTO;
    }

    public List<RoleDTO> convertToDtoList(List<Role> roleList) {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        roleList.forEach(role -> roleDTOList.add(convertToDto(role)));
        return roleDTOList;
    }

    public Role convertToEntity(RoleDTO roleDTO, List<Permission> permissionList) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setPermissions(permissionList);
        return role;
    }

    public Role convertToNewEntity(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return role;
    }
}
