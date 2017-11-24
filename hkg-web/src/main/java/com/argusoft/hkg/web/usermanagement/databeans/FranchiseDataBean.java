/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Mayank
 */
public class FranchiseDataBean {

    private Long id;
    @NotNull(message = "Franchise name not entered...")
    @Size(max = 100, message = "Maximum 100 characters....")
    private String franchiseName;
    @NotNull(message = "Location not entered...")
    private String location;
    private Long city;
    private Long district;
    private Long state;
    private Long country;
    @NotNull(message = "Address not entered...")
    @Size(max = 500, message = "Address length exceeded 500..")
    private String address;
    @NotNull(message = "Pincode not entered..")
    @Size(max = 6, message = "Pin Code length exceeded 6..")
    private String zipCode;
    @NotNull(message = "Contact Person not entered..")
    private String contactPerson;
    @NotNull(message = "Phone number not entered..")
    @Size(max = 100, message = "Phone number length exceeded 100")
    private String phoneNumber;
    @NotNull(message = "Email not entered..")
    @Size(max = 500, message = "Email Id length exceeded 500")
    private String emailAddress;
    private Long franchiseModel;
    private String status;
    private List<FranchiseMinReqDataBean> franchiseMinReq;
    @NotNull(message = "User Name not entered..")
    private String adminUserName;
    @NotNull(message = "Password not entered..")
    @Size(min = 8, message = "Password length should atleast be 8..")
    private String password;
    private List<FranchiseDataBean> children;
    private Date activatedOn;
    private Map<String, Object> franchiseCustom;// for custom field data
    private Map<String, String> franchiseDbType; //for get filed wise dbtype
    private Boolean firstFranchise;
    private String firstName;
    private String middleName;
    private String lastName;
    private String franchiseCode;

    public Boolean isFirstFranchise() {
        return firstFranchise;
    }

    public void setFirstFranchise(Boolean firstFranchise) {
        this.firstFranchise = firstFranchise;
    }

    public Map<String, Object> getFranchiseCustom() {
        return franchiseCustom;
    }

    public void setFranchiseCustom(Map<String, Object> franchiseCustom) {
        this.franchiseCustom = franchiseCustom;
    }

    public Map<String, String> getFranchiseDbType() {
        return franchiseDbType;
    }

    public void setFranchiseDbType(Map<String, String> franchiseDbType) {
        this.franchiseDbType = franchiseDbType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFranchiseName() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getFranchiseModel() {
        return franchiseModel;
    }

    public void setFranchiseModel(Long franchiseModel) {
        this.franchiseModel = franchiseModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FranchiseMinReqDataBean> getFranchiseMinReq() {
        return franchiseMinReq;
    }

    public void setFranchiseMinReq(List<FranchiseMinReqDataBean> franchiseMinReq) {
        this.franchiseMinReq = franchiseMinReq;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FranchiseDataBean> getChildren() {
        return children;
    }

    public void setChildren(List<FranchiseDataBean> children) {
        this.children = children;
    }

    public Date getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(Date activatedOn) {
        this.activatedOn = activatedOn;
    }

    public Long getDistrict() {
        return district;
    }

    public void setDistrict(Long district) {
        this.district = district;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFranchiseCode() {
        return franchiseCode;
    }

    public void setFranchiseCode(String franchiseCode) {
        this.franchiseCode = franchiseCode;
    }
}
