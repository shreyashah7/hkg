/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class MetadataDatabean {

    private String id;
    private String className;
    private Map<String, Object> idMap;
    private Date createdOn;
    private Date modifiedOn;
    private String status;
    private boolean isArchive;
    private boolean isSqlEntity;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the idMap
     */
    public Map<String, Object> getIdMap() {
        return idMap;
    }

    /**
     * @param idMap the idMap to set
     */
    public void setIdMap(Map<String, Object> idMap) {
        this.idMap = idMap;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the isArchive
     */
    public boolean isIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    /**
     * @return the isSqlEntity
     */
    public boolean isIsSqlEntity() {
        return isSqlEntity;
    }

    /**
     * @param isSqlEntity the isSqlEntity to set
     */
    public void setIsSqlEntity(boolean isSqlEntity) {
        this.isSqlEntity = isSqlEntity;
    }
}
