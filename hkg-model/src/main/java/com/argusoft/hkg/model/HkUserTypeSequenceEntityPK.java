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
public class HkUserTypeSequenceEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "user_type", nullable = false)
    private long userType;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;

    public HkUserTypeSequenceEntityPK() {
    }

    public HkUserTypeSequenceEntityPK(long userType, long franchise) {
        this.userType = userType;
        this.franchise = franchise;
    }

    public long getUserType() {
        return userType;
    }

    public void setUserType(long userType) {
        this.userType = userType;
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
        hash += (int) userType;
        hash += (int) franchise;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserTypeSequenceEntityPK)) {
            return false;
        }
        HkUserTypeSequenceEntityPK other = (HkUserTypeSequenceEntityPK) object;
        if (this.userType != other.userType) {
            return false;
        }
        if (this.franchise != other.franchise) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserTypeSequenceEntityPK[ userType=" + userType + ", franchise=" + franchise + " ]";
    }
    
}
