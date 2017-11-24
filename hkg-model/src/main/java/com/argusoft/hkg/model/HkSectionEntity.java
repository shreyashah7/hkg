/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_section_info")
@NamedQueries({
    @NamedQuery(name = "HkSectionEntity.findAll", query = "SELECT h FROM HkSectionEntity h")})
public class HkSectionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "section_code", length = 50)
    private String sectionCode;
    @Basic(optional = false)
    @Column(name = "section_name", nullable = false, length = 200)
    private String sectionName;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section", fetch = FetchType.LAZY)
    private Set<HkFeatureSectionEntity> hkFeatureSectionRelationEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section", fetch = FetchType.LAZY)
    private Set<HkFieldEntity> hkFieldEntitySet;

    public HkSectionEntity() {
    }

    public HkSectionEntity(Long id) {
        this.id = id;
    }

    public HkSectionEntity(Long id, String sectionName, Date createdOn, boolean isArchive) {
        this.id = id;
        this.sectionName = sectionName;
        this.createdOn = createdOn;
        this.isArchive = isArchive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }        

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Set<HkFeatureSectionEntity> getHkFeatureSectionRelationEntitySet() {
        return hkFeatureSectionRelationEntitySet;
    }

    public void setHkFeatureSectionRelationEntitySet(Set<HkFeatureSectionEntity> hkFeatureSectionRelationEntitySet) {
        this.hkFeatureSectionRelationEntitySet = hkFeatureSectionRelationEntitySet;
    }

    public Set<HkFieldEntity> getHkFieldEntitySet() {
        return hkFieldEntitySet;
    }

    public void setHkFieldEntitySet(Set<HkFieldEntity> hkFieldEntitySet) {
        this.hkFieldEntitySet = hkFieldEntitySet;
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
        if (!(object instanceof HkSectionEntity)) {
            return false;
        }
        HkSectionEntity other = (HkSectionEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkSectionEntity[ id=" + id + " ]";
    }
    
}
