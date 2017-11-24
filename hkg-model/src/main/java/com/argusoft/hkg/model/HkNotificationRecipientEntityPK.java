package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Mayank
 */
@Embeddable
public class HkNotificationRecipientEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private String notification;
    @Basic(optional = false)
    @Column(name = "for_user", nullable = false)
    private long forUser;

    public HkNotificationRecipientEntityPK() {
    }

    public HkNotificationRecipientEntityPK(String notification, long forUser) {
        this.notification = notification;
        this.forUser = forUser;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public long getForUser() {
        return forUser;
    }

    public void setForUser(long forUser) {
        this.forUser = forUser;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.notification);
        hash = 89 * hash + (int) (this.forUser ^ (this.forUser >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkNotificationRecipientEntityPK)) {
            return false;
        }
        HkNotificationRecipientEntityPK other = (HkNotificationRecipientEntityPK) object;
        if (this.notification != other.notification) {
            return false;
        }
        if (this.forUser != other.forUser) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkNotificationRecipientEntityPK[ notification=" + notification + ", forUser=" + forUser + " ]";
    }

}