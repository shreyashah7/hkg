/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.databeans;

import java.util.Date;

/**
 *
 * @author dhwani
 */
public class CurrencyMasterDataBean {

    private Long id;
    private String code;
    private String symbol;
    private String symbolPosition;
    private String format;
    private Double lastReferenceRate;
    private boolean isActive;
    private boolean isArchive;
    private Date createdOn;
    private long createdBy;
    private Date lastModifiedOn;
    private long lastModifiedBy;
    private Date appliledFrom;
    private String currencyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolPosition() {
        return symbolPosition;
    }

    public void setSymbolPosition(String symbolPosition) {
        this.symbolPosition = symbolPosition;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Double getLastReferenceRate() {
        return lastReferenceRate;
    }

    public void setLastReferenceRate(Double lastReferenceRate) {
        this.lastReferenceRate = lastReferenceRate;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
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

    public Date getAppliledFrom() {
        return appliledFrom;
    }

    public void setAppliledFrom(Date appliledFrom) {
        this.appliledFrom = appliledFrom;
    }

    @Override
    public String toString() {
        return "CurrencyMasterDataBean{" + "id=" + id + ", code=" + code + ", symbol=" + symbol + ", symbolPosition=" + symbolPosition + ", format=" + format + ", lastReferenceRate=" + lastReferenceRate + ", isActive=" + isActive + ", isArchive=" + isArchive + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", lastModifiedOn=" + lastModifiedOn + ", lastModifiedBy=" + lastModifiedBy + ", appliledFrom=" + appliledFrom + ", currencyName=" + currencyName + '}';
    }
    
}
