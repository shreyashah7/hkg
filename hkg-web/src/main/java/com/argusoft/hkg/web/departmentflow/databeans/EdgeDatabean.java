/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.databeans;

/**
 *
 *  "edges": [
    ["cost_staff", "assortment", {color: '#5C5C5D', weight:2, type:'default'}],
    ["cost_staff", "processing", {color: '#5C5C5D', type:'rev'}],
    ["assortment", "processing", {color: '#5C5C5D', weight:2, type:'default'}],
    
    
    
    ["cost_staff", "cost_staff_head", {color: '#C4C4C5', type:'designation'}],
    ["cost_staff", "cost_staff_checker", {color: '#C4C4C5', type:'designation'}],
    ["planning", "planning_head", {color: '#C4C4C5', type:'designation'}],
   
};
 * @author shifa
 */
public class EdgeDatabean {
    private String fromNode;
    private String toNode;
    private EdgeDataInfoDataBean data;

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public EdgeDataInfoDataBean getData() {
        return data;
    }

    public void setData(EdgeDataInfoDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EdgeDatabean{" + "fromNode=" + fromNode + ", toNode=" + toNode + ", data=" + data + '}';
    }
    
}
