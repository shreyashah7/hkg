/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author harshit
 */
@Document(collection = "goaltemplate")
public class HkGoalTemplateDocument {

    @Id
    private Long id;
    private String templateName;
    private String description;
    private Integer period;
    private Long copyOfTemplate;
    private String templateType;
    private String realizationRule;
    private String generalValidation;
    private String successValue;
    private String failureValue;
    private Long forDesignation;
    private Long forDepartment;
    private Long forService;
    private String status;
    private Date createdOn;
    private Date lastModifiedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getCopyOfTemplate() {
        return copyOfTemplate;
    }

    public void setCopyOfTemplate(Long copyOfTemplate) {
        this.copyOfTemplate = copyOfTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getRealizationRule() {
        return realizationRule;
    }

    public void setRealizationRule(String realizationRule) {
        this.realizationRule = realizationRule;
    }

    public String getGeneralValidation() {
        return generalValidation;
    }

    public void setGeneralValidation(String generalValidation) {
        this.generalValidation = generalValidation;
    }

    public String getSuccessValue() {
        return successValue;
    }

    public void setSuccessValue(String successValue) {
        this.successValue = successValue;
    }

    public String getFailureValue() {
        return failureValue;
    }

    public void setFailureValue(String failureValue) {
        this.failureValue = failureValue;
    }

    public Long getForDesignation() {
        return forDesignation;
    }

    public void setForDesignation(Long forDesignation) {
        this.forDesignation = forDesignation;
    }

    public Long getForDepartment() {
        return forDepartment;
    }

    public void setForDepartment(Long forDepartment) {
        this.forDepartment = forDepartment;
    }

    public Long getForService() {
        return forService;
    }

    public void setForService(Long forService) {
        this.forService = forService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    @Override
    public String toString() {
        return "HkGoalTemplateDocument{" + "id=" + id + ", templateName=" + templateName + ", description=" + description + ", period=" + period + ", copyOfTemplate=" + copyOfTemplate + ", templateType=" + templateType + ", realizationRule=" + realizationRule + ", generalValidation=" + generalValidation + ", successValue=" + successValue + ", failureValue=" + failureValue + ", forDesignation=" + forDesignation + ", forDepartment=" + forDepartment + ", forService=" + forService + ", status=" + status + ", createdOn=" + createdOn + ", lastModifiedOn=" + lastModifiedOn + '}';
    }

}
