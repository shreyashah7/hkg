/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncRoleService;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 *
 * @author gautam
 */
@Service
public class SyncRoleServiceImpl implements SyncRoleService {
    
    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public List<UmDesignationDocument> retrieveRolesByIds(List<Long> roleIds) {
        Query desigQuery = new Query();
        desigQuery.addCriteria(Criteria.where(ID).in(roleIds));
        List<UmDesignationDocument> desigDocs = mongoGenericDao.getMongoTemplate().find(desigQuery, UmDesignationDocument.class);
        return desigDocs;
    }

}
