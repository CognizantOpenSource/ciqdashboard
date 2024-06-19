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

import com.cognizant.ciqdashboardapi.models.chart.DBSort;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TableAggregation
 * @author Cognizant
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class TableAggregation extends FilterAggregation {
    private List<String> projection = new ArrayList<>();
    private LinkedHashMap<String, Object> projectionWithAlias = new LinkedHashMap<>();
    private List<String> excludeFields = new ArrayList<>();
    private DBSort sort;
    private int limit;

}
