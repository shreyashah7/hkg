package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_user_operation_info")
public class HkUserOperationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkUserOperationEntityPK hkUserOperationEntityPK;
    @Basic(optional = false)
    @Column(name = "on_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date onTime;
    @Basic(optional = false)
    @Column(name = "shift_dtl", nullable = false)
    private long shiftDtl;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    private String status;
    @Column(length = 1000)
    private String comments;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "franchise")
    private Long franchise;
    @Basic(optional = false)
    @Column(name = "is_attended", nullable = false)
    private boolean isAttended;

    public HkUserOperationEntity() {
    }

    public HkUserOperationEntity(HkUserOperationEntityPK hkUserOperationEntityPK) {
        this.hkUserOperationEntityPK = hkUserOperationEntityPK;
    }

    public HkUserOperationEntity(HkUserOperationEntityPK hkUserOperationEntityPK, Date onTime, long shiftDtl, String status, Date lastModifiedOn, boolean isArchive) {
        this.hkUserOperationEntityPK = hkUserOperationEntityPK;
        this.onTime = onTime;
        this.shiftDtl = shiftDtl;
        this.status = status;
        this.lastModifiedOn = lastModifiedOn;
        this.isArchive = isArchive;
    }

    public HkUserOperationEntity(long userId, int eventCode, Date createdOn) {
        this.hkUserOperationEntityPK = new HkUserOperationEntityPK(userId, eventCode, createdOn);
    }

    public HkUserOperationEntityPK getHkUserOperationEntityPK() {
        return hkUserOperationEntityPK;
    }

    public void setHkUserOperationEntityPK(HkUserOperationEntityPK hkUserOperationEntityPK) {
        this.hkUserOperationEntityPK = hkUserOperationEntityPK;
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
    }

    public long getShiftDtl() {
        return shiftDtl;
    }

    public void setShiftDtl(long shiftDtl) {
        this.shiftDtl = shiftDtl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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

    public boolean getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(boolean isAttended) {
        this.isAttended = isAttended;
    }        

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkUserOperationEntityPK != null ? hkUserOperationEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkUserOperationEntity)) {
            return false;
        }
        HkUserOperationEntity other = (HkUserOperationEntity) object;
        if ((this.hkUserOperationEntityPK == null && other.hkUserOperationEntityPK != null) || (this.hkUserOperationEntityPK != null && !this.hkUserOperationEntityPK.equals(other.hkUserOperationEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserOperationEntity[ hkUserOperationEntityPK=" + hkUserOperationEntityPK + " ]";
    }

}
