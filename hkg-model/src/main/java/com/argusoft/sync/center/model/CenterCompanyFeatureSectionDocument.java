/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.Map;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author akta
 */
@Document(collection = "companyfeaturesection")
public class CenterCompanyFeatureSectionDocument {

    @Id
    private Integer id;
    private Date modifiedOn;
    private String customFieldVersion;
    private Map<String, Map<String, Object>> featureSectionMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer _id) {
        this.id = _id;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Map<String, Map<String, Object>> getFeatureSectionMap() {
        return featureSectionMap;
    }

    public void setFeatureSectionMap(Map<String, Map<String, Object>> featureSectionMap) {
        this.featureSectionMap = featureSectionMap;
    }

    public String getCustomFieldVersion() {
        return customFieldVersion;
    }

    public void setCustomFieldVersion(String customFieldVersion) {
        this.customFieldVersion = customFieldVersion;
    }

}
