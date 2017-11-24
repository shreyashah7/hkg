/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.databeans;

import java.util.List;

/**
 *
 * @author shifa 
 * 
 * var graphJSON = {
  "nodes": [
    {id:'cost_staff', data: {label: 'Cost Staff', background_color: '#EB99EB'}},
    {id:'assortment', data: {label: 'Assortment', background_color: '#80FF80'}},
    {id:'processing', data: {label: 'processing', background_color: '#80FF80'}},
    
    
    
    {id:'cost_staff_head', data: {label: 'Cost Staff Head', background_color: '#8AA9B1', isDeg: true}},
    {id:'cost_staff_checker', data: {label: 'Cost Staff Checker', background_color: '#8AA9B1', isDeg: true}},
    {id:'planning_head', data: {label: 'Planning Head', background_color: '#8AA9B1', isDeg: true}},
  
    
    
  ],
  "edges": [
    ["cost_staff", "assortment", {color: '#5C5C5D', weight:2, type:'default'}],
    ["cost_staff", "processing", {color: '#5C5C5D', type:'rev'}],
    ["assortment", "processing", {color: '#5C5C5D', weight:2, type:'default'}],
    
    
    
    ["cost_staff", "cost_staff_head", {color: '#C4C4C5', type:'designation'}],
    ["cost_staff", "cost_staff_checker", {color: '#C4C4C5', type:'designation'}],
    ["planning", "planning_head", {color: '#C4C4C5', type:'designation'}],
   
};
 *
 */
public class DepartmentFlowDataBean {

    private List<NodeDataBean> nodes;
    private List<EdgeDatabean> edges;

    public List<NodeDataBean> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDataBean> nodes) {
        this.nodes = nodes;
    }

    public List<EdgeDatabean> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeDatabean> edges) {
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "DepartmentFlowDataBean{" + "nodes=" + nodes + ", edges=" + edges + '}';
    }
     

}
