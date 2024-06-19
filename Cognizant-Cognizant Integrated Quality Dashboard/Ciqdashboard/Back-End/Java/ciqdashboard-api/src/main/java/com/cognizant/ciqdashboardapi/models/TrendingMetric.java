package com.cognizant.ciqdashboardapi.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class TrendingMetric {

    private Integer result;
    private Date month;
}
