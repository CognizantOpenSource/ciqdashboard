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

package com.cognizant.ciqdashboardapi.base.services;

import com.cognizant.ciqdashboardapi.base.models.Account;
import com.cognizant.ciqdashboardapi.base.models.JwtSecurityConstants;
import com.cognizant.ciqdashboardapi.base.models.User;
import com.cognizant.ciqdashboardapi.errors.InvalidAuthenticationException;
import com.cognizant.ciqdashboardapi.services.ProjectMappingService;
import com.cognizant.ciqdashboardapi.services.UserSessionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JwtTokenService
 * @author Cognizant
 */

@Component
@Slf4j
public class JwtTokenService {

    @Value("${app.jwt.token.secret.key}")
    private String appSecretKey;
    @Value("${app.jwt.token.expiration.milliSec}")
    private long appExpirationInMs;
    private long oneDayMilliSec = 24 * 60 * 60 * 1000L;

    private String secretKey;

    //@Autowired
    //AppTokenStoreService appTokenStoreService;
    @Autowired
    ProjectMappingService projectMappingService;
    @Autowired
    UserSessionService userSessionService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(appSecretKey.getBytes());
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtSecurityConstants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(JwtSecurityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

/*    public String getUserIdFromJWT(String token) {
        Claims claims = getClaims(token);

        return claims.getSubject();
    }*/

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        if (claims.getBody().getExpiration().before(new Date())) {
            return false;
        }
        log.info("Verified token for subject " + claims.getBody().getSubject());
        log.info("permissions: " + claims.getBody().get("permissions"));
        return true;
    }

    public User getUser(String token) {
        Claims claims = getClaims(token);
        User user = new User();
        user.setId(claims.getSubject());
        user.setFirstName((String) claims.get("firstName"));
        user.setLastName((String) claims.get("lastName"));
        user.setEmail((String) claims.get("email"));
        user.setActive((boolean) claims.get("active"));
        Account account = new Account();

        List<String> teams = new ArrayList<>();
        List<String> list = (List<String>) claims.get("teams");
        if (!CollectionUtils.isEmpty(list)){
            teams = list;
        }

        List<String> userOwnProjectIds = projectMappingService.getUserOwnProjectIds(claims.getSubject());
        List<String> userProjectIds = projectMappingService.getUserProjectIds(claims.getSubject(), teams);

        account.setOwnProjectIds(userOwnProjectIds);
        account.setProjectIds(userProjectIds);

        user.setAccount(account);
        return user;
    }

    public List<String> getPermissions(String token) {
        Claims claims = getClaims(token);
        String permissionsString = (String) claims.get("permissions");
        if (StringUtils.isEmpty(permissionsString)) return new ArrayList<>();
        return Arrays.asList(permissionsString.split(","));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    //public String getTokenFromDB(String uuidToken){
        //Optional<AppTokenStore> optional = appTokenStoreService.get(uuidToken);
        //if (optional.isPresent()) {
            //return optional.get().getToken();
        //}
        //return "";
    //}

    public void validateUserSession(String jwt)  {
        boolean isValidSession = userSessionService.validateSession(getClaims(jwt));
        if (!isValidSession) throw new InvalidAuthenticationException();
    }
}
