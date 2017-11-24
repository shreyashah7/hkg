/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author dhwani
 */
public class RuleSetDataBean {

    private String id;
    private String name;
    private Long franchise;
    List<RuleDatabean> rules;
    private Boolean isArchive;
    private Boolean isActive;
    private long createdBy;
    private Date createdOn;
    private long lastModifiedBy;
    private Date lastModifiedOn;

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public List<RuleDatabean> getRules() {
        return rules;
    }

    public void setRules(List<RuleDatabean> rules) {
        this.rules = rules;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "RuleSetDataBean{" + "id=" + id + ", name=" + name + ", franchise=" + franchise + ", rules=" + rules + ", isArchive=" + isArchive + ", isActive=" + isActive + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + '}';
    }

}
