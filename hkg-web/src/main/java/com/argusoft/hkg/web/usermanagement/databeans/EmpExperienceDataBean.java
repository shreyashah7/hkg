/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author mansi
 */
public class EmpExperienceDataBean {

    private Long id;
    private String company;
    private Long employmentType;
    private Long designation;
    private BigDecimal salary;
    private Date startedFrom;
    private Date workedTill;
    private String salaryslipImageName;
    private String salaryslippath;
    private Long salaryslipId;
    private String reasonOfLeaving;
    private String remarks;
    private Map<String, Object> experienceCustom;// for custom field data
    private Map<String, String> experienceDbType; //for get filed wise dbtype

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Long getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(Long employmentType) {
        this.employmentType = employmentType;
    }

    public Long getDesignation() {
        return designation;
    }

    public void setDesignation(Long designation) {
        this.designation = designation;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getStartedFrom() {
        return startedFrom;
    }

    public void setStartedFrom(Date startedFrom) {
        this.startedFrom = startedFrom;
    }

    public Date getWorkedTill() {
        return workedTill;
    }

    public void setWorkedTill(Date workedTill) {
        this.workedTill = workedTill;
    }

    public String getReasonOfLeaving() {
        return reasonOfLeaving;
    }

    public void setReasonOfLeaving(String reasonOfLeaving) {
        this.reasonOfLeaving = reasonOfLeaving;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSalaryslipImageName() {
        return salaryslipImageName;
    }

    public void setSalaryslipImageName(String salaryslipImageName) {
        this.salaryslipImageName = salaryslipImageName;
    }

    public String getSalaryslippath() {
        return salaryslippath;
    }

    public void setSalaryslippath(String salaryslippath) {
        this.salaryslippath = salaryslippath;
    }

    public Long getSalaryslipId() {
        return salaryslipId;
    }

    public void setSalaryslipId(Long salaryslipId) {
        this.salaryslipId = salaryslipId;
    }

    public Map<String, Object> getExperienceCustom() {
        return experienceCustom;
    }

    public void setExperienceCustom(Map<String, Object> experienceCustom) {
        this.experienceCustom = experienceCustom;
    }

    public Map<String, String> getExperienceDbType() {
        return experienceDbType;
    }

    public void setExperienceDbType(Map<String, String> experienceDbType) {
        this.experienceDbType = experienceDbType;
    }

}
