/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.web.center.stock.databeans.RoughPurchaseDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shreya
 */
@Service
public class RoughPurchaseTransformer {

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkStockService stockService;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;

    @Autowired
    ApplicationUtil applicationUtil;

    public SelectItem saveRoughPurchase(RoughPurchaseDataBean roughPurchaseDataBean) {
        SelectItem selectItem = null;
        Map<String, Object> roughPurchaseCustomMap = roughPurchaseDataBean.getRoughPurchaseCustom();
        Map<String, String> roughPurchaseDbTypeMap = roughPurchaseDataBean.getRoughPurchaseDbType();
        Iterator<Map.Entry<String, Object>> entries = roughPurchaseCustomMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!(value != null && value.toString() != null && value.toString().length() > 0)) {
                entries.remove();
            }
        }
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roughPurchaseDbTypeMap)) {
            for (Map.Entry<String, String> entrySet : roughPurchaseDbTypeMap.entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        BasicBSONObject fieldValues = null;
        Map<String, String> uiFieldMap = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        Map<String, String> autogeneratedLabelMap = new HashMap<>();
        String year = new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString();
        autogeneratedLabelMap.put("beforeLabel", year + "-");
        if (!CollectionUtils.isEmpty(roughPurchaseCustomMap) && !CollectionUtils.isEmpty(roughPurchaseDbTypeMap)) {
            fieldValues = customFieldService.makeBSONObject(roughPurchaseCustomMap, roughPurchaseDbTypeMap, uiFieldMap, HkSystemConstantUtil.Feature.ROUGH_PURCHASE, loginDataBean.getCompanyId(), null, Boolean.FALSE, null, autogeneratedLabelMap);
            String roughPurchaseId = stockService.savePurchase(fieldValues, loginDataBean.getCompanyId(), loginDataBean.getCompanyId(), roughPurchaseDataBean.getSequenceNumber());
            if (!CollectionUtils.isEmpty(roughPurchaseDataBean.getUiFieldMap())) {
                roughPurchaseDataBean.getUiFieldMap().put("roughPurchaseObjectId", Arrays.asList(roughPurchaseId));
                selectItem = this.retrieveRoughPurchaseById(roughPurchaseDataBean.getUiFieldMap(), roughPurchaseDataBean.getRuleConfigMap());
            }
        }
        return selectItem;
    }

    public SelectItem updateRoughPurchase(RoughPurchaseDataBean roughPurchaseDataBean) {
        SelectItem selectItem = null;
        Map<String, Object> roughPurchaseCustomMap = roughPurchaseDataBean.getRoughPurchaseCustom();
        Map<String, String> roughPurchaseDbTypeMap = roughPurchaseDataBean.getRoughPurchaseDbType();
        Map<String, String> autogeneratedLabelMap = new HashMap<>();
        autogeneratedLabelMap.put("beforeLabel", roughPurchaseDataBean.getYear().toString() + "-");
        Boolean result = stockService.updatePurchase(new ObjectId(roughPurchaseDataBean.getId()), roughPurchaseCustomMap, roughPurchaseDbTypeMap, loginDataBean.getCompanyId(), loginDataBean.getId(), roughPurchaseDataBean.getSequenceNumber(), autogeneratedLabelMap);
        if (result) {
            if (!CollectionUtils.isEmpty(roughPurchaseDataBean.getUiFieldMap())) {
                roughPurchaseDataBean.getUiFieldMap().put("roughPurchaseObjectId", Arrays.asList(roughPurchaseDataBean.getId()));
                selectItem = this.retrieveRoughPurchaseById(roughPurchaseDataBean.getUiFieldMap(), roughPurchaseDataBean.getRuleConfigMap());
            }
        }
        return selectItem;
    }

    public Boolean deleteRoughPurchase(String roughPurchaseId) {
        Boolean response = Boolean.FALSE;
        if (roughPurchaseId != null) {
            response = stockService.deletePurchase(new ObjectId(roughPurchaseId), loginDataBean.getId());
        }
        return response;
    }

    public List<SelectItem> searchRoughPurchase(RoughPurchaseDataBean roughPurchaseDataBean) throws ParseException {
        Map<String, Object> roughPurchaseCustomMap = roughPurchaseDataBean.getRoughPurchaseCustom();
        if (!CollectionUtils.isEmpty(roughPurchaseCustomMap) && roughPurchaseDataBean.getSearchOnParameter()) {
            Iterator<Map.Entry<String, Object>> iter = roughPurchaseCustomMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                    if (value instanceof String && !StringUtils.isEmpty(value)) {
                        List<String> items = Arrays.asList(value.toString().split(","));
                        roughPurchaseCustomMap.put(key, items);
                    } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                        List<String> items = Arrays.asList(value.toString().split(","));
                        roughPurchaseCustomMap.put(key, items);
                    } else {
                        iter.remove();
                    }
                }
            }
        }
        List<ObjectId> roughPurchaseIds = null;
        if (roughPurchaseDataBean.getId() != null) {
            roughPurchaseIds = new ArrayList<>();
            roughPurchaseIds.add(new ObjectId(roughPurchaseDataBean.getId()));
        }
        List<HkPurchaseDocument> hkPurchaseDocuments = stockService.retrievePurchases(roughPurchaseCustomMap, roughPurchaseIds, loginDataBean.getCompanyId(), Boolean.FALSE, roughPurchaseDataBean.getOffset(), roughPurchaseDataBean.getLimit());
        List<SelectItem> selectItems = this.convertModelToUiData(hkPurchaseDocuments, roughPurchaseDataBean.getUiFieldMap(), null, null);
        return selectItems;
    }

    public SelectItem retrieveRoughPurchaseById(Map<String, List<String>> map, Map<String, Object> ruleConfigMap) {
        SelectItem selectItem = null;
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, Object> map1 = ruleConfigMap;
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            ObjectId roughPurchaseObjectId = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("roughPurchaseObjectId")) {
                    roughPurchaseObjectId = new ObjectId(entry.getValue().get(0));
                    break;
                }
            }
            HkPurchaseDocument hkPurchaseDocument = stockService.retrievePurchaseDocumentById(roughPurchaseObjectId);
            Map<String, Map<String, RuleDetailsDataBean>> purchaseFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                purchaseFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(hkPurchaseDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.ROUGH_PURCHASE);
            }
            List<SelectItem> selectItems = this.convertModelToUiData(Arrays.asList(hkPurchaseDocument), map, purchaseFieldRuleDetailsMap, fieldIdToNameMap);
            if (!CollectionUtils.isEmpty(selectItems) && selectItems.get(0) != null) {
                selectItem = selectItems.get(0);
            }
        }
        return selectItem;
    }

    public List<SelectItem> retrieveRoughPurchases(RoughPurchaseDataBean roughPurchaseDataBean) {
        Map<String, List<String>> map = roughPurchaseDataBean.getUiFieldMap();
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, Object> map1 = roughPurchaseDataBean.getRuleConfigMap();
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            List<HkPurchaseDocument> hkPurchaseDocuments = stockService.retrievePurchases(null, null, loginDataBean.getCompanyId(), Boolean.FALSE, roughPurchaseDataBean.getOffset(), roughPurchaseDataBean.getLimit());
            Map<String, Map<String, RuleDetailsDataBean>> purchaseFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                purchaseFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkPurchaseDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.ROUGH_PURCHASE);
            }
            List<SelectItem> selectItems = this.convertModelToUiData(hkPurchaseDocuments, map, purchaseFieldRuleDetailsMap, fieldIdToNameMap);
            return selectItems;
        }
        return null;
    }

    public List<SelectItem> convertModelToUiData(List<HkPurchaseDocument> hkPurchaseDocuments, Map<String, List<String>> map, Map<String, Map<String, RuleDetailsDataBean>> purchaseFieldRuleDetailsMap, Map<String, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(hkPurchaseDocuments)) {
            for (HkPurchaseDocument hkPurchaseDocument : hkPurchaseDocuments) {
                if (hkPurchaseDocument != null) {
                    SelectItem selectItem = new SelectItem(hkPurchaseDocument.getId().toString(), null);
                    selectItem.setYear(hkPurchaseDocument.getYear());
                    selectItem.setBeforeLabel(hkPurchaseDocument.getYear().toString() + "-");
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    BasicBSONObject fieldValue = hkPurchaseDocument.getFieldValue();
                    List<String> invoiceDbFieldNames = map.get("dbFieldNames");
                    if (fieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        for (String dbField : invoiceDbFieldNames) {
                            String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            String componentType = split[1];
                            if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                if (fieldValue.toMap().containsKey(dbField)) {
                                    mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                                }
                                if (fieldValue.toMap().containsKey(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                    mapToSentOnUI.put(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                } else {
                                    if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                        if (mapOfDbFieldWithCurrencyCode.containsKey(dbField)) {
                                            String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(dbField);
                                            if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                mapToSentOnUI.put(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (fieldValue.toMap().containsKey(dbField)) {
                                    mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                                }
                            }
                        }

                        //Set screen rule related details.
                        if (purchaseFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(purchaseFieldRuleDetailsMap)) {
                            if (!CollectionUtils.isEmpty(purchaseFieldRuleDetailsMap.get(hkPurchaseDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = purchaseFieldRuleDetailsMap.get(hkPurchaseDocument.getId().toString());
                                for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                    if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                        screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                    }
                                }
                                selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                            }
                        }

                        selectItem.setCategoryCustom(mapToSentOnUI);
                        selectItems.add(selectItem);
                    }
                }
            }
        }
        return selectItems;
    }

    public Integer getNextRoughPurchaseSequence() {
        Integer nextPurchaseSequence = stockService.getNextPurchaseSequence();
        if (nextPurchaseSequence != null) {
            return ++nextPurchaseSequence;
        }
        return null;
    }

    public Boolean isRoughPurchaseIdExists(Integer roughPurchaseId, String roughPurchaseObjectId) {
        Boolean roughPurchaseSequenceExist = Boolean.FALSE;
        if (roughPurchaseObjectId != null) {
            roughPurchaseSequenceExist = stockService.isPurchaseSequenceExist(roughPurchaseId, new ObjectId(roughPurchaseObjectId));
        } else {
            roughPurchaseSequenceExist = stockService.isPurchaseSequenceExist(roughPurchaseId, null);
        }
        return roughPurchaseSequenceExist;
    }

    public List<SelectItem> retrieveAssociatedRoughPurchase(RoughPurchaseDataBean roughPurchaseDataBean) {
        Map<String, Object> map1 = roughPurchaseDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
//        System.out.println("fieldIds-----" + fieldIds);
        List<HkPurchaseDocument> hkPurchaseDocuments = stockService.retrieveNotAssociatedRoughPurchase();
//        System.out.println("hkPurchaseDocuments :" + hkPurchaseDocuments);
        Map<String, Map<String, RuleDetailsDataBean>> purchaseFieldRuleDetailsMap = null;
        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
            purchaseFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkPurchaseDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.ROUGH_PURCHASE);
        }
        List<SelectItem> selectItems = this.convertModelToUiData(hkPurchaseDocuments, roughPurchaseDataBean.getUiFieldMap(), purchaseFieldRuleDetailsMap, fieldIdToNameMap);
        return selectItems;
    }

    public long getCountofPurchaseDocumentsFromCriteria() {
        long totalItems = stockService.getCountofPurchaseDocumentsFromCriteria(0l, Boolean.FALSE, null, null);
        return totalItems;
    }

    public long getCountofPurchaseDocumentsFromSearchCriteria(RoughPurchaseDataBean roughPurchaseDataBean) {
        long totalItems = 0l;
        if (roughPurchaseDataBean != null) {
            Map<String, Object> roughPurchaseCustomMap = roughPurchaseDataBean.getRoughPurchaseCustom();
            if (!CollectionUtils.isEmpty(roughPurchaseCustomMap) && roughPurchaseDataBean.getSearchOnParameter()) {
                Iterator<Map.Entry<String, Object>> iter = roughPurchaseCustomMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            roughPurchaseCustomMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            roughPurchaseCustomMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
            }
            List<ObjectId> roughPurchaseIds = null;
            if (roughPurchaseDataBean.getId() != null) {
                roughPurchaseIds = new ArrayList<>();
                roughPurchaseIds.add(new ObjectId(roughPurchaseDataBean.getId()));
            }
            totalItems = stockService.getCountofPurchaseDocumentsFromCriteria(0l, Boolean.FALSE, roughPurchaseCustomMap, roughPurchaseIds);
        }
        return totalItems;
    }

}
