/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jyoti
 */
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController<DepartmentDataBean, Long> {

    @Autowired
    DepartmentTransformerBean hkDepartmentTransformerBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    LoginDataBean hkLoginDataBean;

    @Override
    public ResponseEntity<List<DepartmentDataBean>> retrieveAll() {
        List<DepartmentDataBean> departments = hkDepartmentTransformerBean.retrieveDepartmentListInTreeView(hkLoginDataBean.getCompanyId());;
        return new ResponseEntity<List<DepartmentDataBean>>(departments, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<DepartmentDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        return null;
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody DepartmentDataBean departmentDatabean) {
        try {
            if (hkDepartmentTransformerBean.isDepartmentNameExists(departmentDatabean.getDisplayName(), departmentDatabean.getId())) {
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, departmentDatabean.getDisplayName() + " " + "already exists", null);
            } else {
                hkDepartmentTransformerBean.updateDepartment(departmentDatabean);
            }
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (departmentDatabean.getIsActive()) {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "Department saved", null);
        } else {
            return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.SUCCESS, "", null);
        }

    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody DepartmentDataBean departmentDatabean) {
        try {
            if (hkDepartmentTransformerBean.isDepartmentNameExists(departmentDatabean.getDisplayName(), departmentDatabean.getId())) {
                return new ResponseEntity<PrimaryKey<Long>>(null, ResponseCode.FAILURE, departmentDatabean.getDisplayName() + " already exists", null);
            } else {
                PrimaryKey<Long> pk = new PrimaryKey<>();
                pk.setPrimaryKey(hkDepartmentTransformerBean.createDepartment(departmentDatabean));
                return new ResponseEntity<PrimaryKey<Long>>(pk, ResponseCode.SUCCESS, "Department added", null);
            }
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(DepartmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ResponseEntity<DepartmentDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        hkDepartmentTransformerBean.removeDepartment(primaryKey.getPrimaryKey());
        return new ResponseEntity<DepartmentDataBean>(null, ResponseCode.SUCCESS, "Department removed", null);
    }

    @RequestMapping(value = "/searchdepartment", method = RequestMethod.GET, produces = {"application/json"})
    public List<DepartmentDataBean> searchDepartment(@RequestParam("q") String query) throws GenericDatabaseException {
        List<DepartmentDataBean> hkDepartmentDatabeanList = new ArrayList();
        hkDepartmentDatabeanList = hkDepartmentTransformerBean.retrieveAllDepartmentBySearch(query);
        return hkDepartmentDatabeanList;

    }

    @RequestMapping(value = "retrievedeptbyid", method = RequestMethod.GET)
    public ResponseEntity<DepartmentDataBean> retrieveDepartmentById(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(hkDepartmentTransformerBean.retrieveDepartmentByDepartmentId(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, true);
    }

    @RequestMapping(value = "retrievedeptreeview", method = RequestMethod.GET)
    public ResponseEntity<List<DepartmentDataBean>> retrieveSimpleDepView() {
        List<DepartmentDataBean> departments = null;
        departments = hkDepartmentTransformerBean.retrieveDepartmentListInTreeViewSimple(hkLoginDataBean.getCompanyId());
        return new ResponseEntity<List<DepartmentDataBean>>(departments, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/addCustomDataToDepartmentDataBean", method = RequestMethod.POST)
    public DepartmentDataBean addCustomDataToDepartmentDataBean(@RequestBody DepartmentDataBean departmentDataBean) {
        return hkDepartmentTransformerBean.addCustomDataToDepartmentDataBean(departmentDataBean);
    }

    @RequestMapping(value = "/activeuserscountindepartment", method = RequestMethod.POST)
    public ResponseEntity<Integer> activeUsersCountInDepartment(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(hkDepartmentTransformerBean.activeUsersCountInDepartment(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, false);
    }

     @RequestMapping(value = "/checkIfDepartmentIsPresentInAnyFeature", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Boolean>> checkIfDepartmentIsPresentInAnyFeature(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(hkDepartmentTransformerBean.checkIfDepartmentIsPresentInAnyFeature(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, false);
    }
    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
