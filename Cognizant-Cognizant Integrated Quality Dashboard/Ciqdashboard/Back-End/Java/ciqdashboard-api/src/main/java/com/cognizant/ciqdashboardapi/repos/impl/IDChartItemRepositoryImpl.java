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

import com.cognizant.ciqdashboardapi.models.IDChartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * IDChartItemRepositoryImpl
 * @author Cognizant
 */

@Repository
public class IDChartItemRepositoryImpl {
    @Autowired
    MongoTemplate template;

    public List<IDChartItem> findByNameLikeIgnoreCase(List<String> names) {
        Query query = new Query();
        List<Criteria> list = new ArrayList<>();
        names.forEach(name -> {
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("description").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("source").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("options.title").regex(String.format(".*%s.*", name), "i")
            );
            list.add(criteria);
        });
        Criteria criteria = new Criteria().orOperator(list.toArray(new Criteria[list.size()]));
        query.addCriteria(criteria);
        query.limit(20);
        return template.find(query, IDChartItem.class);
    }
}
