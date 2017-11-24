/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import static com.argusoft.hkg.sync.center.core.SyncCenterUserService.FIRST_NAME;
import static com.argusoft.hkg.sync.center.core.SyncCenterUserService.LAST_NAME;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service("userCenterService")
public class SyncCenterUserServiceImpl implements SyncCenterUserService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    public static final String ROLE_ID = "roleId";
    public static final String COMPANY_ID = "company";
    public static final String DEPARTMENT_ID = "departmentId";

    @Override
    public SyncCenterUserDocument getUserbyUserName(String userName, boolean isUserRoleRequired, Boolean isActive) {
        SyncCenterUserDocument user = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!StringUtils.isEmpty(userName)) {
            criterias.add(Criteria.where("userId").is(userName));
            user = (SyncCenterUserDocument) mongoGenericDao.findOneByCriteria(criterias, SyncCenterUserDocument.class);
        }
        return user;
    }

    @Override
    public Long saveOrUpdateUser(SyncCenterUserDocument document) {

        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByRoleId(List<Long> roleId, String search, Long companyId) {
        List<SyncCenterUserDocument> users = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleId)) {
            criterias.add(Criteria.where(ROLE_ID).in(roleId));
        }
        if (!StringUtils.isEmpty(search)) {
            criterias.add(new Criteria().orOperator(Criteria.where(FIRST_NAME).regex(search, "i"), Criteria.where(LAST_NAME).regex(search, "i"), Criteria.where(MIDDLE_NAME).regex(search, "i")));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(COMPANY_ID).is(companyId));
        }
        users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public SyncCenterUserDocument retrieveUsersById(Long userId) {
        return mongoGenericDao.getMongoTemplate().findById(userId, SyncCenterUserDocument.class, "user");
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByIds(List<Long> userIds) {
        List<SyncCenterUserDocument> users = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userIds)) {
            criterias.add(Criteria.where(ID).in(userIds));
            users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        }
        return users;
    }
    
     @Override
    public List<SyncCenterUserDocument> retrieveUsersByDepartmentIds(List<Long> departmentIds, Long companyId) {
        List<SyncCenterUserDocument> users = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departmentIds)) {
            criterias.add(Criteria.where(DEPARTMENT_ID).in(departmentIds));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(COMPANY_ID).is(companyId.toString()));
        }
        users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        return users;
    }


}
