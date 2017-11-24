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
public class QueryJoinDataBean {

    private String firstColumn;
    private String secondColumn;
    private String joinType;
    private boolean disabled;

    public QueryJoinDataBean() {
    }

    public QueryJoinDataBean(String firstColumn, String secondColumn, String joinType, boolean disabled) {
        this.firstColumn = firstColumn;
        this.secondColumn = secondColumn;
        this.joinType = joinType;
        this.disabled = disabled;
    }

    public String getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(String firstColumn) {
        this.firstColumn = firstColumn;
    }

    public String getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(String secondColumn) {
        this.secondColumn = secondColumn;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "QueryJoinDataBean{" + "firstColumn=" + firstColumn + ", secondColumn=" + secondColumn + ", joinType=" + joinType + ", disabled=" + disabled + '}';
    }

}
