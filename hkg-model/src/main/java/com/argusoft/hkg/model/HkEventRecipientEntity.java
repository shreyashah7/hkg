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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_event_recipient_info")
@NamedQueries({
    @NamedQuery(name = "HkEventRecipientEntity.findAll", query = "SELECT h FROM HkEventRecipientEntity h")})
public class HkEventRecipientEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkEventRecipientEntityPK hkEventRecipientEntityPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @MapsId("event")
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkEventEntity hkEventEntity;

    public HkEventRecipientEntity() {
    }

    public HkEventRecipientEntity(HkEventRecipientEntityPK hkEventRecipientEntityPK) {
        this.hkEventRecipientEntityPK = hkEventRecipientEntityPK;
    }

    public HkEventRecipientEntity(HkEventRecipientEntityPK hkEventRecipientEntityPK, long franchise, boolean isArchive) {
        this.hkEventRecipientEntityPK = hkEventRecipientEntityPK;
        this.franchise = franchise;
        this.isArchive = isArchive;
    }

    public HkEventRecipientEntity(long event, String referenceType, long referenceInstance) {
        this.hkEventRecipientEntityPK = new HkEventRecipientEntityPK(event, referenceType, referenceInstance);
    }

    public HkEventRecipientEntityPK getHkEventRecipientEntityPK() {
        return hkEventRecipientEntityPK;
    }

    public void setHkEventRecipientEntityPK(HkEventRecipientEntityPK hkEventRecipientEntityPK) {
        this.hkEventRecipientEntityPK = hkEventRecipientEntityPK;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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
        hash += (hkEventRecipientEntityPK != null ? hkEventRecipientEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventRecipientEntity)) {
            return false;
        }
        HkEventRecipientEntity other = (HkEventRecipientEntity) object;
        if ((this.hkEventRecipientEntityPK == null && other.hkEventRecipientEntityPK != null) || (this.hkEventRecipientEntityPK != null && !this.hkEventRecipientEntityPK.equals(other.hkEventRecipientEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRecipientEntity[ hkEventRecipientEntityPK=" + hkEventRecipientEntityPK + " ]";
    }
    
}
