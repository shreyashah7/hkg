/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "referencerate")
public class HkReferenceRateDocument  {

    @Id
    private Long id;
    private String code;
    private Date applicableFrom;
    private boolean isActive;
    private boolean isArchive;
    private long lastModifiedBy;
    private Date lastModifiedOn;
    private double referenceRate;
    private long franchise;

    public HkReferenceRateDocument() {
    }

    public HkReferenceRateDocument(Long id) {
        this.id = id;
    }

    public HkReferenceRateDocument(Long id, Date applicableFrom, boolean isActive, boolean isArchive, long lastModifiedBy, Date lastModifiedOn, long franchise) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkReferenceRateDocument)) {
            return false;
        }
        HkReferenceRateDocument other = (HkReferenceRateDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HkReferenceRateDocument{" + "id=" + id + '}';
    }

}
