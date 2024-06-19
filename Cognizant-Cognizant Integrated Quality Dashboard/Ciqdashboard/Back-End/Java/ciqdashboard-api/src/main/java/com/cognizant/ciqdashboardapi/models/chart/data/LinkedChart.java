package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class LinkedChart  {

    public Chart chart;
    private Object data;

    public LinkedChart(Chart chart, Object data) {
        this.chart = chart;
        this.data = data;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
