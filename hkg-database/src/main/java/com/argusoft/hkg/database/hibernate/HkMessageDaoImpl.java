/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.database.HkMessageDao;
import com.argusoft.hkg.model.HkMessageEntity;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

/**
 * Implementation class for HkMessageEntityDao.
 *
 * @author mpritmani
 */
@Repository
public class HkMessageDaoImpl extends BaseAbstractGenericDao<HkMessageEntity, Long> implements HkMessageDao {

    private static final Property IS_ARCHIVE = Property.forName("isArchive");
    private static final Property MESSAGE_BODY = Property.forName("messageBody");
    private static final Property COPY_MESSAGE = Property.forName("copyMessage");
    private static final Property FRANCHISE = Property.forName("franchise");
    private static final Property CREATED_BY = Property.forName("createdBy");
    private static final Property STATUS = Property.forName("status");

    @Override
    public List<HkMessageEntity> searchMessageTemplates(String searchString, Long sender, Long franchise, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkMessageEntity.class);
        criteria.add(STATUS.eq(HkSystemConstantUtil.ACTIVE));

        if (searchString != null) {
            criteria.add(COPY_MESSAGE.like(searchString, MatchMode.ANYWHERE).ignoreCase());
        }

        if (sender != null) {
            criteria.add(CREATED_BY.eq(sender));
        }

        if (franchise <= 0) {
            criteria.add(FRANCHISE.eq(0L));
        } else {
            //  Get all the franchise specific and default message if other than 0 is passed as franchise
            criteria.add(FRANCHISE.in(new Long[]{0L, franchise}));
        }

        if (archiveStatus != null) {
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }
}
