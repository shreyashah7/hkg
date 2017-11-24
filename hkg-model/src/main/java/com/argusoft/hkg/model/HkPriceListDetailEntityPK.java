/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author mmodi
 */
@Embeddable
public class HkPriceListDetailEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "price_list", nullable = false)
    private Long priceList;
    @Basic(optional = false)
    @Column(name = "sequence_number", nullable = false)
    private int sequenceNumber;

    public HkPriceListDetailEntityPK() {
    }

    public HkPriceListDetailEntityPK(long priceList, int sequenceNumber) {
        this.priceList = priceList;
        this.sequenceNumber = sequenceNumber;
    }

    public long getPriceList() {
        return priceList;
    }

    public void setPriceList(long priceList) {
        this.priceList = priceList;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.priceList);
        hash = 29 * hash + this.sequenceNumber;
        return hash;
    }
   
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkPriceListDetailEntityPK)) {
            return false;
        }
        HkPriceListDetailEntityPK other = (HkPriceListDetailEntityPK) object;
        if (this.priceList != other.priceList) {
            return false;
        }
        if (this.sequenceNumber != other.sequenceNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkPriceListDetailEntityPK[ priceList=" + priceList + ", sequenceNumber=" + sequenceNumber + " ]";
    }
    
}
