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

import com.cognizant.ciqdashboardapi.models.db.view.*;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
/**
 * CreateViewComponent
 * @author Cognizant
 */

@Component
public class CreateViewComponent {

    public List<AggregationOperation> createView(ViewDetails viewDetails) {
        List<AggregationOperation> aggregations = new ArrayList<>();
        @NotNull(message = "BaseCollection should not be null/empty") BaseCollectionDetailsForView base = viewDetails.getBaseCollection();
        List<CollectionDetailsForView> lookups = viewDetails.getLookups();
        lookups.forEach(lookup -> {
            List<BasicDBObject> dbObjects = getOperations(lookup.getLocalForeignFields());
            BasicDBObject and = new BasicDBObject().append("$and", dbObjects);
            BasicDBObject expr = new BasicDBObject("$expr", and);
            BasicDBObject lookupPipeline = new BasicDBObject().append("from", lookup.getName())
                    .append("let", getLetFields(lookup.getLocalForeignFields().stream().map(LocalForeignField::getLocalField).collect(Collectors.toList())))
                    .append("pipeline", Arrays.asList(new BasicDBObject("$match", expr)))
                    .append("as", lookup.getAlias());
            BasicDBObject lookupBasicDBObject = new BasicDBObject("$lookup", lookupPipeline);
            CustomAggregationOperation lookupOperation = new CustomAggregationOperation(lookupBasicDBObject.toJson());
            aggregations.add(lookupOperation);
            aggregations.add(unwind(lookup.getAlias(), lookup.getAlias() + "ArrayIndex"));
        });
        ProjectionOperation project = Aggregation.project();
        for (FieldWithAlias field : base.getFields()) {
            project = project.andExpression(field.getName()).as(field.getAlias());
        }
        for (CollectionDetailsForView lookup : lookups) {
            for (FieldWithAlias field : lookup.getFields()) {
                project = project.andExpression(String.format("%s.%s", lookup.getAlias(), field.getName())).as(field.getAlias());
            }
        }

        aggregations.add(project);
        return aggregations;
    }

    private BasicDBObject getLetFields(List<String> localFields) {
        BasicDBObject basicDBObject = new BasicDBObject();
        localFields.forEach(localField -> basicDBObject.append(localField, "$" + localField) );
        return basicDBObject;
    }

    private List<BasicDBObject> getOperations(List<LocalForeignField> localForeignFields) {
        List<BasicDBObject> dbObjects = new ArrayList<>();
        localForeignFields.forEach(field -> {
            BasicDBObject basicDBObject = getMatchField(field);
            dbObjects.add(basicDBObject);
        });
        return dbObjects;
    }

    private BasicDBObject getMatchField(LocalForeignField field) {
        switch (field.getOpType()){
            case in:
                return new BasicDBObject("$in", Arrays.asList("$" + field.getForeignField(), "$$" + field.getLocalField()));
            case eq:
            default:
                return new BasicDBObject("$eq", Arrays.asList("$" + field.getForeignField(), "$$" + field.getLocalField()));
        }
    }


}
