package com.argusoft.hkg.nosql.model;

import com.argusoft.sync.center.model.HkFieldDocument;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author harshit
 */
@Document
public class HkSubFormFieldDocument implements Serializable {

    @Id
    private Long id;
    private String componentType;
    private String subFieldName;
    private String subFieldLabel;
    private String subFieldType;
    private Long franchise;
    private Boolean isArchive;
    private Long lastModifiedBy;
    private Date lastModifiedOn;
    private String status;
    private String validationPattern;
    private Boolean isDroplistField;
    private Integer sequenceNo;
    @DBRef
    private HkFieldDocument parentField;

    public HkSubFormFieldDocument() {
    }

    public HkSubFormFieldDocument(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getSubFieldName() {
        return subFieldName;
    }

    public void setSubFieldName(String subFieldName) {
        this.subFieldName = subFieldName;
    }

    public String getSubFieldLabel() {
        return subFieldLabel;
    }

    public void setSubFieldLabel(String subFieldLabel) {
        this.subFieldLabel = subFieldLabel;
    }

    public String getSubFieldType() {
        return subFieldType;
    }

    public void setSubFieldType(String subFieldType) {
        this.subFieldType = subFieldType;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(String validationPattern) {
        this.validationPattern = validationPattern;
    }

    public Boolean getIsDroplistField() {
        return isDroplistField;
    }

    public void setIsDroplistField(Boolean isDroplistField) {
        this.isDroplistField = isDroplistField;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public HkFieldDocument getParentField() {
        return parentField;
    }

    public void setParentField(HkFieldDocument parentField) {
        this.parentField = parentField;
    }

}
