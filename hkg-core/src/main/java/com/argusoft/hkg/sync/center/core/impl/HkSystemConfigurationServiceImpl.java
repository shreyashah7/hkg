/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.HkSystemConfigurationService;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class HkSystemConfigurationServiceImpl implements HkSystemConfigurationService {
    @Autowired
    private MongoGenericDao mongoGenericDao;

    private static final String SYSTEM_KEY = "systemKey";
    @Override
    public HkSystemConfigurationDocument getSystemConfigBySystemKey(String sysKey) {
        return (HkSystemConfigurationDocument) mongoGenericDao.getMongoTemplate().findById(sysKey, HkSystemConfigurationDocument.class);
    }

    @Override
    public String saveSystemConfig(HkSystemConfigurationDocument systemConfigurationDocument) {
        mongoGenericDao.update(systemConfigurationDocument);
        return (systemConfigurationDocument != null) ? systemConfigurationDocument.getId() : null;
    }

}
