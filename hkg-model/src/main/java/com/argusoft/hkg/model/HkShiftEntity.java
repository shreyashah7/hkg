/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Set;
import java.util.Date;
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
@Table(name = "hk_system_shift_info")
@NamedQueries({
    @NamedQuery(name = "HkShiftEntity.findAll", query = "SELECT h FROM HkShiftEntity h")})
public class HkShiftEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "shift_title", nullable = false, length = 100)
    private String shiftTitle;
    @Column(name = "frm_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frmDt;
    @Column(name = "to_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDt;
    @Basic(optional = false)
    @Column(name = "week_days", nullable = false, length = 100)
    private String weekDays;
    @Basic(optional = false)
    @Column(name = "has_rule", nullable = false)
    private boolean hasRule;
    @Basic(optional = false)
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
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
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkShiftEntity")
    private Set<HkShiftRuleEntity> hkShiftRuleEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "temporaryShiftFor")
    private Set<HkShiftEntity> hkShiftEntitySet;
    @JoinColumn(name = "temporary_shift_for", referencedColumnName = "id")
    @ManyToOne
    private HkShiftEntity temporaryShiftFor;
    @JoinColumn(name = "override_shift_for", referencedColumnName = "id")
    @ManyToOne
    private HkShiftEntity overrideShiftFor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "overrideShiftFor")
    private Set<HkShiftEntity> hkOverrideShiftEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkShiftEntity")
    private Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shift")
    private Set<HkShiftDtlEntity> hkShiftDtlEntitySet;

    public HkShiftEntity() {
    }

    public HkShiftEntity(Long id) {
        this.id = id;
    }

    public HkShiftEntity(Long id, String shiftTitle, String weekDays, boolean hasRule, boolean isDefault, String status, boolean isArchive, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, long franchise) {
        this.id = id;
        this.shiftTitle = shiftTitle;
        this.weekDays = weekDays;
        this.hasRule = hasRule;
        this.isDefault = isDefault;
        this.status = status;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.franchise = franchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShiftTitle() {
        return shiftTitle;
    }

    public void setShiftTitle(String shiftTitle) {
        this.shiftTitle = shiftTitle;
    }

    public Date getFrmDt() {
        return frmDt;
    }

    public void setFrmDt(Date frmDt) {
        this.frmDt = frmDt;
    }

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public boolean getHasRule() {
        return hasRule;
    }

    public void setHasRule(boolean hasRule) {
        this.hasRule = hasRule;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
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

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public Set<HkShiftRuleEntity> getHkShiftRuleEntitySet() {
        return hkShiftRuleEntitySet;
    }

    public void setHkShiftRuleEntitySet(Set<HkShiftRuleEntity> hkShiftRuleEntitySet) {
        this.hkShiftRuleEntitySet = hkShiftRuleEntitySet;
    }

    public Set<HkShiftEntity> getHkShiftEntitySet() {
        return hkShiftEntitySet;
    }

    public void setHkShiftEntitySet(Set<HkShiftEntity> hkShiftEntitySet) {
        this.hkShiftEntitySet = hkShiftEntitySet;
    }

    public HkShiftEntity getTemporaryShiftFor() {
        return temporaryShiftFor;
    }

    public void setTemporaryShiftFor(HkShiftEntity temporaryShiftFor) {
        this.temporaryShiftFor = temporaryShiftFor;
    }

    public Set<HkShiftDepartmentEntity> getHkShiftDepartmentEntitySet() {
        return hkShiftDepartmentEntitySet;
    }

    public void setHkShiftDepartmentEntitySet(Set<HkShiftDepartmentEntity> hkShiftDepartmentEntitySet) {
        this.hkShiftDepartmentEntitySet = hkShiftDepartmentEntitySet;
    }

    public Set<HkShiftDtlEntity> getHkShiftDtlEntitySet() {
        return hkShiftDtlEntitySet;
    }

    public void setHkShiftDtlEntitySet(Set<HkShiftDtlEntity> hkShiftDtlEntitySet) {
        this.hkShiftDtlEntitySet = hkShiftDtlEntitySet;
    }

    public HkShiftEntity getOverrideShiftFor() {
        return overrideShiftFor;
    }

    public void setOverrideShiftFor(HkShiftEntity overrideShiftFor) {
        this.overrideShiftFor = overrideShiftFor;
    }

    public Set<HkShiftEntity> getHkOverrideShiftEntitySet() {
        return hkOverrideShiftEntitySet;
    }

    public void setHkOverrideShiftEntitySet(Set<HkShiftEntity> hkOverrideShiftEntitySet) {
        this.hkOverrideShiftEntitySet = hkOverrideShiftEntitySet;
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
        if (!(object instanceof HkShiftEntity)) {
            return false;
        }
        HkShiftEntity other = (HkShiftEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftEntity[ id=" + id + " ]";
    }
    
}
