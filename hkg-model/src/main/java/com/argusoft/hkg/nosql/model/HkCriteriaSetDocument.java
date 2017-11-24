/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shifa
 */
@Document(collection = "criteriaSet")
public class HkCriteriaSetDocument {

    @Id
    private ObjectId id;
    private Long ruleNumber;
    private String ruleName;
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
    private List<Long> fieldsToBeApplied;
    private String apply;
    private Date createOn;
    private Long createdBy;
    private Long modifiedBy;
    private boolean isActive;
    private Boolean isArchive;
    private Long franchiseId;
    private Date modifiedOn;
 
    private String remarks;
    List<HkRuleCriteriaDocument> criterias;
    List<Long> skipRules;

    public Long getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(Long ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    

   
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<HkRuleCriteriaDocument> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<HkRuleCriteriaDocument> criterias) {
        this.criterias = criterias;
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

    public List<Long> getSkipRules() {
        return skipRules;
    }

    public void setSkipRules(List<Long> skipRules) {
        this.skipRules = skipRules;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.ruleNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HkCriteriaSetDocument other = (HkCriteriaSetDocument) obj;
        if (!Objects.equals(this.ruleNumber, other.ruleNumber)) {
            return false;
        }
        return true;
    }
    
}
