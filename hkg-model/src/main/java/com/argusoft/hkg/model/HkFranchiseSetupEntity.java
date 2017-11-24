/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "hk_franchise_setup_info")
public class HkFranchiseSetupEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkFranchiseSetupEntityPK hkFranchiseSetupEntityPK;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;

    public HkFranchiseSetupEntity() {
    }

    public HkFranchiseSetupEntity(HkFranchiseSetupEntityPK hkFranchiseSetupEntityPK) {
        this.hkFranchiseSetupEntityPK = hkFranchiseSetupEntityPK;
    }

    public HkFranchiseSetupEntity(HkFranchiseSetupEntityPK hkFranchiseSetupEntityPK, String status, Date lastModifiedOn) {
        this.hkFranchiseSetupEntityPK = hkFranchiseSetupEntityPK;
        this.status = status;
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkFranchiseSetupEntity(long franchise, String setupItem) {
        this.hkFranchiseSetupEntityPK = new HkFranchiseSetupEntityPK(franchise, setupItem);
    }

    public HkFranchiseSetupEntityPK getHkFranchiseSetupEntityPK() {
        return hkFranchiseSetupEntityPK;
    }

    public void setHkFranchiseSetupEntityPK(HkFranchiseSetupEntityPK hkFranchiseSetupEntityPK) {
        this.hkFranchiseSetupEntityPK = hkFranchiseSetupEntityPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkFranchiseSetupEntityPK != null ? hkFranchiseSetupEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkFranchiseSetupEntity)) {
            return false;
        }
        HkFranchiseSetupEntity other = (HkFranchiseSetupEntity) object;
        if ((this.hkFranchiseSetupEntityPK == null && other.hkFranchiseSetupEntityPK != null) || (this.hkFranchiseSetupEntityPK != null && !this.hkFranchiseSetupEntityPK.equals(other.hkFranchiseSetupEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkFranchiseSetupEntity[ hkFranchiseSetupEntityPK=" + hkFranchiseSetupEntityPK + " ]";
    }
    
}
