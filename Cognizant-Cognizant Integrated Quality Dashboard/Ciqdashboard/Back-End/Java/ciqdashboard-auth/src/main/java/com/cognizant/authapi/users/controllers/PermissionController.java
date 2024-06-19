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
import com.cognizant.authapi.users.beans.Permission;
import com.cognizant.authapi.users.services.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 *PermissionController
 *
 * @author Cognizant
 */
@RestController
@RequestMapping(value = "/permissions")
@AllArgsConstructor
@Slf4j
public class PermissionController {

    private PermissionService permissionService;

    /**
     * Getting all Permissions
     *
     * @return list of permissions which are available in db
     */
    @GetMapping(value = "")
    @PreAuthorize("hasPermission('Permissions','ciqdashboard.permission.read')")
    public List<Permission> getAllPermissions() {
        log.info("Getting all the Permissions from Database.....!");
        return permissionService.getAllPermissions();
    }


    /**
     * Getting a Permission based on provided ID
     * @param permissionId id of permission which need to fetch from db
     * @return permission details based on the id
     */
    @GetMapping(value = "/{permissionId}")
    @PreAuthorize("hasPermission('Permissions','ciqdashboard.permission.read')")
    public Permission getPermission(@PathVariable String permissionId) {
        log.info("Getting Project id is : " + permissionId);
        Optional<Permission> optionalPermission = permissionService.getPermission(permissionId);
        if (optionalPermission.isPresent()) {
            return optionalPermission.get();
        } else {
            throw new PermissionNotFoundException(permissionId);
        }
    }
}
