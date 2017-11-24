/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shreya
 */
@Document(collection = "lot")
public class HkLotDocument extends GenericDocument {

    @Id
    private ObjectId id;
    @Indexed
    private ObjectId invoice;
    @Indexed
    private ObjectId parcel;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;
    private Boolean hasPacket;
    private Integer year;
    private Long sequenceNumber;
    private Long stockPieces;
    private Double stockCarat;
    //fields for merge
    private List<ObjectId> mergedFrom;
    //fields for split
    private List<ObjectId> splitTo;
    private ObjectId splitFrom;
    private List<ObjectId> subLots;
    private List<HkStatusHistoryDocument> statusHistoryList;
    @DBRef
    private HkIssueDocument issueDocument;
    @DBRef
    private HkIssueDocument eodIssueDocument;
    private Boolean haveValue;
    //Added by harshit for Issue/Receive feature
    //Issue/receive status C(Complete when issue/receive flow complete)/P(In pending status)
    private String issueReceiveStatus;
    private Long issueToFranchise;

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

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

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

    public ObjectId getParcel() {
        return parcel;
    }

    public void setParcel(ObjectId parcel) {
        this.parcel = parcel;
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

    public Boolean isHasPacket() {
        return hasPacket;
    }

    public void setHasPacket(Boolean hasPacket) {
        this.hasPacket = hasPacket;
    }

    public List<ObjectId> getMergedFrom() {
        return mergedFrom;
    }

    public void setMergedFrom(List<ObjectId> mergedFrom) {
        this.mergedFrom = mergedFrom;
    }

    public List<ObjectId> getSplitTo() {
        return splitTo;
    }

    public void setSplitTo(List<ObjectId> splitTo) {
        this.splitTo = splitTo;
    }

    public ObjectId getSplitFrom() {
        return splitFrom;
    }

    public void setSplitFrom(ObjectId splitFrom) {
        this.splitFrom = splitFrom;
    }

    public List<HkStatusHistoryDocument> getStatusHistoryList() {
        return statusHistoryList;
    }

    public void setStatusHistoryList(List<HkStatusHistoryDocument> statusHistoryList) {
        this.statusHistoryList = statusHistoryList;
    }

    public HkIssueDocument getIssueDocument() {
        return issueDocument;
    }

    public void setIssueDocument(HkIssueDocument issueDocument) {
        this.issueDocument = issueDocument;
    }

    public HkIssueDocument getEodIssueDocument() {
        return eodIssueDocument;
    }

    public void setEodIssueDocument(HkIssueDocument eodIssueDocument) {
        this.eodIssueDocument = eodIssueDocument;
    }

    public Boolean getHaveValue() {
        return haveValue;
    }

    public void setHaveValue(Boolean haveValue) {
        this.haveValue = haveValue;
    }

    public List<ObjectId> getSubLots() {
        return subLots;
    }

    public void setSubLots(List<ObjectId> subLots) {
        this.subLots = subLots;
    }

    public String getIssueReceiveStatus() {
        return issueReceiveStatus;
    }

    public void setIssueReceiveStatus(String issueReceiveStatus) {
        this.issueReceiveStatus = issueReceiveStatus;
    }

    public Long getIssueToFranchise() {
        return issueToFranchise;
    }

    public void setIssueToFranchise(Long issueToFranchise) {
        this.issueToFranchise = issueToFranchise;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HkLotDocument other = (HkLotDocument) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HkLotDocument{" + "id=" + id + ", invoice=" + invoice + ", parcel=" + parcel + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", status=" + status + ", hasPacket=" + hasPacket + ", year=" + year + ", sequenceNumber=" + sequenceNumber + ", stockPieces=" + stockPieces + ", stockCarat=" + stockCarat + ", mergedFrom=" + mergedFrom + ", splitTo=" + splitTo + ", splitFrom=" + splitFrom + ", statusHistoryList=" + statusHistoryList + ", issueDocument=" + issueDocument + ", eodIssueDocument=" + eodIssueDocument + ", haveValue=" + haveValue + '}';
    }

}
