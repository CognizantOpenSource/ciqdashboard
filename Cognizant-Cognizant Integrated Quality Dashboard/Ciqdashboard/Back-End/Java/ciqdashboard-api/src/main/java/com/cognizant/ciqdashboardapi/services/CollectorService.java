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

package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.db.DBAggregationUtilComponent;
import com.cognizant.ciqdashboardapi.db.GroupAggregateComponent;
import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.ciqdashboardapi.models.chart.data.ChartData;
import com.cognizant.ciqdashboardapi.models.chart.data.ChartDataForFusion;
import com.cognizant.ciqdashboardapi.models.chart.data.DataGridRow;
import com.cognizant.ciqdashboardapi.models.db.view.BaseCollectionDetailsForView;
import com.cognizant.ciqdashboardapi.models.db.view.FieldWithAlias;
import com.cognizant.ciqdashboardapi.models.db.view.LocalForeignField;
import com.cognizant.ciqdashboardapi.models.db.view.ViewDetails;
import com.cognizant.ciqdashboardapi.repos.impl.CollectorRepositoryImpl;
import com.cognizant.ciqdashboardapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CollectorService
 * @author Cognizant
 */

@Service
@Slf4j
public class CollectorService {
    @Autowired
    CollectorRepositoryImpl collectorRepository;
    @Autowired
    DBAggregationUtilComponent dbAggregationUtilComponent;
    @Autowired
    ValidatorService validatorService;
    @Autowired
    GroupAggregateComponent groupAggregateComponent;
    @Autowired
    CIQDashboardDataSourceService dataSourceService;


    /**
     * Getting all collection names from databasd
     *
     * @return set of collections
     */
    public Set<String> getCollectionNames() {
        return collectorRepository.getCollectionNames();
    }

    /**
     * Getting all fields based on {collectionName}
     *
     * @param collectionName name of collection
     * @return set of field names
     */
    public Set<String> getFieldsByCollection(String collectionName) {
        return collectorRepository.getFieldsByCollection(collectionName);
    }

    /**
     * Getting distinct values based on collection and field
     *
     * @param collectionName name of collection
     * @param fieldName      name of field name
     * @return set of field values
     */
    public Set<String> getDistinctValuesByField(String collectionName, String fieldName) {
        return collectorRepository.getDistinctValuesByFieldName(collectionName, fieldName);
    }

    public Set<String> getDistinctValuesBySourceField(String sourceName, String fieldName) {
        CIQDashboardDataSource ciqDashboardDataSource = dataSourceService.assertAndGetByName(sourceName);
        return getDistinctValuesByField(ciqDashboardDataSource.getCollectionName(), fieldName);
    }

    /**
     * Getting Chart based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link ChartData}
     */
    public List<ChartData> getChart(DrillDownChartAggregation chartAggregation, String collectionName) {
        return collectorRepository.getChart(chartAggregation, collectionName);
    }

    public List<ChartDataForFusion> getChartForFusion(DrillDownChartAggregation chartAggregation, String collectionName) {
        return collectorRepository.getChartForFusion(chartAggregation, collectionName);
    }

    /**
     * Getting BarChart based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link ChartData}
     */
    public List<ChartData> getBarChart(BarChartAggregation chartAggregation, String collectionName) {
        Map<String, String> collection = this.getFieldsAndTypesByCollection(collectionName);
        return collectorRepository.getBarChart(chartAggregation, collectionName, collection);
    }

    public List<LinkedHashMap> getBarGaugeChart(BarChartAggregation validateBarGaugeChartAggregation, String collectionName) {
        return collectorRepository.getBarGaugeChart(validateBarGaugeChartAggregation, collectionName);
    }

    /**
     * Getting Table based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link DataGridRow}
     */
    public List<DataGridRow> getTable(TableAggregation chartAggregation, String collectionName) {
        return collectorRepository.getTable(chartAggregation, collectionName);
    }

    /**
     * Getting fields and types based on collection
     *
     * @param collectionName name of collection
     * @return Map, field and type as pair (String, String)
     */
    public List<FieldType> getFieldsAndTypes(String collectionName) {
        return collectorRepository.getFieldsAndTypes(collectionName);
    }

    public Map<String, String> getFieldsAndTypesByCollection(String collectionName) {
        return collectorRepository.getFieldsAndTypesByCollection(collectionName);
    }

    public List<ChartData> getFieldAggregate(List<FilterConfig> filters, AggregateConfig validateAggregate, Set<String> fields, String collectionName) {
        return collectorRepository.getFieldAggregate(filters, validateAggregate, fields, collectionName);
    }

    public List<ChartData> getGroupAggregate(List<FilterConfig> filters, GroupAggregate groupAggregate, Set<String> fields, String collectionName) {
        return groupAggregateComponent.getGroupAggregate(filters, groupAggregate, fields, collectionName);
    }

    public boolean createView(ViewDetails viewDetails) {
        viewValidation(viewDetails);
        return dbAggregationUtilComponent.createView(viewDetails);
    }

    public void deleteView(String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format("Collection(%s) not available in DB", collectionName));
        dbAggregationUtilComponent.deleteView(collectionName);
    }

    public void viewValidation(ViewDetails viewDetails) {
        if (getCollectionNames().contains(viewDetails.getName()))
            throw new InvalidDetailsException(String.format("View already exist with mentioned name(%s) ", viewDetails.getName()));
        BaseCollectionDetailsForView baseCollection = viewDetails.getBaseCollection();
        List<FieldWithAlias> baseCollectionFields = baseCollection.getFields();
        Set<String> baseFieldNames = baseCollectionFields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
        List<String> aliases = baseCollectionFields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toList());

        viewDetails.getLookups().forEach(lookup -> {
            List<FieldWithAlias> lookupFields = lookup.getFields();
            Set<String> fieldNames = lookupFields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
            aliases.addAll(lookupFields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toList()));
            baseFieldNames.addAll(lookup.getLocalForeignFields().stream().map(LocalForeignField::getLocalField).collect(Collectors.toList()));
            fieldNames.addAll(lookup.getLocalForeignFields().stream().map(LocalForeignField::getForeignField).collect(Collectors.toList()));
            validateCollectionField(lookup.getName(), fieldNames);
            validateFieldAndAliases(lookup.getName(), lookupFields);
        });
        validateCollectionField(baseCollection.getName(), baseFieldNames);
        validateFieldAndAliases(baseCollection.getName(), baseCollectionFields);
        Set<String> duplicateAlias = aliases.stream().filter(alias -> Collections.frequency(aliases, alias) > 1)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(duplicateAlias)) {
            throw new InvalidDetailsException(String.format("found same alias name in multiple collections, duplicateAliasList(%s)", duplicateAlias));
        }
    }

    public void validateFieldAndAliases(@NotEmpty(message = "CollectionName should not be null/empty") String collectionName,
                                        @NotEmpty(message = "Fields should not be null/empty") List<FieldWithAlias> fields) {
        Set<String> fieldNames = fields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
        Set<String> aliases = fields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toSet());
        if (fields.size() != fieldNames.size())
            throw new InvalidDetailsException(String.format("found duplicate field name in Collection(%s)", collectionName));
        if (fields.size() != aliases.size())
            throw new InvalidDetailsException(String.format("found duplicate alias name in Collection(%s)", collectionName));
    }

    public void validateCollectionField(@NotEmpty(message = "CollectionName should not be null/empty") String collectionName,
                                        @NotEmpty(message = "Fields should not be null/empty") Set<String> fieldNames) {
        Set<String> fieldsByCollection = getFieldsByCollection(collectionName);
        boolean containsAll = fieldsByCollection.containsAll(fieldNames);
        if (!containsAll)
            throw new InvalidDetailsException(String.format("fields are invalid, it should be in the DB Collection(%s)", collectionName));
    }

    public void addCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        if (getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException("Collection Name " + collectionName + " is already exists in the Database : ");

        collectorRepository.addCustomCollection(collectionName, collectorData);
    }

    public void updateCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format("Collection(%s) not available in DB", collectionName));
        collectorRepository.updateCustomCollection(collectionName, collectorData);
    }
}
