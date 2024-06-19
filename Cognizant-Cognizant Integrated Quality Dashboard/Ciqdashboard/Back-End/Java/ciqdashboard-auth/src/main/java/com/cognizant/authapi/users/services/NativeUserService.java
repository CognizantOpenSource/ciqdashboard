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

import com.cognizant.authapi.base.error.CustomInvalidCredentialException;
import com.cognizant.authapi.base.error.UserNotFoundException;
import com.cognizant.authapi.users.beans.TokenRequest;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.beans.UserLoginDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *NativeUserService
 *
 * @author Cognizant
 */
@Service
@Slf4j
public class NativeUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginDetailsService loginDetailsService;

    public User validateUserDetails(TokenRequest tokenRequest) {
        String username = tokenRequest.getUsername();
        Optional<User> optional = userService.getUserByEmailAndType(username,tokenRequest.getType());
        if (optional.isPresent()) {
            User user = optional.get();
            if (!user.isActive()) throw new CustomInvalidCredentialException(String.format("User account %s is disabled. Please contact administrator.", username));
            if (passwordEncoder.matches(tokenRequest.getPassword(), user.getPassword())) {
                loginDetailsService.save(new UserLoginDetails(user.getEmail()));
                return user;
            } else {
                Optional<UserLoginDetails> detailsOptional = loginDetailsService.get(username);
                detailsOptional.ifPresentOrElse(
                        userLoginDetails -> {
                            UserLoginDetails save = loginDetailsService.save(userLoginDetails.increaseFailureDetails());
                            if (save.getFailureCount().get() >= 5) {
                                user.setActive(false);
                                userService.updateUser(user);
                            }
                        },
                        () -> loginDetailsService.save(UserLoginDetails.getFirstFailure(username))
                );
                throw new CustomInvalidCredentialException("invalid email id/password");
            }
        } else {
            throw new UserNotFoundException(username);
        }
    }
}
