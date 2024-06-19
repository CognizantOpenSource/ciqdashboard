package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_rally_data")
public class SourceRallyData {

    @Id
    private String id;

    /*user stories*/
    private String userStoryID;
    private String name;
    private String ref;
    private String type;
    private Date creationDate;
    private String workspaceName;
    private String createdBy;
    private String description;
    private Boolean expedite;
    public String formattedId;
    private Date lastUpdateDate;
    private Integer milestones;
    private String owner;
    private String projectName;
    private Boolean ready;
    private String flowState;
    private Date flowStateChangedDate;
    private String lastBuild;
    private Date lastRun;
    private Integer passingTestCaseCount;
    private String scheduleState;
    private String scheduleStatePrefix;
    private Integer testCaseCount;
    private Integer attachments;
    private String packageInfo;
    private Date acceptedDate;
    private Boolean blocked;
    private String blockedReason;
    private String blockerRef;
    private Integer children;
    private String defectStatus;
    private Integer defects;
    private Integer directChildrenCount;
    private Integer directPassingTestCaseCount;
    private Boolean hasParent;
    private Date inProgressDate;
    //private Iteration iteration;
    //private UserStory parent;
    private Double planEstimate;
    private Integer predecessors;
    private Integer successors;
    private Integer taskActualTotal;
    private Integer taskEstimateTotal;
    private Integer tasks;
    private Integer testCases;
    private Integer totalDirectTestCaseCount;
    //private PortfolioItemDetails portfolioItem;
    /* User stories*/


    /* Test cases */
    private String testCaseID;
    //private Execution lastResult;
    private String lastVerdict;
    private String method;
    private String postConditions;
    private String preConditions;
    private String priority;
    private Boolean recycled;
    private Integer results;
    private String risk;
    private Integer steps;
    private String testFolder;
    private Integer testSets;
    private String testType;
    private String validationExpectedResult;
    private String validationInput;
    //private WorkProductDetails workProduct;
    /* Test cases */


    /* Executions */
    private String executionID;
    private String build;
    private Date executionDate;
    private Double duration;
    private String testSet;
    private String tester;
    private String verdict;
    /* Executions */

    /* Defects */
    private String defectID;
    private Boolean acceptedDoc;
    private Date closedDate;
    private Integer defectSuites;
    private Integer duplicates;
    private String environment;
    private String fixedInBuild;
    private String foundInBuild;
    //private Iteration iteration;
    private Date openedDate;
    //private Release release;
    //private UserStory requirement;
    private String resolution;
    private String salesforceCaseID;
    private String salesforceCaseNumber;
    private String severity;
    private String state;
    private String submittedBy;
    private String targetBuild;
    private Date targetDate;
    private Integer taskRemainingTotal;
    private String taskStatus;
    private String TestCaseStatus;
    private String verifiedInBuild;
    /* Defects */

}
