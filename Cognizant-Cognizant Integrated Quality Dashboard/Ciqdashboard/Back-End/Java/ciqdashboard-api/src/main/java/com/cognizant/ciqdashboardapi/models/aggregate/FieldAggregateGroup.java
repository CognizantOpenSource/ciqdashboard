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

package com.cognizant.ciqdashboardapi.models.aggregate;

import com.cognizant.ciqdashboardapi.models.Type;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * FieldAggregateGroup
 * @author Cognizant
 */

@Data
public class FieldAggregateGroup {
    private String name;
    @NotEmpty(message = "Aggregates should not be empty/null")
    @Valid
    private List<FieldAggregate> aggregates;
    @NotNull(message = "Operator should not be null")
    private Type.MathOperator operator;
}
