/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.databeans;

import java.util.Date;

/**
 *
 * @author shruti
 */
public class CalcDicsountDetailsDatabean {

    private String IDN;
    private String GROUPNAME;
    private double DISCOUNTPERCENTAGE;
    private double MIXVALUE;
    private String PARAMETER;
    private String FROMVALUE;
    private String TOVALUE;
    private Date LOADDATE;

    /**
     * @return the groupName
     */
    public String getGroupname() {
        return GROUPNAME;
    }

    /**
     * @param groupname the groupName to set
     */
    public void setGroupname(String groupname) {
        this.GROUPNAME = groupname;
    }

    /**
     * @return the discountpercentage
     */
    public double getDiscountpercentage() {
        return DISCOUNTPERCENTAGE;
    }

    /**
     * @param discountpercentage the discountpercentage to set
     */
    public void setDiscountpercentage(double discountpercentage) {
        this.DISCOUNTPERCENTAGE = discountpercentage;
    }

    /**
     * @return the mixValue
     */
    public double getMixValue() {
        return MIXVALUE;
    }

    /**
     * @param mixValue the mixValue to set
     */
    public void setMixValue(double mixValue) {
        this.MIXVALUE = mixValue;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return PARAMETER;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(String parameter) {
        this.PARAMETER = parameter;
    }

    /**
     * @return the tovalue
     */
    public String getTovalue() {
        return TOVALUE;
    }

    /**
     * @param tovalue the tovalue to set
     */
    public void setTovalue(String tovalue) {
        this.TOVALUE = tovalue;
    }

    /**
     * @return the fromvalue
     */
    public String getFromvalue() {
        return FROMVALUE;
    }

    /**
     * @param fromvalue the fromvalue to set
     */
    public void setFromvalue(String fromvalue) {
        this.FROMVALUE = fromvalue;
    }

    /**
     * @return the loaddate
     */
    public Date getLoaddate() {
        return LOADDATE;
    }

    /**
     * @param loaddate the loaddate to set
     */
    public void setLoaddate(Date loaddate) {
        this.LOADDATE = loaddate;
    }

    /**
     * @return the idn
     */
    public String getIdn() {
        return IDN;
    }

    /**
     * @param idn the idn to set
     */
    public void setIdn(String idn) {
        this.IDN = idn;
    }

}
