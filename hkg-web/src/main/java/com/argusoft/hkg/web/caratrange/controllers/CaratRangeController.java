package com.argusoft.hkg.web.caratrange.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.caratrange.databeans.CaratRangeDataBean;
import com.argusoft.hkg.web.caratrange.transformers.CaratRangeTransformer;
import freemarker.ext.beans.HashAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rajkumar
 */
@RestController
@RequestMapping("/caratrange")
public class CaratRangeController extends BaseController<CaratRangeDataBean, Long> {

    @Autowired
    private CaratRangeTransformer caratRangeTransformer;

    @Override
    public ResponseEntity<List<CaratRangeDataBean>> retrieveAll() {
        List<CaratRangeDataBean> caratRangeDataBeans = caratRangeTransformer.retrieveAllActiveCaratRanges();
        return new ResponseEntity<>(caratRangeDataBeans, null, null, null);
    }

    @Override
    public ResponseEntity<CaratRangeDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(CaratRangeDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(CaratRangeDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<CaratRangeDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "saveAll", method = RequestMethod.POST)
    public ResponseEntity saveAll(@RequestBody List<CaratRangeDataBean> caratRangeDataBeans) {
        caratRangeTransformer.saveAll(caratRangeDataBeans);
        return new ResponseEntity(null, ResponseCode.SUCCESS, "Carat ranges updated successfully", null);
    }

    @RequestMapping(value = "retrieveCaratWithNoRange",method = RequestMethod.GET)
    public Map<String,List<String>> retrieveCaratWithNoRange(){
        Map<String,List<String>> result=new HashMap<>();
        result.put("caratRangeWithNoCarat", caratRangeTransformer.retrieveCaratWithNoRange());
        return result;
    }
}
