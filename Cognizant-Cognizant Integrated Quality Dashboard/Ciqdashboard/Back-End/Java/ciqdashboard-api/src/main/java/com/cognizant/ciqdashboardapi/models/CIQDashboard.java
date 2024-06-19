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

import com.cognizant.ciqdashboardapi.base.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * CIQDashboard - Refers dashboards collection in mongodb
 *
 * @author Cognizant
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "dashboards")
public class CIQDashboard extends BaseModel {
    @Id
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
   // @Indexed(unique = true)
    private String name;
    @NotBlank(message = "ProjectName should not be empty/null")
    @Size(min = 4, message = "ProjectName minimum characters should be '4' ")
    private String projectName;
    private boolean active;
    private Boolean openAccess = false;
    private List<IDPageConfig> pages;
    private String category;
    private String projectId;
}
