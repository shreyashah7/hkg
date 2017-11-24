/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "audittrail")
public class AuditTrail {
    @Id
    private ObjectId id;

    private String data;
    private Date modifiedOn;
    private String className;
    private boolean isMongoDocument;

    public AuditTrail(String data, Date modifiedOn, String className, boolean isMongoDocument) {
        this.data = data;
        this.modifiedOn = modifiedOn;
        this.className = className;
        this.isMongoDocument = isMongoDocument;
    }

    /**
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the isMongoDocument
     */
    public boolean isIsMongoDocument() {
        return isMongoDocument;
    }

    /**
     * @param isMongoDocument the isMongoDocument to set
     */
    public void setIsMongoDocument(boolean isMongoDocument) {
        this.isMongoDocument = isMongoDocument;
    }

}
