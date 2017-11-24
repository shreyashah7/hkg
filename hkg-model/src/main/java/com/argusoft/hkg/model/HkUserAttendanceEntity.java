package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_user_attendance_info")
public class HkUserAttendanceEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkUserAttendanceEntityPK hkUserAttendanceEntityPK;
    @Basic(optional = false)
    @Column(name = "shift_hours", nullable = false)
    private float shiftHours;
    @Basic(optional = false)
    @Column(name = "office_hours", nullable = false)
    private float officeHours;
    @Basic(optional = false)
    @Column(name = "login_hours", nullable = false)
    private float loginHours;
    @Basic(optional = false)
    @Column(name = "attendance_type", nullable = false, length = 5)
    private String attendanceType;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "franchise")
    private Long franchise;

    public HkUserAttendanceEntity() {
    }

    public HkUserAttendanceEntity(HkUserAttendanceEntityPK hkUserAttendanceEntityPK) {
        this.hkUserAttendanceEntityPK = hkUserAttendanceEntityPK;
    }

    public HkUserAttendanceEntity(HkUserAttendanceEntityPK hkUserAttendanceEntityPK, float shiftHours, float officeHours, float loginHours, String attendanceType, boolean isArchive) {
        this.hkUserAttendanceEntityPK = hkUserAttendanceEntityPK;
        this.shiftHours = shiftHours;
        this.officeHours = officeHours;
        this.loginHours = loginHours;
        this.attendanceType = attendanceType;
        this.isArchive = isArchive;
    }

    public HkUserAttendanceEntity(long userId, Date onDate) {
        this.hkUserAttendanceEntityPK = new HkUserAttendanceEntityPK(userId, onDate);
    }

    public HkUserAttendanceEntityPK getHkUserAttendanceEntityPK() {
        return hkUserAttendanceEntityPK;
    }

    public void setHkUserAttendanceEntityPK(HkUserAttendanceEntityPK hkUserAttendanceEntityPK) {
        this.hkUserAttendanceEntityPK = hkUserAttendanceEntityPK;
    }

    public float getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(float shiftHours) {
        this.shiftHours = shiftHours;
    }

    public float getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(float officeHours) {
        this.officeHours = officeHours;
    }

    public float getLoginHours() {
        return loginHours;
    }

    public void setLoginHours(float loginHours) {
        this.loginHours = loginHours;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkUserAttendanceEntityPK != null ? hkUserAttendanceEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserAttendanceEntity)) {
            return false;
        }
        HkUserAttendanceEntity other = (HkUserAttendanceEntity) object;
        if ((this.hkUserAttendanceEntityPK == null && other.hkUserAttendanceEntityPK != null) || (this.hkUserAttendanceEntityPK != null && !this.hkUserAttendanceEntityPK.equals(other.hkUserAttendanceEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserAttendanceEntity[ hkUserAttendanceEntityPK=" + hkUserAttendanceEntityPK + " ]";
    }

}
