/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.core.util.MasterType;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.master.databeans.ValueExceptionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FieldDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLanguageEntity;
import com.argusoft.usermanagement.common.model.UMFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author sidhdharth
 */
@Service
public class MasterTransformerBean {

    @Autowired
    HkFoundationService foundationService;

    @Autowired
    I18nService i18nService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    LocalesTransformerBean localesTransformerBean;

    @Autowired
    private HkCustomFieldService customFieldService;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkFoundationDocumentService hkFoundationDocumentService;

    @Autowired
    public BasicPasswordEncryptor basicPasswordEncryptor;
    @Autowired
    public HkFieldService fieldService;
    @Autowired
    public CustomFieldTransformerBean customFieldTransformerBean;
    public static String RETRIEVE_ALL = "ALL";
    public static String RETRIEVE_BY_ID = "BY_ID";

    public List<MasterDataBean> retrieveMasters() {
        int userPrecedence = loginDataBean.getPrecedence();
        Long companyId = loginDataBean.getCompanyId();
        List<HkMasterEntity> hkMasterEntitys = foundationService.retrieveMasters(companyId, (short) userPrecedence, null, Boolean.FALSE);
        List<MasterDataBean> masterDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hkMasterEntitys)) {
            for (HkMasterEntity hkMasterEntity : hkMasterEntitys) {
                MasterDataBean masterDataBean = new MasterDataBean();
                masterDataBean = this.convertHkMasterEntityToMasterDatabean(masterDataBean, hkMasterEntity, RETRIEVE_ALL);
                masterDataBeans.add(masterDataBean);
            }
        }
        return masterDataBeans;
    }

    private MasterDataBean convertHkMasterEntityToMasterDatabean(MasterDataBean masterDataBean, HkMasterEntity hkMasterEntity, String retrieve) {
        if (masterDataBean == null) {
            masterDataBean = new MasterDataBean();
        }
        if (retrieve.equalsIgnoreCase(RETRIEVE_ALL)) {
            Map<String, List<String>> mapOfFeatureUsedInMaster = HkSystemConstantUtil.MASTER_FEATURE_MAP;
            masterDataBean.setMasterName(hkMasterEntity.getMasterName());
            masterDataBean.setCode(hkMasterEntity.getCode());
            masterDataBean.setId(hkMasterEntity.getCode());
            masterDataBean.setIsSensitiveMaster(hkMasterEntity.getIsSensitive());
            masterDataBean.setMasterType(hkMasterEntity.getMasterType());
            if (!HkSystemConstantUtil.MASTER_FEATURE_MAP.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : mapOfFeatureUsedInMaster.entrySet()) {
                    String code = entry.getKey();
                    List<String> value = entry.getValue();
                    String replace = value.toString();
                    String usedInFeauture = replace.replaceAll("\\[", "").replaceAll("\\]", "");
                    if (masterDataBean.getCode().equalsIgnoreCase(code)) {
                        masterDataBean.setUsedInFeature(usedInFeauture.toString());
                    }
                }
            }
        } else {
            masterDataBean.setMasterName(hkMasterEntity.getMasterName());
            masterDataBean.setCode(hkMasterEntity.getCode());
            masterDataBean.setId(hkMasterEntity.getCode());
            masterDataBean.setIsSensitiveMaster(hkMasterEntity.getIsSensitive());
            masterDataBean.setMasterType(hkMasterEntity.getMasterType());
            List<HkValueEntity> hkValueEntitys = hkMasterEntity.getHkValueEntityList();
            if (!CollectionUtils.isEmpty(hkValueEntitys)) {
                List<MasterDataBean> masterDataBeans = new ArrayList<>();
                for (HkValueEntity hkValueEntity : hkValueEntitys) {
                    MasterDataBean dataBean = new MasterDataBean();
                    dataBean.setIsArchieve(!hkValueEntity.getIsActive());
                    dataBean.setIsOftenUsed(hkValueEntity.getIsOftenUsed());
                    dataBean.setShortcutCode(hkValueEntity.getShortcutCode());
                    String value = hkValueEntity.getValueName();
                    if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                        value = hkValueEntity.getTranslatedValueName();
                    }
                    dataBean.setValue(value);
                    dataBean.setValueEntityId(hkValueEntity.getId());
//                    dataBean.setId(hkValueEntity.getId().toString());
                    dataBean.setCodeValue(hkValueEntity.getValueName());
//                    TODO if map is null then initialize it by new map else it will not allow to set value in UI
                    if (hkValueEntity.getTranslateValueMap() == null) {
                        dataBean.setLangaugeIdNameMap(new HashMap<String, String>());
                    } else {
                        dataBean.setLangaugeIdNameMap(hkValueEntity.getTranslateValueMap());
                    }
                    masterDataBeans.add(dataBean);
                }
                masterDataBean.setMasterDataBeans(masterDataBeans);
            }
        }
        return masterDataBean;
    }

    public void update(MasterDataBean masterDataBean) {
        String code = masterDataBean.getCode();
        String password = masterDataBean.getPassword();
        Long companyId = loginDataBean.getCompanyId();
        Short userPrecedence = null;
        List<Long> notify = new ArrayList<>();
        if (loginDataBean.getPrecedence() != null) {
            userPrecedence = loginDataBean.getPrecedence().shortValue();
        }
        if (userPrecedence != null) {
            HkMasterEntity hkMasterEntityFromDb = foundationService.retrieveMaster(code, userPrecedence, loginDataBean.getCompanyId());
            List<String> oldKeys = new ArrayList<>();
            for (HkValueEntity hkValueEntity : hkMasterEntityFromDb.getHkValueEntityList()) {
                oldKeys.add(hkValueEntity.getValueName().replace(" ", ""));
            }
            HkMasterEntity convertMasterDataBeanToHkMasterEntity = this.convertMasterDataBeanToHkMasterEntity(masterDataBean, hkMasterEntityFromDb);
            Set<Long> searchUsersByFeatureName = userManagementServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_MASTER), companyId);
            if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                notify.addAll(searchUsersByFeatureName);
            }
            List<Long> retrieveUsersByRoleName = userManagementServiceWrapper.retrieveUsersByRoleName(HkSystemConstantUtil.ROLE.FRANCHISE_ADMIN, companyId);
            if (!CollectionUtils.isEmpty(retrieveUsersByRoleName)) {
                Iterator<Long> itr = retrieveUsersByRoleName.iterator();
                while (itr.hasNext()) {
                    Long userId = itr.next();
                    if (!CollectionUtils.isEmpty(notify) && notify.contains(userId)) {
                        itr.remove();
                    }
                }
                if (!CollectionUtils.isEmpty(notify)) {
                    Iterator<Long> notifyItr = notify.iterator();
                    while (notifyItr.hasNext()) {
                        Long userId = notifyItr.next();
                        if (userId.equals(loginDataBean.getId())) {
                            notifyItr.remove();
                            break;
                        }
                    }
                }
                notify.addAll(retrieveUsersByRoleName);
            }
            List<MasterDataBean> masterDataBeans = masterDataBean.getMasterDataBeans();
            foundationService.updateMaster(hkMasterEntityFromDb, notify, companyId, null);

            if (!CollectionUtils.isEmpty(oldKeys)) {
                List<String> newKeys = new ArrayList<>();
                for (MasterDataBean dataBean : masterDataBeans) {
                    newKeys.add(dataBean.getValue().replace(" ", ""));
                }
                for (String key : oldKeys) {
                    if (newKeys.indexOf(key) < 0) {
                        userManagementServiceWrapper.deleteLocaleForEntity(key, hkMasterEntityFromDb.getCode(), "MASTER", loginDataBean.getId(), companyId);
                    }
                }

            }
            localesTransformerBean.generateLanguagePropertyFiles(companyId);
        }
    }

    private HkMasterEntity convertMasterDataBeanToHkMasterEntity(MasterDataBean masterDataBean, HkMasterEntity hkMasterEntity) {
        Long companyId = loginDataBean.getCompanyId();
        Long userId = loginDataBean.getId();
        List<HkValueEntity> hkValueEntitySetFromDB = hkMasterEntity.getHkValueEntityList();
        hkMasterEntity.setIsSensitive(masterDataBean.isIsSensitiveMaster());
        List<MasterDataBean> masterDataBeans = masterDataBean.getMasterDataBeans();
        HkFieldEntity fieldEntity = new HkFieldEntity();
        if (hkMasterEntity.getMasterType() != null && hkMasterEntity.getMasterType().equals(MasterType.CUSTOM.value())) {
            fieldEntity = fieldService.retrieveCustomFieldByFieldId(Long.parseLong(masterDataBean.getCode()));
        }
        if (!CollectionUtils.isEmpty(hkValueEntitySetFromDB)) {
            Map<Long, HkValueEntity> valuesMap = new HashMap<>();
            for (HkValueEntity hve : hkValueEntitySetFromDB) {
                valuesMap.put(hve.getId(), hve);
            }
            for (MasterDataBean masterDataBean1 : masterDataBeans) {
                // match id
                HkValueEntity valueEntity = null;
                Long valueEntityId = masterDataBean1.getValueEntityId();
                if (valueEntityId != null) {
                    valueEntity = valuesMap.get(masterDataBean1.getValueEntityId());
                } else {
                    valueEntity = new HkValueEntity();
                    hkValueEntitySetFromDB.add(valueEntity);
                    valueEntity.setCreatedBy(userId);
                    valueEntity.setCreatedOn(new Date());
                    valueEntity.setIsArchive(false);
                    valueEntity.setKeyCode(hkMasterEntity);
                }
                if (hkMasterEntity.getMasterType().equals(MasterType.CUSTOM.value()) && fieldEntity.getIsPrivate() != null && fieldEntity.getIsPrivate().equals(Boolean.TRUE)) {
                    System.out.println("in private ");
                    // this case when custom field is private,So franchise and created by franchise would be of login user franchise
                    valueEntity.setFranchise(companyId);
                    valueEntity.setCreatedByFranchise(companyId);
                } else {
                    System.out.println("Shared");
                    // This case  when custom fiels is shared,So franchise would be of 0 and created by franchise would be login user franchise
                    valueEntity.setFranchise(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
                    valueEntity.setCreatedByFranchise(companyId);
                }
                String value = masterDataBean1.getValue();
                valueEntity.setTranslatedValueName(value);
                valueEntity.setIsOftenUsed(masterDataBean1.isIsOftenUsed());
                valueEntity.setLastModifiedBy(userId);
                valueEntity.setLastModifiedOn(new Date());
                valueEntity.setShortcutCode(masterDataBean1.getShortcutCode());
                valueEntity.setIsActive(!masterDataBean1.isIsArchieve());
                Map<String, String> langaugeIdNameMap = masterDataBean1.getLangaugeIdNameMap();
                if (CollectionUtils.isEmpty(langaugeIdNameMap)) {
                    langaugeIdNameMap = new HashMap<>();
                }
                langaugeIdNameMap.put(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, value);
                valueEntity.setValueName(value.replace(" ", ""));
                valueEntity.setTranslateValueMap(langaugeIdNameMap);
            }
        } else {
            hkValueEntitySetFromDB = new ArrayList<>();
            for (MasterDataBean masterDataBean1 : masterDataBeans) {
                // match id
                String value = masterDataBean1.getValue();
                HkValueEntity valueEntity = new HkValueEntity();
                valueEntity.setCreatedBy(userId);
                valueEntity.setCreatedOn(new Date());
                valueEntity.setIsArchive(false);
                valueEntity.setKeyCode(hkMasterEntity);
                valueEntity.setTranslatedValueName(value);
                if (fieldEntity.getIsPrivate() != null && fieldEntity.getIsPrivate().equals(Boolean.TRUE)) {
                    System.out.println("in private ");
                    // this case when custom field is private,So franchise and created by franchise would be of login user franchise
                    valueEntity.setFranchise(companyId);
                    valueEntity.setCreatedByFranchise(companyId);
                } else {
                    System.out.println("Shared");
                    // This case  when custom fiels is shared,So franchise would be of 0 and created by franchise would be login user franchise
                    valueEntity.setFranchise(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
                    valueEntity.setCreatedByFranchise(companyId);
                }
                valueEntity.setIsOftenUsed(masterDataBean1.isIsOftenUsed());
                valueEntity.setLastModifiedBy(userId);
                valueEntity.setLastModifiedOn(new Date());
                valueEntity.setShortcutCode(masterDataBean1.getShortcutCode());
                valueEntity.setIsActive(!masterDataBean1.isIsArchieve());
                Map<String, String> langaugeIdNameMap = masterDataBean1.getLangaugeIdNameMap();
                if (langaugeIdNameMap == null) {
                    langaugeIdNameMap = new HashMap<>();
                }
                langaugeIdNameMap.put(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH, value);
                valueEntity.setValueName(value.replace(" ", ""));
                valueEntity.setTranslateValueMap(langaugeIdNameMap);
                hkValueEntitySetFromDB.add(valueEntity);
            }
            hkMasterEntity.setHkValueEntityList(hkValueEntitySetFromDB);
        }

        return hkMasterEntity;
    }

    public MasterDataBean retrieveMasterById(String code) {
        int precedence = loginDataBean.getPrecedence();
        HkMasterEntity hkMasterEntity = foundationService.retrieveMaster(code, (short) precedence, loginDataBean.getCompanyId());
        MasterDataBean masterDataBean = new MasterDataBean();
        masterDataBean = this.convertHkMasterEntityToMasterDatabean(masterDataBean, hkMasterEntity, RETRIEVE_BY_ID);
        return masterDataBean;
    }

    public List<MasterDataBean> searchMaster(String searchStringByMasterName) {
        Long companyId = loginDataBean.getCompanyId();
        int precedence = loginDataBean.getPrecedence();
        List<MasterDataBean> masterDataBeans = new ArrayList<>();
        List<HkMasterEntity> searchMasters = foundationService.searchMaster(companyId, searchStringByMasterName, (short) precedence);
        if (!CollectionUtils.isEmpty(searchMasters)) {
            Map<String, List<String>> mapOfFeatureUsedInMaster = HkSystemConstantUtil.MASTER_FEATURE_MAP;
            for (HkMasterEntity hkMasterEntity : searchMasters) {
                if (!hkMasterEntity.getIsArchive()) {
                    MasterDataBean masterDataBean = new MasterDataBean();
                    masterDataBean = this.convertHkMasterEntityToMasterDataBean(masterDataBean, hkMasterEntity);
                    if (!HkSystemConstantUtil.MASTER_FEATURE_MAP.isEmpty()) {
                        for (Map.Entry<String, List<String>> entry : mapOfFeatureUsedInMaster.entrySet()) {
                            String code = entry.getKey();
                            List<String> value = entry.getValue();
                            String replace = value.toString();
                            String usedInFeauture = replace.replaceAll("\\[", "").replaceAll("\\]", "");
                            if (masterDataBean.getId().equalsIgnoreCase(code)) {
                                masterDataBean.setUsedInFeature(usedInFeauture.toString());
                            }
                        }
                    }
                    masterDataBeans.add(masterDataBean);
                }
            }
        }
        return masterDataBeans;
    }

    private MasterDataBean convertHkMasterEntityToMasterDataBean(MasterDataBean masterDataBean, HkMasterEntity hkMasterEntity) {
        if (masterDataBean == null) {
            masterDataBean = new MasterDataBean();
        }
        masterDataBean.setId(hkMasterEntity.getCode());
        masterDataBean.setMasterName(hkMasterEntity.getMasterName());
        return masterDataBean;
    }

    public boolean authenticateForEditMaster(String password) {
        HkSystemConfigurationEntity systemConfigurationEntity = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.MASTERS_AUTH_PWD, loginDataBean.getCompanyId());
        if (systemConfigurationEntity != null) {
            return basicPasswordEncryptor.checkPassword(password, systemConfigurationEntity.getKeyValue());
        }
        return false;
    }

    public Map<String, String> retrieveLanguages() {
        Map<String, String> langageIdNameMap = new HashMap<>();
        List<I18nLanguageEntity> retriveActiveLanguages = i18nService.retriveActiveLanguages();
        if (!CollectionUtils.isEmpty(retriveActiveLanguages)) {
            for (I18nLanguageEntity i18nLanguageEntity : retriveActiveLanguages) {
                if (!i18nLanguageEntity.getLanguagePK().getCode().equalsIgnoreCase(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH)) {
                    langageIdNameMap.put(i18nLanguageEntity.getLanguagePK().getCode(), i18nLanguageEntity.getName());
                }
            }
        }
        return langageIdNameMap;
    }

    public void initMasters() {
        List<HkMasterEntity> masterEntitys = foundationService.retrieveMasters(0l, (short) 2, HkSystemConstantUtil.MasterType.BUILT_IN, Boolean.FALSE);
        List<HkMasterEntity> finalMasterList = new ArrayList<>();
        List<String> exsitingcodeList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(masterEntitys)) {
            for (HkMasterEntity hkMasterEntity : masterEntitys) {
                exsitingcodeList.add(hkMasterEntity.getCode());
            }
        }
        for (Map.Entry<String, String> entry : HkSystemConstantUtil.MASTERS_MAP.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            if (!exsitingcodeList.contains(code)) {
                HkMasterEntity hkMasterEntity = this.convertMasterMapToMasterEntity(code, name);
                finalMasterList.add(hkMasterEntity);
            }
        }
        if (!CollectionUtils.isEmpty(finalMasterList)) {
            foundationService.createAllMasters(finalMasterList);
        }
    }

    private HkMasterEntity convertMasterMapToMasterEntity(String code, String name) {
        HkMasterEntity hkMasterEntity = new HkMasterEntity();
        hkMasterEntity.setCode(code);
        hkMasterEntity.setCreatedBy(1L);
        hkMasterEntity.setCreatedOn(new Date());
        hkMasterEntity.setDescription("");
        hkMasterEntity.setFranchise(0L);
        hkMasterEntity.setIsActive(true);
        hkMasterEntity.setIsArchive(false);
        hkMasterEntity.setIsSensitive(false);
        hkMasterEntity.setLastModifiedBy(1L);
        hkMasterEntity.setLastModifiedOn(new Date());
        hkMasterEntity.setMasterName(name);
        hkMasterEntity.setMasterType(HkSystemConstantUtil.MasterType.BUILT_IN);
        hkMasterEntity.setPrecedence(Short.valueOf(HkSystemConstantUtil.PrecedenceLevel.LEVEL_3_REGULAR));
        return hkMasterEntity;
    }
// <ethod added by Shifa

    @SuppressWarnings("fallthrough")
    public List<SelectItem> retrieveAllValuesForMasters(String keyCode, String searchQuery) {
//        List<String> keyCodes = new ArrayList<>();
//        keyCodes.add(keyCode.toString());
        List<SelectItem> selectItems = null;
        List<Long> forValues = null;
        List<HkValueExceptionDocument> hkValueExceptionDocuments = hkFoundationDocumentService.retrieveValueExceptions(Long.parseLong(keyCode), loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(hkValueExceptionDocuments)) {
            forValues = new ArrayList<>();
            for (HkValueExceptionDocument hkValueExceptionDocument : hkValueExceptionDocuments) {
                // Condition when exception contain only for user.Avoid The xception where for user and dependant value are there
                if (!CollectionUtils.isEmpty(hkValueExceptionDocument.getForUsers()) && CollectionUtils.isEmpty(hkValueExceptionDocument.getDependsOnValueList())) {

                    List<String> forUsers = hkValueExceptionDocument.getForUsers();
                    Boolean isExceptionSatisfied = false;
                    for (String forUser : forUsers) {
                        String[] splitArray = forUser.replace("\"", "").split("\\:");
                        if (null != splitArray[1]) {
                            switch (splitArray[1]) {
                                case "E":
                                    if (loginDataBean.getId().toString().equals(splitArray[0])) {
                                        isExceptionSatisfied = true;
                                    }
                                    break;
                                case "D":

                                    if (loginDataBean.getDepartment() != null && loginDataBean.getDepartment().toString().equals(splitArray[0])) {
                                        isExceptionSatisfied = true;
                                    }
                                    break;
                                case "R":
                                    if (loginDataBean.getRoleIds().contains(Long.parseLong(splitArray[0]))) {
                                        isExceptionSatisfied = true;
                                    }
                                case "All":
                                    // FOr All users
                                    isExceptionSatisfied = true;
                                    break;
                            }
                        }

                    }
                    if (isExceptionSatisfied == true) {

                        forValues.addAll(hkValueExceptionDocument.getForValues());
                    }
                }
            }

        }

        List<HkValueEntity> valueEntities = foundationService.retrieveMasterValuesByCodeForCustom(loginDataBean.getCompanyId(), keyCode, searchQuery);

        if (!CollectionUtils.isEmpty(valueEntities)) {
            selectItems = new ArrayList<>();
            for (HkValueEntity valueEntity : valueEntities) {
                Boolean isAddValue = false;

                if (!CollectionUtils.isEmpty(forValues) && valueEntity.getId() != null) {
                    if (forValues.contains(valueEntity.getId()) || forValues.contains(0l)) {
                        isAddValue = true;
                    }
                } else {
                    isAddValue = true;
                }
                if (isAddValue) {
                    SelectItem selectItem = new SelectItem(valueEntity.getId(), valueEntity.getValueName());
                    selectItem.setShortcutCode(valueEntity.getShortcutCode());
                    selectItem.setIsActive(valueEntity.getIsOftenUsed());
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }

    public FieldDataBean convertFieldEntityToFieldDataBean(HkFieldEntity fieldEntity, FieldDataBean fieldDataBean) {
        fieldDataBean.setComponentType(fieldEntity.getComponentType());
        fieldDataBean.setDbFieldName(fieldEntity.getDbFieldName());
        fieldDataBean.setFieldLabel(fieldEntity.getFieldLabel());
        fieldDataBean.setEditedFieldLabel(fieldEntity.getFieldLabel().toLowerCase().replaceAll("\\s+", ""));
        fieldDataBean.setFieldType(fieldEntity.getFieldType());
        fieldDataBean.setId(fieldEntity.getId());
        fieldDataBean.setUiFieldName(fieldEntity.getUiFieldName());
        fieldDataBean.setValidationPattern(fieldEntity.getValidationPattern());
        fieldDataBean.setDbBaseName(fieldEntity.getDbBaseName());
        fieldDataBean.setDbBaseType(fieldEntity.getDbBaseType());
        fieldDataBean.setFeature(fieldEntity.getFeature());
        fieldDataBean.setAssociatedCurrency(fieldEntity.getAssociatedCurrency());
        fieldDataBean.setIsSubFormValue(Boolean.FALSE);
        return fieldDataBean;
    }

    public List<SelectItem> retrieveCustomFields(Long dependentOnField) {
        Long companyId = loginDataBean.getCompanyId();
        List<String> componentTypes = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();

        Long feature = null;
        Set<Long> valueExceptionFieldIds = new HashSet<>();
        Set<Long> finalValueExceptionFieldIds = new HashSet<>();
        HkFieldEntity hkFieldEntity = fieldService.retrieveCustomFieldByFieldId(dependentOnField);
        if (hkFieldEntity != null) {
            feature = hkFieldEntity.getFeature();
        }

        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY);
        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN);
        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN);

        List<HkFieldEntity> fieldEntitys = fieldService.retrieveCustomFieldEntitysByComponentTypes(componentTypes, companyId, Arrays.asList(feature));
        Set<Long> fieldIds = new HashSet<>();
        fieldIds.add(dependentOnField);
        while (fieldIds.size() > 0) {
            valueExceptionFieldIds = this.checkCircularException(fieldIds, companyId);
            fieldIds = new HashSet<>();
            if(valueExceptionFieldIds.size() > 0){
                fieldIds.addAll(valueExceptionFieldIds);
                finalValueExceptionFieldIds.addAll(valueExceptionFieldIds);
            }
        }
        System.out.println("valueExceptionFieldIds :" + valueExceptionFieldIds);
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            for (HkFieldEntity fieldEntity : fieldEntitys) {
                if (!fieldEntity.getId().equals(dependentOnField) && !finalValueExceptionFieldIds.contains(fieldEntity.getId()) && !fieldEntity.getDbFieldName().equals("machine_to_process_in$DRP$Long") && !fieldEntity.getDbFieldName().equals("carate_range_of_plan$DRP$Long")) {
                    SelectItem selectItem = new SelectItem(fieldEntity.getId(), fieldEntity.getFieldLabel());
                    selectItem.setCommonId(fieldEntity.getDbFieldName());
                    selectItem.setDescription(fieldEntity.getComponentType());
                    selectItems.add(selectItem);
                }

            }
        }
        return selectItems;
    }

    public Set<Long> checkCircularException(Set<Long> fieldIds, Long companyId) {
        Set<Long> valueExceptionFieldIds = new HashSet<>();
        List<HkValueExceptionDocument> hkValueExceptionDocuments = hkFoundationDocumentService.retrieveValueExceptionsByCriteria(fieldIds, companyId);
        if (!CollectionUtils.isEmpty(hkValueExceptionDocuments)) {
            for (HkValueExceptionDocument hkValueExceptionDocument : hkValueExceptionDocuments) {
                valueExceptionFieldIds.add(hkValueExceptionDocument.getInstanceId());
            }
        }
        return valueExceptionFieldIds;
    }

    public List<SelectItem> retrieveCustomFieldsValueByKey(Long fieldId, String componentType) {
        List<SelectItem> valueList = new ArrayList<>();
        if (fieldId != null && !StringUtils.isEmpty(componentType)) {
            if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                HkMasterEntity hkMasterEntity = foundationService.retrieveMaster(fieldId.toString(), (short) 0);
                if (hkMasterEntity != null && hkMasterEntity.getHkValueEntityList() != null) {
                    for (HkValueEntity hkValueEntity : hkMasterEntity.getHkValueEntityList()) {
                        SelectItem selectItem = new SelectItem(hkValueEntity.getId(), hkValueEntity.getValueName());
                        selectItem.setShortcutCode(hkValueEntity.getShortcutCode());
                        valueList.add(selectItem);
                    }
                }
            } else if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                valueList = this.createDropDownListForSubEntity(fieldId);
            }
        }
        return valueList;
    }

    public List<SelectItem> createDropDownListForSubEntity(long hkFieldEntityId) {
        List<SelectItem> selectItemList = null;
        HkFieldEntity fieldEntity = fieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        Map<String, HkSubFormFieldEntity> SubEntityDropField = new HashMap<>();
        List<HkSubFormFieldEntity> listOfSubEntities = fieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            for (HkSubFormFieldEntity listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    SubEntityDropField.put(listOfSubEntity.getSubFieldName(), listOfSubEntity);
                }
            }
        }
        Map<ObjectId, String> dropDownValuesForSubEntity = new HashMap<>();
        List<HkSubFormValueDocument> subFormDocuments = customFieldService.retrieveSubFormValueByInstance(hkFieldEntityId, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(subFormDocuments)) {
            selectItemList = new ArrayList<>();
            for (HkSubFormValueDocument subFormDocument : subFormDocuments) {
                Map<String, Object> fieldValueMap = subFormDocument.getFieldValue().toMap();
                for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                    if (SubEntityDropField.containsKey(entrySet.getKey())) {
                        SelectItem selectItem = new SelectItem(subFormDocument.getId().toString(), entrySet.getValue().toString());
                        selectItemList.add(selectItem);
                        dropDownValuesForSubEntity.put(subFormDocument.getId(), entrySet.getValue().toString());
                    }
                }
            }

        }
        return selectItemList;
    }

    public Map<ObjectId, String> mapOfSubEntityValueIdName(long hkFieldEntityId) {
        List<SelectItem> selectItemList = null;
        HkFieldEntity fieldEntity = fieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        Map<String, HkSubFormFieldEntity> SubEntityDropField = new HashMap<>();
        List<HkSubFormFieldEntity> listOfSubEntities = fieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            for (HkSubFormFieldEntity listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    SubEntityDropField.put(listOfSubEntity.getSubFieldName(), listOfSubEntity);
                }
            }
        }
        Map<ObjectId, String> dropDownValuesForSubEntity = new HashMap<>();
        List<HkSubFormValueDocument> subFormDocuments = customFieldService.retrieveSubFormValueByInstance(hkFieldEntityId, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(subFormDocuments)) {
            selectItemList = new ArrayList<>();
            for (HkSubFormValueDocument subFormDocument : subFormDocuments) {
                Map<String, Object> fieldValueMap = subFormDocument.getFieldValue().toMap();
                for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                    if (SubEntityDropField.containsKey(entrySet.getKey())) {
                        SelectItem selectItem = new SelectItem(subFormDocument.getId().toString(), entrySet.getValue().toString());
                        selectItemList.add(selectItem);
                        dropDownValuesForSubEntity.put(subFormDocument.getId(), entrySet.getValue().toString());
                    }
                }
            }

        }
        return dropDownValuesForSubEntity;
    }

    public HkValueExceptionDocument convertValueExceptionDataBeanToDocument(HkValueExceptionDocument hkValueExceptionDocument, ValueExceptionDataBean valueExceptionDataBean) {
        if (!StringUtils.isEmpty(valueExceptionDataBean.getForValue())) {
            List<Long> forValues = new ArrayList<>();
            List<String> forValuesStrng = Arrays.asList(valueExceptionDataBean.getForValue().split(","));
            if (!CollectionUtils.isEmpty(forValuesStrng)) {
                for (String forVal : forValuesStrng) {
                    forValues.add(new Long(forVal));
                }
                hkValueExceptionDocument.setForValues(forValues);
            }

        }
        if (!StringUtils.isEmpty(valueExceptionDataBean.getForUsers())) {
            List<String> forUsers = Arrays.asList(valueExceptionDataBean.getForUsers().split(","));
            if (!CollectionUtils.isEmpty(forUsers)) {
                hkValueExceptionDocument.setForUsers(forUsers);
            }
        }
        if (!StringUtils.isEmpty(valueExceptionDataBean.getDependentOnField()) && !StringUtils.isEmpty(valueExceptionDataBean.getDependentOnFieldValues())) {
            String dependentOn = valueExceptionDataBean.getDependentOnField();
            String[] split = dependentOn.split("\\|");
            if (split.length > 0) {
                hkValueExceptionDocument.setFieldId(new Long(split[0]));
                hkValueExceptionDocument.setFieldType(split[1]);
                List<String> dependentOnFieldValues = Arrays.asList(valueExceptionDataBean.getDependentOnFieldValues().split(","));
                if (split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                    List<ObjectId> subEntityValues = new ArrayList<>();
                    for (String dependentOnFieldValue : dependentOnFieldValues) {
                        subEntityValues.add(new ObjectId(dependentOnFieldValue));
                    }
                    hkValueExceptionDocument.setDependsOnSubValueList(subEntityValues);
                } else if (split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                    List<Long> dropdownValues = new ArrayList<>();
                    for (String dependentOnFieldValue : dependentOnFieldValues) {
                        dropdownValues.add(new Long(dependentOnFieldValue));
                    }
                    hkValueExceptionDocument.setDependsOnValueList(dropdownValues);
                }
            }
        }
        Date currentDate = new Date();
        if (valueExceptionDataBean.getId() == null) {
            hkValueExceptionDocument.setCreatedBy(loginDataBean.getId());
            hkValueExceptionDocument.setCreatedOn(currentDate);
            hkValueExceptionDocument.setIsArchive(Boolean.FALSE);
        } else {
            hkValueExceptionDocument.setId(new ObjectId(valueExceptionDataBean.getId()));
            hkValueExceptionDocument.setIsArchive(valueExceptionDataBean.getIsArchive());
        }
        hkValueExceptionDocument.setInstanceId(valueExceptionDataBean.getInstanceId());
        hkValueExceptionDocument.setLastModifiedOn(currentDate);
        hkValueExceptionDocument.setLastModifiedBy(loginDataBean.getId());
        hkValueExceptionDocument.setFranchise(loginDataBean.getCompanyId());
        return hkValueExceptionDocument;
    }

    public ValueExceptionDataBean convertValueExceptionDocumentToDataBean(HkValueExceptionDocument hkValueExceptionDocument, ValueExceptionDataBean valueExceptionDataBean) {
        valueExceptionDataBean.setInstanceId(hkValueExceptionDocument.getInstanceId());
        valueExceptionDataBean.setId(hkValueExceptionDocument.getId().toString());
        Map<String, String> userCodeName = userManagementServiceWrapper.retrieveRecipientNames(hkValueExceptionDocument.getForUsers());
        if (!CollectionUtils.isEmpty(hkValueExceptionDocument.getForUsers())) {
            String forUserStrng = "";
            String userToBeDisplay = "";
            for (String forUser : hkValueExceptionDocument.getForUsers()) {
                if (!StringUtils.isEmpty(forUserStrng)) {
                    forUserStrng = forUserStrng + "," + forUser;
                } else {
                    forUserStrng = forUserStrng + forUser;
                }
                if (!StringUtils.isEmpty(userToBeDisplay)) {
                    if (forUser.equals(HkSystemConstantUtil.ALL_USER_CODE)) {
                        userToBeDisplay = userToBeDisplay + "," + forUser.split(":")[1];
                    } else {
                        userToBeDisplay = userToBeDisplay + "," + userCodeName.get(forUser);
                    }
                } else {
                    if (forUser.equals(HkSystemConstantUtil.ALL_USER_CODE)) {
                        userToBeDisplay = userToBeDisplay + forUser.split(":")[1];
                    } else {
                        userToBeDisplay = userToBeDisplay + userCodeName.get(forUser);
                    }

                }
            }
            valueExceptionDataBean.setForUsers(forUserStrng);
            valueExceptionDataBean.setUserToBeDisplay(userToBeDisplay);
        }
        if (!CollectionUtils.isEmpty(hkValueExceptionDocument.getForValues())) {
            Map<Long, HkValueEntity> map = foundationService.retrieveMapOfIdAndValueEntity(hkValueExceptionDocument.getForValues());
            String forValueStrng = "";
            String valueToBeDisplay = "";
            for (Long forValue : hkValueExceptionDocument.getForValues()) {
                if (!StringUtils.isEmpty(forValueStrng)) {
                    forValueStrng = forValueStrng + "," + forValue;
                } else {
                    forValueStrng = forValueStrng + forValue;
                }
                if (!StringUtils.isEmpty(valueToBeDisplay)) {
                    if (forValue.equals(0l)) {
                        valueToBeDisplay = valueToBeDisplay + "," + "All";
                    } else {
                        valueToBeDisplay = valueToBeDisplay + "," + map.get(forValue).getValueName();
                    }
                } else {
                    if (forValue.equals(0l)) {
                        valueToBeDisplay = valueToBeDisplay + "All";
                    } else {
                        valueToBeDisplay = valueToBeDisplay + map.get(forValue).getValueName();
                    }

                }
            }
            valueExceptionDataBean.setForValue(forValueStrng);
            valueExceptionDataBean.setValueToBeDisplay(valueToBeDisplay);
        }
        if (hkValueExceptionDocument.getFieldId() != null && hkValueExceptionDocument.getFieldType() != null) {
            HkFieldEntity fieldEntity = fieldService.retrieveCustomFieldByFieldId(hkValueExceptionDocument.getFieldId());
            valueExceptionDataBean.setFieldId(hkValueExceptionDocument.getFieldId());
            valueExceptionDataBean.setFieldType(hkValueExceptionDocument.getFieldType());
            valueExceptionDataBean.setDependentOnToBeDisplay(fieldEntity.getFieldLabel());
            valueExceptionDataBean.setDependentOnField(hkValueExceptionDocument.getFieldId() + "|" + hkValueExceptionDocument.getFieldType());
            String dependentOnFieldValues = "";
            String dependentOnFieldValuesToBeDisplay = "";
            if (hkValueExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY) && !CollectionUtils.isEmpty(hkValueExceptionDocument.getDependsOnSubValueList())) {
                Map<ObjectId, String> subValueIdMap = this.mapOfSubEntityValueIdName(hkValueExceptionDocument.getFieldId());
                for (ObjectId dependsOnSubValueList : hkValueExceptionDocument.getDependsOnSubValueList()) {
                    if (!StringUtils.isEmpty(dependentOnFieldValues)) {
                        dependentOnFieldValues = dependentOnFieldValues + "," + dependsOnSubValueList.toString();
                    } else {
                        dependentOnFieldValues = dependentOnFieldValues + dependsOnSubValueList.toString();
                    }
                    if (!CollectionUtils.isEmpty(subValueIdMap) && subValueIdMap.containsKey(dependsOnSubValueList) && valueExceptionDataBean.getDependentOnToBeDisplay() != null) {
                        if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + subValueIdMap.get(dependsOnSubValueList);
                        } else {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + subValueIdMap.get(dependsOnSubValueList);
                        }
                    } else {
                        if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + "N/A";
                        } else {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + "N/A";
                        }
                    }

                }
                valueExceptionDataBean.setDependentOnFieldValuesToBeDisplay(dependentOnFieldValuesToBeDisplay);
            } else if (hkValueExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || hkValueExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) && !CollectionUtils.isEmpty(hkValueExceptionDocument.getDependsOnValueList())) {
                List<HkValueEntity> hkValueEntitys = foundationService.retrieveValueEntityListByValueIds(hkValueExceptionDocument.getDependsOnValueList());
                for (Long dependsOnValueList : hkValueExceptionDocument.getDependsOnValueList()) {
                    for (HkValueEntity hkValueEntity : hkValueEntitys) {
                        if (hkValueEntity.getId().equals(dependsOnValueList)) {
                            if (!StringUtils.isEmpty(dependentOnFieldValues)) {
                                dependentOnFieldValues = dependentOnFieldValues + "," + dependsOnValueList.toString();
                            } else {
                                dependentOnFieldValues = dependentOnFieldValues + dependsOnValueList.toString();
                            }
                            if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + hkValueEntity.getValueName();
                            } else {
                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + valueExceptionDataBean.getDependentOnToBeDisplay() + "-" + hkValueEntity.getValueName();
                            }
                        }
                    }
                }
                valueExceptionDataBean.setDependentOnFieldValuesToBeDisplay(dependentOnFieldValuesToBeDisplay);
            }
            valueExceptionDataBean.setDependentOnFieldValues(dependentOnFieldValues);
        }
        return valueExceptionDataBean;
    }

    public void saveException(List<ValueExceptionDataBean> valueExceptionDataBeans) {
        if (!CollectionUtils.isEmpty(valueExceptionDataBeans)) {
            List<HkValueExceptionDocument> hkValueExceptionDocuments = new ArrayList<>();
            for (ValueExceptionDataBean valueExceptionDataBean : valueExceptionDataBeans) {
                HkValueExceptionDocument hkValueExceptionDocument = new HkValueExceptionDocument();
                hkValueExceptionDocument = this.convertValueExceptionDataBeanToDocument(hkValueExceptionDocument, valueExceptionDataBean);
                hkValueExceptionDocuments.add(hkValueExceptionDocument);
            }
            hkFoundationDocumentService.saveValueExceptions(hkValueExceptionDocuments);
        }
        try {
            customFieldTransformerBean.createFeatureSectionMap(Boolean.TRUE, null);
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(MasterTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<ValueExceptionDataBean> retrieveValueExceptions(Long instanceId) {
        Long companyId = loginDataBean.getCompanyId();
        List<ValueExceptionDataBean> valueExceptionDataBeans = new ArrayList<>();
        if (instanceId != null) {
            List<HkValueExceptionDocument> hkValueExceptionDocuments = hkFoundationDocumentService.retrieveValueExceptions(instanceId, companyId);
            if (!CollectionUtils.isEmpty(hkValueExceptionDocuments)) {
                for (HkValueExceptionDocument hkValueExceptionDocument : hkValueExceptionDocuments) {
                    ValueExceptionDataBean valueExceptionDataBean = new ValueExceptionDataBean();
                    valueExceptionDataBean = this.convertValueExceptionDocumentToDataBean(hkValueExceptionDocument, valueExceptionDataBean);
                    valueExceptionDataBeans.add(valueExceptionDataBean);
                }
            }
        }
        return valueExceptionDataBeans;
    }

    public Map<String, Object> retrievePrerequisite(Long instanceId) {
        Map<String, Object> prerequisiteMap = new HashMap<>();
        if (instanceId != null) {
            List<SelectItem> customFields = this.retrieveCustomFields(instanceId);
            if (!CollectionUtils.isEmpty(customFields)) {
                prerequisiteMap.put("dependentFieldList", customFields);
            }
            List<ValueExceptionDataBean> valueExceptionDataBeans = this.retrieveValueExceptions(instanceId);
            if (!CollectionUtils.isEmpty(valueExceptionDataBeans)) {
                prerequisiteMap.put("valueExceptionDataBeans", valueExceptionDataBeans);
            }
        }
        return prerequisiteMap;
    }

}
