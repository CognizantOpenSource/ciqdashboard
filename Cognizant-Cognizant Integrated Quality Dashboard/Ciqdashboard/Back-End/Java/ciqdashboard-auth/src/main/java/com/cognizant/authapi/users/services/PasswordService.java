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

import com.cognizant.authapi.base.beans.CIQDashboardApiResponse;
import com.cognizant.authapi.base.error.CustomInvalidCredentialException;
import com.cognizant.authapi.base.error.UserNotFoundException;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.beans.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
/**
 *PasswordService
 *
 * @author Cognizant
 */
@Service
@Slf4j
@AllArgsConstructor
public class PasswordService {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    //private PasswrdAuditRepositry repository;

    private static final String RESPONSE_TEMPLATE = "Password Changed Successfully. for User %s" ;

    public ResponseEntity changePassword(CharSequence newPassword, CharSequence oldPassword) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUserByEmail(principal.getEmail());
        if (!user.isPresent())
            throw new UserNotFoundException(principal.getEmail());
        if (passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(user.get());
        } else {
            throw new CustomInvalidCredentialException("Password", "UserName", principal.getEmail());
        }
        CIQDashboardApiResponse response = new CIQDashboardApiResponse(LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Success",
                String.format(RESPONSE_TEMPLATE, principal.getEmail())
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity resetPassword(String userEmailId, CharSequence password) {
        Optional<User> optionalUser = userService.getUserByEmail(userEmailId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userService.updateUser(user);
        } else {
            throw new UserNotFoundException(userEmailId);
        }

        CIQDashboardApiResponse response = new CIQDashboardApiResponse(LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Success",
                String.format(RESPONSE_TEMPLATE, userEmailId)
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
