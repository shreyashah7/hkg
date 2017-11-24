/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "rb_report_field_dtl")
public class RbReportField implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "field_label", length = 500)
    private String fieldLabel;
    @Basic(optional = false)
    @Column(name = "db_field_name", nullable = false, length = 200)
    private String dbFieldName;
    @Basic(optional = false)
    @Column(name = "db_base_name", nullable = false, length = 200, columnDefinition = "character varying(200) default 'NA'")
    private String dbBaseName;
    @Basic(optional = false)
    @Column(name = "db_base_type", nullable = false, length = 200, columnDefinition = "character varying(200) default 'RDB'")
    private String dbBaseType;
    @Column(name = "order_type", length = 5)
    private String orderType;
    @Basic(optional = false)
    @Column(name = "col_i18n_req", nullable = false)
    private boolean colI18nReq;
    @Basic(optional = false)
    @Column(name = "row_i18n_req", nullable = false)
    private boolean rowI18nReq;
    @Basic(optional = false)
    @Column(name = "report_field_name", nullable = false, length = 500)
    private String reportFieldName;
    @Column(name = "field_data_type", nullable = false)
    private String fieldDataType;
    @Column(name = "filter_attrs", length = 500)
    private String filterAttributes;
    @Column(name = "join_attrs", length = 500)
    private String joinAttributes;
    @Column(name = "custom_attrs", length = 500)
    private String customAttributes;
    @Column(name = "table_name", length = 500)
    private String tableName;
    @Column(name = "field_seq", nullable = false, columnDefinition = "int default 0")
    private int fieldSequence;
    @Basic(optional = false)
    @Column(name = "is_default_visible", nullable = false)
    private boolean isDefaultVisible;
    @Column(name = "feature_id")
    private Long featureId;
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
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rbReportField", fetch = FetchType.EAGER)
//    private Set<RbFieldConverter> rbFieldConverterSet;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rbReportField", fetch = FetchType.EAGER)
//    private Set<RbFieldFormatter> rbFieldFormatterSet;
    @JoinColumn(name = "report", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private RbReport report;

    public RbReportField() {
    }

    public RbReportField(Long id) {
        this.id = id;
    }

    public RbReportField(Long id, String fieldLabel, String dbFieldName, String dbBaseName, String dbBaseType, boolean colI18nReq, boolean rowI18nReq, String reportFieldName, boolean isDefaultVisible, boolean isArchive, long lastModifiedBy, Date lastModifiedOn, String fieldDataType) {
        this.id = id;
        this.fieldLabel = fieldLabel;
        this.dbFieldName = dbFieldName;
        this.dbBaseName = dbBaseName;
        this.dbBaseType = dbBaseType;
        this.colI18nReq = colI18nReq;
        this.rowI18nReq = rowI18nReq;
        this.reportFieldName = reportFieldName;
        this.isDefaultVisible = isDefaultVisible;
        this.isArchive = isArchive;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.fieldDataType = fieldDataType;
    }

    public Long getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public boolean getColI18nReq() {
        return colI18nReq;
    }

    public void setColI18nReq(boolean colI18nReq) {
        this.colI18nReq = colI18nReq;
    }

    public boolean getRowI18nReq() {
        return rowI18nReq;
    }

    public void setRowI18nReq(boolean rowI18nReq) {
        this.rowI18nReq = rowI18nReq;
    }

    public String getReportFieldName() {
        return reportFieldName;
    }

    public void setReportFieldName(String reportFieldName) {
        this.reportFieldName = reportFieldName;
    }

    public boolean getIsDefaultVisible() {
        return isDefaultVisible;
    }

    public void setIsDefaultVisible(boolean isDefaultVisible) {
        this.isDefaultVisible = isDefaultVisible;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
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

//    public Set<RbFieldConverter> getRbFieldConverterSet() {
//        return rbFieldConverterSet;
//    }
//
//    public void setRbFieldConverterSet(Set<RbFieldConverter> rbFieldConverterSet) {
//        this.rbFieldConverterSet = rbFieldConverterSet;
//    }
//
//    public Set<RbFieldFormatter> getRbFieldFormatterSet() {
//        return rbFieldFormatterSet;
//    }
//
//    public void setRbFieldFormatterSet(Set<RbFieldFormatter> rbFieldFormatterSet) {
//        this.rbFieldFormatterSet = rbFieldFormatterSet;
//    }

    public RbReport getReport() {
        return report;
    }

    public void setReport(RbReport report) {
        this.report = report;
    }

    public String getFieldDataType() {
        return fieldDataType;
    }

    public void setFieldDataType(String fieldDataType) {
        this.fieldDataType = fieldDataType;
    }

    public int getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(int fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

    public String getFilterAttributes() {
        return filterAttributes;
    }

    public void setFilterAttributes(String filterAttributes) {
        this.filterAttributes = filterAttributes;
    }

    public String getJoinAttributes() {
        return joinAttributes;
    }

    public void setJoinAttributes(String joinAttributes) {
        this.joinAttributes = joinAttributes;
    }

    public String getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(String customAttributes) {
        this.customAttributes = customAttributes;
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
        if (!(object instanceof RbReportField)) {
            return false;
        }
        RbReportField other = (RbReportField) object;
        if ((this.id == null && other.id == null)
                || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RbReportField{" + "id=" + id + ", fieldLabel=" + fieldLabel + ", dbFieldName=" + dbFieldName + ", dbBaseName=" + dbBaseName + ", dbBaseType=" + dbBaseType + ", orderType=" + orderType + ", colI18nReq=" + colI18nReq + ", rowI18nReq=" + rowI18nReq + ", reportFieldName=" + reportFieldName + ", fieldDataType=" + fieldDataType + ", filterAttributes=" + filterAttributes + ", joinAttributes=" + joinAttributes + ", customAttributes=" + customAttributes + ", tableName=" + tableName + ", fieldSequence=" + fieldSequence + ", isDefaultVisible=" + isDefaultVisible + ", featureId=" + featureId + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", report=" + report + '}';
    }

    @Override
    public RbReportField clone() throws CloneNotSupportedException {
        return (RbReportField) super.clone();
    }
    
}
