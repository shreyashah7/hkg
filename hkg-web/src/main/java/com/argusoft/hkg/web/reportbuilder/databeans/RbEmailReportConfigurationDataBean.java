/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.reportbuilder.databeans;

import com.argusoft.reportbuilder.model.RbReport;
import java.util.Date;

/**
 *
 * @author gautam
 */
public class RbEmailReportConfigurationDataBean {
    
    private Long id;
    private Long reportId;
    private RbReport report;
    private Date atTime;
    private Date actualEndDate;
    private Integer afterUnits;
    private Date endDate;
    private String endRepeatMode;
    private Integer monthlyOnDay;
    private String repeatativeMode;
    private Integer repetitionCnt;
    private String weeklyOnDays;
    private boolean pdfAttachment;
    private boolean xlsAttachment;
    private String status;

    public RbEmailReportConfigurationDataBean() {
    }

    public RbEmailReportConfigurationDataBean(Long id, Long reportId, Date atTime, Date actualEndDate, Integer afterUnits, Date endDate, String endRepeatMode, Integer monthlyOnDay, String repeatativeMode, Integer repetitionCnt, String weeklyOnDays, boolean pdfAttachment, boolean xlsAttachment, String status) {
        this.id = id;
        this.reportId = reportId;
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public RbReport getReport() {
        return report;
    }

    public void setReport(RbReport report) {
        this.report = report;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RbEmailReportConfigurationDataBean{" + "id=" + id + ", reportId=" + reportId + ", atTime=" + atTime + ", actualEndDate=" + actualEndDate + ", afterUnits=" + afterUnits + ", endDate=" + endDate + ", endRepeatMode=" + endRepeatMode + ", monthlyOnDay=" + monthlyOnDay + ", repeatativeMode=" + repeatativeMode + ", repetitionCnt=" + repetitionCnt + ", weeklyOnDays=" + weeklyOnDays + ", pdfAttachment=" + pdfAttachment + ", xlsAttachment=" + xlsAttachment + ", status=" + status + '}';
    }
    
    
}
