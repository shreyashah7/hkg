package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_notification_user_rel_info")
public class HkNotificationRecipientEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK;
    @Basic(optional = false)
    @Column(name = "is_seen", nullable = false)
    private boolean isSeen;
    @Column(name = "seen_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date seenOn;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @MapsId("notification")
    @JoinColumn(name = "notification", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkNotificationEntity hkNotificationEntity;

    public HkNotificationRecipientEntity() {
    }

    public HkNotificationRecipientEntity(HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK) {
        this.hkNotificationRecipientEntityPK = hkNotificationRecipientEntityPK;
    }

    public HkNotificationRecipientEntity(HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK, boolean isSeen, String status, boolean isArchive, Date lastModifiedOn) {
        this.hkNotificationRecipientEntityPK = hkNotificationRecipientEntityPK;
        this.isSeen = isSeen;
        this.status = status;
        this.isArchive = isArchive;
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkNotificationRecipientEntity(String notification, long forUser) {
        this.hkNotificationRecipientEntityPK = new HkNotificationRecipientEntityPK(notification, forUser);
    }

    public HkNotificationRecipientEntityPK getHkNotificationRecipientEntityPK() {
        return hkNotificationRecipientEntityPK;
    }

    public void setHkNotificationRecipientEntityPK(HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK) {
        this.hkNotificationRecipientEntityPK = hkNotificationRecipientEntityPK;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(Date seenOn) {
        this.seenOn = seenOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public HkNotificationEntity getHkNotificationEntity() {
        return hkNotificationEntity;
    }

    public void setHkNotificationEntity(HkNotificationEntity hkNotificationEntity) {
        this.hkNotificationEntity = hkNotificationEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkNotificationRecipientEntityPK != null ? hkNotificationRecipientEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkNotificationRecipientEntity)) {
            return false;
        }
        HkNotificationRecipientEntity other = (HkNotificationRecipientEntity) object;
        if ((this.hkNotificationRecipientEntityPK == null && other.hkNotificationRecipientEntityPK != null) || (this.hkNotificationRecipientEntityPK != null && !this.hkNotificationRecipientEntityPK.equals(other.hkNotificationRecipientEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationRecipientEntity[ hkNotificationRecipientEntityPK=" + hkNotificationRecipientEntityPK + " ]";
    }

}