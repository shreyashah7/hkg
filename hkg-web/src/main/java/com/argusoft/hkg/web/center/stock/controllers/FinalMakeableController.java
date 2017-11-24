/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.databeans.FinalMakeableDatabean;
import com.argusoft.hkg.web.center.stock.transformers.FinalMakeableTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/finalmakeable")
public class FinalMakeableController {

    @Autowired
    FinalMakeableTransformer finalMakeableTransformer;

    @RequestMapping(value = "/retrievepacketforfinalmakeable", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievePacketsForFinalmakeable(@RequestBody PacketDataBean packetDataBean) {
        List<SelectItem> selectItems = finalMakeableTransformer.retirevePacketsByInStockOf(packetDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/createfinalmakeable", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<String> createFinalMakeable(@RequestBody FinalMakeableDatabean finalMakeableDatabean) {
        String finalMakeableId = finalMakeableTransformer.createFinalMakeable(finalMakeableDatabean);
        return new ResponseEntity<>(finalMakeableId, ResponseCode.SUCCESS, "FinalMakeable Created Successfully", null, true);
    }

    @RequestMapping(value = "/updatefinalmakeable", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> updateFinalMakeable(@RequestBody FinalMakeableDatabean finalMakeableDatabean) {
        String finalMakeableId = finalMakeableTransformer.updateFinalMakeable(finalMakeableDatabean);
        return new ResponseEntity<>(finalMakeableId, ResponseCode.SUCCESS, "FinalMakeable Updated Successfully", null, true);
    }

    @RequestMapping(value = "/retrievefinalmakeablebypacketid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveFinalMakeableByPktId(@RequestBody FinalMakeableDatabean finalMakeableDatabean) {
        SelectItem selectItemFinalMakeable = finalMakeableTransformer.retrieveFinalMakeableByPktId(finalMakeableDatabean);
        return new ResponseEntity<>(selectItemFinalMakeable, ResponseCode.SUCCESS, "", null, false);
    }
}
