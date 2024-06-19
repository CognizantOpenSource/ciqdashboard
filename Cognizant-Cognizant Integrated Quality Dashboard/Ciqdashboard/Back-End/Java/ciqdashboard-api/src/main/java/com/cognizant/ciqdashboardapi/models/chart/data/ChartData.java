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
 * ChartData
 * @author Cognizant
 */

@Data
@Builder
public class ChartData {
    public String name;
    public Object value;
    public String label;
    public String link;
    public List<ChartData> children;
    public List<ChartData> series;

    public ChartData(String name, Object value, String label, String link, List<ChartData> children, List<ChartData> series) {
        this.name = name;
        this.value = value;
        this.label = name;
        if(link!=null){
            this.link = link;
        }
        else {
            if (name != null) {
                this.link = "newchart-xml-" + name.toLowerCase();
            } else {
                this.link = name;
            }
        }
        this.children = children;
        this.series = series;
    }

    public String toString() {
        if (children == null && series == null)
            return String.format("(%s:%s)", name, value);
        else if (series == null)
            return String.format("(%s:%s, children:%s)", name, value, children.toString());
        else
            return String.format("(%s:%s)", name, series.toString());
    }

    public static class ChartDataBuilder {

        public List<ChartData> children;
        public List<ChartData> series;

        public ChartData children(@NonNull ChartData... data) {
            this.children = Arrays.asList(data);
            return this.build();
        }

        public ChartData series(@NonNull ChartData... data) {
            this.series = Arrays.asList(data);
            return this.build();
        }

        public ChartData series(List<ChartData> data) {
            this.series = data;
            return this.build();
        }
    }
}
