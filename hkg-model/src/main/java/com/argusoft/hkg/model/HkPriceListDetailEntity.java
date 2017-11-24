/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "hk_price_list_dtl")
@NamedQueries({
    @NamedQuery(name = "HkPriceListDetailEntity.findAll", query = "SELECT h FROM HkPriceListDetailEntity h")})
public class HkPriceListDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkPriceListDetailEntityPK hkPriceListDetailEntityPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private long color;
    @Basic(optional = false)
    @Column(nullable = false)
    private long clarity;
    @Basic(optional = false)
    @Column(nullable = false)
    private long cut;
    @Basic(optional = false)
    @Column(name = "carat_range", nullable = false)
    private long caratRange;
    @Basic(optional = false)
    @Column(nullable = false)
    private long fluorescence;
    @Basic(optional = false)
    @Column(name = "original_price", nullable = false)
    private double originalPrice;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 8, scale = 8)
    private Float discount;
    @Basic(optional = false)
    @Column(name = "hkg_price", nullable = false)
    private double hkgPrice;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @MapsId("priceList")
    @JoinColumn(name = "price_list", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkPriceListEntity hkPriceListEntity;

    public HkPriceListDetailEntity() {
    }

    public HkPriceListDetailEntity(HkPriceListDetailEntityPK hkPriceListDetailEntityPK) {
        this.hkPriceListDetailEntityPK = hkPriceListDetailEntityPK;
    }

    public HkPriceListDetailEntity(HkPriceListDetailEntityPK hkPriceListDetailEntityPK, long color, long clarity, long cut, long caratRange, long flouscence, double originalPrice, double hkgPrice, boolean isArchive) {
        this.hkPriceListDetailEntityPK = hkPriceListDetailEntityPK;
        this.color = color;
        this.clarity = clarity;
        this.cut = cut;
        this.caratRange = caratRange;
        this.fluorescence = flouscence;
        this.originalPrice = originalPrice;
        this.hkgPrice = hkgPrice;
        this.isArchive = isArchive;
    }

    public HkPriceListDetailEntity(long priceList, int sequenceNumber) {
        this.hkPriceListDetailEntityPK = new HkPriceListDetailEntityPK(priceList, sequenceNumber);
    }

    public HkPriceListDetailEntityPK getHkPriceListDetailEntityPK() {
        return hkPriceListDetailEntityPK;
    }

    public void setHkPriceListDetailEntityPK(HkPriceListDetailEntityPK hkPriceListDetailEntityPK) {
        this.hkPriceListDetailEntityPK = hkPriceListDetailEntityPK;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public long getClarity() {
        return clarity;
    }

    public void setClarity(long clarity) {
        this.clarity = clarity;
    }

    public long getCut() {
        return cut;
    }

    public void setCut(long cut) {
        this.cut = cut;
    }

    public long getCaratRange() {
        return caratRange;
    }

    public void setCaratRange(long caratRange) {
        this.caratRange = caratRange;
    }

    public long getFluorescence() {
        return fluorescence;
    }

    public void setFluorescence(long fluorescence) {
        this.fluorescence = fluorescence;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public double getHkgPrice() {
        return hkgPrice;
    }

    public void setHkgPrice(double hkgPrice) {
        this.hkgPrice = hkgPrice;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkPriceListEntity getHkPriceListEntity() {
        return hkPriceListEntity;
    }

    public void setHkPriceListEntity(HkPriceListEntity hkPriceListEntity) {
        this.hkPriceListEntity = hkPriceListEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkPriceListDetailEntityPK != null ? hkPriceListDetailEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkPriceListDetailEntity)) {
            return false;
        }
        HkPriceListDetailEntity other = (HkPriceListDetailEntity) object;
        if ((this.hkPriceListDetailEntityPK == null && other.hkPriceListDetailEntityPK != null) || (this.hkPriceListDetailEntityPK != null && !this.hkPriceListDetailEntityPK.equals(other.hkPriceListDetailEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkPriceListDetailEntity[ hkPriceListDetailEntityPK=" + hkPriceListDetailEntityPK + " ]";
    }

}
