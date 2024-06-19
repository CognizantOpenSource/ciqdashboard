package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chart {

    public String caption;
    public String subcaption;
    public String numberprefix;
    public String theme;
    public String rotateValues;
    public String plottooltext;

}
