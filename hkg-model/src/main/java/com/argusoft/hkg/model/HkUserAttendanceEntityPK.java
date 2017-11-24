package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Embeddable
public class HkUserAttendanceEntityPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic(optional = false)
    @Column(name = "on_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date onDate;

    public HkUserAttendanceEntityPK() {
    }

    public HkUserAttendanceEntityPK(long userId, Date onDate) {
        this.userId = userId;
        this.onDate = onDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 41 * hash + Objects.hashCode(this.onDate);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserAttendanceEntityPK)) {
            return false;
        }
        HkUserAttendanceEntityPK other = (HkUserAttendanceEntityPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.onDate != other.onDate) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserAttendanceEntityPK[ userId=" + userId + ", onDate=" + onDate + " ]";
    }

}
