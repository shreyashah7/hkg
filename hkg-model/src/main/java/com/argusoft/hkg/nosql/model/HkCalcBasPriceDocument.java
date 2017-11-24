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
public class HkCalcBasPriceDocument {

    @Id
    private ObjectId id;
    private Date modifiedOn;
    private Date loadDate;
    private Double basePrice;
    private Long shape;
    private Long color;
    private Long quality;
    private double caratFrom;
    private double caratTo;
    private boolean isActive;
    private boolean isArchive;

    /**
     * @return the basePrice
     */
    public Double getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice the basePrice to set
     */
    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
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
     * @return the shape
     */
    public Long getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(Long shape) {
        this.shape = shape;
    }

    /**
     * @return the color
     */
    public Long getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Long color) {
        this.color = color;
    }

    /**
     * @return the quality
     */
    public Long getQuality() {
        return quality;
    }

    /**
     * @param quality the quality to set
     */
    public void setQuality(Long quality) {
        this.quality = quality;
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
     * @return the loadDate
     */
    public Date getLoadDate() {
        return loadDate;
    }

    /**
     * @param loadDate the loadDate to set
     */
    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HkCalcBasPriceDocument{" + "id=" + id + ", loadDate=" + loadDate + ", basePrice=" + basePrice + ", shape=" + shape + ", color=" + color + ", quality=" + quality + ", caratFrom=" + caratFrom + ", caratTo=" + caratTo + '}';
    }

}
