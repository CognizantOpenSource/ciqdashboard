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

package com.cognizant.authapi.users.util;

import com.cognizant.authapi.users.beans.Account;
import com.cognizant.authapi.users.beans.Role;
import com.cognizant.authapi.users.dto.AccountDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 *AccUtil
 *
 * @author Cognizant
 */
@Component
public class AccUtil {

    public AccountDTO convertToDto(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        BeanUtils.copyProperties(account, accountDTO);
        accountDTO.setRoleIds(
                account.getRoles().stream().map(Role::getName).collect(toList())
        );

        return accountDTO;
    }

    public List<AccountDTO> convertToDtoList(List<Account> accountList) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        accountList.forEach(account -> accountDTOList.add(convertToDto(account)));
        return accountDTOList;
    }

    public Account convertToEntity(AccountDTO accountDTO, List<Role> roleList) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDTO, account);
        account.setRoles(roleList);
        return account;
    }

    public Account convertToNewEntity(AccountDTO accountDTO) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDTO, account);
        return account;
    }

    public Account mergeToEntity(AccountDTO accountDTO, Account account, List<Role> roleList) {
        BeanUtils.copyProperties(accountDTO, account);
        List<String> ownProjectIds = Optional.ofNullable(account.getOwnProjectIds()).orElseGet(ArrayList::new);
        ownProjectIds.retainAll(account.getProjectIds());
        account.setOwnProjectIds(ownProjectIds);
        account.setRoles(roleList);
        return account;
    }
}
