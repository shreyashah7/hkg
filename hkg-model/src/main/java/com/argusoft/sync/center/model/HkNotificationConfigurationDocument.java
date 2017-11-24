/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author harshit
 */
@Document(collection = "notification_configuration")
public class HkNotificationConfigurationDocument {

    @Id
    private Long id;
    private String notificationName;
    private String description;
    private String notificationType;
    private String associatedRule;
    private Date atTime;
    private Date actualEndDate;
    private Integer afterUnits;
    private Date endDate;
    private String endRepeatMode;
    private Integer monthlyOnDay;
    private String repeatativeMode;
    private Integer repetitionCnt;
    private String weeklyOnDays;
    private String webMessage;
    private String smsMessage;
    private String emailMessage;
    private String status;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long activityGroup;
    private Long activityNode;

    public HkNotificationConfigurationDocument() {
    }

    public HkNotificationConfigurationDocument(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getAssociatedRule() {
        return associatedRule;
    }

    public void setAssociatedRule(String associatedRule) {
        this.associatedRule = associatedRule;
    }

    public Date getAtTime() {
        return atTime;
    }

    public void setAtTime(Date atTime) {
        this.atTime = atTime;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Integer getAfterUnits() {
        return afterUnits;
    }

    public void setAfterUnits(Integer afterUnits) {
        this.afterUnits = afterUnits;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndRepeatMode() {
        return endRepeatMode;
    }

    public void setEndRepeatMode(String endRepeatMode) {
        this.endRepeatMode = endRepeatMode;
    }

    public Integer getMonthlyOnDay() {
        return monthlyOnDay;
    }

    public void setMonthlyOnDay(Integer monthlyOnDay) {
        this.monthlyOnDay = monthlyOnDay;
    }

    public String getRepeatativeMode() {
        return repeatativeMode;
    }

    public void setRepeatativeMode(String repeatativeMode) {
        this.repeatativeMode = repeatativeMode;
    }

    public Integer getRepetitionCnt() {
        return repetitionCnt;
    }

    public void setRepetitionCnt(Integer repetitionCnt) {
        this.repetitionCnt = repetitionCnt;
    }

    public String getWeeklyOnDays() {
        return weeklyOnDays;
    }

    public void setWeeklyOnDays(String weeklyOnDays) {
        this.weeklyOnDays = weeklyOnDays;
    }

    public String getWebMessage() {
        return webMessage;
    }

    public void setWebMessage(String webMessage) {
        this.webMessage = webMessage;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getActivityGroup() {
        return activityGroup;
    }

    public void setActivityGroup(Long activityGroup) {
        this.activityGroup = activityGroup;
    }

    public Long getActivityNode() {
        return activityNode;
    }

    public void setActivityNode(Long activityNode) {
        this.activityNode = activityNode;
    }

    @Override
    public String toString() {
        return "HkNotificationConfigurationDocument{" + "id=" + id + ", notificationName=" + notificationName + ", description=" + description + ", notificationType=" + notificationType + ", associatedRule=" + associatedRule + ", atTime=" + atTime + ", actualEndDate=" + actualEndDate + ", afterUnits=" + afterUnits + ", endDate=" + endDate + ", endRepeatMode=" + endRepeatMode + ", monthlyOnDay=" + monthlyOnDay + ", repeatativeMode=" + repeatativeMode + ", repetitionCnt=" + repetitionCnt + ", weeklyOnDays=" + weeklyOnDays + ", webMessage=" + webMessage + ", smsMessage=" + smsMessage + ", emailMessage=" + emailMessage + ", status=" + status + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", activityGroup=" + activityGroup + ", activityNode=" + activityNode + '}';
    }


}
