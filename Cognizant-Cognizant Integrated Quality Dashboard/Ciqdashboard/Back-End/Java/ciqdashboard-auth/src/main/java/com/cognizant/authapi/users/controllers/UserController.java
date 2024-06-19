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

import com.cognizant.authapi.base.error.UserNotFoundException;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.beans.UserPrincipal;
import com.cognizant.authapi.users.dto.UserUpdateDTO;
import com.cognizant.authapi.users.services.UserService;
import com.cognizant.authapi.users.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *UserController
 *
 * @author Cognizant
 */
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    private UserUtil userUtil;


    /**
     * Getting all Users
     *
     * @return Users List
     */
    @GetMapping(value = "")
    @PreAuthorize("hasPermission('User','ciqdashboard.user.read')")
    public List<User> getAllUsers() {
        //log.info("Getting all the Users from Database.....!");
        return userService.getAllUsers();
    }


    /**
     * Getting a User by ID
     *
     * @param userId to fetch the user details
     * @return User details for that userId
     */
    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasPermission('User','ciqdashboard.user.read')")
    public User getUser(@PathVariable String userId) {
        //log.info("Getting Project id is : " + userId);
        Optional<User> optionalUser = userService.getUser(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException(userId);
        }
    }


    /**
     * Getting a User details based on the login session
     *
     * @return User Details who are logged in the application
     */
    @GetMapping(value = "/profile")
    public User getUser() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) auth;
            return userService.assertAndGetUser(userPrincipal.getId());
        } else {
            throw new AuthenticationCredentialsNotFoundException("user not signed in");
        }
    }


    /**
     * Updating Existing User details
     *
     * @param userUpdateDTO User Details which are to be update
     * @return Updated User details post storing in DB.
     */
    @Validated
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{userId}")
    @PreAuthorize("hasPermission('User','ciqdashboard.user.update')")
    public UserUpdateDTO updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        //log.info("Updating the User data " + userUpdateDTO);
        User user = userService.assertAndGetUser(userId);
        User updatedUser = userService.updateUser(userUtil.convertToUpdateEntity(userUpdateDTO, user));
        return userUtil.convertToUpdateDto(updatedUser);
    }


    /**
     * Activate or Deactivate User List
     *
     * @param map Map contain activate flag (to activate or deactivate) and Users List which should be updated
     * @return Updated User details post storing in DB.
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/activate")
    @PreAuthorize("hasPermission('User','ciqdashboard.user.update')")
    public List<User> activateOrDeactivateUser(@RequestBody Map<String, Object> map) {
        boolean active = (boolean) map.get("activate");
        List<String> userIds = (List<String>) map.get("userIds");
        //log.info("Activate flag is " + active);
        //log.info("User Ids List " + userIds.toString());
        List<User> users = getAllUsersByUserIds(userIds);
        users.forEach(user -> user.setActive(active));
        return userService.updateUserList(users);
    }

    /**
     * Delete Users Based on UserIds
     *
     * @param userIds List of UserIds which are to be delete
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasPermission('User','ciqdashboard.user.delete')")
    public void deleteUser(@RequestBody List<String> userIds) {
        userService.deleteUserList(userIds);
    }

    private List<User> getAllUsersByUserIds(List<String> userIds) {
        return userService.getUserList(userIds);
    }

}
