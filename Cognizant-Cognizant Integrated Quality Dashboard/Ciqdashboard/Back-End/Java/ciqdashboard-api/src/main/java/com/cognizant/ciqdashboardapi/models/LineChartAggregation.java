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
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * LineChartAggregation
 * @author Cognizant
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class LineChartAggregation extends FilterAggregation {
    @NotEmpty(message = "GroupBy should not null/empty")
    private String groupBy;

    @JsonIgnore
    public DrillDownChartAggregation getChartAggregation(){
        DrillDownChartAggregation chartAggregation = new DrillDownChartAggregation();
        chartAggregation.setLevel(1);
        chartAggregation.setGroupBy(Arrays.asList(groupBy));
        chartAggregation.setFilters(getFilters());

        return chartAggregation;
    }

}
