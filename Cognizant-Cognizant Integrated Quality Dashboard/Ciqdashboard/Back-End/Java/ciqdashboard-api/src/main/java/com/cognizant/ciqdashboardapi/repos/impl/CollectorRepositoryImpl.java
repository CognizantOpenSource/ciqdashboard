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

package com.cognizant.ciqdashboardapi.repos.impl;

import com.cognizant.ciqdashboardapi.db.AggregationUtil;
import com.cognizant.ciqdashboardapi.db.DBAggregationUtilComponent;
import com.cognizant.ciqdashboardapi.common.Constants;
import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.*;
import com.cognizant.ciqdashboardapi.models.chart.DBSort;
import com.cognizant.ciqdashboardapi.models.chart.data.ChartData;
import com.cognizant.ciqdashboardapi.models.chart.data.ChartDataForFusion;
import com.cognizant.ciqdashboardapi.models.chart.data.DataGridRow;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.cognizant.ciqdashboardapi.common.Constants.COLLECTION_S_NOT_AVAILABLE_IN_DB;
import static com.cognizant.ciqdashboardapi.models.Type.AggregateType.SUM;

/**
 * CollectorRepositoryImpl
 * @author Cognizant
 */

@Repository
@Slf4j
public class CollectorRepositoryImpl {
    @Autowired
    MongoTemplate template;
    @Autowired
    private AggregationUtil aggregationUtil;
    @Autowired
    DBAggregationUtilComponent dbAggregationUtilComponent;

    String underscoreId = Constants.UNDERSCORE_ID;
    List<String> returnFields = new ArrayList<>();
    String name = Constants.CHART_FIELD_NAME;
    String value = Constants.CHART_FIELD_VALUE;
    String children = Constants.CHART_FIELD_CHILDREN;
    String id = Constants.CHART_FIELD_ID;
    String date = Constants.CHART_FIELD_DATE;

    {
        returnFields.add(name);
        returnFields.add(value);
        returnFields.add(children);
    }

    /**
     * Getting all collection names from databasd
     *
     * @return set of collections
     */
    public Set<String> getCollectionNames() {
        return template.getCollectionNames().stream()
                .filter(collectionName -> collectionName.startsWith(Constants.COLLECTION_FILTER_SOURCE)).collect(Collectors.toSet());

    }

    /**
     * Getting all fields based on {collectionName}
     *
     * @param collectionName name of collection
     * @return set of field names
     */
    public Set<String> getFieldsByCollection(String collectionName) {
        if (getCollectionNames().contains(collectionName)) {
            return new HashSet<>(dbAggregationUtilComponent.findListOfFields(collectionName));
        } else {
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        }
    }

    /**
     * Getting types based on fields for the mentioned {@Code collectionName}
     *
     * @param collectionName name of collection
     * @return {@link Map} with (key, value) pair as (String, Object)
     */
    public Map<String, String> getFieldsAndTypesByCollection(String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));

        final Map<String, String> fieldTypes = new HashMap<>();
        List<String> fields = dbAggregationUtilComponent.findListOfFields(collectionName);
        fields.forEach(field -> {
            String type = dbAggregationUtilComponent.findFieldType(collectionName, field);
            if (StringUtils.isEmpty(type)) {
                fieldTypes.put(field, "object");
            } else {
                fieldTypes.put(field, type);
            }
        });

        return fieldTypes;
    }

    /**
     * Getting Distinct Values by fields in the metioned collectio name
     *
     * @param collectionName name of collection
     * @param fieldName      name of field
     * @return {@link Set} values as String
     */
    public Set<String> getDistinctValuesByFieldName(String collectionName, String fieldName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        String fieldType = dbAggregationUtilComponent.findFieldType(collectionName, fieldName);
        List<AggregationOperation> aggregations = new ArrayList<>();
        if (Date.class.getSimpleName().equals(fieldType)) {
            ProjectionOperation projectionOperation = Aggregation.project()
                    .and(DateOperators.dateOf(fieldName).toString(Constants.CHART_FIELD_DATE_FORMAT_YMD))
                    .as(fieldName);
            aggregations.add(projectionOperation);
        }
        aggregations.add(Aggregation.group(fieldName).count().as(returnFields.get(1)));
        aggregations.add(Aggregation.project().and("_id").as(fieldName));
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, collectionName, Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        return mappedResults.stream()
                .filter(document -> Objects.nonNull(document.get(fieldName)))
                .map(document -> document.get(fieldName).toString()).collect(Collectors.toSet());
    }

    /**
     * Getting distinct values based on collection and field
     *
     * @param collectionName name of collection
     * @param fieldName      name of field name
     * @return set of field values
     */
    public Set<String> getDistinctValuesByStringField(String collectionName, String fieldName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        Set<String> values = new HashSet<>();
        DistinctIterable<String> sets = template.getCollection(collectionName).distinct(fieldName, String.class);
        MongoCursor cursor = sets.iterator();
        while (cursor.hasNext()) {
            String tempValue = (String) cursor.next();
            values.add(tempValue);
        }
        return values;
    }

    /**
     * getting chart details based on chart aggregation
     *
     * @param chartAggregation chart aggregation details
     * @param collectionName   name of collection
     * @return {@Code List} of chart data
     */
    public List<ChartData> getChart(DrillDownChartAggregation chartAggregation, String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
//        List<Filter> filters = chartAggregation.getFilterList();
        List<String> fields = chartAggregation.getGroupBy();
        int level = chartAggregation.getLevel();
        level = Math.min(level, fields.size());
        if (fields.size() > level) fields = fields.subList(0, level);

        Map<String, String> projection = new LinkedHashMap<>();
        projection.put(underscoreId, returnFields.get(0));
        projection.put(returnFields.get(1), returnFields.get(1));
        if (level >= 3) projection.put(returnFields.get(2), returnFields.get(2));
        String[] array = fields.toArray(new String[fields.size()]);

        Aggregation aggregation = aggregationUtil.getAggregation(chartAggregation.getFilters(), array, returnFields, level, getFieldsAndTypesByCollection(collectionName));
        AggregationResults<ChartData> chartDataAggregationResults = template.aggregate(aggregation, collectionName, ChartData.class);
        return chartDataAggregationResults.getMappedResults();
    }

    public List<ChartDataForFusion> getChartForFusion(DrillDownChartAggregation chartAggregation, String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
//        List<Filter> filters = chartAggregation.getFilterList();
        List<String> fields = chartAggregation.getGroupBy();
        int level = chartAggregation.getLevel();
        level = Math.min(level, fields.size());
        if (fields.size() > level) fields = fields.subList(0, level);

        Map<String, String> projection = new LinkedHashMap<>();
        projection.put(underscoreId, returnFields.get(0));
        projection.put(returnFields.get(1), returnFields.get(1));
        if (level >= 3) projection.put(returnFields.get(2), returnFields.get(2));
        String[] array = fields.toArray(new String[fields.size()]);

        Aggregation aggregation = aggregationUtil.getAggregation(chartAggregation.getFilters(), array, returnFields, level, getFieldsAndTypesByCollection(collectionName));
        AggregationResults<ChartDataForFusion> chartDataAggregationResults = template.aggregate(aggregation, collectionName, ChartDataForFusion.class);
        return chartDataAggregationResults.getMappedResults();
    }

    /**
     * Getting BarChart based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link ChartData}
     */
    public List<ChartData> getBarChart(BarChartAggregation chartAggregation, String collectionName, Map<String, String> collection) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        Aggregation barChartAggregation = aggregationUtil.getBarChartAggregation(chartAggregation, collection);
        AggregationResults<ChartData> aggregationResults = template.aggregate(barChartAggregation, collectionName, ChartData.class);
        return aggregationResults.getMappedResults();
    }

    public List<LinkedHashMap> getBarGaugeChart(BarChartAggregation validateBarGaugeChartAggregation, String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        Aggregation barChartAggregation = aggregationUtil.getBarGaugeChartAggregation(validateBarGaugeChartAggregation, getFieldsAndTypesByCollection(collectionName));
        AggregationResults<LinkedHashMap> aggregationResults = template.aggregate(barChartAggregation, collectionName, LinkedHashMap.class);
        return aggregationResults.getMappedResults();
    }

    /**
     * Getting Table based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link DataGridRow}
     */
    public List<DataGridRow> getTable(TableAggregation chartAggregation, String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));

        List<String> fields = chartAggregation.getProjection();
        LinkedHashMap<String, Object> fieldsWithAlias = chartAggregation.getProjectionWithAlias();
        List<String> excludeFields = chartAggregation.getExcludeFields();
        DBSort sort = chartAggregation.getSort();
        int limit = chartAggregation.getLimit();

        Aggregation dataGridAggregation = aggregationUtil.getDataGridAggregation(chartAggregation.getFilters(), fields, fieldsWithAlias, excludeFields, sort, limit, getFieldsAndTypesByCollection(collectionName));
        AggregationResults<DataGridRow> aggregationResults = template.aggregate(dataGridAggregation, collectionName, DataGridRow.class);
        return aggregationResults.getMappedResults();
    }

    public List<FieldType> getFieldsAndTypes(String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
        final List<FieldType> fieldTypes = new ArrayList<>();
        List<String> fields = dbAggregationUtilComponent.findListOfFields(collectionName);
        fields.forEach(field -> {
            String type = dbAggregationUtilComponent.findFieldType(collectionName, field);
            if (StringUtils.isEmpty(type)) {
                fieldTypes.add(new FieldType(field, "object", field));
            } else {
                fieldTypes.add(new FieldType(field, type, field));
            }
        });

        return fieldTypes;
    }

    public List<ChartData> getFieldAggregate(List<FilterConfig> filters, AggregateConfig validateAggregate, Set<String> fields, String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
//        List<Filter> filterList = filters.stream().flatMap(filterConfig -> filterConfig.getConfigs().stream()).collect(Collectors.toList());
        List<String> aggregateFields = validateAggregate.getFields();
        List<AggregationOperation> matchOperation = aggregationUtil.getMatchOperation(filters, getFieldsAndTypesByCollection(collectionName));
        ChartData chartData = new ChartData("result", 0D,"result",null, null, null);
        if (validateAggregate.getType().equals(Type.AggregateType.AVG) && aggregateFields.size() >= 2) {
            Double actual = dbAggregationUtilComponent.findFieldAggregate(matchOperation, collectionName, aggregateFields.get(0), SUM);
            Double expected = dbAggregationUtilComponent.findFieldAggregate(matchOperation, collectionName, aggregateFields.get(1), SUM);
            if (0D == actual || 0D == expected) throw new InvalidDetailsException("Sum of field values are zero");
            double avg = (actual / expected) * 100;
            chartData.setValue(Precision.round(avg, 2));
        } else {
            Double fieldAggregate = dbAggregationUtilComponent.findFieldAggregate(matchOperation, collectionName, aggregateFields.get(0), validateAggregate.getType());
            chartData.setValue(fieldAggregate);
        }
        return Arrays.asList(chartData);
    }

    public Collection<Map<String, Object>> addCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        return template.insert(collectorData, collectionName);
    }

    public Collection<Map<String, Object>> updateCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        return template.insert(collectorData, collectionName);
    }
}
