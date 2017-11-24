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
 * @author harshit
 */
@Document(collection = "plan")
public class HkPacketPlanDocument {

    @Id
    private ObjectId id;
    private ObjectId invoice;
    private ObjectId parcel;
    @Indexed
    private ObjectId lot;
    @Indexed
    private ObjectId packet;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long empId;
    private String status;
    private Integer planId;
    private Boolean isActive;
    private Boolean isSelected;
    private ObjectId copiedFrom;
    private String packetNumber;
    private Boolean finalPlan;
    List<HkPlanDocument> hkPlanDocuments;
    private Long departmentId;
    private Long designationId;

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

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public ObjectId getCopiedFrom() {
        return copiedFrom;
    }

    public void setCopiedFrom(ObjectId copiedFrom) {
        this.copiedFrom = copiedFrom;
    }

    public List<HkPlanDocument> getHkPlanDocuments() {
        return hkPlanDocuments;
    }

    public void setHkPlanDocuments(List<HkPlanDocument> hkPlanDocuments) {
        this.hkPlanDocuments = hkPlanDocuments;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(String packetNumber) {
        this.packetNumber = packetNumber;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Boolean isFinalPlan() {
        return finalPlan;
    }

    public void setFinalPlan(Boolean finalPlan) {
        this.finalPlan = finalPlan;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    @Override
    public String toString() {
        return "HkPacketPlanDocument{" + "id=" + id + ", invoice=" + invoice + ", parcel=" + parcel + ", lot=" + lot + ", packet=" + packet + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", empId=" + empId + ", status=" + status + ", planId=" + planId + ", isActive=" + isActive + ", isSelected=" + isSelected + ", copiedFrom=" + copiedFrom + ", packetNumber=" + packetNumber + ", finalPlan=" + finalPlan + ", hkPlanDocuments=" + hkPlanDocuments + ", departmentId=" + departmentId + ", designationId=" + designationId + '}';
    }

}
