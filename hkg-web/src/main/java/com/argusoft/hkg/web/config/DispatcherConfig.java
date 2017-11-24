package com.argusoft.hkg.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author kelvin
 */
@Configuration
@ComponentScan(
        basePackages = {"com.argusoft.hkg.web"},
        useDefaultFilters = false,
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.CUSTOM, value = CustomControllerFilter.class)}
)
@EnableWebMvc
@EnableAsync
public class DispatcherConfig {
   
    }
