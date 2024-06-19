package com.cognizant.ciqdashboardapi.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("addNewMetricDropDowns")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddNewMetricDropDowns {

    @Id
    private String id;
    private String dropDownCode;
    private String dropDownValue;
    private String remarks;
}
