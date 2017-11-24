/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.List;

/**
 *
 * @author akta
 */
public class SystemFeatureDataBean {

    private Long id;
    private String name;
    private String type;
    private String menuLabel;
    private String description;
    private List<SystemFeatureDataBean> iteamAttributesList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SystemFeatureDataBean> getIteamAttributesList() {
        return iteamAttributesList;
    }

    public void setIteamAttributesList(List<SystemFeatureDataBean> iteamAttributesList) {
        this.iteamAttributesList = iteamAttributesList;
    }

}
