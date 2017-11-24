/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkTheme;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.systemconfiguration.transformers.FranchiseConfigrationTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmpEducationDetalDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmpExperienceDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmpFamilyDetailDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmpPolicyDetailDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.EmployeeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ProfileDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ThemeDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.database.UMUserDao;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMContactAddress;
import com.argusoft.usermanagement.common.model.UMContactDocument;
import com.argusoft.usermanagement.common.model.UMContactEducation;
import com.argusoft.usermanagement.common.model.UMContactExperience;
import com.argusoft.usermanagement.common.model.UMContactInsurance;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserContact;
import com.argusoft.usermanagement.common.model.UMUserIpAssociation;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.argusoft.usermanagement.common.model.UMUserRolePK;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author mansi
 */
@Service
public class EmployeeTransformerBean {

    @Autowired
    HkFoundationService foundationService;
    @Autowired
    UMUserService userService;
    @Autowired
    HkHRService hrService;
    @Autowired
    UMCompanyService companyService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;

    @Autowired
    FranchiseConfigrationTransformerBean franchiseConfigrationTransformerBean;
    @Autowired
    DepartmentTransformerBean departmentTransformerBean;
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    BasicPasswordEncryptor basicPasswordEncryptor;

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    SessionUtil sessionUtil;

    public String STATUS = "status";
    public String PROFILE = "PROFILE";
    public String SALARYSLIP = "SALARYSLIP";
    public String OTHER = "OTHER";
    public String BACKGROUND = "BACKGROUND";

    public String createEmployee(EmployeeDataBean empDataBean) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            Map<Map<Long, Map<String, Object>>, Map<String, String>> allCustomDbTypeMap = new HashMap<>();
            Map<Map<Long, Map<String, Object>>, HkSystemConstantUtil.SectionNameForCustomField> allCustomSectionName = new HashMap<>();

            List<String> documents = new ArrayList<>();
            UMUser user = new UMUser();
            convertEmpDataBeanToUser(empDataBean, user, true);

            UMUserContact userContact = new UMUserContact();
            convertEmpDataBeanToUserContact(empDataBean, userContact, true);
            userContact.setUserobj(user);
            user.setContact(userContact);

            Set<UMContactAddress> contactAddressSet = new HashSet<>();
            UMContactAddress uMContactAddressCurrent = new UMContactAddress();
            convertEmpDataBeanToContactAddress(empDataBean, uMContactAddressCurrent, false);
            uMContactAddressCurrent.setAddressType(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE);
            uMContactAddressCurrent.setUserContact(userContact);
            contactAddressSet.add(uMContactAddressCurrent);
            //native
            if (!empDataBean.isIsNativeAddressSame()) {
                UMContactAddress uMContactAddressNative = new UMContactAddress();
                convertEmpDataBeanToContactAddress(empDataBean, uMContactAddressNative, true);
                uMContactAddressNative.setAddressType(HkSystemConstantUtil.NATIVE_ADDRESS_TYPE);
                uMContactAddressNative.setUserContact(userContact);
                contactAddressSet.add(uMContactAddressNative);
            }
            userContact.setuMContactAddressSet(contactAddressSet);

            Set<UMContactDocument> userDocumentSet = new HashSet<>();
            if (empDataBean.getProfileImageName() != null) {
                UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                uMContactDocumentProfilePict.setDocumentType(PROFILE);
                uMContactDocumentProfilePict.setDocumentName(empDataBean.getProfileImageName());
                userDocumentSet.add(uMContactDocumentProfilePict);
                documents.add(empDataBean.getProfileImageName());
            }
            if (empDataBean.getOtherdocs() != null && empDataBean.getOtherdocs().length > 0) {
                int i = 0;
                for (String otherdoc : empDataBean.getOtherdocs()) {
                    if (otherdoc != null) {
                        UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                        convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                        uMContactDocumentProfilePict.setDocumentType(OTHER);
                        uMContactDocumentProfilePict.setDocumentName(otherdoc);
                        uMContactDocumentProfilePict.setCustom4(empDataBean.getOtherdocsDate()[i]);
                        userDocumentSet.add(uMContactDocumentProfilePict);
                        documents.add(otherdoc);
                    }
                    i++;
                }
            }

            Map<UMContactEducation, Map<String, Object>> educationCustomMap = new HashMap<>();
            Map<UMContactExperience, Map<String, Object>> experienceCustomMap = new HashMap<>();
            Map<UMUserContact, Map<String, Object>> familyCustomMap = new HashMap<>();
            Map<UMContactInsurance, Map<String, Object>> policyCustomMap = new HashMap<>();
            Map<String, String> educationDbType = null;
            Map<String, String> experienceDbType = null;
            Map<String, String> familyDbType = null;
            Map<String, String> policyDbType = null;

            Set<UMContactEducation> educationSet = new HashSet<>();
            if (!CollectionUtils.isEmpty(empDataBean.getEdu())) {
                for (EmpEducationDetalDataBean educationDetailDataBean : empDataBean.getEdu()) {
                    if (educationDetailDataBean != null && educationDetailDataBean.getDegree() != null) {
                        UMContactEducation uMContactEducation = new UMContactEducation();
                        convertEmpDataBeanToContactEducation(educationDetailDataBean, uMContactEducation);
                        uMContactEducation.setIsArchive(false);
                        uMContactEducation.setUserContact(userContact);
                        if (!CollectionUtils.isEmpty(educationDetailDataBean.getEducationCustom())) {
                            educationCustomMap.put(uMContactEducation, educationDetailDataBean.getEducationCustom());
                            educationDbType = educationDetailDataBean.getEducationDbType();
                        }
                        educationSet.add(uMContactEducation);
                    }
                }
            }
            userContact.setuMContactEducationSet(educationSet);

            Set<UMContactExperience> experienceSet = new HashSet<>();
            for (EmpExperienceDataBean experienceDataBean : empDataBean.getExp()) {
                if (experienceDataBean != null && experienceDataBean.getCompany() != null) {
                    UMContactExperience uMContactExperience = new UMContactExperience();
                    convertEmpExperienceDataBeanToContactExperience(experienceDataBean, uMContactExperience);
                    if (experienceDataBean.getSalaryslipImageName() != null) {
                        uMContactExperience.setSalarySlipFileName(experienceDataBean.getSalaryslipImageName());
                        UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                        convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                        uMContactDocumentProfilePict.setDocumentType(SALARYSLIP);
                        uMContactDocumentProfilePict.setDocumentName(experienceDataBean.getSalaryslipImageName());
                        userDocumentSet.add(uMContactDocumentProfilePict);
                        documents.add(experienceDataBean.getSalaryslipImageName());
                    }
                    if (!CollectionUtils.isEmpty(experienceDataBean.getExperienceCustom())) {
                        experienceCustomMap.put(uMContactExperience, experienceDataBean.getExperienceCustom());
                        experienceDbType = experienceDataBean.getExperienceDbType();
                    }
                    uMContactExperience.setEmploymentType(empDataBean.getEmpType().toString());
                    uMContactExperience.setUserContact(userContact);
                    experienceSet.add(uMContactExperience);
                }
            }
            userContact.setuMContactExperienceSet(experienceSet);

            Map<String, UMUserContact> mapFamilyMemTemptoNewId = new HashMap<>();

            mapFamilyMemTemptoNewId.put("E0", userContact);
            Set<UMUserContact> familySet = new HashSet<>();
            for (EmpFamilyDetailDataBean hKEmpFamilyDetailsDataBean : empDataBean.getFamily()) {
                if (hKEmpFamilyDetailsDataBean != null && hKEmpFamilyDetailsDataBean.getFirstName() != null) {
                    UMUserContact familyUserContact = new UMUserContact();
                    familyUserContact.setContactUser(user.getId());
                    familyUserContact.setContactUserObj(user);
                    convertEmpFamilyDetailDataBeanToUserFamilyContact(hKEmpFamilyDetailsDataBean, familyUserContact);

                    if (hKEmpFamilyDetailsDataBean.getFamilyImageName() != null) {
                        UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                        convertFileToContactDocument(uMContactDocumentProfilePict, familyUserContact);
                        uMContactDocumentProfilePict.setDocumentType(PROFILE);
                        uMContactDocumentProfilePict.setDocumentName(hKEmpFamilyDetailsDataBean.getFamilyImageName());
                        Set<UMContactDocument> familyDocumentSet = new HashSet<>();
                        familyDocumentSet.add(uMContactDocumentProfilePict);
                        familyUserContact.setuMContactDocumentSet(familyDocumentSet);
                        documents.add(hKEmpFamilyDetailsDataBean.getFamilyImageName());
                    }
                    if (!CollectionUtils.isEmpty(hKEmpFamilyDetailsDataBean.getFamilyCustom())) {
                        familyCustomMap.put(familyUserContact, hKEmpFamilyDetailsDataBean.getFamilyCustom());
                        familyDbType = hKEmpFamilyDetailsDataBean.getFamilyDbType();
                    }
                    familySet.add(familyUserContact);
                    mapFamilyMemTemptoNewId.put("F" + hKEmpFamilyDetailsDataBean.getIndex(), familyUserContact);
                }
            }
            user.setUMUserContactSet(familySet);
            for (EmpPolicyDetailDataBean policyDetailDataBean : empDataBean.getPolicy()) {
                if (policyDetailDataBean != null && policyDetailDataBean.getContactUser() != null) {
                    UMContactInsurance uMContactInsurance = new UMContactInsurance();
                    convertEmpPolicyDetailDataBeanToContactInsurance(policyDetailDataBean, uMContactInsurance);
                    UMUserContact get = mapFamilyMemTemptoNewId.get(policyDetailDataBean.getContactUser());
                    Set<UMContactInsurance> umContactInsuranceSet = get.getuMContactInsuranceSet();
                    if (umContactInsuranceSet == null) {
                        umContactInsuranceSet = new HashSet<>();
                    }
                    if (!CollectionUtils.isEmpty(policyDetailDataBean.getPolicyCustom())) {
                        policyCustomMap.put(uMContactInsurance, policyDetailDataBean.getPolicyCustom());
                        policyDbType = policyDetailDataBean.getPolicyDbType();
                    }
                    umContactInsuranceSet.add(uMContactInsurance);
                    get.setuMContactInsuranceSet(umContactInsuranceSet);
                    uMContactInsurance.setUserContact(get);
                }
            }

            Set<UMUserRole> roleSet = new HashSet<>();
            if (empDataBean.getWorkdeg().length > 0) {
                for (Long role : empDataBean.getWorkdeg()) {
                    UMUserRole uMUserRole = new UMUserRole();
                    UMUserRolePK rolePK = new UMUserRolePK();
                    rolePK.setRole(role);
                    uMUserRole.setuMUserRolePK(rolePK);
                    roleSet.add(uMUserRole);
                }
            }
            user.setUMUserRoleSet(roleSet);

            if (empDataBean.getIpaddress() != null && empDataBean.getIpaddress().length() > 0) {
                Set<UMUserIpAssociation> ipSet = new HashSet<>();
                UMUserIpAssociation userIpAssociation = new UMUserIpAssociation();
                convertEmpDataBeanToUserIpAssociation(empDataBean, userIpAssociation);
                ipSet.add(userIpAssociation);
                user.setuMUserIpAssociationSet(ipSet);
            }

            userContact.setuMContactDocumentSet(userDocumentSet);
            userManagementServiceWrapper.createUser(user);
            Long company = user.getCompany();
            if (company != null && !company.equals(0l)) {
                UMCompany retrieveCompanyByCompanyId = companyService.retrieveCompanyByCompanyId(company);
                retrieveCompanyByCompanyId.setActiveUsers(retrieveCompanyByCompanyId.getActiveUsers() + 1);
                companyService.updateAllCompany(Arrays.asList(retrieveCompanyByCompanyId));
            }
            Set<UMContactDocument> umContactDocumentSet = userContact.getuMContactDocumentSet();
            if (!CollectionUtils.isEmpty(umContactDocumentSet)) {
                List<UMContactDocument> listDocument = new ArrayList<>();
                for (UMContactDocument contactDocument : umContactDocumentSet) {
                    if (contactDocument.getDocumentName() != null) {
                        contactDocument.setDocumentName(FolderManagement.changeFileName(contactDocument.getDocumentName(), user.getId()));
                        listDocument.add(contactDocument);
                    }
                }
                if (!CollectionUtils.isEmpty(listDocument)) {
                    userService.createOrUpdateAllContactDocument(listDocument);
                }
            }
            Set<UMContactExperience> umContactExperienceSet = userContact.getuMContactExperienceSet();
            if (!CollectionUtils.isEmpty(umContactExperienceSet)) {
                List<UMContactExperience> listExperience = new ArrayList<>();
                for (UMContactExperience uMContactExperience : umContactExperienceSet) {
                    if (uMContactExperience.getSalarySlipFileName() != null) {
                        uMContactExperience.setSalarySlipFileName(FolderManagement.changeFileName(uMContactExperience.getSalarySlipFileName(), user.getId()));
                        listExperience.add(uMContactExperience);
                    }
                }
                if (!CollectionUtils.isEmpty(listExperience)) {
                    userService.createOrUpdateAllUMContactExperience(listExperience);
                }
            }

            if (!CollectionUtils.isEmpty(documents)) {
                try {
                    FolderManagement.saveFile(null, null, user.getId(), null, documents, true);
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            createOrUpdateCustomField(empDataBean, user.getId(), map, allCustomDbTypeMap, allCustomSectionName, educationCustomMap, educationDbType, experienceCustomMap, experienceDbType, familyCustomMap, familyDbType, policyCustomMap, policyDbType);
            response = user.getUserId();
        } catch (Exception ex) {
            Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public boolean doesUserIdExist(String userId) {
        return userService.isUserExist(userId);
    }

    public void createOrUpdateCustomField(EmployeeDataBean empDataBean, Long id, Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map,
            Map<Map<Long, Map<String, Object>>, Map<String, String>> allCustomDbTypeMap,
            Map<Map<Long, Map<String, Object>>, HkSystemConstantUtil.SectionNameForCustomField> allCustomSectionName,
            Map<UMContactEducation, Map<String, Object>> educationCustomMap, Map<String, String> educationDbType,
            Map<UMContactExperience, Map<String, Object>> experienceCustomMap, Map<String, String> experienceDbType,
            Map<UMUserContact, Map<String, Object>> familyCustomMap, Map<String, String> familyDbType,
            Map<UMContactInsurance, Map<String, Object>> policyCustomMap, Map<String, String> policyDbType) {

        Map<Long, Map<String, Object>> personalval = new HashMap<>();
        personalval.put(id, empDataBean.getPersonalCustom());
        allCustomSectionName.put(personalval, HkSystemConstantUtil.SectionNameForCustomField.PERSONAL);
        allCustomDbTypeMap.put(personalval, empDataBean.getPersonalDbType());
        Map<Long, Map<String, Object>> generalval = new HashMap<>();
        generalval.put(id, empDataBean.getGeneralCustom());
        allCustomSectionName.put(generalval, HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
        allCustomDbTypeMap.put(generalval, empDataBean.getGeneralDbType());

        Map<Long, Map<String, Object>> contactval = new HashMap<>();
        contactval.put(id, empDataBean.getContactCustom());
        allCustomSectionName.put(contactval, HkSystemConstantUtil.SectionNameForCustomField.CONTACT);
        allCustomDbTypeMap.put(contactval, empDataBean.getContactDbType());

        Map<Long, Map<String, Object>> identificationval = new HashMap<>();
        identificationval.put(id, empDataBean.getIdentificationCustom());
        allCustomSectionName.put(identificationval, HkSystemConstantUtil.SectionNameForCustomField.IDENTIFICATION);
        allCustomDbTypeMap.put(identificationval, empDataBean.getIdentificationDbType());

        Map<Long, Map<String, Object>> otherval = new HashMap<>();
        otherval.put(id, empDataBean.getOtherCustom());
        allCustomSectionName.put(otherval, HkSystemConstantUtil.SectionNameForCustomField.OTHER);
        allCustomDbTypeMap.put(otherval, empDataBean.getOtherDbType());

        Map<Long, Map<String, Object>> hkgworkval = new HashMap<>();
        hkgworkval.put(id, empDataBean.getHkgworkCustom());
        allCustomSectionName.put(hkgworkval, HkSystemConstantUtil.SectionNameForCustomField.HKGWORK);
        allCustomDbTypeMap.put(hkgworkval, empDataBean.getHkgworkDbType());

        Map<Long, Map<String, Object>> educationval = new HashMap<>();
        for (Map.Entry<UMContactEducation, Map<String, Object>> entry : educationCustomMap.entrySet()) {
            educationval.put(entry.getKey().getId(), entry.getValue());
        }
        allCustomSectionName.put(educationval, HkSystemConstantUtil.SectionNameForCustomField.EDUCATION);
        if (!CollectionUtils.isEmpty(educationDbType)) {
            allCustomDbTypeMap.put(educationval, educationDbType);
        }

        Map<Long, Map<String, Object>> experienceval = new HashMap<>();
        for (Map.Entry<UMContactExperience, Map<String, Object>> entry : experienceCustomMap.entrySet()) {
            experienceval.put(entry.getKey().getId(), entry.getValue());
        }
        allCustomSectionName.put(experienceval, HkSystemConstantUtil.SectionNameForCustomField.EXPERIENCE);
        if (!CollectionUtils.isEmpty(experienceDbType)) {
            allCustomDbTypeMap.put(experienceval, experienceDbType);
        }

        Map<Long, Map<String, Object>> familyval = new HashMap<>();
        for (Map.Entry<UMUserContact, Map<String, Object>> entry : familyCustomMap.entrySet()) {
            familyval.put(entry.getKey().getId(), entry.getValue());
        }
        allCustomSectionName.put(familyval, HkSystemConstantUtil.SectionNameForCustomField.FAMILY);
        if (!CollectionUtils.isEmpty(familyDbType)) {
            allCustomDbTypeMap.put(familyval, familyDbType);
        }

        Map<Long, Map<String, Object>> policyval = new HashMap<>();
        for (Map.Entry<UMContactInsurance, Map<String, Object>> entry : policyCustomMap.entrySet()) {
            policyval.put(entry.getKey().getId(), entry.getValue());
        }
        allCustomSectionName.put(policyval, HkSystemConstantUtil.SectionNameForCustomField.POLICY);
        if (!CollectionUtils.isEmpty(policyDbType)) {
            allCustomDbTypeMap.put(policyval, policyDbType);
        }
        for (Map.Entry<Map<Long, Map<String, Object>>, Map<String, String>> entry : allCustomDbTypeMap.entrySet()) {
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(entry.getValue())) {
                for (Map.Entry<String, String> entrySet : entry.getValue().entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }
            Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            HkSystemConstantUtil.SectionNameForCustomField get = allCustomSectionName.get(entry.getKey());
            List<CustomField> customField = customFieldSevice.makeCustomField(entry.getKey(), entry.getValue(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.USER.toString(), hkLoginDataBean.getCompanyId(), id);
            map.put(get, customField);
        }
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.USER, hkLoginDataBean.getCompanyId(), map);
    }

    public void convertFileToContactDocument(UMContactDocument document, UMUserContact contact) {
        if (document == null) {
            document = new UMContactDocument();
        }
        document.setLastModifiedBy(hkLoginDataBean.getId());
        document.setLastModifiedOn(new Date());
        document.setUserContact(contact);
    }

    public String updateEmployee(EmployeeDataBean empDataBean) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            Map<Map<Long, Map<String, Object>>, Map<String, String>> allCustomDbTypeMap = new HashMap<>();
            Map<Map<Long, Map<String, Object>>, HkSystemConstantUtil.SectionNameForCustomField> allCustomSectionName = new HashMap<>();

            Map<UMContactEducation, Map<String, Object>> educationCustomMap = new HashMap<>();
            Map<UMContactExperience, Map<String, Object>> experienceCustomMap = new HashMap<>();
            Map<UMUserContact, Map<String, Object>> familyCustomMap = new HashMap<>();
            Map<UMContactInsurance, Map<String, Object>> policyCustomMap = new HashMap<>();
            Map<String, String> educationDbType = null;
            Map<String, String> experienceDbType = null;
            Map<String, String> familyDbType = null;
            Map<String, String> policyDbType = null;

            List<String> documents = new ArrayList<>();
            Set<UMContactDocument> userDocumentSet = new HashSet<>();
            Long id = null;
            if (empDataBean.getId() == null) {
                id = hkLoginDataBean.getId();
            } else {
                id = empDataBean.getId();
            }
            UMUser user = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
            convertEmpDataBeanToUser(empDataBean, user, false);
            UMUserContact userContact = user.getContact();
            convertEmpDataBeanToUserContact(empDataBean, userContact, false);
            if (empDataBean.getProfileImageName() != null) {
                UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                uMContactDocumentProfilePict.setId(empDataBean.getProfileImageId());
                uMContactDocumentProfilePict.setDocumentType(PROFILE);
                uMContactDocumentProfilePict.setDocumentName(FolderManagement.changeFileName(empDataBean.getProfileImageName(), user.getId()));
                userDocumentSet.add(uMContactDocumentProfilePict);
                documents.add(empDataBean.getProfileImageName());
            }

            Set<UMContactAddress> contactAddressSet = new HashSet<>();
            UMContactAddress uMContactAddressCurrent = new UMContactAddress(empDataBean.getCurrentAddressId());
            convertEmpDataBeanToContactAddress(empDataBean, uMContactAddressCurrent, false);
            uMContactAddressCurrent.setAddressType(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE);
            uMContactAddressCurrent.setUserContact(userContact);
            contactAddressSet.add(uMContactAddressCurrent);
            //native
            if (!empDataBean.isIsNativeAddressSame()) {
                UMContactAddress uMContactAddressNative = new UMContactAddress();
                convertEmpDataBeanToContactAddress(empDataBean, uMContactAddressNative, true);
                uMContactAddressNative.setAddressType(HkSystemConstantUtil.NATIVE_ADDRESS_TYPE);
                uMContactAddressNative.setUserContact(userContact);
                contactAddressSet.add(uMContactAddressNative);
            }
            userContact.setuMContactAddressSet(contactAddressSet);

            Set<UMContactEducation> educationSet = new HashSet<>();
            if (!CollectionUtils.isEmpty(empDataBean.getEdu())) {
                for (EmpEducationDetalDataBean educationDetailDataBean : empDataBean.getEdu()) {
                    if (educationDetailDataBean != null && educationDetailDataBean.getDegree() != null) {
                        UMContactEducation uMContactEducation = new UMContactEducation();
                        convertEmpDataBeanToContactEducation(educationDetailDataBean, uMContactEducation);
                        uMContactEducation.setIsArchive(false);
                        uMContactEducation.setUserContact(userContact);
                        if (!CollectionUtils.isEmpty(educationDetailDataBean.getEducationCustom())) {
                            educationCustomMap.put(uMContactEducation, educationDetailDataBean.getEducationCustom());
                            educationDbType = educationDetailDataBean.getEducationDbType();
                        }
                        educationSet.add(uMContactEducation);
                    }
                }
            }
            userContact.setuMContactEducationSet(educationSet);

            Set<UMContactExperience> experienceSet = new HashSet<>();
            for (EmpExperienceDataBean experienceDataBean : empDataBean.getExp()) {
                if (experienceDataBean != null && experienceDataBean.getCompany() != null) {
                    UMContactExperience uMContactExperience = new UMContactExperience();
                    convertEmpExperienceDataBeanToContactExperience(experienceDataBean, uMContactExperience);
                    if (experienceDataBean.getSalaryslipImageName() != null) {
                        uMContactExperience.setSalarySlipFileName(FolderManagement.changeFileName(experienceDataBean.getSalaryslipImageName(), user.getId()));
                        UMContactDocument salarySlipDocument = new UMContactDocument();
                        convertFileToContactDocument(salarySlipDocument, userContact);
                        salarySlipDocument.setId(experienceDataBean.getSalaryslipId());
                        salarySlipDocument.setDocumentType(SALARYSLIP);
                        salarySlipDocument.setDocumentName(FolderManagement.changeFileName(experienceDataBean.getSalaryslipImageName(), user.getId()));
                        userDocumentSet.add(salarySlipDocument);
                        documents.add(experienceDataBean.getSalaryslipImageName());
                    }
                    if (!CollectionUtils.isEmpty(experienceDataBean.getExperienceCustom())) {
                        experienceCustomMap.put(uMContactExperience, experienceDataBean.getExperienceCustom());
                        experienceDbType = experienceDataBean.getExperienceDbType();
                    }
                    uMContactExperience.setEmploymentType(empDataBean.getEmpType().toString());
                    uMContactExperience.setUserContact(userContact);
                    uMContactExperience.setIsArchive(false);
                    experienceSet.add(uMContactExperience);
                }
            }
            userContact.setuMContactExperienceSet(experienceSet);

            Map<String, UMUserContact> mapFamilyMemTemptoNewId = new HashMap<>();
            userContact.setuMContactInsuranceSet(null);
            mapFamilyMemTemptoNewId.put("E0", userContact);
            Set<UMUserContact> familySet = new HashSet<>();
            Set<UMUserContact> umUserContactSet = user.getUMUserContactSet();
            Map<Long, UMUserContact> userContactMap = new HashMap<>();
            for (UMUserContact uMUserContact : umUserContactSet) {
                userContactMap.put(uMUserContact.getId(), uMUserContact);
            }
            for (EmpFamilyDetailDataBean hKEmpFamilyDetailsDataBean : empDataBean.getFamily()) {
                if (hKEmpFamilyDetailsDataBean != null && hKEmpFamilyDetailsDataBean.getFirstName() != null) {
                    UMUserContact familyUserContact = null;
                    if (hKEmpFamilyDetailsDataBean.getId() != null) {
                        familyUserContact = userContactMap.get(hKEmpFamilyDetailsDataBean.getId());
                    }
                    if (familyUserContact == null) {
                        familyUserContact = new UMUserContact();
                    }
                    familyUserContact.setContactUser(user.getId());
                    familyUserContact.setContactUserObj(user);
                    convertEmpFamilyDetailDataBeanToUserFamilyContact(hKEmpFamilyDetailsDataBean, familyUserContact);
                    if (hKEmpFamilyDetailsDataBean.getFamilyImageName() != null) {
                        UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                        convertFileToContactDocument(uMContactDocumentProfilePict, familyUserContact);
                        uMContactDocumentProfilePict.setId(hKEmpFamilyDetailsDataBean.getFamilyImageId());
                        uMContactDocumentProfilePict.setDocumentType(PROFILE);
                        uMContactDocumentProfilePict.setDocumentName(FolderManagement.changeFileName(hKEmpFamilyDetailsDataBean.getFamilyImageName(), user.getId()));
                        Set<UMContactDocument> familyDocumentSet = new HashSet<>();
                        familyDocumentSet.add(uMContactDocumentProfilePict);
                        familyUserContact.setuMContactDocumentSet(familyDocumentSet);
                        documents.add(hKEmpFamilyDetailsDataBean.getFamilyImageName());
                    } else {
                        familyUserContact.setuMContactDocumentSet(null);
                    }
                    if (!CollectionUtils.isEmpty(hKEmpFamilyDetailsDataBean.getFamilyCustom())) {
                        familyCustomMap.put(familyUserContact, hKEmpFamilyDetailsDataBean.getFamilyCustom());
                        familyDbType = hKEmpFamilyDetailsDataBean.getFamilyDbType();
                    }
                    familySet.add(familyUserContact);
                    familyUserContact.setuMContactInsuranceSet(null);
                    mapFamilyMemTemptoNewId.put('F' + hKEmpFamilyDetailsDataBean.getIndex(), familyUserContact);
                }
            }
            user.setUMUserContactSet(familySet);

            for (EmpPolicyDetailDataBean policyDetailDataBean : empDataBean.getPolicy()) {
                if (policyDetailDataBean != null && policyDetailDataBean.getContactUser() != null) {
                    UMContactInsurance uMContactInsurance = new UMContactInsurance();
                    convertEmpPolicyDetailDataBeanToContactInsurance(policyDetailDataBean, uMContactInsurance);
                    UMUserContact get = mapFamilyMemTemptoNewId.get(policyDetailDataBean.getContactUser());
                    Set<UMContactInsurance> umContactInsuranceSettemp = new HashSet<>();
                    if (!CollectionUtils.isEmpty(get.getuMContactInsuranceSet())) {
                        for (UMContactInsurance uMContactInsurance1 : get.getuMContactInsuranceSet()) {
                            if (!uMContactInsurance1.getIsArchive()) {
                                umContactInsuranceSettemp.add(uMContactInsurance1);
                            }
                        }
                    }
                    umContactInsuranceSettemp.add(uMContactInsurance);
                    if (get.getId() != null && get.getId().equals(userContact.getId())) {
                        userContact.setuMContactInsuranceSet(umContactInsuranceSettemp);
                        uMContactInsurance.setUserContact(userContact);
                    } else {
                        get.setuMContactInsuranceSet(umContactInsuranceSettemp);
                        uMContactInsurance.setUserContact(get);
                    }
                    if (!CollectionUtils.isEmpty(policyDetailDataBean.getPolicyCustom())) {
                        policyCustomMap.put(uMContactInsurance, policyDetailDataBean.getPolicyCustom());
                        policyDbType = policyDetailDataBean.getPolicyDbType();
                    }
                }
            }
            Set<UMUserRole> roleSet = new HashSet<>();
            if (empDataBean.getWorkdeg().length > 0) {
                for (Long role : empDataBean.getWorkdeg()) {
                    UMUserRole uMUserRole = new UMUserRole(user.getId(), role);
                    UMUserRolePK rolePK = new UMUserRolePK(user.getId(), role);
                    uMUserRole.setuMUserRolePK(rolePK);
                    roleSet.add(uMUserRole);
                }
            }
            user.setUMUserRoleSet(roleSet);

            if (empDataBean.getIpaddress() != null && empDataBean.getIpaddress().length() > 0) {
                Set<UMUserIpAssociation> ipSet = new HashSet<>();
                UMUserIpAssociation userIpAssociation = new UMUserIpAssociation();
                convertEmpDataBeanToUserIpAssociation(empDataBean, userIpAssociation);
                ipSet.add(userIpAssociation);
                user.setuMUserIpAssociationSet(ipSet);
            }
            if (empDataBean.getOtherdocs() != null && empDataBean.getOtherdocs().length > 0) {
                int i = 0;
                for (String otherdoc : empDataBean.getOtherdocs()) {
                    if (otherdoc != null) {
                        UMContactDocument otherContactDocument = new UMContactDocument();
                        convertFileToContactDocument(otherContactDocument, userContact);
                        otherContactDocument.setCustom4(empDataBean.getOtherdocsDate()[i]);
                        if (!empDataBean.getOtherdocIds().isEmpty()) {
                            Long get = empDataBean.getOtherdocIds().get(otherdoc);
                            otherContactDocument.setId(get);
                        }
                        otherContactDocument.setDocumentType(OTHER);
                        otherContactDocument.setDocumentName(FolderManagement.changeFileName(otherdoc, user.getId()));
                        userDocumentSet.add(otherContactDocument);
                        documents.add(otherdoc);
                    }
                    i++;
                }
            }

            userContact.setuMContactDocumentSet(userDocumentSet);
            user.setContact(userContact);
            userManagementServiceWrapper.updateUser(user);

            if (!CollectionUtils.isEmpty(documents)) {
                try {
                    FolderManagement.saveFile(null, null, user.getId(), null, documents, true);
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            createOrUpdateCustomField(empDataBean, user.getId(), map, allCustomDbTypeMap, allCustomSectionName, educationCustomMap, educationDbType, experienceCustomMap, experienceDbType, familyCustomMap, familyDbType, policyCustomMap, policyDbType);
            response = HkSystemConstantUtil.SUCCESS;
        } catch (UMUserManagementException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String updateProfile(ProfileDataBean profileDataBean) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            List<String> documents = new ArrayList<>();
            Set<UMContactDocument> userDocumentSet = new HashSet<>();
            Long id = null;
            if (profileDataBean.getId() == null) {
                id = hkLoginDataBean.getId();
            } else {
                id = profileDataBean.getId();
            }
            UMUser user = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
            convertProfileDataBeanToUser(profileDataBean, user, false);
            UMUserContact userContact = user.getContact();
            convertProfileDataBeanToUserContact(profileDataBean, userContact, false);
            if (profileDataBean.getProfileImageName() != null) {
                UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                uMContactDocumentProfilePict.setId(profileDataBean.getProfileImageId());
                uMContactDocumentProfilePict.setDocumentType(PROFILE);
                uMContactDocumentProfilePict.setDocumentName(FolderManagement.changeFileName(profileDataBean.getProfileImageName(), user.getId()));
                userDocumentSet.add(uMContactDocumentProfilePict);
                documents.add(profileDataBean.getProfileImageName());
            }

            Set<UMContactAddress> contactAddressSet = new HashSet<>();
            UMContactAddress uMContactAddressCurrent = new UMContactAddress(profileDataBean.getCurrentAddressId());
            convertProfileDataBeanToContactAddress(profileDataBean, uMContactAddressCurrent, false);
            uMContactAddressCurrent.setAddressType(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE);
            uMContactAddressCurrent.setUserContact(userContact);
            contactAddressSet.add(uMContactAddressCurrent);
            //native
            if (!profileDataBean.isIsNativeAddressSame()) {
                UMContactAddress uMContactAddressNative = new UMContactAddress();
                convertProfileDataBeanToContactAddress(profileDataBean, uMContactAddressNative, true);
                uMContactAddressNative.setAddressType(HkSystemConstantUtil.NATIVE_ADDRESS_TYPE);
                uMContactAddressNative.setUserContact(userContact);
                contactAddressSet.add(uMContactAddressNative);
            }
            userContact.setuMContactAddressSet(contactAddressSet);
            userContact.setuMContactDocumentSet(userDocumentSet);
            user.setContact(userContact);
            userManagementServiceWrapper.updateUser(user);
            if (!CollectionUtils.isEmpty(documents)) {
                try {
                    FolderManagement.saveFile(null, null, user.getId(), null, documents, true);
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            response = HkSystemConstantUtil.SUCCESS;
        } catch (UMUserManagementException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String updateSuperAdminProfile(Map<String, Object> superadmindetails) throws ParseException {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            List<String> documents = new ArrayList<>();
            Set<UMContactDocument> userDocumentSet = new HashSet<>();
            Long id = null;
            id = hkLoginDataBean.getId();
            UMUser user = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
            convertSuperAdminProfileToUser(superadmindetails, user, false);
            UMUserContact userContact = user.getContact();
            if (userContact == null) {
                userContact = new UMUserContact();
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dateOfBirth = df.parse(superadmindetails.get("dob").toString());
            userContact.setDateOfBirth(HkSystemFunctionUtil.convertToServerDate(dateOfBirth, hkLoginDataBean.getClientRawOffsetInMin()));
            userContact.setGender(superadmindetails.get("gender").toString());
            if (superadmindetails.get("profileImageName") != null) {
                UMContactDocument uMContactDocumentProfilePict = new UMContactDocument();
                convertFileToContactDocument(uMContactDocumentProfilePict, userContact);
                Long profileimage = null;
                if (superadmindetails.containsKey("profileImageId") && superadmindetails.get("profileImageId") != null) {
                    profileimage = Long.parseLong(String.valueOf(superadmindetails.get("profileImageId")));
                }
                uMContactDocumentProfilePict.setId(profileimage);
                uMContactDocumentProfilePict.setDocumentType(PROFILE);
                uMContactDocumentProfilePict.setDocumentName(FolderManagement.changeFileName(superadmindetails.get("profileImageName").toString(), user.getId()));
                userDocumentSet.add(uMContactDocumentProfilePict);
                documents.add(superadmindetails.get("profileImageName").toString());
            }
//
            userContact.setuMContactDocumentSet(userDocumentSet);
            user.setContact(userContact);
            userManagementServiceWrapper.updateUser(user);
            if (!CollectionUtils.isEmpty(documents)) {
                try {
                    FolderManagement.saveFile(null, null, user.getId(), null, documents, true);
                } catch (IOException ex) {
                    Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            response = HkSystemConstantUtil.SUCCESS;
        } catch (UMUserManagementException e) {
            e.printStackTrace();
        }
        return response;
    }

    public UMUser convertEmpDataBeanToUser(EmployeeDataBean employeeDataBean, UMUser user, boolean isCreate) throws UMUserManagementException {
        if (user == null) {
            user = new UMUser();
        }
        user.setCustom2(employeeDataBean.getReportsToId());
        if (employeeDataBean.getEmpName() != null) {
            String[] split = employeeDataBean.getEmpName().split(" ");
            user.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                user.setLastName(split[1]);
            } else if (split.length > 2) {
                user.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                user.setLastName(last.toString());
            }
        }
        user.setUserCode(employeeDataBean.getEmployeeCode());
        user.setCustom5(employeeDataBean.getCustom5());
        user.setStatus(employeeDataBean.getWorkstatus().toString());
        user.setExpiredOn(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getRelievingDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        user.setCustom1(employeeDataBean.getPreviousfranchiseId());
        user.setCustom4(employeeDataBean.getWorkshift());
        user.setEmailAddress(employeeDataBean.getWorkemailId());
        user.setJoiningDate(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getJoiningDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        user.setEmailAddress(employeeDataBean.getEmail());
        user.setMobileNumber(employeeDataBean.getPhnno());
        user.setDepartment(employeeDataBean.getSelecteddep());
        if (isCreate) {
            user.setCompany(hkLoginDataBean.getCompanyId());
            user.setCreatedBy(hkLoginDataBean.getId());
            user.setCreatedOn(new Date());
            user.setIsActive(true);
            user.setIsArchive(false);
        }
        user.setModifiedOn(new Date());
        user.setModifiedBy(hkLoginDataBean.getId());
        user.setPreferredLanguage(HkSystemConstantUtil.I18_LANGUAGE.DEFAULT);
        return user;
    }

    public UMUser convertProfileDataBeanToUser(ProfileDataBean profileDataBean, UMUser user, boolean isCreate) throws UMUserManagementException {
        if (user == null) {
            user = new UMUser();
        }

        if (profileDataBean.getEmpName() != null) {
            String[] split = profileDataBean.getEmpName().split(" ");
            user.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                user.setLastName(split[1]);
            } else if (split.length > 2) {
                user.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                user.setLastName(last.toString());
            }
        }
//        user.setUserCode(profileDataBean.getEmployeeCode());
        user.setEmailAddress(profileDataBean.getEmail());
        user.setMobileNumber(profileDataBean.getPhnno());
        if (isCreate) {
            user.setCompany(hkLoginDataBean.getCompanyId());
            user.setCreatedBy(hkLoginDataBean.getId());
            user.setCreatedOn(new Date());
            user.setIsActive(true);
            user.setIsArchive(false);
        }

        user.setPreferredLanguage(profileDataBean.getPrefferedLang());
        user.setModifiedOn(new Date());
        user.setModifiedBy(hkLoginDataBean.getId());
        return user;
    }

    public UMUser convertSuperAdminProfileToUser(Map<String, Object> superadmindetails, UMUser user, boolean isCreate) throws UMUserManagementException {
        if (user == null) {
            user = new UMUser();
        }

        if ((superadmindetails.get("empName").toString()) != null) {
            String[] split = superadmindetails.get("empName").toString().split(" ");
            user.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                user.setLastName(split[1]);
            } else if (split.length > 2) {
                user.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                user.setLastName(last.toString());
            }
        }
//        user.setUserCode(profileDataBean.getEmployeeCode());
        if (isCreate) {
            user.setCompany(hkLoginDataBean.getCompanyId());
            user.setCreatedBy(hkLoginDataBean.getId());
            user.setCreatedOn(new Date());
            user.setIsActive(true);
            user.setIsArchive(false);
        }
        user.setModifiedOn(new Date());
        user.setModifiedBy(hkLoginDataBean.getId());
        user.setPreferredLanguage(superadmindetails.get("prefferedLang").toString());
        return user;
    }

    /**
     * @author mansi
     * @param employeeDataBean
     * @param userContact
     * @return
     * @throws UMUserManagementException
     */
    public UMUserContact convertEmpDataBeanToUserContact(EmployeeDataBean employeeDataBean, UMUserContact userContact, boolean isCreate) throws UMUserManagementException {
        if (userContact == null) {
            userContact = new UMUserContact();
        }
        userContact.setDateOfBirth(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getDob(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setGender(employeeDataBean.getGender());
        userContact.setBloodGroup(employeeDataBean.getBloodGrp().toString());
        userContact.setMaritalStatus(employeeDataBean.getMaritalstatus().toString());
        userContact.setCaste(employeeDataBean.getCaste().toString());
        userContact.setEmailAddress(employeeDataBean.getWorkemailId());
        userContact.setIdCardNumber(employeeDataBean.getEmpIdCardNum());
        userContact.setAdhaarNumber(employeeDataBean.getEmpAdhaarCardNum());
        userContact.setPanNumber(employeeDataBean.getEmpPanNum());
        userContact.setAccountNumber(employeeDataBean.getEmpAccountNum());
        userContact.setPassportNumber(employeeDataBean.getEmpPassportNum());
        userContact.setPassportIssuedOn(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getEmpPassportIssueOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setPassportExpiresOn(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getEmpPassportExpiresOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setNationality(employeeDataBean.getNationality().toString());
        userContact.setLicenseNumber(employeeDataBean.getEmpLicenceNum());
        userContact.setVehicleNumber(employeeDataBean.getEmpVehicalNum());
        userContact.setPucExpiresOn(HkSystemFunctionUtil.convertToServerDate(employeeDataBean.getEmpPucExpiresOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setMobileNumber(employeeDataBean.getPhnno());
        userContact.setAlternateContactNumber(employeeDataBean.getAltphnno());
        if (employeeDataBean.getEmpName() != null) {
            String[] split = employeeDataBean.getEmpName().split(" ");
            userContact.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                userContact.setLastName(split[1]);
            } else if (split.length > 2) {
                userContact.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                userContact.setLastName(last.toString());
            }
        }
        userContact.setCustom1(employeeDataBean.getEmpType());
        userContact.setCustom2(employeeDataBean.getOtherDetailsOfEmployeeKeysString());
        if (isCreate) {
            userContact.setCreatedOn(new Date());
            userContact.setCreatedBy(hkLoginDataBean.getId());
            userContact.setCompany(hkLoginDataBean.getCompanyId());
        }
        userContact.setModifiedBy(hkLoginDataBean.getId());
        userContact.setModifiedOn(new Date());
        return userContact;
    }

    public UMUserContact convertProfileDataBeanToUserContact(ProfileDataBean profileDataBean, UMUserContact userContact, boolean isCreate) throws UMUserManagementException {
        if (userContact == null) {
            userContact = new UMUserContact();
        }
        userContact.setDateOfBirth(HkSystemFunctionUtil.convertToServerDate(profileDataBean.getDob(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setGender(profileDataBean.getGender());
        userContact.setBloodGroup(profileDataBean.getBloodGrp().toString());
        userContact.setMaritalStatus(profileDataBean.getMaritalstatus().toString());
        userContact.setCaste(profileDataBean.getCaste().toString());
        userContact.setNationality(profileDataBean.getNationality().toString());
        userContact.setMobileNumber(profileDataBean.getPhnno());
        userContact.setAlternateContactNumber(profileDataBean.getAltphnno());
//        userContact.setEmailAddress(profileDataBean.getWorkemailId());

        if (profileDataBean.getEmpName() != null) {
            String[] split = profileDataBean.getEmpName().split(" ");
            userContact.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                userContact.setLastName(split[1]);
            } else if (split.length > 2) {
                userContact.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                userContact.setLastName(last.toString());
            }
        }

        if (isCreate) {
            userContact.setCreatedOn(new Date());
            userContact.setCreatedBy(hkLoginDataBean.getId());
            userContact.setCompany(hkLoginDataBean.getCompanyId());
        }
        userContact.setModifiedBy(hkLoginDataBean.getId());
        userContact.setModifiedOn(new Date());
        return userContact;
    }

    public EmployeeDataBean convertUserContactToEmployeeDatabean(UMUserContact userConact, EmployeeDataBean employeeDataBean) throws UMUserManagementException {
        if (employeeDataBean == null) {
            employeeDataBean = new EmployeeDataBean();
        }
        employeeDataBean.setDob(HkSystemFunctionUtil.convertToClientDate(userConact.getDateOfBirth(), hkLoginDataBean.getClientRawOffsetInMin()));
        employeeDataBean.setGender(userConact.getGender());
        if (userConact.getBloodGroup() != null) {
            employeeDataBean.setBloodGrp(new Long(userConact.getBloodGroup()));
        }
        if (userConact.getMaritalStatus() != null) {
            employeeDataBean.setMaritalstatus(new Long(userConact.getMaritalStatus()));
        }
        if (userConact.getCaste() != null) {
            employeeDataBean.setCaste(new Long(userConact.getCaste()));
        }
        employeeDataBean.setWorkemailId(userConact.getEmailAddress());
        employeeDataBean.setEmpIdCardNum(userConact.getIdCardNumber());
        employeeDataBean.setEmpAdhaarCardNum(userConact.getAdhaarNumber());
        employeeDataBean.setEmpPanNum(userConact.getPanNumber());
        employeeDataBean.setEmpAccountNum(userConact.getAccountNumber());
        employeeDataBean.setEmpPassportNum(userConact.getPassportNumber());
        employeeDataBean.setEmpPassportIssueOn(HkSystemFunctionUtil.convertToClientDate(userConact.getPassportIssuedOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        employeeDataBean.setEmpPassportExpiresOn(userConact.getPassportExpiresOn());
        if (userConact.getNationality() != null) {
            employeeDataBean.setNationality(new Long(userConact.getNationality()));
        }
        employeeDataBean.setEmpLicenceNum(userConact.getLicenseNumber());
        employeeDataBean.setEmpVehicalNum(userConact.getVehicleNumber());
        employeeDataBean.setEmpPucExpiresOn(HkSystemFunctionUtil.convertToClientDate(userConact.getPucExpiresOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        employeeDataBean.setPhnno(userConact.getMobileNumber());
        employeeDataBean.setAltphnno(userConact.getAlternateContactNumber());
        employeeDataBean.setEmpType(userConact.getCustom1());
        employeeDataBean.setOtherDetailsOfEmployeeKeysString(userConact.getCustom2());
        return employeeDataBean;
    }

    public UMContactAddress convertEmpDataBeanToContactAddress(EmployeeDataBean employeeDataBean, UMContactAddress contactAddress, boolean isNative) throws UMUserManagementException {
        if (contactAddress == null) {
            contactAddress = new UMContactAddress();
        }

        if (!isNative) {
            if (employeeDataBean.getCurrentAddressId() != null) {
                contactAddress.setId(employeeDataBean.getCurrentAddressId());
            }
            String currentaddress = employeeDataBean.getCurrentaddress();
            String[] split = currentaddress.split("#");
            contactAddress.setCity(split[0]);
            contactAddress.setDistrict(split[1]);
            contactAddress.setState(split[2]);
            contactAddress.setCustom3(split[3]);

            contactAddress.setAddressLine1(employeeDataBean.getFulladdress());
//            contactAddress.setAddressLine2(employeeDataBean.getFulladdress());
            contactAddress.setZipCode(employeeDataBean.getPincode());
        } else {
            if (employeeDataBean.getNativeAddressId() != null) {
                contactAddress.setId(employeeDataBean.getNativeAddressId());
            }
            String nativeaddress = employeeDataBean.getNativeaddress();
            String[] split = nativeaddress.split("#");
            contactAddress.setCity(split[0]);
            contactAddress.setDistrict(split[1]);
            contactAddress.setState(split[2]);
            contactAddress.setCustom3(split[3]);

            contactAddress.setAddressLine1(employeeDataBean.getNativefulladdress());
//            contactAddress.setAddressLine2(employeeDataBean.getNativefulladdress());
            contactAddress.setZipCode(employeeDataBean.getNativepincode());
        }
        contactAddress.setLastModifiedBy(hkLoginDataBean.getId());
        contactAddress.setLastModifiedOn(new Date());
        return contactAddress;
    }

    public UMContactAddress convertProfileDataBeanToContactAddress(ProfileDataBean profileDataBean, UMContactAddress contactAddress, boolean isNative) throws UMUserManagementException {
        if (contactAddress == null) {
            contactAddress = new UMContactAddress();
        }

        if (!isNative) {
            if (profileDataBean.getCurrentAddressId() != null) {
                contactAddress.setId(profileDataBean.getCurrentAddressId());
            }
            String currentaddress = profileDataBean.getCurrentaddress();
            String[] split = currentaddress.split("#");
            contactAddress.setCity(split[0]);
            contactAddress.setDistrict(split[1]);
            contactAddress.setState(split[2]);
            contactAddress.setCustom3(split[3]);

            contactAddress.setAddressLine1(profileDataBean.getFulladdress());
//            contactAddress.setAddressLine2(employeeDataBean.getFulladdress());
            contactAddress.setZipCode(profileDataBean.getPincode());
        } else {
            if (profileDataBean.getNativeAddressId() != null) {
                contactAddress.setId(profileDataBean.getNativeAddressId());
            }
            String nativeaddress = profileDataBean.getNativeaddress();
            String[] split = nativeaddress.split("#");
            contactAddress.setCity(split[0]);
            contactAddress.setDistrict(split[1]);
            contactAddress.setState(split[2]);
            contactAddress.setAddressLine1(profileDataBean.getNativefulladdress());
//            contactAddress.setAddressLine2(employeeDataBean.getNativefulladdress());
            contactAddress.setZipCode(profileDataBean.getNativepincode());
        }
        contactAddress.setLastModifiedBy(hkLoginDataBean.getId());
        contactAddress.setLastModifiedOn(new Date());
        return contactAddress;
    }

    public EmployeeDataBean convertContactAddressToEmployeeDatabean(UMContactAddress contactAddress, EmployeeDataBean employeeDatabean) throws UMUserManagementException {
        if (employeeDatabean == null) {
            employeeDatabean = new EmployeeDataBean();
        }
        if (contactAddress.getAddressType().equalsIgnoreCase(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE)) {
            employeeDatabean.setCurrentAddressId(contactAddress.getId());
            employeeDatabean.setCurrentaddress(contactAddress.getCity() + "#" + contactAddress.getDistrict() + "#" + contactAddress.getState() + "#" + contactAddress.getCustom3());
            employeeDatabean.setFulladdress(contactAddress.getAddressLine1());
            employeeDatabean.setPincode(contactAddress.getZipCode());
        } else {
            employeeDatabean.setNativeAddressId(contactAddress.getId());
            employeeDatabean.setNativeaddress(contactAddress.getCity() + "#" + contactAddress.getDistrict() + "#" + contactAddress.getState() + "#" + contactAddress.getCustom3());
            employeeDatabean.setNativefulladdress(contactAddress.getAddressLine1());
            employeeDatabean.setNativepincode(contactAddress.getZipCode());
        }
        return employeeDatabean;
    }

    public UMUserIpAssociation convertEmpDataBeanToUserIpAssociation(EmployeeDataBean employeeDataBean, UMUserIpAssociation userIpAssociation) throws UMUserManagementException {
        if (userIpAssociation == null) {
            userIpAssociation = new UMUserIpAssociation();
        }
        userIpAssociation.setIpAddress(employeeDataBean.getIpaddress());
        userIpAssociation.setAccessGivenBy(hkLoginDataBean.getId());
        userIpAssociation.setAccessGivenOn(new Date());
        userIpAssociation.setIsActive(Boolean.TRUE);
        userIpAssociation.setLastModifiedBy(hkLoginDataBean.getId());
        userIpAssociation.setLastModifiedOn(new Date());
        return userIpAssociation;
    }

    public UMContactEducation convertEmpDataBeanToContactEducation(EmpEducationDetalDataBean eduDataBean, UMContactEducation contactEducation) throws UMUserManagementException {
        if (contactEducation == null) {
            contactEducation = new UMContactEducation();
        }
        if (eduDataBean.getId() != null) {
            contactEducation.setId(eduDataBean.getId());
        }
        contactEducation.setUniversity(eduDataBean.getUniversity().toString());
        contactEducation.setDegree(eduDataBean.getDegree().toString());
        contactEducation.setPercentage(new BigDecimal(eduDataBean.getEmpPercentage()));
        contactEducation.setYearOfPassing(new Integer(eduDataBean.getPassingYear().toString()));
        contactEducation.setMedium(eduDataBean.getMedium().toString());
        contactEducation.setLastModifiedBy(hkLoginDataBean.getId());
        contactEducation.setLastModifiedOn(new Date());
        return contactEducation;
    }

    public EmpEducationDetalDataBean convertContactEducationToEducationDatabean(UMContactEducation eduContact, EmpEducationDetalDataBean eduBean) throws UMUserManagementException {
        if (eduBean == null) {
            eduBean = new EmpEducationDetalDataBean();
        }
        eduBean.setId(new Long(eduContact.getId()));
        eduBean.setUniversity(new Long(eduContact.getUniversity()));
        eduBean.setDegree(new Long(eduContact.getDegree()));
        if (eduContact.getPercentage() != null) {
            eduBean.setEmpPercentage(eduContact.getPercentage().toBigInteger().toString());
        }
        eduBean.setPassingYear(new Long(eduContact.getYearOfPassing()));
        eduBean.setMedium(new Long(eduContact.getMedium()));
        return eduBean;
    }

    public UMContactExperience convertEmpExperienceDataBeanToContactExperience(EmpExperienceDataBean hKEmpExperienceDataBean, UMContactExperience contactExperience) throws UMUserManagementException {
        if (contactExperience == null) {
            contactExperience = new UMContactExperience();
        }
        if (hKEmpExperienceDataBean.getId() != null) {
            contactExperience.setId(hKEmpExperienceDataBean.getId());
        }
        contactExperience.setCompany(hKEmpExperienceDataBean.getCompany());
        contactExperience.setDesignation(hKEmpExperienceDataBean.getDesignation().toString());
        contactExperience.setSalary(hKEmpExperienceDataBean.getSalary());
        contactExperience.setStartedFrom(HkSystemFunctionUtil.convertToServerDate(hKEmpExperienceDataBean.getStartedFrom(), hkLoginDataBean.getClientRawOffsetInMin()));
        contactExperience.setWorkedTill(HkSystemFunctionUtil.convertToServerDate(hKEmpExperienceDataBean.getWorkedTill(), hkLoginDataBean.getClientRawOffsetInMin()));
        contactExperience.setReasonOfLeaving(hKEmpExperienceDataBean.getReasonOfLeaving());
        contactExperience.setRemarks(hKEmpExperienceDataBean.getRemarks());
        contactExperience.setLastModifiedBy(hkLoginDataBean.getId());
        contactExperience.setLastModifiedOn(new Date());
        return contactExperience;
    }

    public EmpExperienceDataBean convertContactExperienceToExperienceDataBean(UMContactExperience contactExp, EmpExperienceDataBean expBean) throws UMUserManagementException {
        if (expBean == null) {
            expBean = new EmpExperienceDataBean();
        }
        expBean.setId(contactExp.getId());
        expBean.setCompany(contactExp.getCompany());
        expBean.setEmploymentType(new Long(contactExp.getEmploymentType()));
        expBean.setDesignation(new Long(contactExp.getDesignation()));
        expBean.setSalary(contactExp.getSalary());
        expBean.setStartedFrom(HkSystemFunctionUtil.convertToClientDate(contactExp.getStartedFrom(), hkLoginDataBean.getClientRawOffsetInMin()));
        expBean.setWorkedTill(HkSystemFunctionUtil.convertToClientDate(contactExp.getWorkedTill(), hkLoginDataBean.getClientRawOffsetInMin()));
        expBean.setSalaryslipImageName(contactExp.getSalarySlipFileName());
        expBean.setReasonOfLeaving(contactExp.getReasonOfLeaving());
        expBean.setRemarks(contactExp.getRemarks());
        return expBean;
    }

    public UMUserContact convertEmpFamilyDetailDataBeanToUserFamilyContact(EmpFamilyDetailDataBean hKEmpFamilyDetailsDataBean, UMUserContact userContact) {
        if (userContact == null) {
            userContact = new UMUserContact();
        }
        if (userContact.getCreatedOn() == null) {
            userContact.setCreatedBy(hkLoginDataBean.getId());
            userContact.setCreatedOn(new Date());
        }
        if (hKEmpFamilyDetailsDataBean.getFirstName() != null) {
            String[] split = hKEmpFamilyDetailsDataBean.getFirstName().split(" ");
            userContact.setFirstName(split[0]);
            if (split.length > 1 && split.length == 2) {
                userContact.setLastName(split[1]);
            } else if (split.length > 2) {
                userContact.setMiddleName(split[1]);
                StringBuilder last = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    if (last.toString().length() > 0) {
                        last.append(" ");
                    }
                    last.append(split[i]);
                }
                userContact.setLastName(last.toString());
            }
        }
        userContact.setDateOfBirth(HkSystemFunctionUtil.convertToServerDate(hKEmpFamilyDetailsDataBean.getDateOfBirth(), hkLoginDataBean.getClientRawOffsetInMin()));
        userContact.setBloodGroup(hKEmpFamilyDetailsDataBean.getBloodGroup().toString());
        userContact.setOccupation(hKEmpFamilyDetailsDataBean.getOccupation().toString());
        userContact.setRelation(hKEmpFamilyDetailsDataBean.getRelation().toString());
        userContact.setMobileNumber(hKEmpFamilyDetailsDataBean.getMobileNumber());
        userContact.setModifiedBy(hkLoginDataBean.getId());
        userContact.setModifiedOn(new Date());
        userContact.setIsArchive(false);
        userContact.setIsActive(Boolean.TRUE);
        return userContact;
    }

    public EmpFamilyDetailDataBean convertContactFamilyToFamilyDatabean(UMUserContact contactFamily, EmpFamilyDetailDataBean familyBean) {
        if (familyBean == null) {
            familyBean = new EmpFamilyDetailDataBean();
        }
        familyBean.setId(contactFamily.getId());
        StringBuilder name = new StringBuilder();
        if (contactFamily.getFirstName() != null) {
            name.append(contactFamily.getFirstName());
        }
        if (contactFamily.getMiddleName() != null) {
            if (name.toString().length() > 0) {
                name.append(" ");
            }
            name.append(contactFamily.getMiddleName());
        }
        if (contactFamily.getLastName() != null) {
            if (name.toString().length() > 0) {
                name.append(" ");
            }
            name.append(contactFamily.getLastName());
        }
        familyBean.setFirstName(name.toString());
        familyBean.setDateOfBirth(HkSystemFunctionUtil.convertToClientDate(contactFamily.getDateOfBirth(), hkLoginDataBean.getClientRawOffsetInMin()));
        familyBean.setBloodGroup(new Long(contactFamily.getBloodGroup()));
        familyBean.setOccupation(new Long(contactFamily.getOccupation()));
        familyBean.setRelation(new Long(contactFamily.getRelation()));
        familyBean.setMobileNumber(contactFamily.getMobileNumber());
        return familyBean;
    }

    public UMContactInsurance convertEmpPolicyDetailDataBeanToContactInsurance(EmpPolicyDetailDataBean policyDetailDataBean, UMContactInsurance uMContactInsurance) {
        if (uMContactInsurance == null) {
            uMContactInsurance = new UMContactInsurance();
        } else if (policyDetailDataBean.getId() != null) {
            uMContactInsurance.setId(policyDetailDataBean.getId());
        }
        uMContactInsurance.setCompany(policyDetailDataBean.getCompany().toString());
        uMContactInsurance.setPolicyNumber(policyDetailDataBean.getPolicyNumber());
        uMContactInsurance.setPolicyName(policyDetailDataBean.getPolicyName());
        uMContactInsurance.setStatus(policyDetailDataBean.getStatus());
        uMContactInsurance.setLastModifiedBy(hkLoginDataBean.getId());
        uMContactInsurance.setLastModifiedOn(new Date());
        uMContactInsurance.setIsArchive(Boolean.FALSE);
        return uMContactInsurance;
    }

    public EmpPolicyDetailDataBean convertContactInsuranceToPolicyDatabean(UMContactInsurance contactInsurance, EmpPolicyDetailDataBean policyDatabean) {
        if (policyDatabean == null) {
            policyDatabean = new EmpPolicyDetailDataBean();
        }
        policyDatabean.setId(contactInsurance.getId());
        policyDatabean.setCompany(new Long(contactInsurance.getCompany()));
        policyDatabean.setPolicyNumber(contactInsurance.getPolicyNumber());
        policyDatabean.setPolicyName(contactInsurance.getPolicyName());
        policyDatabean.setStatus(contactInsurance.getStatus());
        return policyDatabean;
    }

    // dont pass archive records
    public EmployeeDataBean retrieveAllDetailsOfEmployeeById(Long id, Boolean isCustomRequired) throws UMUserManagementException, GenericDatabaseException {

        EmployeeDataBean employeeDataBean = new EmployeeDataBean();
        UMUser uMUser = userManagementServiceWrapper.retrieveUserById(id);
        Map<Long, Integer> precedenceMap = userManagementServiceWrapper.retrieveDesignationPrecedence();
        Set<UMUserRole> umUserRoleSets = uMUser.getUMUserRoleSet();

        List<Integer> precedenceList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUserRoleSets)) {
            for (UMUserRole uMUserRole : umUserRoleSets) {
                if (uMUserRole.getIsActive()) {
                    precedenceList.add(precedenceMap.get(uMUserRole.getuMUserRolePK().getRole()));
                }
            }
        }
        int minIndex = precedenceList.indexOf(Collections.min(precedenceList));
        Integer precedenceOfUser = precedenceList.get(minIndex);
        if (precedenceOfUser >= hkLoginDataBean.getPrecedence()) {
            employeeDataBean.setIsUserRoleHigher(true);
        } else {
            employeeDataBean.setIsUserRoleHigher(false);
        }
        if (uMUser != null) {
            //

            List<Map<Long, Map<String, Object>>> educationmap = null;
            List<Map<Long, Map<String, Object>>> experiencemap = null;
            List<Map<Long, Map<String, Object>>> familymap = null;
            List<Map<Long, Map<String, Object>>> policymap = null;
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            if (isCustomRequired) {
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(id, HkSystemConstantUtil.FeatureNameForCustomField.USER, hkLoginDataBean.getCompanyId());
                if (retrieveDocumentByInstanceId != null) {
                    List<Map<Long, Map<String, Object>>> personalmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.PERSONAL);
                    if (personalmap != null) {
                        for (Map<Long, Map<String, Object>> map : personalmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setPersonalCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> generalmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                    if (generalmap != null) {
                        for (Map<Long, Map<String, Object>> map : generalmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setGeneralCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> contactmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.CONTACT);
                    if (contactmap != null) {
                        for (Map<Long, Map<String, Object>> map : contactmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setContactCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> identificationmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.IDENTIFICATION);
                    if (identificationmap != null) {
                        for (Map<Long, Map<String, Object>> map : identificationmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setIdentificationCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> othermap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.OTHER);
                    if (othermap != null) {
                        for (Map<Long, Map<String, Object>> map : othermap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setOtherCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> hkgworkmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.HKGWORK);
                    if (hkgworkmap != null) {
                        for (Map<Long, Map<String, Object>> map : hkgworkmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setHkgworkCustom(res);
                        }
                    }
                    educationmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.EDUCATION);
                    experiencemap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.EXPERIENCE);
                    familymap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.FAMILY);
                    policymap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.POLICY);
                }
            }

            //
            if (uMUser.getCustom2() != null && uMUser.getCustom2().length() > 0) {
                List<String> reportsto = new ArrayList<>();
                reportsto.add(uMUser.getCustom2());
                Map<String, String> retrieveRecipientNames = userManagementServiceWrapper.retrieveRecipientNames(reportsto);
                String get = retrieveRecipientNames.get(uMUser.getCustom2());
                employeeDataBean.setReportsToName(get);
            }
            convertUMUserToEmployeeDataBean(employeeDataBean, uMUser);
            UMUserContact uMUserContact = uMUser.getContact();//new umusercontact should be from database
            convertUserContactToEmployeeDatabean(uMUserContact, employeeDataBean);

            Map<String, Long> salaryslipIds = new HashMap<>();
            List<String> docs = new ArrayList<>();
            List<Date> docsDate = new ArrayList<>();
            Map<String, Long> otherdocIds = new HashMap<>();
            Set<UMContactDocument> umContactDocumentSet = uMUserContact.getuMContactDocumentSet();
            if (!CollectionUtils.isEmpty(umContactDocumentSet)) {
                for (UMContactDocument contactDocument : umContactDocumentSet) {
                    if (!contactDocument.getIsArchive()) {
                        if (contactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                            employeeDataBean.setProfileImageId(contactDocument.getId());
                            employeeDataBean.setProfileImageName(contactDocument.getDocumentName());
                        } else if (contactDocument.getDocumentType().equalsIgnoreCase(OTHER)) {
                            otherdocIds.put(contactDocument.getDocumentName(), contactDocument.getId());
                            docs.add(contactDocument.getDocumentName());
                            docsDate.add(contactDocument.getCustom4());
                        } else {
                            salaryslipIds.put(contactDocument.getDocumentName(), contactDocument.getId());
                        }
                    }
                }
            }
            employeeDataBean.setOtherdocIds(otherdocIds);
            String[] doc = new String[docs.size()];
            docs.toArray(doc);
            employeeDataBean.setOtherdocs(doc);
            Date[] docdate = new Date[docsDate.size()];
            docsDate.toArray(docdate);
            employeeDataBean.setOtherdocsDate(docdate);

            if (!CollectionUtils.isEmpty(uMUser.getuMUserIpAssociationSet())) {
                for (UMUserIpAssociation userIpAssociation : uMUser.getuMUserIpAssociationSet()) {
                    if (userIpAssociation.getIsActive()) {
                        employeeDataBean.setIpaddress(userIpAssociation.getIpAddress());
                        break;
                    }
                }
            }
            Set<UMContactAddress> setContactAddress = uMUserContact.getuMContactAddressSet();
            Map<String, UMContactAddress> mapContactAddress = new HashMap<>();
            for (UMContactAddress contactAddress : setContactAddress) {
                if (!contactAddress.getIsArchive()) {
                    mapContactAddress.put(contactAddress.getAddressType(), contactAddress);
                }
            }
            if (!mapContactAddress.isEmpty()) {
                UMContactAddress uMContactAddressCurrent = mapContactAddress.get(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE);
                if (uMContactAddressCurrent != null) {
                    convertContactAddressToEmployeeDatabean(uMContactAddressCurrent, employeeDataBean);
                }

                UMContactAddress uMContactAddressNative = mapContactAddress.get(HkSystemConstantUtil.NATIVE_ADDRESS_TYPE);
                employeeDataBean.setIsNativeAddressSame(true);

                if (uMContactAddressNative != null) {
                    employeeDataBean.setIsNativeAddressSame(false);
                    convertContactAddressToEmployeeDatabean(uMContactAddressNative, employeeDataBean);
                }
            } else {
                employeeDataBean.setIsNativeAddressSame(true);
            }
            List<EmpEducationDetalDataBean> educationDetalDataBeans = new ArrayList<>();
            for (UMContactEducation contactEducation : uMUserContact.getuMContactEducationSet()) {
                if (!contactEducation.getIsArchive()) {
                    EmpEducationDetalDataBean dataBean = new EmpEducationDetalDataBean();
                    convertContactEducationToEducationDatabean(contactEducation, dataBean);
                    if (educationmap != null) {
                        for (Map<Long, Map<String, Object>> edumap : educationmap) {
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> get = edumap.get(contactEducation.getId());
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                dataBean.setEducationCustom(get);
                                break;
                            }
                        }
                    }
                    educationDetalDataBeans.add(dataBean);
                }
            }
            employeeDataBean.setEdu(educationDetalDataBeans);

            Set<UMUserContact> listUMUserContact = uMUser.getUMUserContactSet();
            //from umContactInsurance where usercontactId=thisContactId
            List<UMContactInsurance> listContactInsurance = new ArrayList<>();
            List<EmpFamilyDetailDataBean> listEmpFamilyDetailDataBean = new ArrayList<>();
            Map<Long, String> familyId = new HashMap<>();
            int i = 0;
            for (UMUserContact usrContact : listUMUserContact) {
                if (!usrContact.getIsArchive()) {
                    familyId.put(usrContact.getId(), "F" + i);
                    EmpFamilyDetailDataBean hKEmpFamilyDetailsDataBean = new EmpFamilyDetailDataBean();
                    convertContactFamilyToFamilyDatabean(usrContact, hKEmpFamilyDetailsDataBean);
                    if (familymap != null) {
                        for (Map<Long, Map<String, Object>> map : familymap) {
                            Map<String, Object> get = map.get(usrContact.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                hKEmpFamilyDetailsDataBean.setFamilyCustom(get);
                                break;
                            }
                        }
                    }
//                    hKEmpFamilyDetailsDataBean.setIndex("F" + i);
                    hKEmpFamilyDetailsDataBean.setIndex(String.valueOf(i));
                    listEmpFamilyDetailDataBean.add(hKEmpFamilyDetailsDataBean);

                    Set<UMContactDocument> familyContactDocumentSet = usrContact.getuMContactDocumentSet();
                    if (!CollectionUtils.isEmpty(familyContactDocumentSet)) {
                        for (UMContactDocument uMContactDocument : familyContactDocumentSet) {
                            if (!uMContactDocument.getIsArchive()) {
                                if (uMContactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                                    hKEmpFamilyDetailsDataBean.setFamilyImageId(uMContactDocument.getId());
                                    hKEmpFamilyDetailsDataBean.setFamilyImageName(uMContactDocument.getDocumentName());
                                    break;
                                }
                            }
                        }
                    }

                    //add all policy details in listUMContactInsurance
                    listContactInsurance.addAll(usrContact.getuMContactInsuranceSet());
                    i++;
                }
            }
            employeeDataBean.setFamily(listEmpFamilyDetailDataBean);
            int policycounter = 0;
            List<EmpPolicyDetailDataBean> listEmpPolicyDetailDataBean = new ArrayList<>();
            for (UMContactInsurance contactInsurance : listContactInsurance) {
                if (!contactInsurance.getIsArchive()) {
                    EmpPolicyDetailDataBean policyDetailDataBean = new EmpPolicyDetailDataBean();
                    convertContactInsuranceToPolicyDatabean(contactInsurance, policyDetailDataBean);
                    if (policymap != null) {
                        for (Map<Long, Map<String, Object>> map : policymap) {
                            Map<String, Object> get = map.get(contactInsurance.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                policyDetailDataBean.setPolicyCustom(get);
                                break;
                            }
                        }
                    }
                    policyDetailDataBean.setIndex(String.valueOf(policycounter));
                    policycounter++;
                    String get = familyId.get(contactInsurance.getUserContact().getId());
                    policyDetailDataBean.setContactUser(get);
                    listEmpPolicyDetailDataBean.add(policyDetailDataBean);
                }
            }
            Set<UMContactInsurance> umContactInsuranceSet = uMUserContact.getuMContactInsuranceSet();
            if (!CollectionUtils.isEmpty(umContactInsuranceSet)) {
                for (UMContactInsurance contactInsurance : umContactInsuranceSet) {
                    if (!contactInsurance.getIsArchive()) {
                        EmpPolicyDetailDataBean policyDetailDataBean = new EmpPolicyDetailDataBean();
                        convertContactInsuranceToPolicyDatabean(contactInsurance, policyDetailDataBean);
                        if (policymap != null) {
                            for (Map<Long, Map<String, Object>> map : policymap) {
                                Map<String, Object> get = map.get(contactInsurance.getId());
                                Map<String, String> uiFieldMap = null;
                                Map<String, Object> res = new HashMap<>();
                                uiFieldMap = this.createFieldNameWithComponentType(get);
                                if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                    for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                        if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                            if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                                String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                                get.put(field.getKey(), value);
                                            }
                                        }
                                    }
                                    res.putAll(get);
                                }
                                if (!CollectionUtils.isEmpty(res)) {
                                    policyDetailDataBean.setPolicyCustom(get);
                                    break;
                                }
                            }
                        }
                        policyDetailDataBean.setIndex(String.valueOf(policycounter));
                        policycounter++;
                        policyDetailDataBean.setContactUser("E0");
                        listEmpPolicyDetailDataBean.add(policyDetailDataBean);
                    }
                }
            }
            employeeDataBean.setPolicy(listEmpPolicyDetailDataBean);

            Set<UMUserIpAssociation> umUserIpAssociationSet = uMUser.getuMUserIpAssociationSet();
            if (!CollectionUtils.isEmpty(umUserIpAssociationSet)) {
                for (UMUserIpAssociation ipAssociation : umUserIpAssociationSet) {
                    if (ipAssociation.getIsActive()) {
                        employeeDataBean.setIpaddress(ipAssociation.getIpAddress());
                        break;
                    }
                }
            }

            List<UMContactExperience> listUMContactExperience = new ArrayList<>(uMUserContact.getuMContactExperienceSet());
            List<EmpExperienceDataBean> listEmpExperienceDataBean = new ArrayList<>();

            for (UMContactExperience contactExperience : listUMContactExperience) {
                if (!contactExperience.getIsArchive()) {
                    EmpExperienceDataBean hKEmpExperienceDataBean = new EmpExperienceDataBean();
                    convertContactExperienceToExperienceDataBean(contactExperience, hKEmpExperienceDataBean);
                    if (hKEmpExperienceDataBean.getSalaryslipImageName() != null) {
                        Long get = salaryslipIds.get(hKEmpExperienceDataBean.getSalaryslipImageName());
                        hKEmpExperienceDataBean.setSalaryslipId(get);
                    }
                    if (experiencemap != null) {
                        for (Map<Long, Map<String, Object>> expmap : experiencemap) {
                            Map<String, Object> get = expmap.get(contactExperience.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                hKEmpExperienceDataBean.setExperienceCustom(get);
                                break;
                            }
                        }
                    }
                    listEmpExperienceDataBean.add(hKEmpExperienceDataBean);
                }
            }
            employeeDataBean.setExp(listEmpExperienceDataBean);

            Set<UMUserRole> umUserRoleSet = uMUser.getUMUserRoleSet();
            if (!CollectionUtils.isEmpty(umUserRoleSet)) {
                List<Long> roleIds = new ArrayList<>();
                for (UMUserRole uMUserRole : umUserRoleSet) {
                    if (!uMUserRole.getIsArchive()) {
                        roleIds.add(uMUserRole.getuMUserRolePK().getRole());
                    }
                }
                Long[] roles = new Long[roleIds.size()];
                roleIds.toArray(roles);
                employeeDataBean.setWorkdeg(roles);
            }
        }

        return employeeDataBean;
    }

    public EmployeeDataBean convertUMUserToEmployeeDataBean(EmployeeDataBean employeeDataBean, UMUser uMUser) {
        if (employeeDataBean == null) {
            employeeDataBean = new EmployeeDataBean();
        }

        employeeDataBean.setId(uMUser.getId());
        employeeDataBean.setUserId(uMUser.getUserId());
        employeeDataBean.setCompanyId(hkLoginDataBean.getCompanyId());
        employeeDataBean.setPreviousfranchiseId(uMUser.getCustom1());
        employeeDataBean.setReportsToId(uMUser.getCustom2());
        employeeDataBean.setEmployeeCode(uMUser.getUserCode());
        employeeDataBean.setDepartmentId(uMUser.getDepartment());
        StringBuilder name = new StringBuilder();
        if (uMUser.getFirstName() != null) {
            name.append(uMUser.getFirstName());
        }
        if (uMUser.getMiddleName() != null) {
            if (name.toString().length() > 0) {
                name.append(" ");
            }
            name.append(uMUser.getMiddleName());
        }
        if (uMUser.getLastName() != null) {
            if (name.toString().length() > 0) {
                name.append(" ");
            }
            name.append(uMUser.getLastName());
        }
        employeeDataBean.setEmpName(name.toString());
        if (uMUser.getStatus() != null) {
            employeeDataBean.setWorkstatus(new Long(uMUser.getStatus()));
        }
        employeeDataBean.setRelievingDate(HkSystemFunctionUtil.convertToClientDate(uMUser.getExpiredOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        employeeDataBean.setSelecteddep(uMUser.getDepartment());
        employeeDataBean.setWorkshift(uMUser.getCustom4());
        employeeDataBean.setEmail(uMUser.getEmailAddress());
        employeeDataBean.setJoiningDate(HkSystemFunctionUtil.convertToClientDate(uMUser.getJoiningDate(), hkLoginDataBean.getClientRawOffsetInMin()));
        if (StringUtils.hasText(uMUser.getPreferredLanguage())) {
            employeeDataBean.setPrefferedLang(uMUser.getPreferredLanguage());
        }
        return employeeDataBean;
    }

    public Map<String, List<SelectItem>> retrieveComboValuesOfKeyCodes(List<String> keyCodesList) {
        Map<String, List<SelectItem>> responseMap = null;
        List<HkValueEntity> valueEntitys = foundationService.retrieveMasterValuesByCode(hkLoginDataBean.getCompanyId(), keyCodesList);
        if (!CollectionUtils.isEmpty(valueEntitys)) {
            responseMap = new LinkedHashMap<>();
            for (HkValueEntity hkValueEntity : valueEntitys) {
                String keycode = hkValueEntity.getKeyCode().getCode();
                String valueName = hkValueEntity.getValueName();
                if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                    valueName = hkValueEntity.getTranslatedValueName();
                }
                if (responseMap.containsKey(keycode)) {
                    if (hkValueEntity.getShortcutCode() != null) {
                        responseMap.get(keycode).add(new SelectItem(hkValueEntity.getId(), valueName, hkValueEntity.getShortcutCode()));
                    } else {
                        responseMap.get(keycode).add(new SelectItem(hkValueEntity.getId(), valueName, 0));
                    }
                } else {
                    List<SelectItem> valuesOfKey = new ArrayList<>();
                    if (hkValueEntity.getShortcutCode() != null) {
                        valuesOfKey.add(new SelectItem(hkValueEntity.getId(), valueName, hkValueEntity.getShortcutCode()));
                    } else {
                        valuesOfKey.add(new SelectItem(hkValueEntity.getId(), valueName, 0));
                    }
                    responseMap.put(keycode, valuesOfKey);

                }
            }
        }

        return responseMap;
    }

    public List<SelectItem> retrieveShiftByDepartment(Long id) {
        List<SelectItem> shifts = new ArrayList<>();
        Map<Long, String> retrieveShiftsByDepartment = hrService.retrieveShiftsByDepartment(hkLoginDataBean.getCompanyId(), id);
        for (Map.Entry<Long, String> entry : retrieveShiftsByDepartment.entrySet()) {
            SelectItem item = new SelectItem(entry.getKey(), entry.getValue());
            shifts.add(item);
        }
        return shifts;
    }

    public List<EmployeeDataBean> searchUser(String user, Long franchiseId, Long status,Boolean isActive) {
        List<EmployeeDataBean> employeeDataBeans = new ArrayList<>();
        Map<Long, String> searchUsers = userManagementServiceWrapper.searchUsers(user, franchiseId, hkLoginDataBean.getPrecedence(), status,isActive);
        if (!CollectionUtils.isEmpty(searchUsers)) {
            for (Map.Entry<Long, String> entry : searchUsers.entrySet()) {
                Long long1 = entry.getKey();
                String string = entry.getValue();
                String[] split = string.split("-");
                EmployeeDataBean employeeDataBean = new EmployeeDataBean();
                employeeDataBean.setId(long1);
                employeeDataBean.setEmployeeCode(split[0]);
                employeeDataBean.setUserId(split[1]);
                employeeDataBean.setDepartment(split[split.length - 1]);
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < split.length - 1; i++) {
                    String string1 = split[i];
                    if (name.toString().length() > 0) {
                        name.append(" - ");
                    }
                    name.append(string1);
                }
                employeeDataBean.setEmpName(name.toString());
                employeeDataBeans.add(employeeDataBean);
            }
        }
        return employeeDataBeans;
    }

    public String terminateEmployee(Long id, Date relievingDate, String status) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            UMUser retrieveUserById = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
            retrieveUserById.setStatus(status);
            retrieveUserById.setExpiredOn(HkSystemFunctionUtil.convertToServerDate(relievingDate, hkLoginDataBean.getClientRawOffsetInMin()));
            userService.updateUser(retrieveUserById, false, false, false, false);
            response = HkSystemConstantUtil.SUCCESS;
        } catch (UMUserManagementException ex) {
            Logger.getLogger(EmployeeTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public String uploadFile(String fileName, String fileType) throws FileNotFoundException, IOException {
        String tempFileName = null;
        if (fileType.equals(PROFILE) || fileType.equals(SALARYSLIP) || fileType.equals(OTHER) || fileType.equals(BACKGROUND)) {
            tempFileName = FolderManagement.getTempFileName(hkLoginDataBean.getCompanyId(), FolderManagement.FEATURE.USER, fileType, hkLoginDataBean.getId(), fileName);
        }
        return tempFileName;
    }

    public List<SelectItem> fillEmployeeStatusList() {
        List<SelectItem> statusList = new ArrayList<>();
        statusList.add(new SelectItem(HkSystemConstantUtil.EMPLOYEE_STATUS.SELECTED, HkSystemConstantUtil.EMPLOYEE_STATUS_MAP.get(HkSystemConstantUtil.EMPLOYEE_STATUS.SELECTED)));
        statusList.add(new SelectItem(HkSystemConstantUtil.EMPLOYEE_STATUS.AWAITING, HkSystemConstantUtil.EMPLOYEE_STATUS_MAP.get(HkSystemConstantUtil.EMPLOYEE_STATUS.AWAITING)));
        statusList.add(new SelectItem(HkSystemConstantUtil.EMPLOYEE_STATUS.RESIGNED, HkSystemConstantUtil.EMPLOYEE_STATUS_MAP.get(HkSystemConstantUtil.EMPLOYEE_STATUS.RESIGNED)));
        statusList.add(new SelectItem(HkSystemConstantUtil.EMPLOYEE_STATUS.TRANSFERRED, HkSystemConstantUtil.EMPLOYEE_STATUS_MAP.get(HkSystemConstantUtil.EMPLOYEE_STATUS.TRANSFERRED)));
        return statusList;
    }

    public Boolean doesEmployeeNameExist(String userName, Long companyId) throws GenericDatabaseException {
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMUserDao.COMPANY, companyId);
        equal.put(UMUserDao.IS_ACTIVE, true);
        List<String> projections = new ArrayList<>();
        projections.add(UMUserDao.FIRST_NAME);
        projections.add(UMUserDao.MIDDLE_NAME);
        projections.add(UMUserDao.LAST_NAME);
        projections.add(UMUserDao.ID);
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
        Map<Long, String> EmployeeDetailMap = new HashMap();
        for (UMUser user : users) {
            StringBuilder employeefullname = new StringBuilder();
            if (user.getFirstName() != null) {
                employeefullname.append(user.getFirstName());
            }
            if (user.getMiddleName() != null) {
                employeefullname.append(" ").append(user.getMiddleName());
            }
            if (user.getLastName() != null) {
                employeefullname.append(" ").append(user.getLastName());
            }
            EmployeeDetailMap.put(user.getId(), employeefullname.toString().toLowerCase());
        }
        if (EmployeeDetailMap.containsValue(userName.toLowerCase())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Map<Long, String> retrieveEmployeeCode() {
        List<SelectItem> retrieveEmployeeTypes = franchiseConfigrationTransformerBean.retrieveEmployeeTypes();
        Map<Long, String> typeCode = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveEmployeeTypes)) {
            for (SelectItem selectItem : retrieveEmployeeTypes) {
                typeCode.put(new Long(selectItem.getValue().toString()), selectItem.getLabel() + "01");
            }
        }
        return typeCode;
    }

    public String retrieveAvatar() {
        String path = null;
        HkSystemConfigurationEntity retrieveSystemConfigurationByKey = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_DEFAULT_IMAGE, hkLoginDataBean.getCompanyId());
        if (retrieveSystemConfigurationByKey != null) {
            path = FolderManagement.getPathOfImage(retrieveSystemConfigurationByKey.getKeyValue());
        }
        return path;
    }

    public Map<String, String> retrieveDOBConfiguration() {
        Map<String, String> map = new HashMap<>();
        HkSystemConfigurationEntity maxAge = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_MAX_AGE, hkLoginDataBean.getCompanyId());
        HkSystemConfigurationEntity minAge = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.EMP_MIN_AGE, hkLoginDataBean.getCompanyId());
        String maximumAge, minimumAge;
        if (maxAge != null) {
            maximumAge = maxAge.getKeyValue();
        } else {
            maximumAge = "55";
        }
        map.put("maxAge", maximumAge);
        if (minAge != null) {
            minimumAge = minAge.getKeyValue();
        } else {
            minimumAge = "18";
        }

        map.put("minAge", minimumAge);
        return map;
    }

    public Map<String, String> retrieveEmployeeConfiguration() {
        String key;
        Map<String, String> map = new HashMap<>();
        List<SelectItem> retrieveEmployeeTypes = franchiseConfigrationTransformerBean.retrieveEmployeeTypes();
        for (SelectItem emptype : retrieveEmployeeTypes) {
            key = "EMP_TYPE_" + emptype.getValue();
            HkSystemConfigurationEntity empTypeConfig = foundationService.retrieveSystemConfigurationByKey(key, hkLoginDataBean.getCompanyId());
            if (empTypeConfig != null) {
                map.put(key, empTypeConfig.getKeyValue());
            }
        }

        return map;
    }

    public String changePassword(Long id, Long company, String password, String userId) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            userManagementServiceWrapper.changePassword(id, company, password, userId);
            response = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
        }
        return response;
    }

    public String getProfileFullPath(Long id) {
        String path = null;

        UMUser loginEmp = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
        UMUserContact uMUserContact = loginEmp.getContact();
        Long imageId = null;
        String imageName = null;

        Set<UMContactDocument> umContactDocumentSet = uMUserContact.getuMContactDocumentSet();
        if (!CollectionUtils.isEmpty(umContactDocumentSet)) {
            for (UMContactDocument contactDocument : umContactDocumentSet) {
                if (!contactDocument.getIsArchive()) {
                    if (contactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                        imageId = contactDocument.getId();
                        imageName = contactDocument.getDocumentName();
                    }
                }
            }
        }
        if (imageName != null) {
            path = FolderManagement.getPathOfImage(imageName);
        }
        return path;
    }

    public String getBackground() throws GenericDatabaseException {
        List<String> requires = new ArrayList<>();
        requires.add(UMUserDao.CONTACT);
        UMUser user = userService.retrieveUserById(hkLoginDataBean.getId(), requires);
        String facebookPage = user.getContact().getFacebookPage();
        String background = null;
        if (facebookPage != null) {
            String[] split = facebookPage.split(",");
            for (String split1 : split) {
                if (split1.indexOf(FolderManagement.UNIQUE_SEPARATOR) != -1) {
                    background = split1;
                }
            }
        }
        return background;
    }

    public void setTheme(ThemeDataBean dataBean) throws GenericDatabaseException {
        userManagementServiceWrapper.setTheme(hkLoginDataBean.getId(), dataBean.getFolderName());
    }

    public List<ThemeDataBean> retrieveAllThemes(String status) {
        List<HkTheme> themes = foundationService.retrieveAllThemes(HkSystemConstantUtil.ACTIVE);
        List<ThemeDataBean> dataBeans = new ArrayList<>();
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(themes)) {
            for (HkTheme hkTheme : themes) {
                ThemeDataBean bean = new ThemeDataBean();
                dataBeans.add(convertThemeModelToThemeDatabean(hkTheme, bean));
            }
        }
        return dataBeans;
    }

    public ThemeDataBean convertThemeModelToThemeDatabean(HkTheme hkTheme, ThemeDataBean themeDataBean) {
        if (themeDataBean == null) {
            themeDataBean = new ThemeDataBean();
        }
        themeDataBean.setId(hkTheme.getId());
        themeDataBean.setThemeName(hkTheme.getThemeName());
        themeDataBean.setFolderName(hkTheme.getFolderName());
        themeDataBean.setStatus(hkTheme.getStatus());
        return themeDataBean;
    }

    public void createPhotoGallery(String file, Boolean store) throws IOException {
        UMUser user = userService.retrieveUserById(hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
        String facebookPage = user.getContact().getFacebookPage();
        if (facebookPage == null) {
            user.getContact().setFacebookPage(file);
        } else {
            String[] split = facebookPage.split(",");
            if (split[0].indexOf(FolderManagement.UNIQUE_SEPARATOR) == -1) {
                user.getContact().setFacebookPage(split[0] + "," + file);
            } else {
                user.getContact().setFacebookPage(file);
            }
        }
        userService.updateUser(user);
        if (store) {
            FolderManagement.saveFile(null, null, user.getId(), null, Arrays.asList(file), true);
        }
    }

    public Map<String, List<String>> retrieveImagePaths(Long userId) throws IOException {
        Map<String, List<String>> map = null;
        StringBuilder random = new StringBuilder();
        random.append(FolderManagement.FEATURE.USER).append(FolderManagement.UNIQUE_SEPARATOR).append(hkLoginDataBean.getCompanyId()).append(FolderManagement.UNIQUE_SEPARATOR).append(hkLoginDataBean.getId()).append(FolderManagement.UNIQUE_SEPARATOR).append(BACKGROUND);
        map = new LinkedHashMap<>();
        List<String> allFilesOfFolder = FolderManagement.getAllFilesOfFolder(random.toString(), Boolean.FALSE);
        if (!CollectionUtils.isEmpty(allFilesOfFolder)) {
            map.put("src", allFilesOfFolder);
        }
        return map;
    }

    public Map<String, String> retrieveImageThumbnailPaths(Long eventId) throws IOException {
        Map<String, String> map = null;
        StringBuilder random = new StringBuilder();
        random.append(FolderManagement.FEATURE.USER).append(FolderManagement.UNIQUE_SEPARATOR).append(hkLoginDataBean.getCompanyId()).append(FolderManagement.UNIQUE_SEPARATOR).append(hkLoginDataBean.getId()).append(FolderManagement.UNIQUE_SEPARATOR).append(BACKGROUND);
        map = new LinkedHashMap<>();
        List<String> allFilesOfFolderThumbnail = FolderManagement.getAllFilesOfFolder(random.toString(), Boolean.TRUE);
        List<String> allFilesOfFolder = FolderManagement.getAllFilesOfFolder(random.toString(), Boolean.FALSE);
        if (!CollectionUtils.isEmpty(allFilesOfFolderThumbnail)) {
            for (int i = 0; i < allFilesOfFolderThumbnail.size(); i++) {
                map.put(allFilesOfFolderThumbnail.get(i), allFilesOfFolder.get(i));
            }
        }
        return map;
    }

    public void initThemes() {
        List<HkTheme> themes = foundationService.retrieveAllThemes(null);
        if (CollectionUtils.isEmpty(themes)) {
            List<HkTheme> list = new ArrayList<>();
            HkTheme hkTheme = new HkTheme();
            hkTheme.setFolderName("default");
            hkTheme.setThemeName("Default");
            hkTheme.setStatus(HkSystemConstantUtil.ACTIVE);
            HkTheme hkTheme1 = new HkTheme();
            hkTheme1.setFolderName("rose");
            hkTheme1.setThemeName("Rose");
            hkTheme1.setStatus(HkSystemConstantUtil.ACTIVE);
            list.add(hkTheme);
            //list.add(hkTheme1);
            foundationService.createAllThemes(list);
        }
    }

    public Map<String, String> createFieldNameWithComponentType(Map<String, Object> fieldValues
    ) {
        Map<String, String> componentCodeMap = new HashMap<>(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP);
        Map<String, String> codeMap = null;
        if (componentCodeMap != null && !componentCodeMap.isEmpty()) {
            codeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : componentCodeMap.entrySet()) {
                codeMap.put(entry.getValue(), entry.getKey());

            }
        }
        Map<String, String> fieldWithComponentMap = new HashMap<>();
        if (fieldValues != null && !fieldValues.isEmpty()) {
            for (Map.Entry<String, Object> custom : fieldValues.entrySet()) {
                String[] split = custom.getKey().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                if (split != null && split.length >= 1) {
                    fieldWithComponentMap.put(custom.getKey(), codeMap.get(split[1]));
                }
            }
        }
        return fieldWithComponentMap;
    }

    public List<SelectItem> retrieveDesignationByDepartment(Long departmentId) {
        List<UMRole> listUMRole = userManagementServiceWrapper.retrieveDesignationsByDepartment(departmentId);
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {
            Collections.sort(listUMRole, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    UMRole role1 = (UMRole) o1;
                    UMRole role2 = (UMRole) o2;
                    return role1.getCreatedOn().compareTo(role2.getCreatedOn());
                }

            });
            for (UMRole umRole : listUMRole) {
                if (umRole.getName() != null) {
                    SelectItem selectItem = new SelectItem(umRole.getId(), umRole.getName());
                    selectItem.setDescription(umRole.getDescription());
                    if (!StringUtils.isEmpty(umRole.getCustom2())) {
                        selectItem.setOtherId(Long.parseLong(umRole.getCustom2()));
                    }
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }

    // dont pass archive records
    public EmployeeDataBean retrieveAllDetailsOfEmployeeByIdForTransfer(Long id, Boolean isCustomRequired) throws UMUserManagementException, GenericDatabaseException {

        EmployeeDataBean employeeDataBean = new EmployeeDataBean();
        UMUser uMUser = userManagementServiceWrapper.retrieveUserById(id);
        Map<Long, Integer> precedenceMap = userManagementServiceWrapper.retrieveDesignationPrecedence();
        Set<UMUserRole> umUserRoleSets = uMUser.getUMUserRoleSet();

        List<Integer> precedenceList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUserRoleSets)) {
            for (UMUserRole uMUserRole : umUserRoleSets) {
                precedenceList.add(precedenceMap.get(uMUserRole.getuMUserRolePK().getRole()));
            }
        }
        int minIndex = precedenceList.indexOf(Collections.min(precedenceList));
        Integer precedenceOfUser = precedenceList.get(minIndex);
        if (precedenceOfUser >= hkLoginDataBean.getPrecedence()) {
            employeeDataBean.setIsUserRoleHigher(true);
        } else {
            employeeDataBean.setIsUserRoleHigher(false);
        }
        if (uMUser != null) {
            //

            List<Map<Long, Map<String, Object>>> educationmap = null;
            List<Map<Long, Map<String, Object>>> experiencemap = null;
            List<Map<Long, Map<String, Object>>> familymap = null;
            List<Map<Long, Map<String, Object>>> policymap = null;
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
            if (isCustomRequired) {
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(id, HkSystemConstantUtil.FeatureNameForCustomField.USER, hkLoginDataBean.getCompanyId());
                if (retrieveDocumentByInstanceId != null) {
                    List<Map<Long, Map<String, Object>>> personalmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.PERSONAL);
                    if (personalmap != null) {
                        for (Map<Long, Map<String, Object>> map : personalmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setPersonalCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> generalmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                    if (generalmap != null) {
                        for (Map<Long, Map<String, Object>> map : generalmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setGeneralCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> contactmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.CONTACT);
                    if (contactmap != null) {
                        for (Map<Long, Map<String, Object>> map : contactmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setContactCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> identificationmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.IDENTIFICATION);
                    if (identificationmap != null) {
                        for (Map<Long, Map<String, Object>> map : identificationmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setIdentificationCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> othermap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.OTHER);
                    if (othermap != null) {
                        for (Map<Long, Map<String, Object>> map : othermap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setOtherCustom(res);
                        }
                    }
                    List<Map<Long, Map<String, Object>>> hkgworkmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.HKGWORK);
                    if (hkgworkmap != null) {
                        for (Map<Long, Map<String, Object>> map : hkgworkmap) {
                            Map<String, Object> get = map.get(uMUser.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            employeeDataBean.setHkgworkCustom(res);
                        }
                    }
                    educationmap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.EDUCATION);
                    experiencemap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.EXPERIENCE);
                    familymap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.FAMILY);
                    policymap = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.POLICY);
                }
            }

            //
            if (uMUser.getCustom2() != null && uMUser.getCustom2().length() > 0) {
                List<String> reportsto = new ArrayList<>();
                reportsto.add(uMUser.getCustom2());
                Map<String, String> retrieveRecipientNames = userManagementServiceWrapper.retrieveRecipientNames(reportsto);
                String get = retrieveRecipientNames.get(uMUser.getCustom2());
                employeeDataBean.setReportsToName(get);
            }
            convertUMUserToEmployeeDataBean(employeeDataBean, uMUser);
            UMUserContact uMUserContact = uMUser.getContact();//new umusercontact should be from database
            convertUserContactToEmployeeDatabean(uMUserContact, employeeDataBean);

            Map<String, Long> salaryslipIds = new HashMap<>();
            List<String> docs = new ArrayList<>();
            List<Date> docsDate = new ArrayList<>();
            Map<String, Long> otherdocIds = new HashMap<>();
            Set<UMContactDocument> umContactDocumentSet = uMUserContact.getuMContactDocumentSet();
            if (!CollectionUtils.isEmpty(umContactDocumentSet)) {
                for (UMContactDocument contactDocument : umContactDocumentSet) {
                    if (contactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                        employeeDataBean.setProfileImageId(contactDocument.getId());
                        employeeDataBean.setProfileImageName(contactDocument.getDocumentName());
                    } else if (contactDocument.getDocumentType().equalsIgnoreCase(OTHER)) {
                        otherdocIds.put(contactDocument.getDocumentName(), contactDocument.getId());
                        docs.add(contactDocument.getDocumentName());
                        docsDate.add(contactDocument.getCustom4());
                    } else {
                        salaryslipIds.put(contactDocument.getDocumentName(), contactDocument.getId());
                    }
                }
            }
            employeeDataBean.setOtherdocIds(otherdocIds);
            String[] doc = new String[docs.size()];
            docs.toArray(doc);
            employeeDataBean.setOtherdocs(doc);
            Date[] docdate = new Date[docsDate.size()];
            docsDate.toArray(docdate);
            employeeDataBean.setOtherdocsDate(docdate);

            if (!CollectionUtils.isEmpty(uMUser.getuMUserIpAssociationSet())) {
                for (UMUserIpAssociation userIpAssociation : uMUser.getuMUserIpAssociationSet()) {
                    employeeDataBean.setIpaddress(userIpAssociation.getIpAddress());
                    break;
                }
            }
            Set<UMContactAddress> setContactAddress = uMUserContact.getuMContactAddressSet();
            Map<String, UMContactAddress> mapContactAddress = new HashMap<>();
            for (UMContactAddress contactAddress : setContactAddress) {
                mapContactAddress.put(contactAddress.getAddressType(), contactAddress);
            }
            if (!mapContactAddress.isEmpty()) {
                UMContactAddress uMContactAddressCurrent = mapContactAddress.get(HkSystemConstantUtil.CURRENT_ADDRESS_TYPE);
                if (uMContactAddressCurrent != null) {
                    convertContactAddressToEmployeeDatabean(uMContactAddressCurrent, employeeDataBean);
                }

                UMContactAddress uMContactAddressNative = mapContactAddress.get(HkSystemConstantUtil.NATIVE_ADDRESS_TYPE);
                employeeDataBean.setIsNativeAddressSame(true);

                if (uMContactAddressNative != null) {
                    employeeDataBean.setIsNativeAddressSame(false);
                    convertContactAddressToEmployeeDatabean(uMContactAddressNative, employeeDataBean);
                }
            } else {
                employeeDataBean.setIsNativeAddressSame(true);
            }
            List<EmpEducationDetalDataBean> educationDetalDataBeans = new ArrayList<>();
            for (UMContactEducation contactEducation : uMUserContact.getuMContactEducationSet()) {
                if (!contactEducation.getIsArchive()) {
                    EmpEducationDetalDataBean dataBean = new EmpEducationDetalDataBean();
                    convertContactEducationToEducationDatabean(contactEducation, dataBean);
                    if (educationmap != null) {
                        for (Map<Long, Map<String, Object>> edumap : educationmap) {
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> get = edumap.get(contactEducation.getId());
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                dataBean.setEducationCustom(get);
                                break;
                            }
                        }
                    }
                    educationDetalDataBeans.add(dataBean);
                }
            }
            employeeDataBean.setEdu(educationDetalDataBeans);

            Set<UMUserContact> listUMUserContact = uMUser.getUMUserContactSet();
            //from umContactInsurance where usercontactId=thisContactId
            List<UMContactInsurance> listContactInsurance = new ArrayList<>();
            List<EmpFamilyDetailDataBean> listEmpFamilyDetailDataBean = new ArrayList<>();
            Map<Long, String> familyId = new HashMap<>();
            int i = 0;
            for (UMUserContact usrContact : listUMUserContact) {
                familyId.put(usrContact.getId(), "F" + i);
                EmpFamilyDetailDataBean hKEmpFamilyDetailsDataBean = new EmpFamilyDetailDataBean();
                convertContactFamilyToFamilyDatabean(usrContact, hKEmpFamilyDetailsDataBean);
                if (familymap != null) {
                    for (Map<Long, Map<String, Object>> map : familymap) {
                        Map<String, Object> get = map.get(usrContact.getId());
                        Map<String, String> uiFieldMap = null;
                        Map<String, Object> res = new HashMap<>();
                        uiFieldMap = this.createFieldNameWithComponentType(get);
                        if (!CollectionUtils.isEmpty(uiFieldMap)) {
                            for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                        String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        get.put(field.getKey(), value);
                                    }
                                }
                            }
                            res.putAll(get);
                        }
                        if (!CollectionUtils.isEmpty(res)) {
                            hKEmpFamilyDetailsDataBean.setFamilyCustom(get);
                            break;
                        }
                    }
                }
//                    hKEmpFamilyDetailsDataBean.setIndex("F" + i);
                hKEmpFamilyDetailsDataBean.setIndex(String.valueOf(i));
                listEmpFamilyDetailDataBean.add(hKEmpFamilyDetailsDataBean);

                Set<UMContactDocument> familyContactDocumentSet = usrContact.getuMContactDocumentSet();
                if (!CollectionUtils.isEmpty(familyContactDocumentSet)) {
                    for (UMContactDocument uMContactDocument : familyContactDocumentSet) {
                        if (uMContactDocument.getDocumentType().equalsIgnoreCase(PROFILE)) {
                            hKEmpFamilyDetailsDataBean.setFamilyImageId(uMContactDocument.getId());
                            hKEmpFamilyDetailsDataBean.setFamilyImageName(uMContactDocument.getDocumentName());
                            break;
                        }
                    }
                }

                //add all policy details in listUMContactInsurance
                listContactInsurance.addAll(usrContact.getuMContactInsuranceSet());
                i++;
            }
            employeeDataBean.setFamily(listEmpFamilyDetailDataBean);
            int policycounter = 0;
            List<EmpPolicyDetailDataBean> listEmpPolicyDetailDataBean = new ArrayList<>();
            for (UMContactInsurance contactInsurance : listContactInsurance) {
                EmpPolicyDetailDataBean policyDetailDataBean = new EmpPolicyDetailDataBean();
                convertContactInsuranceToPolicyDatabean(contactInsurance, policyDetailDataBean);
                if (policymap != null) {
                    for (Map<Long, Map<String, Object>> map : policymap) {
                        Map<String, Object> get = map.get(contactInsurance.getId());
                        Map<String, String> uiFieldMap = null;
                        Map<String, Object> res = new HashMap<>();
                        uiFieldMap = this.createFieldNameWithComponentType(get);
                        if (!CollectionUtils.isEmpty(uiFieldMap)) {
                            for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                        String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        get.put(field.getKey(), value);
                                    }
                                }
                            }
                            res.putAll(get);
                        }
                        if (!CollectionUtils.isEmpty(res)) {
                            policyDetailDataBean.setPolicyCustom(get);
                            break;
                        }
                    }
                }
                policyDetailDataBean.setIndex(String.valueOf(policycounter));
                policycounter++;
                String get = familyId.get(contactInsurance.getUserContact().getId());
                policyDetailDataBean.setContactUser(get);
                listEmpPolicyDetailDataBean.add(policyDetailDataBean);
            }
            Set<UMContactInsurance> umContactInsuranceSet = uMUserContact.getuMContactInsuranceSet();
            if (!CollectionUtils.isEmpty(umContactInsuranceSet)) {
                for (UMContactInsurance contactInsurance : umContactInsuranceSet) {
                    EmpPolicyDetailDataBean policyDetailDataBean = new EmpPolicyDetailDataBean();
                    convertContactInsuranceToPolicyDatabean(contactInsurance, policyDetailDataBean);
                    if (policymap != null) {
                        for (Map<Long, Map<String, Object>> map : policymap) {
                            Map<String, Object> get = map.get(contactInsurance.getId());
                            Map<String, String> uiFieldMap = null;
                            Map<String, Object> res = new HashMap<>();
                            uiFieldMap = this.createFieldNameWithComponentType(get);
                            if (!CollectionUtils.isEmpty(uiFieldMap)) {
                                for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                    if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                        if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                            String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                            get.put(field.getKey(), value);
                                        }
                                    }
                                }
                                res.putAll(get);
                            }
                            if (!CollectionUtils.isEmpty(res)) {
                                policyDetailDataBean.setPolicyCustom(get);
                                break;
                            }
                        }
                    }
                    policyDetailDataBean.setIndex(String.valueOf(policycounter));
                    policycounter++;
                    policyDetailDataBean.setContactUser("E0");
                    listEmpPolicyDetailDataBean.add(policyDetailDataBean);
                }
            }
            employeeDataBean.setPolicy(listEmpPolicyDetailDataBean);

            Set<UMUserIpAssociation> umUserIpAssociationSet = uMUser.getuMUserIpAssociationSet();
            if (!CollectionUtils.isEmpty(umUserIpAssociationSet)) {
                for (UMUserIpAssociation ipAssociation : umUserIpAssociationSet) {
                    employeeDataBean.setIpaddress(ipAssociation.getIpAddress());
                    break;
                }
            }

            List<UMContactExperience> listUMContactExperience = new ArrayList<>(uMUserContact.getuMContactExperienceSet());
            List<EmpExperienceDataBean> listEmpExperienceDataBean = new ArrayList<>();

            for (UMContactExperience contactExperience : listUMContactExperience) {
                EmpExperienceDataBean hKEmpExperienceDataBean = new EmpExperienceDataBean();
                convertContactExperienceToExperienceDataBean(contactExperience, hKEmpExperienceDataBean);
                if (hKEmpExperienceDataBean.getSalaryslipImageName() != null) {
                    Long get = salaryslipIds.get(hKEmpExperienceDataBean.getSalaryslipImageName());
                    hKEmpExperienceDataBean.setSalaryslipId(get);
                }
                if (experiencemap != null) {
                    for (Map<Long, Map<String, Object>> expmap : experiencemap) {
                        Map<String, Object> get = expmap.get(contactExperience.getId());
                        Map<String, String> uiFieldMap = null;
                        Map<String, Object> res = new HashMap<>();
                        uiFieldMap = this.createFieldNameWithComponentType(get);
                        if (!CollectionUtils.isEmpty(uiFieldMap)) {
                            for (Map.Entry<String, String> field : uiFieldMap.entrySet()) {
                                if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                    if (get != null && !get.isEmpty() && get.containsKey(field.getKey())) {
                                        String value = get.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                        get.put(field.getKey(), value);
                                    }
                                }
                            }
                            res.putAll(get);
                        }
                        if (!CollectionUtils.isEmpty(res)) {
                            hKEmpExperienceDataBean.setExperienceCustom(get);
                            break;
                        }
                    }
                }
                listEmpExperienceDataBean.add(hKEmpExperienceDataBean);
            }
            employeeDataBean.setExp(listEmpExperienceDataBean);

            Set<UMUserRole> umUserRoleSet = uMUser.getUMUserRoleSet();
            if (!CollectionUtils.isEmpty(umUserRoleSet)) {
                List<Long> roleIds = new ArrayList<>();
                for (UMUserRole uMUserRole : umUserRoleSet) {
                    roleIds.add(uMUserRole.getuMUserRolePK().getRole());
                }
                Long[] roles = new Long[roleIds.size()];
                roleIds.toArray(roles);
                employeeDataBean.setWorkdeg(roles);
            }
        }

        return employeeDataBean;
    }
}
