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

package com.cognizant.authapi.models;

import com.cognizant.authapi.base.beans.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *AppTokenStore
 *
 * @author Cognizant
 */

@Document(collection = "appTokenStore")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppTokenStore extends BaseModel {
    @Id
    private String id;
    @NotBlank(message = "Reference Id should not be empty/null")
    @Size(min = 24, message = "Reference Id should be minimum 24 characters")
    @Indexed(unique = true)
    private String refId;
    private String name;
    private AppTokenType type;
    private String tokenExpiresAt;
    @JsonIgnore
    private String token;

    public enum  AppTokenType{
        ROBOT, DASHBOARD_PROJECT, USER
    }
}
