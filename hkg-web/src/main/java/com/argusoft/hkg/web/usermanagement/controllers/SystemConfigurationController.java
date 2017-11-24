package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.SystemConfigurationDatabean;
import com.argusoft.hkg.web.usermanagement.transformers.SystemConfigurationTransformer;
import com.argusoft.usermanagement.common.core.UMSystemConfigurationService;
import com.argusoft.usermanagement.common.model.UMSystemConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author satyajit
 */
@RestController
@RequestMapping("/systemconfiguration")
public class SystemConfigurationController {

    @Autowired
    private UMSystemConfigurationService systemConfigurationService;
    @Autowired
    private SystemConfigurationTransformer hkSystemConfigurationTransformer;
    
    @Autowired
    private LoginDataBean hkLoginDataBean;

    @RequestMapping(method = RequestMethod.GET)
    public List<SystemConfigurationDatabean> retrieveAllSysConfig() {
        List<UMSystemConfiguration> sysConfigs = systemConfigurationService.retrieveSystemConfigurationsBasedOnLikeKeySearch("");
        List<SystemConfigurationDatabean> systemConfigurationDatabeanList = new ArrayList<>();

        for (UMSystemConfiguration systemConfig : sysConfigs) {
            systemConfigurationDatabeanList.add(
                    hkSystemConfigurationTransformer.convertSystemConfigurationModelToSystemConfigurationDatabean(systemConfig));
        }
        hkLoginDataBean.getUserId();
        return systemConfigurationDatabeanList;
    }

    @RequestMapping(value = "/create/{key}/{value}", method = RequestMethod.POST)
    public String createSysConfig(@PathVariable("key") String key, @PathVariable("value") String value) {
        String result;
        UMSystemConfiguration systemConfiguration = new UMSystemConfiguration(key, value, true);
        result = systemConfigurationService.createSystemConfiguration(systemConfiguration);
        return result;
    }
}
