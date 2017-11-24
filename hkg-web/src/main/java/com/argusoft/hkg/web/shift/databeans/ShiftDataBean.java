/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.shift.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author sidhdharth
 */
public class ShiftDataBean {

    private Long id;
    @NotNull
    private String departmentIds;
    @NotNull
    @Length(max = 100, message = "Length of shift name should not be more that 100 characters.")
    private String shiftName;
    private boolean franchiseAdmin;
    private String workingDayIds;
    private String departmentNames;
    @NotNull
    private Date shiftStartTime;
    @NotNull
    private Date shiftEndTime;
    private boolean status;
    private boolean defaultShift;
    private boolean overrideShift;
    private List<ShiftBreakDataBean> breakList;
    private List<TemporaryShiftDataBean> temporaryShifts;
    private List<ShiftDataBean> overRideShifts;
    private Map<String, Object> shiftCustom;// for custom field data
    private Map<String, String> shiftDbType; //for get filed wise dbtype

    public List<TemporaryShiftDataBean> getTemporaryShifts() {
        return temporaryShifts;
    }

    public void setTemporaryShifts(List<TemporaryShiftDataBean> temporaryShifts) {
        this.temporaryShifts = temporaryShifts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(String departmentIds) {
        this.departmentIds = departmentIds;
    }

    public Date getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(Date shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public Date getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(Date shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public String getWorkingDayIds() {
        return workingDayIds;
    }

    public void setWorkingDayIds(String workingDayIds) {
        this.workingDayIds = workingDayIds;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ShiftBreakDataBean> getBreakList() {
        return breakList;
    }

    public void setBreakList(List<ShiftBreakDataBean> breakList) {
        this.breakList = breakList;
    }

    public boolean isDefaultShift() {
        return defaultShift;
    }

    public void setDefaultShift(boolean defaultShift) {
        this.defaultShift = defaultShift;
    }

    public Map<String, Object> getShiftCustom() {
        return shiftCustom;
    }

    public void setShiftCustom(Map<String, Object> shiftCustom) {
        this.shiftCustom = shiftCustom;
    }

    public Map<String, String> getShiftDbType() {
        return shiftDbType;
    }

    public void setShiftDbType(Map<String, String> shiftDbType) {
        this.shiftDbType = shiftDbType;
    }

    public String getDepartmentNames() {
        return departmentNames;
    }

    public void setDepartmentNames(String departmentNames) {
        this.departmentNames = departmentNames;
    }

    public boolean isFranchiseAdmin() {
        return franchiseAdmin;
    }

    public void setFranchiseAdmin(boolean franchiseAdmin) {
        this.franchiseAdmin = franchiseAdmin;
    }

    public boolean isOverrideShift() {
        return overrideShift;
    }

    public void setOverrideShift(boolean overrideShift) {
        this.overrideShift = overrideShift;
    }

    public List<ShiftDataBean> getOverRideShifts() {
        return overRideShifts;
    }

    public void setOverRideShifts(List<ShiftDataBean> overRideShifts) {
        this.overRideShifts = overRideShifts;
    }

    @Override
    public String toString() {
        return "ShiftDataBean{" + "id=" + id + ", departmentIds=" + departmentIds + ", shiftName=" + shiftName + ", workingDayIds=" + workingDayIds + ", shiftStartTime=" + shiftStartTime + ", shiftEndTime=" + shiftEndTime + ", status=" + status + ", breakList=" + breakList + ", temporaryShifts=" + temporaryShifts + '}';
    }

}
