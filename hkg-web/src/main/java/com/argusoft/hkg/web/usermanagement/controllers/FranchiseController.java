/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FranchiseMinReqDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FranchiseTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.LocationTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mayank
 *
 */
@RestController
@RequestMapping("/franchise")
public class FranchiseController extends BaseController<FranchiseDataBean, Long> {

     @Autowired
    FranchiseTransformerBean franchiseTransformerBean;

    @Autowired
    LocationTransformerBean locationTransformerBean;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    ApplicationMasterInitializer applicationMasterInitializer;

    @Autowired
    LoginDataBean loginDataBean;



    @RequestMapping(value = "/doesfranchisenameexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> doesFranchiseNameExist(@RequestBody FranchiseDataBean t
    ) {
        return new ResponseEntity<>(franchiseTransformerBean.doesFranchiseNameExist(t.getFranchiseName(), t.getId()), ResponseCode.SUCCESS, null, null, false);
    }

    @Override
    public ResponseEntity<List<FranchiseDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<FranchiseDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(franchiseTransformerBean.retrieveByFranchiseId(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, true);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody FranchiseDataBean franchiseDataBean) {
        String response = franchiseTransformerBean.updateFranchise(franchiseDataBean);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            applicationMasterInitializer.initDesignations();
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Franchise updated successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Franchise can not be updated", null, true);
        }
    }

    /**
     *
     * @param franchiseDataBean
     * @return
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody FranchiseDataBean franchiseDataBean) {
        ResponseEntity<PrimaryKey<Long>> response = franchiseTransformerBean.createFranchise(franchiseDataBean);
        applicationMasterInitializer.initDesignations();

        return response;
    }

    @Override
    public ResponseEntity<FranchiseDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        franchiseTransformerBean.removeFranchise(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Franchise removed", null);
    }

    @RequestMapping(value = "/doesusernameexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> doesUserNameExist(@RequestBody String name) {
        return new ResponseEntity<>(franchiseTransformerBean.doesUserNameExist(name), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieve/locations", method = RequestMethod.GET)
    public ResponseEntity<Map> retrieveAllLocation(@RequestParam(value = "active") String active) {
        Map map = new HashMap();
        map = locationTransformerBean.retieveLocation(active);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/allfranchise", method = RequestMethod.GET)
    public ResponseEntity<List<FranchiseDataBean>> retrieveAllFranchise(@RequestParam(value = "tree") boolean treeview) throws GenericDatabaseException {
        List<FranchiseDataBean> retrieveAllFranchise = franchiseTransformerBean.retrieveAllFranchise(treeview);
        return new ResponseEntity<>(retrieveAllFranchise, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieve/franchiseminreq", method = RequestMethod.GET)
    public ResponseEntity<List<FranchiseMinReqDataBean>> retrieveFranchiseMinRequirementByFranchise(@RequestParam(value = "id") String id) {
        List<FranchiseMinReqDataBean> retrieveFranchiseMinRequirementByFranchiseId = franchiseTransformerBean.retrieveFranchiseMinRequirementByFranchiseId(Long.parseLong(id), false);
        return new ResponseEntity<>(retrieveFranchiseMinRequirementByFranchiseId, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/isthereanylinkwithfranchise", method = RequestMethod.POST)
    public ResponseEntity<Boolean> isThereAnyLinkWithFranchise(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(franchiseTransformerBean.isThereAnyLinkWithFranchise(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrievelocationsbytype", method = RequestMethod.GET)
    public List<SelectItem> retrieveLocationsByType(@RequestParam("type") String type) {
        return locationTransformerBean.retrieveLocationHeirarchyByType(type, true);
    }

    @RequestMapping(value = "/retrievefranchisenames", method = RequestMethod.GET)
    public List<SelectItem> retrieveFranchiseNames() throws GenericDatabaseException {
        return franchiseTransformerBean.retrieveAllFranchiseNames();
    }

    @RequestMapping(value = "/searchByFranchiseName", method = RequestMethod.GET, produces = {"application/json"})
    public List<FranchiseDataBean> searchByFranchiseName(@RequestParam("q") String franchiseName) throws GenericDatabaseException {
        List<FranchiseDataBean> franchiseDataBean = franchiseTransformerBean.searchFranchise(franchiseName);
        return franchiseDataBean;
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
