/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ProjectMapping
 * @author Cognizant
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMapping {
    @Id
    private String projectId;
    @JsonIgnore
    private Type type = Type.CIQ_DASHBOARD_API;
    private String ownerId;
    private List<String> userIds;
    private List<String> teamIds = new ArrayList<>();

    public ProjectMapping(String projectId, String ownerId, List<String> userIds, List<String> teamIds) {
        this.projectId = projectId;
        this.ownerId = ownerId;
        this.userIds = userIds;
        this.teamIds = teamIds;
    }

    @JsonIgnore
    @CreatedDate
    private Date createdDate;
    @JsonIgnore
    @CreatedBy
    private String createdUser;
    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedUser;
    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;

    enum Type{
        WORKBENCH_API, DASHBOARD_API, EXECUTION_API, CIQ_DASHBOARD_API
    }
}
