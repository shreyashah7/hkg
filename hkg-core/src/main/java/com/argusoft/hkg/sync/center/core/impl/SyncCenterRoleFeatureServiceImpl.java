/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;


import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncCenterRoleFeatureService;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service("roleFeatureService")
public class SyncCenterRoleFeatureServiceImpl implements SyncCenterRoleFeatureService {

    @Autowired
    private MongoGenericDao mongoGenericDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCenterRoleFeatureServiceImpl.class);

    public SyncCenterRoleFeatureServiceImpl() {

    }

    @Override
    public Long saveOrUpdate(SyncCenterRoleFeatureDocument document) {
        LOGGER.debug("Class:RoleFeatureServiceImpl  Method:saveOrUpdate");
//        LOGGER.debug("document: " + document);
        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    public SyncCenterRoleFeatureDocument retrieveById(Long id) {
        LOGGER.debug("Class:RoleFeatureServiceImpl  Method:retrieveById");
        LOGGER.debug("id: " + id);
        return mongoGenericDao.getMongoTemplate().findById(id, SyncCenterRoleFeatureDocument.class, "rolefeature");
    }

}
