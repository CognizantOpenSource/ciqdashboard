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

import com.cognizant.ciqdashboardapi.models.DirectDataResponse;
import com.cognizant.ciqdashboardapi.models.FilterConfig;
import com.cognizant.ciqdashboardapi.models.IDChartItem;
import com.cognizant.ciqdashboardapi.models.IDChartItemDataDTO;
import com.cognizant.ciqdashboardapi.services.CIQDashboardDataSourceService;
import com.cognizant.ciqdashboardapi.services.IDChartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/**
 * ChartItemController
 *
 * @author Cognizant
 */

@RestController
@RequestMapping("/items")
public class ChartItemController {

    @Autowired
    IDChartItemService service;
    @Autowired
    CIQDashboardDataSourceService ciqDashboardDataSourceService;

//    @GetMapping
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
//    public List<IDChartItem> getAll() {
//        return service.getAll();
//    }

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public List<IDChartItem> getAllByCategory(@RequestParam("category") String category) {
        return service.getAllByCategory(category);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public IDChartItem get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    /*
     * Commented for the future implementation
     * */
    /*@GetMapping("/search")*/
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
//    public List<IDChartItem> searchByString(@RequestParam String searchString) {
//        return service.searchByString(searchString);
//    }

    @GetMapping("/search/name")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public List<IDChartItem> searchByNames(@RequestParam List<String> names) {
        return service.searchByNames(names);
    }


//    @PostMapping("/{id}/chart-data")
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
//    public IDChartItemDataDTO getChartDataByCategory(@PathVariable String id, @RequestParam("projectName") String projectName, @RequestParam("dashboardName") String dashboardName, @RequestParam("category") String category, @RequestParam("lobId") String lobId,@RequestParam("orgId") String orgId, @RequestBody(required = false) Optional<List<FilterConfig>> filters) throws Exception {
//        System.out.println("******************## Chart Data  --- projName +" + projectName + " " + dashboardName + " lobId - " + lobId);
//        return service.getChartData(id, projectName, dashboardName, category, lobId,null, filters);
//    }

    @PostMapping("/{id}/chart-data")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public IDChartItemDataDTO getChartDataByCategory(@PathVariable String id, @RequestParam("dashboardId") String dashboardId, @RequestParam("category") String category, @RequestParam("layerId") String layerId ,@RequestParam("pageId") String pageId,@RequestBody(required = false) Optional<List<FilterConfig>> filters) throws Exception {
        System.out.println("******************## Chart Data  --- chartItem id - " + id + " dashboardId - " + dashboardId+ " category - " + category + " layerId - " + layerId);
        return service.getChartData(id, dashboardId, category, layerId ,pageId,filters);
    }
    /*public IDChartItemDataDTO getChartDataByCategory(@PathVariable String id, @RequestParam("projectName") String projectName, @RequestParam("dashboardName") String dashboardName, @RequestParam("category") String category, @RequestParam("lobId") String lobId, @RequestParam("orgId") String orgId,@RequestBody(required = false) Optional<List<FilterConfig>> filters) throws Exception {
        System.out.println("******************## Chart Data  --- projName +" + projectName + " " + dashboardName + " lobId - " + lobId);
        return service.getChartData(id, projectName, dashboardName, category, lobId, orgId,filters);
    }*/


//    @PostMapping("/{id}/chart-data")
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
//    public IDChartItemDataDTO getChartDataByCategory(@PathVariable String id, @RequestParam("projectName") String projectName, @RequestParam("dashboardName") String dashboardName, @RequestBody(required = false) Optional<List<FilterConfig>> filters) throws Exception {
//        System.out.println("******************## Chart Data  --- projName +" + projectName + " " + dashboardName + " lobId - " + "lobId");
//        return service.getChartData(id, projectName, dashboardName, "LOB", "636a01dbca027258f5e47bb2", filters);
//    }

    @PostMapping("/preview")
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public IDChartItemDataDTO preview(@Valid @RequestBody IDChartItem chartItem) throws NoSuchFieldException, IllegalAccessException {
        ciqDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.preview(chartItem);
    }

    @PostMapping
    @Validated
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.create')")
    public IDChartItem add(@Valid @RequestBody IDChartItem chartItem) {
        if (!"#none".equalsIgnoreCase(chartItem.getSource()))
            ciqDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.add(chartItem);
    }

    @PutMapping
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.update')")
    public IDChartItem update(@Valid @RequestBody IDChartItem chartItem) {
        if (!"#none".equalsIgnoreCase(chartItem.getSource()))
            ciqDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.update(chartItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }

    @PostMapping("/calculate-directchart-data")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
    public DirectDataResponse calculateDirectDataByCategory(@RequestParam("dashboardId") String dashboardId, @RequestParam("category") String category, @RequestParam("layerId") String layerId , @RequestBody(required = false) Optional<List<FilterConfig>> filters) throws Exception {
       // System.out.println("******************## Chart Data  --- chartItem id - " + id + " dashboardId - " + dashboardId+ " category - " + category + " layerId - " + layerId);
        return service.calculateDirectChartData(layerId,dashboardId,category);
    }

//    @PostMapping("/{id}/metric-data")
//    @ResponseStatus(OK)
//    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','ciqdashboard.chart.view')")
//    public IDChartItemDataDTO getMetricData(@PathVariable String id, @RequestBody(required = false) JSONObject filters) {
//        System.out.println("id - " + id + " projectName - " + filters);
//        return service.getMetricData(id, filters.get("projectName").toString(), filters.get("dashboardName").toString());
//    }
}
