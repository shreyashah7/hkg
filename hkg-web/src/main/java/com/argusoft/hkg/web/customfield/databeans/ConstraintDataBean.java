/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.databeans;

import org.bson.types.ObjectId;

/**
 *
 * @author shifa
 */
public class ConstraintDataBean {
    private Long fieldId;
    private String objectId;
    private String fieldLabel;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    @Override
    public String toString() {
        return "ConstraintDataBean{" + "fieldId=" + fieldId + ", objectId=" + objectId + ", fieldLabel=" + fieldLabel + '}';
    }

   
    
    
}
