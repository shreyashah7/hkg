/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;


import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncCenterUserRoleService;
import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service("userRoleService")
public class SyncCenterUserRoleServiceImpl implements SyncCenterUserRoleService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncCenterUserRoleServiceImpl.class);
    @Autowired
    private MongoGenericDao mongoGenericDao;

    public SyncCenterUserRoleServiceImpl() {

    }

    @Override
    public void saveOrUpdate(SyncCenterUserRoleDocument document) {
        LOGGER.debug("In   saveOrUpdateCoatedRough   RoleFeatureDocument== ");
//        System.out.println(mongoGenericDao.findAll());
//        document.setId(new ObjectId());
        mongoGenericDao.update(document);

    }

    @Override
    public SyncCenterUserRoleDocument retrieve() {
        List findAll = mongoGenericDao.getMongoTemplate().findAll(SyncCenterUserRoleDocument.class, "userrole");
        LOGGER.debug("Size : " + findAll.size());
        if (findAll != null) {
            return (SyncCenterUserRoleDocument) findAll.get((findAll.size() - 1));
        }
        return null;
    }

}
