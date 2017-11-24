/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;


import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.UrlPatternKeyedMap;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkGoalPermissionEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationConfig;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.GoalPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.StaticServiceFieldPermissionDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.SystemFeatureDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.ApplicationMasterInitializer;
import com.argusoft.usermanagement.common.constants.UMUserManagementConstants;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.database.UMRoleDao;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.argusoft.usermanagement.common.model.UMUserRolePK;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author akta
 */
@Service
public class DesignationTransformerBean {

    @Autowired
    HkFieldService fieldService;
    @Autowired
    UMUserService userService;
    @Autowired
    HkFoundationService foundationService;
    @Autowired
    UMRoleService roleService;
    @Autowired
    UMFeatureService featureService;

    @Autowired
    LocalesTransformerBean localesTransformerBean;
    @Autowired
    FeatureTransformerBean featureTransformerBean;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    ApplicationUtil applicationUtil;

    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HkConfigurationService hkConfigurationService;

    public Map<Long, List<FeatureDataBean>> initRoleFeaturesMap() {
        //  Initialize this map from User Management service
        Map<UMRole, Set<UMFeature>> mapRoleFeature = null;
        try {
            mapRoleFeature = roleService.retrieveRoleFeaturesForMenu();
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(DesignationTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Prepare set of default features, we want to give access....
        List<String> defaultFeatures = Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_HOLIDAY, HkSystemConstantUtil.Feature.MANAGE_TASK, HkSystemConstantUtil.Feature.MANAGE_NOTIFICATION, HkSystemConstantUtil.Feature.APPLY_LEAVE, HkSystemConstantUtil.Feature.MANAGE_MESSAGE, HkSystemConstantUtil.Feature.MANAGE_EVENT);
        List<FeatureDataBean> defaultFeatureDataBeanList = new ArrayList<>();
        List<UMFeature> defaultFeatureList = userManagementServiceWrapper.retrieveFeatureByName(defaultFeatures);

        if (!CollectionUtils.isEmpty(defaultFeatureList)) {
            for (UMFeature uMFeature : defaultFeatureList) {
                FeatureDataBean featureDataBean = buildFeatureDataBeanTree(uMFeature, null, true);
                defaultFeatureDataBeanList.add(featureDataBean);
            }
        }

        Map<Long, List<FeatureDataBean>> mapRoleFeatures = new HashMap<>();
        if (!CollectionUtils.isEmpty(mapRoleFeature)) {
            for (Map.Entry<UMRole, Set<UMFeature>> entry : mapRoleFeature.entrySet()) {

                UMRole role = entry.getKey();
                if (role.getName().equalsIgnoreCase(UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY)) {
                    ApplicationMasterInitializer.anonymousRoleId = role.getId().toString();
                }
                if (role.getName().equalsIgnoreCase(UMUserManagementConstants.IS_AUTHENTICATED_FULLY)) {
                    ApplicationMasterInitializer.fullyAuthenticationRoleId = role.getId().toString();
                }
                //    LOGGER.trace(" ROLE "+role);
                Set<UMFeature> featureSet = entry.getValue();
                List<FeatureDataBean> featureDatabeanList = new ArrayList<>();
                for (UMFeature uMFeature : featureSet) {
                    //  	LOGGER.trace(" feature "+uMFeature);
                    FeatureDataBean featureDataBean = buildFeatureDataBeanTree(uMFeature, null, false);
                    featureDatabeanList.add(featureDataBean);
                }

                if (!role.getName().equalsIgnoreCase(HkSystemConstantUtil.ROLE.SUPER_ADMIN)) {
                    featureDatabeanList.addAll(defaultFeatureDataBeanList);
                }

                if (role.getName().equalsIgnoreCase(HkSystemConstantUtil.ROLE.HK_ADMIN) && role.getPrecedence() == 0) {
                    ApplicationMasterInitializer.hkAdminRole = role.getId();
                }

                mapRoleFeatures.put(role.getId(), featureDatabeanList);
            }
        }

        return mapRoleFeatures;
    }

    private FeatureDataBean buildFeatureDataBeanTree(UMFeature feature, FeatureDataBean featureDataBean, boolean defaultFeature) {
        featureDataBean = featureTransformerBean.convertFeatureToFeatureDatabean(feature, featureDataBean);
        //Commented by Mayank 16-Sep-2014 No need child access while assigning parent features
        if (!defaultFeature) {
            if (!CollectionUtils.isEmpty(feature.getuMFeatureChildSet())) {
                for (UMFeature childFeature : feature.getuMFeatureChildSet()) {
                    FeatureDataBean childFeatureDataBean = buildFeatureDataBeanTree(childFeature, null, defaultFeature);
                    List<FeatureDataBean> children = featureDataBean.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        featureDataBean.setChildren(children);
                    }
                    children.add(childFeatureDataBean);
                }
            }
        }
        return featureDataBean;
    }

    public UrlPatternKeyedMap initMapUrlRoles() {
        UrlPatternKeyedMap mapUrlRoles = null;
        //    Filled it by calling User Management Service
        Map<String, Set<String>> mapUrl = featureService.retrieveFeatureUrlAndRoleIdsMap();
        //    Convert Map to UrlPatternKeyedMap
        if (!CollectionUtils.isEmpty(mapUrl)) {
            mapUrlRoles = new UrlPatternKeyedMap();

            for (Map.Entry<String, Set<String>> entry : mapUrl.entrySet()) {
                String url = entry.getKey();
                Set<String> roleSet = entry.getValue();

                StringBuilder rolesBuilder = new StringBuilder();
                int counter = 1;
                for (String role : roleSet) {
                    if (StringUtils.hasText(ApplicationMasterInitializer.anonymousRoleId) && ApplicationMasterInitializer.anonymousRoleId.equalsIgnoreCase(role)) {
                        role = UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY;
                    } else if (StringUtils.hasText(ApplicationMasterInitializer.fullyAuthenticationRoleId) && ApplicationMasterInitializer.fullyAuthenticationRoleId.equalsIgnoreCase(role)) {
                        role = UMUserManagementConstants.IS_AUTHENTICATED_FULLY + "," + UMUserManagementConstants.IS_AUTHENTICATED_REMEMBERED;
                    }
                    rolesBuilder.append(role.trim());
                    if (counter < roleSet.size()) {
                        rolesBuilder.append(",");
                    }
                    counter++;
                }
                mapUrlRoles.put(url, rolesBuilder.toString());
            }
        }
        return mapUrlRoles;
    }

    public void initRoleAndUser(String roleName, List<String> featureList, int precedence,
            String userId, String password, String firstName, String middleName, String lastName) {
        Map<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put(UMRoleDao.NAME, roleName);
        List<UMRole> roleList = null;
        try {
            roleList = roleService.retrieveRoles(null, criteriaMap, null, null, null);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(WebApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        UMRole roleEntity = null;

        //  if role doesn't exist, create the one
        if (CollectionUtils.isEmpty(roleList)) {
            //  Manage franchise, Manage Designation, Manage Feature
            roleEntity = userManagementServiceWrapper.createRole(roleName, precedence, featureList, 0L);
        } else {
            if (roleList != null) {
                roleEntity = roleList.get(0);
            }
        }

        UMUser userEntity = userService.getUserbyUserName(userId, false, true, false, false, false, true);
        if (roleEntity != null && userEntity != null) {
            Set<UMUserRole> userRoleSet = userEntity.getUMUserRoleSet();
            boolean roleExist = false;
            if (!CollectionUtils.isEmpty(userRoleSet)) {
                for (UMUserRole userRole : userRoleSet) {
                    //  if the found user is superadmin i.e. role id matches superadmin role entity
                    if (userRole.getuMUserRolePK().getRole() == roleEntity.getId()) {
                        if (!userRole.getIsActive() || userRole.getIsArchive()) {
                            userRole.setIsActive(true);
                            userRole.setIsArchive(false);
                            try {
                                userService.updateUserRole(userRole);
                                break;
                            } catch (UMUserManagementException ex) {
                                java.util.logging.Logger.getLogger(WebApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        roleExist = true;
                        break;
                    }
                }
            }
            //  if user exist and role doesn't exist, assign the role to him
            if (!roleExist) {
                UMUserRole userRoleEntity = new UMUserRole(new UMUserRolePK(userEntity.getId(), roleEntity.getId()));
                userRoleEntity.setuMUser(userEntity);
                userRoleEntity.setIsActive(true);
                userRoleEntity.setIsArchive(false);
                try {
                    userService.createUserRole(userRoleEntity);
                } catch (UMUserManagementException ex) {
                    java.util.logging.Logger.getLogger(WebApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            userManagementServiceWrapper.createUser(firstName, middleName, lastName, userId, password, roleEntity);
        }
    }

    public List<DesignationDataBean> retrieveDesignations(boolean showRootDesignations) {
        List<UMRole> listUMRole = userManagementServiceWrapper.retrieveDesignations(loginDataBean.getCompanyId(), loginDataBean.getPrecedence(), showRootDesignations);
        List<DesignationDataBean> listDesignationDataBean = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {

            Collections.sort(listUMRole, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    UMRole role1 = (UMRole) o1;
                    UMRole role2 = (UMRole) o2;
                    return role1.getCreatedOn().compareTo(role2.getCreatedOn());
                }
            });
            if (!CollectionUtils.isEmpty(listUMRole)) {
                for (UMRole role : listUMRole) {
                    listDesignationDataBean.add(convertDesignationEntityToDataBean(role, new DesignationDataBean(), null));
                }
            }
        }
        return listDesignationDataBean;
    }

    public DesignationDataBean retrieveDesignation(long id) {
        UMRole role = userManagementServiceWrapper.retrieveDesignation(id);
//        List<HkFeatureFieldPermissionEntity> staticServiceFieldPermissionList = fieldService.retrieveFeatureFieldPermissionsByDesignation(id);
        DesignationDataBean designationDataBean = convertDesignationEntityToDataBean(role, new DesignationDataBean(), null);
        Set<UMRoleFeature> setRoleFeature = role.getUMRoleFeatureSet();
//        Map<Long, List<StaticServiceFieldPermissionDataBean>> featureFieldMap = new HashMap<>();
//        List<StaticServiceFieldPermissionDataBean> fieldPermissionList = null;
        List<Long> sysfeatureids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(setRoleFeature)) {
            for (UMRoleFeature roleFeature : setRoleFeature) {
                // TODO: add this restriction in wrapper class and remove from here
                if (!roleFeature.getIsArchive()) {
                    sysfeatureids.add(roleFeature.getFeatureId());
//                    if (!CollectionUtils.isEmpty(staticServiceFieldPermissionList)) {
//                        fieldPermissionList = new ArrayList<>();
//                        for (HkFeatureFieldPermissionEntity hkStaticServiceFieldPermissionEntity : staticServiceFieldPermissionList) {
//                            if (roleFeature.getId().equals(hkStaticServiceFieldPermissionEntity.getHkFeatureFieldPermissionEntityPK().getRoleFeature())) {
//                                StaticServiceFieldPermissionDataBean permissionDataBean = new StaticServiceFieldPermissionDataBean();
//                                permissionDataBean = convertStaticServiceFieldPermissionEntityToDatBean(hkStaticServiceFieldPermissionEntity, permissionDataBean);
//                                fieldPermissionList.add(permissionDataBean);
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(fieldPermissionList)) {
//                            featureFieldMap.put(roleFeature.getFeatureId(), fieldPermissionList);
//                        }
//                    }
                }
            }
        }
        
//        designationDataBean.setStaticServicesMap(featureFieldMap);
        designationDataBean.setSysFeatureIdList(sysfeatureids);

        return designationDataBean;
    }

    public void update(DesignationDataBean designationDataBean) throws GenericDatabaseException {
        UMRole dbrole = userManagementServiceWrapper.retrieveDesignation(designationDataBean.getId());
        dbrole = convertDesignationDataBeanToEntity(designationDataBean, dbrole);
        dbrole.setModifiedBy(loginDataBean.getId());
        dbrole.setModifiedOn(new Date());
        List<Long> listSysFeatureIds = designationDataBean.getSysFeatureIdList();
        List<Long> addParentOfMenuItems = addParentOfMenuItems(listSysFeatureIds);
        listSysFeatureIds.addAll(addParentOfMenuItems);
        Set<UMRoleFeature> dbRoleFeatureSet = dbrole.getUMRoleFeatureSet();
        // TODO:this code will be common for all who will update designation with its feature collection so move it to wrapper class
        if (!CollectionUtils.isEmpty(dbRoleFeatureSet)) {
            for (UMRoleFeature roleFeatureLoop : dbRoleFeatureSet) {
                boolean isFound = false;
                for (Long sysFeatureId : listSysFeatureIds) {
                    if (roleFeatureLoop.getFeatureId().equals(sysFeatureId)) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    roleFeatureLoop.setIsArchive(true);
                }
            }
        }
        if (!CollectionUtils.isEmpty(listSysFeatureIds)) {
            for (Long sysFeatureId : listSysFeatureIds) {
                UMRoleFeature roleFeature = null;
                for (UMRoleFeature roleFeatureLoop : dbRoleFeatureSet) {
                    if (sysFeatureId.equals(roleFeatureLoop.getFeatureId())) {
                        roleFeature = roleFeatureLoop;
                        roleFeature.setIsArchive(false);
                        break;
                    }
                }
                if (roleFeature == null) {
                    roleFeature = new UMRoleFeature();
                    roleFeature.setFeature(new UMFeature(sysFeatureId));
                    roleFeature.setRole(dbrole);
                    roleFeature.setAllowToCreate(true);
                    roleFeature.setAllowToDelete(true);
                    roleFeature.setAllowToUpdate(true);
                    roleFeature.setIsActive(true);
                    roleFeature.setIsArchive(false);
                    roleFeature.setCompany(0l);
                    dbRoleFeatureSet.add(roleFeature);
                }
            }
        }
        dbrole.setUMRoleFeatureSet(dbRoleFeatureSet);
        userManagementServiceWrapper.updateDesignation(dbrole, true);
        if (dbrole.getId() != null) {
            List<StaticServiceFieldPermissionDataBean> staticServicePermissions = new ArrayList<>();
            Set<UMRoleFeature> umRoleFeatureSet = dbrole.getUMRoleFeatureSet();
            if (!CollectionUtils.isEmpty(designationDataBean.getStaticServicesMap())) {
                for (Map.Entry<Long, List<StaticServiceFieldPermissionDataBean>> map : designationDataBean.getStaticServicesMap().entrySet()) {
                    if (umRoleFeatureSet != null && !umRoleFeatureSet.isEmpty()) {
                        for (UMRoleFeature uMRoleFeature : umRoleFeatureSet) {
                            if (map.getKey().equals(uMRoleFeature.getFeature().getId())) {
                                List<StaticServiceFieldPermissionDataBean> permList = map.getValue();
                                for (StaticServiceFieldPermissionDataBean staticServiceFieldPermissionDataBean : permList) {
                                    staticServiceFieldPermissionDataBean.setRoleFeature(uMRoleFeature.getId());
                                    staticServiceFieldPermissionDataBean.setDesignation(dbrole.getId());
                                    staticServiceFieldPermissionDataBean.setFeature(uMRoleFeature.getFeature().getId());
                                    if (!staticServiceFieldPermissionDataBean.getEditableFlag() && !staticServiceFieldPermissionDataBean.getParentViewFlag() && !staticServiceFieldPermissionDataBean.getReadonlyFlag() && !staticServiceFieldPermissionDataBean.getSearchFlag()) {
                                    } else {
                                        staticServicePermissions.add(staticServiceFieldPermissionDataBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            if (!CollectionUtils.isEmpty(staticServicePermissions)) {
//                List<HkFeatureFieldPermissionEntity> permissionEntitys = new ArrayList<>();
//                for (StaticServiceFieldPermissionDataBean fieldPermissionDataBean : staticServicePermissions) {
//                    HkFeatureFieldPermissionEntity permissionEntity = new HkFeatureFieldPermissionEntity();
//                    permissionEntity = convertDataBeanToStaticServiceFieldPermissionEntity(permissionEntity, fieldPermissionDataBean);
//                    permissionEntitys.add(permissionEntity);
//                }
//                fieldService.saveFeatureFieldPermissions(permissionEntitys, dbrole.getId(), loginDataBean.getCompanyId());
//            }

        }
        userManagementServiceWrapper.createLocaleForEntity(designationDataBean.getDisplayName(), "Designation", loginDataBean.getId(), loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(designationDataBean.getGoalPermissions())) {
            this.saveGoalPermission(designationDataBean.getGoalPermissions(), Boolean.TRUE, "M");
        } else {
            List<HkGoalPermissionEntity> activePermissions = foundationService.retrieveActiveGoalPermissionByDesignation(dbrole.getId());
            if (!CollectionUtils.isEmpty(activePermissions)) {
                List<Long> ids = new ArrayList<>();
                for (HkGoalPermissionEntity hkGoalPermissionEntity : activePermissions) {
                    if (hkGoalPermissionEntity.getAccessOfFeature().equals("M")) {
                        ids.add(hkGoalPermissionEntity.getId());
                    }
                }
                foundationService.deleteGoalPermissions(ids);
            }
        }
        if (!CollectionUtils.isEmpty(designationDataBean.getGoalSheetPermissions())) {
            this.saveGoalPermission(designationDataBean.getGoalSheetPermissions(), Boolean.TRUE, "GS");
        } else {
            List<HkGoalPermissionEntity> activePermissions = foundationService.retrieveActiveGoalPermissionByDesignation(dbrole.getId());
            if (!CollectionUtils.isEmpty(activePermissions)) {
                List<Long> ids = new ArrayList<>();
                for (HkGoalPermissionEntity hkGoalPermissionEntity : activePermissions) {
                    if (hkGoalPermissionEntity.getAccessOfFeature().equals("GS")) {
                        ids.add(hkGoalPermissionEntity.getId());
                    }
                }
                foundationService.deleteGoalPermissions(ids);
            }
        }
    }

    public void create(DesignationDataBean designationDataBean) throws GenericDatabaseException {
        UMRole role = convertDesignationDataBeanToEntity(designationDataBean, new UMRole());
        role.setCreatedBy(loginDataBean.getId());
        role.setModifiedBy(loginDataBean.getId());
        if (loginDataBean.getPrecedence() < 2) {
            role.setPrecedence(loginDataBean.getPrecedence() + 1);
        } else {
            role.setPrecedence(loginDataBean.getPrecedence());
        }
        role.setIsActive(true);
        role.setIsArchive(false);
        List<Long> listSysFeatureIds = designationDataBean.getSysFeatureIdList();
        List<Long> addParentOfMenuItems = addParentOfMenuItems(listSysFeatureIds);
        listSysFeatureIds.addAll(addParentOfMenuItems);
        Set<UMRoleFeature> setRoleFeature = new HashSet<>();
        if (!CollectionUtils.isEmpty(listSysFeatureIds)) {
            for (Long sysFeatureId : listSysFeatureIds) {
                UMRoleFeature roleFeature = new UMRoleFeature();
                roleFeature.setFeature(new UMFeature(sysFeatureId));
                roleFeature.setRole(role);
                roleFeature.setAllowToCreate(true);
                roleFeature.setAllowToDelete(true);
                roleFeature.setAllowToUpdate(true);
                roleFeature.setIsActive(true);
                roleFeature.setIsArchive(false);
                roleFeature.setCompany(0l);
                setRoleFeature.add(roleFeature);
            }
        }
        role.setUMRoleFeatureSet(setRoleFeature);
        userManagementServiceWrapper.createDesignation(role);
        if (role.getId() != null) {
            List<StaticServiceFieldPermissionDataBean> staticServicePermissions = new ArrayList<>();
            Set<UMRoleFeature> umRoleFeatureSet = role.getUMRoleFeatureSet();
            if (!CollectionUtils.isEmpty(designationDataBean.getStaticServicesMap())) {
                for (Map.Entry<Long, List<StaticServiceFieldPermissionDataBean>> map : designationDataBean.getStaticServicesMap().entrySet()) {
                    if (umRoleFeatureSet != null && !umRoleFeatureSet.isEmpty()) {
                        for (UMRoleFeature uMRoleFeature : umRoleFeatureSet) {
                            if (map.getKey().equals(uMRoleFeature.getFeature().getId())) {
                                List<StaticServiceFieldPermissionDataBean> permList = map.getValue();
                                for (StaticServiceFieldPermissionDataBean staticServiceFieldPermissionDataBean : permList) {
                                    staticServiceFieldPermissionDataBean.setRoleFeature(uMRoleFeature.getId());
                                    staticServiceFieldPermissionDataBean.setDesignation(role.getId());
                                    staticServiceFieldPermissionDataBean.setFeature(uMRoleFeature.getFeature().getId());
                                    if (!staticServiceFieldPermissionDataBean.getEditableFlag() && !staticServiceFieldPermissionDataBean.getParentViewFlag() && !staticServiceFieldPermissionDataBean.getReadonlyFlag() && !staticServiceFieldPermissionDataBean.getSearchFlag()) {
                                    } else {
                                        staticServicePermissions.add(staticServiceFieldPermissionDataBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            if (!CollectionUtils.isEmpty(staticServicePermissions)) {
//                List<HkFeatureFieldPermissionEntity> permissionEntitys = new ArrayList<>();
//                for (StaticServiceFieldPermissionDataBean fieldPermissionDataBean : staticServicePermissions) {
//                    HkFeatureFieldPermissionEntity permissionEntity = new HkFeatureFieldPermissionEntity();
//                    permissionEntity = convertDataBeanToStaticServiceFieldPermissionEntity(permissionEntity, fieldPermissionDataBean);
//                    permissionEntitys.add(permissionEntity);
//                }
//                fieldService.saveFeatureFieldPermissions(permissionEntitys, role.getId(), loginDataBean.getCompanyId());
//            }

        }
        userManagementServiceWrapper.createLocaleForEntity(designationDataBean.getDisplayName(), "Designation", loginDataBean.getId(), loginDataBean.getCompanyId());
        if (designationDataBean.getGoalPermissions() != null) {
            List<GoalPermissionDataBean> goalPermissions = designationDataBean.getGoalPermissions();
            List<GoalPermissionDataBean> result = new ArrayList<>();
            for (GoalPermissionDataBean goalPermissionDataBean : goalPermissions) {
                goalPermissionDataBean.setDesignation(role.getId());
                result.add(goalPermissionDataBean);
            }
            this.saveGoalPermission(result, false, "M");
        }
        if (designationDataBean.getGoalSheetPermissions() != null) {
            List<GoalPermissionDataBean> goalSheetPermissions = designationDataBean.getGoalSheetPermissions();
            List<GoalPermissionDataBean> result = new ArrayList<>();
            for (GoalPermissionDataBean goalPermissionDataBean : goalSheetPermissions) {
                goalPermissionDataBean.setDesignation(role.getId());
                result.add(goalPermissionDataBean);
            }
            this.saveGoalPermission(result, false, "GS");
        }
    }

    public void delete(long id) {
        UMRole role = userManagementServiceWrapper.retrieveDesignation(id);
        // TODO:add check for number of active employees with this designation can be removed if and only if there is not active employees associated with it
        role.setIsArchive(true);
        userManagementServiceWrapper.updateDesignation(role, true);
        DesignationDataBean designationDataBean = this.retrieveDesignation(id);
        userManagementServiceWrapper.deleteLocaleForEntity(designationDataBean.getDisplayName(), "Designation", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
        // Delete associated designations from department configuration document
//        List<HkDepartmentConfigDocument> configDocuments = hkConfigurationService.retrieveAssociatedDesignationsFieldForDocuments();
//        List<HkDepartmentConfigDocument> configDocumentsToBeUpdated = null;
//        if (!CollectionUtils.isEmpty(configDocuments)) {
//            configDocumentsToBeUpdated = new ArrayList<>();
//            for (HkDepartmentConfigDocument configDocument : configDocuments) {
//                if (!CollectionUtils.isEmpty(configDocument.getAssociatedDesignations())) {
//                    List<HkAssociatedDesigDocument> associatedDesignations = configDocument.getAssociatedDesignations();
//                    if (!CollectionUtils.isEmpty(associatedDesignations)) {
//                        for (HkAssociatedDesigDocument associatedDesignation : associatedDesignations) {
//                            if (associatedDesignation.getDesignation().equals(id)) {
//                                associatedDesignation.setIsArchive(Boolean.TRUE);
//                            }
//                        }
//                        configDocumentsToBeUpdated.add(configDocument);
//                    }
//                }
//
//            }
//
//        }
//        if (!CollectionUtils.isEmpty(configDocumentsToBeUpdated)) {
//            hkConfigurationService.updateAllConfigDocuments(configDocumentsToBeUpdated);
//        }
    }

    public List<DesignationDataBean> retrieveAllDesignationBySearch(String name) {
        List<UMRole> uMRoles = userManagementServiceWrapper.searchDesignationByName(name, loginDataBean.getCompanyId());
        Map<Long, Integer> existingUser = userManagementServiceWrapper.retrieveUserCountsByRole(loginDataBean.getCompanyId());
        List<DesignationDataBean> listDesignationDataBean = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMRoles)) {
            for (UMRole role : uMRoles) {
                listDesignationDataBean.add(convertDesignationEntityToDataBean(role, new DesignationDataBean(), existingUser));
            }
        }
        return listDesignationDataBean;
    }

    public int activeUsersCountInDesignation(Long id) {
        int existingUsers = userManagementServiceWrapper.retrieveUserCountByRole(id);
        return existingUsers;

    }

    public boolean isDesignationNameExist(String designationNameToCheck, Long id) {
        List<UMRole> listRole = userManagementServiceWrapper.retrieveDesignations(designationNameToCheck, loginDataBean.getCompanyId());
        if (listRole.size() > 0) {
            if (id != null) {
                return !listRole.get(0).getId().equals(id);
            } else {
                return true;
            }
        } else {
            return false;
        }
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
                            SystemFeatureDataBean systemFeatureDataBean = convertUMFeatureToSystemFeatureDatabean(uMFeature, new SystemFeatureDataBean());
                            if (uMFeature.getMenuType().equals("RMI") && !uMFeature.getCompany().equals(loginDataBean.getCompanyId())) {
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

    public Collection<SystemFeatureDataBean> retrieveAllSystemFeatures() {
        List<UMFeature> listUMFeature = userManagementServiceWrapper.retrieveFeatures(loginDataBean.getPrecedence());
        Map<Long, SystemFeatureDataBean> mapSystemFeatureDatabean = new HashMap<>();
        if (!CollectionUtils.isEmpty(listUMFeature)) {
            for (UMFeature uMFeature : listUMFeature) {
                if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.EXTRA_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.EXTRA_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.ReportFeatureTypes.MENU_ITEM)) {//||uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU)
                    SystemFeatureDataBean systemFeatureDataBean = convertUMFeatureToSystemFeatureDatabean(uMFeature, new SystemFeatureDataBean());
                    if (uMFeature.getMenuType().equals("RMI") && !uMFeature.getCompany().equals(loginDataBean.getCompanyId())) {
                    } else {
                        systemFeatureDataBean.setIteamAttributesList(new ArrayList<SystemFeatureDataBean>());
                        mapSystemFeatureDatabean.put(systemFeatureDataBean.getId(), systemFeatureDataBean);
                    }

                }
            }
//            for (UMFeature uMFeature : listUMFeature) {
//                if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.ITEM_ATTRIBUTE)) {
//                    SystemFeatureDataBean systemFeatureDataBean = convertUMFeatureToSystemFeatureDatabean(uMFeature, new SystemFeatureDataBean());
//                    SystemFeatureDataBean featureDataBean = mapSystemFeatureDatabean.get(uMFeature.getParent().getId());
//                    featureDataBean.getIteamAttributesList().add(systemFeatureDataBean);
//                }
//            }
        }

        return mapSystemFeatureDatabean.values();
    }

    public List<Long> addParentOfMenuItems(List<Long> menuItems) {
        List<Long> menu = new ArrayList<>();
        Map<Long, List<Long>> itemMenuParentMap = getItemMenuParentMap();
        if (!CollectionUtils.isEmpty(menuItems)) {
            for (Long long1 : menuItems) {
                List<Long> parents = itemMenuParentMap.get(long1);
                if (!CollectionUtils.isEmpty(parents)) {
                    for (Long parent : parents) {
                        if (parent != null && !menu.contains(parent)) {
                            menu.add(parent);
                        }
                    }
                }
            }
        }
        return menu;
    }

    public Map<Long, List<Long>> getItemMenuParentMap() {
        List<UMFeature> listUMFeature = userManagementServiceWrapper.retrieveFeatures(loginDataBean.getPrecedence());
        Map<Long, List<Long>> itemMenuParentMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(listUMFeature)) {
            for (UMFeature uMFeature : listUMFeature) {
                if (uMFeature.getMenuType().equals(HkSystemConstantUtil.FeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.MENU_ITEM) || uMFeature.getMenuType().equals(HkSystemConstantUtil.DiamondFeatureTypes.EXTRA_ITEM)) {
                    UMFeature parent = uMFeature.getParent();
                    List<Long> parentList = new LinkedList<>();
                    while (parent != null) {
                        parentList.add(parent.getId());
                        parent = parent.getParent();
                    }
                    if (!CollectionUtils.isEmpty(parentList)) {
                        itemMenuParentMap.put(uMFeature.getId(), parentList);
                    }
                }
            }
        }
        return itemMenuParentMap;
    }

    public DesignationDataBean convertDesignationEntityToDataBean(UMRole role, DesignationDataBean designationDataBean, Map<Long, Integer> existingUsers) {
        if (designationDataBean == null) {
            designationDataBean = new DesignationDataBean();
        }
        if (!CollectionUtils.isEmpty(existingUsers)) {
            Integer get = existingUsers.get(role.getId());
            if (get == null) {
                designationDataBean.setExistingUserCount(0);
            } else {
                designationDataBean.setExistingUserCount(get);
            }
        }
        designationDataBean.setId(role.getId());
        designationDataBean.setDisplayName(role.getName());
        designationDataBean.setPrecedence(role.getPrecedence());
        designationDataBean.setAssociatedDepartment(role.getCustom1());
        if (!StringUtils.isEmpty(role.getCustom2())) {
            designationDataBean.setParentDesignation(Long.parseLong(role.getCustom2()));
        }
        return designationDataBean;
    }

    public UMRole convertDesignationDataBeanToEntity(DesignationDataBean designationDataBean, UMRole role) {
        if (role == null) {
            role = new UMRole();
        }
        role.setId(designationDataBean.getId());
        role.setName(designationDataBean.getDisplayName());
        role.setCompany(0l);
        role.setCustom1(designationDataBean.getAssociatedDepartment());
        if (designationDataBean.getParentDesignation() != null) {
            role.setCustom2(designationDataBean.getParentDesignation().toString());
        } else {
            role.setCustom2(null);
        }
        return role;
    }

    public SystemFeatureDataBean convertUMFeatureToSystemFeatureDatabean(UMFeature feature, SystemFeatureDataBean systemFeatureDataBean) {
        if (systemFeatureDataBean == null) {
            systemFeatureDataBean = new SystemFeatureDataBean();
        }
        systemFeatureDataBean.setId(feature.getId());
        systemFeatureDataBean.setName(feature.getMenuLabel());
        systemFeatureDataBean.setType(feature.getMenuType());
        systemFeatureDataBean.setMenuLabel(feature.getMenuLabel());
        systemFeatureDataBean.setDescription(feature.getDescription());
        return systemFeatureDataBean;
    }

    public List<DesignationDataBean> retrieveDesignationOfFranchise(Long franchiseId, boolean showRootDesignations) {
        List<UMRole> listUMRole = userManagementServiceWrapper.retrieveDesignations(franchiseId, loginDataBean.getPrecedence(), showRootDesignations);
        List<DesignationDataBean> listDesignationDataBean = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {
            for (UMRole role : listUMRole) {
                listDesignationDataBean.add(convertDesignationEntityToDataBean(role, new DesignationDataBean(), null));
            }
        }
        return listDesignationDataBean;
    }

    public Map<Long, List<StaticServiceFieldPermissionDataBean>> retrieveFieldsByFeature() {
        List<UMFeature> features = userManagementServiceWrapper.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY), loginDataBean.getCompanyId(), true, null);
        List<Long> featureIds = new ArrayList<>();
        Map<Long, List<StaticServiceFieldPermissionDataBean>> featureFieldMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(features)) {
            for (UMFeature featureEntity : features) {
                featureIds.add(featureEntity.getId());
            }
        }
        List<HkFieldEntity> fieldEntitys = fieldService.retrieveFieldsByFeatures(featureIds, null, loginDataBean.getCompanyId());
        List<StaticServiceFieldPermissionDataBean> fieldFeaturePermissionDataBeans = null;
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            for (UMFeature featureEntity : features) {
                fieldFeaturePermissionDataBeans = new ArrayList<>();
                for (HkFieldEntity hkFieldEntity : fieldEntitys) {
                    if (featureEntity.getId().equals(hkFieldEntity.getFeature()) && hkFieldEntity.getIsCustomField()) {
                        StaticServiceFieldPermissionDataBean fieldPermissionDataBean = new StaticServiceFieldPermissionDataBean();
                        fieldPermissionDataBean.setDesignation(null);
                        fieldPermissionDataBean.setEditableFlag(false);
                        fieldPermissionDataBean.setEntity(featureEntity.getMenuLabel());
                        fieldPermissionDataBean.setField(hkFieldEntity.getId());
                        fieldPermissionDataBean.setFieldName(hkFieldEntity.getFieldLabel());
                        fieldPermissionDataBean.setParentViewFlag(false);
                        fieldPermissionDataBean.setFeature(featureEntity.getId());
                        fieldPermissionDataBean.setReadonlyFlag(false);
                        fieldPermissionDataBean.setRoleFeature(null);
                        fieldPermissionDataBean.setActualFeatureId(null);
                        fieldPermissionDataBean.setActualFeatureName(null);
                        fieldPermissionDataBean.setSearchFlag(false);
                        fieldPermissionDataBean.setSequenceNo(hkFieldEntity.getSeqNo());
                        fieldPermissionDataBean.setDbFieldName(hkFieldEntity.getDbFieldName());
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map customFieldPatternMap = (new Gson()).fromJson(hkFieldEntity.getValidationPattern(), type);
                        if (customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE) != null) {
                            fieldPermissionDataBean.setIsRequired((Boolean) customFieldPatternMap.get(HkSystemConstantUtil.CustomField.Validation.REQUIRE));
                        } else {
                            fieldPermissionDataBean.setIsRequired(false);
                        }
                        fieldFeaturePermissionDataBeans.add(fieldPermissionDataBean);
                    }
                }
                featureFieldMap.put(featureEntity.getId(), fieldFeaturePermissionDataBeans);
            }
        }
        return featureFieldMap;
    }

    public void saveGoalPermission(List<GoalPermissionDataBean> goalPermissions, Boolean isEditing, String type) {
        if (!CollectionUtils.isEmpty(goalPermissions)) {
            List<HkGoalPermissionEntity> goalPermissionEntitys = new ArrayList<>();
            for (GoalPermissionDataBean goalPermission : goalPermissions) {

                if (goalPermission != null) {
                    String permissions = goalPermission.getReferenceInstance();
                    if (!StringUtils.isEmpty(permissions)) {
                        String referenceType = new String();
                        if (HkSystemConstantUtil.RecipientCodeType.DEPARTMENT.equals(goalPermission.getReferenceType())) {
                            referenceType = HkSystemConstantUtil.RecipientCodeType.DEPARTMENT;
                        } else if (HkSystemConstantUtil.RecipientCodeType.DESIGNATION.equals(goalPermission.getReferenceType())) {
                            referenceType = HkSystemConstantUtil.RecipientCodeType.DESIGNATION;
                        } else if (HkSystemConstantUtil.RecipientCodeType.SERVICE.equals(goalPermission.getReferenceType())) {
                            referenceType = HkSystemConstantUtil.RecipientCodeType.SERVICE;
                        }
                        List<String> listOfIds = Arrays.asList(permissions.split(","));

                        if (!CollectionUtils.isEmpty(listOfIds)) {

                            for (String referenceId : listOfIds) {
                                HkGoalPermissionEntity permissionEntity = new HkGoalPermissionEntity();

                                permissionEntity.setAccessOfFeature(type);
                                permissionEntity.setIsArchive(Boolean.FALSE);
                                permissionEntity.setLastModifiedBy(loginDataBean.getId());
                                permissionEntity.setLastModifiedOn(new Date());
                                permissionEntity.setReferenceInstance(Long.parseLong(referenceId));
                                permissionEntity.setReferenceType(referenceType);
                                permissionEntity.setDesignation(goalPermission.getDesignation());

                                goalPermissionEntitys.add(permissionEntity);
                            }

                        } else {
                            System.out.println("listOfIds is empty");
                        }
                    } else {
                        System.out.println("permission is null....");
                    }
                } else {
                    System.out.println("Goal permission is null....");
                }

            }
            if (!CollectionUtils.isEmpty(goalPermissionEntitys)) {
                if (isEditing) {
                    foundationService.updateGoalPermissions(goalPermissionEntitys, Arrays.asList(type));
                } else {
                    foundationService.addGoalPermissions(goalPermissionEntitys);
                }
            } else {
                System.out.println("No goalpermissions to save");
            }
        } else {

        }
    }

    public List<GoalPermissionDataBean> retrieveActiveGoalPermissionsByDesignation(Long designation, String type) {
        List<GoalPermissionDataBean> permissionDataBeans = new ArrayList<>();
        if (designation != null) {
            List<HkGoalPermissionEntity> activeGoalPermission = foundationService.retrieveActiveGoalPermissionByDesignation(designation);

            if (!CollectionUtils.isEmpty(activeGoalPermission)) {
                for (HkGoalPermissionEntity hkGoalPermissionEntity : activeGoalPermission) {
                    if (hkGoalPermissionEntity.getAccessOfFeature().equalsIgnoreCase(type)) {
                        GoalPermissionDataBean permissionDataBean = new GoalPermissionDataBean();

                        permissionDataBean.setId(hkGoalPermissionEntity.getId());
                        permissionDataBean.setAccessOfFeature(hkGoalPermissionEntity.getAccessOfFeature());
                        permissionDataBean.setDesignation(hkGoalPermissionEntity.getDesignation());
                        permissionDataBean.setReferenceInstance(String.valueOf(hkGoalPermissionEntity.getReferenceInstance()));
                        permissionDataBean.setReferenceType(hkGoalPermissionEntity.getReferenceType());

                        permissionDataBeans.add(permissionDataBean);
                    }
                }
            }
        }
        return permissionDataBeans;
    }

    public Map<String, String> retrieveReportGroupName(List<String> valueIds) {
        Map<String, String> reportIdGroupNameMap = new HashMap<>();
        List<Long> values = new ArrayList<>();
        if (!CollectionUtils.isEmpty(valueIds)) {
            for (String valueId : valueIds) {
                values.add(Long.parseLong(valueId));
            }
            List<HkValueEntity> hkValueEntitys = foundationService.retrieveValueEntityListByValueIds(values);
            if (!CollectionUtils.isEmpty(hkValueEntitys)) {
                for (HkValueEntity hkValueEntity : hkValueEntitys) {
                    String translatedValueName = hkValueEntity.getTranslatedValueName();
                    if (!StringUtils.isEmpty(translatedValueName)) {
                        reportIdGroupNameMap.put(hkValueEntity.getId().toString(), translatedValueName);
                    }
                }
            }
        }
        return reportIdGroupNameMap;
    }

    //Find if circular dependency is present in parent loop.
    public boolean checkCircularDependenciesRecursive(Long designationId, Long parentDesignationId, Map<Long, Long> designationToParentMap) {
        if (parentDesignationId == null) {
            return false;
        } else if (designationId.equals(parentDesignationId)) {
            return true;
        } else {
            return checkCircularDependenciesRecursive(designationId, designationToParentMap.get(parentDesignationId), designationToParentMap);
        }
    }

    public Boolean checkCircularDependencies(Long designationId, Long parentDesignationId) {
        boolean result = false;
        if (parentDesignationId == null || designationId == null) {
        } else {
            List<DesignationDataBean> retrieveDesignations = this.retrieveDesignations(true);
            if (!CollectionUtils.isEmpty(retrieveDesignations)) {
                Map<Long, Long> designationToParentMap = new HashMap<>();
                for (DesignationDataBean designationDataBean : retrieveDesignations) {
                    designationToParentMap.put(designationDataBean.getId(), designationDataBean.getParentDesignation());
                }
                result = this.checkCircularDependenciesRecursive(designationId, parentDesignationId, designationToParentMap);
            }
        }
        return result;
    }
    
    public List<UMRole> retrieveChildRolesByCompany(String role, String parentRole) {
        List<UMRole> roles = userManagementServiceWrapper.retrieveChildRolesByCompany(loginDataBean.getCompanyId(), role, parentRole, Boolean.TRUE, false);
        return roles;
    }
}
