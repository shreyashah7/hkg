/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "sell")
public class HkSellDocument extends GenericDocument {

     @Id
    private ObjectId id;
    private List<ObjectId> parcels;
    private Long createdBy;
    private Date createdOn;
    private Long totalPieces;
    private Double totalAmountInDollar;
    private Double totalAmountInRs;
    private Double totalCarat;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;
    private List<HkSellParcelDetailDocument> hkSellParcelDetailDocuments;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<ObjectId> getParcels() {
        return parcels;
    }

    public void setParcels(List<ObjectId> parcels) {
        this.parcels = parcels;
    }

    public Long getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(Long totalPieces) {
        this.totalPieces = totalPieces;
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

    public Double getTotalCarat() {
        return totalCarat;
    }

    public void setTotalCarat(Double totalCarat) {
        this.totalCarat = totalCarat;
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

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HkSellParcelDetailDocument> getHkSellParcelDetailDocuments() {
        return hkSellParcelDetailDocuments;
    }

    public void setHkSellParcelDetailDocuments(List<HkSellParcelDetailDocument> hkSellParcelDetailDocuments) {
        this.hkSellParcelDetailDocuments = hkSellParcelDetailDocuments;
    }

}
