/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFeatureService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSubFormFieldDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.nosql.model.SectionDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncCenterUserService;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.customfield.databeans.DependentFieldDataBean;
import com.argusoft.hkg.web.util.HkSelect2DataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkReferenceRateDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author akta
 */
@Service
public class CenterCustomFieldTransformer {

    @Autowired
    HkFeatureService featureService;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    private HkUMSyncService hkUMSyncService;

    @Autowired
    private HkUserService hkUserService;
    @Autowired
    private HkStockService stockService;

    @Autowired
    private SyncCenterUserService syncCenterUserService;
    @Autowired
    private HkFoundationDocumentService hkFoundationDocumentService;
    @Autowired
    private HkStockService hkStockService;

    @Autowired
    HkConfigurationService hkConfigurationService;
//    @Autowired
//    private UserManagementServiceWrapper userManagementServiceWrapper;

    public List<HkSelect2DataBean> search(String searchValue) throws GenericDatabaseException {
        List<HkSelect2DataBean> hkSelect2DataBeans = new ArrayList<>();
//        List<SyncCenterFeatureDocument> centerFeatures = featureService.retrieveAllFeatures(true);
//        Map<Long, String> featureIdNameMap = new HashMap<>();
//        if (!CollectionUtils.isEmpty(centerFeatures)) {
//            for (SyncCenterFeatureDocument centerFeature : centerFeatures) {
//                featureIdNameMap.put(centerFeature.getId(), centerFeature.getFeatureName());
//                if (Pattern.compile(Pattern.quote(searchValue), Pattern.CASE_INSENSITIVE).matcher(centerFeature.getFeatureName()).find()) {
//                    hkSelect2DataBeans.add(new HkSelect2DataBean(centerFeature.getId() + "", centerFeature.getMenuLabel()));
//                    }
//            }
//        }
//
//        Map<Long, List<String>> searchFields = hkFieldService.searchFields(searchValue, featureIdNameMap);
//        if (!CollectionUtils.isEmpty(searchFields)) {
//            for (Map.Entry<Long, List<String>> entry : searchFields.entrySet()) {
//                Long featureAndSectionId = entry.getKey();
//                for (String value : entry.getValue()) {
//                    HkSelect2DataBean hkSelect2DataBean = new HkSelect2DataBean(featureAndSectionId, value);
//                    hkSelect2DataBeans.add(hkSelect2DataBean);
//                }
//            }
//        }
        return hkSelect2DataBeans;
    }

    public Map<String, List<SelectItem>> retrieveCustomFieldBySearch(String featureName) throws GenericDatabaseException {
        Map<String, List<DependentFieldDataBean>> fieldMap = null;
        SyncCenterFeatureDocument feature = featureService.retireveFeatureByName(featureName);
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveSectionsByFeatureId = hkConfigurationService.retrieveFeatureFieldPermissionForSearchFromConfigDocument(loginDataBean.getDepartment(), feature.getId(), loginDataBean.getRoleIds(), loginDataBean.getCompanyId());
        Map<String, List<SelectItem>> searchDetail = null;
        if (retrieveSectionsByFeatureId != null && !retrieveSectionsByFeatureId.isEmpty()) {
            searchDetail = new HashMap<>();
            for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> sectionTemplate : retrieveSectionsByFeatureId.entrySet()) {
                List<HkFeatureFieldPermissionDocument> listOfFields = sectionTemplate.getValue();

                if (!CollectionUtils.isEmpty(listOfFields)) {
                    for (HkFeatureFieldPermissionDocument field : listOfFields) {
                        String entityName = field.getEntityName();
                        SelectItem selectitem = new SelectItem(field.getHkFieldEntity().getId(), field.getHkFieldEntity().getDbFieldName());
                        String sectionName;
                        if (field.getHkFieldEntity().getSection() != null) {
                            sectionName = field.getHkFieldEntity().getSection().getSectionName();
                        } else {
                            sectionName = "genralSection";
                        }
                        selectitem.setDescription(sectionName);
                        List<SelectItem> searchDetailList = null;
                        if (searchDetail.containsKey(entityName)) {
                            searchDetailList = searchDetail.get(entityName);
                            searchDetailList.add(selectitem);
                        } else {
                            searchDetailList = new ArrayList<>();
                            searchDetailList.add(selectitem);
                        }
                        searchDetail.put(entityName, searchDetailList);
                    }
                }

            }
        }
        return searchDetail;
    }

    /**
     * @Author akta
     * @param featureName name of feature
     * @param designationIds list of designation id
     * @return List of HkFieldEntity
     */
    public Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch(String featureName, List<Long> designationIds) {
        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        SyncCenterFeatureDocument featureDocument = featureService.retireveFeatureByName(featureName);
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> fieldPermissionEntitys = new HashMap<>();
        if (featureDocument.getId() != null) {
            fieldPermissionEntitys = hkConfigurationService.retrieveFeatureFieldPermissionForSearchFromConfigDocument(loginDataBean.getDepartment(), featureDocument.getId(), loginDataBean.getRoleIds(), loginDataBean.getCompanyId());

            if (!CollectionUtils.isEmpty(fieldPermissionEntitys)) {
                fieldEntityList = new LinkedList<>();
                for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : fieldPermissionEntitys.entrySet()) {
                    HkSectionDocument hkSectionDocument = entry.getKey();
                    List<HkFeatureFieldPermissionDocument> list = entry.getValue();
                    if (!CollectionUtils.isEmpty(list)) {
                        for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionDoc : list) {
                            if (hkFeatureFieldPermissionDoc.getHkFieldEntity() != null) {
                                fieldEntityList.add(hkFeatureFieldPermissionDoc.getHkFieldEntity());
                            }
                        }
                    }
                }

            }
        }

        return fieldPermissionEntitys;
    }

    public List<SelectItem> getSelectItemListFromUserList(List<SyncCenterUserDocument> umUsers) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUsers)) {
            for (SyncCenterUserDocument uMUser : umUsers) {
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

    //Method to be called when data could not be found in local storage
    public Map<String, Object> retrieveCustomFieldTemplateBySearch(String featureName) throws GenericDatabaseException {

        Map<String, String> mapOfCurrencyCodeWithCurrencySymbol = this.mapOfCurrencyCodeWithCurrencySymbol();
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveSectionsByFeatureId = retrieveFieldsConfigureForSearch(featureName, loginDataBean.getRoleIds());
        Map<Long, String> featureIdWithNameMap = retrieveFeatureIdWithNameMap();
        if (!CollectionUtils.isEmpty(retrieveSectionsByFeatureId)) {
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : retrieveSectionsByFeatureId.entrySet()) {
                HkSectionDocument hkSectionDocument = entry.getKey();
                List<HkFeatureFieldPermissionDocument> hkFieldEntitys = entry.getValue();
                if (!CollectionUtils.isEmpty(hkFieldEntitys)) {
                    List<Map<String, Object>> customFieldTemplateDataBeans = new ArrayList<>();
                    for (HkFeatureFieldPermissionDocument featureFieldPermDoc : hkFieldEntitys) {
                        if (featureFieldPermDoc.getHkFieldEntity().isIsCustomField()) {
                            Type type = new TypeToken<Map<String, Object>>() {
                            }.getType();
                            Map customFieldPatternMap = (new Gson()).fromJson(featureFieldPermDoc.getHkFieldEntity().getValidationPattern(), type);
                            Map<String, Object> customFieldTemplateDataBean = new HashMap<>();
                            if (featureFieldPermDoc.getHkFieldEntity().getFieldLabel() != null) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.LABEL, featureFieldPermDoc.getHkFieldEntity().getFieldLabel());
                            }
                            if (featureFieldPermDoc.getHkFieldEntity().getDbFieldName() != null) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MODEL, featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                String[] dbFieldNameArr = featureFieldPermDoc.getHkFieldEntity().getDbFieldName().split("\\$");
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MODEL_WITHOUT_SEPERATOR, dbFieldNameArr[0]);
                            }
                            if (featureFieldPermDoc.getHkFieldEntity().getDbBaseType() != null) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, featureFieldPermDoc.getHkFieldEntity().getFieldType());
                            }
                            long feature = featureFieldPermDoc.getHkFieldEntity().getFeature();

                            if (featureIdWithNameMap.containsKey(featureFieldPermDoc.getFeature())) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FEATURE, featureIdWithNameMap.get(featureFieldPermDoc.getHkFieldEntity().getFeature()));
                            }
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE) != null) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.REQUIRED, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE));
                            }
                            Map<String, Object> attributeMap = new HashMap<>();
                            switch (featureFieldPermDoc.getHkFieldEntity().getComponentType()) {
                                case HkSystemConstantUtil.CustomField.ComponentType.POINTER:
                                    // Pointer is for copying the behaviour of the custom field to which it is pointing
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER) != null) {
                                        HkFieldDocument pointerFieldEntity = (HkFieldDocument) hkUMSyncService.getDocumentById(Long.parseLong(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.POINTER).toString()), HkFieldDocument.class);
                                        Type type1 = new TypeToken<Map<String, Object>>() {
                                        }.getType();
                                        // Fetching the behaviour of the customfield to which our custom field is pointing
                                        Map pointerCustomFieldPatternMap = (new Gson()).fromJson(pointerFieldEntity.getValidationPattern(), type1);
                                        // copying attributes which are common to all custom field types
                                        if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                        }
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                        }
                                        // copying common attributes for number,percent,angle and currency
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.NUMBER) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.ANGLE) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PERCENT) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                                                Integer beforeDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                String validatePattern = "";
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
                                                validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                                Integer afterDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                                                String validatePattern = "";
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                    validatePattern += "/^-?";
                                                } else {
                                                    validatePattern += "/^";
                                                }
                                                validatePattern += "[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                                Integer beforeDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                                Integer afterDecimal = (Integer.parseInt((String) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                                String validatePattern = null;
                                                if (afterDecimal.intValue() == 0) {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                                    } else {
                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                                    }
                                                } else {
                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                        validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                    } else {

                                                        validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                                    }
                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                            }
                                            //copying attributes specific to number

                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.NUMBER)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.INTEGER);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER) != null) {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, !(Boolean) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER));
                                                } else {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                                }
                                            }
                                            // copying attributes specific to angle
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.ANGLE)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "Angle");
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                                } else {
                                                    attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, "0");
                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                                    } else {
                                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, "360");
                                                    }
                                                }
                                            }
                                            // copying attributes specific to percent
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PERCENT)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            }
                                            // copying  attributes specific to currency
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "currency");
//                                                String currencyId = pointerFieldEntity.getAssociatedCurrency();
//                                                HkCurrencyEntity currencyEntity = currencyMasterMap.get(Long.parseLong(currencyId));
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_TYPE, currencyEntity.getSymbol());
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_FORMAT, currencyEntity.getFormat());
//                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_PREFIX, currencyEntity.getSymbolPosition());
                                                if (mapOfCurrencyCodeWithCurrencySymbol != null && !mapOfCurrencyCodeWithCurrencySymbol.isEmpty()) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE_SYMBOL_MAP, mapOfCurrencyCodeWithCurrencySymbol);
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_FORMAT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_SYMBOL_POSITION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION));
                                                }
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                            }

                                        }
                                        // copying common attributes for text field,textarea,phone and email
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PHONE) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.EMAIL)) {
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                                                Double maxLength = Double.parseDouble(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null) {

                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                                            }
                                            // copying  attributes specific to Text Field
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MASKED, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null) {
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
                                                                validatePattern += "!@#$%^&*()_ ";
                                                                break;
                                                        }
                                                    }
                                                    validatePattern += "]*$/";
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                                }
                                            }
                                            // copying  attributes specific to Text Area
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "textarea");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);

                                            }
                                            // copying  attributes specific to Phone
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.PHONE)) {
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
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null) {
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
                                                String validatePatternForEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                                                customFieldTemplateDataBean.put("validate", validatePatternForEmail);
                                            }
                                        }
                                        // copying common attributes for radio button,single select(dropdown),multiselect and usermultiselect
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {

                                            // copying  attributes specific to single select(dropdown)
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
//                                                if (pointerFieldEntity.getFieldValues() != null) {
//                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(pointerFieldEntity.getFieldValues()));
//
//                                                }
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "pointerselect");
                                                customFieldTemplateDataBean.put("pointerlabel", pointerFieldEntity.getFieldLabel());
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.LONG);

                                            }
                                            // copying  attributes specific to multiselect
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "pointer_multiSelect");
                                                customFieldTemplateDataBean.put("pointerlabel", pointerFieldEntity.getFieldLabel());
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
//                                                if (pointerFieldEntity.getFieldValues() != null) {
//                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(pointerFieldEntity.getFieldValues()));
//
//                                                }

                                            }
                                            // copying  attributes specific to usermultiselect
                                            if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "UserMultiSelect");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
//                                                if (pointerFieldEntity.getFieldValues() != null) {
//                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(pointerFieldEntity.getFieldValues()));
//
//                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_EMPLOYEE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DEPARTMENT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DESIGNATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION));
                                                }
                                                if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT) != null) {
                                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_MULTISELECT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT));
                                                }
                                            }

                                        }
                                        // copying  attributes specific to CheckBox
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.CHECKBOX)) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "checkbox");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT));
                                            }
                                        }
                                        // copying  attributes specific to Date
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DATE);

                                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                                            } else {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                        }
                                        // copying  attributes specific to Date range
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE)) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DATE);

                                            customFieldTemplateDataBean.put("fromModel", "from" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                            customFieldTemplateDataBean.put("toModel", "to" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
//                                        customFieldTemplateDataBean.remove(HkSystemConstantUtil.DynamicFormAttribute.MODEL);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                            }

                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetimerange");
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");
                                            } else {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                                attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "daterange");
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null) {
                                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                            }
                                        }
                                        // copying  attributes specific to Image Upload
                                        if (pointerFieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.IMAGE)) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.IMAGE);
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "imageUpload");
                                            customFieldTemplateDataBean.put("width", "100%");
                                            customFieldTemplateDataBean.put("height", "100%");
                                            Map<String, Object> flowInits = new HashMap<>();
                                            flowInits.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
                                            flowInits.put("singleFile", true);

                                            customFieldTemplateDataBean.put("flowInit", flowInits);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null) {
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
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.FILE);

                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "fileUpload");
                                            Map<String, Object> flowInit = new HashMap<>();
                                            flowInit.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null) {
                                                customFieldTemplateDataBean.put("maxsize", (Double) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                            }
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE) != null) {
                                                flowInit.put("singleFile", !(Boolean) pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_MULTIPLE_FILE));
                                            } else {
                                                flowInit.put("singleFile", true);
                                            }
                                            customFieldTemplateDataBean.put("flowInit", flowInit);
                                            if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE) != null) {
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
                                                } else {
                                                    if (pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).toString().contains(",")) {
                                                        String[] str = pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).toString().split(",");
                                                        for (String string : str) {
                                                            flieType.put(string, 1);
                                                        }
                                                    } else {
                                                        flieType.put(pointerCustomFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOW_FILE_TYPE).toString(), 1);
                                                    }

                                                }
                                                customFieldTemplateDataBean.put("acceptedFormats", flieType);
                                            }
                                        }

                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.NUMBER:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER));
                                    } else {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }

                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "select");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
//                                    if (featureFieldPermDoc.getHkFieldEntity().getFieldValues() != null) {
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(featureFieldPermDoc.getHkFieldEntity().getFieldValues()));
//                                    }

                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "subEntity");

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));

                                    }
                                    if (featureFieldPermDoc.getHkFieldEntity().getId() != null) {
                                        customFieldTemplateDataBean.put("CustomFieldId", featureFieldPermDoc.getHkFieldEntity().getId());
                                    }
                                    List<SelectItem> createDropDownListForSubEntity = this.createDropDownListForSubEntity(featureFieldPermDoc.getHkFieldEntity().getId());
                                    String fieldValuesForSubentity = null;
                                    if (createDropDownListForSubEntity != null) {

                                        for (SelectItem dropList : createDropDownListForSubEntity) {
                                            if (fieldValuesForSubentity == null) {
                                                fieldValuesForSubentity = dropList.getLabel().toString() + HkSystemConstantUtil.SEPARATOR_PI + dropList.getValue();
                                            } else {
                                                fieldValuesForSubentity = fieldValuesForSubentity + "," + dropList.getLabel().toString() + HkSystemConstantUtil.SEPARATOR_PI + dropList.getValue();
                                            }
                                        }

//                                        customFieldTemplateDataBean.put("dropDownListForSubEntity", makeValuesForSubEntity(fieldValuesForSubentity));
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.FORMULA:
                                    // code added by Shifa on 3 february 2014 for fetching formula from validation pattern
                                    Long featureId = featureFieldPermDoc.getHkFieldEntity().getFeature();
                                    SyncCenterFeatureDocument uMFeature = (SyncCenterFeatureDocument) hkUMSyncService.getDocumentById(featureId, SyncCenterFeatureDocument.class);
                                    String featureNameForField = uMFeature.getName();
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.FEATURE, featureNameForField);

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "formula");
                                    String[] validationsArrFormula = featureFieldPermDoc.getHkFieldEntity().getValidationPattern().replace("{", "")
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
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "multiSelect");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
//                                    if (featureFieldPermDoc.getHkFieldEntity().getFieldValues() != null) {
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(featureFieldPermDoc.getHkFieldEntity().getFieldValues()));
//                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT:

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "UserMultiSelect");

//                                    if (featureFieldPermDoc.getHkFieldEntity().getFieldValues() != null) {
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(featureFieldPermDoc.getHkFieldEntity().getFieldValues()));
//                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_EMPLOYEE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_EMPLOYEE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DEPARTMENT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DEPARTMENT));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_DESIGNATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_DESIGNATION));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_MULTISELECT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MULTISELECT));
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
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
                                                    validatePattern += "!@#$%^&*()_ ";
                                                    break;
                                            }
                                        }
                                        validatePattern += "]*$/";
                                        finalValidatePattern = validatePattern;
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DEFAULT_REGEX_TEXT, finalValidatePattern);
                                        System.out.println("Allowed type not null" + finalValidatePattern);
                                    }
                                    // Kept in if because it will get override if we have regex
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                        finalValidatePattern = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
                                    }
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, finalValidatePattern);

                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.EMAIL:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
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
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.PHONE:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.STRING);
                                    // If user has given regex pattern accept that regex otherwise no regex .
                                    String finalValidatePatternPhone = "";
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ORIGINAL_REGEX).toString().isEmpty() && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString().isEmpty()) {
                                        finalValidatePatternPhone = customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REGEX).toString();
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
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null) {
                                        maxLengthPhone = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                    } else {
                                        maxLengthPhone = 15.00;
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLengthPhone);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null && !customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE).toString().isEmpty()) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
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
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.RADIO_BUTTON:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "radio");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
//                                    if (featureFieldPermDoc.getHkFieldEntity().getFieldValues() != null) {
//                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.VALUES, makeValues(featureFieldPermDoc.getHkFieldEntity().getFieldValues()));
//                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.DATE:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                        attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                                    } else {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                        attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                    }
                                    // code for handling contraints added by Shifa on 27 January 2015
                                    String[] validationsArrDate = featureFieldPermDoc.getHkFieldEntity().getValidationPattern().replace("{", "")
                                            .replace("}", "")
                                            .split(",");
                                    String constraintValueDate = "";
                                    for (String validationValue : validationsArrDate) {
                                        if (validationValue.contains("\"constraint\":")) {
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.IS_CONSTRAINT, true);
                                            String[] constraintArray = validationValue.split(":");
                                            constraintValueDate += constraintArray[1];
                                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CONSTRAINT_VALUE, constraintValueDate);
                                        }
                                    }
                                    // code for contraint ends here
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.DATE_RANGE:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                    customFieldTemplateDataBean.put("fromModel", "from" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                    customFieldTemplateDataBean.put("toModel", "to" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                    customFieldTemplateDataBean.remove(HkSystemConstantUtil.CustomField.MODEL);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                    }

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                        attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetimerange");
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");
                                    } else {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                                        attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "daterange");
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                                    }
//                                customFieldTemplateDataBean.put("from-date-time-model", hkFieldEntity.getDbFieldName());
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.TIME:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "time");
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "time");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.TIME_RANGE:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                    customFieldTemplateDataBean.put("fromModel", "from" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                    customFieldTemplateDataBean.put("toModel", "to" + featureFieldPermDoc.getHkFieldEntity().getDbFieldName());
                                    customFieldTemplateDataBean.remove(HkSystemConstantUtil.CustomField.MODEL);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "time");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "timerange");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_TIME, (Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.CURRENCY:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "currency");
//                                    String currencyId = hkFieldEntity.getHkFieldEntity().getAssociatedCurrency();
//                                    HkCurrencyEntity currencyEntity = currencyMasterMap.get(Long.parseLong(currencyId));
//                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_TYPE, currencyEntity.getSymbol());
//                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_FORMAT, currencyEntity.getFormat());
//                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.CURRENCY_PREFIX, currencyEntity.getSymbolPosition());
                                    if (mapOfCurrencyCodeWithCurrencySymbol != null && !mapOfCurrencyCodeWithCurrencySymbol.isEmpty()) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE_SYMBOL_MAP, mapOfCurrencyCodeWithCurrencySymbol);
                                    }

                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_FORMAT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_FORMAT));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_SYMBOL_POSITION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_SYMBOL_POSITION));
                                    }
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                        String validatePattern = null;
                                        if (afterDecimal.intValue() == 0) {
                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                            } else {
                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                            }
                                        } else {
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            } else {

                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                        }
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.EXCHANGE_RATE:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "exchangeRate");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CURRENCY_CODE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CURRENCY_CODE));
                                    }
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_AFTR_DECIMAL, 3);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DIGITS_BFR_DECIMAL, 16);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, "/^\\d{0,16}(\\.\\d{0,3})?$/");
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.UPLOAD:

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");

                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.CHECKBOX:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "checkbox");
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.CHECKBOX_FORMAT));
                                    } else {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.CHECKBOX_FORMAT, "after");
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.AUTO_GENERATED:
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "AutoGenerated");
//                                customFieldTemplateDataBean.put(HkSystemConstantUtil.DynamicFormAttribute.PLACEHOLDER, "AutoGenerated");
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.ANGLE:

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "Angle");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                    } else {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, "0");
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                    } else {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, "360");
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                        String validatePattern = null;
                                        if (afterDecimal.intValue() == 0) {
                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                            } else {
                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                            }
                                        } else {
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            } else {

                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                        }
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.IMAGE:

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.IMAGE);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "imageUpload");
                                    customFieldTemplateDataBean.put("width", "100%");
                                    customFieldTemplateDataBean.put("height", "100%");
//                                    Map<String, Object> flowInits = new HashMap<>();
//                                    flowInits.put("target", HkSystemConstantUtil.UPLOAD_TARGET_URL);
//                                    flowInits.put("singleFile", true);

//                                    customFieldTemplateDataBean.put("flowInit", flowInits);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) != null) {
                                        customFieldTemplateDataBean.put("maxsize", (Double) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_FILE_SIZE) * 1024 * 1024);
                                    }
                                    Map<String, Object> flieType = new HashMap<>();
                                    flieType.put("jpg", 1);
                                    flieType.put("png", 1);
                                    flieType.put("psd", 1);
                                    customFieldTemplateDataBean.put("fileFormat", flieType);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.SHOWTHUMBNAIL));

                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.ComponentType.PERCENT:

                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                                    customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                                    attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                                        attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                                        String validatePattern = "";
                                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                            validatePattern += "/^-?";
                                        } else {
                                            validatePattern += "/^";
                                        }
                                        validatePattern += "[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                                        Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                                        Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                                        String validatePattern = null;
                                        if (afterDecimal.intValue() == 0) {
                                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                            } else {
                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                            }
                                        } else {
                                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                                validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            } else {

                                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                            }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                                        }
                                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                                    }
                                    break;
                            }
                            if (!CollectionUtils.isEmpty(attributeMap)) {
                                customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.ATTRIBUTES, attributeMap);
                            }
                            customFieldTemplateDataBeans.add(customFieldTemplateDataBean);
                        }
                    }

                    if (hkSectionDocument == null) {
                        result.put("genralSection", customFieldTemplateDataBeans);
                    } else {
                        result.put(hkSectionDocument.getSectionName(), customFieldTemplateDataBeans);
                    }
                }
            }
            return result;
        }
        return null;
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

    public static Comparator<SelectItem> NameComparator = new Comparator<SelectItem>() {
        @Override
        public int compare(SelectItem o1, SelectItem o2) {
            return o1.getLabel().compareToIgnoreCase(o2.getLabel());
        }
    };

    public List<SelectItem> retrieveCurrencyForComboForDynamicForm() {
        List<SelectItem> selectItems = null;
//        List<HkCurrencyEntity> currencyEntitys = hkFoundationService.retrieveAllCurrency(true, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(HkSystemConstantUtil.CURRENCY_CODE_MAP)) {
            Map<String, String> currencyMapUI = new HashMap<String, String>(HkSystemConstantUtil.CURRENCY_CODE_MAP);
            selectItems = new ArrayList<>();
            String code = "";
            String currencyLabel = "";
            String description = "";

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
            Collections.sort(selectItems, NameComparator);
        }
        return selectItems;
    }

    public Map<Long, String> retrieveFeatureIdWithNameMap() {
        Map<String, UMFeatureDetailDataBean> umFeatureDetailDataBeanMap = applicationUtil.getuMFeatureDetailDataBeanMap();
        Map<Long, String> mapFeatureIdName = new HashMap<>();
        for (Map.Entry<String, UMFeatureDetailDataBean> entrySet : umFeatureDetailDataBeanMap.entrySet()) {
            UMFeatureDetailDataBean value = entrySet.getValue();
            mapFeatureIdName.put(value.getId(), value.getFeatureName());
        }
        return mapFeatureIdName;
    }

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

    // This method returns selectItemList containing id as ObjectId and its value for a particular customfield which is of subentitytype
    // This list is used for filling the dropdown for the customfield which is of type subEntityon page
    // Created By Shifa Salheen on 22 January 2015
    public List<SelectItem> createDropDownListForSubEntity(long hkFieldEntityId) {
        List<SelectItem> selectItemList = null;
        HkFieldDocument fieldEntity = (HkFieldDocument) hkUMSyncService.getDocumentById(hkFieldEntityId, HkFieldDocument.class);
        System.out.println("id..." + fieldEntity.getFieldLabel());
        Map<String, HkSubFormFieldDocument> SubEntityDropField = new HashMap<>();

        List<HkSubFormFieldDocument> listOfSubEntities = customFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(fieldEntity.getId(), loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            System.out.println("list not null");
            for (HkSubFormFieldDocument listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    System.out.println("get droplist" + listOfSubEntity.getIsDroplistField());
                    SubEntityDropField.put(listOfSubEntity.getSubFieldName(), listOfSubEntity);
                }
            }
        }

        System.out.println("Sub drop field" + SubEntityDropField);
        Map<ObjectId, String> dropDownValuesForSubEntity = new HashMap<>();
        System.out.println("instance " + hkFieldEntityId);
        List<HkSubFormValueDocument> subFormDocuments = customFieldService.retrieveSubFormValueByInstance(hkFieldEntityId, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(subFormDocuments)) {
            System.out.println("sub doc not empty");
            selectItemList = new ArrayList<>();
            for (HkSubFormValueDocument subFormDocument : subFormDocuments) {
                Map<String, Object> fieldValueMap = subFormDocument.getFieldValue().toMap();
                for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                    System.out.println("entry set get key.." + entrySet.getKey());
                    if (SubEntityDropField.containsKey(entrySet.getKey())) {
                        SelectItem selectItem = new SelectItem(entrySet.getValue().toString(), subFormDocument.getId().toString());
                        selectItemList.add(selectItem);
                        dropDownValuesForSubEntity.put(subFormDocument.getId(), entrySet.getValue().toString());
                    }
                }
            }

        }
        System.out.println("list" + selectItemList);
        return selectItemList;
    }

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

    public Map<String, String> retrieveRecipientNames(String recipientIds) {
        recipientIds = recipientIds.replace("[", "").replace("]", "").replace("\"", "").trim();
        Map<String, String> recipentMap = null;
        List<String> recipientCodes = new ArrayList<>();
        String[] split = recipientIds.split(",");
        for (String split1 : split) {
            recipientCodes.add(split1.trim());
        }
        recipentMap = hkUserService.retrieveRecipientNames(recipientCodes);
        return recipentMap;
    }

    public List<Map<String, Object>> retrieveSubEntities(Long hkFieldEntityId) {
        //This was extra call so removing it
//        HkFieldDocument fieldEntity = customFieldService.retrieveCustomFieldByFieldId(hkFieldEntityId);
        List<HkSubFormFieldDocument> listOfSubEntities = customFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(hkFieldEntityId, loginDataBean.getCompanyId());
        List<Map<String, Object>> customFieldTemplateDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {

            for (HkSubFormFieldDocument hkSubFormFieldEntity : listOfSubEntities) {

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
                        String validatePatternForEmail = "/^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/";
                        customFieldTemplateDataBean.put("validate", validatePatternForEmail);

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.PHONE:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.TEXT_AREA:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "textarea");

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "text");

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH) != null && !((customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH)).toString().isEmpty())) {
                            Double maxLength = Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH).toString());
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.MAX_LENGTH, maxLength);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH) != null) {

                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH, Double.parseDouble(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.MIN_LENGTH).toString()));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, (String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.MASKED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.IS_MASKED));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE) != null && !(customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ALLOWED_TYPE).toString().isEmpty())) {
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
                                        validatePattern += "!@#$%^&*()_ ";
                                        break;
                                }
                            }
                            validatePattern += "]*$/";
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);

                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.NUMBER:
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "number");
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, !(Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.ONLY_INTEGER));
                        } else {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                        }

                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                            String validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                            String validatePattern = "/^[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                            String validatePattern = null;
                            if (afterDecimal.intValue() == 0) {
                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                            } else {
                                validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";

//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.PERCENT:

                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE);
                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "percent");
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.PHONE_NUM, true);
                        attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, true);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.NEGATIVE_ALLOWED, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VAL, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_VALUE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.HINT, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.MIN_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.START_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.MAX_RANGE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.END_RANGE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) == null) {
                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                            String validatePattern = "";
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                validatePattern += "/^-?";
                            } else {
                                validatePattern += "/^";
                            }
                            validatePattern += "[0-9]{0," + beforeDecimal + "}[.]([0-9]{0,15})?$/";
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) == null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));
                            String validatePattern = "";
                            if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                validatePattern += "/^-?";
                            } else {
                                validatePattern += "/^";
                            }
                            validatePattern += "[0-9]*[.]([0-9]{0," + afterDecimal + "})?$/";
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL) != null) {
                            Integer beforeDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_BEFORE_DECIMAL)));
                            Integer afterDecimal = (Integer.parseInt((String) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DIGITS_AFTER_DECIMAL)));

                            String validatePattern = null;
                            if (afterDecimal.intValue() == 0) {
                                attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DECIMAL_ALLOW, false);
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}?$/";
                                } else {
                                    validatePattern = "/^[0-9]{0," + beforeDecimal + "}?$/";
                                }
                            } else {
                                if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.NEGATIVE_ALLOWED) != null) {
                                    validatePattern = "/^-?[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                } else {

                                    validatePattern = "/^[0-9]{0," + beforeDecimal + "}[.]([0-9]{0," + afterDecimal + "})?$/";
                                }
//                                            /^[0-9]{1,4}[.]([0-9]{1,3})?$/
                            }
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.VALIDATE, validatePattern);
                        }
                        break;
                    case HkSystemConstantUtil.CustomField.ComponentType.DATE:

                        customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                        attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DEFAULT_DATE));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME) != null && customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.INCLUDE_TIME).toString().equals("true")) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "datetime");
                            customFieldTemplateDataBean.put(HkSystemConstantUtil.CustomField.DB_TYPE, "datetime");

                        } else {
                            attributeMap.put(HkSystemConstantUtil.CustomField.DATE_FORMAT, HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
                            attributeMap.put(HkSystemConstantUtil.CustomField.TYPE, "date");
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION) != null) {
                            attributeMap.put(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION, customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.DATE_VALIDATION));
                        }
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.HINT_MESSAGE) != null) {
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

    public Map<String, Object> retrieveCustomFieldPagePrerequisite() {

        List<SyncCenterFeatureDocument> allUMFeatures = featureService.retrieveAllFeatures(true);
        Map<String, Object> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(allUMFeatures)) {
            List<SelectItem> hkSelectItemsForAllFeature = new ArrayList<>();
            List<SelectItem> hkSelectItemsForAllFeatureWithEntityType = new ArrayList<>();
            List<String> skipList = Arrays.asList("manageDesignation", "manageCustomField", "manageMasters", "manageLocation", "manageActivityFlow", "manageLocales", "manageMessages", "manageReferenceRate", "manageActivity", "manageFeature");
            Map<Long, String> featureIdAndFeatureMap = new HashMap<>();
            for (SyncCenterFeatureDocument uMFeature : allUMFeatures) {
                if (!skipList.contains(uMFeature.getName())) {
                    SelectItem hkSelectItem = new SelectItem(uMFeature.getId(), uMFeature.getMenuLabel());
                    hkSelectItem.setDescription(uMFeature.getMenuType());
                    if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU)) {
                        hkSelectItem.setIsActive(true);
                    } else {
                        hkSelectItem.setIsActive(false);
                    }
                    hkSelectItemsForAllFeature.add(hkSelectItem);
                    featureIdAndFeatureMap.put(uMFeature.getId(), uMFeature.getMenuLabel());
                }
            }
            resultMap.put("allFeature", hkSelectItemsForAllFeature);
            List<SyncCenterFeatureDocument> featureList = featureService.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), true, null);
            if (!CollectionUtils.isEmpty(featureList)) {
                for (SyncCenterFeatureDocument feature : featureList) {
                    SelectItem hkSelectItemEntity = new SelectItem(feature.getId(), feature.getMenuLabel());
                    hkSelectItemsForAllFeatureWithEntityType.add(hkSelectItemEntity);

                }
            }
            resultMap.put("entityFeature", hkSelectItemsForAllFeatureWithEntityType);
            Set<Long> exitingCustomField = customFieldService.retrieveFeaturesForExistingFields(true, loginDataBean.getCompanyId());
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
        List<SelectItem> conditionalOperatorList = fillConditionalOperatorsList();
        resultMap.put("conditionalOperatorList", conditionalOperatorList);
        List<SelectItem> arithemeticOperatorsList = fillArithemeticOperatorsList();
        resultMap.put("arithemeticOperatorsList", arithemeticOperatorsList);
        return resultMap;
    }

    public List<SelectItem> fillConditionalOperatorsList() {
        List<SelectItem> conditionalOperatorList = new ArrayList<>();
        conditionalOperatorList.add(new SelectItem(">", ">(Greater than)"));
        conditionalOperatorList.add(new SelectItem("<", "<(Less than)"));
        conditionalOperatorList.add(new SelectItem("=", "=(Equal to)"));
        conditionalOperatorList.add(new SelectItem(">=", ">=(Greater than equal to)"));
        conditionalOperatorList.add(new SelectItem("<=", ">=(Less than equal to)"));
        return conditionalOperatorList;
    }

    public List<SelectItem> fillArithemeticOperatorsList() {
        List<SelectItem> arithemeticOperatorsList = new ArrayList<>();
        arithemeticOperatorsList.add(new SelectItem("+", "+"));
        arithemeticOperatorsList.add(new SelectItem("-", "-"));
        arithemeticOperatorsList.add(new SelectItem("*", "*"));
        arithemeticOperatorsList.add(new SelectItem("/", "/"));
        arithemeticOperatorsList.add(new SelectItem("%", "% OF"));
        return arithemeticOperatorsList;
    }

    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFields(String featureName) {
        Map<String, List<DependentFieldDataBean>> fieldMap = null;
        SyncCenterFeatureDocument feature = featureService.retireveFeatureByName(featureName);
        if (feature != null) {
//            UMFeature feature = features.get(0);
            List<HkFeatureFieldPermissionDocument> featureFieldPermissionEntitys = customFieldService.retrieveFeatureFieldPermissions(feature.getId(), loginDataBean.getRoleIds());
            if (!CollectionUtils.isEmpty(featureFieldPermissionEntitys)) {
                fieldMap = new HashMap<>();
                List<DependentFieldDataBean> dependentFieldDataBeans;
                for (HkFeatureFieldPermissionDocument ffPermissionEntity : featureFieldPermissionEntitys) {

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
                        if (ffPermissionEntity.isIsRequired() != null) {
//                            System.out.println("required set to true" + ffPermissionEntity.getHkFieldEntity().getFieldLabel());
                            dependentFieldDataBean.setIsRequired(ffPermissionEntity.isIsRequired());
                        } else {
//                            System.out.println("required set to false" + ffPermissionEntity.getHkFieldEntity().getFieldLabel());
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
        System.out.println("Field map..." + fieldMap);
        return fieldMap;
    }

    public List<SyncCenterUserDocument> retrieveCenterUsersByDesignation(List<Long> roleId, String search) {
        List<SyncCenterUserDocument> users = syncCenterUserService.retrieveUsersByRoleId(roleId, search, loginDataBean.getCompanyId());
        return users;
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

    public List<UmDesignationDocument> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive) {
        return hkUserService.retrieveRolesByCompanyByStatus(companyId, role, isActive, loginDataBean.getPrecedence());
    }

    public Map<String, String> retrieveRecipientNames(List<String> codes) {
        Map<String, String> map = customFieldService.retrieveRecipientNames(codes);
        if (!CollectionUtils.isEmpty(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String id = entry.getKey();
                String name = entry.getValue();
            }
        }
        return map;
    }

    public List<HkDepartmentDocument> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        List<HkDepartmentDocument> departments = hkUserService.retrieveDepartmentsByCompanyByStatus(companyId, department, isActive);
        return departments;
    }

    public List<SyncCenterUserDocument> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive) {
        List<SyncCenterUserDocument> users = hkUserService.retrieveUsersByCompanyByStatus(user, companyId, isActive, true, loginDataBean.getId());
        return users;
    }

    public List<SyncCenterUserDocument> retrieveUsersbyDepartment(List<Long> depIds, Long companyId, String user, Boolean isActive) {
        return hkUserService.retrieveUsersByDepartmentIds(companyId, user, depIds, isActive, true, loginDataBean.getId());
    }

    public List<SyncCenterUserDocument> retrieveUsersbyRole(List<Long> depIds, Long companyId, String user, Boolean isActive) {
        return hkUserService.retrieveUsersByRoleIds(companyId, user, depIds, isActive, true, loginDataBean.getId());
    }

    public String retrieveValueNameForSelect(String valueId) {
        HkMasterValueDocument valueDocument = hkFoundationDocumentService.retrieveValueEntityById(Long.parseLong(valueId));
        String valueName = null;
        if (valueDocument != null) {
            valueName = valueDocument.getValueName();
        }
        return valueName;
    }

    public String retrieveValueNamesForMultiSelect(String value) {
        String valueName = ",";
        String[] split = value.split(",");
        List<String> valueIdArray = Arrays.asList(split);
        List<Long> valueIds = null;
        if (!CollectionUtils.isEmpty(valueIdArray)) {
            valueIds = new ArrayList<>();
            for (String val : valueIdArray) {
                valueIds.add(Long.parseLong(val.trim()));
            }
        }
        List<HkMasterValueDocument> valueEntityList = hkFoundationDocumentService.retrieveValueEntities(valueIds);

        if (!CollectionUtils.isEmpty(valueEntityList)) {
            for (HkMasterValueDocument valEntity : valueEntityList) {
                valueName += valEntity.getValueName() + ",";
            }
        }
        // Now remove first and last comma
        String finalValueName = valueName.substring(1, valueName.length() - 1);
        return finalValueName;
    }

    public String retrieveSubEntityNames(String param, String fieldId) {
        String subEntityName = null;
        if (param != null && param.length() > 0) {
            List<SelectItem> dropdownListForSubentity = this.createDropDownListForSubEntity(Long.parseLong(fieldId));
            if (!CollectionUtils.isEmpty(dropdownListForSubentity)) {
                for (SelectItem select : dropdownListForSubentity) {
                    if (select.getLabel().equals(param)) {
                        subEntityName = select.getValue().toString();
                    }
                }
            }
        }
        return subEntityName;
    }

    public String retrieveCaratRangeNames(String caratId) {
        String caratName = null;
        HkCaratRangeDocument caratDocument = (HkCaratRangeDocument) hkUMSyncService.getDocumentById(Long.parseLong(caratId), HkCaratRangeDocument.class);
        if (caratDocument != null) {
            caratName = caratDocument.getMinValue() + " - " + caratDocument.getMaxValue();
        }
        return caratName;
    }
// Method added by Shifa for conversion of table

    public Map<String, String> retrieveValuesOfComponentIds(Map<String, List<String>> customIds) {
        Map<String, String> idsWithValueName = null;
        if (customIds != null && !customIds.isEmpty()) {
            idsWithValueName = new HashMap<>();
            Boolean isDropDown = false;
            Boolean isMultiSelect = false;
            Boolean isUserMultiSelect = false;
            Boolean isText = false;
            Boolean isSubEntity = false;
            Boolean isCarat = false;
            List<Long> selectOrMultiSelectIds = new ArrayList<>();
            List<String> userMultiSelectIds = new ArrayList<>();
            List<String> textModels = null;
            List<String> subEntityValue = null;
            List<String> caratIds = null;
            for (Map.Entry<String, List<String>> cids : customIds.entrySet()) {
                if (cids.getKey().equals(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
//                    System.out.println("valuess..." + cids.getValue());
                    List<String> dropDownIds = cids.getValue();
                    if (!CollectionUtils.isEmpty(dropDownIds)) {
                        for (String dropDownId : dropDownIds) {
                            System.out.println("Droppdow id" + dropDownId);
                            if (!StringUtils.isEmpty(dropDownId)) {
                                selectOrMultiSelectIds.add(Long.parseLong(dropDownId.trim()));
                            }
//                            System.out.println("Select ids.." + selectIds);
                        }

                    }
                    if (!CollectionUtils.isEmpty(selectOrMultiSelectIds)) {
                        isDropDown = true;
                    }

                } else if (cids.getKey().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                    if (!CollectionUtils.isEmpty(cids.getValue())) {
                        String[] split = cids.getValue().toString().replace("[", "").replace("]", "").split(",");
                        for (String split1 : split) {
                            if (StringUtils.hasText(split1) && StringUtils.hasLength(split1)) {
                                selectOrMultiSelectIds.add(Long.parseLong(split1.trim()));
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(selectOrMultiSelectIds)) {
                        isMultiSelect = true;
                    }
                } else if (cids.getKey().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)) {
                    String[] splitUser = cids.getValue().toString().replace("[", "").replace("]", "").split(",");
                    for (String splitUser1 : splitUser) {
                        userMultiSelectIds.add(splitUser1.trim());
                    }
                    if (!CollectionUtils.isEmpty(userMultiSelectIds)) {
                        isUserMultiSelect = true;
                    }
                } else if (cids.getKey().equals(HkSystemConstantUtil.CustomField.ComponentType.TEXT_FIELD)) {
                    textModels = cids.getValue();
                    isText = true;
                } else if (cids.getKey().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                    subEntityValue = cids.getValue();
                    isSubEntity = true;

                } else if (cids.getKey().equals("caratRange")) {
                    caratIds = cids.getValue();
                    isCarat = true;
                }

            }
            if (isDropDown || isMultiSelect) {
                List<HkMasterValueDocument> valueEntities = hkFoundationDocumentService.retrieveValueEntities(selectOrMultiSelectIds);
                if (!CollectionUtils.isEmpty(valueEntities)) {
                    for (HkMasterValueDocument valueEntity : valueEntities) {
                        idsWithValueName.put(valueEntity.getId().toString(), valueEntity.getValueName());
                    }
                }
            }
            if (isUserMultiSelect) {
                Map<String, String> recipientNames = customFieldService.retrieveRecipientNames(userMultiSelectIds);
                if (recipientNames != null && !recipientNames.isEmpty()) {
                    for (Map.Entry<String, String> recipient : recipientNames.entrySet()) {
                        idsWithValueName.put(recipient.getKey(), recipient.getValue());
                    }
                }
            }
            if (isText) {
                Map<String, HkFieldDocument> mapOfDBFieldNameWithEntity = customFieldService.retrieveMapOfDBFieldNameWithEntity(textModels, loginDataBean.getCompanyId());
                if (mapOfDBFieldNameWithEntity != null && !mapOfDBFieldNameWithEntity.isEmpty()) {
                    for (Map.Entry<String, HkFieldDocument> dbFieldName : mapOfDBFieldNameWithEntity.entrySet()) {
                        String validationPattern = dbFieldName.getValue().getValidationPattern();
                        // creating an array of validation pattern
                        String[] validationsArr = validationPattern.replace("{", "")
                                .replace("}", "")
                                .split(",");
                        for (String validationValue : validationsArr) {

                            if (validationValue.contains("\"isMasked\":")) {
                                String[] maskingArray = validationValue.split(":");

                                if (maskingArray[1].equals("true")) {
                                    // map contains whether dbfield has isMasking property or not
                                    idsWithValueName.put(dbFieldName.getKey(), "true");
                                } else {
                                    idsWithValueName.put(dbFieldName.getKey(), "false");
                                }
                            } else {
                                idsWithValueName.put(dbFieldName.getKey(), "false");
                            }

                        }

                    }
                }
            }
            if (isSubEntity) {
                List<ObjectId> subObjectIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(subEntityValue)) {
                    for (String subValue : subEntityValue) {
                        subObjectIds.add(new ObjectId(subValue));
                    }
                }

                // fetch map of object ids with documents
                Map<ObjectId, HkSubFormValueDocument> mapOfObjectIdsWithDocuments = customFieldService.mapOfObjectIdsWithDocuments(subObjectIds);
                // fetch droplist document with field id
                Map<Long, HkSubFormFieldDocument> mapOfFieldIdAndItsDropListSubDocument = customFieldService.retrieveMapOfFieldIdAndItsDropListSubDocument();

                if (mapOfObjectIdsWithDocuments != null && !mapOfObjectIdsWithDocuments.isEmpty()) {
                    for (Map.Entry<ObjectId, HkSubFormValueDocument> entrySet : mapOfObjectIdsWithDocuments.entrySet()) {
                        String dropListName = null;
                        String finalVal = null;
                        ObjectId key = entrySet.getKey();
                        HkSubFormValueDocument subFormValueDocument = entrySet.getValue();
                        Map fieldValueMap = subFormValueDocument.getFieldValue().toMap();
                        if (mapOfFieldIdAndItsDropListSubDocument.containsKey(subFormValueDocument.getInstanceId())) {
                            dropListName = mapOfFieldIdAndItsDropListSubDocument.get(subFormValueDocument.getInstanceId()).getSubFieldName();
                        }
                        if (fieldValueMap.containsKey(dropListName)) {
                            finalVal = fieldValueMap.get(dropListName).toString();
                        }
                        idsWithValueName.put(key.toString(), finalVal);
                    }

                }

            }
            if (isCarat) {
                List<Long> newCaratIds = null;
                if (!CollectionUtils.isEmpty(caratIds)) {
                    newCaratIds = new ArrayList<>();
                    for (String caratId : caratIds) {
                        newCaratIds.add(Long.parseLong(caratId));
                    }
                }
                Map<Long, String> caratRangeMap = hkStockService.caratRangeMap(newCaratIds);
                if (caratRangeMap != null && !caratRangeMap.isEmpty()) {
                    for (Map.Entry<Long, String> caratRange : caratRangeMap.entrySet()) {
                        idsWithValueName.put(caratRange.getKey().toString(), caratRange.getValue());
                    }
                }
            }

        }
        return idsWithValueName;
    }

    public Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId(Long instanceId, HkSystemConstantUtil.FeatureNameForCustomField feature, long companyId) {
        List<Criteria> criterias = new ArrayList<>();
        List<GenericDocument> documents = null;

        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionCustomFieldMap = null;
        if (instanceId != null) {
            criterias.add(Criteria.where("instanceId").is(instanceId));
        }
        criterias.add(Criteria.where("franchiseId").is(companyId));
        documents = (List<GenericDocument>) customFieldService.retrieveDocument(instanceId, feature, companyId);
        if (!CollectionUtils.isEmpty(documents)) {
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            for (GenericDocument genericDocument : documents) {
                if (genericDocument.getFieldValue() != null) {
                    Map<String, Object> customMap = genericDocument.getFieldValue().toMap();
                    List<String> uiFieldList = new ArrayList<>();
                    for (Map.Entry<String, Object> entrySet : customMap.entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                    Map<String, String> uiFieldListWithComponentType = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
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

    public String retrieveObjectIdValueFromMongoForConstraint(Long fieldId, String invoiceId, String parcelId, String lotId, String packetId) throws GenericDatabaseException {
        System.out.println("Invoice Id:::" + invoiceId);
        HkFieldDocument fieldEntity = (HkFieldDocument) hkUMSyncService.getDocumentById(fieldId, HkFieldDocument.class);
        Long featureId = fieldEntity.getFeature();
        String dbFieldName = fieldEntity.getDbFieldName();
        String instanceValue = null;
        SyncCenterFeatureDocument feature = (SyncCenterFeatureDocument) hkUMSyncService.getDocumentById(featureId, SyncCenterFeatureDocument.class);
        String featureName = feature.getName();
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.INVOICE)) {
            if (invoiceId != null && invoiceId.length() > 0) {
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
        }
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.PARCEL)) {
            if (parcelId != null && parcelId.length() > 0) {
                HkParcelDocument parcelDocument = stockService.retrieveParcelById(new ObjectId(parcelId));
                Map parcelValueMap = parcelDocument.getFieldValue().toMap();
                if (parcelValueMap.containsKey(dbFieldName)) {
                    instanceValue = (String) parcelValueMap.get(dbFieldName).toString();
                }
            }
        }
        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.LOT)) {
            if (lotId != null && lotId.length() > 0) {
                HkLotDocument lotDocument = stockService.retrieveLotById(new ObjectId(lotId));
                Map lotFieldValueMap = lotDocument.getFieldValue().toMap();
                if (lotFieldValueMap.containsKey(dbFieldName)) {
                    instanceValue = (String) lotFieldValueMap.get(dbFieldName).toString();
                }
            }
        }

        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.FeatureMenuLabel.PACKET)) {
            if (packetId != null && packetId.length() > 0) {
                HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(packetId),true);
                Map packetFieldValueMap = packetDocument.getFieldValue().toMap();
                if (packetFieldValueMap.containsKey(dbFieldName)) {
                    instanceValue = (String) packetFieldValueMap.get(dbFieldName).toString();
                }
            }
        }
        return instanceValue;
    }

    public Map<String, String> retrieveValueFromMongoForFormula(List<String> dbFieldNameList, String invoiceId, String parcelId, String lotId, String packetId) {
        Map<String, String> dbFieldNameWithInstanceValue = new HashMap<>();
        Map<String, HkFieldDocument> mapOfDbFieldNameWithEntity = null;
        // retrieving list of features of type entity i.e. diamond features
//        List<UMFeature> featuresList = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);
        List<String> invoiceList = new ArrayList<>();
        List<String> parcelList = new ArrayList<>();
        List<String> lotList = new ArrayList<>();
        List<String> packetList = new ArrayList<>();
        Map<String, String> invoiceMap = new HashMap<>();
        Map<String, String> parcelMap = new HashMap<>();
        Map<String, String> lotMap = new HashMap<>();
        Map<String, String> packetMap = new HashMap<>();
//        Map<Long, String> featureNameMap = new HashMap<>();
        // featureMap containing key as feature id and value as featureName

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
                    for (String newDbField1 : newDbField) {
                    }
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
        if (parcelMap != null && !parcelMap.isEmpty()) {
            List<ObjectId> objectIds = null;
            if (invoiceId != null && !(invoiceId.equalsIgnoreCase("undefined")) && invoiceId.length() > 0) {
                objectIds = new ArrayList<>();
                objectIds.add(new ObjectId(invoiceId));
            }
            List<HkParcelDocument> parcelDocuments = null;
            SyncCenterFeatureDocument feature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.PARCEL);
            if (feature != null) {
//                Map<String, String> uiFieldMap = hkFieldService.retrieveCustomUIFieldNameWithComponentTypes(feature.getId(), loginDataBean.getCompanyId());
                SyncCenterFeatureDocument lotFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.LOT);
//                Map<String, String> lotUIFieldMap = null;
//                if (lotFeature != null) {
//                    lotUIFieldMap = hkFieldService.retrieveCustomUIFieldNameWithComponentTypes(lotFeature.getId(), loginDataBean.getCompanyId());
//                }
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
        if (lotMap != null && !lotMap.isEmpty()) {
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

            SyncCenterFeatureDocument lotFeature = featureService.retireveFeatureByName(HkSystemConstantUtil.Feature.LOT);
            Map<String, String> lotUIFieldMap = null;
//            if (lotFeature != null) {
//                lotUIFieldMap = hkFieldService.retrieveCustomUIFieldNameWithComponentTypes(lotFeature.getId(), loginDataBean.getCompanyId());
//            }

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
        if (packetMap != null && !packetMap.isEmpty()) {
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

            List<HkPacketDocument> packetDocuments = stockService.retrievePackets(null, invoiceObjectIds, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, null,null,null);
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
                HkPacketDocument packetDocument = stockService.retrievePacketById(new ObjectId(packetId),true);
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

    public List<SelectItem> retrieveAllCurrencyByLoginId() {
        List<SelectItem> currencyForComboForDynamicForm = this.retrieveCurrencyForComboForDynamicForm();
        return currencyForComboForDynamicForm;
    }

    public Map<Object, String> makeValuesForCarateRange() {
        Map<Long, HkCaratRangeDocument> carateRangeMap = null;
        Map<Object, String> valueMap = null;
        List<HkCaratRangeDocument> caratRangeEntitys = hkFoundationDocumentService.retrieveCaratRangeByFranchiseAndStatus(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.ACTIVE), null);
        if (!CollectionUtils.isEmpty(caratRangeEntitys)) {
            carateRangeMap = new HashMap<>();
            for (HkCaratRangeDocument hkCaratRangeEntity : caratRangeEntitys) {
                carateRangeMap.put(hkCaratRangeEntity.getId(), hkCaratRangeEntity);
            }

            valueMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(carateRangeMap)) {
                for (Map.Entry<Long, HkCaratRangeDocument> entry : carateRangeMap.entrySet()) {
                    Long id = entry.getKey();
                    HkCaratRangeDocument caratRangeEntity = entry.getValue();
                    String range = caratRangeEntity.getMinValue() + " - " + caratRangeEntity.getMaxValue();
                    valueMap.put(id, range);
                }
            }
        }
        return valueMap;
    }

    public Map<Long, String> makeValuesForMachineAssets() {
        Map<Long, String> valueMap = null;
        HkCategoryDocument machineCategory = hkFoundationDocumentService.retrieveAssetCategoryByPrefix(HkSystemConstantUtil.FRANCHISE_REQUIREMENT_TYPE.MACHINES, loginDataBean.getCompanyId(), CategoryType.ASSET);
        if (machineCategory != null) {
            List<HkAssetDocument> assets = hkFoundationDocumentService.retrieveAssets(machineCategory.getId(), loginDataBean.getCompanyId(), true);
            if (!CollectionUtils.isEmpty(assets)) {
                valueMap = new HashMap<>();
                for (HkAssetDocument assetEntity : assets) {
                    valueMap.put(assetEntity.getId(), assetEntity.getAssetName());
                }
            }
        }
        return valueMap;
    }

    public Map<String, Object> retrieveAutogeneratedFields(String dbFieldName, String search, Integer limit, Integer offset) {
        Map<String, Object> autogeneratedFields = customFieldService.retrieveAutogeneratedFields(dbFieldName, search, limit, offset);
        return autogeneratedFields;
    }

    public List<UmCompanyDocument> searchCompaniesByName(String searchTerm) {
        return hkUserService.searchCompanyByName(searchTerm);
    }

    public Boolean checkUniqueness(String fieldModel, String featureName, Object value) {
        return customFieldService.checkUniqueness(fieldModel, featureName, value, loginDataBean.getCompanyId());
    }

    public List<SelectItem> retrieveExceptionsDependantOnField(Long fieldId, Long valueId) {
        System.out.println(" on this method center");
        Map<Long, String> mapOfFieldidAndDbFieldName = customFieldService.mapOfFieldidAndDbFieldName(loginDataBean.getCompanyId());
        Map<Long, HkMasterValueDocument> valueEntititiesByCriteria = hkFoundationDocumentService.retrieveMapOfIdAndValueEntity(loginDataBean.getCompanyId());
        Map<String, List<HkMasterValueDocument>> mapOfKeyIdWithValueEntities = hkFoundationDocumentService.mapOfKeyIdWithValueEntities(loginDataBean.getCompanyId());
        List<SelectItem> selectItems = new ArrayList<>();
        if (valueId != null) {
            List<HkValueExceptionDocument> valueExceptionsByCriteria = hkFoundationDocumentService.retrieveValueExceptionsByCriteriaWithValue(fieldId, loginDataBean.getCompanyId(), valueId);
            if (!CollectionUtils.isEmpty(valueExceptionsByCriteria)) {

                for (HkValueExceptionDocument allValueException : valueExceptionsByCriteria) {
                    SelectItem selectItem = new SelectItem(allValueException.getInstanceId(), allValueException.getFieldType());
                    if (mapOfFieldidAndDbFieldName != null && !mapOfFieldidAndDbFieldName.isEmpty() && mapOfFieldidAndDbFieldName.containsKey(allValueException.getInstanceId())) {
                        selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(allValueException.getInstanceId()));

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
                                List<HkMasterValueDocument> valeEntitiesForAll = mapOfKeyIdWithValueEntities.get(allValueException.getInstanceId().toString());
                                if (!CollectionUtils.isEmpty(valeEntitiesForAll)) {
                                    for (HkMasterValueDocument valeEntityAll : valeEntitiesForAll) {
                                        valueMaps.put(valeEntityAll.getId(), valeEntityAll.getValueName());
                                    }
                                }
                            }
                        } else {
                            for (Long valueIds : allValueException.getForValues()) {

                                if (valueEntititiesByCriteria.containsKey(valueIds)) {
                                    System.out.println("id..." + valueIds);
                                    System.out.println(" nameee" + valueEntititiesByCriteria.get(valueIds).getValueName());
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
                    
                    if (mapOfFieldidAndDbFieldName.containsKey(firstDocumnet.getInstanceId())) {
                    selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(firstDocumnet.getInstanceId()));
                }
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
                if (mapOfFieldidAndDbFieldName.containsKey(firstDocumnet.getInstanceId())) {
                    selectItem.setCommonId(mapOfFieldidAndDbFieldName.get(firstDocumnet.getInstanceId()));
                }
                selectItems.add(selectItem);
            }

        }
        return selectItems;
    }
    /*
     * Added by Shifa on 24 August 2015 This method is used to retrieve fields by designation as per changes 
     */

    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFieldsFromConfiguration(String featureName) {
        Map<String, List<DependentFieldDataBean>> fieldMap = null;
        Map<Long, String> featureIdNameMap = applicationUtil.getFeatureIdNameMap();
        Map<String, Long> featureNameIdMap = null;
        if (featureIdNameMap != null && !featureIdNameMap.isEmpty()) {
            featureNameIdMap = new HashMap<>();
            for (Map.Entry<Long, String> fidNameMap : featureIdNameMap.entrySet()) {
                featureNameIdMap.put(fidNameMap.getValue(), fidNameMap.getKey());

            }

        }
        if (featureNameIdMap != null && !featureNameIdMap.isEmpty() && featureNameIdMap.containsKey(featureName)) {
            List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = hkConfigurationService.retrieveListOfPermissionDocuments(loginDataBean.getRoleIds(), featureNameIdMap.get(featureName), Boolean.FALSE, loginDataBean.getCompanyId(), null);

            if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
                fieldMap = new HashMap<>();
                List<DependentFieldDataBean> dependentFieldDataBeans;
                for (com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument ffPermissionEntity : featureFieldPermissionDocuments) {

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

    public Boolean checkUniquenessForFields(Map<String, String> mapOfFields, String fieldModelName, String value, String featureName) {
        Map<String, String> idRelatedFields = null;
        Map<String, String> fieldsWithValues = null;
        if (mapOfFields != null && !mapOfFields.isEmpty()) {
            idRelatedFields = new HashMap<>();
            fieldsWithValues = new HashMap<>();

            for (Map.Entry<String, String> originalMap : mapOfFields.entrySet()) {
                String key = originalMap.getKey();
                if (key.equals("invoiceId") || key.equals("parcelId") || key.equals("lotId") || key.equals("packetId")) {
                    idRelatedFields.put(key, originalMap.getValue());
                } else {
                    fieldsWithValues.put(key, originalMap.getValue());
                }

            }
        }
        return customFieldService.checkUniquenessForFields(fieldsWithValues, idRelatedFields, value, fieldModelName, featureName, loginDataBean.getCompanyId());
    }

    public Map<String, List<DependentFieldDataBean>> retrieveDesignationBasedFieldsFromConfigurationBySection(String featureName, String sectionCode) {
        Map<String, List<DependentFieldDataBean>> fieldMap = null;
        Map<Long, String> featureIdNameMap = applicationUtil.getFeatureIdNameMap();
        Map<String, Long> featureNameIdMap = null;
        if (featureIdNameMap != null && !featureIdNameMap.isEmpty()) {
            featureNameIdMap = new HashMap<>();
            for (Map.Entry<Long, String> fidNameMap : featureIdNameMap.entrySet()) {
                featureNameIdMap.put(fidNameMap.getValue(), fidNameMap.getKey());

            }

        }
        if (featureNameIdMap != null && !featureNameIdMap.isEmpty() && featureNameIdMap.containsKey(featureName)) {
            List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = hkConfigurationService.retrieveListOfPermissionDocuments(loginDataBean.getRoleIds(), featureNameIdMap.get(featureName), Boolean.FALSE, loginDataBean.getCompanyId(), sectionCode);

            if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
                fieldMap = new HashMap<>();
                List<DependentFieldDataBean> dependentFieldDataBeans;
                for (com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument ffPermissionEntity : featureFieldPermissionDocuments) {

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

    public Double retrieveReferenceRateForCurrencyForCenter(String currency) {
        Double rate = null;
        if (StringUtils.hasText(currency)) {
            HkReferenceRateDocument referenceRateDocument = customFieldService.retrieveActiveCurrencyRateByCode(currency);
            if (referenceRateDocument != null) {
                System.out.println("Not null");
                rate = referenceRateDocument.getReferenceRate();
            }
        }
        return rate;
    }
}
