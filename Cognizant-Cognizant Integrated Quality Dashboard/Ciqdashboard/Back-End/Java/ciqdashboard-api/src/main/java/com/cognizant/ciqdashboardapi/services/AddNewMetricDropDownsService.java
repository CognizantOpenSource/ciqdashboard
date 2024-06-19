package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.models.AddNewMetricDropDowns;
import com.cognizant.ciqdashboardapi.repos.AddNewMetricDropDownsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddNewMetricDropDownsService {

   @Autowired
    AddNewMetricDropDownsRepository metricDropdownsRepository;


    public List<AddNewMetricDropDowns> getDropDownValues(String dropDownCode) {
        return metricDropdownsRepository.findByDropDownCode(dropDownCode);
    }

}
