/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.bean;

import java.util.List;

/**
 *
 * @author vipul
 */
public class MasterReportDataBean {

    private Long id;
    private String query;
    private List<String> orderList;
    private List<RbFieldDataBean> columns;
    private String description;
    private String reportName;
    private Boolean externalReport;
    private Boolean editable;
    private Boolean isDashboard;
    private String reportCode;
    private String status;
    private Integer count;
    private Integer page;
    private String convertedQuery;
    private String joinAttributes;
    private List<Long> franchiseIds;
    private Long featureId;
    private Long reportGroup;
    private String groupAttributes;
    private String orderAttributes;
    private String colorAttributes;
    private String displayName;
    private List<RbReportTableDetailDataBean> tableDtls;
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOrderAttributes() {
        return orderAttributes;
    }

    public void setOrderAttributes(String orderAttributes) {
        this.orderAttributes = orderAttributes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public Long getReportGroup() {
        return reportGroup;
    }

    public void setReportGroup(Long reportGroup) {
        this.reportGroup = reportGroup;
    }

    public List<RbFieldDataBean> getColumns() {
        return columns;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getConvertedQuery() {
        return convertedQuery;
    }

    public void setConvertedQuery(String convertedQuery) {
        this.convertedQuery = convertedQuery;
    }

    public void setColumns(List<RbFieldDataBean> columns) {
        this.columns = columns;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Boolean isExternalReport() {
        return externalReport;
    }

    public void setExternalReport(Boolean externalReport) {
        this.externalReport = externalReport;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoinAttributes() {
        return joinAttributes;
    }

    public void setJoinAttributes(String joinAttributes) {
        this.joinAttributes = joinAttributes;
    }

    public List<Long> getFranchiseIds() {
        return franchiseIds;
    }

    public void setFranchiseIds(List<Long> franchiseIds) {
        this.franchiseIds = franchiseIds;
    }

    public String getGroupAttributes() {
        return groupAttributes;
    }

    public void setGroupAttributes(String groupAttributes) {
        this.groupAttributes = groupAttributes;
    }

    public String getColorAttributes() {
        return colorAttributes;
    }

    public void setColorAttributes(String colorAttributes) {
        this.colorAttributes = colorAttributes;
    }

    public Boolean isIsDashboard() {
        return isDashboard;
    }

    public void setIsDashboard(Boolean isDashboard) {
        this.isDashboard = isDashboard;
    }

    public List<RbReportTableDetailDataBean> getTableDtls() {
        return tableDtls;
    }

    public void setTableDtls(List<RbReportTableDetailDataBean> tableDtls) {
        this.tableDtls = tableDtls;
    }

    @Override
    public String toString() {
        return "MasterReportDataBean{" + "id=" + id + ", query=" + query + ", orderList=" + orderList + ", columns=" + columns + ", description=" + description + ", reportName=" + reportName + ", externalReport=" + externalReport + ", editable=" + editable + ", isDashboard=" + isDashboard + ", reportCode=" + reportCode + ", status=" + status + ", count=" + count + ", page=" + page + ", convertedQuery=" + convertedQuery + ", joinAttributes=" + joinAttributes + ", franchiseIds=" + franchiseIds + ", featureId=" + featureId + ", reportGroup=" + reportGroup + ", groupAttributes=" + groupAttributes + ", orderAttributes=" + orderAttributes + ", colorAttributes=" + colorAttributes + ", displayName=" + displayName + ", tableDtls=" + tableDtls + '}';
    }

}
