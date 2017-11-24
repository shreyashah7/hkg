/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.hkg.web.center.stock.controllers.*;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.RuleDetailsDataBean;
import com.argusoft.hkg.web.center.stock.databeans.SellDataBean;
import com.argusoft.hkg.web.center.stock.transformers.RuleExecutionTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gautam
 */
@RestController
@RequestMapping("/executerule")
public class RuleExecutionController {

    @Autowired
    RuleExecutionTransformer ruleExecutionTransformer;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RuleExecutionController.class);

    @RequestMapping(value = "/prerule", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<RuleDetailsDataBean> executePreRules(@RequestBody RuleDetailsDataBean ruleDetailsDataBean) {
        System.out.println("ruleDetailsDataBean in controller pre:" + ruleDetailsDataBean);
        RuleDetailsDataBean preRuleResult = ruleExecutionTransformer.prepareDataAndExecutePreRules(ruleDetailsDataBean.getEntityId(), ruleDetailsDataBean.getFeatureName(), ruleDetailsDataBean.getEntityType());
        return new ResponseEntity<>(preRuleResult, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/postrule", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<RuleDetailsDataBean> executePostRules(@RequestBody RuleDetailsDataBean ruleDetailsDataBean, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        System.out.println("ruleDetailsDataBean in controller post master side:" + ruleDetailsDataBean);
        RuleDetailsDataBean postRuleResult = ruleExecutionTransformer.prepareDataAndExecutePostRules(ruleDetailsDataBean.getCurrentFieldValueMap(), ruleDetailsDataBean.getDbType(), ruleDetailsDataBean.getEntityId(), ruleDetailsDataBean.getFeatureName(), ruleDetailsDataBean.getEntityType(), ruleDetailsDataBean.getOtherEntitysIdMap(), ip);
        return new ResponseEntity<>(postRuleResult, ResponseCode.SUCCESS, "", null, true);
    }
}
