/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author harshit
 */
@Document(collection = "issue_receive")
public class HkIssueReceiveDocument extends GenericDocument {

    @Id
    private ObjectId id;
    //Invoice Id
    private ObjectId invoice;
    //Parcel Id
    private ObjectId parcel;
    //Lot ID
    @Indexed
    private ObjectId lot;
    //Packet Id
    @Indexed
    private ObjectId packet;
    //Type should be (Request/Direct Issue/Direct Receive)
    private String type;
    //On request status should be Pending. On collecting status should be Collected. On Issue status should be Issue. On Receive/Direct Issue/Direct recieve should be complete.
    private String status;
    //Source department id
    private Long sourceDepId;
    //Source frenchise id
    private Long sourceFranchiseId;
    //Destination Department Id
    private Long destinationDepId;
    //Destination frenchiseId
    private Long destinationFranchiseId;
    //Stock department Id
    private Long stockDepId;
    //Stock department Franchise Id
    private Long stockFranchiseId;
    //Issued carat
    private Double issueCarat;
    //Issued pcs
    private Integer issuePcs;
    //Received employee Id
    private Long receiveBy;
    //Received date time
    private Date receivedOn;
    //Received carat
    private Double receivedCarat;
    //Received Pcs
    private Integer receivedPcs;
    //loss = Issued caret - Received carat
    private Double loss;
    //User collected ID
    private Long collectedBy;
    //Diamond collected Id
    private Date collectedOn;
    //Issued Oprator Employee Id
    private Long issuedBy;
    //Issued date time
    private Date issuedOn;
    //Issued to person head of destination dep
    private Long issueTo;
    //Request created by
    private Long createdBy;
    //Request created on
    private Date createdOn;
    private Long modifiedBy;
    //On complete record should be inactive
    private Boolean isActive;
    //Slip no
    private String slipNo;
    //Slip date
    private Date slipDate;
    //Designation id of source user
    private Long sourceDesignationId;
    //Designation id of destination assignee
    private Long destinationDesignationId;
    //IW for Inward and OW for Outword
    private String modifier;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    public Date getCollectedOn() {
        return collectedOn;
    }

    public void setCollectedOn(Date collectedOn) {
        this.collectedOn = collectedOn;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
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

    public ObjectId getLot() {
        return lot;
    }

    public void setLot(ObjectId lot) {
        this.lot = lot;
    }

    public ObjectId getPacket() {
        return packet;
    }

    public void setPacket(ObjectId packet) {
        this.packet = packet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSourceDepId() {
        return sourceDepId;
    }

    public void setSourceDepId(Long sourceDepId) {
        this.sourceDepId = sourceDepId;
    }

    public Long getDestinationDepId() {
        return destinationDepId;
    }

    public void setDestinationDepId(Long destinationDepId) {
        this.destinationDepId = destinationDepId;
    }

    public Long getStockDepId() {
        return stockDepId;
    }

    public void setStockDepId(Long stockDepId) {
        this.stockDepId = stockDepId;
    }

    public Double getIssueCarat() {
        return issueCarat;
    }

    public void setIssueCarat(Double issueCarat) {
        this.issueCarat = issueCarat;
    }

    public Integer getIssuePcs() {
        return issuePcs;
    }

    public void setIssuePcs(Integer issuePcs) {
        this.issuePcs = issuePcs;
    }

    public Long getReceiveBy() {
        return receiveBy;
    }

    public void setReceiveBy(Long receiveBy) {
        this.receiveBy = receiveBy;
    }

    public Double getLoss() {
        return loss;
    }

    public void setLoss(Double loss) {
        this.loss = loss;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Long collectedBy) {
        this.collectedBy = collectedBy;
    }

    public Long getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Long issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Long getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(Long issueTo) {
        this.issueTo = issueTo;
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

    public Double getReceivedCarat() {
        return receivedCarat;
    }

    public void setReceivedCarat(Double receivedCarat) {
        this.receivedCarat = receivedCarat;
    }

    public Integer getReceivedPcs() {
        return receivedPcs;
    }

    public void setReceivedPcs(Integer receivedPcs) {
        this.receivedPcs = receivedPcs;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public Date getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(Date slipDate) {
        this.slipDate = slipDate;
    }

    public Long getSourceFranchiseId() {
        return sourceFranchiseId;
    }

    public void setSourceFranchiseId(Long sourceFranchiseId) {
        this.sourceFranchiseId = sourceFranchiseId;
    }

    public Long getDestinationFranchiseId() {
        return destinationFranchiseId;
    }

    public void setDestinationFranchiseId(Long destinationFranchiseId) {
        this.destinationFranchiseId = destinationFranchiseId;
    }

    public Long getStockFranchiseId() {
        return stockFranchiseId;
    }

    public void setStockFranchiseId(Long stockFranchiseId) {
        this.stockFranchiseId = stockFranchiseId;
    }

    public Long getSourceDesignationId() {
        return sourceDesignationId;
    }

    public void setSourceDesignationId(Long sourceDesignationId) {
        this.sourceDesignationId = sourceDesignationId;
    }

    public Long getDestinationDesignationId() {
        return destinationDesignationId;
    }

    public void setDestinationDesignationId(Long destinationDesignationId) {
        this.destinationDesignationId = destinationDesignationId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "HkIssueReceiveDocument{" + "id=" + id + ", invoice=" + invoice + ", parcel=" + parcel + ", lot=" + lot + ", packet=" + packet + ", type=" + type + ", status=" + status + ", sourceDepId=" + sourceDepId + ", sourceFranchiseId=" + sourceFranchiseId + ", destinationDepId=" + destinationDepId + ", destinationFranchiseId=" + destinationFranchiseId + ", stockDepId=" + stockDepId + ", stockFranchiseId=" + stockFranchiseId + ", issueCarat=" + issueCarat + ", issuePcs=" + issuePcs + ", receiveBy=" + receiveBy + ", receivedOn=" + receivedOn + ", receivedCarat=" + receivedCarat + ", receivedPcs=" + receivedPcs + ", loss=" + loss + ", collectedBy=" + collectedBy + ", collectedOn=" + collectedOn + ", issuedBy=" + issuedBy + ", issuedOn=" + issuedOn + ", issueTo=" + issueTo + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", isActive=" + isActive + ", slipNo=" + slipNo + ", slipDate=" + slipDate + ", sourceDesignationId=" + sourceDesignationId + ", destinationDesignationId=" + destinationDesignationId + ", modifier=" + modifier + '}';
    }

}
