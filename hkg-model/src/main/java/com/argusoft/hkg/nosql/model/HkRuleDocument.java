/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author mansi
 */
public class HkRuleDocument {

    private int id;
    private String ruleName;
    private String apply;
    private Date createOn;
    private Long createdBy;
    private Date modifiedOn;
    private Long modifiedBy;
    private boolean isActive;
    private boolean isArchive;
    private Long franchiseId;
    private String remarks;
    List<HkRuleCriteriaDocument> criterias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public List<HkRuleCriteriaDocument> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<HkRuleCriteriaDocument> criterias) {
        this.criterias = criterias;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "HkRuleDocument{" + "id=" + id + ", ruleName=" + ruleName + ", apply=" + apply + ", createOn=" + createOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + ", isActive=" + isActive + ", isArchive=" + isArchive + ", franchiseId=" + franchiseId + ", remarks=" + remarks + ", criterias=" + criterias + '}';
    }

}
