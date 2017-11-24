/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureFieldPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RoleFeatureModifierDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.SystemFeatureDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shreya
 */
@Service
public class DesignationConfigTransformerBean {

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
    DesignationTransformerBean designationTransformerBean;

    public List<DepartmentDataBean> retrieveDesignationListInTreeView() {
        List<UMRole> uMRoles = userManagementServiceWrapper.retrieveRolesByCompanyByStatus(0l, null, Boolean.TRUE, false, hkLoginDataBean.getPrecedence());
        Set<Long> deptIds = new HashSet<>();
        Map<UMDepartment, List<UMRole>> mapOfUmRoleDept = new HashMap<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole uMRole : uMRoles) {
                if (uMRole.getCustom1() != null) {
                    deptIds.add(uMRole.getCustom1());
                }
            }
            Map<Long, UMDepartment> deptIdNameMap = userManagementServiceWrapper.retrieveDepartmentMapByIds(new ArrayList<Long>(deptIds), true);
            if (!CollectionUtils.isEmpty(deptIdNameMap)) {
                for (UMRole uMRole : uMRoles) {
                    Long deptId = uMRole.getCustom1();
                    UMDepartment deptObj = deptIdNameMap.get(deptId);
                    List<UMRole> roleList = new ArrayList<>();
                    if (deptObj != null && mapOfUmRoleDept.containsKey(deptObj)) {
                        if (mapOfUmRoleDept.get(deptObj) != null && !mapOfUmRoleDept.get(deptObj).isEmpty()) {
                            roleList = mapOfUmRoleDept.get(deptObj);
                        }
                    }
                    roleList.add(uMRole);
                    mapOfUmRoleDept.put(deptObj, roleList);
                }
            }
        }
        List<DepartmentDataBean> hkTreeViewDataBeans = new ArrayList<>();
        return convertListToHkTreeViewDataBean(mapOfUmRoleDept, hkTreeViewDataBeans, new ArrayList<>(deptIds));
    }

    private List<DepartmentDataBean> convertListToHkTreeViewDataBean(Map<UMDepartment, List<UMRole>> mapOfDeptRoles, List<DepartmentDataBean> hkTreeViewDataBeans, List<Long> depIds) {
        for (Map.Entry<UMDepartment, List<UMRole>> entrySet : mapOfDeptRoles.entrySet()) {
            UMDepartment uMDepartment = entrySet.getKey();
            List<UMRole> roles = entrySet.getValue();
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
            List<DesignationDataBean> designationDataBeans = convertRoleListToHkTreeViewDataBean(roles);
            departmentDataBean.setDesignationDataBeans(designationDataBeans);
            hkTreeViewDataBeans.add(departmentDataBean);
        }
        return hkTreeViewDataBeans;
    }

    private List<DesignationDataBean> convertRoleListToHkTreeViewDataBean(List<UMRole> uMRoles) {
        List<DesignationDataBean> designationDataBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole uMRole : uMRoles) {
                DesignationDataBean designationDataBean = new DesignationDataBean();
                designationTransformerBean.convertDesignationEntityToDataBean(uMRole, designationDataBean, null);
                designationDataBeans.add(designationDataBean);
            }
        }
        return designationDataBeans;
    }

    private FeatureFieldPermissionDataBean convertHkFeatureFieldPermissionDocumentToFeatureFieldPermissionDataBean(HkFeatureFieldPermissionDocument hkFeatureFieldPermissionDocument, FeatureFieldPermissionDataBean featureFieldPermissionDataBean) {
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
        return featureFieldPermissionDataBean;
    }

    public Map<Long, Collection<SystemFeatureDataBean>> retrieveFeaturesSelectedForDesignation(List<Long> desgIds) {
        Map<Long, Collection<SystemFeatureDataBean>> finalMap = null;
        Map<Long, List<UMFeature>> mapOfDesignationWithFeatures = userManagementServiceWrapper.retrieveDiamondFeaturesSelectedForDesignation(desgIds);
        if (mapOfDesignationWithFeatures != null && !mapOfDesignationWithFeatures.isEmpty()) {
            finalMap = new HashMap<>();
            for (Map.Entry<Long, List<UMFeature>> entrySet : mapOfDesignationWithFeatures.entrySet()) {
                List<UMFeature> listOfFeatures = entrySet.getValue();
                if (!CollectionUtils.isEmpty(listOfFeatures)) {
                    List<SystemFeatureDataBean> featuredatabeanList = new ArrayList<>();
                    for (UMFeature uMFeature : listOfFeatures) {
                        if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.EXTRA_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.EXTRA_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.ReportFeatureTypes.MENU_ITEM)) {//||uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU)
                            SystemFeatureDataBean systemFeatureDataBean = designationTransformerBean.convertUMFeatureToSystemFeatureDatabean(uMFeature, new SystemFeatureDataBean());
                            if (uMFeature.getMenuType().equals("RMI") && !uMFeature.getCompany().equals(hkLoginDataBean.getCompanyId())) {
                            } else {
                                systemFeatureDataBean.setIteamAttributesList(new ArrayList<SystemFeatureDataBean>());
                                featuredatabeanList.add(systemFeatureDataBean);
                            }

                        }
                    }
                    finalMap.put(entrySet.getKey(), featuredatabeanList);

                }
            }

        }

        return finalMap;
    }

    public DesignationDataBean retrieveDesignationConfiguration(Long designationId, List<Long> featureIds) {
        UMRole role = userManagementServiceWrapper.retrieveDesignation(designationId);
        List<FeatureFieldPermissionDataBean> fieldPermissionList = new ArrayList<>();
        List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = customFieldService.retrieveFeatureFieldPermissionsByDesignationsFeatureAndSection(Arrays.asList(designationId), featureIds, null, hkLoginDataBean.getCompanyId());
        DesignationDataBean designationDataBean = designationTransformerBean.convertDesignationEntityToDataBean(role, new DesignationDataBean(), null);
        if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
            for (HkFeatureFieldPermissionDocument featureFieldPermissionDocument : featureFieldPermissionDocuments) {
                FeatureFieldPermissionDataBean featureFieldPermissionDataBean = new FeatureFieldPermissionDataBean();
                featureFieldPermissionDataBean = convertHkFeatureFieldPermissionDocumentToFeatureFieldPermissionDataBean(featureFieldPermissionDocument, featureFieldPermissionDataBean);
                fieldPermissionList.add(featureFieldPermissionDataBean);
            }
        }
        List<RoleFeatureModifierDataBean> modifiers = this.retrieveModifiersByDesignations(Arrays.asList(designationId));
        designationDataBean.setRoleFeatureModifiers(modifiers);
        designationDataBean.setFeatureFieldPermissionDataBeans(fieldPermissionList);
        return designationDataBean;
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
                if (!CollectionUtils.isEmpty(roleFeatureModifierDataBean.getShowPlanUsers())) {
                    Set<String> codes = new HashSet<>();
                    for (Map<String, String> map : roleFeatureModifierDataBean.getShowPlanUsers()) {
                        if (map.get("users") != null) {
                            String usersString = map.get("users");
                            List<String> userList = Arrays.asList(usersString.split(","));
                            codes.addAll(userList);
                        }
                    }
                    System.out.println("codes::"+codes.toString());
                    Map<String, String> codeToName = userManagementServiceWrapper.retrieveRecipientNames(new ArrayList<>(codes));
                    roleFeatureModifierDataBean.setRecepientCodeToName(codeToName);
                }
                roleFeatureModifierDataBean.setAsDesignationIdName(recipientIdCodeMap);
                result.add(roleFeatureModifierDataBean);
            }
            return result;
        }
        return null;
    }

    private RoleFeatureModifierDataBean convertRoleFeatureModifierDocumentToDataBean(HkRoleFeatureModifierDocument roleFeatureModifierDocument) {
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
            roleFeatureModifierDataBean.setPlanAccess(roleFeatureModifierDocument.getPlanAccess());
            roleFeatureModifierDataBean.setShowPlanUsers(roleFeatureModifierDocument.getShowPlanUsers());
        }
        return roleFeatureModifierDataBean;
    }

    public void updateDesignationConfiguration(DesignationDataBean designationDataBean) {
        if (designationDataBean != null) {
            Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldDocument = customFieldService.mapOfFieldIdAndHkFieldDocument(hkLoginDataBean.getCompanyId());
            Set<HkFeatureFieldPermissionDocument> finalListToSave = new HashSet<>();
            Map<String, HkFeatureFieldPermissionDocument> mapOfPermissionIdWithDocument = new HashMap<>();
            List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = customFieldService.retrieveFeatureFieldPermissions(null, Arrays.asList(designationDataBean.getId()));
            List<String> featureFieldPermissionIds = null;
            if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
                featureFieldPermissionIds = new ArrayList<>();
                for (HkFeatureFieldPermissionDocument featureFieldPermissionDocument : featureFieldPermissionDocuments) {
                    featureFieldPermissionIds.add(featureFieldPermissionDocument.getId());
                    mapOfPermissionIdWithDocument.put(featureFieldPermissionDocument.getId(), featureFieldPermissionDocument);
                }
            }
            List<FeatureFieldPermissionDataBean> featureFieldPermissionDataBeanList = designationDataBean.getFeatureFieldPermissionDataBeans();
            if (!CollectionUtils.isEmpty(featureFieldPermissionDataBeanList)) {
                for (FeatureFieldPermissionDataBean featureFieldPermissionDataBean : featureFieldPermissionDataBeanList) {
                    String dataBeanId = hkLoginDataBean.getCompanyId() + "-" + featureFieldPermissionDataBean.getDesignation() + "-" + featureFieldPermissionDataBean.getFeature() + "-" + featureFieldPermissionDataBean.getSectionCode() + "-" + featureFieldPermissionDataBean.getFieldId();
                    HkFeatureFieldPermissionDocument featureFieldPermissionDocument = null;
                    HkFeatureFieldPermissionDocument permissionDocumentForUpdateOrCreate = null;

                    if (!CollectionUtils.isEmpty(featureFieldPermissionIds) && featureFieldPermissionIds.contains(dataBeanId)) {
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
                        finalListToSave.add(featureFieldPermissionDocument);
                    } else {
                        // For create ,of all the create list,add only those which we require
                        if (!featureFieldPermissionDataBean.getEditableFlag() && !featureFieldPermissionDataBean.getParentViewFlag() && !featureFieldPermissionDataBean.getReadonlyFlag() && !featureFieldPermissionDataBean.getSearchFlag()) {
                        } else {
                            featureFieldPermissionDocument = this.convertFeatureFieldPermissionDataBeanToHkFeatureFieldPermissions(featureFieldPermissionDataBean, mapOfFieldIdAndHkFieldDocument, HkSystemConstantUtil.CREATE_OPERATION, permissionDocumentForUpdateOrCreate);
                            featureFieldPermissionDocument.setIsArchive(Boolean.FALSE);
                            finalListToSave.add(featureFieldPermissionDocument);
                        }
                    }
                }
                hkConfigurationService.createOrUpdateHkPermissionDocumentsForDesignation(new ArrayList<>(finalListToSave));
                if (!CollectionUtils.isEmpty(designationDataBean.getRoleFeatureModifiers())) {
                    List<HkRoleFeatureModifierDocument> modifierDocuments = new ArrayList<>();
                    for (RoleFeatureModifierDataBean roleFeatureModifierDataBean : designationDataBean.getRoleFeatureModifiers()) {
                        modifierDocuments.add(this.convertRoleFeatureModifierDataBeanToDocument(roleFeatureModifierDataBean));
                    }
                    hkConfigurationService.saveRoleFeatureModifiers(modifierDocuments);
                }
            }

        }
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
            roleFeatureModifierDocument.setPlanAccess(roleFeatureModifierDataBean.getPlanAccess());
            roleFeatureModifierDocument.setShowPlanUsers(roleFeatureModifierDataBean.getShowPlanUsers());

            if (roleFeatureModifierDataBean.getId() != null && !StringUtils.isEmpty(roleFeatureModifierDataBean.getId())) {
                roleFeatureModifierDocument.setId(new ObjectId(roleFeatureModifierDataBean.getId()));
            }
        }
        return roleFeatureModifierDocument;
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

    public List<DesignationDataBean> searchDesignationByName(String searchStr) {
        List<UMRole> uMRoles = userManagementServiceWrapper.searchDesignationByName(searchStr, 0l);
        List<DesignationDataBean> designationDataBeans = new ArrayList<>();
        Set<Long> deptIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole uMRole : uMRoles) {
                if (uMRole.getCustom1() != null) {
                    deptIds.add(uMRole.getCustom1());
                }
            }
            Map<Long, UMDepartment> mapOfDeptIdName = userManagementServiceWrapper.retrieveDepartmentMapByIds(new ArrayList<>(deptIds), true);
            if (!CollectionUtils.isEmpty(mapOfDeptIdName)) {
                for (UMRole uMRole : uMRoles) {
                    if (mapOfDeptIdName.containsKey(uMRole.getCustom1())) {
                        DesignationDataBean designationDataBean = new DesignationDataBean();
                        designationDataBean = designationTransformerBean.convertDesignationEntityToDataBean(uMRole, designationDataBean, null);
                        designationDataBean.setDepartmentName(mapOfDeptIdName.get(uMRole.getCustom1()).getDeptName());
                        designationDataBeans.add(designationDataBean);
                    }

                }
            }

        }
        return designationDataBeans;
    }
}
