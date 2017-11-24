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
public class HkNotificationConfigrationRecipientEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "notification_configuration", nullable = false)
    private long notificationConfiguration;
    @Basic(optional = false)
    @Column(name = "reference_instance", nullable = false)
    private long referenceInstance;
    @Basic(optional = false)
    @Column(name = "reference_type", nullable = false, length = 5)
    private String referenceType;

    public HkNotificationConfigrationRecipientEntityPK() {
    }

    public HkNotificationConfigrationRecipientEntityPK(long notificationConfiguration, long referenceInstance, String referenceType) {
        this.notificationConfiguration = notificationConfiguration;
        this.referenceInstance = referenceInstance;
        this.referenceType = referenceType;
    }

    public long getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(long notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }

    public long getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(long referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) notificationConfiguration;
        hash += (int) referenceInstance;
        hash += (referenceType != null ? referenceType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkNotificationConfigrationRecipientEntityPK)) {
            return false;
        }
        HkNotificationConfigrationRecipientEntityPK other = (HkNotificationConfigrationRecipientEntityPK) object;
        if (this.notificationConfiguration != other.notificationConfiguration) {
            return false;
        }
        if (this.referenceInstance != other.referenceInstance) {
            return false;
        }
        if ((this.referenceType == null && other.referenceType != null) || (this.referenceType != null && !this.referenceType.equals(other.referenceType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntityPK[ notificationConfiguration=" + notificationConfiguration + ", referenceInstance=" + referenceInstance + ", referenceType=" + referenceType + " ]";
    }
    
}
