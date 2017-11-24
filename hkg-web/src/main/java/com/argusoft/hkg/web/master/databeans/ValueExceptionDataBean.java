/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.databeans;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author shreya
 */
public class ValueExceptionDataBean {

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
    private Boolean isUpdated;
    private String dependentOnFieldValues;
    private Boolean isArchive;

    public ValueExceptionDataBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getForUsers() {
        return forUsers;
    }

    public void setForUsers(String forUsers) {
        this.forUsers = forUsers;
    }

    public String getForValue() {
        return forValue;
    }

    public void setForValue(String forValue) {
        this.forValue = forValue;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
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


    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getUserToBeDisplay() {
        return userToBeDisplay;
    }

    public void setUserToBeDisplay(String userToBeDisplay) {
        this.userToBeDisplay = userToBeDisplay;
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

    @Override
    public String toString() {
        return "ValueExceptionDataBean{" + "id=" + id + ", forUsers=" + forUsers + ", userToBeDisplay=" + userToBeDisplay + ", forValue=" + forValue + ", valueToBeDisplay=" + valueToBeDisplay + ", dependentOnToBeDisplay=" + dependentOnToBeDisplay + ", dependentOnFieldValuesToBeDisplay=" + dependentOnFieldValuesToBeDisplay + ", fieldId=" + fieldId + ", instanceId=" + instanceId + ", fieldType=" + fieldType + ", dependentOnField=" + dependentOnField + ", dependentOnFieldValues=" + dependentOnFieldValues + '}';
    }
    
}
