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
import com.cognizant.ciqdashboardapi.models.aggregate.GroupAggregate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * IDChartItem - Refers to chartItems collection in mongodb
 * @author Cognizant
 */

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "chartItems")
public class IDChartItem extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    private String name;
    private String description;
    private List<FilterConfig> filters;
    private List<String> groupBy;
    private List<String> projection;
    @NotBlank(message = "Type should not be empty/null")
    @Size(min = 3, message = "Type minimum characters should be '3' ")
    private String type;
    private GroupAggregate aggregate;
    private Map<String, ComboChartGroup> comboGroupBy;
    private String sourceGroup;
    private String itemGroup;
    @NotBlank(message = "Source should not be empty/null")
    @Size(min = 3, message = "Source minimum characters should be '3' ")
    private String source;
    @NotEmpty(message = "Options details should not be empty/null")
    private Map<String, Object> options;
    private String metricName;
    private String metricCategory;
    private String category;
    private String dashboardId;
    private String layerId;
}
