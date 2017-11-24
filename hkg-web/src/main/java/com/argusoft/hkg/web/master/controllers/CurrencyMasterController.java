/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.master.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.master.databeans.CurrencyMasterDataBean;
import com.argusoft.hkg.web.master.databeans.CurrencyRateMasterDataBean;
import com.argusoft.hkg.web.master.databeans.CurrencyMasterDataBean;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.master.transformers.CurrencyMasterTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @author jyoti
 */
@RestController
@RequestMapping("/currencymaster")
public class CurrencyMasterController extends BaseController<CurrencyMasterDataBean, Long> {

    @Autowired
    CurrencyMasterTransformerBean currencyMasterTransformerBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyMasterController.class);

    @RequestMapping(value = "retrievecurrency", method = RequestMethod.POST)
    public List<SelectItem> retrieveCurrencyCombo() {
        List<SelectItem> currencyList = currencyMasterTransformerBean.retrieveCurrencyFromDb();
        return currencyList;
    }

    @RequestMapping(value = "addreferencerate", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> addReferenceRate(@RequestBody CurrencyRateMasterDataBean currencyRateMasterDataBean) {
        currencyMasterTransformerBean.addReferenceRate(currencyRateMasterDataBean);
        return new ResponseEntity<Boolean>(null, ResponseCode.SUCCESS, "Reference Rate added successfully", null);
    }

    @Override
    public ResponseEntity<CurrencyMasterDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievecurrencies", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCurrencies() {
        List<SelectItem> selectItems = currencyMasterTransformerBean.retrieveCurrencyFromDb();
        return new ResponseEntity<List<SelectItem>>(selectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievecurrentcurrency", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<CurrencyRateMasterDataBean>> retrieveCurrentCurrencyList() {
        List<CurrencyRateMasterDataBean> rateMasterDataBeans = currencyMasterTransformerBean.retrieveCurrentRateList();
        return new ResponseEntity<List<CurrencyRateMasterDataBean>>(rateMasterDataBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievearchivedcurrency", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, List<CurrencyRateMasterDataBean>>> retrieveArchivedCurrencyMap() {
        Map<String, List<CurrencyRateMasterDataBean>> rateMasterMap = currencyMasterTransformerBean.retrieveArchivedCurrencyMap();
        return new ResponseEntity<>(rateMasterMap, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/updatereferencerate", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateReferenceRate(@RequestBody CurrencyRateMasterDataBean rateMasterDataBean) {
        currencyMasterTransformerBean.updateReferenceRate(rateMasterDataBean);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Reference rate updated successfully", null);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(CurrencyMasterDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(CurrencyMasterDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CurrencyMasterDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<List<CurrencyMasterDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveReferenceRateForCurrency", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Double>> retrieveReferenceRateForCurrency(@RequestBody String currency) {
        System.out.println("Currency ::: " + currency);
        Double rate = currencyMasterTransformerBean.retrieveReferenceRateForCurrency(currency);
        System.out.println("Rate ********************************* " + rate);
        Map<String, Double> res = new HashMap<>();
        if (rate != null) {
            res.put(currency, rate);
        }
        return new ResponseEntity<>(res, ResponseCode.SUCCESS, null, null);
    }

}
