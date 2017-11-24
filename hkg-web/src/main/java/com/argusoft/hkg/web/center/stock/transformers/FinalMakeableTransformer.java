/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkFinalMakeableDocument;
import com.argusoft.hkg.web.center.stock.databeans.FinalMakeableDatabean;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author akta
 */
@Service
public class FinalMakeableTransformer {

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    private HkCustomFieldService fieldService;

    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;

    public List<SelectItem> retirevePacketsByInStockOf(PacketDataBean packetDataBean) {
        List<HkPacketDocument> packetDocuments = stockService.retrievePackets(null, null, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, loginDataBean.getId() + ":E", null,null,null);
        Map<String, Map<String, RuleDetailsDataBean>> packetFieldRuleDetailsMap = null;
        Map<Long, String> fieldIdToNameMap = null;
        if (!CollectionUtils.isEmpty(packetDataBean.getRuleConfigMap())
                && packetDataBean.getRuleConfigMap().get("featureName") != null) {

            if (!CollectionUtils.isEmpty(packetDataBean.getRuleConfigMap()) && packetDataBean.getRuleConfigMap().get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<Long, String>) packetDataBean.getRuleConfigMap().get("fieldIdNameMap");
            }
            packetFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(packetDocuments, new ArrayList<>(fieldIdToNameMap.keySet()), packetDataBean.getRuleConfigMap().get("featureName").toString(), "packet");
        }
        List<SelectItem> packetDocumentsSelectItem = convertModelToUiData(packetDocuments, packetDataBean.getFeatureDbFieldMap().get("dbFieldNames"), packetFieldRuleDetailsMap, fieldIdToNameMap);
        return packetDocumentsSelectItem;
    }

    /**
     * In this method i have used GenericDocument list if anyone wants to write
     * code specific to document type can use this one
     * if(!CollectionUtils.isEmpty(finalMakeableOrPacketDocuments)){
     * if(finalMakeableOrPacketDocuments.get(0).getClass()==ABC.class){ caste
     * object to ABC class and than write ABC class specific code here } }
     *
     * @param finalMakeableOrPacketDocuments
     * @param dbFieldNames
     * @param finalMakeableFieldRuleDetailsMap
     * @param fieldIdToNameMap
     * @return
     */
    public List<SelectItem> convertModelToUiData(List<? extends GenericDocument> finalMakeableOrPacketDocuments, List<String> dbFieldNames,
            Map<String, Map<String, RuleDetailsDataBean>> finalMakeableFieldRuleDetailsMap, Map<Long, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = fieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(finalMakeableOrPacketDocuments)) {
            for (GenericDocument finalMakeableOrPacketDocument : finalMakeableOrPacketDocuments) {
                if (finalMakeableOrPacketDocument != null) {
                    SelectItem selectItem = new SelectItem(finalMakeableOrPacketDocument.getId().toString(), null);
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    BasicBSONObject fieldValue = finalMakeableOrPacketDocument.getFieldValue();
//                    List<String> dbFieldNames = dbFieldNames;
                    if (fieldValue != null && !CollectionUtils.isEmpty(dbFieldNames)) {
                        for (String dbField : dbFieldNames) {
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
                        if (finalMakeableFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(finalMakeableFieldRuleDetailsMap)) {
                            if (!CollectionUtils.isEmpty(finalMakeableFieldRuleDetailsMap.get(finalMakeableOrPacketDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = finalMakeableFieldRuleDetailsMap.get(finalMakeableOrPacketDocument.getId().toString());
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

    public String createFinalMakeable(FinalMakeableDatabean finalMakeableDatabean) {
        Map<String, Object> finalMakeableCustomMap = finalMakeableDatabean.getFinalMakeableCustom();
        Map<String, String> finalMakeableDbTypeMap = finalMakeableDatabean.getFinalMakeableDbType();
        Iterator<Map.Entry<String, Object>> iteratorFinalMakeableCustomMap = finalMakeableCustomMap.entrySet().iterator();
        //This logic removes key with value empty , but use this only when creating finalMakeable not at time of updating it
        while (iteratorFinalMakeableCustomMap.hasNext()) {
            Map.Entry<String, Object> nextFinalMakeableCustom = iteratorFinalMakeableCustomMap.next();
            if (!(nextFinalMakeableCustom.getValue() != null && !(nextFinalMakeableCustom.getValue().toString().isEmpty()))) {
                iteratorFinalMakeableCustomMap.remove();
            }
        }
        List<String> uiFieldList = new ArrayList<>(finalMakeableDbTypeMap.keySet());
        String finalMakeableId = stockService.saveFinalMakeable(finalMakeableCustomMap, finalMakeableDbTypeMap, uiFieldList, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(finalMakeableDatabean.getPacketId()));
        return finalMakeableId;
    }

    public SelectItem retrieveFinalMakeableByPktId(FinalMakeableDatabean finalMakeableDatabean) {
        HkFinalMakeableDocument finalMakeableDocument = stockService.retrieveFinalMakeableByPacketId(finalMakeableDatabean.getPacketId());
        if (finalMakeableDocument != null) {
            List<HkFinalMakeableDocument> listFinalMakeable = new ArrayList<>();
            listFinalMakeable.add(finalMakeableDocument);
            Map<String, Map<String, RuleDetailsDataBean>> finalMakeableFieldRuleDetailsMap = null;
            Map<Long, String> fieldIdToNameMap = null;
            if (!CollectionUtils.isEmpty(finalMakeableDatabean.getRuleConfigMap())
                    && finalMakeableDatabean.getRuleConfigMap().get("featureName") != null) {

                if (!CollectionUtils.isEmpty(finalMakeableDatabean.getRuleConfigMap()) && finalMakeableDatabean.getRuleConfigMap().get("fieldIdNameMap") != null) {
                    fieldIdToNameMap = (Map<Long, String>) finalMakeableDatabean.getRuleConfigMap().get("fieldIdNameMap");
                }
                finalMakeableFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(listFinalMakeable,
                        new ArrayList<>(fieldIdToNameMap.keySet()), finalMakeableDatabean.getRuleConfigMap().get("featureName").toString(),
                        "finalMakeableEntity");
            }
            List<SelectItem> packetDocumentsSelectItem = convertModelToUiData(listFinalMakeable, finalMakeableDatabean.getDbFieldNames(), finalMakeableFieldRuleDetailsMap, fieldIdToNameMap);
            return packetDocumentsSelectItem.get(0);
        } else {
            return null;
        }
    }

    public String updateFinalMakeable(FinalMakeableDatabean finalMakeableDatabean) {
        Map<String, Object> finalMakeableCustomMap = finalMakeableDatabean.getFinalMakeableCustom();
        Map<String, String> finalMakeableDbTypeMap = finalMakeableDatabean.getFinalMakeableDbType();
        Iterator<Map.Entry<String, Object>> iteratorFinalMakeableCustomMap = finalMakeableCustomMap.entrySet().iterator();
        //This logic removes key with value empty , but use this only when creating finalMakeable not at time of updating it
        while (iteratorFinalMakeableCustomMap.hasNext()) {
            Map.Entry<String, Object> nextFinalMakeableCustom = iteratorFinalMakeableCustomMap.next();
            if (!(nextFinalMakeableCustom.getValue() != null && !(nextFinalMakeableCustom.getValue().toString().isEmpty()))) {
                iteratorFinalMakeableCustomMap.remove();
            }
        }
        List<String> uiFieldList = new ArrayList<>(finalMakeableDbTypeMap.keySet());
        String finalMakeableId = stockService.updateFinalMakeable(finalMakeableCustomMap, finalMakeableDbTypeMap, uiFieldList, loginDataBean.getCompanyId(), loginDataBean.getId(), finalMakeableDatabean.getId());
        return finalMakeableId;
    }
}
