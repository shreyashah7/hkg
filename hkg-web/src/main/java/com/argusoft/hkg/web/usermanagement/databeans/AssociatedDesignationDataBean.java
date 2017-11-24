/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shifa
 */
public class AssociatedDesignationDataBean {
   private Long designation;
    private Integer level;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;

    private List<Long> skipAssociatedDepartments;
private List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans;
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

    public List<Long> getSkipAssociatedDepartments() {
        return skipAssociatedDepartments;
    }

    public void setSkipAssociatedDepartments(List<Long> skipAssociatedDepartments) {
        this.skipAssociatedDepartments = skipAssociatedDepartments;
    }

    public List<FeatureFieldPermissionDataBean> getFeatureFieldPermissionDataBeans() {
        return featureFieldPermissionDataBeans;
    }

    public void setFeatureFieldPermissionDataBeans(List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans) {
        this.featureFieldPermissionDataBeans = featureFieldPermissionDataBeans;
    }

    @Override
    public String toString() {
        return "AssociatedDesignationDataBean{" + "designation=" + designation + ", level=" + level + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", skipAssociatedDepartments=" + skipAssociatedDepartments + ", featureFieldPermissionDataBeans=" + featureFieldPermissionDataBeans + '}';
    }

   
    
}
