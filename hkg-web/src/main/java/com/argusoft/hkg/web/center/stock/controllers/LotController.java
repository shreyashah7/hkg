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
import com.argusoft.hkg.web.center.stock.databeans.LotDataBean;
import com.argusoft.hkg.web.center.stock.transformers.LotTransformer;
import com.argusoft.hkg.web.center.stock.transformers.SubLotTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shreya
 */
@RestController
@RequestMapping("/lot")
public class LotController extends BaseController<LotDataBean, Long> {

    @Autowired
    LotTransformer lotTransformer;

    @Autowired
    SubLotTransformer subLotTransformer;

    private static final Logger LOGGER = LoggerFactory.getLogger(LotController.class);
    @Override
    public ResponseEntity<List<LotDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LotDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@RequestBody LotDataBean lotDataBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<LotDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody LotDataBean lotDataBean) {
        String lotId = lotTransformer.updateLot(lotDataBean);
        if (StringUtils.hasText(lotId)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Lot updated successfully, [lot id:" + lotId + "]", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Lot can not be updated", null, true);
        }
    }

    @RequestMapping(value = "/createlot", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> createLot(@RequestBody LotDataBean lotDataBean) {
        Map<String, Object> lotDetails = lotTransformer.createLot(lotDataBean);
        if (!CollectionUtils.isEmpty(lotDetails)) {
            return new ResponseEntity<>(lotDetails, ResponseCode.SUCCESS, "Lot created successfully, [lot id:" + lotDetails.get(lotDetails.get("lotId")) + "]", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not create lot, please try again", null, false);
        }

    }

    @RequestMapping(value = "/retrievesearchedlot", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedLot(@RequestBody LotDataBean lotDataBean) {
        return new ResponseEntity<>(lotTransformer.searchLot(lotDataBean), ResponseCode.SUCCESS, "", null, true);
    }

//    @RequestMapping(value = "/retrievelotfromworkallocation", method = RequestMethod.POST, produces = {"application/json"})
//    public ResponseEntity<List<SelectItem>> retrieveSearchedLotFromWorkAllocation(@RequestBody LotDataBean lotDataBean) {
//        return new ResponseEntity<>(lotTransformer.retrieveLots(), ResponseCode.SUCCESS, "", null, true);
//    }

    @RequestMapping(value = "/retrievelotbyparcelid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveLotByParcelId(@RequestBody Map<String, Object> map) {
        return new ResponseEntity<>(lotTransformer.retrieveLotByParcelId(map), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievesublotbyparcelid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSubLotByParcelId(@RequestBody Map<String, List<String>> map) {
        System.out.println("map in retrieveSubLotByParcelId" + map);
        String parcelId = null;
        if (!CollectionUtils.isEmpty(map)) {
            List<String> ids = map.get("parcelIds");
            if (!CollectionUtils.isEmpty(ids)) {
                parcelId = ids.get(0);
            }
        }
        return new ResponseEntity<>(subLotTransformer.retrieveSubLotByParcelId(parcelId, map, null, null, null), ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievelotbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveLotById(@RequestBody Map<String, Object> map) {
        SelectItem selectItem = lotTransformer.retrieveLotById(map);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/mergelot", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Long> mergeLot(@RequestBody LotDataBean lotDataBean) {
        String lotId = lotTransformer.mergeLot(lotDataBean);
        if (lotId != null) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Lot merged successfully, [lot id:" + lotId + "]", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Lot can not be merged", null, true);
        }
    }

    @RequestMapping(value = "/splitlot", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Long> splitLot(@RequestBody LotDataBean lotDataBean) {
        List<String> splitLot = lotTransformer.splitLot(lotDataBean);
        if (splitLot != null) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Lot merged successfully, [lot id:" + splitLot.toString() + "]", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Lot can not be merged", null, true);
        }
    }

    @RequestMapping(value = "/retrievelot", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveLot(@RequestBody Map<String, List<String>> featureDbfieldNameMap) {
        return new ResponseEntity<>(lotTransformer.retrieveLot(featureDbfieldNameMap), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/deletelot", method = RequestMethod.POST)
    public ResponseEntity<Boolean> deleteLot(@RequestBody String lotId) {
        if (lotId != null) {
            return lotTransformer.deleteLot(new ObjectId(lotId));
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Error while deleting lot", null, true);
    }

    @RequestMapping(value = "/retrievefranchisedetails", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveFranchiseDetails() {
        return new ResponseEntity<>(lotTransformer.retrieveFranchiseDetails(), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/doesfieldsequenceexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> isLotSequenceNumberExist(@RequestBody Map<String, String> franchiseCodeSeqNumberMap) {
        boolean fieldSequenceExist = lotTransformer.isLotSequenceNumberExist(franchiseCodeSeqNumberMap);
        return new ResponseEntity<>(fieldSequenceExist, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/getnextlotsequence", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> getNextInvoiceSequence(@RequestBody Long franchiseId) {
        LOGGER.debug("getnextlotsequence franchise id : " + franchiseId);
        return new ResponseEntity<>(lotTransformer.getNextLotSequence(franchiseId), ResponseCode.SUCCESS, "", null, true);
    }
}
