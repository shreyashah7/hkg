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
public class HkTaskRecipientEntityPK implements Serializable {

    @Basic(optional = false)
    @Column(nullable = false)
    private long task;
    @Basic(optional = false)
    @Column(name = "reference_type", nullable = false, length = 5)
    private String referenceType;
    @Basic(optional = false)
    @Column(name = "reference_instance", nullable = false)
    private long referenceInstance;

    public HkTaskRecipientEntityPK() {
    }

    public HkTaskRecipientEntityPK(long task, String referenceType, long referenceInstance) {
        this.task = task;
        this.referenceType = referenceType;
        this.referenceInstance = referenceInstance;
    }

    public long getTask() {
        return task;
    }

    public void setTask(long task) {
        this.task = task;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public long getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(long referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) task;
        hash += (referenceType != null ? referenceType.hashCode() : 0);
        hash += (int) referenceInstance;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkTaskRecipientEntityPK)) {
            return false;
        }
        HkTaskRecipientEntityPK other = (HkTaskRecipientEntityPK) object;
        if (this.task != other.task) {
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
        return "com.argusoft.hkg.model.HkTaskRecipientEntityPK[ task=" + task + ", referenceType=" + referenceType + ", referenceInstance=" + referenceInstance + " ]";
    }

}
