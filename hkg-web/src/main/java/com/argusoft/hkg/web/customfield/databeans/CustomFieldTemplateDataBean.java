/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import java.util.Map;

/**
 *
 * @author harshit
 */
public class CustomFieldTemplateDataBean {

    private String type;
    private String model;
    private String label;
    private String placeholder;//placeholder
    private String validate;//ng-pattern
    private Integer maxLength;//ng-maxlength
    private Integer minLength;//ng-minlength
    private Boolean readonly;//ng-readonly
    private Boolean required;//ng-required
    private Object val;//defaultvalue
    private Boolean disabled;//ng-disabled
    private String callback;//ng-change
    private Map<Object, String> attributes;
    private Map<Object, String> values;//list

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean isRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Map<Object, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Object, String> attributes) {
        this.attributes = attributes;
    }

    public Map<Object, String> getValues() {
        return values;
    }

    public void setValues(Map<Object, String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "CustomFieldTemplateDataBean{" + "type=" + type + ", model=" + model + ", label=" + label + ", placeholder=" + placeholder + ", validate=" + validate + ", maxLength=" + maxLength + ", minLength=" + minLength + ", readonly=" + readonly + ", required=" + required + ", val=" + val + ", disabled=" + disabled + ", callback=" + callback + ", attributes=" + attributes + ", values=" + values + '}';
    }

}
