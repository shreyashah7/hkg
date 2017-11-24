/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import java.util.List;

/**
 *
 * @author shifa
 */
public class FeatureDetailDataBean {

    private Long id;
    private String displayName;
    private String description;
    private Long parentId;
    private String parentName;
    private Long companyId;
    private List<SectionDetailDataBean> children;
    private Boolean isActive;

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

    public List<SectionDetailDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<SectionDetailDataBean> children) {
        this.children = children;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "FeatureDetailDataBean{" + "id=" + id + ", displayName=" + displayName + ", description=" + description + ", parentId=" + parentId + ", parentName=" + parentName + ", companyId=" + companyId + ", children=" + children + ", isActive=" + isActive + '}';
    }

}
