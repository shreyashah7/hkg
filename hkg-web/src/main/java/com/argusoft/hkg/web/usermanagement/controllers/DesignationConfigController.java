/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationConfigTransformerBean;
import java.util.ArrayList;
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
 * @author shreya
 */
@RestController
@RequestMapping("/configdesign")
public class DesignationConfigController {

    @Autowired
    DesignationConfigTransformerBean designationConfigTransformerBean;
    @Autowired
    DepartmentTransformerBean hkDepartmentTransformerBean;
    @Autowired
    LoginDataBean hkLoginDataBean;

    @RequestMapping(value = "retrieveAllDesignations", method = RequestMethod.GET)
    public ResponseEntity<List<DepartmentDataBean>> retrieveAllDesignations() {
        System.out.println("inside desig");
        List<DepartmentDataBean> departments = designationConfigTransformerBean.retrieveDesignationListInTreeView();
        return new ResponseEntity<>(departments, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "retrieveDesignationConfiguration", method = RequestMethod.POST)
    public ResponseEntity<DesignationDataBean> retrieveDesignationConfiguration(@RequestBody Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            List<Long> featureIds = new ArrayList<>();
            Long designationId = null;
            if (map.containsKey("featureIds") && map.get("featureIds") != null) {
                List<Integer> featureIdsInInt = (List<Integer>) map.get("featureIds");
                if (!CollectionUtils.isEmpty(featureIdsInInt)) {
                    for (Integer fid : featureIdsInInt) {
                        featureIds.add(fid.longValue());
                    }
                }
            }
            if (map.containsKey("designationId") && map.get("designationId") != null) {
                designationId = ((Integer) map.get("designationId")).longValue();
            }
            DesignationDataBean designationDataBean = designationConfigTransformerBean.retrieveDesignationConfiguration(designationId, featureIds);
            return new ResponseEntity<>(designationDataBean, ResponseCode.SUCCESS, "", null);
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "", null);
    }

    @RequestMapping(value = "updateDesignationConfiguration", method = RequestMethod.POST)
    public ResponseEntity<DesignationDataBean> updateDesignationConfiguration(@RequestBody DesignationDataBean designationDataBean) {
        if (designationDataBean != null) {
            designationConfigTransformerBean.updateDesignationConfiguration(designationDataBean);
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Designation Configured Successfully", null);
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Cannot Configure Designation", null);
    }
    
    @RequestMapping(value = "/searchdesignation", method = RequestMethod.GET, produces = {"application/json"})
    public List<DesignationDataBean> searchDesignationByName(@RequestParam("q") String query) throws GenericDatabaseException {
        List<DesignationDataBean> designationDataBeans = designationConfigTransformerBean.searchDesignationByName(query);
        return designationDataBeans;
    }

}
