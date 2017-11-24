/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

/**
 *
 * @author shruti
 */
public class RangeDocument {

    
     private HkCalcMasterDocument from;
  
    private HkCalcMasterDocument to;

    public RangeDocument() {
    }

    public RangeDocument(HkCalcMasterDocument from, HkCalcMasterDocument to) {
        this.from = from;
        this.to = to;
    }

    

    public boolean isInRange(Comparable value) {
        int lower = value.compareTo(getFrom().getValue());
        int upper = value.compareTo(getTo().getValue());
        if (lower >= 0 && upper <= 0) {
            return true;
        }
        return false;
    }

    /**
     * @return the from
     */
    public HkCalcMasterDocument getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(HkCalcMasterDocument from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public HkCalcMasterDocument getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(HkCalcMasterDocument to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "RangeDocument{" + "from=" + from + ", to=" + to + '}';
    }

}
