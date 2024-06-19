package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_alm_assets")
public class SourceAlmAssets {

    @Id
    private String id;

    /* requirements */
    private String requirementId;
    private String requirementName;
    private String assetType;
    private String description;
    private String attachment;
    private String status;
    private String domainName;
    private String projectName;
    private String targetReleaseId;
    private String targetReleaseName;
    private String targetCycleId;
    private String targetCycleName;
    private String author;
    private String parentId;
    private String comments;
    private String requirementTypeId;
    private String requirementTypeName;
    private String product;
    private String fatherName;
    private String reviewed;
    private String priority;
    private String hasLinkage;
    private String noOfSons;
    private String hasRichContent;
    private String verStamp;
    private String isTemplate;
    private String hierarchicalPath;
    private String orderId;
    private Date creationDate;
    private LocalTime creationTime;
    private Date lastModified;
    private String versionNumber;
    /* requirements */

    /* TestRun */
    private String testRunId;
    private String testRunName;
    private long duration;
    private String cycle;
    private String path;
    private String host;
    private String draft;
    private String state;
    private String testDescription;
    private String tester;
    private String assignedCycleId;
    private String assignedCycleName;
    private String releaseId;
    private String testSetName;
    private String osConfig;
    private String testId;
    private String testName;
    private String subtypeId;
    private String cycleId;
    private String testConfigId;
    private String testInstance;
    private String osName;
    private String osBuild;
    private String testCycleName;
    private String testCycleId;
    private Date executionDate;
    private LocalTime executionTime;
    /* TestRun */

    /* Release */
    private String releaseName;
    private String domainId;
    private String projectId;
    private Date startDate;
    private Date endDate;
    private int reqCount;
    private int scopeItemsCount;
    private int milestonesCount;
    private Object hasAttachments;
    /* Release */

    /* Test */
    private String template;
    private String timeout;
    private String designer;
    private String steps;
    private String type;
    private String executionStatus;
    private String devComments;
    private String baseTestId;
    private String stepParam;
    /* Test */

    /* Cycle */
    private String cycleName;
    private int cfCount;
    /* Cycle */

    /* Defect */
    private String defectId;
    private String defectName;
    private String severity;
    private String subject;
    private String project;
    private String reproducible;
    private String environment;
    private String detectedBy;
    private String assignedTo;
    private String detectedCycleId;
    private String detectedCycleName;
    private String detectedReleaseId;
    private String detectedReleaseName;
    private Long actualFixTime;
    private String detectionVersion;
    private String hasOthersLinkage;
    private String requestType;
    private String runReference;
    private String requestNote;
    private String requestServer;
    private String toMail;
    private String stepReference;
    private String estimatedFixTime;
    private String requestId;
    private String cycleReference;
    private String testReference;
    private String plannedClosingVer;
    private String extendedReference;
    private String closingVersion;
    private String hasChange;
    private Date closingDate;
    /* Defect */

}
