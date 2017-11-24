/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author akta
 */
public class RoughMakeableDatabean {

    private String id;
    private Map<String, Object> roughMakeableCustom;
    private Map<String, String> roughMakeableDbType;
    private Map<String, Object> ruleConfigMap;
    private String packetId;
    private List<String> dbFieldNames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getRoughMakeableCustom() {
        return roughMakeableCustom;
    }

    public void setRoughMakeableCustom(Map<String, Object> roughMakeableCustom) {
        this.roughMakeableCustom = roughMakeableCustom;
    }

    public Map<String, String> getRoughMakeableDbType() {
        return roughMakeableDbType;
    }

    public void setRoughMakeableDbType(Map<String, String> roughMakeableDbType) {
        this.roughMakeableDbType = roughMakeableDbType;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public Map<String, Object> getRuleConfigMap() {
        return ruleConfigMap;
    }

    public void setRuleConfigMap(Map<String, Object> ruleConfigMap) {
        this.ruleConfigMap = ruleConfigMap;
    }

    public List<String> getDbFieldNames() {
        return dbFieldNames;
    }

    public void setDbFieldNames(List<String> dbFieldNames) {
        this.dbFieldNames = dbFieldNames;
    }

    @Override
    public String toString() {
        return "RoughMakeableDatabean{" + "id=" + id + ", roughMakeableCustom=" + roughMakeableCustom + ", roughMakeableDbType=" + roughMakeableDbType + ", ruleConfigMap=" + ruleConfigMap + ", packetId=" + packetId + ", dbFieldNames=" + dbFieldNames + '}';
    }

}
