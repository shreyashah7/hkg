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
public class InvoiceDataBean {

    private String id;
    private Map<String, Object> invoiceCustom;
    private Map<String, String> invoiceDbType;
    private Map<String, Map<String, Object>> featureCustomMapValue;
    private List<String> dbFieldNames;
    private Boolean isArchive;
    private Boolean searchOnParameter;
    private Map<String, List<String>> uiFieldMap;
    private Map<String, Object> ruleConfigMap;
    private Integer year;
    private Integer sequenceNumber;
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
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getInvoiceCustom() {
        return invoiceCustom;
    }

    public Map<String, List<String>> getUiFieldMap() {
        return uiFieldMap;
    }

    public void setUiFieldMap(Map<String, List<String>> uiFieldMap) {
        this.uiFieldMap = uiFieldMap;
    }

    public void setInvoiceCustom(Map<String, Object> invoiceCustom) {
        this.invoiceCustom = invoiceCustom;
    }

    public Map<String, String> getInvoiceDbType() {
        return invoiceDbType;
    }

    public void setInvoiceDbType(Map<String, String> invoiceDbType) {
        this.invoiceDbType = invoiceDbType;
    }

    public Map<String, Map<String, Object>> getFeatureCustomMapValue() {
        return featureCustomMapValue;
    }

    public void setFeatureCustomMapValue(Map<String, Map<String, Object>> featureCustomMapValue) {
        this.featureCustomMapValue = featureCustomMapValue;
    }

    public List<String> getDbFieldNames() {
        return dbFieldNames;
    }

    public void setDbFieldNames(List<String> dbFieldNames) {
        this.dbFieldNames = dbFieldNames;
    }

    public Boolean isArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Boolean isSearchOnParameter() {
        return searchOnParameter;
    }

    public void setSearchOnParameter(Boolean searchOnParameter) {
        this.searchOnParameter = searchOnParameter;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    @Override
    public String toString() {
        return "InvoiceDataBean{" + "id=" + id + ", invoiceCustom=" + invoiceCustom + ", invoiceDbType=" + invoiceDbType + ", featureCustomMapValue=" + featureCustomMapValue + ", dbFieldNames=" + dbFieldNames + ", isArchive=" + isArchive + ", searchOnParameter=" + searchOnParameter + ", uiFieldMap=" + uiFieldMap + ", ruleConfigMap=" + ruleConfigMap + ", year=" + year + ", sequenceNumber=" + sequenceNumber + ", offset=" + offset + ", limit=" + limit + '}';
    }

}
