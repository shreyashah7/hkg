package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.MergeParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.ParcelDataBean;
import com.argusoft.hkg.web.center.stock.databeans.StockDataBean;
import com.argusoft.hkg.web.center.stock.transformers.MergeParcelTransformer;
import com.argusoft.hkg.web.util.SelectItem;
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
@RequestMapping("/mergeparcel")
public class MergeParcelController extends BaseController<StockDataBean, Long> {

    @Autowired
    private MergeParcelTransformer mergeParcelTransformer;

//    @RequestMapping(value = "/retrievesearchedparcels", method = RequestMethod.POST, produces = {"application/json"})
//    public ResponseEntity<List<SelectItem>> retrieveSearchedParcel(@RequestBody ParcelDataBean parcelDataBean) {
//        return new ResponseEntity<>(mergeParcelTransformer.searchParcel(parcelDataBean), ResponseCode.SUCCESS, "", null);
//    }

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

//    @RequestMapping(value = "/retrieveparcelsbyids", method = RequestMethod.POST, produces = {"application/json"})
//    public ResponseEntity<SelectItem> retrieveParcelsByIds(@RequestBody List<String> parcelIds) {
//        if (!CollectionUtils.isEmpty(parcelIds)) {
//            return new ResponseEntity<>(mergeParcelTransformer.retrieveParcelsByIds(parcelIds), ResponseCode.SUCCESS, "", null);
//        }
//        return null;
//    }

//    @RequestMapping(value = "/mergeparcels", method = RequestMethod.POST, produces = {"application/json"})
//    public ResponseEntity<SelectItem> mergeParcels(@RequestBody StockDataBean stockDataBean) {
//        Object newId = mergeParcelTransformer.mergeParcels(stockDataBean);
//        if (newId != null) {
//            return new ResponseEntity(null, ResponseCode.SUCCESS, "Parcels merged successfully", null, true);
//        } else {
//            return new ResponseEntity(null, ResponseCode.FAILURE, "Failed to merge parcels", null, true);
//        }
//    }
    
    @RequestMapping(value = "/mergeparcel", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> mergeParcel(@RequestBody MergeParcelDataBean mergeParcelDataBean) {
        Boolean result = mergeParcelTransformer.mergeParcel(mergeParcelDataBean);
        System.out.println("result :"+result);
        if (result) {
            return new ResponseEntity(null, ResponseCode.SUCCESS, "Parcels merged successfully", null, true);
        } else {
            return new ResponseEntity(null, ResponseCode.FAILURE, "Parcels cannot be merged", null, true);
        }
    }

}
