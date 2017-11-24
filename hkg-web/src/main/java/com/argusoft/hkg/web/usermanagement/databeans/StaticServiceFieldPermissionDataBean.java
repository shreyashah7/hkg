/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author shreya
 */
public class StaticServiceFieldPermissionDataBean {

    private Long designation;
    private Long feature;
    private Boolean searchFlag;
    private Boolean parentViewFlag;
    private Boolean readonlyFlag;
    private Boolean editableFlag;
    private Integer sequenceNo;
    private Long roleFeature;
    private Long field;
    private Long actualFeatureId;
    private String actualFeatureName;
    private String fieldName;
    private String entity;
    private String dbFieldName;
    private String sectionCode;
    private Boolean isRequired;

    public StaticServiceFieldPermissionDataBean(Long designation, Long feature, Boolean searchFlag, Boolean parentViewFlag, Boolean readonlyFlag, Boolean editableFlag, Integer sequenceNo, Long roleFeature, Long field, Long actualFeatureId, String actualFeatureName, String fieldName, String entity) {
        this.designation = designation;
        this.feature = feature;
        this.searchFlag = searchFlag;
        this.parentViewFlag = parentViewFlag;
        this.readonlyFlag = readonlyFlag;
        this.editableFlag = editableFlag;
        this.sequenceNo = sequenceNo;
        this.roleFeature = roleFeature;
        this.field = field;
        this.actualFeatureId = actualFeatureId;
        this.actualFeatureName = actualFeatureName;
        this.fieldName = fieldName;
        this.entity = entity;
    }

    public StaticServiceFieldPermissionDataBean() {
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public Boolean getSearchFlag() {
        return searchFlag;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public void setSearchFlag(Boolean searchFlag) {
        this.searchFlag = searchFlag;
    }

    public Boolean getParentViewFlag() {
        return parentViewFlag;
    }

    public void setParentViewFlag(Boolean parentViewFlag) {
        this.parentViewFlag = parentViewFlag;
    }

    public Boolean getReadonlyFlag() {
        return readonlyFlag;
    }

    public void setReadonlyFlag(Boolean readonlyFlag) {
        this.readonlyFlag = readonlyFlag;
    }

    public Boolean getEditableFlag() {
        return editableFlag;
    }

    public void setEditableFlag(Boolean editableFlag) {
        this.editableFlag = editableFlag;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Long getRoleFeature() {
        return roleFeature;
    }

    public void setRoleFeature(Long roleFeature) {
        this.roleFeature = roleFeature;
    }

    public Long getField() {
        return field;
    }

    public void setField(Long field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Long getActualFeatureId() {
        return actualFeatureId;
    }

    public void setActualFeatureId(Long actualFeatureId) {
        this.actualFeatureId = actualFeatureId;
    }

    public String getActualFeatureName() {
        return actualFeatureName;
    }

    public void setActualFeatureName(String actualFeatureName) {
        this.actualFeatureName = actualFeatureName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    @Override
    public String toString() {
        return "StaticServiceFieldPermissionDataBean{" + "designation=" + designation + ", feature=" + feature + ", searchFlag=" + searchFlag + ", parentViewFlag=" + parentViewFlag + ", readonlyFlag=" + readonlyFlag + ", editableFlag=" + editableFlag + ", sequenceNo=" + sequenceNo + ", roleFeature=" + roleFeature + ", field=" + field + ", actualFeatureId=" + actualFeatureId + ", actualFeatureName=" + actualFeatureName + ", fieldName=" + fieldName + ", entity=" + entity + ", dbFieldName=" + dbFieldName + ", sectionCode=" + sectionCode + ", isRequired=" + isRequired + '}';
    }

}
