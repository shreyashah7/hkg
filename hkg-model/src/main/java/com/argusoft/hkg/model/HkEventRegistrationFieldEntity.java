/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_event_registration_field_info")
@NamedQueries({
    @NamedQuery(name = "HkEventRegistrationFieldEntity.findAll", query = "SELECT h FROM HkEventRegistrationFieldEntity h")})
public class HkEventRegistrationFieldEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "field_name", nullable = false, length = 500)
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "component_type", nullable = false, length = 10)
    private String componentType;
    @Column(name = "default_values", length = 1000)
    private String defaultValues;
    @Column(name = "validation_pattern", length = 1000)
    private String validationPattern;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkEventRegistrationFieldEntity")
    private Set<HkEventRegistrationFieldValueEntity> hkEventRegistrationFieldValueEntitySet;
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkEventEntity event;

    public HkEventRegistrationFieldEntity() {
    }

    public HkEventRegistrationFieldEntity(Long id) {
        this.id = id;
    }

    public HkEventRegistrationFieldEntity(Long id, String fieldName, String componentType, boolean isArchive, long lastModifiedBy, Date lastModifiedOn) {
        this.id = id;
        this.fieldName = fieldName;
        this.componentType = componentType;
        this.isArchive = isArchive;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(String defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Set<HkEventRegistrationFieldValueEntity> getHkEventRegistrationFieldValueEntitySet() {
        return hkEventRegistrationFieldValueEntitySet;
    }

    public void setHkEventRegistrationFieldValueEntitySet(Set<HkEventRegistrationFieldValueEntity> hkEventRegistrationFieldValueEntitySet) {
        this.hkEventRegistrationFieldValueEntitySet = hkEventRegistrationFieldValueEntitySet;
    }

    public HkEventEntity getEvent() {
        return event;
    }

    public void setEvent(HkEventEntity event) {
        this.event = event;
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
        if (!(object instanceof HkEventRegistrationFieldEntity)) {
            return false;
        }
        HkEventRegistrationFieldEntity other = (HkEventRegistrationFieldEntity) object;
        if ((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkEventRegistrationFieldEntity[ id=" + id + " ]";
    }
    
}
