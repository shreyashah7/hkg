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
 * @author mmodi
 */
@Document(collection="personcapability")
public class HkPersonCapabilityDocument {

    @Id
    private ObjectId id;   
    private Long forPerson;
    private String propertyName;
    private Long propertyValue;
    private Integer totalFinalPlans;   
    private Integer succeededFinalPlans;
    private Integer failedFinalPlans;
    private Float successRatio;
    private Float breakagePercentage;
    private String status;
    private Date createdOn;
    private Date lastModifiedOn;
    private Long franchise;

    public HkPersonCapabilityDocument() {
    }    

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getForPerson() {
        return forPerson;
    }

    public void setForPerson(Long forPerson) {
        this.forPerson = forPerson;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Long propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getTotalFinalPlans() {
        return totalFinalPlans;
    }

    public void setTotalFinalPlans(Integer totalFinalPlans) {
        this.totalFinalPlans = totalFinalPlans;
    }

    public Integer getSucceededFinalPlans() {
        return succeededFinalPlans;
    }

    public void setSucceededFinalPlans(Integer succeededFinalPlans) {
        this.succeededFinalPlans = succeededFinalPlans;
    }

    public Integer getFailedFinalPlans() {
        return failedFinalPlans;
    }

    public void setFailedFinalPlans(Integer failedFinalPlans) {
        this.failedFinalPlans = failedFinalPlans;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Float getSuccessRatio() {
        return successRatio;
    }

    public void setSuccessRatio(Float successRatio) {
        this.successRatio = successRatio;
    }

    public Float getBreakagePercentage() {
        return breakagePercentage;
    }

    public void setBreakagePercentage(Float breakagePercentage) {
        this.breakagePercentage = breakagePercentage;
    }    
    
    @Override
    public String toString() {
        return "HkPersonCapabilityDocument{" + "id=" + id + '}';
    }     
}
