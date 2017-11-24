/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;

/**
 *
 * @author shruti
 */
public class HkCalcFourCDiscountDocument {

    @Id
    private ObjectId id;
    private long shape;
    private long color;
    private long clarity;
    private Date loadDate;
    private double discount;
    private double rate;
    private double caratFrom;
    private double caratTo;
    private Date fromDate;
    private Date toDate;
    private Date createdOn;
    private Date modifiedOn;
    private boolean isActive;
    private boolean isArchive;

    /**
     * @return the loadDate
     */
    public Date getLoadDate() {
        return loadDate;
    }

    /**
     * @param LoadDate the loadDate to set
     */
    public void setLoadDate(Date LoadDate) {
        this.loadDate = LoadDate;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the caratFrom
     */
    public double getCaratFrom() {
        return caratFrom;
    }

    /**
     * @param caratFrom the caratFrom to set
     */
    public void setCaratFrom(double caratFrom) {
        this.caratFrom = caratFrom;
    }

    /**
     * @return the caratTo
     */
    public double getCaratTo() {
        return caratTo;
    }

    /**
     * @param caratTo the caratTo to set
     */
    public void setCaratTo(double caratTo) {
        this.caratTo = caratTo;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
    public boolean isIsActive() {
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
    public boolean isIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the shape
     */
    public long getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(long shape) {
        this.shape = shape;
    }

    /**
     * @return the color
     */
    public long getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(long color) {
        this.color = color;
    }

    /**
     * @return the clarity
     */
    public long getClarity() {
        return clarity;
    }

    /**
     * @param clarity the clarity to set
     */
    public void setClarity(long clarity) {
        this.clarity = clarity;
    }

    /**
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "HkCalcFourCDiscountDocument{" + "id=" + id + ", shape=" + shape + ", color=" + color + ", clarity=" + clarity + ", LoadDate=" + loadDate + ", discount=" + discount + ", rate=" + rate + ", caratFrom=" + caratFrom + ", caratTo=" + caratTo + ", fromDate=" + fromDate + ", toDate=" + toDate + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", isActive=" + isActive + ", isArchive=" + isArchive + '}';
    }

}
