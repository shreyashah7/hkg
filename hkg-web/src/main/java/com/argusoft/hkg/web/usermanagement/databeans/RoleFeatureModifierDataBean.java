/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gautam
 */
public class RoleFeatureModifierDataBean {

    private String id;
    private Long feature;
    private Long designation;
    private Long franchise;
    private Boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    //add more properties as per your modifiers
    // Issue Receive modifiers
    private List<String> iRMediums;
    private List<String> iRTypes;
    private List<String> iRModes;
    // Allotment modifiers
    private List<Long> asDesignation;
    private Map<String, String> asDesignationIdName;
    //issue receive via stock room access rights
    private List<String> iRVSRAccessRights;

    // Issue Receive modifiers end
    private List<String> planAccess;
    List<Map<String, String>> showPlanUsers;
    Map<String, String> recepientCodeToName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
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

    public List<String> getiRMediums() {
        return iRMediums;
    }

    public void setiRMediums(List<String> iRMediums) {
        this.iRMediums = iRMediums;
    }

    public List<String> getiRTypes() {
        return iRTypes;
    }

    public void setiRTypes(List<String> iRTypes) {
        this.iRTypes = iRTypes;
    }

    public List<String> getiRModes() {
        return iRModes;
    }

    public void setiRModes(List<String> iRModes) {
        this.iRModes = iRModes;
    }

    public List<String> getiRVSRAccessRights() {
        return iRVSRAccessRights;
    }

    public void setiRVSRAccessRights(List<String> iRVSRAccessRights) {
        this.iRVSRAccessRights = iRVSRAccessRights;
    }

    public List<Long> getAsDesignation() {
        return asDesignation;
    }

    public void setAsDesignation(List<Long> asDesignation) {
        this.asDesignation = asDesignation;
    }

    public Map<String, String> getAsDesignationIdName() {
        return asDesignationIdName;
    }

    public void setAsDesignationIdName(Map<String, String> asDesignationIdName) {
        this.asDesignationIdName = asDesignationIdName;
    }

    public List<String> getPlanAccess() {
        return planAccess;
    }

    public void setPlanAccess(List<String> planAccess) {
        this.planAccess = planAccess;
    }

    public List<Map<String, String>> getShowPlanUsers() {
        return showPlanUsers;
    }

    public void setShowPlanUsers(List<Map<String, String>> showPlanUsers) {
        this.showPlanUsers = showPlanUsers;
    }

    public Map<String, String> getRecepientCodeToName() {
        return recepientCodeToName;
    }

    public void setRecepientCodeToName(Map<String, String> recepientCodeToName) {
        this.recepientCodeToName = recepientCodeToName;
    }

    @Override
    public String toString() {
        return "RoleFeatureModifierDataBean{" + "id=" + id + ", feature=" + feature + ", designation=" + designation + ", franchise=" + franchise + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", iRMediums=" + iRMediums + ", iRTypes=" + iRTypes + ", iRModes=" + iRModes + ", asDesignation=" + asDesignation + ", asDesignationIdName=" + asDesignationIdName + ", iRVSRAccessRights=" + iRVSRAccessRights + ", planAccess=" + planAccess + ", showPlanUsers=" + showPlanUsers + ", recepientCodeToName=" + recepientCodeToName + '}';
    }

}
