/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.PacketDataBean;
import com.argusoft.hkg.web.center.stock.databeans.RoughMakeableDatabean;
import com.argusoft.hkg.web.center.stock.transformers.RoughMakeableTransformer;
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
@RequestMapping("/roughmakeable")
public class RoughMakeableController {

    @Autowired
    RoughMakeableTransformer roughMakeableTransformer;

    @RequestMapping(value = "/retrievepacketforroughmakeable", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrievePacketsForRoughmakeable(@RequestBody PacketDataBean packetDataBean) {
        List<SelectItem> selectItems = roughMakeableTransformer.retirevePacketsByInStockOf(packetDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/createroughmakeable", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<String> createRoughMakeable(@RequestBody RoughMakeableDatabean roughMakeableDatabean) {
        String roughMakeableId = roughMakeableTransformer.createRoughMakeable(roughMakeableDatabean);
        return new ResponseEntity<>(roughMakeableId, ResponseCode.SUCCESS, "RoughMakeable Created Successfully", null, true);
    }

    @RequestMapping(value = "/updateroughmakeable", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> updateRoughMakeable(@RequestBody RoughMakeableDatabean roughMakeableDatabean) {
        String roughMakeableId = roughMakeableTransformer.updateRoughMakeable(roughMakeableDatabean);
        return new ResponseEntity<>(roughMakeableId, ResponseCode.SUCCESS, "RoughMakeable Updated Successfully", null, true);
    }

    @RequestMapping(value = "/retrieveroughmakeablebypacketid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveRoughMakeableByPktId(@RequestBody RoughMakeableDatabean roughMakeableDatabean) {
        SelectItem selectItemRoughMakeable = roughMakeableTransformer.retrieveRoughMakeableByPktId(roughMakeableDatabean);
        return new ResponseEntity<>(selectItemRoughMakeable, ResponseCode.SUCCESS, "", null, false);
    }
}
