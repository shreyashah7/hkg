/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author harshit
 */
@Document(collection = "notificationconfigurationrecipient")
public class HkNotificationConfigrationRecipientDocument {

    @Id
    private String id;
    private Long referenceInstance;
    private String referenceType;
    private Boolean isArchive;
    @DBRef
    private HkNotificationConfigurationDocument hkNotificationConfigurationDocument;
    private Date lastModifiedOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(Long referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkNotificationConfigurationDocument getHkNotificationConfigurationDocument() {
        return hkNotificationConfigurationDocument;
    }

    public void setHkNotificationConfigurationDocument(HkNotificationConfigurationDocument hkNotificationConfigurationDocument) {
        this.hkNotificationConfigurationDocument = hkNotificationConfigurationDocument;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @Override
    public String toString() {
        return "HkNotificationConfigrationRecipientDocument{" + "id=" + id + ", referenceInstance=" + referenceInstance + ", referenceType=" + referenceType + ", isArchive=" + isArchive + ", hkNotificationConfigurationDocument=" + hkNotificationConfigurationDocument + ", lastModifiedOn=" + lastModifiedOn + '}';
    }

}
