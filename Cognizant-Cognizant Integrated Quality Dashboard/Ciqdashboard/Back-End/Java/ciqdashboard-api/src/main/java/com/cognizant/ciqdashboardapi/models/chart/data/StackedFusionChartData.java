package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StackedFusionChartData {

    public String chart;

    public List<Categories> categories;

    public List<Map> dataset;


    public StackedFusionChartData(String chart, List<Categories> categories, List<Map> dataset) {
        this.chart = chart;
        this.categories = categories;
        this.dataset = dataset;
    }
}
