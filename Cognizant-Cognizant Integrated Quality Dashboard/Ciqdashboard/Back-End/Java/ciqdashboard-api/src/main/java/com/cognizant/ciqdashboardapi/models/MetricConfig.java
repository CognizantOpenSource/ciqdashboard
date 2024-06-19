package com.cognizant.ciqdashboardapi.models;

import lombok.*;
import net.minidev.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("MetricConfig")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MetricConfig {
    @Id
    private String id;
    private String metricName;
    private String category;
    private String calculationType;
    private String toolType;
    private String projectName;

    private String formula;
    private Map<String, JSONObject> formulaParams;

    //private String groupName;
    private String groupBy;
    private String grouping;
    private String groupValue;

    private String trending;
    private String trendBy;
    private String trendingField;
    private Integer trendCount;

    private String customFunction;
    private String customFunctionName;
}
