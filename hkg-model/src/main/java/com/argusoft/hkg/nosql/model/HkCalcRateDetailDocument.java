/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.Map;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "ratedetail")
public class HkCalcRateDetailDocument {

    @Id
    private String id;
    private Double discount;
    private Double mixamount;
    private String groupName;
    /**
     * Map<master-code,Pair<mastervalue.id,discount rate>>
     */
    private Map<String, RangeDocument> discountDetailsMap;
    private Date craetedOn;
    private Date modifiedOn;
    private boolean isActive;
    private boolean isArchive;

    public HkCalcRateDetailDocument() {
        this.discount = 0.0;
        this.mixamount = 0.0;
    }

  
    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the mixamount
     */
    public Double getMixamount() {
        return mixamount;
    }

    /**
     * @param mixamount the mixamount to set
     */
    public void setMixamount(Double mixamount) {
        this.mixamount = mixamount;
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

    public Map<String, RangeDocument> getDiscountDetailsMap() {
        return discountDetailsMap;
    }

    public void setDiscountDetailsMap(Map<String, RangeDocument> discountDetailsMap) {
        this.discountDetailsMap = discountDetailsMap;
    }

 
//
//    /**
//     * @return the discountDetailsMap
//     */
//    public Map<String, Map<Long, Double>> getDiscountDetailsMap() {
//        return discountDetailsMap;
//    }
//
//    /**
//     * @param discountDetailsMap the discountDetailsMap to set
//     */
//    public void setDiscountDetailsMap(Map<String, Map<Long, Double>> discountDetailsMap) {
//        this.discountDetailsMap = discountDetailsMap;
//    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the isActive
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isArchive
     */
    public boolean getIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

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

    @Override
    public String toString() {
        return "HkCalcRateDetailDocument{" + "id=" + id + ", discount=" + discount + ", mixamount=" + mixamount + ", groupName=" + groupName + ", discountDetailsMap=" + discountDetailsMap + ", craetedOn=" + craetedOn + ", modifiedOn=" + modifiedOn + ", isActive=" + isActive + ", isArchive=" + isArchive + '}';
    }

}
