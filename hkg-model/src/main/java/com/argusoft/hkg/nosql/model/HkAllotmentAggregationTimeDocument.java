/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

/**
 *
 * @author rajkumar
 */
public class HkAllotmentAggregationTimeDocument {

    private String _id;
    private Long time = 0L;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HkAllotmentAggregationTimeDocument{" + "_id=" + _id + ", time=" + time + '}';
    }

}
