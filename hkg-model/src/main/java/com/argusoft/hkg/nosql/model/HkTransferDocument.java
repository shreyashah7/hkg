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
 * @author dhwani
 */
@Document(collection = "transfer")
public class HkTransferDocument extends GenericDocument {

     @Id
    private ObjectId id;
    private ObjectId invoice;
    private ObjectId parcel;
    private ObjectId lot;
    private ObjectId packet;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;

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

    public ObjectId getPacket() {
        return packet;
    }

    public void setPacket(ObjectId packet) {
        this.packet = packet;
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

    public ObjectId getLot() {
        return lot;
    }

    public void setLot(ObjectId lot) {
        this.lot = lot;
    }
}
