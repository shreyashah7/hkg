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
public class HkShiftDepartmentEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private long shift;
    @Basic(optional = false)
    @Column(nullable = false)
    private long department;

    public HkShiftDepartmentEntityPK() {
    }

    public HkShiftDepartmentEntityPK(long shift, long department) {
        this.shift = shift;
        this.department = department;
    }

    public long getShift() {
        return shift;
    }

    public void setShift(long shift) {
        this.shift = shift;
    }

    public long getDepartment() {
        return department;
    }

    public void setDepartment(long department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) shift;
        hash += (int) department;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkShiftDepartmentEntityPK)) {
            return false;
        }
        HkShiftDepartmentEntityPK other = (HkShiftDepartmentEntityPK) object;
        if (this.shift != other.shift) {
            return false;
        }
        if (this.department != other.department) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftDepartmentEntityPK[ shift=" + shift + ", department=" + department + " ]";
    }

}
