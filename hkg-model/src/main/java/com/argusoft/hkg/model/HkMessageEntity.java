/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class for storing messages. This is the main class that stores the
 * details objects by joins.
 *
 * @author Mital
 */
@Entity
@Table(name = "hk_system_message_info")
public class HkMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "message_body", length = 500)
    private String messageBody;
    @Column(name = "copy_message", length = 500)
    private String copyMessage;
    @Basic(optional = false)
    @Column(name = "has_priority")
    private boolean hasPriority;
    @Basic(optional = false)
    @Column(nullable = false, length = 1)
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive")
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "created_by")
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "franchise")
    private Long franchise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkMessage")
    private List<HkMessageRecipientEntity> hkMessageRecipientList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkMessage")
    private List<HkMessageRecipientDtlEntity> hkMessageRecipientDtlList;

    public HkMessageEntity() {
    }

    public HkMessageEntity(Long id) {
        this.id = id;
    }

    public HkMessageEntity(Long id, boolean hasPriority, String status, boolean isArchive, long createdBy, Date createdOn, Long franchise) {
        this.id = id;
        this.hasPriority = hasPriority;
        this.status = status;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.franchise = franchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getCopyMessage() {
        return copyMessage;
    }

    public void setCopyMessage(String copyMessage) {
        this.copyMessage = copyMessage;
    }

    public boolean getHasPriority() {
        return hasPriority;
    }

    public void setHasPriority(boolean hasPriority) {
        this.hasPriority = hasPriority;
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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    @XmlTransient
    public List<HkMessageRecipientEntity> getHkMessageRecipientList() {
        return hkMessageRecipientList;
    }

    public void setHkMessageRecipientList(List<HkMessageRecipientEntity> hkMessageRecipientList) {
        this.hkMessageRecipientList = hkMessageRecipientList;
    }

    @XmlTransient
    public List<HkMessageRecipientDtlEntity> getHkMessageRecipientDtlList() {
        return hkMessageRecipientDtlList;
    }

    public void setHkMessageRecipientDtlList(List<HkMessageRecipientDtlEntity> hkMessageRecipientDtlList) {
        this.hkMessageRecipientDtlList = hkMessageRecipientDtlList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMessageEntity)) {
            return false;
        }
        HkMessageEntity other = (HkMessageEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkSystemMessageInfo[ id=" + id + " ]";
    }

}
