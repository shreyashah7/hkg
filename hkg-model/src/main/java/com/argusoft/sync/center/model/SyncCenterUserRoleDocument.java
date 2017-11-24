/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "userrole")
@CompoundIndexes({
    @CompoundIndex(name = "userRoleIndex", def = "{'userId':1,'roleId':1}")
})
public class SyncCenterUserRoleDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private boolean isActive;
    private boolean isArchive;

    private Long userId;
    private Long roleId;
    private Date modifiedOn;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "UserRoleCenterDocument{" + "id=" + id + ", isActive=" + isActive + ", isArchive=" + isArchive + ", userId=" + userId + ", roleId=" + roleId + '}';
    }

}
