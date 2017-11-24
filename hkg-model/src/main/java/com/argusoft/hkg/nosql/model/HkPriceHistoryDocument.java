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
public class HkPriceHistoryDocument {

    private Double price;
    private String currency;
    private Date onDate;
    private Long byUser;
    private String status;
    private Boolean overrideValue;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Boolean isOverrideValue() {
        return overrideValue;
    }

    public void setOverrideValue(Boolean overrideValue) {
        this.overrideValue = overrideValue;
    }
    
}
