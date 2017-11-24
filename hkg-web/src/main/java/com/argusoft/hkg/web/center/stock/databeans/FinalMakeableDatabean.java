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
public class FinalMakeableDatabean {

    private String id;
    private Map<String, Object> finalMakeableCustom;
    private Map<String, String> finalMakeableDbType;
    private Map<String, Object> ruleConfigMap;
    private String packetId;
    private List<String> dbFieldNames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getFinalMakeableCustom() {
        return finalMakeableCustom;
    }

    public void setFinalMakeableCustom(Map<String, Object> finalMakeableCustom) {
        this.finalMakeableCustom = finalMakeableCustom;
    }

    public Map<String, String> getFinalMakeableDbType() {
        return finalMakeableDbType;
    }

    public void setFinalMakeableDbType(Map<String, String> finalMakeableDbType) {
        this.finalMakeableDbType = finalMakeableDbType;
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
        return "FinalMakeableDatabean{" + "id=" + id + ", finalMakeableCustom=" + finalMakeableCustom + ", finalMakeableDbType=" + finalMakeableDbType + ", ruleConfigMap=" + ruleConfigMap + ", packetId=" + packetId + ", dbFieldNames=" + dbFieldNames + '}';
    }

}
