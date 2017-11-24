/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import java.util.Map;

/**
 *
 * @author gautam
 */
public class RuleDetailsDataBean {

    private String colorCode;
    private String tooltipMessage;
    private String validationMessage;
    private String entityId;
    private String entityType;
    private String featureName;
    private Map<String, Object> currentFieldValueMap;
    private Map<String, String> otherEntitysIdMap;
    private Map<String, String> dbType;

    public RuleDetailsDataBean() {
    }

    public RuleDetailsDataBean(String colorCode, String tooltipMessage) {
        this.colorCode = colorCode;
        this.tooltipMessage = tooltipMessage;
    }

    public RuleDetailsDataBean(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getTooltipMessage() {
        return tooltipMessage;
    }

    public void setTooltipMessage(String tooltipMessage) {
        this.tooltipMessage = tooltipMessage;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Map<String, Object> getCurrentFieldValueMap() {
        return currentFieldValueMap;
    }

    public void setCurrentFieldValueMap(Map<String, Object> currentFieldValueMap) {
        this.currentFieldValueMap = currentFieldValueMap;
    }

    public Map<String, String> getOtherEntitysIdMap() {
        return otherEntitysIdMap;
    }

    public void setOtherEntitysIdMap(Map<String, String> otherEntitysIdMap) {
        this.otherEntitysIdMap = otherEntitysIdMap;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    @Override
    public String toString() {
        return "RuleDetailsDataBean{" + "colorCode=" + colorCode + ", tooltipMessage=" + tooltipMessage + ", validationMessage=" + validationMessage + ", entityId=" + entityId + ", entityType=" + entityType + ", featureName=" + featureName + ", currentFieldValueMap=" + currentFieldValueMap + ", otherEntitysIdMap=" + otherEntitysIdMap + ", dbType=" + dbType + '}';
    }

}
