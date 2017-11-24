/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

/**
 *
 * @author shifa
 */
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProfileDataBean {

    /**
     *
     * @author shifa
     */
    private Long id;
    private String userId;
    private String employeeCode;
    private Long previousfranchiseId;
    @NotNull(message = "Employee Name not entered..")
    private String empName;
    private String gender;
    @NotNull(message = "Date Of Birth not entered..")
    private Date dob;
    @NotNull(message = "Marital Status not entered..")
    private Long maritalstatus;
    @NotNull(message = "Caste not entered..")
    private Long caste;
    @NotNull(message = "Nationality not entered..")
    private Long nationality;
    @NotNull(message = "Blood group not entered..")
    private Long bloodGrp;
    @NotNull(message = "Current address not entered..")
    private String currentaddress;
    @NotNull(message = "Full Address not entered..")
    @Size(max = 500, message = "Full Address length exceeded 500..")
    private String fulladdress;
    @NotNull(message = "Pin code not entered..")
    @Size(max = 6, message = "Pin code length exceeded 6..")
    private String pincode;
    private String nativeaddress;
    private String nativefulladdress;
    private String nativepincode;
    @NotNull(message = "phone number not entered")
    private String phnno;
    private String altphnno;
    private String email;
    private boolean isNativeAddressSame;
    private Long currentDistrict;
    private Long currentState;
    private Long currentCountry;
    private Long nativeDistrict;
    private Long nativeState;
    private Long nativeCountry;
    private Long currentAddressId;
    private Long nativeAddressId;
    private String profileImageName;
    private String profilepath;
    private Long profileImageId;
    private String prefferedLang = HkSystemConstantUtil.I18_LANGUAGE.DEFAULT;

    public String getPrefferedLang() {
        return prefferedLang;
    }

    public void setPrefferedLang(String prefferedLang) {
        this.prefferedLang = prefferedLang;
    }       

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Long getPreviousfranchiseId() {
        return previousfranchiseId;
    }

    public void setPreviousfranchiseId(Long previousfranchiseId) {
        this.previousfranchiseId = previousfranchiseId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Long getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(Long maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public Long getCaste() {
        return caste;
    }

    public void setCaste(Long caste) {
        this.caste = caste;
    }

    public Long getNationality() {
        return nationality;
    }

    public void setNationality(Long nationality) {
        this.nationality = nationality;
    }

    public Long getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(Long bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getCurrentaddress() {
        return currentaddress;
    }

    public void setCurrentaddress(String currentaddress) {
        this.currentaddress = currentaddress;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getNativeaddress() {
        return nativeaddress;
    }

    public void setNativeaddress(String nativeaddress) {
        this.nativeaddress = nativeaddress;
    }

    public String getNativefulladdress() {
        return nativefulladdress;
    }

    public void setNativefulladdress(String nativefulladdress) {
        this.nativefulladdress = nativefulladdress;
    }

    public String getNativepincode() {
        return nativepincode;
    }

    public void setNativepincode(String nativepincode) {
        this.nativepincode = nativepincode;
    }

    public String getPhnno() {
        return phnno;
    }

    public void setPhnno(String phnno) {
        this.phnno = phnno;
    }

    public String getAltphnno() {
        return altphnno;
    }

    public void setAltphnno(String altphnno) {
        this.altphnno = altphnno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsNativeAddressSame() {
        return isNativeAddressSame;
    }

    public void setIsNativeAddressSame(boolean isNativeAddressSame) {
        this.isNativeAddressSame = isNativeAddressSame;
    }

    public Long getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(Long currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public Long getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Long currentState) {
        this.currentState = currentState;
    }

    public Long getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(Long currentCountry) {
        this.currentCountry = currentCountry;
    }

    public Long getNativeDistrict() {
        return nativeDistrict;
    }

    public void setNativeDistrict(Long nativeDistrict) {
        this.nativeDistrict = nativeDistrict;
    }

    public Long getNativeState() {
        return nativeState;
    }

    public void setNativeState(Long nativeState) {
        this.nativeState = nativeState;
    }

    public Long getNativeCountry() {
        return nativeCountry;
    }

    public void setNativeCountry(Long nativeCountry) {
        this.nativeCountry = nativeCountry;
    }

    public Long getCurrentAddressId() {
        return currentAddressId;
    }

    public void setCurrentAddressId(Long currentAddressId) {
        this.currentAddressId = currentAddressId;
    }

    public Long getNativeAddressId() {
        return nativeAddressId;
    }

    public void setNativeAddressId(Long nativeAddressId) {
        this.nativeAddressId = nativeAddressId;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public String getProfilepath() {
        return profilepath;
    }

    public void setProfilepath(String profilepath) {
        this.profilepath = profilepath;
    }

    public Long getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Long profileImageId) {
        this.profileImageId = profileImageId;
    }

    @Override
    public String toString() {
        return "ProfileDataBean{" + "id=" + id + ", userId=" + userId + ", employeeCode=" + employeeCode + ", previousfranchiseId=" + previousfranchiseId + ", empName=" + empName + ", gender=" + gender + ", dob=" + dob + ", maritalstatus=" + maritalstatus + ", caste=" + caste + ", nationality=" + nationality + ", bloodGrp=" + bloodGrp + ", currentaddress=" + currentaddress + ", fulladdress=" + fulladdress + ", pincode=" + pincode + ", nativeaddress=" + nativeaddress + ", nativefulladdress=" + nativefulladdress + ", nativepincode=" + nativepincode + ", phnno=" + phnno + ", altphnno=" + altphnno + ", email=" + email + ", isNativeAddressSame=" + isNativeAddressSame + ", currentDistrict=" + currentDistrict + ", currentState=" + currentState + ", currentCountry=" + currentCountry + ", nativeDistrict=" + nativeDistrict + ", nativeState=" + nativeState + ", nativeCountry=" + nativeCountry + ", currentAddressId=" + currentAddressId + ", nativeAddressId=" + nativeAddressId + ", profileImageName=" + profileImageName + ", profilepath=" + profilepath + ", profileImageId=" + profileImageId + '}';
    }

}
