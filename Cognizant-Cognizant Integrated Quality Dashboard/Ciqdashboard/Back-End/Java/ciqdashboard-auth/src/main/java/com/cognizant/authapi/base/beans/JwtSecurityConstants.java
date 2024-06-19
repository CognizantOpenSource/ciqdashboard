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


package com.cognizant.authapi.base.beans;

/**
 * JwtSecurityConstants - Security Token Constants
 *
 * @author Cognizant
 */


public class JwtSecurityConstants {

    private JwtSecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String AUTH_TOKEN = "auth_token";
    public static final String EXPIRES_AT = "expiresAt";
    public static final String COLLECTOR_TOKEN = "collector_token";
    public static final String EXTERNAL_TOKEN = "external_token";
    public static final String TOKEN_EXPIRES_AT = "tokenExpiresAt";

    /*Custom Token Constants*/
    public static final String CUSTOM_TOKEN_ID = "X-ciqdashboard-token-id";
    public static final String BEARER_TEMPLATE = "Bearer %s";
}