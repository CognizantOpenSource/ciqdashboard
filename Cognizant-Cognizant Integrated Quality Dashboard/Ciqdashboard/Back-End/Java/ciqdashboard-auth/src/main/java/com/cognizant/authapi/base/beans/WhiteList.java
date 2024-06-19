/*
package com.cognizant.authapi.base.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Document(collection = "whitelist")
@Data
public class WhiteList {
    @Id
    private String id;
    private boolean whitelist;
    private String type;
    private List<String> match;

    */
/* User Auditing fields*//*

    @JsonIgnore
    @CreatedBy
    private String createdUser;
    @JsonIgnore
    @CreatedDate
    private Date createdDate;
    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedUser;
    @JsonIgnore
    @LastModifiedDate
    private Instant lastModifiedDate;

}
*/
