    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author sidhdharth
 */
public class MasterDataBean {

    private String id;
    private String code;
    private String masterName;
    private String usedInFeature;
    private String password;
    private String masterType;
    private boolean isSensitiveMaster;
    private Integer shortcutCode;
    private String value;
    private String codeValue;
    private boolean isOftenUsed;
    private boolean isArchieve;
    private Long valueEntityId;
    private List<MasterDataBean> masterDataBeans;
    private Map<String, String> langaugeIdNameMap;

    public Map<String, String> getLangaugeIdNameMap() {
        return langaugeIdNameMap;
    }

    public void setLangaugeIdNameMap(Map<String, String> langaugeIdNameMap) {
        this.langaugeIdNameMap = langaugeIdNameMap;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getUsedInFeature() {
        return usedInFeature;
    }

    public void setUsedInFeature(String usedInFeature) {
        this.usedInFeature = usedInFeature;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsSensitiveMaster() {
        return isSensitiveMaster;
    }

    public void setIsSensitiveMaster(boolean isSensitiveMaster) {
        this.isSensitiveMaster = isSensitiveMaster;
    }

    public Integer getShortcutCode() {
        return shortcutCode;
    }

    public void setShortcutCode(Integer shortcutCode) {
        this.shortcutCode = shortcutCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIsOftenUsed() {
        return isOftenUsed;
    }

    public void setIsOftenUsed(boolean isOftenUsed) {
        this.isOftenUsed = isOftenUsed;
    }

    public boolean isIsArchieve() {
        return isArchieve;
    }

    public void setIsArchieve(boolean isArchieve) {
        this.isArchieve = isArchieve;
    }

    public List<MasterDataBean> getMasterDataBeans() {
        return masterDataBeans;
    }

    public void setMasterDataBeans(List<MasterDataBean> masterDataBeans) {
        this.masterDataBeans = masterDataBeans;
    }

    public Long getValueEntityId() {
        return valueEntityId;
    }

    public void setValueEntityId(Long valueEntityId) {
        this.valueEntityId = valueEntityId;
    }

    public String getMasterType() {
        return masterType;
    }

    public void setMasterType(String masterType) {
        this.masterType = masterType;
    }

    @Override
    public String toString() {
        return "MasterDataBean{" + "id=" + id + ", code=" + code + ", masterName=" + masterName + ", usedInFeature=" + usedInFeature + ", password=" + password + ", masterType=" + masterType + ", isSensitiveMaster=" + isSensitiveMaster + ", shortcutCode=" + shortcutCode + ", value=" + value + ", isOftenUsed=" + isOftenUsed + ", isArchieve=" + isArchieve + ", valueEntityId=" + valueEntityId + ", masterDataBeans=" + masterDataBeans + ", langaugeIdNameMap=" + langaugeIdNameMap + '}';
    }

}
