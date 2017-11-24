/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database.hibernate;

import com.argusoft.generic.database.common.impl.BaseAbstractGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.database.HkMessageRecipientDtlDao;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Implementation class for HkMessageRecipientDtlDao.
 *
 * @author Mital
 */
public class HkMessageRecipientDtlDaoImpl extends BaseAbstractGenericDao<HkMessageRecipientDtlEntity, HkMessageRecipientDtlEntityPK> implements HkMessageRecipientDtlDao {

    private static final Property IS_ARCHIVE = Property.forName("isArchive");
    private static final Property HAS_PRIORITY = Property.forName("hasPriority");
    private static final Property MESSAGE_TO = Property.forName("hkMessageRecipientDtlPK.messageTo");
    private static final Property IS_ATTENDED = Property.forName("isAttended");
    private static final Property MESSAGE_FROM = Property.forName("messageFrom");
    private static final Property STATUS = Property.forName("status");
    private static final Property MESSAGE_OBJ = Property.forName("hkMessageRecipientDtlPK.messageObj");

    @Override
    public List<HkMessageRecipientDtlEntity> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority) {
        Criteria criteria = getCurrentSession().createCriteria(HkMessageRecipientDtlEntity.class);
        if (hasPriority != null) {
            criteria.add(HAS_PRIORITY.eq(hasPriority));
        }
        criteria.add(STATUS.eq(HkSystemConstantUtil.MessageStatus.PENDING));
        if (recipient != null) {
            criteria.add(Restrictions.or(
                    //  if recipient is the receiver whom the message is sent and if he has not seen the message yet.
                    Restrictions.and(MESSAGE_TO.eq(recipient), IS_ATTENDED.eq(Boolean.FALSE)),
                    //  if recipient had sent the message earlier and now he has got the reply which he hasn't seen yet i.e. status = R.
                    Restrictions.and(MESSAGE_FROM.eq(recipient), STATUS.eq(HkSystemConstantUtil.MessageStatus.REPLIED)))
            );
        }

        if (archiveStatus != null) {
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }

    @Override
    public List<HkMessageRecipientDtlEntity> retrieveMessageRecipientDtlsByMessageId(Set<Long> messageIds, Boolean archiveStatus) {
        Criteria criteria = getCurrentSession().createCriteria(HkMessageRecipientDtlEntity.class);

        criteria.add(MESSAGE_OBJ.in(messageIds));

        if (archiveStatus != null) {
            criteria.add(IS_ARCHIVE.eq(archiveStatus));
        }

        return criteria.list();
    }
}
