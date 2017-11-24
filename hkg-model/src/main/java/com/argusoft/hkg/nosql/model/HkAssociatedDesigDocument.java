/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author dhwani
 */
public class HkAssociatedDesigDocument {

    private Long designation;
    private Integer level;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;

    private List<Long> skipAssociatedDepartments;

    @DBRef
    private List<HkFeatureFieldPermissionDocument> featureFieldPermissions;

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<HkFeatureFieldPermissionDocument> getFeatureFieldPermissions() {
        return featureFieldPermissions;
    }

    public void setFeatureFieldPermissions(List<HkFeatureFieldPermissionDocument> featureFieldPermissions) {
        this.featureFieldPermissions = featureFieldPermissions;
    }

    public List<Long> getSkipAssociatedDepartments() {
        return skipAssociatedDepartments;
    }

    public void setSkipAssociatedDepartments(List<Long> skipAssociatedDepartments) {
        this.skipAssociatedDepartments = skipAssociatedDepartments;
    }

    public boolean getIsArchive() {
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
}
