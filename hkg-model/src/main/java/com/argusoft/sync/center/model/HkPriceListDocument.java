/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mmodi
 */
@Document(collection = "pricelist")
public class HkPriceListDocument {

    @Id
    private Long id;
    private Date uploadedOn;
    private Long uploadedBy;
    private String status;
    private Long franchise;
    private String uploadedFileName;
    private Date lastModifiedOn;
    private Collection<HkPriceListDetailDocument> hkPriceListDetailEntityCollection;
    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Collection<HkPriceListDetailDocument> getHkPriceListDetailEntityCollection() {
        return hkPriceListDetailEntityCollection;
    }

    public void setHkPriceListDetailEntityCollection(Collection<HkPriceListDetailDocument> hkPriceListDetailEntityCollection) {
        this.hkPriceListDetailEntityCollection = hkPriceListDetailEntityCollection;
    }

    @Override
    public String toString() {
        return "HkPriceListDocument{" + "id=" + id + ", uploadedOn=" + uploadedOn + ", uploadedBy=" + uploadedBy + ", status=" + status + ", franchise=" + franchise + ", uploadedFileName=" + uploadedFileName + ", lastModifiedOn=" + lastModifiedOn + ", hkPriceListDetailEntityCollection=" + hkPriceListDetailEntityCollection + '}';
    }

}
