/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import org.bson.BasicBSONObject;

/**
 *
 * @author dhwani
 */
public class CustomField {

    private Long id;
    private BasicBSONObject fieldValue;

    public CustomField() {
    }

    public CustomField(Long id, BasicBSONObject fieldValue) {
        this.id = id;
        this.fieldValue = fieldValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BasicBSONObject getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(BasicBSONObject fieldValue) {
        this.fieldValue = fieldValue;
    }

}
