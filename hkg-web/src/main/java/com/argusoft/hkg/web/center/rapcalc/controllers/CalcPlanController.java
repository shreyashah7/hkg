/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.rapcalc.databeans.CalcPlanDatabean;
import com.argusoft.hkg.web.center.rapcalc.transformers.CalcPlanTransformer;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/calcplan")
public class CalcPlanController extends BaseController<CalcPlanDatabean, String> {

    @Autowired
    CalcPlanTransformer calcPlanTransformer;

    @Override
    public ResponseEntity<List<CalcPlanDatabean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @RequestMapping(value = "/retrievebylot", method = GET)
//    public ResponseEntity<List<CalcPlanDatabean>> retrieveByLotId(@RequestParam("lotId") String lotId) {
//        List<CalcPlanDatabean> retrieveByPacketId = calcPlanTransformer.retrieveByLotId(new ObjectId(lotId));
//        if (retrieveByPacketId != null) {
//            return new ResponseEntity<>(retrieveByPacketId, ResponseCode.SUCCESS, "Plan added successfully", null, true);
//        } else {
//            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan can not be added", null, true);
//    }
//    }
//
//    @RequestMapping(value = "/retrievebypacket", method = GET)
//    public ResponseEntity<List<CalcPlanDatabean>> retrieveByPacketId(@RequestParam("packetId") String packetId) {
//        List<CalcPlanDatabean> retrieveByPacketId = calcPlanTransformer.retrieveByPacketId(new ObjectId(packetId));
//        if (retrieveByPacketId != null) {
//            return new ResponseEntity<>(retrieveByPacketId, ResponseCode.SUCCESS, "Plan added successfully", null, true);
//        } else {
//            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan can not be added", null, true);
//        }
//    }
//
//    @RequestMapping(value = "/deleteselected", method = POST)
//    @ResponseStatus(OK)
//    public void deleteSelected(@RequestBody List<String> idsList) {
//        calcPlanTransformer.deleteAll(idsList);
//    }
//
//    @Override
//    public ResponseEntity<CalcPlanDatabean> retrieveById(@RequestBody PrimaryKey<String> primaryKey) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public ResponseEntity<PrimaryKey<String>> update(@RequestBody CalcPlanDatabean t) {
//        String planId = calcPlanTransformer.update(t);
//        if (!StringUtils.isEmpty(planId)) {
//            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Plan updated successfully", null, true);
//        } else {
//            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan could not be updated", null, true);
//        }
//    }
//
//    @Override
//    public ResponseEntity<PrimaryKey<String>> create(@RequestBody CalcPlanDatabean t) {
//        String planId = calcPlanTransformer.save(t);
//        if (!StringUtils.isEmpty(planId)) {
//            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Plan added successfully", null, true);
//        } else {
//            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Plan can not be added", null, true);
//        }
//    }
//
    @Override
    public ResponseEntity<CalcPlanDatabean> deleteById(@RequestBody PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CalcPlanDatabean> retrieveById(PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> update(CalcPlanDatabean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> create(CalcPlanDatabean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
