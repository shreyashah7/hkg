/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

/**
 *
 * @author dhwani
 */
public class DependentFieldDataBean {

    private Long fieldId;
    private String entityName;
    private Integer sequenceNo;
    private Boolean isEditable;
    private Boolean isRequired;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Boolean isIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

   

    @Override
    public String toString() {
        return "DependentFieldDataBean{" + "fieldId=" + fieldId + ", entityName=" + entityName + ", sequenceNo=" + sequenceNo + ", isEditable=" + isEditable + ", isRequired=" + isRequired + '}';
    }

}
