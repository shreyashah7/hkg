/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.transformers.CenterMasterTransformer;
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkMasterValueDocument;
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
 * @author akta
 */
@RestController
@RequestMapping("/master")
public class CenterMasterController extends BaseController<MasterDataBean, String> {

    @Autowired
    CenterMasterTransformer centerMasterTransformer;
// get request retrieve

    public ResponseEntity<List<MasterDataBean>> retrieveAll() {
        List<MasterDataBean> masterDataBeans = null;
        masterDataBeans = centerMasterTransformer.retrieveMasters();
        return new ResponseEntity<>(masterDataBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "retrieve", method = RequestMethod.POST)
    public ResponseEntity<MasterDataBean> retrieveById(@RequestBody PrimaryKey<String> primaryKey) {
        MasterDataBean masterDataBean = centerMasterTransformer.retrieveMasterById(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(masterDataBean, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveMasterValues", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<MasterDataBean> retriveCustomMasters(@RequestBody String label) {
        MasterDataBean finalDataBean = null;
        List<MasterDataBean> masterDataBeans = null;
        List<MasterDataBean> customMaster = new ArrayList<>();
        masterDataBeans = centerMasterTransformer.retrieveMasters();
        for (MasterDataBean masterDataBean : masterDataBeans) {
            if (masterDataBean.getMasterType().equals("B") || masterDataBean.getMasterType().equals("b")) {
            } else {
                customMaster.add(masterDataBean);
            }
        }

        if (!CollectionUtils.isEmpty(customMaster)) {
            String code = "";
            for (MasterDataBean customMaster1 : customMaster) {

                if (customMaster1.getMasterName().equals(label)) {
                    code = customMaster1.getCode();
                }
            }
            finalDataBean = centerMasterTransformer.retrieveMasterById(code);
        }
        return new ResponseEntity<>(finalDataBean, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> update(MasterDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> create(MasterDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MasterDataBean> deleteById(PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveAllValuesForMasters", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllValuesForMasters(@RequestBody PrimaryKey<String> keyCode) {
        List<SelectItem> allValuesForMasters = centerMasterTransformer.retrieveAllValuesForMasters(keyCode.getPrimaryKey(), null);
        return new ResponseEntity<>(allValuesForMasters, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveAllValuesForMastersFrMultiSelect", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllValuesForMastersFrMultiSelect(@RequestBody String[] searchList) {
        String searchQuery = searchList[1];
        String KeyCode = searchList[0];
        System.out.println("Search" + searchQuery + "----oce" + KeyCode);
        if (searchQuery.isEmpty()) {
            searchQuery = null;
        }
        List<SelectItem> allValuesForMasters = centerMasterTransformer.retrieveAllValuesForMasters(KeyCode, searchQuery);
        System.out.println("All Val for masters" + allValuesForMasters);
        return new ResponseEntity<>(allValuesForMasters, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveAllValuesForAllMasters", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, List<SelectItem>>> retrieveAllValuesForAllMasters(@RequestBody List<String> keyCodes) {
        Map<String, List<SelectItem>> result = centerMasterTransformer.retrieveAllValuesForMasters(keyCodes, null);
        //System.out.println("All Val for masters"+allValuesForMasters);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, null, null, false);
    }

}
