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

package com.cognizant.authapi.users;

import com.cognizant.authapi.users.dto.ChangePasswordDto;
import com.cognizant.authapi.users.dto.PasswordResetDto;
import com.cognizant.authapi.users.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class UserPasswordTest {
    /*
    private static final List<String> VALID_PASSWORDS = Arrays.asList("Test@123", "Test@123Test@123Test");
    private static final List<String> INVALID_PASSWORDS = Arrays.asList("Test@12", "Test@123Test@123Test@123", "Test@test", "Tests123");
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testSignUpPasswordPattern() {
        Function<String, UserDTO> getUser = (password) -> new UserDTO() {
            {
                setPassword(password);
                setEmail("test@test.test");
                setOrg("test");
            }
        };
        INVALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(getUser.apply(password));
            Assertions.assertFalse(violations.isEmpty(), "regex password validation failed for - " + password);
        });
        VALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(getUser.apply(password));
            Assertions.assertTrue(violations.isEmpty(), "regex password validation failed for - " + password);
        });
    }

    @Test
    void testResetPasswordPattern() {
        Function<String, PasswordResetDto> getPR = (password) -> new PasswordResetDto() {
            {
                setPassword(password);
                setEmail("test@test.test");
            }
        };
        INVALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<PasswordResetDto>> violations = validator.validate(getPR.apply(password));
            Assertions.assertFalse(violations.isEmpty(), "regex password validation failed for - " + password);
        });
        VALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<PasswordResetDto>> violations = validator.validate(getPR.apply(password));
            Assertions.assertTrue(violations.isEmpty(), "regex password validation failed for - " + password);
        });
    }
    @Test
    void testChangePasswordPattern() {
        Function<String, ChangePasswordDto> getPR = (password) -> new ChangePasswordDto() {
            {
                setNewPassword(password);
                setOldPassword("test");
            }
        };
        INVALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(getPR.apply(password));
            Assertions.assertFalse(violations.isEmpty(), "regex password validation failed for - " + password);
        });
        VALID_PASSWORDS.forEach(password -> {
            Set<ConstraintViolation<ChangePasswordDto>> violations = validator.validate(getPR.apply(password));
            Assertions.assertTrue(violations.isEmpty(), "regex password validation failed for - " + password);
        });
    }

     */
}
