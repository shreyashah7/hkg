/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mmodi
 */
@Document(collection = "roughstockdetail")
public class HkRoughStockDetailDocument {

    @Id
    private ObjectId id;
//    @DBRef
//    removed reference as it was frequently used in issue/receive and parceldocument was not required in that case
    private ObjectId parcel;
    private Long pieces;
    private Double carat;
    private Double rate;
//    private Integer year;
    private String operation; // Rough, Sell, Merge, Memo,Issue,Receive
    private String action; //D - Debit C - Credit
    //Employee Id
    private Long empId;
    //Department Id
    private Long depId;
    private Double exchangeRate;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModefiedOn;
    private Long frenchiseId;
    private ObjectId lotId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getParcel() {
        return parcel;
    }

    public void setParcel(ObjectId parcel) {
        this.parcel = parcel;
    }

    public Long getPieces() {
        return pieces;
    }

    public void setPieces(Long pieces) {
        this.pieces = pieces;
    }

    public Double getCarat() {
        return carat;
    }

    public void setCarat(Double carat) {
        this.carat = carat;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

//    public Integer getYear() {
//        return year;
//    }
//
//    public void setYear(Integer year) {
//        this.year = year;
//    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModefiedOn() {
        return lastModefiedOn;
    }

    public void setLastModefiedOn(Date lastModefiedOn) {
        this.lastModefiedOn = lastModefiedOn;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public Long getFrenchiseId() {
        return frenchiseId;
    }

    public void setFrenchiseId(Long frenchiseId) {
        this.frenchiseId = frenchiseId;
    }

    public ObjectId getLotId() {
        return lotId;
    }

    public void setLotId(ObjectId lotId) {
        this.lotId = lotId;
    }

}
