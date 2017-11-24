/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.bean;

import java.util.List;

/**
 *
 * @author vipul
 */
public class RbFieldDataBean {
    
    private Long id;
    private String colName;
    private List<ConverterDataBean> converterDataBeanList;
    private String joinAttributes;
    private String filter;
    private String format;
    private String editedFieldLabel;
    private String dataType;
    private String alias;
    private String dbBaseName;
    private String dbBaseType;
    private String fieldLabel;
    private Integer fieldSequence;
    private Long feature;
    private Boolean colI18nRequired;
    private Boolean showTotal;
    private String orderType;
    private String sectionName;
    private String componentType;
    private String hkFieldId;
    private String convertedColumn;
    private String masterCode;
    private String fieldDisplayName;
    private Integer associatedCurrency;
    private Boolean isDff;
    private Boolean isSubFormValue;
    private Boolean includeTime;
    private String parentDbFieldName;
    private String parentDbBaseName;
    private String parentFieldLabel;
    private Boolean isRule;
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public Boolean isColI18nRequired() {
        return colI18nRequired;
    }

    public void setColI18nRequired(Boolean colI18nRequired) {
        this.colI18nRequired = colI18nRequired;
    }

    public Integer getAssociatedCurrency() {
        return associatedCurrency;
    }

    public Boolean getIsSubFormValue() {
        return isSubFormValue;
    }

    public void setIsSubFormValue(Boolean isSubFormValue) {
        this.isSubFormValue = isSubFormValue;
    }

    public Boolean getIsRule() {
        return isRule;
    }

    public void setIsRule(Boolean isRule) {
        this.isRule = isRule;
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

    public void setAssociatedCurrency(Integer associatedCurrency) {
        this.associatedCurrency = associatedCurrency;
    }

    public Boolean getIsDff() {
        return isDff;
    }

    public void setIsDff(Boolean isDff) {
        this.isDff = isDff;
    }

    public Boolean isIncludeTime() {
        return includeTime;
    }

    public void setIncludeTime(Boolean includeTime) {
        this.includeTime = includeTime;
    }
    
    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEditedFieldLabel() {
        return editedFieldLabel;
    }

    public void setEditedFieldLabel(String editedFieldLabel) {
        this.editedFieldLabel = editedFieldLabel;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public List<ConverterDataBean> getConverterDataBeanList() {
        return converterDataBeanList;
    }

    public void setConverterDataBeanList(List<ConverterDataBean> converterDataBeanList) {
        this.converterDataBeanList = converterDataBeanList;
    }

    public Integer getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(Integer fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getJoinAttributes() {
        return joinAttributes;
    }

    public void setJoinAttributes(String joinAttributes) {
        this.joinAttributes = joinAttributes;
    }
    
    public Boolean getShowTotal() {
        return showTotal;
    }

    public void setShowTotal(Boolean showTotal) {
        this.showTotal = showTotal;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public String getHkFieldId() {
        return hkFieldId;
    }

    public void setHkFieldId(String hkFieldId) {
        this.hkFieldId = hkFieldId;
    }

    public String getConvertedColumn() {
        return convertedColumn;
    }

    public void setConvertedColumn(String convertedColumn) {
        this.convertedColumn = convertedColumn;
    }

    public String getParentFieldLabel() {
        return parentFieldLabel;
    }

    public void setParentFieldLabel(String parentFieldLabel) {
        this.parentFieldLabel = parentFieldLabel;
    }

    @Override
    public String toString() {
        return "RbFieldDataBean{" + "id=" + id + ", colName=" + colName + ", converterDataBeanList=" + converterDataBeanList + ", joinAttributes=" + joinAttributes + ", filter=" + filter + ", format=" + format + ", editedFieldLabel=" + editedFieldLabel + ", dataType=" + dataType + ", alias=" + alias + ", dbBaseName=" + dbBaseName + ", dbBaseType=" + dbBaseType + ", fieldLabel=" + fieldLabel + ", fieldSequence=" + fieldSequence + ", feature=" + feature + ", colI18nRequired=" + colI18nRequired + ", showTotal=" + showTotal + ", orderType=" + orderType + ", sectionName=" + sectionName + ", componentType=" + componentType + ", hkFieldId=" + hkFieldId + ", convertedColumn=" + convertedColumn + ", masterCode=" + masterCode + ", fieldDisplayName=" + fieldDisplayName + ", associatedCurrency=" + associatedCurrency + ", isDff=" + isDff + ", isSubFormValue=" + isSubFormValue + ", includeTime=" + includeTime + ", parentDbFieldName=" + parentDbFieldName + ", parentDbBaseName=" + parentDbBaseName + ", isRule=" + isRule+ ", parentFieldLabel=" + parentFieldLabel  + '}';
    }


}
