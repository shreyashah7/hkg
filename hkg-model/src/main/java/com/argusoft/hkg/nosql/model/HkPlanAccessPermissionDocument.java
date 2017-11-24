/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mmodi
 */
@Document(collection = "planaccesspermission")
public class HkPlanAccessPermissionDocument {

    @Id
    private ObjectId id;
    private Long activityNode;
    private List<String> accessToUsers;
    private List<String> accessToStatuses;
    private String status;
    private Long franchiseId;
    private Date createdOn;
    private Long createdBy;
    private Date lastModifiedOn;
    private Long lastModifiedBy;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getActivityNode() {
        return activityNode;
    }

    public void setActivityNode(Long activityNode) {
        this.activityNode = activityNode;
    }

    public List<String> getAccessToUsers() {
        return accessToUsers;
    }

    public void setAccessToUsers(List<String> accessToUsers) {
        this.accessToUsers = accessToUsers;
    }

    public List<String> getAccessToStatuses() {
        return accessToStatuses;
    }

    public void setAccessToStatuses(List<String> accessToStatuses) {
        this.accessToStatuses = accessToStatuses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "HkPlanAccessPermissionDocument{" + "id=" + id + '}';
    }        
}
