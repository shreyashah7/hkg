/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Embeddable
public class RbEmailReportStatusEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "email_report_configuration", nullable = false)
    private long emailReportConfiguration;
    @Basic(optional = false)
    @Column(name = "on_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date onTime;
    @Basic(optional = false)
    @Column(name = "for_user", nullable = false)
    private long forUser;

    public RbEmailReportStatusEntityPK() {
    }

    public RbEmailReportStatusEntityPK(long emailReportConfiguration, Date onTime, long forUser) {
        this.emailReportConfiguration = emailReportConfiguration;
        this.onTime = onTime;
        this.forUser = forUser;
    }

    public long getEmailReportConfiguration() {
        return emailReportConfiguration;
    }

    public void setEmailReportConfiguration(long emailReportConfiguration) {
        this.emailReportConfiguration = emailReportConfiguration;
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
    }

    public long getForUser() {
        return forUser;
    }

    public void setForUser(long forUser) {
        this.forUser = forUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) emailReportConfiguration;
        hash += (onTime != null ? onTime.hashCode() : 0);
        hash += (int) forUser;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RbEmailReportStatusEntityPK)) {
            return false;
        }
        RbEmailReportStatusEntityPK other = (RbEmailReportStatusEntityPK) object;
        if (this.emailReportConfiguration != other.emailReportConfiguration) {
            return false;
        }
        if ((this.onTime == null && other.onTime != null) || (this.onTime != null && !this.onTime.equals(other.onTime))) {
            return false;
        }
        if (this.forUser != other.forUser) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.reportbuilder.model.RbEmailReportStatusEntityPK[ emailReportConfiguration=" + emailReportConfiguration + ", onTime=" + onTime + ", forUser=" + forUser + " ]";
    }
    
}
