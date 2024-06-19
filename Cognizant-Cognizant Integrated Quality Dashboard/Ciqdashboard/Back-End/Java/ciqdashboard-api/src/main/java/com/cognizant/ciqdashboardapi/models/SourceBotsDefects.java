package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_bots_defects")
public class SourceBotsDefects {


    @Id
    private String id;
    private String defId;
    private String files;
    private Integer associatedCount;
    private String projectId;
    private String runId;
    private String modelId;
    private String defectKeys;
    private Integer sevCount;
    private String priorityGraphLabel;
    private String severityGraphLabel;
    private String moduleGraphLabel;
    private Integer modulecnt;
    private Integer prioritycnt;
    private List<String> linkedTestCases;
    private Integer linkedTcCount;
    private String type;
    private String defectData;
    private String linkedTescaseID;
    private String linkedTescaseDescription;
    private String defpredresult;
    private String testcaseResult;

}
