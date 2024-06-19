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

import com.cognizant.authapi.users.beans.TokenRequest;
import com.cognizant.authapi.users.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *AuthService
 *
 * @author Cognizant
 */
@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    TokenIdService tokenIdService;
    UserService userService;

    /**
     * Generate the token based on the user details provided
     * @param tokenRequest user details to generate token
     * @return token and expiry date with time details
     */
    public Map<String, Object> provideToken(TokenRequest tokenRequest) {
        return tokenIdService.provideToken(tokenRequest);
    }

    /**
     * Signing up or creating new user based on the Third party application's (Google, Microsoft etc..) token as of now Google implemented
     *
     * @param tokenRequest third party token which is generated and return from application
     * @return post signing up based on the token will return user detail which are stored in ciqdashboard DB
     */
    public UserDTO getUser(TokenRequest tokenRequest) {
        return tokenIdService.getUser(tokenRequest);
    }
}
