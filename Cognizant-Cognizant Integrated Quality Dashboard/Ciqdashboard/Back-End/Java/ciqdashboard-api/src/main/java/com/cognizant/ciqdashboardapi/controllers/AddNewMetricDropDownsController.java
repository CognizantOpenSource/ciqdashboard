package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.AddNewMetricDropDowns;
import com.cognizant.ciqdashboardapi.services.AddNewMetricDropDownsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/metricDropdowns")
public class AddNewMetricDropDownsController {

    @Autowired
    AddNewMetricDropDownsService metricDropdownsService;

    @GetMapping
    @ResponseStatus(OK)
    //@PreAuthorize("hasPermission(#addNewMetricDropdownId, 'addNewMetricDropdown','addNewMetricDropdown.chart.view')")
    public List<AddNewMetricDropDowns> getDropDownValues(@RequestParam("dropDownCode") String dropDownCode) {
        return metricDropdownsService.getDropDownValues(dropDownCode);
    }
}
