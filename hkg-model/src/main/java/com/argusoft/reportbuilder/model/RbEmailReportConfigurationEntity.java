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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "rb_email_report_configuration_info")
public class RbEmailReportConfigurationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "report", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private RbReport report;
    @Column(name = "at_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date atTime;
    @Column(name = "actual_end_date")
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;
    @Column(name = "after_units")
    private Integer afterUnits;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "end_repeat_mode", length = 5)
    private String endRepeatMode;
    @Column(name = "monthly_on_day")
    private Integer monthlyOnDay;
    @Column(name = "repeatative_mode", length = 5)
    private String repeatativeMode;
    @Column(name = "repetition_cnt")
    private Integer repetitionCnt;
    @Column(name = "weekly_on_days", length = 50)
    private String weeklyOnDays;
    @Column(name = "pdf_attachment")
    private boolean pdfAttachment;
    @Column(name = "xls_attachment")
    private boolean xlsAttachment;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
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

    public RbEmailReportConfigurationEntity() {
    }

    public RbEmailReportConfigurationEntity(Long id) {
        this.id = id;
    }

    public RbEmailReportConfigurationEntity(Long id, RbReport report, Date atTime, Date actualEndDate, Integer afterUnits, Date endDate, String endRepeatMode, Integer monthlyOnDay, String repeatativeMode, Integer repetitionCnt, String weeklyOnDays, boolean pdfAttachment, boolean xlsAttachment, String status, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, long franchise) {
        this.id = id;
        this.report = report;
        this.atTime = atTime;
        this.actualEndDate = actualEndDate;
        this.afterUnits = afterUnits;
        this.endDate = endDate;
        this.endRepeatMode = endRepeatMode;
        this.monthlyOnDay = monthlyOnDay;
        this.repeatativeMode = repeatativeMode;
        this.repetitionCnt = repetitionCnt;
        this.weeklyOnDays = weeklyOnDays;
        this.pdfAttachment = pdfAttachment;
        this.xlsAttachment = xlsAttachment;
        this.status = status;
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

    public RbReport getReport() {
        return report;
    }

    public void setReport(RbReport report) {
        this.report = report;
    }

    public boolean isPdfAttachment() {
        return pdfAttachment;
    }

    public void setPdfAttachment(boolean pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
    }

    public boolean isXlsAttachment() {
        return xlsAttachment;
    }

    public void setXlsAttachment(boolean xlsAttachment) {
        this.xlsAttachment = xlsAttachment;
    }
    
    public Date getAtTime() {
        return atTime;
    }

    public void setAtTime(Date atTime) {
        this.atTime = atTime;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Integer getAfterUnits() {
        return afterUnits;
    }

    public void setAfterUnits(Integer afterUnits) {
        this.afterUnits = afterUnits;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndRepeatMode() {
        return endRepeatMode;
    }

    public void setEndRepeatMode(String endRepeatMode) {
        this.endRepeatMode = endRepeatMode;
    }

    public Integer getMonthlyOnDay() {
        return monthlyOnDay;
    }

    public void setMonthlyOnDay(Integer monthlyOnDay) {
        this.monthlyOnDay = monthlyOnDay;
    }

    public String getRepeatativeMode() {
        return repeatativeMode;
    }

    public void setRepeatativeMode(String repeatativeMode) {
        this.repeatativeMode = repeatativeMode;
    }

    public Integer getRepetitionCnt() {
        return repetitionCnt;
    }

    public void setRepetitionCnt(Integer repetitionCnt) {
        this.repetitionCnt = repetitionCnt;
    }

    public String getWeeklyOnDays() {
        return weeklyOnDays;
    }

    public void setWeeklyOnDays(String weeklyOnDays) {
        this.weeklyOnDays = weeklyOnDays;
    }   

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RbEmailReportConfigurationEntity)) {
            return false;
        }
        RbEmailReportConfigurationEntity other = (RbEmailReportConfigurationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.reportbuilder.model.RbEmailReportConfigurationEntity[ id=" + id + " ]";
    }
    
}
