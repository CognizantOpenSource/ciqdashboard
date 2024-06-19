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

import com.cognizant.ciqdashboardapi.common.Constants;
import com.cognizant.ciqdashboardapi.errors.ApiResponse;
import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.FieldType;
import com.cognizant.ciqdashboardapi.models.db.view.ViewDetails;
import com.cognizant.ciqdashboardapi.services.CollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
/**
 * CollectorController
 * @author Cognizant
 */

@RestController
@RequestMapping("/collector")
@Slf4j
public class CollectorController {
    @Autowired
    CollectorService collectorService;

    @GetMapping("/collection-names")
    public Set<String> getCollectionNames() {
        return collectorService.getCollectionNames();
    }

    @GetMapping("/fields-by-collection")
    public Set<String> getFieldsByCollection(@RequestParam(name = "collection-name") String collectionName) {
        return collectorService.getFieldsByCollection(collectionName);
    }

    @GetMapping("/fields-types")
    public List<FieldType> getFieldsAndTypes(@RequestParam(name = "collection-name") String collectionName) {
        return collectorService.getFieldsAndTypes(collectionName);
    }
    @GetMapping("/distinct-values-by-field-collection")
    public Set<String> getDistinctValuesByField(@RequestParam(name = "collection-name") String collectionName,
                                                @RequestParam(name = "field-name") String fieldName) {
        return collectorService.getDistinctValuesByField(collectionName, fieldName);
    }

    @GetMapping("/distinct-values-by-field")
    public Set<String> getDistinctValuesByFieldSource(@RequestParam(name = "source-name") String sourceName,
                                                @RequestParam(name = "field-name") String fieldName) {
        return collectorService.getDistinctValuesBySourceField(sourceName, fieldName);
    }

    @PostMapping("/create-view")
    @Validated
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.create')")
    public ResponseEntity<ApiResponse> createView(@Valid @RequestBody ViewDetails viewDetails, HttpServletRequest request) {
        if (!viewDetails.getName().startsWith(Constants.COLLECTION_FILTER_SOURCE))
            throw new InvalidDetailsException(String.format("View name should start with '%s'", Constants.COLLECTION_FILTER_SOURCE));
        collectorService.createView(viewDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(LocalDateTime.now(),
                CREATED.value(),
                "",
                "View created successfully",
                request.getRequestURI()));
    }

    @PostMapping("/create-collection")
    @ResponseStatus(CREATED)
    public ResponseEntity<ApiResponse> createCustomCollection(@RequestParam String collectionName,
                                                              @RequestBody List<Map<String, Object>> collectorData,
                                                              HttpServletRequest request) {
        if (!collectionName.startsWith(Constants.COLLECTION_FILTER_SOURCE))
            throw new InvalidDetailsException(String.format("Collection name should start with '%s'", Constants.COLLECTION_FILTER_SOURCE));
        collectorService.addCustomCollection(collectionName, collectorData);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(LocalDateTime.now(),
                CREATED.value(),
                "",
                "Collection created successfully",
                request.getRequestURI()));

    }

    @PutMapping("/update-collection")
    @ResponseStatus(OK)
    public ResponseEntity<ApiResponse> updateCustomCollection(@RequestParam String collectionName,
                                                              @RequestBody List<Map<String, Object>> collectorData,
                                                              @RequestParam(required = false, defaultValue = "false") boolean override,
                                                              HttpServletRequest request) {
        if (!override) {
            collectorService.updateCustomCollection(collectionName, collectorData);
        } else {
            deleteView(collectionName, request);
            createCustomCollection(collectionName, collectorData, request);
        }
        return ResponseEntity.status(OK).body(new ApiResponse(LocalDateTime.now(),
                OK.value(),
                "",
                "Collection updated successfully",
                request.getRequestURI()));

    }

    @DeleteMapping("/drop-collection")
    @Validated
    public ResponseEntity<ApiResponse> deleteView(@RequestParam String collectionName, HttpServletRequest request) {
        collectorService.deleteView(collectionName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(LocalDateTime.now(),
                NO_CONTENT.value(),
                "",
                "Collection/View dropped successfully",
                request.getRequestURI()));
    }

}
