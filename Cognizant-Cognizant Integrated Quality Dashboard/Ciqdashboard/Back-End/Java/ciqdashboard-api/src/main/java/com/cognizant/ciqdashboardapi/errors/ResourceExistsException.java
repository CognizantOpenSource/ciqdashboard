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

package com.cognizant.ciqdashboardapi.errors;

/**
 * ResourceExistsException
 * @author Cognizant
 */

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String type, String name) {
        super(String.format("%s (%s) already exists!", type, name));
    }

    public ResourceExistsException(String type, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s : %s", type, fieldName, fieldValue));
    }
}
