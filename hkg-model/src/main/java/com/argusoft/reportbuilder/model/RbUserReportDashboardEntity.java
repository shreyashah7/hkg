/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.reportbuilder.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author gautam
 */
@Entity
@Table(name = "rb_user_report_dashboard_info")
public class RbUserReportDashboardEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RbUserReportDashboardEntityPK rbUserReportDashboardEntityPK;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name= "status",nullable = false, length = 10)
    private String status;

    public RbUserReportDashboardEntity() {
    }

    public RbUserReportDashboardEntity(RbUserReportDashboardEntityPK rbUserReportDashboardEntityPK) {
        this.rbUserReportDashboardEntityPK = rbUserReportDashboardEntityPK;
    }

    public RbUserReportDashboardEntity(RbUserReportDashboardEntityPK rbUserReportDashboardEntityPK, boolean isArchive, String status) {
        this.rbUserReportDashboardEntityPK = rbUserReportDashboardEntityPK;
        this.isArchive = isArchive;
        this.status = status;
    }

    public RbUserReportDashboardEntityPK getRbUserReportDashboardEntityPK() {
        return rbUserReportDashboardEntityPK;
    }

    public void setRbUserReportDashboardEntityPK(RbUserReportDashboardEntityPK rbUserReportDashboardEntityPK) {
        this.rbUserReportDashboardEntityPK = rbUserReportDashboardEntityPK;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rbUserReportDashboardEntityPK != null ? rbUserReportDashboardEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RbUserReportDashboardEntity)) {
            return false;
        }
        RbUserReportDashboardEntity other = (RbUserReportDashboardEntity) object;
        if ((this.rbUserReportDashboardEntityPK == null && other.rbUserReportDashboardEntityPK != null) || (this.rbUserReportDashboardEntityPK != null && !this.rbUserReportDashboardEntityPK.equals(other.rbUserReportDashboardEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RbUserReportDashboardEntity{" + "rbUserReportDashboardEntityPK=" + rbUserReportDashboardEntityPK + ", isArchive=" + isArchive + ", status=" + status + '}';
    }
    
    
}
