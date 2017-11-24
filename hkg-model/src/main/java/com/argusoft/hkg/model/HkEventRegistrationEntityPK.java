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
public class HkEventRegistrationEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private long event;
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;

    public HkEventRegistrationEntityPK() {
    }

    public HkEventRegistrationEntityPK(long event, long userId) {
        this.event = event;
        this.userId = userId;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
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
        hash += (int) event;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventRegistrationEntityPK)) {
            return false;
        }
        HkEventRegistrationEntityPK other = (HkEventRegistrationEntityPK) object;
        if (this.event != other.event) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRegistrationEntityPK[ event=" + event + ", userId=" + userId + " ]";
    }
    
}
