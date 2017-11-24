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
public class AssociatedDepartmentDataBean {

    private Long department;
    private Boolean isDefaultDept;
    private String medium;
    private boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long maxcountForAssociatedDept;
    private List<RuleDatabean> rules;

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

    public List<RuleDatabean> getRules() {
        return rules;
    }

    public void setRules(List<RuleDatabean> rules) {
        this.rules = rules;
    }

    public Long getMaxcountForAssociatedDept() {
        return maxcountForAssociatedDept;
    }

    public void setMaxcountForAssociatedDept(Long maxcountForAssociatedDept) {
        this.maxcountForAssociatedDept = maxcountForAssociatedDept;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return "AssociatedDepartmentDataBean{" + "department=" + department + ", isDefaultDept=" + isDefaultDept + ", medium=" + medium + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", maxcountForAssociatedDept=" + maxcountForAssociatedDept + ", rules=" + rules + '}';
    }

}
