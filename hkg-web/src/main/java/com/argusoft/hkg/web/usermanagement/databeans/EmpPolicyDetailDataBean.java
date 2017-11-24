/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Map;

/**
 *
 * @author mansi
 */
public class EmpPolicyDetailDataBean {

    //to set in UMContactInsurance.uMUserContact--to be taken from ids of added family members in usercontact table's id field
    private String policyNumber;
    private String policyName;
    private Long company;
    private String status;
    private String contactUser;
    private Long id;
    private String index;
    private Map<String, Object> policyCustom;// for custom field data
    private Map<String, String> policyDbType; //for get filed wise dbtype

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public Map<String, Object> getPolicyCustom() {
        return policyCustom;
    }

    public void setPolicyCustom(Map<String, Object> policyCustom) {
        this.policyCustom = policyCustom;
    }

    public Map<String, String> getPolicyDbType() {
        return policyDbType;
    }

    public void setPolicyDbType(Map<String, String> policyDbType) {
        this.policyDbType = policyDbType;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
