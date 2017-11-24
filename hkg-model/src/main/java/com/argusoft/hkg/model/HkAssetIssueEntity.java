/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_asset_issue_info")
public class HkAssetIssueEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "issue_to_type", nullable = false, length = 5)
    private String issueToType;
    @Basic(optional = false)
    @Column(name = "issue_to_instance", nullable = false)
    private long issueToInstance;
    @Basic(optional = false)
    @Column(name = "issued_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedOn;
    @Column(name = "issued_units")
    private Integer issuedUnits;
    @Column(length = 500)
    private String remarks;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
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
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @JoinColumn(name = "asset", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkAssetEntity asset;
    private transient Map<String, Object> issueCustomData;// for custom field data
    private transient Map<String, String> dbTypeForIssue; //for get filed wise dbtype

    public HkAssetIssueEntity() {
    }

    public HkAssetIssueEntity(Long id) {
        this.id = id;
    }

    public HkAssetIssueEntity(Long id, String issueToType, long issueToInstance, Date issuedOn, String status, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, boolean isArchive, long franchise) {
        this.id = id;
        this.issueToType = issueToType;
        this.issueToInstance = issueToInstance;
        this.issuedOn = issuedOn;
        this.status = status;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.isArchive = isArchive;
        this.franchise = franchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueToType() {
        return issueToType;
    }

    public void setIssueToType(String issueToType) {
        this.issueToType = issueToType;
    }

    public long getIssueToInstance() {
        return issueToInstance;
    }

    public void setIssueToInstance(long issueToInstance) {
        this.issueToInstance = issueToInstance;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    public Integer getIssuedUnits() {
        return issuedUnits;
    }

    public void setIssuedUnits(Integer issuedUnits) {
        this.issuedUnits = issuedUnits;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public HkAssetEntity getAsset() {
        return asset;
    }

    public void setAsset(HkAssetEntity asset) {
        this.asset = asset;
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
        if (!(object instanceof HkAssetIssueEntity)) {
            return false;
        }
        HkAssetIssueEntity other = (HkAssetIssueEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetIssueEntity[ id=" + id + " ]";
    }

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
}
