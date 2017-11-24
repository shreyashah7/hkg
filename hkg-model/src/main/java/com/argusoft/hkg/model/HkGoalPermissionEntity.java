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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "hk_goal_permission_info")
@NamedQueries({
    @NamedQuery(name = "HkGoalPermissionEntity.findAll", query = "SELECT h FROM HkGoalPermissionEntity h")})
public class HkGoalPermissionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(nullable = false)
    private long designation;
    @Basic(optional = false)
    @Column(name = "reference_type", nullable = false, length = 10)
    private String referenceType;
    @Basic(optional = false)
    @Column(name = "reference_instance", nullable = false)
    private long referenceInstance;
    @Basic(optional = false)
    @Column(name = "access_of_feature", nullable = false, length = 10)
    private String accessOfFeature;
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

    public HkGoalPermissionEntity() {
    }

    public HkGoalPermissionEntity(Long id) {
        this.id = id;
    }

    public HkGoalPermissionEntity(Long id, long designation, String referenceType, long referenceInstance, String accessOfFeature, boolean isArchive, long lastModifiedBy, Date lastModifiedOn) {
        this.id = id;
        this.designation = designation;
        this.referenceType = referenceType;
        this.referenceInstance = referenceInstance;
        this.accessOfFeature = accessOfFeature;
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

    public long getDesignation() {
        return designation;
    }

    public void setDesignation(long designation) {
        this.designation = designation;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public long getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(long referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    public String getAccessOfFeature() {
        return accessOfFeature;
    }

    public void setAccessOfFeature(String accessOfFeature) {
        this.accessOfFeature = accessOfFeature;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkGoalPermissionEntity)) {
            return false;
        }
        HkGoalPermissionEntity other = (HkGoalPermissionEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkGoalPermissionEntity[ id=" + id + " ]";
    }
    
}
