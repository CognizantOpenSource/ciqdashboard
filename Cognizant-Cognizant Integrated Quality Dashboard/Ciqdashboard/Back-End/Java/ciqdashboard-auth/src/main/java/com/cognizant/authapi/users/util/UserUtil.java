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

import com.cognizant.authapi.users.beans.Account;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.dto.UserDTO;
import com.cognizant.authapi.users.dto.UserUpdateDTO;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *UserUtil
 *
 * @author Cognizant
 */
@Component
public class UserUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String GOOGLE = "Google";

    public User convertPayloadToUser(GoogleIdToken.Payload payload) {
        User user = new User();
        user.setFirstName((String) payload.get("given_name"));
        user.setLastName((String) payload.get("family_name"));
        user.setEmail( payload.getEmail());
        user.setImage((String) payload.get("picture"));
        user.setOrg(GOOGLE);

        return user;
    }


    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public User convertToNewEntity(UserDTO userDTO, Account account) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setCreatedDate(new Date());
        user.setAccount(account);
        if (userDTO.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }

    public User convertToUpdateEntity(UserUpdateDTO userUpdateDTO, User user) {
        BeanUtils.copyProperties(userUpdateDTO, user);
        return user;
    }

    public UserUpdateDTO convertToUpdateDto(User updatedUser) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        BeanUtils.copyProperties(updatedUser, userUpdateDTO);
        return userUpdateDTO;
    }
}
