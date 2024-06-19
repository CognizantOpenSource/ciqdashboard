/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.models.CIQDashboardProject;
import com.cognizant.ciqdashboardapi.models.LOB;
import com.cognizant.ciqdashboardapi.models.Organization;
import com.cognizant.ciqdashboardapi.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

/**
 * DashboardProjectController
 *
 * @author Cognizant
 */

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Slf4j
public class DashboardProjectController {

    private CIQDashboardProjectService service;
    private CIQDashboardService dashboardService;
    private ProjectMappingService projectMappingService;
    private UserValidationService userValidationService;
    private SourceToolsService sourceToolsService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','Project.View')")
    public List<CIQDashboardProject> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.get(getUserProjectIds());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.view')")
    public CIQDashboardProject get(@PathVariable String id, @RequestParam("category") String category) {
        return service.getOrAssert(id, category);
    }

//    @GetMapping("/{id}/dashboards")
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.view')")
//    public List<CIQDashboard> getDashboards(@PathVariable String id) {
//
//            CIQDashboardProject ciqDashboardProject = get(id);
//            return dashboardService.getByProjectName(ciqDashboardProject.getName());
//    }

    @GetMapping("/{id}/dashboards")
    @ResponseStatus(OK)
   // @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.view')")
    public List<CIQDashboard> getDashboards(@PathVariable String id, @RequestParam("category") String category) {
        if (category.equalsIgnoreCase("PRJ")) {
            CIQDashboardProject ciqDashboardProject = get(id, category);
            return dashboardService.getByProjectName(ciqDashboardProject.getName(), category);
        } else if (category.equalsIgnoreCase("LOB")) {
            LOB lobObject = service.getLob(id);
            return dashboardService.getByProjectName(lobObject.getLobName(), category);
        } else if (category.equalsIgnoreCase("ORG")) {
            Organization orgObject = service.getOrganization(id);
            return dashboardService.getByProjectName(orgObject.getOrganizationName(), category);
        }
        return null;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#projectId, 'Project','ciqdashboard.project.create')")
    public CIQDashboardProject insert(@Valid @RequestBody CIQDashboardProject project) {
        service.assertNotExists(project);
        CIQDashboardProject dashboardProject = service.insert(project);
        projectMappingService.insert(dashboardProject.getId());
        return dashboardProject;
    }

    @PutMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','ciqdashboard.project.update')")
    public CIQDashboardProject update(@RequestParam("category") String category, @Valid @RequestBody CIQDashboardProject project) {
        CIQDashboardProject dashboardProject =service.update(project, category);
        projectMappingService.update(dashboardProject.getId());
        return dashboardProject;
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(NO_CONTENT)
//    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.delete')")
//    public void delete(@PathVariable String id) {
//        Optional<CIQDashboardProject> dashboardProject = service.get(id);
//        if (dashboardProject.isPresent()) {
//            dashboardService.deleteByProjectName(dashboardProject.get().getName());
//            projectMappingService.delete(dashboardProject.get().getId());
//            service.delete(id);
//        }
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.delete')")
    public void delete(@PathVariable String id, @RequestParam("category") String category) {
        if (category.equalsIgnoreCase("PRJ")) {
            Optional<CIQDashboardProject> dashboardProject = service.get(id);
            if (dashboardProject.isPresent()) {
              //  dashboardService.deleteByProjectName(dashboardProject.get().getName());
                List<CIQDashboard> dashboardList = dashboardService.getByProjectId(id, category);
                List<String> dashboardIdList = dashboardList.stream().map(ciqDashboard -> ciqDashboard.getId()).collect(Collectors.toList());
                dashboardService.deleteByIdIn(dashboardIdList);
                projectMappingService.delete(dashboardProject.get().getId());
                service.delete(id);
            }

        } else if (category.equalsIgnoreCase("LOB")) {
            LOB lobObject = service.getLob(id);
            //dashboardService.deleteByProjectName(lobObject.getLobName());
            List<CIQDashboard> dashboardList = dashboardService.getByProjectId(id, category);
            List<String> dashboardIdList = dashboardList.stream().map(ciqDashboard -> ciqDashboard.getId()).collect(Collectors.toList());
            dashboardService.deleteByIdIn(dashboardIdList);
            service.deleteLob(id);
        }
        else if (category.equalsIgnoreCase("ORG")) {
            Organization orgObject = service.getOrganization(id);
           // dashboardService.deleteByProjectName(orgObject.getOrganizationName());
            List<CIQDashboard> dashboardList = dashboardService.getByProjectId(id, category);
            List<String> dashboardIdList = dashboardList.stream().map(ciqDashboard -> ciqDashboard.getId()).collect(Collectors.toList());
            dashboardService.deleteByIdIn(dashboardIdList);
            service.deleteOrganization(id);
        }
    }

    @GetMapping("/lob/{lobId}")
    public List<CIQDashboardProject> getProjectsByLobId(@PathVariable("lobId") String lobId) {
        return service.getProjectsByLobId(lobId);
    }
    @GetMapping("/org/{orgId}")
    public List<CIQDashboardProject> getProjectsByOrgId(@PathVariable("orgId") String orgId) {
        return service.getProjectsByOrgId(orgId);
    }
    private List<String> getUserProjectIds() {
        return userValidationService.getCurrentUserProjectIds();
    }

    @GetMapping("/source_data")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public JSONArray getDistinctSourceData() throws ClassNotFoundException {

        return sourceToolsService.getDistinctSourceData();

    }
}
