/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.database.bean;

import java.util.List;

/**
 *
 * @author vipul
 */
public class MongoFieldDatabean {

    private String fieldName;
    private String queryType;
    private List<String> queryTypeValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<String> getQueryTypeValue() {
        return queryTypeValue;
    }

    public void setQueryTypeValue(List<String> queryTypeValue) {
        this.queryTypeValue = queryTypeValue;
    }

}
