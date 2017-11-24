/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.leavemanagement.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mansi
 */
public class LeaveWorkflowDataBean {

    private Long id;
    private Long department;
    private String departmentName;
    private Integer lastLevel;
    private long franchise;
    private boolean isArchive;
    private List<SelectItem> approvalMap;
    private boolean isDefault;
    private Map<String, Object> leaveWorkflowCustom;// for custom field data
    private Map<String, String> leaveWorkflowDbType; //for get filed wise dbtype

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Integer getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(Integer lastLevel) {
        this.lastLevel = lastLevel;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public List<SelectItem> getApprovalMap() {
        return approvalMap;
    }

    public void setApprovalMap(List<SelectItem> approvalMap) {
        this.approvalMap = approvalMap;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Map<String, Object> getLeaveWorkflowCustom() {
        return leaveWorkflowCustom;
    }

    public void setLeaveWorkflowCustom(Map<String, Object> leaveWorkflowCustom) {
        this.leaveWorkflowCustom = leaveWorkflowCustom;
    }

    public Map<String, String> getLeaveWorkflowDbType() {
        return leaveWorkflowDbType;
    }

    public void setLeaveWorkflowDbType(Map<String, String> leaveWorkflowDbType) {
        this.leaveWorkflowDbType = leaveWorkflowDbType;
    }
}
