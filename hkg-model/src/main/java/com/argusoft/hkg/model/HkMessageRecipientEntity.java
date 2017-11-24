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
import javax.persistence.Table;

/**
 * Entity class for storing message recipient references and the value of it.
 * Eg. D for Department and Planning (Id: 18) as the value.
 *
 * @author Mital
 */
@Entity
@Table(name = "hk_message_recipent_info")
public class HkMessageRecipientEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkMessageRecipientEntityPK hkMessageRecipientPK;
    @Basic(optional = false)
    @Column(name = "is_archive")
    private boolean isArchive;
    @JoinColumn(name = "message_obj", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkMessageEntity hkMessage;

    public HkMessageRecipientEntity() {
    }

    public HkMessageRecipientEntity(HkMessageRecipientEntityPK hkMessageRecipientPK) {
        this.hkMessageRecipientPK = hkMessageRecipientPK;
    }

    public HkMessageRecipientEntity(HkMessageRecipientEntityPK hkMessageRecipientPK, boolean isArchive) {
        this.hkMessageRecipientPK = hkMessageRecipientPK;
        this.isArchive = isArchive;
    }

    public HkMessageRecipientEntity(Long messageObj, String referenceType, Long referenceInstance) {
        this.hkMessageRecipientPK = new HkMessageRecipientEntityPK(messageObj, referenceType, referenceInstance);
    }

    public HkMessageRecipientEntityPK getHkMessageRecipentPK() {
        return hkMessageRecipientPK;
    }

    public void setHkMessageRecipentPK(HkMessageRecipientEntityPK hkMessageRecipientPK) {
        this.hkMessageRecipientPK = hkMessageRecipientPK;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkMessageEntity getHkMessage() {
        return hkMessage;
    }

    public void setHkMessage(HkMessageEntity hkMessage) {
        this.hkMessage = hkMessage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkMessageRecipientPK != null ? hkMessageRecipientPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMessageRecipientEntity)) {
            return false;
        }
        HkMessageRecipientEntity other = (HkMessageRecipientEntity) object;
        if ((this.hkMessageRecipientPK == null && other.hkMessageRecipientPK != null) || (this.hkMessageRecipientPK != null && !this.hkMessageRecipientPK.equals(other.hkMessageRecipientPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkMessageRecipent[ hkMessageRecipientPK=" + hkMessageRecipientPK + " ]";
    }

}
