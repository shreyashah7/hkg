/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.databeans;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author dhwani
 */
public class CategoryDataBean {

    private Long id;
    @NotNull(message = "Category name not entered")
    @Length(max = 100, message = "Category name length exceeded 100")
    private String displayName;
    @Length(max = 500, message = "Category description length exceeded 500")
    private String description;
    private Long parentId;
    private String parentName;
    private Long companyId;
    private Boolean editable;
    private List<CategoryDataBean> children;
    private Boolean isActive;
    private Boolean haveDiamondProcessingMch;
    private String status;
    private String categoryPrefix;
    private String startIndex;
    private String type;
    private String selected;
    private Integer assetNumber;
    private Integer categoryCount;
    private Map<String, Object> categoryCustomData;// for custom field data
    private Map<String, String> dbTypeForCategory; //for get filed wise dbtype
    private String originalName;
    private String pattern;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Integer assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
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

    public List<CategoryDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryDataBean> children) {
        this.children = children;
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

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean isHaveDiamondProcessingMch() {
        return haveDiamondProcessingMch;
    }

    public void setHaveDiamondProcessingMch(Boolean haveDiamondProcessingMch) {
        this.haveDiamondProcessingMch = haveDiamondProcessingMch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryPrefix() {
        return categoryPrefix;
    }

    public void setCategoryPrefix(String categoryPrefix) {
        this.categoryPrefix = categoryPrefix;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }
 
    public Integer getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }

    public Map<String, Object> getCategoryCustomData() {
        return categoryCustomData;
    }

    public void setCategoryCustomData(Map<String, Object> categoryCustomData) {
        this.categoryCustomData = categoryCustomData;
    }

    public Map<String, String> getDbTypeForCategory() {
        return dbTypeForCategory;
    }

    public void setDbTypeForCategory(Map<String, String> dbTypeForCategory) {
        this.dbTypeForCategory = dbTypeForCategory;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public String toString() {
        return "HkCategoryDataBean{" + "id=" + id + ", displayName=" + displayName + ", description=" + description + ", parentId=" + parentId + ", parentName=" + parentName + ", companyId=" + companyId + ", children=" + children + '}';
    }

}
