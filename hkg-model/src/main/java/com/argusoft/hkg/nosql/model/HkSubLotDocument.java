/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "sublot")
public class HkSubLotDocument extends GenericDocument {

    @Id
    private ObjectId id;
    @Indexed
    private ObjectId invoice;
    @Indexed
    private ObjectId parcel;
    private String subLotId;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private ObjectId associatedLot;
//    private Date lastModifiedOn;

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

    public String getSubLotId() {
        return subLotId;
    }

    public void setSubLotId(String subLotId) {
        this.subLotId = subLotId;
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

    public ObjectId getAssociatedLot() {
        return associatedLot;
    }

    public void setAssociatedLot(ObjectId associatedLot) {
        this.associatedLot = associatedLot;
    }

//    public Date getLastModifiedOn() {
//        return lastModifiedOn;
//    }
//
//    public void setLastModifiedOn(Date lastModifiedOn) {
//        this.lastModifiedOn = lastModifiedOn;
//    }

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
        final HkSubLotDocument other = (HkSubLotDocument) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HkSubLotDocument{" + "id=" + id + ", invoice=" + invoice + ", parcel=" + parcel + ", subLotId=" + subLotId + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + " '}'";
    }

}
