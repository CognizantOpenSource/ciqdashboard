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

package com.cognizant.ciqdashboardapi.models.chart;

import com.cognizant.ciqdashboardapi.models.chart.data.ChartData;
import com.cognizant.ciqdashboardapi.models.chart.data.ChartType;
import com.cognizant.ciqdashboardapi.models.chart.data.DataGridChartData;
import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * IDBChart
 * @author Cognizant
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IDBChart {
    private String id;
    private ChartType type;
    private String name;
    private String projectName;
    private ChartProperties properties;
    private ChartTemplate template;
    private Object data;

    public void datavalue(@NonNull ChartData... data) {
        setData(Arrays.asList(data));
    }

    public static class IDBChartBuilder {

        public IDBChart chartdata(@NonNull ChartData... data) {
            this.data = Arrays.asList(data);
            return this.build();
        }
        public IDBChart lstchartdata(@NonNull List<ChartData> data) {
            this.data = data;
            return this.build();
        }
        public IDBChart grdchartdata(@NonNull DataGridChartData data) {
            this.data = data;
            return this.build();
        }
    }
}
