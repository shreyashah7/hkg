/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.transformers.ChangeStatusTransformer;
import com.argusoft.hkg.web.center.stock.transformers.EndOfDayTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author brijesh
 */
@RestController
@RequestMapping("/endoftheday")
public class EndOFDayController {

    @Autowired
    EndOfDayTransformer endOfDayTransformer;

    @Autowired
    ChangeStatusTransformer changeStatusTransformer;

    @RequestMapping(value = "/retrieveissuestocks", method = RequestMethod.POST)
    public List<SelectItem> retrieveIssueStocks(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {
//        System.out.println("----retrieveIssueStocks-----feature map-------" + payload);
        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        List<String> issueDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : payload.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (value.equals(HkSystemConstantUtil.Feature.LOT)) {
                lotDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                invoiceDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                parcelDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PACKET)) {
                packetDbFieldName.add(key);
            }else if (value.equals(HkSystemConstantUtil.Feature.ISSUE)) {
                issueDbFieldName.add(key);
            }
        }
        List<SelectItem> retrieveIssueStocks = endOfDayTransformer.retrieveIssueStocks(invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName,issueDbFieldName);
        return retrieveIssueStocks;
    }

    @RequestMapping(value = "/retrievereturnstocks", method = RequestMethod.POST)
    public List<SelectItem> retrieveReturnStocks(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {

//        System.out.println("-----retrieveReturnStocks----feature map-------" + payload);
        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        List<String> issueDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : payload.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (value.equals(HkSystemConstantUtil.Feature.LOT)) {
                lotDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                invoiceDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                parcelDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PACKET)) {
                packetDbFieldName.add(key);
            }else if (value.equals(HkSystemConstantUtil.Feature.ISSUE)) {
                issueDbFieldName.add(key);
            }
        }

        List<SelectItem> retrieveReturnStocks = endOfDayTransformer.retrieveReturnStocks(invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName,issueDbFieldName);
        return retrieveReturnStocks;
    }

    @RequestMapping(value = "/retrieverecievestocks", method = RequestMethod.POST)
    public List<SelectItem> retrieveRecieveStocks(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {

        //System.out.println("-----retrieveReturnStocks----feature map-------" + payload);
        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        List<String> issueDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : payload.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (value.equals(HkSystemConstantUtil.Feature.LOT)) {
                lotDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                invoiceDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                parcelDbFieldName.add(key);
            } else if (value.equals(HkSystemConstantUtil.Feature.PACKET)) {
                packetDbFieldName.add(key);
            }else if (value.equals(HkSystemConstantUtil.Feature.ISSUE)) {
                issueDbFieldName.add(key);
            }
        }

        List<SelectItem> retrieveRecieveStocks = endOfDayTransformer.retrieveRecieveStocks(invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName,issueDbFieldName);
        return retrieveRecieveStocks;
    }

    @RequestMapping(value = "/retrieveusersforcarrierboysid", method = RequestMethod.GET)
    public List<SelectItem> retrieveUsersForCarrierBoysId() throws GenericDatabaseException {

        List<SelectItem> selectItemListFromUserList = endOfDayTransformer.retrieveUsersForInstockOfIssue();

        return selectItemListFromUserList;
    }

    @RequestMapping(value = "/issuelotsorpackets", method = RequestMethod.POST)
    public Map<String, List<String>> issueLotsOrPackets(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {
        List<String> scannedUniqueIds = (List<String>) payload.get("uniqueScannedIds");
        String carrierBoy = (String) payload.get("carrierBoy");
        String department = (String) payload.get("department");
        //System.out.println("------scannedUniqueIds----------" + scannedUniqueIds);
        Map<String, List<String>> issueLotsOrPackets = endOfDayTransformer.issueLotsOrPackets(scannedUniqueIds, carrierBoy, department);
        //System.out.println("------issueLotsOrPackets----------" + issueLotsOrPackets);
        return issueLotsOrPackets;
    }

    @RequestMapping(value = "/recievelotsorpackets", method = RequestMethod.POST)
    public Map<String, List<String>> recieveLotsOrPackets(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {
        List<String> scannedUniqueIds = (List<String>) payload.get("uniqueScannedIds");
        //System.out.println("------scannedUniqueIds----------" + scannedUniqueIds);
        Map<String, List<String>> recieveLotsOrPackets = endOfDayTransformer.recieveLotsOrPackets(scannedUniqueIds);
        //System.out.println("------recieveLotsOrPackets----------" + recieveLotsOrPackets);
        return recieveLotsOrPackets;
    }

    @RequestMapping(value = "/returnlotsorpackets", method = RequestMethod.POST)
    public Map<String, List<String>> returnLotsOrPackets(@RequestBody Map<String, Object> payload) throws GenericDatabaseException {
        List<String> scannedUniqueIds = (List<String>) payload.get("uniqueScannedIds");
        //System.out.println("------scannedUniqueIds----------" + scannedUniqueIds);
        Map<String, List<String>> returnLotsOrPackets = endOfDayTransformer.returnLotsOrPackets(scannedUniqueIds);
        //System.out.println("------returnLotsOrPackets----------" + returnLotsOrPackets);
        return returnLotsOrPackets;
    }
    
    
    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {
        //System.out.println("inside ::::");
        List<SelectItem> hkSelectItems = endOfDayTransformer.retrieveUsers(user);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }
}
