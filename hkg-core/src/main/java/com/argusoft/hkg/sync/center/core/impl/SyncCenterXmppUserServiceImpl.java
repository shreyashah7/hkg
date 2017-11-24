/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;


import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncCenterXmppUserService;
import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service("centerHkXmppUserService")
public class SyncCenterXmppUserServiceImpl implements SyncCenterXmppUserService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public SyncCenterXmppUserDocument getUserbyUserName(String userName) {
        SyncCenterXmppUserDocument user = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!StringUtils.isEmpty(userName)) {
            criterias.add(Criteria.where("userName").is(userName));
            user = (SyncCenterXmppUserDocument) mongoGenericDao.findOneByCriteria(criterias, SyncCenterXmppUserDocument.class);
        }
        return user;
    }

    @Override
    public Long saveOrUpdateUser(SyncCenterXmppUserDocument document) {

        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    public SyncCenterXmppUserDocument getUserDocumentById() {
        return mongoGenericDao.getMongoTemplate().findById(1l, SyncCenterXmppUserDocument.class, "xmppuser");
    }
}
