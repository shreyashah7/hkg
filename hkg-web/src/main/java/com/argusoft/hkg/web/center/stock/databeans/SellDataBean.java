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
public class SellDataBean {
    private String id;
    private Long totalPieces;
    private Double totalCarat;
    private Double totalAmountInDollar;
    private Double totalAmountInRs;
    private List<String> parcels;
    private List<RoughStockDetailDataBean> roughStockDetailDataBeans;
    private Map<String, Object> sellCustom;
    private Map<String, String> sellDbType;
    private Map<String, List<String>> uiFieldMap;
    private Map<String, Object> ruleConfigMap;
    private Map<String,Object> staticSearchParameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Long totalPieces) {
        this.totalPieces = totalPieces;
    }

    public Double getTotalCarat() {
        return totalCarat;
    }

    public List<String> getParcels() {
        return parcels;
    }

    public void setParcels(List<String> parcels) {
        this.parcels = parcels;
    }

    public void setTotalCarat(Double totalCarat) {
        this.totalCarat = totalCarat;
    }

    public Double getTotalAmountInDollar() {
        return totalAmountInDollar;
    }

    public void setTotalAmountInDollar(Double totalAmountInDollar) {
        this.totalAmountInDollar = totalAmountInDollar;
    }

    public Double getTotalAmountInRs() {
        return totalAmountInRs;
    }

    public void setTotalAmountInRs(Double totalAmountInRs) {
        this.totalAmountInRs = totalAmountInRs;
    }

    public Map<String, Object> getSellCustom() {
        return sellCustom;
    }

    public void setSellCustom(Map<String, Object> sellCustom) {
        this.sellCustom = sellCustom;
    }

    public Map<String, String> getSellDbType() {
        return sellDbType;
    }

    public void setSellDbType(Map<String, String> sellDbType) {
        this.sellDbType = sellDbType;
    }

    public Map<String, List<String>> getUiFieldMap() {
        return uiFieldMap;
    }

    public void setUiFieldMap(Map<String, List<String>> uiFieldMap) {
        this.uiFieldMap = uiFieldMap;
    }

    public Map<String, Object> getStaticSearchParameters() {
        return staticSearchParameters;
    }

    public void setStaticSearchParameters(Map<String, Object> staticSearchParameters) {
        this.staticSearchParameters = staticSearchParameters;
    }

    public List<RoughStockDetailDataBean> getRoughStockDetailDataBeans() {
        return roughStockDetailDataBeans;
    }

    public void setRoughStockDetailDataBeans(List<RoughStockDetailDataBean> roughStockDetailDataBeans) {
        this.roughStockDetailDataBeans = roughStockDetailDataBeans;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    @Override
    public String toString() {
        return "SellDataBean{" + "id=" + id + ", totalPieces=" + totalPieces + ", totalCarat=" + totalCarat + ", totalAmountInDollar=" + totalAmountInDollar + ", totalAmountInRs=" + totalAmountInRs + ", parcels=" + parcels + ", roughStockDetailDataBeans=" + roughStockDetailDataBeans + ", sellCustom=" + sellCustom + ", sellDbType=" + sellDbType + ", uiFieldMap=" + uiFieldMap + ", ruleConfigMap=" + ruleConfigMap + ", staticSearchParameters=" + staticSearchParameters + '}';
    }

}
