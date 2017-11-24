/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_feature_section_rel_info")
@NamedQueries({
    @NamedQuery(name = "HkFeatureSectionEntity.findAll", query = "SELECT h FROM HkFeatureSectionEntity h")})
public class HkFeatureSectionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(nullable = false)
    private long feature;
    @Basic(optional = false)
    @Column(name = "is_view_only", nullable = false)
    private boolean isViewOnly;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "section", insertable = false, updatable = false)
    private Long sectionId;
    @JoinColumn(name = "section", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkSectionEntity section;

    public HkFeatureSectionEntity() {
    }

    public HkFeatureSectionEntity(Long id) {
        this.id = id;
    }

    public HkFeatureSectionEntity(Long id, long feature, boolean isViewOnly, boolean isArchive, Date createdOn) {
        this.id = id;
        this.feature = feature;
        this.isViewOnly = isViewOnly;
        this.isArchive = isArchive;
        this.createdOn = createdOn;
    }

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

    public boolean getIsViewOnly() {
        return isViewOnly;
    }

    public void setIsViewOnly(boolean isViewOnly) {
        this.isViewOnly = isViewOnly;
    }

    public boolean getIsArchive() {
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

    public HkSectionEntity getSection() {
        return section;
    }

    public void setSection(HkSectionEntity section) {
        this.section = section;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkFeatureSectionEntity)) {
            return false;
        }
        HkFeatureSectionEntity other = (HkFeatureSectionEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkFeatureSectionEntity[ id=" + id + " ]";
    }
    
}
