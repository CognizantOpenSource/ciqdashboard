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

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AddFieldsOperation
 * @author Cognizant
 */

public class AddFieldsOperation implements AggregationOperation {

    private final List<Document> documents;

    public AddFieldsOperation(Map<String, String> fieldAndAliasPair) {
        documents = new ArrayList<>();
        fieldAndAliasPair.entrySet().forEach(entry -> {
            documents.add(new Document(entry.getValue(), DateOperators.DateToString.dateOf(entry.getKey())
                    .toString("%Y-%m-%d").toDocument(Aggregation.DEFAULT_CONTEXT)));
        });
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {
        return null;
    }

    @Override
    public List<Document> toPipelineStages(AggregationOperationContext context) {
        return documents.stream().map(document -> new Document("$addFields", document)).collect(Collectors.toList());
    }
}
