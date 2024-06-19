package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.Organization;
import com.cognizant.ciqdashboardapi.services.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/organization")
@RestController
@Slf4j
public class OrganizationController {

    @Autowired
    OrganizationService orgService;

    @PostMapping("/create")
    @PreAuthorize("hasPermission(#projectId, 'Project','ORG.dashboard.Create')")
    public Organization createOrganization(@RequestBody Organization orgObject) throws Exception {

        return orgService.createOrganization(orgObject);
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission(#projectId, 'Project','ORG.dashboard.view')")
    public List<Organization> getAllOrganization() {
        return orgService.getAllOrganization();

    }
    @GetMapping("/{id}")
    public Organization getOrganizationById(@PathVariable("id") String id) throws Exception {
        return orgService.getOrganizationById(id);

    }
}
