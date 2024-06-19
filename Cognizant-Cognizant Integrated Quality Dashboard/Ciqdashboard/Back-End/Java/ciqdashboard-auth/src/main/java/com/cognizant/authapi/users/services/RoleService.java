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

import com.cognizant.authapi.base.error.RoleExistsException;
import com.cognizant.authapi.users.beans.Role;
import com.cognizant.authapi.users.repos.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

/**
 *RoleService
 *
 * @author Cognizant
 */
@Service
@Slf4j
@AllArgsConstructor
public class RoleService {
    private RoleRepository roleRepository;

    //Getting all Role list
    public List<Role> getAllRoles() {
        log.info("Getting all the Roles from DB .....!");
        return roleRepository.findAll();
    }

    //Getting a Role by Role ID
    public Optional<Role> getRole(String roleId) {
        log.info("Getting Role based on the Id. id is : " + roleId);
        return roleRepository.findById(roleId);
    }

    //Getting a Permission by List of Permission ID's
    public Iterable<Role> getAllRoleById(List<String> roleIdList) {
        log.info("Getting List of Role based on the List of Id. id's are : " + roleIdList.toString());
        return roleRepository.findAllById(roleIdList);
    }

    //Adding a new Role
    public Role addNewRole(@RequestBody Role role) {
        log.info("Saving the Project data: " + role.toString());
        try {
            return roleRepository.insert(role);
        } catch (org.springframework.dao.DuplicateKeyException | com.mongodb.DuplicateKeyException e) {
            throw new RoleExistsException("RoleName", role.getName());
        }
    }

    //Adding all Role List
    public List<Role> addRolesList(List<Role> roleList) {
        log.info("Saving the Project data: " + roleList.toString());
        return roleRepository.insert(roleList);
    }

    //Updating a Role
    public Role updateRole(Role role) {
        log.info("Updating the Role data: " + role.toString());
        return roleRepository.save(role);
    }

    public void removeRole(String roleId) {
        roleRepository.deleteById(roleId);
    }

    public void deleteRoles(List<String> roleNames) {
        roleRepository.deleteByNameIn(roleNames);
    }
}
