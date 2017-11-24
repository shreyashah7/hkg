package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.web.usermanagement.databeans.SystemConfigurationDatabean;
import com.argusoft.usermanagement.common.model.UMSystemConfiguration;
import org.springframework.stereotype.Service;

/**
 *
 * @author satyajit
 */
@Service
public class SystemConfigurationTransformer {

    public SystemConfigurationDatabean convertSystemConfigurationModelToSystemConfigurationDatabean(UMSystemConfiguration systemConfiguration) {

        SystemConfigurationDatabean systemConfigurationDatabean = new SystemConfigurationDatabean();

        systemConfigurationDatabean.setSystemKey(systemConfiguration.getSystemKey());
        systemConfigurationDatabean.setKeyValue(systemConfiguration.getKeyValue());
        systemConfigurationDatabean.setIsActive(systemConfiguration.getIsActive());

        return systemConfigurationDatabean;
    }

    public UMSystemConfiguration convertSystemConfigurationDatabeanToSystemConfigurationModel(SystemConfigurationDatabean systemConfigurationDatabean, UMSystemConfiguration systemConfiguration) {
        if (systemConfiguration == null) {
            systemConfiguration = new UMSystemConfiguration();
        }
        systemConfiguration.setSystemKey(systemConfigurationDatabean.getSystemKey());
        systemConfiguration.setKeyValue(systemConfigurationDatabean.getKeyValue());
        systemConfiguration.setIsActive(systemConfigurationDatabean.isIsActive());
        return systemConfiguration;
    }

}
