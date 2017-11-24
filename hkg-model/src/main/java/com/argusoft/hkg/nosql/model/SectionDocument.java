/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.SectionNameForCustomField;
import java.util.List;

/**
 *
 * @author dhwani
 */
public class SectionDocument {

    private SectionNameForCustomField sectionName;
    private List<CustomField> customFields;

//    private boolean isArray;
//    private List<CustomRecord> customRecords;
//    private Boolean isArchive;
    public SectionNameForCustomField getSectionName() {
        return sectionName;
    }

    public void setSectionName(SectionNameForCustomField sectionName) {
        this.sectionName = sectionName;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }
}
