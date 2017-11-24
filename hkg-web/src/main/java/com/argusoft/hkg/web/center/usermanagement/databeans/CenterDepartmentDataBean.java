/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.databeans;

import com.argusoft.hkg.web.usermanagement.databeans.*;
import java.util.List;

/**
 *
 * @author jyoti
 */
public class CenterDepartmentDataBean {

    private Long id;
    private String displayName;
    private String description;
    private List<CenterDepartmentDataBean> children;
    private Long parentId;
    private String parentName;
    private Long companyId;
    private Boolean isActive;
    private String selected;
    private String deptName;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CenterDepartmentDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<CenterDepartmentDataBean> children) {
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

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "CenterDepartmentDataBean{" + "id=" + id + ", displayName=" + displayName + ", description=" + description + ", children=" + children + ", parentId=" + parentId + ", parentName=" + parentName + ", companyId=" + companyId + ", isActive=" + isActive + ", selected=" + selected + ", deptName=" + deptName + '}';
    }

}
