package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.SellDataBean;
import com.argusoft.hkg.web.center.stock.transformers.RoughSaleTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Shreya
 */
@RestController
@RequestMapping("/roughsale")
public class RoughSaleController {

    @Autowired
    RoughSaleTransformer sellParcelTransformer;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RoughSaleController.class);

    @RequestMapping(value = "/sell", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Boolean> createOrUpdateSellDocumentForParcel(@RequestBody SellDataBean sellDataBean) throws GenericDatabaseException {
        Boolean result = sellParcelTransformer.createOrUpdateSellDocumentForParcel(sellDataBean);
        if (result) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Parcel Sold Successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Cannot Sold Parcel", null, true);
        }
    }

    @RequestMapping(value = "/retrieve", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSellDocuments(@RequestBody SellDataBean sellDataBean) {
        System.out.println("sellDataBean in controller :" + sellDataBean);
        List<SelectItem> selectItems = sellParcelTransformer.retrieveAllSellDocuments(sellDataBean);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveselldocumentbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrieveSellDocumentForParcelById(@RequestBody SellDataBean sellDataBean) {
        System.out.println("sellDataBean in retrieveSellDocumentForParcelById method  :" + sellDataBean);
        SelectItem selectItem = sellParcelTransformer.retrieveSellDocumentById(sellDataBean);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrievesearcheddata", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedParcel(@RequestBody ParcelDataBean parcelDataBean) {
        LOGGER.info("parcelDataBean" + parcelDataBean);
        return new ResponseEntity<>(sellParcelTransformer.searchParcel(parcelDataBean), ResponseCode.SUCCESS, "", null);
    }
}
