/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_leave_approval_info")
@NamedQueries({
    @NamedQuery(name = "HkLeaveApprovalEntity.findAll", query = "SELECT h FROM HkLeaveApprovalEntity h")})
public class HkLeaveApprovalEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "on_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date onDate;
    @Basic(optional = false)
    @Column(nullable = false)
    private int level;
    @Column(length = 1000)
    private String remarks;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Column(name = "attended_by")
    private Long attendedBy;
    @Column(name = "attended_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attendedOn;
    @Basic(optional = false)
    @Column(nullable = false)
    private long workflow;
    @JoinColumn(name = "leave_request", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkLeaveEntity leaveRequest;

    public HkLeaveApprovalEntity() {
    }

    public HkLeaveApprovalEntity(Long id) {
        this.id = id;
    }

    public HkLeaveApprovalEntity(Long id, int level, String status, long workflow) {
        this.id = id;
        this.level = level;
        this.status = status;
        this.workflow = workflow;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(Long attendedBy) {
        this.attendedBy = attendedBy;
    }

    public Date getAttendedOn() {
        return attendedOn;
    }

    public void setAttendedOn(Date attendedOn) {
        this.attendedOn = attendedOn;
    }

    public long getWorkflow() {
        return workflow;
    }

    public void setWorkflow(long workflow) {
        this.workflow = workflow;
    }

    public HkLeaveEntity getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(HkLeaveEntity leaveRequest) {
        this.leaveRequest = leaveRequest;
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
        if (!(object instanceof HkLeaveApprovalEntity)) {
            return false;
        }
        HkLeaveApprovalEntity other = (HkLeaveApprovalEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkLeaveApprovalEntity[ id=" + id + " ]";
    }
    
}
