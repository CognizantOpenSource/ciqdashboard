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

package com.cognizant.authapi.users.controllers;

import com.cognizant.authapi.base.error.PermissionNotFoundException;
import com.cognizant.authapi.base.error.RoleExistsException;
import com.cognizant.authapi.base.error.RoleNotFoundException;
import com.cognizant.authapi.users.beans.Permission;
import com.cognizant.authapi.users.beans.Role;
import com.cognizant.authapi.users.dto.RoleDTO;
import com.cognizant.authapi.users.services.PermissionService;
import com.cognizant.authapi.users.services.RoleService;
import com.cognizant.authapi.users.util.RoleUtil;
import com.mongodb.DuplicateKeyException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *RoleController
 *
 * @author Cognizant
 */
@RestController
@RequestMapping(value = "/roles")
@AllArgsConstructor
@Slf4j
public class RoleController {

    private RoleService roleService;
    private PermissionService permissionService;
    private RoleUtil util;

    /**
     * Getting all Roles
     * @return list of roles
     */
    @GetMapping(value = "")
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.read')")
    public List<RoleDTO> getAllRoles() {
        log.info("Getting all the Roles from Database.....!");
        return util.convertToDtoList(roleService.getAllRoles());
    }

    /**
     * Getting a Role by ID
     * @param roleId id of role
     * @return role
     */
    @GetMapping(value = "/{roleId}")
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.read')")
    public RoleDTO getRole(@PathVariable String roleId) {
        log.info("Getting Project id is : " + roleId);
        Optional<Role> optionalRole = roleService.getRole(roleId);
        if (optionalRole.isPresent()) {
            return util.convertToDto(optionalRole.get());
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }

    /**
     * Creating Role
     * In RoleDTO Provide Permission Ids instead of permissions List
     * @param roleDTO role details which are store in the db
     * @return posting storing db return the stored roles
     */
    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "")
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.create')")
    public RoleDTO addNewRole(@Valid @RequestBody RoleDTO roleDTO) {
        log.info("Adding new Role, Role: " + roleDTO.toString());
        Role role = util.convertToEntity(
                roleDTO,
                getPermissionList(roleDTO.getPermissionIds())
        );
        try {
            return util.convertToDto(roleService.addNewRole(role));
        } catch (DuplicateKeyException e) {
            throw new RoleExistsException("RoleId", roleDTO.getName());
        }
    }

    /**
     * In the roleDTOList send permission id's instead of sending permissions list
     * @param roleDTOList  list of role details which need to store in db
     * @return list of roles, post storing in db
     */
    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/addAll")
    @Transactional
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.create')")
    public List<RoleDTO> addMultipleNewRoles(@Valid @RequestBody List<RoleDTO> roleDTOList) {
        log.info("Adding new Role, Role: " + roleDTOList.toString());
        List<Role> roleList = roleDTOList.stream()
                .map(roleDTO -> util.convertToEntity(
                        roleDTO
                        , getPermissionList(roleDTO.getPermissionIds())
                        )
                ).collect(Collectors.toList());
        return util.convertToDtoList(roleService.addRolesList(roleList));
    }

    /**
     * Updating a Role
     * In RoleDTO Provide Permission Ids instead of permissions List
     * @param roleDTO role details which needs to store in db
     * @return updated role details
     */
    @Validated
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{roleId}")
    @Transactional
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.update')")
    public RoleDTO updateRole(@Valid @RequestBody RoleDTO roleDTO) {
        log.info("Updating the Role data " + roleDTO.toString());
        assertRole(roleDTO.getName());
        Role role = util.convertToEntity(
                roleDTO,
                getPermissionList(roleDTO.getPermissionIds())
        );
        return util.convertToDto(roleService.updateRole(role));
    }

    /**
     * Remove Role based on the Role Id
     * @param roleId id of the role
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{roleId}")
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.delete')")
    public void removeRole(@PathVariable String roleId) {
        log.info("Deleting Role based on id, id is : " + roleId);
        assertRole(roleId);
        roleService.removeRole(roleId);
    }

    /**
     * Remove Roles based on the list of Role Ids
     * @param roleIds list of role's ids
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/remove")
    @PreAuthorize("hasPermission('Role','ciqdashboard.role.delete')")
    public void deleteUser(@RequestBody List<String> roleIds) {
        roleService.deleteRoles(roleIds);
    }

    private void assertRole(String roleId) {
        if (!roleService.getRole(roleId).isPresent()) {
            throw new RoleNotFoundException(roleId);
        }
    }

    private List<Permission> getPermissionList(List<String> permissionIdList) {
        List<Permission> permissions = (List<Permission>) permissionService.getAllPermissionById(permissionIdList);
        if (permissions.isEmpty())
            throw new PermissionNotFoundException(permissionIdList.toString());
        return permissions;
    }
}
