package com.argusoft.hkg.web.center.usermanagement.databeans;

import com.argusoft.hkg.web.usermanagement.databeans.*;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author piyush
 */
public class CenterFeatureDataBean implements Cloneable {

    private Long id;
    private String displayName;
    @NotNull
    @Length(max = 100, message = "Length of Location name should not be more that 100 characters.")
    private String featureName;
    @NotNull
    @Length(max = 100, message = "Length of Location name should not be more that 100 characters.")
    private String menuLabel;
    private String description;
    @NotNull
    private Integer precedence;
    @NotNull
    private String menuType;
    @NotNull
    @Length(max = 100, message = "Length of Feature URL should not be more that 100 characters.")
    private String featureURL;
    @NotNull
    private Integer seqNo;
    private String isCrud;
    private Long parentId;
    private String parentName;
    private String isActive;
    private List<CenterFeatureDataBean> children;
    private List<FeaturePermissionDataBean> permissionSetList;
    private FeaturePermissionDataBean bindedPermissionSet;
    private boolean isDisable;
    private String webserviceUrl;
    private String cssClass;
    private String menuCategory;

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }        

    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public void setWebserviceUrl(String webserviceUrl) {
        this.webserviceUrl = webserviceUrl;
    }

    public List<FeaturePermissionDataBean> getPermissionSetList() {
        return permissionSetList;
    }

    public void setPermissionSetList(List<FeaturePermissionDataBean> permissionSetList) {
        this.permissionSetList = permissionSetList;
    }

    public FeaturePermissionDataBean getBindedPermissionSet() {
        return bindedPermissionSet;
    }

    public void setBindedPermissionSet(FeaturePermissionDataBean bindedPermissionSet) {
        this.bindedPermissionSet = bindedPermissionSet;
    }

    public boolean getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

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

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getIsCrud() {
        return isCrud;
    }

    public void setIsCrud(String isCrud) {
        this.isCrud = isCrud;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<CenterFeatureDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<CenterFeatureDataBean> children) {
        this.children = children;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String toString() {
        return "HkFeatureDataBean{" + "id=" + id + ", displayName=" + displayName + ", featureName=" + featureName + ", menuLabel=" + menuLabel + ", description=" + description + ", precedence=" + precedence + ", menuType=" + menuType + ", featureURL=" + featureURL + ", seqNo=" + seqNo + ", isCrud=" + isCrud + ", parentId=" + parentId + ", parentName=" + parentName + ", isActive=" + isActive + ", children=" + children + ", permissionSetList=" + permissionSetList + ", bindedPermissionSet=" + bindedPermissionSet + ", isDisable=" + isDisable + ", webserviceUrl=" + webserviceUrl + ", cssClass=" + cssClass + '}';
    }

    @Override
    public CenterFeatureDataBean clone() throws CloneNotSupportedException {
        CenterFeatureDataBean hkFeatureDataBean = new CenterFeatureDataBean();

        hkFeatureDataBean.setChildren(this.children);
        hkFeatureDataBean.setDescription(this.description);
        hkFeatureDataBean.setDisplayName(this.displayName);
        hkFeatureDataBean.setFeatureName(this.featureName);
        hkFeatureDataBean.setFeatureURL(this.featureURL);
        hkFeatureDataBean.setId(this.id);
        hkFeatureDataBean.setIsActive(this.isActive);
        hkFeatureDataBean.setIsCrud(this.isCrud);
        hkFeatureDataBean.setIsDisable(this.isDisable);
        hkFeatureDataBean.setMenuLabel(this.menuLabel);
        hkFeatureDataBean.setMenuType(this.menuType);
        hkFeatureDataBean.setParentId(this.parentId);
        hkFeatureDataBean.setParentName(this.parentName);
        hkFeatureDataBean.setPrecedence(this.precedence);
        hkFeatureDataBean.setSeqNo(this.seqNo);
        hkFeatureDataBean.setWebserviceUrl(this.webserviceUrl);
        hkFeatureDataBean.setPermissionSetList(this.permissionSetList);
        hkFeatureDataBean.setBindedPermissionSet(this.bindedPermissionSet);
        hkFeatureDataBean.setCssClass(this.cssClass);
        hkFeatureDataBean.setMenuCategory(this.menuCategory);

        return hkFeatureDataBean;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CenterFeatureDataBean other = (CenterFeatureDataBean) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
