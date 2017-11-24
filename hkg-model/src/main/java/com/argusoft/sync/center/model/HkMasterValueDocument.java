/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.Map;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Harshit
 */
@Document(collection = "mastervalue")
public class HkMasterValueDocument {

    @Id
    private Long id;
    private Integer shortcutCode;
    private String valueName;
    private String translatedValueName;
    private Boolean isActive;
    private Boolean isArchive;
    private Date lastModifiedOn;
    private String code;
    private boolean isOftenUsed;
    @Transient
    private Map<String, String> translateValueMap;
    private long franchise;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShortcutCode() {
        return shortcutCode;
    }

    public void setShortcutCode(Integer shortcutCode) {
        this.shortcutCode = shortcutCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getTranslatedValueName() {
        return translatedValueName;
    }

    public void setTranslatedValueName(String translatedValueName) {
        this.translatedValueName = translatedValueName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIsOftenUsed() {
        return isOftenUsed;
    }

    public void setIsOftenUsed(boolean isOftenUsed) {
        this.isOftenUsed = isOftenUsed;
    }

    public Map<String, String> getTranslateValueMap() {
        return translateValueMap;
    }

    public void setTranslateValueMap(Map<String, String> translateValueMap) {
        this.translateValueMap = translateValueMap;
    }

    @Override
    public String toString() {
        return "HkMasterValueDocument{" + "id=" + id + ", shortcutCode=" + shortcutCode + ", valueName=" + valueName + ", translatedValueName=" + translatedValueName + ", isActive=" + isActive + ", isArchive=" + isArchive + ", lastModifiedOn=" + lastModifiedOn + '}';
    }

    /**
     * @return the franchise
     */
    public long getFranchise() {
        return franchise;
    }

    /**
     * @param franchise the franchise to set
     */
    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

}
