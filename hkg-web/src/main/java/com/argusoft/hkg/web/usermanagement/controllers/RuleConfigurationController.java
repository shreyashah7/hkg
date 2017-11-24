/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.databeans.StaticServiceFieldPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.RuleConfigurationTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gautam
 */
@RestController
@RequestMapping("/ruleconfig")
public class RuleConfigurationController extends BaseController<RuleDatabean, Long> {

    @Autowired
    RuleConfigurationTransformerBean ruleConfigurationTransformerBean;
    @Autowired
    DesignationTransformerBean designationTransformer;

    @Override
    public ResponseEntity<List<RuleDatabean>> retrieveAll() {
        List<RuleDatabean> rules = ruleConfigurationTransformerBean.retrieveAllRules();
        return new ResponseEntity<>(rules, ResponseCode.SUCCESS, "", null, false);
    }

    @Override
    public ResponseEntity<RuleDatabean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody RuleDatabean ruleDatabean) {
        Long ruleNumber = ruleConfigurationTransformerBean.saveRuleConfiguration(ruleDatabean);
        if (ruleNumber != null) {
            return new ResponseEntity<>(new PrimaryKey<Long>(ruleNumber), ResponseCode.SUCCESS, "Rule configuration saved successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Some error occured, Try again later", null);
        }
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@RequestBody RuleDatabean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<RuleDatabean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> ruleTypesMap = new HashMap<>();
        ruleTypesMap.put(HkSystemConstantUtil.RuleConfiguration.RuleType.PRE, "Pre");
        ruleTypesMap.put(HkSystemConstantUtil.RuleConfiguration.RuleType.POST, "Post");
        ruleTypesMap.put(HkSystemConstantUtil.RuleConfiguration.RuleType.SCREEN, "Screen");
        //List<String> ruleTypes = Arrays.asList(HkSystemConstantUtil.RuleConfiguration.RuleType.PRE, HkSystemConstantUtil.RuleConfiguration.RuleType.POST, HkSystemConstantUtil.RuleConfiguration.RuleType.SCREEN);
        Map<String, List<RuleDatabean>> rules = ruleConfigurationTransformerBean.retrieveAllRulesMap();
        List<SelectItem> featureList = ruleConfigurationTransformerBean.retrieveDiamondFeatureList();
        Map<Long, List<StaticServiceFieldPermissionDataBean>> fieldFeatureMap = designationTransformer.retrieveFieldsByFeature();

        response.put("ruleTypes", ruleTypesMap);
        response.put("rules", rules);
        response.put("featureList", featureList);
        response.put("fieldFeatureMap", fieldFeatureMap);
        return new ResponseEntity<>(response, ResponseCode.SUCCESS, "", null, false);
    }

    @RequestMapping(value = "/retrieveruletypes", method = RequestMethod.GET)
    public ResponseEntity<List<String>> retrieveStaticStatusList() {
        List<String> response = Arrays.asList(HkSystemConstantUtil.RuleConfiguration.RuleType.PRE, HkSystemConstantUtil.RuleConfiguration.RuleType.POST, HkSystemConstantUtil.RuleConfiguration.RuleType.SCREEN);
        return new ResponseEntity<>(response, ResponseCode.SUCCESS, "", null, false);
    }

    @RequestMapping(value = "/retrieverulebyrulenumber", method = RequestMethod.POST)
    public ResponseEntity<RuleDatabean> retrieveRuleByRuleNumber(@RequestBody Long ruleNumber) {
        RuleDatabean rule = ruleConfigurationTransformerBean.retrieveRuleConfigurationByRuleNumber(ruleNumber);
        if (rule != null) {
            return new ResponseEntity<>(rule, ResponseCode.SUCCESS, "", null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Some error occured, Try again later", null);
        }
    }

    @RequestMapping(value = "/searchrules", method = RequestMethod.GET)
    public List<RuleDatabean> searchRulesByName(@RequestParam("q") String search) {
        return ruleConfigurationTransformerBean.searchRuleDocumentsByName(search);
    }

    @RequestMapping(value = "/removeRule", method = RequestMethod.POST)
    public void removeRule(@RequestBody Long ruleNumber) {
        ruleConfigurationTransformerBean.removeRule(ruleNumber);
    }

    @RequestMapping(value = "/retrieveAllRules", method = RequestMethod.GET)
    public Map<String, List<RuleDatabean>> retrieveRules() {
        Map<String, List<RuleDatabean>> rules = ruleConfigurationTransformerBean.retrieveAllRulesMap();
        return rules;
    }

}
