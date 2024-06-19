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

package com.cognizant.authapi.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 *UserSettingsDTO
 *
 * @author Cognizant
 */
@Data
@NoArgsConstructor
@Validated
public class UserSettingsDTO {
    @Valid
    private Object jenkins;
    @Valid
    private Object gitlab;
    @Valid
    private Object github;
    @Valid
    private Object bitbucket;
    @Valid
    private Object externalApps;

    private Object dashboard;
}
