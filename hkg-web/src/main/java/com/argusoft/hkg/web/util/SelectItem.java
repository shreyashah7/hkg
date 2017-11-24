/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import com.argusoft.hkg.web.center.stock.databeans.DynamicServiceInitDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author piyush
 */
public class SelectItem {

    private String label;
    private Object value;
    private String description;
    private String id;
    private Long otherId;
    private boolean isActive = true;
    private Integer shortcutCode;
    private Map<String, Object> categoryCustom;
    private Map<String, String> dbType;
    private Map<Object, Object> custom1;
    private Map<Object, Object> custom3;
    private Map<Object, Object> custom4;
    private Map<Object, Object> custom5;
    private Map<Object, Object> fieldNameIdMap;
    private List<SelectItem> custom2;
    private List<Map<String, Object>> custom6;
    private String status;
    private String lotId;
    private String packetId;
    private String parcelId;
    private String sellId;
    private String transferId;
    private Object lotSlipId;
    private Object packetSlipId;
    private String commonId;
    private Date dateObj;
    private Boolean haveValue;
    private Long custom7;
    private DynamicServiceInitDataBean serviceInitDataBean;
    private List<Long> dependantOnException;
    private List<Long> forValuesException;
    private List<String> forUsers;
    private List<String> otherValues;
    private String beforeLabel;
    private String afterLabel;
    private Integer year;
    //Rules related variables. Field id with details.
    private Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName;
    private RuleDetailsDataBean preRuleDetails;

    public Boolean isHaveValue() {
        return haveValue;
    }

    public void setHaveValue(Boolean haveValue) {
        this.haveValue = haveValue;
    }

    public DynamicServiceInitDataBean getServiceInitDataBean() {
        return serviceInitDataBean;
    }

    public void setServiceInitDataBean(DynamicServiceInitDataBean serviceInitDataBean) {
        this.serviceInitDataBean = serviceInitDataBean;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public List<String> getOtherValues() {
        return otherValues;
    }

    public void setOtherValues(List<String> otherValues) {
        this.otherValues = otherValues;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getCommonId() {
        return commonId;
    }

    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    public List<Map<String, Object>> getCustom6() {
        return custom6;
    }

    public void setCustom6(List<Map<String, Object>> custom6) {
        this.custom6 = custom6;
    }

    public SelectItem() {
    }

    public SelectItem(Object value, String label) {
        super();
        this.label = label;
        this.value = value;
    }

    public SelectItem(Object value, String label, Integer shortcutCode) {
        super();
        this.label = label;
        this.value = value;
        this.shortcutCode = shortcutCode;
    }

    public SelectItem(Object value, String label, String desription) {
        super();
        this.label = label;
        this.value = value;
        this.description = desription;
    }

    public SelectItem(Object value, String label, String desription, String id) {
        super();
        this.label = label;
        this.value = value;
        this.description = desription;
        this.id = id;
    }

    public SelectItem(Object value, String label, String desription, boolean isActive) {
        super();
        this.label = label;
        this.value = value;
        this.description = desription;
        this.isActive = isActive;
    }

    public SelectItem(String description, Long otherId, Object value, String label) {
        super();
        this.label = label;
        this.value = value;
        this.description = description;
        this.otherId = otherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Object, Object> getCustom1() {
        return custom1;
    }

    public void setCustom1(Map<Object, Object> custom1) {
        this.custom1 = custom1;
    }

    public Long getOtherId() {
        return otherId;
    }

    public void setOtherId(Long otherId) {
        this.otherId = otherId;
    }

    public Integer getShortcutCode() {
        return shortcutCode;
    }

    public void setShortcutCode(Integer shortcutCode) {
        this.shortcutCode = shortcutCode;
    }

    public Object getLotSlipId() {
        return lotSlipId;
    }

    public void setLotSlipId(Object lotSlipId) {
        this.lotSlipId = lotSlipId;
    }

    public Object getPacketSlipId() {
        return packetSlipId;
    }

    public void setPacketSlipId(Object packetSlipId) {
        this.packetSlipId = packetSlipId;
    }

    public Map<String, Object> getCategoryCustom() {
        return categoryCustom;
    }

    public void setCategoryCustom(Map<String, Object> categoryCustom) {
        this.categoryCustom = categoryCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public Date getDateObj() {
        return dateObj;
    }

    public void setDateObj(Date dateObj) {
        this.dateObj = dateObj;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<SelectItem> getCustom2() {
        return custom2;
    }

    public void setCustom2(List<SelectItem> custom2) {
        this.custom2 = custom2;
    }

    public Map<Object, Object> getCustom3() {
        return custom3;
    }

    public void setCustom3(Map<Object, Object> custom3) {
        this.custom3 = custom3;
    }

    public Map<Object, Object> getCustom4() {
        return custom4;
    }

    public void setCustom4(Map<Object, Object> custom4) {
        this.custom4 = custom4;
    }

    public Map<Object, Object> getCustom5() {
        return custom5;
    }

    public void setCustom5(Map<Object, Object> custom5) {
        this.custom5 = custom5;
    }

    public Map<Object, Object> getFieldNameIdMap() {
        return fieldNameIdMap;
    }

    public void setFieldNameIdMap(Map<Object, Object> fieldNameIdMap) {
        this.fieldNameIdMap = fieldNameIdMap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public List<Long> getDependantOnException() {
        return dependantOnException;
    }

    public void setDependantOnException(List<Long> dependantOnException) {
        this.dependantOnException = dependantOnException;
    }

    public List<Long> getForValuesException() {
        return forValuesException;
    }

    public void setForValuesException(List<Long> forValuesException) {
        this.forValuesException = forValuesException;
    }

    public List<String> getForUsers() {
        return forUsers;
    }

    public void setForUsers(List<String> forUsers) {
        this.forUsers = forUsers;
    }

    public Long getCustom7() {
        return custom7;
    }

    public void setCustom7(Long custom7) {
        this.custom7 = custom7;
    }

    public String getBeforeLabel() {
        return beforeLabel;
    }

    public void setBeforeLabel(String beforeLabel) {
        this.beforeLabel = beforeLabel;
    }

    public String getAfterLabel() {
        return afterLabel;
    }

    public void setAfterLabel(String afterLabel) {
        this.afterLabel = afterLabel;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Map<String, RuleDetailsDataBean> getScreenRuleDetailsWithDbFieldName() {
        return screenRuleDetailsWithDbFieldName;
    }

    public void setScreenRuleDetailsWithDbFieldName(Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName) {
        this.screenRuleDetailsWithDbFieldName = screenRuleDetailsWithDbFieldName;
    }

    public RuleDetailsDataBean getPreRuleDetails() {
        return preRuleDetails;
    }

    public void setPreRuleDetails(RuleDetailsDataBean preRuleDetails) {
        this.preRuleDetails = preRuleDetails;
    }

    @Override
    public String toString() {
        return "SelectItem{" + "label=" + label + ", value=" + value + ", description=" + description + ", id=" + id + ", otherId=" + otherId + ", isActive=" + isActive + ", shortcutCode=" + shortcutCode + ", categoryCustom=" + categoryCustom + ", dbType=" + dbType + ", custom1=" + custom1 + ", custom3=" + custom3 + ", custom4=" + custom4 + ", custom5=" + custom5 + ", fieldNameIdMap=" + fieldNameIdMap + ", custom2=" + custom2 + ", custom6=" + custom6 + ", status=" + status + ", lotId=" + lotId + ", packetId=" + packetId + ", parcelId=" + parcelId + ", sellId=" + sellId + ", transferId=" + transferId + ", lotSlipId=" + lotSlipId + ", packetSlipId=" + packetSlipId + ", commonId=" + commonId + ", dateObj=" + dateObj + ", haveValue=" + haveValue + ", custom7=" + custom7 + ", serviceInitDataBean=" + serviceInitDataBean + ", dependantOnException=" + dependantOnException + ", forValuesException=" + forValuesException + ", forUsers=" + forUsers + ", otherValues=" + otherValues + ", beforeLabel=" + beforeLabel + ", afterLabel=" + afterLabel + ", year=" + year + ", screenRuleDetailsWithDbFieldName=" + screenRuleDetailsWithDbFieldName + ", preRuleDetails=" + preRuleDetails + '}';
    }
    
}
