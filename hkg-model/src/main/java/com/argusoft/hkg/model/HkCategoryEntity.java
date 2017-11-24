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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_system_category_mst")
public class HkCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "category_prefix", length = 20)
    private String categoryPrefix;
    @Basic(optional = false)
    @Column(name = "category_title", nullable = false, length = 200)
    private String categoryTitle;
    @Column(name = "category_type", length = 5)
    private String categoryType;
    @Column(length = 5000)
    private String description;
    @Column(name = "start_index")
    private Integer startIndex;
    @Column(name = "current_index")
    private Integer currentIndex;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private HkCategoryEntity parent;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Column(name = "have_diamond_processing_mch")
    private Boolean haveDiamondProcessingMch;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Column(name = "category_pattern", length = 20)
    private String categoryPattern;

    public HkCategoryEntity() {
    }

    public HkCategoryEntity(Long id) {
        this.id = id;
    }

    public HkCategoryEntity(Long id, String categoryTitle, HkCategoryEntity parent, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, boolean isActive, boolean isArchive, long franchise, boolean haveDiamondProcessingMch) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.parent = parent;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.isActive = isActive;
        this.isArchive = isArchive;
        this.franchise = franchise;
        this.haveDiamondProcessingMch = haveDiamondProcessingMch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryPrefix() {
        return categoryPrefix;
    }

    public void setCategoryPrefix(String categoryPrefix) {
        this.categoryPrefix = categoryPrefix;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public HkCategoryEntity getParent() {
        return parent;
    }

    public void setParent(HkCategoryEntity parent) {
        this.parent = parent;
    }

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

    public Boolean getHaveDiamondProcessingMch() {
        return haveDiamondProcessingMch;
    }

    public void setHaveDiamondProcessingMch(Boolean haveDiamondProcessingMch) {
        this.haveDiamondProcessingMch = haveDiamondProcessingMch;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public String getCategoryPattern() {
        return categoryPattern;
    }

    public void setCategoryPattern(String categoryPattern) {
        this.categoryPattern = categoryPattern;
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
        if (!(object instanceof HkCategoryEntity)) {
            return false;
        }
        HkCategoryEntity other = (HkCategoryEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkCategoryEntity[ id=" + id + " ]";
    }
    
}
