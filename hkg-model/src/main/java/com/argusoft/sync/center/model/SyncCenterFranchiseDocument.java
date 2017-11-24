/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "franchise")
public class SyncCenterFranchiseDocument {

    @Id
    private Long id;
    private String name;
    private Long franchiseId;
    private String franchiseAdminId;
    private Date modifiedOn;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFranchiseAdminId() {
        return franchiseAdminId;
    }

    public void setFranchiseAdminId(String franchiseAdminId) {
        this.franchiseAdminId = franchiseAdminId;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "CenterFranchiseDocument{" + "id=" + id + ", name=" + name + ", franchiseId=" + franchiseId + ", franchiseAdminId=" + franchiseAdminId + '}';
    }

}
