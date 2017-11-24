/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author dhwani
 */
public class IssueAssetDataBean {

    private String parentName;
    @NotNull
    private Long categoryId;
    private String assetName;
    @NotNull
    private Date issuedOn;
    @NotNull
    private String issueTo;
    private String issueToType;
    @Length(max = 500)
    private String remarks;
    private List<AssetDataBean> assetDataBeanList;
    private List<AssetDataBean> nonManagedAssetDataBeans;
    private Map<String, Object> issueCustomData;// for custom field data
    private Map<String, String> dbTypeForIssue; //for get filed wise dbtype

    public Map<String, Object> getIssueCustomData() {
        return issueCustomData;
    }

    public void setIssueCustomData(Map<String, Object> issueCustomData) {
        this.issueCustomData = issueCustomData;
    }

    public Map<String, String> getDbTypeForIssue() {
        return dbTypeForIssue;
    }

    public void setDbTypeForIssue(Map<String, String> dbTypeForIssue) {
        this.dbTypeForIssue = dbTypeForIssue;
    }

    public List<AssetDataBean> getAssetDataBeanList() {
        return assetDataBeanList;
    }

    public void setAssetDataBeanList(List<AssetDataBean> assetDataBeanList) {
        this.assetDataBeanList = assetDataBeanList;
    }

    public List<AssetDataBean> getNonManagedAssetDataBeans() {
        return nonManagedAssetDataBeans;
    }

    public void setNonManagedAssetDataBeans(List<AssetDataBean> nonManagedAssetDataBeans) {
        this.nonManagedAssetDataBeans = nonManagedAssetDataBeans;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(String issueTo) {
        this.issueTo = issueTo;
    }

    public String getIssueToType() {
        return issueToType;
    }

    public void setIssueToType(String issueToType) {
        this.issueToType = issueToType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

}
