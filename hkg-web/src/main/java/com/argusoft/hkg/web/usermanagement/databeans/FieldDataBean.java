/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author sidhdharth
 */
public class FieldDataBean {

    private Long id;
    private String fieldLabel;
    private String fieldType;
    private String componentType;
    private String uiFieldName;
    private String dbFieldName;
    private String validationPattern;
    private String dbBaseName;
    private String dbBaseType;
    private Long feature;
    private String editedFieldLabel;
    private String orderType;
    private Integer fieldSequence;
    private Boolean isCustom;
    private Boolean isEditable;
    private Integer associatedCurrency;
    private Boolean isSubFormValue;
    private String parentDbFieldName;
    private String parentDbBaseName;
    private String parentFieldLabel;
    private String formulaValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public Boolean getIsSubFormValue() {
        return isSubFormValue;
    }

    public void setIsSubFormValue(Boolean isSubFormValue) {
        this.isSubFormValue = isSubFormValue;
    }

    public String getParentDbFieldName() {
        return parentDbFieldName;
    }

    public void setParentDbFieldName(String parentDbFieldName) {
        this.parentDbFieldName = parentDbFieldName;
    }

    public String getParentDbBaseName() {
        return parentDbBaseName;
    }

    public void setParentDbBaseName(String parentDbBaseName) {
        this.parentDbBaseName = parentDbBaseName;
    }
    
    public Integer getAssociatedCurrency() {
        return associatedCurrency;
    }

    public void setAssociatedCurrency(Integer associatedCurrency) {
        this.associatedCurrency = associatedCurrency;
    }

    public String getEditedFieldLabel() {
        return editedFieldLabel;
    }

    public void setEditedFieldLabel(String editedFieldLabel) {
        this.editedFieldLabel = editedFieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getDbBaseName() {
        return dbBaseName;
    }

    public void setDbBaseName(String dbBaseName) {
        this.dbBaseName = dbBaseName;
    }

    public String getDbBaseType() {
        return dbBaseType;
    }

    public void setDbBaseType(String dbBaseType) {
        this.dbBaseType = dbBaseType;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public String getUiFieldName() {
        return uiFieldName;
    }

    public void setUiFieldName(String uiFieldName) {
        this.uiFieldName = uiFieldName;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public Integer getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(Integer fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

    public Boolean isIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public Boolean isIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getParentFieldLabel() {
        return parentFieldLabel;
    }

    public void setParentFieldLabel(String parentFieldLabel) {
        this.parentFieldLabel = parentFieldLabel;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    @Override
    public String toString() {
        return "FieldDataBean{" + "id=" + id + ", fieldLabel=" + fieldLabel + ", fieldType=" + fieldType + ", componentType=" + componentType + ", uiFieldName=" + uiFieldName + ", dbFieldName=" + dbFieldName + ", validationPattern=" + validationPattern + ", dbBaseName=" + dbBaseName + ", dbBaseType=" + dbBaseType + ", feature=" + feature + ", editedFieldLabel=" + editedFieldLabel + ", orderType=" + orderType + ", fieldSequence=" + fieldSequence + ", isCustom=" + isCustom + ", isEditable=" + isEditable + ", associatedCurrency=" + associatedCurrency + ", isSubFormValue=" + isSubFormValue + ", parentDbFieldName=" + parentDbFieldName + ", parentDbBaseName=" + parentDbBaseName + ", parentFieldLabel=" + parentFieldLabel + '}';
    }
}
