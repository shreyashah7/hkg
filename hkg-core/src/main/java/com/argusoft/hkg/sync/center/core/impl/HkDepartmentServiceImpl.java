/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.HkDepartmentService;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class HkDepartmentServiceImpl implements HkDepartmentService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public HkDepartmentDocument getDepartmentById(Long id) {
        return (HkDepartmentDocument) mongoGenericDao.getMongoTemplate().findById(id, HkDepartmentDocument.class, "department");
    }

    @Override
    public Long saveOrUpdateDepartment(HkDepartmentDocument document) {
        mongoGenericDao.update(document);
        return document != null ? document.getId() : null;
    }

}
