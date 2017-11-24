/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.database;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.hkg.model.HkMessageEntity;
import java.util.List;

/**
 * Dao Class for HkMessageEntity.
 *
 * @author Mital
 */
public interface HkMessageDao extends GenericDao<HkMessageEntity, Long> {

    /**
     * Searches for the message body that matches the given text.
     *
     * @param searchMessage The message body that needs to be matched.
     * @param sender The id of the user who is sending the message.
     * @param franchise Id of franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of HkMessageEntity objects.
     */
    public List<HkMessageEntity> searchMessageTemplates(String searchMessage, Long sender, Long franchise, Boolean archiveStatus);
}
