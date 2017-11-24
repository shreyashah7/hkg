/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "hk_notification_configuration_info")
@NamedQueries({
    @NamedQuery(name = "HkNotificationConfigurationEntity.findAll", query = "SELECT h FROM HkNotificationConfigurationEntity h")})
public class HkNotificationConfigurationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "notification_name", nullable = false, length = 200)
    private String notificationName;
    @Column(length = 500)
    private String description;
    @Basic(optional = false)
    @Column(name = "notification_type", nullable = false, length = 10)
    private String notificationType;
    @Column(name = "associated_rule", length = 50)
    private String associatedRule;
    @Column(name = "at_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date atTime;
    @Column(name = "actual_end_date")
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;
    @Column(name = "after_units")
    private Integer afterUnits;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "end_repeat_mode", length = 5)
    private String endRepeatMode;
    @Column(name = "monthly_on_day")
    private Integer monthlyOnDay;
    @Column(name = "repeatative_mode", length = 5)
    private String repeatativeMode;
    @Column(name = "repetition_cnt")
    private Integer repetitionCnt;
    @Column(name = "weekly_on_days", length = 50)
    private String weeklyOnDays;
    @Column(name = "web_message", length = 500)
    private String webMessage;
    @Column(name = "sms_message", length = 500)
    private String smsMessage;
    @Column(name = "email_message", length = 500)
    private String emailMessage;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Column(name = "activity_node")
    private Long activityNode;
    @Column(name = "activity_group")
    private Long activityGroup;

    public HkNotificationConfigurationEntity() {
    }

    public HkNotificationConfigurationEntity(Long id) {
        this.id = id;
    }

    public HkNotificationConfigurationEntity(Long id, String notificationName, String notificationType, String status, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn) {
        this.id = id;
        this.notificationName = notificationName;
        this.notificationType = notificationType;
        this.status = status;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public Long getActivityNode() {
        return activityNode;
    }

    public void setActivityNode(Long activityNode) {
        this.activityNode = activityNode;
    }

    public Long getActivityGroup() {
        return activityGroup;
    }

    public void setActivityGroup(Long activityGroup) {
        this.activityGroup = activityGroup;
    }    
  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkNotificationConfigurationEntity)) {
            return false;
        }
        HkNotificationConfigurationEntity other = (HkNotificationConfigurationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationConfigurationEntity[ id=" + id + " ]";
    }
    
}
