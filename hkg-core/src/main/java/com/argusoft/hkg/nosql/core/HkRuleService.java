/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author mansi
 */
public interface HkRuleService {

    public ObjectId saveOrUpdateRuleSet(HkRuleSetDocument document);

    public Map<ObjectId, HkRuleSetDocument> retrieveAllRuleSet(long companyId, Boolean isActive);

    public void deleteRuleSet(ObjectId ruleId);

    public HkRuleSetDocument retrieveRuleSetById(ObjectId ruleSetId);
    
    public List<HkRuleSetDocument> retrieveRuleSetByIds(List<ObjectId> ruleSetIds);

    public void executeRuleSet(ObjectId ruleSetId, Map<Long, String> entityIdMap, Map<Long, List<Map<String, Object>>> entityFieldMap, Map<String, Object> data);

    public boolean executeRule(HkRuleDocument ruleDocument,Map<String,Object> dataMap);
    
    public void saveAllRuleSet(List<HkRuleSetDocument> documents);
    
     public void saveAllCriteriaSetDocuments(List<HkCriteriaSetDocument> documents);
}
