/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

/**
 *
 * @author harshit
 */
public class CustomFieldInfoDataBean {

    //Id
    private Long id;
    // Feature Id
    private Long featureId;
    // Store SectionId
    private Long sectionId;
    // List of CustomField
    private CustomFieldDataBean[] customFieldDataBeans;
    private CustomFieldDataBean customFieldDataBean;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public CustomFieldDataBean[] getCustomFieldDataBeans() {
        return customFieldDataBeans;
    }

    public void setCustomFieldDataBeans(CustomFieldDataBean[] customFieldDataBeans) {
        this.customFieldDataBeans = customFieldDataBeans;
    }

    public CustomFieldDataBean getCustomFieldDataBean() {
        return customFieldDataBean;
    }

    public void setCustomFieldDataBean(CustomFieldDataBean customFieldDataBean) {
        this.customFieldDataBean = customFieldDataBean;
    }

    @Override
    public String toString() {
        return "CustomFieldInfoDataBean{" + "id=" + id + ", featureId=" + featureId + ", sectionId=" + sectionId + ", customFieldDataBeans=" + customFieldDataBeans + ", customFieldDataBean=" + customFieldDataBean + '}';
    }

   
}
