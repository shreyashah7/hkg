/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.databeans;

/**
 *
 * {id:'cost_staff', data: {label: 'Cost Staff', background_color: '#EB99EB'}},
 *
 * {id:'cost_staff_head', data: {label: 'Cost Staff Head', background_color:
 * '#8AA9B1', isDeg: true}},
 *
 * @author shifa
 */
public class NodeDataInfoDataBean {

    private String label;
    private String backgroundColor;
    private Boolean isDesignation;
    

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Boolean getIsDesignation() {
        return isDesignation;
    }

    public void setIsDesignation(Boolean isDesignation) {
        this.isDesignation = isDesignation;
    }

    @Override
    public String toString() {
        return "DataInfoDataBean{" + "label=" + label + ", backgroundColor=" + backgroundColor + ", isDesignation=" + isDesignation + '}';
    }
    

}
