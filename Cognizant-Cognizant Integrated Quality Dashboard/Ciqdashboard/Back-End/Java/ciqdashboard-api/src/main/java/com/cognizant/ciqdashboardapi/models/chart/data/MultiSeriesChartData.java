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

import lombok.*;

import java.util.Arrays;
import java.util.List;

/**
 * MultiSeriesChartData
 * @author Cognizant
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiSeriesChartData {
    public String name;
    public Object value;
    public List<MultiSeriesChartData> series;

    public String toString() {
        if (series == null)
            return String.format("(%s:%s)", name, value);
        else
            return String.format("(%s:%s, children:%s)", name, value, series.toString());
    }

    public static class MultiSeriesChartDataBuilder {

        public List<MultiSeriesChartData> series;

        public MultiSeriesChartData series(@NonNull MultiSeriesChartData... data) {
            this.series = Arrays.asList(data);
            return this.build();
        }
    }
}
