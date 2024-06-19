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

package com.cognizant.authapi.users.services;

import com.cognizant.authapi.base.error.CustomInvalidCredentialException;
import com.cognizant.authapi.base.error.UserNotFoundException;
import com.cognizant.authapi.users.beans.Account;
import com.cognizant.authapi.users.beans.TokenRequest;
import com.cognizant.authapi.users.beans.User;
import com.cognizant.authapi.users.beans.UserLoginDetails;
import com.cognizant.authapi.users.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.util.Optional;

import static com.cognizant.authapi.collector.model.BaseConstants.LDAP;
import static com.cognizant.authapi.collector.model.BaseConstants.SAM_ACCOUNTNAME;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 *LDAPUserService
 *
 * @author Cognizant
 */

@Service
public class LDAPUserService {
    @Autowired
    private UserService userService;
    @Autowired
    LdapTemplate ldapTemplate;
    @Value("${ldap.user.mailAttribute}")
    private String mail;
    @Value("${ldap.user.orgAttribute}")
    private String org;
    @Value("${ldap.user.firstNameAttribute}")
    private String cn;
    @Value("${ldap.user.securityAuthentication}")
    private String securityAuthentication;

    public User validateUserDetails(TokenRequest tokenRequest) {
        DirContextOperations dirContextOperations;
        try {
            AuthenticatedLdapEntryContextMapper<DirContextOperations> mapper = authenticatedLdapEntryContextMapper();
            ldapTemplate.setIgnorePartialResultException(true);
            dirContextOperations = ldapTemplate.authenticate(LdapQueryBuilder.query().where(SAM_ACCOUNTNAME).is(tokenRequest.getUsername()), tokenRequest.getPassword(), mapper);
        } catch (org.springframework.ldap.AuthenticationException e) {
            throw new CustomInvalidCredentialException("User not Found! Please Enter Valid User");
        } catch (Exception e) {
            throw new CustomInvalidCredentialException("Unable to connect to LDAP Server! Please try again");
        }
        Optional<User> optional = userService.getUserByEmailAndType(dirContextOperations.getStringAttribute(mail), LDAP);
        if (optional.isPresent()) {
            User user = optional.get();
            if (!user.isActive())
                throw new CustomInvalidCredentialException(String.format("Please contact CIQDashboard administrator to authorize your id."));
            return user;
        } else {
            return generateUser(dirContextOperations);
        }
    }

    private AuthenticatedLdapEntryContextMapper<DirContextOperations> authenticatedLdapEntryContextMapper() {
        return (DirContext ctx, LdapEntryIdentification ldapEntryIdentification) -> {
            try {
                ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, securityAuthentication);
                return (DirContextOperations) ctx.lookup(ldapEntryIdentification.getRelativeName());
            } catch (NamingException e) {
                throw new RuntimeException("lookup failed for:" + ldapEntryIdentification.getRelativeName(), e);
            }
        };
    }

    private User generateUser(DirContextOperations dirContextOperations) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(dirContextOperations.getStringAttribute(mail));
        userDTO.setFirstName(dirContextOperations.getStringAttribute(cn));
        userDTO.setOrg(dirContextOperations.getStringAttribute(org));
        userDTO.setType(LDAP);
        return userService.signUpUser(userDTO);
    }
}