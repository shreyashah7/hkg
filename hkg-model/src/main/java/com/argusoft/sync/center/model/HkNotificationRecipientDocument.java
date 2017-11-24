/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "notificationrecipient")
public class HkNotificationRecipientDocument {

    private static final long serialVersionUID = 1L;
    @Id
    String id;
    protected HkNotificationRecipientDocumentPK hkNotificationRecipientEntityPK;
    private Boolean isSeen;
    private Date seenOn;
    private String status;
    private Boolean isArchive;
    private Date lastModifiedOn;
    private HkNotificationDocument hkNotificationEntity;

    public HkNotificationRecipientDocument() {
    }

    public HkNotificationRecipientDocument(String notification, long forUser) {
        this.hkNotificationRecipientEntityPK = new HkNotificationRecipientDocumentPK(notification, forUser);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HkNotificationRecipientDocumentPK getHkNotificationRecipientEntityPK() {
        return hkNotificationRecipientEntityPK;
    }

    public void setHkNotificationRecipientEntityPK(HkNotificationRecipientDocumentPK hkNotificationRecipientEntityPK) {
        this.hkNotificationRecipientEntityPK = hkNotificationRecipientEntityPK;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
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

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public HkNotificationDocument getHkNotificationEntity() {
        return hkNotificationEntity;
    }

    public void setHkNotificationEntity(HkNotificationDocument hkNotificationEntity) {
        this.hkNotificationEntity = hkNotificationEntity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.hkNotificationRecipientEntityPK);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HkNotificationRecipientDocument other = (HkNotificationRecipientDocument) obj;
        if (!Objects.equals(this.hkNotificationRecipientEntityPK, other.hkNotificationRecipientEntityPK)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HkNotificationRecipientDocument{" + "id=" + id + ", hkNotificationRecipientEntityPK=" + hkNotificationRecipientEntityPK + ", isSeen=" + isSeen + ", seenOn=" + seenOn + ", status=" + status + ", isArchive=" + isArchive + ", lastModifiedOn=" + lastModifiedOn + ", hkNotificationEntity=" + hkNotificationEntity + '}';
    }

}
