 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkFranchiseRequirementEntity;
import com.argusoft.hkg.model.HkFranchiseSetupEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntityPK;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import com.argusoft.hkg.sync.SyncXmppUserService;
import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.internationalization.wrapper.InternationalizationServiceWrapper;
import com.argusoft.hkg.web.usermanagement.controllers.FranchiseController;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseMinReqDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserContact;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author Mayank
 */
@Service
public class FranchiseTransformerBean {

    private static final String PASSWORD = "argusadmin";
    @Autowired
    HkFoundationService operationsService;
    @Autowired
    UMUserService userService;
    @Autowired
    UMCompanyService companyService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    @Autowired
    HKCategoryService categoryService;
    @Autowired
    RbReportService reportService;
    @Autowired
    private HkFoundationService hkFoundationService;
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;
    @Autowired
    LocalesTransformerBean localesTransformerBean;
    @Autowired
    InternationalizationServiceWrapper internationalizationServiceWrapper;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    BasicPasswordEncryptor basicPasswordEncryptor;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    private SyncXmppUserService hkXmppUserService;

    @Autowired
    private SyncXmppClient xmppClient;

    @Transactional
    public ResponseEntity<PrimaryKey<Long>> createFranchise(FranchiseDataBean franchiseDataBean) {

        try {
            UMCompany company = new UMCompany();
            convertFranchiseDataBeanToUMCompany(franchiseDataBean, company);
            if (company.getCustom1() == null) {
                company.setCustom1(new Long("0"));
            }
            Boolean isFirstFranchise = franchiseDataBean.isFirstFranchise();
            if (isFirstFranchise) {
                company.setStatus(HkSystemConstantUtil.COMPANY_STATUS_ACTIVE);
                company.setActivatedBy(loginDataBean.getId());
                company.setActivatedOn(new Date());
            } else {
                company.setDescription(HkSystemConstantUtil.SETUP_PENDING);
            }
            company.setActiveUsers(0);
            Long id = companyService.createCompany(company);
            franchiseDataBean.setId(id);

            UMUser adminUser = createUser(franchiseDataBean, Boolean.TRUE);
            company.setWebsiteUrl(adminUser.getId().toString());

            userManagementServiceWrapper.createLocaleForEntity(franchiseDataBean.getFranchiseName(), "Franchise", loginDataBean.getId(), loginDataBean.getCompanyId());
            ExecutorService executorService1 = Executors.newCachedThreadPool();
            SyncXmppUser xmppUser = new SyncXmppUser();
            xmppUser.setFranchise(franchiseDataBean.getId());
            xmppUser.setUserName(franchiseDataBean.getFranchiseName().replaceAll("\\s+", "") + "_" + franchiseDataBean.getId());
            xmppUser.setPassword(PASSWORD);
            xmppUser.setCreatedOn(new Date());
            xmppUser.setCreatedBy(loginDataBean.getId());
            xmppUser.setIsActive(true);
            xmppUser.setIsArchive(false);
            xmppUser.setUserCreatedInTigase(false);
            Map<String, String> properties = new HashMap<>();
            properties.put(SyncHelper.FRANCHISE_ID, franchiseDataBean.getId().toString());

            //If for first frachise
            if (isFirstFranchise) {
                System.out.println("First franchise");
                properties.put(SyncHelper.IS_MASTER__STRING, "true");
                /*
                 * -------------- Add machine category of asset --------- Added
                 * by Dhwani 03/09/2014
                 */
                //Adding Machine Category for First Franchise
                HkCategoryEntity category = new HkCategoryEntity();
                category.setCategoryPrefix(HkSystemConstantUtil.ASSET_CATEGORY_MACHINES);
                category.setCategoryTitle(HkSystemConstantUtil.ASSET_CATEGORY_MACHINES_NAME);
                category.setCreatedBy(loginDataBean.getId());
                category.setCreatedOn(new Date());
                category.setCurrentIndex(1);
                category.setDescription(null);
                category.setFranchise(id);
                category.setIsActive(Boolean.TRUE);
                category.setIsArchive(Boolean.FALSE);
                category.setStartIndex(1);
                category.setLastModifiedOn(new Date());
                category.setLastModifiedBy(loginDataBean.getId());
                categoryService.createCategory(category, CategoryType.ASSET);

                //Adding and assigning HKAdmin Role for first franchise
                UMRole hkAdminRole = userManagementServiceWrapper.createRole(HkSystemConstantUtil.ROLE.HK_ADMIN, 0, 0l);
                if (hkAdminRole != null) {
                    userManagementServiceWrapper.assignRoleToUser(adminUser, hkAdminRole, true);
                }

                //Adding Franchise Admin Role for first franchise
                userManagementServiceWrapper.createRole(HkSystemConstantUtil.ROLE.FRANCHISE_ADMIN, 1, 0l);

                internationalizationServiceWrapper.copyLocalesFromFranchise(0l, id, loginDataBean.getId());                
                createOrUpdateCustomField(id, franchiseDataBean);
                if (!StringUtils.isEmpty(company.getEmailAddress())) {
                    addConfigurationPropertyForSync(company.getEmailAddress(), company.getId());
                }
                try {
                    //Update FeatureFromTemplateMap of Application Util to store custom field data of newly created franchise.
                    customFieldTransformerBean.createFeatureSectionMap(false, null);
                } catch (GenericDatabaseException ex) {
                    Logger.getLogger(FranchiseTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                properties.put(SyncHelper.IS_MASTER__STRING, "false");
                //else add following calls
                if (franchiseDataBean.getFranchiseModel() != null && franchiseDataBean.getFranchiseModel() > 0l) {
                    List<HkCategoryEntity> copyMachinesFromFranchise = operationsService.copyMachinesFromFranchise(franchiseDataBean.getFranchiseModel(), id);
                    List<String> categoryNameList = new ArrayList<>();
                    for (HkCategoryEntity categoryEntity : copyMachinesFromFranchise) {
                        categoryNameList.add(categoryEntity.getCategoryTitle());
                    }
                    userManagementServiceWrapper.createAllLocaleForEntity(categoryNameList, "assetcategory", loginDataBean.getId(), loginDataBean.getCompanyId());

                    // Copy roles while creating franchise commented for Task #7211 by Shifa on 10 August 2015
//                    List<UMRole> copyRolesFromFranchise = userManagementServiceWrapper.copyRolesFromFranchise(franchiseDataBean.getFranchiseModel(), id, loginDataBean.getId());
                    //Adding and assigning HKAdmin Role for first franchise
                    UMRole franchiseAdminRole = userManagementServiceWrapper.retrieveRole(HkSystemConstantUtil.ROLE.FRANCHISE_ADMIN, franchiseDataBean.getId(), false,loginDataBean.getPrecedence());
                    if (franchiseAdminRole != null) {
                        userManagementServiceWrapper.assignRoleToUser(adminUser, franchiseAdminRole, false);
                    }

                    //Adding minimum requirement of machines and designations for the new franchise
                    Map<String, Long> roleMap = new HashMap<>();
                    Map<String, Long> categoryMap = new HashMap<>();
//
                    List<UMRole> allRoles = userManagementServiceWrapper.retrieveRolesByCompanyByStatus(0l, null, true, true,-1);
                    // Method changed by Shifa .Earlier it was taking roles from coped designation.Now Role map created by retrieving roles for 0 companyId 
                    if (!CollectionUtils.isEmpty(allRoles)) {
                        for (UMRole uMRole : allRoles) {
                            roleMap.put(uMRole.getName(), uMRole.getId());
                        }
                    }
                    if (!CollectionUtils.isEmpty(copyMachinesFromFranchise)) {
                        for (HkCategoryEntity categoryEntity : copyMachinesFromFranchise) {
//                            System.out.println("##Id "+categoryEntity.getId());
                            categoryMap.put(categoryEntity.getCategoryTitle(), categoryEntity.getId());
                        }
                    }
                    createFranchiseMinReq(franchiseDataBean, roleMap, categoryMap, HkSystemConstantUtil.CREATE_OPERATION);

                    internationalizationServiceWrapper.copyLocalesFromFranchise(franchiseDataBean.getFranchiseModel(), id, loginDataBean.getId());
                }
            }

            executorService1.execute(() -> {
                try {
                    Boolean isUserAddedTigase = xmppClient.addUser(xmppUser.getUserName(), xmppUser.getPassword(), null);
                    xmppUser.setUserCreatedInTigase(isUserAddedTigase);
                    if (isUserAddedTigase) {
                        xmppClient.addVCard(xmppUser.getUserName(), xmppUser.getPassword(), properties);
                        xmppClient.createRosterEntry(xmppUser.getUserName());
                    }
                } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException ex) {
                    Logger.getLogger(FranchiseController.class.getName()).log(Level.SEVERE, null, ex);
                }
                hkXmppUserService.createUser(xmppUser);
            });

            localesTransformerBean.generateLanguagePropertyFiles(id);

            if (isFirstFranchise) {
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Franchise added successfully", null, true);
            } else {
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Franchise created successfully, franchise admin login will be activated within an hour", null, true);
            }
        } catch (NumberFormatException | UMUserManagementException e) {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Franchise can not be created", null, true);
        }
    }

    private UMUser createUser(FranchiseDataBean franchiseDataBean, Boolean isDefaultDepartment) throws UMUserManagementException {
        UMUser uMUser = new UMUser();
        uMUser.setUserId(franchiseDataBean.getAdminUserName());
        uMUser.setFirstName(franchiseDataBean.getFirstName());
        uMUser.setMiddleName(franchiseDataBean.getMiddleName());
        uMUser.setLastName(franchiseDataBean.getLastName());
        String encrypted = basicPasswordEncryptor.encryptPassword(franchiseDataBean.getPassword());
        uMUser.setPassword(encrypted);
        if (isDefaultDepartment) {
            List<UMDepartment> deptList = userManagementServiceWrapper.retrieveDefaultDepartment(0l);
            if (!CollectionUtils.isEmpty(deptList)) {
                UMDepartment defaultDept = deptList.get(0);
                uMUser.setDepartment(defaultDept.getId());
            }
        }
        uMUser.setIsActive(true);
        uMUser.setCompany(franchiseDataBean.getId());
        uMUser.setCreatedBy(loginDataBean.getId());
        uMUser.setUserCode(franchiseDataBean.getAdminUserName().trim());
        UMUserContact contact = new UMUserContact();
        contact.setFirstName(franchiseDataBean.getFirstName());
        contact.setLastName(franchiseDataBean.getLastName());
        contact.setMiddleName(franchiseDataBean.getMiddleName());
        contact.setCompany(franchiseDataBean.getId());
        contact.setCreatedBy(loginDataBean.getId());
        contact.setCreatedOn(new Date());
        contact.setIsActive(true);
        contact.setUserobj(uMUser);
        uMUser.setContact(contact);
        userService.createUser(uMUser);
        StringBuilder builder = new StringBuilder(franchiseDataBean.getFirstName());
        if (franchiseDataBean.getMiddleName() != null) {
            builder.append(" ").append(franchiseDataBean.getMiddleName());
        }
        if (franchiseDataBean.getLastName() != null) {
            builder.append(" ").append(franchiseDataBean.getLastName());
        }
        userManagementServiceWrapper.createLocaleForEntity(builder.toString(), "Employee", uMUser.getCreatedBy(), uMUser.getCompany());
        return uMUser;
    }

    private void createFranchiseMinReq(FranchiseDataBean franchiseDataBean, Map<String, Long> roleMap, Map<String, Long> categoryMap, String operation) {
        if (!CollectionUtils.isEmpty(franchiseDataBean.getFranchiseMinReq())) {
            List<FranchiseMinReqDataBean> minReqDataBeans = franchiseDataBean.getFranchiseMinReq();
            List<HkFranchiseRequirementEntity> franchiseRequirementEntitys = new ArrayList<>();
            for (FranchiseMinReqDataBean franchiseMinReqDataBean : minReqDataBeans) {
                if (franchiseMinReqDataBean.getRequiredValue() != null) {
                    franchiseMinReqDataBean.setFranchise(franchiseDataBean.getId());
                    HkFranchiseRequirementEntity franchiseRequirementEntity = new HkFranchiseRequirementEntity();
//                    System.out.println("Requirement name : "+franchiseMinReqDataBean.getRequirementName());
                    convertFranchiseRequirementDataBeanToHkFranchiseRequirementEntity(franchiseRequirementEntity, franchiseMinReqDataBean, operation, roleMap, categoryMap);
                    franchiseRequirementEntitys.add(franchiseRequirementEntity);
                }
            }
            operationsService.createFranchiseRequirements(franchiseRequirementEntitys);
        }
    }

    private UMCompany convertFranchiseDataBeanToUMCompany(FranchiseDataBean franchiseDataBean, UMCompany company) {
        Date currentDateTime = new Date();
        if (franchiseDataBean.getId() == null) {
            company.setCreatedOn(currentDateTime);
            company.setStatus(HkSystemConstantUtil.COMPANY_STATUS_PENDING);
            company.setIsActive(true);
            company.setCreatedBy(loginDataBean.getId());
        } else {
            company.setModifiedBy(loginDataBean.getId());
            company.setModifiedOn(currentDateTime);
        }

        company.setName(franchiseDataBean.getFranchiseName());
        company.setAddress(franchiseDataBean.getAddress());
        company.setZipCode(franchiseDataBean.getZipCode());
        if (franchiseDataBean.getCity() != null) {
            company.setCity(franchiseDataBean.getCity().toString());
        }
        if (franchiseDataBean.getDistrict() != null) {
            company.setAlternateContactNumber(franchiseDataBean.getDistrict().toString());
        }
        if (franchiseDataBean.getState() != null) {
            company.setProvince(franchiseDataBean.getState().toString());
        }
        if (franchiseDataBean.getCountry() != null) {
            company.setCountry(franchiseDataBean.getCountry().toString());
        }
        company.setCustom2(franchiseDataBean.getContactPerson());
        company.setContactNumber(franchiseDataBean.getPhoneNumber());
        company.setEmailAddress(franchiseDataBean.getEmailAddress());
        company.setCustom1(franchiseDataBean.getFranchiseModel());
        company.setCompanyCode(franchiseDataBean.getFranchiseCode());
        return company;
    }

    public boolean doesFranchiseNameExist(String franchiseName, Long skipFranchiseId) {
        return userManagementServiceWrapper.doesFranchiseNameExist(franchiseName, skipFranchiseId);
    }

    public boolean doesUserNameExist(String userName) {
        return userService.isUserExist(userName);
    }

    @Transactional
    public void removeFranchise(Long primaryKey) {
        UMCompany retrieveCompanyByCompanyId = companyService.retrieveCompanyByCompanyId(primaryKey);
        retrieveCompanyByCompanyId.setIsActive(Boolean.FALSE);
        retrieveCompanyByCompanyId.setIsArchive(Boolean.TRUE);
        retrieveCompanyByCompanyId.setModifiedOn(new Date());
        retrieveCompanyByCompanyId.setModifiedBy(loginDataBean.getId());
        List<UMCompany> companys = new ArrayList<>();
        companys.add(retrieveCompanyByCompanyId);
        companyService.updateAllCompany(companys);
        FranchiseDataBean franchiseDataBean = this.retrieveByFranchiseId(primaryKey);
        userManagementServiceWrapper.deleteLocaleForEntity(franchiseDataBean.getFranchiseName(), "franchise", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
        operationsService.removeAllFranchiseRequirements(primaryKey);
    }

    @Transactional
    public String updateFranchise(FranchiseDataBean franchiseDataBean) {
        String response = HkSystemConstantUtil.SUCCESS;
        try {
            if (franchiseDataBean.getId() != null) {
                UMCompany company = companyService.retrieveCompanyByCompanyId(franchiseDataBean.getId());
                convertFranchiseDataBeanToUMCompany(franchiseDataBean, company);
                if (franchiseDataBean.getStatus().equalsIgnoreCase(HkSystemConstantUtil.COMPANY_STATUS_PENDING) || company.getStatus().equalsIgnoreCase(HkSystemConstantUtil.COMPANY_STATUS_PENDING)) {
                    createFranchiseMinReq(franchiseDataBean, null, null, HkSystemConstantUtil.UPDATE_OPERATION);
                    if (franchiseDataBean.getStatus().equalsIgnoreCase(HkSystemConstantUtil.COMPANY_STATUS_ACTIVE)) {
                        company.setStatus(HkSystemConstantUtil.COMPANY_STATUS_ACTIVE);
                        company.setActivatedBy(loginDataBean.getId());
                        company.setActivatedOn(new Date());
                    }
                }               

                List<UMCompany> uMCompanys = new ArrayList<>();
                uMCompanys.add(company);
                companyService.updateAllCompany(uMCompanys);
                createOrUpdateCustomField(company.getId(), franchiseDataBean);
                userManagementServiceWrapper.createLocaleForEntity(franchiseDataBean.getFranchiseName(), "Franchise", loginDataBean.getId(), loginDataBean.getCompanyId());

            }
        } catch (Exception e) {
            response = HkSystemConstantUtil.FAILURE;
        }
        return response;
    }

    public void createOrUpdateCustomField(Long id, FranchiseDataBean franchiseDataBean) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, franchiseDataBean.getFranchiseCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(franchiseDataBean.getFranchiseDbType())) {
            for (Map.Entry<String, String> entrySet : franchiseDataBean.getFranchiseDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, franchiseDataBean.getFranchiseDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE.toString(), loginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE, loginDataBean.getCompanyId(), map);
    }

    public boolean isThereAnyLinkWithFranchise(Long id) {
        boolean link = false;
        int existingUserCount = userManagementServiceWrapper.retrieveUserCountByCompany(id);
        Map<Long, Integer> retrieveAssetCategorywiseCount = operationsService.retrieveAssetCategorywiseCount(id);
        int existingAssetCount = 0;
        if (!CollectionUtils.isEmpty(retrieveAssetCategorywiseCount)) {
            for (Map.Entry<Long, Integer> entry : retrieveAssetCategorywiseCount.entrySet()) {
                if (entry.getValue() != null) {
                    existingAssetCount = existingAssetCount + entry.getValue();
                }
            }
        }
        if (existingUserCount > 0 || existingAssetCount > 0) {
            link = true;
        }
        return link;
    }

    public FranchiseDataBean retrieveByFranchiseId(Long primaryKey) {
        UMCompany companyBasicDetails = companyService.retrieveCompanyByCompanyId(primaryKey);
        FranchiseDataBean franchiseDataBean = new FranchiseDataBean();
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(primaryKey, HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE, loginDataBean.getCompanyId());
        franchiseDataBean = convertUMCompanyToFranchiseDataBean(franchiseDataBean, companyBasicDetails, retrieveDocumentByInstanceId);
        try {
            UMUser retrieveUserById = userService.retrieveUserById(new Long(companyBasicDetails.getWebsiteUrl()), null);
            if (retrieveUserById != null) {
                franchiseDataBean.setFirstName(retrieveUserById.getFirstName());
                franchiseDataBean.setMiddleName(retrieveUserById.getMiddleName());
                franchiseDataBean.setLastName(retrieveUserById.getLastName());
                franchiseDataBean.setAdminUserName(retrieveUserById.getUserId());
                franchiseDataBean.setPassword(retrieveUserById.getPassword());
            }
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(FranchiseTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<FranchiseMinReqDataBean> retrieveFranchiseMinRequirementByFranchiseId = retrieveFranchiseMinRequirementByFranchiseId(primaryKey, true);
        franchiseDataBean.setFranchiseMinReq(retrieveFranchiseMinRequirementByFranchiseId);
        return franchiseDataBean;
    }

    public Map<Long, String> getDesignationMap(Long id) {
        List<UMRole> retrieveDesignations = userManagementServiceWrapper.retrieveDesignations(id, 1, false);
        Map<Long, String> designationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveDesignations)) {
            for (UMRole uMRole : retrieveDesignations) {
                designationMap.put(uMRole.getId(), uMRole.getName());
            }
        }
        return designationMap;
    }

//    public Map<String, Long> getDesignationMapOrder(Long id) {
//        List<UMRole> retrieveDesignations = userManagementServiceWrapper.retrieveDesignations(id);
//        Map<String, Long> designationMap = new HashMap<>();
//        if (!CollectionUtils.isEmpty(retrieveDesignations)) {
//            for (UMRole uMRole : retrieveDesignations) {
//                designationMap.put(uMRole.getName(), uMRole.getId());
//            }
//        }
//        return designationMap;
//    }
    public Map<String, Long> getDesignationMapOrder(Long id) {
        List<UMRole> retrieveDesignations = userManagementServiceWrapper.retrieveDesignations(id, loginDataBean.getPrecedence(), false);
        Map<String, Long> designationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveDesignations)) {
            for (UMRole uMRole : retrieveDesignations) {
                designationMap.put(uMRole.getName(), uMRole.getId());
            }
        }
        return designationMap;
    }

    public List<FranchiseMinReqDataBean> retrieveFranchiseMinRequirementByFranchiseId(Long id, boolean fromRetriveById) {
        List<FranchiseMinReqDataBean> minReqDataBeans = new LinkedList<>();

        if (fromRetriveById) {
            Map<Long, Integer> retrieveUserCountsByRole = userManagementServiceWrapper.retrieveUserCountsByRole(id);
            Map<Long, Integer> retrieveAssetCategorywiseCount = operationsService.retrieveAssetCategorywiseCount(id);
            List<HkFranchiseRequirementEntity> retrieveFranchiseRequirements = operationsService.retrieveFranchiseRequirements(id);
            if (!CollectionUtils.isEmpty(retrieveFranchiseRequirements)) {
                for (HkFranchiseRequirementEntity hkFranchiseRequirementEntity : retrieveFranchiseRequirements) {
                    if (hkFranchiseRequirementEntity.getRequiredValue() != null) {
                        if (hkFranchiseRequirementEntity.getReqType().equalsIgnoreCase(HkSystemConstantUtil.DESIGNATION_KEY_CODE)) {
                            // designationMap.remove(hkFranchiseRequirementEntity.getReqId());
                            FranchiseMinReqDataBean franchiseMinReqDataBean = new FranchiseMinReqDataBean();
                            convertHkFranchiseRequirementEntityToDataBean(hkFranchiseRequirementEntity, franchiseMinReqDataBean);
                            franchiseMinReqDataBean.setAcquiredValue(retrieveUserCountsByRole.get(hkFranchiseRequirementEntity.getReqId()));
                            minReqDataBeans.add(franchiseMinReqDataBean);
                        } else {
                            FranchiseMinReqDataBean franchiseMinReqDataBean = new FranchiseMinReqDataBean();
                            convertHkFranchiseRequirementEntityToDataBean(hkFranchiseRequirementEntity, franchiseMinReqDataBean);
                            Integer retrieveAssetCount = 0;
                            if (!CollectionUtils.isEmpty(retrieveAssetCategorywiseCount)) {
                                retrieveAssetCount = retrieveAssetCategorywiseCount.get(hkFranchiseRequirementEntity.getReqId());
                            }
                            franchiseMinReqDataBean.setAcquiredValue(retrieveAssetCount);
                            minReqDataBeans.add(franchiseMinReqDataBean);
                            // assetMap.remove(hkFranchiseRequirementEntity.getReqId());
                        }
                    }
                }
            }
        } else {
            Map<Long, String> designationMap = getDesignationMap(id);
            Map<Long, String> assetMap = getAssetMap(id);

            for (Map.Entry<Long, String> entry : designationMap.entrySet()) {
                Long long1 = entry.getKey();
                String string = entry.getValue();
                FranchiseMinReqDataBean franchiseMinReqDataBean = new FranchiseMinReqDataBean();
                franchiseMinReqDataBean.setFranchise(id);
                franchiseMinReqDataBean.setReqId(long1);
                franchiseMinReqDataBean.setRequirementType(HkSystemConstantUtil.DESIGNATION_KEY_CODE);
                franchiseMinReqDataBean.setRequirementName(string);
                //franchiseMinReqDataBean.setAcquiredValue(retrieveUserCountsByRole.get(long1));
                minReqDataBeans.add(franchiseMinReqDataBean);
            }
            for (Map.Entry<Long, String> entry : assetMap.entrySet()) {
                Long long1 = entry.getKey();
                String string = entry.getValue();
                FranchiseMinReqDataBean franchiseMinReqDataBean = new FranchiseMinReqDataBean();
                franchiseMinReqDataBean.setFranchise(id);
                franchiseMinReqDataBean.setReqId(long1);
                franchiseMinReqDataBean.setRequirementType(HkSystemConstantUtil.MACHINE_KEY_CODE);
                franchiseMinReqDataBean.setRequirementName(string);
//                if (!CollectionUtils.isEmpty(retrieveAssetCategorywiseCount) && retrieveAssetCategorywiseCount.containsKey(long1)) {
//                    franchiseMinReqDataBean.setAcquiredValue(retrieveAssetCategorywiseCount.get(long1));
//                }
                minReqDataBeans.add(franchiseMinReqDataBean);
            }
        }
        return minReqDataBeans;
    }

    public List<FranchiseDataBean> retrieveAllFranchise(boolean treeView) throws GenericDatabaseException {
        List<FranchiseDataBean> franchiseDataBeanList = new ArrayList<>();
        List<UMCompany> retrieveCompanies = companyService.retrieveAllCompany(Boolean.TRUE);
        if (!CollectionUtils.isEmpty(retrieveCompanies)) {
            if (!treeView) {
                for (UMCompany uMCompany : retrieveCompanies) {
                    FranchiseDataBean dataBean = new FranchiseDataBean();
                    dataBean.setId(uMCompany.getId());
                    dataBean.setFranchiseName(uMCompany.getName());
                    dataBean.setFranchiseCode(uMCompany.getCompanyCode());
                    franchiseDataBeanList.add(dataBean);
                }
            } else {
                Map<Long, List<UMCompany>> treemap = new HashMap<>();
                Map<Long, UMCompany> companymap = new HashMap<>();

                for (UMCompany uMCompany : retrieveCompanies) {
                    companymap.put(uMCompany.getId(), uMCompany);
                }
                for (Map.Entry<Long, UMCompany> entry : companymap.entrySet()) {
                    UMCompany uMCompany = entry.getValue();
                    if (uMCompany.getCustom1() == null || uMCompany.getCustom1().equals(0l)) {
                        List<UMCompany> get = treemap.get(uMCompany.getId());
                        if (get == null) {
                            get = new ArrayList<>();
                        }
                        treemap.put(uMCompany.getId(), get);
                    } else {
                        List<UMCompany> get = treemap.get(uMCompany.getCustom1());
                        if (get == null) {
                            get = new ArrayList<>();
                        }
                        get.add(uMCompany);
                        treemap.put(uMCompany.getCustom1(), get);
                    }
                }
                for (Map.Entry<Long, List<UMCompany>> entry : treemap.entrySet()) {
                    Long long1 = entry.getKey();
                    UMCompany get = companymap.get(long1);
                    if (get != null) {
                        List<UMCompany> uMCompanyList = entry.getValue();
                        List<FranchiseDataBean> children = new ArrayList<>();
                        for (UMCompany uMCompany1 : uMCompanyList) {
                            FranchiseDataBean franchisechild = new FranchiseDataBean();
                            franchisechild.setId(uMCompany1.getId());
                            franchisechild.setFranchiseName(uMCompany1.getName());
                            children.add(franchisechild);
                        }
                        FranchiseDataBean franchise = new FranchiseDataBean();
                        franchise.setId(get.getId());
                        franchise.setFranchiseName(get.getName());
                        franchise.setChildren(children);
                        franchiseDataBeanList.add(franchise);
                    }
                }
            }
        }
        if (franchiseDataBeanList.size() > 0) {
            Collections.sort(franchiseDataBeanList, new Comparator<FranchiseDataBean>() {
                @Override
                public int compare(final FranchiseDataBean object1, final FranchiseDataBean object2) {
                    return object1.getFranchiseName().compareTo(object2.getFranchiseName());
                }
            });
        }

        return franchiseDataBeanList;
    }

    public List<SelectItem> retrieveAllFranchiseNames() {
        List<SelectItem> franchiseDataBeanList = new ArrayList<>();
        List<UMCompany> retrieveCompanies = companyService.retrieveAllCompany(Boolean.TRUE);
        for (UMCompany uMCompany : retrieveCompanies) {
            SelectItem selectItem = new SelectItem(uMCompany.getId(), uMCompany.getName());
            franchiseDataBeanList.add(selectItem);
        }
        return franchiseDataBeanList;
    }

    private FranchiseDataBean convertUMCompanyToFranchiseDataBean(FranchiseDataBean franchiseDataBean, UMCompany companyBasicDetails, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId) {
        franchiseDataBean.setId(companyBasicDetails.getId());
        if (companyBasicDetails.getName() != null) {
            franchiseDataBean.setFranchiseName(companyBasicDetails.getName());
        }
        if (companyBasicDetails.getCustom1() != null) {
            franchiseDataBean.setFranchiseModel(companyBasicDetails.getCustom1());
        }
        if (companyBasicDetails.getCity() != null) {
            franchiseDataBean.setCity(Long.parseLong(companyBasicDetails.getCity()));
        }

        if (companyBasicDetails.getProvince() != null) {
            franchiseDataBean.setState(Long.parseLong(companyBasicDetails.getProvince()));
        }

        if (companyBasicDetails.getCountry() != null) {
            franchiseDataBean.setCountry(Long.parseLong(companyBasicDetails.getCountry()));
        }

        if (companyBasicDetails.getAlternateContactNumber() != null) {
            franchiseDataBean.setDistrict(Long.parseLong(companyBasicDetails.getAlternateContactNumber()));
        }

        if (companyBasicDetails.getAddress() != null) {
            franchiseDataBean.setAddress(companyBasicDetails.getAddress());
        }

        if (companyBasicDetails.getCustom2() != null) {
            franchiseDataBean.setContactPerson(companyBasicDetails.getCustom2());
        }

        if (companyBasicDetails.getContactNumber() != null) {
            franchiseDataBean.setPhoneNumber(companyBasicDetails.getContactNumber());
        }

        if (companyBasicDetails.getEmailAddress() != null) {
            franchiseDataBean.setEmailAddress(companyBasicDetails.getEmailAddress());
        }

        if (companyBasicDetails.getStatus() != null) {
            franchiseDataBean.setStatus(companyBasicDetails.getStatus());
        }

        if (companyBasicDetails.getZipCode() != null) {
            franchiseDataBean.setZipCode(companyBasicDetails.getZipCode());
        }

        if (companyBasicDetails.getActivatedOn() != null) {
            franchiseDataBean.setActivatedOn(companyBasicDetails.getActivatedOn());
        }
        franchiseDataBean.setFranchiseCode(companyBasicDetails.getCompanyCode());
        if (retrieveDocumentByInstanceId != null) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    franchiseDataBean.setFranchiseCustom(map.get(companyBasicDetails.getId()));
                }
            }
        }
        return franchiseDataBean;

    }

    private void convertHkFranchiseRequirementEntityToDataBean(HkFranchiseRequirementEntity hkFranchiseRequirementEntity, FranchiseMinReqDataBean franchiseMinReqDataBean) {
        if (hkFranchiseRequirementEntity.getAcquiredValue() != null) {
            franchiseMinReqDataBean.setAcquiredValue(hkFranchiseRequirementEntity.getAcquiredValue());
        }
        if (hkFranchiseRequirementEntity.getRequiredValue() != null) {
            franchiseMinReqDataBean.setRequiredValue(hkFranchiseRequirementEntity.getRequiredValue());
        }
        franchiseMinReqDataBean.setId(hkFranchiseRequirementEntity.getId());
        franchiseMinReqDataBean.setFranchise(hkFranchiseRequirementEntity.getFranchise());
        franchiseMinReqDataBean.setRequirementName(hkFranchiseRequirementEntity.getReqName());
        franchiseMinReqDataBean.setReqId(hkFranchiseRequirementEntity.getReqId());
        franchiseMinReqDataBean.setRequirementType(hkFranchiseRequirementEntity.getReqType());
    }

    private void convertFranchiseRequirementDataBeanToHkFranchiseRequirementEntity(HkFranchiseRequirementEntity hkFranchiseRequirementEntity, FranchiseMinReqDataBean franchiseMinReqDataBean, String operation, Map<String, Long> degmap, Map<String, Long> assetMap) {
        if (franchiseMinReqDataBean.getReqId() != null) {
            hkFranchiseRequirementEntity.setReqId(franchiseMinReqDataBean.getReqId());
        }
        if (franchiseMinReqDataBean.getFranchise() != null) {
            hkFranchiseRequirementEntity.setFranchise(franchiseMinReqDataBean.getFranchise());
        }
        if (franchiseMinReqDataBean.getRequirementType() != null) {
            hkFranchiseRequirementEntity.setReqType(franchiseMinReqDataBean.getRequirementType());
        }
        if (franchiseMinReqDataBean.getRequirementName() != null) {
            hkFranchiseRequirementEntity.setReqName(franchiseMinReqDataBean.getRequirementName());
//            System.out.println("Req. Type: "+franchiseMinReqDataBean.getRequirementType());
            if (operation != null && HkSystemConstantUtil.CREATE_OPERATION.equalsIgnoreCase(operation)) {
                if (franchiseMinReqDataBean.getRequirementType().equalsIgnoreCase(HkSystemConstantUtil.DESIGNATION_KEY_CODE)) {
                    Long get = degmap.get(franchiseMinReqDataBean.getRequirementName());
                    if (get != null) {
                        hkFranchiseRequirementEntity.setReqId(get);
                    }
                } else if (franchiseMinReqDataBean.getRequirementType().equalsIgnoreCase(HkSystemConstantUtil.MACHINE_KEY_CODE)) {
                    Long get = assetMap.get(franchiseMinReqDataBean.getRequirementName());
                    if (get != null) {
                        hkFranchiseRequirementEntity.setReqId(get);
                    }
                }
//                 System.out.println("Req. Id: "+franchiseMinReqDataBean.getReqId());
            }
        }
        hkFranchiseRequirementEntity.setId(franchiseMinReqDataBean.getId());
        hkFranchiseRequirementEntity.setIsArchive(false);
        hkFranchiseRequirementEntity.setRequiredValue(franchiseMinReqDataBean.getRequiredValue());
        hkFranchiseRequirementEntity.setAcquiredValue(franchiseMinReqDataBean.getAcquiredValue());
    }

    public List<FranchiseDataBean> searchFranchise(String franchiseName) throws GenericDatabaseException {
        List<FranchiseDataBean> retrieveAllFranchise = new ArrayList<>();
        List<UMCompany> searchCompanyByName = userManagementServiceWrapper.searchCompanyByName(franchiseName);
        if (!CollectionUtils.isEmpty(searchCompanyByName)) {

            List<Long> ids = new ArrayList<>();
            for (UMCompany uMCompany : searchCompanyByName) {
                if (uMCompany.getWebsiteUrl() != null) {
                    ids.add(new Long(uMCompany.getWebsiteUrl()));
                }
            }
            List<UMUser> retrieveUsers = userManagementServiceWrapper.retrieveUsers(ids, true);
            Map<String, String> useridmap = new HashMap<>();
            if (!CollectionUtils.isEmpty(retrieveUsers)) {
                for (UMUser uMUser : retrieveUsers) {
                    useridmap.put(uMUser.getId().toString(), uMUser.getUserId());
                }
            }
            for (UMCompany uMCompany : searchCompanyByName) {
                FranchiseDataBean franchiseDataBean = new FranchiseDataBean();
                franchiseDataBean.setAdminUserName(useridmap.get(uMCompany.getWebsiteUrl()));
                retrieveAllFranchise.add(convertUMCompanyToFranchiseDataBean(franchiseDataBean, uMCompany, null));
            }
        }
        return retrieveAllFranchise;
    }

    public Map<Long, String> getAssetMap(Long companyId) {
        Map<Long, String> assetMap = new HashMap<>();
        List<HkCategoryEntity> retrieveCategoryBasedonSearchCriteria = categoryService.retrieveCategoryBasedonSearchCriteria(null, companyId, CategoryType.ASSET);
//        if (!CollectionUtils.isEmpty(retrieveCategoryBasedonSearchCriteria)) {
//            List<HkAssetEntity> hkAssetEntityList = hkOperationsService.retrieveAssets(retrieveCategoryBasedonSearchCriteria.get(0).getId(), companyId, false);
//            if (!CollectionUtils.isEmpty(hkAssetEntityList)) {
//                for (HkAssetEntity hkAssetEntity : hkAssetEntityList) {
//                    if (!hkAssetEntity.getIsArchive()) {
//                        assetMap.put(hkAssetEntity.getId(), hkAssetEntity.getAssetName());
//                    }
//                }
//            }
//        }
        if (!CollectionUtils.isEmpty(retrieveCategoryBasedonSearchCriteria)) {
            for (HkCategoryEntity hkCategoryEntity : retrieveCategoryBasedonSearchCriteria) {
                {

                    assetMap.put(hkCategoryEntity.getId(), hkCategoryEntity.getCategoryTitle());
                }
            }
        }
        return assetMap;
    }

    public void activateFranchise() throws GenericDatabaseException, UMUserManagementException {
        List<UMCompany> pendingCompanies = userManagementServiceWrapper.reteievePendingCompanies();

        if (!CollectionUtils.isEmpty(pendingCompanies)) {

            for (UMCompany franchise : pendingCompanies) {
                HkFranchiseSetupEntity franchiseSetup = fieldService.retrieveFranchiseSetupByFranchiseSetupItem(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.CUSTOM_FIELD);

                if (franchiseSetup == null) {
                    franchiseSetup = new HkFranchiseSetupEntity(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.CUSTOM_FIELD);
                    franchiseSetup.setStatus("P");
                    franchiseSetup.setLastModifiedOn(new Date());

                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup);
                    //Copy Custom fields from source franchise to Desination franchise
//                    fieldService.copyFieldsFromFranchise(franchise.getCustom1(), franchise.getId());
                    customFieldSevice.copyFeatureFieldPermission(franchise.getCustom1(), franchise.getId());
                    //Update FeatureFromTemplateMap of Application Util to store custom field data of newly created franchise.
                    customFieldTransformerBean.createFeatureSectionMap(false, null);
                    franchiseSetup.setStatus("A");
                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup);
                }

                HkFranchiseSetupEntity franchiseSetup1 = fieldService.retrieveFranchiseSetupByFranchiseSetupItem(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.ACTIVITY_FLOW);

                if (franchiseSetup1 == null) {
                    franchiseSetup1 = new HkFranchiseSetupEntity(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.ACTIVITY_FLOW);
                    franchiseSetup1.setStatus("P");
                    franchiseSetup1.setLastModifiedOn(new Date());

                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup);
                    
                    franchiseSetup1.setStatus("A");
                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup1);
                }
                HkFranchiseSetupEntity franchiseSetup2 = fieldService.retrieveFranchiseSetupByFranchiseSetupItem(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.REPORT);

                if (franchiseSetup2 == null) {
                    franchiseSetup2 = new HkFranchiseSetupEntity(franchise.getId(), HkSystemConstantUtil.FranchiseSetupEntity.REPORT);
                    franchiseSetup2.setStatus("P");
                    franchiseSetup2.setLastModifiedOn(new Date());

                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup);

                    reportService.copyReportsFromSourceToDestination(franchise.getCustom1(), franchise.getId());

                    franchiseSetup2.setStatus("A");
                    fieldService.createOrUpdateFranchiseSetup(franchiseSetup2);
                }

                List<UMUser> inactiveUsers = userManagementServiceWrapper.retrieveUsersByCompany(franchise.getId(), Boolean.TRUE, false, null);

                if (!CollectionUtils.isEmpty(inactiveUsers)) {
                    List<UMUserRole> userroles = userManagementServiceWrapper.retrieveUserRolesByUserId(inactiveUsers.get(0).getId(), false, true);

                    if (!CollectionUtils.isEmpty(userroles)) {
                        for (UMUserRole uMUserRole : userroles) {
                            uMUserRole.setIsActive(true);
                            uMUserRole.setIsArchive(false);
                        }
                        userService.updateAllUserRole(userroles);
                    }
                }

                franchise.setDescription(null);
                franchise.setStatus(HkSystemConstantUtil.ACTIVE);
            }
            companyService.updateAllCompany(pendingCompanies);
        }
    }

    public void addConfigurationPropertyForSync(String email, Long companyId) {
        List<HkSystemConfigurationEntity> configurationEntitys = new LinkedList<>();
        configurationEntitys.add(new HkSystemConfigurationEntity(new HkSystemConfigurationEntityPK(HkSystemConstantUtil.FranchiseConfiguration.DEFAULT_XMPP_EMAIL_ADDRESS, companyId), false, email, 0, new Date()));
        hkFoundationService.saveSystemConfigurations(configurationEntitys);
    }
}
