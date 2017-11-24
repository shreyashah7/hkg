/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.taskmanagement.databeans;

import java.util.Date;

/**
 *
 * @author kuldeep
 */
public class TaskRecipientDetailDataBean {

    private Long id;
    private Date attendedOn;
    private Date completedOn;
    private Date dueDate;
    private Date onDate;
    private String status;
    private Long task;
    private Long userId;
    private String userName;
    private boolean taskEdited;
    private Date updatedOn;
    private Long recipientCategory;
    private Integer repetitionCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAttendedOn() {
        return attendedOn;
    }

    public void setAttendedOn(Date attendedOn) {
        this.attendedOn = attendedOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public boolean isTaskEdited() {
        return taskEdited;
    }

    public void setTaskEdited(boolean taskEdited) {
        this.taskEdited = taskEdited;
    }

    public Long getRecipientCategory() {
        return recipientCategory;
    }

    public void setRecipientCategory(Long recipientCategory) {
        this.recipientCategory = recipientCategory;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    @Override
    public String toString() {
        return "TaskRecipientDetailDataBean{" + "id=" + id + ", attendedOn=" + attendedOn + ", completedOn=" + completedOn + ", dueDate=" + dueDate + ", onDate=" + onDate + ", status=" + status + ", task=" + task + ", userId=" + userId + ", userName=" + userName + ", taskEdited=" + taskEdited + ", updatedOn=" + updatedOn + ", recipientCategory=" + recipientCategory + '}';
    }
    
}
