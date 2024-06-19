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

package com.cognizant.authapi.external.cotroller;

import com.cognizant.authapi.base.error.InvalidDetailsException;
import com.cognizant.authapi.collector.model.BaseConstants;
import com.cognizant.authapi.external.service.ExternalTokenService;
import com.cognizant.authapi.models.AppTokenStore;
import com.cognizant.authapi.services.AppTokenStoreService;
import com.cognizant.authapi.users.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *ExternalTokenController
 *
 * @author Cognizant
 */

@RestController
@RequestMapping("/external-token")
public class ExternalTokenController {
    private static final int daysInYear = 365;

    @Autowired
    ExternalTokenService service;
    @Autowired
    AppTokenStoreService appTokenStoreService;

    @PostMapping("/current-user/token")
    public AppTokenStore generateCurrentUserToken(@RequestParam(required = false, defaultValue = "99") int years) {
        User currentUser = service.getCurrentUser();
        Optional<AppTokenStore> optional = appTokenStoreService.getByRefId(currentUser.getId());
        if (optional.isPresent())
            return optional.get();

        Map<String, Object> currentUserToken = service.generateCurrentUserToken(currentUser, years*daysInYear);
        String token = (String) currentUserToken.get(BaseConstants.EXTERNAL_TOKEN);
        Date validity = (Date) currentUserToken.get(BaseConstants.TOKEN_EXPIRES_AT);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String validityString = sdf.format(validity);

        AppTokenStore appTokenStore = AppTokenStore.builder()
                .id(UUID.randomUUID().toString())
                .name(currentUser.getName())
                .refId(currentUser.getId())
                .type(AppTokenStore.AppTokenType.USER)
                .token(token)
                .tokenExpiresAt(validityString)
                .build();
        return appTokenStoreService.add(appTokenStore);
    }

    @GetMapping(value = "/current-user/token")
    public AppTokenStore getTokenByProjectId() {
        User currentUser = service.getCurrentUser();
        return appTokenStoreService.assertOrGetByRefId(currentUser.getId());
    }

    @DeleteMapping(value = "/current-user/token")
    public void deleteTokenByProjectId(@PathVariable String id) {
        User currentUser = service.getCurrentUser();
        appTokenStoreService.deleteByRefId(currentUser.getId());
    }


    @PostMapping(value = "/robot")
    @Validated
    public Map<String, Object> generateRobotToken(@Valid  @RequestParam @NotBlank String robotId, @RequestParam @NotBlank String robotName,
                                                      @RequestParam(required = false, defaultValue = "99") int years) {
        if (StringUtils.isEmpty(robotName) || StringUtils.isEmpty(robotId))
            throw new InvalidDetailsException("RobotName and RobotId should not be null/empty");
        return service.generateTokenByRobot(robotId, robotName, years*daysInYear);
    }
    @PostMapping(value = "/test-plugin")
    @Validated
    public Map<String, Object> generateTestFrameworkPluginToken(@Valid  @RequestParam @NotBlank String userId,
                                                                @RequestParam @NotBlank String username,
                                                                @RequestParam(required = false, defaultValue = "99") int years) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(userId))
            throw new InvalidDetailsException("Username and userId should not be null/empty");
        return service.generateTokenForDashboard(userId, username, years*daysInYear);
    }



}
