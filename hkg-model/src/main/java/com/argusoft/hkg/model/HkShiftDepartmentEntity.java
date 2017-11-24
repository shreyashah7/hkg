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
@Table(name = "hk_shift_department_info")
@NamedQueries({
    @NamedQuery(name = "HkShiftDepartmentEntity.findAll", query = "SELECT h FROM HkShiftDepartmentEntity h")})
public class HkShiftDepartmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkShiftDepartmentEntityPK hkShiftDepartmentEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @JoinColumn(name = "shift", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkShiftEntity hkShiftEntity;

    public HkShiftDepartmentEntity() {
    }

    public HkShiftDepartmentEntity(HkShiftDepartmentEntityPK hkShiftDepartmentEntityPK) {
        this.hkShiftDepartmentEntityPK = hkShiftDepartmentEntityPK;
    }

    public HkShiftDepartmentEntity(HkShiftDepartmentEntityPK hkShiftDepartmentEntityPK, boolean isArchive, Date lastModifiedOn) {
        this.hkShiftDepartmentEntityPK = hkShiftDepartmentEntityPK;
        this.isArchive = isArchive;
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkShiftDepartmentEntity(long shift, long department) {
        this.hkShiftDepartmentEntityPK = new HkShiftDepartmentEntityPK(shift, department);
    }

    public HkShiftDepartmentEntityPK getHkShiftDepartmentEntityPK() {
        return hkShiftDepartmentEntityPK;
    }

    public void setHkShiftDepartmentEntityPK(HkShiftDepartmentEntityPK hkShiftDepartmentEntityPK) {
        this.hkShiftDepartmentEntityPK = hkShiftDepartmentEntityPK;
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

    public HkShiftEntity getHkShiftEntity() {
        return hkShiftEntity;
    }

    public void setHkShiftEntity(HkShiftEntity hkShiftEntity) {
        this.hkShiftEntity = hkShiftEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkShiftDepartmentEntityPK != null ? hkShiftDepartmentEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkShiftDepartmentEntity)) {
            return false;
        }
        HkShiftDepartmentEntity other = (HkShiftDepartmentEntity) object;
        if ((this.hkShiftDepartmentEntityPK == null && other.hkShiftDepartmentEntityPK != null) || (this.hkShiftDepartmentEntityPK != null && !this.hkShiftDepartmentEntityPK.equals(other.hkShiftDepartmentEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftDepartmentEntity[ hkShiftDepartmentEntityPK=" + hkShiftDepartmentEntityPK + " ]";
    }
    
}
