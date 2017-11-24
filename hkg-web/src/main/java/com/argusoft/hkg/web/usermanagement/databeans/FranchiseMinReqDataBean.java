/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author ravi
 */
public class FranchiseMinReqDataBean {
    
    private Long id;
    private Long reqId;    
    private Long franchise;
    private String requirementName;
    private String requirementType;
    private Integer requiredValue;
    private Integer acquiredValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }    

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }    

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    public Integer getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(Integer requiredValue) {
        this.requiredValue = requiredValue;
    }

    public Integer getAcquiredValue() {
        return acquiredValue;
    }

    public void setAcquiredValue(Integer acquiredValue) {
        this.acquiredValue = acquiredValue;
    }

    @Override
    public String toString() {
        return "FranchiseMinReqDataBean{" + "id=" + id + ", reqId=" + reqId + ", franchise=" + franchise + ", requirementName=" + requirementName + ", requirementType=" + requirementType + ", requiredValue=" + requiredValue + ", acquiredValue=" + acquiredValue + '}';
    }

}
