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

package com.cognizant.authapi.team.controller;

import com.cognizant.authapi.base.error.InvalidDetailsException;
import com.cognizant.authapi.team.beans.Team;
import com.cognizant.authapi.team.services.TeamService;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.beans.UserPrincipal;
import com.cognizant.authapi.users.services.UserService;
import com.cognizant.authapi.users.services.UserValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *TeamController
 *
 * @author Cognizant
 */

@RestController
@RequestMapping("/teams")
@Slf4j
public class TeamController {
    @Autowired
    TeamService service;
    @Autowired
    UserService userService;
    @Autowired
    UserValidationService userValidationService;

    @GetMapping
    @PreAuthorize("hasPermission('Team','team.view')")
    public List<Team> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.findUserBelongsToWhichTeams(userValidationService.getCurrentUserEmailId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('Team','team.view')")
    public Team get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasPermission('Team','team.view')")
    public Team getByName(@RequestParam String name) {
        return service.assertAndGetByName(name);
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasPermission('Team','team.view')")
    public List<Team> getTeamsByCurrentUser() {
        String currentUserEmailId = getCurrentUserEmailId();
        return service.findUserBelongsToWhichTeams(currentUserEmailId);
    }

    @GetMapping("/names/current-user")
    @PreAuthorize("hasPermission('Team','team.view')")
    public List<String> getTeamNamesByCurrentUser() {
        return getTeamsByCurrentUser().stream().map(Team::getName).collect(Collectors.toList());
    }

    @PostMapping
    @Validated
    @PreAuthorize("hasPermission('Team','team.create')")
    public Team add(@Valid @RequestBody Team team) {
        validateUserEmailIds(team.getMembers());
        return service.add(team);
    }

    @PutMapping
    @Validated
    @PreAuthorize("hasPermission('Team','team.update')")
    public Team save(@Valid @RequestBody Team team) {
        validateUserEmailIds(team.getMembers());
        return service.update(team);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('Team','team.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    private void validateUserEmailIds(Set<String> userEmailIds) {
        List<User> allUserByEmails = userService.getAllUserByEmails(new ArrayList<>(userEmailIds));
        if (CollectionUtils.isEmpty(allUserByEmails) || userEmailIds.size() != allUserByEmails.size()) {
            throw new InvalidDetailsException("Some of User Email Ids are not found");
        }
    }

    private String getCurrentUserEmailId() {
        return getUserPrincipal().getEmail();
    }

    private UserPrincipal getUserPrincipal() {
        Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth instanceof UserPrincipal) {
            return ((UserPrincipal) auth);
        } else {
            throw new AuthenticationCredentialsNotFoundException("user not signed in");
        }
    }


}
