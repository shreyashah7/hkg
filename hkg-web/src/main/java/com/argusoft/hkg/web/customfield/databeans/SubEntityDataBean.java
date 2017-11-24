/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import com.argusoft.hkg.model.HkFieldEntity;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author shifa
 */
public class SubEntityDataBean {

    private Long id;
    private Long subFieldId;
    private String componentType;
    private String subFieldName;
    private String subFieldLabel;
    private String subFieldType;
    private long franchise;
    private boolean isArchive;
    private long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;
    private String validationPattern;
    private boolean isDroplistField;
    private Integer sequenceNo;
    private HkFieldEntity parentField;
    private Boolean isCreate;
    private Boolean isUpdate;
    private Boolean isServerData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getSubFieldName() {
        return subFieldName;
    }

    public void setSubFieldName(String subFieldName) {
        this.subFieldName = subFieldName;
    }

    public String getSubFieldLabel() {
        return subFieldLabel;
    }

    public void setSubFieldLabel(String subFieldLabel) {
        this.subFieldLabel = subFieldLabel;
    }

    public String getSubFieldType() {
        return subFieldType;
    }

    public void setSubFieldType(String subFieldType) {
        this.subFieldType = subFieldType;
    }

    public boolean isIsDroplistField() {
        return isDroplistField;
    }

    public void setIsDroplistField(boolean isDroplistField) {
        this.isDroplistField = isDroplistField;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public HkFieldEntity getParentField() {
        return parentField;
    }

    public void setParentField(HkFieldEntity parentField) {
        this.parentField = parentField;
    }

    public Boolean isIsCreate() {
        return isCreate;
    }

    public void setIsCreate(Boolean isCreate) {
        this.isCreate = isCreate;
    }

    public Boolean isIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Long getSubFieldId() {
        return subFieldId;
    }

    public void setSubFieldId(Long subFieldId) {
        this.subFieldId = subFieldId;
    }

    public Boolean getIsServerData() {
        return isServerData;
    }

    public void setIsServerData(Boolean isServerData) {
        this.isServerData = isServerData;
    }

    @Override
    public String toString() {
        return "SubEntityDataBean{" + "id=" + id + ", subFieldId=" + subFieldId + ", componentType=" + componentType + ", subFieldName=" + subFieldName + ", subFieldLabel=" + subFieldLabel + ", subFieldType=" + subFieldType + ", franchise=" + franchise + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", status=" + status + ", validationPattern=" + validationPattern + ", isDroplistField=" + isDroplistField + ", sequenceNo=" + sequenceNo + ", parentField=" + parentField + ", isCreate=" + isCreate + ", isUpdate=" + isUpdate + ", isServerData=" + isServerData + '}';
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.subFieldLabel);
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
        final SubEntityDataBean other = (SubEntityDataBean) obj;
        if (!Objects.equals(this.subFieldLabel, other.subFieldLabel)) {
            return false;
        }
        return true;
    }

}
