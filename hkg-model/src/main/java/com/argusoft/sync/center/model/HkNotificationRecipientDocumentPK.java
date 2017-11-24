/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

/**
 *
 * @author shruti
 */
public class HkNotificationRecipientDocumentPK {
    private String notification;
    private long forUser;

    public HkNotificationRecipientDocumentPK() {
    }

    public HkNotificationRecipientDocumentPK(String notification, long forUser) {
        this.notification = notification;
        this.forUser = forUser;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public long getForUser() {
        return forUser;
    }

    public void setForUser(long forUser) {
        this.forUser = forUser;
    }

    @Override
    public String toString() {
        return "HkNotificationRecipientDocumentPK{" + "notification=" + notification + ", forUser=" + forUser + '}';
    }

}
