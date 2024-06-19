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

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.CIQDashboardDataSource;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardDataSourceRepository;
import com.cognizant.ciqdashboardapi.repos.impl.CollectorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * CIQDashboardDataSourceService
 * @author Cognizant
 */

@Service
public class CIQDashboardDataSourceService {
    @Autowired
    CIQDashboardDataSourceRepository repository;
    @Autowired
    MongoTemplate template;
    @Autowired
    CollectorRepositoryImpl collectorRepository;

    public List<CIQDashboardDataSource> getAll() {
        return repository.findAll();
    }

    public Optional<CIQDashboardDataSource> get(String id) {
        return repository.findById(id);
    }

    public CIQDashboardDataSource assertAndGet(String id) {
        Optional<CIQDashboardDataSource> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "id", id);
        }
    }

    public CIQDashboardDataSource add(CIQDashboardDataSource ciqDashboardDataSource) {
        assertInsert(ciqDashboardDataSource);
        return repository.insert(ciqDashboardDataSource);
    }

    public CIQDashboardDataSource update(CIQDashboardDataSource ciqDashboardDataSource) {
        assertUpdate(ciqDashboardDataSource);
        return repository.save(ciqDashboardDataSource);
    }

    public void deleteById(String id) {
        assertAndGet(id);
        repository.deleteById(id);
    }

    public Optional<CIQDashboardDataSource> getByName(String name) {
        return repository.findByName(name);
    }

    public Optional<CIQDashboardDataSource> getByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public CIQDashboardDataSource assertAndGetByName(String name) {
        Optional<CIQDashboardDataSource> optional = getByName(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("DataSource", "name", name);
        }
    }

    public void deleteByIdIn(List<String> ids) {
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(CIQDashboardDataSource ciqDashboardDataSource) {
        if (!StringUtils.isEmpty(ciqDashboardDataSource.getId())) {
            throw new InvalidDetailsException("Id field should be empty/null");
        }
        assertCollectionName(ciqDashboardDataSource.getCollectionName());
        Optional<CIQDashboardDataSource> byName = getByNameIgnoreCase(ciqDashboardDataSource.getName());
        if (byName.isPresent()) {
            throw new ResourceExistsException("DataSource", "name", ciqDashboardDataSource.getName());
        }
    }

    private void assertUpdate(CIQDashboardDataSource ciqDashboardDataSource) {
        assertAndGet(ciqDashboardDataSource.getId());
        assertCollectionName(ciqDashboardDataSource.getCollectionName());
        Optional<CIQDashboardDataSource> optional = getByNameIgnoreCase(ciqDashboardDataSource.getName());
        if (optional.isPresent() && !optional.get().getId().equals(ciqDashboardDataSource.getId())) {
            throw new ResourceExistsException("DataSource", "name", ciqDashboardDataSource.getName());
        }
    }

    private void assertCollectionName(String name) {
        Set<String> collectionNames = collectorRepository.getCollectionNames();
        if (!collectionNames.contains(name)) {
            throw new InvalidDetailsException("Collection Name not Found in Database with name : " + name);
        }
    }
}
