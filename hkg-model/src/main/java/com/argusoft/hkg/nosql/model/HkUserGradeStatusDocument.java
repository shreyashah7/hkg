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
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author dhwani
 */
@Document(collection = "usergradestatus")
public class HkUserGradeStatusDocument {

    @Id
    private ObjectId id;
    private Long userId;
    private Long grade;
    private Long goingToGrade;
    private Integer noOfDiamondsInStock;
    private Date lastAllocatedOn;
    private Long franchise;
    private Boolean isArchive;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private Long department;
    private List<Long> designations;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Long getGoingToGrade() {
        return goingToGrade;
    }

    public void setGoingToGrade(Long goingToGrade) {
        this.goingToGrade = goingToGrade;
    }

    public Integer getNoOfDiamondsInStock() {
        return noOfDiamondsInStock;
    }

    public void setNoOfDiamondsInStock(Integer noOfDiamondsInStock) {
        this.noOfDiamondsInStock = noOfDiamondsInStock;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
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

    public Date getLastAllocatedOn() {
        return lastAllocatedOn;
    }

    public void setLastAllocatedOn(Date lastAllocatedOn) {
        this.lastAllocatedOn = lastAllocatedOn;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public List<Long> getDesignations() {
        return designations;
    }

    public void setDesignations(List<Long> designations) {
        this.designations = designations;
    }
}
