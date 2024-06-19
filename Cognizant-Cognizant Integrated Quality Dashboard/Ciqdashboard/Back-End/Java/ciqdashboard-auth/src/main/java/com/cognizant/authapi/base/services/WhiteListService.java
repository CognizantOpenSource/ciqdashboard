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

package com.cognizant.authapi.base.services;

import com.cognizant.authapi.base.error.InvalidValueException;
//import com.cognizant.authapi.users.repos.WhiteListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *WhiteListService
 *
 * @author Cognizant
 */
@Service
@Slf4j
public class WhiteListService {
    public static final String TYPE_URL = "url";
    public static final Boolean ALLOW = true;
    public static final Boolean BLOCK = false;

    //private WhiteListRepository repo;

    private Map<String, ListEntry> lists;

    /*public WhiteListService(WhiteListRepository repo) {
        this.repo = repo;
        load();
    }*/

    /**
     * load whitelist from repo and cache them as predicates of allowed & blocked patterns for each type

    private void load() {
        Map<String, Map<Boolean, List<String>>> store = new HashMap<>();
        for (WhiteList entry : repo.findAll()) {
            if (!store.containsKey(entry.getType())) {
                Map typeMap = new HashMap();
                typeMap.put(true, new ArrayList<String>());
                typeMap.put(false, new ArrayList<String>());
                store.put(entry.getType(), typeMap);
            }
            store.get(entry.getType()).get(entry.isWhitelist()).addAll(entry.getMatch());
        }
        log.info("loading whitelists - {}", store);
        lists = Collections.unmodifiableMap(store.entrySet().stream().
                map(entry -> new HashMap.SimpleImmutableEntry<>(
                        entry.getKey(),
                        ListEntry.create(entry.getKey(), entry.getValue().get(ALLOW), entry.getValue().get(BLOCK))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
    */
    private boolean defaultAction() {
        return BLOCK;
    }

    /**
     * refresh whitelists

    public void reload() {
        load();
    }
     */

    public boolean isValid(String type, String target) {
        if (lists.containsKey(type)) {
            return lists.get(type).isValid(target);
        }
        log.debug("type '{}' is not configured in whitelist, available - {}", type, lists.keySet());
        return defaultAction();
    }

    public String validateUrl(String url) {
        if (!isValidUrl(url)) {
            throw new InvalidValueException("whitelist error: invalid url - " + url);
        }
        return url;
    }

    public boolean isValidUrl(String url) {
        return isValid(TYPE_URL, url);
    }

    private static class ListEntry {
        /**
         * initial state will not block or allow
         */
        private Predicate<String> allowed = s -> false;
        private Predicate<String> blocked = s -> false;
        private String type;

        private ListEntry() {
        }

        public static ListEntry create(String type, List<String> allow, List<String> block) {
            ListEntry entry = new ListEntry();
            entry.type = type;
            /**
             * set predicate state to match patterns if at-least one matcher present
             */
            if (Objects.nonNull(allow) && !allow.isEmpty()) {
                entry.allowed = Pattern.compile(
                        allow.stream().collect(Collectors.joining("|"))).asPredicate();
            }
            if (Objects.nonNull(block) && !block.isEmpty()) {
                entry.blocked = Pattern.compile(
                        block.stream().collect(Collectors.joining("|"))).asPredicate();
            }
            return entry;
        }

        public boolean isValid(String target) {
            if (blocked.test(target)) {
                log.debug("{} '{}' is blacklisted", type, target);
                return false;
            }
            if (allowed.test(target))
                return true;
            log.debug("{} '{}' is not whitelisted", type, target);
            /**
             * not blacklisted or whitelisted - return invalid as default behaviour
             */
            return target == null || target.isEmpty();
        }

    }
}
