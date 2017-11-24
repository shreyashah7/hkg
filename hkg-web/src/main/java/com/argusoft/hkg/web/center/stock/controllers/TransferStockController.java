/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.center.stock.databeans.StockDataBean;
import com.argusoft.hkg.web.center.stock.transformers.TransferStockTransformer;
import java.util.ArrayList;
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
 * @author brijesh
 */
@RestController
@RequestMapping("/transferstock")
public class TransferStockController {

    @Autowired
    TransferStockTransformer transferStockTransformer;

    @RequestMapping(value = "/retrievesearchedstock", method = RequestMethod.POST, produces = {"application/json"})
    public StockDataBean retrieveSearchedStock(@RequestBody Map<String, Object> featureMap) throws GenericDatabaseException {
        //System.out.println("----retrieveIssueStocks-----feature map-------" + featureMap);
        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : featureMap.entrySet()) {
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
            }
        }

        return transferStockTransformer.searchedStocks(invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName);
    }

    @RequestMapping(value = "/retrievestockbyworkallotmentid", method = RequestMethod.POST, consumes = {"application/json"})
    public StockDataBean retrieveStockByWorkAllotmentId(@RequestBody Map<String, Object> requestMap) throws GenericDatabaseException {
        if (!CollectionUtils.isEmpty(requestMap)) {
            String workAllotmentId = null;
            Boolean isPacket = null;
            if (requestMap.get("workAllotmentId") != null) {
                workAllotmentId = requestMap.get("workAllotmentId").toString();
            }
            if (requestMap.get("isPacket") != null) {
                isPacket = Boolean.parseBoolean(requestMap.get("isPacket").toString());
            }
            return transferStockTransformer.retrieveStockByWorkAllotmentId(workAllotmentId,isPacket);
        }
        return null;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, consumes = {"application/json"})
    public Object transferStock(@RequestBody StockDataBean stockDataBean) throws GenericDatabaseException {
        return transferStockTransformer.transferStock(stockDataBean);
    }
}
