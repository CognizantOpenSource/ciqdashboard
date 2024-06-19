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

package com.cognizant.authapi.team.beans;

import com.cognizant.authapi.base.beans.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;


/**
 * Team - refer the Team collection in mongodb
 *
 * @author Cognizant
 */
@Data
@Document(collection = "teams")
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseModel {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 3, message = "Name should have at least 3 Characters")
    private String name;
    @NotEmpty(message = "Members list should not be empty/null")
    @Size(min = 2, message = "Members list should have at least 2 members")
    private Set<String> members;
}
