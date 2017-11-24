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
import javax.persistence.FetchType;
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
@Table(name = "hk_leave_info")
@NamedQueries({
    @NamedQuery(name = "HkLeaveEntity.findAll", query = "SELECT h FROM HkLeaveEntity h")})
public class HkLeaveEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "leave_reason")
    private Long leaveReason;
    @Column(length = 500)
    private String description;
    @Basic(optional = false)
    @Column(name = "for_user", nullable = false)
    private long forUser;
    @Basic(optional = false)
    @Column(name = "frm_dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date frmDt;
    @Column(name = "to_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_days", precision = 8, scale = 8)
    private Float totalDays;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Column(name = "final_remarks", length = 1000)
    private String finalRemarks;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Column(name = "created_by")
    private Long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "leaveRequest", fetch = FetchType.LAZY)
    private Set<HkLeaveApprovalEntity> hkLeaveApprovalEntitySet;

    public HkLeaveEntity() {
    }

    public HkLeaveEntity(Long id) {
        this.id = id;
    }

    public HkLeaveEntity(Long id, long forUser, Date frmDt, String status, boolean isArchive, Date createdOn, long franchise) {
        this.id = id;
        this.forUser = forUser;
        this.frmDt = frmDt;
        this.status = status;
        this.isArchive = isArchive;
        this.createdOn = createdOn;
        this.franchise = franchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(Long leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForUser() {
        return forUser;
    }

    public void setForUser(long forUser) {
        this.forUser = forUser;
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

    public Float getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Float totalDays) {
        this.totalDays = totalDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFinalRemarks() {
        return finalRemarks;
    }

    public void setFinalRemarks(String finalRemarks) {
        this.finalRemarks = finalRemarks;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public Set<HkLeaveApprovalEntity> getHkLeaveApprovalEntitySet() {
        return hkLeaveApprovalEntitySet;
    }

    public void setHkLeaveApprovalEntitySet(Set<HkLeaveApprovalEntity> hkLeaveApprovalEntitySet) {
        this.hkLeaveApprovalEntitySet = hkLeaveApprovalEntitySet;
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
        if (!(object instanceof HkLeaveEntity)) {
            return false;
        }
        HkLeaveEntity other = (HkLeaveEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkLeaveEntity[ id=" + id + " ]";
    }

}
