package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Data;

@Data
public class Value {
    private  Integer value;
    private String link;

    public Value setValue(Integer value, String link) {
        this.value = value;
        this.link = link;
        return this;
    }
}
