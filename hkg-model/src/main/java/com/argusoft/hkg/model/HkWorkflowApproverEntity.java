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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_workflow_approver_rel_info")
@NamedQueries({
    @NamedQuery(name = "HkWorkflowApproverEntity.findAll", query = "SELECT h FROM HkWorkflowApproverEntity h")})
public class HkWorkflowApproverEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkWorkflowApproverEntityPK hkWorkflowApproverEntityPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private long level;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @JoinColumn(name = "workflow", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkWorkflowEntity hkWorkflowEntity;

    public HkWorkflowApproverEntity() {
    }

    public HkWorkflowApproverEntity(HkWorkflowApproverEntityPK hkWorkflowApproverEntityPK) {
        this.hkWorkflowApproverEntityPK = hkWorkflowApproverEntityPK;
    }

    public HkWorkflowApproverEntity(HkWorkflowApproverEntityPK hkWorkflowApproverEntityPK, long level, boolean isArchive) {
        this.hkWorkflowApproverEntityPK = hkWorkflowApproverEntityPK;
        this.level = level;
        this.isArchive = isArchive;
    }

    public HkWorkflowApproverEntity(long workflow, String referenceType, long referenceInstance) {
        this.hkWorkflowApproverEntityPK = new HkWorkflowApproverEntityPK(workflow, referenceType, referenceInstance);
    }

    public HkWorkflowApproverEntityPK getHkWorkflowApproverEntityPK() {
        return hkWorkflowApproverEntityPK;
    }

    public void setHkWorkflowApproverEntityPK(HkWorkflowApproverEntityPK hkWorkflowApproverEntityPK) {
        this.hkWorkflowApproverEntityPK = hkWorkflowApproverEntityPK;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkWorkflowEntity getHkWorkflowEntity() {
        return hkWorkflowEntity;
    }

    public void setHkWorkflowEntity(HkWorkflowEntity hkWorkflowEntity) {
        this.hkWorkflowEntity = hkWorkflowEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkWorkflowApproverEntityPK != null ? hkWorkflowApproverEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkWorkflowApproverEntity)) {
            return false;
        }
        HkWorkflowApproverEntity other = (HkWorkflowApproverEntity) object;
        if ((this.hkWorkflowApproverEntityPK == null && other.hkWorkflowApproverEntityPK != null) || (this.hkWorkflowApproverEntityPK != null && !this.hkWorkflowApproverEntityPK.equals(other.hkWorkflowApproverEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkWorkflowApproverEntity[ hkWorkflowApproverEntityPK=" + hkWorkflowApproverEntityPK + " ]";
    }
    
}
