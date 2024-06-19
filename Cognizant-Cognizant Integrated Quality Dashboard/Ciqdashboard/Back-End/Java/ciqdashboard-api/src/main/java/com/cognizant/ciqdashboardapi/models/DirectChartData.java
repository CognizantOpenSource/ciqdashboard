package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "directChartData")
public class DirectChartData {

    @Id
    private String id;
    private String itemId;
    private String category;
    private Instant lastCalculatedDate;
    private Instant createdOn;
    private Object chartValues;
    private List<String> groupBy;
    private List<FilterConfig> filters;

    private String layerName;
    private String layerId;
    private String dashboardId;
    private String pageId;

    //private List<TrendingMetric> trendingMetricValues;
}
