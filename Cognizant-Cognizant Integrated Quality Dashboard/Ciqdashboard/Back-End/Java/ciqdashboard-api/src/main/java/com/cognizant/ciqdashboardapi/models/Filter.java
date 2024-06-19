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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Filter
 * @author Cognizant
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private String field;
    private OPType op;
    private Object value;
    private Object maxValue;

    public enum OPType {
        eq, equals, ne,
        gt, gte, lt, lte, between,
        in, nin,
        contains, startswith, endswith, notcontains,
        matches, regex,
        lastNYear, lastNMonth, lastNWeek, lastNDay,
        thisYear, thisMonth, thisWeek, thisDay
    }

    public Filter(String field, OPType op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }
}
