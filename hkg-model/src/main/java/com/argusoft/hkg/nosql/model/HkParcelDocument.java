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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "parcel")
public class HkParcelDocument extends GenericDocument {

    @Id
    private ObjectId id;
    @Indexed
    private ObjectId invoice;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;
    private Boolean haveValue;
    private Boolean haveLot = false;
    private List<ObjectId> mergedFrom;
    private Long stockPieces;
    private Double stockCarat;
    private Integer year;
    private Integer sequenceNumber;
    //Added by harshit for Issue/Receive feature
    //Employee Id in stock of
    private Long inStockOf;
    //Department Id in stock of
    private Long inStockOfDep;
    //Issue/receive status C(Complete when issue/receive flow complete)/P(In pending status)
    private String issueReceiveStatus;
    //for the use of rough purchase
    private Boolean isLinked;
    private List<ObjectId> associatedPurchases;
    private Long issueToFranchise;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getInvoice() {
        return invoice;
    }

    public void setInvoice(ObjectId invoice) {
        this.invoice = invoice;
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

    public Boolean getHaveValue() {
        return haveValue;
    }

    public void setHaveValue(Boolean haveValue) {
        this.haveValue = haveValue;
    }

    public Boolean getHaveLot() {
        return haveLot;
    }

    public void setHaveLot(Boolean haveLot) {
        this.haveLot = haveLot;
    }

    public List<ObjectId> getMergedFrom() {
        return mergedFrom;
    }

    public void setMergedFrom(List<ObjectId> mergedFrom) {
        this.mergedFrom = mergedFrom;
    }

    public Long getStockPieces() {
        return stockPieces;
    }

    public void setStockPieces(Long stockPieces) {
        this.stockPieces = stockPieces;
    }

    public Double getStockCarat() {
        return stockCarat;
    }

    public void setStockCarat(Double stockCarat) {
        this.stockCarat = stockCarat;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getInStockOf() {
        return inStockOf;
    }

    public void setInStockOf(Long inStockOf) {
        this.inStockOf = inStockOf;
    }

    public Long getInStockOfDep() {
        return inStockOfDep;
    }

    public void setInStockOfDep(Long inStockOfDep) {
        this.inStockOfDep = inStockOfDep;
    }

    public String getIssueReceiveStatus() {
        return issueReceiveStatus;
    }

    public void setIssueReceiveStatus(String issueReceiveStatus) {
        this.issueReceiveStatus = issueReceiveStatus;
    }

    public Boolean getIsLinked() {
        return isLinked;
    }

    public void setIsLinked(Boolean isLinked) {
        this.isLinked = isLinked;
    }

    public List<ObjectId> getAssociatedPurchases() {
        return associatedPurchases;
    }

    public void setAssociatedPurchases(List<ObjectId> associatedPurchases) {
        this.associatedPurchases = associatedPurchases;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getIssueToFranchise() {
        return issueToFranchise;
    }

    public void setIssueToFranchise(Long issueToFranchise) {
        this.issueToFranchise = issueToFranchise;
    }

}
