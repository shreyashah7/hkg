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
 * @author Mayank
 */
@Embeddable
public class HkAssetDocumentEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)    
    private Long asset;
    @Basic(optional = false)
    @Column(name = "document_path", nullable = false, length = 500)
    private String documentPath;

    public HkAssetDocumentEntityPK() {
    }

    public HkAssetDocumentEntityPK(long asset, String documentPath) {
        this.asset = asset;
        this.documentPath = documentPath;
    }

    public Long getAsset() {
        return asset;
    }

    public void setAsset(Long asset) {
        this.asset = asset;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asset != null ? asset.hashCode() : 0);
        hash += (documentPath != null ? documentPath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkAssetDocumentEntityPK)) {
            return false;
        }
        HkAssetDocumentEntityPK other = (HkAssetDocumentEntityPK) object;
        if (this.asset.equals(other.asset)) {
            return false;
        }
        if ((this.documentPath == null && other.documentPath != null) || (this.documentPath != null && !this.documentPath.equals(other.documentPath))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkAssetDocumentEntityPK[ asset=" + asset + ", documentPath=" + documentPath + " ]";
    }
    
}
