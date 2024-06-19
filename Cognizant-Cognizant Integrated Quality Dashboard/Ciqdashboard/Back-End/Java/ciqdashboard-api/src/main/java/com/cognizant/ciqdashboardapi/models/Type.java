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

import java.util.Arrays;
import java.util.Optional;

import static com.cognizant.ciqdashboardapi.models.Type.GenericChartItemType.*;

/**
 * Type
 * @author Cognizant
 */

public class Type {

    public static GenericChartItemType getGenericChartItemType(ChartItemType chartItemType) {
        switch (chartItemType) {
            case TABLE:
                return TABLE;
            case LABEL:
            case IMAGE:
                return NONE;

            case PIE_CHART:
            case PIE_CHART_ADVANCED:
            case PIE_CHART_GRID:
            case CARD_CHART:
            case TREE_MAP_CHART:
                return DRILL_DOWN_CHART;

            case BAR_CHART:
            case BAR_VERTICAL_STACKED:
            case BAR_HORIZONTAL_STACKED:
            case BAR_VERTICAL_NORMALIZED:
            case BAR_HORIZONTAL_NORMALIZED:
            case LINE_CHART_SERIES:
            case LINE_CHART:
            case POLAR_CHART:
            case AREA_CHART:
            case AREA_CHART_NORMALIZED:
            case LINE_CHART_AREA_STACKED:
            case BUBBLE_CHART:
            case BAR_VERTICAL_GROUP:
            case BAR_HORIZONTAL_GROUP:
            case HEATMAP_CHART:
            case DATA_GRID:
                return BAR_CHART;

            case BAR_CHART_VERTICAL_GAUGE:
                return BAR_GAUGE_CHART;

            case GAUGE_CHART:
            case LINER_GAUGE_CHART:
            case BAR_CHART_VERTICAL:
            case BAR_CHART_HORIZONTAL:
            case TREE_MAP_INTERACTIVE_CHART:
                return DRILL_DOWN_CHART;
            case CHART_FUSION_COLUMN2D:
            case AREA2D_CHART_FUSION:
            case BAR2D_CHART_FUSION:
            case BAR3D_CHART_FUSION:
            case COLUMN3D_CHART_FUSION:
            case DOUGHNUT2D_CHART_FUSION:
            case DOUGHNUT3D_CHART_FUSION:
            case FUNNEL_CHART_FUSION:
            case PIE2D_CHART_FUSION:
            case PIE3D_CHART_FUSION:
            case LINE2D_CHART_FUSION:
            case COLUMN2D_CHART_FUSION:
                return FUSION_CHART2;
            case CHART_FUSION_DRILLDOWN_BARVERTICAL_PIE:
                return FUSION_CHART;
            case BAR_VERTICAL_GROUP_FUSION:
            case CHART_CARD_RAG_MAIN:
            case HUNDRED_PERCENT_STACKEDBAR_FUSION:
            case BAR_VERTICAL_STACKED_FUSION:
                return FUSION_CHART_VERTICAL_GROUP;
            case CHART_HEATMAP_FUSION:
            case CHART_GUAGE_FUSION:
            case CHART_LINEARSCALE_FUSION:
                return FUSION_CHART2;
            default:
                return LINE;
        }
    }

    public enum GenericChartItemType {
        BAR_CHART, DRILL_DOWN_CHART, LINE, TABLE, BAR_GAUGE_CHART, NONE, AGGREGATE,FUSION_CHART,FUSION_CHART2,FUSION_CHART_VERTICAL_GROUP;
    }

    public enum ChartItemType {
        COMBO("combo"),
        TABLE("table"),
        LABEL("label"),
        IMAGE("image"),
        LINE_CHART_SERIES("line-chart-series"),
        LINE_CHART("line-chart"),
        AREA_CHART("area-chart"),
        AREA_CHART_NORMALIZED("area-chart-normalized"),
        LINE_CHART_AREA_STACKED("line-chart-area-stacked"),
        BAR_CHART_HORIZONTAL("bar-chart-horizontal"),
        BAR_HORIZONTAL_STACKED("bar-horizontal-stacked"),
        BAR_HORIZONTAL_NORMALIZED("bar-horizontal-normalized"),
        BAR_VERTICAL_NORMALIZED("bar-vertical-normalized"),
        BAR_VERTICAL_STACKED("bar-vertical-stacked"),
        BAR_CHART_VERTICAL("bar-chart-vertical"),
        BAR_CHART_VERTICAL_GAUGE("bar-chart-vertical-gauge"),
        BAR_CHART("bar-chart"),
        BAR_HORIZONTAL_GROUP("bar-horizontal-group"),
        BAR_VERTICAL_GROUP("bar-vertical-group"),
        PIE_CHART("pie-chart"),
        PIE_CHART_ADVANCED("pie-chart-advanced"),
        PIE_CHART_GRID("pie-chart-grid"),
        POLAR_CHART("polar-chart"),
        BUBBLE_CHART("bubble-chart"),
        HEATMAP_CHART("heatmap-chart"),
        TREE_MAP_CHART("tree-map-chart"),
        CARD_CHART("card-chart"),
        GAUGE_CHART("gauge-chart"),
        LINER_GAUGE_CHART("liner-gauge-chart"),
        TREE_MAP_INTERACTIVE_CHART("tree-map-interactive-chart"),
        CHART_FUSION_COLUMN2D("bar-vertical-chart-fusion"),
        CHART_FUSION_DRILLDOWN_BARVERTICAL_PIE("drilldown-bar-vertical-pie-chart-fusion"),
        AREA2D_CHART_FUSION("area2d-chart-fusion"),
        BAR2D_CHART_FUSION("bar2d-chart-fusion"),
        BAR3D_CHART_FUSION("bar3d-chart-fusion"),
        COLUMN3D_CHART_FUSION("column3d-chart-fusion"),
        DOUGHNUT2D_CHART_FUSION("doughnut2d-chart-fusion"),
        DOUGHNUT3D_CHART_FUSION("doughnut3d-chart-fusion"),
        BAR_VERTICAL_GROUP_FUSION("bar-vertical-group-fusion"),
        BAR_VERTICAL_STACKED_FUSION("bar-vertical-stacked-fusion"),
        FUNNEL_CHART_FUSION("funnel-chart-fusion"),
        PIE2D_CHART_FUSION("pie2d-chart-fusion"),
        PIE3D_CHART_FUSION("pie3d-chart-fusion"),
        LINE2D_CHART_FUSION("line2d-chart-fusion"),
        COLUMN2D_CHART_FUSION("column2d-chart-fusion"),
        CHART_CARD_RAG_MAIN("chart-card-rag-main"),
        CHART_HEATMAP_FUSION("chart-heatmap-fusion"),
        CHART_GUAGE_FUSION("chart-gauge-fusion"),
        HUNDRED_PERCENT_STACKEDBAR_FUSION("hundred-percent-stackedbar-fusion"),

        CHART_LINEARSCALE_FUSION("chart-linearscale-fusion"),
        DATA_GRID("data-grid");


        private String type;

        ChartItemType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static Optional<ChartItemType> getChartItemType(String type) {
            return Arrays.asList(values()).stream().filter(chartItemType -> chartItemType.getType().equals(type)).findAny();
        }

    }

    public enum AggregateType {
        SUM, AVG, COUNT, MIN, MAX, CONSTANT, DISTINCT_COUNT
    }

    public enum MathOperator {
        add, sub, mul, div
    }

    public enum DateType {
        YEAR, MONTH, WEEK, DAY
    }
}
