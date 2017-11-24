/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

/**
 *
 * @author mansi
 */
public class HkRuleCriteriaDocument {

    private int id;
    private int priority;
    private Long entity;
    private Long field;
    private String fieldType;
    private String dbFieldType;
    private String entityName;
    private String dbFieldName;
    private String operator;
    private Object value;
    private Object value1;
    private boolean isString = false;
    // For only customfield of type subentity
    private String subentityComponentType;
    // For only customfield of type pointer
    private String pointerId;
    private String pointerComponentType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getEntity() {
        return entity;
    }

    public void setEntity(Long entity) {
        this.entity = entity;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Long getField() {
        return field;
    }

    public void setField(Long field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue1() {
        return value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public boolean isIsString() {
        return isString;
    }

    public void setIsString(boolean isString) {
        this.isString = isString;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getPointerId() {
        return pointerId;
    }

    public void setPointerId(String pointerId) {
        this.pointerId = pointerId;
    }

    public String getPointerComponentType() {
        return pointerComponentType;
    }

    public void setPointerComponentType(String pointerComponentType) {
        this.pointerComponentType = pointerComponentType;
    }

    public String getSubentityComponentType() {
        return subentityComponentType;
    }

    public void setSubentityComponentType(String subentityComponentType) {
        this.subentityComponentType = subentityComponentType;
    }

    public String getDbFieldType() {
        return dbFieldType;
    }

    public void setDbFieldType(String dbFieldType) {
        this.dbFieldType = dbFieldType;
    }
    

}
