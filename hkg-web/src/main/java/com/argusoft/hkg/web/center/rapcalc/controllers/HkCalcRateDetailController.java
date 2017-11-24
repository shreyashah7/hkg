/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.rapcalc.databeans.CalcRateDetailDatabean;
import com.argusoft.hkg.web.center.rapcalc.transformers.CalcRateDetailTransformer;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/rapcalc")
public class HkCalcRateDetailController extends BaseController<CalcRateDetailDatabean, String> {

    @Autowired
    private CalcRateDetailTransformer calcRateDetailTransformer;

    @Override
    public ResponseEntity<List<CalcRateDetailDatabean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CalcRateDetailDatabean> retrieveById(PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> update(CalcRateDetailDatabean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> create(CalcRateDetailDatabean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CalcRateDetailDatabean> deleteById(PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/calculatediamondprice", method = POST)
    public ResponseEntity<CalcRateDetailDatabean> calculateDiamondPrice(@RequestBody CalcRateDetailDatabean calcRateDetailDatabean) {
        CalcRateDetailDatabean rateDetail = calcRateDetailTransformer.calculateDiamondPrice(calcRateDetailDatabean);
        if (rateDetail != null) {
            return new ResponseEntity<>(rateDetail, ResponseCode.SUCCESS, "Calculated Successfully", null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Error occured while calculating discount", null, false);
        }

    }
}
