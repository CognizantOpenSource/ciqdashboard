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

import com.cognizant.authapi.users.dto.ChangePasswordDto;
import com.cognizant.authapi.users.dto.PasswordResetDto;
import com.cognizant.authapi.users.services.PasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *PasswordController
 *
 * @author Cognizant
 */
@Slf4j
@RestController
@RequestMapping("/users/password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/update")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordDto data) {
        return passwordService.changePassword(data.getNewPassword(), data.getOldPassword());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reset")
    @PreAuthorize("hasPermission('Password','ciqdashboard.permission.admin')")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetDto data) {
        return passwordService.resetPassword(data.getEmail(), data.getPassword());
    }
}
