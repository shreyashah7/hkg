/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_user_histry_dtl")
@NamedQueries({
    @NamedQuery(name = "HkUserWorkHistoryEntity.findAll", query = "SELECT h FROM HkUserWorkHistoryEntity h")})
public class HkUserWorkHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic(optional = false)
    @Column(nullable = false)
    private long department;
    @Basic(optional = false)
    @Column(nullable = false)
    private long shift;
    @Column(name = "effected_frm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectedFrm;
    @Column(name = "effected_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectedTo;
    private String designation;
    @Column(name = "reports_to")
    private String reportsTo;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @OneToMany(mappedBy = "linkedWith")
    private Set<HkUserWorkHistoryEntity> hkUserWorkHistoryEntitySet;
    @JoinColumn(name = "linked_with", referencedColumnName = "id")
    @ManyToOne
    private HkUserWorkHistoryEntity linkedWith;

    public HkUserWorkHistoryEntity() {
    }

    public HkUserWorkHistoryEntity(Long id) {
        this.id = id;
    }

    public HkUserWorkHistoryEntity(Long id, long userId, long department, long shift, boolean isArchive) {
        this.id = id;
        this.userId = userId;
        this.department = department;
        this.shift = shift;
        this.isArchive = isArchive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDepartment() {
        return department;
    }

    public void setDepartment(long department) {
        this.department = department;
    }

    public long getShift() {
        return shift;
    }

    public void setShift(long shift) {
        this.shift = shift;
    }

    public Date getEffectedFrm() {
        return effectedFrm;
    }

    public void setEffectedFrm(Date effectedFrm) {
        this.effectedFrm = effectedFrm;
    }

    public Date getEffectedTo() {
        return effectedTo;
    }

    public void setEffectedTo(Date effectedTo) {
        this.effectedTo = effectedTo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Set<HkUserWorkHistoryEntity> getHkUserWorkHistoryEntitySet() {
        return hkUserWorkHistoryEntitySet;
    }

    public void setHkUserWorkHistoryEntitySet(Set<HkUserWorkHistoryEntity> hkUserWorkHistoryEntitySet) {
        this.hkUserWorkHistoryEntitySet = hkUserWorkHistoryEntitySet;
    }

    public HkUserWorkHistoryEntity getLinkedWith() {
        return linkedWith;
    }

    public void setLinkedWith(HkUserWorkHistoryEntity linkedWith) {
        this.linkedWith = linkedWith;
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
        if (!(object instanceof HkUserWorkHistoryEntity)) {
            return false;
        }
        HkUserWorkHistoryEntity other = (HkUserWorkHistoryEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkUserWorkHistoryEntity[ id=" + id + " ]";
    }
    
}
