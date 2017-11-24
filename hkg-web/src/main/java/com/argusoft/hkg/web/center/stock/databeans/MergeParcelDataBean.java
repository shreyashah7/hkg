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
public class MergeParcelDataBean {
    
    private String id;
    private Long mergedPieces;
    private Double mergedCarat;
    private Double mergedAmountInDollar;
    private Double mergedAmountInRs;
    private Double mergedRate;
    private List<RoughStockDetailDataBean> roughStockDetailDataBeans;
    private Map<String,Object> staticSearchParameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMergedPieces() {
        return mergedPieces;
    }

    public void setMergedPieces(Long mergedPieces) {
        this.mergedPieces = mergedPieces;
    }

    public Double getMergedCarat() {
        return mergedCarat;
    }

    public void setMergedCarat(Double mergedCarat) {
        this.mergedCarat = mergedCarat;
    }

    public Double getMergedAmountInDollar() {
        return mergedAmountInDollar;
    }

    public void setMergedAmountInDollar(Double mergedAmountInDollar) {
        this.mergedAmountInDollar = mergedAmountInDollar;
    }

    public Double getMergedAmountInRs() {
        return mergedAmountInRs;
    }

    public void setMergedAmountInRs(Double mergedAmountInRs) {
        this.mergedAmountInRs = mergedAmountInRs;
    }

    public Double getMergedRate() {
        return mergedRate;
    }

    public void setMergedRate(Double mergedRate) {
        this.mergedRate = mergedRate;
    }

    public List<RoughStockDetailDataBean> getRoughStockDetailDataBeans() {
        return roughStockDetailDataBeans;
    }

    public void setRoughStockDetailDataBeans(List<RoughStockDetailDataBean> roughStockDetailDataBeans) {
        this.roughStockDetailDataBeans = roughStockDetailDataBeans;
    }

    public Map<String, Object> getStaticSearchParameters() {
        return staticSearchParameters;
    }

    public void setStaticSearchParameters(Map<String, Object> staticSearchParameters) {
        this.staticSearchParameters = staticSearchParameters;
    }

    @Override
    public String toString() {
        return "MergeParcelDataBean{" + "id=" + id + ", mergedPieces=" + mergedPieces + ", mergedCarat=" + mergedCarat + ", mergedAmountInDollar=" + mergedAmountInDollar + ", mergedAmountInRs=" + mergedAmountInRs + ", mergedRate=" + mergedRate + ", roughStockDetailDataBeans=" + roughStockDetailDataBeans + ", staticSearchParameters=" + staticSearchParameters + '}';
    }

}
