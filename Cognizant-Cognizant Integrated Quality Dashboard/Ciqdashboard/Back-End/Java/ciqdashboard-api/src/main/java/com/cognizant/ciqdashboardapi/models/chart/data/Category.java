package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Data;

@Data
public class Category {

    private  String label;

    public Category setLabel(String label) {
        this.label = label;
        return this;
    }
}
