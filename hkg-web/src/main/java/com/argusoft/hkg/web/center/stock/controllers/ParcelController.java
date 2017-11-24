/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.transformers.ParcelTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
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
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    ParcelTransformer parcelTransformer;

    @RequestMapping(value = "/createparcel", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> createParcel(@RequestBody ParcelDataBean parcelDataBean) {
        SelectItem selectItem = parcelTransformer.createParcel(parcelDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Parcel added successfully", null, true);
    }

    @RequestMapping(value = "/updateparcel", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> updateParcel(@RequestBody ParcelDataBean parcelDataBean) {
        SelectItem selectItem = parcelTransformer.updateParcel(parcelDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Parcel updated successfully", null, true);
    }

    @RequestMapping(value = "/deleteparcel", method = RequestMethod.POST)
    public ResponseEntity<Boolean> deleteParcel(@RequestBody String parcelId) {
        if (parcelId != null) {
            Boolean flag = parcelTransformer.deleteParcel(new ObjectId(parcelId));
            if (flag) {
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Parcel deleted successfully", null, true);
            } else {
                return new ResponseEntity<>(null, ResponseCode.FAILURE, "Can not delete Parcel because it is having lots", null, true);
            }
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Error while deleting parcel", null, true);
    }

    @RequestMapping(value = "/retrievesearchedparcel", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedParcel(@RequestBody ParcelDataBean parcelDataBean) {
        return new ResponseEntity<>(parcelTransformer.searchParcel(parcelDataBean), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveparcelbyinvoiceids", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveParcelsByInvoiceIds(@RequestBody ParcelDataBean parcelDataBean) {
        return new ResponseEntity<>(parcelTransformer.retrieveParcelsByInvoice(parcelDataBean), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveparcelbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveParcelsById(@RequestBody Map<String, List<String>> map) {
        return new ResponseEntity<>(parcelTransformer.retrieveParcelById(map, null), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveallparcels", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllParcels(@RequestBody ParcelDataBean parcelDataBean) {
        return new ResponseEntity<>(parcelTransformer.retrieveAllParcels(parcelDataBean), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/getnextparcelsequence", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getNextParcelSequence() {
        Map<String, Object> map = new HashMap<>();
        Integer nextParcelId = parcelTransformer.getNextParcelSequence();
        if (nextParcelId != null) {
            map.put("parcelId$AG$String", nextParcelId);
        } else {
            map.put("parcelId$AG$String", null);
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/isparcelidexists", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> isParcelIdExists(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> map = new HashMap<>();
        if (requestMap != null) {
            String parcelObjectId = null;
            Integer parcelId = new Integer(requestMap.get("parcelId").toString());
            if (requestMap.get("parcelObjectId") != null) {
                parcelObjectId = requestMap.get("parcelObjectId").toString();
            }
            Boolean parcelIdExists = parcelTransformer.isParcelIdExists(parcelId, parcelObjectId);
            if (parcelIdExists != null) {
                map.put("parcelExist", parcelIdExists);
            } else {
                map.put("parcelExist", Boolean.FALSE);
            }
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }
    
    @RequestMapping(value = "/retrieveassociatedroughparcels", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAssociatedRoughParcels(@RequestBody ParcelDataBean parcelDataBean) {
        return new ResponseEntity<>(parcelTransformer.retrieveAssociatedRoughParcels(parcelDataBean), ResponseCode.SUCCESS, "", null);
    }
    
    @RequestMapping(value = "/delinkroughparcelwithpurchase", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Boolean> deLinkRoughParcelWithPurchase(@RequestBody String parcelId){
        return new ResponseEntity<>(parcelTransformer.deLinkRoughParcelWithPurchase(parcelId), ResponseCode.SUCCESS, "", null);
    }
    
    @RequestMapping(value = "/countParcels", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> getCountofInvoiceDocumentsFromCriteria(@RequestBody String invoiceId) {
        Map<String, Long> map = new HashMap<>();
        long totalItems = parcelTransformer.getCountofParcelDocumentsFromCriteria(invoiceId);
        map.put("totalItems", totalItems);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }
}
