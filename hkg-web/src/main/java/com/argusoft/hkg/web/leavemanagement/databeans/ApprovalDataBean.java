/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author vipul
 */
public class ApprovalDataBean {

    private Long id;
    private String requestFrom;
    private String department;
    private String reason;
    private Date fromDate;
    private Date toDate;
    private float days;
    private String status;
    private String description;
    private Date applyedOn;
    private Long userId;
    private String requestType;
    private Map<String, Object> leaveCustom;
    private Map<String, Object> approverCustom;
    private Map<String, String> dbType;
    private Boolean edit;
    private Long reasonId;
    private String remarks;
    private Long approvalId;

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Boolean isEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getApplyedOn() {
        return applyedOn;
    }

    public void setApplyedOn(Date applyedOn) {
        this.applyedOn = applyedOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public float getDays() {
        return days;
    }

    public void setDays(float days) {
        this.days = days;
    }

    public Map<String, Object> getApproverCustom() {
        return approverCustom;
    }

    public void setApproverCustom(Map<String, Object> approverCustom) {
        this.approverCustom = approverCustom;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
