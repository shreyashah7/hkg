/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;

/**
 *
 * @author mmodi
 */
public class HkPriceListDetailDocument {

    private String id;
    private Long priceList;
    private Integer sequenceNumber;
    private Long color;
    private Long clarity;
    private Long cut;
    private Long caratRange;
    private Long fluorescence;
    private Double originalPrice;
    private Float discount;
    private Double hkgPrice;
    private Boolean isArchive;
    private Date lastModifiedOn;

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getColor() {
        return color;
    }

    public void setColor(Long color) {
        this.color = color;
    }

    public Long getClarity() {
        return clarity;
    }

    public void setClarity(Long clarity) {
        this.clarity = clarity;
    }

    public Long getCut() {
        return cut;
    }

    public void setCut(Long cut) {
        this.cut = cut;
    }

    public Long getCaratRange() {
        return caratRange;
    }

    public void setCaratRange(Long caratRange) {
        this.caratRange = caratRange;
    }

    public Long getFluorescence() {
        return fluorescence;
    }

    public void setFluorescence(Long fluorescence) {
        this.fluorescence = fluorescence;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Double getHkgPrice() {
        return hkgPrice;
    }

    public void setHkgPrice(Double hkgPrice) {
        this.hkgPrice = hkgPrice;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getPriceList() {
        return priceList;
    }

    public void setPriceList(Long priceList) {
        this.priceList = priceList;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "HkPriceListDetailDocument{" + "id=" + id + ", priceList=" + priceList + ", sequenceNumber=" + sequenceNumber + ", color=" + color + ", clarity=" + clarity + ", cut=" + cut + ", caratRange=" + caratRange + ", fluorescence=" + fluorescence + ", originalPrice=" + originalPrice + ", discount=" + discount + ", hkgPrice=" + hkgPrice + ", isArchive=" + isArchive + ", lastModifiedOn=" + lastModifiedOn + '}';
    }

    @Override
    public HkPriceListDetailDocument clone() {
        HkPriceListDetailDocument priceListDetailDocument = new HkPriceListDetailDocument();
        priceListDetailDocument.setCaratRange(this.caratRange);
        priceListDetailDocument.setClarity(this.clarity);
        priceListDetailDocument.setColor(this.color);
        priceListDetailDocument.setCut(this.cut);
        priceListDetailDocument.setDiscount(this.discount);
        priceListDetailDocument.setFluorescence(this.fluorescence);
        priceListDetailDocument.setHkgPrice(this.hkgPrice);
        priceListDetailDocument.setId(this.id);
        priceListDetailDocument.setIsArchive(this.isArchive);
        priceListDetailDocument.setLastModifiedOn(this.lastModifiedOn);
        priceListDetailDocument.setOriginalPrice(this.originalPrice);
        priceListDetailDocument.setPriceList(this.priceList);
        priceListDetailDocument.setSequenceNumber(this.sequenceNumber);
        return priceListDetailDocument;
    }
}
