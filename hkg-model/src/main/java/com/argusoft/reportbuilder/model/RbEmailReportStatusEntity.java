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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "rb_email_report_status_info")
public class RbEmailReportStatusEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RbEmailReportStatusEntityPK rbEmailReportStatusEntityPK;
    @Basic(optional = false)
    @Column(name = "email_sent", nullable = false)
    private boolean emailSent;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "generated_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date generatedOn;

    public RbEmailReportStatusEntity() {
    }

    public RbEmailReportStatusEntity(RbEmailReportStatusEntityPK rbEmailReportStatusEntityPK) {
        this.rbEmailReportStatusEntityPK = rbEmailReportStatusEntityPK;
    }

    public RbEmailReportStatusEntity(RbEmailReportStatusEntityPK rbEmailReportStatusEntityPK, boolean emailSent, String status, Date generatedOn) {
        this.rbEmailReportStatusEntityPK = rbEmailReportStatusEntityPK;
        this.emailSent = emailSent;
        this.status = status;
        this.generatedOn = generatedOn;
    }

    public RbEmailReportStatusEntity(long emailReportConfiguration, Date onTime, long forUser) {
        this.rbEmailReportStatusEntityPK = new RbEmailReportStatusEntityPK(emailReportConfiguration, onTime, forUser);
    }

    public RbEmailReportStatusEntityPK getRbEmailReportStatusEntityPK() {
        return rbEmailReportStatusEntityPK;
    }

    public void setRbEmailReportStatusEntityPK(RbEmailReportStatusEntityPK rbEmailReportStatusEntityPK) {
        this.rbEmailReportStatusEntityPK = rbEmailReportStatusEntityPK;
    }

    public boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGeneratedOn() {
        return generatedOn;
    }

    public void setGeneratedOn(Date generatedOn) {
        this.generatedOn = generatedOn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rbEmailReportStatusEntityPK != null ? rbEmailReportStatusEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RbEmailReportStatusEntity)) {
            return false;
        }
        RbEmailReportStatusEntity other = (RbEmailReportStatusEntity) object;
        if ((this.rbEmailReportStatusEntityPK == null && other.rbEmailReportStatusEntityPK != null) || (this.rbEmailReportStatusEntityPK != null && !this.rbEmailReportStatusEntityPK.equals(other.rbEmailReportStatusEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.reportbuilder.model.RbEmailReportStatusEntity[ rbEmailReportStatusEntityPK=" + rbEmailReportStatusEntityPK + " ]";
    }
    
}
