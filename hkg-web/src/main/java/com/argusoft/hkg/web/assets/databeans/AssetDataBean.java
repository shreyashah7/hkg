/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.databeans;

import com.argusoft.hkg.web.eventmanagement.databeans.EventRecipientDataBean;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author dhwani
 */
public class AssetDataBean {

    private String modelNumber;
    @NotNull(message = "not null")
    @Length(max = 100)
    private String modelName;
    private String serialNumber;
    private String parentCategory;
    private String status;
    private Long id;
    private String accessType;
    private String description;
    @NotNull
    private Boolean canProduceImages;
    private String imagePath;
    private String purchasedFrm;
    private BigInteger manufacturer;
    private Date purchaseDt;
    private Date inwardDt;
    @Length(max = 500)
    private String remarks;
    private Boolean isArchive;
    private String barcode;
    private long createdBy;
    private Date createdOn;
    private long lastModifiedBy;
    private Date lastModifiedOn;
    @NotNull
    private Long category;
//    private String startIndex;
    @NotNull
    private Boolean assetType;
    private Boolean canGenerateBarcode;
    private String purchasedBy;
    private String prefix;
    private List<FileDataBean> fileDataBeans;
    private String purchasedByName;
    private Integer units;
    private Integer remaingUnits;
    private boolean ticked = false;
    private List<String> fileList;
    private Map<String, Object> addAssetData;// for custom field data
    private Map<String, String> dbType; //for get filed wise dbtype
    private String statusString;
    private List<EventRecipientDataBean> purchaserDataBeanList;
    private String assetName;
    private String issueToVal;
    private String issueToName;
    private Date issuedOn;
    private String pattern;

    public String getIssueToName() {
        return issueToName;
    }

    public void setIssueToName(String issueToName) {
        this.issueToName = issueToName;
    }

    public String getIssueToVal() {
        return issueToVal;
    }

    public void setIssueToVal(String issueToVal) {
        this.issueToVal = issueToVal;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public List<EventRecipientDataBean> getPurchaserDataBeanList() {
        return purchaserDataBeanList;
    }

    public void setPurchaserDataBeanList(List<EventRecipientDataBean> purchaserDataBeanList) {
        this.purchaserDataBeanList = purchaserDataBeanList;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Map<String, Object> getAddAssetData() {
        return addAssetData;
    }

    public void setAddAssetData(Map<String, Object> addAssetData) {
        this.addAssetData = addAssetData;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public boolean getTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

    public Integer getRemaingUnits() {
        return remaingUnits;
    }

    public void setRemaingUnits(Integer remaingUnits) {
        this.remaingUnits = remaingUnits;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public String getPurchasedByName() {
        return purchasedByName;
    }

    public void setPurchasedByName(String purchasedByName) {
        this.purchasedByName = purchasedByName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public Boolean isAssetType() {
        return assetType;
    }

    public void setAssetType(Boolean assetType) {
        this.assetType = assetType;
    }

    public Boolean isCanGenerateBarcode() {
        return canGenerateBarcode;
    }

    public void setCanGenerateBarcode(Boolean canGenerateBarcode) {
        this.canGenerateBarcode = canGenerateBarcode;
    }

//    public String getStartIndex() {
//        return startIndex;
//    }
//
//    public void setStartIndex(String startIndex) {
//        this.startIndex = startIndex;
//    }
    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCanProduceImages() {
        return canProduceImages;
    }

    public void setCanProduceImages(Boolean canProduceImages) {
        this.canProduceImages = canProduceImages;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPurchasedFrm() {
        return purchasedFrm;
    }

    public void setPurchasedFrm(String purchasedFrm) {
        this.purchasedFrm = purchasedFrm;
    }

    public BigInteger getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(BigInteger manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getPurchaseDt() {
        return purchaseDt;
    }

    public void setPurchaseDt(Date purchaseDt) {
        this.purchaseDt = purchaseDt;
    }

    public Date getInwardDt() {
        return inwardDt;
    }

    public void setInwardDt(Date inwardDt) {
        this.inwardDt = inwardDt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public List<FileDataBean> getFileDataBeans() {
        return fileDataBeans;
    }

    public void setFileDataBeans(List<FileDataBean> fileDataBeans) {
        this.fileDataBeans = fileDataBeans;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
