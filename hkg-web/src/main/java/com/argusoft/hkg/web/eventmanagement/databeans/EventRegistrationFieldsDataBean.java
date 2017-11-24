/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.eventmanagement.databeans;

/**
 *
 * @author kuldeep
 */
public class EventRegistrationFieldsDataBean {

    private Long id;
    private String fieldName;
    private String componentType;
    private String defaultValues;
    private String value;
    private String validationPattern;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(String defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    @Override
    public String toString() {
        return "EventRegistrationFieldsDataBean{" + "id=" + id + ", fieldName=" + fieldName + ", componentType=" + componentType + ", defaultValues=" + defaultValues + ", value=" + value + ", validationPattern=" + validationPattern + '}';
    }

}
