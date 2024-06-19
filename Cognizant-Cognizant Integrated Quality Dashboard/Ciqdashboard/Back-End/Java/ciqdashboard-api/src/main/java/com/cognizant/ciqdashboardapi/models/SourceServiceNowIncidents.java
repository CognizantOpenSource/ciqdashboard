package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "source_servicenow_incidents")
public class SourceServiceNowIncidents {

    @Id
    private String id;

    /* incident */
    private String type;
    private String shortDescription;
    private String projectName;
    private String category;
    private Object assignmentGroup;
    private Object configurationItem;
    private Object situation;
    private Object reminderNotifications;
    private Object assignedTo;
    private Object ETA;
    private Object affectedEnvironment;
    private Object openedBy;
    private String impact;
    private String description;
    private String priority;
    private String contactType;
    private String urgency;
    private Object location;
    private Object state;
    private Object reportedBy;
    private Object openedAt;
    private Object bestContactNumber;
    private Object workplaceLocation;
    private Object subCategory;
    private Object majorIncidentManager;
    /* incident */


    /* RITMS */
    private Object requester;
    private String serviceCategory;
    private Object subcategory;
    private Object businessService;
    /* RITMS */

}
