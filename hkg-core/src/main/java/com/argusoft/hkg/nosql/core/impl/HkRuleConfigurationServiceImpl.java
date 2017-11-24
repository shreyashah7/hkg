/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkRuleConfigurationService;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
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
import com.argusoft.hkg.ruleexecution.IsAbsentToday;
import com.argusoft.hkg.ruleexecution.IsAfter;
import com.argusoft.hkg.ruleexecution.IsBefore;
import com.argusoft.hkg.ruleexecution.LessThan;
import com.argusoft.hkg.ruleexecution.LessThanEquals;
import com.argusoft.hkg.ruleexecution.Not;
import com.argusoft.hkg.ruleexecution.NotEquals;
import com.argusoft.hkg.ruleexecution.NotInBetween;
import com.argusoft.hkg.ruleexecution.Operations;
import com.argusoft.hkg.ruleexecution.Or;
import com.argusoft.hkg.ruleexecution.RestrictByIP;
import com.argusoft.hkg.ruleexecution.Rule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author gautam
 */
@Service
public class HkRuleConfigurationServiceImpl implements HkRuleConfigurationService {

    @Autowired
    MongoGenericDao mongoGenericDao;
    @Autowired
    @Lazy(true)
    HkStockService stockService;

    public static final String RULE_NUMBER = "ruleNumber";
    public static final String FRANCHISE_ID = "franchiseId";
    public static final String IS_ARCHIVE = "isArchive";
    public static final String CATEGORY = "category";
    public static final String SKIP_RULES = "skipRules";
    public static final String RULE_NAME = "ruleName";
    public static final String TO_DATE = "toDate";
    public static final String FROM_DATE = "fromDate";
    public static final String TYPE = "type";
    public static final String FEATURES = "features";
    public static final String FIELDS_TO_BE_APPLIED = "fieldsToBeApplied";

    private Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    @Override
    public HkCriteriaSetDocument retrieveRuleDocumentById(ObjectId id) {
        HkCriteriaSetDocument ruleDocument = null;
        if (id != null) {
            ruleDocument = (HkCriteriaSetDocument) mongoGenericDao.retrieveById(id, HkCriteriaSetDocument.class);
        }
        return ruleDocument;
    }

    @Override
    public HkCriteriaSetDocument retrieveRuleDocumentByRuleNumber(Long ruleNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RULE_NUMBER).is(ruleNumber));
        return mongoGenericDao.getMongoTemplate().findOne(query, HkCriteriaSetDocument.class);
    }

    @Override
    @MongoTransaction
    public void saveRuleDocument(HkCriteriaSetDocument hkCriteriaSetDocument) {
        if (hkCriteriaSetDocument != null) {
            mongoGenericDao.update(hkCriteriaSetDocument);
        }
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveAllRules(Long franchiseId) {
        Query query = new Query();
        List<HkCriteriaSetDocument> documents = null;
        query.addCriteria(Criteria.where(FRANCHISE_ID).is(franchiseId));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        List<HkCriteriaSetDocument> criteriaSetDocuments = mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
        if (!CollectionUtils.isEmpty(criteriaSetDocuments)) {
            documents = new ArrayList<>();
            for (HkCriteriaSetDocument setDocument : criteriaSetDocuments) {
                if (setDocument.getFeatures() != null) {
                    if (setDocument.getFeatures().contains(-1)) {
                        documents.add(setDocument);
                    } else {
                        if (setDocument.getFromDate().before(getCurrentDate()) && setDocument.getToDate().after(getCurrentDate())) {
                            documents.add(setDocument);
                        }
                    }
                }
            }
        }
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveRuleDocumentsByRuleNumbers(List<Long> ruleNumbers) {
        Query query = new Query();
        query.addCriteria(Criteria.where(RULE_NUMBER).in(ruleNumbers));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveExceptionRuleDocuments(Long ruleNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where(SKIP_RULES).in(ruleNumber));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> searchRuleDocumentsByName(String searchName) {
        //{ $or: [ { quantity: { $lt: 20 } }, { price: 10 } ] }
        BasicQuery query
                = new BasicQuery("{ $or: [ { $where: \"/" + searchName + "/i.test(this." + RULE_NAME + ")\" }, { $where: \"/" + searchName + "/i.test(this." + RULE_NUMBER + ")\" } ] }");
        //Query query = new Query();
        //query.addCriteria(new Criteria().orOperator(Criteria.where(RULE_NAME).regex(searchName, "i"), Criteria.where(RULE_NUMBER).regex(searchName, "i")));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveScreenRulesForFeature(Long featureId) {

        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        query.addCriteria(Criteria.where(TYPE).is(HkSystemConstantUtil.RuleConfiguration.RuleType.SCREEN));
        query.addCriteria(Criteria.where(FEATURES).in(featureId));
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveScreenRulesAssociatedWithField(Long featureId, List<Long> fieldIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        query.addCriteria(Criteria.where(TYPE).is(HkSystemConstantUtil.RuleConfiguration.RuleType.SCREEN));
        query.addCriteria(Criteria.where(FEATURES).in(featureId));
        query.addCriteria(Criteria.where(FIELDS_TO_BE_APPLIED).in(fieldIds));
        List<HkCriteriaSetDocument> rules = mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);

        return rules;
    }

    @Override
    public List<HkCriteriaSetDocument> retrievePreRulesByFeature(Long featureId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        query.addCriteria(Criteria.where(TYPE).is(HkSystemConstantUtil.RuleConfiguration.RuleType.PRE));
        query.addCriteria(Criteria.where(FEATURES).in(featureId));
        List<HkCriteriaSetDocument> rules = mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);

        return rules;
    }

    @Override
    public List<HkCriteriaSetDocument> retrievePostRulesByFeature(Long featureId, Boolean dateCriteria) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        if (dateCriteria != null && dateCriteria) {
            query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
            query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        }
        query.addCriteria(Criteria.where(CATEGORY).ne(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY));
        query.addCriteria(Criteria.where(TYPE).is(HkSystemConstantUtil.RuleConfiguration.RuleType.POST));
        query.addCriteria(Criteria.where(FEATURES).in(featureId));
        List<HkCriteriaSetDocument> rules = mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);

        return rules;
    }

    @Override
    public List<HkCriteriaSetDocument> retrieveExceptionsForRules(List<Long> ruleNumbers) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        query.addCriteria(Criteria.where(TO_DATE).gte(getCurrentDate()));
        query.addCriteria(Criteria.where(FROM_DATE).lte(getCurrentDate()));
        query.addCriteria(Criteria.where(CATEGORY).is(HkSystemConstantUtil.RuleConfiguration.EXCEPTION_CATEGORY));
        query.addCriteria(Criteria.where(SKIP_RULES).in(ruleNumbers));
        return mongoGenericDao.getMongoTemplate().find(query, HkCriteriaSetDocument.class);
    }

    @Override
    public List<HkCriteriaSetDocument> executeRuleSet(Map<String, Object> currentDataMap, List<HkCriteriaSetDocument> ruleDocuments, ObjectId entityId, String entityType, boolean isScreenRule, Map<String, String> otherEntitysIdMap) {
        System.out.println("Executing Rule Set");
        Set<HkCriteriaSetDocument> result = new HashSet<>();
        if (!CollectionUtils.isEmpty(ruleDocuments)) {
            List<Long> ruleNumbers = new ArrayList<>();
            for (HkCriteriaSetDocument ruleDocument : ruleDocuments) {
                ruleNumbers.add(ruleDocument.getRuleNumber());
            }
            System.out.println("RuleNumbers:::" + ruleNumbers);
            List<HkCriteriaSetDocument> exceptionDocuments = this.retrieveExceptionsForRules(ruleNumbers);
            if (currentDataMap == null) {
                currentDataMap = new HashMap<>();
            }
            //Find if any entity other details required.
            Set<String> requiredFieldsToEvaluate = new HashSet<>();
            Set<String> additionalEntityRequired = new HashSet<>();
            Map<String, Object> entityDataMap = new HashMap<>();
            if (StringUtils.hasText(entityType) && !entityType.equalsIgnoreCase(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN)) {
                for (HkCriteriaSetDocument ruleDocument : ruleDocuments) {
                    for (HkRuleCriteriaDocument criteria : ruleDocument.getCriterias()) {
                        requiredFieldsToEvaluate.add(criteria.getEntityName() + "." + criteria.getDbFieldName());
                    }
                }
                for (HkCriteriaSetDocument exceptionDocument : exceptionDocuments) {
                    for (HkRuleCriteriaDocument criteria : exceptionDocument.getCriterias()) {
                        requiredFieldsToEvaluate.add(criteria.getEntityName() + "." + criteria.getDbFieldName());
                    }
                }
                System.out.println("currentDataMap::::" + currentDataMap);
                for (String str : requiredFieldsToEvaluate) {
                    if (!currentDataMap.keySet().contains(str)) {
                        additionalEntityRequired.add(str.split("\\.")[0]);//invoice.createdBy
                    }
                }
                System.out.println("requiredFieldsToEvaluate:::" + requiredFieldsToEvaluate);
                System.out.println("additionalEntityRequired:::" + additionalEntityRequired);
                if (!CollectionUtils.isEmpty(additionalEntityRequired)) {
                    entityDataMap = prepareEntityMap(new ArrayList<>(additionalEntityRequired), entityType, entityId, otherEntitysIdMap);
                }
            }
            System.out.println("entityDataMap:::" + entityDataMap);

            Set<Long> rulesToIgnore = new HashSet<>();
            if (!CollectionUtils.isEmpty(exceptionDocuments)) {
                for (HkCriteriaSetDocument exceptionDocument : exceptionDocuments) {
                    boolean res = executeRule(exceptionDocument, entityDataMap, currentDataMap);
                    if (res) {
                        rulesToIgnore.addAll(exceptionDocument.getSkipRules());
                    }
                }
            }
            //Remove rules to ignore.
            if (!CollectionUtils.isEmpty(rulesToIgnore)) {
                Iterator<HkCriteriaSetDocument> itr = ruleDocuments.iterator();
                while (itr.hasNext()) {
                    HkCriteriaSetDocument ruleDoc = itr.next();
                    if (rulesToIgnore.contains(ruleDoc.getRuleNumber())) {
                        itr.remove();
                    }
                }
            }

            for (HkCriteriaSetDocument ruleDocument : ruleDocuments) {
                boolean res = executeRule(ruleDocument, entityDataMap, currentDataMap);
                System.out.println("RESULT INSIDE:::" + res);
                if (res) {
                    if (isScreenRule) {
                        result.add(ruleDocument);
                    } else {
                        result.add(ruleDocument);
                        break;
                    }
                }
            }

        }
        return new ArrayList<>(result);
    }

    private Map<String, Object> prepareEntityMap(List<String> requiredEntitys, String entityType, ObjectId entityId, Map<String, String> otherEntitysIdMap) {
        Map<String, Object> resultMap = new HashMap<>();
        ObjectId invoiceId, parcelId, lotId, packetId;
        //In case of Adding new entity object, where entity id won't be available.
        if (entityId == null) {
            if (!CollectionUtils.isEmpty(otherEntitysIdMap)) {
                if (otherEntitysIdMap.containsKey("invoiceId")) {
                    invoiceId = new ObjectId(otherEntitysIdMap.get("invoiceId"));
                    if (requiredEntitys.contains("invoice")) {
                        HkInvoiceDocument invoice = stockService.retrieveInvoiceById(invoiceId);
                        resultMap.put(HkSystemConstantUtil.Feature.INVOICE, invoice);
                    }
                }
                if (otherEntitysIdMap.containsKey("parcelId")) {
                    parcelId = new ObjectId(otherEntitysIdMap.get("parcelId"));
                    if (requiredEntitys.contains("parcel")) {
                        HkParcelDocument parcel = stockService.retrieveParcelById(parcelId);
                        resultMap.put(HkSystemConstantUtil.Feature.PARCEL, parcel);
                    }
                }
                if (otherEntitysIdMap.containsKey("lotId")) {
                    lotId = new ObjectId(otherEntitysIdMap.get("lotId"));
                    if (requiredEntitys.contains("lot")) {
                        HkLotDocument lot = stockService.retrieveLotById(lotId);
                        resultMap.put(HkSystemConstantUtil.Feature.LOT, lot);
                    }
                }
                if (otherEntitysIdMap.containsKey("packetId")) {
                    packetId = new ObjectId(otherEntitysIdMap.get("packetId"));
                    if (requiredEntitys.contains("packet")) {
                        HkPacketDocument packet = stockService.retrievePacketById(packetId,true);
                        resultMap.put(HkSystemConstantUtil.Feature.PACKET, packet);
                    }
                }
            }
        } else {
            switch (entityType) {
                case HkSystemConstantUtil.Feature.INVOICE:
                    HkInvoiceDocument invoice = stockService.retrieveInvoiceById(entityId);
                    resultMap.put(HkSystemConstantUtil.Feature.INVOICE, invoice);
                    break;
                case HkSystemConstantUtil.Feature.PARCEL:
                    HkParcelDocument parcel = stockService.retrieveParcelById(entityId);
                    resultMap.put(HkSystemConstantUtil.Feature.PARCEL, parcel);
                    if (requiredEntitys.contains("invoice")) {
                        resultMap.put(HkSystemConstantUtil.Feature.INVOICE, stockService.retrieveInvoiceById(parcel.getInvoice()));
                    }
                    break;
                case HkSystemConstantUtil.Feature.SUB_LOT:
                    List<HkSubLotDocument> subLots = stockService.retrieveSubLotByIds(Arrays.asList(entityId));
                    if (!CollectionUtils.isEmpty(subLots)) {
                        resultMap.put(HkSystemConstantUtil.Feature.SUB_LOT, subLots.get(0));
                        if (requiredEntitys.contains("invoice")) {
                            resultMap.put(HkSystemConstantUtil.Feature.INVOICE, stockService.retrieveInvoiceById(subLots.get(0).getInvoice()));
                        }
                        if (requiredEntitys.contains("parcel")) {
                            resultMap.put(HkSystemConstantUtil.Feature.PARCEL, stockService.retrieveParcelById(subLots.get(0).getParcel()));
                        }
                    }
                    break;
                case HkSystemConstantUtil.Feature.LOT:
                    HkLotDocument lot = stockService.retrieveLotById(entityId);
                    resultMap.put(HkSystemConstantUtil.Feature.LOT, lot);
                    if (requiredEntitys.contains("invoice")) {
                        resultMap.put(HkSystemConstantUtil.Feature.INVOICE, stockService.retrieveInvoiceById(lot.getInvoice()));
                    }
                    if (requiredEntitys.contains("parcel")) {
                        resultMap.put(HkSystemConstantUtil.Feature.PARCEL, stockService.retrieveParcelById(lot.getParcel()));
                    }
                    break;
                case HkSystemConstantUtil.Feature.PACKET:
                    HkPacketDocument packet = stockService.retrievePacketById(entityId,true);
                    resultMap.put(HkSystemConstantUtil.Feature.PACKET, packet);
                    if (requiredEntitys.contains("invoice")) {
                        resultMap.put(HkSystemConstantUtil.Feature.INVOICE, stockService.retrieveInvoiceById(packet.getInvoice()));
                    }
                    if (requiredEntitys.contains("parcel")) {
                        resultMap.put(HkSystemConstantUtil.Feature.PARCEL, stockService.retrieveParcelById(packet.getParcel()));
                    }
                    if (requiredEntitys.contains("lot")) {
                        resultMap.put(HkSystemConstantUtil.Feature.LOT, stockService.retrieveLotById(packet.getLot()));
                    }
                    break;
                case HkSystemConstantUtil.Feature.ROUGH_PURCHASE:
                    HkPurchaseDocument purchase = stockService.retrievePurchaseDocumentById(entityId);
                    resultMap.put(HkSystemConstantUtil.Feature.ROUGH_PURCHASE, purchase);
                    break;
                case HkSystemConstantUtil.Feature.SELL:
                    HkSellDocument sell = stockService.retrieveSellDocumentById(entityId);
                    resultMap.put(HkSystemConstantUtil.Feature.SELL, sell);
                    break;
            }
        }
        return resultMap;
    }

    @Override
    public boolean executeRule(HkCriteriaSetDocument ruleDocument, Map<String, Object> dataMap, Map<String, Object> currentDataMap) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, String> fieldTypes = new HashMap<>();
        Map<String, String> componentTypes = new HashMap<>();
        /**
         * *************** Prepare dataMap in String,Object form **************
         */
        if (!CollectionUtils.isEmpty(dataMap)) {

            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                switch (entry.getKey()) {
                    case HkSystemConstantUtil.Feature.INVOICE:
                        HkInvoiceDocument invoiceDocument = (HkInvoiceDocument) entry.getValue();
                        BasicBSONObject fieldValue1 = invoiceDocument.getFieldValue();
                        Map<String, Object> toMap1 = fieldValue1.toMap();
                        if (!CollectionUtils.isEmpty(toMap1)) {
                            for (Map.Entry<String, Object> entry1 : toMap1.entrySet()) {
                                map.put("invoice." + entry1.getKey(), entry1.getValue());
                            }
                            map.put("invoice.createdOn", invoiceDocument.getCreatedOn());
                            map.put("invoice.invoice_createdBy", invoiceDocument.getCreatedBy() + ":E");
                            map.put("invoice.invoice_lastModifiedBy", invoiceDocument.getLastModifiedBy() + ":E");
                            map.put("invoice.lastModifiedOn", invoiceDocument.getLastModifiedOn());
                            map.put("invoice.invoice_status", invoiceDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PARCEL:
                        HkParcelDocument parcelDocument = (HkParcelDocument) entry.getValue();
                        BasicBSONObject fieldValue2 = parcelDocument.getFieldValue();
                        Map<String, Object> toMap2 = fieldValue2.toMap();
                        if (!CollectionUtils.isEmpty(toMap2)) {
                            for (Map.Entry<String, Object> entry2 : toMap2.entrySet()) {
                                map.put("parcel." + entry2.getKey(), entry2.getValue());
                            }
                            map.put("parcel.createdOn", parcelDocument.getCreatedOn());
                            map.put("parcel.parcel_createdBy", parcelDocument.getCreatedBy() + ":E");
                            map.put("parcel.parcel_lastModifiedBy", parcelDocument.getLastModifiedBy() + ":E");
                            map.put("parcel.lastModifiedOn", parcelDocument.getLastModifiedOn());
                            map.put("parcel.parcel_status", parcelDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.LOT:
                        HkLotDocument lotDocument = (HkLotDocument) entry.getValue();
                        BasicBSONObject fieldValue3 = lotDocument.getFieldValue();
                        Map<String, Object> toMap3 = fieldValue3.toMap();
                        if (!CollectionUtils.isEmpty(toMap3)) {
                            for (Map.Entry<String, Object> entry3 : toMap3.entrySet()) {
                                map.put("lot." + entry3.getKey(), entry3.getValue());
                            }
                            map.put("lot.createdOn", lotDocument.getCreatedOn());
                            map.put("lot.lot_createdBy", lotDocument.getCreatedBy() + ":E");
                            map.put("lot.lot_lastModifiedBy", lotDocument.getLastModifiedBy() + ":E");
                            map.put("lot.lastModifiedOn", lotDocument.getLastModifiedOn());
                            map.put("lot.lot_status", lotDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.PACKET:
                        HkPacketDocument packetDocument = (HkPacketDocument) entry.getValue();
                        BasicBSONObject fieldValue4 = packetDocument.getFieldValue();
                        Map<String, Object> toMap4 = fieldValue4.toMap();
                        if (!CollectionUtils.isEmpty(toMap4)) {
                            for (Map.Entry<String, Object> entry4 : toMap4.entrySet()) {
                                map.put("packet." + entry4.getKey(), entry4.getValue());
                            }
                            map.put("packet.createdOn", packetDocument.getCreatedOn());
                            map.put("packet.packet_createdBy", packetDocument.getCreatedBy() + ":E");
                            map.put("packet.packet_lastModifiedBy", packetDocument.getLastModifiedBy() + ":E");
                            map.put("packet.lastModifiedOn", packetDocument.getLastModifiedOn());
                            map.put("packet.packet_status", packetDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.ISSUE:
                        HkIssueDocument issueDocument = (HkIssueDocument) entry.getValue();
                        BasicBSONObject fieldValue5 = issueDocument.getFieldValue();
                        Map<String, Object> toMap5 = fieldValue5.toMap();
                        if (!CollectionUtils.isEmpty(toMap5)) {
                            for (Map.Entry<String, Object> entry5 : toMap5.entrySet()) {
                                map.put("issue." + entry5.getKey(), entry5.getValue());
                            }
                            map.put("issue.createdOn", issueDocument.getCreatedOn());
                            map.put("issue.issue_createdBy", issueDocument.getCreatedBy() + ":E");
                            map.put("issue.issue_lastModifiedBy", issueDocument.getLastModifiedBy() + ":E");
                            map.put("issue.lastModifiedOn", issueDocument.getLastModifiedOn());
                            map.put("issue.issue_status", issueDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.TRANSFER:
                        HkTransferDocument transferDocument = (HkTransferDocument) entry.getValue();
                        BasicBSONObject fieldValue6 = transferDocument.getFieldValue();
                        Map<String, Object> toMap6 = fieldValue6.toMap();
                        if (!CollectionUtils.isEmpty(toMap6)) {
                            for (Map.Entry<String, Object> entry6 : toMap6.entrySet()) {
                                map.put("transfer." + entry6.getKey(), entry6.getValue());
                            }
                            map.put("transfer.createdOn", transferDocument.getCreatedOn());
                            map.put("transfer.transfer_createdBy", transferDocument.getCreatedBy() + ":E");
                            map.put("transfer.transfer_lastModifiedBy", transferDocument.getLastModifiedBy() + ":E");
                            map.put("transfer.lastModifiedOn", transferDocument.getLastModifiedOn());
                            map.put("transfer.transfer_status", transferDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SELL:
                        HkSellDocument sellDocument = (HkSellDocument) entry.getValue();
                        BasicBSONObject fieldValue7 = sellDocument.getFieldValue();
                        Map<String, Object> toMap7 = fieldValue7.toMap();
                        if (!CollectionUtils.isEmpty(toMap7)) {
                            for (Map.Entry<String, Object> entry7 : toMap7.entrySet()) {
                                map.put("sell." + entry7.getKey(), entry7.getValue());
                            }
                            map.put("sell.createdOn", sellDocument.getCreatedOn());
                            map.put("sell.sell_createdBy", sellDocument.getCreatedBy() + ":E");
                            map.put("sell.sell_lastModifiedBy", sellDocument.getLastModifiedBy() + ":E");
                            map.put("sell.lastModifiedOn", sellDocument.getLastModifiedOn());
                            map.put("sell.sell_status", sellDocument.getStatus());
                        }
                        break;
                    case HkSystemConstantUtil.Feature.SUB_LOT:
                        HkSubLotDocument subLotDocument = (HkSubLotDocument) entry.getValue();
                        BasicBSONObject fieldValue8 = subLotDocument.getFieldValue();
                        Map<String, Object> toMap8 = fieldValue8.toMap();
                        if (!CollectionUtils.isEmpty(toMap8)) {
                            for (Map.Entry<String, Object> entry8 : toMap8.entrySet()) {
                                map.put("subLotEntity." + entry8.getKey(), entry8.getValue());
                            }
                            map.put("subLotEntity.createdOn", subLotDocument.getCreatedOn());
                            map.put("subLotEntity.sublot_createdBy", subLotDocument.getCreatedBy() + ":E");
                            map.put("subLotEntity.sublot_lastModifiedBy", subLotDocument.getLastModifiedBy() + ":E");
//                            map.put("subLotEntity.lastModifiedOn", subLotDocument.getLastModifiedOn());
//                            map.put("subLotEntity.lot_status", subLotDocument.getStatus());
                        }
                        break;
                }
            }
            data.add(map);
        }

        if (CollectionUtils.isEmpty(data)) {
            data.add(new HashMap<>());
        }
        if (!CollectionUtils.isEmpty(currentDataMap)) {
            data.get(0).putAll(currentDataMap);
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
                if (hkRuleCriteriaDocument.getEntity() != -1l) {
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
                        fieldTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getDbFieldType());
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
                    componentTypes.put(hkRuleCriteriaDocument.getEntityName() + "." + hkRuleCriteriaDocument.getDbFieldName(), hkRuleCriteriaDocument.getFieldType());
                    if (count != ruleDocument.getCriterias().size()) {
                        query.append(" ").append(appliesTo).append(" ");
                    }
                    count = count + 1;
                } else {
                    System.out.println("hkRuleCriteriaDocument.getOperator()" + hkRuleCriteriaDocument.getOperator());
                    if (hkRuleCriteriaDocument.getOperator().equalsIgnoreCase(HkSystemConstantUtil.RuleConfiguration.CONSTANT.IsAbsentToday)) {
                        query.append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + ".").append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.IsAbsentToday).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ");
                    }
                    if (hkRuleCriteriaDocument.getOperator().equalsIgnoreCase(HkSystemConstantUtil.RuleConfiguration.CONSTANT.RESTRICT_BY_IP)) {
                        query.append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + ".").append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.RESTRICT_BY_IP).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ");
                    }
                    if (hkRuleCriteriaDocument.getOperator().equalsIgnoreCase(HkSystemConstantUtil.RuleConfiguration.CONSTANT.ABSENT_WITHOUT_INFO)) {
                        query.append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.LOGIN + ".").append(HkSystemConstantUtil.RuleConfiguration.CONSTANT.RESTRICT_BY_IP).append(" ").append(HkSystemConstantUtil.ruleOperatorMap.get(hkRuleCriteriaDocument.getOperator())).append(" ");
                    }
                }
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
        operations.registerOperation(new IsAbsentToday());
        operations.registerOperation(new RestrictByIP());

        Expression ex1 = ExpressionParser.fromString(query.toString(), fieldTypes);

        // create the rules and link them to the accoridng expression and action
        Rule rule1 = new Rule.Builder()
                .withExpression(ex1)
                .build();
        boolean result = false;
        if (!CollectionUtils.isEmpty(data)) {
            for (Map<String, Object> map : data) {
                result = rule1.eval(map, fieldTypes, componentTypes);
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

}
