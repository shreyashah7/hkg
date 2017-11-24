/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleCriteriaDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleSetDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author mansi
 */
@Service
public class RuleManagementTransformerBean {

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    FeatureTransformerBean featureTransformerBean;
    @Autowired
    HkFieldService hkFieldService;
    @Autowired
    HkFoundationService foundationService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    private UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkRuleService ruleService;
    @Autowired
    UMFeatureService featureService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleManagementTransformerBean.class);

    public Map<Long, String> retrieveEntitys(Long companyId) {
        List<FeatureDataBean> retrieveSystemFeaturesByListOfType = featureTransformerBean.retrieveSystemFeaturesByListOfType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), companyId, true);
        Map<Long, String> entityMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retrieveSystemFeaturesByListOfType)) {
            for (FeatureDataBean featureDataBean : retrieveSystemFeaturesByListOfType) {
                entityMap.put(featureDataBean.getId(), featureDataBean.getMenuLabel());
            }
        }
        return entityMap;
    }

    public List<CustomFieldDataBean> retrieveFieldsByEntity(Long featureId, Long companyId) {
        Map<HkSectionEntity, List<HkFieldEntity>> retrieveSectionsByFeatureId = hkFieldService.retrieveSectionsByFeatureId(featureId, companyId, null, false);
        List<CustomFieldDataBean> customFieldDataBeans = new ArrayList<>();
        for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entry : retrieveSectionsByFeatureId.entrySet()) {
            List<HkFieldEntity> list = entry.getValue();
            if (!CollectionUtils.isEmpty(list)) {
                for (HkFieldEntity hkFieldEntity : list) {
                    Boolean isRuleField = Boolean.TRUE;
                    if (!StringUtils.isEmpty(hkFieldEntity.getValidationPattern()) && !hkFieldEntity.getValidationPattern().equals("{}") && !hkFieldEntity.getValidationPattern().equalsIgnoreCase("p1")) {
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map<String, Object> validationPatternMap = (new Gson()).fromJson(hkFieldEntity.getValidationPattern().trim(), type);
                        if (!CollectionUtils.isEmpty(validationPatternMap) && validationPatternMap.containsKey("isRuleField")) {
                            isRuleField = Boolean.valueOf(validationPatternMap.get("isRuleField").toString().trim());
                        }
                    }
                    Boolean fieldToBeAdded = true;
                    String[] validationsArr = hkFieldEntity.getValidationPattern().replace("{", "")
                            .replace("}", "")
                            .split(",");

                    for (String validationValue : validationsArr) {
                        if (validationValue.contains("\"isObjectId\":")) {
                            String[] objectIdArray = validationValue.split(":");
                            if (objectIdArray[1].replace("\"", "").equals("true") || objectIdArray[1].replace("\"", "").equals(true)) {
                                fieldToBeAdded = false;
                            }
                        }
                    }
                    if (!isRuleField) {
                        fieldToBeAdded = false;
                    }
                    if (fieldToBeAdded) {
                        CustomFieldDataBean customFieldDataBean = new CustomFieldDataBean();
                        customFieldDataBean.setId(hkFieldEntity.getId());
                        customFieldDataBean.setLabel(hkFieldEntity.getFieldLabel());
                        customFieldDataBean.setOldLabelName(hkFieldEntity.getFieldLabel());
                        customFieldDataBean.setType(hkFieldEntity.getComponentType());
                        customFieldDataBean.setFieldType(hkFieldEntity.getFieldType());
                        customFieldDataBean.setValidationPattern(hkFieldEntity.getValidationPattern());
                        customFieldDataBean.setValues(hkFieldEntity.getFieldValues());
                        customFieldDataBean.setCurrencyMasterId(hkFieldEntity.getAssociatedCurrency());
                        customFieldDataBean.setDbFieldName(hkFieldEntity.getDbFieldName());
                        customFieldDataBeans.add(customFieldDataBean);
                    }
                }
            }
        }
        return customFieldDataBeans;
    }

    public List<SelectItem> retrieveMasterByField(String masterCode, Long companyId) {
        List<SelectItem> master = new ArrayList<>();
        List<HkValueEntity> retrieveMasterValuesByCode = foundationService.retrieveMasterValuesByCode(companyId, Arrays.asList(masterCode));
        if (!CollectionUtils.isEmpty(retrieveMasterValuesByCode)) {
            for (HkValueEntity hkValueEntity : retrieveMasterValuesByCode) {
                String valueName = hkValueEntity.getValueName();
                if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                    valueName = hkValueEntity.getTranslatedValueName();
                }
                master.add(new SelectItem(hkValueEntity.getId(), valueName, hkValueEntity.getShortcutCode()));
            }
        }
        return master;
    }

    public ObjectId createRule(RuleSetDataBean databean) {

        ObjectId response = null;
//        try {

        HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
        databean.setIsActive(Boolean.TRUE);
        databean.setIsArchive(Boolean.FALSE);
        ruleSetDocument = convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, databean, ruleSetDocument);
//            ruleSetDocument.setFranchise(loginDataBean.getCompanyId());
        ruleSetDocument.setCreatedOn(new Date());
        ruleSetDocument.setCreatedBy(loginDataBean.getId());
        ruleSetDocument.setLastModifiedOn(new Date());
        ruleSetDocument.setLastModifiedBy(loginDataBean.getId());

        ruleService.saveOrUpdateRuleSet(ruleSetDocument);
        response = ruleSetDocument.getId();
//        } catch (Exception e) {
//        }
        return response;
    }

    public HkRuleDocument convertRuleDatabeanToRuleDocument(String operation, RuleDatabean ruleDatabean, HkRuleDocument ruleDocument) {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap = dropListEntityFromSubFieldMap();
        Map<Long, String> featureMap = userManagementServiceWrapper.retrieveFeatureMap(true);
        if (ruleDocument == null) {
            ruleDocument = new HkRuleDocument();
        }
        if (loginDataBean != null) {
            if (operation != null && operation.equalsIgnoreCase(HkSystemConstantUtil.CREATE_OPERATION)) {
                ruleDocument.setCreateOn(new Date());
                ruleDocument.setCreatedBy(loginDataBean.getId());
            } else if (operation != null && operation.equalsIgnoreCase(HkSystemConstantUtil.UPDATE_OPERATION)) {
                ruleDocument.setModifiedOn(new Date());
                ruleDocument.setModifiedBy(loginDataBean.getId());
            } else {
                ruleDocument.setCreateOn(ruleDatabean.getCreateOn());
                ruleDocument.setCreatedBy(ruleDatabean.getCreatedBy());
                ruleDocument.setModifiedBy(ruleDatabean.getModifiedBy());
                ruleDocument.setModifiedOn(ruleDatabean.getModifiedOn());
            }
            if (ruleDatabean.getId() != null) {
                ruleDocument.setId(Integer.parseInt(ruleDatabean.getId()));
            }
            ruleDocument.setApply(ruleDatabean.getApply());
            ruleDocument.setFranchiseId(ruleDatabean.getFranchiseId());
            ruleDocument.setIsActive(ruleDatabean.isIsActive());
            ruleDocument.setIsArchive(ruleDatabean.isIsArchive());

            // ----------------------- add logic to generate id of rule ----------------------//
//        if (ruleDatabean.getId() != null) {
//            ObjectId objectId = new ObjectId(ruleDatabean.getId());
//            ruleDocument.setId(objectId);
//        }
            //--------------------------------------------------------------------------------//
            ruleDocument.setRuleName(ruleDatabean.getRuleName());
            ruleDocument.setRemarks(ruleDatabean.getRemarks());
//            System.out.println("Rule DataBean criterias" + ruleDatabean.getCriterias());
            List<RuleCriteriaDataBean> rules = ruleDatabean.getCriterias();
            if (!CollectionUtils.isEmpty(rules)) {
                List<HkRuleCriteriaDocument> ruleCriteriaDocuments = new ArrayList<>();
                for (RuleCriteriaDataBean ruleCriteriaDataBean : rules) {
                    Long fieldId = ruleCriteriaDataBean.getField();
                    if (ruleCriteriaDataBean.getEntity() != null && (ruleCriteriaDataBean.getEntity() < 0 || mapOfFieldIdAndHkFieldEntity.containsKey(fieldId))) {
                        if (ruleCriteriaDataBean.getEntity() > 0) {
                            HkFieldEntity fieldEntity = mapOfFieldIdAndHkFieldEntity.get(fieldId);
                            ruleCriteriaDataBean.setDbFieldName(fieldEntity.getDbFieldName());
                            ruleCriteriaDataBean.setDbFieldType(fieldEntity.getFieldType());

                            // Below code is for pointer handling we are storing pointer Id and pointer component type
                            if (fieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                                String ValidationPattern = fieldEntity.getValidationPattern();
                                String[] validationsArr = ValidationPattern.replace("{", "")
                                        .replace("}", "")
                                        .split(",");
                                String pointerValue = "";
                                for (String validationValue : validationsArr) {
                                    if (validationValue.contains("\"pointer\":")) {
                                        String[] pointerArray = validationValue.split(":");
                                        pointerValue = pointerArray[1];
                                    }

                                    ruleCriteriaDataBean.setPointerId(pointerValue.replace("\"", ""));
                                }
                                // Now fetch the pointerEntity from the map
                                Long pointer = new Long(pointerValue.replace("\"", ""));
                                if (pointerValue != null && !pointerValue.isEmpty()) {
                                    if (mapOfFieldIdAndHkFieldEntity.containsKey(pointer)) {
                                        HkFieldEntity pointerEntity = mapOfFieldIdAndHkFieldEntity.get(pointer);
                                        ruleCriteriaDataBean.setPointerComponentType(pointerEntity.getComponentType());
                                        ruleCriteriaDataBean.setDbFieldType(pointerEntity.getFieldType());
                                    }
                                }
                            }
                            if (fieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                                if (dropListEntityFromSubFieldMap.containsKey(fieldId)) {
                                    HkSubFormFieldEntity subFieldEntity = dropListEntityFromSubFieldMap.get(fieldId);
                                    ruleCriteriaDataBean.setSubentityComponentType(subFieldEntity.getComponentType());
                                }
                            }
                        }
                    }
                    Long entityId = ruleCriteriaDataBean.getEntity();
                    if (featureMap.containsKey(entityId)) {
                        ruleCriteriaDataBean.setEntityName(featureMap.get(entityId));
                    }
//                    Object value = ruleCriteriaDataBean.getValue();
                    if (ruleCriteriaDataBean.getFieldType() != null && StringUtils.hasText(ruleCriteriaDataBean.getFieldType()) && ruleCriteriaDataBean.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        TimeZone tz = TimeZone.getTimeZone("UTC");
                        SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                        Date convertDate = null;
                        try {
                            sdf.setTimeZone(tz);
                            convertDate = sdf.parse(ruleCriteriaDataBean.getValue().toString());
                            ruleCriteriaDataBean.setValue(convertDate);
                        } catch (Exception e) {
                        }

                    }
                    if (ruleCriteriaDataBean.getOperator().equals(HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE_FROM)) {
                        String[] valueArray = ruleCriteriaDataBean.getValue().toString().split(",");
                        ruleCriteriaDataBean.setValue(valueArray);
                    }
                    Gson gson = new Gson();
                    LOGGER.info("before converting to JSON" + ruleCriteriaDataBean);
                    String json = gson.toJson(ruleCriteriaDataBean);
                    LOGGER.info("converted JSON" + ruleCriteriaDataBean);
//                    System.out.println("JSON Rules"+json);
//                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                    ObjectMapper m = new ObjectMapper();
//                    HkRuleCriteriaDocument criteriaDatabean = m.convertValue(map, HkRuleCriteriaDocument.class);
                    HkRuleCriteriaDocument ruleCriteriaDocument = new Gson().fromJson(json, HkRuleCriteriaDocument.class);
                    ruleCriteriaDocuments.add(ruleCriteriaDocument);
                }
                ruleDocument.setCriterias(ruleCriteriaDocuments);
            }
        }

        return ruleDocument;
    }

    public RuleDatabean convertRuleDocumentToRuleDatabean(RuleDatabean ruleDatabean, HkRuleDocument ruleDocument) {
        if (ruleDocument == null) {
            ruleDocument = new HkRuleDocument();
        }
        if (ruleDatabean != null) {
            ruleDatabean.setApply(ruleDocument.getApply());
            ruleDatabean.setCreateOn(ruleDocument.getCreateOn());
            ruleDatabean.setCreatedBy(ruleDocument.getCreatedBy());
            ruleDatabean.setFranchiseId(ruleDocument.getFranchiseId());
            ruleDatabean.setId(String.valueOf(ruleDocument.getId()));
            ruleDatabean.setIsActive(ruleDocument.isIsActive());
            ruleDatabean.setIsArchive(ruleDocument.isIsArchive());
            ruleDatabean.setModifiedBy(ruleDocument.getModifiedBy());
            ruleDatabean.setModifiedOn(ruleDocument.getModifiedOn());
            ruleDatabean.setRuleName(ruleDocument.getRuleName());
            ruleDatabean.setRemarks(ruleDocument.getRemarks());
            List<HkRuleCriteriaDocument> rules = ruleDocument.getCriterias();
            if (!CollectionUtils.isEmpty(rules)) {
                List<RuleCriteriaDataBean> dataBeans = new ArrayList<>();
                for (HkRuleCriteriaDocument ruleClass : rules) {
                    Gson gson = new Gson();
                    String json = gson.toJson(ruleClass);
//                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                    ObjectMapper m = new ObjectMapper();
                    RuleCriteriaDataBean ruleCriteriaDataBean;
                    ruleCriteriaDataBean = new Gson().fromJson(json, RuleCriteriaDataBean.class);
                    dataBeans.add(ruleCriteriaDataBean);
                }
                ruleDatabean.setCriterias(dataBeans);
            }
        }

        return ruleDatabean;
    }

    public String updateRule(RuleSetDataBean databean, Boolean isDelete) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            databean.setLastModifiedBy(loginDataBean.getId());
            databean.setLastModifiedOn(new Date());
            LOGGER.info("databean.getId()" + databean.getId());
            HkRuleSetDocument document = new HkRuleSetDocument();
            if (databean.getId() != null) {
                document = ruleService.retrieveRuleSetById(new ObjectId(databean.getId()));
            }
            long createdBy = document.getCreatedBy();
            Date createdOn = document.getCreatedOn();
//            Boolean isActive = document.getIsActive();
            HkRuleSetDocument rulesetToUpdate = convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.UPDATE_OPERATION, databean, document);
            rulesetToUpdate.setCreatedBy(createdBy);
            rulesetToUpdate.setCreatedOn(createdOn);
            ruleService.saveOrUpdateRuleSet(rulesetToUpdate);
            response = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
        }
        return response;
    }

    public String deleteRuleSet(ObjectId id) {
        String response = HkSystemConstantUtil.FAILURE;
        try {
            ruleService.deleteRuleSet(id);
            response = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
        }
        return response;
    }

    public List<RuleSetDataBean> retrieveAllRules(Long companyId, Boolean isActive) {
        List<RuleSetDataBean> ruleDatabeans = new ArrayList<>();
        Map<ObjectId, HkRuleSetDocument> retrieveAllRule = ruleService.retrieveAllRuleSet(companyId, isActive);
        if (!CollectionUtils.isEmpty(retrieveAllRule)) {
            for (Map.Entry<ObjectId, HkRuleSetDocument> entry : retrieveAllRule.entrySet()) {
                HkRuleSetDocument hkRuleSetDocument = entry.getValue();
                RuleSetDataBean ruleSetDb = convertRuleSetDocumentToRuleSetDataBean(hkRuleSetDocument, new RuleSetDataBean());
                ruleDatabeans.add(ruleSetDb);
            }
        }
        return ruleDatabeans;
    }

    public void executeRule(ObjectId id, Long companyId) {

        Map<Long, String> retrieveEntitys = retrieveEntitys(companyId);
        Set<Long> keySet = retrieveEntitys.keySet();
        List<Long> ids = new ArrayList<>();
        ids.addAll(keySet);

        List<Long> companyIds = new ArrayList<>();
        companyIds.add(companyId);

        Map<Long, List<Map<String, Object>>> entityFieldMap = new HashMap<>();

        Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> retrieveCompanyFeatureSectionsMap = hkFieldService.retrieveCompanyFeatureSectionsMap(ids, companyIds);
        if (!CollectionUtils.isEmpty(retrieveCompanyFeatureSectionsMap)) {
            Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> get = retrieveCompanyFeatureSectionsMap.get(companyId);
            for (Map.Entry<Long, Map<HkSectionEntity, List<HkFieldEntity>>> entry : get.entrySet()) {
                Long long1 = entry.getKey(); // feature id
                Map<HkSectionEntity, List<HkFieldEntity>> sectionFieldMap = entry.getValue();
                List<HkFieldEntity> get1 = sectionFieldMap.get(null);
                List<Map<String, Object>> list = new ArrayList<>();
                if (!CollectionUtils.isEmpty(get1)) {
                    for (HkFieldEntity hkFieldEntity : get1) {
                        Map<String, Object> fieldValuesMap = new HashMap<>();
                        fieldValuesMap.put("id", hkFieldEntity.getId());
                        fieldValuesMap.put("fieldLabel", hkFieldEntity.getFieldLabel());
                        fieldValuesMap.put("componentType", hkFieldEntity.getComponentType());
                        List<String> asList = new ArrayList<>();
                        if (hkFieldEntity.getFieldValues() != null) {
                            String[] split = hkFieldEntity.getFieldValues().split(",");
                            asList = Arrays.asList(split);
                        }
                        fieldValuesMap.put("fieldValues", asList);
                        list.add(fieldValuesMap);
                    }
                }
                entityFieldMap.put(long1, list);
            }
        }
        Map<String, Object> datapass = new HashMap<>();
        datapass.put("37", "xyz");
        ruleService.executeRuleSet(id, retrieveEntitys, entityFieldMap, datapass);

        Map<String, Object> datapass1 = new HashMap<>();
        datapass1.put("37", "xyznot");
        ruleService.executeRuleSet(id, retrieveEntitys, entityFieldMap, datapass1);

    }

    public RuleSetDataBean retrieveRuleById(ObjectId id, Long companyId) {
        HkRuleSetDocument ruleSetDocument = ruleService.retrieveRuleSetById(id);
        RuleSetDataBean ruleSetDataBean = new RuleSetDataBean();
        ruleSetDataBean = convertRuleSetDocumentToRuleSetDataBean(ruleSetDocument, ruleSetDataBean);
        return ruleSetDataBean;
    }

    public Map<String, List<SelectItem>> retrieveOperatorsByType() {
        Map<String, List<SelectItem>> operatorTypeMap = new HashMap<>();

        operatorTypeMap.put("Numeric", getIntegerOperators());
        operatorTypeMap.put("String", getStringOperators());
        operatorTypeMap.put("Date", getDateOperators());
        operatorTypeMap.put("Date range", getDateRangeOperators());
        operatorTypeMap.put("Boolean", getBooleanOperators());
        operatorTypeMap.put("Image", getImageOperators());
        operatorTypeMap.put("Long", getLongOperators());
        return operatorTypeMap;
    }

    public List<SelectItem> getIntegerOperators() {
        List<SelectItem> integerOperators = new ArrayList<>();
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_EQUAL_TO, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN_EQUAL_TO, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.IN_BETWEEN, 2));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_IN_BETWEEN, 2));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_NO_VALUE, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE, 1));
        integerOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE_FROM, 1));
        return integerOperators;
    }

    public List<SelectItem> getBooleanOperators() {
        List<SelectItem> booleanOperators = new LinkedList<>();
        booleanOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        booleanOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));

        return booleanOperators;
    }

    public List<SelectItem> getImageOperators() {
        List<SelectItem> imageOperators = new LinkedList<>();
        imageOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_NO_VALUE, 1));
        imageOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE, 1));

        return imageOperators;
    }

    public List<SelectItem> getStringOperators() {
        List<SelectItem> stringOperators = new LinkedList<>();
        stringOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        stringOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));
        stringOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_NO_VALUE, 1));
        stringOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE, 1));
        stringOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE_FROM, 1));
        return stringOperators;
    }

    public List<SelectItem> getLongOperators() {
        List<SelectItem> longOperators = new LinkedList<>();
        longOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        longOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));
        longOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_NO_VALUE, 1));
        longOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE, 1));
        return longOperators;
    }

    public List<SelectItem> getDateOperators() {
        List<SelectItem> dateOperators = new LinkedList<>();
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_EQUAL_TO, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN_EQUAL_TO, 1));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.IN_BETWEEN, 2));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_IN_BETWEEN, 2));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.IS_BEFORE, 2));
        dateOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.IS_AFTER, 2));
        return dateOperators;
    }

    public List<SelectItem> getDateRangeOperators() {
        List<SelectItem> dateRangeOperators = new LinkedList<>();
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.EQUALS_TO, 1));
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.NOT_EQUAL_TO, 1));
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_, 1));
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.LESS_THAN_EQUAL_TO, 1));
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN, 1));
        dateRangeOperators.add(new SelectItem("", HkSystemConstantUtil.RULE_OPERATOR.GREATER_THAN_EQUAL_TO, 1));
        return dateRangeOperators;
    }

    public HkRuleSetDocument convertRuleSetDatabeanToRuleSetDocument(String operation, RuleSetDataBean ruleSetDatabean, HkRuleSetDocument ruleSetDocument) {
        if (ruleSetDocument == null) {
            ruleSetDocument = new HkRuleSetDocument();
        }
        if (ruleSetDatabean != null) {
            LOGGER.info("ruleSetDatabean.getFranchise()" + ruleSetDatabean.getFranchise());
            ruleSetDocument.setFranchise(ruleSetDatabean.getFranchise());
            LOGGER.info("ruleSetDatabean.getIsActive()" + ruleSetDatabean.getIsActive());
            ruleSetDocument.setIsActive(ruleSetDatabean.getIsActive());
            LOGGER.info("ruleSetDatabean.getIsArchive()" + ruleSetDatabean.getIsArchive());
            ruleSetDocument.setIsArchive(ruleSetDatabean.getIsArchive());
            LOGGER.info("ruleSetDatabean.getCreatedOn()" + ruleSetDatabean.getCreatedOn());
            ruleSetDocument.setCreatedOn(ruleSetDatabean.getCreatedOn());
            LOGGER.info("ruleSetDatabean.getCreatedBy()" + ruleSetDatabean.getCreatedBy());
            ruleSetDocument.setCreatedBy(ruleSetDatabean.getCreatedBy());
            ruleSetDocument.setLastModifiedOn(new Date());
            ruleSetDocument.setLastModifiedBy(loginDataBean.getId());

            if (!CollectionUtils.isEmpty(ruleSetDatabean.getRules())) {
                List<HkRuleDocument> ruleDocuments = new LinkedList<>();
                for (RuleDatabean ruleDatabean : ruleSetDatabean.getRules()) {
                    HkRuleDocument hkRuleDocument = this.convertRuleDatabeanToRuleDocument(operation, ruleDatabean, new HkRuleDocument());
                    ruleDocuments.add(hkRuleDocument);
                }
                ruleSetDocument.setRules(ruleDocuments);
            }
        }

        return ruleSetDocument;
    }

    public RuleSetDataBean convertRuleSetDocumentToRuleSetDataBean(HkRuleSetDocument ruleSetDocument, RuleSetDataBean ruleSetDatabean) {
        if (ruleSetDatabean == null) {
            ruleSetDatabean = new RuleSetDataBean();
        }
        if (ruleSetDocument != null) {

            if (ruleSetDocument.getId() != null) {
                ruleSetDatabean.setId(ruleSetDocument.getId().toString());
            }
            ruleSetDatabean.setFranchise(ruleSetDocument.getFranchise());
            ruleSetDatabean.setIsActive(ruleSetDocument.getIsActive());
            ruleSetDatabean.setIsArchive(ruleSetDocument.getIsArchive());
            ruleSetDatabean.setCreatedOn(ruleSetDocument.getCreatedOn());
            ruleSetDatabean.setCreatedBy(ruleSetDocument.getCreatedBy());
            ruleSetDatabean.setLastModifiedOn(ruleSetDocument.getLastModifiedOn());
            ruleSetDatabean.setLastModifiedBy(ruleSetDocument.getLastModifiedBy());

            if (!CollectionUtils.isEmpty(ruleSetDocument.getRules())) {
                List<RuleDatabean> ruleDatabeans = new LinkedList<>();
                for (HkRuleDocument hkRuleDocument : ruleSetDocument.getRules()) {
                    RuleDatabean ruleDatabean = convertRuleDocumentToRuleDatabean(new RuleDatabean(), hkRuleDocument);
                    ruleDatabeans.add(ruleDatabean);
                }
                ruleSetDatabean.setRules(ruleDatabeans);
            }
        }

        return ruleSetDatabean;
    }

    public Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap() {
        Map<Long, HkSubFormFieldEntity> dropListEntityMap = null;
        dropListEntityMap = new HashMap<>();
        List<HkSubFormFieldEntity> listOfSubEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(null);
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            for (HkSubFormFieldEntity listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    dropListEntityMap.put(listOfSubEntity.getParentField().getId(), listOfSubEntity);
                }
            }
        }
        return dropListEntityMap;
    }

    public List<SelectItem> createDropDownListForSubEntity(Long customFieldId) {
        List<SelectItem> selectItemList = null;
        HkFieldEntity fieldEntity = hkFieldService.retrieveCustomFieldByFieldId(customFieldId);
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
        Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap = this.dropListEntityFromSubFieldMap();
        List<HkSubFormValueDocument> subFormDocuments = customFieldSevice.retrieveSubFormValueByInstance(customFieldId, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(subFormDocuments)) {
            selectItemList = new ArrayList<>();
            for (HkSubFormValueDocument subFormDocument : subFormDocuments) {
                Map<String, Object> fieldValueMap = subFormDocument.getFieldValue().toMap();
                for (Map.Entry<String, Object> entrySet : fieldValueMap.entrySet()) {
                    if (SubEntityDropField.containsKey(entrySet.getKey())) {
                        SelectItem selectItem = new SelectItem(subFormDocument.getId().toString(), entrySet.getValue().toString());
                        if (dropListEntityFromSubFieldMap.containsKey(customFieldId)) {
                            String subFieldDropListComponentType = dropListEntityFromSubFieldMap.get(customFieldId).getComponentType();
                            selectItem.setDescription(subFieldDropListComponentType);
                        }
                        selectItemList.add(selectItem);
                        dropDownValuesForSubEntity.put(subFormDocument.getId(), entrySet.getValue().toString());
                    }
                }
            }

        }
        return selectItemList;
    }

    public List<SelectItem> retrieveStaticStatusList(String dbFieldName) {
        Map<String, String> staticStatus = null;
        if (dbFieldName.equals("lot_status") || dbFieldName.equals("packet_status")) {
            staticStatus = new HashMap<>(HkSystemConstantUtil.lotStatusMap);
        } else if (dbFieldName.equals("status_of_plan")) {
            staticStatus = new HashMap<>(HkSystemConstantUtil.PLAN_STATUS);
        } else if (dbFieldName.equals("parcel_status") || dbFieldName.equals("invoice_status")) {
            staticStatus = new HashMap<>(HkSystemConstantUtil.invoiceAndParceStatusMap);
        } else if (dbFieldName.equals("issue_status")) {
            staticStatus = new HashMap<>(HkSystemConstantUtil.ISSUE_STATUS);
        } else if (dbFieldName.equals("type_of_plan")) {
            staticStatus = new HashMap<>(HkSystemConstantUtil.typeOfPlanMap);
        }
        List<SelectItem> selectItemList = null;
        if (staticStatus != null && !staticStatus.isEmpty()) {
            selectItemList = new ArrayList<>();
            for (Map.Entry<String, String> lot : staticStatus.entrySet()) {
                SelectItem selectItem = new SelectItem(lot.getKey(), lot.getValue());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

}
