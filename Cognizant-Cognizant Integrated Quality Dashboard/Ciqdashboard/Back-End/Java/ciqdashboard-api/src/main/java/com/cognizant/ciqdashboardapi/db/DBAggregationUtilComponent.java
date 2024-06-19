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

package com.cognizant.ciqdashboardapi.db;

import com.cognizant.ciqdashboardapi.models.Type;
import com.cognizant.ciqdashboardapi.models.db.view.ViewDetails;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * DBAggregationUtilComponent
 * @author Cognizant
 */

@Component
public class DBAggregationUtilComponent {
    private static final String DATA = "data";
    private static final String KEYS = "keys";
    private static final String TYPE = "type";

    @Autowired
    MongoTemplate template;
    @Autowired
    CreateViewComponent createViewComponent;

    public boolean createView(ViewDetails viewDetails) {

        List<AggregationOperation> aggregations = createViewComponent.createView(viewDetails);

        return createView(viewDetails.getName(), viewDetails.getBaseCollection().getName(), aggregations);
    }

    public void deleteView(String collectionName) {
        template.dropCollection(collectionName);
    }

    public boolean createView(String viewName, String viewOn, List<AggregationOperation> aggregations) {
        List<Document> pipeline = newAggregation(aggregations).toPipeline(DEFAULT_CONTEXT);
        template.getDb().createView(viewName, viewOn, pipeline);
        return true;
    }

    public List<String> findListOfFields(String collectionName) {
        List<String> keys = new ArrayList<>();
        List<AggregationOperation> aggregations = new ArrayList<>();
        aggregations.clear();
        aggregations.add(project().and(ObjectOperators.valueOf("$$ROOT").toArray()).as(DATA));
        aggregations.add(project().and(String.format("$%s.k", DATA)).as(DATA));
        aggregations.add(Aggregation.unwind(String.format("$%s", DATA)));
        aggregations.add(Aggregation.group().addToSet(DATA).as(KEYS));
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, collectionName, Document.class);

        Document uniqueMappedResult = aggregate.getUniqueMappedResult();
        if (null != uniqueMappedResult) {
            keys = (List<String>) uniqueMappedResult.get(KEYS);
        }

        return keys;
    }

    public String findFieldType(String collectionName, String fieldName) {
        String type = "";
        List<AggregationOperation> aggregations = new ArrayList<>();
        aggregations.clear();
        aggregations.add(project().andExpression(String.format("%s(%s)", TYPE, fieldName)).as(TYPE));
        aggregations.add(Aggregation.match(Criteria.where(TYPE).ne("missing")));
        aggregations.add(Aggregation.group().addToSet(TYPE).as(TYPE));
        aggregations.add(project().and(TYPE).arrayElementAt(0));
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, collectionName, Document.class);

        Document uniqueMappedResult = aggregate.getUniqueMappedResult();
        if (null != uniqueMappedResult) {
            type = (String) uniqueMappedResult.get(TYPE);
        }

        return type;
    }

    public Double findFieldAggregate(List<AggregationOperation> matches, String collectionName, String fieldName, Type.AggregateType aggregateType) {
        if (aggregateType == Type.AggregateType.DISTINCT_COUNT){
            return getDistinctCount(matches, collectionName, fieldName);
        }
        final String resultKey = "result";
        List<AggregationOperation> aggregations = new ArrayList<>();
        aggregations.clear();
        aggregations.addAll(matches);
        aggregations.add(getGroupOperation(fieldName, aggregateType, resultKey));
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, collectionName, Document.class);
        Document uniqueMappedResult = aggregate.getUniqueMappedResult();
        if (Objects.nonNull(uniqueMappedResult)) {
            Object result = uniqueMappedResult.get(resultKey);
            try {
                return Double.parseDouble(result.toString());
            }catch (Exception e){
                //***************************************************************************************
                // Avoid [e.printStackTrace()] a hacker able to identify more details about your program
                // it gives hackar very clearly sending the actual Exception message
                //*****************************************************************************************
                //e.printStackTrace();
                throw new RuntimeException(e);

            }
        }
        return 0D;
    }

    public GroupOperation getGroupOperation(String fieldName, Type.AggregateType aggregateType, String resultKey) {
        switch (aggregateType) {
            case AVG:
                return Aggregation.group().avg(fieldName).as(resultKey);
            case MAX:
                return Aggregation.group().max(fieldName).as(resultKey);
            case MIN:
                return Aggregation.group().min(fieldName).as(resultKey);
            case SUM:
                return Aggregation.group().sum(fieldName).as(resultKey);
            case COUNT:
            default:
                return Aggregation.group().count().as(resultKey);
        }
    }

    public Double getDistinctCount(List<AggregationOperation> matches, String collectionName, String fieldName){
        List<AggregationOperation> aggregations = new ArrayList<>();
        aggregations.clear();
        aggregations.addAll(matches);
        aggregations.add(Aggregation.group(fieldName).count().as("value"));
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, collectionName, Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        if (CollectionUtils.isEmpty(mappedResults)) return 0D;
        else return Double.valueOf(mappedResults.size());
    }
}
