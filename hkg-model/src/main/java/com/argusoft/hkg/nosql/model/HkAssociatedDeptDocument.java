/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author dhwani
 */
public class HkAssociatedDeptDocument {

    private Long department;
    private Boolean isDefaultDept;
    private String medium;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;

    List<HkCriteriaSetDocument> criteriaSetDocumentList;

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Boolean getIsDefaultDept() {
        return isDefaultDept;
    }

    public void setIsDefaultDept(Boolean isDefaultDept) {
        this.isDefaultDept = isDefaultDept;
    }

    public List<HkCriteriaSetDocument> getCriteriaSetDocumentList() {
        return criteriaSetDocumentList;
    }

    public void setCriteriaSetDocumentList(List<HkCriteriaSetDocument> criteriaSetDocumentList) {
        this.criteriaSetDocumentList = criteriaSetDocumentList;
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

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
    
}
