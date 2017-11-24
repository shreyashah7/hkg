package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.PlanDataBean;
import com.argusoft.hkg.web.center.stock.transformers.PlanTransformer;
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
 * @author rajkumar
 */
@RestController
@RequestMapping("/plan")
public class PlanController extends BaseController<PlanDataBean, Long> {

    @Autowired
    private PlanTransformer planTransformer;

    @Override
    public ResponseEntity<List<PlanDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PlanDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(PlanDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(PlanDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PlanDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Map<Integer, String>> savePlans(@RequestBody List<PlanDataBean> planDataBeans) {
        Map<Integer, ObjectId> planToObjectId = planTransformer.savePlans(planDataBeans);
        Map<Integer, String> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(planToObjectId)) {
            if (planToObjectId.containsKey(-1)) {
                return new ResponseEntity<>(result, ResponseCode.FAILURE, "Packet not exist", null, true);
            }
            planToObjectId.entrySet().stream().forEach((entry) -> {
                result.put(entry.getKey(), entry.getValue().toString());
            });
            return new ResponseEntity<>(result, ResponseCode.SUCCESS, "Plans saved successfully", null, true);
        } else {
            return new ResponseEntity<>(result, ResponseCode.FAILURE, "Plan cannot be saved", null, true);
        }

    }

    @RequestMapping(value = "/retrievebypacket", method = RequestMethod.POST)
    public Map<String, Object> retrieveByPacket(@RequestBody String packetNumber) {
        return planTransformer.retrievePlansByPacket(packetNumber.trim());
    }

    @RequestMapping(value = "/retrievemodifiers", method = RequestMethod.POST)
    public ResponseEntity<List<String>> retrieveModifiers() {
        List<String> modifiers = planTransformer.retrieveModifiers();
        return new ResponseEntity<>(modifiers, null, null, null, false);
    }

    @RequestMapping(value = "/retrievefinalplans", method = RequestMethod.POST)
    public List<PlanDataBean> retrieveFinalPlans(@RequestBody String packetNumber) {
        return planTransformer.retrieveFinalPlans(packetNumber.trim());
    }

    @RequestMapping(value = "/finalizeplan", method = RequestMethod.POST)
    public void finalizePlan(@RequestBody String planId) {
        if (!StringUtils.isEmpty(planId)) {
            planTransformer.finalizePlan(new ObjectId(planId));
        }
    }

}
