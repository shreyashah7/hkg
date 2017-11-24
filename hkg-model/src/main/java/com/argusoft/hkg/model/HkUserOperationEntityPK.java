package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
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
public class HkUserOperationEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic(optional = false)
    @Column(name = "event_code", nullable = false)
    private int eventCode;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public HkUserOperationEntityPK() {
    }

    public HkUserOperationEntityPK(long userId, int eventCode, Date createdOn) {
        this.userId = userId;
        this.eventCode = eventCode;
        this.createdOn = createdOn;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) eventCode;
        hash += (createdOn != null ? createdOn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserOperationEntityPK)) {
            return false;
        }
        HkUserOperationEntityPK other = (HkUserOperationEntityPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.eventCode != other.eventCode) {
            return false;
        }
        if ((this.createdOn == null && other.createdOn != null) || (this.createdOn != null && !this.createdOn.equals(other.createdOn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserOperationEntityPK[ userId=" + userId + ", eventCode=" + eventCode + ", createdOn=" + createdOn + " ]";
    }

}