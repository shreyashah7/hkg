/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.departmentflow.databeans.DepartmentFlowDataBean;
import com.argusoft.hkg.web.departmentflow.transformers.DepartmentFlowTransformer;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shifa
 */
@RestController
@RequestMapping("/departmentflow")
public class DepartmentFlowController extends BaseController<DepartmentFlowDataBean, Long> {

    @Autowired
    DepartmentFlowTransformer departmentFlowTransformerBean;

    @Override
    public ResponseEntity<List<DepartmentFlowDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<DepartmentFlowDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(DepartmentFlowDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(DepartmentFlowDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<DepartmentFlowDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrieveDataForProcessFlow", method = RequestMethod.GET)
    public ResponseEntity<DepartmentFlowDataBean> retrieveDataForProcessFlow() {
        return new ResponseEntity<>(departmentFlowTransformerBean.retrieveDataForProcessFlow(), ResponseCode.SUCCESS, "", null, false);
    }

}
