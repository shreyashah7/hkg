/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

/**
 *
 * @author shreya
 */
public class SubEntityValueExceptionDataBean {
    
    private String id;
    private String forUsers;
    private String userToBeDisplay;
    private String forValue;
    private String valueToBeDisplay;
    private String dependentOnToBeDisplay;
    private String dependentOnFieldValuesToBeDisplay;
    private Long fieldId;
    private Long instanceId;
    private String fieldType;
    private String dependentOnField;
    private String dependentOnFieldValues;
    private Boolean isArchive;

    public SubEntityValueExceptionDataBean() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForUsers() {
        return forUsers;
    }

    public void setForUsers(String forUsers) {
        this.forUsers = forUsers;
    }

    public String getUserToBeDisplay() {
        return userToBeDisplay;
    }

    public void setUserToBeDisplay(String userToBeDisplay) {
        this.userToBeDisplay = userToBeDisplay;
    }

    public String getForValue() {
        return forValue;
    }

    public void setForValue(String forValue) {
        this.forValue = forValue;
    }

    public String getValueToBeDisplay() {
        return valueToBeDisplay;
    }

    public void setValueToBeDisplay(String valueToBeDisplay) {
        this.valueToBeDisplay = valueToBeDisplay;
    }

    public String getDependentOnToBeDisplay() {
        return dependentOnToBeDisplay;
    }

    public void setDependentOnToBeDisplay(String dependentOnToBeDisplay) {
        this.dependentOnToBeDisplay = dependentOnToBeDisplay;
    }

    public String getDependentOnFieldValuesToBeDisplay() {
        return dependentOnFieldValuesToBeDisplay;
    }

    public void setDependentOnFieldValuesToBeDisplay(String dependentOnFieldValuesToBeDisplay) {
        this.dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDependentOnField() {
        return dependentOnField;
    }

    public void setDependentOnField(String dependentOnField) {
        this.dependentOnField = dependentOnField;
    }

    public String getDependentOnFieldValues() {
        return dependentOnFieldValues;
    }

    public void setDependentOnFieldValues(String dependentOnFieldValues) {
        this.dependentOnFieldValues = dependentOnFieldValues;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    @Override
    public String toString() {
        return "SubEntityValueExceptionDataBean{" + "id=" + id + ", forUsers=" + forUsers + ", userToBeDisplay=" + userToBeDisplay + ", forValue=" + forValue + ", valueToBeDisplay=" + valueToBeDisplay + ", dependentOnToBeDisplay=" + dependentOnToBeDisplay + ", dependentOnFieldValuesToBeDisplay=" + dependentOnFieldValuesToBeDisplay + ", fieldId=" + fieldId + ", instanceId=" + instanceId + ", fieldType=" + fieldType + ", dependentOnField=" + dependentOnField + ", dependentOnFieldValues=" + dependentOnFieldValues + ", isArchive=" + isArchive + '}';
    }
    
}
