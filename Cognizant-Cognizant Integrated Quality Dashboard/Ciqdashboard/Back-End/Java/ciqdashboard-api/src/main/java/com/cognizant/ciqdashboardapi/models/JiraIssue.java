package com.cognizant.ciqdashboardapi.models;

/*
 *  © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JiraIssue - Refers to jiraIssue collection in mongodb
 * @author Cognizant
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_jira_issues")
public class JiraIssue {

    @Id
    private String id;
    private String expand;
    private String self;
    private String key;

    /* Fields */

    /* project */
    private String projectSelf;
    private String projectId;
    private String projectKey;
    private String projectName;
    private String projectTypeKey;
    private Map<String, String> projectAvatarUrls;
    private String projectCategoryId;
    private String projectCategoryName;
    /* project */

    /* issueType */
    private String issueTypeSelf;
    private String issueTypeId;
    private String issueTypeDescription;
    private String issueTypeIconUrl;
    private String issueTypeName;
    private Boolean issueTypeSubtask;
    private Integer issueTypeAvatarId;
    /* issueType */

    /*components*/
    private String componentId;
    private String componentName;
    /*components*/

    private Double reOpenCounter;
    private boolean reOpenFlag;
    private Object timeSpent;
    private Object timeOriginalEstimate;
    private String description;

    /*Fix Version*/
    private String fixVersionId;
    private String fixVersionName;
    private boolean fixVersionArchived;
    private boolean fixVersionReleased;
    private Date fixVersionReleaseDate;
    /*Fix Version*/

    private Object aggregateTimeSpent;
    private Object timeTracking;
    //private List<String> customField10104 = null;
    //private Object customField10203;
    //private Object customField10105;
    private List<Object> attachment = null;
    private Object aggregateTimeEstimate;
    private Date resolutionDate;
    private Long workRatio;
    private String summary;
    private Object lastViewed;
    private Object watches;
    private List<Object> subTasks = null;
    //private Object customField10000;
    private Object aggregateProgress;

    /* Priority */
    private String priority;
    private String priorityId;
    /* Priority */

    /* versions */
    //private String versionsSelf;
    private String versionsId;
    //private String versionsDescription;
    private String versionsName;
    //private Boolean versionsArchived;
    //private Boolean versionsReleased;
    //private Date versionsReleaseDate;
    /* versions */

    //    private Object customField10100;
//    private Object customField10201;
    private List<Object> labels = null;
    //    private Object customField10202;
    private Object environment;
    private Object timeEstimate;
    private Object aggregateTimeOriginalEstimate;
    private List<Object> versions = null;
    private Object dueDate;
    private Object progress;
    private Object comment;
    private Object votes;
    private Object workLog;

    private Date created;
    private Date updated;

    /* Sprint Details */
    private String sprintId;
    private Integer sprintRapidViewId;
    private String sprintState;
    private String sprintName;
    private Integer sprintSequence;
    private Object sprintGoal;
    private String sprintStartDate;
    private String sprintEndDate;
    private String sprintCompleteDate;
    private String sprintActivatedDate;
    private Boolean sprintAutoStartStop;
    /* Sprint Details */

    /* creator */
    private String creatorSelf;
    private String creatorName;
    private String creatorKey;
    private String creatorEmailAddress;
    private Map<String, String> creatorAvatarUrls;
    private String creatorDisplayName;
    private Boolean creatorActive;
    private String creatorTimeZone;
    /* creator */

    /* reporter */
    private String reporterSelf;
    private String reporterName;
    private String reporterKey;
    private String reporterEmailAddress;
    private Map<String, String> reporterAvatarUrls;
    private String reporterDisplayName;
    private Boolean reporterActive;
    private String reporterTimeZone;
    /* reporter */

    /* assignee */
    private String assigneeSelf;
    private String assigneeName;
    private String assigneeKey;
    private String assigneeEmailAddress;
    private Map<String, String> assigneeAvatarUrls;
    private String assigneeDisplayName;
    private Boolean assigneeActive;
    private String assigneeTimeZone;
    /* assignee */

    /* resolution */
    private String resolutionSelf;
    private String resolutionId;
    private String resolutionDescription;
    private String resolutionName;
    /* resolution */

    /* status */
    private String statusSelf;
    private String statusDescription;
    private String statusIconUrl;
    private String statusName;
    private String statusId;

    /* statusCategory */
    private String statusCategorySelf;
    private Integer statusCategoryId;
    private String statusCategoryKey;
    private String statusCategoryColorName;
    private String statusCategoryName;


    private String defectType;
    //private String causeOfDefect;
    //private String severity;
    //private String workstream;
    private String module;
    //private String detectedInEnvironment;
    private String testing;
    private String testType;

    /*Linked Issues */
    //private List<IssueLink> issueLinks;
    private String linkedEpicId;
    private String linkedEpicKey;
    private String linkedStoryId;
    private String linkedStoryKey;
    private String linkedTaskId;
    private String linkedTaskKey;
    private String linkedSubTaskId;
    private String linkedSubTaskKey;
    private String linkedTestId;
    private String linkedTestKey;
    private String linkedBugId;
    private String linkedBugKey;


    private Object issueLinks;
    private Object watchCount;
    private Object isWatching;
    private Object total;
    private Object percent;
    private String hasVoted;
    private String inwardIssue;
    private String outwardIssue;
    private String issueType;
    private String status;
    private String iconUrl;
    private String name;
    private String subtask;
    private String statusCategory;
    private String colorName;

    private String predictedDefects;
    private String duplicateDefects;


    private Object testingType;
    private Object defectSeverity;
    private Object bugClassification;
    private Object defectImpact;
    private String defectImpactedSystem;
    private String ExternalApplicationName;

    private String impactedTestCase;
    private Boolean impactedTestCasesFlag;
    private Integer impactedTestCasesCount;
    private String ETA;

}
