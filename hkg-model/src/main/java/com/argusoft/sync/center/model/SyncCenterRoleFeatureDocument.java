/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "rolefeature")
public class SyncCenterRoleFeatureDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String mapUrlRoles;
    private Map<Long, List<SyncCenterFeatureDocument>> mapRoleFeatures = null;
    private Date modifiedOn;
    public SyncCenterRoleFeatureDocument() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMapUrlRoles() {
        return mapUrlRoles;
    }

    public void setMapUrlRoles(String mapUrlRoles) {
        this.mapUrlRoles = mapUrlRoles;
    }

    public Map<Long, List<SyncCenterFeatureDocument>> getMapRoleFeatures() {
        return mapRoleFeatures;
    }

    public void setMapRoleFeatures(Map<Long, List<SyncCenterFeatureDocument>> mapRoleFeatures) {
        this.mapRoleFeatures = mapRoleFeatures;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public String toString() {
        return "RoleFeatureDocument{" + "id=" + id + ", mapUrlRoles=" + mapUrlRoles + ", mapRoleFeatures=" + mapRoleFeatures + '}';
    }

}
