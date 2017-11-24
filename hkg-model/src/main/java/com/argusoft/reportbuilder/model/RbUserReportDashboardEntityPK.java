/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.reportbuilder.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author gautam
 */
@Embeddable
public class RbUserReportDashboardEntityPK implements Serializable {
    
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic(optional = false)
    @Column(name = "report", nullable = false)
    private long reportId;

    public RbUserReportDashboardEntityPK() {
    }

    public RbUserReportDashboardEntityPK(long userId, long reportId) {
        this.userId = userId;
        this.reportId = reportId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) reportId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RbUserReportDashboardEntityPK other = (RbUserReportDashboardEntityPK) obj;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.reportId != other.reportId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RbUserReportDashboardEntityPK{" + "userId=" + userId + ", reportId=" + reportId + '}';
    }
    
}
