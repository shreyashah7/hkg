/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync;

/**
 *
 * @author akta
 */

public interface HkSyncSqlGenericService {

    /**
     * This method is made for only update HkNotificationRecipientEntity when it
     * comes from center to master by sync, please do not use this method in
     * other purpose as this method does not use our @Transactional annotation
     * and spring transaction manager This method is made so that
     * HkNotificationRecipientEntity once updated by record coming from center
     * to master it should not go again from master to center
     *
     * @param hkNotificationRecipientEntity
     *
     */
    public void updateEntity(Object hkNotificationRecipientEntity);
}
