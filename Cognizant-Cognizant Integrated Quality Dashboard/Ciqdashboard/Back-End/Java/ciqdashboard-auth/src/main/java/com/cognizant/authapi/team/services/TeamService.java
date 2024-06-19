/*
 *  © [2021] Cognizant. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.cognizant.authapi.team.services;

import com.cognizant.authapi.base.error.ResourceExistsException;
import com.cognizant.authapi.base.error.ResourceNotFoundException;
import com.cognizant.authapi.team.beans.Team;
import com.cognizant.authapi.team.repos.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *TeamService
 *
 * @author Cognizant
 */

@Service
public class TeamService {
    @Autowired
    TeamRepository repository;

    public List<Team> getAll() {
        return repository.findAll();
    }

    public Optional<Team> get(String id) {
        return repository.findById(id);
    }

    public Optional<Team> getByName(String name) {
        return repository.findByName(name);
    }

    public Team assertAndGet(String id) {
        Optional<Team> optional = get(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Team", "id", id);
        }
        return optional.get();
    }

    public Team assertAndGetByName(String name) {
        Optional<Team> optional = getByName(name);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Team", "name", name);
        }
        return optional.get();
    }

    public List<Team> findUserBelongsToWhichTeams(String userEmailId){
        return repository.findByMembers(userEmailId);
    }

    public Team add(Team team) {
        try {
            return repository.insert(team);
        } catch (DuplicateKeyException exception) {
            throw new ResourceExistsException("Team", "name", team.getName());
        }
    }

    public Team update(Team team) {
        assertAndGet(team.getId());
        try {
            return repository.save(team);
        } catch (DuplicateKeyException exception) {
            throw new ResourceExistsException("Team", "name", team.getName());
        }
    }

    public void deleteById(String id){
        assertAndGet(id);
        repository.deleteById(id);
    }

    public List<String> getTeamIdsByUserEmailId(String userEmailId){
        return findUserBelongsToWhichTeams(userEmailId).stream().map(Team::getId).collect(Collectors.toList());
    }
}
