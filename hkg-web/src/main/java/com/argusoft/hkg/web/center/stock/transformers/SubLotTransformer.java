/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.web.center.stock.databeans.InvoiceDataBean;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.stock.databeans.SublotDatabean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SubLotTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubLotTransformer.class);

    @Autowired
    private HkStockService stockService;
    @Autowired
    private HkCustomFieldService fieldService;
    @Autowired
    private HkCustomFieldService customFieldService;
    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;
    @Autowired
    private LoginDataBean loginDataBean;
    @Autowired
    ApplicationUtil applicationUtil;

    public SelectItem saveSubLot(SublotDatabean subLotDataBean) {
        System.out.println("In save sublot method:::");
        SelectItem selectItem = null;
        Map<String, Object> subLotCustomMap = subLotDataBean.getSubLotCustom();
        Map<String, String> subLotDbTypeMap = subLotDataBean.getSubLotDbType();
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, Object> entry : subLotCustomMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!(value != null && value.toString() != null && value.toString().length() > 0)) {
                keysToRemove.add(key);
            }
        }
        if (!CollectionUtils.isEmpty(keysToRemove)) {
            for (String key : keysToRemove) {
                subLotCustomMap.remove(key);
            }
        }
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subLotDbTypeMap)) {
            for (Map.Entry<String, String> entrySet : subLotDbTypeMap.entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.SUB_LOT);
        Map<String, String> dbFieldWithFormulaMap = null;
//        Commmented by Shifa temporarly
        List<HkFieldDocument> totalformulaList = fieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        System.out.println("service call");
        String subLotId = stockService.saveSubLot(subLotCustomMap, subLotDbTypeMap, uiFieldMap, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(subLotDataBean.getParcelDataBean().getId()), totalformulaList, feature.getId());

        if (!CollectionUtils.isEmpty(subLotDataBean.getUiFieldMap())) {
            subLotDataBean.getUiFieldMap().put("subLotObjectId", Arrays.asList(subLotId));
            selectItem = this.retrieveSublotById(subLotDataBean.getUiFieldMap(), subLotDataBean.getRuleConfigMap());
        }
        return selectItem;
    }

    public List<SelectItem> retrieveSublots(Map<String, List<String>> map) {
        if (!CollectionUtils.isEmpty(map)) {
            List<HkSubLotDocument> hkSubLotDocuments = (List<HkSubLotDocument>) stockService.retrieveAllDocumentsByCriteria(loginDataBean.getCompanyId(), Boolean.FALSE, HkSubLotDocument.class);
            List<SelectItem> selectItems = this.convertModelToUiData(hkSubLotDocuments, map, null, null);
            return selectItems;
        }
        return null;
    }

    public List<SelectItem> convertModelToUiData(List<HkSubLotDocument> sublotDocuments, Map<String, List<String>> map, Map<String, Map<String, RuleDetailsDataBean>> subLotFieldRuleDetailsMap, Map<String, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = fieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(sublotDocuments)) {
            for (HkSubLotDocument sublotDocument : sublotDocuments) {
                if (sublotDocument != null) {
                    SelectItem selectItem = new SelectItem(sublotDocument.getId().toString(), null);
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    BasicBSONObject fieldValue = sublotDocument.getFieldValue();
                    List<String> invoiceDbFieldNames = map.get("dbFieldNames");
                    if (fieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        for (String dbField : invoiceDbFieldNames) {
                            // Code modified by Shreya for solving the issue of currency component where currency code was not getting retrieved
                            String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            if (split.length > 1) {
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    // For Currency bring currency code as well
                                    if (fieldValue.toMap().containsKey(dbField)) {
                                        // This will bring currency values
                                        mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                                    }
                                    if (fieldValue.toMap().containsKey(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                        // This will bring currency code
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
                        }
                        if (subLotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(subLotFieldRuleDetailsMap)) {
                            System.out.println("subLotFieldRuleDetailsMap:::" + subLotFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(subLotFieldRuleDetailsMap.get(sublotDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = subLotFieldRuleDetailsMap.get(sublotDocument.getId().toString());
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

    public SelectItem retrieveSublotById(Map<String, List<String>> map, Map<String, Object> ruleConfigMap) {
        Map<String, String> mapOfDbFieldWithCurrencyCode = fieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        SelectItem selectItem = null;
        if (!CollectionUtils.isEmpty(map)) {
            ObjectId sublotObjecId = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("sublotObjectId")) {
                    sublotObjecId = new ObjectId(entry.getValue().get(0));
                    break;
                }
            }
            Map<String, Object> map1 = ruleConfigMap;
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            HkSubLotDocument sublotDocument = (HkSubLotDocument) stockService.retrieveDocumentById(sublotObjecId, HkSubLotDocument.class);
            Map<String, Map<String, RuleDetailsDataBean>> subLotFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                subLotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(sublotDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.SUB_LOT);
            }
            if (sublotDocument != null) {
                selectItem = new SelectItem(sublotDocument.getId().toString(), null);
                Map<String, Object> mapToSentOnUI = new HashMap<>();
                BasicBSONObject fieldValue = sublotDocument.getFieldValue();
                List<String> sublotDbFieldNames = map.get("dbFieldNames");
                if (fieldValue != null && !CollectionUtils.isEmpty(sublotDbFieldNames)) {
                    for (String dbField : sublotDbFieldNames) {
                        // Code modified by Shreya shah for solving the issue of currency component where currency code was not getting retrieved
                        String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                        String componentType = split[1];
                        if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                            // For Currency bring currency code as well
                            LOGGER.info("CURRENCY dbfield found from UI" + dbField);
                            if (fieldValue.toMap().containsKey(dbField)) {
                                // This will bring currency values
                                mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));

                            }
                            if (fieldValue.toMap().containsKey(dbField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                // This will bring currency code
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
                    if (subLotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(subLotFieldRuleDetailsMap)) {
                        System.out.println("subLotFieldRuleDetailsMap:::" + subLotFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(subLotFieldRuleDetailsMap.get(sublotDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = subLotFieldRuleDetailsMap.get(sublotDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                        }
                    }
                    selectItem.setCategoryCustom(mapToSentOnUI);
                }
            }
        }
        return selectItem;
    }

    public SelectItem updateSublot(SublotDatabean sublotDataBean) {
        SelectItem selectItem = null;
        Map<String, Object> sublotCustomMap = sublotDataBean.getSubLotCustom();
        Map<String, String> sublotDbTypeMap = sublotDataBean.getSubLotDbType();
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sublotDbTypeMap)) {
            for (Map.Entry<String, String> entrySet : sublotDbTypeMap.entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.SUB_LOT);
        Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula = fieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(loginDataBean.getCompanyId(), HkSystemConstantUtil.Feature.SUB_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
         List<HkFieldDocument> totalformulaList =fieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Boolean result = stockService.updateSublot(new ObjectId(sublotDataBean.getId()), sublotCustomMap, sublotDbTypeMap, mapForFeatureInvolvedInFormula, totalformulaList, applicationUtil.getFeatureIdNameMap(), loginDataBean.getCompanyId(), loginDataBean.getId(), feature.getId());
        if (result) {
            if (!CollectionUtils.isEmpty(sublotDataBean.getUiFieldMap())) {
                sublotDataBean.getUiFieldMap().put("sublotObjectId", Arrays.asList(sublotDataBean.getId()));
                selectItem = this.retrieveSublotById(sublotDataBean.getUiFieldMap(), sublotDataBean.getRuleConfigMap());
            }
        }
        return selectItem;
    }

    public Map<Boolean, String> deleteSublot(String sublotId) {
        Map<Boolean, String> responseAndMsgMap = new HashMap<>();
        if (sublotId != null) {
            List<ObjectId> sublotObjectIds = new ArrayList<>();
            sublotObjectIds.add(new ObjectId(sublotId));
//            List<ObjectId> parcelIds = stockService.retrieveParcelIds(null, sublotObjectIds, loginDataBean.getCompanyId(), Boolean.FALSE);
//            if (CollectionUtils.isEmpty(parcelIds)) {
            Boolean deleteSublot = stockService.deleteDocument(new ObjectId(sublotId), loginDataBean.getId(), HkSubLotDocument.class);
            if (deleteSublot) {
                responseAndMsgMap.put(Boolean.TRUE, "Sublot deleted successfully");
                return responseAndMsgMap;
            } else {
                responseAndMsgMap.put(Boolean.FALSE, "Sublot can not be deleted");
                return responseAndMsgMap;
            }
        }
        return responseAndMsgMap;
//                else {
////                responseAndMsgMap.put(Boolean.FALSE, "Can not delete Sublot because it is having ");
//                return responseAndMsgMap;
//            }
    }

    public List<SelectItem> retrieveSubLotByParcelId(String parcelId, Map<String, List<String>> map, Map<String, Object> ruleConfigMap, Boolean excludeSubLotWithAssociatedLot, String includeSubLotWithAssociatedLot) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, Object> map1 = ruleConfigMap;
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        ObjectId includeSubLotWithAssociatedLotObjId = null;
        if (includeSubLotWithAssociatedLot != null) {
            includeSubLotWithAssociatedLotObjId = new ObjectId(includeSubLotWithAssociatedLot);
        }
        if (!StringUtils.isEmpty(parcelId)) {
            List<HkSubLotDocument> hkSubLotDocuments = stockService.retrieveSubLotsByCriteria(Arrays.asList(new ObjectId(parcelId)), null, loginDataBean.getCompanyId(), Boolean.FALSE, excludeSubLotWithAssociatedLot, includeSubLotWithAssociatedLotObjId);

            Map<String, Map<String, RuleDetailsDataBean>> subLotFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                subLotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkSubLotDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.SUB_LOT);
            }
            if (!CollectionUtils.isEmpty(hkSubLotDocuments)) {
                selectItems = this.convertModelToUiData(hkSubLotDocuments, map, subLotFieldRuleDetailsMap, fieldIdToNameMap);
            }
        }
        return selectItems;
    }

    public List<SelectItem> retrieveAllottedParcels(ParcelDataBean parcelDataBean, InvoiceDataBean invoiceDataBean) {
        List<HkParcelDocument> parcelDocuments = stockService.retrieveAllottedParcels(loginDataBean.getId());
        Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
        Map<String, Object> map1 = parcelDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }

        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
            parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(parcelDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
        }
        List<SelectItem> selectItems = this.convertModelToUiDataForParcel(parcelDocuments, parcelFieldRuleDetailsMap, fieldIdToNameMap, parcelDataBean, invoiceDataBean);
        return selectItems;

    }

    public List<SelectItem> convertModelToUiDataForParcel(List<HkParcelDocument> parcelDocuments, Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap, Map<String, String> fieldIdToNameMap, ParcelDataBean parcelDataBean, InvoiceDataBean invoiceDataBean) {
        List<SelectItem> selectItems = null;
        if (!CollectionUtils.isEmpty(parcelDocuments)) {
            selectItems = new ArrayList<>();

            Map<ObjectId, Map<String, Object>> invoices = new LinkedHashMap<>();
            HkInvoiceDocument invoiceDocument = null;
            List<String> parcelDbField = parcelDataBean.getFeatureDbFieldMap().get("dbFieldNames");
            Map<String, Object> tmpCategoryCustomMap;
            List<String> invoiceDbFieldNames = invoiceDataBean.getDbFieldNames();
            for (HkParcelDocument parcelDocument : parcelDocuments) {
                Map<String, Object> map1 = new HashMap<>();
                SelectItem selectItem = new SelectItem(parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());

                tmpCategoryCustomMap = invoices.get(parcelDocument.getInvoice());
                if (tmpCategoryCustomMap == null) {
                    tmpCategoryCustomMap = new LinkedHashMap<>();
                    invoiceDocument = stockService.retrieveInvoiceById(parcelDocument.getInvoice());
                    BasicBSONObject invoiceFieldValue = invoiceDocument.getFieldValue();
                    if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        Map invoiceFieldValueMap = invoiceFieldValue.toMap();
                        for (String dbField : invoiceDbFieldNames) {
                            if (invoiceFieldValueMap.containsKey(dbField)) {
                                tmpCategoryCustomMap.put(dbField, invoiceFieldValueMap.get(dbField));
                            }
                        }
                    }
                    selectItem.setDescription(invoiceDocument.getId().toString());
                    invoices.put(parcelDocument.getInvoice(), tmpCategoryCustomMap);
                }
                map1.putAll(tmpCategoryCustomMap);
                BasicBSONObject fieldValue = parcelDocument.getFieldValue();
                if (fieldValue != null) {
                    map1.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_CARAT, parcelDocument.getStockCarat());
                    map1.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_PIECES, parcelDocument.getStockPieces());
                    map1.put("parcelDbObjectId", parcelDocument.getId().toString());

                    if (!CollectionUtils.isEmpty(parcelDbField)) {
                        Map fieldValueMap = fieldValue.toMap();
                        for (String dbField : parcelDbField) {
                            if (fieldValueMap.containsKey(dbField)) {
                                Object value = fieldValueMap.get(dbField);

                                if (dbField.contains("$MS$") || dbField.contains("$UMS$") || dbField.contains("$AG$")) {

                                    value = value.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                                }
                                System.out.println("DBFIELD:: " + dbField + "   VALUE " + value);
                                map1.put(dbField, value);
                            }
                        }
                    }
                }

                //Set screen rule related details.
                if (!CollectionUtils.isEmpty(parcelFieldRuleDetailsMap) && !CollectionUtils.isEmpty(parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString()))) {
                    Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                    Map<String, RuleDetailsDataBean> ruleDetails = parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString());
                    for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                        if (fieldIdToNameMap.get(entry.getKey()) != null) {
                            screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                        }
                    }
                    selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                }
                selectItem.setCategoryCustom(map1);
                selectItems.add(selectItem);
            }

            return selectItems;
        }
        return null;
    }

}
