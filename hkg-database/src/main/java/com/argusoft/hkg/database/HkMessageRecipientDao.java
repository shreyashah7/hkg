/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.hkg.model.HkMessageRecipientEntity;
import com.argusoft.hkg.model.HkMessageRecipientEntityPK;
import java.util.List;
import java.util.Set;

/**
 * Dao clasHkMessageRecipientEntityipient entity.
 *
 * @author Mital
 */
public interface HkMessageRecipientDao extends GenericDao<HkMessageRecipientEntity, HkMessageRecipientEntityPK> {

    /**
     * This method retrieves the list of HkMessageRecipientEntity objects for
     * the given list of message ids.
     *
     * @param messageIds The message ids that need to be retrieved.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of HkMessageRecipientEntity objects.
     */
    public List<HkMessageRecipientEntity> retrieveMessageRecipientDtlsByMessageId(Set<Long> messageIds, Boolean archiveStatus);
}
