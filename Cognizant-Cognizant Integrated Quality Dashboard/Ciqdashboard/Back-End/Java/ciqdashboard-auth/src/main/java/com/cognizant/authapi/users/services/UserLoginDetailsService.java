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

import com.cognizant.authapi.users.beans.UserLoginDetails;
import com.cognizant.authapi.users.repos.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *UserLoginDetailsService
 *
 * @author Cognizant
 */

@Service
public class UserLoginDetailsService {
    @Autowired
    UserLoginDetailsRepository repository;

    public Optional<UserLoginDetails> get(String userEmailId){
        return repository.findById(userEmailId);
    }

    public UserLoginDetails save(UserLoginDetails userLoginDetails){
        return repository.save(userLoginDetails);
    }
}
