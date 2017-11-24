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
import com.argusoft.hkg.nosql.model.HkRoughMakeableDocument;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RoughMakeableDatabean;
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
public class RoughMakeableTransformer {

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
     * if(!CollectionUtils.isEmpty(roughMakeableOrPacketDocuments)){
     * if(roughMakeableOrPacketDocuments.get(0).getClass()==ABC.class){ caste
     * object to ABC class and than write ABC class specific code here } }
     *
     * @param roughMakeableOrPacketDocuments
     * @param dbFieldNames
     * @param roughMakeableFieldRuleDetailsMap
     * @param fieldIdToNameMap
     * @return
     */
    public List<SelectItem> convertModelToUiData(List<? extends GenericDocument> roughMakeableOrPacketDocuments, List<String> dbFieldNames,
            Map<String, Map<String, RuleDetailsDataBean>> roughMakeableFieldRuleDetailsMap, Map<Long, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = fieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(roughMakeableOrPacketDocuments)) {
            for (GenericDocument roughMakeableOrPacketDocument : roughMakeableOrPacketDocuments) {
                if (roughMakeableOrPacketDocument != null) {
                    SelectItem selectItem = new SelectItem(roughMakeableOrPacketDocument.getId().toString(), null);
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    BasicBSONObject fieldValue = roughMakeableOrPacketDocument.getFieldValue();
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
                        if (roughMakeableFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(roughMakeableFieldRuleDetailsMap)) {
                            if (!CollectionUtils.isEmpty(roughMakeableFieldRuleDetailsMap.get(roughMakeableOrPacketDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = roughMakeableFieldRuleDetailsMap.get(roughMakeableOrPacketDocument.getId().toString());
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

    public String createRoughMakeable(RoughMakeableDatabean roughMakeableDatabean) {
        Map<String, Object> roughMakeableCustomMap = roughMakeableDatabean.getRoughMakeableCustom();
        Map<String, String> roughMakeableDbTypeMap = roughMakeableDatabean.getRoughMakeableDbType();
        Iterator<Map.Entry<String, Object>> iteratorRoughMakeableCustomMap = roughMakeableCustomMap.entrySet().iterator();
        //This logic removes key with value empty , but use this only when creating roughMakeable not at time of updating it
        while (iteratorRoughMakeableCustomMap.hasNext()) {
            Map.Entry<String, Object> nextRoughMakeableCustom = iteratorRoughMakeableCustomMap.next();
            if (!(nextRoughMakeableCustom.getValue() != null && !(nextRoughMakeableCustom.getValue().toString().isEmpty()))) {
                iteratorRoughMakeableCustomMap.remove();
            }
        }
        List<String> uiFieldList = new ArrayList<>(roughMakeableDbTypeMap.keySet());
        String roughMakeableId = stockService.saveRoughMakeable(roughMakeableCustomMap, roughMakeableDbTypeMap, uiFieldList, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(roughMakeableDatabean.getPacketId()));
        return roughMakeableId;
    }

    public SelectItem retrieveRoughMakeableByPktId(RoughMakeableDatabean roughMakeableDatabean) {
        HkRoughMakeableDocument roughMakeableDocument = stockService.retrieveRoughMakeableByPacketId(roughMakeableDatabean.getPacketId());
        if (roughMakeableDocument != null) {
            List<HkRoughMakeableDocument> listRoughMakeable = new ArrayList<>();
            listRoughMakeable.add(roughMakeableDocument);
            Map<String, Map<String, RuleDetailsDataBean>> roughMakeableFieldRuleDetailsMap = null;
            Map<Long, String> fieldIdToNameMap = null;
            if (!CollectionUtils.isEmpty(roughMakeableDatabean.getRuleConfigMap())
                    && roughMakeableDatabean.getRuleConfigMap().get("featureName") != null) {

                if (!CollectionUtils.isEmpty(roughMakeableDatabean.getRuleConfigMap()) && roughMakeableDatabean.getRuleConfigMap().get("fieldIdNameMap") != null) {
                    fieldIdToNameMap = (Map<Long, String>) roughMakeableDatabean.getRuleConfigMap().get("fieldIdNameMap");
                }
                roughMakeableFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(listRoughMakeable,
                        new ArrayList<>(fieldIdToNameMap.keySet()), roughMakeableDatabean.getRuleConfigMap().get("featureName").toString(),
                        "roughMakeableEntity");
            }
            List<SelectItem> packetDocumentsSelectItem = convertModelToUiData(listRoughMakeable, roughMakeableDatabean.getDbFieldNames(), roughMakeableFieldRuleDetailsMap, fieldIdToNameMap);
            return packetDocumentsSelectItem.get(0);
        } else {
            return null;
        }
    }

    public String updateRoughMakeable(RoughMakeableDatabean roughMakeableDatabean) {
        Map<String, Object> roughMakeableCustomMap = roughMakeableDatabean.getRoughMakeableCustom();
        Map<String, String> roughMakeableDbTypeMap = roughMakeableDatabean.getRoughMakeableDbType();
        Iterator<Map.Entry<String, Object>> iteratorRoughMakeableCustomMap = roughMakeableCustomMap.entrySet().iterator();
        //This logic removes key with value empty , but use this only when creating roughMakeable not at time of updating it
        while (iteratorRoughMakeableCustomMap.hasNext()) {
            Map.Entry<String, Object> nextRoughMakeableCustom = iteratorRoughMakeableCustomMap.next();
            if (!(nextRoughMakeableCustom.getValue() != null && !(nextRoughMakeableCustom.getValue().toString().isEmpty()))) {
                iteratorRoughMakeableCustomMap.remove();
            }
        }
        List<String> uiFieldList = new ArrayList<>(roughMakeableDbTypeMap.keySet());
        String roughMakeableId = stockService.updateRoughMakeable(roughMakeableCustomMap, roughMakeableDbTypeMap, uiFieldList, loginDataBean.getCompanyId(), loginDataBean.getId(), roughMakeableDatabean.getId());
        return roughMakeableId;
    }
}
