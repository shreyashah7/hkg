/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author jyoti
 */
public class NotificationDataBean {

    private String id;
    private String notificationtype;
    private Date onDate;
    private String description;
    private String instanceType;
    private Long instanceId;
    private Long franchise;
    private Boolean isArchive;
    private Set<NotificationRecipientDataBean> notificationRecipientDataBeans;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationtype() {
        return notificationtype;
    }

    public void setNotificationtype(String notificationtype) {
        this.notificationtype = notificationtype;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Set<NotificationRecipientDataBean> getNotificationRecipientDataBeans() {
        return notificationRecipientDataBeans;
    }

    public void setNotificationRecipientDataBeans(Set<NotificationRecipientDataBean> notificationRecipientDataBeans) {
        this.notificationRecipientDataBeans = notificationRecipientDataBeans;
    }

    @Override
    public String toString() {
        return "NotificationDataBean{" + "id=" + id + ", notificationtype=" + notificationtype + ", onDate=" + onDate + ", description=" + description + ", instanceType=" + instanceType + ", instanceId=" + instanceId + ", franchise=" + franchise + ", isArchive=" + isArchive + ", notificationRecipientDataBeans=" + notificationRecipientDataBeans + '}';
    }

}
