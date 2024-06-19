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

import com.cognizant.ciqdashboardapi.models.chart.data.*;

/**
 * Charts - Chart builder class
 * @author Cognizant
 */

public class Charts {

    public static ChartData.ChartDataBuilder pie() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder doughnut() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder card() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder treeMap() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder treeMapInteractive() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder bar() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder line() {
        return ChartData.builder();
    }

    public static IDBChart.IDBChartBuilder pieChart(ChartConfiguration config) {
        return pieChart(config, true);
    }

    public static IDBChart.IDBChartBuilder doughnutChart(ChartConfiguration config) {
        return pieChart(config, false);
    }

    public static IDBChart.IDBChartBuilder cardChart(ChartConfiguration config) {
        return chart(config, ChartType.card);
    }

    public static IDBChart.IDBChartBuilder createChart(ChartConfiguration config) {
        return chart(config, config.getType());
    }

    public static IDBChart.IDBChartBuilder treeMapChart(ChartConfiguration config) {
        return chart(config, ChartType.treeMap);
    }

    public static IDBChart.IDBChartBuilder treeMapInteractiveChart(ChartConfiguration config) {
        return chart(config, ChartType.treeMapInteractive);
    }

    public static IDBChart.IDBChartBuilder barChart(ChartConfiguration config) {
        return chart(config, ChartType.verticalStakedBar);
    }

    public static IDBChart.IDBChartBuilder lineChart(ChartConfiguration config) {
        return chart(config, ChartType.lineSeries);
    }
    public static IDBChart.IDBChartBuilder dataGridChart(ChartConfiguration config) {
        return chart(config, ChartType.dataGrid);
    }
    public static DataGridChartData.DataGridChartDataBuilder dataGrid() {
        return DataGridChartData.builder();
    }
    public static DataGridHeaderCell.DataGridHeaderCellBuilder head() {
        return DataGridHeaderCell.builder();
    }

    public static DataGridHeaderCell head(String name, String label) {
        return DataGridHeaderCell.builder().name(name).label(label).build();
    }
    public static DataGridRow.DataGridRowBuilder row() {
        return DataGridRow.builder();
    }
    public static ChartData item(String name,Object value){
        return ChartData.builder().name(name).value(value).build();
    }
    private static IDBChart.IDBChartBuilder chart(ChartConfiguration config, ChartType type) {
        IDBChart.IDBChartBuilder builder = build(config);
        builder.type(type);
        return builder;
    }

    private static IDBChart.IDBChartBuilder pieChart(ChartConfiguration config, boolean isPie) {
        IDBChart.IDBChartBuilder builder = build(config);
        builder.type(ChartType.pie);
        ChartProperties properties = config.getProperties();
        if (properties != null) {
            properties.setPie(isPie);
        } else {
            properties = ChartProperties.builder().pie(isPie).build();
        }
        return builder.properties(properties);
    }

    private static IDBChart.IDBChartBuilder build(ChartConfiguration config) {
        return IDBChart.builder()
                .id(config.getId())
                .name(config.getName())
                .type(config.getType())
                .projectName(config.getProjectName())
                .template(config.getTemplate())
                .properties(config.getProperties());

    }

}
