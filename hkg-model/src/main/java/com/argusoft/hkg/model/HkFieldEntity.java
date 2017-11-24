/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_field_info")
@NamedQueries({
    @NamedQuery(name = "HkFieldEntity.findAll", query = "SELECT h FROM HkFieldEntity h")})
public class HkFieldEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "field_label", nullable = false, length = 500)
    private String fieldLabel;
    @Column(name = "field_type", length = 50)
    private String fieldType;
    @Basic(optional = false)
    @Column(name = "component_type", nullable = false, length = 15)
    private String componentType;
    @Column(name = "ui_field_name", length = 100)
    private String uiFieldName;
    @Column(name = "db_field_name", length = 100)
    private String dbFieldName;
    @Basic(optional = false)
    @Column(name = "is_custom_field", nullable = false)
    private boolean isCustomField;
    @Column(name = "is_editable")
    private Boolean isEditable;
    @Column(name = "is_dependant")
    private Boolean isDependant;
    @Column(name = "db_base_name", length = 200)
    private String dbBaseName;
    @Column(name = "db_base_type", length = 10)
    private String dbBaseType;
    @Basic(optional = false)
    @Column(nullable = false)
    private long feature;
    @Column(name = "associated_currency")
    private Integer associatedCurrency;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "franchise")
    private Long franchise;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Column(name = "created_by_franchise")
    private Long createdByFranchise;
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
    @JoinColumn(name = "section", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HkSectionEntity section;
    @Column(name = "validation_pattern", length = 1000)
    private String validationPattern;
    @Column(name = "formula_value", length = 1000)
    private String formulaValue;
    @Transient
    private String fieldValues;
    @Column(name = "source_field_id")
    private Long sourceFieldId;
    @Column(name = "is_private")
    private Boolean isPrivate;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentField", fetch = FetchType.EAGER)
    private Set<HkSubFormFieldEntity> hkSubFormFields;

    public HkFieldEntity() {
    }

    public HkFieldEntity(Long id) {
        this.id = id;
    }

    public HkFieldEntity(Long id, String fieldLabel, String componentType, boolean isCustomField, long feature, String status, boolean isArchive, Long franchise, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, Long createdByFranchise) {
        this.id = id;
        this.fieldLabel = fieldLabel;
        this.componentType = componentType;
        this.isCustomField = isCustomField;
        this.feature = feature;
        this.status = status;
        this.isArchive = isArchive;
        this.franchise = franchise;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.createdByFranchise = createdByFranchise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getUiFieldName() {
        return uiFieldName;
    }

    public void setUiFieldName(String uiFieldName) {
        this.uiFieldName = uiFieldName;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public boolean getIsCustomField() {
        return isCustomField;
    }

    public void setIsCustomField(boolean isCustomField) {
        this.isCustomField = isCustomField;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getDbBaseName() {
        return dbBaseName;
    }

    public void setDbBaseName(String dbBaseName) {
        this.dbBaseName = dbBaseName;
    }

    public String getDbBaseType() {
        return dbBaseType;
    }

    public void setDbBaseType(String dbBaseType) {
        this.dbBaseType = dbBaseType;
    }

    public long getFeature() {
        return feature;
    }

    public void setFeature(long feature) {
        this.feature = feature;
    }

    public Integer getAssociatedCurrency() {
        return associatedCurrency;
    }

    public void setAssociatedCurrency(Integer associatedCurrency) {
        this.associatedCurrency = associatedCurrency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
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

    public HkSectionEntity getSection() {
        return section;
    }

    public void setSection(HkSectionEntity section) {
        this.section = section;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Boolean getIsDependant() {
        return isDependant;
    }

    public void setIsDependant(Boolean isDependant) {
        this.isDependant = isDependant;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    public Long getSourceFieldId() {
        return sourceFieldId;
    }

    public void setSourceFieldId(Long sourceFieldId) {
        this.sourceFieldId = sourceFieldId;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public long getCreatedByFranchise() {
        return createdByFranchise;
    }

    public void setCreatedByFranchise(Long createdByFranchise) {
        this.createdByFranchise = createdByFranchise;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Set<HkSubFormFieldEntity> getHkSubFormFields() {
        return hkSubFormFields;
    }

    public void setHkSubFormFields(Set<HkSubFormFieldEntity> hkSubFormFields) {
        this.hkSubFormFields = hkSubFormFields;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkFieldEntity)) {
            return false;
        }
        HkFieldEntity other = (HkFieldEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)) || (this.id == null && other.id == null)) {
            return false;
        }
        return true;
    }

    @Override
    public HkFieldEntity clone() throws CloneNotSupportedException {
        HkFieldEntity hkFieldEntity = (HkFieldEntity) super.clone();
        hkFieldEntity.setHkSubFormFields(new HashSet<>(hkSubFormFields));
        return hkFieldEntity;
    }

    @Override
    public String toString() {
        return "HkFieldEntity{" + "id=" + id + ", fieldLabel=" + fieldLabel + ", fieldType=" + fieldType + ", componentType=" + componentType + ", uiFieldName=" + uiFieldName + ", dbFieldName=" + dbFieldName + ", isCustomField=" + isCustomField + ", isEditable=" + isEditable + ", isDependant=" + isDependant + ", dbBaseName=" + dbBaseName + ", dbBaseType=" + dbBaseType + ", feature=" + feature + ", associatedCurrency=" + associatedCurrency + ", status=" + status + ", isArchive=" + isArchive + ", franchise=" + franchise + ", seqNo=" + seqNo + ", createdBy=" + createdBy + ", createdByFranchise=" + createdByFranchise + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", section=" + section + ", validationPattern=" + validationPattern + ", formulaValue=" + formulaValue + ", fieldValues=" + fieldValues + ", hkSubFormFields=" + hkSubFormFields + '}';
    }

}
