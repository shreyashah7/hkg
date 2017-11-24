/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.databeans;

/**
 *
 *  ["cost_staff", "assortment", {color: '#5C5C5D', weight:2, type:'default'}],
    ["cost_staff", "processing", {color: '#5C5C5D', type:'rev'}],
    ["assortment", "processing", {color: '#5C5C5D', weight:2, type:'default'}],
    
 * @author shifa
 */
public class EdgeDataInfoDataBean {
    private String color;
    private Integer weight;
    private String type;
    private Boolean isSelf;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(Boolean isSelf) {
        this.isSelf = isSelf;
    }

    @Override
    public String toString() {
        return "EdgeDataInfoDataBean{" + "color=" + color + ", weight=" + weight + ", type=" + type + ", self=" + isSelf + '}';
    }

}
