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
@Document(collection="stockroutestatus")
public class HkStockRouteStatusDocument {

    @Id
    private ObjectId id;   
    private Long activityFlowNode;
    private Long activityFlowGroup;
    private Long activityVersion;
    private String unitType;
    private String unitInstance;
    private Integer occurences;
    private Integer duration;
    private String status;
    private Date createdOn;
    private Long createdBy;
    private Date lastModifiedOn;
    private Long lastModifiedBy;   
    private Long franchise;

    public HkStockRouteStatusDocument(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public Long getActivityVersion() {
        return activityVersion;
    }

    public void setActivityVersion(Long activityVersion) {
        this.activityVersion = activityVersion;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitInstance() {
        return unitInstance;
    }

    public void setUnitInstance(String unitInstance) {
        this.unitInstance = unitInstance;
    }

    public Integer getOccurences() {
        return occurences;
    }

    public void setOccurences(Integer occurences) {
        this.occurences = occurences;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    @Override
    public String toString() {
        return "HkStockRouteStatusDocument{" + "id=" + id + '}';
    }                
}
