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

package com.cognizant.authapi.users.beans;

import com.cognizant.authapi.base.beans.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *UserLoginDetails - to update the sign up details
 *
 * @author Cognizant
 */

@Data
@Document(collection = "userLoginDetails")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserLoginDetails extends BaseModel {
    @Id
    private String userEmailId;
    private LoginStatus lastLoginStatus = LoginStatus.SUCCESS;
    private AtomicInteger failureCount = new AtomicInteger();
    private Date lastSuccessLoginTime = new Date();
    private List<Date> failureLoginTimes = new ArrayList<>();

    public UserLoginDetails increaseFailureDetails(){
        this.setLastLoginStatus(UserLoginDetails.LoginStatus.FAILURE);
        this.getFailureCount().incrementAndGet();
        this.getFailureLoginTimes().add(new Date());
        return this;
    }

    public UserLoginDetails(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public static UserLoginDetails getFirstFailure(String userEmailId){
        UserLoginDetails userLoginDetails = new UserLoginDetails(userEmailId);
        userLoginDetails.setLastLoginStatus(LoginStatus.FAILURE);
        userLoginDetails.setFailureCount(new AtomicInteger(1));
        userLoginDetails.setFailureLoginTimes(Collections.singletonList(new Date()));
        return userLoginDetails;
    }

    public enum LoginStatus{
        SUCCESS, FAILURE
    }
}
