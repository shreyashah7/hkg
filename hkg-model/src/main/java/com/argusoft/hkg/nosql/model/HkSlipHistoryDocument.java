/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;

/**
 *
 * @author mmodi
 */
public class HkSlipHistoryDocument {

    private String slipNumber;
    private Date onDate;
    private Long byUser;
    private String status;

    public String getSlipNumber() {
        return slipNumber;
    }

    public void setSlipNumber(String slipNumber) {
        this.slipNumber = slipNumber;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public Long getByUser() {
        return byUser;
    }

    public void setByUser(Long byUser) {
        this.byUser = byUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }        

}
