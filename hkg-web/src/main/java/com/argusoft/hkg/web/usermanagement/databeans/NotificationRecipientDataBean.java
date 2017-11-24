/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;

/**
 *
 * @author jyoti
 */
public class NotificationRecipientDataBean {

    private String notification;
    private Long forUser;
    private Boolean isSeen;
    private Date seenOn;
    private String status;
    private Boolean isArchive;
    private Date lastModifiedOn;
    private NotificationDataBean notificationDataBean;

    public NotificationDataBean getNotificationDataBean() {
        return notificationDataBean;
    }

    public void setNotificationDataBean(NotificationDataBean notificationDataBean) {
        this.notificationDataBean = notificationDataBean;
    }


    public Boolean isIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(Date seenOn) {
        this.seenOn = seenOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Long getForUser() {
        return forUser;
    }

    public void setForUser(Long forUser) {
        this.forUser = forUser;
    }

    @Override
    public String toString() {
        return "NotificationRecipientDataBean{" + "notification=" + notification + ", forUser=" + forUser + ", isSeen=" + isSeen + ", seenOn=" + seenOn + ", status=" + status + ", isArchive=" + isArchive + ", lastModifiedOn=" + lastModifiedOn + ", notificationDataBean=" + notificationDataBean + '}';
    }


  

}
