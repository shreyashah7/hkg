/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "hk_system_key_mst")
@NamedQueries({
    @NamedQuery(name = "HkMasterEntity.findAll", query = "SELECT h FROM HkMasterEntity h")})
public class HkMasterEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 50, unique = true)
    private String code;
    @Basic(optional = false)
    @Column(name = "master_name", nullable = false, length = 100)
    private String masterName;
    @Column(name = "master_type", length = 5)
    private String masterType;
    @Column(length = 1000)
    private String description;
    @Basic(optional = false)
    @Column(nullable = false)
    private short precedence;
    @Basic(optional = false)
    @Column(name = "is_sensitive", nullable = false)
    private boolean isSensitive;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    private long franchise;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keyCode")
    private List<HkValueEntity> hkValueEntityList;

    public HkMasterEntity() {
    }

    public HkMasterEntity(String code) {
        this.code = code;
    }

    public HkMasterEntity(String code, String masterName, short precedence, boolean isSensitive, Date createdOn, long createdBy, long lastModifiedBy, Date lastModifiedOn, boolean isArchive) {
        this.code = code;
        this.masterName = masterName;
        this.precedence = precedence;
        this.isSensitive = isSensitive;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.isArchive = isArchive;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterType() {
        return masterType;
    }

    /**
     * Use this method with causion. This is the only way we track
     * custom/built-in masters and the type is not modifiable.
     *
     * @param masterType
     */
    public void setMasterType(String masterType) {
        this.masterType = masterType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getPrecedence() {
        return precedence;
    }

    public void setPrecedence(short precedence) {
        this.precedence = precedence;
    }

    public boolean getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
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

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<HkValueEntity> getHkValueEntityList() {
        return hkValueEntityList;
    }

    public void setHkValueEntityList(List<HkValueEntity> hkValueEntityList) {
        this.hkValueEntityList = hkValueEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkMasterEntity)) {
            return false;
        }
        HkMasterEntity other = (HkMasterEntity) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkKeyEntity[ code=" + code + " ]";
    }

    @Override
    public HkMasterEntity clone() throws CloneNotSupportedException {
        HkMasterEntity hkMasterEntity = (HkMasterEntity) super.clone();

        hkMasterEntity.setHkValueEntityList(new ArrayList<>(this.hkValueEntityList));

        return hkMasterEntity;
    }

}
