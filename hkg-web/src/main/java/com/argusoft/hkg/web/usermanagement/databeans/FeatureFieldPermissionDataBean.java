/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;

/**
 *
 * @author shifa
 */
public class FeatureFieldPermissionDataBean {
   private String id;
    private Long designation;
    private Long feature;
    private boolean searchFlag;
    private boolean parentViewFlag;
    private boolean readonlyFlag;
    private boolean editableFlag;
    private Boolean isRequired;
    private Integer sequenceNo;
    private String entityName;
    private String sectionCode;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long franchise;  
    private Long fieldId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public boolean getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    public boolean getParentViewFlag() {
        return parentViewFlag;
    }

    public void setParentViewFlag(boolean parentViewFlag) {
        this.parentViewFlag = parentViewFlag;
    }

    public boolean getReadonlyFlag() {
        return readonlyFlag;
    }

    public void setReadonlyFlag(boolean readonlyFlag) {
        this.readonlyFlag = readonlyFlag;
    }

    public boolean getEditableFlag() {
        return editableFlag;
    }

    public void setEditableFlag(boolean editableFlag) {
        this.editableFlag = editableFlag;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    @Override
    public String toString() {
        return "FeatureFieldPermissionDataBean{" + "id=" + id + ", designation=" + designation + ", feature=" + feature + ", searchFlag=" + searchFlag + ", parentViewFlag=" + parentViewFlag + ", readonlyFlag=" + readonlyFlag + ", editableFlag=" + editableFlag + ", isRequired=" + isRequired + ", sequenceNo=" + sequenceNo + ", entityName=" + entityName + ", sectionCode=" + sectionCode + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", franchise=" + franchise + ", fieldId=" + fieldId + '}';
    }
    
    
}
