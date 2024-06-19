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

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DataGridChartData
 * @author Cognizant
 */

@Data
@Builder
public class DataGridChartData {
    private List<DataGridHeaderCell> header;
    private List<DataGridRow> rows;

    public String toString() {
        return String.format("DataGrid(header=%s, rows=%s)", header, rows);
    }

    public static class DataGridChartDataBuilder {
        private List<DataGridHeaderCell> header;
        private List<DataGridRow> rows;

        public DataGridChartDataBuilder chardataheader(@NonNull DataGridHeaderCell... header) {
            this.header = Arrays.asList(header);
            return this;
        }

        public DataGridChartData chartdatarows(@NonNull Map... rows) {
            this.rows = Arrays.asList(rows).stream().map(DataGridRow::new).collect(Collectors.toList());
            return this.build();
        }

        public DataGridChartData chartdatarows(@NonNull List<DataGridRow> rows) {
            this.rows = rows;
            return this.build();
        }
    }
}
