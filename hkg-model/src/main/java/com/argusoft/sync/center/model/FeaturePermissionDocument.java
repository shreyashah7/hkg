/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.List;

/**
 *
 * @author shruti
 */
public class FeaturePermissionDocument {
    private Long id;
    private String name;
    private String description;
    private Long featureId;
    private String selectedAttributes;
    private List<SyncCenterFeatureDocument> attributes;
    private Boolean isActive;
    private boolean isEditable;
    private Long company;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the featureId
     */
    public Long getFeatureId() {
        return featureId;
    }

    /**
     * @param featureId the featureId to set
     */
    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    /**
     * @return the selectedAttributes
     */
    public String getSelectedAttributes() {
        return selectedAttributes;
    }

    /**
     * @param selectedAttributes the selectedAttributes to set
     */
    public void setSelectedAttributes(String selectedAttributes) {
        this.selectedAttributes = selectedAttributes;
    }

    /**
     * @return the attributes
     */
    public List<SyncCenterFeatureDocument> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<SyncCenterFeatureDocument> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isEditable
     */
    public boolean isIsEditable() {
        return isEditable;
    }

    /**
     * @param isEditable the isEditable to set
     */
    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * @return the company
     */
    public Long getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Long company) {
        this.company = company;
    }
}
