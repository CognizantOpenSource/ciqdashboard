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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * CIQDashboardDataSource - Refers to ciqDashboardDataSources collection in mongodb
 * @author Cognizant
 */

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "ciqDashboardDataSources")
public class CIQDashboardDataSource extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    @Indexed(unique = true)
    private String name;
    @NotBlank(message = "Group should not be empty/null")
    @Size(min = 3, message = "Group minimum characters should be '3' ")
    private String group;
    @NotBlank(message = "CollectionName should not be empty/null")
    @Size(min = 3, message = "CollectionName minimum characters should be '3' ")
    private String collectionName;
    private String description;
    @NotBlank(message = "ToolName should not be empty/null")
    @Size(min = 3, message = "ToolName minimum characters should be '3' ")
    private String toolName;
    private String image;
    private String imageType;
    @NotEmpty(message = "Fields list should not be empty/null")
    private List<FieldType> fields;
}
