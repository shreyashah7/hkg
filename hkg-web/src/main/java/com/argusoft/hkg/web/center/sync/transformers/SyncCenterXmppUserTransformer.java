/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.transformers;

import com.argusoft.hkg.sync.center.core.SyncCenterXmppUserService;
import com.argusoft.hkg.sync.xmpp.databeans.SyncXmppUserDatabean;
import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncCenterXmppUserTransformer {

    @Autowired
    SyncCenterXmppUserService centerHkXmppUserService;
    

    public void saveHkXmppUser(SyncXmppUserDatabean hkXmppUserDatabean) {
        centerHkXmppUserService.saveOrUpdateUser(convertHkXmppUserDatabeanToHkXmppUserDocument(hkXmppUserDatabean, null));
    }

    public SyncCenterXmppUserDocument getUserById() {
        SyncCenterXmppUserDocument userDocumentById = centerHkXmppUserService.getUserDocumentById();
        return userDocumentById;
    }
    public SyncCenterXmppUserDocument convertHkXmppUserDatabeanToHkXmppUserDocument(SyncXmppUserDatabean xmppUserDatabean, SyncCenterXmppUserDocument hkXmppUserDocument) {
        if (hkXmppUserDocument == null) {
            hkXmppUserDocument = new SyncCenterXmppUserDocument();
        }
        hkXmppUserDocument.setId(1l);
        hkXmppUserDocument.setPassword(xmppUserDatabean.getPassword());
        hkXmppUserDocument.setUserName(xmppUserDatabean.getUserName());
        return hkXmppUserDocument;
    }

    public SyncXmppUserDatabean convertHkXmppUserDocumentToHkXmppUserDatabean(SyncCenterXmppUserDocument hkXmppUserDocument, SyncXmppUserDatabean xmppUserDatabean) {
        if (xmppUserDatabean == null) {
            xmppUserDatabean = new SyncXmppUserDatabean();
        }
        xmppUserDatabean.setId(1l);
        xmppUserDatabean.setPassword(hkXmppUserDocument.getPassword());
        xmppUserDatabean.setUserName(hkXmppUserDocument.getUserName());
        return xmppUserDatabean;
    }
}
