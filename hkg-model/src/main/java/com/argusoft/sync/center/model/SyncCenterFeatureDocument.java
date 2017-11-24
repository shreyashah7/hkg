/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "feature")
public class SyncCenterFeatureDocument implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    @Id
    private Long id;
    private String displayName;
    private String name;
    private String menuLabel;
    private String description;
    private Integer precedence;
    private String menuType;
    private String featureURL;
    private Integer seqNo;
    private Boolean isCrud;
    private Long parentId;
    private String parentName;
    private Boolean isActive;
    private boolean isDisable;
    private String webserviceUrl;
    private String cssClass;
    private String menuCategory;
    private Date modifiedOn;
     private List<SyncCenterFeatureDocument> children;
     private List<FeaturePermissionDocument> permissionSetList;
     private FeaturePermissionDocument bindedPermissionSet;

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

    public String getName() {
        return name;
    }

    public void setName(String featureName) {
        this.name = featureName;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getFeatureURL() {
        return featureURL;
    }

    public void setFeatureURL(String featureURL) {
        this.featureURL = featureURL;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Boolean getIsCrud() {
        return isCrud;
    }

    public void setIsCrud(Boolean isCrud) {
        this.isCrud = isCrud;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public boolean isIsDisable() {
        return isDisable;
    }

    public void setIsDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public void setWebserviceUrl(String webserviceUrl) {
        this.webserviceUrl = webserviceUrl;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "FeatureDocument{" + "id=" + getId() + ", displayName=" + getDisplayName() + ", menuLabel=" + getMenuLabel() + ", description=" + getDescription() + ", precedence=" + getPrecedence() + ", menuType=" + getMenuType() + ", featureURL=" + getFeatureURL() + ", seqNo=" + getSeqNo() + ", isCrud=" + getIsCrud() + ", parentId=" + getParentId() + ", parentName=" + getParentName() + ", isActive=" + getIsActive() + ", isDisable=" + isIsDisable() + ", webserviceUrl=" + getWebserviceUrl() + ", cssClass=" + getCssClass() + ", menuCategory=" + getMenuCategory() + '}';
    }

    /**
     * @return the children
     */
    public List<SyncCenterFeatureDocument> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<SyncCenterFeatureDocument> children) {
        this.children = children;
    }

    /**
     * @return the permissionSetList
     */
    public List<FeaturePermissionDocument> getPermissionSetList() {
        return permissionSetList;
    }

    /**
     * @param permissionSetList the permissionSetList to set
     */
    public void setPermissionSetList(List<FeaturePermissionDocument> permissionSetList) {
        this.permissionSetList = permissionSetList;
    }

    /**
     * @return the bindedPermissionSet
     */
    public FeaturePermissionDocument getBindedPermissionSet() {
        return bindedPermissionSet;
    }

    /**
     * @param bindedPermissionSet the bindedPermissionSet to set
     */
    public void setBindedPermissionSet(FeaturePermissionDocument bindedPermissionSet) {
        this.bindedPermissionSet = bindedPermissionSet;
    }

}
