/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.List;

/**
 *
 * @author piyush
 */
public class FeaturePermissionDataBean {

    private Long id;
    private String name;
    private String description;
    private Long featureId;
    private String selectedAttributes;
    private List<FeatureDataBean> attributes;
    private Boolean isActive;
    private boolean isEditable;
    private Long company;

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public boolean isIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public FeaturePermissionDataBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public List<FeatureDataBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<FeatureDataBean> attributes) {
        this.attributes = attributes;
    }

    public String getSelectedAttributes() {
        return selectedAttributes;
    }

    public void setSelectedAttributes(String selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "FeaturePermissionDataBean{" + "id=" + id + ", name=" + name + ", description=" + description + ", featureId=" + featureId + ", selectedAttributes=" + selectedAttributes + ", attributes=" + attributes + ", isActive=" + isActive + ", isEditable=" + isEditable + ", company=" + company + '}';
    }
}
