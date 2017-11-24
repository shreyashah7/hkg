/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.databeans;

import java.util.Date;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author vipul
 */
public class LeaveDataBean {

    private Long id;
    private String reason;
    @NotNull(message="Leave reason not selected")
    private Long reasonId;
    @NotNull(message="Leave description not entered")
    @Length(max = 500)
    private String description;
    @NotNull(message="Leave start date not entered")
    private Date fromDate;
    @NotNull(message="Leave end date not entered")
    private Date toDate;
    private Boolean editFlage;
    private Boolean forceEdit;
    private Float days;
 //   @NotNull(message="")
    private String status;
    private Boolean edit;
    private Boolean cancel;
    private String requestType;
    private String totalDays;
    private Map<String, Object> leaveCustom;
    private Map<String, String> dbType;
    @NotNull(message="Remark not entered")
    private String remarks;
    private Date applyedOn;

    public Date getApplyedOn() {
        return applyedOn;
    }

    public void setApplyedOn(Date applyedOn) {
        this.applyedOn = applyedOn;
    }
    
    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
    public Map<String, Object> getLeaveCustom() {
        return leaveCustom;
    }

    public void setLeaveCustom(Map<String, Object> leaveCustom) {
        this.leaveCustom = leaveCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Boolean isCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public Boolean isEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getDays() {
        return days;
    }

    public void setDays(Float totalDays) {
        this.days = totalDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Boolean isEditFlage() {
        return editFlage;
    }

    public void setEditFlage(Boolean editFlage) {
        this.editFlage = editFlage;
    }

    public Boolean isForceEdit() {
        return forceEdit;
    }

    public void setForceEdit(Boolean forceEdit) {
        this.forceEdit = forceEdit;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    @Override
    public String toString() {
        return "LeaveDataBean{" + "id=" + id + ", reason=" + reason + ", description=" + description + ", fromDate=" + fromDate + ", toDate=" + toDate + ", editFlage=" + editFlage + ", forceEdit=" + forceEdit + '}';
    }

}
