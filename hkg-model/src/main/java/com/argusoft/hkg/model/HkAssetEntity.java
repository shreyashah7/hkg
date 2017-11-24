/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_system_asset_info")
public class HkAssetEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "asset_type", nullable = false, length = 5)
    private String assetType;
    @Basic(optional = false)
    @Column(name = "asset_name", nullable = false, length = 100)
    private String assetName;
    @Column(name = "serial_number")
    private Integer serialNumber;
    @Column(name = "model_number", length = 100)
    private String modelNumber;
    private BigInteger manufacturer;
    @Column(name = "purchase_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDt;
    @Column(name = "inward_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inwardDt;  
    @Column(name = "remaining_units")
    private Integer remainingUnits;
    @Column(length = 500)
    private String remarks;
    @Basic(optional = false)
    @Column(name = "can_produce_images", nullable = false)
    private boolean canProduceImages;
    @Column(name = "image_path", length = 500)
    private String imagePath;
    @Column(length = 200)
    private String barcode;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkAssetEntity")
    private Set<HkAssetDocumentEntity> hkAssetDocumentEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asset")
    private Set<HkAssetIssueEntity> hkAssetIssueEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkAssetEntity")
    private Set<HkAssetPurchaserEntity> hkAssetPurchaserEntitySet;
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkCategoryEntity category;
    @JoinColumn(name = "asset_last_issued", referencedColumnName = "id")
    @OneToOne
    private HkAssetIssueEntity assetLastIssued;

    public HkAssetEntity() {
    }

    public HkAssetEntity(Long id) {
        this.id = id;
    }

    public HkAssetEntity(Long id, String assetType, String assetName, boolean canProduceImages, String status, boolean isArchive, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, long franchise) {
        this.id = id;
        this.assetType = assetType;
        this.assetName = assetName;
        this.canProduceImages = canProduceImages;
        this.status = status;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.franchise = franchise;
    }

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

    public boolean getCanProduceImages() {
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

    public boolean getIsArchive() {
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

    public Set<HkAssetDocumentEntity> getHkAssetDocumentEntitySet() {
        return hkAssetDocumentEntitySet;
    }

    public void setHkAssetDocumentEntitySet(Set<HkAssetDocumentEntity> hkAssetDocumentEntitySet) {
        this.hkAssetDocumentEntitySet = hkAssetDocumentEntitySet;
    }

    public Set<HkAssetIssueEntity> getHkAssetIssueEntitySet() {
        return hkAssetIssueEntitySet;
    }

    public void setHkAssetIssueEntitySet(Set<HkAssetIssueEntity> hkAssetIssueEntitySet) {
        this.hkAssetIssueEntitySet = hkAssetIssueEntitySet;
    }

    public Set<HkAssetPurchaserEntity> getHkAssetPurchaserEntitySet() {
        return hkAssetPurchaserEntitySet;
    }

    public void setHkAssetPurchaserEntitySet(Set<HkAssetPurchaserEntity> hkAssetPurchaserEntitySet) {
        this.hkAssetPurchaserEntitySet = hkAssetPurchaserEntitySet;
    }   

    public HkCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(HkCategoryEntity category) {
        this.category = category;
    }

    public HkAssetIssueEntity getAssetLastIssued() {
        return assetLastIssued;
    }

    public void setAssetLastIssued(HkAssetIssueEntity assetLastIssued) {
        this.assetLastIssued = assetLastIssued;
    }        

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkAssetEntity)) {
            return false;
        }
        HkAssetEntity other = (HkAssetEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetEntity[ id=" + id + " ]";
    }

    }
