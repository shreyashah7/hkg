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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_user_event_registration_info")
@NamedQueries({
    @NamedQuery(name = "HkEventRegistrationEntity.findAll", query = "SELECT h FROM HkEventRegistrationEntity h")})
public class HkEventRegistrationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkEventRegistrationEntityPK hkEventRegistrationEntityPK;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    private String status;
    @Basic(optional = false)
    @Column(name = "adult_count", nullable = false)
    private int adultCount;
    @Basic(optional = false)
    @Column(name = "child_count", nullable = false)
    private int childCount;
    @Basic(optional = false)
    @Column(name = "guest_count", nullable = false)
    private int guestCount;
    @Column(name = "guest_info", length = 5000)
    private String guestInfo;
    @Column(name = "reason", length = 500)
    private String reason;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkEventEntity hkEventEntity;

    public HkEventRegistrationEntity() {
    }

    public HkEventRegistrationEntity(HkEventRegistrationEntityPK hkEventRegistrationEntityPK) {
        this.hkEventRegistrationEntityPK = hkEventRegistrationEntityPK;
    }

    public HkEventRegistrationEntity(HkEventRegistrationEntityPK hkEventRegistrationEntityPK, String status, int adultCount, int childCount, int guestCount, boolean isArchive, Date lastModifiedOn) {
        this.hkEventRegistrationEntityPK = hkEventRegistrationEntityPK;
        this.status = status;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.guestCount = guestCount;
        this.isArchive = isArchive;
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkEventRegistrationEntity(long event, long userId) {
        this.hkEventRegistrationEntityPK = new HkEventRegistrationEntityPK(event, userId);
    }

    public HkEventRegistrationEntityPK getHkEventRegistrationEntityPK() {
        return hkEventRegistrationEntityPK;
    }

    public void setHkEventRegistrationEntityPK(HkEventRegistrationEntityPK hkEventRegistrationEntityPK) {
        this.hkEventRegistrationEntityPK = hkEventRegistrationEntityPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public String getGuestInfo() {
        return guestInfo;
    }

    public void setGuestInfo(String guestInfo) {
        this.guestInfo = guestInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkEventEntity getHkEventEntity() {
        return hkEventEntity;
    }

    public void setHkEventEntity(HkEventEntity hkEventEntity) {
        this.hkEventEntity = hkEventEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkEventRegistrationEntityPK != null ? hkEventRegistrationEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventRegistrationEntity)) {
            return false;
        }
        HkEventRegistrationEntity other = (HkEventRegistrationEntity) object;
        if ((this.hkEventRegistrationEntityPK == null && other.hkEventRegistrationEntityPK != null) || (this.hkEventRegistrationEntityPK != null && !this.hkEventRegistrationEntityPK.equals(other.hkEventRegistrationEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRegistrationEntity[ hkEventRegistrationEntityPK=" + hkEventRegistrationEntityPK + " ]";
    }
    
}
