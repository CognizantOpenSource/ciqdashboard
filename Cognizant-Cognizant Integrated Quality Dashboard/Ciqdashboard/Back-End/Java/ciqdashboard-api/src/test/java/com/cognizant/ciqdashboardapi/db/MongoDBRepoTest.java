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

import com.cognizant.ciqdashboardapi.repos.ProjectMappingRepository;
import com.cognizant.ciqdashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * MongoDBRepoTest
 * @author Cognizant
 */

@SpringBootTest
class MongoDBRepoTest {

    @Autowired
    IDChartItemRepositoryImpl repository;
    @Autowired
    MongoTemplate template;
    @Autowired
    ProjectMappingRepository projectMappingRepository;

    //@Test
    //void findByUserIdOrTeamIds(){
        //List<ProjectMapping> test = projectMappingRepository.findByUserIdsContainingOrTeamIdsIn("test", Arrays.asList("5f9010d07c8f8360262e1f42"));
      //Assertions.assertNotNull(projectMappingRepository);
    //}

    /*
    @Test
    void findTest() {
        Criteria criteria = Criteria.where("name").is("anji");
        MatchOperation match = Aggregation.match(criteria);
//        List<Document> studentDetails = template.find(new Query(criteria), Document.class, "studentDetails");
        Aggregation aggregation = Aggregation.newAggregation(match);
       //AggregationResults<Document> aggregate = template.aggregate(aggregation, "studentDetails", Document.class);
        Assertions.assertNotNull(template);
    }*/
}
