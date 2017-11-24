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
import com.argusoft.hkg.nosql.model.HkRoughStockDetailDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.nosql.model.HkSellParcelDetailDocument;
import com.argusoft.hkg.web.center.stock.databeans.RoughStockDetailDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.stock.databeans.SellDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
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
public class RoughSaleTransformer {

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    HkStockService stockService;

    @Autowired
    RuleExecutionTransformer centerRuleExecutionTransformer;
    
    @Autowired
    LoginDataBean loginDataBean;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RoughSaleTransformer.class);

    public List<SelectItem> retrieveAllSellDocuments(SellDataBean sellDataBean) {
        Map<String, List<String>> map = sellDataBean.getUiFieldMap();
        Map<String, Object> map1 = sellDataBean.getRuleConfigMap();
        Map<String, String> fieldIdToNameMap = new HashMap<>();
        Set<Long> fieldIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
            fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
            for (String lng : fieldIdToNameMap.keySet()) {
                fieldIds.add(Long.parseLong(lng));
            }
        }
        System.out.println("fieldIds-----" + fieldIds);
        List<HkSellDocument> hkSellDocuments = stockService.retrieveSellDocuments(null, null, 0l, loginDataBean.getId());
        Map<String, Map<String, RuleDetailsDataBean>> sellFieldRuleDetailsMap = null;
        if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
            sellFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(hkSellDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.SELL);
        }
        List<SelectItem> selectItems = this.convertModelToUiData(hkSellDocuments, map, sellFieldRuleDetailsMap, fieldIdToNameMap);
        return selectItems;
    }

    private List<SelectItem> convertModelToUiData(List<HkSellDocument> hkSellDocuments, Map<String, List<String>> map, Map<String, Map<String, RuleDetailsDataBean>> sellFieldRuleDetailsMap, Map<String, String> fieldIdToNameMap) {
        List<SelectItem> selectItems = new ArrayList<>();
        Map<String, String> mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(hkSellDocuments)) {
            for (HkSellDocument hkSellDocument : hkSellDocuments) {
                if (hkSellDocument != null) {
                    SelectItem selectItem = new SelectItem(hkSellDocument.getId().toString(), null);
                    Map<String, Object> mapToSentOnUI = new HashMap<>();
                    mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_DOLLAR, hkSellDocument.getTotalAmountInDollar());
                    mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_RS, hkSellDocument.getTotalAmountInRs());
                    mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_CARAT, hkSellDocument.getTotalCarat());
                    mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_PIECES, hkSellDocument.getTotalPieces());
                    mapToSentOnUI.put("parcelObjectIds", hkSellDocument.getParcels());
                    BasicBSONObject fieldValue = hkSellDocument.getFieldValue();
                    List<String> sellDbFieldNames = map.get("dbFieldNames");
                    if (fieldValue != null && !CollectionUtils.isEmpty(sellDbFieldNames)) {
                        for (String dbField : sellDbFieldNames) {
                            // Code modified by Shreya for solving the issue of currency component where currency code was not getting retrieved
                            String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
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
                        List<String> parcels = new ArrayList<>();
                        for (ObjectId parcel : hkSellDocument.getParcels()) {
                            parcels.add(parcel.toString());
                        }
                        mapToSentOnUI.put("sellObjectId", hkSellDocument.getId().toString());
                        selectItem.setOtherValues(parcels);
                        selectItem.setCategoryCustom(mapToSentOnUI);

                        //Set screen rule related details.
                        if (sellFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(sellFieldRuleDetailsMap)) {
                            System.out.println("sellFieldRuleDetailsMap:::" + sellFieldRuleDetailsMap);
                            System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                            if (!CollectionUtils.isEmpty(sellFieldRuleDetailsMap.get(hkSellDocument.getId().toString()))) {
                                Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                                Map<String, RuleDetailsDataBean> ruleDetails = sellFieldRuleDetailsMap.get(hkSellDocument.getId().toString());
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

    public SelectItem retrieveSellDocumentById(SellDataBean sellDataBean) {
        Map<String, String> mapOfDbFieldWithCurrencyCode = customFieldService.mapOfDbFieldWithCurrencyCode(loginDataBean.getCompanyId());
        SelectItem selectItem = null;
        Map<String, List<String>> map = sellDataBean.getUiFieldMap();
        if (!CollectionUtils.isEmpty(map)) {
            ObjectId sellObjecId = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("sellObjectId")) {
                    sellObjecId = new ObjectId(entry.getValue().get(0));
                    break;
                }
            }
            Map<String, Object> map1 = sellDataBean.getRuleConfigMap();
            Map<String, String> fieldIdToNameMap = new HashMap<>();
            Set<Long> fieldIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(map1) && map1.get("fieldIdNameMap") != null) {
                fieldIdToNameMap = (Map<String, String>) map1.get("fieldIdNameMap");
                for (String lng : fieldIdToNameMap.keySet()) {
                    fieldIds.add(Long.parseLong(lng));
                }
            }
            System.out.println("fieldIds-----" + fieldIds);
            HkSellDocument hkSellDocument = stockService.retrieveSellDocumentById(sellObjecId);
            List<ObjectId> objectIdsOfParcels = hkSellDocument.getParcels();

            if (hkSellDocument != null) {
                Map<String, Map<String, RuleDetailsDataBean>> sellFieldRuleDetailsMap = null;
                if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                    sellFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(Arrays.asList(hkSellDocument), new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.SELL);
                }
                selectItem = new SelectItem(hkSellDocument.getId().toString(), null);
                Map<String, Object> mapToSentOnUI = new HashMap<>();
                List<String> parcels = new ArrayList<>();
                for (ObjectId parcel : hkSellDocument.getParcels()) {
                    parcels.add(parcel.toString());
                }
                selectItem.setOtherValues(parcels);
                List<Map<String, Object>> customMapList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkSellDocument.getHkSellParcelDetailDocuments())) {
                    for (HkSellParcelDetailDocument hkSellParcelDetailDocument : hkSellDocument.getHkSellParcelDetailDocuments()) {
                        Map<String, Object> sellParcelDetailMap = new HashMap<>();
                        sellParcelDetailMap.put(HkSystemConstantUtil.SELL_PARCEL_DETAIL_FIELDS.SELL_PIECES, hkSellParcelDetailDocument.getSellPieces());
                        sellParcelDetailMap.put(HkSystemConstantUtil.SELL_PARCEL_DETAIL_FIELDS.SELL_CARATS, hkSellParcelDetailDocument.getSellCarats());
                        sellParcelDetailMap.put(HkSystemConstantUtil.SELL_PARCEL_DETAIL_FIELDS.EXCHANGE_RATE, hkSellParcelDetailDocument.getExchangeRate());
                        sellParcelDetailMap.put(HkSystemConstantUtil.SELL_PARCEL_DETAIL_FIELDS.PARCEL, hkSellParcelDetailDocument.getParcel().toString());
                        customMapList.add(sellParcelDetailMap);
                    }
                }
                selectItem.setCustom6(customMapList);
                mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_DOLLAR, hkSellDocument.getTotalAmountInDollar());
                mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_RS, hkSellDocument.getTotalAmountInRs());
                mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_CARAT, hkSellDocument.getTotalCarat());
                mapToSentOnUI.put(HkSystemConstantUtil.SellStaticFields.TOTAL_PIECES, hkSellDocument.getTotalPieces());
                BasicBSONObject fieldValue = hkSellDocument.getFieldValue();
                List<String> sellDbFieldNames = map.get("dbFieldNames");
                if (fieldValue != null && !CollectionUtils.isEmpty(sellDbFieldNames)) {
                    for (String dbField : sellDbFieldNames) {
                        // Code modified by Shreya shah for solving the issue of currency component where currency code was not getting retrieved
                        String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
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
                    //Set screen rule related details.
                    if (sellFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(sellFieldRuleDetailsMap)) {
                        System.out.println("sellFieldRuleDetailsMap:::" + sellFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(sellFieldRuleDetailsMap.get(hkSellDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = sellFieldRuleDetailsMap.get(hkSellDocument.getId().toString());
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

    public Boolean createOrUpdateSellDocumentForParcel(SellDataBean sellDataBean) {
        LOGGER.info("sellDataBean" + sellDataBean);
        Boolean result = Boolean.FALSE;
        if (sellDataBean != null) {
            Map<String, Object> sellCustomMap = sellDataBean.getSellCustom();
            Map<String, String> sellDbTypeMap = sellDataBean.getSellDbType();
            List<String> keysToRemove = new ArrayList<>();
            for (Map.Entry<String, Object> entry : sellCustomMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (!(value != null && value.toString() != null && value.toString().length() > 0)) {
                    keysToRemove.add(key);
                }
            }
            if (!CollectionUtils.isEmpty(keysToRemove)) {
                for (String key : keysToRemove) {
                    sellCustomMap.remove(key);
                }
            }
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(sellDbTypeMap)) {
                for (Map.Entry<String, String> entrySet : sellDbTypeMap.entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }
            Map<String, String> uiFieldMap = customFieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            HkSellDocument hkSellDocument = null;
            if (!CollectionUtils.isEmpty(sellDataBean.getRoughStockDetailDataBeans())) {
                List<RoughStockDetailDataBean> roughStockDetailDataBeans = sellDataBean.getRoughStockDetailDataBeans();
                if (!CollectionUtils.isEmpty(roughStockDetailDataBeans)) {
                    List<ObjectId> roughStockIds = new ArrayList<>();
                    for (RoughStockDetailDataBean roughStockDetailDataBean : roughStockDetailDataBeans) {
                        if (roughStockDetailDataBean.getId() != null) {
                            roughStockIds.add(new ObjectId(roughStockDetailDataBean.getId()));
                        }
                    }
                    Map<ObjectId, HkRoughStockDetailDocument> mapOfIdDocument = new HashMap<>();
                    List<HkRoughStockDetailDocument> hkRoughStockDetailDocuments = stockService.retrieveRoughStockDetailByIds(roughStockIds);
                    if (!CollectionUtils.isEmpty(hkRoughStockDetailDocuments)) {
                        for (HkRoughStockDetailDocument hkRoughStockDetailDocument : hkRoughStockDetailDocuments) {
                            mapOfIdDocument.put(hkRoughStockDetailDocument.getId(), hkRoughStockDetailDocument);
                        }
                    }
                    List<HkRoughStockDetailDocument> hkRoughStockDetailDocumentList = new ArrayList<>();
                    for (RoughStockDetailDataBean roughStockDetailDataBean : roughStockDetailDataBeans) {
                        HkRoughStockDetailDocument hkRoughStockDetailDocument = null;
                        if (roughStockDetailDataBean.getId() != null) {
                            if (!CollectionUtils.isEmpty(mapOfIdDocument) && mapOfIdDocument.get(roughStockDetailDataBean.getId()) != null) {
                                hkRoughStockDetailDocument = mapOfIdDocument.get(roughStockDetailDataBean.getId());
                            }
                        } else {
                            hkRoughStockDetailDocument = new HkRoughStockDetailDocument();
                            hkRoughStockDetailDocument.setCreatedBy(loginDataBean.getId());
                            hkRoughStockDetailDocument.setCreatedOn(new Date());
                        }
                        hkRoughStockDetailDocument = this.convertRoughStockDetailDataBeanToModel(hkRoughStockDetailDocument, roughStockDetailDataBean, loginDataBean.getId());
                        hkRoughStockDetailDocumentList.add(hkRoughStockDetailDocument);
                    }
                    if (sellDataBean.getId() != null) {
                        hkSellDocument = stockService.retrieveSellDocumentById(new ObjectId(sellDataBean.getId()));
                        if (hkSellDocument != null) {
                            stockService.updateRoughStockDetailsWithParcel(hkRoughStockDetailDocumentList, hkSellDocument);
                            hkSellDocument = this.convertSellDataBeanToModel(hkSellDocument, sellDataBean, uiFieldMap, sellDataBean.getRoughStockDetailDataBeans());
                        }
                    } else {
                        hkSellDocument = this.convertSellDataBeanToModel(null, sellDataBean, uiFieldMap, sellDataBean.getRoughStockDetailDataBeans());
                        stockService.updateRoughStockDetailsWithParcel(hkRoughStockDetailDocumentList, null);
                    }

                }

            }
            stockService.sellParcel(hkSellDocument, loginDataBean.getId(), 0l);
            result = Boolean.TRUE;
        }
        return result;

    }

    private HkRoughStockDetailDocument convertRoughStockDetailDataBeanToModel(HkRoughStockDetailDocument hkRoughStockDetailDocument, RoughStockDetailDataBean roughStockDetailDataBean, Long createdBy) {
        hkRoughStockDetailDocument.setAction(HkSystemConstantUtil.RoughStockActions.DEBIT);
        hkRoughStockDetailDocument.setOperation(HkSystemConstantUtil.RoughStockOperations.SELL);
        hkRoughStockDetailDocument.setCarat(roughStockDetailDataBean.getChangedCarat());
        hkRoughStockDetailDocument.setLastModifiedBy(createdBy);
        hkRoughStockDetailDocument.setLastModefiedOn(new Date());
        hkRoughStockDetailDocument.setExchangeRate(roughStockDetailDataBean.getChangedExchangeRate());
//        HkParcelDocument hkParcelDocument = stockService.retrieveParcelById(new ObjectId(roughStockDetailDataBean.getParcel()));
        hkRoughStockDetailDocument.setParcel(new ObjectId(roughStockDetailDataBean.getParcel()));
        hkRoughStockDetailDocument.setPieces(roughStockDetailDataBean.getChangedPieces());
        hkRoughStockDetailDocument.setRate(roughStockDetailDataBean.getChangedRate());
//        hkRoughStockDetailDocument.setYear(hkParcelDocument.getYear());
        hkRoughStockDetailDocument.setEmpId(loginDataBean.getId());
        hkRoughStockDetailDocument.setDepId(loginDataBean.getDepartment());
        return hkRoughStockDetailDocument;
    }

    private HkSellDocument convertSellDataBeanToModel(HkSellDocument hkSellDocument, SellDataBean sellDataBean, Map<String, String> uiFieldMap, List<RoughStockDetailDataBean> roughStockDetailDataBeans) {
        if (hkSellDocument == null) {
            hkSellDocument = new HkSellDocument();
            hkSellDocument.setCreatedByFranchise(loginDataBean.getCompanyId());
            hkSellDocument.setCreatedBy(loginDataBean.getId());
            hkSellDocument.setCreatedOn(new Date());
        }
        hkSellDocument.setFranchiseId(0l);

        hkSellDocument.setIsArchive(Boolean.FALSE);

        hkSellDocument.setLastModifiedBy(loginDataBean.getId());
        List<ObjectId> parcelIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sellDataBean.getParcels())) {
            for (String parcelId : sellDataBean.getParcels()) {
                parcelIds.add(new ObjectId(parcelId));
            }
            hkSellDocument.setParcels(parcelIds);
        }
        if (!CollectionUtils.isEmpty(roughStockDetailDataBeans)) {
            List<HkSellParcelDetailDocument> hkSellParcelDetailDocuments = new ArrayList<>();
            for (RoughStockDetailDataBean roughStockDetailDataBean : roughStockDetailDataBeans) {
                if (!CollectionUtils.isEmpty(hkSellDocument.getHkSellParcelDetailDocuments())) {
                    int count = 0;
                    for (HkSellParcelDetailDocument hkSellParcelDetailDocument : hkSellDocument.getHkSellParcelDetailDocuments()) {
                        if (roughStockDetailDataBean.getParcel().equals(hkSellParcelDetailDocument.getParcel().toString())) {
                            count++;
                            hkSellParcelDetailDocument.setExchangeRate(roughStockDetailDataBean.getChangedExchangeRate());
                            hkSellParcelDetailDocument.setSellCarats(roughStockDetailDataBean.getChangedCarat());
                            hkSellParcelDetailDocument.setSellPieces(roughStockDetailDataBean.getChangedPieces());
                            hkSellParcelDetailDocuments.add(hkSellParcelDetailDocument);
                        }
                    }
                    if (count == 0) {
                        HkSellParcelDetailDocument hkSellParcelDetailDocument = new HkSellParcelDetailDocument();
                        hkSellParcelDetailDocument.setExchangeRate(roughStockDetailDataBean.getChangedExchangeRate());
                        hkSellParcelDetailDocument.setParcel(new ObjectId(roughStockDetailDataBean.getParcel()));
                        hkSellParcelDetailDocument.setSellCarats(roughStockDetailDataBean.getChangedCarat());
                        hkSellParcelDetailDocument.setSellPieces(roughStockDetailDataBean.getChangedPieces());
                        hkSellParcelDetailDocuments.add(hkSellParcelDetailDocument);
                    }
                } else {
                    HkSellParcelDetailDocument hkSellParcelDetailDocument = new HkSellParcelDetailDocument();
                    hkSellParcelDetailDocument.setExchangeRate(roughStockDetailDataBean.getChangedExchangeRate());
                    hkSellParcelDetailDocument.setParcel(new ObjectId(roughStockDetailDataBean.getParcel()));
                    hkSellParcelDetailDocument.setSellCarats(roughStockDetailDataBean.getChangedCarat());
                    hkSellParcelDetailDocument.setSellPieces(roughStockDetailDataBean.getChangedPieces());
                    hkSellParcelDetailDocuments.add(hkSellParcelDetailDocument);
                }
            }
            hkSellDocument.setHkSellParcelDetailDocuments(hkSellParcelDetailDocuments);
        }
        hkSellDocument.setLastModifiedOn(new Date());
        hkSellDocument.setStatus(HkSystemConstantUtil.StockStatus.SOLD);
        hkSellDocument.setTotalAmountInRs(sellDataBean.getTotalAmountInRs());
        hkSellDocument.setTotalAmountInDollar(sellDataBean.getTotalAmountInDollar());
        hkSellDocument.setTotalCarat(sellDataBean.getTotalCarat());
        hkSellDocument.setTotalPieces(sellDataBean.getTotalPieces());
        BasicBSONObject fieldValues = customFieldService.makeBSONObject(sellDataBean.getSellCustom(), sellDataBean.getSellDbType(), uiFieldMap, HkSystemConstantUtil.Feature.SELL, 0L, null, Boolean.FALSE, null, null);
        hkSellDocument.setFieldValue(fieldValues);
        return hkSellDocument;
    }

    public List<SelectItem> searchParcel(ParcelDataBean parcelDataBean) {
        Map<String, List<String>> featureDbFieldMap = parcelDataBean.getFeatureDbFieldMap();
        List<String> sellDbField = null;
        if (!CollectionUtils.isEmpty(featureDbFieldMap)) {
            if (!CollectionUtils.isEmpty(featureDbFieldMap.get("sell"))) {
                sellDbField = featureDbFieldMap.get("sell");
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
        Map<String, Object> sellFieldMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(featureCustomMapValue) || parcelDataBean.isSearchOnParameter() == false) {
            List<String> labels = new ArrayList<>();
            if (!CollectionUtils.isEmpty(featureCustomMapValue)) {
                for (Map.Entry<String, Map<String, Object>> entry : featureCustomMapValue.entrySet()) {
                    String featureName = entry.getKey();
                    Map<String, Object> map = entry.getValue();
                    if (!StringUtils.isEmpty(featureName)) {
                        if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
//                        System.out.println("In search parcel :::: ");
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
                        } else if (featureName.equalsIgnoreCase(HkSystemConstantUtil.Feature.SELL)) {
                            if (!CollectionUtils.isEmpty(map)) {
                                for (Map.Entry<String, Object> entry1 : map.entrySet()) {
                                    String key = entry1.getKey();
                                    Object value = entry1.getValue();
                                    sellFieldMap.put(key, value);
                                }
                            }
                        }
                    }
                }
            }
            List<HkInvoiceDocument> invoiceDocuments = null;
            LOGGER.info("invoice field map" + invoiceFieldMap);
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
                invoiceDocuments = stockService.retrieveInvoices(invoiceFieldMap, 0l, Boolean.FALSE, null, null,null,null);
                LOGGER.info("invoiceDocuments" + invoiceDocuments);
            }

            if (!CollectionUtils.isEmpty(sellFieldMap)) {
                Iterator<Map.Entry<String, Object>> iter = sellFieldMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (key.contains("$MS$") || key.contains("$UMS$") || key.contains("$AG$")) {
                        if (value instanceof String && !StringUtils.isEmpty(value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            sellFieldMap.put(key, items);
                        } else if (value instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) value)) {
                            List<String> items = Arrays.asList(value.toString().split(","));
                            sellFieldMap.put(key, items);
                        } else {
                            iter.remove();
                        }
                    }
                }
            }

            List<ObjectId> invoiceIds = null;
            if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                invoiceIds = new ArrayList<>();
                for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                    invoiceIds.add(new ObjectId(invoiceDocument.getId().toString()));
                }
            }
            List<HkParcelDocument> parcelDocuments = null;
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
            List<ObjectId> parcelIds = null;
            LOGGER.info("parcelFieldMap" + parcelFieldMap);
            LOGGER.info("invoiceIds" + invoiceIds);
            if (!CollectionUtils.isEmpty(parcelFieldMap) || !CollectionUtils.isEmpty(invoiceIds)) {
                parcelDocuments = stockService.retrieveParcels(parcelFieldMap, invoiceIds, null, 0l, Boolean.FALSE, null, null,null,null);
            }
            if (!CollectionUtils.isEmpty(parcelDocuments)) {
                LOGGER.info("parcelDocuments not empty" + parcelDocuments);
                parcelIds = new ArrayList<>();
                for (HkParcelDocument parcelObj : parcelDocuments) {
                    parcelIds.add(parcelObj.getId());
                }
            }
            LOGGER.info("sellFieldMap" + sellFieldMap);
            LOGGER.info("parcelIds" + parcelIds);
//            LOGGER.info("franchise" + franchise);
            List<HkSellDocument> sellDocuments = null;
            if (!CollectionUtils.isEmpty(sellFieldMap) || !CollectionUtils.isEmpty(parcelIds)) {
                sellDocuments = stockService.retrieveSellDocuments(sellFieldMap, parcelIds, 0l, null);
            }
            Map<String, Map<String, RuleDetailsDataBean>> sellFieldRuleDetailsMap = null;
            if (!CollectionUtils.isEmpty(map1) && map1.get("featureName") != null) {
                sellFieldRuleDetailsMap = centerRuleExecutionTransformer.prepareDataAndExecuteScreenRules(sellDocuments, new ArrayList<>(fieldIds), map1.get("featureName").toString(), HkSystemConstantUtil.Feature.SELL);
            }

            List<SelectItem> selectItems = null;
            if (!CollectionUtils.isEmpty(sellDocuments)) {
                LOGGER.info("sellDocuments not empty");
                selectItems = new ArrayList<>();
                invoiceIds = new ArrayList<>();
                for (HkSellDocument sellDocument : sellDocuments) {
                    Map<String, Object> map = new HashMap<>();
                    SelectItem selectItem = new SelectItem(sellDocument.getId().toString(), null);
                    BasicBSONObject fieldValue = sellDocument.getFieldValue();
                    map.put("totalPieces", sellDocument.getTotalPieces());
                    map.put("totalCarat", sellDocument.getTotalCarat());
                    map.put("totalAmountInDollar", sellDocument.getTotalAmountInDollar());
                    map.put("totalAmountInRs", sellDocument.getTotalAmountInRs());
                    map.put("sellObjectId", sellDocument.getId().toString());
                    if (fieldValue != null && !CollectionUtils.isEmpty(sellDbField)) {
                        map.put("sellObjectId", sellDocument.getId().toString());

//                            }
                        for (String dbField : sellDbField) {
                            if (fieldValue.toMap().containsKey(dbField)) {
                                map.put(dbField, fieldValue.toMap().get(dbField));
                            }
                        }
                    }
                    selectItem.setCategoryCustom(map);
                    //Set screen rule related details.
                    if (sellFieldRuleDetailsMap != null && !CollectionUtils.isEmpty(sellFieldRuleDetailsMap)) {
                        System.out.println("sellFieldRuleDetailsMap:::" + sellFieldRuleDetailsMap);
                        System.out.println("fieldIdToNameMap:::" + fieldIdToNameMap);
                        if (!CollectionUtils.isEmpty(sellFieldRuleDetailsMap.get(sellDocument.getId().toString()))) {
                            Map<String, RuleDetailsDataBean> screenRuleDetailsWithDbFieldName = new HashMap<>();
                            Map<String, RuleDetailsDataBean> ruleDetails = sellFieldRuleDetailsMap.get(sellDocument.getId().toString());
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

                return selectItems;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}
