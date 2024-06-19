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
import com.cognizant.ciqdashboardapi.models.CIQDashboardProject;
import com.cognizant.ciqdashboardapi.models.LOB;
import com.cognizant.ciqdashboardapi.models.Organization;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CIQDashboardProjectService
 *
 * @author Cognizant
 */

@Service
public class CIQDashboardProjectService {

    @Autowired
    LOBService lobService;
    @Autowired
    OrganizationService orgService;
    @Autowired
    private CIQDashboardProjectRepository repository;

    public List<CIQDashboardProject> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<CIQDashboardProject> get(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
    }

    public List<String> getNamesByIds(List<String> ids) {
        return repository.findByIds(ids, Sort.by(Sort.Direction.DESC, "lastModifiedDate"))
                .stream().map(CIQDashboardProject::getName).collect(Collectors.toList());
    }

    public Optional<CIQDashboardProject> get(String id) {
        return repository.findById(id);
    }

    public CIQDashboardProject update(CIQDashboardProject project, String category) {
        assertUpdate(project, category);
        return repository.save(project);
    }

    public CIQDashboardProject insert(CIQDashboardProject project) {
        return repository.insert(project);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private void assertUpdate(CIQDashboardProject project, String category) {
        getOrAssert(project.getId(), category);
        Optional<CIQDashboardProject> optional = getByNameIgnoreCase(project.getName());
        if (optional.isPresent() && !optional.get().getId().equals(project.getId())) {
            throw new ResourceExistsException("Project", "name", project.getName());
        }
    }

    public CIQDashboardProject getOrAssert(String id, String category) {
        if (category.equalsIgnoreCase("PRJ")) {
            Optional<CIQDashboardProject> optional = get(id);
            return optional.orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        } else if (category.equalsIgnoreCase("LOB")) {
            CIQDashboardProject dashboardProject = new CIQDashboardProject();
            LOB lobObject = lobService.getLobById(id);
            dashboardProject.setDescription(lobObject.getLobDescription());
            dashboardProject.setName(lobObject.getLobName());
            dashboardProject.setId(lobObject.getId());
            dashboardProject.setOrgId(lobObject.getOrgId());
            return dashboardProject;
        }else if (category.equalsIgnoreCase("ORG")) {
            CIQDashboardProject dashboardProject = new CIQDashboardProject();
            Organization organization = orgService.getOrganizationById(id);
            dashboardProject.setDescription(organization.getOrganizationDesc());
            dashboardProject.setName(organization.getOrganizationName());
            dashboardProject.setId(organization.getId());
            return dashboardProject;
        }
        return null;
    }

    public void assertNotExists(CIQDashboardProject project) {
        Optional<CIQDashboardProject> byName = getByNameIgnoreCase(project.getName());
        if (byName.isPresent()) throw new ResourceExistsException("Project", "name", project.getName());
        if (!StringUtils.isEmpty(project.getId()))
            throw new InvalidDetailsException("id", project.getId(), "Id should be empty/null");
    }

    public Optional<CIQDashboardProject> getByNameNotId(String name, String id) {
        return repository.findFirstByNameAndIdNot(name, id);
    }

    public Optional<CIQDashboardProject> getByName(String name) {
        return repository.findByName(name);
    }

    public Optional<CIQDashboardProject> getByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    public CIQDashboardProject assertAndGetByName(String name) {
        Optional<CIQDashboardProject> byName = getByName(name);
        if (byName.isPresent()) {
            return byName.get();
        } else {
            throw new ResourceNotFoundException("Project", "name", name);
        }
    }

    public List<CIQDashboardProject> getProjectsByLobId(String lobId) {
        return repository.findByLobId(lobId);
    }

    public LOB getLob(String id) {
        return lobService.getLobById(id);
    }

    public Organization getOrganization(String id){
        return orgService.getOrganizationById(id);
    }

    public void deleteLob(String id) {
        lobService.deleteById(id);
    }
    public void deleteOrganization(String id) {
        orgService.deleteById(id);
    }

    public List<CIQDashboardProject> getProjectsByOrgId(String orgId) {
        return repository.findByOrgId(orgId);
    }
}
