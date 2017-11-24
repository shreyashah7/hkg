/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.databeans;

import java.util.Date;

/**
 *
 * @author shruti
 */
public class CalcRateRecord {

    private String idn;
    private String groupname;
    private double discountpercentage;
    private double mixValue;
    private String parameter;
    private String fromvalue;
    private String tovalue;
    private Date loaddate;

    /**
     * @return the groupName
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * @param groupname the groupName to set
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    /**
     * @return the discountpercentage
     */
    public double getDiscountpercentage() {
        return discountpercentage;
    }

    /**
     * @param discountpercentage the discountpercentage to set
     */
    public void setDiscountpercentage(double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    /**
     * @return the mixValue
     */
    public double getMixValue() {
        return mixValue;
    }

    /**
     * @param mixValue the mixValue to set
     */
    public void setMixValue(double mixValue) {
        this.mixValue = mixValue;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the tovalue
     */
    public String getTovalue() {
        return tovalue;
    }

    /**
     * @param tovalue the tovalue to set
     */
    public void setTovalue(String tovalue) {
        this.tovalue = tovalue;
    }

    /**
     * @return the fromvalue
     */
    public String getFromvalue() {
        return fromvalue;
    }

    /**
     * @param fromvalue the fromvalue to set
     */
    public void setFromvalue(String fromvalue) {
        this.fromvalue = fromvalue;
    }

    /**
     * @return the loaddate
     */
    public Date getLoaddate() {
        return loaddate;
    }

    /**
     * @param loaddate the loaddate to set
     */
    public void setLoaddate(Date loaddate) {
        this.loaddate = loaddate;
    }

    /**
     * @return the idn
     */
    public String getIdn() {
        return idn;
    }

    /**
     * @param idn the idn to set
     */
    public void setIdn(String idn) {
        this.idn = idn;
    }

}
