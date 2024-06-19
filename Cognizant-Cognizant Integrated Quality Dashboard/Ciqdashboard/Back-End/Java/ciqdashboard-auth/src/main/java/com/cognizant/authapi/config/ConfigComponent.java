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

package com.cognizant.authapi.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Base64;

/**
 *ConfigComponent - get the value from properties files
 *
 * @author Cognizant
 */

@Configuration
public class ConfigComponent {
    @Autowired
    Environment environment;

    @Bean("JasyptEncryptorBean")
    public StringEncryptor stringEncryptor(){
        String property = environment.getProperty("jasypt.encryptor.password");
        String decode = new String(Base64.getDecoder().decode(property));
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(decode);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setPoolSize(1);
        config.setIvGenerator(new RandomIvGenerator());
        config.setProviderName("SunJCE");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
