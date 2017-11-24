/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmppclient;

import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import java.util.Map;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatMessageListener;

/**
 *
 * @author shruti
 */
public interface SyncXmppClient extends ChatMessageListener {

    public void sendMessage(SyncTransactionEntity hkTransactionEntity, String from) throws XMPPException, SmackException.NotConnectedException;

    public void connect();

    public void addVCard(String jid, String password, Map<String, String> properties);

    public Boolean addUser(String userid, String password, Map<String, String> properties) throws SmackException.NoResponseException, XMPPException.XMPPErrorException, SmackException.NotConnectedException;

    public boolean createRosterEntry(String userId);
}
