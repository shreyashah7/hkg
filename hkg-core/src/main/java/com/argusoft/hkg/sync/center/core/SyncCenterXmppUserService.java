/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;


import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;

/**
 *
 * @author shruti
 */
public interface SyncCenterXmppUserService {

    /**
     * getUserbyUserId method retrieves a User object according to userId
     *
     * @param userName Takes in the name of User
     *
     * @return object of class SyncCenterXmppUserDocument
     */
    public SyncCenterXmppUserDocument getUserbyUserName(String userName);

    /**
     * This method is used to create/update User
     *
     * @author shruti
     * @param document object of SyncCenterXmppUserDocument
     * @return id of document
     */
    public Long saveOrUpdateUser(SyncCenterXmppUserDocument document);

    /**
     * this method will return xmpp user document by default id will be 1 as
     * there will be only one entry
     *
     * @return
     */
    public SyncCenterXmppUserDocument getUserDocumentById();
}
