/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_system_values_mst")
@NamedQueries({
    @NamedQuery(name = "HkValueEntity.findAll", query = "SELECT h FROM HkValueEntity h")})
public class HkValueEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "shortcut_code")
    private Integer shortcutCode;
    @Basic(optional = false)
    @Column(name = "value_name", nullable = false, length = 1000)
    private String valueName;
    @Column(name = "translated_value_name", length = 1000)
    private String translatedValueName;
    @Basic(optional = false)
    @Column(name = "is_often_used", nullable = false)
    private boolean isOftenUsed;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Column(name = "created_by_franchise")
    private Long createdByFranchise;
    @JoinColumn(name = "key_code", referencedColumnName = "code", nullable = false)
    @ManyToOne(optional = false)
    private HkMasterEntity keyCode;
    @Transient
    private Map<String, String> translateValueMap;

    public HkValueEntity() {
    }

    public HkValueEntity(Long id) {
        this.id = id;
    }

    public String getTranslatedValueName() {
        return translatedValueName;
    }

    public void setTranslatedValueName(String translatedValueName) {
        this.translatedValueName = translatedValueName;
    }

    public Map<String, String> getTranslateValueMap() {
        return translateValueMap;
    }

    public void setTranslateValueMap(Map<String, String> translateValueMap) {
        this.translateValueMap = translateValueMap;
    }

    public HkValueEntity(Long id, Integer shortcutCode, String valueName,
            boolean isOftenUsed, long franchise, boolean isActive,
            boolean isArchive, long createdBy, Date createdOn,
            long lastModifiedBy, Date lastModifiedOn, HkMasterEntity keyCode) {
        super();
        this.id = id;
        this.shortcutCode = shortcutCode;
        this.valueName = valueName;
        this.isOftenUsed = isOftenUsed;
        this.franchise = franchise;
        this.isActive = isActive;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.keyCode = keyCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShortcutCode() {
        return shortcutCode;
    }

    public void setShortcutCode(Integer shortcutCode) {
        this.shortcutCode = shortcutCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public boolean getIsOftenUsed() {
        return isOftenUsed;
    }

    public void setIsOftenUsed(boolean isOftenUsed) {
        this.isOftenUsed = isOftenUsed;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public HkMasterEntity getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(HkMasterEntity keyCode) {
        this.keyCode = keyCode;
    }

    public Long getCreatedByFranchise() {
        return createdByFranchise;
    }

    public void setCreatedByFranchise(Long createdByFranchise) {
        this.createdByFranchise = createdByFranchise;
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
        if (!(object instanceof HkValueEntity)) {
            return false;
        }
        HkValueEntity other = (HkValueEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkValueEntity[ id=" + id + " ]";
    }

    @Override
    public HkValueEntity clone() throws CloneNotSupportedException {
        return (HkValueEntity) super.clone();
    }

}
