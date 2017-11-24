/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.shift.databeans;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author sidhdharth
 */
public class TemporaryShiftDataBean {

    private Long tempShiftId;
    private String beginRuleId;
    private String endRuleId;
    private String dateRangeRuleId;
    private String departmentIdsForTempShift;
    @NotNull
    @Length(max = 100, message = "Length of temprorary shift name should not be more that 100 characters.")
    private String tempShiftName;
    private String tempShiftWorkingDayIds;
    private String beginsAction;
    private String endsAction;
    private String beginsEventType;
    private String endsEventType;
    private Long beginsEventOrHolidayId;
    private Long endsEventOrHolidayId;
    @NotNull
    private Date tempShiftStartTime;
    @NotNull
    private Date tempShiftEndTime;
    private boolean setRule;
    private Integer beginsDayCount;
    private Integer endsDayCount;
    private boolean dateRange;
    private Date tempShiftFromDate;
    private Date tempShiftEndDate;
    private boolean tempShiftStatus;
    private Long parentShiftId;
    private boolean defaultTempShift;
    private boolean franchiseAdmin;
    private List<ShiftBreakDataBean> tempShiftBreakList;

    public Long getParentShiftId() {
        return parentShiftId;
    }

    public void setParentShiftId(Long parentShiftId) {
        this.parentShiftId = parentShiftId;
    }

    public Long getTempShiftId() {
        return tempShiftId;
    }

    public void setTempShiftId(Long tempShiftId) {
        this.tempShiftId = tempShiftId;
    }

    public String getDepartmentIdsForTempShift() {
        return departmentIdsForTempShift;
    }

    public void setDepartmentIdsForTempShift(String departmentIdsForTempShift) {
        this.departmentIdsForTempShift = departmentIdsForTempShift;
    }

    public String getTempShiftName() {
        return tempShiftName;
    }

    public void setTempShiftName(String tempShiftName) {
        this.tempShiftName = tempShiftName;
    }

    public String getTempShiftWorkingDayIds() {
        return tempShiftWorkingDayIds;
    }

    public void setTempShiftWorkingDayIds(String tempShiftWorkingDayIds) {
        this.tempShiftWorkingDayIds = tempShiftWorkingDayIds;
    }

    public Date getTempShiftStartTime() {
        return tempShiftStartTime;
    }

    public void setTempShiftStartTime(Date tempShiftStartTime) {
        this.tempShiftStartTime = tempShiftStartTime;
    }

    public Date getTempShiftEndTime() {
        return tempShiftEndTime;
    }

    public void setTempShiftEndTime(Date tempShiftEndTime) {
        this.tempShiftEndTime = tempShiftEndTime;
    }

    public boolean isSetRule() {
        return setRule;
    }

    public void setSetRule(boolean setRule) {
        this.setRule = setRule;
    }

    public Integer getBeginsDayCount() {
        return beginsDayCount;
    }

    public void setBeginsDayCount(Integer beginsDayCount) {
        this.beginsDayCount = beginsDayCount;
    }

    public Integer getEndsDayCount() {
        return endsDayCount;
    }

    public void setEndsDayCount(Integer endsDayCount) {
        this.endsDayCount = endsDayCount;
    }

    public boolean isDateRange() {
        return dateRange;
    }

    public void setDateRange(boolean dateRange) {
        this.dateRange = dateRange;
    }

    public Date getTempShiftFromDate() {
        return tempShiftFromDate;
    }

    public void setTempShiftFromDate(Date tempShiftFromDate) {
        this.tempShiftFromDate = tempShiftFromDate;
    }

    public Date getTempShiftEndDate() {
        return tempShiftEndDate;
    }

    public void setTempShiftEndDate(Date tempShiftEndDate) {
        this.tempShiftEndDate = tempShiftEndDate;
    }

    public boolean isTempShiftStatus() {
        return tempShiftStatus;
    }

    public void setTempShiftStatus(boolean tempShiftStatus) {
        this.tempShiftStatus = tempShiftStatus;
    }

    public List<ShiftBreakDataBean> getTempShiftBreakList() {
        return tempShiftBreakList;
    }

    public void setTempShiftBreakList(List<ShiftBreakDataBean> tempShiftBreakList) {
        this.tempShiftBreakList = tempShiftBreakList;
    }

    public String getBeginsAction() {
        return beginsAction;
    }

    public void setBeginsAction(String beginsAction) {
        this.beginsAction = beginsAction;
    }

    public String getEndsAction() {
        return endsAction;
    }

    public void setEndsAction(String endsAction) {
        this.endsAction = endsAction;
    }

    public String getBeginsEventType() {
        return beginsEventType;
    }

    public void setBeginsEventType(String beginsEventType) {
        this.beginsEventType = beginsEventType;
    }

    public String getEndsEventType() {
        return endsEventType;
    }

    public void setEndsEventType(String endsEventType) {
        this.endsEventType = endsEventType;
    }

    public Long getBeginsEventOrHolidayId() {
        return beginsEventOrHolidayId;
    }

    public void setBeginsEventOrHolidayId(Long beginsEventOrHolidayId) {
        this.beginsEventOrHolidayId = beginsEventOrHolidayId;
    }

    public Long getEndsEventOrHolidayId() {
        return endsEventOrHolidayId;
    }

    public void setEndsEventOrHolidayId(Long endsEventOrHolidayId) {
        this.endsEventOrHolidayId = endsEventOrHolidayId;
    }

    public String getBeginRuleId() {
        return beginRuleId;
    }

    public void setBeginRuleId(String beginRuleId) {
        this.beginRuleId = beginRuleId;
    }

    public String getEndRuleId() {
        return endRuleId;
    }

    public void setEndRuleId(String endRuleId) {
        this.endRuleId = endRuleId;
    }

    public String getDateRangeRuleId() {
        return dateRangeRuleId;
    }

    public void setDateRangeRuleId(String dateRangeRuleId) {
        this.dateRangeRuleId = dateRangeRuleId;
    }

    public boolean isDefaultTempShift() {
        return defaultTempShift;
    }

    public void setDefaultTempShift(boolean defaultTempShift) {
        this.defaultTempShift = defaultTempShift;
    }

    public boolean isFranchiseAdmin() {
        return franchiseAdmin;
    }

    public void setFranchiseAdmin(boolean franchiseAdmin) {
        this.franchiseAdmin = franchiseAdmin;
    }

    @Override
    public String toString() {
        return "\nTemporaryShiftDataBean{" + "tempShiftId=" + tempShiftId + ", departmentIdsForTempShift=" + departmentIdsForTempShift + ", tempShiftName=" + tempShiftName + ", tempShiftWorkingDayIds=" + tempShiftWorkingDayIds + ", tempShiftStartTime=" + tempShiftStartTime + ", tempShiftEndTime=" + tempShiftEndTime + ", setRule=" + setRule + ", beginsDayCount=" + beginsDayCount + ", endsDayCount=" + endsDayCount + ", dateRange=" + dateRange + ", tempShiftFromDate=" + tempShiftFromDate + ", tempShiftEndDate=" + tempShiftEndDate + ", tempShiftStatus=" + tempShiftStatus + '}';
    }

}
