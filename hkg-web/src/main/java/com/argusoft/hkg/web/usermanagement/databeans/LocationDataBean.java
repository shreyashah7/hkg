/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author heena
 */
public class LocationDataBean {

    private Long id;
    @NotNull
    @Length(max = 100, message = "Length of Location name should not be more that 100 characters.")
    private String locationName;
    private String locationType;
    private Long parentId;
    @NotNull
    private boolean isActive;
    private String displayName;
    private List<LocationDataBean> children;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<LocationDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<LocationDataBean> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "HKLocationDataBean{" + "id=" + id + ", locationName=" + locationName + ", locationType=" + locationType + ", parentId=" + parentId + ", isActive=" + isActive + ", displayName=" + displayName + ", children=" + children + '}';
    }

}
