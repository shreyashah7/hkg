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
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.FeatureTransformerBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author piyush
 */
@RestController
@RequestMapping("/feature")
public class FeatureController extends BaseController<FeatureDataBean, Long> {

    @Autowired
    private FeatureTransformerBean featureTransformer;
    @Autowired
    private LoginDataBean loginDataBean;
    List<FeatureDataBean> sortedSystemFeatureDatabean = null;
    Map<Long, FeatureDataBean> idTofeatureDatabeanMap = null;
    private static final String menuTypeKey = "menutypes";

    @RequestMapping(value = "/retrieve/features", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<FeatureDataBean>> retrieveFeatures() {

        List<FeatureDataBean> featureDatabeans = featureTransformer.retrieveFeaturesByStatusBySeqnoByCompany(Boolean.TRUE, Boolean.TRUE, null);
        List<FeatureDataBean> systemFeatureFinalList = null;

        if (!CollectionUtils.isEmpty(featureDatabeans)) {

            sortedSystemFeatureDatabean = new ArrayList<>();
            idTofeatureDatabeanMap = new HashMap<>();
            systemFeatureFinalList = new ArrayList<>();

            // convert all to DTO and vreate a map of id to dto
            for (FeatureDataBean featureDatabean : featureDatabeans) {
                idTofeatureDatabeanMap.put(featureDatabean.getId(), featureDatabean);
            }

            // sort accordingly
            // see if parent is added in list, then add its children
            for (FeatureDataBean hkSystemFeatureDatabean : featureDatabeans) {
                addHkSystemFeatureDatabeanToSortedList(hkSystemFeatureDatabean);
            }

            // set children list to parent and add root nodes to rootList
            for (FeatureDataBean systemFeatureDatabean : sortedSystemFeatureDatabean) {
                long parentId = systemFeatureDatabean.getParentId();
                if (parentId != 0) {
                    FeatureDataBean parentDto = idTofeatureDatabeanMap.get(parentId);
                    List<FeatureDataBean> childrenOfparent = parentDto.getChildren();
                    if (childrenOfparent == null) {
                        childrenOfparent = new ArrayList<>();
                        parentDto.setChildren(childrenOfparent);
                    }
                    childrenOfparent.add(systemFeatureDatabean);
                } else {
                    systemFeatureFinalList.add(systemFeatureDatabean);
                }
            }
        }
        return new ResponseEntity<>(systemFeatureFinalList, ResponseCode.SUCCESS, "", null);
    }

    private void addHkSystemFeatureDatabeanToSortedList(FeatureDataBean featureToAddIntosortedHkSystemFeatureDatabean) {
        if (!sortedSystemFeatureDatabean.contains(featureToAddIntosortedHkSystemFeatureDatabean)) {
            long parentId = featureToAddIntosortedHkSystemFeatureDatabean.getParentId();
            if (parentId != 0) {
                FeatureDataBean parentDatabean = idTofeatureDatabeanMap.get(parentId);
                if (sortedSystemFeatureDatabean.contains(parentDatabean)) {
                    sortedSystemFeatureDatabean.add(featureToAddIntosortedHkSystemFeatureDatabean);
                } else {
                    addHkSystemFeatureDatabeanToSortedList(parentDatabean);
                    sortedSystemFeatureDatabean.add(featureToAddIntosortedHkSystemFeatureDatabean);
                }
            } else {
                sortedSystemFeatureDatabean.add(featureToAddIntosortedHkSystemFeatureDatabean);
            }
        }
    }

    @RequestMapping(value = "/create/feature", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, Long>> createFeature(@Valid @RequestBody FeatureDataBean hkSystemFeaturesDatabean) throws GenericDatabaseException {

        Map<String, Long> result = new HashMap<>();
        if (featureTransformer.isFeatureNameExists(hkSystemFeaturesDatabean.getFeatureName(), loginDataBean.getCompanyId())) {
            featureTransformer.createFeature(hkSystemFeaturesDatabean);
            result.put("result", 1L);
            return new ResponseEntity<>(result, ResponseCode.SUCCESS, "Feature created successfully", null);
        } else {
            result.put("result", 0L);
            return new ResponseEntity<>(result, ResponseCode.FAILURE, "Feature name already exists", null);
        }

    }

    @RequestMapping(value = "/update/feature", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map<String, Long>> updateFeature(@Valid @RequestBody FeatureDataBean hkSystemFeaturesDatabean) {
        Map<String, Long> result = new HashMap<>();

        if (featureTransformer.updateFeature(hkSystemFeaturesDatabean)) {
            result.put("result", 1L);
            return new ResponseEntity<>(result, ResponseCode.SUCCESS, "Feature updated successfully", null);
        } else {
            result.put("result", 0L);
            return new ResponseEntity<>(result, ResponseCode.FAILURE, "Feature name already exists", null);
        }
    }

//    @RequestMapping(value = "/retrieve/permissions", method = RequestMethod.POST, consumes = {"application/json"})
//    public @ResponseBody
//    List<FeaturePermissionDataBean> retrievePermissions(@RequestBody Long featureId) {
//
//        System.out.println("featureId : " + featureId);
//
//        Long companyId = loginDataBean.getCompanyId();
//
//        List<UMFeaturePermission> featurePermissions = featurePermissionTransformerBean.retrieveFeaturePermissionsByFeatureAndCompany(featureId, companyId);
//        List<FeaturePermissionDataBean> permissions = new ArrayList<>();
//        FeaturePermissionDataBean addNewPermission = new FeaturePermissionDataBean();
//        addNewPermission.setName("Add new");
//        addNewPermission.setDescription("Add new");
//        addNewPermission.setIsEditable(true);
//        addNewPermission.setId(0L);
//        permissions.add(addNewPermission);
//
//        if (!CollectionUtils.isEmpty(featurePermissions)) {
//            System.out.println("SIZE : " + featurePermissions.size());
//            for (UMFeaturePermission featurePermission : featurePermissions) {
//                FeaturePermissionDataBean permissionDatabean = featurePermissionTransformerBean.convertFeaturePermissionToFeaturePermissionDatabean(featurePermission, new FeaturePermissionDataBean());
//                String attributes = permissionDatabean.getSelectedAttributes();
//                String[] list = attributes.split(",");
//                List<FeatureDataBean> featureDatabeans = new ArrayList<>();
//                for (String string : list) {
//                    string = string.replace("#", "");
//                    UMFeature feature = featureTransformer.retrieveFeatureByFeatureId(Long.valueOf(string));
//                    FeatureDataBean featureDatabean = new FeatureDataBean();
//                    featureDatabean.setId(feature.getId());
//                    featureDatabean.setFeatureName(feature.getName());
//                    featureDatabeans.add(featureDatabean);
//                }
//
//                if (companyId.equals(permissionDatabean.getCompany())) {
//                    permissionDatabean.setIsEditable(true);
//                } else {
//                    permissionDatabean.setIsEditable(false);
//                }
//                permissionDatabean.setAttributes(featureDatabeans);
//                permissions.add(permissionDatabean);
//            }
//        }
//
//        return permissions;
//    }
    @RequestMapping(value = "/retrievebylist", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<FeatureDataBean>> retrieveSystemFeaturesByListOfType(@RequestBody Map<String, List<String>> map) {
        List<String> menuTypeVals = map.get(menuTypeKey);
        List<FeatureDataBean> retrieveSystemFeaturesByListOfType = featureTransformer.retrieveSystemFeaturesByListOfType(menuTypeVals, null, Boolean.TRUE);
        return new ResponseEntity<>(retrieveSystemFeaturesByListOfType, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrievefeaturecategory", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<FeatureDataBean>>> retrieveSystemFeaturesByCategory() throws GenericDatabaseException {
        Map<String, List<FeatureDataBean>> retrieveSystemFeaturesByCategory = featureTransformer.retrieveSystemFeaturesByCategory(loginDataBean.getCompanyId());
        return new ResponseEntity<>(retrieveSystemFeaturesByCategory, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/savesequencing", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<FeatureDataBean>>> saveFeatureSequencing(@RequestBody List<Long> ids) throws GenericDatabaseException {
        String response = featureTransformer.saveSequencing(ids);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Feature sequence stored successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Feature sequence can not be stored.", null);
        }
    }

    @Override
    public ResponseEntity<List<FeatureDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<FeatureDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(FeatureDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(FeatureDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<FeatureDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
