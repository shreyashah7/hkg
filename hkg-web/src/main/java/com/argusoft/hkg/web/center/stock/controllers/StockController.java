/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.EditValueDataBean;
import com.argusoft.hkg.web.center.stock.databeans.StockDataBean;
import com.argusoft.hkg.web.center.stock.transformers.AllotmentTransformer;
import com.argusoft.hkg.web.center.stock.transformers.PacketTransformer;
import com.argusoft.hkg.web.center.stock.transformers.StockTransformer;
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
 * @author rajkumar
 */
@RestController
@RequestMapping("/stock")
public class StockController extends BaseController<StockDataBean, Long> {

    @Autowired
    private StockTransformer stockTransformer;

    @Autowired
    AllotmentTransformer allotTransformer;

    @Autowired
    PacketTransformer packetTransformer;

    @Override
    public ResponseEntity<List<StockDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<StockDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(StockDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(StockDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<StockDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveSearchedLotsAndPackets", method = RequestMethod.POST)
    public StockDataBean retrieveSearchedLotsAndPackets() throws GenericDatabaseException {
        StockDataBean result = stockTransformer.retrieveSearchedLotsAndPackets(Boolean.TRUE);
        return result;
    }

    @RequestMapping(value = "/mergeStock", method = RequestMethod.POST)
    public ResponseEntity mergeStock(@RequestBody StockDataBean stockDataBean) {
        Object newId = stockTransformer.mergeStock(stockDataBean);
        if (newId != null) {
            return new ResponseEntity(null, ResponseCode.SUCCESS, "Stock merged successfully", null, true);
        } else {
            return new ResponseEntity(null, ResponseCode.FAILURE, "Failed to merge stock", null, true);
        }
    }

    @RequestMapping(value = "/splitStock", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> splitStock(@RequestBody StockDataBean stockDataBean) {
        List<String> splitStock = stockTransformer.splitStock(stockDataBean);
        if (!CollectionUtils.isEmpty(splitStock)) {
            Map<String, String> map = new HashMap<>();
            String stockid = null;
            stockid = splitStock.toString();
            map.put("data", stockid);
            return new ResponseEntity(map, ResponseCode.SUCCESS, "Stock splitted successfully " + splitStock.toString(), null, true);
        } else {
            return new ResponseEntity(null, ResponseCode.FAILURE, "Failed to split stock", null, false);
        }
    }

    @RequestMapping(value = "/retrieveLotsAndPacketsEditValue", method = RequestMethod.POST)
    public EditValueDataBean retrieveLotsAndPacketsEditValue(@RequestBody Map<String, List<String>> featureDbfieldNameMap) {
        //System.out.println("featureDbfieldNameMap::::" + featureDbfieldNameMap);
        EditValueDataBean result = stockTransformer.retrieveLotsAndPacketsEditValue(featureDbfieldNameMap);
        //System.out.println("result:::" + result);
        return result;
    }

    @RequestMapping(value = "/retrievePlansByLotOrPacket", method = RequestMethod.POST)
    public List<EditValueDataBean> retrievePlansByLotOrPacket(@RequestBody Map<String, List<String>> payload) {
        //System.out.println("payload::::" + payload);
        return stockTransformer.retrievePlansByLotOrPacket(payload);
    }

    @RequestMapping(value = "/editValues", method = RequestMethod.POST)
    public ResponseEntity editValues(@RequestBody List<Map<String, Object>> payload) {
        //System.out.println("payload::::" + payload);
        Boolean result = stockTransformer.editValues(payload);
        if (result) {
            return new ResponseEntity(null, ResponseCode.SUCCESS, "Value of plans edited successfully", null, true);
        } else {
            return new ResponseEntity(null, ResponseCode.FAILURE, "Value cannot be edited due to some reason", null, true);
        }
    }

    @RequestMapping(value = "/retrievesplitpacket", method = RequestMethod.POST)
    public ResponseEntity retrieveSplitPacket(@RequestBody Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            return new ResponseEntity(packetTransformer.retrieveSplitPacket(map), ResponseCode.SUCCESS, null, null, true);
        } else {
            return new ResponseEntity(null, ResponseCode.SUCCESS, null, null, false);
        }
    }
}
