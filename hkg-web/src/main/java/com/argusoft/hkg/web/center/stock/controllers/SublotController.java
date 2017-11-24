/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.InvoiceDataBean;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.SublotDatabean;
import com.argusoft.hkg.web.center.stock.transformers.ParcelTransformer;
import com.argusoft.hkg.web.center.stock.transformers.SubLotTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
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
 * @author shreya
 */
@RestController
@RequestMapping("/sublot")
public class SublotController extends BaseController<SublotDatabean, ObjectId> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SublotController.class);

    @Autowired
    private SubLotTransformer subLotTransformer;

    @Autowired
    ParcelTransformer parcelTransformer;

    @Autowired
    LoginDataBean loginDataBean;

    @Override
    public ResponseEntity<List<SublotDatabean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<SublotDatabean> retrieveById(PrimaryKey<ObjectId> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<ObjectId>> update(@RequestBody SublotDatabean invoiceDataBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<ObjectId>> create(@RequestBody SublotDatabean invoiceDataBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ResponseEntity<SublotDatabean> deleteById(PrimaryKey<ObjectId> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @RequestMapping(value = "/retrievesublots", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSublots(@RequestBody Map<String, List<String>> map) {
        System.out.println("map in controller :" + map);
        List<SelectItem> selectItems = subLotTransformer.retrieveSublots(map);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveallottedparcelandinvoice", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllottedParcelAndInvoice(@RequestBody Map<String, String> invoiceParcelMap) {
        Gson gson = new Gson();
        InvoiceDataBean invoiceDataBean = gson.fromJson(invoiceParcelMap.get("invoice"), InvoiceDataBean.class);
        ParcelDataBean parcel = gson.fromJson(invoiceParcelMap.get("parcel"), ParcelDataBean.class);
        System.out.println("parceldatabean:   ::: " + parcel);
        System.out.println("invoicedatabean::::: :" + invoiceDataBean);
        List<SelectItem> selectItems = subLotTransformer.retrieveAllottedParcels(parcel, invoiceDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievesublotsbyparcel", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSublotsByParcel(@RequestBody Map map) {
        String parcelId = (String) map.get("parcelId");
        Map<String, List<String>> propMap = (Map<String, List<String>>) map.get("map");
        System.out.println("parcelid:  :" + parcelId);
        Map<String, Object> ruleConfigMap = (Map<String, Object>) map.get("ruleConfigMap");
        Boolean excludeSubLotWithAssociatedLot = null;
        String includeSubLotWithAssociatedLot = null;
        if (map.get("excludeSubLotWithAssociatedLot") != null) {
            excludeSubLotWithAssociatedLot = (Boolean) map.get("excludeSubLotWithAssociatedLot");
        }
        if (map.get("includeSubLotWithAssociatedLot") != null) {
            includeSubLotWithAssociatedLot = (String) map.get("includeSubLotWithAssociatedLot");
        }
        List<SelectItem> selectItems = subLotTransformer.retrieveSubLotByParcelId(parcelId, propMap, ruleConfigMap, excludeSubLotWithAssociatedLot, includeSubLotWithAssociatedLot);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/createsublot", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> createSublot(@RequestBody SublotDatabean sublotDataBean) {
        System.out.println("PARCELID: " + sublotDataBean);
        SelectItem selectItem = subLotTransformer.saveSubLot(sublotDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Sublot Created Successfully", null, true);
    }

    @RequestMapping(value = "/updatesublot", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> updateSublot(@RequestBody SublotDatabean invoiceDataBean) {
        SelectItem selectItem = subLotTransformer.updateSublot(invoiceDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Sublot Updated Successfully", null, true);
    }

    @RequestMapping(value = "/deletesublot", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> deleteSublot(@RequestBody String sublotId) {
        Map<Boolean, String> responseAndMsgMap = subLotTransformer.deleteSublot(sublotId);
        if (!CollectionUtils.isEmpty(responseAndMsgMap)) {
            for (Map.Entry<Boolean, String> entry : responseAndMsgMap.entrySet()) {
                Boolean key = entry.getKey();
                String msg = entry.getValue();
                if (key) {
                    return new ResponseEntity<>(null, ResponseCode.SUCCESS, msg, null, true);
                } else {
                    return new ResponseEntity<>(null, ResponseCode.FAILURE, msg, null, true);
                }
            }
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Sublot Cannot be deleted", null, true);
    }
}
