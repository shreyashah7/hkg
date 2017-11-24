/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
public class DynamicField {

    private List<Pair> list;
    private String entityName;
    private Long id;
    private Map<String, String> map;

    public DynamicField() {
    }

    public List<Pair> getList() {
        return list;
    }

    public void setList(List<Pair> list) {
        this.list = list;

    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void convert() {
        map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Pair pair : list) {
                map.put(Long.toString(pair.getId()), pair.getType());
//            System.out.println(Long.toString(pair.getId()) + "     " + pair.getType());
            }
        }
        list = null;
    }

    public static class Pair {

        private long id;
        private String label;
        private String oldLabelName;
        private String type;
        private List<Object> values;
        private Long currencyMasterId;
        private String validationPattern;
        private Boolean isNewField;
        private Long seqNo;

        public Pair() {
        }

        public Pair(long id, String label, String oldLabelName, String type, List<Object> values, Long currencyMasterId, String validationPattern, Boolean isNewField, Long seqNo) {
            this.id = id;
            this.label = label;
            this.oldLabelName = oldLabelName;
            this.type = type;
            this.values = values;
            this.currencyMasterId = currencyMasterId;
            this.validationPattern = validationPattern;
            this.isNewField = isNewField;
            this.seqNo = seqNo;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getOldLabelName() {
            return oldLabelName;
        }

        public void setOldLabelName(String oldLabelName) {
            this.oldLabelName = oldLabelName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Object> getValues() {
            return values;
        }

        public void setValues(List<Object> values) {
            this.values = values;
        }

        public Long getCurrencyMasterId() {
            return currencyMasterId;
        }

        public void setCurrencyMasterId(Long currencyMasterId) {
            this.currencyMasterId = currencyMasterId;
        }

        public String getValidationPattern() {
            return validationPattern;
        }

        public void setValidationPattern(String validationPattern) {
            this.validationPattern = validationPattern;
        }

        public Boolean isIsNewField() {
            return isNewField;
        }

        public void setIsNewField(Boolean isNewField) {
            this.isNewField = isNewField;
        }

        public Long getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(Long seqNo) {
            this.seqNo = seqNo;
        }

        @Override
        public String toString() {
            return "Pair{" + "id=" + id + ", label=" + label + ", oldLabelName=" + oldLabelName + ", type=" + type + ", values=" + values + ", currencyMasterId=" + currencyMasterId + ", validationPattern=" + validationPattern + ", isNewField=" + isNewField + ", seqNo=" + seqNo + '}';
        }

    }

    @Override
    public String toString() {
        return "DynamicField{" + "list=" + list + ", entityName=" + entityName + ", id=" + id + ", map=" + map + '}';
    }

}
