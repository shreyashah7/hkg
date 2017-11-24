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
public class HolidayDatabean {

    private Long id;
    @NotNull
    @Length(max=100)
    private String title;  
    private String description;
    @NotNull
    private Date startDt;
    @NotNull
    private Date endDt;
    private Boolean editFlage;
    private Boolean forceEdit;
    private Map<String, Object> holidayCustom;
    private Map<String, String> dbType; 

    public Map<String, Object> getHolidayCustom() {
        return holidayCustom;
    }

    public void setHolidayCustom(Map<String, Object> holidayCustom) {
        this.holidayCustom = holidayCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    
    
    public Long getId() {
        return id;
    }

    public Boolean isEditFlage() {
        return editFlage;
    }

    public void setEditFlage(Boolean editFlage) {
        this.editFlage = editFlage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isForceEdit() {
        return forceEdit;
    }

    public void setForceEdit(Boolean forceEdit) {
        this.forceEdit = forceEdit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    @Override
    public String toString() {
        return "HkManageHolidayDatabean{" + "id=" + id + ", title=" + title + ", description=" + description + ", startDt=" + startDt + ", endDt=" + endDt + '}';
    }

}
