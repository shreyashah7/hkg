/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.Map;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document
public class MetadataDocument {

    @Id
    private ObjectId id;
    private String className;
    private Map<String, Object> idMap;
    private Date createdOn;
    private Date modifiedOn;
    private String status;
    private boolean isArchive;
    private boolean isSqlEntity;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Object> getIdMap() {
        return idMap;
    }

    public void setIdMap(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
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

    public boolean isIsSqlEntity() {
        return isSqlEntity;
    }

    public void setIsSqlEntity(boolean isSqlEntity) {
        this.isSqlEntity = isSqlEntity;
    }

}
