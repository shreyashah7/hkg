/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.InvoiceDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RoughPurchaseDataBean;
import com.argusoft.hkg.web.center.stock.transformers.RoughPurchaseTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shreya
 */
@RestController
@RequestMapping("/roughpurchase")
public class RoughPurchaseController {

    @Autowired
    RoughPurchaseTransformer roughPurchaseTransformer;

    @RequestMapping(value = "/createroughpurchase", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> createRoughPurchase(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) {
        SelectItem selectItem = roughPurchaseTransformer.saveRoughPurchase(roughPurchaseDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Rough Purchase Created Successfully", null, true);
    }

    @RequestMapping(value = "/updateroughpurchase", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> updateRoughPurchase(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) {
        SelectItem selectItem = roughPurchaseTransformer.updateRoughPurchase(roughPurchaseDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Rough Purchase Updated Successfully", null, true);
    }

    @RequestMapping(value = "/deleteroughpurchase", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> deleteRoughPurchase(@RequestBody String roughPurchaseId) {
        Boolean response = roughPurchaseTransformer.deleteRoughPurchase(roughPurchaseId);
        if (response) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Rough Purchase deleted Successfully", null, true);
        }else{
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Rough Purchase cannot be deleted", null, true);
        }
        
    }

    @RequestMapping(value = "/retrievesearchedroughpurchases", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedRoughPurchases(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) throws ParseException {
        List<SelectItem> searchRoughPurchase = roughPurchaseTransformer.searchRoughPurchase(roughPurchaseDataBean);
        return new ResponseEntity<>(searchRoughPurchase, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveroughpurchases", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveRoughPurchases(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) {
        System.out.println("roughPurchaseDataBean in controller :" + roughPurchaseDataBean);
        List<SelectItem> selectItems = roughPurchaseTransformer.retrieveRoughPurchases(roughPurchaseDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/getnextroughpurchasesequence", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getNextRoughPurchaseSequence() {
        Map<String, Object> map = new HashMap<>();
        Integer nextRoughPurchaseId = roughPurchaseTransformer.getNextRoughPurchaseSequence();
        if (nextRoughPurchaseId != null) {
            map.put("roughPurchaseID$AG$String", nextRoughPurchaseId);
        } else {
            map.put("roughPurchaseID$AG$String", null);
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/isroughpurchaseidexists", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> isRoughPurchaseIdExists(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> map = new HashMap<>();
        if (requestMap != null) {
            String roughPurchaseObjectId = null;
            Integer roughPurchaseId = new Integer(requestMap.get("roughPurchaseId").toString());
            if (requestMap.get("roughPurchaseObjectId") != null) {
                roughPurchaseObjectId = requestMap.get("roughPurchaseObjectId").toString();
            }
            Boolean roughPurchaseIdExists = roughPurchaseTransformer.isRoughPurchaseIdExists(roughPurchaseId, roughPurchaseObjectId);
            if (roughPurchaseIdExists != null) {
                map.put("roughPurchaseExist", roughPurchaseIdExists);
            } else {
                map.put("roughPurchaseExist", Boolean.FALSE);
            }
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }
    
    @RequestMapping(value = "/retrieveassociatedroughpurchases", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAssociatedRoughPurchase(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) {
        System.out.println("roughPurchaseDataBean in controller :" + roughPurchaseDataBean);
        List<SelectItem> selectItems = roughPurchaseTransformer.retrieveAssociatedRoughPurchase(roughPurchaseDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }
    
    @RequestMapping(value = "/countPurchases", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Long>> getCountofPurchaseDocumentsFromCriteria() {
        Map<String, Long> map = new HashMap<>();
        long totalItems = roughPurchaseTransformer.getCountofPurchaseDocumentsFromCriteria();
        map.put("totalItems", totalItems);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/countSearchPurchases", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> getCountofPurchaseDocumentsFromSearchCriteria(@RequestBody RoughPurchaseDataBean roughPurchaseDataBean) {
        Map<String, Long> map = new HashMap<>();
        long totalItems = roughPurchaseTransformer.getCountofPurchaseDocumentsFromSearchCriteria(roughPurchaseDataBean);
        map.put("totalItems", totalItems);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }
}
