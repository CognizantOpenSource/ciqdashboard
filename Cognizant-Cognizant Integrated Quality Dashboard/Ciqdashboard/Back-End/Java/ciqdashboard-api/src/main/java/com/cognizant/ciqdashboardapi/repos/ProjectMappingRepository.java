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

import com.cognizant.ciqdashboardapi.models.ProjectMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * ProjectMappingRepository
 * @author Cognizant
 */

public interface ProjectMappingRepository extends MongoRepository<ProjectMapping, String> {
    List<ProjectMapping> findByUserIdsContaining(String userId);

    List<ProjectMapping> findByUserIdsContainingOrTeamIdsIn(String userId, List<String> teamIds);

    List<ProjectMapping> findByOwnerId(String userId);

    List<ProjectMapping> findByTeamIdsContaining(String teamName);
}
