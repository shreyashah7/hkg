/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import java.util.Map;

/**
 *
 * @author shifa
 */
public class SubEntityValueDataBean {
    
    private String id;
    private Map<String, Object> dbMap;// for custom field data
    private Map<String, String> dbType; //for get filed wise dbtype
    private Long instanceId;
    private String tempId; // temporary Id
private Boolean isArchive;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getDbMap() {
        return dbMap;
    }

    public void setDbMap(Map<String, Object> dbMap) {
        this.dbMap = dbMap;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    @Override
    public String toString() {
        return "SubEntityValueDataBean{" + "id=" + id + ", dbMap=" + dbMap + ", dbType=" + dbType + ", instanceId=" + instanceId + ", tempId=" + tempId + ", isArchive=" + isArchive + '}';
    }

    
    
}
