/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ConfigurationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.ConfigurationTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.EmployeeTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.RuleManagementTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shifa
 */
@RestController
@RequestMapping("/configdept")
public class ConfigurationController extends BaseController<ConfigurationDataBean, Long> {

    @Autowired
    ConfigurationTransformerBean hkConfigurationTransformerBean;
    @Autowired
    DepartmentTransformerBean hkDepartmentTransformerBean;
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    LoginDataBean hkLoginDataBean;
    @Autowired
    RuleManagementTransformerBean ruleManagementTransformerBean;
    @Autowired
    EmployeeTransformerBean employeeTransformerBean;

    @RequestMapping(value = "retrieveAllDepartments", method = RequestMethod.GET)
    public ResponseEntity<List<DepartmentDataBean>> retrieveAllDepartments() {
        List<DepartmentDataBean> departments = hkConfigurationTransformerBean.retrieveDepartmentListInTreeView();
        return new ResponseEntity<>(departments, ResponseCode.SUCCESS, "", null);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        Map<Long, String> retrieveEntitys = ruleManagementTransformerBean.retrieveEntitys(hkLoginDataBean.getCompanyId());
        Map<String, List<SelectItem>> retrieveOperatorsByType = ruleManagementTransformerBean.retrieveOperatorsByType();
        Map<String, Object> map = new HashMap<>();
        map.put("entity", retrieveEntitys);
        map.put("operator", retrieveOperatorsByType);
        return new ResponseEntity<>(map, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/retrieveFieldsByEntity", method = RequestMethod.POST)
    public List<CustomFieldDataBean> retrieveFieldsByEntity(@RequestBody PrimaryKey<Long> primaryKey) {
        List<CustomFieldDataBean> retrieveFieldsByEntity = ruleManagementTransformerBean.retrieveFieldsByEntity(primaryKey.getPrimaryKey(), hkLoginDataBean.getCompanyId());
        return retrieveFieldsByEntity;
    }

    @Override
    public ResponseEntity<List<ConfigurationDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<ConfigurationDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retriveConfigDetailByDepId", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<ConfigurationDataBean> retriveConfigDetailByDepId(@RequestBody Long deptId) {
        ConfigurationDataBean configDataBean = hkConfigurationTransformerBean.retrieveConfigurationDetailsByDepartmentId(deptId);

        return new ResponseEntity<>(configDataBean, ResponseCode.SUCCESS, "", null, false);

    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(ConfigurationDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override

    public ResponseEntity<PrimaryKey<Long>> create(@RequestBody ConfigurationDataBean configurationDataBean) {
        Long deptId = hkConfigurationTransformerBean.createConfigurationForDepartment(configurationDataBean);
        if (deptId != null) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Configuration saved successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Failed to save configuration", null, true);
        }
    }

    @Override
    public ResponseEntity<ConfigurationDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveDesignationForDept", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveDesignationForDept(@RequestBody Long depId) {
        List<SelectItem> desgList = employeeTransformerBean.retrieveDesignationByDepartment(depId);
        return desgList;
    }

    @RequestMapping(value = "/searchDepartmentForConfiguration", method = RequestMethod.GET, produces = {"application/json"})
    public List<ConfigurationDataBean> searchDepartmentForConfiguration(@RequestParam("q") String query) throws GenericDatabaseException {
        List<ConfigurationDataBean> hkconfigurationList = new ArrayList();
        hkconfigurationList = hkConfigurationTransformerBean.searchDepartmentForConfiguration(query);
        return hkconfigurationList;

    }

    @RequestMapping(value = "/retrieveAssociatedDepartments", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveAssociatedDepartments(@RequestBody(required = false) Long depId) {
        List<SelectItem> deptList = hkConfigurationTransformerBean.retrieveAssociatedDepts(depId);
        return deptList;
    }
}
