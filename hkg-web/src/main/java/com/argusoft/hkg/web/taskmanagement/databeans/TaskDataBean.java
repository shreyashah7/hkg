/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.taskmanagement.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kuldeep
 */
public class TaskDataBean {

    private Long id;
    private Long taskCategory;
    private String taskName;
    private Date dueDate;
    private boolean repeatTask;
    private String repeatativeMode;
    private String weeklyOnDays;
    private Integer monthlyOnDay;
    private String endRepeatMode;
    private Date endDate;
    private Integer afterUnits;
    private String[] taskRecipients;
    private String recipientNames;
    private String status;
    private String lastUpdate;
    private String assignedBy;
    private Long assignedById;
    private Integer repetitionCount;
    private List<TaskRecipientDataBean> taskRecipientDataBeanList;
    private List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList;
    private Map<Integer, List<TaskRecipientDetailDataBean>> repititionTaskRecipeintDetailMap;
    private Map<String, Object> taskCustom;
    private Map<String, String> dbType;

    public Map<String, Object> getTaskCustom() {
        return taskCustom;
    }

    public void setTaskCustom(Map<String, Object> taskCustom) {
        this.taskCustom = taskCustom;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long assignedById) {
        this.assignedById = assignedById;
    }

    public String getRecipientNames() {
        return recipientNames;
    }

    public void setRecipientNames(String recipientNames) {
        this.recipientNames = recipientNames;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }
    
    
    

//    private Integer repetitionCount;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isRepeatTask() {
        return repeatTask;
    }

    public void setRepeatTask(boolean repeatTask) {
        this.repeatTask = repeatTask;
    }

    public String getRepeatativeMode() {
        return repeatativeMode;
    }

    public void setRepeatativeMode(String repeatativeMode) {
        this.repeatativeMode = repeatativeMode;
    }

    public String getWeeklyOnDays() {
        return weeklyOnDays;
    }

    public void setWeeklyOnDays(String weeklyOnDays) {
        this.weeklyOnDays = weeklyOnDays;
    }

    public Integer getMonthlyOnDay() {
        return monthlyOnDay;
    }

    public void setMonthlyOnDay(Integer monthlyOnDay) {
        this.monthlyOnDay = monthlyOnDay;
    }

    public String getEndRepeatMode() {
        return endRepeatMode;
    }

    public void setEndRepeatMode(String endRepeatMode) {
        this.endRepeatMode = endRepeatMode;
    }

    public Integer getAfterUnits() {
        return afterUnits;
    }

    public void setAfterUnits(Integer afterUnits) {
        this.afterUnits = afterUnits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(Long taskCategory) {
        this.taskCategory = taskCategory;
    }

    public String[] getTaskRecipients() {
        return taskRecipients;
    }

    public void setTaskRecipients(String[] taskRecipients) {
        this.taskRecipients = taskRecipients;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public List<TaskRecipientDetailDataBean> getTaskRecipientDetailDataBeanList() {
        return taskRecipientDetailDataBeanList;
    }

    public void setTaskRecipientDetailDataBeanList(List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList) {
        this.taskRecipientDetailDataBeanList = taskRecipientDetailDataBeanList;
    }

    public List<TaskRecipientDataBean> getTaskRecipientDataBeanList() {
        return taskRecipientDataBeanList;
    }

    public void setTaskRecipientDataBeanList(List<TaskRecipientDataBean> taskRecipientDataBeanList) {
        this.taskRecipientDataBeanList = taskRecipientDataBeanList;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public Map<Integer, List<TaskRecipientDetailDataBean>> getRepititionTaskRecipeintDetailMap() {
        return repititionTaskRecipeintDetailMap;
    }

    public void setRepititionTaskRecipeintDetailMap(Map<Integer, List<TaskRecipientDetailDataBean>> repititionTaskRecipeintDetailMap) {
        this.repititionTaskRecipeintDetailMap = repititionTaskRecipeintDetailMap;
    }

    @Override
    public String toString() {
        return "TaskDataBean{" + "id=" + id + ", taskCategory=" + taskCategory + ", taskName=" + taskName + ", dueDate=" + dueDate + ", repeatTask=" + repeatTask + ", repeatativeMode=" + repeatativeMode + ", weeklyOnDays=" + weeklyOnDays + ", monthlyOnDay=" + monthlyOnDay + ", endRepeatMode=" + endRepeatMode + ", endDate=" + endDate + ", afterUnits=" + afterUnits + ", taskRecipients=" + taskRecipients + ", recipientNames=" + recipientNames + ", status=" + status + ", lastUpdate=" + lastUpdate + ", assignedBy=" + assignedBy + ", assignedById=" + assignedById + ", repetitionCount=" + repetitionCount + ", taskRecipientDataBeanList=" + taskRecipientDataBeanList + ", taskRecipientDetailDataBeanList=" + taskRecipientDetailDataBeanList + ", repititionTaskRecipeintDetailMap=" + repititionTaskRecipeintDetailMap + ", taskCustom=" + taskCustom + ", dbType=" + dbType + '}';
    }

   
}
