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
@Table(name = "hk_sub_entity_field_info")
@NamedQueries({
    @NamedQuery(name = "HkSubFormFieldEntity.findAll", query = "SELECT h FROM HkSubFormFieldEntity h")})
public class HkSubFormFieldEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "component_type", nullable = false, length = 15)
    private String componentType;
    @Column(name = "sub_field_name", length = 100)
    private String subFieldName;
    @Basic(optional = false)
    @Column(name = "sub_field_label", nullable = false, length = 500)
    private String subFieldLabel;
    @Column(name = "sub_field_type", length = 50)
    private String subFieldType;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Column(name = "validation_pattern", length = 1000)
    private String validationPattern;
    @Basic(optional = false)
    @Column(name = "is_droplist_field", nullable = false)
    private boolean isDroplistField;
    @Column(name = "sequence_no")
    private Integer sequenceNo;
//    @Basic(optional = false)    
    @JoinColumn(name = "parent_field", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkFieldEntity parentField;

    public HkSubFormFieldEntity() {
    }

    public HkSubFormFieldEntity(Long id) {
        this.id = id;
    }

    public HkSubFormFieldEntity(Long id, String componentType, String subFieldLabel, long franchise, boolean isArchive, long lastModifiedBy, Date lastModifiedOn, String status, HkFieldEntity parentField) {
        this.id = id;
        this.componentType = componentType;
        this.subFieldLabel = subFieldLabel;
        this.franchise = franchise;
        this.isArchive = isArchive;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.status = status;
        this.parentField = parentField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getSubFieldName() {
        return subFieldName;
    }

    public void setSubFieldName(String subFieldName) {
        this.subFieldName = subFieldName;
    }

    public String getSubFieldLabel() {
        return subFieldLabel;
    }

    public void setSubFieldLabel(String subFieldLabel) {
        this.subFieldLabel = subFieldLabel;
    }

    public String getSubFieldType() {
        return subFieldType;
    }

    public void setSubFieldType(String subFieldType) {
        this.subFieldType = subFieldType;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public boolean getIsDroplistField() {
        return isDroplistField;
    }

    public void setIsDroplistField(boolean isDroplistField) {
        this.isDroplistField = isDroplistField;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public HkFieldEntity getParentField() {
        return parentField;
    }

    public void setParentField(HkFieldEntity parentField) {
        this.parentField = parentField;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subFieldLabel != null ? subFieldLabel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkSubFormFieldEntity)) {
            return false;
        }
        HkSubFormFieldEntity other = (HkSubFormFieldEntity) object;
        if ((this.subFieldLabel == null && other.subFieldLabel != null) || (this.subFieldLabel != null && !this.subFieldLabel.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkSubFormFieldEntity[ id=" + id + " ]";
    }

    @Override
    public HkSubFormFieldEntity clone() throws CloneNotSupportedException {
        return (HkSubFormFieldEntity) super.clone();
    }

}
