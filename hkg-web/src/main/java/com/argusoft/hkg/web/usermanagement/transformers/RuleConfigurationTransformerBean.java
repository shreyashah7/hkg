/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkRuleConfigurationService;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleCriteriaDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author gautam
 */
@Service
public class RuleConfigurationTransformerBean {

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    HkRuleConfigurationService hkRuleConfigurationService;

    @Autowired
    HkFieldService hkFieldService;

    @Autowired
    HkConfigurationService hkConfigurationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigurationTransformerBean.class);

    public Long saveRuleConfiguration(RuleDatabean ruleDatabean) {
        if (ruleDatabean != null) {
            HkCriteriaSetDocument ruleDocument = null;
            LOGGER.info("ruleDatabean.getRuleNumber()" + ruleDatabean.getRuleNumber());
            ruleDocument = retrieveRuleDocumentByRuleNumber(ruleDatabean.getRuleNumber());
            if (ruleDocument == null) {
                LOGGER.info("ruleDocument is null");
            }
            ruleDocument = convertRuleDataBeanToEntity(ruleDatabean, ruleDocument);
            hkRuleConfigurationService.saveRuleDocument(ruleDocument);
            return ruleDocument.getRuleNumber();
        }
        return null;
    }

    public HkCriteriaSetDocument retrieveRuleDocumentByRuleNumber(Long ruleNumber) {
        HkCriteriaSetDocument ruleDocument = null;
        if (ruleNumber != null) {
            ruleDocument = hkRuleConfigurationService.retrieveRuleDocumentByRuleNumber(ruleNumber);
        }
        return ruleDocument;
    }

    public RuleDatabean retrieveRuleConfigurationByRuleNumber(Long ruleNumber) {
        RuleDatabean ruleDatabean = null;
        HkCriteriaSetDocument ruleDocument = retrieveRuleDocumentByRuleNumber(ruleNumber);
        if (ruleDocument != null) {
            ruleDatabean = convertRuleEntityToDataBean(ruleDocument, ruleDatabean);
            if (ruleDocument.getCategory().equals(HkSystemConstantUtil.RuleConfiguration.RULE_CATEGORY)) {
                List<HkCriteriaSetDocument> exceptionDocuments = hkRuleConfigurationService.retrieveExceptionRuleDocuments(ruleNumber);
                if (!CollectionUtils.isEmpty(exceptionDocuments)) {
                    List<SelectItem> exceptionRules = new ArrayList<>();
                    for (HkCriteriaSetDocument exceptionDocument : exceptionDocuments) {
                        exceptionRules.add(new SelectItem(exceptionDocument.getRuleNumber(), exceptionDocument.getRuleNumber() + "-" + exceptionDocument.getRuleName()));
                    }
                    ruleDatabean.setExceptionList(exceptionRules);
                }
            }
        }
        return ruleDatabean;
    }

    public List<RuleDatabean> retrieveAllRules() {
        List<RuleDatabean> ruleDataBeans = null;
        List<HkCriteriaSetDocument> ruleDocuments = hkRuleConfigurationService.retrieveAllRules(hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(ruleDocuments)) {
            ruleDataBeans = new ArrayList<>();
            for (HkCriteriaSetDocument ruleDocument : ruleDocuments) {
                ruleDataBeans.add(convertRuleEntityToDataBean(ruleDocument, null));
            }
        }
        return ruleDataBeans;
    }

    public Map<String, List<RuleDatabean>> retrieveAllRulesMap() {
        Map<String, List<RuleDatabean>> result = new HashMap<>();
        List<HkCriteriaSetDocument> ruleDocuments = hkRuleConfigurationService.retrieveAllRules(hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(ruleDocuments)) {
            LOGGER.info("ruleDocuments.size() " + ruleDocuments.size());
            for (HkCriteriaSetDocument ruleDocument : ruleDocuments) {
                LOGGER.info("ruleDocument id " + ruleDocument.getId().toString());
                if (!result.containsKey(ruleDocument.getCategory())) {
                    result.put(ruleDocument.getCategory(), new ArrayList<>());
                }
                RuleDatabean ruleDataBean = new RuleDatabean();
                ruleDataBean.setId(ruleDocument.getRuleNumber().toString());
                ruleDataBean.setDisplayName(ruleDocument.getRuleNumber() + "-" + ruleDocument.getRuleName());
                ruleDataBean.setRuleName(ruleDocument.getRuleName());
                ruleDataBean.setType(ruleDocument.getType());
                result.get(ruleDocument.getCategory()).add(ruleDataBean);
            }
        }
        LOGGER.info("result" + result);
        return result;
    }

    public RuleDatabean convertRuleEntityToDataBean(HkCriteriaSetDocument ruleDocument, RuleDatabean ruleDatabean) {
        if (ruleDatabean == null) {
            ruleDatabean = new RuleDatabean();
        }
        if (ruleDocument != null) {
            ruleDatabean.setId(ruleDocument.getId().toString());
            ruleDatabean.setRuleNumber(ruleDocument.getRuleNumber());
            ruleDatabean.setApply(ruleDocument.getApply());
            ruleDatabean.setCategory(ruleDocument.getCategory());
            ruleDatabean.setColorCode(ruleDocument.getColorCode());
            ruleDatabean.setDescription(ruleDocument.getDescription());
            ruleDatabean.setFeatures(ruleDocument.getFeatures());
            ruleDatabean.setFieldsToBeApplied(ruleDocument.getFieldsToBeApplied());
            ruleDatabean.setFromDate(ruleDocument.getFromDate());
            ruleDatabean.setRemarks(ruleDocument.getRemarks());
            ruleDatabean.setRuleName(ruleDocument.getRuleName());
            ruleDatabean.setSkipRules(ruleDocument.getSkipRules());
            ruleDatabean.setStatus(ruleDocument.getStatus());
            ruleDatabean.setToDate(ruleDocument.getToDate());
            ruleDatabean.setTooltipMsg(ruleDocument.getTooltipMsg());
            ruleDatabean.setType(ruleDocument.getType());
            ruleDatabean.setValidationMessage(ruleDocument.getValidationMessage());

            if (!CollectionUtils.isEmpty(ruleDocument.getCriterias())) {
                List<RuleCriteriaDataBean> criteriaDataBeanList = new ArrayList<>();
                for (HkRuleCriteriaDocument criteriaDocument : ruleDocument.getCriterias()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(criteriaDocument);
//                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                    ObjectMapper m = new ObjectMapper();
                    RuleCriteriaDataBean ruleCriteriaDataBean;
                    ruleCriteriaDataBean = new Gson().fromJson(json, RuleCriteriaDataBean.class);
                    criteriaDataBeanList.add(ruleCriteriaDataBean);
                }
                ruleDatabean.setCriterias(criteriaDataBeanList);
            }
        }
        return ruleDatabean;
    }

    public HkCriteriaSetDocument convertRuleDataBeanToEntity(RuleDatabean ruleDatabean, HkCriteriaSetDocument ruleDocument) {
        if (ruleDocument == null) {
            ruleDocument = new HkCriteriaSetDocument();
        }
        if (ruleDatabean != null) {
            Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
            Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap = dropListEntityFromSubFieldMap();
            Map<Long, String> featureMap = userManagementServiceWrapper.retrieveFeatureMap(true);
            if (ruleDatabean.getRuleNumber() != null) {
                //Update..id and rule number persists as in incoming model
            } else {
                Long lastRuleNumber = retrieveRuleNumberSequence();
                ruleDocument.setRuleNumber(lastRuleNumber + 1);
                ruleDocument.setCreatedBy(hkLoginDataBean.getId());
                ruleDocument.setCreateOn(new Date());
            }
            LOGGER.info("ruleDatabean.getApply()" + ruleDatabean.getApply());
            ruleDocument.setApply(ruleDatabean.getApply());
            LOGGER.info("ruleDatabean.getCategory()" + ruleDatabean.getCategory());
            ruleDocument.setCategory(ruleDatabean.getCategory());
            LOGGER.info("ruleDatabean.getColorCode()" + ruleDatabean.getColorCode());
            ruleDocument.setColorCode(ruleDatabean.getColorCode());
            LOGGER.info("ruleDatabean.getDescription()" + ruleDatabean.getDescription());
            ruleDocument.setDescription(ruleDatabean.getDescription());
            LOGGER.info("ruleDatabean.getFeatures()" + ruleDatabean.getFeatures());
            ruleDocument.setFeatures(ruleDatabean.getFeatures());
            LOGGER.info("ruleDatabean.getFieldsToBeApplied()" + ruleDatabean.getFieldsToBeApplied());
            ruleDocument.setFieldsToBeApplied(ruleDatabean.getFieldsToBeApplied());
            ruleDocument.setFranchiseId(hkLoginDataBean.getCompanyId());
            LOGGER.info("ruleDatabean.getFromDate()" + ruleDatabean.getFromDate());
            ruleDocument.setFromDate(ruleDatabean.getFromDate());
            LOGGER.info("ruleDatabean.getRemarks()" + ruleDatabean.getRemarks());
            ruleDocument.setRemarks(ruleDatabean.getRemarks());
            LOGGER.info("ruleDatabean.getRuleName()" + ruleDatabean.getRuleName());
            ruleDocument.setRuleName(ruleDatabean.getRuleName());
            LOGGER.info("ruleDatabean.getSkipRules()" + ruleDatabean.getSkipRules());
            ruleDocument.setSkipRules(ruleDatabean.getSkipRules());
            LOGGER.info("ruleDatabean.getStatus()" + ruleDatabean.getStatus());
            ruleDocument.setStatus(ruleDatabean.getStatus());
            LOGGER.info("ruleDatabean.getToDate()" + ruleDatabean.getToDate());
            ruleDocument.setToDate(ruleDatabean.getToDate());
            LOGGER.info("ruleDatabean.getTooltipMsg()" + ruleDatabean.getTooltipMsg());
            ruleDocument.setTooltipMsg(ruleDatabean.getTooltipMsg());
            LOGGER.info("ruleDatabean.getType()" + ruleDatabean.getType());
            ruleDocument.setType(ruleDatabean.getType());
            LOGGER.info("ruleDatabean.getValidationMessage()" + ruleDatabean.getValidationMessage());
            ruleDocument.setValidationMessage(ruleDatabean.getValidationMessage());
            LOGGER.info("ruleDatabean.getStatus()" + ruleDatabean.getStatus());
            ruleDocument.setIsArchive(ruleDatabean.getStatus().equals(HkSystemConstantUtil.INACTIVE) ? Boolean.TRUE : Boolean.FALSE);
            ruleDocument.setModifiedBy(hkLoginDataBean.getId());
            ruleDocument.setModifiedOn(new Date());
            ruleDocument.setIsActive(true);
            if (!CollectionUtils.isEmpty(ruleDatabean.getCriterias())) {
                List<HkRuleCriteriaDocument> criteriaDocumentList = new ArrayList<>();
                boolean fieldsOnly = false;
                for (RuleCriteriaDataBean criteria : ruleDatabean.getCriterias()) {
                    if (criteria.getEntity() < 0) {
                        fieldsOnly = true;
                    }
                }
                for (RuleCriteriaDataBean criteriaDataBean : ruleDatabean.getCriterias()) {
                    Long fieldId = criteriaDataBean.getField();
                    if (criteriaDataBean.getEntity() != null && (criteriaDataBean.getEntity() < 0 || (fieldId != null && mapOfFieldIdAndHkFieldEntity.containsKey(fieldId)))) {
                        if (criteriaDataBean.getEntity() > 0 && !fieldsOnly) {
                            HkFieldEntity fieldEntity = mapOfFieldIdAndHkFieldEntity.get(fieldId);
                            criteriaDataBean.setDbFieldName(fieldEntity.getDbFieldName());
                            criteriaDataBean.setDbFieldType(fieldEntity.getFieldType());
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

                                    criteriaDataBean.setPointerId(pointerValue.replace("\"", ""));
                                }
                                // Now fetch the pointerEntity from the map
                                Long pointer = new Long(pointerValue.replace("\"", ""));
                                if (pointerValue != null && !pointerValue.isEmpty()) {
                                    if (mapOfFieldIdAndHkFieldEntity.containsKey(pointer)) {
                                        HkFieldEntity pointerEntity = mapOfFieldIdAndHkFieldEntity.get(pointer);
                                        criteriaDataBean.setPointerComponentType(pointerEntity.getComponentType());
                                        criteriaDataBean.setDbFieldType(pointerEntity.getFieldType());
                                    }
                                }
                            }
                            if (fieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                                if (dropListEntityFromSubFieldMap.containsKey(fieldId)) {
                                    HkSubFormFieldEntity subFieldEntity = dropListEntityFromSubFieldMap.get(fieldId);
                                    criteriaDataBean.setSubentityComponentType(subFieldEntity.getComponentType());
                                }
                            }
                        }
                    }
                    Long entityId = criteriaDataBean.getEntity();
                    if (featureMap.containsKey(entityId)) {

                        criteriaDataBean.setEntityName(featureMap.get(entityId));
                    }
//                    Object value = criteriaDataBean.getValue();
                    if (criteriaDataBean.getFieldType() != null && criteriaDataBean.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        TimeZone tz = TimeZone.getTimeZone("UTC");
                        SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                        Date convertDate = null;
                        try {
                            sdf.setTimeZone(tz);
                            convertDate = sdf.parse(criteriaDataBean.getValue().toString());
                            criteriaDataBean.setValue(convertDate);
                        } catch (Exception e) {
                        }

                    }
                    if (criteriaDataBean.getOperator().equals(HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE_FROM)) {
                        String[] valueArray = criteriaDataBean.getValue().toString().split(",");
                        criteriaDataBean.setValue(valueArray);
                    }
                    Gson gson = new Gson();
                    LOGGER.info("before converting to JSON" + criteriaDataBean);
                    String json = gson.toJson(criteriaDataBean);
                    LOGGER.info("converted JSON" + criteriaDataBean);
                    HkRuleCriteriaDocument criteriaDocument = new Gson().fromJson(json, HkRuleCriteriaDocument.class);
                    criteriaDocumentList.add(criteriaDocument);
                }
                ruleDocument.setCriterias(criteriaDocumentList);
            }
        }
        return ruleDocument;
    }

    public Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap() {
        Map<Long, HkSubFormFieldEntity> dropListEntityMap = new HashMap<>();
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

    public Long retrieveRuleNumberSequence() {
        return hkConfigurationService.retrieveMaxCountOfRule(hkLoginDataBean.getCompanyId());
    }

    public List<SelectItem> retrieveDiamondFeatureList() {
        List<UMFeature> features = userManagementServiceWrapper.retrieveAllDiamondFeatures(hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(features)) {
            List<SelectItem> featureList = new ArrayList<>();
            for (UMFeature feature : features) {
                featureList.add(new SelectItem(feature.getId(), feature.getMenuLabel()));
            }
            return featureList;
        }
        return null;
    }

    public List<RuleDatabean> searchRuleDocumentsByName(String name) {
        System.out.println("name" + name);
        List<RuleDatabean> results = new ArrayList<>();
        if (!StringUtils.isEmpty(name)) {
            List<HkCriteriaSetDocument> searchedDocuments = hkRuleConfigurationService.searchRuleDocumentsByName(name);
            if (!CollectionUtils.isEmpty(searchedDocuments)) {
                for (HkCriteriaSetDocument ruleDocument : searchedDocuments) {
                    RuleDatabean ruleDatabean = new RuleDatabean();
                    ruleDatabean.setId(ruleDocument.getRuleNumber().toString());
                    ruleDatabean.setRuleName(ruleDocument.getRuleName());
                    String category = ruleDocument.getCategory().equals(HkSystemConstantUtil.RuleConfiguration.EXCEPTION_CATEGORY) ? "Exception" : "General";
                    ruleDatabean.setCategory(category);
                    ruleDatabean.setDisplayName(ruleDocument.getRuleName() + ", " + ruleDocument.getRuleNumber() + ", " + category);
                    results.add(ruleDatabean);
                }
            }
        }
        System.out.println("results..." + results);
        return results;
    }

    public void removeRule(Long ruleNumber) {
        HkCriteriaSetDocument criteriaSetDocument = this.retrieveRuleDocumentByRuleNumber(ruleNumber);
        criteriaSetDocument.setIsArchive(Boolean.TRUE);
        criteriaSetDocument.setStatus(HkSystemConstantUtil.INACTIVE);
        hkRuleConfigurationService.saveRuleDocument(criteriaSetDocument);
    }

//    public Map<Long, List<Long>> retrieveScreenRulesAssociatedWithField(Long featureId, List<Long> fieldIds){
//        Map<Long, List<Long>> result = new HashMap<>();
//        Map<Long, List<HkCriteriaSetDocument>> rules = hkRuleConfigurationService.retrieveScreenRulesAssociatedWithField(featureId, fieldIds);
//        if(!CollectionUtils.isEmpty(rules)){
//            for(Map.Entry<Long, List<HkCriteriaSetDocument>> entry : rules.entrySet()){
//                Set<Long> ruleNumbers = new HashSet<>();
//                for(HkCriteriaSetDocument ruleDoc : entry.getValue()){
//                    ruleNumbers.add(ruleDoc.getRuleNumber());
//                }
//                result.put(entry.getKey(), new ArrayList<>(ruleNumbers));
//            }
//        }
//        return result;
//    }
//    
//    public boolean executeRuleSet(List<Long> ruleNumbers, Map<String, Object> dataMap, Long featureId, String entityType, ObjectId entityId){
//        return hkRuleConfigurationService.executeRuleSet(dataMap, ruleNumbers, featureId, entityId, entityType);
//    }
}
