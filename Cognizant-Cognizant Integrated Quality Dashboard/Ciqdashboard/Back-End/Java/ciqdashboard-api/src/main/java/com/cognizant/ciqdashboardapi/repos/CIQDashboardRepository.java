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

package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * CIQDashboardRepository
 * @author Cognizant
 */

public interface CIQDashboardRepository extends MongoRepository<CIQDashboard, String> {
    void deleteByIdIn(List<String> ids);

    List<CIQDashboard> findByProjectName(String projectName);

    List<CIQDashboard> findByProjectNameIn(List<String> projectNames);

    void deleteByProjectName(String projectName);

   // Optional<CIQDashboard> findByNameIgnoreCaseAndProjectId(String name,String projectId);

    Optional<CIQDashboard> findByNameAndProjectName(String name,String projectName);

    Optional<CIQDashboard> findByIdAndProjectId(String Id,String projectId);

    List<CIQDashboard> findByCategory(String category);

    Optional<CIQDashboard> findByIdAndCategory(String id, String category);
}
