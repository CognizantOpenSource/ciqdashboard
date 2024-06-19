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

package com.cognizant.ciqdashboardapi.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * IDPageConfig
 * @author Cognizant
 */

@Data
public class IDPageConfig {

    private String pageId = UUID.randomUUID().toString();
    @NotBlank(message = "PageName should not be empty/null")
    @Size(min = 3, message = "PageName minimum characters should be '3' ")
    private String name;
    private GridConfig gridConfig;
    private boolean active;
    private List<IDItemConfig> items;

    @Data
    public class GridConfig{
        private int rows;
        private int columns;
        private int margin;
    }
}
