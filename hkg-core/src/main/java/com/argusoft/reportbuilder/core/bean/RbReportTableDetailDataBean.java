/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.bean;

/**
 *
 * @author shreya
 */
public class RbReportTableDetailDataBean {
    
    private Long id;
    private String tableName;
    private Integer tableSeq;

    public RbReportTableDetailDataBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getTableSeq() {
        return tableSeq;
    }

    public void setTableSeq(Integer tableSeq) {
        this.tableSeq = tableSeq;
    }

    @Override
    public String toString() {
        return "RbReportTableDetailDataBean{" + "id=" + id + ", tableName=" + tableName + ", tableSeq=" + tableSeq + '}';
    }
    
    
}
