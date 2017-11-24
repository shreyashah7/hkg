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
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FieldDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.GoalPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.StaticServiceFieldPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.SystemFeatureDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationTransformerBean;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author akta
 */
@RestController
@RequestMapping("/designation")
public class DesignationController extends BaseController<DesignationDataBean, Long> {

    @Autowired
    DesignationTransformerBean designationTransformer;

    @Autowired
    ApplicationMasterInitializer applicationMasterInitializer;

    @Override
    public ResponseEntity<List<DesignationDataBean>> retrieveAll() {
        List<DesignationDataBean> listDesignationDataBean = designationTransformer.retrieveDesignations(true);
        return new ResponseEntity<>(listDesignationDataBean, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<DesignationDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        DesignationDataBean designationDataBean = designationTransformer.retrieveDesignation(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(designationDataBean, ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody DesignationDataBean designationDataBean) {
        if (designationTransformer.isDesignationNameExist(designationDataBean.getDisplayName(), designationDataBean.getId())) {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, designationDataBean.getDisplayName() + " already exists.", null, false);
        } else {
            try {
                designationTransformer.update(designationDataBean);
                applicationMasterInitializer.initDesignations();
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Designation updated.", null);
            } catch (GenericDatabaseException ex) {
                Logger.getLogger(DesignationController.class.getName()).log(Level.SEVERE, null, ex);
                return new ResponseEntity<>(null, ResponseCode.FAILURE, "Designation could not be updated.", null);
            }
        }
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody DesignationDataBean designationDataBean) {
        if (designationTransformer.isDesignationNameExist(designationDataBean.getDisplayName(), null)) {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, designationDataBean.getDisplayName() + " already exists.", null, false);
        } else {
            try {
                designationTransformer.create(designationDataBean);
                applicationMasterInitializer.initDesignations();
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Designation saved.", null);
            } catch (GenericDatabaseException ex) {
                Logger.getLogger(DesignationController.class.getName()).log(Level.SEVERE, null, ex);
                return new ResponseEntity<>(null, ResponseCode.FAILURE, "Designation could not be saved.", null);

            }
        }
    }

    @Override
    public ResponseEntity<DesignationDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        designationTransformer.delete(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Designation removed.", null, true);
    }

    @RequestMapping(value = "retrieve/systemfeatures", method = RequestMethod.GET)
    public ResponseEntity<Collection<SystemFeatureDataBean>> retrieveAllSystemFeatures() {
        Collection<SystemFeatureDataBean> listSystemFeatureDatabean = designationTransformer.retrieveAllSystemFeatures();
        return new ResponseEntity<>(listSystemFeatureDatabean, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/searchdesignation", method = RequestMethod.GET, produces = {"application/json"})
    public List<DesignationDataBean> searchDepartment(@RequestParam("q") String query) throws GenericDatabaseException {
        List<DesignationDataBean> hkDepartmentDatabeanList = new ArrayList();
        hkDepartmentDatabeanList = designationTransformer.retrieveAllDesignationBySearch(query);
        return hkDepartmentDatabeanList;
    }

    @RequestMapping(value = "/activeuserscountindesignation", method = RequestMethod.POST)
    public ResponseEntity<Integer> activeUsersCountInDepartment(@RequestBody PrimaryKey<Long> primaryKey) {
        return new ResponseEntity<>(designationTransformer.activeUsersCountInDesignation(primaryKey.getPrimaryKey()), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieveFeaturesSelectedForDesignation", method = RequestMethod.POST)
    public ResponseEntity<Map<Long, Collection<SystemFeatureDataBean>>> retrieveFeaturesSelectedForDesignation(@RequestBody List<Long> desigIds) {
        return new ResponseEntity<>(designationTransformer.retrieveFeaturesSelectedForDesignation(desigIds), ResponseCode.SUCCESS, null, null, false);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievefieldsbyfeature", method = RequestMethod.GET)
    public ResponseEntity<Map<Long, List<StaticServiceFieldPermissionDataBean>>> retrieveFieldsByFeature() {
        Map<Long, List<StaticServiceFieldPermissionDataBean>> fieldFeatureMap = designationTransformer.retrieveFieldsByFeature();
        if (!CollectionUtils.isEmpty(fieldFeatureMap)) {
            return new ResponseEntity<>(fieldFeatureMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/savegoalpermission", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveGoalPermission(@RequestBody List<GoalPermissionDataBean> goalPermissions) {
        designationTransformer.saveGoalPermission(goalPermissions, true, "M");
    }

    @RequestMapping(value = "/retrievegoalpermissionbydesignation", method = RequestMethod.GET)
    public ResponseEntity<List<GoalPermissionDataBean>> retrieveActiveGoalPermissionsByDesignation(@RequestParam("designation") Long designation) {
        List<GoalPermissionDataBean> goalPermissions = designationTransformer.retrieveActiveGoalPermissionsByDesignation(designation, "M");

        if (!CollectionUtils.isEmpty(goalPermissions)) {
            return new ResponseEntity<>(goalPermissions, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/retrievegoalsheetpermissionbydesignation", method = RequestMethod.GET)
    public ResponseEntity<List<GoalPermissionDataBean>> retrieveActiveGoalSheetPermissionsByDesignation(@RequestParam("designation") Long designation) {
        List<GoalPermissionDataBean> goalPermissions = designationTransformer.retrieveActiveGoalPermissionsByDesignation(designation, "GS");

        if (!CollectionUtils.isEmpty(goalPermissions)) {
            return new ResponseEntity<>(goalPermissions, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/retrievereportgroupnames", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> retrieveReportGroupNames(@RequestBody List<String> valueIds) {
        Map<String, String> groupIdNameMap = designationTransformer.retrieveReportGroupName(valueIds);
        if (!CollectionUtils.isEmpty(groupIdNameMap)) {
            return new ResponseEntity<>(groupIdNameMap, ResponseCode.SUCCESS, null, null, false);
        } else {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/checkcirculardependency", method = RequestMethod.POST)
    public ResponseEntity<Boolean> checkCircularDependency(@RequestBody Map<String, Long> requestMap) {
        Long designationId = requestMap.get("designationId");
        Long parentId = requestMap.get("parentId");
        Boolean result = designationTransformer.checkCircularDependencies(designationId, parentId);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, null, null, false);
    }
    
    @RequestMapping(value = "/retrievechildroles", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveChildRoles(@RequestBody Map<String, String> searchRoleMap) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchRoleMap) && searchRoleMap.get("parentRole") != null) {
            String parentRole = searchRoleMap.get("parentRole");
            String searchRole = searchRoleMap.get("searchRole");
            List<UMRole> uMRoles = designationTransformer.retrieveChildRolesByCompany(searchRole, parentRole);
            if (!CollectionUtils.isEmpty(uMRoles)) {
                for (UMRole uMRole : uMRoles) {
                    hkSelectItems.add(new SelectItem(uMRole.getId(), uMRole.getName(), HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
                }
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }
}
