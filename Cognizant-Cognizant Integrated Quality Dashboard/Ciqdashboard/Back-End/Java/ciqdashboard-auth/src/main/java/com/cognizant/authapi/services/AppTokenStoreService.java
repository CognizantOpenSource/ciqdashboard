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

package com.cognizant.authapi.services;

import com.cognizant.authapi.base.error.ResourceNotFoundException;
import com.cognizant.authapi.models.AppTokenStore;
import com.cognizant.authapi.repos.AppTokenStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *AppTokenStoreService
 *
 * @author Cognizant
 */

@Service
public class AppTokenStoreService {
    @Autowired
    AppTokenStoreRepository repository;

    public Optional<AppTokenStore> get(String id){
        return repository.findById(id);
    }

    public AppTokenStore assertOrGet(String id){
        Optional<AppTokenStore> optional = get(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("AppToken", "id", id);
        }
    }

    public Optional<AppTokenStore> getByRefId(String refId){
        return repository.findByRefId(refId);
    }

    public AppTokenStore assertOrGetByRefId(String refId){
        Optional<AppTokenStore> optional = repository.findByRefId(refId);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("AppToken", "ReferenceId", refId);
        }
    }

    public AppTokenStore add(AppTokenStore appTokenStore){
        return repository.insert(appTokenStore);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

    public void deleteByRefId(String id) {
        repository.deleteByRefId(id);
    }
}
