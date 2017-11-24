/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author jyoti
 */
public class DepartmentDataBean {

    private Long id;
    @NotNull(message = "Department name should not be null")
    @Length(max = 100, message = "Length of Department name should be below 100")
    private String displayName;
    @Length(max = 500, message = "Length of Description should be below 500")
    private String description;
    private List<DepartmentDataBean> children;
    private Long parentId;
    private String parentName;
    private Long companyId;
    private Boolean isActive;
    private String selected;
    private Boolean showAllDepartment;
    private Long shiftRotationDays;
    private Map<String, Object> departmentCustom;
    private Integer existingUserCount;
    private Map<String, String> dbType;
    private String deptName;
    private Boolean isDefaultDep;
    private Boolean isConfigExists;
    private List<DesignationDataBean> designationDataBeans;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    public Long getShiftRotationDays() {
        return shiftRotationDays;
    }

    public void setShiftRotationDays(Long shiftRotationDays) {
        this.shiftRotationDays = shiftRotationDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DepartmentDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentDataBean> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isShowAllDepartment() {
        return showAllDepartment;
    }

    public void setShowAllDepartment(Boolean showAllDepartment) {
        this.showAllDepartment = showAllDepartment;
    }

    public Map<String, Object> getDepartmentCustom() {
        return departmentCustom;
    }

    public void setDepartmentCustom(Map<String, Object> departmentCustom) {
        this.departmentCustom = departmentCustom;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public Integer getExistingUserCount() {
        return existingUserCount;
    }

    public void setExistingUserCount(Integer existingUserCount) {
        this.existingUserCount = existingUserCount;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Boolean getIsDefaultDep() {
        return isDefaultDep;
    }

    public void setIsDefaultDep(Boolean isDefaultDep) {
        this.isDefaultDep = isDefaultDep;
    }

    public Boolean isIsConfigExists() {
        return isConfigExists;
    }

    public void setIsConfigExists(Boolean isConfigExists) {
        this.isConfigExists = isConfigExists;
    }

    public List<DesignationDataBean> getDesignationDataBeans() {
        return designationDataBeans;
    }

    public void setDesignationDataBeans(List<DesignationDataBean> designationDataBeans) {
        this.designationDataBeans = designationDataBeans;
    }

    @Override
    public String toString() {
        return "DepartmentDataBean{" + "id=" + id + ", displayName=" + displayName + ", description=" + description + ", children=" + children + ", parentId=" + parentId + ", parentName=" + parentName + ", companyId=" + companyId + ", isActive=" + isActive + ", selected=" + selected + ", showAllDepartment=" + showAllDepartment + ", shiftRotationDays=" + shiftRotationDays + ", departmentCustom=" + departmentCustom + ", existingUserCount=" + existingUserCount + ", dbType=" + dbType + ", deptName=" + deptName + ", isDefaultDep=" + isDefaultDep + ", isConfigExists=" + isConfigExists + '}';
    }

}
