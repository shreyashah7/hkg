/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "rb_report_mst")
public class RbReport implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "report_name", nullable = false, length = 50)
    private String reportName;
    @Column(length = 500)
    private String description;
    @Basic(optional = false)
    @Column(name = "is_external_report", nullable = false)
    private boolean isExternalReport;
    @Column(name = "report_code", length = 100)
    private String reportCode;
    @Column(name = "is_editable")
    private Boolean isEditable;
    @Column(name = "report_query", length = 4000)
    private String reportQuery;
    @Column(name = "join_attrs", length = 1000)
    private String joinAttributes;
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
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    private String status;
    @Column(name = "linked_with")
    private BigInteger linkedWith;
    private Long custom1;
    @Column(length = 1000)
    private String custom2;
    @Column(name = "group_json", length = 1000)
    private String groupJson;
    @Column(name = "order_json", length = 1000)
    private String orderJson;
    @Column(name = "color_json", length = 1500)
    private String colorJson;
    @Column(name = "report_group")
    private Long reportGroup;
    @Column(name = "company")
    private Long company;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<RbReportField> rbReportFieldList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<RbReportTableDtl> rbReportTableDtls;

    public RbReport() {
    }

    public RbReport(Long id) {
        this.id = id;
    }

    public RbReport(Long id, String reportName, boolean isExternalReport, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn, boolean isArchive, boolean isActive, String status) {
        this.id = id;
        this.reportName = reportName;
        this.isExternalReport = isExternalReport;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.isArchive = isArchive;
        this.isActive = isActive;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsExternalReport() {
        return isExternalReport;
    }

    public void setIsExternalReport(boolean isExternalReport) {
        this.isExternalReport = isExternalReport;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getReportQuery() {
        return reportQuery;
    }

    public void setReportQuery(String reportQuery) {
        this.reportQuery = reportQuery;
    }

    public String getJoinAttributes() {
        return joinAttributes;
    }

    public void setJoinAttributes(String joinAttributes) {
        this.joinAttributes = joinAttributes;
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

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getLinkedWith() {
        return linkedWith;
    }

    public void setLinkedWith(BigInteger linkedWith) {
        this.linkedWith = linkedWith;
    }

    public Long getCustom1() {
        return custom1;
    }

    public void setCustom1(Long custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getGroupJson() {
        return groupJson;
    }

    public void setGroupJson(String groupJson) {
        this.groupJson = groupJson;
    }

    public String getOrderJson() {
        return orderJson;
    }

    public void setOrderJson(String orderJson) {
        this.orderJson = orderJson;
    }

    public String getColorJson() {
        return colorJson;
    }

    public void setColorJson(String colorJson) {
        this.colorJson = colorJson;
    }

    public Long getReportGroup() {
        return reportGroup;
    }

    public void setReportGroup(Long reportGroup) {
        this.reportGroup = reportGroup;
    }
    

//    public List<RbReportAccess> getRbReportAccessSet() {
//        return rbReportAccessList;
//    }
//
//    public void setRbReportAccessSet(List<RbReportAccess> rbReportAccessSet) {
//        this.rbReportAccessList = rbReportAccessSet;
//    }
    public List<RbReportField> getRbReportFieldSet() {
        return rbReportFieldList;
    }

    public void setRbReportFieldSet(List<RbReportField> rbReportFieldSet) {
        this.rbReportFieldList = rbReportFieldSet;
    }

    public List<RbReportTableDtl> getRbReportTableDtls() {
        return rbReportTableDtls;
    }

    public void setRbReportTableDtls(List<RbReportTableDtl> rbReportTableDtls) {
        this.rbReportTableDtls = rbReportTableDtls;
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
        if (!(object instanceof RbReport)) {
            return false;
        }
        RbReport other = (RbReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.reportbuilder.common.model.RbReport[ id=" + id + " ]";
    }

    @Override
    public RbReport clone() throws CloneNotSupportedException {
        RbReport report=(RbReport)super.clone();
        
        report.setRbReportFieldSet(new ArrayList<>(rbReportFieldList));
        
        return report;
    }

    
}
