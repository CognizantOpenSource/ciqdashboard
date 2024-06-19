package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class LinkedData {

    public String id;
    public Object linkedChart;

    public LinkedData(String id, Object linkedChart) {
        this.id = id;
        this.linkedChart = linkedChart;
    }
}
