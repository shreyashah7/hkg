/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.databeans;

import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncXmppUserDatabean {

    private Long id = 1l;
    private String userName;
    private String password;
    private Boolean userCreatedInTigase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUserCreatedInTigase() {
        return userCreatedInTigase;
    }

    public void setUserCreatedInTigase(Boolean userCreatedInTigase) {
        this.userCreatedInTigase = userCreatedInTigase;
    }

    @Override
    public String toString() {
        return "HkXmppUserDatabean{" + "id=" + id + ", userName=" + userName + ", password=" + password + '}';
    }
}
