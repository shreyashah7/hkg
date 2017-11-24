/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;

/**
 *
 * @author shruti
 */
public class HkNotificationDocument {

    private static final long serialVersionUID = 1L;
    private String id;
    private String notificationType;
    private Date onDate;
    private String description;
    private String instanceType;
    private Long instanceId;
    private Long franchise;
    private Boolean isArchive;
    private String notificationConfigurationType;
    private Long notificationConfiguration;
    private String mailStatus;

    public HkNotificationDocument() {
    }

    public HkNotificationDocument(String id, String notificationType, Date onDate, String description, Long franchise, Boolean isArchive) {
        this.id = id;
        this.notificationType = notificationType;
        this.onDate = onDate;
        this.description = description;
        this.franchise = franchise;
        this.isArchive = isArchive;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
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

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getNotificationConfigurationType() {
        return notificationConfigurationType;
    }

    public void setNotificationConfigurationType(String notificationConfigurationType) {
        this.notificationConfigurationType = notificationConfigurationType;
    }

    public Long getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(Long notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
    }

    @Override
    public String toString() {
        return "HkNotificationDocument{" + "id=" + id + ", notificationType=" + notificationType + ", onDate=" + onDate + ", description=" + description + ", instanceType=" + instanceType + ", instanceId=" + instanceId + ", franchise=" + franchise + ", isArchive=" + isArchive + ", notificationConfigurationType=" + notificationConfigurationType + ", notificationConfiguration=" + notificationConfiguration + ", mailStatus=" + mailStatus + '}';
    }

}
