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
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleSetDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.RuleManagementTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mansi
 *
 */
@RestController
@RequestMapping("/rulemanagement")
public class RuleManagementController extends BaseController<RuleSetDataBean, Long> {

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    RuleManagementTransformerBean ruleManagementTransformerBean;
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleManagementController.class);

    @Override
    public ResponseEntity<List<RuleSetDataBean>> retrieveAll() {
        List<RuleSetDataBean> retrieveAllRules = ruleManagementTransformerBean.retrieveAllRules(loginDataBean.getCompanyId(), Boolean.TRUE);
        return new ResponseEntity<>(retrieveAllRules, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<RuleSetDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        return null;
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody RuleSetDataBean t) {
        String response = ruleManagementTransformerBean.updateRule(t, Boolean.FALSE);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Ruleset updated successfully.", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Ruleset could not be updated.", null);
        }
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody RuleSetDataBean t) {
        System.out.println("Create method");
        ObjectId response = ruleManagementTransformerBean.createRule(t);
        if (response != null) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Ruleset created successfully.", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Ruleset could not be created.", null);
        }
    }

    @Override
    public ResponseEntity<RuleSetDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        LOGGER.info("retrievePrerequisite called in rule management");
        Map<Long, String> retrieveEntitys = ruleManagementTransformerBean.retrieveEntitys(loginDataBean.getCompanyId());
        Map<String, List<SelectItem>> retrieveOperatorsByType = ruleManagementTransformerBean.retrieveOperatorsByType();
//        List<RuleSetDataBean> retrieveAllRules = ruleManagementTransformerBean.retrieveAllRules(loginDataBean.getCompanyId(), Boolean.TRUE);
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(retrieveEntitys)) {
            retrieveEntitys = new HashMap<>();
        }
        LOGGER.info("retrieveEntitys map"+retrieveEntitys);
        retrieveEntitys.put(-1l, "Login");
        LOGGER.info("after map"+retrieveEntitys);
        map.put("entity", retrieveEntitys);
        map.put("operator", retrieveOperatorsByType);
//        map.put("allrules", retrieveAllRules);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieve/field", method = RequestMethod.POST)
    public List<CustomFieldDataBean> retrieveFieldsByEntity(@RequestBody PrimaryKey<Long> primaryKey) {
        List<CustomFieldDataBean> retrieveFieldsByEntity = ruleManagementTransformerBean.retrieveFieldsByEntity(primaryKey.getPrimaryKey(), loginDataBean.getCompanyId());
        return retrieveFieldsByEntity;
    }

    @RequestMapping(value = "/retrieveDropListForSubEntity", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> createDropDownListForSubEntity(@RequestBody Long customFieldId) {
        List<SelectItem> dropDownListForSubEntity = ruleManagementTransformerBean.createDropDownListForSubEntity(customFieldId);
        return dropDownListForSubEntity;
    }

    @RequestMapping(value = "/retrieveLotStatus", method = RequestMethod.POST)
    public List<SelectItem> retrieveStaticStatusList(@RequestBody String dbFieldName) {
        List<SelectItem> lotStatus = ruleManagementTransformerBean.retrieveStaticStatusList(dbFieldName);
        return lotStatus;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<RuleDatabean> removeRuleById(@RequestBody PrimaryKey<ObjectId> primaryKey) {
        String response = ruleManagementTransformerBean.deleteRuleSet(primaryKey.getPrimaryKey());
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Rule deleted successfully.", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Rule could not be deleted.", null);
        }
    }

    @RequestMapping(value = "/retrieve/rule", method = RequestMethod.POST)
    public ResponseEntity<RuleSetDataBean> retrieveRuleById(@RequestBody PrimaryKey<ObjectId> primaryKey) {
        RuleSetDataBean retrieveRuleById = ruleManagementTransformerBean.retrieveRuleById(primaryKey.getPrimaryKey(), loginDataBean.getCompanyId());
        return new ResponseEntity<>(retrieveRuleById, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/master", method = RequestMethod.POST)
    public List<SelectItem> retrieveMasterByField(@RequestBody PrimaryKey<String> primaryKey) {
        if (primaryKey.getPrimaryKey().equalsIgnoreCase("-1")) {
            List<SelectItem> result = new ArrayList<>();
            for (Map.Entry<Long, String> entry : HkSystemConstantUtil.activityServiceSymbolMap.entrySet()) {
                SelectItem selectItem = new SelectItem(entry.getKey(), entry.getValue(), 1);
                result.add(selectItem);
            }
            return result;
        }
        List<SelectItem> retrieveMasterByField = ruleManagementTransformerBean.retrieveMasterByField(primaryKey.getPrimaryKey(), loginDataBean.getCompanyId());
        return retrieveMasterByField;
    }
}
