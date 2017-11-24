/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.shift.databeans;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author sidhdharth
 */
public class ShiftBreakDataBean {

//    @NotNull
//    @Length(max = 100, message = "Length of break title should not be more that 100 characters.")
    private String breakSlotTitle;
//    @NotNull
    private Date breakStartTime;
//    @NotNull
    private Date breakEndTime;
    private Long breakId;

    public String getBreakSlotTitle() {
        return breakSlotTitle;
    }

    public void setBreakSlotTitle(String breakSlotTitle) {
        this.breakSlotTitle = breakSlotTitle;
    }

    public Date getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(Date breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public Date getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(Date breakEndTime) {
        this.breakEndTime = breakEndTime;
    }

    public Long getBreakId() {
        return breakId;
    }

    public void setBreakId(Long breakId) {
        this.breakId = breakId;
    }

    @Override
    public String toString() {
        return "ShiftBreakDataBean{" + "slotTitle=" + breakSlotTitle + ", breakStartTime=" + breakStartTime + ", breakEndTime=" + breakEndTime + ", breakId=" + breakId + '}';
    }

}
