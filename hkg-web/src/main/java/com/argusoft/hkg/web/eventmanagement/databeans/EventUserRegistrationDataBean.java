/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.eventmanagement.databeans;

import java.util.List;

/**
 *
 * @author shreya
 */
public class EventUserRegistrationDataBean {
    
    private String empName;
    private int adultCount;
    private int childCount;
    private int guestCount;
    private Long eventId;
    private Long userId;
    private String status;
    private String reason;
    private List<GuestDataBean> guests;

    public EventUserRegistrationDataBean() {
    }

    public EventUserRegistrationDataBean(String empName, int adultCount, int childCount, int guestCount, Long eventId, Long userId, List<GuestDataBean> guests) {
        this.empName = empName;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.guestCount = guestCount;
        this.eventId = eventId;
        this.userId = userId;
        this.guests = guests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public List<GuestDataBean> getGuests() {
        return guests;
    }

    public void setGuests(List<GuestDataBean> guests) {
        this.guests = guests;
    }

    @Override
    public String toString() {
        return "EventUserRegistrationDataBean{" + "empName=" + empName + ", adultCount=" + adultCount + ", childCount=" + childCount + ", guestCount=" + guestCount + ", guests=" + guests + '}';
    }
    
    
}
