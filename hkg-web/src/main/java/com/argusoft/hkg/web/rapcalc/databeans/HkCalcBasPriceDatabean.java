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
public class HkCalcBasPriceDatabean {
    private String ID;
    private Date LOADDATE;
    private String PRICE;
    private String SHAPE;
    private String COLOR;
    private String QUALITY;
    private String CARATFROM;
    private String CARATTO;

    /**
     * @return the LOADDATE
     */
    public Date getLoaddate() {
        return LOADDATE;
    }

    /**
     * @param loaddate the LOADDATE to set
     */
    public void setLoaddate(Date loaddate) {
        this.LOADDATE = loaddate;
    }

    /**
     * @return the PRICE
     */
    public String getPRICE() {
        return PRICE;
    }

    /**
     * @param PRICE the PRICE to set
     */
    public void setPRICE(String PRICE) {
        this.PRICE = PRICE.trim();
    }

    /**
     * @return the SHAPE
     */
    public String getShape() {
        return SHAPE;
    }

    /**
     * @param shape the SHAPE to set
     */
    public void setShape(String shape) {
        this.SHAPE = shape.trim();
    }

    /**
     * @return the COLOR
     */
    public String getColor() {
        return COLOR;
    }

    /**
     * @param color the COLOR to set
     */
    public void setColor(String color) {
        this.COLOR = color.trim();
    }

    /**
     * @return the QUALITY
     */
    public String getQuality() {
        return QUALITY;
    }

    /**
     * @param quality the QUALITY to set
     */
    public void setQuality(String quality) {
        this.QUALITY = quality.trim();
    }

    /**
     * @return the CARATFROM
     */
    public String getCaratfrom() {
        return CARATFROM;
    }

    /**
     * @param caratfrom the CARATFROM to set
     */
    public void setCaratfrom(String caratfrom) {
        this.CARATFROM = caratfrom.trim();
    }

    /**
     * @return the CARATTO
     */
    public String getCaratto() {
        return CARATTO;
    }

    /**
     * @param caratto the CARATTO to set
     */
    public void setCaratto(String caratto) {
        this.CARATTO = caratto.trim();
    }

    /**
     * @return the ID
     */
    public String getId() {
        return ID;
    }

    /**
     * @param id the ID to set
     */
    public void setId(String id) {
        this.ID = id;
    }

}
