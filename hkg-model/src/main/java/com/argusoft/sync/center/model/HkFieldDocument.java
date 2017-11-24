/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author akta
 */
@Document(collection = "field")
public class HkFieldDocument {
    @Id
    private Long id;
    private String fieldLabel;
    private String fieldType;
    private String componentType;
    private String uiFieldName;
    private String dbFieldName;
    private boolean isCustomField;
    private Boolean isEditable;
    private Boolean isDependant;
    private String dbBaseName;
    private String dbBaseType;
    private long feature;
    private Integer associatedCurrency;
    private String status;
    private boolean isArchive;
    private Long franchise;
    private Integer seqNo;
    private long createdBy;
    private Date createdOn;
    private long lastModifiedBy;
    private Date lastModifiedOn;
    @DBRef
    private HkSectionDocument section;
    private String validationPattern;
    private String formulaValue;
    private String fieldValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
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

    public boolean isIsCustomField() {
        return isCustomField;
    }

    public void setIsCustomField(boolean isCustomField) {
        this.isCustomField = isCustomField;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Boolean getIsDependant() {
        return isDependant;
    }

    public void setIsDependant(Boolean isDependant) {
        this.isDependant = isDependant;
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

    public long getFeature() {
        return feature;
    }

    public void setFeature(long feature) {
        this.feature = feature;
    }

    public Integer getAssociatedCurrency() {
        return associatedCurrency;
    }

    public void setAssociatedCurrency(Integer associatedCurrency) {
        this.associatedCurrency = associatedCurrency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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

    public HkSectionDocument getSection() {
        return section;
    }

    public void setSection(HkSectionDocument section) {
        this.section = section;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    @Override
    public String toString() {
        return "HkFieldDocument{" + "id=" + id + '}';
    }

}
