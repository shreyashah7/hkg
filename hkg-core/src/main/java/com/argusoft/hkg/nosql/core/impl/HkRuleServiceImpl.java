/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.rule.RuleLogic;
import com.argusoft.hkg.ruleexecution.And;
import com.argusoft.hkg.ruleexecution.Equals;
import com.argusoft.hkg.ruleexecution.Expression;
import com.argusoft.hkg.ruleexecution.ExpressionParser;
import com.argusoft.hkg.ruleexecution.GreaterThan;
import com.argusoft.hkg.ruleexecution.GreaterThanEquals;
import com.argusoft.hkg.ruleexecution.HasAnyValue;
import com.argusoft.hkg.ruleexecution.HasNoValue;
import com.argusoft.hkg.ruleexecution.In;
import com.argusoft.hkg.ruleexecution.InBetween;
import com.argusoft.hkg.ruleexecution.IsAfter;
import com.argusoft.hkg.ruleexecution.IsBefore;
import com.argusoft.hkg.ruleexecution.LessThan;
import com.argusoft.hkg.ruleexecution.LessThanEquals;
import com.argusoft.hkg.ruleexecution.Not;
import com.argusoft.hkg.ruleexecution.NotEquals;
import com.argusoft.hkg.ruleexecution.NotInBetween;
import com.argusoft.hkg.ruleexecution.Operations;
import com.argusoft.hkg.ruleexecution.Or;
import com.argusoft.hkg.ruleexecution.Rule;
import com.google.gson.Gson;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author mansi Changed by dhwani for transforming logic from rule to ruleset -
 * 09/12/2014
 */
@Service
public class HkRuleServiceImpl implements HkRuleService {

    private static final String IS_ARCHIVE = "isArchive";
    private static final String IS_ACTIVE = "isActive";
    private static final String FRANCHISE = "franchise";
    private static final String ID = "_id";

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    @MongoTransaction
    public ObjectId saveOrUpdateRuleSet(HkRuleSetDocument document) {
        document.setIsArchive(Boolean.FALSE);
        mongoGenericDao.update(document);
        return document.getId();
    }

    @Override
    @MongoTransaction
    public void deleteRuleSet(ObjectId ruleId) {
        HkRuleSetDocument document = (HkRuleSetDocument) mongoGenericDao.retrieveById(ruleId, HkRuleSetDocument.class);
        document.setIsActive(false);
        document.setIsArchive(true);
        mongoGenericDao.update(document);
    }

    @Override
    public Map<ObjectId, HkRuleSetDocument> retrieveAllRuleSet(long companyId, Boolean isActive) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkRuleSetDocument> documents = null;
        Map<ObjectId, HkRuleSetDocument> instanceSectionCustomFieldMap = null;
        criterias.add(Criteria.where(FRANCHISE).is(companyId));
        if (isActive != null) {
            criterias.add(Criteria.where(IS_ACTIVE).is(isActive));
        }
        criterias.add(Criteria.where(IS_ARCHIVE).is(false));
        documents = mongoGenericDao.findByCriteria(criterias, HkRuleSetDocument.class);

        if (!CollectionUtils.isEmpty(documents)) {
            instanceSectionCustomFieldMap = new HashMap<>();
            for (HkRuleSetDocument ruleSetDocument : documents) {
                instanceSectionCustomFieldMap.put(ruleSetDocument.getId(), ruleSetDocument);
            }
            return instanceSectionCustomFieldMap;
        }
        return null;
    }

    @Override
    public List<HkRuleSetDocument> retrieveRuleSetByIds(List<ObjectId> ruleSetIds) {
        if (!CollectionUtils.isEmpty(ruleSetIds)) {
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(ID).in(ruleSetIds));
            criterias.add(Criteria.where(IS_ARCHIVE).is(false));
            return mongoGenericDao.findByCriteria(criterias, HkRuleSetDocument.class);
        } else {
            return null;
        }
    }

    @Override
    public HkRuleSetDocument retrieveRuleSetById(ObjectId ruleSetId) {
        HkRuleSetDocument document = (HkRuleSetDocument) mongoGenericDao.retrieveById(ruleSetId, HkRuleSetDocument.class);
        return document;
    }

    @Override
    public void executeRuleSet(ObjectId ruleSetId, Map<Long, String> entityIdMap, Map<Long, List<Map<String, Object>>> entityFieldMap, Map<String, Object> data) {
        HkRuleSetDocument document = retrieveRuleSetById(ruleSetId);
        if (document != null) {
            if (!CollectionUtils.isEmpty(document.getRules())) {
                for (HkRuleDocument hkRuleDocument : document.getRules()) {
                    Gson gson = new Gson();
                    String toJson = gson.toJson(hkRuleDocument);
                    RuleLogic ruleLogic = new RuleLogic();
                    try {
                        ruleLogic.run(toJson, entityIdMap, entityFieldMap, data);
                    } catch (RemoteException ex) {
                        Logger.getLogger(HkRuleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ConfigurationException ex) {
                        Logger.getLogger(HkRuleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (javax.rules.ConfigurationException ex) {
                        Logger.getLogger(HkRuleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private Map<String, Object> prepareMap(Object object, Map<String, Object> map) {
        if (object instanceof HkPlanDocument) {
            HkPlanDocument hkPlanDocument = (HkPlanDocument) object;
            BasicBSONObject fieldValue = hkPlanDocument.getFieldValue();
            Map<String, Object> toMap = fieldValue.toMap();
            if (!CollectionUtils.isEmpty(toMap)) {
                for (Map.Entry<String, Object> entry : toMap.entrySet()) {
                    map.put("plan." + entry.getKey(), entry.getValue());
                }
                map.put("plan.createdOn", hkPlanDocument.getCreatedOn());
                map.put("plan.createdBy", hkPlanDocument.getCreatedBy());
                map.put("plan.lastModifiedBy", hkPlanDocument.getLastModifiedBy());
                map.put("plan.lastModifiedOn", hkPlanDocument.getLastModifiedOn());
//                map.put("plan.status_of_plan", hkPlanDocument.getStatus());
//                map.put("plan.planType", hkPlanDocument.getPlanType());

            }
            return map;
        } else if (object instanceof HkIssueDocument) {
            HkIssueDocument hkIssueDocument = (HkIssueDocument) object;
            BasicBSONObject fieldValue = hkIssueDocument.getFieldValue();
            Map<String, Object> toMap = fieldValue.toMap();
            if (!CollectionUtils.isEmpty(toMap)) {
                for (Map.Entry<String, Object> entry : toMap.entrySet()) {
                    map.put("issue." + entry.getKey(), entry.getValue());
                }
                map.put("issue.createdOn", hkIssueDocument.getCreatedOn());
                map.put("issue.createdBy", hkIssueDocument.getCreatedBy());
                map.put("issue.lastModifiedBy", hkIssueDocument.getLastModifiedBy());
                map.put("issue.lastModifiedOn", hkIssueDocument.getLastModifiedOn());
                map.put("issue.issue_status", hkIssueDocument.getStatus());
            }
            return map;
        }
        return map;
    }

    private List<Map<String, Object>> prepareDataMapForPlanNextActNectSer(Map<String, Object> dataMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (dataMap.get("Plan") != null || dataMap.get("Issue") != null || dataMap.get("NextActivity") != null || dataMap.get("NextService") != null) {
            List<HkPlanDocument> plans = (ArrayList<HkPlanDocument>) dataMap.get("Plan");
            List<HkIssueDocument> issues = (ArrayList<HkIssueDocument>) dataMap.get("Issue");
            List<List<Object>> processList = new ArrayList<>();
            Integer index = 0;
            if (!CollectionUtils.isEmpty(plans)) {
                processList.add(new ArrayList<>());
                List<Object> objectList = new ArrayList<>(plans);
                processList.set(index++, objectList);
            }
            if (!CollectionUtils.isEmpty(issues)) {
                processList.add(new ArrayList<>());
                List<Object> objectList = new ArrayList<>(issues);
                processList.set(index++, objectList);
            }
            if (processList.size() == 1) {
                for (Object object : processList.get(0)) {
                    Map<String, Object> map = new HashMap<>();
                    result.add(prepareMap(object, map));
                }
                return result;
            } else if (processList.size() == 2) {
                for (Object object : processList.get(0)) {
                    Map<String, Object> map = new HashMap<>();
                    map = prepareMap(object, map);
                    for (Object object1 : processList.get(1)) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1 = prepareMap(object1, map);
                        result.add(map1);
                    }
                }
                return result;
            } else if (processList.size() == 3) {
                for (Object object : processList.get(0)) {
                    Map<String, Object> map = new HashMap<>();
                    map = prepareMap(object, map);
                    for (Object object1 : processList.get(1)) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1 = prepareMap(object1, map);
                        for (Object object2 : processList.get(2)) {
                            Map<String, Object> map2 = new HashMap<>();
                            map2 = prepareMap(object2, map1);
                            result.add(map2);
                        }
                    }
                }
                return result;
            } else if (processList.size() == 4) {
                for (Object object : processList.get(0)) {
                    Map<String, Object> map = new HashMap<>();
                    map = prepareMap(object, map);
                    for (Object object1 : processList.get(1)) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1 = prepareMap(object1, map);
                        for (Object object2 : processList.get(2)) {
                            Map<String, Object> map2 = new HashMap<>();
                            map2 = prepareMap(object2, map1);
                            for (Object object3 : processList.get(3)) {
                                Map<String, Object> map3 = new HashMap<>();
                                map3 = prepareMap(object3, map2);
                                result.add(map3);
                            }

                        }
                    }
                }
                return result;
            }
        }
        return result;
    }

    @Override
    public boolean executeRule(HkRuleDocument ruleDocument, Map<String, Object> dataMap) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, String> fieldTypes = new HashMap<>();
        /**
         * *************** Prepare dataMap in String,Object form **************
         */
        if (!CollectionUtils.isEmpty(dataMap)) {
            if ((dataMap.get("Plan") != null && !CollectionUtils.isEmpty((Collection<?>) dataMap.get("Plan"))) || (dataMap.get("Issue") != null && !CollectionUtils.isEmpty((Collection<?>) dataMap.get("Issue"))) || dataMap.get("NextActivity") != null || dataMap.get("NextService") != null) {
                List<Map<String, Object>> preparedDataMap = prepareDataMapForPlanNextActNectSer(dataMap);
                Map<String, Object> map = new HashMap<>();
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    switch (entry.getKey()) {
                        case "Invoice":
                            HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) entry.getValue();
                            BasicBSONObject fieldValue1 = invoiceDocument.getFieldValue();
                            Map<String, Object> toMap1 = fieldValue1.toMap();
                            if (!CollectionUtils.isEmpty(toMap1)) {
                                for (Map.Entry<String, Object> entry1 : toMap1.entrySet()) {
                                    map.put("invoice." + entry1.getKey(), entry1.getValue());
                                }
                                map.put("invoice.createdOn", invoiceDocument.getCreatedOn());
                                map.put("invoice.createdBy", invoiceDocument.getCreatedBy());
                                map.put("invoice.lastModifiedBy", invoiceDocument.getLastModifiedBy());
                                map.put("invoice.lastModifiedOn", invoiceDocument.getLastModifiedOn());
                                map.put("invoice.invoice_status", invoiceDocument.getStatus());
                            }
                            break;
                        case "Parcel":
                            HkParcelDocument parcelDocument = (HkParcelDocument) entry.getValue();
                            BasicBSONObject fieldValue2 = parcelDocument.getFieldValue();
                            Map<String, Object> toMap2 = fieldValue2.toMap();
                            if (!CollectionUtils.isEmpty(toMap2)) {
                                for (Map.Entry<String, Object> entry2 : toMap2.entrySet()) {
                                    map.put("parcel." + entry2.getKey(), entry2.getValue());
                                }
                                map.put("parcel.createdOn", parcelDocument.getCreatedOn());
                                map.put("parcel.createdBy", parcelDocument.getCreatedBy());
                                map.put("parcel.lastModifiedBy", parcelDocument.getLastModifiedBy());
                                map.put("parcel.lastModifiedOn", parcelDocument.getLastModifiedOn());
                                map.put("parcel.parcel_status", parcelDocument.getStatus());
                            }
                            break;
                        case "Lot":
                            HkLotDocument lotDocument = (HkLotDocument) entry.getValue();
                            BasicBSONObject fieldValue3 = lotDocument.getFieldValue();
                            Map<String, Object> toMap3 = fieldValue3.toMap();
                            if (!CollectionUtils.isEmpty(toMap3)) {
                                for (Map.Entry<String, Object> entry3 : toMap3.entrySet()) {
                                    map.put("lot." + entry3.getKey(), entry3.getValue());
                                }
                                map.put("lot.createdOn", lotDocument.getCreatedOn());
                                map.put("lot.createdBy", lotDocument.getCreatedBy());
                                map.put("lot.lastModifiedBy", lotDocument.getLastModifiedBy());
                                map.put("lot.lastModifiedOn", lotDocument.getLastModifiedOn());
                                map.put("lot.lot_status", lotDocument.getStatus());
                            }
                            break;
                        case "Packet":
                            HkPacketDocument packetDocument = (HkPacketDocument) entry.getValue();
                            BasicBSONObject fieldValue4 = packetDocument.getFieldValue();
                            Map<String, Object> toMap4 = fieldValue4.toMap();
                            if (!CollectionUtils.isEmpty(toMap4)) {
                                for (Map.Entry<String, Object> entry4 : toMap4.entrySet()) {
                                    map.put("packet." + entry4.getKey(), entry4.getValue());
                                }
                                map.put("packet.createdOn", packetDocument.getCreatedOn());
                                map.put("packet.createdBy", packetDocument.getCreatedBy());
                                map.put("packet.lastModifiedBy", packetDocument.getLastModifiedBy());
                                map.put("packet.lastModifiedOn", packetDocument.getLastModifiedOn());
                                map.put("packet.packet_status", packetDocument.getStatus());
                            }
                            break;
                        case "Issue":
                            HkIssueDocument issueDocument = (HkIssueDocument) entry.getValue();
                            BasicBSONObject fieldValue5 = issueDocument.getFieldValue();
                            Map<String, Object> toMap5 = fieldValue5.toMap();
                            if (!CollectionUtils.isEmpty(toMap5)) {
                                for (Map.Entry<String, Object> entry5 : toMap5.entrySet()) {
                                    map.put("issue." + entry5.getKey(), entry5.getValue());
                                }
                                map.put("issue.createdOn", issueDocument.getCreatedOn());
                                map.put("issue.createdBy", issueDocument.getCreatedBy());
                                map.put("issue.lastModifiedBy", issueDocument.getLastModifiedBy());
                                map.put("issue.lastModifiedOn", issueDocument.getLastModifiedOn());
                                map.put("issue.issue_status", issueDocument.getStatus());
                            }
                            break;
                        case "Transfer":
                            HkTransferDocument transferDocument = (HkTransferDocument) entry.getValue();
                            BasicBSONObject fieldValue6 = transferDocument.getFieldValue();
                            Map<String, Object> toMap6 = fieldValue6.toMap();
                            if (!CollectionUtils.isEmpty(toMap6)) {
                                for (Map.Entry<String, Object> entry6 : toMap6.entrySet()) {
                                    map.put("transfer." + entry6.getKey(), entry6.getValue());
                                }
                                map.put("transfer.createdOn", transferDocument.getCreatedOn());
                                map.put("transfer.createdBy", transferDocument.getCreatedBy());
                                map.put("transfer.lastModifiedBy", transferDocument.getLastModifiedBy());
                                map.put("transfer.lastModifiedOn", transferDocument.getLastModifiedOn());
                                map.put("transfer.transfer_status", transferDocument.getStatus());
                            }
                            break;
                        case "Sell":
                            HkSellDocument sellDocument = (HkSellDocument) entry.getValue();
                            BasicBSONObject fieldValue7 = sellDocument.getFieldValue();
                            Map<String, Object> toMap7 = fieldValue7.toMap();
                            if (!CollectionUtils.isEmpty(toMap7)) {
                                for (Map.Entry<String, Object> entry7 : toMap7.entrySet()) {
                                    map.put("sell." + entry7.getKey(), entry7.getValue());
                                }
                                map.put("sell.createdOn", sellDocument.getCreatedOn());
                                map.put("sell.createdBy", sellDocument.getCreatedBy());
                                map.put("sell.lastModifiedBy", sellDocument.getLastModifiedBy());
                                map.put("sell.lastModifiedOn", sellDocument.getLastModifiedOn());
                                map.put("sell.sell_status", sellDocument.getStatus());
                            }
                            break;
                    }
                }
                for (Map<String, Object> map1 : preparedDataMap) {
                    map1.putAll(map);
                    data.add(map1);
                }
            } else {
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {

                    switch (entry.getKey()) {
                        case "Invoice":
                            HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
                            BasicBSONObject fieldValue1 = invoiceDocument.getFieldValue();
                            Map<String, Object> toMap1 = fieldValue1.toMap();
                            if (!CollectionUtils.isEmpty(toMap1)) {
                                for (Map.Entry<String, Object> entry1 : toMap1.entrySet()) {
                                    data.get(0).put("invoice." + entry1.getKey(), entry1.getValue());
                                }
                                data.get(0).put("invoice.createdOn", invoiceDocument.getCreatedOn());
                                data.get(0).put("invoice.createdBy", invoiceDocument.getCreatedBy());
                                data.get(0).put("invoice.lastModifiedBy", invoiceDocument.getLastModifiedBy());
                                data.get(0).put("invoice.lastModifiedOn", invoiceDocument.getLastModifiedOn());
                                data.get(0).put("invoice.invoice_status", invoiceDocument.getStatus());
                            }
                            break;
                        case "Parcel":
                            HkParcelDocument parcelDocument = (HkParcelDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
//                            data.get(0).putAll(parcelDocument.getFieldValue());
                            BasicBSONObject fieldValue2 = parcelDocument.getFieldValue();
                            Map<String, Object> toMap2 = fieldValue2.toMap();
                            if (!CollectionUtils.isEmpty(toMap2)) {
                                for (Map.Entry<String, Object> entry2 : toMap2.entrySet()) {
                                    data.get(0).put("parcel." + entry2.getKey(), entry2.getValue());
                                }
                                data.get(0).put("parcel.createdOn", parcelDocument.getCreatedOn());
                                data.get(0).put("parcel.createdBy", parcelDocument.getCreatedBy());
                                data.get(0).put("parcel.lastModifiedBy", parcelDocument.getLastModifiedBy());
                                data.get(0).put("parcel.lastModifiedOn", parcelDocument.getLastModifiedOn());
                                data.get(0).put("parcel.parcel_status", parcelDocument.getStatus());
                            }
                            break;
                        case "Lot":
                            HkLotDocument lotDocument = (HkLotDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
//                            data.get(0).putAll(lotDocument.getFieldValue());
                            BasicBSONObject fieldValue3 = lotDocument.getFieldValue();
                            Map<String, Object> toMap3 = fieldValue3.toMap();
                            if (!CollectionUtils.isEmpty(toMap3)) {
                                for (Map.Entry<String, Object> entry3 : toMap3.entrySet()) {
                                    data.get(0).put("lot." + entry3.getKey(), entry3.getValue());
                                }
                                data.get(0).put("lot.createdOn", lotDocument.getCreatedOn());
                                data.get(0).put("lot.createdBy", lotDocument.getCreatedBy());
                                data.get(0).put("lot.lastModifiedBy", lotDocument.getLastModifiedBy());
                                data.get(0).put("lot.lastModifiedOn", lotDocument.getLastModifiedOn());
                                data.get(0).put("lot.lot_status", lotDocument.getStatus());
                            }
                            break;
                        case "Packet":
                            HkPacketDocument packetDocument = (HkPacketDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
//                            data.get(0).putAll(packetDocument.getFieldValue());
                            BasicBSONObject fieldValue4 = packetDocument.getFieldValue();
                            Map<String, Object> toMap4 = fieldValue4.toMap();
                            if (!CollectionUtils.isEmpty(toMap4)) {
                                for (Map.Entry<String, Object> entry4 : toMap4.entrySet()) {
                                    data.get(0).put("packet." + entry4.getKey(), entry4.getValue());
                                }
                                data.get(0).put("packet.createdOn", packetDocument.getCreatedOn());
                                data.get(0).put("packet.createdBy", packetDocument.getCreatedBy());
                                data.get(0).put("packet.lastModifiedBy", packetDocument.getLastModifiedBy());
                                data.get(0).put("packet.lastModifiedOn", packetDocument.getLastModifiedOn());
                                data.get(0).put("packet.packet_status", packetDocument.getStatus());
                            }
                            break;

                        case "Transfer":
                            HkTransferDocument transferDocument = (HkTransferDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
//                            data.get(0).putAll(packetDocument.getFieldValue());
                            BasicBSONObject fieldValue6 = transferDocument.getFieldValue();
                            Map<String, Object> toMap6 = fieldValue6.toMap();
                            if (!CollectionUtils.isEmpty(toMap6)) {
                                for (Map.Entry<String, Object> entry6 : toMap6.entrySet()) {
                                    data.get(0).put("transfer." + entry6.getKey(), entry6.getValue());
                                }
                                data.get(0).put("transfer.createdOn", transferDocument.getCreatedOn());
                                data.get(0).put("transfer.createdBy", transferDocument.getCreatedBy());
                                data.get(0).put("transfer.lastModifiedBy", transferDocument.getLastModifiedBy());
                                data.get(0).put("transfer.lastModifiedOn", transferDocument.getLastModifiedOn());
                                data.get(0).put("transfer.transfer_status", transferDocument.getStatus());
                            }
                            break;
                        case "Sell":
                            HkSellDocument sellDocument = (HkSellDocument) entry.getValue();
                            if (CollectionUtils.isEmpty(data)) {
                                data.add(new HashMap<>());
                            }
//                            data.get(0).putAll(packetDocument.getFieldValue());
                            BasicBSONObject fieldValue7 = sellDocument.getFieldValue();
                            Map<String, Object> toMap7 = fieldValue7.toMap();
                            if (!CollectionUtils.isEmpty(toMap7)) {
                                for (Map.Entry<String, Object> entry7 : toMap7.entrySet()) {
                                    data.get(0).put("sell." + entry7.getKey(), entry7.getValue());
                                }
                                data.get(0).put("sell.createdOn", sellDocument.getCreatedOn());
                                data.get(0).put("sell.createdBy", sellDocument.getCreatedBy());
                                data.get(0).put("sell.lastModifiedBy", sellDocument.getLastModifiedBy());
                                data.get(0).put("sell.lastModifiedOn", sellDocument.getLastModifiedOn());
                                data.get(0).put("sell.sell_status", sellDocument.getStatus());
                            }
                            break;
                    }
                }
            }
        }
        /**
         * ************* DataMap Prepared ******************
         */

        /**
         * ************* Prepare query and fieldtype Map ******************
         */
        StringBuilder query = new StringBuilder();
        if (ruleDocument != null && !CollectionUtils.isEmpty(ruleDocument.getCriterias())) {
            String appliesTo = null;
            if (ruleDocument.getApply().equalsIgnoreCase("All")) {
                appliesTo = "AND";
            } else if (ruleDocument.getApply().equalsIgnoreCase("Any")) {
                appliesTo = "OR";
            }

            Integer count = 1;
            for (HkRuleCriteriaDocument hkRuleCriteriaDocument : ruleDocument.getCriterias()) {
                String fieldType = hkRuleCriteriaDocument.getFieldType();
                if (hkRuleCriteriaDocument.getOperator().equals("in between") || hkRuleCriteriaDocument.getOperator().equals("not in between")) {
                    String value = StringUtils.collectionToCommaDelimitedString(Arrays.asList(hkRuleCriteriaDocument.getValue(), hkRuleCriteriaDocument.getValue1()));
                    if (hkRuleCriteriaDocument.getEntity() < 0) {
                        query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(value).append("' ");
                    } else {
                        query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(value).append("' ");
                    }
                    if (hkRuleCriteriaDocument.getValue() instanceof Date) {
                        fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), "DateList");
                    }
                    if (hkRuleCriteriaDocument.getValue() instanceof Integer) {
                        fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), "IntegerList");
                    }
                } else if (hkRuleCriteriaDocument.getOperator().equals("has no value") || hkRuleCriteriaDocument.getOperator().equals("has any value")) {
                    if (hkRuleCriteriaDocument.getEntity() < 0) {
                        query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ");
                    } else {
                        query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ");
                    }
                } else if (hkRuleCriteriaDocument.getOperator().equals("has any value from")) {
                    if (hkRuleCriteriaDocument.getEntity() < 0) {
                        query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(hkRuleCriteriaDocument.getValue()).append("' ");
                    } else {
                        query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(hkRuleCriteriaDocument.getValue()).append("' ");
                    }
                } else if (fieldType.equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                    if (hkRuleCriteriaDocument.getPointerComponentType().equals("Date")) {
                        if (hkRuleCriteriaDocument.getEntity() < 0) {
                            query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(hkRuleCriteriaDocument.getValue().toString()).append("' ");
                            fieldTypes.put(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + "." + HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString())), hkRuleCriteriaDocument.getFieldType());
                        } else {
                            query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(hkRuleCriteriaDocument.getValue().toString()).append("' ");
                            fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
                        }

                    } else {
                        if (hkRuleCriteriaDocument.getEntity() < 0) {
                            query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ").append(hkRuleCriteriaDocument.getValue());
                            fieldTypes.put(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + "." + HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString())), hkRuleCriteriaDocument.getFieldType());
                        } else {
                            query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ").append(hkRuleCriteriaDocument.getValue());
                            fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
                        }
//                        fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getPointerComponentType());
                    }
                } else if (fieldType.equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                    query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ").append(hkRuleCriteriaDocument.getValue());
                    fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), "String");
                } else if (hkRuleCriteriaDocument.getFieldType().equals("Date")) {
                    if (hkRuleCriteriaDocument.getEntity() < 0) {
                        query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(hkRuleCriteriaDocument.getValue().toString()).append("' ");
                        fieldTypes.put(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + "." + HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString())), hkRuleCriteriaDocument.getFieldType());
                    } else {
                        if (hkRuleCriteriaDocument.getValue() != null) {
                            try {
                                String dateStr = hkRuleCriteriaDocument.getValue().toString();
                                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS\'Z\'");
                                Date dateObj = formatDate.parse(dateStr);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(dateObj);
                                cal.set(Calendar.HOUR, 0);
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                cal.set(Calendar.SECOND, 0);
                                query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" '").append(String.valueOf(cal.getTime())).append("' ");
                                fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
                            } catch (ParseException ex) {
                                Logger.getLogger(HkRuleServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
//                    fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
                } else {
                    if (hkRuleCriteriaDocument.getEntity() < 0) {
                        query.append(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + ".").append(HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString()))).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ").append(hkRuleCriteriaDocument.getValue());
                        fieldTypes.put(HkSystemConstantUtil.ACTIVITY_SERVICE_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getEntity().toString())) + "." + HkSystemConstantUtil.ACTIVITY_SERVICE_FIELD_ENTITY_MAP.get(Integer.parseInt(hkRuleCriteriaDocument.getField().toString())), hkRuleCriteriaDocument.getFieldType());
                    } else {
                        query.append(hkRuleCriteriaDocument.getEntityName() + ".").append(hkRuleCriteriaDocument.getDbFieldName()).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ").append(hkRuleCriteriaDocument.getValue());
                        fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
                    }

                }
                if (count != ruleDocument.getCriterias().size()) {
                    query.append(" ").append(appliesTo).append(" ");
                }
                count = count + 1;
            }
        }
        /**
         * ************* Query and fieldType map prepared ******************
         */

        System.out.println("data::" + data.toString());
        System.out.println("query::" + query.toString());
        System.out.println("fieldTypes::" + fieldTypes.toString());

        /**
         * ************* Execute query in rule engine ******************
         */
        Operations operations = Operations.INSTANCE;

        // register new operations with the previously created container
        operations.registerOperation(new And());
        operations.registerOperation(new Or());
        operations.registerOperation(new Equals());
        operations.registerOperation(new NotEquals());
        operations.registerOperation(new Not());
        operations.registerOperation(new GreaterThan());
        operations.registerOperation(new LessThan());
        operations.registerOperation(new GreaterThanEquals());
        operations.registerOperation(new LessThanEquals());
        operations.registerOperation(new HasNoValue());
        operations.registerOperation(new HasAnyValue());
        operations.registerOperation(new InBetween());
        operations.registerOperation(new NotInBetween());
        operations.registerOperation(new IsBefore());
        operations.registerOperation(new IsAfter());
        operations.registerOperation(new In());

        Expression ex1 = ExpressionParser.fromString(query.toString(), fieldTypes);

        // create the rules and link them to the accoridng expression and action
        Rule rule1 = new Rule.Builder()
                .withExpression(ex1)
                .build();
        boolean result = false;
        if (!CollectionUtils.isEmpty(data)) {
            for (Map<String, Object> map : data) {
                result = rule1.eval(map, fieldTypes, null);
                if (result) {
                    break;
                }
            }
        }
        System.out.println("Action triggered: " + result);

        /**
         * ************** Rule executed *****************
         */
        return result;
    }

    @Override
    @MongoTransaction
    public void saveAllRuleSet(List<HkRuleSetDocument> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            mongoGenericDao.createAll(documents);
        }
    }

    @Override
    @MongoTransaction
    public void saveAllCriteriaSetDocuments(List<HkCriteriaSetDocument> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            for (HkCriteriaSetDocument document : documents) {
                mongoGenericDao.update(document);
            }
        }
    }
}
