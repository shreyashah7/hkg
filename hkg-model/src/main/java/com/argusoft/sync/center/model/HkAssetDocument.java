/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author akta
 */
@Document(collection = "asset")
public class HkAssetDocument {

    @Id
    private Long id;
    private String assetType;
    private String assetName;
    private Integer serialNumber;
    private String modelNumber;
    private BigInteger manufacturer;
    private Date purchaseDt;
    private Date inwardDt;
    private Integer remainingUnits;
    private String remarks;
    private boolean canProduceImages;
    private String imagePath;
    private String barcode;
    private String status;
    private boolean isArchive;
    private long createdBy;
    private Date createdOn;
    private long lastModifiedBy;
    private Date lastModifiedOn;
    private long franchise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
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

    public Integer getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(Integer remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isCanProduceImages() {
        return canProduceImages;
    }

    public void setCanProduceImages(boolean canProduceImages) {
        this.canProduceImages = canProduceImages;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

}
