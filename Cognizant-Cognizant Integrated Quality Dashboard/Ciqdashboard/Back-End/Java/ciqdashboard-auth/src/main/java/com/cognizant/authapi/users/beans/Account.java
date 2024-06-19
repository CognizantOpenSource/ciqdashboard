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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 *Account - refers the accounts collection in MongoDB
 *
 * @author Cognizant
 */
@Data
@Document(collection = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String userId;
    @DBRef
    private List<Role> roles;
    private List<String> projectIds;
    private List<String> ownProjectIds;

    @JsonIgnore
    @CreatedBy
    private String user;
    @JsonIgnore
    @CreatedDate
    private Instant createdDate;
    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedUser;
    @JsonIgnore
    @LastModifiedDate
    private Instant lastModifiedDate;

    @JsonIgnore
    @Value("${app.permission.admin}")
    private String adminPermission;


    @JsonIgnore
    public boolean hasProject(String projectId) {
        return Objects.nonNull(projectIds) && projectIds.contains(projectId);
    }

    @JsonIgnore
    public boolean hasOwnProject(String projectId) {
        return Objects.nonNull(ownProjectIds) && ownProjectIds.contains(projectId);
    }

    @JsonIgnore
    public boolean hasPermission(String permissionId) {
        return Objects.nonNull(roles) && roles.stream().map(Role::getPermissions).flatMap(List::stream)
                .map(Permission::getId).anyMatch(localPermissionId -> localPermissionId.equalsIgnoreCase(permissionId));
    }

    @JsonIgnore
    public boolean isAdmin() {
        return Objects.nonNull(roles) && roles.stream().map(Role::getPermissions).flatMap(List::stream)
                .map(Permission::getId).anyMatch(localPermissionId -> localPermissionId.equalsIgnoreCase(adminPermission));
    }
}
