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

package com.cognizant.authapi.base.filters;

        import com.cognizant.authapi.base.error.InvalidValueException;
        import com.cognizant.authapi.base.error.UserNotFoundException;
        import com.cognizant.authapi.base.services.JwtTokenService;
        import com.cognizant.authapi.base.util.TokenUtil;
        import com.cognizant.authapi.users.beans.User;
        import com.cognizant.authapi.users.beans.UserPrincipal;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpHeaders;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
        import org.springframework.util.StringUtils;
        import org.springframework.web.filter.OncePerRequestFilter;

        import javax.servlet.FilterChain;
        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        import java.util.List;

/**
 *JwtAuthenticationFilter - Filter the service for Authentication
 *
 * @author Cognizant
 */

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService tokenProvider;

    //@Autowired
    //private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            log.debug("jwt token ****** " + jwt);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                User user = tokenProvider.getUser(jwt);
                List<String> permissions = tokenProvider.getPermissions(jwt);
                UserDetails userDetails = UserPrincipal.create(user, permissions);
                if (userDetails != null && userDetails.isEnabled()) {
                    //log.debug("'{}' logged in", userDetails.getUsername());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new UserNotFoundException("Inactive or Username not found");
                }
            }
        } catch (UserNotFoundException | InvalidValueException ex) {
            log.error("Could not authenticate user", ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String uuidToken = "";
        if (StringUtils.hasText(bearerToken)) {
            uuidToken = TokenUtil.getUUIDStringFromToken(bearerToken);
            if (StringUtils.hasText(uuidToken)) {
                String tokenFromDB = tokenProvider.getTokenFromDB(uuidToken);
                if (StringUtils.hasText(tokenFromDB)) return tokenFromDB;
            }
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            throw new InvalidValueException("Invalid Auth token - " + bearerToken);
        }
    }
}