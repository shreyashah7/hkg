/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

/**
 *
 * @author shreya
 */
public class RoughStockDetailDataBean {

    private String id;
    private Long changedPieces;
    private Double changedCarat;
    private Double changedRate;
    private Double changedExchangeRate;
    private Double changedAmountInDollar;
    private Double changedAmountInRs;
    private String parcel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getChangedPieces() {
        return changedPieces;
    }

    public void setChangedPieces(Long changedPieces) {
        this.changedPieces = changedPieces;
    }

    public Double getChangedCarat() {
        return changedCarat;
    }

    public void setChangedCarat(Double changedCarat) {
        this.changedCarat = changedCarat;
    }

    public Double getChangedRate() {
        return changedRate;
    }

    public void setChangedRate(Double changedRate) {
        this.changedRate = changedRate;
    }

    public Double getChangedExchangeRate() {
        return changedExchangeRate;
    }

    public void setChangedExchangeRate(Double changedExchangeRate) {
        this.changedExchangeRate = changedExchangeRate;
    }

    public Double getChangedAmountInDollar() {
        return changedAmountInDollar;
    }

    public void setChangedAmountInDollar(Double changedAmountInDollar) {
        this.changedAmountInDollar = changedAmountInDollar;
    }

    public Double getChangedAmountInRs() {
        return changedAmountInRs;
    }

    public void setChangedAmountInRs(Double changedAmountInRs) {
        this.changedAmountInRs = changedAmountInRs;
    }

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    @Override
    public String toString() {
        return "RoughStockDetailDataBean{" + "id=" + id + ", changedPieces=" + changedPieces + ", changedCarat=" + changedCarat + ", changedRate=" + changedRate + ", changedExchangeRate=" + changedExchangeRate + ", changedAmountInDollar=" + changedAmountInDollar + ", changedAmountInRs=" + changedAmountInRs + ", parcel=" + parcel + '}';
    }

}
