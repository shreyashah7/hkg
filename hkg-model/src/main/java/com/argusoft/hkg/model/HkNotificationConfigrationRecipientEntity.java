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
 * @author mmodi
 */
@Entity
@Table(name = "hk_notification_configration_recipient_info")
@NamedQueries({
    @NamedQuery(name = "HkNotificationConfigrationRecipientEntity.findAll", query = "SELECT h FROM HkNotificationConfigrationRecipientEntity h")})
public class HkNotificationConfigrationRecipientEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkNotificationConfigrationRecipientEntityPK hkNotificationConfigrationRecipientEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

    public HkNotificationConfigrationRecipientEntity() {
    }

    public HkNotificationConfigrationRecipientEntity(HkNotificationConfigrationRecipientEntityPK hkNotificationConfigrationRecipientEntityPK) {
        this.hkNotificationConfigrationRecipientEntityPK = hkNotificationConfigrationRecipientEntityPK;
    }

    public HkNotificationConfigrationRecipientEntity(HkNotificationConfigrationRecipientEntityPK hkNotificationConfigrationRecipientEntityPK, boolean isArchive) {
        this.hkNotificationConfigrationRecipientEntityPK = hkNotificationConfigrationRecipientEntityPK;
        this.isArchive = isArchive;
    }

    public HkNotificationConfigrationRecipientEntity(long notificationConfiguration, long referenceInstance, String referenceType) {
        this.hkNotificationConfigrationRecipientEntityPK = new HkNotificationConfigrationRecipientEntityPK(notificationConfiguration, referenceInstance, referenceType);
    }

    public HkNotificationConfigrationRecipientEntityPK getHkNotificationConfigrationRecipientEntityPK() {
        return hkNotificationConfigrationRecipientEntityPK;
    }

    public void setHkNotificationConfigrationRecipientEntityPK(HkNotificationConfigrationRecipientEntityPK hkNotificationConfigrationRecipientEntityPK) {
        this.hkNotificationConfigrationRecipientEntityPK = hkNotificationConfigrationRecipientEntityPK;
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
        hash += (hkNotificationConfigrationRecipientEntityPK != null ? hkNotificationConfigrationRecipientEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkNotificationConfigrationRecipientEntity)) {
            return false;
        }
        HkNotificationConfigrationRecipientEntity other = (HkNotificationConfigrationRecipientEntity) object;
        if ((this.hkNotificationConfigrationRecipientEntityPK == null && other.hkNotificationConfigrationRecipientEntityPK != null) || (this.hkNotificationConfigrationRecipientEntityPK != null && !this.hkNotificationConfigrationRecipientEntityPK.equals(other.hkNotificationConfigrationRecipientEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity[ hkNotificationConfigrationRecipientEntityPK=" + hkNotificationConfigrationRecipientEntityPK + " ]";
    }
    
}
