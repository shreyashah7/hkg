package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_franchise_min_req_info")
@NamedQueries({
    @NamedQuery(name = "HkFranchiseRequirementEntity.findAll", query = "SELECT h FROM HkFranchiseRequirementEntity h")})
public class HkFranchiseRequirementEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "req_id", nullable = false)
    private long reqId;
    @Basic(optional = false)
    @Column(name = "req_name", nullable = false, length = 100)
    private String reqName;
    @Basic(optional = false)
    @Column(name = "req_type", nullable = false, length = 5)
    private String reqType;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Column(name = "required_value")
    private Integer requiredValue;
    @Column(name = "acquired_value")
    private Integer acquiredValue;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

    public HkFranchiseRequirementEntity() {
    }

    public HkFranchiseRequirementEntity(Long id) {
        this.id = id;
    }

    public HkFranchiseRequirementEntity(Long id, long reqId, String reqName, String reqType, long franchise, boolean isArchive) {
        this.id = id;
        this.reqId = reqId;
        this.reqName = reqName;
        this.reqType = reqType;
        this.franchise = franchise;
        this.isArchive = isArchive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public Integer getRequiredValue() {
        return requiredValue;
    }

    public void setRequiredValue(Integer requiredValue) {
        this.requiredValue = requiredValue;
    }

    public Integer getAcquiredValue() {
        return acquiredValue;
    }

    public void setAcquiredValue(Integer acquiredValue) {
        this.acquiredValue = acquiredValue;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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
        if (!(object instanceof HkFranchiseRequirementEntity)) {
            return false;
        }
        HkFranchiseRequirementEntity other = (HkFranchiseRequirementEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkFranchiseRequirementEntity[ id=" + id + " ]";
    }

}