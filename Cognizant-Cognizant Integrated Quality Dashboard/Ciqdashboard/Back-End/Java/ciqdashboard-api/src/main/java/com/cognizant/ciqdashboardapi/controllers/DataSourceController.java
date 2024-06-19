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

import com.cognizant.ciqdashboardapi.models.CIQDashboardDataSource;
import com.cognizant.ciqdashboardapi.services.CIQDashboardDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * DataSourceController
 * @author Cognizant
 */

@RestController
@RequestMapping("/data-sources")
public class DataSourceController {
    @Autowired
    CIQDashboardDataSourceService service;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public List<CIQDashboardDataSource> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public CIQDashboardDataSource get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public CIQDashboardDataSource getByName(@RequestParam String name) {
        return service.assertAndGetByName(name);
    }

    @PostMapping
    @Validated
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.create')")
    public CIQDashboardDataSource add(@Valid @RequestBody CIQDashboardDataSource dashboardDataSource) {
        return service.add(dashboardDataSource);
    }

    @PutMapping
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.update')")
    public CIQDashboardDataSource update(@Valid @RequestBody CIQDashboardDataSource dashboardDataSource) {
        return service.update(dashboardDataSource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }

}
