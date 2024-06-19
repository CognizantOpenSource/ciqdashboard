
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

package com.cognizant.authapi.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HttpProxySelectorBuilder
 *
 * @author Cognizant
 */

@Slf4j
@Service
public class HttpProxySelectorBuilder {

    @Value("${http.proxyExclude:127.0.0.1}")
    String proxyExclude;

    public ProxySelector buildFor(Proxy proxy) {
        Predicate excluded = Pattern.compile(
                Stream.of(Objects.toString(proxyExclude, "").split(";")).collect(Collectors.joining("|"))).asPredicate();
        return new ProxySelector() {

            private boolean excluded(URI uri) {
                return excluded.test((uri.getHost()));
            }

            @Override
            public List<Proxy> select(URI uri) {
                if (!excluded(uri)) {
                    log.debug("selected proxy '{}' for '{}'", proxy.address(), uri.getHost());
                    return Collections.singletonList(proxy);
                }
                log.debug("ignored proxy for '{}'", uri.getHost());
                return Collections.emptyList();
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                log.error("error '{}' while connecting to {} with proxy {}", ioe.getMessage(), uri.getHost(), sa);
            }
        };
    }


}
