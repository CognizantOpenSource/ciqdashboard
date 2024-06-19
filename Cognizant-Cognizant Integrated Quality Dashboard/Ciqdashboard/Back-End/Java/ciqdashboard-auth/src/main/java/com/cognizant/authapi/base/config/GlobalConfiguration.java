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

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.security.GeneralSecurityException;

/**
 * GlobalConfiguration To get URI address
 *
 * @author Cognizant
 */

@Configuration
@Slf4j
@EnableAsync
public class GlobalConfiguration {
    private static InetSocketAddress address;

    @Value("${app.uri.get.proxy}")
    private String proxyURI;

    @Autowired
    HttpProxySelectorBuilder httpProxySelectorBuilder;
    @PostConstruct
    private void init() {
        address = getProxy(URI.create(proxyURI));
    }

    @Bean
    public HttpTransport getHttpTransport() throws GeneralSecurityException {
        HttpTransport httpTransport = null;
        if (address != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            httpTransport = new NetHttpTransport.Builder()
                    .doNotValidateCertificate()
                    .setProxy(proxy).build();
        } else {
            httpTransport = new NetHttpTransport();
        }
        return httpTransport;
    }

    private InetSocketAddress getProxy(URI uri) {
        System.setProperty("java.net.useSystemProxies", "true");
        Proxy proxy = ProxySelector.getDefault().select(uri).iterator().next();
        log.info("proxy type : " + proxy.type());
        InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy.address();

        if (inetSocketAddress == null) {
            log.info("No Proxy");
        } else {
            log.info("proxy hostname : " + inetSocketAddress.getHostName());
            log.info("proxy port : " + inetSocketAddress.getPort());
        }
        return inetSocketAddress;
    }

}
