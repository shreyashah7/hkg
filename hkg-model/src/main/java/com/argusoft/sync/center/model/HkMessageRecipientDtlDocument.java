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
 * @author akta
 */
@Document(collection = "message_recipient_dtl")
public class HkMessageRecipientDtlDocument {
    @Id
    String id;
    protected HkMessageRecipientDtlDocumentPk hkMessageRecipientDtlPK;  //  includes messageObj and messageTo
    private Long messageFrom;
    private boolean hasPriority;
    private boolean isAttended;
    private Date attendedOn;
    private String repliedMessageBody;
    private Date repliedOn;
    private String status;
    private boolean isArchive;
    @DBRef
    private HkMessageDocument hkMessage;
    private Date lastModifiedOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HkMessageRecipientDtlDocumentPk getHkMessageRecipientDtlPK() {
        return hkMessageRecipientDtlPK;
    }

    public void setHkMessageRecipientDtlPK(HkMessageRecipientDtlDocumentPk hkMessageRecipientDtlPK) {
        this.hkMessageRecipientDtlPK = hkMessageRecipientDtlPK;
    }

    public Long getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(Long messageFrom) {
        this.messageFrom = messageFrom;
    }

    public boolean isHasPriority() {
        return hasPriority;
    }

    public void setHasPriority(boolean hasPriority) {
        this.hasPriority = hasPriority;
    }

    public boolean isIsAttended() {
        return isAttended;
    }

    public void setIsAttended(boolean isAttended) {
        this.isAttended = isAttended;
    }

    public Date getAttendedOn() {
        return attendedOn;
    }

    public void setAttendedOn(Date attendedOn) {
        this.attendedOn = attendedOn;
    }

    public String getRepliedMessageBody() {
        return repliedMessageBody;
    }

    public void setRepliedMessageBody(String repliedMessageBody) {
        this.repliedMessageBody = repliedMessageBody;
    }

    public Date getRepliedOn() {
        return repliedOn;
    }

    public void setRepliedOn(Date repliedOn) {
        this.repliedOn = repliedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkMessageDocument getHkMessage() {
        return hkMessage;
    }

    public void setHkMessage(HkMessageDocument hkMessage) {
        this.hkMessage = hkMessage;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

}
