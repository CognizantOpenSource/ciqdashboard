package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_derived")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SourceMetrics {
    @Id
    private String id;
    private String toolType;

    private String projectName;
    private String dashboardName;
    private String pageName;
    private String itemId;

    private String customFunction;
    private String customFunctionName;
    private Map<String, String> customParams;

    private String groupValue;
    private String groupBy;
    private boolean grouping;
    //private JSONArray groupedResult;

    private String trending;
    private String trendBy;
    private String trendingField;
    private int trendCount;

    private String metricName;
    private int metricValue;
    private JSONArray metricValues;
    private List<TrendingMetric> trendingMetricValues;

    private Instant lastCalculatedDate;

    private String layerName;
    private String layerId;
    private String dashboardId;
}
