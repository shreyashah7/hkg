    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.customfield.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.impl.HkCustomFieldServiceImpl;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSubEntityExceptionDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.nosql.model.SectionDocument;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldInfoDataBean;
import com.argusoft.hkg.web.customfield.databeans.DependentFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.FeatureDetailDataBean;
import com.argusoft.hkg.web.customfield.databeans.SectionDetailDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityValueDataBean;
import com.argusoft.hkg.web.customfield.databeans.SubEntityValueExceptionDataBean;
import com.argusoft.hkg.web.sync.listner.SyncRecordedEntity;
import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.CommonUtil;
import com.argusoft.hkg.web.util.HkSelect2DataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.argusoft.sync.center.model.CenterCustomFieldDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.core.UMSystemConfigurationService;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMSystemConfiguration;
import com.argusoft.usermanagement.common.model.UMUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author harshit This methods consists of methods use for the Custom Fields.
 */
@Service
public class CustomFieldTransformerBean {

    @Autowired
    private UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    private HkFieldService hkFieldService;
    @Autowired
    private HkCustomFieldService customFieldService;
    @Autowired
    private LoginDataBean loginDataBean;
    @Autowired
    private HkFoundationService hkFoundationService;
    @Autowired
    FeatureTransformerBean featureTransformerBean;
    @Autowired
    private UMFeatureService featureService;
    @Autowired
    private HkStockService stockService;
    @Autowired
    private SyncCenterUserService syncCenterUserService;
    @Autowired
    private HkFoundationDocumentService hkFoundationDocumentService;
    @Autowired
    private HkConfigurationService hkConfigurationService;
    @Autowired
    private UMSystemConfigurationService systemConfigurationService;

    @Autowired
    private SyncXmppClient xmppClient;

    @Autowired
    private ApplicationUtil applicationUtil;

    public Map<String, Object> retrieveCustomFieldPagePrerequisite() {

        List<UMFeature> allUMFeatures = userManagementServiceWrapper.retrieveAllMainFeature(loginDataBean.getCompanyId());
        Map<String, Object> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(allUMFeatures)) {
            List<SelectItem> hkSelectItemsForAllFeature = new ArrayList<>();
            List<SelectItem> hkSelectItemsForAllFeatureWithEntityType = new ArrayList<>();
            Map<Long, String> featureIdAndFeatureMap = new HashMap<>();
            allUMFeatures.stream().filter((uMFeature) -> (!HkSystemConstantUtil.CustomField.EXCLUDE_FEATURES_LIST.contains(uMFeature.getName()))).forEach((uMFeature) -> {
                SelectItem hkSelectItem = new SelectItem(uMFeature.getId(), uMFeature.getMenuLabel());
                hkSelectItem.setDescription(uMFeature.getMenuType());
                if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU)) {
                    hkSelectItem.setIsActive(true);
                } else {
                    hkSelectItem.setIsActive(false);
                    hkSelectItemsForAllFeatureWithEntityType.add(hkSelectItem);

                }
                hkSelectItemsForAllFeature.add(hkSelectItem);
                featureIdAndFeatureMap.put(uMFeature.getId(), uMFeature.getMenuLabel());
            });
            resultMap.put("allFeature", hkSelectItemsForAllFeature);
            //MM-ISSUE-FOR-SHIFA No need to send separate SelectItems for Entity, can manage with first one
            resultMap.put("entityFeature", hkSelectItemsForAllFeatureWithEntityType);
            Set<Long> exitingCustomField = hkFieldService.retrieveFeaturesForExistingFields(true, loginDataBean.getCompanyId());
            if (!CollectionUtils.isEmpty(exitingCustomField)) {
                List<TreeViewDataBean> hkExistingCustomField = new ArrayList<>();
                for (Long featureId : exitingCustomField) {
                    String featureName = featureIdAndFeatureMap.get(featureId);
                    if (featureName != null) {
                        TreeViewDataBean hkTreeViewDataBean = new TreeViewDataBean();
                        hkTreeViewDataBean.setId(featureId);
                        hkTreeViewDataBean.setText(featureName);
                        hkTreeViewDataBean.setDisplayName(featureName);
                        hkExistingCustomField.add(hkTreeViewDataBean);
                    }
                }
                resultMap.put("exitingFeature", hkExistingCustomField);
            }
        }
        resultMap.put("conditionalOperatorList", CommonUtil.convertStringMapToSelectItem(HkSystemConstantUtil.CustomField.CONDITIONAL_OPERATORS_MAP));
        resultMap.put("arithemeticOperatorsList", CommonUtil.convertStringMapToSelectItem(HkSystemConstantUtil.CustomField.ARITHEMETIC_OPERATORS_MAP));
        return resultMap;
    }

    public Map<String, Long> createCustomFields(CustomFieldInfoDataBean customFieldInfoDataBean) throws GenericDatabaseException {
        List<HkFieldEntity> hkFieldEntitys;
        Map<String, Long> customFieldIdMap = new HashMap<>();
        List<CustomFieldDataBean> customFieldDataBeans = Arrays.asList(customFieldInfoDataBean.getCustomFieldDataBeans());
        if (!CollectionUtils.isEmpty(customFieldDataBeans)) {
            hkFieldEntitys = new ArrayList<>();
            for (CustomFieldDataBean customFieldDataBean : customFieldDataBeans) {
                HkFieldEntity hkFieldEntity = new HkFieldEntity();
                hkFieldEntity.setId(customFieldDataBean.getId());
                hkFieldEntity.setLastModifiedBy(loginDataBean.getId());
                hkFieldEntity.setLastModifiedOn(new Date());
                if (customFieldDataBean.getId() == null) {
                    hkFieldEntity.setCreatedOn(new Date());
                    hkFieldEntity.setCreatedBy(loginDataBean.getId());
                }
                hkFieldEntity.setFieldLabel(customFieldDataBean.getLabel());
                hkFieldEntity.setComponentType(customFieldDataBean.getType());
                hkFieldEntity.setValidationPattern(customFieldDataBean.getValidationPattern());
                hkFieldEntity.setIsCustomField(true);
                if (customFieldInfoDataBean.getSectionId() != -1l) {
                    hkFieldEntity.setSection(new HkSectionEntity(customFieldInfoDataBean.getSectionId()));
                }
                hkFieldEntity.setFeature(customFieldInfoDataBean.getFeatureId());
                hkFieldEntity.setFieldValues(customFieldDataBean.getValues());
                hkFieldEntity.setFranchise(loginDataBean.getCompanyId());
                hkFieldEntity.setSeqNo(customFieldDataBean.getSeqNo());
                hkFieldEntitys.add(hkFieldEntity);
            }

            //---------- Added by dhwani 17/12 start---------------------------
            String dbdocumentName = "";
            if (customFieldInfoDataBean.getSectionId() == -1l) {
                try {
                    UMFeature feature = featureService.retrieveFeatureById(customFieldInfoDataBean.getFeatureId(), null);
                    if (feature != null) {
                        if (HkSystemConstantUtil.featureDocumentMap.containsKey(feature.getName())) {
                            dbdocumentName = HkSystemConstantUtil.featureDocumentMap.get(feature.getName());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    Logger.getLogger(CustomFieldTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                HkSectionEntity sectionEntity = hkFieldService.retrieveSectionById(customFieldInfoDataBean.getSectionId(), false);
                if (sectionEntity != null) {
                    if (HkSystemConstantUtil.sectionDocumentMap.containsKey(sectionEntity.getSectionName())) {
                        dbdocumentName = HkSystemConstantUtil.sectionDocumentMap.get(sectionEntity.getSectionName());
                    }
                }
            }

            hkFieldService.saveFields(customFieldInfoDataBean.getFeatureId(), customFieldInfoDataBean.getSectionId(), dbdocumentName, hkFieldEntitys, true, loginDataBean.getCompanyId());

            List<String> labels = new LinkedList<>();
            for (HkFieldEntity hkFieldEntity : hkFieldEntitys) {
                labels.add(hkFieldEntity.getFieldLabel());
                customFieldIdMap.put(hkFieldEntity.getFieldLabel(), hkFieldEntity.getId());
            }
            userManagementServiceWrapper.createAllLocaleForEntity(labels, HkSystemConstantUtil.CustomField.I18_FIELD, loginDataBean.getId(), loginDataBean.getCompanyId());

            createFeatureSectionMap(Boolean.TRUE, null);
        }
        return customFieldIdMap;
    }

    public Map<String, Object> retrieveSectionAndCustomFieldByFeatureId(Long featureId) {
        Map<HkSectionEntity, List<HkFieldEntity>> retrieveSectionsByFeatureId = hkFieldService.retrieveSectionsByFeatureId(featureId, loginDataBean.getCompanyId(), true, true);
        Map<String, Object> result = null;
        if (!CollectionUtils.isEmpty(retrieveSectionsByFeatureId)) {
            result = new HashMap<>();
            for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entry : retrieveSectionsByFeatureId.entrySet()) {
                HkSectionEntity hkSectionEntity = entry.getKey();
                List<HkFieldEntity> hkFieldEntitys = entry.getValue();
                List<HkSelect2DataBean> hkSelectItems = (List<HkSelect2DataBean>) result.get("sectionList");
                if (hkSelectItems == null) {
                    hkSelectItems = new ArrayList<>();
                    result.put("sectionList", hkSelectItems);
                }
                if (hkSectionEntity != null) {
                    HkSelect2DataBean hkSelectItem = new HkSelect2DataBean(hkSectionEntity.getId(), hkSectionEntity.getSectionName());
                    hkSelectItems.add(hkSelectItem);
                }
                Map<Long, List<CustomFieldDataBean>> customFieldMap = (Map<Long, List<CustomFieldDataBean>>) result.get("customFieldsMap");
                if (customFieldMap == null) {
                    customFieldMap = new HashMap<>();
                    result.put("customFieldsMap", customFieldMap);
                }
                if (!CollectionUtils.isEmpty(hkFieldEntitys)) {
                    List<CustomFieldDataBean> customFieldDataBeans = new ArrayList<>();
                    for (HkFieldEntity hkFieldEntity : hkFieldEntitys) {
                        CustomFieldDataBean customFieldDataBean = new CustomFieldDataBean();
                        customFieldDataBean.setId(hkFieldEntity.getId());
                        customFieldDataBean.setLabel(hkFieldEntity.getFieldLabel());
                        customFieldDataBean.setOldLabelName(hkFieldEntity.getFieldLabel());
                        customFieldDataBean.setType(hkFieldEntity.getComponentType());
                        customFieldDataBean.setValidationPattern(hkFieldEntity.getValidationPattern());
                        customFieldDataBean.setValues(hkFieldEntity.getFieldValues());
                        customFieldDataBeans.add(customFieldDataBean);
                    }
                    if (hkSectionEntity == null) {
                        customFieldMap.put(-1l, customFieldDataBeans);
                    } else {
                        customFieldMap.put(hkSectionEntity.getId(), customFieldDataBeans);
                    }
                }
            }
        }
        return result;
    }

    // method modified By Shifa on 23 December 2014
    public List<HkSelect2DataBean> search(String searchValue) throws GenericDatabaseException {
        List<HkSelect2DataBean> hkSelect2DataBeans = new ArrayList<>();
        Map<Long, String> featureIdNameMap = userManagementServiceWrapper.retrieveFeatureMap(Boolean.FALSE);
        Map<Long, List<String>> searchFields = hkFieldService.searchFields(searchValue, featureIdNameMap, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(searchFields)) {
            for (Map.Entry<Long, List<String>> entry : searchFields.entrySet()) {
                Long featureAndSectionId = entry.getKey();
                for (String value : entry.getValue()) {
                    HkSelect2DataBean hkSelect2DataBean = new HkSelect2DataBean(featureAndSectionId, value);
                    hkSelect2DataBeans.add(hkSelect2DataBean);
                }
            }
        }
        return hkSelect2DataBeans;
    }

    public List<HkSelect2DataBean> searchSubEntityFields(String searchValue) throws GenericDatabaseException {
        List<HkSelect2DataBean> hkSelect2DataBeans = new ArrayList<>();
        Map<Long, String> featureIdNameMap = userManagementServiceWrapper.retrieveFeatureMap(Boolean.FALSE);
        Map<String, List<String>> searchFields = hkFieldService.searchSubEntityFields(searchValue, featureIdNameMap, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(searchFields)) {
            for (Map.Entry<String, List<String>> entry : searchFields.entrySet()) {
                String fieldAndFeatureId = entry.getKey();
                for (String value : entry.getValue()) {
                    HkSelect2DataBean hkSelect2DataBean = new HkSelect2DataBean(fieldAndFeatureId, value);
                    hkSelect2DataBeans.add(hkSelect2DataBean);
                }
            }
        }
        return hkSelect2DataBeans;
    }

    public Map<String, Object> retrieveSectionAndCustomFieldTemplateByFeatureName(String featureName) {
        Map<String, Object> customFields = null;
        if (StringUtils.hasText(featureName)) {
            Map<Long, Map<String, Map<String, Object>>> companyAndFeatureWithFieldMap = retrieveCompanyFeatureSectionsMap();
            if (companyAndFeatureWithFieldMap != null && companyAndFeatureWithFieldMap.containsKey(loginDataBean.getCompanyId())) {
                Map<String, Map<String, Object>> featureWithFieldMap = companyAndFeatureWithFieldMap.get(loginDataBean.getCompanyId());
                if (featureWithFieldMap != null && featureWithFieldMap.containsKey(featureName)) {
                    customFields = featureWithFieldMap.get(featureName);
                }
            }
        }
        return customFields;
    }

    public Map<Long, Map<String, Map<String, Object>>> retrieveCompanyFeatureSectionsMap() {

        List<UMFeature> uMFeatures = userManagementServiceWrapper.retrieveFeatures(0);
        Map<Long, String> featureNameMap = new HashMap<>();
        for (UMFeature uMFeature : uMFeatures) {
            featureNameMap.put(uMFeature.getId(), uMFeature.getName());
        }

        List<UMCompany> uMCompanys = userManagementServiceWrapper.retrieveAllCompany();
        List<Long> companyIds = new ArrayList<>();
        for (UMCompany uMCompany : uMCompanys) {
            companyIds.add(uMCompany.getId());
        }

        Map<Long, List<String>> mapOfFieldIdWithConstraintsDependantOnField = this.retrieveMapOfFieldIdWithConstraintsDependantOnField();
        Map<Long, Boolean> checkFieldHasException = this.checkFieldHasExceptionOrNot();

        List<HkFieldEntity> fieldEntitiesOfFormulaType = hkFieldService.retrieveFieldByFeatureAndComponentType(null, null, HkSystemConstantUtil.CustomField.ComponentType.FORMULA, Boolean.FALSE);
        List<HkFieldEntity> fieldEntitiesOfdefaultDateType = hkFieldService.retrieveFieldByFeatureAndComponentType(null, null, HkSystemConstantUtil.CustomField.ComponentType.DATE, Boolean.FALSE);

        Map<Long, HkFieldEntity> mapOfFieldIdAndFeatureId = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<String, HkFieldEntity> mapOfDBFieldNameWithEntity = hkFieldService.retrieveMapOfDBFieldNameWithEntity(null, null);
        Map<Long, String> mapOfFieldIdAndValueMasterDetailAssociated = hkFieldService.retrieveMapOfFieldIdAndValueMasterDetailAssociated();
        Map<String, String> dbFieldNameWithConstraintValueDbField = new HashMap<>();
        Map<String, String> dbFieldWithLabelForConstraintMessage = new HashMap<>();
        Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> retrieveSectionsByFeatureId = hkFieldService.retrieveCompanyFeatureSectionsMap(new ArrayList<Long>(featureNameMap.keySet()), companyIds);
        Map<String, String> mapOfCurrencyCodeWithCurrencySymbol = this.mapOfCurrencyCodeWithCurrencySymbol();
        Map<Long, String> featureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureIdWithNameMap();
        Map<Long, List<SelectItem>> allExceptionsDependantOnField = this.retrieveAllExceptionsDependantOnField();
        Map<String, Map<String, String>> mapOfFieldsForDefaultDate = this.mapOfFieldsForDefaultDate();

        List<HkFieldEntity> allFields = hkFieldService.retrieveFieldByFeatureAndComponentType(null, null, HkSystemConstantUtil.CustomField.ComponentType.DATE, Boolean.FALSE);
        Map<String, String> mapOfFieldsWithSkipHolidays = this.mapOfFieldsWithSkipHolidays(allFields);

        if (!CollectionUtils.isEmpty(retrieveSectionsByFeatureId)) {
            Map<Long, Map<String, Map<String, Object>>> result = new HashMap<>();
            for (Map.Entry<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> entrySet : retrieveSectionsByFeatureId.entrySet()) {
                Long key = entrySet.getKey();
                Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> value = entrySet.getValue();
                Map<String, Map<String, Object>> companyIdAndSectionEntityMap = result.get(key);
                if (companyIdAndSectionEntityMap == null) {
                    companyIdAndSectionEntityMap = new HashMap<>();
                    result.put(key, companyIdAndSectionEntityMap);
                }
                for (Map.Entry<Long, Map<HkSectionEntity, List<HkFieldEntity>>> entrySet1 : value.entrySet()) {
                    Long key1 = entrySet1.getKey();
                    Map<HkSectionEntity, List<HkFieldEntity>> value1 = entrySet1.getValue();
                    Map<String, Object> sectionEntityMap = companyIdAndSectionEntityMap.get(featureNameMap.get(key1));
                    if (sectionEntityMap == null) {
                        sectionEntityMap = new HashMap<>();
                        companyIdAndSectionEntityMap.put(featureNameMap.get(key1), sectionEntityMap);
                    }
                    for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entrySet2 : value1.entrySet()) {
                        HkSectionEntity hkSectionEntity = entrySet2.getKey();
                        List<HkFieldEntity> hkFieldEntitys = entrySet2.getValue();
                        if (!CollectionUtils.isEmpty(hkFieldEntitys)) {
                            List<Map<String, Object>> customFieldTemplateDataBeans = new ArrayList<>();
                            for (HkFieldEntity hkFieldEntity : hkFieldEntitys) {
                                if (hkFieldEntity.getIsCustomField()) {
                                    Type type = new TypeToken<Map<String, Object>>() {
                                    }.getType();
                                    Map customFieldPatternMap = (new Gson()).fromJson(hkFieldEntity.getValidationPattern(), type);

//                                    if (customFieldPatternMap != null && !customFieldPatternMap.isEmpty() ) {
                                    Map<String, Object> customFieldTemplateDataBean = new HashMap<>();
                                    if (hkFieldEntity.getFieldLabel() != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LABEL, hkFieldEntity.getFieldLabel());
                                    }
                                    if (hkFieldEntity.getDbFieldName() != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MODEL, hkFieldEntity.getDbFieldName());
                                        String[] dbFieldNameArr = hkFieldEntity.getDbFieldName().split("\\$");

                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MODEL_WITHOUT_SEPERATOR, dbFieldNameArr[0]);
                                    }
                                    if (hkFieldEntity.getDbBaseType() != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, hkFieldEntity.getFieldType());
                                    }
                                    if (hkFieldEntity.getId() != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.ID, hkFieldEntity.getId());
                                    }

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE).toString().isEmpty()) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.REQUIRED, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE));
                                    }

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SKIP_HOLIDAYS) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SKIP_HOLIDAYS).toString().isEmpty()) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SKIP_HOLIDAYS, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SKIP_HOLIDAYS));
                                    }
                                    if (featureIdWithNameMap.containsKey(hkFieldEntity.getFeature())) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FEATURE, featureIdWithNameMap.get(hkFieldEntity.getFeature()));
                                    }
                                    if (hkFieldEntity.getSeqNo() != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SEQUENCE, hkFieldEntity.getSeqNo());
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.BACKGROUND_COLOR) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.BACKGROUND_COLOR).toString().isEmpty()) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.BACKGROUND_COLOR, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.BACKGROUND_COLOR));
                                    }
                                    Map<String, Object> attributeMap = new HashMap<>();
                                    switch (hkFieldEntity.getComponentType()) {

                                        case HkSystemConstantUtil.CustomField.ComponentType.POINTER:
                                            // Pointer is for copying the behaviour of the custom field to which it is pointing
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER).toString().isEmpty()) {
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                                }
                                                HkFieldEntity pointerFieldEntity = null;
                                                if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER).toString()))) {
                                                    pointerFieldEntity = mapOfFieldIdAndFeatureId.get(Long.parseLong(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER).toString()));
                                                    if (mapOfFieldIdAndValueMasterDetailAssociated.containsKey(pointerFieldEntity.getId())) {
                                                        String pointerFieldValue = mapOfFieldIdAndValueMasterDetailAssociated.get(pointerFieldEntity.getId());
                                                        pointerFieldEntity.setFieldValues(pointerFieldValue);
                                                    }
                                                }
                                                Type type1 = new TypeToken<Map<String, Object>>() {
                                                }.getType();

                                                // Fetching the behaviour of the customfield to which our custom field is pointing
                                                Map pointerCustomFieldPatternMap = (new Gson()).fromJson(pointerFieldEntity.getValidationPattern(), type1);

                                                // copying attributes which are common to all custom field types
                                                if (pointerCustomFieldPatternMap != null && !pointerCustomFieldPatternMap.isEmpty()) {

                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                                    }

                                                    // copying common attributes for number,percent,angle and currency
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.NUMBER) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.ANGLE) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PERCENT) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE)) {

                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                                && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                                && (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                            Integer beforeDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                            String validatePattern = "";
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                                validatePattern += "/^-?";
                                                            } else {
                                                                validatePattern += "/^";
                                                            }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                            validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                                        }
                                                        if ((pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                                                && (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                                && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                            Integer afterDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                            String validatePattern = null;

                                                            int totalDecimal = 16;
                                                            int remainingBeforeDecimal = totalDecimal - afterDecimal;
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                                                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                                    validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                                                } else {
                                                                    validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                                                }

                                                            } else {
                                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                                    validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                                                } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                                                    validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                                }
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                                && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                                && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                                && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                                                            Integer beforeDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                            Integer afterDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                                                            String validatePattern = null;
                                                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                                    validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                                } else {
                                                                    validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                                }
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                                    validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                                } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                                    validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                                }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                                        }
                                                        if ((pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                                                || pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                                                || pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                        }

                                                        //copying attributes specific to number
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.NUMBER)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                                            } else {
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, Boolean.FALSE);

                                                            }

                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER).toString().isEmpty()) {
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, !(Boolean) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER));
                                                            } else {
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                                            }
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));

                                                            // Copying constraints 
                                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(pointerFieldEntity.getId())) {
                                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(pointerFieldEntity.getId());
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                                }
                                                            }

                                                            String[] validationsArrNumber = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String constraintValue = "";
                                                            for (String validationValue : validationsArrNumber) {
                                                                if (validationValue.contains("\"constraint\":")) {

                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                                    String[] constraintArray = validationValue.split(":");
                                                                    constraintValue += constraintArray[1];
                                                                    String constraintValueArray[] = constraintValue.split("@");
                                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                                    Long idOfField = null;
                                                                    HkFieldEntity fieldEntityConstraint = null;
                                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                                        idOfField = fieldEntityConstraint.getFeature();
                                                                        // This we are creating as in constraint message we need label instead of label
                                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                                        }
                                                                    }

                                                                    if (idOfField.equals(pointerFieldEntity.getFeature())) {
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                                    }
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                                }
                                                            }
                                                            // code for contraint ends here
                                                        }

                                                        // copying attributes specific to angle
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "Angle");
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, "0");
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, "360");
                                                            }

                                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                                            }
                                                            // Copying constraints 
                                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(pointerFieldEntity.getId())) {
                                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(pointerFieldEntity.getId());
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                                }
                                                            }

                                                            String[] validationsArrNumber = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String constraintValue = "";
                                                            for (String validationValue : validationsArrNumber) {
                                                                if (validationValue.contains("\"constraint\":")) {

                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                                    String[] constraintArray = validationValue.split(":");
                                                                    constraintValue += constraintArray[1];
                                                                    String constraintValueArray[] = constraintValue.split("@");
                                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                                    Long idOfField = null;
                                                                    HkFieldEntity fieldEntityConstraint = null;
                                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                                        idOfField = fieldEntityConstraint.getFeature();
                                                                        // This we are creating as in constraint message we need label instead of label
                                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                                        }
                                                                    }

                                                                    if (idOfField.equals(pointerFieldEntity.getFeature())) {
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                                    }
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                                }
                                                            }
                                                            // code for contraint ends here

                                                        }
                                                        // copying attributes specific to percent
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
                                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                                            }

                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                                            }
                                                            // Copying constraints 
                                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(pointerFieldEntity.getId())) {
                                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(pointerFieldEntity.getId());
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                                }
                                                            }

                                                            String[] validationsArrNumber = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String constraintValue = "";
                                                            for (String validationValue : validationsArrNumber) {
                                                                if (validationValue.contains("\"constraint\":")) {

                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                                    String[] constraintArray = validationValue.split(":");
                                                                    constraintValue += constraintArray[1];
                                                                    String constraintValueArray[] = constraintValue.split("@");
                                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                                    Long idOfField = null;
                                                                    HkFieldEntity fieldEntityConstraint = null;
                                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                                        idOfField = fieldEntityConstraint.getFeature();
                                                                        // This we are creating as in constraint message we need label instead of label
                                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                                        }
                                                                    }

                                                                    if (idOfField.equals(pointerFieldEntity.getFeature())) {
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                                    }
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                                }
                                                            }
                                                            // code for contraint ends here

                                                        }
                                                        // copying  attributes specific to currency
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "currency");
//                                                        String currencyId = pointerFieldEntity.getAssociatedCurrency();
//                                                        HkCurrencyEntity currencyEntity = currencyMasterMap.get(Long.parseLong(currencyId));
//                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_TYPE, currencyEntity.getSymbol());
//                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_FORMAT, currencyEntity.getFormat());
//                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_PREFIX, currencyEntity.getSymbolPosition());
                                                            if (mapOfCurrencyCodeWithCurrencySymbol != null && !mapOfCurrencyCodeWithCurrencySymbol.isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE_SYMBOL_MAP, mapOfCurrencyCodeWithCurrencySymbol);
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_FORMAT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_SYMBOL_POSITION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                                            }
                                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                                            }
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                            // Copying constraints 
                                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(pointerFieldEntity.getId())) {
                                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(pointerFieldEntity.getId());
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                                }
                                                            }

                                                            String[] validationsArrNumber = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String constraintValue = "";
                                                            for (String validationValue : validationsArrNumber) {
                                                                if (validationValue.contains("\"constraint\":")) {

                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                                    String[] constraintArray = validationValue.split(":");
                                                                    constraintValue += constraintArray[1];
                                                                    String constraintValueArray[] = constraintValue.split("@");
                                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                                    Long idOfField = null;
                                                                    HkFieldEntity fieldEntityConstraint = null;
                                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                                        idOfField = fieldEntityConstraint.getFeature();
                                                                        // This we are creating as in constraint message we need label instead of label
                                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                                        }
                                                                    }

                                                                    if (idOfField.equals(pointerFieldEntity.getFeature())) {
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                                    }
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                                }
                                                            }
                                                            // code for contraint ends here

                                                        }

                                                        // copying  attributes specific to currency
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "exchangeRate");

                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                                            }

                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);

                                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(pointerFieldEntity.getId())) {
                                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(pointerFieldEntity.getId());
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                                }
                                                            }

                                                            String[] validationsArrNumber = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String constraintValue = "";
                                                            for (String validationValue : validationsArrNumber) {
                                                                if (validationValue.contains("\"constraint\":")) {

                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                                    String[] constraintArray = validationValue.split(":");
                                                                    constraintValue += constraintArray[1];
                                                                    String constraintValueArray[] = constraintValue.split("@");
                                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                                    Long idOfField = null;
                                                                    HkFieldEntity fieldEntityConstraint = null;
                                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                                        idOfField = fieldEntityConstraint.getFeature();
                                                                        // This we are creating as in constraint message we need label instead of label
                                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                                    }

                                                                    if (idOfField.equals(pointerFieldEntity.getFeature())) {
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                                    }
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                                }
                                                            }
                                                        }

                                                    }
                                                    // copying common attributes for text field,textarea,phone and email
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PHONE) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.EMAIL)) {
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                                                            Double maxLength = Double.parseDouble(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString().isEmpty()) {

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                                                        }
                                                        // copying  attributes specific to Text Field
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MASKED, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED));
                                                            }
                                                            String finalValidatePattern = "";

                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null
                                                                    && !(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().isEmpty())) {
                                                                String[] allowTypes = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().split(",");
                                                                String validatePattern = "/^[";
                                                                for (String allowType : allowTypes) {
                                                                    switch (allowType) {
                                                                        case "Numeric":
                                                                            validatePattern += "0-9";
                                                                            break;
                                                                        case "Alphabet":
                                                                            validatePattern += "a-zA-Z";
                                                                            break;
                                                                        case "Special character":
                                                                            validatePattern += "!-@#$%^&*()_ ";
                                                                            break;
                                                                    }
                                                                }
                                                                validatePattern += "]*$/";
                                                                finalValidatePattern = validatePattern;
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_TEXT, finalValidatePattern);
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                                finalValidatePattern = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, finalValidatePattern);
                                                        }
                                                        // copying  attributes specific to Text Area
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "textarea");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
// If user has given regex pattern accept that regex otherwise no regex .
                                                            String finalValidatePatternTextArea = "";
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                                finalValidatePatternTextArea = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                                                customFieldTemplateDataBean.put("validate", finalValidatePatternTextArea);
                                                            }
                                                        }
                                                        // copying  attributes specific to Phone
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PHONE)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                                            String finalValidatePatternPhone = "";
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                                finalValidatePatternPhone = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                                            } else {
                                                                finalValidatePatternPhone = "/^[0-9.+()-]*$/";
                                                            }
                                                            String defaultPhone = "/^[0-9.+()-]*$/";
                                                            // Used same variable for phone and email
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultPhone);

                                                            customFieldTemplateDataBean.put("validate", finalValidatePatternPhone);
//                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
//                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);

                                                            // code for dependancy ends here
                                                            Double maxLengthPhone = null;
                                                            if ((pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                                                                maxLengthPhone = Double.parseDouble(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                                            } else {
                                                                maxLengthPhone = 15.00;
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);

                                                        }
                                                        // copying  attributes specific to Email
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.EMAIL)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);

                                                            // If user has given regex pattern accept that regex otherwise use default one.
                                                            String finalValidatePatternEmail = "";
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                                finalValidatePatternEmail = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                                            } else {
                                                                finalValidatePatternEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                                                            }
                                                            String defaultEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultEmail);

                                                            customFieldTemplateDataBean.put("validate", finalValidatePatternEmail);

                                                        }
                                                    }
                                                    // copying common attributes for radio button,single select(dropdown),multiselect and usermultiselect
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                        // copying  attributes specific to single select(dropdown)
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "pointerselect");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Pointer.LABEL, pointerFieldEntity.getFieldLabel());
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Pointer.FIELD_ID, pointerFieldEntity.getId());

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.LONG);

                                                            String[] dropdownValidationPatternPointer = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String displayShortcutCode = null;
                                                            for (String drpDwnValidation : dropdownValidationPatternPointer) {
                                                                if (drpDwnValidation.contains("\"displayShortcutCode\":")) {
                                                                    String[] displayShortcutCodeArr = drpDwnValidation.split(":");
                                                                    displayShortcutCode = displayShortcutCodeArr[1];
                                                                }
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DISPLAY_SHORTCUT_CODE, displayShortcutCode);

                                                        }
                                                        // copying  attributes specific to multiselect
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "pointer_multiSelect");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Pointer.LABEL, pointerFieldEntity.getFieldLabel());
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Pointer.FIELD_ID, pointerFieldEntity.getId());
                                                            String[] multiSelectValidationPatternPointer = pointerFieldEntity.getValidationPattern().replace("{", "")
                                                                    .replace("}", "")
                                                                    .split(",");
                                                            String displayShortcutCode = null;
                                                            for (String multiselectValidation : multiSelectValidationPatternPointer) {
                                                                if (multiselectValidation.contains("\"displayShortcutCode\":")) {
                                                                    String[] displayShortcutCodeArr = multiselectValidation.split(":");
                                                                    displayShortcutCode = displayShortcutCodeArr[1];
                                                                }
                                                            }
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DISPLAY_SHORTCUT_CODE, displayShortcutCode);

                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                                        }
                                                        // copying  attributes specific to usermultiselect
                                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "UserMultiSelect");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
//                                                        if (pointerFieldEntity.getFieldValues() != null) {
//                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(pointerFieldEntity.getFieldValues()));
//
//                                                        }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_EMPLOYEE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DEPARTMENT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DESIGNATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_MULTISELECT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT));
                                                            }
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE).toString().isEmpty()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_FRANCHISE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE));
                                                            }
                                                        }

                                                    }
                                                    // copying  attributes specific to CheckBox
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CHECKBOX)) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "checkbox");
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT));
                                                        }
                                                    }
                                                    // copying  attributes specific to Date
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DATE);
                                                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);

                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE) != null && (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).equals(true)) || pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).equals("true")) {
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {
                                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                                            }
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                                                        } else {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION).toString().isEmpty()) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                                        }
//                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
//                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.HINT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
//                                                        }
                                                    }
                                                    // copying  attributes specific to Date range
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DATE);

                                                        customFieldTemplateDataBean.put("fromModel", "from" + hkFieldEntity.getDbFieldName());
                                                        customFieldTemplateDataBean.put("toModel", "to" + hkFieldEntity.getDbFieldName());
//                                        customFieldTemplateDataBean.remove(HkSystemConstantUtil.DynamicFormAttribute.MODEL);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                                        }

                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().isEmpty() && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetimerange");
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");
                                                        } else {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "daterange");
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION).toString().isEmpty()) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                                        }
//                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
//                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.HINT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
//                                                        }
                                                    }
                                                    // copying  attributes specific to Image Upload
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.IMAGE)) {
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL));

                                                        }
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.IMAGE);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "imageUpload");
                                                        customFieldTemplateDataBean.put("width", "100%");
                                                        customFieldTemplateDataBean.put("height", "100%");
//                                            Map<String, Object> flowInits = new HashMap<>();
//                                            flowInits.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
//                                            flowInits.put("singleFile", true);
//
//                                            customFieldTemplateDataBean.put("flowInit", flowInits);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put("maxsize", (Double) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                                        }
                                                        Map<String, Object> flieType = new HashMap<>();
                                                        flieType.put("jpg", 1);
                                                        flieType.put("png", 1);
                                                        flieType.put("psd", 1);
                                                        customFieldTemplateDataBean.put("fileFormat", flieType);
                                                    }
                                                    // copying  attributes specific to File Upload
                                                    if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.UPLOAD)) {
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                                        }
                                                        if (pointerFieldEntity.getIsDependant() != null) {
                                                            if (pointerFieldEntity.getIsDependant()) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                            } else {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                            }
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                                        }
                                                        // code for dependancy ends here
//                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.FILE);

                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "fileUpload");
//                                Map<String, Object> flowInit = new HashMap<>();
//                                flowInit.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put("maxsize", (Double) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                                        }
//                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE).toString().isEmpty()) {
//                                    flowInit.put("singleFile", !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE));
//                                } else {
//                                    flowInit.put("singleFile", true);
//                                }
//                                customFieldTemplateDataBean.put("flowInit", flowInit);
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE).toString().isEmpty()) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SINGLE_FILE, !(Boolean) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE));
                                                        } else {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SINGLE_FILE, true);
//                                               
                                                        }
                                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).toString().isEmpty()) {
                                                            Map<String, Object> flieType = new HashMap<>();
                                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Image")) {
                                                                flieType.put("jpg", 1);
                                                                flieType.put("psd", 1);
                                                                flieType.put("png", 1);
                                                                flieType.put("gif", 1);
                                                                flieType.put("jpeg", 1);
                                                                flieType.put("bmp", 1);
                                                                flieType.put("tiff", 1);
                                                            } else if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Text")) {
                                                                flieType.put("txt", 1);
                                                                flieType.put("doc", 1);
                                                                flieType.put("docx", 1);
                                                                flieType.put("oxps", 1);
                                                                flieType.put("pdf", 1);
                                                            } else if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Media")) {
                                                                flieType.put("mpg", 1);
                                                                flieType.put("mp3", 1);
                                                                flieType.put("mp4", 1);
                                                            } else if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("All")) {
                                                                flieType.put("jpg", 1);
                                                                flieType.put("psd", 1);
                                                                flieType.put("png", 1);
                                                                flieType.put("gif", 1);
                                                                flieType.put("jpeg", 1);
                                                                flieType.put("bmp", 1);
                                                                flieType.put("tiff", 1);
                                                                flieType.put("txt", 1);
                                                                flieType.put("doc", 1);
                                                                flieType.put("docx", 1);
                                                                flieType.put("oxps", 1);
                                                                flieType.put("pdf", 1);
                                                                flieType.put("mpg", 1);
                                                                flieType.put("mp3", 1);
                                                                flieType.put("mp4", 1);
                                                            } else if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Other")) {
                                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER) != null && !pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().isEmpty()) {
                                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().contains(",")) {
                                                                        String[] str = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().split(",");
                                                                        for (String string : str) {
                                                                            flieType.put(string, 1);
                                                                        }
                                                                    } else {
                                                                        flieType.put(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString(), 1);
                                                                    }
                                                                }
                                                            } else {

                                                            }
                                                            customFieldTemplateDataBean.put("acceptedFormats", flieType);
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.FORMULA:
                                            Boolean isInvolvedInFormulaForformula = false;
                                            Map<String, String> formulaInvolvedformulas = null;
                                            Map<String, String> formulaRoundOffFrm = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedformulas = new HashMap<>();
                                                formulaRoundOffFrm = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
                                                    String roundToFrm = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundToFrm = roundOffArr[1];
                                                        }
                                                    }

                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForformula = true;
                                                        formulaInvolvedformulas.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOffFrm.put(fieldEntity.getDbFieldName(), roundToFrm);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForformula);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedformulas);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOffFrm);
                                            // code added by Shifa on 3 february 2014 for fetching formula from validation pattern

//                                            Long featureId = hkFieldEntity.getFeature();
//                                            if (featureId != null) {
//                                                UMFeature uMFeature = featureService.retrieveFeatureById(featureId, null);
//                                                if (uMFeature != null) {
//                                                    String featureName = uMFeature.getName();
//                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.FEATURE, featureName);
//                                                }
//                                            }
                                            Boolean isCurrencyInvolvedInField = false;
                                            String featureName = featureIdWithNameMap.get(hkFieldEntity.getFeature());
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FEATURE, featureName);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "formula");
                                            String[] validationsArrFormula = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String formulaValue = "";
                                            String formulaOptionValue = "";
                                            for (String validationValue : validationsArrFormula) {
                                                if (validationValue.contains("\"formula\":")) {
                                                    String[] formulaArray = validationValue.split(":");
                                                    formulaValue += formulaArray[1];
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_VALUE, formulaValue);
                                                }
                                                if (validationValue.contains("\"formulaOption\":")) {

                                                    String[] formulaOptionArray = validationValue.split(":");
                                                    formulaOptionValue += formulaOptionArray[1];
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_OPTION_VALUE, formulaOptionValue);
                                                }
                                            }
                                            // Code added by Shifa Salheen on 6 May 2015 for checking formula contains currency field
                                            if (hkFieldEntity.getFormulaValue() != null) {
                                                String fieldFormulaValue = hkFieldEntity.getFormulaValue();
                                                String[] splitFormula = fieldFormulaValue.split("\\|");
                                                for (String splitVal : splitFormula) {
                                                    if (splitVal.contains(HkSystemConstantUtil.Feature.INVOICE) || splitVal.contains(HkSystemConstantUtil.Feature.PARCEL) || splitVal.contains(HkSystemConstantUtil.Feature.LOT) || splitVal.contains(HkSystemConstantUtil.Feature.PACKET)) {
                                                        String formulaSubPart[] = splitVal.split("\\.");
                                                        String dbFieldName = formulaSubPart[1];
                                                        if (mapOfDBFieldNameWithEntity.containsKey(dbFieldName)) {
                                                            if (mapOfDBFieldNameWithEntity.get(dbFieldName).getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                                isCurrencyInvolvedInField = true;
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                            if (isCurrencyInvolvedInField) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, isCurrencyInvolvedInField);
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.NUMBER:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                                            if (mapOfFieldsForDefaultDate != null && !mapOfFieldsForDefaultDate.isEmpty() && mapOfFieldsForDefaultDate.containsKey(hkFieldEntity.getDbFieldName())) {
                                                Map<String, String> fieldsForDefaultDate = mapOfFieldsForDefaultDate.get(hkFieldEntity.getDbFieldName());
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MAP_FOR_DEFAULT_DATE, fieldsForDefaultDate);
                                            }
                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER).toString().isEmpty()) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER));
                                            } else {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            }
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                            } else {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, Boolean.FALSE);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                String validatePattern = "";
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                String validatePattern = null;

                                                int totalDecimal = 16;
                                                int remainingBeforeDecimal = totalDecimal - afterDecimal;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                                    }

                                                } else {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                                    } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                                        validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                                                String validatePattern = null;
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                    }
                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            }
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }

                                            String[] validationsArrNumber = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValue = "";
                                            for (String validationValue : validationsArrNumber) {
                                                if (validationValue.contains("\"constraint\":")) {

                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValue += constraintArray[1];
                                                    String constraintValueArray[] = constraintValue.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");

//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();
                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);
                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                        }
                                                    }

                                                    if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                    }
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValue);
                                                }
                                            }
                                            // code for contraint ends here
                                            // Code for formula starts here added on 20 Feb 2015 By Shifa
                                            Boolean isInvolvedInFormulaForNumber = false;
                                            Map<String, String> formulaInvolvedNumber = null;
                                            Map<String, String> formulaRoundOff = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedNumber = new HashMap<>();
                                                formulaRoundOff = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
//                                                    Long fieldEntityFeature = fieldEntity.getFeature();
                                                    String roundTo = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundTo = roundOffArr[1];
                                                        }
                                                    }
                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForNumber = true;
                                                        formulaInvolvedNumber.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOff.put(fieldEntity.getDbFieldName(), roundTo);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForNumber);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOff);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedNumber);
                                            // code ends here

                                            Map<String, String> defaultDateForNumber = new HashMap<>();
                                            if (fieldEntitiesOfdefaultDateType != null && !fieldEntitiesOfdefaultDateType.isEmpty()) {
                                                for (HkFieldEntity fieldEntitiesOfdefaultDateType1 : fieldEntitiesOfdefaultDateType) {
                                                    String[] validationsArrDefDate = fieldEntitiesOfdefaultDateType1.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vDefArr : validationsArrDefDate) {

                                                        if (vDefArr.contains("\"defaultDate\":")) {
                                                            String[] vDefArray = vDefArr.split(":");
                                                            String defStringFrDates = vDefArray[1];
                                                            String[] arrFrDefaultDate = defStringFrDates.split("#@");
                                                            if (arrFrDefaultDate.length > 1 && arrFrDefaultDate[2].replace("\"", "").equals(hkFieldEntity.getDbFieldName())) {
                                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DATE_INVOLVED_FOR_OTHER_DEF_DATE, Boolean.TRUE);
                                                                if (mapOfFieldsWithSkipHolidays != null && !mapOfFieldsWithSkipHolidays.isEmpty() && mapOfFieldsWithSkipHolidays.containsKey(fieldEntitiesOfdefaultDateType1.getDbFieldName())) {
                                                                    String skipHolidayVal = mapOfFieldsWithSkipHolidays.get(fieldEntitiesOfdefaultDateType1.getDbFieldName());
                                                                    defaultDateForNumber.put(fieldEntitiesOfdefaultDateType1.getDbFieldName() + "%" + skipHolidayVal, defStringFrDates);
                                                                } else {
                                                                    defaultDateForNumber.put(fieldEntitiesOfdefaultDateType1.getDbFieldName() + "%" + "false", defStringFrDates);
                                                                }
                                                            }

                                                        }
                                                    }

                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DEFAULT_DATE_NUM, defaultDateForNumber);

                                            }

                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "select");

                                            String[] dropdownValidationPattern = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String displayShortcutCode = null;
                                            for (String drpDwnValidation : dropdownValidationPattern) {
                                                if (drpDwnValidation.contains("\"displayShortcutCode\":")) {
                                                    String[] displayShortcutCodeArr = drpDwnValidation.split(":");
                                                    displayShortcutCode = displayShortcutCodeArr[1];
                                                }
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DISPLAY_SHORTCUT_CODE, displayShortcutCode);

                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            // For reverse scenario exceptions that which fields are dependant on it for exceptions
                                            if (allExceptionsDependantOnField != null && allExceptionsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                List<SelectItem> listOfExceptions = allExceptionsDependantOnField.get(hkFieldEntity.getId());
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.LIST_OF_EXCEPTIONS, listOfExceptions);
                                            }
                                            // For Forward scenario exception i.e if it contains exception or not
                                            if (checkFieldHasException != null && !checkFieldHasException.isEmpty() && checkFieldHasException.containsKey(hkFieldEntity.getId())) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.FIELD_HAS_EXCEPTION, checkFieldHasException.get(hkFieldEntity.getId()));

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
//                                            if (hkFieldEntity.getFieldValues() != null) {
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(hkFieldEntity.getFieldValues()));
//                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            // Code added by Shifa for handling dependent fields by Shifa on 2 January 2015
                                            // Retrieving all the dependant customfields
                                            List<HkFieldEntity> dependantFieldList = hkFieldService.retrieveAllDependantFieldsByCompanyId(null);
                                            if (!CollectionUtils.isEmpty(dependantFieldList)) {
                                                String dependantMasters = "";
                                                for (HkFieldEntity dependantField : dependantFieldList) {
                                                    // creating an array of validation pattern
                                                    String[] validationsArr = dependantField.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    String dependantMasterId = "";
                                                    String dependantMasterValue = "";
                                                    for (String validationValue : validationsArr) {

                                                        if (validationValue.contains("\"dependantMasterId\":")) {
                                                            String[] dependantMasterIdArray = validationValue.split(":");
                                                            dependantMasterId += dependantMasterIdArray[1];

                                                        }
                                                        if (validationValue.contains("\"dependantMasterValue\":")) {
                                                            String[] dependantMasterValueArray = validationValue.split(":");
                                                            dependantMasterValue += dependantMasterValueArray[1];
                                                        }

                                                    }
                                                    if (dependantMasterId != null && !dependantMasterId.isEmpty() && dependantMasterValue != null && !dependantMasterValue.isEmpty()) {
                                                        // Creating a string of dependantmasterId delimiter dependantmastervalue delimiter fieldid which is depending
                                                        // This string contains all dependant fields whether they are depending on the current iterating field or not
                                                        dependantMasters += dependantMasterId.replace("\"", "") + "A" + dependantMasterValue.replace("\"", "") + "A" + dependantField.getId() + ",";
                                                    }
                                                }
                                                // After creating dependamt masters
                                                if (!dependantMasters.isEmpty()) {

                                                    String dependantFieldsOnSingleSelectList = "";
                                                    String[] singleSelectdependantMasterArray = dependantMasters.split(",");
                                                    for (String singleSelectdependantMasters : singleSelectdependantMasterArray) {
                                                        String tiltSeparatedArray[] = singleSelectdependantMasters.split("A", 2);
                                                        // checking whether the dependant Id is equal to the currently iterating field i.e. whether
                                                        // it is depending on current iterating field
                                                        if (tiltSeparatedArray[0].equals(hkFieldEntity.getId().toString())) {
                                                            dependantFieldsOnSingleSelectList += tiltSeparatedArray[1] + ",";
                                                        }

                                                    }
                                                    if (!dependantFieldsOnSingleSelectList.isEmpty()) {
                                                        // final list of dependant fields which are dependant on currently iterating custom field
                                                        // last extra comma removed
                                                        String finaldependantFieldsOnSingleSelectList = dependantFieldsOnSingleSelectList.substring(0, dependantFieldsOnSingleSelectList.length() - 1);
                                                        customFieldTemplateDataBean.put("dependantFieldsOnSingleSelectList", finaldependantFieldsOnSingleSelectList);
                                                        attributeMap.put("dependantFieldsOnSingleSelectList", finaldependantFieldsOnSingleSelectList);
                                                    }

                                                }
//
                                            }

                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "subEntity");

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));

                                            }
                                            if (hkFieldEntity.getId() != null) {
                                                customFieldTemplateDataBean.put("CustomFieldId", hkFieldEntity.getId());
                                            }
                                            List<SelectItem> createDropDownListForSubEntity = this.createDropDownListForSubEntity(hkFieldEntity.getId());
                                            String fieldValuesForSubentity = null;
                                            if (createDropDownListForSubEntity != null) {

                                                for (SelectItem dropList : createDropDownListForSubEntity) {
                                                    if (fieldValuesForSubentity == null) {

                                                        fieldValuesForSubentity = dropList.getLabel() + HkSystemConstantUtil.SEPARATOR_PI + dropList.getValue();
                                                    } else {
                                                        fieldValuesForSubentity = fieldValuesForSubentity + "," + dropList.getLabel() + HkSystemConstantUtil.SEPARATOR_PI + dropList.getValue();
                                                    }
                                                }
//                                                customFieldTemplateDataBean.put("dropDownListForSubEntity", makeValuesForSubEntity(fieldValuesForSubentity));
                                            }
                                            break;

                                        case HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));
                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            String[] multiSelectValidationPattern = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String displayShortcutCodeMultiSelect = null;
                                            for (String msValidation : multiSelectValidationPattern) {
                                                if (msValidation.contains("\"displayShortcutCode\":")) {
                                                    String[] displayShortcutCodeArr = msValidation.split(":");
                                                    displayShortcutCodeMultiSelect = displayShortcutCodeArr[1];
                                                }
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DISPLAY_SHORTCUT_CODE, displayShortcutCodeMultiSelect);
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "multiSelect");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
//                                            if (hkFieldEntity.getFieldValues() != null) {
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(hkFieldEntity.getFieldValues()));
//                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }

                                            // Code added by Shifa for handling dependent fields by Shifa on 3 January 2015
// Retrieving all the dependant customfields
                                            List<HkFieldEntity> dependantFieldListForMultiSelect = hkFieldService.retrieveAllDependantFieldsByCompanyId(null);
                                            if (!CollectionUtils.isEmpty(dependantFieldListForMultiSelect)) {
                                                List<HkValueEntity> valueEntities = hkFoundationService.retrieveAllValueEntities();
                                                Map<String, HkValueEntity> valueEntityMap = new HashMap<>();;
                                                if (!CollectionUtils.isEmpty(valueEntities)) {

                                                    for (HkValueEntity valueEntity : valueEntities) {
                                                        valueEntityMap.put(valueEntity.getId().toString(), valueEntity);
                                                    }
                                                }
                                                String dependantMasters = "";
                                                for (HkFieldEntity dependantField : dependantFieldListForMultiSelect) {
                                                    // creating an array of validation pattern
                                                    String[] validationsArr = dependantField.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    String dependantMasterId = "";
                                                    String dependantMasterValue = "";
                                                    for (String validationValue : validationsArr) {

                                                        if (validationValue.contains("\"dependantMasterId\":")) {
                                                            String[] dependantMasterIdArray = validationValue.split(":");
                                                            dependantMasterId += dependantMasterIdArray[1];

                                                        }
                                                        if (validationValue.contains("\"dependantMasterValue\":")) {
                                                            String[] dependantMasterValueArray = validationValue.split(":");
                                                            if (valueEntityMap.containsKey(dependantMasterValueArray[1]
                                                                    .replace("\"", ""))) {
                                                            }
//                                                HkValueEntity valueEntityMultiSelect = valueEntityMap.get(dependantMasterValueArray[1].replace("\"", ""));
//                                                if (valueEntityMultiSelect != null) {
//                                                    dependantMasterValue += valueEntityMultiSelect.getId();
//                                                }
                                                            dependantMasterValue += dependantMasterValueArray[1];
                                                        }
                                                    }
                                                    if (dependantMasterId != null && !dependantMasterId.isEmpty() && dependantMasterValue != null && !dependantMasterValue.isEmpty()) {
                                                        // Creating a string of dependantmasterId delimiter dependantmastervalue delimiter fieldid which is depending
                                                        // This string contains all dependant fields whether they are depending on the current iterating field or not
                                                        dependantMasters += dependantMasterId.replace("\"", "") + "A" + dependantMasterValue + "A" + dependantField.getId() + ",";
                                                    }
                                                }
                                                // After creating dependant masters
                                                if (!dependantMasters.isEmpty()) {

                                                    String dependantFieldsOnMultiSelectList = "";
                                                    String[] multiSelectdependantMasterArray = dependantMasters.split(",");
                                                    for (String multiSelectdependantMasters : multiSelectdependantMasterArray) {
                                                        String tiltSeparatedArray[] = multiSelectdependantMasters.split("A", 2);
                                                        // checking whether the dependant Id is equal to the currently iterating field i.e. whether
                                                        // it is depending on current iterating field
                                                        if (tiltSeparatedArray[0].equals(hkFieldEntity.getId().toString())) {
                                                            dependantFieldsOnMultiSelectList += tiltSeparatedArray[1] + ",";
                                                        }

                                                    }
                                                    if (!dependantFieldsOnMultiSelectList.isEmpty()) {
                                                        // final list of dependant fields which are dependant on currently iterating custom field
                                                        // last extra comma removed
                                                        String finaldependantFieldsOnMultiSelectList = dependantFieldsOnMultiSelectList.substring(0, dependantFieldsOnMultiSelectList.length() - 1);
                                                        customFieldTemplateDataBean.put("dependantFieldsOnMultiSelectList", finaldependantFieldsOnMultiSelectList);
                                                        attributeMap.put("dependantFieldsOnSingleSelectList", finaldependantFieldsOnMultiSelectList);
                                                    }

                                                }
//
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "UserMultiSelect");

//                                            if (hkFieldEntity.getFieldValues() != null) {
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(hkFieldEntity.getFieldValues()));
//                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_EMPLOYEE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DEPARTMENT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DESIGNATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_FRANCHISE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_MULTISELECT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_FRANCHISE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_FRANCHISE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SELECTED_PARAMETER) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SELECTED_PARAMETER).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SELECTED_PARAMETER, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SELECTED_PARAMETER));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPT_LIST) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPT_LIST).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEPT_LIST, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPT_LIST));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DESG_LIST) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DESG_LIST).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DESG_LIST, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DESG_LIST));
                                            }

                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.AUTO_GENERATED:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "AutoGenerated");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.PLACEHOLDER, "AutoGenerated");
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                                                Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_BARCODE_REQUIRED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_BARCODE_REQUIRED).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_BARCODE_REQUIRED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_BARCODE_REQUIRED));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_BARCODE_REQUIRED, true);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null
                                                    && !(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().isEmpty())) {
                                                String[] allowTypes = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().split(",");
                                                String validatePattern = "/^[";
                                                String finalValidatePattern = "";
                                                for (String allowType : allowTypes) {
                                                    switch (allowType) {
                                                        case "Numeric":
                                                            validatePattern += "0-9";
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, false);
                                                            break;
                                                        case "Alphabet":
                                                            validatePattern += "a-zA-Z";
                                                            break;
                                                        case "Special character":
                                                            validatePattern += "!-@#$%^&*()_ ";
                                                            break;
                                                    }
                                                }
                                                validatePattern += "]*$/";
                                                finalValidatePattern = validatePattern;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_TEXT, finalValidatePattern);
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                                                Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString().isEmpty()) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MASKED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED));
                                            }
                                            String finalValidatePattern = "";

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null
                                                    && !(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().isEmpty())) {
                                                String[] allowTypes = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().split(",");
                                                String validatePattern = "/^[";
                                                for (String allowType : allowTypes) {
                                                    switch (allowType) {
                                                        case "Numeric":
                                                            validatePattern += "0-9";
                                                            break;
                                                        case "Alphabet":
                                                            validatePattern += "a-zA-Z";
                                                            break;
                                                        case "Special character":
                                                            validatePattern += "!-@#$%^&*()_ ";
                                                            break;
                                                    }
                                                }
                                                validatePattern += "]*$/";
                                                finalValidatePattern = validatePattern;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_TEXT, finalValidatePattern);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                finalValidatePattern = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                            }

                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, finalValidatePattern);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_UNIQUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_UNIQUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_UNIQUE, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_UNIQUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.UNIQUE_FOR_FIELDS) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.UNIQUE_FOR_FIELDS).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.UNIQUE_FOR_FIELDS, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.UNIQUE_FOR_FIELDS));
                                            }
                                            break;

                                        case HkSystemConstantUtil.CustomField.ComponentType.EMAIL:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                            // If user has given regex pattern accept that regex otherwise use default one.
                                            String finalValidatePatternEmail = "";
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                finalValidatePatternEmail = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                            } else {
                                                finalValidatePatternEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                                            }
                                            customFieldTemplateDataBean.put("validate", finalValidatePatternEmail);
                                            String defaultEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultEmail);

                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                                                Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.PHONE:
                                            // If user has given regex pattern accept that regex otherwise no regex .
                                            String finalValidatePatternPhone = "";
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty()
                                                    && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                finalValidatePatternPhone = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                            } else {
                                                finalValidatePatternPhone = "/^[0-9.+()-]*$/";
                                            }
                                            String defaultPhone = "/^[0-9.+()-]*$/";
                                            // Used same variable for phone and email
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultPhone);

                                            customFieldTemplateDataBean.put("validate", finalValidatePatternPhone);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            // code for dependancy ends here
                                            Double maxLengthPhone = null;
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                                                maxLengthPhone = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                            } else {
                                                maxLengthPhone = 15.00;
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "textarea");
                                            // If user has given regex pattern accept that regex otherwise no regex .
                                            String finalValidatePatternTextArea = "";
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                                finalValidatePatternTextArea = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                                customFieldTemplateDataBean.put("validate", finalValidatePatternTextArea);
                                            }
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                                                Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString().isEmpty()) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "radio");
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
//                                            if (hkFieldEntity.getFieldValues() != null) {
//                                                radioValuesWithCustomId.put(hkFieldEntity.getId(), hkFieldEntity.getFieldValues());
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(hkFieldEntity.getFieldValues()));
//
//                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.DATE:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                            System.out.println("val.."+ customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString());
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).toString().equals("true")) {
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {

//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                                            } else {

                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION).toString().isEmpty()) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnFieldDate = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnFieldDate);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }
                                            String[] validationsArrDate = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValueDate = "";
                                            for (String validationValue : validationsArrDate) {
                                                if (validationValue.contains("\"constraint\":")) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValueDate += constraintArray[1];
                                                    String constraintValueArray[] = constraintValueDate.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");
//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();
                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);

                                                        if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                            dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                        }
                                                    }
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueDate);
                                                }
                                                Map<String, String> defaultDateForOther = new HashMap<>();
                                                if (fieldEntitiesOfdefaultDateType != null && !fieldEntitiesOfdefaultDateType.isEmpty()) {
                                                    for (HkFieldEntity fieldEntitiesOfdefaultDateType1 : fieldEntitiesOfdefaultDateType) {
                                                        String[] validationsArrDefDate = fieldEntitiesOfdefaultDateType1.getValidationPattern().replace("{", "")
                                                                .replace("}", "")
                                                                .split(",");
                                                        for (String vDefArr : validationsArrDefDate) {
                                                            if (vDefArr.contains("\"defaultDate\":")) {
                                                                String[] vDefArray = vDefArr.split(":");
                                                                String defStringFrDates = vDefArray[1];
                                                                String[] arrFrDefaultDate = defStringFrDates.split("#@");

                                                                if (arrFrDefaultDate[0].replace("\"", "").equals(hkFieldEntity.getDbFieldName())) {
                                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DATE_INVOLVED_FOR_OTHER_DEF_DATE, Boolean.TRUE);
                                                                    if (mapOfFieldsWithSkipHolidays != null && !mapOfFieldsWithSkipHolidays.isEmpty() && mapOfFieldsWithSkipHolidays.containsKey(fieldEntitiesOfdefaultDateType1.getDbFieldName())) {
                                                                        String skipHolidayVal = mapOfFieldsWithSkipHolidays.get(fieldEntitiesOfdefaultDateType1.getDbFieldName());
                                                                        defaultDateForOther.put(fieldEntitiesOfdefaultDateType1.getDbFieldName() + "%" + skipHolidayVal, defStringFrDates);
                                                                    } else {
                                                                        defaultDateForOther.put(fieldEntitiesOfdefaultDateType1.getDbFieldName() + "%" + "false", defStringFrDates);
                                                                    }
                                                                }

                                                            }
                                                        }

                                                    }
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DEFAULT_DATE_OTHER, defaultDateForOther);

                                                }

                                                String[] defaultDateArray;
                                                String defaultString = null;
                                                if (validationValue.contains("\"defaultDate\":")) {
                                                    defaultDateArray = validationValue.split(":");
                                                    String[] seperatorDateArr = defaultDateArray[1].split("#@");
                                                    String def_dep_field = seperatorDateArr[seperatorDateArr.length - 1].replace("\"", "");
                                                    for (String seperatorDateArr1 : seperatorDateArr) {
                                                    }
                                                    if (def_dep_field.equals("Today") || seperatorDateArr[0].replace("\"", "").equals("Today")) {
                                                        if (def_dep_field.equals("Today")) {
                                                            defaultString = defaultDateArray.toString();
                                                        } else {
                                                            try {
                                                                Integer.parseInt(def_dep_field);
                                                                defaultString = defaultDateArray[1].toString();
                                                            } catch (NumberFormatException e) {
                                                            }
                                                        }

                                                    }

                                                }
                                                if (validationValue.contains("\"skipHolidays\":")) {
                                                    String[] skipHolidayArray = validationValue.split(":");
                                                    String skipHolidayDate = skipHolidayArray[1];
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SKIP_HOLIDAY_FOR_DATE, skipHolidayDate);

                                                }
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).toString().equals("true")) {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {
                                                        if (defaultString != null) {
                                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));

                                                        }

                                                    }
                                                }
                                            }
                                            // code for contraint ends here
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            customFieldTemplateDataBean.put("fromModel", "from" + hkFieldEntity.getDbFieldName());
                                            customFieldTemplateDataBean.put("toModel", "to" + hkFieldEntity.getDbFieldName());
//                                        customFieldTemplateDataBean.remove(HkSystemConstantUtil.DynamicFormAttribute.MODEL);
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (StringUtils.hasText((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE)) && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).toString().equals("true")) {
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetimerange");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");
                                            } else {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "daterange");
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION).toString().isEmpty()) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
//                                customFieldTemplateDataBean.put("from-date-time-model", hkFieldEntity.getDbFieldName());
                                            break;

                                        case HkSystemConstantUtil.CustomField.ComponentType.TIME:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "time");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "time");
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.TIME_RANGE:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                            customFieldTemplateDataBean.put("fromModel", "from" + hkFieldEntity.getDbFieldName());
                                            customFieldTemplateDataBean.put("toModel", "to" + hkFieldEntity.getDbFieldName());
//                                        customFieldTemplateDataBean.remove(HkSystemConstantUtil.DynamicFormAttribute.MODEL);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "time");
                                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "timerange");
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.CURRENCY:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "currency");
//                                            String currencyId = hkFieldEntity.getAssociatedCurrency();
//                                            HkCurrencyEntity currencyEntity = currencyMasterMap.get(Long.parseLong(currencyId));
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_TYPE, currencyEntity.getSymbol());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_FORMAT, currencyEntity.getFormat());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_PREFIX, currencyEntity.getSymbolPosition());
                                            if (mapOfCurrencyCodeWithCurrencySymbol != null && !mapOfCurrencyCodeWithCurrencySymbol.isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE_SYMBOL_MAP, mapOfCurrencyCodeWithCurrencySymbol);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_FORMAT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_SYMBOL_POSITION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION));
                                            }
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                String validatePattern = "";
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                String validatePattern = null;

                                                int totalDecimal = 16;
                                                int remainingBeforeDecimal = totalDecimal - afterDecimal;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                                    }

                                                } else {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                                    } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                                        validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                                                String validatePattern = null;
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                    }
                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            }
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnFieldCurrency = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnFieldCurrency);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }
                                            String[] validationsArrNumberForCurrency = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValueForCurrency = "";
                                            for (String validationValue : validationsArrNumberForCurrency) {
                                                if (validationValue.contains("\"constraint\":")) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValueForCurrency += constraintArray[1];
                                                    String constraintValueArray[] = constraintValueForCurrency.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");
//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();

                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);

                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                        }
                                                    }

                                                    if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                    }

                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueForCurrency);
                                                }
                                            }
                                            // code for contraint ends here
                                            // Code for formula starts here added on 20 Feb 2015 By Shifa
                                            Boolean isInvolvedInFormulaForCurrency = false;
//                                            Map<String, String> formulaMap = null;
                                            Map<String, String> formulaInvolvedCurrency = null;
                                            Map<String, String> formulaRoundOffCurr = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedCurrency = new HashMap<>();
                                                formulaRoundOffCurr = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
//                                                    Long fieldEntityFeature = fieldEntity.getFeature();
                                                    String roundToCurr = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundToCurr = roundOffArr[1];
                                                        }
                                                    }

                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForCurrency = true;
                                                        formulaInvolvedCurrency.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOffCurr.put(fieldEntity.getDbFieldName(), roundToCurr);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForCurrency);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOffCurr);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedCurrency);
                                            // code ends here
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "exchangeRate");

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                            }

                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, 3);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, 16);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, "/^\\d{0,16}(\\.\\d{0,3})?$/");
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnFieldCurrency = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnFieldCurrency);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }
                                            String[] validationsArrNumberForExchangeRate = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValueForExchangeRate = "";
                                            for (String validationValue : validationsArrNumberForExchangeRate) {
                                                if (validationValue.contains("\"constraint\":")) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValueForExchangeRate += constraintArray[1];
                                                    String constraintValueArray[] = constraintValueForExchangeRate.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");
//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();

                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);

                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                        }
                                                    }

                                                    if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                    }

                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueForExchangeRate);
                                                }
                                            }
                                            // code for contraint ends here
                                            // Code for formula starts here added on 20 Feb 2015 By Shifa
                                            Boolean isInvolvedInFormulaForExchangeRate = false;
//                                            Map<String, String> formulaMap = null;
                                            Map<String, String> formulaRoundOffExc = null;
                                            Map<String, String> formulaInvolvedExchangeRate = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedExchangeRate = new HashMap<>();
                                                formulaRoundOffExc = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
//                                                    Long fieldEntityFeature = fieldEntity.getFeature();
                                                    String roundToExc = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundToExc = roundOffArr[1];
                                                        }
                                                    }

                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForExchangeRate = true;
                                                        formulaInvolvedExchangeRate.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOffExc.put(fieldEntity.getDbFieldName(), roundToExc);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForExchangeRate);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOffExc);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedExchangeRate);
                                            // code ends here
                                            break;

                                        case HkSystemConstantUtil.CustomField.ComponentType.PERCENT:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                            }

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                String validatePattern = "";
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                String validatePattern = null;

                                                int totalDecimal = 16;
                                                int remainingBeforeDecimal = totalDecimal - afterDecimal;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                                    }

                                                } else {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                                    } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                                        validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                                                String validatePattern = null;
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                    }
                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            }
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnFieldPercent = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnFieldPercent);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }
                                            String[] validationsArrNumberForPercent = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValueForPercent = "";
                                            for (String validationValue : validationsArrNumberForPercent) {
                                                if (validationValue.contains("\"constraint\":")) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValueForPercent += constraintArray[1];
                                                    String constraintValueArray[] = constraintValueForPercent.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");
//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();
                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);

                                                        // Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                        }
                                                    }

                                                    if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                    }

                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueForPercent);
                                                }
                                            }
                                            // code for contraint ends here
                                            // Code for formula starts here added on 20 Feb 2015 By Shifa
                                            Boolean isInvolvedInFormulaForPercent = false;
                                            Map<String, String> formulaInvolvedInPercent = null;
                                            Map<String, String> formulaRoundOffPer = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedInPercent = new HashMap<>();
                                                formulaRoundOffPer = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
//                                                    Long fieldEntityFeature = fieldEntity.getFeature();

                                                    String roundToPer = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundToPer = roundOffArr[1];
                                                        }
                                                    }
                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForPercent = true;
                                                        formulaInvolvedInPercent.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOffPer.put(fieldEntity.getDbFieldName(), roundToPer);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForPercent);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedInPercent);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOffPer);
                                            // code ends here
                                            break;
                                        //
                                        case HkSystemConstantUtil.CustomField.ComponentType.ANGLE:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "Angle");
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
//                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }

                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, "0");
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, "360");
                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                                String validatePattern = "";
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                                    && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                String validatePattern = null;

                                                int totalDecimal = 16;
                                                int remainingBeforeDecimal = totalDecimal - afterDecimal;
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                                    }

                                                } else {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                                    } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                                        validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                                    && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                                    && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                                                Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                                                String validatePattern = null;
                                                if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                    }
                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                        validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                                    }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                                    || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                                            }
                                            // code for handling contraints added by Shifa on 27 January 2015
//                                            List<String> listOfFieldsConstraintOnField = this.listOfFieldEntitiesDependentForConstraintOnField(hkFieldEntity.getId());
//                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.LIST_FIELDS_ON_CONSTRAINT, listOfFieldsConstraintOnField);
                                            if (mapOfFieldIdWithConstraintsDependantOnField != null && !mapOfFieldIdWithConstraintsDependantOnField.isEmpty()) {
                                                if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(hkFieldEntity.getId())) {
                                                    List<String> listOfFieldsConstraintOnField = mapOfFieldIdWithConstraintsDependantOnField.get(hkFieldEntity.getId());
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LIST_OF_CONSTRAINTS_ON_FIELD, listOfFieldsConstraintOnField);

                                                }
                                            }

                                            String[] validationsArrAngle = hkFieldEntity.getValidationPattern().replace("{", "")
                                                    .replace("}", "")
                                                    .split(",");
                                            String constraintValueAngle = "";
                                            for (String validationValue : validationsArrAngle) {
                                                if (validationValue.contains("\"constraint\":")) {

                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                                    String[] constraintArray = validationValue.split(":");
                                                    constraintValueAngle += constraintArray[1];
                                                    String constraintValueArray[] = constraintValueAngle.split("@");
                                                    String id = constraintValueArray[constraintValueArray.length - 1].replace("\"", "");
//                                                    Long idOfField = new Long(id);
                                                    Long idOfField = null;
                                                    HkFieldEntity fieldEntityConstraint = null;
                                                    if (mapOfFieldIdAndFeatureId.containsKey(Long.parseLong(id))) {
                                                        fieldEntityConstraint = mapOfFieldIdAndFeatureId.get(Long.parseLong(id));
                                                        idOfField = fieldEntityConstraint.getFeature();

                                                        // This we are creating as in constraint message we need label instead of label
                                                        String idDbName = fieldEntityConstraint.getDbFieldName();
                                                        String idLabel = fieldEntityConstraint.getFieldLabel();
                                                        dbFieldWithLabelForConstraintMessage.put(idDbName, idLabel);
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_LABEL_FOR_CONST_MSG, dbFieldWithLabelForConstraintMessage);

// Code added by SHifa Salheen on 6 May 2015 to check whether the field on which constraint is dependent is of currency type
                                                        String componentType = fieldEntityConstraint.getComponentType();
                                                        if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CURRENCY_INVOLVED_IN_FIELD, true);
                                                        }
                                                    }

                                                    if (idOfField.equals(hkFieldEntity.getFeature())) {
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT_ON_SAME_FEATURE, true);
                                                        dbFieldNameWithConstraintValueDbField.put(hkFieldEntity.getDbFieldName(), fieldEntityConstraint.getDbFieldName());
                                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD, dbFieldNameWithConstraintValueDbField);
                                                    }
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueAngle);
                                                }
                                            }
                                            // code for contraint ends here
                                            // Code for formula starts here added on 20 Feb 2015 By Shifa
                                            Boolean isInvolvedInFormulaForAngle = false;
                                            Map<String, String> formulaRoundOffAng = null;
                                            Map<String, String> formulaInvolvedAngle = null;
                                            if (!CollectionUtils.isEmpty(fieldEntitiesOfFormulaType)) {
                                                formulaInvolvedAngle = new HashMap<>();
                                                formulaRoundOffAng = new HashMap<>();
                                                Long hkFieldEntityFeature = hkFieldEntity.getFeature();
                                                for (HkFieldEntity fieldEntity : fieldEntitiesOfFormulaType) {
//                                                    Long fieldEntityFeature = fieldEntity.getFeature();
                                                    String roundToAng = null;
                                                    String[] validationsArrFrml = fieldEntity.getValidationPattern().replace("{", "")
                                                            .replace("}", "")
                                                            .split(",");
                                                    for (String vFrmlArr : validationsArrFrml) {

                                                        if (vFrmlArr.contains("\"roundTo\":")) {
                                                            String[] roundOffArr = vFrmlArr.split(":");
                                                            roundToAng = roundOffArr[1];
                                                        }
                                                    }

                                                    if (fieldEntity.getFormulaValue().contains(hkFieldEntity.getDbFieldName()) && hkFieldEntityFeature.equals((Long) fieldEntity.getFeature())) {
                                                        isInvolvedInFormulaForAngle = true;
                                                        formulaInvolvedAngle.put(fieldEntity.getDbFieldName(), fieldEntity.getFormulaValue());
                                                        formulaRoundOffAng.put(fieldEntity.getDbFieldName(), roundToAng);
                                                    }
                                                }

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_INVOLVED_IN_FORMULA, isInvolvedInFormulaForAngle);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULAS_IN_WHICH_FIELD_INVOLVED, formulaInvolvedAngle);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FORMULA_ROUNDOFF, formulaRoundOffAng);
// code ends here
                                            break;
                                        //
                                        case HkSystemConstantUtil.CustomField.ComponentType.UPLOAD:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.FILE);

                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "fileUpload");
//                                            Map<String, Object> flowInit = new HashMap<>();
//                                            flowInit.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put("maxsize", (Double) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SINGLE_FILE, !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE));
//                                                flowInit.put("singleFile", !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.SINGLE_FILE, true);
//                                               
                                            }
//                                            customFieldTemplateDataBean.put("flowInit", flowInit);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).toString().isEmpty()) {
                                                Map<String, Object> flieType = new HashMap<>();
                                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Image")) {
                                                    flieType.put("jpg", 1);
                                                    flieType.put("psd", 1);
                                                    flieType.put("png", 1);
                                                    flieType.put("gif", 1);
                                                    flieType.put("jpeg", 1);
                                                    flieType.put("bmp", 1);
                                                    flieType.put("tiff", 1);
                                                } else if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Text")) {
                                                    flieType.put("txt", 1);
                                                    flieType.put("doc", 1);
                                                    flieType.put("docx", 1);
                                                    flieType.put("oxps", 1);
                                                    flieType.put("pdf", 1);
                                                } else if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Media")) {
                                                    flieType.put("mpg", 1);
                                                    flieType.put("mp3", 1);
                                                    flieType.put("mp4", 1);
                                                } else if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("All")) {
                                                    flieType.put("jpg", 1);
                                                    flieType.put("psd", 1);
                                                    flieType.put("png", 1);
                                                    flieType.put("gif", 1);
                                                    flieType.put("jpeg", 1);
                                                    flieType.put("bmp", 1);
                                                    flieType.put("tiff", 1);
                                                    flieType.put("txt", 1);
                                                    flieType.put("doc", 1);
                                                    flieType.put("docx", 1);
                                                    flieType.put("oxps", 1);
                                                    flieType.put("pdf", 1);
                                                    flieType.put("mpg", 1);
                                                    flieType.put("mp3", 1);
                                                    flieType.put("mp4", 1);
                                                } else if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).equals("Other")) {
                                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().isEmpty()) {
                                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().contains(",")) {
                                                            String[] str = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString().split(",");
                                                            for (String string : str) {
                                                                flieType.put(string, 1);
                                                            }
                                                        } else {
                                                            flieType.put(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SUPPORTED_TYPES_FOR_OTHER).toString(), 1);
                                                        }
                                                    }
                                                } else {

                                                }
                                                customFieldTemplateDataBean.put("acceptedFormats", flieType);
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.IMAGE:
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL));

                                            }
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.IMAGE);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "imageUpload");
                                            customFieldTemplateDataBean.put("width", "100%");
                                            customFieldTemplateDataBean.put("height", "100%");
//                                            Map<String, Object> flowInits = new HashMap<>();
//                                            flowInits.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
//                                            flowInits.put("singleFile", true);
//
//                                            customFieldTemplateDataBean.put("flowInit", flowInits);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put("maxsize", (Double) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                            }
                                            Map<String, Object> flieType = new HashMap<>();
                                            flieType.put("jpg", 1);
                                            flieType.put("png", 1);
                                            flieType.put("psd", 1);
                                            customFieldTemplateDataBean.put("fileFormat", flieType);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                        case HkSystemConstantUtil.CustomField.ComponentType.CHECKBOX:
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "checkbox");
                                            // code added By Shifa on 3 January 2015 in every type to check whether they are dependant fields or not and storing their values
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_ID));

                                            }
                                            if (hkFieldEntity.getIsDependant() != null) {
                                                if (hkFieldEntity.getIsDependant()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, true);

                                                } else {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.IS_DEPENDANT, false);
                                                }
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEPENDANT_MASTER_VALUE));
                                            }
                                            // code for dependancy ends here
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT));
                                            } else {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, "after");
                                            }
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                            break;
                                    }
                                    if (!CollectionUtils.isEmpty(attributeMap)) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.ATTRIBUTES, attributeMap);
                                    }
                                    customFieldTemplateDataBeans.add(customFieldTemplateDataBean);

                                }
                            }

                            if (hkSectionEntity == null) {
                                sectionEntityMap.put("genralSection", customFieldTemplateDataBeans);
                            } else {
                                sectionEntityMap.put(hkSectionEntity.getSectionName(), customFieldTemplateDataBeans);
                            }
                        }

                    }
                }

            }

            return result;
        }
        return null;
    }

    @Async
    public void createFeatureSectionMap(Boolean versionIncrease, HkFieldEntity fieldEntity) throws GenericDatabaseException {

        Map<Long, Map<String, Map<String, Object>>> featureMap = retrieveCompanyFeatureSectionsMap();
        applicationUtil.setFeatureFromTemplateMap(featureMap);
        Map<String, String> systemConfigrationMap = applicationUtil.getSystemConfigrationMap();
        if (systemConfigrationMap == null) {
            systemConfigrationMap = new HashMap<>();
        }
        Integer customFieldVersion = 1;

        if (systemConfigrationMap.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION) != null) {
            customFieldVersion = Integer.parseInt(systemConfigrationMap.get(HkSystemConstantUtil.CUSTOM_FIELD_VERSION));
            if (versionIncrease) {
                customFieldVersion++;
            }
        }
        systemConfigrationMap.put(HkSystemConstantUtil.CUSTOM_FIELD_VERSION, customFieldVersion.toString());
        applicationUtil.setSystemConfigrationMap(systemConfigrationMap);
        systemConfigurationService.updateSystemConfiguration(new UMSystemConfiguration(HkSystemConstantUtil.CUSTOM_FIELD_VERSION, customFieldVersion.toString(), true));
        convertHkFieldEntityToCenterCustomFiled(fieldEntity);
    }

    @Async
    public void convertHkFieldEntityToCenterCustomFiled(HkFieldEntity fieldEntity
    ) {
        if (fieldEntity != null) {
            ExecutorService executorService1 = Executors.newCachedThreadPool();
            executorService1.execute(() -> {
                try {
                    CenterCustomFieldDocument customFieldDocument = new CenterCustomFieldDocument();
                    UMFeature feature = featureService.retrieveFeatureById(fieldEntity.getFeature(), null);
                    customFieldDocument.setFeature(feature.getName());
                    customFieldDocument.setSectionName(fieldEntity.getSection() == null ? "genralSection" : fieldEntity.getSection().getSectionName());
                    customFieldDocument.setLastModifiedOn(fieldEntity.getLastModifiedOn());
                    customFieldDocument.setObject(applicationUtil.getFeatureFromTemplateMap().get(fieldEntity.getFranchise()).get(customFieldDocument.getFeature()).get(customFieldDocument.getSectionName()));
                    customFieldDocument.setFranchise(fieldEntity.getFranchise());
                    SyncRecordedEntity hkRecordedEntity = new SyncRecordedEntity();
                    hkRecordedEntity.setEntity(customFieldDocument);
                    hkRecordedEntity.setIsDirty(true);
                    SyncTransactionEntity hkTransactionEntity = new SyncTransactionEntity();
                    hkTransactionEntity.setTransactionId(new Random().nextLong());
                    hkTransactionEntity.setIsSql(true);
                    hkTransactionEntity.setIsAcknowledge(false);
                    List<SyncRecordedEntity> listRecordedEntity = new ArrayList<>();
                    listRecordedEntity.add(hkRecordedEntity);
                    hkTransactionEntity.setHkRecordedEntitys(listRecordedEntity);
                    xmppClient.sendMessage(hkTransactionEntity, null);
                } catch (GenericDatabaseException ex) {
                    Logger.getLogger(CustomFieldTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XMPPException ex) {
                    Logger.getLogger(CustomFieldTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SmackException.NotConnectedException ex) {
                    Logger.getLogger(CustomFieldTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public Map<Long, Map<String, Map<String, Object>>> retrieveFeatureSectionMap() throws GenericDatabaseException {

        return retrieveCompanyFeatureSectionsMap();
    }

    // deprecated,dont use it
    private Map<Object, String> makeValues(String value) {
        Map<Object, String> valueMap = new HashMap<>();
        if (value != null) {
            for (String singleVal : value.split(",")) {
                String[] values = singleVal.split("\\|");
                valueMap.put(Long.parseLong(values[0]), values[1]);
            }
        }
        return valueMap;
    }
// deprecated dont use it

    private Map<Object, String> makeValuesForSubEntity(String value) {
        Map<Object, String> valueMap = new HashMap<>();
        if (value != null) {
            for (String singleVal : value.toString().split(",")) {
                String[] values = singleVal.split("\\|");
                valueMap.put(values[0], values[1]);
            }
        }
        return valueMap;
    }

    /**
     * @Author Shifa
     * @param search is the search string this method retrieves only
     * Number,Percent,Currency,Formula,Pointer custom components
     * @return Map of customFields
     */
    public Map<String, String> retrieveEntitiesWithCustomFieldsForFormula(String search) {
        Map<String, String> customFieldMap = null;
        List<FeatureDataBean> featureList = featureTransformerBean.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, search);
        Map<Long, List<HkFieldEntity>> featureWithCustomFieldMap = hkFieldService.retrieveFeatureWiseCustomFieldList(Arrays.asList(HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE, HkSystemConstantUtil.CustomField.DbFieldType.INTEGER, HkSystemConstantUtil.CustomField.DbFieldType.LONG), Arrays.asList(HkSystemConstantUtil.CustomField.ComponentType.NUMBER, HkSystemConstantUtil.CustomField.ComponentType.PERCENT, HkSystemConstantUtil.CustomField.ComponentType.CURRENCY, HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE, HkSystemConstantUtil.CustomField.ComponentType.ANGLE, HkSystemConstantUtil.CustomField.ComponentType.FORMULA));
        List<UMFeature> featuresList = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);

        Map<Long, String> featureNameMap = new HashMap<>();
        // featureMap containing key as feature id and value as featureName
        if (!CollectionUtils.isEmpty(featuresList)) {
            for (UMFeature feature : featuresList) {
                featureNameMap.put(feature.getId(), feature.getName());
            }
        }

        if (!CollectionUtils.isEmpty(featureList)) {
            for (FeatureDataBean feature : featureList) {
                List<HkFieldEntity> customFieldsForFeature = featureWithCustomFieldMap.get(feature.getId());
                if (!CollectionUtils.isEmpty(customFieldsForFeature)) {
                    customFieldMap = new HashMap<>();
                    for (HkFieldEntity field : customFieldsForFeature) {
                        if (field.getIsCustomField()) {
                            String Value = feature.getMenuLabel() + "." + field.getFieldLabel();
                            customFieldMap.put(featureNameMap.get(field.getFeature()) + "." + field.getDbFieldName(), Value);
                        }
                    }
                }
            }
        }

        return customFieldMap;
    }

    /**
     * @Author Shifa
     * @param search is the search string for pointer component this method
     * retrieves all custom components
     * @return Map of customFields
     */
    public Map<Long, String> retrieveEntitiesWithCustomFieldsForPointer(String search) {
        Map<Long, String> customFieldMap = null;
        List<FeatureDataBean> featureList = featureTransformerBean.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, search);
        Map<Long, List<HkFieldEntity>> featureWithCustomFieldMap = hkFieldService.retrieveFeatureWiseCustomFieldList(Arrays.asList(HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE, HkSystemConstantUtil.CustomField.DbFieldType.INTEGER, HkSystemConstantUtil.CustomField.DbFieldType.LONG, HkSystemConstantUtil.CustomField.DbFieldType.DATE, HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME, HkSystemConstantUtil.CustomField.DbFieldType.FILE, HkSystemConstantUtil.CustomField.DbFieldType.IMAGE, HkSystemConstantUtil.CustomField.DbFieldType.STRING, HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY, HkSystemConstantUtil.CustomField.DbFieldType.TIME), Arrays.asList(HkSystemConstantUtil.CustomField.ComponentType.ANGLE, HkSystemConstantUtil.CustomField.ComponentType.CURRENCY, HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE, HkSystemConstantUtil.CustomField.ComponentType.DATE, HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN, HkSystemConstantUtil.CustomField.ComponentType.EMAIL, HkSystemConstantUtil.CustomField.ComponentType.IMAGE, HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN, HkSystemConstantUtil.CustomField.ComponentType.NUMBER, HkSystemConstantUtil.CustomField.ComponentType.PERCENT, HkSystemConstantUtil.CustomField.ComponentType.PHONE, HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA, HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD, HkSystemConstantUtil.CustomField.ComponentType.UPLOAD, HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT));
        if (!CollectionUtils.isEmpty(featureList)) {
            for (FeatureDataBean feature : featureList) {
                List<HkFieldEntity> customFieldsForFeature = featureWithCustomFieldMap.get(feature.getId());
                if (!CollectionUtils.isEmpty(customFieldsForFeature)) {
                    customFieldMap = new HashMap<>();
                    for (HkFieldEntity field : customFieldsForFeature) {
                        if (field.getIsCustomField()) {
                            String Value = feature.getMenuLabel() + "." + field.getFieldLabel();
                            customFieldMap.put(field.getId(), Value);
                        }
                    }
                }
            }
        }
        return customFieldMap;
    }

    // method made by Shifa on 6 January 2015 for retrieving fields for constraints
    public Map<Long, String> retrieveEntitiesWithCustomFieldsForConstraints(String searchType, String searchParam) {
        Map<Long, String> customFieldMap = null;
        List<FeatureDataBean> featureList = featureTransformerBean.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, searchParam);
        Map<Long, List<HkFieldEntity>> featureWithCustomFieldMap = new HashMap<>();
        if (searchType.equals("Number")) {
            featureWithCustomFieldMap = hkFieldService.retrieveFeatureWiseCustomFieldList(Arrays.asList(HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE, HkSystemConstantUtil.CustomField.DbFieldType.INTEGER, HkSystemConstantUtil.CustomField.DbFieldType.LONG), null);
        }
        if (searchType.equals("Date")) {
            featureWithCustomFieldMap = hkFieldService.retrieveFeatureWiseCustomFieldList(Arrays.asList(HkSystemConstantUtil.CustomField.DbFieldType.DATE), null);
        }
        if (!CollectionUtils.isEmpty(featureList)) {
            for (FeatureDataBean feature : featureList) {
                List<HkFieldEntity> customFieldsForFeature = featureWithCustomFieldMap.get(feature.getId());
                if (!CollectionUtils.isEmpty(customFieldsForFeature)) {
                    customFieldMap = new HashMap<>();
                    for (HkFieldEntity field : customFieldsForFeature) {
                        if (field.getIsCustomField()) {
                            String Value = feature.getMenuLabel() + "." + field.getFieldLabel();
                            customFieldMap.put(field.getId(), Value);
                        }
                    }
                }
            }
        }
        return customFieldMap;

    }

    /*
     * Added by Dhwani for user multiselect
     */
    public List<UMUser> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive) {
        List<UMUser> users = userManagementServiceWrapper.retrieveUsersByCompanyByStatus(companyId, user, isActive, true, loginDataBean.getId());
        return users;
    }

    public List<UMUser> retrieveUsersByCompanyByDepartments(String search, List<Long> deptIds) {
        List<UMUser> usersByDept = userManagementServiceWrapper.retrieveUsersByCompanyIdAndDepartmentWithSearch(search, loginDataBean.getCompanyId(), deptIds, Boolean.TRUE, loginDataBean.getId());
        return usersByDept;
    }

    public List<UMUser> retrieveUsersByCompanyByRoleIds(String user, List<Long> roleIds) {
        List<UMUser> usersByDept = userManagementServiceWrapper.retrieveUsersByRoleIds(user, roleIds, loginDataBean.getCompanyId());
        return usersByDept;
    }


    /*
     * Added by Dhwani for user multiselect
     */
    public List<UMDepartment> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        List<UMDepartment> departments = userManagementServiceWrapper.retrieveDepartmentsByCompanyByStatus(companyId, department, isActive);
        return departments;
    }

    /*
     * Added by Dhwani for user multiselect
     */
    public List<SelectItem> getSelectItemListFromUserList(List<UMUser> umUsers) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUsers)) {
            for (UMUser uMUser : umUsers) {
                StringBuilder name = new StringBuilder();
                if (uMUser.getUserCode() != null) {
                    name.append(uMUser.getUserCode()).append(" - ");
                }
                name.append(uMUser.getFirstName());
                if (uMUser.getLastName() != null) {
                    name.append(" ").append(uMUser.getLastName());
                }
                hkSelectItems.add(new SelectItem(uMUser.getId(),
                        name.toString(),
                        HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
            }
        }
        return hkSelectItems;
    }

    public Map<String, String> retrieveRecipientNames(List<String> codes) {
        Map<String, String> map = userManagementServiceWrapper.retrieveRecipientNames(codes);
        if (!CollectionUtils.isEmpty(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String id = entry.getKey();
                String name = entry.getValue();
            }
        }
        return map;
    }

    public List<UMRole> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive) {
        List<UMRole> roles = userManagementServiceWrapper.retrieveRolesByCompanyByStatus(companyId, role, isActive, false, loginDataBean.getPrecedence());
        return roles;
    }

    // method made by Shifa on 22 December for retrieving custom fields by field Id
    public CustomFieldInfoDataBean retrieveFieldById(Long id) {
        HkFieldEntity allCustomField = hkFieldService.retrieveCustomFieldByFieldId(id);
        // Code added for storing default values for radio,dropdown custom field components added by shifa on 31 January 2015
        String defaultValue = null;
        String[] validationsArrNumber = allCustomField.getValidationPattern().replace("{", "")
                .replace("}", "")
                .split(",");
        for (String validationValue : validationsArrNumber) {
            if (validationValue.contains("\"defaultValue\":")) {

                String[] defaultArray = validationValue.split(":");
                defaultValue = defaultArray[1].replace("\"", "");
            }
        }
        String defaultSelectedValue = null;
        String defaultMultiSelect = "";
        String defaultSelectedValueForMultiSelect = null;
        Map<String, String> defaultValueMap = null;

        if (defaultValue != null && !defaultValue.isEmpty()) {
            if (allCustomField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
                HkValueEntity hkvalueEntity = hkFoundationService.searchMasterValue(Long.parseLong(defaultValue));
                defaultSelectedValue = hkvalueEntity.getValueName();
            }
            if (allCustomField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                List<Long> defaultIds = null;
                String[] defaultValueArray = defaultValue.replace("\"", "").split("&");
                if (defaultValueArray != null) {
                    List<HkValueEntity> valueEntityList = hkFoundationService.retrieveAllValueEntities();
                    Map<String, String> allValueEntityMap = new HashMap<>();
                    if (!CollectionUtils.isEmpty(valueEntityList)) {
                        for (HkValueEntity valueEntity : valueEntityList) {
                            allValueEntityMap.put(valueEntity.getId().toString(), valueEntity.getValueName());
                        }
                    }
                    defaultValueMap = new HashMap<>();
                    defaultIds = new ArrayList<>();
                    for (String defaultVal : defaultValueArray) {
                        if (!defaultVal.isEmpty()) {
                            if (allValueEntityMap.containsKey(defaultVal.trim())) {
                                defaultValueMap.put(allValueEntityMap.get(defaultVal.trim()), allValueEntityMap.get(defaultVal.trim()));
                            }

                            defaultIds.add(Long.parseLong(defaultVal.trim()));
                        }
                    }

                }
            }
        }
        // code ends here
        List<HkValueEntity> valueEntity = hkFoundationService.retrieveMasterValuesOfSameKeyCodeByCustomFieldId(id);
        CustomFieldInfoDataBean customInfoDataBean = null;
        if (!CollectionUtils.isEmpty(valueEntity)) {
            if (allCustomField != null) {
                customInfoDataBean = this.convertHkFieldEntityToCustomInfoDataBean(allCustomField, valueEntity, defaultSelectedValue, defaultSelectedValueForMultiSelect, defaultValueMap);
            }
        } else {
            customInfoDataBean = this.convertHkFieldEntityToCustomInfoDataBean(allCustomField, null, defaultSelectedValue, defaultSelectedValueForMultiSelect, defaultValueMap);
        }
        return customInfoDataBean;
    }

    // method made by Shifa on 24 December 2014 for removing individual custom field
    public Boolean removeCustomFieldById(Long id) {
        try {
            hkFieldService.removeFieldById(id);
            // Code for removing master and sending notification added By Shifa on 16 MArch 2015
            Short userPrecedence = null;
            if (loginDataBean.getPrecedence() != null) {
                userPrecedence = loginDataBean.getPrecedence().shortValue();
            }
            HkMasterEntity hkMasterEntity = hkFoundationService.retrieveMaster(id.toString(), userPrecedence, loginDataBean.getCompanyId());
            if (hkMasterEntity != null) {
                hkMasterEntity.setIsArchive(Boolean.TRUE);
                List<String> oldKeys = new ArrayList<>();
                for (HkValueEntity hkValueEntity : hkMasterEntity.getHkValueEntityList()) {
                    oldKeys.add(hkValueEntity.getValueName().replace(" ", ""));
                }
                List<Long> notify = new ArrayList<>();
                Long companyId = loginDataBean.getCompanyId();
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
                hkFoundationService.updateMaster(hkMasterEntity, notify, loginDataBean.getCompanyId(), Boolean.TRUE);
                // Code ends here
                // Code for removing all the value entities is deleted
                hkFoundationService.removeAllValueEntitiesOfMasterKey(id);
                // code for removing labels
                if (!CollectionUtils.isEmpty(oldKeys)) {

                    for (String key : oldKeys) {
                        userManagementServiceWrapper.deleteLocaleForEntity(key, id.toString(), "MASTER", loginDataBean.getId(), companyId);
                    }

                }
            }

            createFeatureSectionMap(Boolean.TRUE, null);
            return true;
        } catch (Exception E) {

            E.printStackTrace();
            return false;
        }
    }

    // method made by Shifa on 23 December 2014 as per the requirement changes as now at a time only there would be one customDataBean instead of list
    public Boolean createCustomField(CustomFieldInfoDataBean customFieldInfoDataBean, boolean isWebsocketRequest, Long companyId, Long userId) throws GenericDatabaseException {
        // First check if label exist or not
        Boolean labelAlreadyExist = false;
        Boolean isUpdate = false;
        Long fieldId = null;
        if (!isWebsocketRequest) {
            companyId = loginDataBean.getCompanyId();
            userId = loginDataBean.getId();
        }
        if (customFieldInfoDataBean.getCustomFieldDataBean().getId() != null) {
            isUpdate = true;
            fieldId = customFieldInfoDataBean.getCustomFieldDataBean().getId();
        } else {
            isUpdate = false;
        }
        int numberOfLabelsForFeature = hkFieldService.retrieveNumberOfLabelsForFeature(customFieldInfoDataBean.getCustomFieldDataBean().getLabel(), companyId, customFieldInfoDataBean.getFeatureId(), isUpdate, fieldId);
        // Create time check label
        if (numberOfLabelsForFeature > 0) {
            labelAlreadyExist = true;
        } else {
            labelAlreadyExist = false;
        }
        Map<String, Long> customFieldIdMap = null;
        if (labelAlreadyExist == false) {

            List<String> allSubEntities = null;
            HkFieldEntity hkFieldEntity = new HkFieldEntity();
            CustomFieldDataBean customFieldDataBean = customFieldInfoDataBean.getCustomFieldDataBean();
            Set<HkSubFormFieldEntity> hkSubFormFieldsEntity = new HashSet<>();
            HkFieldEntity fieldEntityForUpdate = null;
            Set<HkSubFormFieldEntity> temphkSubFormFieldsEntity = new HashSet<>();
            if (customFieldDataBean != null) {
                if (customFieldDataBean.getId() != null) {
                    hkFieldEntity.setId(customFieldDataBean.getId());
                    fieldEntityForUpdate = hkFieldService.retrieveCustomFieldByFieldId(customFieldDataBean.getId());
                    temphkSubFormFieldsEntity = fieldEntityForUpdate.getHkSubFormFields();

                    // Update customField
                    List<HkSubFormValueDocument> SubFormValueByInstance = customFieldService.retrieveSubFormValueByInstance(fieldId, Boolean.FALSE);
                    if (!CollectionUtils.isEmpty(SubFormValueByInstance)) {
                        for (HkSubFormValueDocument subDocument : SubFormValueByInstance) {

                            // Custom Field Private or Shared case
                            if (customFieldDataBean.getIsPrivate() != null && customFieldDataBean.getIsPrivate().equals(Boolean.TRUE)) {
                                // if we are changing from false to true...i.e. from shared to private...Then copy createdByFranchise to franchise
                                subDocument.setFranchiseId(subDocument.getCreatedByFranchise());

                            } else {
                                // If we are changing from true to false...i.e. from private to shared...
                                subDocument.setFranchiseId(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
                            }
                        }
                        // Update All SubForm
                        customFieldService.saveSubFormValues(SubFormValueByInstance);
                    }

                }
                hkFieldEntity.setLastModifiedBy(userId);
                hkFieldEntity.setLastModifiedOn(new Date());
//            hkFieldEntity.setAssociatedCurrency(customFieldDataBean.getCurrencyMasterId());
                if (customFieldDataBean.getId() == null) {
                    hkFieldEntity.setCreatedOn(new Date());
                    hkFieldEntity.setCreatedBy(userId);
                }
                hkFieldEntity.setFieldLabel(customFieldDataBean.getLabel());
                if (customFieldDataBean.getIsDependable() != null) {
                    hkFieldEntity.setIsDependant(customFieldDataBean.getIsDependable());
                } else {
                    hkFieldEntity.setIsDependant(Boolean.FALSE);
                }
                hkFieldEntity.setComponentType(customFieldDataBean.getType());
                hkFieldEntity.setValidationPattern(customFieldDataBean.getValidationPattern());
                if (customFieldDataBean.getFormulaValue() != null) {
                    hkFieldEntity.setFormulaValue(customFieldDataBean.getFormulaValue());
                }
                hkFieldEntity.setIsCustomField(true);
                if (customFieldInfoDataBean.getSectionId() != -1l) {
                    hkFieldEntity.setSection(new HkSectionEntity(customFieldInfoDataBean.getSectionId()));
                }
                hkFieldEntity.setFeature(customFieldInfoDataBean.getFeatureId());
                hkFieldEntity.setFieldValues(customFieldDataBean.getValues());
                hkFieldEntity.setFranchise(0l);
                hkFieldEntity.setCreatedByFranchise(companyId);
                hkFieldEntity.setSeqNo(customFieldDataBean.getSeqNo());
                if (customFieldDataBean.getIsPrivate() != null) {
                    hkFieldEntity.setIsPrivate(customFieldDataBean.getIsPrivate());
                } else {
                    hkFieldEntity.setIsPrivate(Boolean.FALSE);
                }
            }
            // Code added by Shifa  on 13 January 2015 for handling subentities
            Set<SubEntityDataBean> subEntityDataBean = null;
            if (customFieldDataBean.getSubEntityDataBean() != null) {
                allSubEntities = hkFieldService.retrieveAllSubEntitiesDbFieldName(loginDataBean.getCompanyId());
                subEntityDataBean = customFieldDataBean.getSubEntityDataBean();
            }
            if (subEntityDataBean == null) {
                subEntityDataBean = new LinkedHashSet<>();
            }
            // List for handling new subentities
            Set< HkSubFormFieldEntity> hksubFormEntityCreateNew = new HashSet<>();
            // Map for handling subentities which needs to be updated
            List<Long> updateSubEntityList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(subEntityDataBean)) {
                for (SubEntityDataBean subEntityDataBean1 : subEntityDataBean) {
                    if (subEntityDataBean1.isIsCreate() != null || subEntityDataBean1.isIsUpdate() != null) {
                        if (subEntityDataBean1.isIsCreate() || subEntityDataBean1.isIsUpdate()) {
                            if (subEntityDataBean1.isIsUpdate() != null) {
                                if (subEntityDataBean1.isIsUpdate()) {
                                    updateSubEntityList.add(subEntityDataBean1.getId());

                                }
                            }
                            HkSubFormFieldEntity hkSubFormEntity = new HkSubFormFieldEntity();
                            hkSubFormEntity.setFranchise(companyId);
                            if (subEntityDataBean1.getComponentType() != null) {
                                hkSubFormEntity.setComponentType(subEntityDataBean1.getComponentType());
                            }

                            hkSubFormEntity.setIsDroplistField(subEntityDataBean1.isIsDroplistField());
                            hkSubFormEntity.setParentField(hkFieldEntity);
                            if (subEntityDataBean1.getSequenceNo() != null) {
                                hkSubFormEntity.setSequenceNo(subEntityDataBean1.getSequenceNo());
                            }

                            hkSubFormEntity.setLastModifiedBy(userId);
                            hkSubFormEntity.setLastModifiedOn(new Date());
                            hkSubFormEntity.setStatus(subEntityDataBean1.getStatus());
                            hkSubFormEntity.setSubFieldLabel(subEntityDataBean1.getSubFieldLabel());

//                        if (subEntityDataBean1.getSubFieldName() != null) {
//                            hkSubFormEntity.setSubFieldName(subEntityDataBean1.getSubFieldName());
//                        }
                            if (subEntityDataBean1.getSubFieldType() != null) {
                                hkSubFormEntity.setSubFieldType(subEntityDataBean1.getSubFieldType());
                            }
                            hkSubFormEntity.setValidationPattern(subEntityDataBean1.getValidationPattern());
                            if (subEntityDataBean1.isIsArchive()) {
                                hkSubFormEntity.setIsArchive(true);
                            } else {
                                hkSubFormEntity.setIsArchive(false);
                            }
                            // Code for storing subFieldName which is the mongoKey
//                            hkSubFormEntity.setSubFieldName(subEntityDataBean1.getSubFieldLabel().replaceAll(" ", "_").toLowerCase());
//                            hkSubFormEntity.setSubFieldName(subEntityDataBean1.getSubFieldLabel().replaceAll("\\.", "_").toLowerCase());

                            if (subEntityDataBean1.isIsCreate()) {
                                String ModifieldSubField = subEntityDataBean1.getSubFieldLabel().replaceAll(" ", "_").toLowerCase();
                                ModifieldSubField = ModifieldSubField.replaceAll("\\.", "_").toLowerCase();
                                hkSubFormEntity.setSubFieldName(ModifieldSubField);
// Code for checking the uniqueness for mongoKey
                                String uniqueSubFieldName = this.uniqueSubFieldName(ModifieldSubField, allSubEntities);
                                hkSubFormEntity.setSubFieldName(uniqueSubFieldName);
                            } else {
                                // don't apply recursion logic for update

                                String SubFieldName = subEntityDataBean1.getSubFieldName();
                                if (SubFieldName != null) {

                                    // Update
                                    hkSubFormEntity.setSubFieldName(subEntityDataBean1.getSubFieldName());
                                } else {
                                    // Local Update,so its isCreate is false,then calculate its subfieldname
                                    String ModifieldSubField = subEntityDataBean1.getSubFieldLabel().replaceAll(" ", "_").toLowerCase();
                                    ModifieldSubField = ModifieldSubField.replaceAll("\\.", "_").toLowerCase();
                                    hkSubFormEntity.setSubFieldName(ModifieldSubField);
                                    String uniqueSubFieldName = this.uniqueSubFieldName(ModifieldSubField, allSubEntities);
                                    hkSubFormEntity.setSubFieldName(uniqueSubFieldName);
                                }

                            }
                            if (subEntityDataBean1.isIsCreate() || subEntityDataBean1.isIsUpdate() && !subEntityDataBean1.isIsArchive()) {
                                hksubFormEntityCreateNew.add(hkSubFormEntity);

                            }

                        }
                    }
                }
                if (!CollectionUtils.isEmpty(hksubFormEntityCreateNew)) {

                    hkSubFormFieldsEntity.addAll(hksubFormEntityCreateNew);

                }
            }
            if (!CollectionUtils.isEmpty(updateSubEntityList)) {
                List<HkSubFormFieldEntity> subFormEntityUpdate = hkFieldService.retrieveSubFormEntityById(updateSubEntityList);
                if (!CollectionUtils.isEmpty(subFormEntityUpdate)) {
                    for (HkSubFormFieldEntity subFormEntityUpdate1 : subFormEntityUpdate) {
                        subFormEntityUpdate1.setIsArchive(true);
                    }
                    hkFieldService.saveSubEntityField(subFormEntityUpdate);
                } else {
                    //System.out.println("empty");
                }
            }

            hkFieldEntity.setHkSubFormFields(hkSubFormFieldsEntity);
            // code ends here
            String dbdocumentName = "";
            if (customFieldInfoDataBean.getSectionId() == -1l) {
                try {
                    UMFeature feature = featureService.retrieveFeatureById(customFieldInfoDataBean.getFeatureId(), null);
                    if (feature != null) {
                        if (HkSystemConstantUtil.featureDocumentMap.containsKey(feature.getName())) {
                            dbdocumentName = HkSystemConstantUtil.featureDocumentMap.get(feature.getName());

                        }
                    }
                } catch (GenericDatabaseException ex) {
                    Logger.getLogger(CustomFieldTransformerBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                HkSectionEntity sectionEntity = hkFieldService.retrieveSectionById(customFieldInfoDataBean.getSectionId(), false);
                if (sectionEntity != null) {
                    if (HkSystemConstantUtil.sectionDocumentMap.containsKey(sectionEntity.getSectionName())) {
                        dbdocumentName = HkSystemConstantUtil.sectionDocumentMap.get(sectionEntity.getSectionName());
                    }
                }
            }

            hkFieldService.saveField(customFieldInfoDataBean.getFeatureId(), customFieldInfoDataBean.getSectionId(), dbdocumentName, hkFieldEntity, true, companyId);
            userManagementServiceWrapper.createLocaleForEntity(hkFieldEntity.getFieldLabel(), "CustomField", userId, companyId);
            //------------------------ end -------------------------------------
            customFieldIdMap = new HashMap<>();
            if (hkFieldEntity != null) {
                customFieldIdMap.put(hkFieldEntity.getFieldLabel(), hkFieldEntity.getId());
            }
            createFeatureSectionMap(Boolean.TRUE, hkFieldEntity);
        }
        return labelAlreadyExist;
    }

    private String uniqueSubFieldName(String subFieldName, List<String> subFieldList) {
        int count = 0;
        if (!CollectionUtils.isEmpty(subFieldList)) {
            for (String dbName : subFieldList) {

                if (dbName.equals(subFieldName)) {
                    count++;
                }
            }
        }
        String newDbFieldName = subFieldName;
        if (count > 0) {
            newDbFieldName = subFieldName + count;
            return uniqueSubFieldName(newDbFieldName, subFieldList);
        } else {
            return newDbFieldName;
        }
    }

    // added by Shifa on 13 JAn 2015
    private Set<SubEntityDataBean> convertHkSubFieldEntityToCustomDataBean(Collection<HkSubFormFieldEntity> hkSubFormFields) {
        Set<SubEntityDataBean> subEntitiesDataBean = new LinkedHashSet<>();
        if (!CollectionUtils.isEmpty(hkSubFormFields)) {

            for (HkSubFormFieldEntity hkSubFormField : hkSubFormFields) {
                SubEntityDataBean subForm = new SubEntityDataBean();
                subForm.setId(hkSubFormField.getId());
                subForm.setSubFieldId(hkSubFormField.getId());
                if (hkSubFormField.getComponentType() != null) {
                    subForm.setComponentType(hkSubFormField.getComponentType());
                }
                subForm.setFranchise(hkSubFormField.getFranchise());
                subForm.setIsArchive(hkSubFormField.getIsArchive());
                subForm.setIsDroplistField(hkSubFormField.getIsDroplistField());
                subForm.setLastModifiedBy(hkSubFormField.getLastModifiedBy());
                subForm.setLastModifiedOn(hkSubFormField.getLastModifiedOn());
                subForm.setIsServerData(Boolean.TRUE);
//                subForm.setParentField(hkSubFormField.getParentField());
                if (hkSubFormField.getSequenceNo() != null) {
                    subForm.setSequenceNo(hkSubFormField.getSequenceNo());
                }
                subForm.setStatus(hkSubFormField.getStatus());
                if (hkSubFormField.getSubFieldLabel() != null) {
                    subForm.setSubFieldLabel(hkSubFormField.getSubFieldLabel());
                }
                if (hkSubFormField.getSubFieldName() != null) {
                    subForm.setSubFieldName(hkSubFormField.getSubFieldName());
                }
                if (hkSubFormField.getSubFieldType() != null) {
                    subForm.setSubFieldType(hkSubFormField.getSubFieldType());
                }
                if (hkSubFormField.getValidationPattern() != null) {
                    subForm.setValidationPattern(hkSubFormField.getValidationPattern());
                }
                if (!hkSubFormField.getIsArchive()) {
                    subEntitiesDataBean.add(subForm);
                }
            }
        }
        return subEntitiesDataBean;
    }

    // method made by Shifa on 22 December 2014
    private CustomFieldInfoDataBean convertHkFieldEntityToCustomInfoDataBean(HkFieldEntity hkFieldEntity, List<HkValueEntity> valueEntity, String defaultValue, String defaultSelectedValueForMultiSelect, Map<String, String> defaultMultiSelectedValueMap) {

        CustomFieldInfoDataBean customInfoDataBean = new CustomFieldInfoDataBean();
        customInfoDataBean.setFeatureId(hkFieldEntity.getFeature());
        if (hkFieldEntity.getSection() != null) {
            customInfoDataBean.setSectionId(hkFieldEntity.getSection().getId());
        }

        customInfoDataBean.setId(hkFieldEntity.getId());
        CustomFieldDataBean customFieldDataBean = new CustomFieldDataBean();
        List<String> valueNames = new ArrayList<>();
        if (valueEntity != null) {
            for (HkValueEntity valueEntity1 : valueEntity) {
                valueNames.add(valueEntity1.getValueName());
            }

            String valueNameList = valueNames.toString();
            String Values = valueNameList.substring(1, valueNameList.length() - 1).replace(", ", ",");
            customFieldDataBean.setValues(Values);
        }
        if (hkFieldEntity.getIsEditable() != null) {
            customFieldDataBean.setIsEditable(hkFieldEntity.getIsEditable());
        }
        if (hkFieldEntity.getHkSubFormFields() != null) {
            Set<SubEntityDataBean> convertHkSubFieldEntityToCustomDataBean = convertHkSubFieldEntityToCustomDataBean(hkFieldEntity.getHkSubFormFields());
            List<SubEntityDataBean> newList = new ArrayList<>(convertHkSubFieldEntityToCustomDataBean);
            customFieldDataBean.setSubEntityDataBean(convertHkSubFieldEntityToCustomDataBean);
            customFieldDataBean.setSubEntityList(newList);
        }
        if (hkFieldEntity.getFieldValues() != null) {
            customFieldDataBean.setValues(hkFieldEntity.getFieldValues());
        }
        customFieldDataBean.setLabel(hkFieldEntity.getFieldLabel());
        if (hkFieldEntity.getSeqNo() != null) {
            customFieldDataBean.setSeqNo(hkFieldEntity.getSeqNo());
        }
        customFieldDataBean.setId(hkFieldEntity.getId());
        if (hkFieldEntity.getComponentType() != null) {
            customFieldDataBean.setType(hkFieldEntity.getComponentType());
        }
        if (hkFieldEntity.getValidationPattern() != null) {
            customFieldDataBean.setValidationPattern(hkFieldEntity.getValidationPattern());
        }
        if (defaultValue != null) {
            customFieldDataBean.setDefaultSelectedValue(defaultValue);
        }
        if (defaultSelectedValueForMultiSelect != null) {
            customFieldDataBean.setDefaultMultiSelectedValue(defaultSelectedValueForMultiSelect);
        }
        if (defaultMultiSelectedValueMap != null) {
            customFieldDataBean.setDefaultMultiSelectedValueMap(defaultMultiSelectedValueMap);
        }
        if (hkFieldEntity.getIsPrivate() != null) {
            customFieldDataBean.setIsPrivate(hkFieldEntity.getIsPrivate());
        }
        customInfoDataBean.setCustomFieldDataBean(customFieldDataBean);
        return customInfoDataBean;
    }

    // method made by Shifa on 30 December 2014 for fetching dependantfields masters
    public List<SelectItem> retrieveCustomFieldMastersForDependantField(Long featureId) {
        List<SelectItem> hkSelectItemsForEntities = new ArrayList<>();
        List<HkFieldEntity> listOfFieldEntities = hkFieldService.retrieveAllFieldsByFeatureId(featureId, null);
        if (!CollectionUtils.isEmpty(listOfFieldEntities)) {
            for (HkFieldEntity listOfFieldEntity : listOfFieldEntities) {
                if (listOfFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON) || listOfFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || listOfFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                    if (listOfFieldEntity.getIsCustomField() == true) {
                        SelectItem hkSelectItem = new SelectItem(listOfFieldEntity.getId(), listOfFieldEntity.getFieldLabel());
                        hkSelectItemsForEntities.add(hkSelectItem);
                    }
                }
            }

        }
        return hkSelectItemsForEntities;
    }

    // method made by Shifa on 20 January 2015 for retrieving list of custom fields which have subentity as component type by feature id
    public List<SelectItem> retrieveCustomFieldOfTypeSubEntityByFeatureId(Long featureId) {
        List<SelectItem> hkSelectItemsForSubEntities = new ArrayList<>();
        List<HkFieldEntity> fieldEntities = hkFieldService.retrieveAllFieldsByFeatureId(featureId, HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY);
        if (!CollectionUtils.isEmpty(fieldEntities)) {
            for (HkFieldEntity fieldEntity : fieldEntities) {
                SelectItem hkSelectItem = new SelectItem(fieldEntity.getId(), fieldEntity.getFieldLabel());
                hkSelectItemsForSubEntities.add(hkSelectItem);

            }

        }
        return hkSelectItemsForSubEntities;
    }
// method made by Shifa on 30 December 2014 for fetching dependantfields values

    public List<SelectItem> retrieveCustomFieldValuesForDependantField(String key) {
        List<SelectItem> hkValueEntityList = new ArrayList<>();
        List<HkValueEntity> listOfValueEntities = hkFoundationService.retrieveMasterValuesOfSameKeyCodeByCustomFieldId(Long.parseLong(key.toString()));
        if (!CollectionUtils.isEmpty(listOfValueEntities)) {
            for (HkValueEntity listOfFieldEntity : listOfValueEntities) {
                SelectItem hkSelectItem = new SelectItem(listOfFieldEntity.getId(), listOfFieldEntity.getValueName());
                hkValueEntityList.add(hkSelectItem);

            }

        }
        return hkValueEntityList;
    }
// method added by Shifa on 20 January for retrieving subEntities which needs to be loaded in directive

    public List<Map<String, Object>> retrieveSubEntities(Long hkFieldEntityId) {
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        List<HkSubFormFieldEntity> listOfSubEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
        List<Map<String, Object>> customFieldTemplateDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {

            for (HkSubFormFieldEntity hkSubFormFieldEntity : listOfSubEntities) {

                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map customFieldPatternMap = (new Gson()).fromJson(hkSubFormFieldEntity.getValidationPattern(), type);
                Map<String, Object> customFieldTemplateDataBean = new HashMap<>();

                if (hkSubFormFieldEntity.getSubFieldLabel() != null) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LABEL, hkSubFormFieldEntity.getSubFieldLabel());
                }
                if (hkSubFormFieldEntity.getSubFieldName() != null) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MODEL, hkSubFormFieldEntity.getSubFieldName());
                }
                if (hkSubFormFieldEntity.getSubFieldType() != null) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, hkSubFormFieldEntity.getSubFieldType());
                }
                if (hkSubFormFieldEntity.getId() != null) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.ID, hkSubFormFieldEntity.getId());
                }
                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE) != null) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.REQUIRED, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE));
                }
                Map<String, Object> attributeMap = new HashMap<>();
                switch (hkSubFormFieldEntity.getComponentType()) {
                    case HkSystemConstantUtil.CustomField.ComponentType.EMAIL:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                        // If user has given regex pattern accept that regex otherwise use default one.
                        String finalValidatePatternEmail = "";
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                            finalValidatePatternEmail = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                        } else {
                            finalValidatePatternEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                        }
                        String defaultEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultEmail);

                        customFieldTemplateDataBean.put("validate", finalValidatePatternEmail);

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.PHONE:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                        // If user has given regex pattern accept that regex otherwise no regex .
                        String finalValidatePatternPhone = "";
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty()
                                && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                            finalValidatePatternPhone = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                        } else {
                            finalValidatePatternPhone = "/^[0-9.+()-]*$/";
                        }
                        String defaultPhone = "/^[0-9.+()-]*$/";
                        // Used same variable for phone and email
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_EMAIL, defaultPhone);

                        customFieldTemplateDataBean.put("validate", finalValidatePatternPhone);
//                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
//                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                        Double maxLengthPhone = null;
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {

                            maxLengthPhone = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                        } else {
                            maxLengthPhone = 15.00;
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "textarea");
                        // If user has given regex pattern accept that regex otherwise no regex .
                        String finalValidatePatternTextArea = "";
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                            finalValidatePatternTextArea = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                            customFieldTemplateDataBean.put("validate", finalValidatePatternTextArea);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString().isEmpty()) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString().isEmpty()) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString().isEmpty()) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MASKED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED));
                        }
                        String finalValidatePattern = "";

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null
                                && !(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().isEmpty())) {
                            String[] allowTypes = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().split(",");
                            String validatePattern = "/^[";
                            for (String allowType : allowTypes) {
                                switch (allowType) {
                                    case "Numeric":
                                        validatePattern += "0-9";
                                        break;
                                    case "Alphabet":
                                        validatePattern += "a-zA-Z";
                                        break;
                                    case "Special character":
                                        validatePattern += "!-@#$%^&*()_ ";
                                        break;
                                }
                            }
                            validatePattern += "]*$/";
                            finalValidatePattern = validatePattern;
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_TEXT, finalValidatePattern);
                        }
                        // Kept in if because it will get override if we have regex
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                            finalValidatePattern = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                        }
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, finalValidatePattern);

                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.NUMBER:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                        } else {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, Boolean.FALSE);

                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                        }
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                            String validatePattern = "";
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                validatePattern += "/^-?";
                            } else {
                                validatePattern += "/^";
                            }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                            validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                            String validatePattern = null;

                            int totalDecimal = 16;
                            int remainingBeforeDecimal = totalDecimal - afterDecimal;
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                } else {
                                    validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                }

                            } else {
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                    validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                }
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                            String validatePattern = null;
                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                } else {
                                    validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                }
                            } else {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                    validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                    validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.PERCENT:

                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
//                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
//                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                        } else {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, Boolean.FALSE);

                        }

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
//                                          
//                                                attributeMap.put(HkSystemConstantUtil.DynamicFormAttribute.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                        }
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                            String validatePattern = "";
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
                                validatePattern += "/^-?";
                            } else {
                                validatePattern += "/^";
                            }
//                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                            validatePattern += "[0-9]{0," + beforeDecimal + "}?$/";

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty())
                                && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                            String validatePattern = null;

                            int totalDecimal = 16;
                            int remainingBeforeDecimal = totalDecimal - afterDecimal;
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, remainingBeforeDecimal);
                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + totalDecimal + "}?$/";
                                } else {
                                    validatePattern = "/^[0-9]{0," + totalDecimal + "}?$/";
                                }

                            } else {
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
//                                                        validatePattern = "-?\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";

                                } else {
//                                                        validatePattern = "\\d{0," + remainingBeforeDecimal + "}(?:\\.[0-9]{0," + afterDecimal + "})?$";
                                    validatePattern = "/^\\d{0," + remainingBeforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                }
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()
                                && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null
                                && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty()) {

                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, afterDecimal);
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, beforeDecimal);

                            String validatePattern = null;
                            if (afterDecimal == 0) {
//                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
//                                             
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                } else {
                                    validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                }
                            } else {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, true);
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED).toString().isEmpty()) {
//                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                    validatePattern = "/^-?\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                } else {

//                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                    validatePattern = "/^\\d{0," + beforeDecimal + "}(\\.\\d{0," + afterDecimal + "})?$/";
                                }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if ((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null
                                || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL).toString().isEmpty()) && (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null
                                || customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL).toString().isEmpty())) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DECIMAL_ALLOW, false);
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.DATE:

                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_DEFAULT_DATE).toString().equals("true")) {
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE).toString().isEmpty()) {
                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                            }
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                        } else {
                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION).toString().isEmpty()) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE).toString().isEmpty()) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }

                        break;

                }
                if (!CollectionUtils.isEmpty(attributeMap)) {
                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.ATTRIBUTES, attributeMap);
                }
                customFieldTemplateDataBeans.add(customFieldTemplateDataBean);
            }

        }

        return customFieldTemplateDataBeans;
    }

    // method added by Shifa on 21 January 2015 for saving subEntitiesValue in mongo
    public void saveSubEntitiesValue(List<SubEntityValueDataBean> SubEntityValueDataBeans) {
        if (!CollectionUtils.isEmpty(SubEntityValueDataBeans)) {
            List<HkSubFormValueDocument> listOfHkSubFormValueDocumentsForUpdate = new ArrayList<>();
            // Fetching first element only as all will contain same instanceId.All subvalues will be associated with single fieldId
            SubEntityValueDataBean subDataBean = SubEntityValueDataBeans.get(0);
            Long instanceId = subDataBean.getInstanceId();
            HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(instanceId);
            // Fecth isPrivate property of the field to which these subForms are associated
            Boolean isPrivate = fieldEntity.getIsPrivate();

            for (SubEntityValueDataBean subEntityValueDataBean : SubEntityValueDataBeans) {
                Map<String, Object> dbValueMap = subEntityValueDataBean.getDbMap();
                dbValueMap.remove("tempId");
                if (dbValueMap.containsKey("isArchive")) {
                    subEntityValueDataBean.setIsArchive(Boolean.TRUE);
                } else {
                    subEntityValueDataBean.setIsArchive(Boolean.FALSE);
                }
                BasicBSONObject bsonObjectForSubEntity = makeBSONObjectForSubEntity(dbValueMap, subEntityValueDataBean.getDbType(), loginDataBean.getCompanyId(), subEntityValueDataBean.getInstanceId());
                HkSubFormValueDocument hkSubFormValueDocument = convertSubEntityIntoHkSubFormValueDocument(bsonObjectForSubEntity, subEntityValueDataBean, isPrivate);
                listOfHkSubFormValueDocumentsForUpdate.add(hkSubFormValueDocument);
            }

            customFieldService.saveSubFormValues(listOfHkSubFormValueDocumentsForUpdate);
        }
    }

    // method added By Shifa on 21 January 2015 for creating BSONObject For Subentity
    private BasicBSONObject makeBSONObjectForSubEntity(Map<String, Object> val, Map<String, String> dbTypeMap, long companyId, Object instanseId) {
        if (!CollectionUtils.isEmpty(val)) {
            BasicBSONObject basicBSONObject = new BasicBSONObject();
            for (Map.Entry<String, Object> entry : val.entrySet()) {
                String colName = entry.getKey();
                Object value = entry.getValue();
                String dbType = dbTypeMap.get(colName);
                if (dbType != null && value != null && !"".equals(value.toString())) {
                    switch (dbType) {
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
                            basicBSONObject.put(colName, new DateTime(value.toString()));

                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()));
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));

                                } catch (ParseException ex) {
                                    Logger.getLogger(HkCustomFieldServiceImpl.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()));
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));

                                } catch (ParseException ex) {
                                    Logger.getLogger(HkCustomFieldServiceImpl.class
                                            .getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                            basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                            basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                            basicBSONObject.put(colName, Long.parseLong(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
                            basicBSONObject.put(colName, value.toString());
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
                            basicBSONObject.put(colName, (ArrayList) value);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
                            basicBSONObject.put(colName, (ArrayList<Long>) value);
                            break;

                    }
                }
            }
            return basicBSONObject;
        }
        return null;
    }

    // method added by Shifa on 21 January 2015
    public HkSubFormValueDocument convertSubEntityIntoHkSubFormValueDocument(BasicBSONObject bsonObjectForSubEntity, SubEntityValueDataBean subEntityValueDataBean, Boolean isPrivate) {
        HkSubFormValueDocument hkSubFormValueDocument = new HkSubFormValueDocument();
        if (subEntityValueDataBean.getId() != null) {
            hkSubFormValueDocument.setId(new ObjectId(subEntityValueDataBean.getId()));
        }
        hkSubFormValueDocument.setFeatureName(HkSystemConstantUtil.FeatureNameForCustomField.SUBENTITY);
        hkSubFormValueDocument.setFieldValue(bsonObjectForSubEntity);
        hkSubFormValueDocument.setInstanceId(subEntityValueDataBean.getInstanceId());
        hkSubFormValueDocument.setIsArchive(subEntityValueDataBean.getIsArchive());
        hkSubFormValueDocument.setCreatedByFranchise(loginDataBean.getCompanyId());
        if (isPrivate != null && isPrivate.equals(Boolean.TRUE)) {
            // this case when custom field is private,So franchise and created by franchise would be of login user franchise
            hkSubFormValueDocument.setFranchiseId(loginDataBean.getCompanyId());
        } else {
            // This case  when custom fiels is shared,So franchise would be of 0 and created by franchise would be login user franchise
            hkSubFormValueDocument.setFranchiseId(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
        }

        return hkSubFormValueDocument;
    }

    // method added by Shifa on 21 January 2015
    public List<SubEntityValueDataBean> convertHkSubFormValueDocumentIntoSubEntity(List<HkSubFormValueDocument> hkSubFormValueDocuments) {
        List<SubEntityValueDataBean> subEntityValueDataBeans = null;
        if (!CollectionUtils.isEmpty(hkSubFormValueDocuments)) {
            subEntityValueDataBeans = new ArrayList<>();
            for (HkSubFormValueDocument hkSubFormValueDocument : hkSubFormValueDocuments) {
                SubEntityValueDataBean subEntityValueDataBean = new SubEntityValueDataBean();
                subEntityValueDataBean.setId(hkSubFormValueDocument.getId().toString());
                subEntityValueDataBean.setTempId(hkSubFormValueDocument.getId().toString());
                subEntityValueDataBean.setInstanceId(hkSubFormValueDocument.getInstanceId());
                if (hkSubFormValueDocument.getFieldValue() != null) {
                    subEntityValueDataBean.setDbMap(hkSubFormValueDocument.getFieldValue().toMap());
                }
                subEntityValueDataBeans.add(subEntityValueDataBean);

            }

        }
        return subEntityValueDataBeans;

    }

    // method added by Shifa on 21 January 2015 for retrieving subform documents by Instance Id
    public List<HkSubFormValueDocument> retrieveSubFormValueByInstanceid(long instanceId) {
        List<HkSubFormValueDocument> hkSubFormDocuments = customFieldService.retrieveSubFormValueByInstance(instanceId, Boolean.FALSE);
        return hkSubFormDocuments;
    }

    // This method returns selectItemList containing id as ObjectId and its value for a particular customfield which is of subentitytype
    // This list is used for filling the dropdown for the customfield which is of type subEntityon page
    // Created By Shifa Salheen on 22 January 2015
    public List<SelectItem> createDropDownListForSubEntity(long hkFieldEntityId) {
        List<SelectItem> selectItemList = null;
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        Map<String, HkSubFormFieldEntity> SubEntityDropField = new HashMap<>();
        //MM-ISSUE-FOR-SHIFA You can merge above method as well as this one
        List<HkSubFormFieldEntity> listOfSubEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            for (HkSubFormFieldEntity listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    SubEntityDropField.put(listOfSubEntity.getSubFieldName(), listOfSubEntity);
                }
            }
        }
        List<HkSubFormValueDocument> subFormDocuments = customFieldService.retrieveSubFormValueByInstance(hkFieldEntityId, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(subFormDocuments)) {
            selectItemList = new ArrayList<>();
            for (HkSubFormValueDocument subFormDocument : subFormDocuments) {
                Map<String, Object> fieldValueMap = subFormDocument.getFieldValue().toMap();
                for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                    if (SubEntityDropField.containsKey(entrySet.getKey())) {
                        SelectItem selectItem = new SelectItem(entrySet.getValue().toString(), subFormDocument.getId().toString());
                        selectItemList.add(selectItem);
                    }
                }
            }
        }
        return selectItemList;
    }

    public String retrieveObjectIdValueFromMongoForConstraint(Long fieldId, String invoiceId, String parcelId, String lotId, String packetId) throws GenericDatabaseException {
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(fieldId);
        Long featureId = fieldEntity.getFeature();
        String dbFieldName = fieldEntity.getDbFieldName();
        String instanceValue = null;
        UMFeature uMFeature = featureService.retrieveFeatureById(featureId, null);
        String featureName = uMFeature.getName();
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.INVOICE)) {
            HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(new ObjectId(invoiceId));
            Map invoiceFieldValueMap = invoiceDocument.getFieldValue().toMap();
            if (invoiceFieldValueMap.containsKey(dbFieldName)) {
                boolean flag = invoiceFieldValueMap.get(dbFieldName) instanceof Date;
                if (flag) {
                    String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .format(invoiceFieldValueMap.get(dbFieldName));
                    instanceValue = formatted;
                } else {
                    instanceValue = (String) invoiceFieldValueMap.get(dbFieldName).toString();
                }
            }
        }
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.PARCEL)) {
            HkParcelDocument parcelDocument = stockService.retrieveParcelById(new ObjectId(parcelId));
            Map parcelValueMap = parcelDocument.getFieldValue().toMap();
            if (parcelValueMap.containsKey(dbFieldName)) {
                instanceValue = (String) parcelValueMap.get(dbFieldName).toString();
            }
        }
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.LOT)) {
            HkLotDocument lotDocument = stockService.retrieveLotById(new ObjectId(lotId));
            Map lotFieldValueMap = lotDocument.getFieldValue().toMap();
            if (lotFieldValueMap.containsKey(dbFieldName)) {
                instanceValue = (String) lotFieldValueMap.get(dbFieldName).toString();
            }
        }

        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.PACKET)) {
            HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(packetId), true);
            Map packetFieldValueMap = packetDocument.getFieldValue().toMap();
            if (packetFieldValueMap.containsKey(dbFieldName)) {
                instanceValue = (String) packetFieldValueMap.get(dbFieldName).toString();
            }
        }
        return instanceValue;
    }

    public Map<String, String> retrieveValueFromMongoForFormula(List<String> dbFieldNameList, String invoiceId, String parcelId, String lotId, String packetId) {
        Map<String, String> dbFieldNameWithInstanceValue = new HashMap<>();
        List<String> invoiceList = new ArrayList<>();
        List<String> parcelList = new ArrayList<>();
        List<String> lotList = new ArrayList<>();
        List<String> packetList = new ArrayList<>();
        Map<String, String> invoiceMap = new HashMap<>();
        Map<String, String> parcelMap = new HashMap<>();
        Map<String, String> lotMap = new HashMap<>();
        Map<String, String> packetMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(dbFieldNameList)) {
            for (String dbFieldName : dbFieldNameList) {
                if (dbFieldName.contains("(")) {
                    String[] split = dbFieldName.split("\\(");
                    String[] splitSecondPart = split[1].split("\\.");
                    if (splitSecondPart[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        invoiceMap.put(splitSecondPart[1].replace(")", ""), split[0]);
                    }
                    if (splitSecondPart[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        parcelMap.put(splitSecondPart[1].replace(")", ""), split[0]);
                    }
                    if (splitSecondPart[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        lotMap.put(splitSecondPart[1].replace(")", ""), split[0]);
                    }
                    if (splitSecondPart[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        packetMap.put(splitSecondPart[1].replace(")", ""), split[0]);
                    }
                } else {
                    String[] newDbField = dbFieldName.split("\\.");
                    if (newDbField[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        invoiceList.add(newDbField[1]);
                    }
                    if (newDbField[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        parcelList.add(newDbField[1]);
                    }
                    if (newDbField[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        lotList.add(newDbField[1]);
                    }
                    if (newDbField[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)) {
                        packetList.add(newDbField[1]);
                    }
                }
            }

        }
        // loop ends here
        if (!CollectionUtils.isEmpty(invoiceList)) {
            if (invoiceId != null && !(invoiceId.equalsIgnoreCase("undefined")) && invoiceId.length() > 0) {
                // retrieve invoice documents on the basis of invoice id
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(new ObjectId(invoiceId));
                // fetch the fieldvalue and by iterating fetch the mongo value
                Map invoiceFieldValueMap = invoiceDocument.getFieldValue().toMap();
                for (String invoice : invoiceList) {
                    if (invoiceFieldValueMap.containsKey(invoice)) {
                        dbFieldNameWithInstanceValue.put(invoice, invoiceFieldValueMap.get(invoice).toString());
                    } else {
                        dbFieldNameWithInstanceValue.put(invoice, HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(parcelMap)) {
            List<ObjectId> objectIds = null;
            if (invoiceId != null && !(invoiceId.equalsIgnoreCase("undefined")) && invoiceId.length() > 0) {
                objectIds = new ArrayList<>();
                objectIds.add(new ObjectId(invoiceId));
            }
            List<HkParcelDocument> parcelDocuments = null;
            UMFeature feature = userManagementServiceWrapper.retrieveUMFeatureByNameAndCompanyId(HkSystemConstantUtil.Feature.PARCEL, loginDataBean.getCompanyId());
            if (feature != null) {
                parcelDocuments = stockService.retrieveParcels(null, objectIds, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
            }

            for (Map.Entry<String, String> entrySet : parcelMap.entrySet()) {
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    Double aggregrateFunctionResult = stockService.calculateParcelAggegateFunction(parcelDocuments, entrySet.getKey(), entrySet.getValue());
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), aggregrateFunctionResult.toString());

                } else {
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);

                }
            }
        }
        if (!CollectionUtils.isEmpty(parcelList)) {
            if (parcelId != null && !(parcelId.equalsIgnoreCase("undefined")) && parcelId.length() > 0) {
                HkParcelDocument parcelDocument = stockService.retrieveParcelById(new ObjectId(parcelId));
                Map parcelFieldValueMap = parcelDocument.getFieldValue().toMap();
                for (String parcel : parcelList) {
                    if (parcelFieldValueMap.containsKey(parcel)) {
                        dbFieldNameWithInstanceValue.put(parcel, parcelFieldValueMap.get(parcel).toString());
                    } else {
                        dbFieldNameWithInstanceValue.put(parcel, HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(lotMap)) {
            List<ObjectId> invoiceObjectIds = null;
            if (invoiceId != null && !(invoiceId.equalsIgnoreCase("undefined")) && invoiceId.length() > 0) {
                invoiceObjectIds = new ArrayList<>();
                invoiceObjectIds.add(new ObjectId(invoiceId));
            }
            List<ObjectId> parcelObjectIds = null;
            if (parcelId != null && !(parcelId.equalsIgnoreCase("undefined")) && parcelId.length() > 0) {
                parcelObjectIds = new ArrayList<>();
                parcelObjectIds.add(new ObjectId(parcelId));
            }

            List<HkLotDocument> lotDocuments = stockService.retrieveLots(null, invoiceObjectIds, parcelObjectIds, null, loginDataBean.getCompanyId(), Boolean.FALSE);
            for (Map.Entry<String, String> entrySet : lotMap.entrySet()) {
                if (!CollectionUtils.isEmpty(lotDocuments) && lotDocuments.size() > 0) {
                    Double aggregrateFunctionResult = stockService.calculateLotAggegateFunction(lotDocuments, entrySet.getKey(), entrySet.getValue());
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), aggregrateFunctionResult.toString());
                } else {
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                }
            }
        }
        if (!CollectionUtils.isEmpty(lotList)) {
            if (lotId != null && !(lotId.equalsIgnoreCase("undefined")) && lotId.length() > 0) {
                HkLotDocument lotDocument = stockService.retrieveLotById(new ObjectId(lotId));
                Map lotFieldValueMap = lotDocument.getFieldValue().toMap();
                for (String lot : lotList) {
                    if (lotFieldValueMap.containsKey(lot)) {
                        dbFieldNameWithInstanceValue.put(lot, lotFieldValueMap.get(lot).toString());
                    } else {
                        dbFieldNameWithInstanceValue.put(lot, HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(packetMap)) {
            List<ObjectId> invoiceObjectIds = null;
            if (invoiceId != null && !(invoiceId.equalsIgnoreCase("undefined")) && invoiceId.length() > 0) {
                invoiceObjectIds = new ArrayList<>();
                invoiceObjectIds.add(new ObjectId(invoiceId));
            }
            List<ObjectId> parcelObjectIds = null;
            if (parcelId != null && !(parcelId.equalsIgnoreCase("undefined")) && parcelId.length() > 0) {
                parcelObjectIds = new ArrayList<>();
                parcelObjectIds.add(new ObjectId(parcelId));
            }
            List<ObjectId> lotObjectIds = null;
            if (lotId != null && !(lotId.equalsIgnoreCase("undefined")) && lotId.length() > 0) {
                lotObjectIds = new ArrayList<>();
                lotObjectIds.add(new ObjectId(lotId));
            }

            List<HkPacketDocument> packetDocuments = stockService.retrievePackets(null, invoiceObjectIds, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, null, null, null);
            for (Map.Entry<String, String> entrySet : packetMap.entrySet()) {
                if (!CollectionUtils.isEmpty(packetDocuments) && packetDocuments.size() > 0) {
                    Double aggregrateFunctionResult = stockService.calculatePacketAggegateFunction(packetDocuments, entrySet.getKey(), entrySet.getValue());
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), aggregrateFunctionResult.toString());
                } else {
                    dbFieldNameWithInstanceValue.put(entrySet.getKey(), HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                }
            }
        }
        if (!CollectionUtils.isEmpty(packetList)) {
            if (packetId != null && !(packetId.equalsIgnoreCase("undefined")) && packetId.length() > 0) {
                HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(packetId), true);
                Map packetFieldValueMap = packetDocument.getFieldValue().toMap();
                for (String packet : packetList) {
                    if (packetFieldValueMap.containsKey(packet)) {
                        dbFieldNameWithInstanceValue.put(packet, packetFieldValueMap.get(packet).toString());
                    } else {
                        dbFieldNameWithInstanceValue.put(packet, HkSystemConstantUtil.DIAMOND_NOT_AVAILABLE);
                    }
                }
            }
        }

        return dbFieldNameWithInstanceValue;
    }

    public Map<String, String> retrieveFeatureNameMapOfDbFieldList(List<String> dbFieldNameList) {
        Map<String, String> featureNameMapWithDbField = null;
        //MM-ISSUE-FOR-SHIFA As we have to retrieve fields based on DB Field Name, which is unique. No need to fetch featurelist first. Discuss if require.
        List<UMFeature> featuresList = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);
        Map<Long, String> featureNameMap = new HashMap<>();
        // featureMap containing key as feature id and value as featureName
        if (!CollectionUtils.isEmpty(featuresList)) {
            for (UMFeature feature : featuresList) {
                featureNameMap.put(feature.getId(), feature.getName());
            }
        }
        if (!CollectionUtils.isEmpty(dbFieldNameList)) {
            featureNameMapWithDbField = new HashMap<>();

            Map<String, HkFieldEntity> mapOfDbFieldWithEntity = hkFieldService.retrieveMapOfDBFieldNameWithEntity(dbFieldNameList, loginDataBean.getCompanyId());
            if (!mapOfDbFieldWithEntity.isEmpty()) {
                for (Map.Entry<String, HkFieldEntity> entrySet : mapOfDbFieldWithEntity.entrySet()) {
                    Long featureId = entrySet.getValue().getFeature();
                    if (featureNameMap.containsKey(featureId)) {
                        featureNameMapWithDbField.put(entrySet.getKey(), featureNameMap.get(featureId));
                    }
                }
            }
        }
        return featureNameMapWithDbField;
    }

    public List<String> retrieveValueNamesForMultiSelect(String param) {
        List<String> valueNameList = null;
        if (param != null && !"undefined".equals(param) && param.length() > 0) {
            String[] valueIdArray = param.split(",");
            List<Long> valueIds = new ArrayList<>();
            for (String valueId : valueIdArray) {
                valueIds.add(Long.valueOf(valueId.trim()));
            }
            List<HkValueEntity> ValueEntityList = hkFoundationService.retrieveValueEntityListByValueIds(valueIds);
            if (!CollectionUtils.isEmpty(ValueEntityList)) {
                valueNameList = new ArrayList<>();
                for (HkValueEntity ValueEntity : ValueEntityList) {
                    valueNameList.add(ValueEntity.getValueName());
                }
            }

        }
        return valueNameList;
    }

    public String retrieveSubEntityNames(String param, String fieldId) {
        String subEntityName = null;
        if (param != null && !"undefined".equals(param) && param.length() > 0) {
            List<SelectItem> dropdownListForSubentity = this.createDropDownListForSubEntity(Long.parseLong(fieldId));
            if (!CollectionUtils.isEmpty(dropdownListForSubentity)) {
                for (SelectItem select : dropdownListForSubentity) {
                    if (select.getId().equals(param)) {
                        subEntityName = select.getLabel();
                    }
                }
            }
        }
        return subEntityName;
    }

    public Map<Long, String> retrieveAllEntitiesWithCustomFields() {
        Map<Long, String> customFieldMap = new HashMap<>();
        List<FeatureDataBean> featureList = featureTransformerBean.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);
        Map<Long, List<HkFieldEntity>> featureWithCustomFieldMap = hkFieldService.retrieveFeatureWiseCustomFieldList(null, null);
        if (!CollectionUtils.isEmpty(featureList)) {
            for (FeatureDataBean feature : featureList) {
                List<HkFieldEntity> customFieldsForFeature = featureWithCustomFieldMap.get(feature.getId());
                if (!CollectionUtils.isEmpty(customFieldsForFeature)) {
                    for (HkFieldEntity field : customFieldsForFeature) {
                        if (field.getIsCustomField()) {
                            String Value = feature.getMenuLabel() + "." + field.getFieldLabel();
                            customFieldMap.put(field.getId(), Value);
                        }
                    }
                }
            }
        }
        return customFieldMap;
    }

    public Map<String, String> retrieveRecipientNames(String recipientIds) {
        recipientIds = recipientIds.replace("[", "").replace("]", "").replace("\"", "").trim();
        List<String> recipientCodes = new ArrayList<>();
        String[] split = recipientIds.split(",");
        for (String split1 : split) {
            recipientCodes.add(split1.trim());
        }
        Map<String, String> recipentMap = userManagementServiceWrapper.retrieveRecipientNames(recipientCodes);
        return recipentMap;
    }

    public Boolean checkFieldIsInvolvedInOtherFields(Long id) {
        List<HkFieldEntity> allCustomFields = hkFieldService.retrieveAllFieldsByCompanyId(loginDataBean.getCompanyId());
        List<HkFieldEntity> listOfCustomFieldsDependantOnFieldId = new ArrayList<>();
        HkFieldEntity customFieldEntity = hkFieldService.retrieveCustomFieldByFieldId(id);
        UMFeature featureEntity = userManagementServiceWrapper.retrieveFeatureByIdForCustom(customFieldEntity.getFeature());
        String formulaString = featureEntity.getName() + "." + customFieldEntity.getDbFieldName();

        if (!CollectionUtils.isEmpty(allCustomFields)) {
            for (HkFieldEntity customField : allCustomFields) {
                // To check if the field is present in formula Value of some other custom field
                String formulaValue = customField.getFormulaValue();
                if (formulaValue != null) {
                    if (formulaValue.contains(formulaString)) {
                        listOfCustomFieldsDependantOnFieldId.add(customField);
                    }
                }

                String[] validationsArr = customField.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                String constraintValue = "";
                String pointerValue = "";
                String dependantValue = "";

                for (String validationValue : validationsArr) {
                    // To check if the field is present in constraint validation pattern of some other custom field
                    if (validationValue.contains("\"constraint\":")) {
                        String[] constraintArray = validationValue.split(":");
                        constraintValue += constraintArray[1];
                        String[] splitConstraint = constraintValue.split("@");

                        if (splitConstraint[splitConstraint.length - 1].replace("\"", "").equals(id.toString())) {
                            listOfCustomFieldsDependantOnFieldId.add(customField);
                        }
                    }
                    // To check if the field is present in pointer validation pattern of some other custom field
                    if (validationValue.contains("\"pointer\":")) {
                        String[] pointerArray = validationValue.split(":");
                        pointerValue += pointerArray[1];
                        if (pointerValue.replace("\"", "").equals(id.toString())) {
                            listOfCustomFieldsDependantOnFieldId.add(customField);
                        }
                    }
                    // To check if the field is present as dependant field in  other custom field
                    if (validationValue.contains("\"dependantMasterId\":")) {
                        String[] dependantMasterArray = validationValue.split(":");
                        dependantValue += dependantMasterArray[1];
                        if (dependantValue.replace("\"", "").equals(id.toString())) {
                            listOfCustomFieldsDependantOnFieldId.add(customField);
                        }
                    }
                }
            }

        }
        return listOfCustomFieldsDependantOnFieldId.size() > 0;
    }

    public Set<SubEntityDataBean> retrieveDropListEntityForSubEntity(Long fieldId, Long companyId) {
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(fieldId);
        List<HkSubFormFieldEntity> listOfSubFormEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
        Set<SubEntityDataBean> subEntityDataBean = this.convertHkSubFieldEntityToCustomDataBean(listOfSubFormEntities);
        return subEntityDataBean;
    }

    // For Currency Component of custom field
    // Methods added by Shifa on 19 March 2015
    public List<SelectItem> retrieveCurrencyForCombo() {
        List<SelectItem> selectItems = null;
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.CURRENCY_MASTER_MAP)) {
            Map<String, String> currencyMapUI = new HashMap<>(HkSystemConstantUtil.CURRENCY_MASTER_MAP);
            selectItems = new ArrayList<>();
            String code = "";
            String currencyLabel = "";
            String description = "";
            if (!CollectionUtils.isEmpty(currencyMapUI)) {
                for (Map.Entry<String, String> entry : currencyMapUI.entrySet()) {
                    code = entry.getKey();
                    currencyLabel = code.concat(" ").concat("(").concat(entry.getValue()).concat(")");;
                    description = "";
                    int indexOf = currencyLabel.indexOf("'");
                    if (indexOf > 0) {
                        description = currencyLabel.substring(indexOf);
                    }
                    SelectItem selectItem = new SelectItem((String) code, currencyLabel, description);
                    selectItems.add(selectItem);
                }
            }
            Collections.sort(selectItems, CommonUtil.NameComparator);
        }
        return selectItems;
    }

    // This method is for displaying currency code options where currency component will be generated
    // Code to be used in dynamic form.js
    // Methods added by Shifa on 20 March 2015
    public List<SelectItem> retrieveAllCurrencyByLoginId() {
        return this.retrieveCurrencyForComboForDynamicForm();
    }

    public List<SelectItem> retrieveCurrencyForComboForDynamicForm() {
        List<SelectItem> selectItems = null;
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.CURRENCY_CODE_MAP)) {
            Map<String, String> currencyMapUI = new HashMap<>(HkSystemConstantUtil.CURRENCY_CODE_MAP);
            selectItems = new ArrayList<>();
            String code;
            String currencyLabel;
            String description;
            if (!CollectionUtils.isEmpty(currencyMapUI)) {
                for (Map.Entry<String, String> entry : currencyMapUI.entrySet()) {
                    code = entry.getKey();
                    currencyLabel = entry.getValue();
                    description = "";
                    int indexOf = currencyLabel.indexOf("'");
                    if (indexOf > 0) {
                        description = currencyLabel.substring(indexOf);
                    }
                    SelectItem selectItem = new SelectItem((String) code, currencyLabel, description);
                    selectItems.add(selectItem);
                }
            }
            Collections.sort(selectItems, CommonUtil.NameComparator);
        }
        return selectItems;
    }

    public Map<String, String> mapOfCurrencyCodeWithCurrencySymbol() {
        Map<String, String> mapOfCurrencyCodeWithCurrencySymbol = null;
        List<SelectItem> currencyForComboForDynamicForm = this.retrieveCurrencyForComboForDynamicForm();
        if (!CollectionUtils.isEmpty(currencyForComboForDynamicForm)) {
            mapOfCurrencyCodeWithCurrencySymbol = new HashMap<>();
            for (SelectItem currencyForCombo : currencyForComboForDynamicForm) {
                mapOfCurrencyCodeWithCurrencySymbol.put((String) currencyForCombo.getValue(), currencyForCombo.getLabel());
            }
        }
        return mapOfCurrencyCodeWithCurrencySymbol;
    }

    public Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId(Long instanceId, HkSystemConstantUtil.FeatureNameForCustomField feature, long companyId) {
        List<GenericDocument> documents = (List<GenericDocument>) customFieldService.retrieveDocument(instanceId, feature, companyId);

        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionCustomFieldMap = null;
        if (!CollectionUtils.isEmpty(documents)) {
            //MM-ISSUE-FOR-SHIFA Add constant for @*
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (GenericDocument genericDocument : documents) {
                if (genericDocument.getFieldValue() != null) {
                    Map<String, Object> customMap = genericDocument.getFieldValue().toMap();
                    List<String> uiFieldList = new ArrayList<>();
                    for (Map.Entry<String, Object> entrySet : customMap.entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                    //MM-ISSUE-FOR-SHIFA We can remove this call now as component type already available with the field name
                    Map<String, String> uiFieldListWithComponentType = hkFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                    if (!CollectionUtils.isEmpty(uiFieldListWithComponentType)) {
                        for (Map.Entry<String, String> field : uiFieldListWithComponentType.entrySet()) {
                            if (field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || field.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || field.getValue().equals(pointerComponentType)) {
                                if (customMap.containsKey(field.getKey())) {
                                    String value = customMap.get(field.getKey()).toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                    customMap.put(field.getKey(), value);
                                }
                            }
                        }
                    }
                    if (genericDocument.getFieldValue() != null) {
                        sectionCustomFieldMap = new HashMap<>();
                        List<Map<Long, Map<String, Object>>> customFieldMapList = new ArrayList<>();
                        Map<Long, Map<String, Object>> idFieldMap = new HashMap<>();
                        idFieldMap.put(genericDocument.getInstanceId(), customMap);
                        customFieldMapList.add(idFieldMap);
                        sectionCustomFieldMap.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, customFieldMapList);
                    }
                }
                if (!CollectionUtils.isEmpty(genericDocument.getSectionList())) {
                    for (SectionDocument sectionDocument : genericDocument.getSectionList()) {
                        List<Map<Long, Map<String, Object>>> customFieldMapList = new ArrayList<>();
                        Map<Long, Map<String, Object>> idFieldMap = new HashMap<>();
                        if (!CollectionUtils.isEmpty(sectionDocument.getCustomFields())) {
                            for (CustomField customField : sectionDocument.getCustomFields()) {
                                idFieldMap.put(customField.getId(), customField.getFieldValue().toMap());
                            }
                            customFieldMapList.add(idFieldMap);
                            sectionCustomFieldMap.put(sectionDocument.getSectionName(), customFieldMapList);
                        }
                    }
                }
            }
            return sectionCustomFieldMap;
        }
        return null;
    }

    public List<UMUser> retrieveUserByDesignation(List<Long> roleId, String search) {
        return userManagementServiceWrapper.retrieveUsersByRoleId(roleId, search, loginDataBean.getCompanyId());
    }

    // dont use this method for storing in local storage
    public Map<Object, String> makeValuesForCarateRange() {
        Map<Long, HkCaratRangeEntity> carateRangeMap;
        Map<Object, String> valueMap = null;
        List<HkCaratRangeEntity> caratRangeEntitys = hkFoundationService.retrieveCaratRangeByFranchiseAndStatus(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.ACTIVE), null);
        if (!CollectionUtils.isEmpty(caratRangeEntitys)) {
            carateRangeMap = new HashMap<>();
            for (HkCaratRangeEntity hkCaratRangeEntity : caratRangeEntitys) {
                carateRangeMap.put(hkCaratRangeEntity.getId(), hkCaratRangeEntity);
            }

            valueMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(carateRangeMap)) {
                for (Map.Entry<Long, HkCaratRangeEntity> entry : carateRangeMap.entrySet()) {
                    Long id = entry.getKey();
                    HkCaratRangeEntity caratRangeEntity = entry.getValue();
                    String range = caratRangeEntity.getMinValue() + " - " + caratRangeEntity.getMaxValue();
                    valueMap.put(id, range);
                }
            }
        }
        return valueMap;
    }

    public List<SyncCenterUserDocument> retrieveCenterUsersByDesignation(List<Long> roleId, String search) {
        return syncCenterUserService.retrieveUsersByRoleId(roleId, search, loginDataBean.getCompanyId());
    }

    public List<SelectItem> getSelectItemListFromCenterUserList(List<SyncCenterUserDocument> umUsers) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUsers)) {
            for (SyncCenterUserDocument centerUserDocument : umUsers) {
                StringBuilder name = new StringBuilder();
                if (centerUserDocument.getUserCode() != null) {
                    name.append(centerUserDocument.getUserCode()).append(" - ");
                }
                name.append(centerUserDocument.getFirstName());
                if (centerUserDocument.getLastName() != null) {
                    name.append(" ").append(centerUserDocument.getLastName());
                }
                hkSelectItems.add(new SelectItem(centerUserDocument.getId(),
                        name.toString(),
                        HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
            }
        }
        return hkSelectItems;
    }

    public Map<Long, String> makeValuesForMachineAssets() {
        Map<Long, String> valueMap = null;
        HkCategoryEntity machineCategory = hkFoundationService.retrieveAssetCategoryByPrefix(HkSystemConstantUtil.FRANCHISE_REQUIREMENT_TYPE.MACHINES, loginDataBean.getCompanyId());
        if (machineCategory != null) {
            List<HkAssetEntity> assets = hkFoundationService.retrieveAssets(machineCategory.getId(), loginDataBean.getCompanyId(), true);
            if (!CollectionUtils.isEmpty(assets)) {
                valueMap = new HashMap<>();
                for (HkAssetEntity assetEntity : assets) {
                    valueMap.put(assetEntity.getId(), assetEntity.getAssetName());
                }
            }
        }
        return valueMap;
    }

    public List<FeatureDetailDataBean> retrieveTreeViewFeatures() {
        List<UMFeature> allUMFeatures = userManagementServiceWrapper.retrieveAllMainFeature(loginDataBean.getCompanyId());
        Map<String, Long> sectionNameIdMap = hkFieldService.sectionNameIdMap();
        Map<String, List<String>> featureWithSectionDetails = hkFieldService.featureWithSectionDetails();
        List<FeatureDetailDataBean> featureDetailDataBeans = new ArrayList<>();
        List<String> skipList = HkSystemConstantUtil.CustomField.EXCLUDE_FEATURES_LIST;
        for (UMFeature uMFeature : allUMFeatures) {
            if (!skipList.contains(uMFeature.getName())) {
                FeatureDetailDataBean featureDetailDataBean = new FeatureDetailDataBean();
                featureDetailDataBean.setCompanyId(loginDataBean.getCompanyId());
                featureDetailDataBean.setId(uMFeature.getId());
                featureDetailDataBean.setDisplayName(uMFeature.getMenuLabel());
                featureDetailDataBean.setParentId(0L);
                featureDetailDataBean.setParentName(HkSystemConstantUtil.NONE);
                if (featureWithSectionDetails.containsKey(uMFeature.getName())) {
                    // It means it has section.Fill the child also
                    List<String> sectionList = featureWithSectionDetails.get(uMFeature.getName());
                    List<SectionDetailDataBean> sectionDetailList = new ArrayList<>();

                    if (!CollectionUtils.isEmpty(sectionList)) {
                        for (String section : sectionList) {
                            SectionDetailDataBean sectionDataBean = new SectionDetailDataBean();
                            if (sectionNameIdMap.containsKey(section)) {
                                Long sectionId = sectionNameIdMap.get(section);

                                sectionDataBean.setId(sectionId);
                                sectionDataBean.setDisplayName(convertToCamelCase(section));
                                sectionDataBean.setCompanyId(loginDataBean.getCompanyId());
                                sectionDataBean.setParentId(uMFeature.getId());
                                sectionDataBean.setParentName(uMFeature.getMenuLabel());
                                sectionDetailList.add(sectionDataBean);
                            } else {
                                sectionDataBean.setId(-1L);
                                sectionDataBean.setDisplayName(convertToCamelCase(HkSystemConstantUtil.Section.GENERAL_SECTION_DISPLAY));
                                sectionDataBean.setCompanyId(loginDataBean.getCompanyId());
                                sectionDataBean.setParentId(uMFeature.getId());
                                sectionDataBean.setParentName(uMFeature.getMenuLabel());
                                sectionDetailList.add(sectionDataBean);
                            }
                        }
                        featureDetailDataBean.setChildren(sectionDetailList);
                    }
                }
                featureDetailDataBeans.add(featureDetailDataBean);
            }
        }
        return featureDetailDataBeans;
    }

    private String convertToCamelCase(String str) {
        String[] strArray = str.split(" ");
        String finalString = "";
        for (String strArray1 : strArray) {
            String modString = strArray1.substring(0, 1).toUpperCase() + strArray1.substring(1).toLowerCase();
            finalString += modString + " ";
        }
        return finalString;
    }

    public List<CustomFieldDataBean> retrieveFeatureOrSectionCustomFields(Boolean isSection, Long sectionId, Long featureId, Long companyId) {
        if (!isSection) {
            sectionId = null;
        }
        Map<Long, String> featureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureMap(Boolean.FALSE);
        // sectionNameIdMap contains key as sectionName and value as sectionId
        Map<String, Long> sectionNameIdMap = hkFieldService.sectionNameIdMap();
        Map< Long, String> newSectionMap = new HashMap<>();
        // Reverse sectionNameIdMap so that we get sectionId as key
        for (Map.Entry<String, Long> entry : sectionNameIdMap.entrySet()) {
            newSectionMap.put(entry.getValue(), entry.getKey());
        }
        List<HkFieldEntity> customFields = hkFieldService.retrieveFeatureOrSectionRelatedCustomFields(isSection, sectionId, featureId, companyId);
        List<CustomFieldDataBean> customFieldsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customFields)) {
            for (HkFieldEntity fieldEntity : customFields) {
                CustomFieldDataBean customDataBean = new CustomFieldDataBean();
                customDataBean.setLabel(fieldEntity.getFieldLabel());
                customDataBean.setFieldType(fieldEntity.getComponentType());
                if (fieldEntity.getSeqNo() != null) {
                    customDataBean.setSeqNo(fieldEntity.getSeqNo());
                } else {
                    customDataBean.setSeqNo(1);
                }
                customDataBean.setId(fieldEntity.getId());
                if (featureIdWithNameMap.containsKey(fieldEntity.getFeature())) {
                    String featureName = featureIdWithNameMap.get(fieldEntity.getFeature());
                    customDataBean.setFeatureName(featureName);
                }
                if (fieldEntity.getSection() != null) {
                    if (newSectionMap.containsKey(fieldEntity.getSection().getId())) {
                        String sectionName = newSectionMap.get(fieldEntity.getSection().getId());
                        customDataBean.setSectionName(convertToCamelCase(sectionName));
                    }
                } else {
                    customDataBean.setSectionName(convertToCamelCase("General Section"));
                }
                customFieldsList.add(customDataBean);
            }
        }
        return customFieldsList;
    }

    public void updateSequenceNumber(Map<Long, String> seqNumWithId) {

        try {
            hkFieldService.updateSequenceNumber(seqNumWithId);
            createFeatureSectionMap(Boolean.TRUE, null);
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(CustomFieldTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Map<Long, List<String>> retrieveMapOfFieldIdWithConstraintsDependantOnField() {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, String> customFieldsWithConstraintsValue = hkFieldService.retrieveCustomFieldsWithConstraintsValue();
        Map<Long, List<String>> mapOfFieldIdWithConstraintsDependantOnField = null;
        if (customFieldsWithConstraintsValue != null && !customFieldsWithConstraintsValue.isEmpty()) {
            mapOfFieldIdWithConstraintsDependantOnField = new HashMap<>();

            for (Map.Entry<Long, String> customField : customFieldsWithConstraintsValue.entrySet()) {
                String dbFieldName = null;
                if (mapOfFieldIdAndHkFieldEntity != null && !mapOfFieldIdAndHkFieldEntity.isEmpty()) {
                    if (mapOfFieldIdAndHkFieldEntity.containsKey(customField.getKey())) {
                        dbFieldName = mapOfFieldIdAndHkFieldEntity.get(customField.getKey()).getDbFieldName();

                    }
                }
                Long key = customField.getKey();
                String constraintValue = customField.getValue();
                if (constraintValue != null) {

                    String[] split = constraintValue.split("@");

                    String fieldId = split[split.length - 1].replace("\"", "");
                    List<String> constraintsList = null;
                    if (fieldId != null) {
                        String reverseConstraintValue = null;
                        String replaceId = null;
                        if (mapOfFieldIdWithConstraintsDependantOnField.containsKey(Long.parseLong(fieldId))) {
                            constraintsList = mapOfFieldIdWithConstraintsDependantOnField.get(Long.parseLong(fieldId));

                        } else {
                            constraintsList = new ArrayList<>();

                        }
//                        if (constraintValue.contains("<")) {
//                            reverseConstraintValue = constraintValue.replace("<", ">");
//
//                        } else if (constraintValue.contains(">")) {
//                            reverseConstraintValue = constraintValue.replace(">", "<");
//
//                        } else {
//                            reverseConstraintValue = constraintValue;
//                        }
                        replaceId = constraintValue.replace("\"", "");

                        constraintsList.add(replaceId + "AS" + dbFieldName);
                        mapOfFieldIdWithConstraintsDependantOnField.put(Long.parseLong(fieldId), constraintsList);
                    }
                }
            }

        }
        return mapOfFieldIdWithConstraintsDependantOnField;
    }

    public List<UMCompany> searchCompaniesByName(String searchTerm) {
        return userManagementServiceWrapper.searchCompanyByName(searchTerm);
    }

    public List<FeatureDetailDataBean> retrieveSubentityTreeViewFeatures() {
        List<UMFeature> allUMFeatures = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);
        Map<Long, List<HkFieldEntity>> featureFieldDetails = hkFieldService.retrieveFeatureWiseCustomFieldList(null, Arrays.asList(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY));
        List<FeatureDetailDataBean> featureDetailDataBeans = new ArrayList<>();
        for (UMFeature uMFeature : allUMFeatures) {
            FeatureDetailDataBean featureDetailDataBean = new FeatureDetailDataBean();
            featureDetailDataBean.setCompanyId(loginDataBean.getCompanyId());
            featureDetailDataBean.setId(uMFeature.getId());
            featureDetailDataBean.setDisplayName(uMFeature.getMenuLabel());
            featureDetailDataBean.setParentId(0L);
            featureDetailDataBean.setParentName(HkSystemConstantUtil.NONE);
            if (featureFieldDetails.containsKey(uMFeature.getId())) {

// It means it has fields.Fill the child also
                List<HkFieldEntity> subEntityFields = featureFieldDetails.get(uMFeature.getId());
                List<SectionDetailDataBean> fieldDetailList = new ArrayList<>();

                if (!CollectionUtils.isEmpty(subEntityFields)) {
                    for (HkFieldEntity fieldEntity : subEntityFields) {
                        SectionDetailDataBean sectionDataBean = new SectionDetailDataBean();

                        sectionDataBean.setId(fieldEntity.getId());
                        sectionDataBean.setDisplayName(fieldEntity.getFieldLabel());
                        sectionDataBean.setCompanyId(loginDataBean.getCompanyId());
                        sectionDataBean.setParentId(uMFeature.getId());
                        sectionDataBean.setParentName(uMFeature.getMenuLabel());
                        fieldDetailList.add(sectionDataBean);
                    }
                    featureDetailDataBean.setChildren(fieldDetailList);
                }
            } else {
                // No fields.No children

            }
            featureDetailDataBeans.add(featureDetailDataBean);

        }
        return featureDetailDataBeans;
    }

    public List<SelectItem> retrieveCustomFields(Long dependentOnField) {
        Long companyId = loginDataBean.getCompanyId();
        List<String> componentTypes = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();
        Long feature = null;
        Set<Long> valueExceptionFieldIds = new HashSet<>();
        Set<Long> finalValueExceptionFieldIds = new HashSet<>();
        HkFieldEntity hkFieldEntity = hkFieldService.retrieveCustomFieldByFieldId(dependentOnField);
        if (hkFieldEntity != null) {
            feature = hkFieldEntity.getFeature();
        }
        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY);
        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN);
        componentTypes.add(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN);
        List<HkFieldEntity> fieldEntitys = hkFieldService.retrieveCustomFieldEntitysByComponentTypes(componentTypes, companyId, Arrays.asList(feature));
        Set<Long> fieldIds = new HashSet<>();
        fieldIds.add(dependentOnField);
        while (fieldIds.size() > 0) {
            valueExceptionFieldIds = this.checkCircularException(fieldIds, companyId);
            fieldIds = new HashSet<>();
            if (valueExceptionFieldIds.size() > 0) {
                fieldIds.addAll(valueExceptionFieldIds);
                finalValueExceptionFieldIds.addAll(valueExceptionFieldIds);
            }
        }
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
        List<HkSubEntityExceptionDocument> hkSubEntityExceptionDocuments = customFieldService.retrieveValueExceptionsByCriteria(fieldIds, companyId);
        if (!CollectionUtils.isEmpty(hkSubEntityExceptionDocuments)) {
            for (HkSubEntityExceptionDocument hkSubEntityExceptionDocument : hkSubEntityExceptionDocuments) {
                valueExceptionFieldIds.add(hkSubEntityExceptionDocument.getInstanceId());
            }
        }
        return valueExceptionFieldIds;
    }

    public List<SelectItem> retrieveCustomFieldsValueByKey(Long fieldId, String componentType) {
        List<SelectItem> valueList = new ArrayList<>();
        if (fieldId != null && !StringUtils.isEmpty(componentType)) {
            if (componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || componentType.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                HkMasterEntity hkMasterEntity = hkFoundationService.retrieveMaster(fieldId.toString(), (short) 0);
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

    public Map<ObjectId, String> mapOfSubEntityValueIdName(long hkFieldEntityId) {
        List<SelectItem> selectItemList = null;
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        Map<String, HkSubFormFieldEntity> SubEntityDropField = new HashMap<>();
        List<HkSubFormFieldEntity> listOfSubEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity);
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

    public HkSubEntityExceptionDocument convertValueExceptionDataBeanToDocument(HkSubEntityExceptionDocument hkSubEntityExceptionDocument, SubEntityValueExceptionDataBean subEntityValueExceptionDataBean) {
        if (!StringUtils.isEmpty(subEntityValueExceptionDataBean.getForValue())) {
            List<ObjectId> forValues = new ArrayList<>();
            List<String> forValuesStrng = Arrays.asList(subEntityValueExceptionDataBean.getForValue().split(","));
            if (!CollectionUtils.isEmpty(forValuesStrng)) {
                for (String forVal : forValuesStrng) {
                    forValues.add(new ObjectId(forVal));
                }
                hkSubEntityExceptionDocument.setForValues(forValues);
            }

        }
        if (!StringUtils.isEmpty(subEntityValueExceptionDataBean.getForUsers())) {
            List<String> forUsers = Arrays.asList(subEntityValueExceptionDataBean.getForUsers().split(","));
            if (!CollectionUtils.isEmpty(forUsers)) {
                hkSubEntityExceptionDocument.setForUsers(forUsers);
            }
        }
        if (!StringUtils.isEmpty(subEntityValueExceptionDataBean.getDependentOnField()) && !StringUtils.isEmpty(subEntityValueExceptionDataBean.getDependentOnFieldValues())) {
            String dependentOn = subEntityValueExceptionDataBean.getDependentOnField();
            String[] split = dependentOn.split("\\|");
            if (split.length > 0) {
                hkSubEntityExceptionDocument.setFieldId(new Long(split[0]));
                hkSubEntityExceptionDocument.setFieldType(split[1]);
                List<String> dependentOnFieldValues = Arrays.asList(subEntityValueExceptionDataBean.getDependentOnFieldValues().split(","));
                if (split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                    List<ObjectId> subEntityValues = new ArrayList<>();
                    for (String dependentOnFieldValue : dependentOnFieldValues) {
                        subEntityValues.add(new ObjectId(dependentOnFieldValue));
                    }
                    hkSubEntityExceptionDocument.setDependsOnSubValueList(subEntityValues);
                } else if (split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || split[1].equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                    List<Long> dropdownValues = new ArrayList<>();
                    for (String dependentOnFieldValue : dependentOnFieldValues) {
                        dropdownValues.add(new Long(dependentOnFieldValue));
                    }
                    hkSubEntityExceptionDocument.setDependsOnValueList(dropdownValues);
                }
            }
        }
        Date currentDate = new Date();
        if (subEntityValueExceptionDataBean.getId() == null) {
            hkSubEntityExceptionDocument.setCreatedBy(loginDataBean.getId());
            hkSubEntityExceptionDocument.setCreatedOn(currentDate);
            hkSubEntityExceptionDocument.setIsArchive(Boolean.FALSE);
        } else {
            hkSubEntityExceptionDocument.setId(new ObjectId(subEntityValueExceptionDataBean.getId()));
            hkSubEntityExceptionDocument.setIsArchive(subEntityValueExceptionDataBean.getIsArchive());
        }
        hkSubEntityExceptionDocument.setInstanceId(subEntityValueExceptionDataBean.getInstanceId());
        hkSubEntityExceptionDocument.setLastModifiedOn(currentDate);
        hkSubEntityExceptionDocument.setLastModifiedBy(loginDataBean.getId());
        hkSubEntityExceptionDocument.setFranchise(loginDataBean.getCompanyId());
        return hkSubEntityExceptionDocument;
    }

    public SubEntityValueExceptionDataBean convertValueExceptionDocumentToDataBean(HkSubEntityExceptionDocument hkSubEntityExceptionDocument, SubEntityValueExceptionDataBean subEntityValueExceptionDataBean) {
        subEntityValueExceptionDataBean.setInstanceId(hkSubEntityExceptionDocument.getInstanceId());
        subEntityValueExceptionDataBean.setId(hkSubEntityExceptionDocument.getId().toString());
        Map<String, String> userCodeName = userManagementServiceWrapper.retrieveRecipientNames(hkSubEntityExceptionDocument.getForUsers());
        if (!CollectionUtils.isEmpty(hkSubEntityExceptionDocument.getForUsers())) {
            String forUserStrng = "";
            String userToBeDisplay = "";
            for (String forUser : hkSubEntityExceptionDocument.getForUsers()) {
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
            subEntityValueExceptionDataBean.setForUsers(forUserStrng);
            subEntityValueExceptionDataBean.setUserToBeDisplay(userToBeDisplay);
        }
        if (!CollectionUtils.isEmpty(hkSubEntityExceptionDocument.getForValues())) {
            Map<ObjectId, String> map = this.mapOfSubEntityValueIdName(hkSubEntityExceptionDocument.getInstanceId());
            String forValueStrng = "";
            String valueToBeDisplay = "";
            for (ObjectId forValue : hkSubEntityExceptionDocument.getForValues()) {
                if (!StringUtils.isEmpty(forValueStrng)) {
                    forValueStrng = forValueStrng + "," + forValue;
                } else {
                    forValueStrng = forValueStrng + forValue;
                }
                if (!StringUtils.isEmpty(valueToBeDisplay)) {
                    if (forValue.equals(0l)) {
                        valueToBeDisplay = valueToBeDisplay + "," + "All";
                    } else {
                        valueToBeDisplay = valueToBeDisplay + "," + map.get(forValue);
                    }
                } else {
                    if (forValue.equals(0l)) {
                        valueToBeDisplay = valueToBeDisplay + "All";
                    } else {
                        valueToBeDisplay = valueToBeDisplay + map.get(forValue);
                    }

                }
            }
            subEntityValueExceptionDataBean.setForValue(forValueStrng);
            subEntityValueExceptionDataBean.setValueToBeDisplay(valueToBeDisplay);
        }
        if (hkSubEntityExceptionDocument.getFieldId() != null && hkSubEntityExceptionDocument.getFieldType() != null) {
            HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(hkSubEntityExceptionDocument.getFieldId());
            subEntityValueExceptionDataBean.setFieldId(hkSubEntityExceptionDocument.getFieldId());
            subEntityValueExceptionDataBean.setFieldType(hkSubEntityExceptionDocument.getFieldType());
            subEntityValueExceptionDataBean.setDependentOnToBeDisplay(fieldEntity.getFieldLabel());
            subEntityValueExceptionDataBean.setDependentOnField(hkSubEntityExceptionDocument.getFieldId() + "|" + hkSubEntityExceptionDocument.getFieldType());
            String dependentOnFieldValues = "";
            String dependentOnFieldValuesToBeDisplay = "";
            if (hkSubEntityExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY) && !CollectionUtils.isEmpty(hkSubEntityExceptionDocument.getDependsOnSubValueList())) {
                Map<ObjectId, String> subValueIdMap = this.mapOfSubEntityValueIdName(hkSubEntityExceptionDocument.getFieldId());
                for (ObjectId dependsOnSubValueList : hkSubEntityExceptionDocument.getDependsOnSubValueList()) {
                    if (!StringUtils.isEmpty(dependentOnFieldValues)) {
                        dependentOnFieldValues = dependentOnFieldValues + "," + dependsOnSubValueList.toString();
                    } else {
                        dependentOnFieldValues = dependentOnFieldValues + dependsOnSubValueList.toString();
                    }
                    if (!CollectionUtils.isEmpty(subValueIdMap) && subValueIdMap.containsKey(dependsOnSubValueList) && subEntityValueExceptionDataBean.getDependentOnToBeDisplay() != null) {
                        if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + subValueIdMap.get(dependsOnSubValueList);
                        } else {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + subValueIdMap.get(dependsOnSubValueList);
                        }
                    } else {
                        if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + "N/A";
                        } else {
                            dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + "N/A";
                        }
                    }

                }
                subEntityValueExceptionDataBean.setDependentOnFieldValuesToBeDisplay(dependentOnFieldValuesToBeDisplay);
            } else if (hkSubEntityExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || hkSubEntityExceptionDocument.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) && !CollectionUtils.isEmpty(hkSubEntityExceptionDocument.getDependsOnValueList())) {
                List<HkValueEntity> hkValueEntitys = hkFoundationService.retrieveValueEntityListByValueIds(hkSubEntityExceptionDocument.getDependsOnValueList());
                for (Long dependsOnValueList : hkSubEntityExceptionDocument.getDependsOnValueList()) {
                    for (HkValueEntity hkValueEntity : hkValueEntitys) {
                        if (hkValueEntity.getId().equals(dependsOnValueList)) {
                            if (!StringUtils.isEmpty(dependentOnFieldValues)) {
                                dependentOnFieldValues = dependentOnFieldValues + "," + dependsOnValueList.toString();
                            } else {
                                dependentOnFieldValues = dependentOnFieldValues + dependsOnValueList.toString();
                            }
                            if (!StringUtils.isEmpty(dependentOnFieldValuesToBeDisplay)) {
                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + "," + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + hkValueEntity.getValueName();
                            } else {
                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + subEntityValueExceptionDataBean.getDependentOnToBeDisplay() + "-" + hkValueEntity.getValueName();
                            }
                        }
                    }
                }
                subEntityValueExceptionDataBean.setDependentOnFieldValuesToBeDisplay(dependentOnFieldValuesToBeDisplay);
            }
            subEntityValueExceptionDataBean.setDependentOnFieldValues(dependentOnFieldValues);
        }
        return subEntityValueExceptionDataBean;
    }

    public void saveException(List<SubEntityValueExceptionDataBean> subEntityValueExceptionDataBeans) {
        if (!CollectionUtils.isEmpty(subEntityValueExceptionDataBeans)) {
            List<HkSubEntityExceptionDocument> hkSubEntityExceptionDocuments = new ArrayList<>();
            for (SubEntityValueExceptionDataBean subEntityValueExceptionDataBean : subEntityValueExceptionDataBeans) {
                HkSubEntityExceptionDocument hkSubEntityExceptionDocument = new HkSubEntityExceptionDocument();
                hkSubEntityExceptionDocument = this.convertValueExceptionDataBeanToDocument(hkSubEntityExceptionDocument, subEntityValueExceptionDataBean);
                hkSubEntityExceptionDocuments.add(hkSubEntityExceptionDocument);
            }
            customFieldService.saveValueExceptions(hkSubEntityExceptionDocuments);
        }
    }

    public List<SubEntityValueExceptionDataBean> retrieveValueExceptions(Long instanceId) {
        Long companyId = loginDataBean.getCompanyId();
        List<SubEntityValueExceptionDataBean> subEntityValueExceptionDataBeans = new ArrayList<>();
        if (instanceId != null) {
            List<HkSubEntityExceptionDocument> hkSubEntityExceptionDocuments = customFieldService.retrieveValueExceptions(instanceId, companyId);
            if (!CollectionUtils.isEmpty(hkSubEntityExceptionDocuments)) {
                for (HkSubEntityExceptionDocument hkSubEntityExceptionDocument : hkSubEntityExceptionDocuments) {
                    SubEntityValueExceptionDataBean subEntityValueExceptionDataBean = new SubEntityValueExceptionDataBean();
                    subEntityValueExceptionDataBean = this.convertValueExceptionDocumentToDataBean(hkSubEntityExceptionDocument, subEntityValueExceptionDataBean);
                    subEntityValueExceptionDataBeans.add(subEntityValueExceptionDataBean);
                }
            }
        }
        return subEntityValueExceptionDataBeans;
    }
// Reverse scenario.This method brings field and all the other field's exceptions which are depending oon it

    public Map<Long, List<SelectItem>> retrieveAllExceptionsDependantOnField() {
        Map<Long, HkValueEntity> valueEntititiesByCriteria = hkFoundationService.retrieveMapOfIdAndValueEntity(null);
//        Map<Long, HkMasterValueDocument> valueEntititiesByCriteria = hkFoundationDocumentService.retrieveValueEntititiesByCriteria(null, null);
        List<HkValueExceptionDocument> allValueExceptions = hkFoundationDocumentService.retrieveValueExceptions(null, null);
        Map<Long, HkFieldEntity> mapOfFieldidAndDbFieldName = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, List<SelectItem>> mapofFieldIdWithExceptionsDependantOn = null;

        if (!CollectionUtils.isEmpty(allValueExceptions)) {
            mapofFieldIdWithExceptionsDependantOn = new HashMap<>();
            for (HkValueExceptionDocument allValueException : allValueExceptions) {
                Long fieldId = allValueException.getFieldId();
                if (fieldId != null) {
                    SelectItem selectItem = new SelectItem(allValueException.getInstanceId(), allValueException.getFieldType());
                    selectItem.setDependantOnException(allValueException.getDependsOnValueList());
                    if (allValueException.getForUsers() != null) {
                        selectItem.setForUsers(allValueException.getForUsers());
                    }

                    if (mapOfFieldidAndDbFieldName != null && !mapOfFieldidAndDbFieldName.isEmpty() && mapOfFieldidAndDbFieldName.containsKey(allValueException.getInstanceId())) {
                        selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(allValueException.getInstanceId()).getDbFieldName());

                    }
                    Map<Object, Object> valueMaps = new HashMap<>();
                    if (!CollectionUtils.isEmpty(allValueException.getForValues())) {
                        for (Long valueId : allValueException.getForValues()) {
                            if (valueEntititiesByCriteria.containsKey(valueId)) {
                                valueMaps.put(valueId, valueEntititiesByCriteria.get(valueId).getValueName());
                            }
                        }
                        selectItem.setCustom1(valueMaps);
                    }

                    selectItem.setForValuesException(allValueException.getForValues());
                    List<SelectItem> totalList = null;
                    if (mapofFieldIdWithExceptionsDependantOn.containsKey(fieldId)) {

                        totalList = mapofFieldIdWithExceptionsDependantOn.get(fieldId);

                    } else {
                        totalList = new ArrayList<>();

                    }
                    totalList.add(selectItem);

                    mapofFieldIdWithExceptionsDependantOn.put(allValueException.getFieldId(), totalList);
                }
            }

        }
        return mapofFieldIdWithExceptionsDependantOn;
    }
// Method made by Shifa Salheen for ajax call which fetch all the exception dependant on field id

    public List<SelectItem> retrieveExceptionsDependantOnField(Long fieldId, Long valueId) {
        Map<Long, HkFieldEntity> mapOfFieldidAndDbFieldName = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, HkValueEntity> valueEntititiesByCriteria = hkFoundationService.retrieveMapOfIdAndValueEntity(null);
        Map<String, List<HkValueEntity>> mapOfKeyIdWithValueEntities = hkFoundationService.mapOfKeyIdWithValueEntities(loginDataBean.getCompanyId());
        List<SelectItem> selectItems = new ArrayList<>();
        if (valueId != null) {
            List<HkValueExceptionDocument> valueExceptionsByCriteria = hkFoundationDocumentService.retrieveValueExceptionsByCriteriaWithValue(fieldId, loginDataBean.getCompanyId(), valueId);
            if (!CollectionUtils.isEmpty(valueExceptionsByCriteria)) {
                for (HkValueExceptionDocument allValueException : valueExceptionsByCriteria) {
                    SelectItem selectItem = new SelectItem(allValueException.getInstanceId(), allValueException.getFieldType());
                    if (mapOfFieldidAndDbFieldName != null && !mapOfFieldidAndDbFieldName.isEmpty() && mapOfFieldidAndDbFieldName.containsKey(allValueException.getInstanceId())) {
                        selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(allValueException.getInstanceId()).getDbFieldName());

                    }
                    if (allValueException.getForUsers() != null) {
                        selectItem.setForUsers(allValueException.getForUsers());
                    }
                    // To set it has exception
                    selectItem.setIsActive(Boolean.TRUE);
                    Map<Object, Object> valueMaps = new HashMap<>();
                    if (!CollectionUtils.isEmpty(allValueException.getForValues())) {

                        if (allValueException.getForValues().contains(0l)) {
                            if (mapOfKeyIdWithValueEntities.containsKey(allValueException.getInstanceId().toString()));
                            {
                                List<HkValueEntity> valeEntitiesForAll = mapOfKeyIdWithValueEntities.get(allValueException.getInstanceId().toString());
                                if (!CollectionUtils.isEmpty(valeEntitiesForAll)) {
                                    for (HkValueEntity valeEntityAll : valeEntitiesForAll) {
                                        valueMaps.put(valeEntityAll.getId(), valeEntityAll.getValueName());
                                    }
                                }
                            }
                        } else {
                            for (Long valueIds : allValueException.getForValues()) {

                                if (valueEntititiesByCriteria.containsKey(valueIds)) {
                                    valueMaps.put(valueIds, valueEntititiesByCriteria.get(valueIds).getValueName());
                                }
                            }
                        }
                        selectItem.setCustom1(valueMaps);
                    }
                    selectItem.setForValuesException(allValueException.getForValues());
                    selectItems.add(selectItem);
                }
            } else {
                // This has to be executed when ,if I select a field,then its value and there is no exception on that value
                List<HkValueExceptionDocument> valueExceptionsForField = hkFoundationDocumentService.retrieveValueExceptionsByCriteriaWithValue(fieldId, loginDataBean.getCompanyId(), null);
                if (!CollectionUtils.isEmpty(valueExceptionsForField)) {
                    // All will contain same value,thats why we are not iterating and fetching from first documnet only
                    HkValueExceptionDocument firstDocumnet = valueExceptionsForField.get(0);
                    SelectItem selectItem = new SelectItem(firstDocumnet.getInstanceId(), firstDocumnet.getFieldType());
                    selectItem.setIsActive(Boolean.FALSE);
                    selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(firstDocumnet.getInstanceId()).getDbFieldName());
                    selectItems.add(selectItem);
                }
            }
        } else {
            // Call this when it has no value selected,So clear all those which were populated wit this
            List<HkValueExceptionDocument> valueExceptionsForField = hkFoundationDocumentService.retrieveValueExceptionsByCriteriaWithValue(fieldId, loginDataBean.getCompanyId(), null);
            // All will contain same value,thats why we are not iterating and fetching from first documnet only
            if (!CollectionUtils.isEmpty(valueExceptionsForField)) {
                HkValueExceptionDocument firstDocumnet = valueExceptionsForField.get(0);
                SelectItem selectItem = new SelectItem(firstDocumnet.getInstanceId(), firstDocumnet.getFieldType());
                selectItem.setIsActive(Boolean.FALSE);
                selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(firstDocumnet.getInstanceId()).getDbFieldName());
                selectItems.add(selectItem);
            }

        }
        return selectItems;
    }

    // Forward scenario.This method brings all exceptions made on field
    public Map<Long, Boolean> checkFieldHasExceptionOrNot() {
        List<HkValueExceptionDocument> allValueExceptions = hkFoundationDocumentService.retrieveValueExceptions(null, null);
        Map<Long, Boolean> mapOfFieldWithException = null;
        if (!CollectionUtils.isEmpty(allValueExceptions)) {
            mapOfFieldWithException = new HashMap<>();

            for (HkValueExceptionDocument allValueException : allValueExceptions) {
                mapOfFieldWithException.put(allValueException.getInstanceId(), Boolean.TRUE);
            }

        }
        return mapOfFieldWithException;
    }

    public Boolean checkUniqueness(String fieldModel, String featureName, Object value) {
        return customFieldService.checkUniqueness(fieldModel, featureName, value, loginDataBean.getCompanyId());
    }

    public Map<String, Object> retrievePrerequisiteForException(Long instanceId) {
        Map<String, Object> prerequisiteMap = new HashMap<>();
        if (instanceId != null) {
            List<SelectItem> customFields = this.retrieveCustomFields(instanceId);
            if (!CollectionUtils.isEmpty(customFields)) {
                prerequisiteMap.put("dependentFieldList", customFields);
            }
            List<SubEntityValueExceptionDataBean> subEntityValueExceptionDataBeans = this.retrieveValueExceptions(instanceId);
            if (!CollectionUtils.isEmpty(subEntityValueExceptionDataBeans)) {
                prerequisiteMap.put("subEntityValueExceptionDataBeans", subEntityValueExceptionDataBeans);
            }
        }
        return prerequisiteMap;
    }

    /*
     * Added by Shifa on 24 August 2015 This method is used to retrieve fields by designation as per changes 
     */
    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFieldsFromConfiguration(String featureName) {
        Map<String, List<DependentFieldDataBean>> fieldMap = null;

        List<UMFeature> features = userManagementServiceWrapper.retrieveFeatureByName(Arrays.asList(featureName));
        if (!CollectionUtils.isEmpty(features)) {
            UMFeature feature = features.get(0);
            List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = hkConfigurationService.retrieveListOfPermissionDocuments(loginDataBean.getRoleIds(), feature.getId(), Boolean.FALSE, loginDataBean.getCompanyId(), null);

            if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
                fieldMap = new HashMap<>();
                List<DependentFieldDataBean> dependentFieldDataBeans;
                for (HkFeatureFieldPermissionDocument ffPermissionEntity : featureFieldPermissionDocuments) {

                    if (ffPermissionEntity.getParentViewFlag()) {
                        DependentFieldDataBean dependentFieldDataBean = new DependentFieldDataBean();
                        dependentFieldDataBean.setFieldId(ffPermissionEntity.getFieldId());

                        dependentFieldDataBean.setEntityName(ffPermissionEntity.getEntityName());
                        dependentFieldDataBean.setSequenceNo(ffPermissionEntity.getSequenceNo());
                        dependentFieldDataBean.setIsEditable(false);
                        String entityname = ffPermissionEntity.getEntityName() + HkSystemConstantUtil.DIAMOND_PARENT_VIEW_FLAG;
                        if (!fieldMap.containsKey(entityname)) {
                            dependentFieldDataBeans = new ArrayList<>();
                        } else {
                            dependentFieldDataBeans = fieldMap.get(entityname);
                        }
                        dependentFieldDataBeans.add(dependentFieldDataBean);
                        fieldMap.put(entityname, dependentFieldDataBeans);
                    }
                    if (ffPermissionEntity.getReadonlyFlag() || ffPermissionEntity.getEditableFlag()) {
                        DependentFieldDataBean dependentFieldDataBean = new DependentFieldDataBean();
                        dependentFieldDataBean.setFieldId(ffPermissionEntity.getFieldId());

                        dependentFieldDataBean.setEntityName(ffPermissionEntity.getEntityName());
                        dependentFieldDataBean.setSequenceNo(ffPermissionEntity.getSequenceNo());
                        if (ffPermissionEntity.isIsRequired()) {
                            dependentFieldDataBean.setIsRequired(ffPermissionEntity.isIsRequired());
                        } else {
                            dependentFieldDataBean.setIsRequired(Boolean.FALSE);
                        }
                        if (ffPermissionEntity.getReadonlyFlag() && !ffPermissionEntity.getEditableFlag()) {
                            dependentFieldDataBean.setIsEditable(Boolean.FALSE);
                        } else {
                            dependentFieldDataBean.setIsEditable(Boolean.TRUE);
                        }
                        if (!fieldMap.containsKey(ffPermissionEntity.getEntityName())) {
                            dependentFieldDataBeans = new ArrayList<>();
                        } else {
                            dependentFieldDataBeans = fieldMap.get(ffPermissionEntity.getEntityName());
                        }
                        dependentFieldDataBeans.add(dependentFieldDataBean);
                        fieldMap.put(ffPermissionEntity.getEntityName(), dependentFieldDataBeans);
                    }
                }
            }
        }
        return fieldMap;
    }
// Method added by SHifa for uniquenesss feature

    public Map<String, String> retrieveFieldsByFeatureForUniqueness(Long featureId) {
        Map<String, String> fieldList = null;
        List<Long> featureIds = new ArrayList<>();
        featureIds.add(featureId);
        Map<Long, String> featureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureIdWithNameMap();
        List<HkFieldEntity> hkFieldEntity = hkFieldService.retrieveFieldsByFeatures(featureIds, null, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(hkFieldEntity)) {
            fieldList = new HashMap<>();
            for (HkFieldEntity fieldEntity : hkFieldEntity) {
                if (featureIdWithNameMap.containsKey(fieldEntity.getFeature())) {
                    String featureName = featureIdWithNameMap.get(fieldEntity.getFeature());
                    fieldList.put(fieldEntity.getDbFieldName(), featureName + "." + fieldEntity.getFieldLabel());
                }
            }
            // Add static list to
            fieldList.put("invoiceId", "Invoice.Id");
            fieldList.put("parcelId", "Parcel.Id");
            fieldList.put("lotId", "Lot.Id");
            fieldList.put("packetId", "Packet.Id");
        }
        return fieldList;
    }

    public Map<String, String> retrieveNumberEntitiesForDate(String search, Long feature) {
        Map<String, String> customFieldMap = null;
        List<HkFieldEntity> entityList = hkFieldService.retrieveCustomFieldByFeatureAndComponentTypes(loginDataBean.getCompanyId(), feature, Arrays.asList(HkSystemConstantUtil.CustomField.ComponentType.NUMBER), search);
        if (!CollectionUtils.isEmpty(entityList)) {
            customFieldMap = new HashMap<>();
            for (HkFieldEntity field : entityList) {
                if (field.getIsCustomField()) {
                    customFieldMap.put(field.getDbFieldName(), field.getFieldLabel());
                }
            }
        }
        return customFieldMap;
    }

    public Map<String, String> retrieveDateEntities(String search, Long feature) {
        Map<String, String> customFieldMap = null;
        List<HkFieldEntity> entityList = hkFieldService.retrieveCustomFieldByFeatureAndComponentTypes(loginDataBean.getCompanyId(), feature, Arrays.asList(HkSystemConstantUtil.CustomField.ComponentType.DATE), search);
        if (!CollectionUtils.isEmpty(entityList)) {
            customFieldMap = new HashMap<>();
            customFieldMap.put("Today", "Today");
            for (HkFieldEntity field : entityList) {
                if (field.getIsCustomField()) {
                    customFieldMap.put(field.getDbFieldName(), field.getFieldLabel());
                }
            }
        }
        return customFieldMap;
    }

    private Map<String, String> mapOfFieldsWithSkipHolidays(List<HkFieldEntity> allDateFields) {
        Map<String, String> mapOfFieldsWithHoliday = null;
        if (!CollectionUtils.isEmpty(allDateFields)) {
            mapOfFieldsWithHoliday = new HashMap<>();
            String skipHoliday = "false";
            for (HkFieldEntity allField : allDateFields) {
                String[] validationsArr = allField.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                for (String validationValue : validationsArr) {
                    if (validationValue.contains("\"skipHolidays\":")) {
                        String skipArray[] = validationValue.split(":");
                        skipHoliday = skipArray[1];
                        mapOfFieldsWithHoliday.put(allField.getDbFieldName(), skipHoliday);
                    }
                }

            }

        }
        return mapOfFieldsWithHoliday;
    }

    private Map<String, Map<String, String>> mapOfFieldsForDefaultDate() {
        List<HkFieldEntity> allFields = hkFieldService.retrieveFieldByFeatureAndComponentType(null, null, HkSystemConstantUtil.CustomField.ComponentType.DATE, Boolean.FALSE);
        Map<String, String> mapOfFieldsWithSkipHolidays = this.mapOfFieldsWithSkipHolidays(allFields);
        Map<String, Map<String, String>> outerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(allFields)) {
            Map<String, String> innerMap = null;

            for (HkFieldEntity allField : allFields) {
                String[] validationsArr = allField.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                String[] defArray = null;
                String key = null;
                for (String validationValue : validationsArr) {

                    if (validationValue.contains("\"defaultDate\":")) {
                        defArray = validationValue.split(":");
                        String[] seperatorArr = defArray[1].split("#@");
                        String dep_field = seperatorArr[seperatorArr.length - 1].replace("\"", "");
                        if (!dep_field.equals("Today")) {
                            try {
                                Integer.parseInt(dep_field);
                            } catch (NumberFormatException e) {
                                // This logic is for dbfields e.g. today + carat
                                key = dep_field;
                                if (outerMap.containsKey(key)) {

                                    innerMap = outerMap.get(key);
                                } else {
                                    innerMap = new HashMap<>();

                                }
                                if (mapOfFieldsWithSkipHolidays != null && !mapOfFieldsWithSkipHolidays.isEmpty() && mapOfFieldsWithSkipHolidays.containsKey(allField.getDbFieldName())) {
                                    innerMap.put(allField.getDbFieldName() + "!@" + mapOfFieldsWithSkipHolidays.get(allField.getDbFieldName()), defArray[1].toString());
                                    outerMap.put(key, innerMap);
                                }
                            }

                        }
                    }
                }

            }
        }
        return outerMap;
    }
}
