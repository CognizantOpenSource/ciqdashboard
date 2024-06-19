package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.LOB;
import com.cognizant.ciqdashboardapi.models.Organization;
import com.cognizant.ciqdashboardapi.services.LOBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lob")
@Slf4j
public class LOBController {

    @Autowired
    LOBService lobService;


    @PostMapping("/create")
    @PreAuthorize("hasPermission(#projectId, 'Project','Lob.Create')")
    public LOB createLOB(@RequestBody LOB lobObject) throws Exception {

        return lobService.createLOB(lobObject);
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission(#projectId, 'Project','ORG.LOB.View')")
    public List<LOB> getAllLOB() {
        return lobService.getAllLOB();
    }

    @GetMapping("/{id}")
    public LOB getLOBbyId(@PathVariable("id") String id) throws Exception {
        return lobService.getLobById(id);

    }

    @GetMapping("/org/{orgId}")
    public List<LOB> getLobByOrgId(@PathVariable("orgId") String orgId){
        return lobService.getLobByOrgId(orgId);
    }

}
