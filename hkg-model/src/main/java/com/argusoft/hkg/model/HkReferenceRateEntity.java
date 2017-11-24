/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dhwani
 */
@Entity
@Table(name = "hk_reference_rate_info")
public class HkReferenceRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic
    @Column(name = "code", nullable = true, length = 255)
    private String code;
    @Basic(optional = false)
    @Column(name = "applicable_from", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date applicableFrom;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(name = "reference_rate", nullable = false)
    private double referenceRate;
    @Basic(optional = false)
    @Column(name = "franchise")
    private long franchise;    
    @Basic(optional = false)
    @Column(name = "created_by_franchise")
    private Long createdByFranchise;

    public HkReferenceRateEntity() {
    }

    public HkReferenceRateEntity(Long id) {
        this.id = id;
    }

    public HkReferenceRateEntity(Long id, Date applicableFrom, boolean isActive, boolean isArchive, long lastModifiedBy, Date lastModifiedOn, long franchise) {
        this.id = id;
        this.applicableFrom = applicableFrom;
        this.isActive = isActive;
        this.isArchive = isArchive;
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

    public double getReferenceRate() {
        return referenceRate;
    }

    public void setReferenceRate(double referenceRate) {
        this.referenceRate = referenceRate;
    }

    public Date getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(Date applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCreatedByFranchise() {
        return createdByFranchise;
    }

    public void setCreatedByFranchise(Long createdByFranchise) {
        this.createdByFranchise = createdByFranchise;
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
        if (!(object instanceof HkReferenceRateEntity)) {
            return false;
        }
        HkReferenceRateEntity other = (HkReferenceRateEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkCurrencyRateEntity[ id=" + id + " ]";
    }

}
