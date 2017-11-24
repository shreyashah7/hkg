/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.InvoiceDataBean;
import com.argusoft.hkg.web.center.stock.transformers.InvoiceTransformer;
import com.argusoft.hkg.web.center.stock.transformers.ParcelTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceTransformer invoiceTransformer;

    @Autowired
    ParcelTransformer parcelTransformer;

    @Autowired
    LoginDataBean loginDataBean;

    @RequestMapping(value = "/createinvoice", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> createInvoice(@RequestBody InvoiceDataBean invoiceDataBean) {
        SelectItem selectItem = invoiceTransformer.saveInvoice(invoiceDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Invoice Created Successfully", null, true);
    }

    @RequestMapping(value = "/updateinvoice", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<SelectItem> updateInvoice(@RequestBody InvoiceDataBean invoiceDataBean) {
        SelectItem selectItem = invoiceTransformer.updateInvoice(invoiceDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "Invoice Updated Successfully", null, true);
    }

    @RequestMapping(value = "/deleteinvoice", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> deleteInvoice(@RequestBody String invoiceId) {
        Map<Boolean, String> responseAndMsgMap = invoiceTransformer.deleteInvoice(invoiceId);
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
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Invoice Cannot be deleted", null, true);
    }

    @RequestMapping(value = "/retrievesearchedinvoices", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedInvoices(@RequestBody InvoiceDataBean invoiceDataBean) throws ParseException {
        List<SelectItem> searchInvoice = invoiceTransformer.searchInvoice(invoiceDataBean);
        return new ResponseEntity<>(searchInvoice, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveinvoices", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveInvoices(@RequestBody InvoiceDataBean invoiceDataBean) {
        System.out.println("invoiceDataBean in controller :" + invoiceDataBean);
        List<SelectItem> selectItems = invoiceTransformer.retrieveInvoices(invoiceDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/getnextinvoicesequence", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getNextInvoiceSequence() {
        Map<String, Object> map = new HashMap<>();
        Integer nextInvoiceId = invoiceTransformer.getNextInvoiceId();
        if (nextInvoiceId != null) {
            map.put("invoiceId$AG$String", nextInvoiceId);
        } else {
            map.put("invoiceId$AG$String", null);
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/isinvoiceidexists", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> isInvoiceIdExists(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> map = new HashMap<>();
        if (requestMap != null) {
            String invoiceObjectId = null;
            Integer invoiceId = new Integer(requestMap.get("invoiceId").toString());
            if (requestMap.get("invoiceObjectId") != null) {
                invoiceObjectId = requestMap.get("invoiceObjectId").toString();
            }
            Boolean invoiceIdExists = invoiceTransformer.isInvoiceIdExists(invoiceId, invoiceObjectId);
            if (invoiceIdExists != null) {
                map.put("invoiceExist", invoiceIdExists);
            } else {
                map.put("invoiceExist", Boolean.FALSE);
            }
        }
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/linkroughpurchase", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> linkRoughParcelWithPurchase(@RequestBody Map<String, List<String>> map) {
        System.out.println("map in controller :" + map);
        Boolean result = Boolean.FALSE;
        if (!CollectionUtils.isEmpty(map) && map.get("parcelIds") != null && map.get("purchaseIds") != null) {
            List<String> parcelIds = map.get("parcelIds");
            List<String> purchaseIds = map.get("purchaseIds");
            result = invoiceTransformer.linkRoughParcelWithPurchase(parcelIds, purchaseIds);
        }
        if (result) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Linked Rough Purchase Successfully", null, true);
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Cannot Link Rough Purchase. Please try again later", null, true);
    }

    @RequestMapping(value = "/countInvoices", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Long>> getCountofInvoiceDocumentsFromCriteria() {
        Map<String, Long> map = new HashMap<>();
        long totalItems = invoiceTransformer.getCountofInvoiceDocumentsFromCriteria();
        map.put("totalItems", totalItems);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/countSearchInvoices", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Long>> getCountofInvoiceDocumentsFromSearchCriteria(InvoiceDataBean invoiceDataBean) {
        Map<String, Long> map = new HashMap<>();
        long totalItems = invoiceTransformer.getCountofInvoiceDocumentsFromSearchCriteria(invoiceDataBean);
        map.put("totalItems", totalItems);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

}
