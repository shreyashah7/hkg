/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.databeans;

import java.util.Date;

/**
 *
 * @author jyoti
 */
public class CurrencyRateMasterDataBean {

    private Long id;
    private Date applicableFrom;
    private boolean isActive;
    private boolean isArchive;
    private Date lastModifiedOn;
    private long lastModifiedBy;
    private Double referenceRate;
    private String code;
    private Double prevReferenceRate;
    private Long franchise;
    private String currencyName;
    private Long currencyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(Date applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Double getReferenceRate() {
        return referenceRate;
    }

    public void setReferenceRate(Double referenceRate) {
        this.referenceRate = referenceRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrevReferenceRate() {
        return prevReferenceRate;
    }

    public void setPrevReferenceRate(Double prevReferenceRate) {
        this.prevReferenceRate = prevReferenceRate;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "CurrencyRateMasterDataBean{" + "id=" + id + ", applicableFrom=" + applicableFrom + ", isActive=" + isActive + ", isArchive=" + isArchive + ", lastModifiedOn=" + lastModifiedOn + ", lastModifiedBy=" + lastModifiedBy + ", referenceRate=" + referenceRate + ", code=" + code + ", prevReferenceRate=" + prevReferenceRate + ", franchise=" + franchise + ", currencyName=" + currencyName + ", currencyId=" + currencyId + '}';
    }
}
