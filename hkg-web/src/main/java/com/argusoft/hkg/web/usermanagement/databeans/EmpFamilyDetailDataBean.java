/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author mansi
 */
public class EmpFamilyDetailDataBean {

    private Long id;
    private String firstName;
    private Date dateOfBirth;
    private Long bloodGroup;
    private Long occupation;
    private Long relation;
    private String mobileNumber;
    private String familyImageName;
    private Long familyImageId;
    private String index;
    private Map<String, Object> familyCustom;// for custom field data
    private Map<String, String> familyDbType; //for get filed wise dbtype

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(Long bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Long getOccupation() {
        return occupation;
    }

    public void setOccupation(Long occupation) {
        this.occupation = occupation;
    }

    public Long getRelation() {
        return relation;
    }

    public void setRelation(Long relation) {
        this.relation = relation;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFamilyImageName() {
        return familyImageName;
    }

    public void setFamilyImageName(String familyImageName) {
        this.familyImageName = familyImageName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Long getFamilyImageId() {
        return familyImageId;
    }

    public void setFamilyImageId(Long familyImageId) {
        this.familyImageId = familyImageId;
    }

    public Map<String, Object> getFamilyCustom() {
        return familyCustom;
    }

    public void setFamilyCustom(Map<String, Object> familyCustom) {
        this.familyCustom = familyCustom;
    }

    public Map<String, String> getFamilyDbType() {
        return familyDbType;
    }

    public void setFamilyDbType(Map<String, String> familyDbType) {
        this.familyDbType = familyDbType;
    }
}
