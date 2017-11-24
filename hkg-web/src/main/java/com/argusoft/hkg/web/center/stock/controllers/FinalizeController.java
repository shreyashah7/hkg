/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.FinalizeServiceDataBean;
import com.argusoft.hkg.web.center.stock.transformers.FinalizeTransformer;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author siddharth
 */
@RestController
@RequestMapping("/finalize")
public class FinalizeController {

    @Autowired
    FinalizeTransformer finalizeTransformer;

    @RequestMapping(value = "/retrievestockbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveStock(@RequestBody Map<String, List<String>> featureDbfieldNameMap) {
        return new ResponseEntity<>(finalizeTransformer.retrieveStock(featureDbfieldNameMap), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievevaluesofmaster", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<MasterDataBean>> retrieveValueOfMaster() {
        return new ResponseEntity<>(finalizeTransformer.retrieveValueOfMaster(), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/checkcaratrange", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Double>> checkCaratRangeAndCalculatePrice(@RequestBody Map<String, Float> map) {
        Double checkCaratRange = finalizeTransformer.checkCaratRange(map);
        Map<String, Double> map1 = new HashMap<>();
        map1.put("price", checkCaratRange);
        return new ResponseEntity<>(map1, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievecurrencycode", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCurrencyCode() {
        return new ResponseEntity<>(finalizeTransformer.retrieveCurrencyCode(), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<PrimaryKey<ObjectId>> savePlan(@RequestBody FinalizeServiceDataBean finalizeServiceDataBean) {
        String planId = finalizeTransformer.savePlan(finalizeServiceDataBean);
        if (StringUtils.hasText(planId)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Plan added successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan can not be added", null, true);
        }
    }

    @RequestMapping(value = "/retrievesavedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<FinalizeServiceDataBean>> retrieveSavedPlan(@RequestBody String workAllotmentId) {
        return new ResponseEntity<>(finalizeTransformer.retrievePlan(workAllotmentId, HkSystemConstantUtil.ShowPlanStatus.ENTERED), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> submitPlan(@RequestBody Map<String, String> map) {
        Boolean submitPlans = finalizeTransformer.submitPlans(map);
        if (submitPlans) {
            return new ResponseEntity<>(submitPlans, ResponseCode.SUCCESS, "Plan finalized successfully", null, true);
        }
        return new ResponseEntity<>(submitPlans, ResponseCode.FAILURE, "Plan can not be finalized", null, true);
    }

    @RequestMapping(value = "/retrievesubmittedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<FinalizeServiceDataBean>> retrieveAccessiblePlan(@RequestBody Map<String, String> map) {
        return new ResponseEntity<>(finalizeTransformer.retrieveAccessiblePlan(map), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/deleteplan", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> deletePlan(@RequestBody String planId) {
        Boolean deletePlan = finalizeTransformer.deletePlan(planId);
        if (deletePlan) {
            return new ResponseEntity<>(deletePlan, ResponseCode.SUCCESS, "Plan deleted successfully", null, true);
        }
        return new ResponseEntity<>(deletePlan, ResponseCode.FAILURE, "Plan can not be deleted", null, true);
    }

    @RequestMapping(value = "/retrievepricelist", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map> retrievePriceList() {
        Map<Long, Date> result = finalizeTransformer.retrievePriceList();
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievevaluefrompricelist", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map> retrieveValueFromPriceList(@RequestBody Map<String, Map<String, Object>> payload) {
        Map result = new HashMap();
        //System.out.println(!CollectionUtils.isEmpty(payload) && payload.size() > 1 && payload.get("pricelist") != null);
        if (!CollectionUtils.isEmpty(payload) && payload.size() > 1 && payload.get("pricelist") != null) {
            result = finalizeTransformer.retrieveValueFromPriceList(payload);
        }

        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/editsavedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Boolean> saveEditedPlan(@RequestBody FinalizeServiceDataBean finalizeServiceDataBean) {
        Boolean submitPlans = finalizeTransformer.editEnteredPlan(finalizeServiceDataBean);
        if (submitPlans) {
            return new ResponseEntity<>(submitPlans, ResponseCode.SUCCESS, "Plan edited successfully", null, true);
        }
        return new ResponseEntity<>(submitPlans, ResponseCode.FAILURE, "Plan can not be edited", null, true);
    }
}
