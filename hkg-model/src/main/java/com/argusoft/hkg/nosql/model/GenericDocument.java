/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.FeatureNameForCustomField;
import java.util.Date;
import java.util.List;
import org.bson.BasicBSONObject;

/**
 *
 * @author dhwani
 */
public class GenericDocument {

    protected FeatureNameForCustomField featureName;
    protected Long instanceId;
    protected BasicBSONObject fieldValue;
    protected List<SectionDocument> sectionList;
    protected Boolean isArchive;
    protected Long franchiseId;
    private Date modifiedOn;
    private Long createdByFranchise;

    public BasicBSONObject getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(BasicBSONObject fieldValue) {
        this.fieldValue = fieldValue;
    }
 
    public FeatureNameForCustomField getFeatureName() {
        return featureName;
    }

    public void setFeatureName(FeatureNameForCustomField featureName) {
        this.featureName = featureName;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public List<SectionDocument> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionDocument> sectionList) {
        this.sectionList = sectionList;
    }

    public Boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Long getCreatedByFranchise() {
        return createdByFranchise;
    }

    public void setCreatedByFranchise(Long createdByFranchise) {
        this.createdByFranchise = createdByFranchise;
    }

    /**
     * This method is added to stop compile time errors when writing generic
     * code for GenericDocument As we are not using GenericDocument directly as
     * a document anywhere and always extending it so this method will call
     * getId() method of subclass instance always
     *
     * @return
     */
    public Object getId() {
        return null;
    }

}
