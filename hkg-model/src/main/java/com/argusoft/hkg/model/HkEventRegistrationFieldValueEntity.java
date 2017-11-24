/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_event_registration_field_value_info")
@NamedQueries({
    @NamedQuery(name = "HkEventRegistrationFieldValueEntity.findAll", query = "SELECT h FROM HkEventRegistrationFieldValueEntity h")})
public class HkEventRegistrationFieldValueEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkEventRegistrationFieldValueEntityPK hkEventRegistrationFieldValueEntityPK;
    @Basic(optional = false)
    @Column(name = "field_value", nullable = false, length = 1000)
    private String fieldValue;
    @Basic(optional = false)
    @Column(nullable = false)
    private long event;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @JoinColumn(name = "registration_field", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkEventRegistrationFieldEntity hkEventRegistrationFieldEntity;

    public HkEventRegistrationFieldValueEntity() {
    }

    public HkEventRegistrationFieldValueEntity(HkEventRegistrationFieldValueEntityPK hkEventRegistrationFieldValueEntityPK) {
        this.hkEventRegistrationFieldValueEntityPK = hkEventRegistrationFieldValueEntityPK;
    }

    public HkEventRegistrationFieldValueEntity(HkEventRegistrationFieldValueEntityPK hkEventRegistrationFieldValueEntityPK, String fieldValue, long event, boolean isArchive) {
        this.hkEventRegistrationFieldValueEntityPK = hkEventRegistrationFieldValueEntityPK;
        this.fieldValue = fieldValue;
        this.event = event;
        this.isArchive = isArchive;
    }

    public HkEventRegistrationFieldValueEntity(long registrationField, long userId) {
        this.hkEventRegistrationFieldValueEntityPK = new HkEventRegistrationFieldValueEntityPK(registrationField, userId);
    }

    public HkEventRegistrationFieldValueEntityPK getHkEventRegistrationFieldValueEntityPK() {
        return hkEventRegistrationFieldValueEntityPK;
    }

    public void setHkEventRegistrationFieldValueEntityPK(HkEventRegistrationFieldValueEntityPK hkEventRegistrationFieldValueEntityPK) {
        this.hkEventRegistrationFieldValueEntityPK = hkEventRegistrationFieldValueEntityPK;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkEventRegistrationFieldEntity getHkEventRegistrationFieldEntity() {
        return hkEventRegistrationFieldEntity;
    }

    public void setHkEventRegistrationFieldEntity(HkEventRegistrationFieldEntity hkEventRegistrationFieldEntity) {
        this.hkEventRegistrationFieldEntity = hkEventRegistrationFieldEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkEventRegistrationFieldValueEntityPK != null ? hkEventRegistrationFieldValueEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkEventRegistrationFieldValueEntity)) {
            return false;
        }
        HkEventRegistrationFieldValueEntity other = (HkEventRegistrationFieldValueEntity) object;
        if ((this.hkEventRegistrationFieldValueEntityPK == null && other.hkEventRegistrationFieldValueEntityPK != null) || (this.hkEventRegistrationFieldValueEntityPK != null && !this.hkEventRegistrationFieldValueEntityPK.equals(other.hkEventRegistrationFieldValueEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRegistrationFieldValueEntity[ hkEventRegistrationFieldValueEntityPK=" + hkEventRegistrationFieldValueEntityPK + " ]";
    }
    
}
