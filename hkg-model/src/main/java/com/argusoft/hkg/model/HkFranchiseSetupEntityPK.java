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
 * @author mmodi
 */
@Embeddable
public class HkFranchiseSetupEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "setup_item", nullable = false, length = 10)
    private String setupItem;

    public HkFranchiseSetupEntityPK() {
    }

    public HkFranchiseSetupEntityPK(long franchise, String setupItem) {
        this.franchise = franchise;
        this.setupItem = setupItem;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public String getSetupItem() {
        return setupItem;
    }

    public void setSetupItem(String setupItem) {
        this.setupItem = setupItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) franchise;
        hash += (setupItem != null ? setupItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkFranchiseSetupEntityPK)) {
            return false;
        }
        HkFranchiseSetupEntityPK other = (HkFranchiseSetupEntityPK) object;
        if (this.franchise != other.franchise) {
            return false;
        }
        if ((this.setupItem == null && other.setupItem != null) || (this.setupItem != null && !this.setupItem.equals(other.setupItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkFranchiseSetupEntityPK[ franchise=" + franchise + ", setupItem=" + setupItem + " ]";
    }
    
}
