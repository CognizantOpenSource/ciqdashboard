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

package com.cognizant.authapi.users.beans;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *Account - refers the permissions collection in MongoDB
 *
 * @author Cognizant
 */
@Data
@Document(collection = "permissions")
public class Permission {
    @Id
    @NotBlank(message = "Id should not be empty")
    @Size(min = 4, message = "Id should have at least 4 characters")
    private String id;
    @NotBlank(message = "Name should not be empty")
    @Size(min = 4, message = "Name should have at least 4 characters")
    private String name;

}
