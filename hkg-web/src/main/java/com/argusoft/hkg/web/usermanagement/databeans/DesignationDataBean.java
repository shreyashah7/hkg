/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author akta
 */
public class DesignationDataBean {

    private Long id;
    @NotNull(message = "Designation name not entered..")
    @Size(max = 100, message = "Designation name length exceeded 100..")
    private String displayName;
    private List<Long> sysFeatureIdList;
    private Map<Long, List<StaticServiceFieldPermissionDataBean>> staticServicesMap;
    private int precedence;
    private Integer existingUserCount;
    private Long associatedDepartment;
    private String departmentName;
    private Long parentDesignation;
    private List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans;
    private List<GoalPermissionDataBean> goalPermissions;
    private List<GoalPermissionDataBean> goalSheetPermissions;
    private List<RoleFeatureModifierDataBean> roleFeatureModifiers;

    // TODO: Add field number of active employee with given designation, as it will required in suggestion of search box in page
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Long> getSysFeatureIdList() {
        return sysFeatureIdList;
    }

    public void setSysFeatureIdList(List<Long> sysFeatureIdList) {
        this.sysFeatureIdList = sysFeatureIdList;
    }

    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    public Integer getExistingUserCount() {
        return existingUserCount;
    }

    public void setExistingUserCount(Integer existingUserCount) {
        this.existingUserCount = existingUserCount;
    }

    public Map<Long, List<StaticServiceFieldPermissionDataBean>> getStaticServicesMap() {
        return staticServicesMap;
    }

    public void setStaticServicesMap(Map<Long, List<StaticServiceFieldPermissionDataBean>> staticServicesMap) {
        this.staticServicesMap = staticServicesMap;
    }

    public List<GoalPermissionDataBean> getGoalPermissions() {
        return goalPermissions;
    }

    public void setGoalPermissions(List<GoalPermissionDataBean> goalPermissions) {
        this.goalPermissions = goalPermissions;
    }

    public List<GoalPermissionDataBean> getGoalSheetPermissions() {
        return goalSheetPermissions;
    }

    public void setGoalSheetPermissions(List<GoalPermissionDataBean> goalSheetPermissions) {
        this.goalSheetPermissions = goalSheetPermissions;
    }

    public Long getAssociatedDepartment() {
        return associatedDepartment;
    }

    public void setAssociatedDepartment(Long associatedDepartment) {
        this.associatedDepartment = associatedDepartment;
    }

    public Long getParentDesignation() {
        return parentDesignation;
    }

    public void setParentDesignation(Long parentDesignation) {
        this.parentDesignation = parentDesignation;
    }

    public List<FeatureFieldPermissionDataBean> getFeatureFieldPermissionDataBeans() {
        return featureFieldPermissionDataBeans;
    }

    public void setFeatureFieldPermissionDataBeans(List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans) {
        this.featureFieldPermissionDataBeans = featureFieldPermissionDataBeans;
    }

    public List<RoleFeatureModifierDataBean> getRoleFeatureModifiers() {
        return roleFeatureModifiers;
    }

    public void setRoleFeatureModifiers(List<RoleFeatureModifierDataBean> roleFeatureModifiers) {
        this.roleFeatureModifiers = roleFeatureModifiers;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DesignationDataBean{" + "id=" + id + ", displayName=" + displayName + ", sysFeatureIdList=" + sysFeatureIdList + ", staticServicesMap=" + staticServicesMap + ", precedence=" + precedence + ", existingUserCount=" + existingUserCount + ", associatedDepartment=" + associatedDepartment + ", parentDesignation=" + parentDesignation + ", goalPermissions=" + goalPermissions + ", goalSheetPermissions=" + goalSheetPermissions + '}';
    }

}
