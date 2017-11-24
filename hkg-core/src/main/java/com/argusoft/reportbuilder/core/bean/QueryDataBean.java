/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.bean;

import java.util.List;

/**
 *
 * @author gautam
 */
public class QueryDataBean {

    private String selectedTables;
    private String selectedColumns;
    private List<QueryOrderByDataBean> orderMap;
    private List<QueryJoinDataBean> joinList;

    public QueryDataBean() {
    }

    public QueryDataBean(String selectedTables, String selectedColumns, List<QueryOrderByDataBean> orderMap, List<QueryJoinDataBean> joinList) {
        this.selectedTables = selectedTables;
        this.selectedColumns = selectedColumns;
        this.orderMap = orderMap;
        this.joinList = joinList;
    }

    public String getSelectedTables() {
        return selectedTables;
    }

    public void setSelectedTables(String selectedTables) {
        this.selectedTables = selectedTables;
    }

    public String getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(String selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public List<QueryOrderByDataBean> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(List<QueryOrderByDataBean> orderMap) {
        this.orderMap = orderMap;
    }

    public List<QueryJoinDataBean> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<QueryJoinDataBean> joinList) {
        this.joinList = joinList;
    }

    @Override
    public String toString() {
        return "QueryDataBean{" + "selectedTables=" + selectedTables + ", selectedColumns=" + selectedColumns + ", orderMap=" + orderMap + ", joinList=" + joinList + '}';
    }

}
