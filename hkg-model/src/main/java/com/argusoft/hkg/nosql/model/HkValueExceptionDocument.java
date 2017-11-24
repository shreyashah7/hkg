/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "valueexception")
public class HkValueExceptionDocument {
    
    @Id
    private ObjectId id;
    private List<String> forUsers;
    private List<Long> forValues;
    private Long fieldId;
    private String fieldType;
    private Long instanceId;
    private List<Long> dependsOnValueList;
    private List<ObjectId> dependsOnSubValueList;
    private boolean isArchive;
    private long franchise;
    private long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Long> getForValues() {
        return forValues;
    }

    public void setForValues(List<Long> forValues) {
        this.forValues = forValues;
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

    public List<Long> getDependsOnValueList() {
        return dependsOnValueList;
    }

    public void setDependsOnValueList(List<Long> dependsOnValueList) {
        this.dependsOnValueList = dependsOnValueList;
    }

    public List<ObjectId> getDependsOnSubValueList() {
        return dependsOnSubValueList;
    }

    public void setDependsOnSubValueList(List<ObjectId> dependsOnSubValueList) {
        this.dependsOnSubValueList = dependsOnSubValueList;
    }   

    public List<String> getForUsers() {
        return forUsers;
    }

    public void setForUsers(List<String> forUsers) {
        this.forUsers = forUsers;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
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

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "HkValueExceptionDocument{" + "id=" + id + ", forUsers=" + forUsers + ", forValues=" + forValues + ", fieldId=" + fieldId + ", fieldType=" + fieldType + ", instanceId=" + instanceId + ", dependsOnValueList=" + dependsOnValueList + ", dependsOnSubValueList=" + dependsOnSubValueList + ", isArchive=" + isArchive + ", franchise=" + franchise + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + '}';
    }
}
