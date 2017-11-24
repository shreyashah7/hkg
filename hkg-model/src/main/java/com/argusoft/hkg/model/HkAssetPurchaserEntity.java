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
import javax.persistence.Table;

/**
 *
 * @author dhwani
 */
@Entity
@Table(name = "hk_asset_purchaser_info")
public class HkAssetPurchaserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @MapsId("asset")
    @JoinColumn(name = "asset", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkAssetEntity hkAssetEntity;

    public HkAssetPurchaserEntity() {
    }

    public HkAssetPurchaserEntity(HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK) {
        this.hkAssetPurchaserEntityPK = hkAssetPurchaserEntityPK;
    }

    public HkAssetPurchaserEntity(HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK, boolean isArchive) {
        this.hkAssetPurchaserEntityPK = hkAssetPurchaserEntityPK;
        this.isArchive = isArchive;
    }

    public HkAssetPurchaserEntity(long asset, long purchaseByInstance, String purchaseByType) {
        this.hkAssetPurchaserEntityPK = new HkAssetPurchaserEntityPK(asset, purchaseByInstance, purchaseByType);
    }

    public HkAssetPurchaserEntityPK getHkAssetPurchaserEntityPK() {
        return hkAssetPurchaserEntityPK;
    }

    public void setHkAssetPurchaserEntityPK(HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK) {
        this.hkAssetPurchaserEntityPK = hkAssetPurchaserEntityPK;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkAssetEntity getHkAssetEntity() {
        return hkAssetEntity;
    }

    public void setHkAssetEntity(HkAssetEntity hkAssetEntity) {
        this.hkAssetEntity = hkAssetEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkAssetPurchaserEntityPK != null ? hkAssetPurchaserEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkAssetPurchaserEntity)) {
            return false;
        }
        HkAssetPurchaserEntity other = (HkAssetPurchaserEntity) object;
        if ((this.hkAssetPurchaserEntityPK == null && other.hkAssetPurchaserEntityPK != null) || (this.hkAssetPurchaserEntityPK != null && !this.hkAssetPurchaserEntityPK.equals(other.hkAssetPurchaserEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetPurchaserEntity[ hkAssetPurchaserEntityPK=" + hkAssetPurchaserEntityPK + " ]";
    }
    
}
