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

package com.cognizant.ciqdashboardapi.models.chart.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ChartType - Group of charts
 * @author Cognizant
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChartType {
    verticalStakedBar("vertical-stacked-bar"),
    lineSeries("line-series"),
    pie("pie-chart"),
    treeMap("tree-map"),
    card("card"),
    dataGrid("data-grid"),
    treeMapInteractive("tree-map-interactive");


    private String type;

    private ChartType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}