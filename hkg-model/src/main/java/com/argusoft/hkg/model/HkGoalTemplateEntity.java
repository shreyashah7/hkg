/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mmodi
 */
@Entity
@Table(name = "hk_system_goal_template_info")
@NamedQueries({
    @NamedQuery(name = "HkGoalTemplateEntity.findAll", query = "SELECT h FROM HkGoalTemplateEntity h")})
public class HkGoalTemplateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;
    @Column(length = 1000)
    private String description;
    @Basic(optional = false)
    @Column(nullable = false)
    private int period;
    @Column(name = "copy_of_template")
    private Long copyOfTemplate;
    @Basic(optional = false)
    @Column(name = "template_type", nullable = false, length = 5)
    private String templateType;
    @Column(name = "realization_rule", length = 50)
    private String realizationRule;
    @Column(name = "general_validation", length = 500)
    private String generalValidation;
    @Column(name = "success_value", length = 500)
    private String successValue;
    @Column(name = "failure_value", length = 500)
    private String failureValue;
    @Column(name = "for_designation")
    private Long forDesignation;
    @Column(name = "for_department")
    private Long forDepartment;
    @Column(name = "for_service")
    private Long forService;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String status;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false)
    private long lastModifiedBy;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(name = "franchise")
    private Long franchise;

    public HkGoalTemplateEntity() {
    }

    public HkGoalTemplateEntity(Long id) {
        this.id = id;
    }

    public HkGoalTemplateEntity(Long id, String templateName, int period, String templateType, String status, long createdBy, Date createdOn, long lastModifiedBy, Date lastModifiedOn) {
        this.id = id;
        this.templateName = templateName;
        this.period = period;
        this.templateType = templateType;
        this.status = status;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedOn = lastModifiedOn;
    }

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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }        

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkGoalTemplateEntity)) {
            return false;
        }
        HkGoalTemplateEntity other = (HkGoalTemplateEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkGoalTemplateEntity[ id=" + id + " ]";
    }

}
