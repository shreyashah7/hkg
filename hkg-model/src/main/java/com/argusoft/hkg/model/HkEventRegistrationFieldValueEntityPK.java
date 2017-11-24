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
public class HkEventRegistrationFieldValueEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "registration_field", nullable = false)
    private long registrationField;
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;

    public HkEventRegistrationFieldValueEntityPK() {
    }

    public HkEventRegistrationFieldValueEntityPK(long registrationField, long userId) {
        this.registrationField = registrationField;
        this.userId = userId;
    }

    public long getRegistrationField() {
        return registrationField;
    }

    public void setRegistrationField(long registrationField) {
        this.registrationField = registrationField;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) registrationField;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventRegistrationFieldValueEntityPK)) {
            return false;
        }
        HkEventRegistrationFieldValueEntityPK other = (HkEventRegistrationFieldValueEntityPK) object;
        if (this.registrationField != other.registrationField) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRegistrationFieldValueEntityPK[ registrationField=" + registrationField + ", userId=" + userId + " ]";
    }
    
}
