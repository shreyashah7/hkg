/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author dhwani
 */
@Embeddable
public class HkAssetPurchaserEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private long asset;
    @Basic(optional = false)
    @Column(name = "purchase_by_instance", nullable = false)
    private long purchaseByInstance;
    @Basic(optional = false)
    @Column(name = "purchase_by_type", nullable = false, length = 5)
    private String purchaseByType;

    public HkAssetPurchaserEntityPK() {
    }

    public HkAssetPurchaserEntityPK(long asset, long purchaseByInstance, String purchaseByType) {
        this.asset = asset;
        this.purchaseByInstance = purchaseByInstance;
        this.purchaseByType = purchaseByType;
    }

    public long getAsset() {
        return asset;
    }

    public void setAsset(long asset) {
        this.asset = asset;
    }

    public long getPurchaseByInstance() {
        return purchaseByInstance;
    }

    public void setPurchaseByInstance(long purchaseByInstance) {
        this.purchaseByInstance = purchaseByInstance;
    }

    public String getPurchaseByType() {
        return purchaseByType;
    }

    public void setPurchaseByType(String purchaseByType) {
        this.purchaseByType = purchaseByType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) asset;
        hash += (int) purchaseByInstance;
        hash += (purchaseByType != null ? purchaseByType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkAssetPurchaserEntityPK)) {
            return false;
        }
        HkAssetPurchaserEntityPK other = (HkAssetPurchaserEntityPK) object;
        if (this.asset != other.asset) {
            return false;
        }
        if (this.purchaseByInstance != other.purchaseByInstance) {
            return false;
        }
        if ((this.purchaseByType == null && other.purchaseByType != null) || (this.purchaseByType != null && !this.purchaseByType.equals(other.purchaseByType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetPurchaserEntityPK[ asset=" + asset + ", purchaseByInstance=" + purchaseByInstance + ", purchaseByType=" + purchaseByType + " ]";
    }
    
}
