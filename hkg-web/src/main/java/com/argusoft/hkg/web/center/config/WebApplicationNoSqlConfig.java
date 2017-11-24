package com.argusoft.hkg.web.center.config;

import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.web.config.CustomComponentServiceFilter;
import com.argusoft.hkg.web.sync.listner.SyncMongoEventListener;
import com.argusoft.hkg.web.sync.mongotransaction.SyncMongoTransactionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 *
 * @author satyajit
 */
@Configuration
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@Import(HkCoreApplicationConfig.class)
@ComponentScan(
        basePackages = {"com.argusoft.hkg"},
        useDefaultFilters = false,
        includeFilters = {
            //            @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Service.class, Component.class}),
            @ComponentScan.Filter(type = FilterType.CUSTOM, value = CustomComponentServiceFilter.class)
        }
)
@ImportResource({"/WEB-INF/securityContext.xml"})
public class WebApplicationNoSqlConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public SyncMongoEventListener syncMongoEventListener() {
        return new SyncMongoEventListener();
    }

    @Bean
    public SyncMongoTransactionAspect syncMongoTransactionAspect() {
        return new SyncMongoTransactionAspect();
    }
}
