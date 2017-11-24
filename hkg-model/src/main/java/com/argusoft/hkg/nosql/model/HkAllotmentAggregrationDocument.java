/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

/**
 *
 * @author shreya
 */
public class HkAllotmentAggregrationDocument {

    private String _id;
    private Integer allotment = 0;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Integer getAllotment() {
        return allotment;
    }

    public void setAllotment(Integer allotment) {
        this.allotment = allotment;
    }

    @Override
    public String toString() {
        return "AllotmentSummary{" + "_id=" + _id + ", allotment=" + allotment + '}';
    }        
}
