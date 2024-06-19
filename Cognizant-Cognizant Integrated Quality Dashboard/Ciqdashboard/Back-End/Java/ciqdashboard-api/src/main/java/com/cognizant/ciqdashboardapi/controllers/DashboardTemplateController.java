package com.cognizant.ciqdashboardapi.controllers;


import com.cognizant.ciqdashboardapi.models.CIQDashboardTemplate;
import com.cognizant.ciqdashboardapi.services.CIQDashboardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/dashboard-template")
public class DashboardTemplateController {
    @Autowired
    CIQDashboardTemplateService service;

    @GetMapping("/get-all-templates/{category}")
    @PreAuthorize("hasPermission(#projectId, 'Project','Dashboard.Template')")
    public List<CIQDashboardTemplate> getAllTemplates(@PathVariable("category") String category) throws Exception {
        System.out.println("$$$$$$$$$ category -- " + category);
        return service.getAllTemplates(category);
    }

    @GetMapping("/get-all-templates/")
    @PreAuthorize("hasPermission(#projectId, 'Project','Dashboard.Template')")
    public List<CIQDashboardTemplate> getAllTemplates() throws Exception {

        return service.getAllTemplates();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated
    @PreAuthorize("hasPermission(#projectId, 'Project','Dashboard.Template')")
    public CIQDashboardTemplate add(@Valid @RequestBody CIQDashboardTemplate ciqDashboardTemplate) {
        //validateProjectAccess(dashboard.getProjectName(), dashboard.getCategory());
        return service.add(ciqDashboardTemplate);
    }
}
