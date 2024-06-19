package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ChartDataTrending {

    public String name;
    public Object value;

    public ChartDataTrending(String name, Object value) {
        this.name = name;
        this.value = value;
    }



}
