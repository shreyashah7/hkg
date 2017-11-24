/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRuleConfigurationService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkFinalMakeableDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRoughMakeableDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.sync.center.model.HkLeaveDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author gautam
 */
@Service
public class RuleExecutionTransformer {

    @Autowired
    HkRuleConfigurationService ruleConfigurationService;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    HkCustomFieldService customFieldService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    HkStockService stockService;

    @Autowired
    HkUserService userService;
    /**
     * Execute Screen rules and pre rules
     *
     * @param hkStockDocuments
     * @param fieldIds
     * @param featureName
     * @param EntityType
     * @return
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RuleExecutionTransformer.class);

    public Map<String, Map<String, RuleDetailsDataBean>> prepareDataAndExecuteScreenRules(List<?> hkStockDocuments, List<Long> fieldIds, String featureName, String EntityType) {
        Long featureId = applicationUtil.getFeatureNameIdMap().get(featureName);
        System.out.println("Feature Id:::" + featureId);
        if (featureId != null) {

            List<HkCriteriaSetDocument> screenRuleDocuments = ruleConfigurationService.retrieveScreenRulesAssociatedWithField(featureId, new ArrayList<>(fieldIds));
            System.out.println("fieldWithRuleDocuments:::" + screenRuleDocuments);
            Map<Long, List<Long>> fieldWithRuleNumbers = new HashMap<>();
            Map<String, RuleDetailsDataBean> fieldWithRuleDetails = new HashMap<>();
            Map<String, Map<String, RuleDetailsDataBean>> fieldRuleDetailsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {

                for (HkCriteriaSetDocument ruleDocument : screenRuleDocuments) {
                    for (Long field : fieldIds) {
                        if (ruleDocument.getFieldsToBeApplied().contains(field)) {
                            if (fieldWithRuleNumbers.get(field) == null) {
                                fieldWithRuleNumbers.put(field, new ArrayList<>());
                            }
                            fieldWithRuleNumbers.get(field).add(ruleDocument.getRuleNumber());
                        }
                    }
                }
            }

            LOGGER.info("allRuleDocuments:::" + screenRuleDocuments);
            //Execute screen rules
            if (hkStockDocuments != null) {
                List<HkCriteriaSetDocument> succeedScreenRules = null;
                switch (EntityType) {
                    case HkSystemConstantUtil.Feature.INVOICE:
                        List<HkInvoiceDocument> hkInvoiceDocuments = (List<HkInvoiceDocument>) hkStockDocuments;
                        for (HkInvoiceDocument invoiceDocument : hkInvoiceDocuments) {
                            Map<String, Object> fieldValueMap = invoiceDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("invoice." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("invoice.createdOn", invoiceDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("invoice.invoice_createdBy", invoiceDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("invoice.invoice_lastModifiedBy", invoiceDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("invoice.lastModifiedOn", invoiceDocument.getLastModifiedOn());
                            fieldValueMapToEvaluate.put("invoice.invoice_status", invoiceDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), invoiceDocument.getId(), HkSystemConstantUtil.Feature.INVOICE, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(invoiceDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PARCEL:
                        List<HkParcelDocument> hkParcelDocuments = (List<HkParcelDocument>) hkStockDocuments;
                        for (HkParcelDocument parcelDocument : hkParcelDocuments) {
                            Map<String, Object> fieldValueMap = parcelDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("parcel." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("parcel.createdOn", parcelDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("parcel.parcel_createdBy", parcelDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("parcel.parcel_lastModifiedBy", parcelDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("parcel.lastModifiedOn", parcelDocument.getLastModifiedOn());
                            fieldValueMapToEvaluate.put("parcel.parcel_status", parcelDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), parcelDocument.getId(), HkSystemConstantUtil.Feature.PARCEL, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(parcelDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.LOT:
                        List<HkLotDocument> hkLotDocuments = (List<HkLotDocument>) hkStockDocuments;
                        for (HkLotDocument lotDocument : hkLotDocuments) {
                            Map<String, Object> fieldValueMap = lotDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("lot." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("lot.createdOn", lotDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("lot.lot_createdBy", lotDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("lot.lot_lastModifiedBy", lotDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("lot.lastModifiedOn", lotDocument.getLastModifiedOn());
                            fieldValueMapToEvaluate.put("lot.lot_status", lotDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), lotDocument.getId(), HkSystemConstantUtil.Feature.LOT, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                LOGGER.info("fieldWithRuleNumbers:::" + fieldWithRuleNumbers);
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            LOGGER.info("fieldWithRuleDetails:::" + fieldWithRuleDetails);
                            fieldRuleDetailsMap.put(lotDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PACKET:
                        List<HkPacketDocument> hkPacketDocuments = (List<HkPacketDocument>) hkStockDocuments;
                        for (HkPacketDocument packetDocument : hkPacketDocuments) {
                            Map<String, Object> fieldValueMap = packetDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("packet." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("packet.createdOn", packetDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("packet.packet_createdBy", packetDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("packet.packet_lastModifiedBy", packetDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("packet.lastModifiedOn", packetDocument.getLastModifiedOn());
                            fieldValueMapToEvaluate.put("packet.packet_status", packetDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), packetDocument.getId(), HkSystemConstantUtil.Feature.PACKET, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                LOGGER.info("fieldWithRuleNumbers:::" + fieldWithRuleNumbers);
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            LOGGER.info("fieldWithRuleDetails:::" + fieldWithRuleDetails);
                            fieldRuleDetailsMap.put(packetDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SUB_LOT:
                        List<HkSubLotDocument> hkSubLotDocuments = (List<HkSubLotDocument>) hkStockDocuments;
                        for (HkSubLotDocument subLotDocument : hkSubLotDocuments) {
                            Map<String, Object> fieldValueMap = subLotDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("subLotEntity." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("subLotEntity.createdOn", subLotDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("subLotEntity.sublot_createdBy", subLotDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("subLotEntity.sublot_lastModifiedBy", subLotDocument.getLastModifiedBy() + ":E");
//                fieldValueMapToEvaluate.put("subLotEntity.lastModifiedOn", subLotDocument.getLastModifiedOn());
//                fieldValueMapToEvaluate.put("subLotEntity.invoice_status", subLotDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), subLotDocument.getId(), HkSystemConstantUtil.Feature.SUB_LOT, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(subLotDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.ROUGH_PURCHASE:
                        List<HkPurchaseDocument> hkPurchaseDocuments = (List<HkPurchaseDocument>) hkStockDocuments;
                        for (HkPurchaseDocument purchaseDocument : hkPurchaseDocuments) {
                            Map<String, Object> fieldValueMap = purchaseDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("purchase." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("purchase.createdOn", purchaseDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("purchase.purchase_createdBy", purchaseDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("purchase.purchase_lastModifiedBy", purchaseDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("purchase.lastModifiedOn", purchaseDocument.getLastModifiedOn());
//                            fieldValueMapToEvaluate.put("purchase.purchase_status", purchaseDocument.getStatus());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), purchaseDocument.getId(), HkSystemConstantUtil.Feature.ROUGH_PURCHASE, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(purchaseDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SELL:
                        List<HkSellDocument> hkSellDocuments = (List<HkSellDocument>) hkStockDocuments;
                        for (HkSellDocument sellDocument : hkSellDocuments) {
                            Map<String, Object> fieldValueMap = sellDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("sell." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("sell.createdOn", sellDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("sell.sell_createdBy", sellDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("sell.sell_lastModifiedBy", sellDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("sell.lastModifiedOn", sellDocument.getLastModifiedOn());
                            fieldValueMapToEvaluate.put("sell.sell_status", sellDocument.getStatus());
                            fieldValueMapToEvaluate.put("sell." + HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_DOLLAR, sellDocument.getTotalAmountInDollar());
                            fieldValueMapToEvaluate.put("sell." + HkSystemConstantUtil.SellStaticFields.TOTAL_AMOUNT_IN_RS, sellDocument.getTotalAmountInRs());
                            fieldValueMapToEvaluate.put("sell." + HkSystemConstantUtil.SellStaticFields.TOTAL_CARAT, sellDocument.getTotalCarat());
                            fieldValueMapToEvaluate.put("sell." + HkSystemConstantUtil.SellStaticFields.TOTAL_PIECES, sellDocument.getTotalPieces());
                            LOGGER.info("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), sellDocument.getId(), HkSystemConstantUtil.Feature.SELL, true, null);
                            }
                            LOGGER.info("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(sellDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case "roughMakeableEntity":
                        List<HkRoughMakeableDocument> hkroughMakeableDocuments = (List<HkRoughMakeableDocument>) hkStockDocuments;
                        for (HkRoughMakeableDocument roughMakeableDocument : hkroughMakeableDocuments) {
                            Map<String, Object> fieldValueMap = roughMakeableDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("roughmakeable." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("roughmakeable.createdOn", roughMakeableDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("roughmakeable.roughmakeable_createdBy", roughMakeableDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("roughmakeable.roughmakeable_lastModifiedBy", roughMakeableDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("roughmakeable.lastModifiedOn", roughMakeableDocument.getModifiedOn());
//                            fieldValueMapToEvaluate.put("purchase.purchase_status", roughMakeableDocument.getStatus());
                            System.out.println("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), roughMakeableDocument.getId(), HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, true, null);
                            }
                            System.out.println("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(roughMakeableDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                    case "finalMakeableEntity":
                        List<HkFinalMakeableDocument> hkfinalMakeableDocuments = (List<HkFinalMakeableDocument>) hkStockDocuments;
                        for (HkFinalMakeableDocument finalMakeableDocument : hkfinalMakeableDocuments) {
                            Map<String, Object> fieldValueMap = finalMakeableDocument.getFieldValue().toMap();
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()) {
                                fieldValueMapToEvaluate.put("finalmakeable." + entry.getKey(), entry.getValue());
                            }
                            fieldValueMapToEvaluate.put("finalmakeable.createdOn", finalMakeableDocument.getCreatedOn());
                            fieldValueMapToEvaluate.put("finalmakeable.finalmakeable_createdBy", finalMakeableDocument.getCreatedBy() + ":E");
                            fieldValueMapToEvaluate.put("finalmakeable.finalmakeable_lastModifiedBy", finalMakeableDocument.getLastModifiedBy() + ":E");
                            fieldValueMapToEvaluate.put("finalmakeable.lastModifiedOn", finalMakeableDocument.getModifiedOn());
//                            fieldValueMapToEvaluate.put("purchase.purchase_status", finalMakeableDocument.getStatus());
                            System.out.println("fieldValueMapToEvaluate:::" + fieldValueMapToEvaluate);
                            if (!CollectionUtils.isEmpty(screenRuleDocuments)) {
                                succeedScreenRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(screenRuleDocuments), finalMakeableDocument.getId(), HkSystemConstantUtil.Feature.FINAL_MAKEABLE, true, null);
                            }
                            System.out.println("Res screen::::::" + succeedScreenRules);
                            fieldWithRuleDetails = new HashMap<>();
                            if (!CollectionUtils.isEmpty(succeedScreenRules)) {
                                for (Map.Entry<Long, List<Long>> entry : fieldWithRuleNumbers.entrySet()) {
                                    for (HkCriteriaSetDocument rule : succeedScreenRules) {
                                        if (entry.getValue().contains(rule.getRuleNumber())) {
                                            fieldWithRuleDetails.put(entry.getKey().toString(), new RuleDetailsDataBean(rule.getColorCode(), rule.getTooltipMsg()));
                                            break;
                                        }
                                    }
                                }
                            }
                            fieldRuleDetailsMap.put(finalMakeableDocument.getId().toString(), fieldWithRuleDetails);
                        }
                        break;
                }

            }
            return fieldRuleDetailsMap;
        }
        return null;
    }

    public RuleDetailsDataBean prepareDataAndExecutePreRules(String entityId, String featureName, String entityType) {
        Long featureId = applicationUtil.getFeatureNameIdMap().get(featureName);
        LOGGER.info("Feature Id:::" + featureId);
        LOGGER.info("Entity Id:::" + entityId);
        if (featureId != null) {

            List<HkCriteriaSetDocument> preRuleDocuments = ruleConfigurationService.retrievePreRulesByFeature(featureId);
            LOGGER.info("preRuleDocuments:::" + preRuleDocuments);
            RuleDetailsDataBean ruleDetailsDataBean = new RuleDetailsDataBean();

            //Execute screen rules
            if (entityId != null && !StringUtils.isEmpty(entityId) && entityType != null && !StringUtils.isEmpty(entityType)) {
                List<HkCriteriaSetDocument> succeedPreRules = null;
                switch (entityType) {
                    case HkSystemConstantUtil.Feature.INVOICE:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.INVOICE, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PARCEL:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.PARCEL, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.LOT:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.LOT, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PACKET:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.PACKET, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SUB_LOT:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.SUB_LOT, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.ROUGH_PURCHASE:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.ROUGH_PURCHASE, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SELL:
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.SELL, false, null);
                        }
                        LOGGER.info("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case "roughMakeableEntity":
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.ROUGH_MAKEABLE, false, null);
                        }
                        System.out.println("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case "finalMakeableEntity":
                        if (!CollectionUtils.isEmpty(preRuleDocuments)) {
                            succeedPreRules = ruleConfigurationService.executeRuleSet(null, new ArrayList<>(preRuleDocuments), new ObjectId(entityId), HkSystemConstantUtil.Feature.FINAL_MAKEABLE, false, null);
                        }
                        System.out.println("Res pre:::::::" + succeedPreRules);
                        if (!CollectionUtils.isEmpty(succeedPreRules)) {
                            HkCriteriaSetDocument preRule = succeedPreRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                }

            }
            return ruleDetailsDataBean;
        }
        return null;
    }

    public RuleDetailsDataBean prepareDataAndExecutePostRules(Map<String, Object> currentFieldValueMap1, Map<String, String> dbType, String entityId, String featureName, String entityType, Map<String, String> otherEntitysIdMap, String IP) {
        Long featureId = null;
        if (StringUtils.hasText(entityType) && entityType.equalsIgnoreCase(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN)) {
            featureId = -1l;
        } else {
            featureId = applicationUtil.getFeatureNameIdMap().get(featureName);
        }
        BasicBSONObject currentFieldValueMap = null;
        if (StringUtils.hasText(entityType) && (entityType.equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE) || entityType.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL))) {
            if (entityType.equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)) {
                if (!CollectionUtils.isEmpty(currentFieldValueMap1)) {
                    if (currentFieldValueMap1.containsKey("parcelDbObjectId")) {
                        currentFieldValueMap1.remove("parcelDbObjectId");
                    }
                    if (currentFieldValueMap1.containsKey("linkPurchase")) {
                        currentFieldValueMap1.remove("linkPurchase");
                    }
                }
            }
            currentFieldValueMap = customFieldService.makeBSONObject(currentFieldValueMap1, dbType, entityType, 0l, null, null);
        } else if (StringUtils.hasText(entityType) && featureId != null && featureId != -1) {
            currentFieldValueMap = customFieldService.makeBSONObject(currentFieldValueMap1, dbType, entityType, loginDataBean.getCompanyId(), null, null);
        }

        LOGGER.info("Feature Id:::" + featureId);
        LOGGER.info("Entity Id:::" + entityId);
        if (featureId != null) {
            List<HkCriteriaSetDocument> postRuleDocuments = null;
            if (featureId > 0) {
                postRuleDocuments = ruleConfigurationService.retrievePostRulesByFeature(featureId, true);
            } else {
                postRuleDocuments = ruleConfigurationService.retrievePostRulesByFeature(featureId, null);
            }
            LOGGER.info("postRuleDocuments:::" + postRuleDocuments);
            RuleDetailsDataBean ruleDetailsDataBean = new RuleDetailsDataBean();

            //Execute screen rules
            if (entityType != null && !StringUtils.isEmpty(entityType)) {
                List<HkCriteriaSetDocument> succeedPostRules = null;
                switch (entityType) {
                    case HkSystemConstantUtil.Feature.INVOICE:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                    fieldValueMapToEvaluate.put("invoice." + entry.getKey(), entry.getValue());
                                }
                            }
                            succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.INVOICE, false, otherEntitysIdMap);
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PARCEL:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("parcel." + entry.getKey(), entry.getValue());
                                    }
                                }
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.PARCEL, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.LOT:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("lot." + entry.getKey(), entry.getValue());
                                    }
                                }
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.LOT, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PACKET:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("packet." + entry.getKey(), entry.getValue());
                                    }
                                }
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.PACKET, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SUB_LOT:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("subLotEntity." + entry.getKey(), entry.getValue());
                                    }
                                }

                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.SUB_LOT, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.ROUGH_PURCHASE:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("purchase." + entry.getKey(), entry.getValue());
                                    }
                                }
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.ROUGH_PURCHASE, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SELL:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                                if (!CollectionUtils.isEmpty(currentFieldValueMap)) {
                                    for (Map.Entry<String, Object> entry : currentFieldValueMap.entrySet()) {
                                        fieldValueMapToEvaluate.put("sell." + entry.getKey(), entry.getValue());
                                    }
                                }
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), entityId == null ? null : new ObjectId(entityId), HkSystemConstantUtil.Feature.SELL, false, otherEntitysIdMap);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                    case HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN:
                        if (!CollectionUtils.isEmpty(postRuleDocuments)) {
                            Map<String, Object> fieldValueMapToEvaluate = new HashMap<>();
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            Date toDate = cal.getTime();
                            List<SyncCenterUserDocument> userDocuments = userService.retrieveUsersByCriteria(currentFieldValueMap1.get("username").toString(), null, Boolean.TRUE, null);
                            if (!CollectionUtils.isEmpty(userDocuments)) {
                                LOGGER.info("user id" + userDocuments.get(0).getId());
                                LOGGER.info("new Date()" + new Date());
                                LOGGER.info("toDate" + toDate);
                                List<HkLeaveDocument> leaveDocuments = stockService.retrieveLeavesForUserBetweenFromDateAndToDate(Arrays.asList(userDocuments.get(0).getId()), new Date(), toDate, true);
                                if (!CollectionUtils.isEmpty(leaveDocuments)) {
                                    LOGGER.info("here on leave" + "from date--" + leaveDocuments.get(0).getFrmDt() + "to date--" + leaveDocuments.get(0).getToDt());
                                    fieldValueMapToEvaluate.put(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + "." + HkSystemConstantUtil.RuleConfiguration.CONSTANT.IsAbsentToday, true);
                                } else {
                                    fieldValueMapToEvaluate.put(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + "." + HkSystemConstantUtil.RuleConfiguration.CONSTANT.IsAbsentToday, null);
                                }

//                                fieldValueMapToEvaluate.put(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + "." + HkSystemConstantUtil.RuleConfiguration.CONSTANT.RESTRICT_BY_IP, true);
//                                fieldValueMapToEvaluate.put(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + "." + HkSystemConstantUtil.RuleConfiguration.CONSTANT.ABSENT_WITHOUT_INFO, true);
                                succeedPostRules = ruleConfigurationService.executeRuleSet(fieldValueMapToEvaluate, new ArrayList<>(postRuleDocuments), null, HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN, false, null);
                            }
                        }
                        LOGGER.info("Res post:::::::" + succeedPostRules);
                        if (!CollectionUtils.isEmpty(succeedPostRules)) {
                            HkCriteriaSetDocument preRule = succeedPostRules.get(0);
                            ruleDetailsDataBean.setEntityId(entityId);
                            ruleDetailsDataBean.setValidationMessage(preRule.getValidationMessage());
                        }
                        break;
                }

            }
            return ruleDetailsDataBean;
        }
        return null;
    }

}
