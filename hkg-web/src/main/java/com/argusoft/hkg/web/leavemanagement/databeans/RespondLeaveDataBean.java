/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.databeans;

import java.util.Map;

/**
 *
 * @author vipul
 */
public class RespondLeaveDataBean {

    private Long id;
    private String remarks;
    private Map<String, Object> respondCustom;
    private Map<String, String> dbType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Map<String, Object> getRespondCustom() {
        return respondCustom;
    }

    public void setRespondCustom(Map<String, Object> respondCustom) {
        this.respondCustom = respondCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    
}
