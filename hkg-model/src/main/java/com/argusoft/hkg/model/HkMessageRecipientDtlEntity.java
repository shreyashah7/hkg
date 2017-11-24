/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class for storing user messages. Sender and Receiver information,
 * Replies are stored here.
 *
 * @author Mital
 */
@Entity
@Table(name = "hk_message_recipient_dtl")
public class HkMessageRecipientDtlEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkMessageRecipientDtlEntityPK hkMessageRecipientDtlPK;  //  includes messageObj and messageTo
    @Basic(optional = false)
    @Column(name = "message_from")
    private Long messageFrom;
    @Basic(optional = false)
    @Column(name = "has_priority")
    private boolean hasPriority;
    @Basic(optional = false)
    @Column(name = "is_attended")
    private boolean isAttended;
    @Column(name = "attended_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attendedOn;
    @Column(name = "replied_message_body",length = 500)
    private String repliedMessageBody;
    @Column(name = "replied_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date repliedOn;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive")
    private boolean isArchive;
    @JoinColumn(name = "message_obj", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkMessageEntity hkMessage;

    public HkMessageRecipientDtlEntity() {
    }

    public HkMessageRecipientDtlEntity(HkMessageRecipientDtlEntityPK hkMessageRecipientDtlPK) {
        this.hkMessageRecipientDtlPK = hkMessageRecipientDtlPK;
    }

    public HkMessageRecipientDtlEntity(HkMessageRecipientDtlEntityPK hkMessageRecipientDtlPK, Long messageFrom, boolean hasPriority, boolean isAttended, String status, boolean isArchive) {
        this.hkMessageRecipientDtlPK = hkMessageRecipientDtlPK;
        this.messageFrom = messageFrom;
        this.hasPriority = hasPriority;
        this.isAttended = isAttended;
        this.status = status;
        this.isArchive = isArchive;
    }

    public HkMessageRecipientDtlEntity(Long messageObj, Long messageTo) {
        this.hkMessageRecipientDtlPK = new HkMessageRecipientDtlEntityPK(messageObj, messageTo);
    }

    public HkMessageRecipientDtlEntityPK getHkMessageRecipientDtlPK() {
        return hkMessageRecipientDtlPK;
    }

    public void setHkMessageRecipientDtlPK(HkMessageRecipientDtlEntityPK hkMessageRecipientDtlPK) {
        this.hkMessageRecipientDtlPK = hkMessageRecipientDtlPK;
    }

    public Long getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(Long messageFrom) {
        this.messageFrom = messageFrom;
    }

    public boolean getHasPriority() {
        return hasPriority;
    }

    public void setHasPriority(boolean hasPriority) {
        this.hasPriority = hasPriority;
    }

    public boolean getIsAttended() {
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

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkMessageEntity getHkMessage() {
        return hkMessage;
    }

    public void setHkMessage(HkMessageEntity hkMessage) {
        this.hkMessage = hkMessage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkMessageRecipientDtlPK != null ? hkMessageRecipientDtlPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMessageRecipientDtlEntity)) {
            return false;
        }
        HkMessageRecipientDtlEntity other = (HkMessageRecipientDtlEntity) object;
        if ((this.hkMessageRecipientDtlPK == null && other.hkMessageRecipientDtlPK != null) || (this.hkMessageRecipientDtlPK != null && !this.hkMessageRecipientDtlPK.equals(other.hkMessageRecipientDtlPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkMessageRecipientDtl[ hkMessageRecipientDtlPK=" + hkMessageRecipientDtlPK + " ]";
    }

}
