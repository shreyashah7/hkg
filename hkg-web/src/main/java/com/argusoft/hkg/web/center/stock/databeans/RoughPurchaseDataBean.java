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
public class RoughPurchaseDataBean {
    
    private String id;
    private Map<String, Object> roughPurchaseCustom;
    private Map<String, String> roughPurchaseDbType;
    private List<String> dbFieldNames;
    private Boolean isArchive;
    private Boolean searchOnParameter;
    private Integer sequenceNumber;
    private Integer year;
    private Integer offset;
    private Integer limit;
    private Map<String, List<String>> uiFieldMap;
    private Map<String, Object> ruleConfigMap;

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

    public Map<String, Object> getRoughPurchaseCustom() {
        return roughPurchaseCustom;
    }

    public void setRoughPurchaseCustom(Map<String, Object> roughPurchaseCustom) {
        this.roughPurchaseCustom = roughPurchaseCustom;
    }

    public Map<String, String> getRoughPurchaseDbType() {
        return roughPurchaseDbType;
    }

    public void setRoughPurchaseDbType(Map<String, String> roughPurchaseDbType) {
        this.roughPurchaseDbType = roughPurchaseDbType;
    }

    public List<String> getDbFieldNames() {
        return dbFieldNames;
    }

    public void setDbFieldNames(List<String> dbFieldNames) {
        this.dbFieldNames = dbFieldNames;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Boolean getSearchOnParameter() {
        return searchOnParameter;
    }

    public void setSearchOnParameter(Boolean searchOnParameter) {
        this.searchOnParameter = searchOnParameter;
    }

    public Map<String, List<String>> getUiFieldMap() {
        return uiFieldMap;
    }

    public void setUiFieldMap(Map<String, List<String>> uiFieldMap) {
        this.uiFieldMap = uiFieldMap;
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

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    @Override
    public String toString() {
        return "RoughPurchaseDataBean{" + "id=" + id + ", roughPurchaseCustom=" + roughPurchaseCustom + ", roughPurchaseDbType=" + roughPurchaseDbType + ", dbFieldNames=" + dbFieldNames + ", isArchive=" + isArchive + ", searchOnParameter=" + searchOnParameter + ", sequenceNumber=" + sequenceNumber + ", year=" + year + ", offset=" + offset + ", limit=" + limit + ", uiFieldMap=" + uiFieldMap + ", ruleConfigMap=" + ruleConfigMap + '}';
    }

}
