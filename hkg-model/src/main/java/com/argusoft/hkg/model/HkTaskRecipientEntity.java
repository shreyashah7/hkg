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
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_task_recipient_info")
public class HkTaskRecipientEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkTaskRecipientEntityPK hkTaskRecipientEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @JoinColumn(name = "task", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkTaskEntity hkTaskEntity;

    public HkTaskRecipientEntity() {
    }

    public HkTaskRecipientEntity(HkTaskRecipientEntityPK hkTaskRecipientEntityPK) {
        this.hkTaskRecipientEntityPK = hkTaskRecipientEntityPK;
    }

    public HkTaskRecipientEntity(HkTaskRecipientEntityPK hkTaskRecipientEntityPK, boolean isArchive) {
        this.hkTaskRecipientEntityPK = hkTaskRecipientEntityPK;
        this.isArchive = isArchive;
    }

    public HkTaskRecipientEntity(long task, String referenceType, long referenceInstance) {
        this.hkTaskRecipientEntityPK = new HkTaskRecipientEntityPK(task, referenceType, referenceInstance);
    }

    public HkTaskRecipientEntityPK getHkTaskRecipientEntityPK() {
        return hkTaskRecipientEntityPK;
    }

    public void setHkTaskRecipientEntityPK(HkTaskRecipientEntityPK hkTaskRecipientEntityPK) {
        this.hkTaskRecipientEntityPK = hkTaskRecipientEntityPK;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkTaskEntity getHkTaskEntity() {
        return hkTaskEntity;
    }

    public void setHkTaskEntity(HkTaskEntity hkTaskEntity) {
        this.hkTaskEntity = hkTaskEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkTaskRecipientEntityPK != null ? hkTaskRecipientEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkTaskRecipientEntity)) {
            return false;
        }
        HkTaskRecipientEntity other = (HkTaskRecipientEntity) object;
        if ((this.hkTaskRecipientEntityPK == null && other.hkTaskRecipientEntityPK == null) || (this.hkTaskRecipientEntityPK == null && other.hkTaskRecipientEntityPK != null) || (this.hkTaskRecipientEntityPK != null && !this.hkTaskRecipientEntityPK.equals(other.hkTaskRecipientEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkTaskRecipientEntity[ hkTaskRecipientEntityPK=" + hkTaskRecipientEntityPK + " ]";
    }
    
}
