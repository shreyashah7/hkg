/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author harshit
 */
public class CustomFieldDataBean {

    //Custom field id
    private Long id;
    //Custom field lable
    private String label;
    //custom field label name for save old name
    private String oldLabelName;
    //Custom field type like text,textArea , etc.
    private String type;
    //Store value in comma seprater for value of multivalue store
    private String values;
    //Store value currency master
    private Integer currencyMasterId;
    //validation pattern
    private String validationPattern;
    private String formulaValue;
    //isnew is flag that added new or is it old
    private Boolean isNewField = Boolean.FALSE;
    //Seq no
    private Integer seqNo;
    private String Status;
    private Boolean isEditable;
    private Boolean isDependable;
    private String fieldType;
    private String dbFieldName;
    private Map<String, String> dependantValuesMap;
    private Set<SubEntityDataBean> subEntityDataBean;
    private List<SubEntityDataBean> subEntityList;
    private String defaultSelectedValue;
    private String defaultMultiSelectedValue;
    private Map<String, String> defaultMultiSelectedValueMap;
    private String featureName;
    private String sectionName;
    private Boolean isPrivate;

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Boolean isIsNewField() {
        return isNewField;
    }

    public void setIsNewField(Boolean isNewField) {
        this.isNewField = isNewField;
    }

    public Integer getCurrencyMasterId() {
        return currencyMasterId;
    }

    public void setCurrencyMasterId(Integer currencyMasterId) {
        this.currencyMasterId = currencyMasterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public String getOldLabelName() {
        return oldLabelName;
    }

    public void setOldLabelName(String oldLabelName) {
        this.oldLabelName = oldLabelName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Map<String, String> getDependantValuesMap() {
        return dependantValuesMap;
    }

    public void setDependantValuesMap(Map<String, String> dependantValuesMap) {
        this.dependantValuesMap = dependantValuesMap;
    }

    public Boolean getIsDependable() {
        return isDependable;
    }

    public void setIsDependable(Boolean isDependable) {
        this.isDependable = isDependable;
    }

    public Set<SubEntityDataBean> getSubEntityDataBean() {
        return subEntityDataBean;
    }

    public void setSubEntityDataBean(Set<SubEntityDataBean> subEntityDataBean) {
        this.subEntityDataBean = subEntityDataBean;
    }

    public List<SubEntityDataBean> getSubEntityList() {
        return subEntityList;
    }

    public void setSubEntityList(List<SubEntityDataBean> subEntityList) {
        this.subEntityList = subEntityList;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDefaultSelectedValue() {
        return defaultSelectedValue;
    }

    public void setDefaultSelectedValue(String defaultSelectedValue) {
        this.defaultSelectedValue = defaultSelectedValue;
    }

    public String getDefaultMultiSelectedValue() {
        return defaultMultiSelectedValue;
    }

    public void setDefaultMultiSelectedValue(String defaultMultiSelectedValue) {
        this.defaultMultiSelectedValue = defaultMultiSelectedValue;
    }

    public Map<String, String> getDefaultMultiSelectedValueMap() {
        return defaultMultiSelectedValueMap;
    }

    public void setDefaultMultiSelectedValueMap(Map<String, String> defaultMultiSelectedValueMap) {
        this.defaultMultiSelectedValueMap = defaultMultiSelectedValueMap;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Override
    public String toString() {
        return "CustomFieldDataBean{" + "id=" + id + ", label=" + label + ", oldLabelName=" + oldLabelName + ", type=" + type + ", values=" + values + ", currencyMasterId=" + currencyMasterId + ", validationPattern=" + validationPattern + ", formulaValue=" + formulaValue + ", isNewField=" + isNewField + ", seqNo=" + seqNo + ", Status=" + Status + ", isEditable=" + isEditable + ", isDependable=" + isDependable + ", fieldType=" + fieldType + ", dbFieldName=" + dbFieldName + ", dependantValuesMap=" + dependantValuesMap + ", subEntityDataBean=" + subEntityDataBean + ", subEntityList=" + subEntityList + ", defaultSelectedValue=" + defaultSelectedValue + ", defaultMultiSelectedValue=" + defaultMultiSelectedValue + ", defaultMultiSelectedValueMap=" + defaultMultiSelectedValueMap + ", featureName=" + featureName + ", sectionName=" + sectionName + ", isPrivate=" + isPrivate + '}';
    }

  
}
