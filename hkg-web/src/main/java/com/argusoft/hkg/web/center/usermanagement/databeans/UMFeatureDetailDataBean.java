/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.databeans;

import java.util.Date;

/**
 *
 * @author harshit
 */
public class UMFeatureDetailDataBean {

    private Long id;
    private String displayName;
    private String featureName;
    private String menuLabel;
    private String description;
    private Integer precedence;
    private String menuType;
    private String featureURL;
    private Integer seqNo;
    private Boolean isCrud;
    private Long parentId;
    private String parentName;
    private boolean isDisable;
    private String webserviceUrl;
    private String cssClass;
    private String menuCategory;

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

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
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

    @Override
    public String toString() {
        return "UMFeatureDetailDataBean{" + "id=" + id + ", displayName=" + displayName + ", featureName=" + featureName + ", menuLabel=" + menuLabel + ", description=" + description + ", precedence=" + precedence + ", menuType=" + menuType + ", featureURL=" + featureURL + ", seqNo=" + seqNo + ", isCrud=" + isCrud + ", parentId=" + parentId + ", parentName=" + parentName + ", isDisable=" + isDisable + ", webserviceUrl=" + webserviceUrl + ", cssClass=" + cssClass + ", menuCategory=" + menuCategory + '}';
    }

}
