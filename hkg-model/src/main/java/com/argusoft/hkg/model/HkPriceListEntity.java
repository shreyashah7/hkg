/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author mmodi
 */
@Entity
@Table(name = "hk_price_list_info")
@NamedQueries({
    @NamedQuery(name = "HkPriceListEntity.findAll", query = "SELECT h FROM HkPriceListEntity h")})
public class HkPriceListEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "uploaded_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedOn;
    @Basic(optional = false)
    @Column(name = "uploaded_by", nullable = false)
    private long uploadedBy;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "uploaded_file_name", nullable = false, length = 100)
    private String uploadedFileName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkPriceListEntity")
    private Collection<HkPriceListDetailEntity> hkPriceListDetailEntityCollection;

    public HkPriceListEntity() {
    }

    public HkPriceListEntity(Long id) {
        this.id = id;
    }

    public HkPriceListEntity(Long id, Date uploadedOn, long uploadedBy, String status, long franchise, String uploadedFileName) {
        this.id = id;
        this.uploadedOn = uploadedOn;
        this.uploadedBy = uploadedBy;
        this.status = status;
        this.franchise = franchise;
        this.uploadedFileName = uploadedFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Collection<HkPriceListDetailEntity> getHkPriceListDetailEntityCollection() {
        return hkPriceListDetailEntityCollection;
    }

    public void setHkPriceListDetailEntityCollection(Collection<HkPriceListDetailEntity> hkPriceListDetailEntityCollection) {
        this.hkPriceListDetailEntityCollection = hkPriceListDetailEntityCollection;
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
        if (!(object instanceof HkPriceListEntity)) {
            return false;
        }
        HkPriceListEntity other = (HkPriceListEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkPriceListEntity[ id=" + id + " ]";
    }
    
}
