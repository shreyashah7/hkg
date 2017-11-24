/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shreya
 */
public class ParcelDataBean {

    private String id;
    private Map<String, Object> parcelCustom;
    private Map<String, String> parcelDbType;
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private Map<String, List<String>> featureDbFieldMap;
    private Map<String, Object> ruleConfigMap;
    private InvoiceDataBean invoiceDataBean;
    private List<ParcelDataBean> parcelList;
    private Boolean searchOnParameter;
    private Integer sequenceNumber;
    private Integer year;
    private Integer offset;
    private Integer limit;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, Object> getParcelCustom() {
        return parcelCustom;
    }

    public void setParcelCustom(Map<String, Object> parcelCustom) {
        this.parcelCustom = parcelCustom;
    }

    public Map<String, String> getParcelDbType() {
        return parcelDbType;
    }

    public void setParcelDbType(Map<String, String> parcelDbType) {
        this.parcelDbType = parcelDbType;
    }

    public InvoiceDataBean getInvoiceDataBean() {
        return invoiceDataBean;
    }

    public void setInvoiceDataBean(InvoiceDataBean invoiceDataBean) {
        this.invoiceDataBean = invoiceDataBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Map<String, List<Object>> getFeatureCustomMap() {
//        return featureCustomMap;
//    }
//
//    public void setFeatureCustomMap(Map<String, List<Object>> featureCustomMap) {
//        this.featureCustomMap = featureCustomMap;
//    }

    public List<ParcelDataBean> getParcelList() {
        return parcelList;
    }

    public void setParcelList(List<ParcelDataBean> parcelList) {
        this.parcelList = parcelList;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public Map<String, List<String>> getFeatureDbFieldMap() {
        return featureDbFieldMap;
    }

    public void setFeatureDbFieldMap(Map<String, List<String>> featureDbFieldMap) {
        this.featureDbFieldMap = featureDbFieldMap;
    }

    public Boolean isSearchOnParameter() {
        return searchOnParameter;
    }

    public void setSearchOnParameter(Boolean searchOnParameter) {
        this.searchOnParameter = searchOnParameter;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ParcelDataBean{" + "id=" + id + ", parcelCustom=" + parcelCustom + ", parcelDbType=" + parcelDbType + ", featureCustomMapValue=" + featureCustomMapValue + ", featureDbFieldMap=" + featureDbFieldMap + ", featureDbFieldIdMap=" + ruleConfigMap + ", invoiceDataBean=" + invoiceDataBean + ", parcelList=" + parcelList + ", searchOnParameter=" + searchOnParameter + '}';
    }

   
}
