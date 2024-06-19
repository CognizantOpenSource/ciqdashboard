package com.cognizant.ciqdashboardapi.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DirectDataResponse {

    private String message;
    private CIQDashboard data;
    private boolean calculated;
}
