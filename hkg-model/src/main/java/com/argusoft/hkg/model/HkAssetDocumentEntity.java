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
 * @author Mayank
 */
@Entity
@Table(name = "hk_asset_document_info")
public class HkAssetDocumentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId        
    protected HkAssetDocumentEntityPK hkAssetDocumentEntityPK;
    @Column(name = "document_title", length = 100)
    private String documentTitle;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @MapsId("asset")
    @JoinColumn(name = "asset", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkAssetEntity hkAssetEntity;

    public HkAssetDocumentEntity() {
    }

    public HkAssetDocumentEntity(HkAssetDocumentEntityPK hkAssetDocumentEntityPK) {
        this.hkAssetDocumentEntityPK = hkAssetDocumentEntityPK;
    }

    public HkAssetDocumentEntity(HkAssetDocumentEntityPK hkAssetDocumentEntityPK, boolean isArchive) {
        this.hkAssetDocumentEntityPK = hkAssetDocumentEntityPK;
        this.isArchive = isArchive;
    }

    public HkAssetDocumentEntity(long asset, String documentPath) {
        this.hkAssetDocumentEntityPK = new HkAssetDocumentEntityPK(asset, documentPath);
    }

    public HkAssetDocumentEntityPK getHkAssetDocumentEntityPK() {
        return hkAssetDocumentEntityPK;
    }

    public void setHkAssetDocumentEntityPK(HkAssetDocumentEntityPK hkAssetDocumentEntityPK) {
        this.hkAssetDocumentEntityPK = hkAssetDocumentEntityPK;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
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
        hash += (hkAssetDocumentEntityPK != null ? hkAssetDocumentEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkAssetDocumentEntity)) {
            return false;
        }
        HkAssetDocumentEntity other = (HkAssetDocumentEntity) object;
        if ((this.hkAssetDocumentEntityPK == null && other.hkAssetDocumentEntityPK != null) || (this.hkAssetDocumentEntityPK != null && !this.hkAssetDocumentEntityPK.equals(other.hkAssetDocumentEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetDocumentEntity[ hkAssetDocumentEntityPK=" + hkAssetDocumentEntityPK + " ]";
    }
    
}
