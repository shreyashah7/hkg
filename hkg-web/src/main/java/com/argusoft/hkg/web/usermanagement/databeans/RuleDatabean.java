/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mansi
 */
public class RuleDatabean {

    private String id;
    private String ruleName;
    private String displayName;
    private List<RuleDatabean> children;
    private Long parentId;
    private Long ruleNumber;
    private String category;
    private String description;
    private String type;
    private List<Long> features;
    private Date fromDate;
    private Date toDate;
    private String status;
    private String validationMessage;
    private String colorCode;
    private String tooltipMsg;
    private String apply;
    private Boolean isArchive;
    private List<Long> fieldsToBeApplied;
    private Long franchiseId;
    private boolean isActive;
    private Date createOn;
    private Long createdBy;
    private Date modifiedOn;
    private Long modifiedBy;
    private String remarks;
    List<RuleCriteriaDataBean> criterias;
    private List<SelectItem> exceptionList;
    private List<Long> skipRules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
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

    public List<RuleCriteriaDataBean> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<RuleCriteriaDataBean> rules) {
        this.criterias = rules;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(Long ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getFeatures() {
        return features;
    }

    public void setFeatures(List<Long> features) {
        this.features = features;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getTooltipMsg() {
        return tooltipMsg;
    }

    public void setTooltipMsg(String tooltipMsg) {
        this.tooltipMsg = tooltipMsg;
    }

    public List<Long> getFieldsToBeApplied() {
        return fieldsToBeApplied;
    }

    public void setFieldsToBeApplied(List<Long> fieldsToBeApplied) {
        this.fieldsToBeApplied = fieldsToBeApplied;
    }

    public List<SelectItem> getExceptionList() {
        return exceptionList;
    }

    public void setExceptionList(List<SelectItem> exceptionList) {
        this.exceptionList = exceptionList;
    }

    public List<Long> getSkipRules() {
        return skipRules;
    }

    public void setSkipRules(List<Long> skipRules) {
        this.skipRules = skipRules;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<RuleDatabean> getChildren() {
        return children;
    }

    public void setChildren(List<RuleDatabean> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    

    @Override
    public String toString() {
        return "RuleDatabean{" + "id=" + id + ", ruleName=" + ruleName + ", displayName=" + displayName + ", ruleNumber=" + ruleNumber + ", category=" + category + ", description=" + description + ", type=" + type + ", features=" + features + ", fromDate=" + fromDate + ", toDate=" + toDate + ", status=" + status + ", validationMessage=" + validationMessage + ", colorCode=" + colorCode + ", tooltipMsg=" + tooltipMsg + ", apply=" + apply + ", isArchive=" + isArchive + ", fieldsToBeApplied=" + fieldsToBeApplied + ", franchiseId=" + franchiseId + ", isActive=" + isActive + ", createOn=" + createOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + ", remarks=" + remarks + ", criterias=" + criterias + ", exceptionList=" + exceptionList + ", skipRules=" + skipRules + '}';
    }

    

}


