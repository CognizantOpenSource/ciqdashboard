package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("scheduler_runs")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CollectorRunScheduler {
    @Id
    private String id;
    @LastModifiedDate
    private Instant lastUpdatedDate;
    private String desc;
    private String toolName;
}
