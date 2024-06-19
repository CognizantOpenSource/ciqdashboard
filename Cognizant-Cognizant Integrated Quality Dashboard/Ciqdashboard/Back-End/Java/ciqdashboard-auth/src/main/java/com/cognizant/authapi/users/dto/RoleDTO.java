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

import com.cognizant.authapi.users.beans.Permission;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *RoleDTO {
 *
 * @author Cognizant
 */
@Data
public class RoleDTO {
    @NotBlank(message = "Name should not be empty")
    @Size(min = 4, message = "Name should have at least 4 characters")
    private String name;
    private String desc;
    private List<Permission> permissions;
    @NotEmpty(message = "PermissionId's List should not be empty")
    @Size(min = 1, message = "PermissionId's List should have at least 1 PermissionId")
    private List<String> permissionIds;
}
