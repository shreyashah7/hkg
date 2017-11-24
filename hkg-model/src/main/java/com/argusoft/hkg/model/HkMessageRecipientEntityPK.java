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
 * Primary key class for HkMessageRecipient entity.
 *
 * @author Mital
 */
@Embeddable
public class HkMessageRecipientEntityPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "message_obj")
    private Long messageObj;
    @Basic(optional = false)
    @Column(name = "reference_type")
    private String referenceType;
    @Basic(optional = false)
    @Column(name = "reference_instance")
    private Long referenceInstance;

    public HkMessageRecipientEntityPK() {
    }

    public HkMessageRecipientEntityPK(Long messageObj, String referenceType, Long referenceInstance) {
        this.messageObj = messageObj;
        this.referenceType = referenceType;
        this.referenceInstance = referenceInstance;
    }

    public Long getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(Long messageObj) {
        this.messageObj = messageObj;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(Long referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (long) messageObj;
        hash += (referenceType != null ? referenceType.hashCode() : 0);
        hash += (long) referenceInstance;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMessageRecipientEntityPK)) {
            return false;
        }
        HkMessageRecipientEntityPK other = (HkMessageRecipientEntityPK) object;
        if (this.messageObj != other.messageObj) {
            return false;
        }
        if ((this.referenceType == null && other.referenceType != null) || (this.referenceType != null && !this.referenceType.equals(other.referenceType))) {
            return false;
        }
        if (this.referenceInstance != other.referenceInstance) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkMessageRecipentInfoPK[ messageObj=" + messageObj + ", referenceType=" + referenceType + ", referenceInstance=" + referenceInstance + " ]";
    }

}
