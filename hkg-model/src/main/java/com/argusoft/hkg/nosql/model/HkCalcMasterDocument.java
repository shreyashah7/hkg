/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "calcmaster")
public class HkCalcMasterDocument implements Comparable<HkCalcMasterDocument> {
//Set id to code-value
    @Id
    private String id;
    private int sequence;
    private Object value;
    private String code;
    private Long masterValueId;
    private boolean isActive;
    private boolean isArchive;
    private Date createdOn;
    private Date modifiedOn;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isArchive
     */
    public boolean isIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param ModifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date ModifiedOn) {
        this.modifiedOn = ModifiedOn;
    }

    /**
     * @return the masterValueId
     */
    public Long getMasterValueId() {
        return masterValueId;
    }

    /**
     * @param masterValueId the masterValueId to set
     */
    public void setMasterValueId(Long masterValueId) {
        this.masterValueId = masterValueId;
    }

    @Override
    public String toString() {
        return "CalcMasterDocument{" + "id=" + id + ", sequence=" + sequence + ", value=" + value + ", code=" + code + ", masterValueId=" + masterValueId + ", isActive=" + isActive + ", isArchive=" + isArchive + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + '}';
    }

    @Override
    public int compareTo(HkCalcMasterDocument calcMasterDocument) {
        if (calcMasterDocument != null) {
            return Integer.compare(sequence, calcMasterDocument.sequence);
        }
        return 0;
    }

}
