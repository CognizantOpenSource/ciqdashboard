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

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.models.DirectChartData;
import com.cognizant.ciqdashboardapi.models.SourceMetrics;
import com.cognizant.ciqdashboardapi.services.UserValidationService;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CIQDashboardRepositoryImpl
 *
 * @author Cognizant
 */

@Repository
public class CIQDashboardRepositoryImpl {
    @Autowired
    MongoTemplate template;
    @Autowired
    UserValidationService userValidationService;

    public Optional<CIQDashboard> findById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        CIQDashboard dashboard = template.findOne(query, CIQDashboard.class);
        return Optional.ofNullable(dashboard);
    }

//    public List<CIQDashboard> getByProjectName(String projectName) {
//        Criteria criteria = Criteria.where("projectName").is(projectName);
//        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
//        Query query = new Query().addCriteria(criteria);
//        return template.find(query, CIQDashboard.class);
//    }

    public List<CIQDashboard> getByProjectName(String projectName, String category) {
        Criteria criteria = Criteria.where("projectName").is(projectName).andOperator(Criteria.where("category").is(category));
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, CIQDashboard.class);
    }

    public List<CIQDashboard> getByProjectId(String projectId, String category) {
        Criteria criteria = Criteria.where("projectId").is(projectId).andOperator(Criteria.where("category").is(category));
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, CIQDashboard.class);
    }

    public List<CIQDashboard> findAll() {
        Criteria criteria = new Criteria();
        criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, CIQDashboard.class);
    }

    public void deleteByIds(List<String> ids) {
        Criteria criteria = Criteria.where("_id").in(ids);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        DeleteResult deleteResult = template.remove(query, CIQDashboard.class);

        if(deleteResult.wasAcknowledged()) {
            Criteria criteria1 = Criteria.where("dashboardId").in(ids);
            Query query1 = new Query().addCriteria(criteria1);
            template.remove(query1, SourceMetrics.class);
            template.remove(query1, DirectChartData.class);
        }
    }

    private Criteria[] getCriteria() {
        Criteria[] criteriaArray = new Criteria[2];
        criteriaArray[0] = Criteria.where("openAccess").in(false, null).and("createdUser").is(userValidationService.getCurrentUserEmailId());
        criteriaArray[1] = Criteria.where("openAccess").is(true).and("projectName").in(userValidationService.getCurrentUserProjectNames());
        return criteriaArray;
    }
}
