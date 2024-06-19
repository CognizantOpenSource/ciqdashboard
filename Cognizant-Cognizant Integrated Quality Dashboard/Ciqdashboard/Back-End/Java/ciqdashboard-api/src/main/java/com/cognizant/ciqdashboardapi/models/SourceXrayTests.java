package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_xray_tests")
public class SourceXrayTests {

    @Id
    private String id;
    private String unstructured;
    private String gherkin;
    private String scenarioType;
    private String comment;
    private Date startedOn;
    private String defects;
    private String executedById;
    private String assigneeId;
    private Date finishedOn;
    private Date lastModified;

    /*Status*/
    private String statusName;
    private String statusDescription;
    private Boolean statusFinal;
    /*Status*/

    /*Test Type*/
    private String testTypeId;
    private String testTypeName;
    private String testTypeKind;
    /*Test Type*/

    /*Project*/
    private String projectId;
    private String projectKey;
    private String projectName;
    private String projectTypeKey;
    /*Project*/

    /*Fix Version*/
    private String fixVersionId;
    private String fixVersionName;
    private Boolean fixVersionArchived;
    private Boolean fixVersionReleased;
    private Date fixVersionReleaseDate;
    /*Fix Version*/

    /*Test*/
    private String testId;

    private String testKey;

    private String testPriorityName;
    private String testPriorityId;

    private String testSets;
    private String testPlans;

    private String testAssigneeName;
    private String testAssigneeKey;
    private String testAssigneeEmailAddress;
    private String testAssigneeDisplayName;
    private Boolean testAssigneeActive;
    private String testAssigneeTimeZone;

    private String testReporterName;
    private String testReporterKey;
    private String testReporterEmailAddress;
    private String testReporterDisplayName;
    private Boolean testReporterActive;
    private String testReporterTimeZone;

    private String testUnstructured;
    private String testGherkin;

    private String testFolderName;
    private String testFolderPath;

    private String testScenarioType;
    private Date testLastModified;
    /*Test*/

    /*Test Execution*/
    private String testExecId;

    private String testExecKey;

    private String testEnvironments;

    private String testExecAssigneeName;
    private String testExecAssigneeKey;
    private String testExecAssigneeEmailAddress;
    private String testExecAssigneeDisplayName;
    private Boolean testExecAssigneeActive;
    private String testExecAssigneeTimeZone;

    private String testExecReporterName;
    private String testExecReporterKey;
    private String testExecReporterEmailAddress;
    private String testExecReporterDisplayName;
    private Boolean testExecReporterActive;
    private String testExecReporterTimeZone;

    private Date testExecLastModified;
    /*Test Execution*/

    private Object[] customFields;
    private Object[] parameters;

}
