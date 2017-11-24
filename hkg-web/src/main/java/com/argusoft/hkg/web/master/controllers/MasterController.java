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
import com.argusoft.hkg.web.master.databeans.MasterDataBean;
import com.argusoft.hkg.web.master.transformers.MasterTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.master.databeans.ValueExceptionDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sidhdharth
 */
@RestController
@RequestMapping("/master")
public class MasterController extends BaseController<MasterDataBean, String> {

    @Autowired
    MasterTransformerBean masterTransformerBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Override
    public ResponseEntity<List<MasterDataBean>> retrieveAll() {
        List<MasterDataBean> masterDataBeans = null;
        masterDataBeans = masterTransformerBean.retrieveMasters();
        return new ResponseEntity<>(masterDataBeans, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveMasterValues", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<MasterDataBean> retriveCustomMasters(@RequestBody String label) {
        MasterDataBean finalDataBean = null;
        List<MasterDataBean> masterDataBeans = null;
        List<MasterDataBean> customMaster = new ArrayList<>();
        masterDataBeans = masterTransformerBean.retrieveMasters();
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
            finalDataBean = masterTransformerBean.retrieveMasterById(code);
        }
        return new ResponseEntity<>(finalDataBean, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<MasterDataBean> retrieveById(@RequestBody PrimaryKey<String> primaryKey) {
        MasterDataBean masterDataBean = masterTransformerBean.retrieveMasterById(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(masterDataBean, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> update(@RequestBody MasterDataBean masterDataBean) {
        List<MasterDataBean> masterDataBeans = masterDataBean.getMasterDataBeans();
        masterTransformerBean.update(masterDataBean);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<String>> create(MasterDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<MasterDataBean> deleteById(PrimaryKey<String> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/searchMaster", method = RequestMethod.GET, produces = {"application/json"})
    public List<MasterDataBean> searchMaster(@RequestParam("q") String searchString) {
        //        TODO
        List<MasterDataBean> masterDataBeans = masterTransformerBean.searchMaster(searchString);
        return masterDataBeans;
    }

    @RequestMapping(value = "/checkpassword", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> authenticateForEditMaster(@RequestBody String password) {
        return new ResponseEntity<>(masterTransformerBean.authenticateForEditMaster(password), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievelanguages", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Map<String, String>> retrieveLanguages() {
        Map<String, String> retrieveLanguageMap = masterTransformerBean.retrieveLanguages();
        return new ResponseEntity<>(retrieveLanguageMap, ResponseCode.SUCCESS, null, null, false);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveAllValuesForMasters", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllValuesForMasters(@RequestBody PrimaryKey<String> keyCode) {
        List<SelectItem> allValuesForMasters = masterTransformerBean.retrieveAllValuesForMasters(keyCode.getPrimaryKey(), null);
        //System.out.println("All Val for masters"+allValuesForMasters);
        return new ResponseEntity<>(allValuesForMasters, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveAllValuesForMastersFrMultiSelect", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveAllValuesForMastersFrMultiSelect(@RequestBody String[] searchList) {
        String searchQuery = searchList[1];
        String KeyCode = searchList[0];
        if (searchQuery.isEmpty()) {
            searchQuery = null;
        }
        List<SelectItem> allValuesForMasters = masterTransformerBean.retrieveAllValuesForMasters(KeyCode, searchQuery);
        return new ResponseEntity<>(allValuesForMasters, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievecustomfields", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCustomFields(@RequestBody Long instanceId) {
        List<SelectItem> selectItems = masterTransformerBean.retrieveCustomFields(instanceId);
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievecustomfieldsvaluebykey", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveCustomFieldsValueByKey(@RequestBody Map payload) {
        List<SelectItem> selectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(payload)) {
            Long fieldId = Long.parseLong(payload.get("fieldId").toString());
            String componentType = payload.get("componentType").toString();
            selectItems = masterTransformerBean.retrieveCustomFieldsValueByKey(fieldId, componentType);
        }
        return new ResponseEntity<>(selectItems, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/saveexception", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> saveException(@RequestBody List<ValueExceptionDataBean> valueExceptions) {
        if (!CollectionUtils.isEmpty(valueExceptions)) {
            masterTransformerBean.saveException(valueExceptions);
        }
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Value Exception Added successfully.", null, false);
    }

    @RequestMapping(value = "/retrievevalueexceptions", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<ValueExceptionDataBean>> retrieveValueExceptions(@RequestBody Long instanceId) {
        List<ValueExceptionDataBean> valueExceptionDataBeans = new ArrayList<>();
        if (instanceId != null) {
            valueExceptionDataBeans = masterTransformerBean.retrieveValueExceptions(instanceId);
        }
        return new ResponseEntity<>(valueExceptionDataBeans, ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveprerequisites", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> retrievePrerequisite(@RequestBody Long instanceId) {
        Map<String, Object> prerequisiteMap = new HashMap<>();
        if (instanceId != null) {
            prerequisiteMap = masterTransformerBean.retrievePrerequisite(instanceId);
        }
        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, null, null, false);
    }

}
