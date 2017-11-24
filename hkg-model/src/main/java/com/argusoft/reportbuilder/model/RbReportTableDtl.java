/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.model;

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
 * @author shreya
 */
@Entity
@Table(name = "rb_report_table_dtl")
public class RbReportTableDtl {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @JoinColumn(name = "report", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private RbReport report;
    @Column(name = "table_name", length = 500)
    private String tableName;
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
    @Column(name = "table_seq", nullable = false, columnDefinition = "int default 0")
    private int tableSequence;

    public RbReportTableDtl() {
    }

    public RbReportTableDtl(Long id) {
        this.id = id;
    }

    public RbReportTableDtl(Long id, RbReport report, String tableName, boolean isArchive, long lastModifiedBy, Date lastModifiedOn, int tableSequence) {
        this.id = id;
        this.report = report;
        this.tableName = tableName;
        this.isArchive = isArchive;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.tableSequence = tableSequence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RbReport getReport() {
        return report;
    }

    public void setReport(RbReport report) {
        this.report = report;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isIsArchive() {
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

    public int getTableSequence() {
        return tableSequence;
    }

    public void setTableSequence(int tableSequence) {
        this.tableSequence = tableSequence;
    }

    @Override
    public String toString() {
        return "RbReportTableDtl{" + "id=" + id + ", report=" + report + ", tableName=" + tableName + ", isArchive=" + isArchive + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedOn=" + lastModifiedOn + ", tableSequence=" + tableSequence + '}';
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
        if (!(object instanceof RbReportTableDtl)) {
            return false;
        }
        RbReportTableDtl other = (RbReportTableDtl) object;
        if ((this.id == null && other.id == null)
                || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
