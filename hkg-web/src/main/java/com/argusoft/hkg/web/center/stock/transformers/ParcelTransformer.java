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
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.UMFeatureDetailDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
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
public class ParcelTransformer {

    @Autowired
    HkStockService stockService;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HkCustomFieldService customFieldService;
    @Autowired
    ApplicationUtil applicationUtil;
    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;

    public SelectItem createParcel(ParcelDataBean parcelDataBean) {
        if (parcelDataBean != null) {
            SelectItem selectItem = null;
            Map<String, Object> parcelCustomMap = parcelDataBean.getParcelCustom();
            Map<String, String> parcelDbTypeMap = parcelDataBean.getParcelDbType();
            List<String> keysToRemove = new ArrayList<>();
            for (Map.Entry<String, Object> entry : parcelCustomMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!(value != null && value.toString() != null && value.toString().length() > 0)) {
                    keysToRemove.add(key);
                }
            }
            if (!CollectionUtils.isEmpty(keysToRemove)) {
                for (String key : keysToRemove) {
                    parcelCustomMap.remove(key);
                }
            }
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(parcelDbTypeMap)) {
                for (Map.Entry<String, String> entrySet : parcelDbTypeMap.entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }

            List<BasicBSONObject> basicBSONObjects = new ArrayList<>();
            Map<String, String> uIFieldNameWithComponentTypes = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;

            if (!CollectionUtils.isEmpty(parcelDataBean.getParcelCustom())) {
                for (Map.Entry<String, Object> entry : parcelDataBean.getParcelCustom().entrySet()) {
                    String key = entry.getKey();
                    if (key != null && uIFieldNameWithComponentTypes.containsKey(key)) {
                        String type = uIFieldNameWithComponentTypes.get(key);
                        if (type.equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || type.equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT) || type.equals(pointerComponentType)) {
                            if (parcelDataBean.getParcelDbType().containsKey(key)) {
                                parcelDataBean.getParcelDbType().put(key, HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                            }

                            if (parcelDataBean.getParcelCustom().containsKey(key)) {
                                String customVal = parcelDataBean.getParcelCustom().get(key).toString();
                                List<String> values = new ArrayList<>();
                                String[] valueArray = customVal.replace("\"", "").split(",");
                                for (String v : valueArray) {
                                    values.add(v.replace("\"", ""));
                                }
                                parcelDataBean.getParcelCustom().put(key, values);
                            }
                        }
                    }
                }

                Map<String, String> autogeneratedLabelMap = new HashMap<>();
                String year = new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString();
                autogeneratedLabelMap.put("beforeLabel", year + "-");

                BasicBSONObject bSONObject = customFieldService.makeBSONObject(parcelDataBean.getParcelCustom(), parcelDataBean.getParcelDbType(), HkSystemConstantUtil.Feature.PARCEL, loginDataBean.getCompanyId(), null, autogeneratedLabelMap);
                if (bSONObject != null) {
                    basicBSONObjects.add(bSONObject);
                }
                UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
//                Commmented by Shifa temporarly
                List<HkFieldDocument> totalformulaList = customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
                String parcelId = stockService.createParcel(bSONObject, totalformulaList, loginDataBean.getCompanyId(), loginDataBean.getId(), new ObjectId(parcelDataBean.getInvoiceDataBean().getId()), feature.getId(), parcelDataBean.getSequenceNumber());
                if (parcelId != null) {
                    parcelDataBean.getFeatureDbFieldMap().put("parcelObjectId", Arrays.asList(parcelId));
                    selectItem = this.retrieveParcelById(parcelDataBean.getFeatureDbFieldMap(), parcelDataBean.getRuleConfigMap());
                }
                return selectItem;
            }

        }
        return null;
    }

    public SelectItem updateParcel(ParcelDataBean parcelDataBean) {
        SelectItem selectItem = null;
        Map<String, Object> parcelCustomMap = parcelDataBean.getParcelCustom();
        if (!CollectionUtils.isEmpty(parcelCustomMap)) {
            if (parcelCustomMap.containsKey("parcelDbObjectId")) {
                parcelCustomMap.remove("parcelDbObjectId");
            }
            if (parcelCustomMap.containsKey("linkPurchase")) {
                parcelCustomMap.remove("linkPurchase");
            }
        }
        Map<String, String> parcelDbTypeMap = parcelDataBean.getParcelDbType();
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(parcelDbTypeMap)) {
            for (Map.Entry<String, String> entrySet : parcelDbTypeMap.entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> autogeneratedLabelMap = new HashMap<>();
        autogeneratedLabelMap.put("beforeLabel", parcelDataBean.getYear().toString() + "-");
        Map<String, String> uiFieldMap = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
        Map<String, HkFieldDocument> mapForFeatureInvolvedInFormula = customFieldService.retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(loginDataBean.getCompanyId(), HkSystemConstantUtil.Feature.PARCEL, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Map<String, String> lotUIFieldMap = null;
        List<HkFieldDocument> totalformulaList =customFieldService.retrieveAllFormulaList(feature.getId(), loginDataBean.getCompanyId(), HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
        Boolean result = stockService.updateParcel(new ObjectId(parcelDataBean.getId()), parcelCustomMap, parcelDbTypeMap, mapForFeatureInvolvedInFormula, uiFieldMap, lotUIFieldMap, totalformulaList, applicationUtil.getFeatureIdNameMap(), loginDataBean.getCompanyId(), loginDataBean.getId(), feature.getId(), parcelDataBean.getSequenceNumber(), autogeneratedLabelMap);
        if (result) {
            if (!CollectionUtils.isEmpty(parcelDataBean.getFeatureDbFieldMap())) {
                parcelDataBean.getFeatureDbFieldMap().put("parcelObjectId", Arrays.asList(parcelDataBean.getId()));
                selectItem = this.retrieveParcelById(parcelDataBean.getFeatureDbFieldMap(), parcelDataBean.getRuleConfigMap());
            }
        }
        return selectItem;
    }

    public Boolean deleteParcel(ObjectId parcelId) {
        List<HkLotDocument> activeLots = stockService.retrieveLots(null, null, Arrays.asList(parcelId), null, loginDataBean.getCompanyId(), false);
        if (CollectionUtils.isEmpty(activeLots)) {
            stockService.deleteParcel(parcelId, loginDataBean.getId());
            return true;
        } else {
            return false;
        }
    }

    public List<SelectItem> searchParcel(ParcelDataBean parcelDataBean) {
        Map<String, List<String>> featureDbFieldMap = parcelDataBean.getFeatureDbFieldMap();
        List<String> invoiceDbField = null;
        List<String> parcelDbField = null;
        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("invoice"))) {
                invoiceDbField = featureDbFieldMap.get("invoice");
            }
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("parcel"))) {
                parcelDbField = featureDbFieldMap.get("parcel");
            }

        }
        Map<String, Object> map1 = parcelDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        Long franchise = loginDataBean.getCompanyId();
        Map<String, Map<String, Object>> featureCustomMapValue = parcelDataBean.getFeatureCustomMapValue();
        Map<String, Object> invoiceFieldMap = new HashMap<>();
        Map<String, Object> parcelFieldMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(featureCustomMapValue) || parcelDataBean.isSearchOnParameter() == false) {
            if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
                for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                    String featureName = entry.getKey();
                    Map<String, Object> map = entry.getValue();
                    if (!StringUtils.isEmpty(featureName)) {
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                            if (!CollectionUtils.isEmpty(map)) {
                                for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                    String key = entry1.getKey();
                                    Object value = entry1.getValue();
                                    invoiceFieldMap.put(key, value);
                                }
                            }

                        } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                            if (!CollectionUtils.isEmpty(map)) {
                                for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                    String key = entry1.getKey();
                                    Object value = entry1.getValue();
                                    parcelFieldMap.put(key, value);
                                }
                            }

                        }
                    }
                }
            }
            List<HkInvoiceDocument> invoiceDocuments = null;
            if (!CollectionUtils.isEmpty(invoiceFieldMap)) {
                Iterator<Map.Entry<String, Object>> iter = invoiceFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            invoiceFieldMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            invoiceFieldMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
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
                if (!CollectionUtils.isEmpty(parcelFieldMap)) {
                    Iterator<Map.Entry<String, Object>> iter = parcelFieldMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Object> entry = iter.next();
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                            if (value instanceof String && !StringUtils.isEmpty(value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                                List<String> items = Arrays.asList(value.toString().split(","));
                                parcelFieldMap.put(key, items);
                            } else {
                                iter.remove();
                            }
                        }
                    }
                }
            }
            List<ObjectId> parcelIds = null;
            if (parcelDataBean.isSearchOnParameter() != null && parcelDataBean.isSearchOnParameter() == false && parcelDataBean.getId() != null) {
                parcelIds = new ArrayList<>();
                String[] parcelId = parcelDataBean.getId().split(",");
                for (String string : parcelId) {
                    parcelIds.add(new ObjectId(string.replaceAll("\\s+", "")));
                }
            }
            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds) || !CollectionUtils.isEmpty(parcelIds)) {
                parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, parcelIds, franchise, Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,null,null);
                if (CollectionUtils.isEmpty(parcelDocuments)) {
                    return null;
                }
            }
            List<SelectItem> selectItems = null;
            if (!CollectionUtils.isEmpty(parcelDocuments)) {
                selectItems = new ArrayList<>();
                invoiceIds = new ArrayList<>();
                for (HkParcelDocument parcelDocument : parcelDocuments) {
                    invoiceIds.add(parcelDocument.getInvoice());
                }

                List<HkInvoiceDocument> hkInvoiceDocuments = stockService.retrieveInvoices(invoiceIds, franchise, Boolean.FALSE);
                Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
                if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                    parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(parcelDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
                }
                if (!CollectionUtils.isEmpty(hkInvoiceDocuments)) {
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        Map<String, Object> map = new HashMap<>();
                        SelectItem selectItem = new SelectItem(parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());
                        BasicBSONObject fieldValue = parcelDocument.getFieldValue();
                        if (fieldValue != null) {
                            map.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_CARAT, parcelDocument.getStockCarat());
                            map.put(HkSystemConstantUtil.ParcelStaticFieldName.STOCK_PIECES, parcelDocument.getStockPieces());
                            map.put("parcelDbObjectId", parcelDocument.getId().toString());
                        }
                        if (fieldValue != null && !CollectionUtils.isEmpty(parcelDbField)) {
                            for (String dbField : parcelDbField) {
                                if (fieldValue.toMap().containsKey(dbField)) {
                                    map.put(dbField, fieldValue.toMap().get(dbField));
                                }
                            }
                        }
                        for (HkInvoiceDocument hkInvoiceDocument : hkInvoiceDocuments) {
                            if (hkInvoiceDocument.getId().toString().equals(parcelDocument.getInvoice().toString())) {
                                BasicBSONObject invoiceFieldValue = hkInvoiceDocument.getFieldValue();
                                if (invoiceFieldValue != null && !CollectionUtils.isEmpty(invoiceDbField)) {
                                    for (String dbField : invoiceDbField) {
                                        if (invoiceFieldValue.toMap().containsKey(dbField)) {
                                            map.put(dbField, invoiceFieldValue.toMap().get(dbField));
                                        }
                                    }
                                }
                                selectItem.setDescription(hkInvoiceDocument.getId().toString());
                                break;
                            }
                        }
                        //Set screen rule related details.
                        if (parcelFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(parcelFieldRuleDetailsMap)) {
                            if (!CollectionUtils.isEmpty(parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString());
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
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public SelectItem retrieveParcelById(Map<String, List<String>> map, Map<String, Object> ruleConfigMap) {
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
            System.out.println("fieldIds-----" + fieldIds);
            ObjectId parcelObjecId = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("parcelObjectId")) {
                    parcelObjecId = new ObjectId(entry.getValue().get(0));
                    break;
                }
            }
            HkParcelDocument parcelDocument = stockService.retrieveParcelById(parcelObjecId);
            Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(parcelDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
            }
            List<SelectItem> selectItems = this.convertModelToUiData(Arrays.asList(parcelDocument), map, parcelFieldRuleDetailsMap, fieldIdToNameMap);
            if (!CollectionUtils.isEmpty(selectItems) && selectItems.get(0) != null) {
                selectItem = selectItems.get(0);
            }
        }
        return selectItem;
    }

    public List<SelectItem> retrieveAllParcels(ParcelDataBean parcelDataBean) {
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.StockStatus.NEW_ROUGH);
        statusList.add(HkSystemConstantUtil.StockStatus.PARTIALLY_MERGED);
        statusList.add(HkSystemConstantUtil.StockStatus.PARTIALLY_SOLD);
        Map<String, List<String>> map = parcelDataBean.getFeatureDbFieldMap();
        List<String> parcelIds = map.get("parcelIds");
        List<ObjectId> parcelObjectIds = new ArrayList<ObjectId>();
        if (!CollectionUtils.isEmpty(parcelIds)) {
            for (String id : parcelIds) {
                parcelObjectIds.add(new ObjectId(id));
            }
        }
        Map<String, Object> map1 = parcelDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        List<HkParcelDocument> hkParcelDocuments = stockService.retrieveAllParcel(0l, Boolean.FALSE, statusList, parcelObjectIds);
        Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
            parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkParcelDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
        }

        List<SelectItem> selectItems = this.convertModelToUiData(hkParcelDocuments, map, parcelFieldRuleDetailsMap, fieldIdToNameMap);
        return selectItems;
    }

    public List<SelectItem> retrieveParcelsByInvoice(ParcelDataBean parcelDataBean) {
        List<ObjectId> invoiceIds = new ArrayList<>();
        List<SelectItem> selectItems = new ArrayList<>();
        if (parcelDataBean != null && !StringUtils.isEmpty(parcelDataBean.getFeatureDbFieldMap())) {
            Map<String, List<String>> map = parcelDataBean.getFeatureDbFieldMap();
            ObjectId invoiceId = new ObjectId(map.get("invoiceObjectId").get(0));
            invoiceIds.add(invoiceId);
            List<String> parcelDbFieldNames = map.get("dbFieldNames");
            Map<String, Object> map1 = parcelDataBean.getRuleConfigMap();
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            UMFeatureDetailDataBean feature = applicationUtil.getuMFeatureDetailDataBeanMap().get(HkSystemConstantUtil.Feature.PARCEL);
            if (feature != null) {
                List<HkParcelDocument> parcelDocuments = stockService.retrieveParcels(null, invoiceIds, null, loginDataBean.getCompanyId(), Boolean.FALSE, null, HkSystemConstantUtil.StockStatus.NEW_ROUGH,parcelDataBean.getOffset(),parcelDataBean.getLimit());
                Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
                if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                    parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(parcelDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
                }

                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    Map<String, String> mapOfDbFieldWithCurrencyCode = null;
                    for (String dbField : parcelDbFieldNames) {
                        String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                        String componentType = split[1];
                        if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                            mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
                            break;
                        }
                    }
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        Map<String, Object> mapToSentOnUI = new HashMap<>();
                        SelectItem selectItem = new SelectItem(parcelDocument.getId().toString(), parcelDocument.getInvoice().toString());
                        selectItem.setHaveValue(Boolean.FALSE);
                        selectItem.setYear(parcelDocument.getYear());
                        BasicBSONObject fieldValue = parcelDocument.getFieldValue();
                        if (fieldValue != null) {
                            Map fieldValueMapFromDb = fieldValue.toMap();
                            if (!CollectionUtils.isEmpty(fieldValueMapFromDb) && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                                mapToSentOnUI.put("parcelDbObjectId", parcelDocument.getId().toString());
                                mapToSentOnUI.put("linkPurchase", parcelDocument.getIsLinked());
                                for (String parcelField : parcelDbFieldNames) {
                                    String[] split = parcelField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                                    String componentType = split[1];
                                    if (componentType.equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY))) {
                                        if (fieldValue.toMap().containsKey(parcelField)) {
                                            mapToSentOnUI.put(parcelField, fieldValue.toMap().get(parcelField));
                                        }
                                        if (fieldValue.toMap().containsKey(parcelField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                                            mapToSentOnUI.put(parcelField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, fieldValue.toMap().get(parcelField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM));
                                        } else {
                                            if (mapOfDbFieldWithCurrencyCode != null && !mapOfDbFieldWithCurrencyCode.isEmpty()) {
                                                if (mapOfDbFieldWithCurrencyCode.containsKey(parcelField)) {
                                                    String currencyCodeVal = mapOfDbFieldWithCurrencyCode.get(parcelField);
                                                    if (StringUtils.hasLength(currencyCodeVal) && StringUtils.hasText(currencyCodeVal)) {
                                                        String currencyCode = currencyCodeVal.toString().replaceAll("^\"|\"$", "");
                                                        mapToSentOnUI.put(parcelField + HkSystemConstantUtil.CURRENCY_CODE_CUSTOM, currencyCode);
                                                    }

                                                }
                                            }
                                        }

                                    } else {
                                        mapToSentOnUI.put(parcelField, fieldValueMapFromDb.get(parcelField));
                                    }
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(mapToSentOnUI)) {
                            selectItem.setCategoryCustom(mapToSentOnUI);
                        }
                        //Set screen rule related details.
                        if (parcelFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(parcelFieldRuleDetailsMap)) {
                            System.out.println("parcelFieldRuleDetailsMap:::" + parcelFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = parcelFieldRuleDetailsMap.get(parcelDocument.getId().toString());
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
        }
        return selectItems;
    }

    public Integer getNextParcelSequence() {
        Integer nextParcelSequence = stockService.getNextParcelSequence();
        if (nextParcelSequence != null) {
            return ++nextParcelSequence;
        }
        return null;
    }

    public Boolean isParcelIdExists(Integer parcelId, String parcelObjectId) {
        Boolean parcelSequenceExist = Boolean.FALSE;
        if (parcelObjectId != null) {
            parcelSequenceExist = stockService.isParcelSequenceExist(parcelId, new ObjectId(parcelObjectId));
        } else {
            parcelSequenceExist = stockService.isParcelSequenceExist(parcelId, null);
        }
        return parcelSequenceExist;
    }

    public List<SelectItem> convertModelToUiData(List<HkParcelDocument> hkParcelDocuments, Map<String, List<String>> map, Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap, Map<String, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(hkParcelDocuments)) {
            for (HkParcelDocument hkParcelDocument : hkParcelDocuments) {
                if (hkParcelDocument != null) {
                    SelectItem selectItem = new SelectItem(hkParcelDocument.getId().toString(), null);
                    selectItem.setYear(hkParcelDocument.getYear());
                    selectItem.setBeforeLabel(hkParcelDocument.getYear().toString() + "-");
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    mapToSentOnUI.put("Stock Carat", hkParcelDocument.getStockCarat());
                    mapToSentOnUI.put("Stock Pieces", hkParcelDocument.getStockPieces());
                    mapToSentOnUI.put("linkPurchase", hkParcelDocument.getIsLinked());
                    BasicBSONObject fieldValue = hkParcelDocument.getFieldValue();
                    List<String> parcelDbFieldNames = map.get("dbFieldNames");
                    if (fieldValue != null && !CollectionUtils.isEmpty(parcelDbFieldNames)) {
                        for (String dbField : parcelDbFieldNames) {
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
                        if (parcelFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(parcelFieldRuleDetailsMap)) {
                            System.out.println("parcelFieldRuleDetailsMap:::" + parcelFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(parcelFieldRuleDetailsMap.get(hkParcelDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = parcelFieldRuleDetailsMap.get(hkParcelDocument.getId().toString());
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

    public List<SelectItem> retrieveAssociatedRoughParcels(ParcelDataBean parcelDataBean) {
        Map<String, Object> map1 = parcelDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        List<HkParcelDocument> hkParcelDocuments = stockService.retrieveNotAssociatedParcels();
        Map<String, Map<String, RuleDetailsDataBean>> parcelFieldRuleDetailsMap = null;
        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
            parcelFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkParcelDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.PARCEL);
        }
        List<SelectItem> selectItems = this.convertModelToUiData(hkParcelDocuments, parcelDataBean.getFeatureDbFieldMap(), parcelFieldRuleDetailsMap, fieldIdToNameMap);
        return selectItems;
    }

    public Boolean deLinkRoughParcelWithPurchase(String parcelId) {
        Boolean result = stockService.deLinkRoughParcelWithPurchase(new ObjectId(parcelId));
        return result;
    }
    
    public long getCountofParcelDocumentsFromCriteria(String invoiceId) {
        long totalItems = stockService.getCountofParcelDocumentsFromCriteria(0l,Boolean.FALSE,new ObjectId(invoiceId));
        return totalItems;
    }

}
