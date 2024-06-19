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

package com.cognizant.authapi.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *TokenUtil - pattern to create a token
 *
 * @author Cognizant
 */

public class TokenUtil {
    private static final String regex = "(?s)[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private TokenUtil(){}

    public static String getUUIDStringFromToken(String token){
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(token);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
