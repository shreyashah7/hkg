/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;


import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;

/**
 *
 * @author dhwani
 */
public class AllotmentSuggestionDataBean {

    private Long userId;
    private String userCode;
    private String employeeName;
    private Float carat;
    private Float cut;
    private Float color;
    private Float clarity;
    private Float breakage;
    private Boolean presence;
    private Integer workLoad;
    //date in id and status in status of selectItem
    private List<SelectItem> leaveDetails;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Float getCarat() {
        return carat;
    }

    public void setCarat(Float carat) {
        this.carat = carat;
    }

    public Float getCut() {
        return cut;
    }

    public void setCut(Float cut) {
        this.cut = cut;
    }

    public Float getColor() {
        return color;
    }

    public void setColor(Float color) {
        this.color = color;
    }

    public Float getClarity() {
        return clarity;
    }

    public void setClarity(Float clarity) {
        this.clarity = clarity;
    }

    public Float getBreakage() {
        return breakage;
    }

    public void setBreakage(Float breakage) {
        this.breakage = breakage;
    }

    public Boolean isPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public List<SelectItem> getLeaveDetails() {
        return leaveDetails;
    }

    public void setLeaveDetails(List<SelectItem> leaveDetails) {
        this.leaveDetails = leaveDetails;
    }
}
