package com.argusoft.hkg.web.center.stock.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class IssueReceiveDataBean {

    private String id;
    //Invoice ObjectId
    private String invoiceId;
    //Invoice Number
    private String invoiceNumber;
    //Parcel ObjectId
    private String parcelId;
    //Parcel Number
    private String parcelNumber;
    //Lot ObjectId
    private String lotId;
    //Lot Number
    private String lotNumber;
    //Packet ObjectId
    private String packetId;
    //Packet Number
    private String packetNumber;
    //Type Direct ISsue,Direct Receive,Request
    private String type;
    //DI,DR,RQ,CL,IS,RC
    private String requestType;
    //Status
    private String status;
    //Source Department Id
    private Long srcDeptId;
    private Long srcFranchiseId;
    //Destination Department Id
    private Long destDeptId;
    private Long destFranchiseId;
    //Stock Department Id
    private Long stockDeptId;
    private Long stockFranchiseId;
    //Issue carat
    private Double issueCarat;
    //Issue Pieces
    private Integer issuePcs;
    //Received By
    private Long receivedBy;
    //Received On
    private Date receivedOn;
    //receive carat
    private Double receiveCarat;
    //receive Pieces
    private Integer receivePcs;
    //Collected By
    private Long collectedBy;
    //Collected On
    private Date collectedOn;
    //Issued By
    private Long issuedBy;
    //Issued On
    private Date issuedOn;
    //Issue To User Id
    private Long issueTo;
    //isActive
    private Boolean isActive;
    //Slip date
    private Date slipDate;
    //Slip Number
    private String slipNo;
    //Modifier
    private String modifier;
    //Destination Designation Id
    private Long destinationDesignationId;
    private String createdByUserName;
    private String sourceDepartmentName;
    private String issueToUserName;
    private String destinationDepartmentName;

    private Map<String, Object> fieldValue;
    private Map<String, String> fieldDbType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(String packetNumber) {
        this.packetNumber = packetNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSrcDeptId() {
        return srcDeptId;
    }

    public void setSrcDeptId(Long srcDeptId) {
        this.srcDeptId = srcDeptId;
    }

    public Long getDestDeptId() {
        return destDeptId;
    }

    public void setDestDeptId(Long destDeptId) {
        this.destDeptId = destDeptId;
    }

    public Long getStockDeptId() {
        return stockDeptId;
    }

    public void setStockDeptId(Long stockDeptId) {
        this.stockDeptId = stockDeptId;
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

    public Long getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Long receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    public Double getReceiveCarat() {
        return receiveCarat;
    }

    public void setReceiveCarat(Double receiveCarat) {
        this.receiveCarat = receiveCarat;
    }

    public Integer getReceivePcs() {
        return receivePcs;
    }

    public void setReceivePcs(Integer receivePcs) {
        this.receivePcs = receivePcs;
    }

    public Long getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Long collectedBy) {
        this.collectedBy = collectedBy;
    }

    public Date getCollectedOn() {
        return collectedOn;
    }

    public void setCollectedOn(Date collectedOn) {
        this.collectedOn = collectedOn;
    }

    public Long getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Long issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    public Long getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(Long issueTo) {
        this.issueTo = issueTo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Date getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(Date slipDate) {
        this.slipDate = slipDate;
    }

    public String getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public Long getSrcFranchiseId() {
        return srcFranchiseId;
    }

    public void setSrcFranchiseId(Long srcFranchiseId) {
        this.srcFranchiseId = srcFranchiseId;
    }

    public Long getDestFranchiseId() {
        return destFranchiseId;
    }

    public void setDestFranchiseId(Long destFranchiseId) {
        this.destFranchiseId = destFranchiseId;
    }

    public Long getStockFranchiseId() {
        return stockFranchiseId;
    }

    public void setStockFranchiseId(Long stockFranchiseId) {
        this.stockFranchiseId = stockFranchiseId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Long getDestinationDesignationId() {
        return destinationDesignationId;
    }

    public void setDestinationDesignationId(Long destinationDesignationId) {
        this.destinationDesignationId = destinationDesignationId;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getSourceDepartmentName() {
        return sourceDepartmentName;
    }

    public void setSourceDepartmentName(String sourceDepartmentName) {
        this.sourceDepartmentName = sourceDepartmentName;
    }

    public String getIssueToUserName() {
        return issueToUserName;
    }

    public void setIssueToUserName(String issueToUserName) {
        this.issueToUserName = issueToUserName;
    }

    public String getDestinationDepartmentName() {
        return destinationDepartmentName;
    }

    public void setDestinationDepartmentName(String destinationDepartmentName) {
        this.destinationDepartmentName = destinationDepartmentName;
    }

    public Map<String, Object> getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Map<String, Object> fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Map<String, String> getFieldDbType() {
        return fieldDbType;
    }

    public void setFieldDbType(Map<String, String> fieldDbType) {
        this.fieldDbType = fieldDbType;
    }

    @Override
    public String toString() {
        return "IssueReceiveDataBean{" + "id=" + id + ", invoiceId=" + invoiceId + ", invoiceNumber=" + invoiceNumber + ", parcelId=" + parcelId + ", parcelNumber=" + parcelNumber + ", lotId=" + lotId + ", lotNumber=" + lotNumber + ", packetId=" + packetId + ", packetNumber=" + packetNumber + ", type=" + type + ", requestType=" + requestType + ", status=" + status + ", srcDeptId=" + srcDeptId + ", srcFranchiseId=" + srcFranchiseId + ", destDeptId=" + destDeptId + ", destFranchiseId=" + destFranchiseId + ", stockDeptId=" + stockDeptId + ", stockFranchiseId=" + stockFranchiseId + ", issueCarat=" + issueCarat + ", issuePcs=" + issuePcs + ", receivedBy=" + receivedBy + ", receivedOn=" + receivedOn + ", receiveCarat=" + receiveCarat + ", receivePcs=" + receivePcs + ", collectedBy=" + collectedBy + ", collectedOn=" + collectedOn + ", issuedBy=" + issuedBy + ", issuedOn=" + issuedOn + ", issueTo=" + issueTo + ", isActive=" + isActive + ", slipDate=" + slipDate + ", slipNo=" + slipNo + ", modifier=" + modifier + ", destinationDesignationId=" + destinationDesignationId + ", createdByUserName=" + createdByUserName + ", sourceDepartmentName=" + sourceDepartmentName + ", issueToUserName=" + issueToUserName + ", destinationDepartmentName=" + destinationDepartmentName + ", fieldValue=" + fieldValue + ", fieldDbType=" + fieldDbType + '}';
    }

}
