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
 * @author dhwani
 */
@Document(collection = "usergoalstatus")
public class HkUserGoalStatusDocument implements Cloneable {

    @Id
    private ObjectId id;
    private Long goalTemplate;
    private Long forUser;
    private Date fromDate;
    private Date toDate;
    private Long activityNode;
    private Long activityGroup;
    private String goalType;
    private Integer minTarget;
    private Integer maxTarget;
    private Integer targetCount;
    private Integer realizedCount;
    private Boolean goalAchieved;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    protected Long franchiseId;
    private String status;
    private ObjectId previousGoalStatusDocumentId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGoalTemplate() {
        return goalTemplate;
    }

    public void setGoalTemplate(Long goalTemplate) {
        this.goalTemplate = goalTemplate;
    }

    public Long getForUser() {
        return forUser;
    }

    public void setForUser(Long forUser) {
        this.forUser = forUser;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public Integer getMinTarget() {
        return minTarget;
    }

    public void setMinTarget(Integer minTarget) {
        this.minTarget = minTarget;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getRealizedCount() {
        return realizedCount;
    }

    public void setRealizedCount(Integer realizedCount) {
        this.realizedCount = realizedCount;
    }

    public Boolean getGoalAchieved() {
        return goalAchieved;
    }

    public void setGoalAchieved(Boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public ObjectId getPreviousGoalStatusDocumentId() {
        return previousGoalStatusDocumentId;
    }

    public void setPreviousGoalStatusDocumentId(ObjectId previousGoalStatusDocumentId) {
        this.previousGoalStatusDocumentId = previousGoalStatusDocumentId;
    }

    public Integer getMaxTarget() {
        return maxTarget;
    }

    public void setMaxTarget(Integer maxTarget) {
        this.maxTarget = maxTarget;
    }

    @Override
    public String toString() {
        return "HkUserGoalStatusDocument{" + "id=" + id + ", goalTemplate=" + goalTemplate + ", forUser=" + forUser + ", fromDate=" + fromDate + ", toDate=" + toDate + ", activityNode=" + activityNode + ", activityGroup=" + activityGroup + ", goalType=" + goalType + ", minTarget=" + minTarget + ", targetCount=" + targetCount + ", realizedCount=" + realizedCount + ", goalAchieved=" + goalAchieved + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", franchiseId=" + franchiseId + ", status=" + status + ", previousGoalStatusDocumentId=" + previousGoalStatusDocumentId + '}';
    }

    @Override
    public HkUserGoalStatusDocument clone() throws CloneNotSupportedException {
        return (HkUserGoalStatusDocument) super.clone();
    }

}
