package com.cognizant.ciqdashboardapi.models.chart.data;

import java.util.Comparator;

public class SortByName implements Comparator<Object> {


    @Override
    public int compare(Object o1, Object o2) {
       if(o1 instanceof ChartData){
           return orderChartData((ChartData) o1,(ChartData)o2);
       }else if(o1 instanceof ChartDataForFusion){
           return orderChartDataFusion((ChartDataForFusion) o1,(ChartDataForFusion)o2);
       }else{
           return 0;
       }
    }

    private int orderChartData(ChartData o1, ChartData o2) {
        if (o1.getName() == null && o2.getName() == null) {
            return 0;
        } else if(o1.getName() == null) {
            return -1;
        } else if(o2.getName() == null) {
            return 1;
        }else {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private int orderChartDataFusion(ChartDataForFusion o1, ChartDataForFusion o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
