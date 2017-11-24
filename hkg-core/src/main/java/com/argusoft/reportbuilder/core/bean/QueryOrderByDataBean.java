/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.core.bean;

/**
 *
 * @author gautam
 */
public class QueryOrderByDataBean {

    private String column;
    private String orderValue;

    public QueryOrderByDataBean() {
    }

    public QueryOrderByDataBean(String column, String orderValue) {
        this.column = column;
        this.orderValue = orderValue;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    @Override
    public String toString() {
        return "QueryOrderByDataBean{" + "column=" + column + ", orderValue=" + orderValue + '}';
    }

}
