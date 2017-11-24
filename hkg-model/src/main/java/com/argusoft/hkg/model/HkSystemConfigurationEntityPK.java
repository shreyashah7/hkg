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
 * @author mital
 */
@Embeddable
public class HkSystemConfigurationEntityPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "system_key")
    private String systemKey;
    @Basic(optional = false)
    @Column(name = "franchise")
    private long franchise;

    public HkSystemConfigurationEntityPK() {
    }

    public HkSystemConfigurationEntityPK(String systemKey, long franchise) {
        this.systemKey = systemKey;
        this.franchise = franchise;
    }

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (systemKey != null ? systemKey.hashCode() : 0);
        hash += (int) franchise;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkSystemConfigurationEntityPK)) {
            return false;
        }
        HkSystemConfigurationEntityPK other = (HkSystemConfigurationEntityPK) object;
        if ((this.systemKey == null && other.systemKey == null) || (this.systemKey == null && other.systemKey != null) || (this.systemKey != null && !this.systemKey.equals(other.systemKey))) {
            return false;
        }
        if (this.franchise != other.franchise) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkSystemConfigurationEntityPK[ systemKey=" + systemKey + ", franchise=" + franchise + " ]";
    }

}
