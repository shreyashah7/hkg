/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mmodi
 */
@Document(collection = "lotstockdetail")
public class HkLotStockDetailDocument {

    @Id
    private ObjectId id;
    //Lot Id
    private ObjectId lot;
    private Long pieces;
    private Double carat;
    private Double rate;
    private String operation; //  Merge, Memo,Request,Issue,Collect,Receive
    private String action; //D - Debit C - Credit
    //Employee Id
    private Long empId;
    //Department Id
    private Long depId;
    private ObjectId packetId;
    private Double exchangeRate;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModefiedOn;
    private Long frenchiseId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getLot() {
        return lot;
    }

    public void setLot(ObjectId lot) {
        this.lot = lot;
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

    public ObjectId getPacketId() {
        return packetId;
    }

    public void setPacketId(ObjectId packetId) {
        this.packetId = packetId;
    }

    
    @Override
    public String toString() {
        return "HkLotStockDetailDocument{" + "id=" + id + ", lot=" + lot + ", pieces=" + pieces + ", carat=" + carat + ", rate=" + rate + ", operation=" + operation + ", action=" + action + ", empId=" + empId + ", depId=" + depId + ", packetId=" + packetId + ", exchangeRate=" + exchangeRate + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModefiedOn=" + lastModefiedOn + ", frenchiseId=" + frenchiseId + '}';
    }
}
