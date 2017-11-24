/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Primary key class for HkMessageRecipientDtl.
 *
 * @author Mital
 */
@Embeddable
public class HkMessageRecipientDtlEntityPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "message_obj")
    private Long messageObj;
    @Basic(optional = false)
    @Column(name = "message_to")
    private Long messageTo;

    public HkMessageRecipientDtlEntityPK() {
    }

    public HkMessageRecipientDtlEntityPK(Long messageObj, Long messageTo) {
        this.messageObj = messageObj;
        this.messageTo = messageTo;
    }

    public Long getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(Long messageObj) {
        this.messageObj = messageObj;
    }

    public Long getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(Long messageTo) {
        this.messageTo = messageTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (long) messageObj;
        hash += (long) messageTo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMessageRecipientDtlEntityPK)) {
            return false;
        }
        HkMessageRecipientDtlEntityPK other = (HkMessageRecipientDtlEntityPK) object;
        if (this.messageObj != other.messageObj) {
            return false;
        }
        if (this.messageTo != other.messageTo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkMessageRecipientDtlPK[ messageObj=" + messageObj + ", messageTo=" + messageTo + " ]";
    }

}
