/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import com.argusoft.hkg.sync.SyncXmppUserService;
import com.argusoft.hkg.sync.xmpp.databeans.SyncXmppUserDatabean;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncXmppUserTransformer {

    @Autowired
    SyncXmppUserService hkXmppUserService;

    public SyncXmppUserDatabean getHkXmppUserByFranchiseId(Long franchiseId) {
        SyncXmppUser hkXmppUser = hkXmppUserService.getUserbyFranchise(franchiseId);
        return hkXmppUser != null ? convertHkXmppUserToHkXmppUserDatabean(hkXmppUser, null) : null;
    }

    public SyncXmppUserDatabean convertHkXmppUserToHkXmppUserDatabean(SyncXmppUser xmppUser, SyncXmppUserDatabean xmppUserDatabean) {
        if (xmppUserDatabean == null) {
            xmppUserDatabean = new SyncXmppUserDatabean();
        }
        xmppUserDatabean.setId(1l);
        xmppUserDatabean.setPassword(xmppUser.getPassword());
        xmppUserDatabean.setUserName(xmppUser.getUserName());
        xmppUserDatabean.setUserCreatedInTigase(xmppUser.getUserCreatedInTigase());
//        xmppUserDatabean.setRoster(null);
        return xmppUserDatabean;
    }

    public SyncXmppUser convertHkXmppUserDatabeanToHkXmppUser(SyncXmppUserDatabean xmppUserDatabean, SyncXmppUser xmppUser) {
        if (xmppUser == null) {
            xmppUser = new SyncXmppUser();
        }
        xmppUser.setId(xmppUserDatabean.getId());
        xmppUser.setPassword(xmppUserDatabean.getPassword());
        xmppUser.setUserName(xmppUserDatabean.getUserName());
        xmppUser.setFranchise(0l);
        xmppUser.setCreatedOn(new Date());
        xmppUser.setIsActive(true);
//        xmppUserDatabean.setRoster(null);
        return xmppUser;
    }

    public void saveXmppUser(SyncXmppUser xmppUser) {
        hkXmppUserService.createUser(xmppUser);
    }

}
