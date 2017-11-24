/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.HkAssociatedDeptDocument;
import com.argusoft.hkg.nosql.model.HkAssociatedDesigDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.AssociatedDepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.AssociatedDesignationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ConfigurationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureFieldPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RoleFeatureModifierDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleCriteriaDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shifa
 */
@Service
public class ConfigurationTransformerBean {

    @Autowired
    LoginDataBean hkLoginDataBean;
    @Autowired
    HkFieldService hkFieldService;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkCustomFieldService customFieldService;
    @Autowired
    HkConfigurationService hkConfigurationService;
    @Autowired
    HkRuleService ruleService;
    public static Long maxCountOfRule = 0l;

    public Long createConfigurationForDepartment(ConfigurationDataBean configurationDataBean) {
        // Retrieve Max count for rule

        maxCountOfRule = hkConfigurationService.retrieveMaxCountOfRule(hkLoginDataBean.getCompanyId());

        Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldDocument = customFieldService.mapOfFieldIdAndHkFieldDocument(hkLoginDataBean.getCompanyId());
        // Convert Main Department Config Document to Its DataBean
        Map<Long, HkCriteriaSetDocument> mapOfRuleListForDepartment = null;
        mapOfRuleListForDepartment = hkConfigurationService.mapOfretrieveRuleListForDepartment(configurationDataBean.getDepartment());

        HkDepartmentConfigDocument departmentConfigDocument = this.convertHkDepartmentConfigDataBeanToHkDepartmentConfigDocument(configurationDataBean, HkSystemConstantUtil.CREATE_OPERATION, mapOfRuleListForDepartment);

        // ---------------------------------Code for associated department of designation starts here------------------------------------------------
        Map<Long, HkAssociatedDeptDocument> mapofExistingAssociatedDepartmentsForDept = null;
        List<Long> existingAssociatedDepartmentIds = null;
        mapofExistingAssociatedDepartmentsForDept = hkConfigurationService.retrieveMapOfAssociatedDepartmentsForDept(departmentConfigDocument.getDepartment());
        if (mapofExistingAssociatedDepartmentsForDept != null && !mapofExistingAssociatedDepartmentsForDept.isEmpty()) {
            existingAssociatedDepartmentIds = new ArrayList<>();
            for (Map.Entry<Long, HkAssociatedDeptDocument> entrySet : mapofExistingAssociatedDepartmentsForDept.entrySet()) {
                existingAssociatedDepartmentIds.add(entrySet.getKey());

            }
        }

        List<AssociatedDepartmentDataBean> associatedDepartmentsList = configurationDataBean.getAssociatedDepartments();
        List<HkAssociatedDeptDocument> associatedDeptDocumentList = null;
        List<Long> associatedDepartmentIds = null;
        if (!CollectionUtils.isEmpty(associatedDepartmentsList)) {
            associatedDepartmentIds = new ArrayList<>();
            associatedDeptDocumentList = new ArrayList<>();
            Long countAchievedSoFar;
//            if (configurationDataBean.getMaxCountAchievedForRule() != null) {
//                countAchievedSoFar = configurationDataBean.getMaxCountAchievedForRule();
//            } else {
//                countAchievedSoFar = maxCountOfRule;
//            }

            for (AssociatedDepartmentDataBean associatedDepartment : associatedDepartmentsList) {
                // Convert Associated departments databean to iys document
                HkAssociatedDeptDocument existingAssociatedDeptDocument = null;
                associatedDepartmentIds.add(associatedDepartment.getDepartment());
                String operation = null;
                if (!CollectionUtils.isEmpty(existingAssociatedDepartmentIds) && existingAssociatedDepartmentIds.contains(associatedDepartment.getDepartment())) {
                    // Update
                    if (mapofExistingAssociatedDepartmentsForDept != null && !mapofExistingAssociatedDepartmentsForDept.isEmpty() && mapofExistingAssociatedDepartmentsForDept.containsKey(associatedDepartment.getDepartment())) {
                        existingAssociatedDeptDocument = mapofExistingAssociatedDepartmentsForDept.get(associatedDepartment.getDepartment());
                        operation = HkSystemConstantUtil.UPDATE_OPERATION;
                    }
                } else {
                    // Create
                    operation = HkSystemConstantUtil.CREATE_OPERATION;
                }
                HkAssociatedDeptDocument associatedDeptDocument = this.convertAssociatedDepartmentDataBeanToHkAssociatedDeptDocument(associatedDepartment, existingAssociatedDeptDocument, operation);
                associatedDeptDocumentList.add(associatedDeptDocument);
            }
            // 
            if (!CollectionUtils.isEmpty(existingAssociatedDepartmentIds) && !CollectionUtils.isEmpty(associatedDepartmentIds)) {
                // Make a copy of list which is coming from server
                List<Long> archiveListForAssociatedDep = new ArrayList<>(existingAssociatedDepartmentIds);
                // Make a list which removes the elements which are in our databean list.So it contains the elements which are of no use & needs to be archived
                archiveListForAssociatedDep.removeAll(associatedDepartmentIds);
                if (!CollectionUtils.isEmpty(archiveListForAssociatedDep)) {
                    for (Long archiveAssociatedData : archiveListForAssociatedDep) {
                        if (mapofExistingAssociatedDepartmentsForDept != null && !mapofExistingAssociatedDepartmentsForDept.isEmpty() && mapofExistingAssociatedDepartmentsForDept.containsKey(archiveAssociatedData)) {
                            {
                                HkAssociatedDeptDocument associatedDeptDocForArchive = mapofExistingAssociatedDepartmentsForDept.get(archiveAssociatedData);
                                associatedDeptDocForArchive.setIsArchive(Boolean.TRUE);
                                associatedDeptDocumentList.add(associatedDeptDocForArchive);
                            }
                        }
                    }
                }
            }

            departmentConfigDocument.setAssociatedDepartments(associatedDeptDocumentList);
        }
        //------------------------------------ Code for associated department of designation ends  here--------------------------------------------------
//        Map<Long, List<HkFeatureFieldPermissionDocument>> mapOfDesignationIdWithListOfpermissionDocument = this.retrieveMapOfDesignationIdWithListOfpermissionDocument(configurationDataBean.getDepartment());
//        List<AssociatedDesignationDataBean> associatedDesignationList = configurationDataBean.getAssociatedDesignations();
//        List<HkAssociatedDesigDocument> associatedDesigDocumentList = null;
//        if (!CollectionUtils.isEmpty(associatedDesignationList)) {
//            associatedDesigDocumentList = new ArrayList<>();
//            for (AssociatedDesignationDataBean associatedDesig : associatedDesignationList) {
//                List<HkFeatureFieldPermissionDocument> fieldPermissionForDesg = null;
//                List<String> featureFieldPermissionIds = null;
//                Map<String, HkFeatureFieldPermissionDocument> featureFieldPermissionIdWithDocument = null;
//                if (mapOfDesignationIdWithListOfpermissionDocument != null && !mapOfDesignationIdWithListOfpermissionDocument.isEmpty() && mapOfDesignationIdWithListOfpermissionDocument.containsKey(associatedDesig.getDesignation())) {
//                    fieldPermissionForDesg = mapOfDesignationIdWithListOfpermissionDocument.get(associatedDesig.getDesignation());
//                    if (!CollectionUtils.isEmpty(fieldPermissionForDesg)) {
//                        featureFieldPermissionIds = new ArrayList<>();
//                        featureFieldPermissionIdWithDocument = new HashMap<>();
//                        for (HkFeatureFieldPermissionDocument fieldPermission : fieldPermissionForDesg) {
//                            featureFieldPermissionIds.add(fieldPermission.getId());
//                            featureFieldPermissionIdWithDocument.put(fieldPermission.getId(), fieldPermission);
//
//                        }
//                    }
//                }
//                HkAssociatedDesigDocument associatedDesigDocument = this.convertAssociatedDesignationDataBeanToHkAssociatedDesigDocument(associatedDesig, mapOfFieldIdAndHkFieldDocument, featureFieldPermissionIds, featureFieldPermissionIdWithDocument);
//
//                associatedDesigDocumentList.add(associatedDesigDocument);
//            }
//            departmentConfigDocument.setAssociatedDesignations(associatedDesigDocumentList);
//        }
        if (!CollectionUtils.isEmpty(configurationDataBean.getRoleFeatureModifiers())) {
            List<HkRoleFeatureModifierDocument> modifierDocuments = new ArrayList<>();
            for (RoleFeatureModifierDataBean roleFeatureModifierDataBean : configurationDataBean.getRoleFeatureModifiers()) {
                modifierDocuments.add(this.convertRoleFeatureModifierDataBeanToDocument(roleFeatureModifierDataBean));
            }
            hkConfigurationService.saveRoleFeatureModifiers(modifierDocuments);
        }
        Long deptId = hkConfigurationService.createConfiguration(departmentConfigDocument);

        return deptId;
    }

    public List<RoleFeatureModifierDataBean> retrieveModifiersByDesignations(List<Long> designationIds) {
        List<HkRoleFeatureModifierDocument> modifierDocumnets = hkConfigurationService.retrieveModifiersByDesignations(designationIds, hkLoginDataBean.getCompanyId(), null);
        if (!CollectionUtils.isEmpty(modifierDocumnets)) {
            List<RoleFeatureModifierDataBean> result = new ArrayList<>();
            for (HkRoleFeatureModifierDocument roleFeatureModifierDocument : modifierDocumnets) {
                List<String> designationIdList = new ArrayList<>();
                if (roleFeatureModifierDocument.getAsDesignation() != null) {
                    for (Long asDesignation : roleFeatureModifierDocument.getAsDesignation()) {
                        designationIdList.add(asDesignation.toString() + ":R");
                    }
                }
                Map<String, String> recipientIdCodeMap = userManagementServiceWrapper.retrieveRecipientNames(designationIdList);
                RoleFeatureModifierDataBean roleFeatureModifierDataBean = this.convertRoleFeatureModifierDocumentToDataBean(roleFeatureModifierDocument);
                roleFeatureModifierDataBean.setAsDesignationIdName(recipientIdCodeMap);
                result.add(roleFeatureModifierDataBean);
            }
            return result;
        }
        return null;
    }

    public HkRoleFeatureModifierDocument convertRoleFeatureModifierDataBeanToDocument(RoleFeatureModifierDataBean roleFeatureModifierDataBean) {
        HkRoleFeatureModifierDocument roleFeatureModifierDocument = new HkRoleFeatureModifierDocument();
        if (roleFeatureModifierDataBean != null) {
            roleFeatureModifierDocument.setDesignation(roleFeatureModifierDataBean.getDesignation());
            roleFeatureModifierDocument.setFeature(roleFeatureModifierDataBean.getFeature());
            roleFeatureModifierDocument.setFranchise(hkLoginDataBean.getCompanyId());
            roleFeatureModifierDocument.setIsArchive(Boolean.FALSE);
            roleFeatureModifierDocument.setLastModifiedBy(hkLoginDataBean.getId());
            roleFeatureModifierDocument.setLastModifiedOn(new Date());
            roleFeatureModifierDocument.setiRMediums(roleFeatureModifierDataBean.getiRMediums());
            roleFeatureModifierDocument.setiRModes(roleFeatureModifierDataBean.getiRModes());
            roleFeatureModifierDocument.setiRTypes(roleFeatureModifierDataBean.getiRTypes());
            roleFeatureModifierDocument.setiRVSRAccessRights(roleFeatureModifierDataBean.getiRVSRAccessRights());
            roleFeatureModifierDocument.setAsDesignation(roleFeatureModifierDataBean.getAsDesignation());

            if (roleFeatureModifierDataBean.getId() != null && !StringUtils.isEmpty(roleFeatureModifierDataBean.getId())) {
                roleFeatureModifierDocument.setId(new ObjectId(roleFeatureModifierDataBean.getId()));
            }
        }
        return roleFeatureModifierDocument;
    }

    public RoleFeatureModifierDataBean convertRoleFeatureModifierDocumentToDataBean(HkRoleFeatureModifierDocument roleFeatureModifierDocument) {
        RoleFeatureModifierDataBean roleFeatureModifierDataBean = new RoleFeatureModifierDataBean();
        if (roleFeatureModifierDocument != null) {
            roleFeatureModifierDataBean.setId(roleFeatureModifierDocument.getId().toString());
            roleFeatureModifierDataBean.setDesignation(roleFeatureModifierDocument.getDesignation());
            roleFeatureModifierDataBean.setFeature(roleFeatureModifierDocument.getFeature());
            roleFeatureModifierDataBean.setiRMediums(roleFeatureModifierDocument.getiRMediums());
            roleFeatureModifierDataBean.setiRModes(roleFeatureModifierDocument.getiRModes());
            roleFeatureModifierDataBean.setiRTypes(roleFeatureModifierDocument.getiRTypes());
            roleFeatureModifierDataBean.setiRVSRAccessRights(roleFeatureModifierDocument.getiRVSRAccessRights());
            roleFeatureModifierDataBean.setAsDesignation(roleFeatureModifierDocument.getAsDesignation());
        }
        return roleFeatureModifierDataBean;
    }

    public HkDepartmentConfigDocument convertHkDepartmentConfigDataBeanToHkDepartmentConfigDocument(ConfigurationDataBean configurationDataBean, String operation, Map<Long, HkCriteriaSetDocument> mapOfRuleForDept) {
        HkDepartmentConfigDocument departmentConfigDocument = new HkDepartmentConfigDocument();
        if (configurationDataBean.getConfigId() != null) {
            departmentConfigDocument.setId(new ObjectId(configurationDataBean.getConfigId()));
        }
        departmentConfigDocument.setDepartment(configurationDataBean.getDepartment());
        departmentConfigDocument.setFranchise(hkLoginDataBean.getCompanyId());
        departmentConfigDocument.setStockRoom(configurationDataBean.getStockRoom());
        departmentConfigDocument.setDefaultDepartment(configurationDataBean.getDefaultDepartment());
        departmentConfigDocument.setCreatedBy(hkLoginDataBean.getId());
        departmentConfigDocument.setCreatedOn(new Date());
        departmentConfigDocument.setLastModifiedOn(new Date());
        departmentConfigDocument.setLastModifiedBy(hkLoginDataBean.getId());
        departmentConfigDocument.setIsArchive(Boolean.FALSE);
        departmentConfigDocument.setFlowMode(configurationDataBean.getFlowMode());
        if (configurationDataBean.getStatus() != null) {
            departmentConfigDocument.setStatus(configurationDataBean.getStatus());
        }
        if (configurationDataBean.getDepartmentName() != null) {
            departmentConfigDocument.setDepartmentName(configurationDataBean.getDepartmentName());
        }
        if (configurationDataBean.isNoPhysicalDiamonds() != null && configurationDataBean.isNoPhysicalDiamonds()) {
            departmentConfigDocument.setNoPhysicalDiamonds(Boolean.TRUE);
        } else {
            departmentConfigDocument.setNoPhysicalDiamonds(Boolean.FALSE);
        }

        List<RuleDatabean> ruleList = configurationDataBean.getRuleList();
        List<HkCriteriaSetDocument> criteriaSetDocList = null;
        if (!CollectionUtils.isEmpty(ruleList)) {
            criteriaSetDocList = new ArrayList<>();
//            Long copyMaxCountRule = maxCountOfRule;
            for (RuleDatabean rule : ruleList) {
                HkCriteriaSetDocument criteriaSetDoc = null;
                if (mapOfRuleForDept != null && mapOfRuleForDept.containsKey(rule.getRuleNumber())) {
                    criteriaSetDoc = this.convertRuleDatabeanToRuleDocument(HkSystemConstantUtil.UPDATE_OPERATION, rule, mapOfRuleForDept.get(rule.getRuleNumber()), rule.getRuleNumber());
                } else {
                    maxCountOfRule = maxCountOfRule + 1;
                    criteriaSetDoc = this.convertRuleDatabeanToRuleDocument(HkSystemConstantUtil.CREATE_OPERATION, rule, null, maxCountOfRule);
                }
                criteriaSetDocList.add(criteriaSetDoc);
            }
            if (!CollectionUtils.isEmpty(criteriaSetDocList)) {

                ruleService.saveAllCriteriaSetDocuments(criteriaSetDocList);
                departmentConfigDocument.setCriteriaRuleList(criteriaSetDocList);
            }
//            System.out.println("count reached till" + copyMaxCountRule);
//            configurationDataBean.setMaxCountAchievedForRule(copyMaxCountRule);

        }

//        HkRuleSetDocument ruleSetDocument = convertRuleSetDatabeanToRuleSetDocument(operation, configurationDataBean.getRuleSetDataBean(), null);
//        ruleService.saveOrUpdateRuleSet(ruleSetDocument);
//        System.out.println("ruleset doc def" + ruleSetDocument.getId());
//        departmentConfigDocument.setRuleSet(ruleSetDocument);
        return departmentConfigDocument;
    }

    public HkAssociatedDeptDocument convertAssociatedDepartmentDataBeanToHkAssociatedDeptDocument(AssociatedDepartmentDataBean associatedDepartmentDataBean, HkAssociatedDeptDocument hkAssociatedDeptDocument, String operation) {
        if (hkAssociatedDeptDocument == null) {
            hkAssociatedDeptDocument = new HkAssociatedDeptDocument();
        }
        List<Long> criteriaSetIdsFromServer = null;
        Map<Long, HkCriteriaSetDocument> mapOfCriteriaDocumentsFrmServer = null;
        if (hkAssociatedDeptDocument.getCriteriaSetDocumentList() != null) {
            criteriaSetIdsFromServer = new ArrayList<>();
            List<HkCriteriaSetDocument> criteriaSetListFromServer = hkAssociatedDeptDocument.getCriteriaSetDocumentList();
            if (!CollectionUtils.isEmpty(criteriaSetListFromServer)) {
                mapOfCriteriaDocumentsFrmServer = new HashMap<>();
                for (HkCriteriaSetDocument rule : criteriaSetListFromServer) {
                    criteriaSetIdsFromServer.add(rule.getRuleNumber());
                    mapOfCriteriaDocumentsFrmServer.put(rule.getRuleNumber(), rule);
                }
            }
        }
        hkAssociatedDeptDocument.setDepartment(associatedDepartmentDataBean.getDepartment());
        hkAssociatedDeptDocument.setIsArchive(Boolean.FALSE);
        hkAssociatedDeptDocument.setIsDefaultDept(associatedDepartmentDataBean.getIsDefaultDept());
        hkAssociatedDeptDocument.setMedium(associatedDepartmentDataBean.getMedium());
        hkAssociatedDeptDocument.setLastModifiedOn(new Date());
        hkAssociatedDeptDocument.setLastModifiedBy(hkLoginDataBean.getId());
        List<HkCriteriaSetDocument> criteriaSetDocumentList = null;
        if (!CollectionUtils.isEmpty(associatedDepartmentDataBean.getRules())) {
            criteriaSetDocumentList = new ArrayList<>();
            for (RuleDatabean ruleDatabean : associatedDepartmentDataBean.getRules()) {
                HkCriteriaSetDocument hkRuleDocument = null;
                if (!CollectionUtils.isEmpty(criteriaSetDocumentList) && !CollectionUtils.isEmpty(criteriaSetIdsFromServer) && criteriaSetIdsFromServer.contains(ruleDatabean.getRuleNumber())) {
                    // Update
                    if (mapOfCriteriaDocumentsFrmServer != null && mapOfCriteriaDocumentsFrmServer.containsKey(ruleDatabean.getRuleNumber())) {
                        HkCriteriaSetDocument criteriaSetDocument = mapOfCriteriaDocumentsFrmServer.get(ruleDatabean.getRuleNumber());
                        hkRuleDocument = this.convertRuleDatabeanToRuleDocument(HkSystemConstantUtil.UPDATE_OPERATION, ruleDatabean, criteriaSetDocument, ruleDatabean.getRuleNumber());

                    }
                } else {
                    maxCountOfRule = maxCountOfRule + 1;
                    hkRuleDocument = this.convertRuleDatabeanToRuleDocument(HkSystemConstantUtil.CREATE_OPERATION, ruleDatabean, null, maxCountOfRule);

                }
                criteriaSetDocumentList.add(hkRuleDocument);
            }
            if (!CollectionUtils.isEmpty(criteriaSetDocumentList)) {
                ruleService.saveAllCriteriaSetDocuments(criteriaSetDocumentList);
                hkAssociatedDeptDocument.setCriteriaSetDocumentList(criteriaSetDocumentList);
            }
//            ruleSetDocument.setRules(ruleDocuments);
            hkAssociatedDeptDocument.setCriteriaSetDocumentList(criteriaSetDocumentList);
        }

//        HkRuleSetDocument ruleSetDocument = convertRuleSetDatabeanToRuleSetDocument(operation, associatedDepartmentDataBean.getRuleSet(), ruleSetFromServer);
//        ruleService.saveOrUpdateRuleSet(ruleSetDocument);
//        hkAssociatedDeptDocument.setRuleSet(ruleSetDocument);
        return hkAssociatedDeptDocument;
    }

    public HkAssociatedDesigDocument convertAssociatedDesignationDataBeanToHkAssociatedDesigDocument(AssociatedDesignationDataBean associatedDesignationDataBean, Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldDocument, List<String> fieldPermissionIds, Map<String, HkFeatureFieldPermissionDocument> mapOfPermissionIdWithDocument) {
        HkAssociatedDesigDocument hkAssociatedDesigDocument = new HkAssociatedDesigDocument();
        hkAssociatedDesigDocument.setDesignation(associatedDesignationDataBean.getDesignation());
        hkAssociatedDesigDocument.setIsArchive(Boolean.FALSE);
        hkAssociatedDesigDocument.setLevel(associatedDesignationDataBean.getLevel());
        hkAssociatedDesigDocument.setSkipAssociatedDepartments(associatedDesignationDataBean.getSkipAssociatedDepartments());
        List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans = associatedDesignationDataBean.getFeatureFieldPermissionDataBeans();
        List<HkFeatureFieldPermissionDocument> featureFieldList = null;
        if (!CollectionUtils.isEmpty(featureFieldPermissionDataBeans)) {
            featureFieldList = new ArrayList<>();
            for (FeatureFieldPermissionDataBean featureFieldPermissionDataBean : featureFieldPermissionDataBeans) {
                String dataBeanId = hkLoginDataBean.getCompanyId() + "-" + featureFieldPermissionDataBean.getDesignation() + "-" + featureFieldPermissionDataBean.getFeature() + "-" + featureFieldPermissionDataBean.getSectionCode() + "-" + featureFieldPermissionDataBean.getFieldId();
                HkFeatureFieldPermissionDocument featureFieldPermissionDocument = null;
                HkFeatureFieldPermissionDocument permissionDocumentForUpdateOrCreate = null;

                if (!CollectionUtils.isEmpty(fieldPermissionIds) && fieldPermissionIds.contains(dataBeanId)) {
                    if (mapOfPermissionIdWithDocument.containsKey(dataBeanId)) {
                        permissionDocumentForUpdateOrCreate = mapOfPermissionIdWithDocument.get(dataBeanId);
                    }
                    featureFieldPermissionDocument = this.convertFeatureFieldPermissionDataBeanToHkFeatureFieldPermissions(featureFieldPermissionDataBean, mapOfFieldIdAndHkFieldDocument, HkSystemConstantUtil.UPDATE_OPERATION, permissionDocumentForUpdateOrCreate);

                    if (!featureFieldPermissionDataBean.getEditableFlag() && !featureFieldPermissionDataBean.getParentViewFlag() && !featureFieldPermissionDataBean.getReadonlyFlag() && !featureFieldPermissionDataBean.getSearchFlag()) {
                        // For archiving already existing documents
                        featureFieldPermissionDocument.setIsArchive(Boolean.TRUE);
                    } else {
                        // For updating documents
                        featureFieldPermissionDocument.setIsArchive(Boolean.FALSE);
                    }
                    hkConfigurationService.createOrUpdateHkPermissionDocumentsForDesignation(Arrays.asList(featureFieldPermissionDocument));
                    if (!featureFieldPermissionDocument.isIsArchive()) {
                        featureFieldList.add(featureFieldPermissionDocument);
                    }

                } else {

                    // For create ,of all the create list,add only those which we require
                    if (!featureFieldPermissionDataBean.getEditableFlag() && !featureFieldPermissionDataBean.getParentViewFlag() && !featureFieldPermissionDataBean.getReadonlyFlag() && !featureFieldPermissionDataBean.getSearchFlag()) {
                    } else {
                        featureFieldPermissionDocument = this.convertFeatureFieldPermissionDataBeanToHkFeatureFieldPermissions(featureFieldPermissionDataBean, mapOfFieldIdAndHkFieldDocument, HkSystemConstantUtil.CREATE_OPERATION, permissionDocumentForUpdateOrCreate);
                        featureFieldPermissionDocument.setIsArchive(Boolean.FALSE);
                        hkConfigurationService.createOrUpdateHkPermissionDocumentsForDesignation(Arrays.asList(featureFieldPermissionDocument));
                        featureFieldList.add(featureFieldPermissionDocument);
                    }

                }

            }

            hkAssociatedDesigDocument.setFeatureFieldPermissions(featureFieldList);
        }
        return hkAssociatedDesigDocument;
    }

    public HkFeatureFieldPermissionDocument convertFeatureFieldPermissionDataBeanToHkFeatureFieldPermissions(FeatureFieldPermissionDataBean featureFieldPermissionDataBean, Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldDocument, String operation, HkFeatureFieldPermissionDocument hkFeatureFieldPermissionDocument) {
        if (hkFeatureFieldPermissionDocument == null) {
            hkFeatureFieldPermissionDocument = new HkFeatureFieldPermissionDocument();
        }
        String Id = hkLoginDataBean.getCompanyId() + "-" + featureFieldPermissionDataBean.getDesignation() + "-" + featureFieldPermissionDataBean.getFeature() + "-" + featureFieldPermissionDataBean.getSectionCode() + "-" + featureFieldPermissionDataBean.getFieldId();
        hkFeatureFieldPermissionDocument.setId(Id);
        hkFeatureFieldPermissionDocument.setDesignation(featureFieldPermissionDataBean.getDesignation());
        hkFeatureFieldPermissionDocument.setEditableFlag(featureFieldPermissionDataBean.getEditableFlag());
        if (featureFieldPermissionDataBean.getEntityName() != null) {
            hkFeatureFieldPermissionDocument.setEntityName(featureFieldPermissionDataBean.getEntityName());
        }
        hkFeatureFieldPermissionDocument.setSectionCode(featureFieldPermissionDataBean.getSectionCode());
        hkFeatureFieldPermissionDocument.setFeature(featureFieldPermissionDataBean.getFeature());
        hkFeatureFieldPermissionDocument.setFranchise(hkLoginDataBean.getCompanyId());

        hkFeatureFieldPermissionDocument.setIsRequired(featureFieldPermissionDataBean.getIsRequired());
        hkFeatureFieldPermissionDocument.setParentViewFlag(featureFieldPermissionDataBean.getParentViewFlag());
        hkFeatureFieldPermissionDocument.setReadonlyFlag(featureFieldPermissionDataBean.getReadonlyFlag());
        hkFeatureFieldPermissionDocument.setSearchFlag(featureFieldPermissionDataBean.getSearchFlag());
        hkFeatureFieldPermissionDocument.setLastModifiedBy(hkLoginDataBean.getId());
        hkFeatureFieldPermissionDocument.setLastModifiedOn(new Date());
        if (featureFieldPermissionDataBean.getSequenceNo() != null) {
            hkFeatureFieldPermissionDocument.setSequenceNo(featureFieldPermissionDataBean.getSequenceNo());
        }
        if (mapOfFieldIdAndHkFieldDocument != null && !mapOfFieldIdAndHkFieldDocument.isEmpty() && mapOfFieldIdAndHkFieldDocument.containsKey(featureFieldPermissionDataBean.getFieldId())) {
            HkFieldDocument fieldDocument = mapOfFieldIdAndHkFieldDocument.get(featureFieldPermissionDataBean.getFieldId());
            hkFeatureFieldPermissionDocument.setHkFieldEntity(fieldDocument);
        }
        hkFeatureFieldPermissionDocument.setFieldId(featureFieldPermissionDataBean.getFieldId());
        return hkFeatureFieldPermissionDocument;
    }

//    public HkRuleSetDocument convertRuleSetDatabeanToRuleSetDocument(String operation, RuleSetDataBean ruleSetDatabean, HkRuleSetDocument ruleSetDocument) {
//        if (ruleSetDocument == null) {
//            ruleSetDocument = new HkRuleSetDocument();
//        }
//        if (ruleSetDatabean != null) {
//            System.out.println("Rule Set databean not null");
//            ruleSetDocument.setFranchise(ruleSetDatabean.getFranchise());
//            ruleSetDocument.setIsActive(ruleSetDatabean.getIsActive());
//            ruleSetDocument.setIsArchive(ruleSetDatabean.getIsArchive());
//            ruleSetDocument.setCreatedOn(ruleSetDatabean.getCreatedOn());
//            ruleSetDocument.setCreatedBy(ruleSetDatabean.getCreatedBy());
//            ruleSetDocument.setLastModifiedOn(new Date());
//            ruleSetDocument.setLastModifiedBy(hkLoginDataBean.getId());
//
//            if (!CollectionUtils.isEmpty(ruleSetDatabean.getRules())) {
//                List<HkRuleDocument> ruleDocuments = new LinkedList<>();
//                for (RuleDatabean ruleDatabean : ruleSetDatabean.getRules()) {
//                    HkRuleDocument hkRuleDocument = this.convertRuleDatabeanToRuleDocument(operation, ruleDatabean, new HkRuleDocument());
//                    ruleDocuments.add(hkRuleDocument);
//                }
//                ruleSetDocument.setRules(ruleDocuments);
//            }
//        }
//
//        return ruleSetDocument;
//    }
    public HkCriteriaSetDocument convertRuleDatabeanToRuleDocument(String operation, RuleDatabean ruleDatabean, HkCriteriaSetDocument ruleDocument, Long maxCountForRule) {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = hkFieldService.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap = dropListEntityFromSubFieldMap();
        Map<Long, String> featureMap = userManagementServiceWrapper.retrieveFeatureMap(true);
        if (ruleDocument == null) {
            ruleDocument = new HkCriteriaSetDocument();
        }
        if (hkLoginDataBean != null) {
            if (operation != null && operation.equalsIgnoreCase(HkSystemConstantUtil.CREATE_OPERATION)) {
                ruleDocument.setCreateOn(new Date());
                ruleDocument.setCreatedBy(hkLoginDataBean.getId());
                ruleDocument.setRuleNumber(maxCountForRule);

            } else if (operation != null && operation.equalsIgnoreCase(HkSystemConstantUtil.UPDATE_OPERATION)) {
                ruleDocument.setModifiedOn(new Date());
                ruleDocument.setModifiedBy(hkLoginDataBean.getId());
                ruleDocument.setRuleNumber(ruleDatabean.getRuleNumber());

            } else {
                ruleDocument.setCreateOn(ruleDatabean.getCreateOn());
                ruleDocument.setCreatedBy(ruleDatabean.getCreatedBy());
                ruleDocument.setModifiedBy(ruleDatabean.getModifiedBy());
                ruleDocument.setModifiedOn(ruleDatabean.getModifiedOn());
            }
            if (ruleDatabean.getId() != null) {
                ruleDocument.setId(new ObjectId(ruleDatabean.getId()));
            }
            ruleDocument.setCategory(HkSystemConstantUtil.RuleConfiguration.DEPARTMENT_CATEGORY);
            ruleDocument.setApply(ruleDatabean.getApply());
            ruleDocument.setFranchiseId(hkLoginDataBean.getCompanyId());
            ruleDocument.setIsActive(ruleDatabean.isIsActive());
            ruleDocument.setIsArchive(ruleDatabean.isIsArchive());

            ruleDocument.setRuleName(ruleDatabean.getRuleName());
            ruleDocument.setRemarks(ruleDatabean.getRemarks());
//            System.out.println("Rule DataBean criterias" + ruleDatabean.getCriterias());
            List<RuleCriteriaDataBean> rules = ruleDatabean.getCriterias();
            if (!CollectionUtils.isEmpty(rules)) {
                List<HkRuleCriteriaDocument> dataBeans = new ArrayList<>();
                for (RuleCriteriaDataBean ruleClass : rules) {
                    Long fieldId = ruleClass.getField();
                    if (mapOfFieldIdAndHkFieldEntity.containsKey(fieldId)) {
                        HkFieldEntity fieldEntity = mapOfFieldIdAndHkFieldEntity.get(fieldId);
                        ruleClass.setDbFieldName(fieldEntity.getDbFieldName());
                        ruleClass.setDbFieldType(fieldEntity.getFieldType());
                        // Below code is for pointer handling we are storing pointer Id and pointer component type
                        if (fieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                            String ValidationPattern = fieldEntity.getValidationPattern();
                            String[] validationsArr = ValidationPattern.replace("{", "")
                                    .replace("}", "")
                                    .split(",");
                            String pointerValue = "";
                            for (String validationValue : validationsArr) {
                                if (validationValue.contains("\"pointer\":")) {
                                    String[] pointerArray = validationValue.split(":");
                                    pointerValue = pointerArray[1];
                                }

                                ruleClass.setPointerId(pointerValue.replace("\"", ""));
                            }
                            // Now fetch the pointerEntity from the map
                            Long pointer = new Long(pointerValue.replace("\"", ""));
                            if (pointerValue != null && !pointerValue.isEmpty()) {
                                if (mapOfFieldIdAndHkFieldEntity.containsKey(pointer)) {
                                    HkFieldEntity pointerEntity = mapOfFieldIdAndHkFieldEntity.get(pointer);
                                    ruleClass.setPointerComponentType(pointerEntity.getComponentType());
                                    ruleClass.setDbFieldType(pointerEntity.getFieldType());
                                }
                            }
                        }
                        if (fieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.SUBENTITY)) {
                            if (dropListEntityFromSubFieldMap.containsKey(fieldId)) {
                                HkSubFormFieldEntity subFieldEntity = dropListEntityFromSubFieldMap.get(fieldId);
                                ruleClass.setSubentityComponentType(subFieldEntity.getComponentType());
                            }
                        }
                    }
                    Long entityId = ruleClass.getEntity();
                    if (featureMap.containsKey(entityId)) {

                        ruleClass.setEntityName(featureMap.get(entityId));
                    }
                    Object value = ruleClass.getValue();
                    if (ruleClass.getFieldType().equals(HkSystemConstantUtil.CustomField.ComponentType.DATE)) {
                        TimeZone tz = TimeZone.getTimeZone("UTC");
                        SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                        Date convertDate = null;
                        try {
                            sdf.setTimeZone(tz);
                            convertDate = sdf.parse(ruleClass.getValue().toString());
                            ruleClass.setValue(convertDate);
                        } catch (Exception e) {
                        }

                    }
                    if (ruleClass.getOperator().equals(HkSystemConstantUtil.RULE_OPERATOR.HAS_ANY_VALUE_FROM)) {
                        String[] valueArray = ruleClass.getValue().toString().split(",");
                        ruleClass.setValue(valueArray);
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(ruleClass);
//                    System.out.println("JSON Rules"+json);
//                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                    ObjectMapper m = new ObjectMapper();
//                    HkRuleCriteriaDocument criteriaDatabean = m.convertValue(map, HkRuleCriteriaDocument.class);
                    HkRuleCriteriaDocument criteriaDatabean = new Gson().fromJson(json, HkRuleCriteriaDocument.class);
                    dataBeans.add(criteriaDatabean);
                }
                ruleDocument.setCriterias(dataBeans);
            }
        }

        return ruleDocument;
    }

    public Map<Long, HkSubFormFieldEntity> dropListEntityFromSubFieldMap() {
        Map<Long, HkSubFormFieldEntity> dropListEntityMap = null;
        dropListEntityMap = new HashMap<>();
        List<HkSubFormFieldEntity> listOfSubEntities = hkFieldService.retrieveListOfSubEntitiesAssociatedWithFieldId(null);
        if (!CollectionUtils.isEmpty(listOfSubEntities)) {
            for (HkSubFormFieldEntity listOfSubEntity : listOfSubEntities) {
                if (listOfSubEntity.getIsDroplistField()) {
                    dropListEntityMap.put(listOfSubEntity.getParentField().getId(), listOfSubEntity);
                }
            }
        }
        return dropListEntityMap;
    }

    public ConfigurationDataBean retrieveConfigurationDetailsByDepartmentId(Long deptId) {
        HkDepartmentConfigDocument configDocument = hkConfigurationService.retrieveDocumentByDepartmentId(deptId, hkLoginDataBean.getCompanyId());
        ConfigurationDataBean configDataBean = null;
        if (configDocument != null) {
            configDataBean = new ConfigurationDataBean();
            configDataBean.setDefaultDepartment(configDocument.getDefaultDepartment());
            configDataBean.setDepartment(configDocument.getDepartment());
            configDataBean.setConfigId(configDocument.getId().toString());
            configDataBean.setFlowMode(configDocument.getFlowMode());
            configDataBean.setFranchise(configDocument.getFranchise());
            configDataBean.setStockRoom(configDocument.getStockRoom());
            configDataBean.setStatus(configDocument.getStatus());
            configDataBean.setNoPhysicalDiamonds(configDocument.isNoPhysicalDiamonds());
            List<HkCriteriaSetDocument> criteriaRuleList = configDocument.getCriteriaRuleList();
            List<RuleDatabean> ruleCriteriaList = null;
            if (!CollectionUtils.isEmpty(criteriaRuleList)) {
                ruleCriteriaList = new ArrayList<>();
                for (HkCriteriaSetDocument criteriaRule : criteriaRuleList) {
                    RuleDatabean ruleDataBean = this.convertRuleDocumentToRuleDatabean(null, criteriaRule);
                    ruleCriteriaList.add(ruleDataBean);
                }
                configDataBean.setRuleList(ruleCriteriaList);
            }

//            HkRuleSetDocument ruleSetDocument = configDocument.getRuleSet();
//            if (ruleSetDocument != null) {
//                RuleSetDataBean ruleSetDataBean = convertRuleSetDocumentToRuleSetDataBean(ruleSetDocument, new RuleSetDataBean());
//                configDataBean.setRuleSetDataBean(ruleSetDataBean);
//            }
            List<HkAssociatedDeptDocument> associatedDepartments = configDocument.getAssociatedDepartments();
            List<AssociatedDepartmentDataBean> associatedDepDataBeanList = null;
            if (!CollectionUtils.isEmpty(associatedDepartments)) {
                associatedDepDataBeanList = new ArrayList<>();
                for (HkAssociatedDeptDocument associatedDepartmentDoc : associatedDepartments) {

                    if (!associatedDepartmentDoc.getIsArchive()) {
                        AssociatedDepartmentDataBean associatedDepDataBean = this.convertHkAssociatedDeptDocumentToAssociatedDepartmentDataBean(associatedDepartmentDoc, new AssociatedDepartmentDataBean());
                        associatedDepDataBeanList.add(associatedDepDataBean);
                    }

                }
                configDataBean.setAssociatedDepartments(associatedDepDataBeanList);
            }
//            List<HkAssociatedDesigDocument> associatedDesignations = configDocument.getAssociatedDesignations();
//            List<AssociatedDesignationDataBean> associatedDesignationDataBeans = null;
            List<Long> designationIds = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(associatedDesignations)) {
//                associatedDesignationDataBeans = new ArrayList<>();
//                for (HkAssociatedDesigDocument associatedDesignation : associatedDesignations) {
//                    if (!associatedDesignation.getIsArchive()) {
//                        AssociatedDesignationDataBean associatedDesgDataBean = this.convertAssociatedDesigDocumentToAssociatedDesignationDataBean(associatedDesignation, new AssociatedDesignationDataBean());
//                        associatedDesignationDataBeans.add(associatedDesgDataBean);
//                        designationIds.add(associatedDesgDataBean.getDesignation());
//                    }
//                }
//                configDataBean.setAssociatedDesignations(associatedDesignationDataBeans);
//            }
            List<RoleFeatureModifierDataBean> modifiers = this.retrieveModifiersByDesignations(designationIds);
            configDataBean.setRoleFeatureModifiers(modifiers);
        }
        return configDataBean;
    }

    public AssociatedDesignationDataBean convertAssociatedDesigDocumentToAssociatedDesignationDataBean(HkAssociatedDesigDocument hkAssociatedDesigDocument, AssociatedDesignationDataBean associatedDesignationDataBean) {
        if (associatedDesignationDataBean == null) {
            associatedDesignationDataBean = new AssociatedDesignationDataBean();
        }
        associatedDesignationDataBean.setDesignation(hkAssociatedDesigDocument.getDesignation());
        associatedDesignationDataBean.setIsArchive(hkAssociatedDesigDocument.getIsArchive());
        associatedDesignationDataBean.setLevel(hkAssociatedDesigDocument.getLevel());
        if (!CollectionUtils.isEmpty(hkAssociatedDesigDocument.getSkipAssociatedDepartments())) {
            associatedDesignationDataBean.setSkipAssociatedDepartments(hkAssociatedDesigDocument.getSkipAssociatedDepartments());
        }
        List<HkFeatureFieldPermissionDocument> featureFieldPermissions = hkAssociatedDesigDocument.getFeatureFieldPermissions();
        List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(featureFieldPermissions)) {
            for (HkFeatureFieldPermissionDocument featureFieldPermission : featureFieldPermissions) {
                FeatureFieldPermissionDataBean featurePermsnDataBean = this.convertHkFeatureFieldPermissionDocumentToFeatureFieldPermissionDataBean(featureFieldPermission, null);
                featureFieldPermissionDataBeans.add(featurePermsnDataBean);
            }
            associatedDesignationDataBean.setFeatureFieldPermissionDataBeans(featureFieldPermissionDataBeans);
        }
        return associatedDesignationDataBean;
    }

    public FeatureFieldPermissionDataBean convertHkFeatureFieldPermissionDocumentToFeatureFieldPermissionDataBean(HkFeatureFieldPermissionDocument hkFeatureFieldPermissionDocument, FeatureFieldPermissionDataBean featureFieldPermissionDataBean) {
        if (featureFieldPermissionDataBean == null) {
            featureFieldPermissionDataBean = new FeatureFieldPermissionDataBean();
        }
        featureFieldPermissionDataBean.setDesignation(hkFeatureFieldPermissionDocument.getDesignation());
        featureFieldPermissionDataBean.setEditableFlag(hkFeatureFieldPermissionDocument.getEditableFlag());
        featureFieldPermissionDataBean.setEntityName(hkFeatureFieldPermissionDocument.getEntityName());
        featureFieldPermissionDataBean.setFeature(hkFeatureFieldPermissionDocument.getFeature());
        featureFieldPermissionDataBean.setFranchise(hkFeatureFieldPermissionDocument.getFranchise());
        featureFieldPermissionDataBean.setId(hkFeatureFieldPermissionDocument.getId());
        featureFieldPermissionDataBean.setIsRequired(hkFeatureFieldPermissionDocument.isIsRequired());
        featureFieldPermissionDataBean.setIsArchive(hkFeatureFieldPermissionDocument.isIsArchive());
        featureFieldPermissionDataBean.setParentViewFlag(hkFeatureFieldPermissionDocument.getParentViewFlag());
        featureFieldPermissionDataBean.setReadonlyFlag(hkFeatureFieldPermissionDocument.getReadonlyFlag());
        featureFieldPermissionDataBean.setSearchFlag(hkFeatureFieldPermissionDocument.getSearchFlag());
        featureFieldPermissionDataBean.setSequenceNo(hkFeatureFieldPermissionDocument.getSequenceNo());
        featureFieldPermissionDataBean.setFieldId(hkFeatureFieldPermissionDocument.getFieldId());
        featureFieldPermissionDataBean.setSectionCode(hkFeatureFieldPermissionDocument.getSectionCode());
//        HkFieldDocument fieldDocument = hkFeatureFieldPermissionDocument.getFieldDocument();
//        if (fieldDocument != null) {
//            featureFieldPermissionDataBean.setFieldId(fieldDocument.getId());
//        }
        return featureFieldPermissionDataBean;
    }

    public AssociatedDepartmentDataBean convertHkAssociatedDeptDocumentToAssociatedDepartmentDataBean(HkAssociatedDeptDocument hkAssociatedDeptDocument, AssociatedDepartmentDataBean associatedDepartmentDataBean) {
        if (associatedDepartmentDataBean == null) {
            associatedDepartmentDataBean = new AssociatedDepartmentDataBean();
        }
        associatedDepartmentDataBean.setDepartment(hkAssociatedDeptDocument.getDepartment());
        associatedDepartmentDataBean.setIsDefaultDept(hkAssociatedDeptDocument.getIsDefaultDept());
        associatedDepartmentDataBean.setIsArchive(hkAssociatedDeptDocument.getIsArchive());
        associatedDepartmentDataBean.setMedium(hkAssociatedDeptDocument.getMedium());

        List<HkCriteriaSetDocument> criteriaSetDocumentList = hkAssociatedDeptDocument.getCriteriaSetDocumentList();

        if (!CollectionUtils.isEmpty(criteriaSetDocumentList)) {
            List<RuleDatabean> ruleDatabeans = new LinkedList<>();
            for (HkCriteriaSetDocument hkRuleDocument : criteriaSetDocumentList) {
                RuleDatabean ruleDatabean = convertRuleDocumentToRuleDatabean(new RuleDatabean(), hkRuleDocument);
                ruleDatabeans.add(ruleDatabean);
            }
            associatedDepartmentDataBean.setRules(ruleDatabeans);
        }

//        HkRuleSetDocument ruleSetForAssociatedDep = hkAssociatedDeptDocument.getRuleSet();
//        if (ruleSetForAssociatedDep != null) {
//            RuleSetDataBean ruleSetDataBeanForAssociatedDep = convertRuleSetDocumentToRuleSetDataBean(ruleSetForAssociatedDep, new RuleSetDataBean());
//            associatedDepartmentDataBean.setRuleSet(ruleSetDataBeanForAssociatedDep);
//        }
        return associatedDepartmentDataBean;
    }

//    public RuleSetDataBean convertRuleSetDocumentToRuleSetDataBean(HkRuleSetDocument ruleSetDocument, RuleSetDataBean ruleSetDatabean) {
//        if (ruleSetDatabean == null) {
//            ruleSetDatabean = new RuleSetDataBean();
//        }
//        if (ruleSetDocument != null) {
//            
//            if (ruleSetDocument.getId() != null) {
//                ruleSetDatabean.setId(ruleSetDocument.getId().toString());
//            }
//            ruleSetDatabean.setFranchise(ruleSetDocument.getFranchise());
//            ruleSetDatabean.setIsActive(ruleSetDocument.getIsActive());
//            ruleSetDatabean.setIsArchive(ruleSetDocument.getIsArchive());
//            ruleSetDatabean.setCreatedOn(ruleSetDocument.getCreatedOn());
//            ruleSetDatabean.setCreatedBy(ruleSetDocument.getCreatedBy());
//            ruleSetDatabean.setLastModifiedOn(ruleSetDocument.getLastModifiedOn());
//            ruleSetDatabean.setLastModifiedBy(ruleSetDocument.getLastModifiedBy());
//            
//            if (!CollectionUtils.isEmpty(ruleSetDocument.getRules())) {
//                List<RuleDatabean> ruleDatabeans = new LinkedList<>();
//                for (HkRuleDocument hkRuleDocument : ruleSetDocument.getRules()) {
//                    RuleDatabean ruleDatabean = convertRuleDocumentToRuleDatabean(new RuleDatabean(), hkRuleDocument);
//                    ruleDatabeans.add(ruleDatabean);
//                }
//                ruleSetDatabean.setRules(ruleDatabeans);
//            }
//        }
//        
//        return ruleSetDatabean;
//    }
    public RuleDatabean convertRuleDocumentToRuleDatabean(RuleDatabean ruleDatabean, HkCriteriaSetDocument ruleDocument) {
        if (ruleDatabean == null) {
            ruleDatabean = new RuleDatabean();
        }
        if (ruleDocument != null) {
            ruleDatabean.setApply(ruleDocument.getApply());
            ruleDatabean.setCreateOn(ruleDocument.getCreateOn());
            ruleDatabean.setCreatedBy(ruleDocument.getCreatedBy());
            ruleDatabean.setFranchiseId(ruleDocument.getFranchiseId());
            ruleDatabean.setId(String.valueOf(ruleDocument.getId()));
            ruleDatabean.setIsActive(ruleDocument.isIsActive());
            ruleDatabean.setIsArchive(ruleDocument.isIsArchive());
            ruleDatabean.setModifiedBy(ruleDocument.getModifiedBy());
            ruleDatabean.setModifiedOn(ruleDocument.getModifiedOn());
            ruleDatabean.setRuleName(ruleDocument.getRuleName());
            ruleDatabean.setRuleNumber(ruleDocument.getRuleNumber());
            ruleDatabean.setRemarks(ruleDocument.getRemarks());
            ruleDatabean.setCategory(ruleDocument.getCategory());
            List<HkRuleCriteriaDocument> rules = ruleDocument.getCriterias();
            if (!CollectionUtils.isEmpty(rules)) {
                List<RuleCriteriaDataBean> dataBeans = new ArrayList<>();
                for (HkRuleCriteriaDocument ruleClass : rules) {
                    Gson gson = new Gson();
                    String json = gson.toJson(ruleClass);
//                    Map<String, Object> map = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                    ObjectMapper m = new ObjectMapper();
                    RuleCriteriaDataBean ruleCriteriaDataBean;
                    ruleCriteriaDataBean = new Gson().fromJson(json, RuleCriteriaDataBean.class);
                    dataBeans.add(ruleCriteriaDataBean);
                }
                ruleDatabean.setCriterias(dataBeans);
            }
        }

        return ruleDatabean;
    }

    public List<ConfigurationDataBean> searchDepartmentForConfiguration(String query) {
        List<UMDepartment> departments = userManagementServiceWrapper.retrieveDepartments(hkLoginDataBean.getCompanyId(), Boolean.TRUE);
        Map<Long, String> departmentIdNameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(departments)) {
            for (UMDepartment dep : departments) {
                departmentIdNameMap.put(dep.getId(), dep.getDeptName());
            }
        }
        List<HkDepartmentConfigDocument> departmentConfigDocumentList = hkConfigurationService.searchConfigurationsByDepartmentName(query);
        List<ConfigurationDataBean> configurationDataBeanList = new ArrayList<>();
        List<Long> existingConfigIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(departmentConfigDocumentList)) {

            for (HkDepartmentConfigDocument departmentConfigDocument : departmentConfigDocumentList) {
                ConfigurationDataBean configDataBean = new ConfigurationDataBean();
                configDataBean.setStockRoom(departmentConfigDocument.getStockRoom());
                configDataBean.setStockDeptName(departmentIdNameMap.get(departmentConfigDocument.getStockRoom()));
                configDataBean.setDepartmentName(departmentConfigDocument.getDepartmentName());
                configDataBean.setId(departmentConfigDocument.getDepartment());
                if (departmentConfigDocument.getFlowMode().equals(HkSystemConstantUtil.FLOW_MODE_TYPE_MANUAL)) {
                    configDataBean.setFlowMode("Manual");
                } else {
                    configDataBean.setFlowMode("Automatic");
                }
                if (!CollectionUtils.isEmpty(departmentConfigDocument.getAssociatedDepartments())) {
                    configDataBean.setNumberOfAssociatedDepartments(departmentConfigDocument.getAssociatedDepartments().size());
                }
//                if (!CollectionUtils.isEmpty(departmentConfigDocument.getAssociatedDesignations())) {
//                    configDataBean.setNumberOfAssociatedDesignation(departmentConfigDocument.getAssociatedDesignations().size());
//                }
                configurationDataBeanList.add(configDataBean);
                existingConfigIds.add(departmentConfigDocument.getDepartment());
            }
        }
        if (!CollectionUtils.isEmpty(departments)) {
            for (UMDepartment department : departments) {
                if (!existingConfigIds.contains(department.getId()) && department.getDeptName().toLowerCase().contains(query.toLowerCase())) {
                    ConfigurationDataBean configDataBean = new ConfigurationDataBean();
                    configDataBean.setDepartmentName(department.getDeptName());
                    configDataBean.setId(department.getId());
                    configurationDataBeanList.add(configDataBean);
                }
            }
        }
        return configurationDataBeanList;

    }

//    public Map<Long, List<HkFeatureFieldPermissionDocument>> retrieveMapOfDesignationIdWithListOfpermissionDocument(Long depId) {
//        Map<Long, List<HkFeatureFieldPermissionDocument>> mapOfDesignationIdWithListOfpermissionDocument = hkConfigurationService.retrieveMapOfDesignationIdWithListOfpermissionDocument(depId, hkLoginDataBean.getCompanyId());
//        return mapOfDesignationIdWithListOfpermissionDocument;
//    }
// This method is used when rmeoving a department to check whether it is present in any config document

    public int retrieveCountIfDepartmentExistInAnyDepartmentConfigDocument(Long depId) {
        int countIfDepExist = 0;
        List<HkDepartmentConfigDocument> configDocumentsForRemovingDepartment = hkConfigurationService.retrieveConfigDocumentsForRemovingDepartment();
        if (!CollectionUtils.isEmpty(configDocumentsForRemovingDepartment)) {
            for (HkDepartmentConfigDocument configDoc : configDocumentsForRemovingDepartment) {
                // If department is present in stock room
                if (configDoc.getStockRoom().equals(depId)) {
                    countIfDepExist++;
                }
                // If department is available  as default department for configuration
                if (configDoc.getDefaultDepartment() != null && configDoc.getDefaultDepartment().equals(depId)) {
                    countIfDepExist++;
                }
                // If department is the main element of configuration element
                if (configDoc.getDepartment().equals(depId)) {
                    countIfDepExist++;
                }
                // If department is available as associated Departments
                List<HkAssociatedDeptDocument> associatedDepartmentsList = configDoc.getAssociatedDepartments();
                if (!CollectionUtils.isEmpty(associatedDepartmentsList)) {

                    for (HkAssociatedDeptDocument associatedDepartment : associatedDepartmentsList) {
                        if (associatedDepartment.getDepartment().equals(depId)) {
                            countIfDepExist++;
                        }
                    }
                }
            }
        }
        return countIfDepExist;
    }

    public List<Long> retrieveAlreadyConfiguredDepts() {
        return hkConfigurationService.retrieveAvailableDepartmentConfigurations(hkLoginDataBean.getCompanyId());
    }

    public List<DepartmentDataBean> retrieveDepartmentListInTreeView() {
        List<UMDepartment> uMDepartmentList = userManagementServiceWrapper.retrieveDepartments(hkLoginDataBean.getCompanyId(), Boolean.FALSE);
        List<Long> depIds = this.retrieveAlreadyConfiguredDepts();
        if (!CollectionUtils.isEmpty(depIds)) {

        }
        List<DepartmentDataBean> hkTreeViewDataBeans = new ArrayList<>();
        return convertUMDepartmentToHkTreeViewDataBeanDatabean(uMDepartmentList, hkTreeViewDataBeans, depIds);
    }

    private List<DepartmentDataBean> convertUMDepartmentToHkTreeViewDataBeanDatabean(List<UMDepartment> uMDepartments, List<DepartmentDataBean> hkTreeViewDataBeans, List<Long> depIds) {

        for (UMDepartment uMDepartment : uMDepartments) {
            if (uMDepartment.getIsActive()) {
                DepartmentDataBean departmentDataBean = new DepartmentDataBean();
                if (departmentDataBean.getDepartmentCustom() == null) {
                    departmentDataBean.setDepartmentCustom(new HashMap<String, Object>());
                }
                departmentDataBean.setId(uMDepartment.getId());
                departmentDataBean.setDisplayName(uMDepartment.getDeptName());
                departmentDataBean.setDeptName(uMDepartment.getDeptName());
                departmentDataBean.setIsActive(uMDepartment.getIsActive());
                departmentDataBean.setDescription(uMDepartment.getDescription());
                departmentDataBean.setShiftRotationDays(uMDepartment.getCustom1());
                departmentDataBean.setCompanyId(uMDepartment.getCompany());
                departmentDataBean.setIsDefaultDep(uMDepartment.getCustom4());
                if (depIds.contains(departmentDataBean.getId())) {
                    departmentDataBean.setIsConfigExists(Boolean.TRUE);
                }
                if (uMDepartment.getParent() == null) {
                    departmentDataBean.setParentId(0L);
                    departmentDataBean.setParentName(HkSystemConstantUtil.NONE);
                } else {
                    departmentDataBean.setParentId(uMDepartment.getParent().getId());
                    departmentDataBean.setParentName(uMDepartment.getParent().getDeptName());
                }
                List<DepartmentDataBean> listChild;
                try {
                    if (!CollectionUtils.isEmpty(uMDepartment.getuMDepartmentSet())) {
                        listChild = new ArrayList<>();
                        Set<UMDepartment> l = uMDepartment.getuMDepartmentSet();
                        List<DepartmentDataBean> l1 = convertUMDepartmentToHkTreeViewDataBeanDatabean(new ArrayList<>(l), listChild, depIds);

                        departmentDataBean.setChildren(l1);
                    }
                } catch (Exception e) {
                }
                hkTreeViewDataBeans.add(departmentDataBean);
            }
        }

        return hkTreeViewDataBeans;
    }

    public List<SelectItem> retrieveAssociatedDepts(Long deptId) {
        if (deptId == null) {
            deptId = hkLoginDataBean.getDepartment();
        }
        List<SelectItem> result = new ArrayList<>();
        Map<Long, HkAssociatedDeptDocument> associatedDepts = hkConfigurationService.retrieveMapOfAssociatedDepartmentsForDept(deptId);
        for (Iterator<Map.Entry<Long, HkAssociatedDeptDocument>> it = associatedDepts.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Long, HkAssociatedDeptDocument> entry = it.next();
            if (entry.getValue().getIsArchive()) {
                it.remove();
            }
        }
        HkDepartmentConfigDocument deptConfig = hkConfigurationService.retrieveDocumentByDepartmentId(deptId, hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(associatedDepts)) {
            Set<Long> deptIds = associatedDepts.keySet();
            Map<Long, UMDepartment> depts = userManagementServiceWrapper.retrieveDepartmentMapByIds(new ArrayList<>(deptIds), true);
            if (!CollectionUtils.isEmpty(depts)) {
                for (Map.Entry<Long, UMDepartment> entry : depts.entrySet()) {
                    if (!entry.getKey().equals(hkLoginDataBean.getDepartment())) {
                        SelectItem item = new SelectItem(entry.getKey(), deptConfig.getStockRoom().toString(), entry.getValue().getDeptName());
                        System.out.println("associatedDepts.get(entry.getKey()):::::" + associatedDepts.get(entry.getKey()));
                        item.setCommonId(associatedDepts.get(entry.getKey()).getMedium());
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

}
