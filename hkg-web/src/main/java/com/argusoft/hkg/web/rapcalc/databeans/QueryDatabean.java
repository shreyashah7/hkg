/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.databeans;

/**
 *
 * @author shruti
 */
public class QueryDatabean {
    private String queryName;
    private String query;
    private String className;
    private String countQuery;

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the queryName
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * @param queryName the queryName to set
     */
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the countQuery
     */
    public String getCountQuery() {
        return countQuery;
    }

    /**
     * @param countQuery the countQuery to set
     */
    public void setCountQuery(String countQuery) {
        this.countQuery = countQuery;
    }
}
