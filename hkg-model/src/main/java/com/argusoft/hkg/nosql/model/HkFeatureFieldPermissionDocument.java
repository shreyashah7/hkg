/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import com.argusoft.sync.center.model.HkFieldDocument;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
    @Document(collection = "featurefieldpermission")
public class HkFeatureFieldPermissionDocument implements Cloneable{

    @Id
    String id;
    private Long roleFeature;
    private Long designation;
    private Long feature;
    private Boolean searchFlag;
    private Boolean parentViewFlag;
    private Boolean readonlyFlag;
    private Boolean editableFlag;
    private Boolean isRequired;
    private Integer sequenceNo;
    private String entityName;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long franchise;
    private Long fieldId;
    @DBRef
    private HkFieldDocument hkFieldEntity;
    private String sectionCode;

    public Long getRoleFeature() {
        return roleFeature;
    }

    public void setRoleFeature(Long roleFeature) {
        this.roleFeature = roleFeature;
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

    public Boolean getSearchFlag() {
        return searchFlag;
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

 
    public Boolean isIsRequired() {
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

    public HkFieldDocument getHkFieldEntity() {
        return hkFieldEntity;
    }

    public void setHkFieldEntity(HkFieldDocument hkFieldEntity) {
        this.hkFieldEntity = hkFieldEntity;
    }


    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }
    @Override
    public HkFeatureFieldPermissionDocument clone() throws CloneNotSupportedException {
        return (HkFeatureFieldPermissionDocument) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

   

}
