/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author gautam
 */
public interface HkRuleConfigurationService {

    /**
     * @param id
     * @return Rule document by id
     */
    public HkCriteriaSetDocument retrieveRuleDocumentById(ObjectId id);

    /**
     * @param ruleNumber
     * @return Rule document by rule number(unique to each rule)
     */
    public HkCriteriaSetDocument retrieveRuleDocumentByRuleNumber(Long ruleNumber);

    /**
     * @param ruleNumber
     * @return rule documents where ruleNumber is in skip list.
     */
    public List<HkCriteriaSetDocument> retrieveExceptionRuleDocuments(Long ruleNumber);

    /**
     * @param ruleNumbers
     * @return rule documents of same rule numbers
     */
    public List<HkCriteriaSetDocument> retrieveRuleDocumentsByRuleNumbers(List<Long> ruleNumbers);

    /**
     * Save or update document
     *
     * @param hkCriteriaSetDocument
     */
    public void saveRuleDocument(HkCriteriaSetDocument hkCriteriaSetDocument);

    /**
     *
     * @param franchiseId
     * @return all rule documents
     */
    public List<HkCriteriaSetDocument> retrieveAllRules(Long franchiseId);

    /**
     *
     * @param searchName
     * @return rule documents according to matched name.
     */
    public List<HkCriteriaSetDocument> searchRuleDocumentsByName(String searchName);

    public List<HkCriteriaSetDocument> retrieveScreenRulesForFeature(Long featureId);

    public List<HkCriteriaSetDocument> retrieveExceptionsForRules(List<Long> ruleNumbers);

    public boolean executeRule(HkCriteriaSetDocument ruleDocument, Map<String, Object> dataMap, Map<String, Object> currentDataMap);

    public List<HkCriteriaSetDocument> retrieveScreenRulesAssociatedWithField(Long featureId, List<Long> fieldIds);

    public List<HkCriteriaSetDocument> retrievePreRulesByFeature(Long featureId);

    public List<HkCriteriaSetDocument> retrievePostRulesByFeature(Long featureId, Boolean dateCriteria);

    public List<HkCriteriaSetDocument> executeRuleSet(Map<String, Object> currentDataMap, List<HkCriteriaSetDocument> ruleDocuments, ObjectId entityId, String entityType, boolean isScreenRule, Map<String, String> otherEntitysIdMap);

}
