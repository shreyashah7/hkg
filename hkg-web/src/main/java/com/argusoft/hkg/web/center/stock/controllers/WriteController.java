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
import com.argusoft.hkg.web.center.stock.databeans.WriteServiceDataBean;
import com.argusoft.hkg.web.center.stock.transformers.WriteTransformer;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/write")
public class WriteController {

    @Autowired
    WriteTransformer writeTransformer;

    @RequestMapping(value = "/retrievestockbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveStock(@RequestBody Map<String, List<String>> featureDbfieldNameMap) {
        return new ResponseEntity<>(writeTransformer.retrieveStock(featureDbfieldNameMap), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievevaluesofmaster", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<MasterDataBean>> retrieveValueOfMaster() {
        return new ResponseEntity<>(writeTransformer.retrieveValueOfMaster(), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/checkcaratrange", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Double>> checkCaratRangeAndCalculatePrice(@RequestBody Map<String, Float> map) {
        Double checkCaratRange = writeTransformer.checkCaratRange(map);
        Map<String, Double> map1 = new HashMap<>();
        map1.put("price", checkCaratRange);
        return new ResponseEntity<>(map1, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievecurrencycode", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCurrencyCode() {
        return new ResponseEntity<>(writeTransformer.retrieveCurrencyCode(), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<PrimaryKey<ObjectId>> savePlan(@RequestBody WriteServiceDataBean writeServiceDataBean) {
        String planId = writeTransformer.savePlan(writeServiceDataBean);
        if (StringUtils.hasText(planId)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Plan added successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan can not be added", null, true);
        }
    }

    @RequestMapping(value = "/retrievesavedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<WriteServiceDataBean>> retrieveSavedPlan(@RequestBody String workallotmentId) {
        return new ResponseEntity<>(writeTransformer.retrievePlan(workallotmentId,HkSystemConstantUtil.ShowPlanStatus.ENTERED), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> submitPlan(@RequestBody Map<String, List<String>> map) {
        Boolean submitPlans = writeTransformer.submitPlans(map);
        if (submitPlans) {
            return new ResponseEntity<>(submitPlans, ResponseCode.SUCCESS, "Plan submitted successfully", null, true);
        }
        return new ResponseEntity<>(submitPlans, ResponseCode.FAILURE, "Plan can not be submitted", null, true);
    }

    @RequestMapping(value = "/retrievesubmittedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<WriteServiceDataBean>> retrieveAccessiblePlan(@RequestBody Map<String, String> map) {
        return new ResponseEntity<>(writeTransformer.retrieveAccessiblePlan(map), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/deleteplan", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> deletePlan(@RequestBody String planId) {
        Boolean deletePlan = writeTransformer.deletePlan(planId);
        if (deletePlan) {
            return new ResponseEntity<>(deletePlan, ResponseCode.SUCCESS, "Plan deleted successfully", null, true);
        }
        return new ResponseEntity<>(deletePlan, ResponseCode.FAILURE, "Plan can not be deleted", null, true);
    }

    @RequestMapping(value = "/editsavedplan", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Boolean> saveEditedPlan(@RequestBody WriteServiceDataBean writeServiceDataBean) {
        Boolean submitPlans = writeTransformer.editEnteredPlan(writeServiceDataBean);
        if (submitPlans) {
            return new ResponseEntity<>(submitPlans, ResponseCode.SUCCESS, "Plan edited successfully", null, true);
        }
        return new ResponseEntity<>(submitPlans, ResponseCode.FAILURE, "Plan can not be edited", null, true);
    }
}
