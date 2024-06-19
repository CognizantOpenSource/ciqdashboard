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

import com.cognizant.authapi.base.error.PermissionExistsException;
import com.cognizant.authapi.users.beans.Permission;
import com.cognizant.authapi.users.repos.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *PermissionService
 *
 * @author Cognizant
 */
@Service
@Slf4j
@AllArgsConstructor
public class PermissionService {
    private PermissionRepository permissionRepository;

    //Getting all Permission list
    public List<Permission> getAllPermissions() {
        log.info("Getting all the Permissions from DB .....!");
        return permissionRepository.findAll();
    }

    //Getting a Permission by Permission ID
    public Optional<Permission> getPermission(String permissionId) {
        log.info("Getting Permission based on the Id. id is : " + permissionId);
        return permissionRepository.findById(permissionId);
    }

    //Getting a Permission by List of Permission ID's
    public Iterable<Permission> getAllPermissionById(List<String> permissionIdList) {
        log.info("Getting List of Permission based on the List of Id. id's are : " + permissionIdList.toString());
        return permissionRepository.findAllById(permissionIdList);
    }

    //Adding a new Permission
    public Permission addNewPermission(Permission permission) {
        log.info("Saving the Permission data: " + permission.toString());
        Permission returnPermission;
        try {
            returnPermission = permissionRepository.insert(permission);
        } catch (org.springframework.dao.DuplicateKeyException | com.mongodb.DuplicateKeyException e) {
            throw new PermissionExistsException("PermissionId", permission.getId());
        }
        return returnPermission;
    }

    //Adding a new Permission
    public List<Permission> addAllPermission(List<Permission> permissionList) {
        log.info("Saving the List of Permission data: " + permissionList.toString());
        return permissionRepository.insert(permissionList);
    }

    //Adding all Permission List
    public List<Permission> addPermissionsList(List<Permission> permissionList) {
        log.info("Saving the Project data: " + permissionList.toString());
        return permissionRepository.saveAll(permissionList);
    }

    //Updating a Permission
    public Permission updatePermission(Permission permission) {
        log.info("Updating the Permission data: " + permission.toString());
        return permissionRepository.save(permission);
    }

    public List<Permission> getAllPermissionByIdLike(String id) {
        log.info("Getting List of Permission like id. id is : " + id);
        return permissionRepository.findByIdLike(id);
    }

}
