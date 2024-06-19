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

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * DashboardController
 *
 * @author Cognizant
 */

@RestController
@RequestMapping("/dashboards")
@AllArgsConstructor
public class DashboardController {

    public UserValidationService userValidationService;
    private CIQDashboardService service;
    private CIQDashboardProjectService projectService;
    private LOBService lobService;
    private OrganizationService orgService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public List<CIQDashboard> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.getAllByPermissions();
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public List<CIQDashboard> getAllByCategory(@PathVariable("category") String category) {
        if (userValidationService.isAdmin())
            return service.getAllByCategory(category);
        return service.getAllByPermissions();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public CIQDashboard get(@PathVariable String id, @RequestParam("category") String category) {
        return service.assertAndGetByIdAndCategory(id, category);
    }


//    @GetMapping("/search")
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
//    public List<CIQDashboard> getByProjectName(@RequestParam String projectName) {
//        validateProjectAccess(projectName, "PRJ");
//        return service.getByProjectName(projectName);
//    }
//
//    @PostMapping
//    @ResponseStatus(CREATED)
//    @Validated
//    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.create')")
//    public CIQDashboard add(@Valid @RequestBody CIQDashboard dashboard) {
//        validateProjectAccess(dashboard.getProjectName());
//        return service.add(dashboard);
//    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public List<CIQDashboard> getByProjectName(@RequestParam String projectName) {
        validateProjectAccess(projectName, "PRJ");
        return service.getByProjectName(projectName, null);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.create')")
    public CIQDashboard add(@Valid @RequestBody CIQDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName(), dashboard.getCategory());
        return service.add(dashboard);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.update')")
    public CIQDashboard update(@Valid @RequestBody CIQDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName(), dashboard.getCategory());
        validateUpdateOpenAccess(dashboard, service.assertAndGetByIdAndCategory(dashboard.getId(), dashboard.getCategory()));
        return service.update(dashboard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }

    private void validateProjectAccess(String projectName, String category) {
        if (category.equalsIgnoreCase("PRJ")) {
            projectService.assertAndGetByName(projectName);
            if (!userValidationService.isAdmin()) {
                List<String> names = userValidationService.getCurrentUserProjectNames();
                if (!names.contains(projectName)) {
                    throw new AccessDeniedException(String.format("Project access not found with projectName:%s", projectName));
                }
            }
        } else if (category.equalsIgnoreCase("LOB")) {
            lobService.assertAndGetByName(projectName);
//            if (!userValidationService.isAdmin()) {
//                List<String> names = userValidationService.getCurrentUserProjectNames();
//                if (!names.contains(projectName)) {
//                    throw new AccessDeniedException(String.format("Project access not found with projectName:%s", projectName));
//                }
//            }
        } else if (category.equalsIgnoreCase("ORG")) {
            orgService.assertAndGetByName(projectName);
        }
    }

    private void validateUpdateOpenAccess(CIQDashboard newDashboard, CIQDashboard existDashboard) {
        if (!userValidationService.isAdmin()) {
            Boolean openAccess = newDashboard.getOpenAccess();
            if ((null == openAccess || false == openAccess)
                    && !existDashboard.getCreatedUser().equals(userValidationService.getCurrentUserEmailId())) {
                throw new InvalidDetailsException("Not allowed to update openAccess for the dashboard");
            }
        }
    }


}
