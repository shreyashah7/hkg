/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import java.util.List;
import java.util.Set;

/**
 * Dao class for HkMessageRecipientDtlEntity.
 *
 * @author Mital
 */
public interface HkMessageRecipientDtlDao extends GenericDao<HkMessageRecipientDtlEntity, HkMessageRecipientDtlEntityPK> {

    /**
     * This method retrieves the priority notifications. It retrieves both
     * notifications - message and its reply. If the initiated message is a
     * priority, its reply is also considered as priority.
     *
     * @param recipient Id of the user for whom priority notifications are to be
     * fetched.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of HkMessageRecipientDtl objects.
     */
    public List<HkMessageRecipientDtlEntity> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority);

    /**
     *
     * @param messageIds
     * @param archiveStatus
     * @return
     */
    public List<HkMessageRecipientDtlEntity> retrieveMessageRecipientDtlsByMessageId(Set<Long> messageIds, Boolean archiveStatus);
}
