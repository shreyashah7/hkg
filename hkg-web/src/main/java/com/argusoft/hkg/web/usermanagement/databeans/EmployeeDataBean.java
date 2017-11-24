/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mansi
 */
public class EmployeeDataBean {

    private Long id;
    private String userId;
    private Long companyId;
    private String employeeCode;
    private Long previousfranchiseId;   
    private Long empType;
    @NotNull(message = "Employee Name not entered..")
    private String empName;
    private String gender;
    @NotNull(message = "Date Of Birth not entered..")
    private Date dob;
    private Long maritalstatus;
    private Long caste;
    private Long nationality;
    private Long bloodGrp;
    private String currentaddress;   
    private String fulladdress;  
    private String pincode;
    private String nativeaddress;
    private String nativefulladdress;
    private String nativepincode;
    private String phnno;
    private String altphnno;
    private String email;
    private boolean isNativeAddressSame;
    private String empAdhaarCardNum;
    private String empPanNum;
    private String empIdCardNum;
    private String empLicenceNum;
    private String empVehicalNum;
    private Date empPucExpiresOn;
    private String empPassportNum;
    private Date empPassportIssueOn;
    private Date empPassportExpiresOn;
    private String empAccountNum;
    private String otherDetailsOfEmployeeKeysString;
    private Date joiningDate;
    private Long workstatus;
    private Long[] workdeg;
    private String reportsToId;
    private String reportsToName;
    private Long workshift;
    private String workemailId;
    private String generatebarcode;
    private String ipaddress;
    private Long selecteddep;
    private String department;
    private Long departmentId;
    private List<EmpEducationDetalDataBean> edu;
    private List<EmpExperienceDataBean> exp;
    private List<EmpPolicyDetailDataBean> policy;
    private List<EmpFamilyDetailDataBean> family;
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
    private String[] otherdocs;
    private Date[] otherdocsDate;
    private Map<String, Long> otherdocIds;
    private String otherdocpath;
    private Date relievingDate;
    // for custom field data,//for get filed wise dbtype
    private Map<String, Object> personalCustom;
    private Map<String, String> personalDbType;
    private Map<String, Object> generalCustom;
    private Map<String, String> generalDbType;
    private Map<String, Object> contactCustom;
    private Map<String, String> contactDbType;
    private Map<String, Object> identificationCustom;
    private Map<String, String> identificationDbType;
    private Map<String, Object> otherCustom;
    private Map<String, String> otherDbType;
    private Map<String, Object> hkgworkCustom;
    private Map<String, String> hkgworkDbType;
    private String prefferedLang = HkSystemConstantUtil.I18_LANGUAGE.DEFAULT;
    private Boolean isUserRoleHigher;
    private Long custom5;

    public Long getCustom5() {
        return custom5;
    }

    public void setCustom5(Long custom5) {
        this.custom5 = custom5;
    }
    
    public Date[] getOtherdocsDate() {
        return otherdocsDate;
    }

    public void setOtherdocsDate(Date[] otherdocsDate) {
        this.otherdocsDate = otherdocsDate;
    }

    public String getPrefferedLang() {
        return prefferedLang;
    }

    public void setPrefferedLang(String prefferedLang) {
        this.prefferedLang = prefferedLang;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPreviousfranchiseId() {
        return previousfranchiseId;
    }

    public void setPreviousfranchiseId(Long previousfranchiseId) {
        this.previousfranchiseId = previousfranchiseId;
    }

    public Long getEmpType() {
        return empType;
    }

    public void setEmpType(Long empType) {
        this.empType = empType;
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

//    public Long getNativenationality() {
//        return nativenationality;
//    }
//
//    public void setNativenationality(Long nativenationality) {
//        this.nativenationality = nativenationality;
//    }
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

    public String getEmpAdhaarCardNum() {
        return empAdhaarCardNum;
    }

    public void setEmpAdhaarCardNum(String empAdhaarCardNum) {
        this.empAdhaarCardNum = empAdhaarCardNum;
    }

    public String getEmpPanNum() {
        return empPanNum;
    }

    public void setEmpPanNum(String empPanNum) {
        this.empPanNum = empPanNum;
    }

    public String getEmpIdCardNum() {
        return empIdCardNum;
    }

    public void setEmpIdCardNum(String empIdCardNum) {
        this.empIdCardNum = empIdCardNum;
    }

    public String getEmpLicenceNum() {
        return empLicenceNum;
    }

    public void setEmpLicenceNum(String empLicenceNum) {
        this.empLicenceNum = empLicenceNum;
    }

    public String getEmpVehicalNum() {
        return empVehicalNum;
    }

    public void setEmpVehicalNum(String empVehicalNum) {
        this.empVehicalNum = empVehicalNum;
    }

    public Date getEmpPucExpiresOn() {
        return empPucExpiresOn;
    }

    public void setEmpPucExpiresOn(Date empPucExpiresOn) {
        this.empPucExpiresOn = empPucExpiresOn;
    }

    public String getEmpPassportNum() {
        return empPassportNum;
    }

    public void setEmpPassportNum(String empPassportNum) {
        this.empPassportNum = empPassportNum;
    }

    public Date getEmpPassportIssueOn() {
        return empPassportIssueOn;
    }

    public void setEmpPassportIssueOn(Date empPassportIssueOn) {
        this.empPassportIssueOn = empPassportIssueOn;
    }

    public Date getEmpPassportExpiresOn() {
        return empPassportExpiresOn;
    }

    public void setEmpPassportExpiresOn(Date empPassportExpiresOn) {
        this.empPassportExpiresOn = empPassportExpiresOn;
    }

    public String getEmpAccountNum() {
        return empAccountNum;
    }

    public void setEmpAccountNum(String empAccountNum) {
        this.empAccountNum = empAccountNum;
    }

    public String getOtherDetailsOfEmployeeKeysString() {
        return otherDetailsOfEmployeeKeysString;
    }

    public void setOtherDetailsOfEmployeeKeysString(String otherDetailsOfEmployeeKeysString) {
        this.otherDetailsOfEmployeeKeysString = otherDetailsOfEmployeeKeysString;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getWorkstatus() {
        return workstatus;
    }

    public void setWorkstatus(Long workstatus) {
        this.workstatus = workstatus;
    }

    public Long[] getWorkdeg() {
        return workdeg;
    }

    public void setWorkdeg(Long[] workdeg) {
        this.workdeg = workdeg;
    }

    public String getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(String reportsToId) {
        this.reportsToId = reportsToId;
    }

    public Long getWorkshift() {
        return workshift;
    }

    public void setWorkshift(Long workshift) {
        this.workshift = workshift;
    }

    public String getWorkemailId() {
        return workemailId;
    }

    public void setWorkemailId(String workemailId) {
        this.workemailId = workemailId;
    }

    public String getGeneratebarcode() {
        return generatebarcode;
    }

    public void setGeneratebarcode(String generatebarcode) {
        this.generatebarcode = generatebarcode;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Long getSelecteddep() {
        return selecteddep;
    }

    public void setSelecteddep(Long selecteddep) {
        this.selecteddep = selecteddep;
    }

    public List<EmpEducationDetalDataBean> getEdu() {
        return edu;
    }

    public void setEdu(List<EmpEducationDetalDataBean> edu) {
        this.edu = edu;
    }

    public List<EmpExperienceDataBean> getExp() {
        return exp;
    }

    public void setExp(List<EmpExperienceDataBean> exp) {
        this.exp = exp;
    }

    public List<EmpPolicyDetailDataBean> getPolicy() {
        return policy;
    }

    public void setPolicy(List<EmpPolicyDetailDataBean> policy) {
        this.policy = policy;
    }

    public List<EmpFamilyDetailDataBean> getFamily() {
        return family;
    }

    public void setFamily(List<EmpFamilyDetailDataBean> family) {
        this.family = family;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String[] getOtherdocs() {
        return otherdocs;
    }

    public void setOtherdocs(String[] otherdocs) {
        this.otherdocs = otherdocs;
    }

    public Date getRelievingDate() {
        return relievingDate;
    }

    public void setRelievingDate(Date relievingDate) {
        this.relievingDate = relievingDate;
    }

    public String getProfilepath() {
        return profilepath;
    }

    public void setProfilepath(String profilepath) {
        this.profilepath = profilepath;
    }

    public String getOtherdocpath() {
        return otherdocpath;
    }

    public void setOtherdocpath(String otherdocpath) {
        this.otherdocpath = otherdocpath;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getReportsToName() {
        return reportsToName;
    }

    public void setReportsToName(String reportsToName) {
        this.reportsToName = reportsToName;
    }

    public Long getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Long profileImageId) {
        this.profileImageId = profileImageId;
    }

    public Map<String, Long> getOtherdocIds() {
        return otherdocIds;
    }

    public void setOtherdocIds(Map<String, Long> otherdocIds) {
        this.otherdocIds = otherdocIds;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Map<String, Object> getPersonalCustom() {
        return personalCustom;
    }

    public void setPersonalCustom(Map<String, Object> personalCustom) {
        this.personalCustom = personalCustom;
    }

    public Map<String, String> getPersonalDbType() {
        return personalDbType;
    }

    public void setPersonalDbType(Map<String, String> personalDbType) {
        this.personalDbType = personalDbType;
    }

    public Map<String, Object> getContactCustom() {
        return contactCustom;
    }

    public void setContactCustom(Map<String, Object> contactCustom) {
        this.contactCustom = contactCustom;
    }

    public Map<String, String> getContactDbType() {
        return contactDbType;
    }

    public void setContactDbType(Map<String, String> contactDbType) {
        this.contactDbType = contactDbType;
    }

    public Map<String, Object> getIdentificationCustom() {
        return identificationCustom;
    }

    public void setIdentificationCustom(Map<String, Object> identificationCustom) {
        this.identificationCustom = identificationCustom;
    }

    public Map<String, String> getIdentificationDbType() {
        return identificationDbType;
    }

    public void setIdentificationDbType(Map<String, String> identificationDbType) {
        this.identificationDbType = identificationDbType;
    }

    public Map<String, Object> getOtherCustom() {
        return otherCustom;
    }

    public void setOtherCustom(Map<String, Object> otherCustom) {
        this.otherCustom = otherCustom;
    }

    public Map<String, String> getOtherDbType() {
        return otherDbType;
    }

    public void setOtherDbType(Map<String, String> otherDbType) {
        this.otherDbType = otherDbType;
    }

    public Map<String, Object> getHkgworkCustom() {
        return hkgworkCustom;
    }

    public void setHkgworkCustom(Map<String, Object> hkgworkCustom) {
        this.hkgworkCustom = hkgworkCustom;
    }

    public Map<String, String> getHkgworkDbType() {
        return hkgworkDbType;
    }

    public void setHkgworkDbType(Map<String, String> hkgworkDbType) {
        this.hkgworkDbType = hkgworkDbType;
    }

    public Map<String, Object> getGeneralCustom() {
        return generalCustom;
    }

    public void setGeneralCustom(Map<String, Object> generalCustom) {
        this.generalCustom = generalCustom;
    }

    public Map<String, String> getGeneralDbType() {
        return generalDbType;
    }

    public void setGeneralDbType(Map<String, String> generalDbType) {
        this.generalDbType = generalDbType;
    }

    public Boolean isIsUserRoleHigher() {
        return isUserRoleHigher;
    }

    public void setIsUserRoleHigher(Boolean isUserRoleHigher) {
        this.isUserRoleHigher = isUserRoleHigher;
    }

    @Override
    public String toString() {
        return "EmployeeDataBean{" + "id=" + id + ", userId=" + userId + ", companyId=" + companyId + ", employeeCode=" + employeeCode + ", previousfranchiseId=" + previousfranchiseId + ", empType=" + empType + ", empName=" + empName + ", gender=" + gender + ", dob=" + dob + ", maritalstatus=" + maritalstatus + ", caste=" + caste + ", nationality=" + nationality + ", bloodGrp=" + bloodGrp + ", currentaddress=" + currentaddress + ", fulladdress=" + fulladdress + ", pincode=" + pincode + ", nativeaddress=" + nativeaddress + ", nativefulladdress=" + nativefulladdress + ", nativepincode=" + nativepincode + ", phnno=" + phnno + ", altphnno=" + altphnno + ", email=" + email + ", isNativeAddressSame=" + isNativeAddressSame + ", empAdhaarCardNum=" + empAdhaarCardNum + ", empPanNum=" + empPanNum + ", empIdCardNum=" + empIdCardNum + ", empLicenceNum=" + empLicenceNum + ", empVehicalNum=" + empVehicalNum + ", empPucExpiresOn=" + empPucExpiresOn + ", empPassportNum=" + empPassportNum + ", empPassportIssueOn=" + empPassportIssueOn + ", empPassportExpiresOn=" + empPassportExpiresOn + ", empAccountNum=" + empAccountNum + ", otherDetailsOfEmployeeKeysString=" + otherDetailsOfEmployeeKeysString + ", joiningDate=" + joiningDate + ", workstatus=" + workstatus + ", workdeg=" + workdeg + ", reportsToId=" + reportsToId + ", reportsToName=" + reportsToName + ", workshift=" + workshift + ", workemailId=" + workemailId + ", generatebarcode=" + generatebarcode + ", ipaddress=" + ipaddress + ", selecteddep=" + selecteddep + ", department=" + department + ", departmentId=" + departmentId + ", edu=" + edu + ", exp=" + exp + ", policy=" + policy + ", family=" + family + ", currentDistrict=" + currentDistrict + ", currentState=" + currentState + ", currentCountry=" + currentCountry + ", nativeDistrict=" + nativeDistrict + ", nativeState=" + nativeState + ", nativeCountry=" + nativeCountry + ", currentAddressId=" + currentAddressId + ", nativeAddressId=" + nativeAddressId + ", profileImageName=" + profileImageName + ", profilepath=" + profilepath + ", profileImageId=" + profileImageId + ", otherdocs=" + otherdocs + ", otherdocsDate=" + otherdocsDate + ", otherdocIds=" + otherdocIds + ", otherdocpath=" + otherdocpath + ", relievingDate=" + relievingDate + ", personalCustom=" + personalCustom + ", personalDbType=" + personalDbType + ", generalCustom=" + generalCustom + ", generalDbType=" + generalDbType + ", contactCustom=" + contactCustom + ", contactDbType=" + contactDbType + ", identificationCustom=" + identificationCustom + ", identificationDbType=" + identificationDbType + ", otherCustom=" + otherCustom + ", otherDbType=" + otherDbType + ", hkgworkCustom=" + hkgworkCustom + ", hkgworkDbType=" + hkgworkDbType + ", prefferedLang=" + prefferedLang + ", isUserRoleHigher=" + isUserRoleHigher + '}';
    }

    
}
