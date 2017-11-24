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
public class ConfigurationDataBean {

    private String configId;
    private Long department;
    private String departmentName;
    private Long stockRoom;
    private String stockDeptName;
    private Long id;
    private Long defaultDepartment;
    private String flowMode;
    private String status;
    private Long franchise;
    private Boolean noPhysicalDiamonds;
    private Boolean isArchive;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private List<RuleDatabean> ruleList;
    private List<AssociatedDesignationDataBean> associatedDesignations;
    private List<AssociatedDepartmentDataBean> associatedDepartments;
    private List<RoleFeatureModifierDataBean> roleFeatureModifiers;
    private int numberOfAssociatedDepartments;
    private int numberOfAssociatedDesignation;
    private Long maxCountAchievedForRule;

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getStockRoom() {
        return stockRoom;
    }

    public void setStockRoom(Long stockRoom) {
        this.stockRoom = stockRoom;
    }

    public Long getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(Long defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public String getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(String flowMode) {
        this.flowMode = flowMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockDeptName() {
        return stockDeptName;
    }

    public void setStockDeptName(String stockDeptName) {
        this.stockDeptName = stockDeptName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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

    public List<RuleDatabean> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<RuleDatabean> ruleList) {
        this.ruleList = ruleList;
    }


    public List<AssociatedDesignationDataBean> getAssociatedDesignations() {
        return associatedDesignations;
    }

    public void setAssociatedDesignations(List<AssociatedDesignationDataBean> associatedDesignations) {
        this.associatedDesignations = associatedDesignations;
    }

    public List<AssociatedDepartmentDataBean> getAssociatedDepartments() {
        return associatedDepartments;
    }

    public void setAssociatedDepartments(List<AssociatedDepartmentDataBean> associatedDepartments) {
        this.associatedDepartments = associatedDepartments;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    

    public int getNumberOfAssociatedDepartments() {
        return numberOfAssociatedDepartments;
    }

    public void setNumberOfAssociatedDepartments(int numberOfAssociatedDepartments) {
        this.numberOfAssociatedDepartments = numberOfAssociatedDepartments;
    }

    public int getNumberOfAssociatedDesignation() {
        return numberOfAssociatedDesignation;
    }

    public void setNumberOfAssociatedDesignation(int numberOfAssociatedDesignation) {
        this.numberOfAssociatedDesignation = numberOfAssociatedDesignation;
    }

    public Long getMaxCountAchievedForRule() {
        return maxCountAchievedForRule;
    }

    public void setMaxCountAchievedForRule(Long maxCountAchievedForRule) {
        this.maxCountAchievedForRule = maxCountAchievedForRule;
    }

    public Boolean isNoPhysicalDiamonds() {
        return noPhysicalDiamonds;
    }

    public void setNoPhysicalDiamonds(Boolean noPhysicalDiamonds) {
        this.noPhysicalDiamonds = noPhysicalDiamonds;
    }

    public List<RoleFeatureModifierDataBean> getRoleFeatureModifiers() {
        return roleFeatureModifiers;
    }

    public void setRoleFeatureModifiers(List<RoleFeatureModifierDataBean> roleFeatureModifiers) {
        this.roleFeatureModifiers = roleFeatureModifiers;
    }

    @Override
    public String toString() {
        return "ConfigurationDataBean{" + "configId=" + configId + ", department=" + department + ", departmentName=" + departmentName + ", stockRoom=" + stockRoom + ", stockDeptName=" + stockDeptName + ", id=" + id + ", defaultDepartment=" + defaultDepartment + ", flowMode=" + flowMode + ", status=" + status + ", franchise=" + franchise + ", noPhysicalDiamonds=" + noPhysicalDiamonds + ", isArchive=" + isArchive + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", ruleList=" + ruleList + ", associatedDesignations=" + associatedDesignations + ", associatedDepartments=" + associatedDepartments + ", numberOfAssociatedDepartments=" + numberOfAssociatedDepartments + ", numberOfAssociatedDesignation=" + numberOfAssociatedDesignation + ", maxCountAchievedForRule=" + maxCountAchievedForRule + '}';
    }


}