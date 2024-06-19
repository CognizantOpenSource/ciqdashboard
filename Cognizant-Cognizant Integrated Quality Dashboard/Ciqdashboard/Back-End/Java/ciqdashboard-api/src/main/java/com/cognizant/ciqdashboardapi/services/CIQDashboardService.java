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
import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.models.Filter;
import com.cognizant.ciqdashboardapi.models.FilterConfig;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardRepository;
import com.cognizant.ciqdashboardapi.repos.impl.CIQDashboardRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * CIQDashboardService
 *
 * @author Cognizant
 */

@Service
public class CIQDashboardService {

    @Autowired
    CIQDashboardRepository repository;
    @Autowired
    CIQDashboardRepositoryImpl implRepo;

    public List<CIQDashboard> getAllByPermissions() {
        return implRepo.findAll();
    }

    public List<CIQDashboard> getAll() {
        return repository.findAll();
    }

    public List<CIQDashboard> getAllByCategory(String category) {
        return repository.findByCategory(category);
    }

    public Optional<CIQDashboard> get(String id) {
        return implRepo.findById(id);
    }

    public Optional<CIQDashboard> getByNameIgnoreCaseAndProjectName(String name,String projectName) {
        return repository.findByNameAndProjectName(name,projectName);
    }

    public CIQDashboard assertAndGet(String id) {
        Optional<CIQDashboard> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("Dashboard", "id", id);
        }
    }

    public CIQDashboard assertAndGetByIdAndCategory(String id, String category) {
        Optional<CIQDashboard> optional = repository.findByIdAndCategory(id, category);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("Dashboard", "id", id);
        }
    }

//    public List<CIQDashboard> getByProjectName(String projectName) {
//        return implRepo.getByProjectName(projectName);
//    }

    public List<CIQDashboard> getByProjectName(String projectName, String category) {
        return implRepo.getByProjectName(projectName, category);
    }

    public List<CIQDashboard> getByProjectId(String projectId, String category) {
        return implRepo.getByProjectId(projectId, category);
    }

    public List<CIQDashboard> getByProjectNameList(List<String> projectNames) {
        return repository.findByProjectNameIn(projectNames);
    }

    public CIQDashboard add(CIQDashboard dashboard) {
        assertInsert(dashboard);
        var filters = List.of(new Filter("projectName", Filter.OPType.equals,dashboard.getProjectName(),""));
        var filterConfig = new FilterConfig("Project Filter", FilterConfig.LogicalOperatorType.AND,filters,true);
        dashboard.getPages().forEach(page->{
            page.getItems().forEach(item->{
                if(item.getFilters() == null) {
                  //  item.getFilters().add(filterConfig);
                }else{
                    item.getFilters().add(filterConfig);
                }
            });

        });
        dashboard.setOpenAccess(false);
        return repository.save(dashboard);
    }

    public CIQDashboard update(CIQDashboard dashboard) {
        assertUpdate(dashboard);
        CIQDashboard exist = assertAndGetByIdAndCategory(dashboard.getId(),dashboard.getCategory());
        dashboard.setCreatedUser(exist.getCreatedUser());
        dashboard.setCreatedDate(exist.getCreatedDate());
        return repository.save(dashboard);
    }

    public void deleteById(String id) {
        assertAndGet(id);
        implRepo.deleteByIds(Arrays.asList(id));
    }

    public void deleteByIdIn(List<String> ids) {
        implRepo.deleteByIds(ids);
    }

    private void assertUpdate(CIQDashboard dashboard) {
        Optional<CIQDashboard> optional = getByNameIgnoreCaseAndProjectName(dashboard.getName(),dashboard.getProjectName());
        if (optional.isPresent() && !optional.get().getId().equals(dashboard.getId())) {
            throw new ResourceExistsException("Dashboard", "name", dashboard.getName());
        }
    }

    private void assertInsert(CIQDashboard dashboard) {
        if (!StringUtils.isEmpty(dashboard.getId())) {
            throw new InvalidDetailsException("Id should be null/empty");
        }
        Optional<CIQDashboard> optional = getByNameIgnoreCaseAndProjectName(dashboard.getName(),dashboard.getProjectName());
        if (optional.isPresent()) {
            throw new ResourceExistsException("Dashboard", "name", dashboard.getName());
        }
    }

    public void deleteByProjectName(String projectName) {
        repository.deleteByProjectName(projectName);
    }


}
