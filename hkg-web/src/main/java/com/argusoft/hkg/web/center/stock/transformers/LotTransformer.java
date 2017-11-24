/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkNotificationDocumentService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.center.stock.databeans.LotDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkNotificationDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
 * @author shreya
 */
@Service
public class LotTransformer {

    @Autowired
    HkStockService stockService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    HkNotificationDocumentService notificationService;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkUserService hkUserService;

    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;

    @Autowired
    StockTransformer stockTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(LotTransformer.class);

    public List<SelectItem> searchLot(LotDataBean lotDataBean) {
        Long franchise = loginDataBean.getCompanyId();
        Map<String, List<String>> featureDbFieldMap = lotDataBean.getFeatureDbFieldMap();
        List<String> invoiceDbField = null;
        List<String> parcelDbField = null;
        List<String> lotDbField = null;
        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("invoice"))) {
                invoiceDbField = featureDbFieldMap.get("invoice");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("parcel"))) {
                parcelDbField = featureDbFieldMap.get("parcel");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("lot"))) {
                lotDbField = featureDbFieldMap.get("lot");
            }
        }
        Map<String, Object> map1 = lotDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        Map<String, Map<String, Object>> featureCustomMapValue = lotDataBean.getFeatureCustomMapValue();
        Map<String, Object> invoiceFieldMap = new HashMap<>();
        Map<String, Object> parcelFieldMap = new HashMap<>();
        Map<String, Object> lotFieldMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
            for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                String featureName = entry.getKey();
                Map<String, Object> map = entry.getValue();
                if (!StringUtils.isEmpty(featureName)) {
                    if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            this.prepareAndValidateFieldMap(map, invoiceFieldMap);
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            this.prepareAndValidateFieldMap(map, parcelFieldMap);
                        }
                    } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                        if (!CollectionUtils.isEmpty(map)) {
                            this.prepareAndValidateFieldMap(map, lotFieldMap);
                        }
                    }
                }
            }

            List<HkInvoiceDocument> invoiceDocuments = null;
            if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, franchise, Boolean.FALSE, null, null,null,null);
                if (CollectionUtils.isEmpty(invoiceDocuments)) {
                    return null;
                }
            }
            List<ObjectId> invoiceIds = null;
            if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                invoiceIds = new ArrayList<>();
                for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                    invoiceIds.add(invoiceDocument.getId());
                }
            }
            List<HkParcelDocument> parcelDocuments = null;
            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                if (CollectionUtils.isEmpty(parcelDocuments)) {
                    return null;
                }
            }
            List<ObjectId> parcelIds = null;
            if (!CollectionUtils.isEmpty(parcelDocuments)) {
                parcelIds = new ArrayList<>();
                for (HkParcelDocument parcelDocument : parcelDocuments) {
                    parcelIds.add(parcelDocument.getId());
                }
            }
            List<HkLotDocument> lotDocuments = null;
            if (!CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds) || !CollectionUtils.isEmpty(lotFieldMap)) {
                lotDocuments = stockService.retrieveLots(lotFieldMap, invoiceIds, parcelIds, null, franchise, Boolean.FALSE);
            }
            List<SelectItem> selectItems = null;
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                selectItems = new ArrayList<>();
                invoiceIds = new ArrayList<>();
                parcelIds = new ArrayList<>();
                for (HkLotDocument lotDocument : lotDocuments) {
                    invoiceIds.add(lotDocument.getInvoice());
                    parcelIds.add(lotDocument.getParcel());
                }
                List<HkInvoiceDocument> hkInvoiceDocuments = null;
                List<HkParcelDocument> hkParcelDocuments = null;
                if (!CollectionUtils.isEmpty(invoiceDbField)) {
                    hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                }
                if (!CollectionUtils.isEmpty(parcelDbField)) {
                    hkParcelDocuments = stockService.retrieveParcels(parcelIds, franchise, Boolean.FALSE);
                }
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    Map<String, Map<String, RuleDetailsDataBean>> lotFieldRuleDetailsMap = null;
                    if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                        lotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(lotDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.LOT);
                    }
                    for (HkLotDocument lotDocument : lotDocuments) {
//                        if (lotDocument.isHasPacket().equals(lotDataBean.isHasPacketTrue())) {
                        Map<String, Object> map = new HashMap<>();
                        SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
                        BasicBSONObject fieldValue = lotDocument.getFieldValue();
                        map.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_CARAT, lotDocument.getStockCarat());
                        map.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_PIECES, lotDocument.getStockPieces());
                        if (fieldValue != null) {
                            map.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValue.toMap().get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                            map.put(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT, fieldValue.toMap().get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT));
                            if (!CollectionUtils.isEmpty(lotDbField)) {
                                for (String dbField : lotDbField) {
                                    if (fieldValue.toMap().containsKey(dbField) && fieldValue.toMap().get(dbField) != null) {
                                        map.put(dbField, fieldValue.toMap().get(dbField));
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(invoiceDbField)) {
                                for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                                    if (hkInvoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
                                        BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                        if (invoiceFieldValue != null) {
                                            for (String dbField : invoiceDbField) {
                                                if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                                    map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelDbField)) {
                                for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                                    if (hkParcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
                                        for (String dbField : parcelDbField) {
                                            if (hkParcelDocument.getFieldValue().toMap().containsKey(dbField)) {
                                                map.put(dbField, hkParcelDocument.getFieldValue().toMap().get(dbField));
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            selectItem.setCategoryCustom(map);
                            //Set screen rule related details.
                            if (lotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(lotFieldRuleDetailsMap)) {
                                System.out.println("lotFieldRuleDetailsMap:::" + lotFieldRuleDetailsMap);
                                System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                                if (!CollectionUtils.isEmpty(lotFieldRuleDetailsMap.get(lotDocument.getId().toString()))) {
                                    Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                    Map<String, RuleDetailsDataBean> ruleDetails = lotFieldRuleDetailsMap.get(lotDocument.getId().toString());
                                    for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                        if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                            screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());

                                        }
                                    }
                                    selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                                }

                            }
                            selectItem.setCategoryCustom(map);
                            selectItems.add(selectItem);
                        }
                    }
                    return selectItems;
                }
            }
        }
        return null;
    }

    public String updateLot(LotDataBean lotDataBean) {
        Map<String, Object> lotCustom = lotDataBean.getLotCustom();
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.LOT);
        Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula = customFieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(loginDataBean.getCompanyId(), HkSystemConstantUtil.Feature.LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotDataBean.getLotDbType())) {
            for (Map.Entry<String, String> entrySet : lotDataBean.getLotDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        List<ObjectId> subLots = new ArrayList<>();
        Map<String, String> uiFieldMap = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        Map<String, String> dbFieldWithFormulaMap = null;
        //--- Commmented by Shifa temporarly
              List<HkFieldDocument> totalformulaList= customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        if (!CollectionUtils.isEmpty(lotDataBean.getSubLots())) {
            for (String subLot : lotDataBean.getSubLots()) {
                subLots.add(new ObjectId(subLot));
            }
        }
        return stockService.updateLot(new ObjectId(lotDataBean.getId()), Boolean.FALSE, lotCustom, lotDataBean.getLotDbType(), mapForFeatureInvolvedInFormula, uiFieldMap, totalformulaList, applicationUtil.getFeatureIdNameMap(), lotDataBean.getFranchise(), loginDataBean.getId(), feature.getId(), lotDataBean.getSequenceNumber(), subLots);
    }

    public List<SelectItem> retrieveLotByParcelId(Map<String, Object> map) {
        List<SelectItem> selectItems = null;
        List<ObjectId> parcelIds = new ArrayList<>();
        ObjectId parcelId = new ObjectId();
        if (!CollectionUtils.isEmpty(map)) {
            List<String> parcelObjIds = (List<String>) map.get("parcelObjectId");
            if (parcelObjIds != null) {
                parcelId = new ObjectId(parcelObjIds.get(0));
                parcelIds.add(parcelId);
            }
            List<String> parcelDbFieldNames = (List<String>) map.get("parcelDbFieldName");
            List<String> invoiceDbFieldNames = (List<String>) map.get("invoiceDbFieldName");
            List<String> lotDbFieldNames = (List<String>) map.get("lotDbFieldName");

            Map<String, Object> map1 = (Map<String, Object>) map.get("ruleConfigMap");
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            HkParcelDocument parcelDocument = null;
            parcelDocument = stockService.retrieveParcelById(parcelId);
            selectItems = new ArrayList<>();
            if (parcelDocument != null) {
                SelectItem parcelItem = new SelectItem(parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());
                BasicBSONObject fieldValue = parcelDocument.getFieldValue();
                if (fieldValue != null && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                    Map<Object, Object> mapToSentOnUI = new HashMap<>();
                    for (String dbField : parcelDbFieldNames) {
                        if (fieldValue.toMap().containsKey(dbField)) {
                            mapToSentOnUI.put(dbField, fieldValue.toMap().get(dbField));
                        }
                    }
                    parcelItem.setCustom1(mapToSentOnUI);
                }
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(parcelDocument.getInvoice());
                if (invoiceDocument != null) {
                    BasicBSONObject invoiceFieldValue = invoiceDocument.getFieldValue();
                    if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        for (String dbField : invoiceDbFieldNames) {
                            if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                mapToSentOnUI.put(dbField, invoiceFieldValue.toMap().get(dbField));
                            }
                        }
                        parcelItem.setCustom3(mapToSentOnUI);
                    }
                }
                selectItems.add(parcelItem);
            }
            List<HkLotDocument> lotDocuments = stockService.retrieveLots(null, null, parcelIds, null, loginDataBean.getCompanyId(), Boolean.FALSE);
            Map<String, Map<String, RuleDetailsDataBean>> lotFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                lotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(lotDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.LOT);
            }
            if (!CollectionUtils.isEmpty(lotDocuments)) {
                Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                for (String dbField : lotDbFieldNames) {
                    String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                    String componentType = split[1];
                    if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                        mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                        break;
                    }
                }
                for (HkLotDocument lotDocument : lotDocuments) {
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
                    selectItem.setCustom7(lotDocument.getFranchiseId());
                    selectItem.setOtherId(lotDocument.getSequenceNumber());
                    if (!CollectionUtils.isEmpty(lotDocument.getSubLots())) {
                        List<String> subLots = new ArrayList<>();
                        for (ObjectId subLot : lotDocument.getSubLots()) {
                            subLots.add(subLot.toString());
                        }
                        selectItem.setOtherValues(subLots);
                    }
                    BasicBSONObject fieldValue = lotDocument.getFieldValue();
                    if (fieldValue != null) {
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                            if (lotDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1) {
                                mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValueMapFromDb.get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                            }
                            for (String lotField : lotDbFieldNames) {
                                String[] split = lotField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                String componentType = split[1];
                                if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                    if (fieldValue.toMap().containsKey(lotField) && fieldValue.toMap().get(lotField) != null) {
                                        mapToSentOnUI.put(lotField, fieldValue.toMap().get(lotField));
                                    }
                                    if (fieldValue.toMap().containsKey(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                        mapToSentOnUI.put(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                    } else {
                                        if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                            if (mapOfDbFieldWithCurrencyCode.containsKey(lotField)) {
                                                String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(lotField);
                                                if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                    String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                    mapToSentOnUI.put(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(mapToSentOnUI)) {
                        selectItem.setCategoryCustom(mapToSentOnUI);
                    }
                    //Set screen rule related details.
                    if (lotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(lotFieldRuleDetailsMap)) {
                        System.out.println("lotFieldRuleDetailsMap:::" + lotFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(lotFieldRuleDetailsMap.get(lotDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = lotFieldRuleDetailsMap.get(lotDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            selectItem.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                        }
                    }
                    selectItems.add(selectItem);
                }
            }
        }
        return selectItems;
    }

    public Map<String, Object> createLot(LotDataBean lotDataBean) {
        HkLotDocument hkLotDocument = null;
        Map<String, Object> resultMap = new HashMap<>();
        if (lotDataBean != null) {
            List<String> uiList = new ArrayList<>();
            List<String> keysToRemove = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lotDataBean.getLotCustom())) {
                for (Map.Entry<String, String> entry : lotDataBean.getLotDbType().entrySet()) {
                    String key = entry.getKey();
                    uiList.add(key);
                }
                Map<String, Object> lotCustom = lotDataBean.getLotCustom();
                for (Map.Entry<String, Object> entry : lotCustom.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (!(value != null && value.toString() != null && value.toString().length() > 0)) {
                        keysToRemove.add(key);
                    }
                }
                if (!CollectionUtils.isEmpty(keysToRemove)) {
                    for (String key : keysToRemove) {
                        lotCustom.remove(key);
                    }
                }
            }
            Map<String, Object> map1 = lotDataBean.getRuleConfigMap();
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            if (!CollectionUtils.isEmpty(uiList)) {
                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                if (!CollectionUtils.isEmpty(lotDataBean.getLotCustom())) {
                    for (Map.Entry<String, Object> entry : lotDataBean.getLotCustom().entrySet()) {
                        String key = entry.getKey();
                        if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                            String type = uIFieldNameWithComponentTypes.get(key);
                            if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                if (lotDataBean.getLotDbType().containsKey(key)) {
                                    lotDataBean.getLotDbType().put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                }

                                if (lotDataBean.getLotCustom().containsKey(key)) {
                                    String customVal = lotDataBean.getLotCustom().get(key).toString();
                                    List<String> values = new ArrayList<>();
                                    String[] valueArray = customVal.replace("\"", "").split(",");
                                    for (String v : valueArray) {
                                        values.add(v.replace("\"", ""));
                                    }
                                    lotDataBean.getLotCustom().put(key, values);
                                }
                            }
                        }
                    }
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(lotDataBean.getLotCustom(), lotDataBean.getLotDbType(), HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null, null);
                    if (bSONObject != null) {
                        List<ObjectId> subLots = new ArrayList<>();
                        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.LOT);
                          List<HkFieldDocument> totalformulaList= customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
                        if (!CollectionUtils.isEmpty(lotDataBean.getSubLots())) {
                            for (String subLot : lotDataBean.getSubLots()) {
                                subLots.add(new ObjectId(subLot));
                            }
                        }
                        LOGGER.info("lotDataBean :" + lotDataBean);
                        hkLotDocument = stockService.createLot(bSONObject, totalformulaList, lotDataBean.getFranchise(), loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(lotDataBean.getInvoiceDataBean().getId()), new ObjectId(lotDataBean.getParcelDataBean().getId()), feature.getId(), lotDataBean.getSequenceNumber(), subLots);
                    }
                }
            }
            List<Long> notifyUsers = new ArrayList<>();
            if (hkLotDocument != null) {
                BasicBSONObject basicBSONObject = hkLotDocument.getFieldValue();
                if (basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF)) {
                    ArrayList<String> inStockOf = (ArrayList<String>) basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.IN_STOCK_OF);
                    if (inStockOf != null) {
                        String[] split = inStockOf.get(0).split(":");
                        if (split != null) {
                            Long userId = Long.parseLong(split[0]);
                            notifyUsers.add(userId);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(notifyUsers) && basicBSONObject.containsField(HkSystemConstantUtil.LotStaticFieldName.LOT_ID)) {
                    Map<String, Object> valuesMap = new HashMap<>();
                    valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LOT_ID, basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID).toString());
                    HkNotificationDocument notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.ALLOT_LOT,
                            HkSystemConstantUtil.NotificationInstanceType.ALLOT_LOT, valuesMap, null, loginDataBean.getCompanyId());
                    notificationService.sendNotification(notification, notifyUsers);
                }
                Map<String, Map<String, RuleDetailsDataBean>> lotFieldRuleDetailsMap = null;
                if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                    lotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(hkLotDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.LOT);
                }

                //Set screen rule related details.
                if (lotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(lotFieldRuleDetailsMap)) {
                    System.out.println("lotFieldRuleDetailsMap:::" + lotFieldRuleDetailsMap);
                    System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                    if (!CollectionUtils.isEmpty(lotFieldRuleDetailsMap.get(hkLotDocument.getId().toString()))) {
                        Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                        Map<String, RuleDetailsDataBean> ruleDetails = lotFieldRuleDetailsMap.get(hkLotDocument.getId().toString());
                        for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                            if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                            }
                        }
                        resultMap.put("screenRuleDetailsWithDbFieldName", screenRuleDetailsWithDbFieldName);
                    }
                }
                System.out.println("hkLotDocument.getId().toString():::" + hkLotDocument.getId().toString());
                System.out.println("basicBSONObject:::" + basicBSONObject);
//                System.out.println("HkSystemConstantUtil.LotStaticFieldName.LOT_ID=" + HkSystemConstantUtil.LotStaticFieldName.LOT_ID);
//                System.out.println("basicBSONObject.get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID)=" + basicBSONObject.getString(HkSystemConstantUtil.LotStaticFieldName.LOT_ID));
                resultMap.put(hkLotDocument.getId().toString(), basicBSONObject.getString(HkSystemConstantUtil.LotStaticFieldName.LOT_ID));
                resultMap.put("lotId", hkLotDocument.getId().toString());
                return resultMap;
            }

        }

        return null;
    }

    public SelectItem retrieveLotById(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            List<String> list = (List<String>) map.get("lotObjectId");
            ObjectId lotId = new ObjectId(list.get(0));
            List<String> parcelDbFieldNames = (List<String>) map.get("parcelDbFieldName");
            List<String> invoiceDbFieldNames = (List<String>) map.get("invoiceDbFieldName");
            List<String> lotDbFieldNames = (List<String>) map.get("lotDbFieldName");
            Map<String, Object> map1 = (Map<String, Object>) map.get("ruleConfigMap");
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            HkLotDocument lotDocument = stockService.retrieveLotById(lotId);
            if (lotDocument != null) {
                ObjectId invoice = lotDocument.getInvoice();
                ObjectId parcel = lotDocument.getParcel();
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(invoice);
                HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcel);
                if (invoiceDocument != null && parcelDocument != null && lotDocument != null) {
                    Map<String, Map<String, RuleDetailsDataBean>> lotFieldRuleDetailsMap = null;
                    if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                        lotFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(lotDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.LOT);
                    }
                    SelectItem item = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), invoiceDocument.getId().toString());
                    item.setCustom7(lotDocument.getFranchiseId());
                    item.setOtherId(lotDocument.getSequenceNumber());
                    if (!CollectionUtils.isEmpty(lotDocument.getSubLots())) {
                        List<String> subLots = new ArrayList<>();
                        for (ObjectId subLot : lotDocument.getSubLots()) {
                            subLots.add(subLot.toString());
                        }
                        item.setOtherValues(subLots);
                    }
                    BasicBSONObject fieldValue = lotDocument.getFieldValue();
                    if (fieldValue != null && !CollectionUtils.isEmpty(lotDbFieldNames)) {
                        Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                        for (String dbField : lotDbFieldNames) {
                            String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            String componentType = split[1];
                            if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                                break;
                            }
                        }
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        Map fieldValueMapFromDb = fieldValue.toMap();
                        if (lotDbFieldNames.indexOf(HkSystemConstantUtil.AutoNumber.LOT_ID) == -1) {
                            mapToSentOnUI.put(HkSystemConstantUtil.AutoNumber.LOT_ID, fieldValueMapFromDb.get(HkSystemConstantUtil.AutoNumber.LOT_ID));
                        }
                        mapToSentOnUI.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_CARAT, parcelDocument.getStockCarat());
                        mapToSentOnUI.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_PIECES, parcelDocument.getStockPieces());
                        for (String lotField : lotDbFieldNames) {
                            String[] split = lotField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            String componentType = split[1];
                            if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                if (fieldValue.toMap().containsKey(lotField)) {
                                    mapToSentOnUI.put(lotField, fieldValue.toMap().get(lotField));
                                }
                                if (fieldValue.toMap().containsKey(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                    mapToSentOnUI.put(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                } else {
                                    if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                        if (mapOfDbFieldWithCurrencyCode.containsKey(lotField)) {
                                            String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(lotField);
                                            if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                mapToSentOnUI.put(lotField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                            }

                                        }
                                    }
                                }

                            } else {
                                mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
                            }
                        }
                        item.setCustom1(mapToSentOnUI);
                    }
                    BasicBSONObject invoiceFieldValue = invoiceDocument.getFieldValue();
                    if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        Map fieldValueMapFromDb = invoiceFieldValue.toMap();
                        for (String invoiceField : invoiceDbFieldNames) {
                            mapToSentOnUI.put(invoiceField, fieldValueMapFromDb.get(invoiceField));
                        }
                        item.setCustom3(mapToSentOnUI);
                    }
                    BasicBSONObject parcelFieldValue = parcelDocument.getFieldValue();
                    if (parcelFieldValue != null && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                        Map<Object, Object> mapToSentOnUI = new HashMap<>();
                        Map fieldValueMapFromDb = parcelFieldValue.toMap();
                        for (String parcelField : parcelDbFieldNames) {
                            mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                        }
                        item.setCustom4(mapToSentOnUI);
                    }
                    //Set screen rule related details.
                    if (lotFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(lotFieldRuleDetailsMap)) {
                        System.out.println("lotFieldRuleDetailsMap:::" + lotFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(lotFieldRuleDetailsMap.get(lotDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = lotFieldRuleDetailsMap.get(lotDocument.getId().toString());
                            for (Map.Entry<String, RuleDetailsDataBean> entry : ruleDetails.entrySet()) {
                                if (fieldIdToNameMap.get(entry.getKey()) != null) {
                                    screenRuleDetailsWithDbFieldName.put(fieldIdToNameMap.get(entry.getKey()), entry.getValue());
                                }
                            }
                            item.setScreenRuleDetailsWithDbFieldName(screenRuleDetailsWithDbFieldName);
                        }
                    }
                    return item;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public String mergeLot(LotDataBean lotDataBean) {
        List<LotDataBean> lotList = lotDataBean.getLotList();
        List<BasicBSONObject> basicBSONObjects = new ArrayList<>();
        List<ObjectId> lotIdsTobeMerged = null;
        if (!CollectionUtils.isEmpty(lotList)) {
            lotIdsTobeMerged = new ArrayList<>();
            for (LotDataBean dataBean : lotList) {
                lotIdsTobeMerged.add(new ObjectId(dataBean.getId()));
            }
        }
        if (!CollectionUtils.isEmpty(lotList)) {
//            lotCreated = true;
            List<String> uiList = new ArrayList<>();
            for (LotDataBean dataBean : lotList) {
                if (!CollectionUtils.isEmpty(dataBean.getLotCustom())) {
                    for (Map.Entry<String, String> entry : dataBean.getLotDbType().entrySet()) {
                        String key = entry.getKey();
                        String type = entry.getValue();
                        uiList.add(key);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(uiList)) {

                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                for (LotDataBean lotDb : lotList) {
                    if (!CollectionUtils.isEmpty(lotDb.getLotCustom())) {
                        for (Map.Entry<String, Object> entry : lotDb.getLotCustom().entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                                String type = uIFieldNameWithComponentTypes.get(key);
                                if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                    if (lotDb.getLotDbType().containsKey(key)) {
                                        lotDb.getLotDbType().put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                    }

                                    if (lotDb.getLotCustom().containsKey(key)) {
                                        String customVal = lotDb.getLotCustom().get(key).toString();
                                        List<String> values = new ArrayList<>();
                                        String[] valueArray = customVal.replace("\"", "").split(",");
                                        for (String v : valueArray) {
                                            values.add(v.replace("\"", ""));
                                        }
                                        lotDb.getLotCustom().put(key, values);
                                    }
                                }
                            }

                        }
                    }
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(lotDb.getLotCustom(), lotDb.getLotDbType(), HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null, null);
                    if (bSONObject != null) {
                        basicBSONObjects.add(bSONObject);
                    }
                }
            }
        }
        //--------------------------------------------------------------------//
        //********************* here currently I have passed null for  workAllotment ids - Last parameter******************************//
        //--------------------------------------------------------------------//
        return stockService.mergeLot(new ObjectId(lotDataBean.getParcelDataBean().getId()), lotIdsTobeMerged, basicBSONObjects, loginDataBean.getCompanyId(), loginDataBean.getId(), null);
    }

    public List<String> splitLot(LotDataBean lotDataBean) {
        List<LotDataBean> lotDataBeans = lotDataBean.getLotList();
        Boolean lotCreated = false;
        if (!CollectionUtils.isEmpty(lotDataBeans)) {
            lotCreated = true;
            List<String> uiList = new ArrayList<>();
            for (LotDataBean dataBean : lotDataBeans) {
                if (!CollectionUtils.isEmpty(dataBean.getLotCustom())) {
                    for (Map.Entry<String, String> entry : dataBean.getLotDbType().entrySet()) {
                        String key = entry.getKey();
                        String type = entry.getValue();
                        uiList.add(key);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(uiList)) {
                List<BasicBSONObject> basicBSONObjects = new ArrayList<>();
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

                Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiList);
                for (LotDataBean lotDb : lotDataBeans) {
                    if (!CollectionUtils.isEmpty(lotDb.getLotCustom())) {
                        for (Map.Entry<String, Object> entry : lotDb.getLotCustom().entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                                String type = uIFieldNameWithComponentTypes.get(key);
                                if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                                    if (lotDb.getLotDbType().containsKey(key)) {
                                        lotDb.getLotDbType().put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                                    }

                                    if (lotDb.getLotCustom().containsKey(key)) {
                                        String customVal = lotDb.getLotCustom().get(key).toString();
                                        List<String> values = new ArrayList<>();
                                        String[] valueArray = customVal.replace("\"", "").split(",");
                                        for (String v : valueArray) {
                                            values.add(v.replace("\"", ""));
                                        }
                                        lotDb.getLotCustom().put(key, values);
                                    }
                                }
                            }

                        }
                    }
                    BasicBSONObject bSONObject = customFieldService.makeBSONObject(lotDb.getLotCustom(), lotDb.getLotDbType(), HkSystemConstantUtil.Feature.LOT, loginDataBean.getCompanyId(), null, null);
                    if (bSONObject != null) {
                        basicBSONObjects.add(bSONObject);
                    }
                }
                //--------------------------------------------------------------------//
                //********************* here currently I have passed null for  workAllotment ids - Last parameter******************************//
                //--------------------------------------------------------------------//
                return stockService.splitLot(new ObjectId(lotDataBean.getParcelDataBean().getId()), new ObjectId(lotDataBean.getId()), basicBSONObjects, loginDataBean.getCompanyId(), loginDataBean.getId(), null);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

//    public List<SelectItem> retrieveLots() {
//        List<Long> users = new ArrayList<>();
//        users.add(loginDataBean.getId());
//        Long companyId = loginDataBean.getCompanyId();
//        List<HkLotDocument> lotDocuments = workAllotmentService.retrieveLotsOrPacketsFromWorkAllocation(null, users, null, Arrays.asList(HkSystemConstantUtil.ActivityServiceStatus.IN_PROCESS), Arrays.asList(HkSystemConstantUtil.SERVICE_CODE.ADD_PACKET), companyId, Boolean.FALSE, Boolean.TRUE);
//        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch = null;
//        Map<String, List<String>> entityNameAndModelMap = null;
//        List<SelectItem> packetItems = null;
//        List<SelectItem> lotItems = null;
//        if (CollectionUtils.isEmpty(lotDocuments)) {
//            return null;
//        } else {
//            retrieveFieldsConfigureForSearch = this.retrieveFieldsConfigureForSearch();
//            List<String> dbFieldNames = null;
//            List<String> invoiceDbFieldName = null;
//            List<String> parcelDbFieldName = null;
//            List<String> lotDbFieldName = null;
//            Map<Object, Object> modelAndLabelMap = null;
//            if (!CollectionUtils.isEmpty(retrieveFieldsConfigureForSearch)) {
//                entityNameAndModelMap = new HashMap<>();
//                modelAndLabelMap = new HashMap<>();
//                for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : retrieveFieldsConfigureForSearch.entrySet()) {
//                    HkSectionDocument key = entry.getKey();
//                    List<HkFeatureFieldPermissionDocument> list = entry.getValue();
//                    if (!CollectionUtils.isEmpty(list)) {
//                        for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : list) {
//                            if (hkFeatureFieldPermissionEntity.getSearchFlag()) {
//                                dbFieldNames = new ArrayList<>();
//                                String dbFieldName = hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName();
//                                modelAndLabelMap.put(hkFeatureFieldPermissionEntity.getHkFieldEntity().getFieldLabel(), hkFeatureFieldPermissionEntity.getHkFieldEntity().getDbFieldName());
//                                dbFieldNames.add(dbFieldName);
//                                if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                                    if (CollectionUtils.isEmpty(invoiceDbFieldName)) {
//                                        invoiceDbFieldName = new ArrayList<>();
//                                    }
//                                    invoiceDbFieldName.add(dbFieldName);
//                                } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
//                                    if (CollectionUtils.isEmpty(parcelDbFieldName)) {
//                                        parcelDbFieldName = new ArrayList<>();
//                                    }
//                                    parcelDbFieldName.add(dbFieldName);
//                                } else if (hkFeatureFieldPermissionEntity.getEntityName().equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
//                                    if (CollectionUtils.isEmpty(lotDbFieldName)) {
//                                        lotDbFieldName = new ArrayList<>();
//                                    }
//                                    lotDbFieldName.add(dbFieldName);
//                                }
//                                entityNameAndModelMap.put(hkFeatureFieldPermissionEntity.getEntityName(), dbFieldNames);
//                            }
//                        }
//
//                    }
//                }
//                if (!CollectionUtils.isEmpty(modelAndLabelMap)) {
//                    Boolean invoiceFieldExistForSearch = false;
//                    Boolean parcelFieldExistForSearch = false;
//                    Boolean lotFieldExistForSearch = false;
//                    for (Map.Entry<String, List<String>> entry : entityNameAndModelMap.entrySet()) {
//                        String featureName = entry.getKey();
//                        List<String> list = entry.getValue();
//                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                            invoiceFieldExistForSearch = true;
//                        } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
//                            parcelFieldExistForSearch = true;
//                        } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
//                            lotFieldExistForSearch = true;
//                        }
//                    }
//                    List<ObjectId> invoiceIds = null;
//                    List<ObjectId> parcelIds = null;
//                    List<ObjectId> lotIds = null;
//
////                    retrieve lot details
//                    if (!CollectionUtils.isEmpty(lotDocuments)) {
//                        invoiceIds = new ArrayList<>();
//                        parcelIds = new ArrayList<>();;
//                        for (HkLotDocument lotDocument : lotDocuments) {
//                            invoiceIds.add(lotDocument.getInvoice());
//                            parcelIds.add(lotDocument.getParcel());
////                            workAllotmentIds.add(lotDocument.getWorkAllotmentId());
//                        }
//                        List<HkInvoiceDocument> invoiceDocumentForLot = null;
//                        if (invoiceFieldExistForSearch) {
//                            invoiceDocumentForLot = stockService.retrieveInvoices(invoiceIds, companyId, Boolean.FALSE);
//                        }
//                        List<HkParcelDocument> parcelDocumentForLot = null;
//                        if (parcelFieldExistForSearch) {
//                            parcelDocumentForLot = stockService.retrieveParcels(parcelIds, companyId, Boolean.FALSE);
//                        }
//                        lotItems = new ArrayList<>();
//                        Integer index = 0;
//                        for (HkLotDocument lotDocument : lotDocuments) {
//                            Map<Object, Object> mapToSentOnUI = new HashMap<>();
//                            String issuedDocumentId = null;
//                            if (lotDocument.getIssueDocument() != null) {
//                                issuedDocumentId = lotDocument.getIssueDocument().getId().toString();
//                            }
//                            SelectItem selectItem = new SelectItem(lotDocument.getId().toString(), lotDocument.getParcel().toString(), lotDocument.getInvoice().toString());
//                            selectItem.setCustom7(lotDocument.getFranchiseId());
//                            Map fieldValueMapFromDb = lotDocument.getFieldValue().toMap();
//                            mapToSentOnUI.put(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT, fieldValueMapFromDb.get(HkSystemConstantUtil.MANDATORY_FIELD.CARAT_OF_LOT));
//                            if (lotFieldExistForSearch && !CollectionUtils.isEmpty(lotDbFieldName)) {
//                                for (String lotField : lotDbFieldName) {
//                                    if (fieldValueMapFromDb.get(lotField) != null) {
//                                        mapToSentOnUI.put(lotField, fieldValueMapFromDb.get(lotField));
//                                    }
//                                }
//                            }
//                            if (invoiceFieldExistForSearch && !CollectionUtils.isEmpty(invoiceDbFieldName)) {
//                                for (HkInvoiceDocument invoiceDocument : invoiceDocumentForLot) {
//                                    if (invoiceDocument.getId().toString().equals(lotDocument.getInvoice().toString())) {
//                                        for (String invoiceField : invoiceDbFieldName) {
//                                            Object get = invoiceDocument.getFieldValue().toMap().get(invoiceField);
//                                            if (get != null) {
//                                                mapToSentOnUI.put(invoiceField, get.toString());
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            if (parcelFieldExistForSearch && !CollectionUtils.isEmpty(parcelDbFieldName)) {
//                                for (HkParcelDocument parcelDocument : parcelDocumentForLot) {
//                                    if (parcelDocument.getId().toString().equals(lotDocument.getParcel().toString())) {
//                                        for (String parcelField : parcelDbFieldName) {
//                                            Object parcelObject = parcelDocument.getFieldValue().toMap().get(parcelField);
//                                            if (parcelObject != null) {
//                                                mapToSentOnUI.put(parcelField, parcelObject.toString());
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            selectItem.setCustom1(mapToSentOnUI);
//                            lotItems.add(selectItem);
//                            if (!CollectionUtils.isEmpty(packetItems)) {
//                                lotItems.addAll(packetItems);
//                            }
//                            index++;
//                        }
//                    }
//                }
//            } else {
//                return null;
//            }
//        }
//        return lotItems;
//    }
    private Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFieldsConfigureForSearch() {

        List<HkFieldDocument> fieldEntityList = null;
        //Retrieved FeatureId
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get("packetAdd");
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> fieldPermissionEntitys = new HashMap<>();
        if (feature != null) {
            fieldPermissionEntitys = customFieldService.retrieveFeatureFieldPermissionForSearch(feature.getId(), loginDataBean.getRoleIds());
            if (!CollectionUtils.isEmpty(fieldPermissionEntitys)) {
                fieldEntityList = new LinkedList<>();
                for (Map.Entry<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> entry : fieldPermissionEntitys.entrySet()) {
                    HkSectionDocument hkSectionEntity = entry.getKey();
                    List<HkFeatureFieldPermissionDocument> list = entry.getValue();
                    if (!CollectionUtils.isEmpty(list)) {
                        for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : list) {
                            if (hkFeatureFieldPermissionEntity.getHkFieldEntity() != null) {
                                fieldEntityList.add(hkFeatureFieldPermissionEntity.getHkFieldEntity());
                            }
                        }
                    }
                }

            }
        }
        return fieldPermissionEntitys;
    }

    public SelectItem retrieveLot(Map<String, List<String>> featureDbfieldNameMap) {
        List<String> ids = featureDbfieldNameMap.get("id");
        Long companyId = loginDataBean.getCompanyId();
        SelectItem selectItem = null;
        HkLotDocument lotDocument = stockService.retrieveLotById(new ObjectId(ids.get(0)));
        if (lotDocument != null && !CollectionUtils.isEmpty(featureDbfieldNameMap)) {
            ObjectId lotId = lotDocument.getId();
            ObjectId invoiceId = lotDocument.getInvoice();
            ObjectId parcelId = lotDocument.getParcel();
            selectItem = new SelectItem(lotId.toString(), parcelId.toString(), invoiceId.toString());
            selectItem.setCustom7(lotDocument.getFranchiseId());
            selectItem.setOtherId(lotDocument.getSequenceNumber());
            if (!CollectionUtils.isEmpty(lotDocument.getSubLots())) {
                List<String> subLots = new ArrayList<>();
                for (ObjectId subLot : lotDocument.getSubLots()) {
                    subLots.add(subLot.toString());
                }
                selectItem.setOtherValues(subLots);
            }
            Boolean invoiceFieldExist = false;
            Boolean parcelFieldExist = false;
            Boolean lotFieldExist = false;
            List<String> invoiceDbFieldNames = null;
            List<String> parcelDbFieldNames = null;
            List<String> lotDbFieldNames = null;
            for (Map.Entry<String, List<String>> entry : featureDbfieldNameMap.entrySet()) {
                String featureName = entry.getKey();
                if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceFieldExist = true;
                    invoiceDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelFieldExist = true;
                    parcelDbFieldNames = entry.getValue();
                } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)) {
                    lotFieldExist = true;
                    lotDbFieldNames = entry.getValue();
                }
            }
            if (invoiceFieldExist) {
                HkInvoiceDocument invoiceDocument = stockService.retrieveInvoiceById(invoiceId);
                Map<String, Object> invoiceFieldValueMap = invoiceDocument.getFieldValue().toMap();
                if (!CollectionUtils.isEmpty(invoiceDbFieldNames)) {
                    Map<Object, Object> invoiceMap = new HashMap<>();
                    for (String invoiceField : invoiceDbFieldNames) {
                        Object value = invoiceFieldValueMap.get(invoiceField);
                        invoiceMap.put(invoiceField, value);
                    }
                    selectItem.setCustom1(invoiceMap);
                }
            }
            if (parcelFieldExist) {
                HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcelId);
                Map<String, Object> parcelFieldValueMap = new HashMap();
                if (parcelDocument != null) {
                    parcelFieldValueMap = parcelDocument.getFieldValue().toMap();
                }
                if (!CollectionUtils.isEmpty(parcelDbFieldNames) && parcelDocument != null) {
                    Map<Object, Object> parcelMap = new HashMap<>();
                    for (String parcelField : parcelDbFieldNames) {
                        Object value = parcelFieldValueMap.get(parcelField);
                        parcelMap.put(parcelField, value);
                    }
                    selectItem.setCustom3(parcelMap);
                }
            }
            if (lotFieldExist) {
                Map<String, Object> lotFieldValueMap = lotDocument.getFieldValue().toMap();
                if (!CollectionUtils.isEmpty(lotDbFieldNames)) {
                    Map<Object, Object> lotMap = new HashMap<>();
                    lotMap.put(HkSystemConstantUtil.LotStaticFieldName.LOT_ID, lotFieldValueMap.get(HkSystemConstantUtil.LotStaticFieldName.LOT_ID));
                    lotMap.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_CARAT, lotDocument.getStockCarat());
                    lotMap.put(HkSystemConstantUtil.LotStaticFieldName.STOCK_PIECES, lotDocument.getStockPieces());
                    for (String lotField : lotDbFieldNames) {
                        Object value = lotFieldValueMap.get(lotField);
                        lotMap.put(lotField, value);
                    }
                    selectItem.setCustom4(lotMap);
                }
            }
        }
        return selectItem;
    }

    public ResponseEntity<Boolean> deleteLot(ObjectId lotId) {
        List<HkPacketDocument> activePackets = stockService.retrievePackets(null, null, null, Arrays.asList(lotId), loginDataBean.getCompanyId(), false, null, null,null,null);
        if (CollectionUtils.isEmpty(activePackets)) {
            HkLotDocument lotObj = stockService.retrieveLotById(lotId);
            if (lotObj.getStatus().equals(HkSystemConstantUtil.StockStatus.IN_PRODUCTION)) {
                return new ResponseEntity<>(null, ResponseCode.FAILURE, "Lot is already In Production, please terminate it first", null, true);
            }
            stockService.deleteLot(lotId, loginDataBean.getId());
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Lot deleted successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Can not delete Lot because it is having packets", null, true);
        }
    }

    public List<SelectItem> retrieveFranchiseDetails() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<UmCompanyDocument> companyDocuments = hkUserService.retrieveFranchises();
        if (!CollectionUtils.isEmpty(companyDocuments)) {
            for (UmCompanyDocument companyDocument : companyDocuments) {
                SelectItem selectItem = new SelectItem(companyDocument.getId(), companyDocument.getName(), companyDocument.getCompanyCode());
                selectItems.add(selectItem);
            }
        }
        return selectItems;
    }

    public Map<String, Object> getNextLotSequence(Long franchise) {
        Map<String, Object> lotSequenceMap = new HashMap<>();
        Long nextLotSequence = stockService.getNextLotSequence(franchise);
        if (nextLotSequence != null) {
            nextLotSequence = nextLotSequence + 1l;
            String formatted = String.format("%03d", nextLotSequence);
            System.out.println("nextLotSequence :" + nextLotSequence);
            lotSequenceMap.put("sequenceNumber", formatted);
        }
        Calendar now = Calendar.getInstance();
        Integer year = now.get(Calendar.YEAR);
        System.out.println("year :" + year);
        lotSequenceMap.put("year", year);
        UmCompanyDocument umCompanyDocument = hkUserService.retrieveFranchiseById(franchise);
        if (umCompanyDocument != null) {
            if (umCompanyDocument.getCompanyCode() != null) {
                lotSequenceMap.put("franchiseCode", umCompanyDocument.getCompanyCode());
            }
        }
        return lotSequenceMap;
    }

    private Map<String, Object> prepareAndValidateFieldMap(Map<String, Object> map, Map<String, Object> fieldMap) {
        Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                if (value instanceof String && !StringUtils.isEmpty(value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                    List<String> items = Arrays.asList(value.toString().split(","));
                    fieldMap.put(key, items);
                } else {
                    iter.remove();
                }
            } else {
                fieldMap.put(key, value);
            }
        }
        return fieldMap;
    }

    public boolean isLotSequenceNumberExist(Map<String, String> franchiseSeqNumber) {
        Boolean lotSequenceExist = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(franchiseSeqNumber)) {
            Long franchiseId = new Long(franchiseSeqNumber.get("franchise").toString());
            Long seqNumber = new Long(franchiseSeqNumber.get("seqNumber").toString());
            if (franchiseId != null && seqNumber != null) {
                lotSequenceExist = stockService.isLotSequenceExist(seqNumber, franchiseId);
            }
        }
        return lotSequenceExist;
    }
}
