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
@Document(collection = "departmentconfig")
public class HkDepartmentConfigDocument {

    @Id
    private ObjectId id;
    private Long department;
    private String departmentName;
    private String status;
    private Long stockRoom;
    private Long defaultDepartment;
    private String flowMode;
    private Long franchise;
    private Boolean noPhysicalDiamonds;
    private Boolean isArchive;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private Date lastModifiedOn;

    private List<HkCriteriaSetDocument> criteriaRuleList;
//    private List<HkAssociatedDesigDocument> associatedDesignations;
    private List<HkAssociatedDeptDocument> associatedDepartments;

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getStockRoom() {
        return stockRoom;
    }

    public void setStockRoom(Long stockRoom) {
        this.stockRoom = stockRoom;
    }

    public List<HkCriteriaSetDocument> getCriteriaRuleList() {
        return criteriaRuleList;
    }

    public void setCriteriaRuleList(List<HkCriteriaSetDocument> criteriaRuleList) {
        this.criteriaRuleList = criteriaRuleList;
    }

    

    public Long getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(Long defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public List<HkAssociatedDeptDocument> getAssociatedDepartments() {
        return associatedDepartments;
    }

    public void setAssociatedDepartments(List<HkAssociatedDeptDocument> associatedDepartments) {
        this.associatedDepartments = associatedDepartments;
    }

    public String getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(String flowMode) {
        this.flowMode = flowMode;
    }
//
//    public List<HkAssociatedDesigDocument> getAssociatedDesignations() {
//        return associatedDesignations;
//    }
//
//    public void setAssociatedDesignations(List<HkAssociatedDesigDocument> associatedDesignations) {
//        this.associatedDesignations = associatedDesignations;
//    }

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Boolean isNoPhysicalDiamonds() {
        return noPhysicalDiamonds;
    }

    public void setNoPhysicalDiamonds(Boolean noPhysicalDiamonds) {
        this.noPhysicalDiamonds = noPhysicalDiamonds;
    }
 
}
