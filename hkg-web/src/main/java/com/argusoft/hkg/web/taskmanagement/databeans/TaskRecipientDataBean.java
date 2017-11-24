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
public class TaskRecipientDataBean {

    private Long recipientInstance;
    private String recipientType;
    private String recipientValue;

    public Long getRecipientInstance() {
        return recipientInstance;
    }

    public void setRecipientInstance(Long recipientInstance) {
        this.recipientInstance = recipientInstance;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getRecipientValue() {
        return recipientValue;
    }

    public void setRecipientValue(String recipientValue) {
        this.recipientValue = recipientValue;
    }

    @Override
    public String toString() {
        return "TaskRecipientDataBean{" + "recipientInstance=" + recipientInstance + ", recipientType=" + recipientType + ", recipientValue=" + recipientValue + '}';
    }

}
