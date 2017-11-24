package com.argusoft.hkg.core.config;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.argusoft.generic.core.config.CoreApplicationConfig;


@Configuration
@Import(CoreApplicationConfig.class)
public class HkCoreApplicationConfig {
    @Bean
    public BasicPasswordEncryptor basicPasswordEncryptor() {

        return new BasicPasswordEncryptor();
    }
}
