/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.hkg.database.HkMessageRecipientDao;
import com.argusoft.hkg.model.HkMessageRecipientEntity;
import com.argusoft.hkg.model.HkMessageRecipientEntityPK;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

/**
 * Implementation class for HkMessageRecipientDao.
 *
 * @author mpritmani
 */
@Repository
public class HkMessageRecipientDaoImpl extends BaseAbstractGenericDao<HkMessageRecipientEntity, HkMessageRecipientEntityPK> implements HkMessageRecipientDao {

    private static final Property MESSAGE_OBJ = Property.forName("hkMessageRecipientPK.messageObj");
    private static final Property IS_ARCHIVE = Property.forName("isArchive");

    @Override
    public List<HkMessageRecipientEntity> retrieveMessageRecipientDtlsByMessageId(Set<Long> messageIds, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkMessageRecipientEntity.class);

        criteria.add(MESSAGE_OBJ.in(messageIds));

        if (archiveStatus != null) {
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }
}
