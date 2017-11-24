/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author akta
 */
@Document(collection = "feature_section")
public class HkFeatureSectionDocument {

    @Id
    private Long id;
    private long feature;
    private boolean isViewOnly;
    private boolean isArchive;
    private Date createdOn;
    private Long sectionId;
    @DBRef
    private HkSectionDocument section;
    private Date lastModifiedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFeature() {
        return feature;
    }

    public void setFeature(long feature) {
        this.feature = feature;
    }

    public boolean isIsViewOnly() {
        return isViewOnly;
    }

    public void setIsViewOnly(boolean isViewOnly) {
        this.isViewOnly = isViewOnly;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public HkSectionDocument getSection() {
        return section;
    }

    public void setSection(HkSectionDocument section) {
        this.section = section;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

}
