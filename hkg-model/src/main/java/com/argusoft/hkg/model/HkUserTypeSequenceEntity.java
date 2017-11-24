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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_usertype_seq_mst")
@NamedQueries({
    @NamedQuery(name = "HkUserTypeSequenceEntity.findAll", query = "SELECT h FROM HkUserTypeSequenceEntity h")})
public class HkUserTypeSequenceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkUserTypeSequenceEntityPK hkUserTypeSequenceEntityPK;
    @Basic(optional = false)
    @Column(name = "current_value", nullable = false)
    private int currentValue;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

    public HkUserTypeSequenceEntity() {
    }

    public HkUserTypeSequenceEntity(HkUserTypeSequenceEntityPK hkUserTypeSequenceEntityPK) {
        this.hkUserTypeSequenceEntityPK = hkUserTypeSequenceEntityPK;
    }

    public HkUserTypeSequenceEntity(HkUserTypeSequenceEntityPK hkUserTypeSequenceEntityPK, int currentValue, boolean isArchive) {
        this.hkUserTypeSequenceEntityPK = hkUserTypeSequenceEntityPK;
        this.currentValue = currentValue;
        this.isArchive = isArchive;
    }

    public HkUserTypeSequenceEntity(long userType, long franchise) {
        this.hkUserTypeSequenceEntityPK = new HkUserTypeSequenceEntityPK(userType, franchise);
    }

    public HkUserTypeSequenceEntityPK getHkUserTypeSequenceEntityPK() {
        return hkUserTypeSequenceEntityPK;
    }

    public void setHkUserTypeSequenceEntityPK(HkUserTypeSequenceEntityPK hkUserTypeSequenceEntityPK) {
        this.hkUserTypeSequenceEntityPK = hkUserTypeSequenceEntityPK;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkUserTypeSequenceEntityPK != null ? hkUserTypeSequenceEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserTypeSequenceEntity)) {
            return false;
        }
        HkUserTypeSequenceEntity other = (HkUserTypeSequenceEntity) object;
        if ((this.hkUserTypeSequenceEntityPK == null && other.hkUserTypeSequenceEntityPK != null) || (this.hkUserTypeSequenceEntityPK != null && !this.hkUserTypeSequenceEntityPK.equals(other.hkUserTypeSequenceEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserTypeSequenceEntity[ hkUserTypeSequenceEntityPK=" + hkUserTypeSequenceEntityPK + " ]";
    }
    
}
