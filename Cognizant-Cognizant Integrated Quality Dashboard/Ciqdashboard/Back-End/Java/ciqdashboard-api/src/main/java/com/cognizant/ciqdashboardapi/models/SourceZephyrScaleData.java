package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_zephyrscale_data")
public class SourceZephyrScaleData {


    @Id
    private String id;
    private String key;
    private String name;
    private long majorVersion;
    private String type;

    /* Project */
    private long projectId;
    private String projectKey;
    private String projectName;
    /* Project */

    private Long folderId;
    private String folderName;

    private Date createdOn;
    private String createdBy;
    private Date updatedOn;
    private String updatedBy;

    /* Priority */
    private Long priorityId;
    private String priorityName;
    private Long priorityIndex;
    private Boolean priorityIsDefault;
    /* Priority */

    /* Status */
    private Long statusId;
    private String statusName;
    private Long statusIndex;
    private Boolean statusIsDefault;
    /* Status */

    /* Last Test Result */
    private Long lastTestResultStatusId;
    private String lastTestResultStatusName;
    private Boolean lastTestResultStatusIsDefault;
    /* Last Test Result */

    private String[] labels;

    /* Folder */
    private Long dropId;
    private String dropName;
    /* Folder */

    /* Sub-Folder:child(1) */
    private Long moduleId;
    private String moduleName;
    /* Sub-Folder:child(1) */

    /* Sub-Folder:child(2) */
    private Long subModuleId;
    private String subModuleName;
    /* Sub-Folder:child(2) */

    /* CustomFields */
    private String testType;
    private String testEnvironment;
    private String cycleName;
    private String srClassification;
    private String srType;
    private String language;
    private String reqIdAll;
    private String reqId;
    private String srAction;
    private String wave;
    private String srArea;
    private String srSubArea;
    private String release;
    private String useCaseId;
    /* CustomFields */

    private Long averageTime;
    private boolean archived;
    private boolean latestVersion;


    private String testCaseKey;
    private String testCaseStatus;
    private Object executionSummary;
    private String folder;
    private String cycleKey;
    private String cycleExecutionStatus;
    private String assignedTo;
    private String userKey;
    private Date plannedStartDate;
    private Date plannedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String executedBy;
    private Date executionDate;
    private Integer estimatedTime;
    private Integer executionTime;
    private Integer issueCount;
    private Integer testCaseCount;

    private Object projectCategoryId;
    private String projectCategoryName;
    private String owner;
    private String description;
    private String notExecuted;
    private String deferred;

    private String firstFolderName;
    private String day7Flag;
    private String day30Flag;

}
