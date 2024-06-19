package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.base.models.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document(collection = "ciqDashboardLOB")
public class LOB extends BaseModel {
    @Id
    private String id;
    @NotBlank(message = "LOB Name should not be empty/null")
    private String lobName;
    private String lobDescription;
    private String orgId;
}
