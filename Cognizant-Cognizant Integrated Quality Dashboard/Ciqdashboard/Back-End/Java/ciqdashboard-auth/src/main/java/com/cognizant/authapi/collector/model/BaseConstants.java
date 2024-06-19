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

package com.cognizant.authapi.collector.model;

/**
 * BaseConstants - General constants linked through object
 *
 * @author Cognizant
 */
public class BaseConstants {
    private BaseConstants() {}

    public static final String CHART_COLLECTOR = "CHART_COLLECTOR";
    public static final String CIQDASHBOARD_CHART = "ciqdashboard.chart";
    public static final String CIQDASHBOARD_APP_STATUS = "ciqdashboard.appstatus";
    public static final String USER_DOES_NOT_HAVE_THE_S_ROLE = "Invalid User to generate Collector token (User doesn't have the %s role)";

    /*Token keys*/
    public static final String EXTERNAL_TOKEN = "external_token";
    public static final String TOKEN_EXPIRES_AT = "tokenExpiresAt";
    public static final String TOKEN_ID = "token-id";

    /*sAMAccountName is the ldap attribute that should match the login name */
    public static final String SAM_ACCOUNTNAME ="sAMAccountName";
    public static final String LDAP = "LDAP";

    /*AES Encryption*/
    public static final String AES_ALGORITHM ="AES";
    public static final String MSG_DIGEST ="MD5";
    public static final String AES_CBC ="AES/CBC/PKCS5Padding";
    public static final int KEY_LENGTH =32;
    public static final int IV_LENGTH =16;
    public static final int ITERATION =1;

}
