/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mmodi
 */
@Document(collection = "notificationcheck")
public class HkNotificationCheckDocument {
    
    @Id
    private ObjectId id;
    
    private Long notificationConfiguration;
    private String unitType;
    private ObjectId unitInstance;   
    private Long forUser;
    private Long activityFlowNode;
    private Long activityFlowGroup;    
    private String status;
    private Date createdOn;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(Long notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public ObjectId getUnitInstance() {
        return unitInstance;
    }

    public void setUnitInstance(ObjectId unitInstance) {
        this.unitInstance = unitInstance;
    }

    public Long getForUser() {
        return forUser;
    }

    public void setForUser(Long forUser) {
        this.forUser = forUser;
    }

    public Long getActivityFlowNode() {
        return activityFlowNode;
    }

    public void setActivityFlowNode(Long activityFlowNode) {
        this.activityFlowNode = activityFlowNode;
    }

    public Long getActivityFlowGroup() {
        return activityFlowGroup;
    }

    public void setActivityFlowGroup(Long activityFlowGroup) {
        this.activityFlowGroup = activityFlowGroup;
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

    @Override
    public String toString() {
        return "HkNotificationCheckDocument{" + "id=" + id+ '}';
    }
       
}
