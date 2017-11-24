/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import org.bson.types.ObjectId;

/**
 *
 * @author shreya
 */
public class HkSellParcelDetailDocument {
    
    private Long sellPieces;
    private Double sellCarats;
    private Double exchangeRate;
    private ObjectId parcel;

    public HkSellParcelDetailDocument() {
    }

    public Long getSellPieces() {
        return sellPieces;
    }

    public void setSellPieces(Long sellPieces) {
        this.sellPieces = sellPieces;
    }

    public Double getSellCarats() {
        return sellCarats;
    }

    public void setSellCarats(Double sellCarats) {
        this.sellCarats = sellCarats;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public ObjectId getParcel() {
        return parcel;
    }

    public void setParcel(ObjectId parcel) {
        this.parcel = parcel;
    }
    
    
}
