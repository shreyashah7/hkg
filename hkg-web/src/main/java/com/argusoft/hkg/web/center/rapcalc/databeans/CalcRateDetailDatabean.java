/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class CalcRateDetailDatabean {

    private String id;
    private Double baseAmount;
    private Double graphBaseAmount;
    private Double mixAmount;
    private Double graphMixAmount;
    private Double amount; //price after discount and mixAmount
    private Double graphAmount;
    private Double discount;
    private Double graphDiscount;
    private Double carat;
    //Assumption: We will always send total calculated discount and won't need to send discount for a particular field to client.
    // This map will contain customfield and selected value.
     private Map<String, Object> discountDetailsMap;
     private Map<String, Object> graphDiscountDetailsMap;
    private Date craetedOn;
    private Map<String, Object> fourCMap;
    private Map<String, Object> graphFourCMap;    
    private Map<String, String> dbTypeMap;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the baseAmount
     */
    public Double getBaseAmount() {
        return baseAmount;
    }

    /**
     * @param baseAmount the baseAmount to set
     */
    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    /**
     * @return the mixamount
     */
    public Double getMixAmount() {
        return mixAmount;
    }

    /**
     * @param mixamount the mixamount to set
     */
    public void setMixAmount(Double mixamount) {
        this.mixAmount = mixamount;
    }

    /**
     * @return the craetedOn
     */
    public Date getCraetedOn() {
        return craetedOn;
    }

    /**
     * @param craetedOn the craetedOn to set
     */
    public void setCraetedOn(Date craetedOn) {
        this.craetedOn = craetedOn;
    }

    /**
     * @return the fourCMap
     */
    public Map<String, Object> getFourCMap() {
        return fourCMap;
    }

    /**
     * @param fourCMap the fourCMap to set
     */
    public void setFourCMap(Map<String, Object> fourCMap) {
        this.fourCMap = fourCMap;
    }

    /**
     * @return the dbTypeMap
     */
    public Map<String, String> getDbTypeMap() {
        return dbTypeMap;
    }

    /**
     * @param dbTypeMap the dbTypeMap to set
     */
    public void setDbTypeMap(Map<String, String> dbTypeMap) {
        this.dbTypeMap = dbTypeMap;
    }

    /**
     * @return the discountDetailsMap
     */
    public Map<String, Object> getDiscountDetailsMap() {
        return discountDetailsMap;
    }

    /**
     * @param discountDetailsMap the discountDetailsMap to set
     */
    public void setDiscountDetailsMap(Map<String, Object> discountDetailsMap) {
        this.discountDetailsMap = discountDetailsMap;
    }

    public Double getGraphBaseAmount() {
        return graphBaseAmount;
    }

    public void setGraphBaseAmount(Double graphBaseAmount) {
        this.graphBaseAmount = graphBaseAmount;
    }

    public Double getGraphMixAmount() {
        return graphMixAmount;
    }

    public void setGraphMixAmount(Double graphMixAmount) {
        this.graphMixAmount = graphMixAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getGraphAmount() {
        return graphAmount;
    }

    public void setGraphAmount(Double graphAmount) {
        this.graphAmount = graphAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getGraphDiscount() {
        return graphDiscount;
    }

    public void setGraphDiscount(Double graphDiscount) {
        this.graphDiscount = graphDiscount;
    }

    public Map<String, Object> getGraphDiscountDetailsMap() {
        return graphDiscountDetailsMap;
    }

    public void setGraphDiscountDetailsMap(Map<String, Object> graphDiscountDetailsMap) {
        this.graphDiscountDetailsMap = graphDiscountDetailsMap;
    }

    public Map<String, Object> getGraphFourCMap() {
        return graphFourCMap;
    }

    public void setGraphFourCMap(Map<String, Object> graphFourCMap) {
        this.graphFourCMap = graphFourCMap;
    }

    /**
     * @return the carat
     */
    public Double getCarat() {
        return carat;
    }

    /**
     * @param carat the carat to set
     */
    public void setCarat(Double carat) {
        this.carat = carat;
    }
    
    

    }
