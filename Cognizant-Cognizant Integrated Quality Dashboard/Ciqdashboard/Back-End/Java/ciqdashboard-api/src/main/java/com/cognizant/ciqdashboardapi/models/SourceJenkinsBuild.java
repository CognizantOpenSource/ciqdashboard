package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_jenkins_build")
public class SourceJenkinsBuild {

    @Id
    private String id;
    private String jobFullName;
    private String jobDescription;
    private String jobName;
    private Boolean building;
    private Object description;
    private String displayName;
    private Integer duration;
    private Integer estimatedDuration;
    private Object executor;
    private String fullDisplayName;
    private String buildId;
    private Boolean keepLog;
    private Integer number;
    private Integer queueId;
    private String result;
    private Long timestamp;
    private Date buildStartTime;
    private Date buildEndTime;
    private String url;
    private List<Object> changeSets = null;
    private List<Object> culprits = null;
    //private BaseBuild _nextBuild;
    //private BaseBuild _previousBuild;

}
