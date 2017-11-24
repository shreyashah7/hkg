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
 * @author mital
 */
@Entity
@Table(name = "hk_system_configuration_info")
public class HkSystemConfigurationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected HkSystemConfigurationEntityPK hkSystemConfigurationEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive")
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "key_value")
    private String keyValue;
    @Basic(optional = false)
    @Column(name = "modified_by")
    private long modifiedBy;
    @Basic(optional = false)
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    public HkSystemConfigurationEntity() {
    }

    public HkSystemConfigurationEntity(HkSystemConfigurationEntityPK hkSystemConfigurationEntityPK) {
        this.hkSystemConfigurationEntityPK = hkSystemConfigurationEntityPK;
    }

    public HkSystemConfigurationEntity(HkSystemConfigurationEntityPK hkSystemConfigurationEntityPK, boolean isArchive, String keyValue, long modifiedBy, Date modifiedOn) {
        this.hkSystemConfigurationEntityPK = hkSystemConfigurationEntityPK;
        this.isArchive = isArchive;
        this.keyValue = keyValue;
        this.modifiedBy = modifiedBy;
        this.modifiedOn = modifiedOn;
    }

    public HkSystemConfigurationEntity(String systemKey, long franchise) {
        this.hkSystemConfigurationEntityPK = new HkSystemConfigurationEntityPK(systemKey, franchise);
    }

    public HkSystemConfigurationEntityPK getHkSystemConfigurationEntityPK() {
        return hkSystemConfigurationEntityPK;
    }

    public void setHkSystemConfigurationEntityPK(HkSystemConfigurationEntityPK hkSystemConfigurationEntityPK) {
        this.hkSystemConfigurationEntityPK = hkSystemConfigurationEntityPK;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkSystemConfigurationEntityPK != null ? hkSystemConfigurationEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkSystemConfigurationEntity)) {
            return false;
        }
        HkSystemConfigurationEntity other = (HkSystemConfigurationEntity) object;
        if ((this.hkSystemConfigurationEntityPK == null && other.hkSystemConfigurationEntityPK == null) || (this.hkSystemConfigurationEntityPK == null && other.hkSystemConfigurationEntityPK != null) || (this.hkSystemConfigurationEntityPK != null && !this.hkSystemConfigurationEntityPK.equals(other.hkSystemConfigurationEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkSystemConfigurationEntity[ hkSystemConfigurationEntityPK=" + hkSystemConfigurationEntityPK + " ]";
    }

}
