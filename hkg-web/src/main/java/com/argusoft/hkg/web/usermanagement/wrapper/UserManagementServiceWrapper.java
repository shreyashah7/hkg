/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.wrapper;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.common.GenericDao.QueryOperators;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkUserTypeSequenceEntity;
import com.argusoft.hkg.model.HkUserTypeSequenceEntityPK;
import com.argusoft.hkg.model.HkUserWorkHistoryEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.web.config.WebApplicationConfig;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.internationalization.LabelType;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.argusoft.usermanagement.common.constants.UMUserManagementConstants;
import com.argusoft.usermanagement.common.core.UMCompanyService;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.core.UMGroupService;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.core.serviceimpl.UMDepartmentServiceImpl;
import com.argusoft.usermanagement.common.database.UMCompanyDao;
import com.argusoft.usermanagement.common.database.UMDepartmentDao;
import com.argusoft.usermanagement.common.database.UMFeatureDao;
import com.argusoft.usermanagement.common.database.UMGroupContactDao;
import com.argusoft.usermanagement.common.database.UMRoleDao;
import com.argusoft.usermanagement.common.database.UMRoleFeatureDao;
import com.argusoft.usermanagement.common.database.UMUserDao;
import com.argusoft.usermanagement.common.database.UMUserGroupDao;
import com.argusoft.usermanagement.common.database.UMUserRoleDao;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMGroupContact;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserContact;
import com.argusoft.usermanagement.common.model.UMUserGroup;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.argusoft.usermanagement.common.model.UMUserRolePK;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;
import org.bson.BasicBSONObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author Tejas
 */
@Service
@Transactional
public class UserManagementServiceWrapper {

    @Autowired
    UMDepartmentService departmentService;
    @Autowired
    UMRoleService roleService;
    @Autowired
    UMFeatureService featureService;
    @Autowired
    UMUserService userService;
    @Autowired
    UMGroupService groupService;
    @Autowired
    UMCompanyService companyService;
    @Autowired
    HkFoundationService operationsService;
    @Autowired
    HkHRService hrService;
    @Autowired
    CommonDAO commonDao;
    @Autowired
    HkNotificationService notificationService;
    @Autowired
    BasicPasswordEncryptor basicPasswordEncryptor;
    @Autowired
    I18nService i18nService;
    @Autowired
    HkCustomFieldService customFieldService;
    @Autowired
    HkFieldService fieldService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String IS_ARCHIVE = "isArchive";
    public static final String COMPANY = "company";
    public static final String IS_ACTIVE = "isActive";
    public static final String UIFIELDNAME = "uiFieldName";
    public static final String STATUS = "status";

    public static class UMFeatureDetail {

        private static final String MENU_LABEL = "menuLabel";
        private static final String MENU_TYPE = "menuType";
        public static final String ID = "id";
        public static final String FEATURE_NAME = "name";

    }

    public static class UMRoleFeatureDetail {

        private static final String ROLE = "roleId";
    }

    public static class HkUserTypeSequenceField {

        public static final String USER_TYPE = "hkUserTypeSequenceEntityPK.userType";
        public static final String FRANCHISE = "hkUserTypeSequenceEntityPK.franchise";
    }

    public static class HkUserWorkHistoryField {

        public static final String USER_ID = "userId";
    }

    public List<UMDepartment> retrieveDepartments(long companyId, Boolean isChildrenRequired) {
        return departmentService.retrieveDepartmentList(companyId, Boolean.TRUE, Boolean.FALSE, isChildrenRequired);
    }

    public List<Long> retrieveDepartmentIdTree(long departmentId) {
        List<Long> departmentIds = new LinkedList<>();

        Map<QueryOperators, Object> criterias = new HashMap<>();
        Map<String, Object> equalMap = new HashMap<>();
        equalMap.put(UMDepartmentDao.IS_ACTIVE, true);
        equalMap.put(UMDepartmentDao.IS_ARCHIVE, false);
        equalMap.put(UMDepartmentDao.ID, departmentId);

        criterias.put(QueryOperators.EQUAL, equalMap);

        List<String> requires = new ArrayList<>();
        requires.add(UMDepartmentDao.PARENT);
        requires.add(UMDepartmentDao.UM_DEPARTMENT_SET);

        try {
            List<UMDepartment> departments = departmentService.retrieveDepartments(null, criterias, requires);
            if (!CollectionUtils.isEmpty(departments)) {
                for (UMDepartment uMDepartment : departments) {
                    departmentIds.add(uMDepartment.getId());
                    departmentIds = recursiveDeparmentData(uMDepartment, departmentIds);
                }
            }
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(UMDepartmentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departmentIds;
    }

    private List<Long> recursiveDeparmentData(UMDepartment uMDepartment, List<Long> departmentIds) {
        if (!CollectionUtils.isEmpty(uMDepartment.getuMDepartmentSet())) {
            for (UMDepartment childDepartment : uMDepartment.getuMDepartmentSet()) {
                departmentIds.add(childDepartment.getId());
                recursiveDeparmentData(childDepartment, departmentIds);
            }
        }
        return departmentIds;
    }

    public List<UMDepartment> retrieveDepartmentsIncludingRemoved(long companyId) {
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        Map<String, Object> equalMap = new HashMap<>();

        equalMap.put(UMDepartmentDao.IS_ARCHIVE, Boolean.FALSE);
        equalMap.put(UMDepartmentDao.COMPANY, getCompnies(companyId));

        criterias.put(GenericDao.QueryOperators.EQUAL, equalMap);

        try {
            return departmentService.retrieveDepartments(null, criterias, null);
        } catch (GenericDatabaseException ex) {

        }
        return null;
    }

    public UMDepartment retrieveDepartment(Long departmentId) {
        try {
            return departmentService.retrieveDepartmentById(departmentId, true, true, true);
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Could not retrieve departments", ex);
        }
        return null;
    }

    public List<UMDepartment> retrieveDepartment(String departmentName, long companyId) {

        Map<String, Object> queryEquals = new HashMap<>();

        queryEquals.put(UMDepartmentDao.DEPT_NAME, departmentName);
        queryEquals.put(UMDepartmentDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMDepartmentDao.COMPANY, getCompnies(companyId));
        List<UMDepartment> departments;
        try {
            departments = departmentService.retrieveDepartments(null, queryEquals, null, null, null);
            if (departments != null && !departments.isEmpty()) {
                return departments;
            }
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Error retriving departments", ex);
        }
        return null;
    }

    public List<UMDepartment> retrieveDefaultDepartment(Long companyId) {
        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMDepartmentDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMDepartmentDao.COMPANY, getCompnies(companyId));
        queryEquals.put(UMDepartmentDao.DEFAULT_DEP, Boolean.TRUE);
        List<UMDepartment> departments;
        try {
            departments = departmentService.retrieveDepartments(null, queryEquals, null, null, null);
            if (departments != null && !departments.isEmpty()) {
                return departments;
            }
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Error retriving departments", ex);
        }
        return null;
    }

    public boolean isDepartmentNameExist(String departmentName, Long id, Long companyId) throws GenericDatabaseException {

        Map<GenericDao.QueryOperators, Object> map = new HashMap<>();
        Map<String, Object> equalMap = new HashMap<>();
        equalMap.put(UMDepartmentDao.IS_ACTIVE, Boolean.TRUE);
        equalMap.put(UMDepartmentDao.DEPT_NAME, departmentName);
        equalMap.put(UMDepartmentDao.COMPANY, getCompnies(companyId));
        if (id != null) {
            Map<String, Object> notEqualMap = new HashMap<>();
            notEqualMap.put(UMDepartmentDao.ID, id);
            map.put(GenericDao.QueryOperators.NOT_EQUAL, notEqualMap);
        }
        map.put(GenericDao.QueryOperators.EQUAL, equalMap);
        boolean departmentExits = departmentService.isDepartmentExits(map);
        return departmentExits;
    }

    public List<UMDepartment> searchDepartmentByName(String departmentName, long companyId) {

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMDepartmentDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMDepartmentDao.COMPANY, getCompnies(companyId));

        Map<String, Object> queryLike = new HashMap<>();
        queryLike.put(UMDepartmentDao.DEPT_NAME, departmentName);

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();

        criterias.put(GenericDao.QueryOperators.EQUAL, queryEquals);
        criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, queryLike);

        try {
            return departmentService.retrieveDepartments(null, criterias, null);
        } catch (GenericDatabaseException ex) {

        }
        return null;
    }

    public List<UMCompany> searchCompanyByName(String companyName) {

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMCompanyDao.IS_ACTIVE, Boolean.TRUE);

        Map<String, Object> queryLike = new HashMap<>();
        if (companyName != null) {
            queryLike.put(UMCompanyDao.NAME, companyName);
        }

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();

        criterias.put(GenericDao.QueryOperators.EQUAL, queryEquals);
        criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, queryLike);

        try {
            return companyService.retrieveCompanies(null, criterias, null);
        } catch (GenericDatabaseException ex) {

        }
        return null;
    }

    public List<UMCompany> retrieveAllCompany() {

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMCompanyDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMCompanyDao.IS_ARCHIVE, Boolean.FALSE);

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();

        criterias.put(GenericDao.QueryOperators.EQUAL, queryEquals);

        try {
            return companyService.retrieveCompanies(null, criterias, null);
        } catch (GenericDatabaseException ex) {

        }
        return null;
    }

    public boolean removeDepartment(long departmentId) {
        try {
            departmentService.deleteDepartmentById(departmentId);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /*
     * DESIGNATION
     */
    public List<UMRole> retrieveDesignations(Long companyId, Integer precedence, boolean showRootDesignations) {
//        System.out.println("Company Id " + companyId);
//        System.out.println("Precedence " + precedence);
//        System.out.println("Root Desig " + showRootDesignations);
        List<UMRole> userRoles = null;

        Map<QueryOperators, Object> criteriasMap = new HashMap<>();

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);

        Map<String, Object> queryIn = new HashMap<>();
//        if (showRootDesignations) {
        queryIn.put(UMRoleDao.COMPANY, getCompnies(companyId));
//        } else {
//            queryIn.put(UMRoleDao.COMPANY, Arrays.asList(companyId));
//        }

        if (precedence != null) {
            Map<String, Object> queryGreaterOrEqual = new HashMap<>();
            queryGreaterOrEqual.put(UMRoleDao.PRECEDENCE, precedence);
            criteriasMap.put(QueryOperators.GREATER_THAN_EQUAL_TO, queryGreaterOrEqual);
        }

        criteriasMap.put(QueryOperators.EQUAL, queryEquals);
        criteriasMap.put(QueryOperators.IN, queryIn);
        criteriasMap.put(QueryOperators.ORDER_ASC, UMRoleDao.CREATED_ON);

        try {
            userRoles = roleService.retrieveRoles(null, criteriasMap, null);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }

        return userRoles;
    }

    public List<UMRole> retrieveDesignationsByDepartment(Long depId) {
        List<UMRole> userRoles = null;

        Map<QueryOperators, Object> criteriasMap = new HashMap<>();

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMRoleDao.COMPANY, 0l);
        queryEquals.put(UMRoleDao.CUSTOM1, depId);
        criteriasMap.put(QueryOperators.EQUAL, queryEquals);
        criteriasMap.put(QueryOperators.ORDER_ASC, UMRoleDao.CREATED_ON);
        try {
            userRoles = roleService.retrieveRoles(null, criteriasMap, null);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }

        return userRoles;
    }

    public List<UMRole> retrieveDesignationsByDepartmentIds(List<Long> depIds) {
        List<UMRole> userRoles = null;

        Map<QueryOperators, Object> criteriasMap = new HashMap<>();

        Map<String, Object> queryEquals = new HashMap<>();
        Map<String, Object> queryIns = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMRoleDao.COMPANY, 0l);
        queryIns.put(UMRoleDao.CUSTOM1, depIds);
        criteriasMap.put(QueryOperators.EQUAL, queryEquals);
        criteriasMap.put(QueryOperators.IN, queryIns);
        criteriasMap.put(QueryOperators.ORDER_ASC, UMRoleDao.CREATED_ON);
        try {
            userRoles = roleService.retrieveRoles(null, criteriasMap, null);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }

        return userRoles;
    }

    public List<UMRole> searchDesignationByName(String searchStr, long companyId) {
        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMRoleDao.COMPANY, getCompnies(companyId));

        Map<String, Object> queryLike = new HashMap<>();
        queryLike.put(UMRoleDao.NAME, searchStr);

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        boolean isDepartment = searchStr.toUpperCase().startsWith(HkSystemConstantUtil.DEPARTMENT_CONFIG_SEARCH_CODE.DEPARTMENT.toUpperCase());

        Criteria criteria = commonDao.getCurrentSession().createCriteria(UMDepartment.class);
        criteria.add(Restrictions.eq(IS_ARCHIVE, false));
        if (isDepartment) {
            searchStr = searchStr.substring(searchStr.toUpperCase().indexOf(HkSystemConstantUtil.DEPARTMENT_CONFIG_SEARCH_CODE.DEPARTMENT) + HkSystemConstantUtil.DEPARTMENT_CONFIG_SEARCH_CODE.DEPARTMENT.length()).trim();
            Map<Long, String> map = this.searchDepartments(searchStr, companyId);
            if (!CollectionUtils.isEmpty(map)) {
                List<Long> deptIds = new ArrayList<>();
                for (Map.Entry<Long, String> entrySet : map.entrySet()) {
                    Long deptId = entrySet.getKey();
                    deptIds.add(deptId);
                }
                criterias.put(GenericDao.QueryOperators.EQUAL, deptIds);
            }
        } else {
            criterias.put(GenericDao.QueryOperators.EQUAL, queryEquals);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, queryLike);
        }
        try {
            return roleService.retrieveRoles(null, criterias, null);
        } catch (GenericDatabaseException ex) {

        }
        return null;
    }

    public List<UMRole> retrieveDesignations(String companyName, long companyId) {
        List<UMRole> userRoles = null;
        Map<QueryOperators, Object> queryEquals = new HashMap<>();

        Map<String, String> equals = new HashMap();
        equals.put(UMRoleDao.NAME, companyName);

        queryEquals.put(QueryOperators.IGNORE_CASE_LIKE_START_MATCH, equals);
        queryEquals.put(QueryOperators.IGNORE_CASE_LIKE_END_MATCH, equals);
        Map<String, Object> notArchieved = new HashMap<>();
        notArchieved.put(UMRoleDao.IS_ARCHIVE, false);
        Map<String, Object> company = new HashMap<>();
        company.put(UMRoleDao.COMPANY, getCompnies(companyId));
        queryEquals.put(QueryOperators.EQUAL, notArchieved);
        queryEquals.put(QueryOperators.IN, company);

        try {
            userRoles = roleService.retrieveRoles(null, queryEquals, null);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }
        return userRoles;
    }

    public UMRole retrieveDesignation(long id) {
        List<String> projectionList = new ArrayList<String>();
        projectionList.add(UMRoleDao.UM_ROLE_FEATURE_SET);

        UMRole userRole = null;
        Map<String, Object> queryEquals = new HashMap<>();

        queryEquals.put(UMRoleDao.ID, id);
        queryEquals.put(UMRoleDao.UM_ROLE_FEATURE_SET + "." + UMRoleDao.IS_ARCHIVE, Boolean.TRUE);
        try {
            userRole = roleService.retrieveRoleById(id, projectionList);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }

        return userRole;
    }

    public List<UMRole> retrieveDesignationByIds(List<Long> ids) {
        List<UMRole> userRoles = null;
        Map<String, Object> queryIn = new HashMap<>();

        queryIn.put(UMRoleDao.ID, ids);

        Map<QueryOperators, Object> criteria = new HashMap<>();
        criteria.put(QueryOperators.IN, queryIn);

        List<String> requires = null;
        requires = new ArrayList<>();
        requires.add(UMRoleDao.UM_ROLE_FEATURE_SET);

        try {
            return roleService.retrieveRoles(null, criteria, requires);
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }

        return userRoles;
    }

    public Map<Long, Integer> retrieveDesignationPrecedence() {
        Search search = SearchFactory.getSearch(UMRole.class);
        //search.addField(UMRoleDao.PRECEDENCE);
        Map<Long, Integer> map = new HashMap<>();
        List<UMRole> roles = commonDao.search(search);
        for (UMRole umRole : roles) {
            map.put(umRole.getId(), umRole.getPrecedence());
        }
        return map;
    }

    public void updateDesignation(UMRole designation, boolean sendNotification) {
        roleService.updateRole(designation);

        if (sendNotification) {
            //  Send notification to users of this designation
            //  FS: To users of desgination, To users with desgination management rights
            //  Get the feature specific users
            Set<Long> notifyUserSet = new LinkedHashSet<>();
            //  Get the users of this role
            Set<Long> roleUserSet = this.retrieveRecipientIds(Arrays.asList(designation.getId() + ":" + HkSystemConstantUtil.RecipientCodeType.DESIGNATION));
            if (!CollectionUtils.isEmpty(roleUserSet)) {
                notifyUserSet.addAll(roleUserSet);
            }
            //  Send notification
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put("EDIT", "Your desgination has been updated please save all your work and refresh the page");
            HkNotificationEntity notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.DESIGNATION,
                    HkSystemConstantUtil.NotificationInstanceType.UPDATE_DESIGNATION_SELF, valuesMap, designation.getId(), designation.getCompany());
            notificationService.sendNotification(notification, notifyUserSet);

            //Commented by MM : 01-11-2014 Bug #2006
            Set<Long> desigRightsUserSet = this.searchUsersByFeatureName(Arrays.asList("designationAdd", "designationEdit"), designation.getCompany());
            if (!CollectionUtils.isEmpty(desigRightsUserSet)) {
                desigRightsUserSet.removeAll(notifyUserSet);
            }

            valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DESIGNATION_NAME, designation.getName());
            notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.DESIGNATION,
                    HkSystemConstantUtil.NotificationInstanceType.UPDATE_DESIGNATION, valuesMap, designation.getId(), designation.getCompany());
            notificationService.sendNotification(notification, desigRightsUserSet);
        }
    }

    public void createDesignation(UMRole designation) {
        roleService.createRole(designation);
    }

    private List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null) {
            companyIds.add(companyId);
        }
        return companyIds;
    }

    public void removeRoleFeatures(List<Long> featureIds) {
        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);
        queryEquals.put(UMRoleDao.IS_ARCHIVE, Boolean.FALSE);

        Map<String, Object> queryIn = new HashMap<>();
        queryIn.put(UMRoleFeatureDao.FEATURE_ID, featureIds);

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();

        criterias.put(GenericDao.QueryOperators.EQUAL, queryEquals);
        criterias.put(GenericDao.QueryOperators.IN, queryIn);

        try {
            List<UMRoleFeature> roleFeatures = roleService.retrieveRoleFeatures(null, criterias, null);
            for (UMRoleFeature uMRoleFeature : roleFeatures) {
                uMRoleFeature.setIsArchive(true);
                uMRoleFeature.setIsActive(false);
            }

            roleService.updateAllRoleFeature(roleFeatures);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<Long, List<UMFeature>> retrieveDiamondFeaturesSelectedForDesignation(List<Long> desgIds) {
        Search search = SearchFactory.getSearch(UMRoleFeature.class);
        search.addFilterIn(UMRoleFeatureDetail.ROLE, desgIds);
        Map<Long, List<Long>> designationWithFeatureIds = null;
        List<UMRoleFeature> roles = commonDao.search(search);
        List<Long> totalFeatureIds = null;
        Map<Long, List<UMFeature>> mapOfRoleWithFeatureList = null;
        if (!CollectionUtils.isEmpty(roles)) {
            designationWithFeatureIds = new HashMap<>();
            totalFeatureIds = new ArrayList<>();
            for (UMRoleFeature role : roles) {
                List<Long> featureIds = null;
                if (designationWithFeatureIds.containsKey(role.getRole().getId())) {
                    featureIds = designationWithFeatureIds.get(role.getRole().getId());
                } else {
                    featureIds = new ArrayList<>();
                }
                featureIds.add(role.getFeatureId());
                if (totalFeatureIds.contains(role.getFeatureId())) {
                } else {
                    totalFeatureIds.add(role.getFeatureId());
                }
                designationWithFeatureIds.put(role.getRole().getId(), featureIds);
            }
            if (!CollectionUtils.isEmpty(totalFeatureIds)) {
                Map<Long, UMFeature> mapOfFeatureIdWithFeatures = this.retrieveFeaturesByFeatureIds(totalFeatureIds, null);
                if (designationWithFeatureIds != null && !designationWithFeatureIds.isEmpty()) {
                    mapOfRoleWithFeatureList = new HashMap<>();
                    for (Map.Entry<Long, List<Long>> roleMap : designationWithFeatureIds.entrySet()) {
                        Long keyOfRoleMap = roleMap.getKey();
                        List<Long> featureIdsWithRole = roleMap.getValue();
                        if (!CollectionUtils.isEmpty(featureIdsWithRole)) {
                            List<UMFeature> featureEntity = new ArrayList<>();
                            for (Long featureId : featureIdsWithRole) {
                                if (mapOfFeatureIdWithFeatures != null && !mapOfFeatureIdWithFeatures.isEmpty() && mapOfFeatureIdWithFeatures.containsKey(featureId)) {

                                    UMFeature umFeature = mapOfFeatureIdWithFeatures.get(featureId);
                                    featureEntity.add(umFeature);
                                }

                            }
                            mapOfRoleWithFeatureList.put(keyOfRoleMap, featureEntity);
                        }

                    }
                }
            }

        }

        return mapOfRoleWithFeatureList;
    }

    public List<UMFeature> retrieveFeatures(Integer precedence) {
        List<UMFeature> featureList = featureService.retrieveAllFeatures(Boolean.TRUE);
        if (!CollectionUtils.isEmpty(featureList) && precedence != null) {
            List<UMFeature> selectedFeatures = new LinkedList<>();
            for (UMFeature uMFeature : featureList) {
                if (uMFeature.getPrecedence() >= precedence) {
                    selectedFeatures.add(uMFeature);
                }
            }
            featureList = selectedFeatures;
        }
        return featureList;
    }

    /**
     * For messaging feature
     * <br/>Retrieves users by companyId and status
     * <br/>Searches for match anywhere in first-name/last-name
     * <br/>Used Projections
     * <br/>isLoggedInUserRequired->pass false if loggedIn user is not required
     */
    public List<UMUser> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserDao.COMPANY, companyId);
            equal.put(UMUserDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMUserDao.FIRST_NAME);
            projections.add(UMUserDao.LAST_NAME);
            projections.add(UMUserDao.USER_CODE);
            projections.add(UMUserDao.ID);
            List<String> match = new ArrayList<>();
            match.add(UMUserDao.FIRST_NAME);
            match.add(UMUserDao.LAST_NAME);
            match.add(UMUserDao.USER_CODE);
            Map<List<String>, Object> matchname = new HashMap<>();
            if (user != null) {
                matchname.put(match, user);
            }

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, matchname);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            if (!isLoggedInUserRequired) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMUserDao.ID, loggedInId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }

            List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Users", ex);
            return null;
        }
    }

    public List<UMUser> retrieveUsersByCompanyIdAndDepartmentWithSearch(String user, Long companyId, List<Long> departmentIds, boolean isLoggedInUserRequired, Long loggedInId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            Map<String, Object> in = new HashMap<>();
            if (companyId != null) {
                equal.put(UMUserDao.COMPANY, companyId);
            }
            equal.put(UMUserDao.IS_ACTIVE, Boolean.TRUE);
            if (!CollectionUtils.isEmpty(departmentIds)) {
                in.put(UMUserDao.DEPARTMENT, departmentIds);
            }
            List<String> projections = new ArrayList<>();
            projections.add(UMUserDao.FIRST_NAME);
            projections.add(UMUserDao.LAST_NAME);
            projections.add(UMUserDao.USER_CODE);
            projections.add(UMUserDao.ID);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IN, in);
            if (!isLoggedInUserRequired) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMUserDao.ID, loggedInId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }
            List<String> match = new ArrayList<>();
            match.add(UMUserDao.FIRST_NAME);
            match.add(UMUserDao.LAST_NAME);
            match.add(UMUserDao.USER_CODE);
            Map<List<String>, Object> matchname = new HashMap<>();
            if (user != null) {
                matchname.put(match, user);
            }
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, matchname);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMUser> users = userService.retrieveUsers(null, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public List<UMUser> retrieveUsersByRoleIds(String user, List<Long> roleIds, Long companyId) {
        System.out.println("role ids..." + roleIds);
        List<UMUser> userByRole = null;
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<String> users = new ArrayList<>();

            List<String> projections = new ArrayList<>();
            projections.add(UMUserRoleDao.UM_USER_ID);

            Map<String, Object> designationIdMap = new HashMap<>();
            designationIdMap.put(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleIds);

            //Criterias
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, designationIdMap);

            List<UMUserRole> userRoles = null;
            try {
                userRoles = userService.retrieveUserRoles(projections, criterias, null);
            } catch (GenericDatabaseException ex) {
                java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!CollectionUtils.isEmpty(userRoles)) {
                System.out.println(" user role not empty");
                for (UMUserRole uMUserRole : userRoles) {
                    System.out.println("usersssss::::::::::" + uMUserRole.getUserId());
                    users.add(uMUserRole.getUserId().toString());
                }
            }

            Map<String, Object> equal = new HashMap<>();
            Map<String, Object> in = new HashMap<>();
            if (companyId != null) {
                equal.put(UMUserDao.COMPANY, companyId);
            }
            if (!CollectionUtils.isEmpty(users)) {
                in.put(UMUserDao.USER_ID, users);
            }
            List<String> projections1 = new ArrayList<>();
            projections1.add(UMUserDao.FIRST_NAME);
            projections1.add(UMUserDao.LAST_NAME);
            projections1.add(UMUserDao.USER_CODE);
            projections1.add(UMUserDao.ID);
            Map<GenericDao.QueryOperators, Object> criterias1 = new HashMap<>();
            criterias1.put(GenericDao.QueryOperators.EQUAL, equal);
            List<String> match = new ArrayList<>();
            match.add(UMUserDao.FIRST_NAME);
            match.add(UMUserDao.LAST_NAME);
            match.add(UMUserDao.USER_CODE);
            Map<List<String>, Object> matchname = new HashMap<>();
            if (user != null) {
                matchname.put(match, user);
            }
            criterias1.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, matchname);
            criterias1.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);
//            criterias1.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            criterias1.put(GenericDao.QueryOperators.IN, in);
            try {
                userByRole = userService.retrieveUsers(null, criterias1, null);
            } catch (GenericDatabaseException ex) {
                java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return userByRole;
    }

    /**
     * For messaging feature
     * <br/>Retrieves roles by companyId and status
     * <br/>Searches for match anywhere in role-name
     * <br/>Used Projections
     *
     * @param companyId
     * @param role
     * @param isActive
     * @param fetchRoleFeatures
     * @return
     */
    public List<UMRole> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive, boolean fetchRoleFeatures, int precedence) {
        try {
            Map<String, Object> equal = new HashMap<>();
            Map<String, Object> grt = new HashMap<>();
            if (companyId != null && !companyId.equals(0l)) {
                equal.put(UMRoleDao.COMPANY, getCompnies(companyId));
            }
            equal.put(UMRoleDao.IS_ACTIVE, isActive);
            grt.put(UMRoleDao.PRECEDENCE, precedence);
            List<String> requires = null;
            if (fetchRoleFeatures) {
                requires = new ArrayList<>();
                requires.add(UMRoleDao.UM_ROLE_FEATURE_SET);
            }
            List<String> projections = new ArrayList<>();
            projections.add(UMRoleDao.NAME);
            projections.add(UMRoleDao.ID);
            projections.add(UMRoleDao.CUSTOM1);
            Map<String, Object> in = new HashMap<>();
            if (role != null) {
                in.put(UMRoleDao.NAME, role);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, in);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMRoleDao.NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            criterias.put(GenericDao.QueryOperators.GREATER_THAN_EQUAL_TO, grt);
            List<UMRole> roles = roleService.retrieveRoles(projections, criterias, requires);
            return roles;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves designations by companyId and status
     * <br/>Searches for match anywhere in department-name
     * <br/>Used Projections
     */
    public List<UMDepartment> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMDepartmentDao.COMPANY, getCompnies(companyId));
            equal.put(UMDepartmentDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMDepartmentDao.DEPT_NAME);
            projections.add(UMDepartmentDao.ID);
            Map<String, Object> name = new HashMap<>();
            if (department != null) {
                name.put(UMDepartmentDao.DEPT_NAME, department);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, name);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMDepartmentDao.DEPT_NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMDepartment> departments = departmentService.retrieveDepartments(projections, criterias, null);
            return departments;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Departments", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves designations by companyId and status
     * <br/>Searches for match anywhere in department-name
     * <br/>Used Projections
     */
    public List<UMDepartment> searchDepartmentsOfOtherCompany(Long currentCompany, String query, Boolean isActive) {
        try {
            Map<String, Object> notEqual = new HashMap<>();
            notEqual.put(UMDepartmentDao.COMPANY, currentCompany);

            Map<String, Object> equal = new HashMap<>();
            equal.put(UMDepartmentDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMDepartmentDao.DEPT_NAME);
            projections.add(UMDepartmentDao.ID);
            projections.add(UMDepartmentDao.COMPANY);
            Map<String, Object> likeName = new HashMap<>();
            likeName.put(UMDepartmentDao.DEPT_NAME, query);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, likeName);
            criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMDepartmentDao.DEPT_NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMDepartment> departments = departmentService.retrieveDepartments(projections, criterias, null);
            return departments;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Departments", ex);
            return null;
        }
    }

    public Map<Long, UMDepartment> retrieveDepartmentMapByIds(List<Long> departmentIds, boolean onlyMinimalInfo) {
        Map<Long, UMDepartment> departmentMap = null;
        if (!CollectionUtils.isEmpty(departmentIds)) {
            List<String> projections = new ArrayList<>();
            if (onlyMinimalInfo) {
                projections.add(UMDepartmentDao.DEPT_NAME);
                projections.add(UMDepartmentDao.ID);
                projections.add(UMDepartmentDao.COMPANY);
            }

            Map<String, Object> departmentIdMap = new HashMap<>();
            departmentIdMap.put(UMDepartmentDao.ID, departmentIds);

            //Criterias
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, departmentIdMap);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMDepartmentDao.DEPT_NAME);

            List<UMDepartment> departments = null;
            try {
                departments = departmentService.retrieveDepartments(projections, criterias, null);
            } catch (GenericDatabaseException ex) {
                java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!CollectionUtils.isEmpty(departments)) {
                departmentMap = new LinkedHashMap<>();
                for (UMDepartment department : departments) {
                    departmentMap.put(department.getId(), department);
                }
            }
        }
        return departmentMap;
    }

    /**
     * For messaging feature
     * <br/>Retrieves groups by companyId and status
     * <br/>Searches for match anywhere in group-name
     * <br/>Used Projections
     */
    public List<UMUserGroup> retrieveGroupsByCompanyByStatus(Long companyId, String group, Boolean isActive) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserGroupDao.COMPANY, companyId);
            equal.put(UMUserGroupDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMUserGroupDao.NAME);
            projections.add(UMUserGroupDao.ID);
            Map<String, Object> name = new HashMap<>();
            if (group != null) {
                name.put(UMUserGroupDao.NAME, group);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, name);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserGroupDao.NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMUserGroup> userGroups = groupService.retrieveUserGroups(projections, criterias, null);
            return userGroups;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Groups", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves users by companyId and status
     * <br/>Searches for match anywhere in first-name
     * <br/>Used Projections
     */
    public List<UMUser> retrieveActivitiesByCompanyByStatus(Long companyId, String activity, Boolean isActive) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserDao.COMPANY, companyId);
            equal.put(UMUserDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMUserDao.FIRST_NAME);
            projections.add(UMUserDao.LAST_NAME);
            projections.add(UMUserDao.USER_CODE);
            projections.add(UMUserDao.ID);
            Map<String, Object> name = new HashMap<>();
            name.put(UMUserDao.FIRST_NAME, activity);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, name);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Activities", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves user-roles by roleId
     * <br/>isRequired->Pass true if user object is required
     */
    public List<UMUserRole> retrieveUserRolesByRoleId(Long roleId, boolean isRequired) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleId);
            List<String> require = new ArrayList<>();
            if (isRequired) {
                require.add(UMUserRoleDao.UM_USER);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);

            List<UMUserRole> retrieveUserRoleByRoleId = roleService.retrieveUserRoles(null, criterias, require);
            return retrieveUserRoleByRoleId;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles by id", ex);
            return null;
        }
    }

    public List<UMUserRole> retrieveUserRolesByRoleIds(List<Long> roleId, boolean isRequired) {
        try {
            Map<String, Object> in = new HashMap<>();
            in.put(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleId);
            List<String> require = new ArrayList<>();
            if (isRequired) {
                require.add(UMUserRoleDao.UM_USER);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, in);

            List<UMUserRole> retrieveUserRoleByRoleId = roleService.retrieveUserRoles(null, criterias, require);
            return retrieveUserRoleByRoleId;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles by id", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves group-contacts by groupId
     * <br/>isRequired->Pass true if user contact and user object are required
     */
    public List<UMGroupContact> retrieveGroupContacts(Long userGroupId, boolean isRequired) {
        try {
            UMUserGroup uMUserGroup = new UMUserGroup(userGroupId);
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMGroupContactDao.UM_USER_GROUP, uMUserGroup);
            List<String> require = new ArrayList<>();
            if (isRequired) {
                require.add(UMGroupContactDao.UM_USER_CONTACT);
                require.add(UMGroupContactDao.UM_USER_IN_UM_USER_CONTACT);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);

            List<UMGroupContact> groupContacts = groupService.retrieveGroupContacts(null, criterias, require);
            return groupContacts;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Group Contact by id", ex);
            return null;
        }
    }

    /**
     * For messaging feature
     * <br/>Retrieves users by companyId by departmentId
     * <br/>isLoggedInUserRequired->pass false if loggedIn user is not required
     */
    public List<UMUser> retrieveUsersByCompanyByDepartment(Long companyId, Long departmentId, boolean isLoggedInUserRequired, Long loggedInId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            if (companyId != null) {
                equal.put(UMUserDao.COMPANY, companyId);
            }
            equal.put(UMUserDao.IS_ACTIVE, Boolean.TRUE);
            equal.put(UMUserDao.DEPARTMENT, departmentId);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            if (!isLoggedInUserRequired) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMUserDao.ID, loggedInId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }

            List<UMUser> users = userService.retrieveUsers(null, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public List<UMUser> retrieveUsersByCompanyIdsByDepartment(Long companyId, List<Long> departmentIds, boolean isLoggedInUserRequired, Long loggedInId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            Map<String, Object> in = new HashMap<>();
            if (companyId != null) {
                equal.put(UMUserDao.COMPANY, companyId);
            }
            equal.put(UMUserDao.IS_ACTIVE, Boolean.TRUE);
            if (!CollectionUtils.isEmpty(departmentIds)) {
                in.put(UMUserDao.DEPARTMENT, departmentIds);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IN, in);
            if (!isLoggedInUserRequired) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMUserDao.ID, loggedInId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }

            List<UMUser> users = userService.retrieveUsers(null, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public List<UMUser> retrieveUsersByShiftByDepartment(Long companyId, List<Long> shiftIds, List<Long> departmentIds, boolean isLoggedInUserRequired, Long loggedInId) {
        List<UMUser> users = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(shiftIds) && !CollectionUtils.isEmpty(departmentIds)) {
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                Map<String, Object> in = new HashMap<>();
                in.put(UMUserDao.DEPARTMENT, departmentIds);
                in.put(UMUserDao.CUSTOM4, shiftIds);
                criterias.put(GenericDao.QueryOperators.IN, in);

                Map<String, Object> equal = new HashMap<>();
                if (companyId != null) {
                    equal.put(UMUserDao.COMPANY, companyId);
                }
                equal.put(UMUserDao.IS_ACTIVE, Boolean.TRUE);
                criterias.put(GenericDao.QueryOperators.EQUAL, equal);
                List<String> requires = new ArrayList<>();
                requires.add(UMUserDao.USER_ROLE_COLLECTION);
                if (!isLoggedInUserRequired) {
                    Map<String, Object> notEqual = new HashMap<>();
                    notEqual.put(UMUserDao.ID, loggedInId);
                    criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
                }
                users = userService.retrieveUsers(null, criterias, requires);
            }
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public List<UMFeature> retrieveAllFeature(Long companyId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            if (companyId != null) {
                equal.put(UMFeatureDao.COMPANY, companyId);
            }
            equal.put(UMUserDao.IS_ACTIVE, Boolean.TRUE);

            List<String> projectionList = new ArrayList<>();
            projectionList.add(UMFeatureDao.ID);
            projectionList.add(UMFeatureDao.NAME);
            projectionList.add(UMFeatureDao.SEQ_NO);
            projectionList.add(UMFeatureDao.MENU_LABEL);
            projectionList.add(UMFeatureDao.MENU_TYPE);
            projectionList.add(UMFeatureDao.DESCRIPTION);
            projectionList.add(UMFeatureDao.FEATURE_URL);
            projectionList.add(UMFeatureDao.WEBSERVICE_URL);
            projectionList.add(UMFeatureDao.PRECEDENCE);
            projectionList.add(UMFeatureDao.MENU_CATEGORY);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            List<UMFeature> uMFeatures = featureService.retrieveFeatures(projectionList, criterias, null);
            return uMFeatures;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public List<UMFeature> retrieveAllMainFeature(Long companyId) {
        try {
            Map<String, Object> in = new HashMap<>();
            if (companyId != null) {
                in.put(UMFeatureDao.COMPANY, getCompnies(companyId));
            }
            in.put(UMFeatureDao.MENU_TYPE, Arrays.asList(HkSystemConstantUtil.FeatureTypes.MENU, HkSystemConstantUtil.FeatureTypes.ENTITY));

            Map<String, Object> equal = new HashMap<>();
            equal.put(UMFeatureDao.IS_ACTIVE, Boolean.TRUE);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, in);
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMFeatureDao.MENU_LABEL);
            List<UMFeature> uMFeatures = featureService.retrieveFeatures(null, criterias, null);
            return uMFeatures;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving users", ex);
            return null;
        }
    }

    public boolean doesFranchiseNameExist(String franchiseName, Long skipFranchiseId) {
        boolean result = false;
        if (StringUtils.hasText(franchiseName)) {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMCompanyDao.NAME, franchiseName);
            equal.put(UMCompanyDao.IS_ARCHIVE, Boolean.FALSE);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);

            if (skipFranchiseId != null) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMCompanyDao.ID, skipFranchiseId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }

            try {
                result = companyService.isCompanyExits(criterias);
            } catch (GenericDatabaseException ex) {
                LOGGER.error("Error looking for company", ex);
            }
        }
        return result;
    }

    public void createOrUpdateWorkHistory(List<HkUserWorkHistoryEntity> hkUserWorkHistoryEntityList) {
        if (!CollectionUtils.isEmpty(hkUserWorkHistoryEntityList)) {
            commonDao.saveAll(hkUserWorkHistoryEntityList);
        }
    }

    public Map<String, String> retrieveRecipientNames(List<String> recipientCodes) {
        Map<String, String> recipientCodeNameMap = null;
        final String SEPARATOR = ":";
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            recipientCodeNameMap = new LinkedHashMap<>();
            List<Long> employeeIds = new LinkedList<>();
            List<Long> departmentIds = new LinkedList<>();
            List<Long> otherFranchisedepartmentIds = new LinkedList<>();
            List<Long> groupIds = new LinkedList<>();
            List<Long> activityIds = new LinkedList<>();
            List<Long> designationIds = new LinkedList<>();
            List<Long> franchiseIds = new LinkedList<>();

            for (String recipientCode : recipientCodes) {
                StringTokenizer tokenizer = new StringTokenizer(recipientCode, SEPARATOR);
                if (tokenizer.countTokens() == 2) {
                    Long id = new Long(tokenizer.nextToken());
                    String type = tokenizer.nextToken();
                    switch (type) {
                        case HkSystemConstantUtil.RecipientCodeType.EMPLOYEE:
                            employeeIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT:
                            otherFranchisedepartmentIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DEPARTMENT:
                            departmentIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DESIGNATION:
                            designationIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.ACTIVITY:
                            activityIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.FRANCHISE:
                            franchiseIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.GROUP:
                            groupIds.add(id);
                            break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(employeeIds)) {
                try {

                    List<String> projections = new ArrayList<>();
                    projections.add(UMUserDao.FIRST_NAME);
                    projections.add(UMUserDao.LAST_NAME);
                    projections.add(UMUserDao.USER_CODE);
                    projections.add(UMUserDao.ID);

                    Map<String, Object> employeeIdMap = new HashMap<>();
                    employeeIdMap.put(UMUserDao.ID, employeeIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, employeeIdMap);
                    criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);

                    List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
                    if (!CollectionUtils.isEmpty(users)) {
                        for (UMUser uMUser : users) {
                            StringBuilder name = new StringBuilder();
                            if (StringUtils.hasText(uMUser.getUserCode())) {
                                name.append(uMUser.getUserCode());
                                name.append(" - ");
                            }
                            name.append(uMUser.getFirstName());
                            if (StringUtils.hasText(uMUser.getLastName())) {
                                name.append(" ");
                                name.append(uMUser.getLastName());
                            }
                            StringBuilder key = new StringBuilder(uMUser.getId().toString());
                            key.append(SEPARATOR);
                            key.append(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                            recipientCodeNameMap.put(key.toString(), name.toString());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("Messaging->Error retrieving Employees", ex);
                }
            }

            if (!CollectionUtils.isEmpty(departmentIds)) {
                try {

                    List<String> projections = new ArrayList<>();
                    projections.add(UMDepartmentDao.DEPT_NAME);
                    projections.add(UMDepartmentDao.ID);

                    Map<String, Object> departmentIdMap = new HashMap<>();
                    departmentIdMap.put(UMDepartmentDao.ID, departmentIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, departmentIdMap);
                    criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMDepartmentDao.DEPT_NAME);

                    List<UMDepartment> departments = departmentService.retrieveDepartments(projections, criterias, null);
                    if (!CollectionUtils.isEmpty(departments)) {
                        for (UMDepartment uMDepartment : departments) {
                            StringBuilder key = new StringBuilder(uMDepartment.getId().toString());
                            key.append(SEPARATOR);
                            key.append(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
                            recipientCodeNameMap.put(key.toString(), uMDepartment.getDeptName());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("Messaging->Error retrieving Employees", ex);
                }
            }

            if (!CollectionUtils.isEmpty(otherFranchisedepartmentIds)) {
                try {
                    List<String> projections = new ArrayList<>();
                    projections.add(UMDepartmentDao.DEPT_NAME);
                    projections.add(UMDepartmentDao.ID);
                    projections.add(UMDepartmentDao.COMPANY);

                    Map<String, Object> departmentIdMap = new HashMap<>();
                    departmentIdMap.put(UMDepartmentDao.ID, otherFranchisedepartmentIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, departmentIdMap);
                    criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMDepartmentDao.DEPT_NAME);

                    List<UMDepartment> departments = departmentService.retrieveDepartments(projections, criterias, null);
                    Map<Long, UMCompany> franchiseMap = this.retrieveActiveFranchises(true);

                    if (!CollectionUtils.isEmpty(departments) && !CollectionUtils.isEmpty(franchiseMap)) {
                        for (UMDepartment uMDepartment : departments) {
                            StringBuilder key = new StringBuilder(uMDepartment.getId().toString());
                            key.append(SEPARATOR);
                            key.append(HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT);
                            StringBuilder nameBuilder = new StringBuilder(uMDepartment.getDeptName());
                            if (uMDepartment.getCompany() != null && franchiseMap.containsKey(uMDepartment.getCompany())) {
                                nameBuilder.append(" - ");
                                nameBuilder.append(franchiseMap.get(uMDepartment.getCompany()).getName());
                            }
                            recipientCodeNameMap.put(key.toString(), nameBuilder.toString());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("Messaging->Error retrieving Employees", ex);
                }
            }

            if (!CollectionUtils.isEmpty(designationIds)) {
                try {

                    List<String> projections = new ArrayList<>();
                    projections.add(UMRoleDao.NAME);
                    projections.add(UMRoleDao.ID);

                    Map<String, Object> designationIdMap = new HashMap<>();
                    designationIdMap.put(UMRoleDao.ID, designationIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, designationIdMap);
                    criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMRoleDao.NAME);

                    List<UMRole> designations = userService.retrieveRoles(projections, criterias, null);
                    if (!CollectionUtils.isEmpty(designations)) {
                        for (UMRole uMRole : designations) {
                            StringBuilder key = new StringBuilder(uMRole.getId().toString());
                            key.append(SEPARATOR);
                            key.append(HkSystemConstantUtil.RecipientCodeType.DESIGNATION);
                            recipientCodeNameMap.put(key.toString(), uMRole.getName());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("Messaging->Error retrieving Employees", ex);
                }
            }
        }
        return recipientCodeNameMap;
    }

    public Set<Long> retrieveRecipientIds(List<String> recipientCodes) {
        return this.retrieveRecipientIds(recipientCodes, false);
    }

    public Set<Long> retrieveRecipientIds(List<String> recipientCodes, boolean includeChildDepartments) {
        Set<Long> recipientIdsSet = new HashSet<>();

        final String SEPARATOR = ":";
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            List<Long> employeeIds = new LinkedList<>();
            List<Long> departmentIds = new LinkedList<>();
            List<Long> groupIds = new LinkedList<>();
            List<Long> activityIds = new LinkedList<>();
            List<Long> designationIds = new LinkedList<>();
            List<Long> franchiseIds = new LinkedList<>();

            for (String recipientCode : recipientCodes) {

                StringTokenizer tokenizer = new StringTokenizer(recipientCode, SEPARATOR);
                if (tokenizer.countTokens() == 2) {
                    Long id = new Long(tokenizer.nextToken());
                    String type = tokenizer.nextToken();

                    switch (type) {
                        case HkSystemConstantUtil.RecipientCodeType.EMPLOYEE:
                            employeeIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DEPARTMENT:
                            if (includeChildDepartments) {
                                departmentIds.addAll(retrieveDepartmentIdTree(id));
                            } else {
                                departmentIds.add(id);
                            }
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DESIGNATION:
                            designationIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.ACTIVITY:
                            activityIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.FRANCHISE:
                            franchiseIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.GROUP:
                            groupIds.add(id);
                            break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(employeeIds)) {
                recipientIdsSet.addAll(employeeIds);
            }

            if (!CollectionUtils.isEmpty(departmentIds)) {
                try {
                    List<String> projections = new ArrayList<>();
                    projections.add(UMUserDao.ID);

                    Map<String, Object> departmentIdMap = new HashMap<>();
                    departmentIdMap.put(UMUserDao.DEPARTMENT, departmentIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, departmentIdMap);

                    List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
                    if (!CollectionUtils.isEmpty(users)) {
                        for (UMUser uMUser : users) {
                            recipientIdsSet.add(uMUser.getId());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("User wrapper :retrieveRecipientIds ->Error retrieving Depts", ex);
                }
            }

            if (!CollectionUtils.isEmpty(designationIds)) {
                try {

                    List<String> projections = new ArrayList<>();
                    projections.add(UMUserRoleDao.UM_USER_ID);

                    Map<String, Object> designationIdMap = new HashMap<>();
                    designationIdMap.put(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, designationIds);

                    //Criterias
                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.IN, designationIdMap);
                    List<UMUserRole> userRoles = userService.retrieveUserRoles(projections, criterias, null);
                    if (!CollectionUtils.isEmpty(userRoles)) {
                        for (UMUserRole uMUserRole : userRoles) {
                            recipientIdsSet.add(uMUserRole.getUserId());
                        }
                    }
                } catch (GenericDatabaseException ex) {
                    LOGGER.error("User wrapper :retrieveRecipientIds ->Error retrieving Roles", ex);
                }
            }
        }
        return recipientIdsSet;
    }

    public boolean removeFranchise(Long franchiseId) {
        companyService.deleteCompany(companyService.retrieveCompanyByCompanyId(franchiseId));
        operationsService.removeAllFranchiseRequirements(franchiseId);
        return true;
    }

    public void createUserCode(UMUser user) {
        StringBuilder userCode = new StringBuilder();
        Search search = new Search(HkUserTypeSequenceEntity.class);
        search.addFilterEqual(HkUserTypeSequenceField.USER_TYPE, user.getContact().getCustom1());
        search.addFilterEqual(HkUserTypeSequenceField.FRANCHISE, user.getCompany());
        search.addFilterEqual(IS_ARCHIVE, false);
        HkUserTypeSequenceEntity userType = (HkUserTypeSequenceEntity) commonDao.searchUnique(search);
        String key = "EMP_TYPE_" + user.getContact().getCustom1();
        HkSystemConfigurationEntity seq = operationsService.retrieveSystemConfigurationByKey(key, user.getCompany());
        String value = null;
        if (seq != null) {
            value = seq.getKeyValue();
        }
        if (userType != null) {
            int employeenum = userType.getCurrentValue() + 1;
            if (value != null) {
                DecimalFormat decimalFormat = new DecimalFormat(value);
//                userCode.append(user.getContact().getCustom1());
                userCode.append(decimalFormat.format(employeenum));
            }
            user.setType(String.valueOf(employeenum));
            user.setUserCode(userCode.toString());
            //  update the current value and increase it to 1 - it's code sequence
            userType.setCurrentValue(userType.getCurrentValue() + 1);
//                commonDAO.save(userType);
        } else {
            if (value != null) {
                DecimalFormat decimalFormat = new DecimalFormat(value);
//                userCode.append(user.getContact().getCustom1());
                userCode.append(decimalFormat.format(1));
            }
            user.setType("1");
            user.setUserCode(userCode.toString());
            HkUserTypeSequenceEntity hkUserTypeSequenceEntity = new HkUserTypeSequenceEntity();
            hkUserTypeSequenceEntity.setCurrentValue(2);
            hkUserTypeSequenceEntity.setIsArchive(false);
            HkUserTypeSequenceEntityPK hkUserTypeSequenceEntityPK = new HkUserTypeSequenceEntityPK();
            hkUserTypeSequenceEntityPK.setFranchise(user.getCompany());
            hkUserTypeSequenceEntityPK.setUserType(user.getContact().getCustom1());
            hkUserTypeSequenceEntity.setHkUserTypeSequenceEntityPK(hkUserTypeSequenceEntityPK);
            hkUserTypeSequenceEntity.setCurrentValue(1);
            commonDao.save(hkUserTypeSequenceEntity);
        }
        user.setUserCode(userCode.toString());
    }

    /**
     * This method creates the user.
     *
     * @param user The user object to be created.
     */
    public void createUser(UMUser user) {
        try {
            createUserCode(user);
            user.setUserId(this.createUserId(user.getFirstName(), user.getLastName()));
            user.setPassword(basicPasswordEncryptor.encryptPassword(user.getUserId() + "123"));
            StringBuilder builder = new StringBuilder(user.getFirstName());
            if (user.getMiddleName() != null) {
                builder.append(" ").append(user.getMiddleName());
            }
            if (user.getLastName() != null) {
                builder.append(" ").append(user.getLastName());
            }
            createLocaleForEntity(builder.toString(), "Employee", user.getCreatedBy(), user.getCompany());
            userService.createUser(user);

            HkUserWorkHistoryEntity userHistory = new HkUserWorkHistoryEntity();
            userHistory.setDepartment(user.getDepartment());
            userHistory.setDesignation(this.retrieveDesignationStrForHistory(user));
            userHistory.setEffectedFrm(new Date());
            userHistory.setIsArchive(false);
            userHistory.setReportsTo(user.getCustom2());
            userHistory.setShift(user.getCustom4());
            userHistory.setUserId(user.getId());
            commonDao.save(userHistory);
            String franchiseName = "Default franchise";
            if (user.getCompany() != null) {
                UMCompany retrieveCompanyByCompanyId = companyService.retrieveCompanyByCompanyId(user.getCompany());
                if (retrieveCompanyByCompanyId != null) {
                    franchiseName = retrieveCompanyByCompanyId.getName();
                }
            }
            //  Send notification to users with um rights that the new employee has been added
            Set<Long> umAuthorizedUsers = this.searchUsersByFeatureName(Arrays.asList("employeesAdd", "employeesEdit"), user.getCompany());
            if (!CollectionUtils.isEmpty(umAuthorizedUsers)) {
                umAuthorizedUsers.remove(user.getId());
            }
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, user.getUserCode());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.FRANCHISE_NAME, franchiseName);
            HkNotificationEntity notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.EMPLOYEE,
                    HkSystemConstantUtil.NotificationInstanceType.ADD_EMP, valuesMap, user.getId(), user.getCompany());
            notificationService.sendNotification(notification, umAuthorizedUsers);

            //  Send notification to reporting officer
            if (user.getCustom2() != null) {
                valuesMap = new HashMap<>();
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, user.getUserCode());
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
                notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.EMPLOYEE,
                        HkSystemConstantUtil.NotificationInstanceType.NEW_EMPLOYEE_REPORTING, valuesMap, user.getId(), user.getCompany());
                notificationService.sendNotification(notification, this.retrieveRecipientIds(Arrays.asList(user.getCustom2().split(","))));
            }

            //  Send notification to new user
            valuesMap = new HashMap<>();
            valuesMap.put("LOGIN", "Welcome to Hari Krishna Group");
            notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.EMPLOYEE,
                    HkSystemConstantUtil.NotificationInstanceType.NEW_EMPLOYEE_FIRST_LOGIN, valuesMap, user.getId(), user.getCompany());
            notificationService.sendNotification(notification, Arrays.asList(user.getId()));

        } catch (UMUserManagementException ex) {
            java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String createUserId(String firstName, String lastName) {
        String userIdBase = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String userId = userIdBase;
        Search search = new Search(UMUser.class);
        search.addFilterEqual(UMUserManagementConstants.UserObjects.USER_ID, userId);
        int count = 1;
        while (commonDao.count(search) > 0) {
            userId = userIdBase + count++;
            search = new Search(UMUser.class);
            search.addFilterEqual(UMUserManagementConstants.UserObjects.USER_ID, userId);
        }
        return userId;
    }

    /**
     * This method updates the user object.
     *
     * @param user The object which is to be updated.
     * @return Returns true if the user was updated.
     * @throws
     * com.argusoft.usermanagement.common.exception.UMUserManagementException
     */
    public boolean updateUser(UMUser user) throws UMUserManagementException {
        if (user.getUserCode() == null) {
            createUserCode(user);
        }
        userService.updateUser(user);
        StringBuilder builder = new StringBuilder(user.getFirstName());
        if (user.getMiddleName() != null) {
            builder.append(" ").append(user.getMiddleName());
        }
        if (user.getLastName() != null) {
            builder.append(" ").append(user.getLastName());
        }
        createLocaleForEntity(builder.toString(), "Employee", user.getCreatedBy(), user.getCompany());
        //  Archive previous history detail and create new one
        Search search = SearchFactory.getSearch(HkUserWorkHistoryEntity.class);
        search.addFilterEqual(HkUserWorkHistoryField.USER_ID, user.getId());

        if (user.getCompany() != null && !user.getCompany().equals(0l)) {
            if (user.getDepartment() != null) {
                HkUserWorkHistoryEntity workHistory = (HkUserWorkHistoryEntity) commonDao.searchUnique(search);
                HkUserWorkHistoryEntity userHistory = new HkUserWorkHistoryEntity();
                userHistory.setDepartment(user.getDepartment());
                userHistory.setDesignation(this.retrieveDesignationStrForHistory(user));
                userHistory.setEffectedFrm(new Date());
                userHistory.setIsArchive(false);
                userHistory.setReportsTo(user.getCustom2());
                if (user.getCustom4() != null) {
                    userHistory.setShift(user.getCustom4());
                }
                userHistory.setUserId(user.getId());
                commonDao.save(userHistory);

                //  update old object
                if (workHistory != null) {
                    workHistory.setEffectedTo(new Date());
                    workHistory.setIsArchive(true);
                }
            }
            String franchiseName = "Default franchise";
            UMCompany retrieveCompanyByCompanyId = companyService.retrieveCompanyByCompanyId(user.getCompany());
            if (retrieveCompanyByCompanyId != null) {
                franchiseName = retrieveCompanyByCompanyId.getName();
            }
            //  Send notification to users with um rights that the new employee has been added
            Set<Long> umAuthorizedUsers = this.searchUsersByFeatureName(Arrays.asList("employeesAdd", "employeesEdit"), user.getCompany());
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, user.getUserCode());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.FRANCHISE_NAME, franchiseName);
            HkNotificationEntity notification = notificationService.createNotification(HkSystemConstantUtil.NotificationType.EMPLOYEE,
                    HkSystemConstantUtil.NotificationInstanceType.UPDATE_EMP, valuesMap, user.getId(), user.getCompany());
            notificationService.sendNotification(notification, umAuthorizedUsers);
        }
        return true;
    }

    /**
     * Written for private use. This method will return the designation to be
     * set in history table of user.
     *
     * @param user
     * @return
     */
    public String retrieveDesignationStrForHistory(UMUser user) {
        Set<UMUserRole> roleSet = user.getUMUserRoleSet();
        Set<Long> roleIdSet = new TreeSet<>();
        String designation = "";
        if (!CollectionUtils.isEmpty(roleSet)) {
            for (UMUserRole userRole : roleSet) {
                //  Adding in treeset first so all the ids are added in order and then we'll convert it in string
                roleIdSet.add(userRole.getuMUserRolePK().getRole());
            }
            designation = roleIdSet.toString().substring(1, roleIdSet.toString().length() - 1);
        }
        return designation;
    }

    /**
     * This method searches for the users.
     *
     * @param searchString The search string provided by user in search box.
     * @param company The id of company.
     * @param userPrecedence
     * @return Returns the map of id of user and the string to be shown to user
     * as suggestion.
     */
    public Map<Long, String> searchUsers(String searchString, Long company, Integer userPrecedence, Long status, Boolean isActive) {
        System.out.println("Search Users..." + searchString + "---company" + company + "--prec" + userPrecedence);
        Map<Long, String> userMap = null;
        if (StringUtils.hasText(searchString) && searchString.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            Map<Long, String> roleMap = new HashMap<>();
            //  if search string starts with @R, means search string is role
            boolean isRole = searchString.toUpperCase().startsWith("@" + HkSystemConstantUtil.RecipientCodeType.DESIGNATION.toUpperCase());
            Search search = null;
            if (isActive) {
                search = SearchFactory.getActiveSearch(UMRole.class);
            } else {
                search = SearchFactory.getSearch(UMRole.class);
            }

            search.addFilterGreaterOrEqual(UMRoleDao.PRECEDENCE, userPrecedence);
//            search.addFilterEqual(COMPANY, company);
            if (isRole) {
                System.out.println("Is Role");
                searchString = searchString.substring(searchString.toUpperCase().indexOf("@" + HkSystemConstantUtil.RecipientCodeType.DESIGNATION) + ("@" + HkSystemConstantUtil.RecipientCodeType.DESIGNATION).length()).trim();
                search.addFilterILike(UMUserManagementConstants.RoleObjects.ROLE_NAME, "%" + searchString + "%");
            }
            search.addField(UMUserManagementConstants.RoleObjects.ROLE_NAME);
            search.addField(UMUserManagementConstants.RoleObjects.ROLE_ID);
            List<UMRole> roleList = commonDao.search(search);

            if (!CollectionUtils.isEmpty(roleList)) {
                for (UMRole role : roleList) {
                    roleMap.put(role.getId(), role.getName());
                }
                if (isActive) {
                    search = SearchFactory.getActiveSearch(UMUser.class);
                } else {
                    search = SearchFactory.getSearch(UMUser.class);
                }
                if (status != null) {
                    search.addFilterEqual(STATUS, status.toString());
                }
                search.addFilterEqual(COMPANY, company);
                if (isRole) {
                    //  if it's role, fetch all the users with these roles
                    Search roleSearch = null;
                    if (isActive) {
                       roleSearch = SearchFactory.getActiveSearch(UMUserRole.class);
                    } else {
                       roleSearch = SearchFactory.getSearch(UMUserRole.class);
                    }
                    
                    roleSearch.addFilterIn(UMUserManagementConstants.UserRoleObjects.ROLE_ID, roleMap.keySet());
                    roleSearch.addField(UMUserManagementConstants.UserRoleObjects.USER_ID);
                    List<Long> finalUsers = commonDao.search(roleSearch);
                    if (!CollectionUtils.isEmpty(finalUsers)) {
                        //  put this in main search to find the users with given roles
                        search.addFilterIn(UMUserManagementConstants.UserObjects.ID, finalUsers);
                    }
                } else {
                    search.addFilterOr(Filter.ilike(UMUserManagementConstants.UserObjects.FIRST_NAME, "%" + searchString + "%"),
                            Filter.ilike(UMUserManagementConstants.UserObjects.MIDDLE_NAME, "%" + searchString + "%"),
                            Filter.ilike(UMUserManagementConstants.UserObjects.LAST_NAME, "%" + searchString + "%"),
                            Filter.ilike(UMUserManagementConstants.UserObjects.USER_ID, "%" + searchString + "%"),
                            Filter.ilike(UMUserManagementConstants.UserObjects.USER_CODE, "%" + searchString + "%"));
                }

                search.addFetch(UMUserManagementConstants.UserObjects.USER_ROLE_SET)
                        .setDistinct(true);
                List<UMUser> userList = commonDao.search(search);
                if (!CollectionUtils.isEmpty(userList)) {
                    userMap = new HashMap<>();
                    for (UMUser user : userList) {
                        StringBuilder builder = new StringBuilder(user.getUserCode());
                        builder.append("-").append(user.getUserId());
                        builder.append(" - ").append(user.getFirstName());
                        if (user.getMiddleName() != null) {
                            builder.append(" ").append(user.getMiddleName()).append(" ");
                        }

                        String designation = getRoleString(user.getUMUserRoleSet(), roleMap);
                        if (!designation.equalsIgnoreCase("NotAvailabl") && designation.indexOf("NotAvailabl") <= -1) {
                            builder.append(user.getLastName()).append(" - ").append(designation);
                            userMap.put(user.getId(), builder.toString());
                        }

                    }
                }
            }
        }
        return userMap;
    }

    private String getRoleString(Set<UMUserRole> userRoleSet, Map<Long, String> roleMap) {
        String roles = "";
        if (!CollectionUtils.isEmpty(userRoleSet)) {
            for (UMUserRole userRole : userRoleSet) {
                if (userRole.getIsActive()) {
                    if (roleMap.get(userRole.getuMUserRolePK().getRole()) != null) {
                        roles += roleMap.get(userRole.getuMUserRolePK().getRole()) + ", ";
                    } else {

                        roles += "NotAvailable";
                    }

                }
            }
            if (roles.length() > 2) {
                roles = roles.substring(0, roles.length() - 2);
            }
        } else {
            roles = "N/A";
        }
        return roles;
    }

    /**
     * For edit franchise
     * <br/>Retrieves users by companyId and status
     * <br/>Used Projections, only userId and password
     * <br/>isLoggedInUserRequired->pass false if loggedIn user is not required
     */
    public List<UMUser> retrieveUsersByCompany(Long companyId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserDao.COMPANY, companyId);
            equal.put(UMUserDao.IS_ACTIVE, isActive);
            List<String> projections = new ArrayList<>();
            projections.add(UMUserDao.USER_ID);
            projections.add(UMUserDao.PASSWORD);
            projections.add(UMUserDao.ID);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            if (!isLoggedInUserRequired) {
                Map<String, Object> notEqual = new HashMap<>();
                notEqual.put(UMUserDao.ID, loggedInId);
                criterias.put(GenericDao.QueryOperators.NOT_EQUAL, notEqual);
            }

            List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Franchise->Error retrieving Users", ex);
            return null;
        }
    }

    public Map<Long, List<UMUser>> retrieveUsersByType(Long companyId, Boolean isActive, List<Long> type) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(UMUserManagementConstants.UserObjects.COMPANY_ID, companyId);
        search.addFilterIn(UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.CUSTOM1, type);
        List<UMUser> users = commonDao.search(search);
        Map<Long, List<UMUser>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(users)) {
            for (UMUser uMUser : users) {
                List<UMUser> get = map.get(uMUser.getContact().getCustom1());
                if (get == null) {
                    get = new ArrayList<>();
                }
                get.add(uMUser);
                map.put(uMUser.getContact().getCustom1(), get);

            }
        }
        return map;
    }

    public int retrieveUserCountByCompany(Long companyId) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(UMUserManagementConstants.UserObjects.COMPANY_ID, companyId);
        return commonDao.count(search);
    }

    public int retrieveUserCountByDepartment(Long departmentId) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(UMUserManagementConstants.UserObjects.DEPARTMENT_ID, departmentId);
        return commonDao.count(search);
    }

    public int relieveResignedUsers(Date relieveToDate) {
        int relievedUsers = 0;
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterLessOrEqual(UMUserManagementConstants.UserObjects.EXPIRED_ON, relieveToDate);
        List<UMUser> usersToRelieve = commonDao.search(search);
        if (!CollectionUtils.isEmpty(usersToRelieve)) {
            Set<UMUserRole> uMUserRoles = new HashSet<>();
            for (UMUser user : usersToRelieve) {
                uMUserRoles.addAll(user.getUMUserRoleSet());
                user.setIsActive(false);
                relievedUsers++;
            }
            if (!CollectionUtils.isEmpty(uMUserRoles)) {
                for (UMUserRole uMUserRole : uMUserRoles) {
                    uMUserRole.setIsActive(false);
                    uMUserRole.setIsArchive(true);
                }
            }
        }

        return relievedUsers;
    }

    public List<UMFeature> retrieveFeatureByName(List<String> names) {
        try {
            List<UMFeature> features;
            if (!CollectionUtils.isEmpty(names)) {
                Map<String, Object> equalCriteria = new HashMap<>();
                equalCriteria.put(UMFeatureDao.NAME, names);
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                criterias.put(GenericDao.QueryOperators.EQUAL, equalCriteria);
                features = featureService.retrieveFeatures(null, criterias, null);
                return features;
            }
        } catch (GenericDatabaseException ex) {
            LOGGER.error("User Service Wrapper:", ex);
        }
        return null;

    }

    public Map<String, UMFeature> getAllParentByFeatureName(List<String> featureNames) {
        try {
            Map<String, UMFeature> map = new HashMap<String, UMFeature>();
            List<UMFeature> features = new ArrayList<>();
            if (!CollectionUtils.isEmpty(featureNames)) {
                Map<String, Object> equalCriteria = new HashMap<>();
                equalCriteria.put(UMFeatureDao.NAME, featureNames);
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                criterias.put(GenericDao.QueryOperators.EQUAL, equalCriteria);
                features = featureService.retrieveFeatures(null, criterias, null);
                if (!CollectionUtils.isEmpty(features)) {
                    for (UMFeature uMFeature : features) {
                        map.put(uMFeature.getName(), uMFeature);
                    }
                }
                return map;
            }
        } catch (GenericDatabaseException ex) {
            LOGGER.error("User Service Wrapper:", ex);
        }
        return null;
    }

    public Map<Long, String> retrieveFeatureIdWithNameMap() {
        List<UMFeature> featureList = new ArrayList<>();
        Map<Long, String> featureIdNameMap = null;
        Search search = new Search(UMFeature.class);
        search.addFilterEqual(IS_ARCHIVE, false);

        featureList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(featureList)) {
            featureIdNameMap = new HashMap<>();
            for (UMFeature feature : featureList) {
                featureIdNameMap.put(feature.getId(), feature.getName());
            }
        }
        return featureIdNameMap;
    }

    public UMFeature retrieveUMFeatureByNameAndCompanyId(String featureName, Long companyId) {
        try {
            Map<String, Object> equal = new HashMap<>();
//            equal.put(UMFeatureDao.COMPANY, companyId);
            equal.put(UMFeatureDao.NAME, featureName);
            List<String> projections = new ArrayList<>();
            projections.add(UMFeatureDao.ID);
            Map<String, Object> in = new HashMap<>();
            in.put(UMFeatureDao.COMPANY, getCompnies(companyId));

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IN, in);

            List<UMFeature> uMFeatures = featureService.retrieveFeatures(projections, criterias, null);
            if (!CollectionUtils.isEmpty(uMFeatures)) {
                return uMFeatures.get(0);
            }
            return null;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Franchise->Error retrieving Users", ex);
            return null;
        }
    }

    public Map<Long, String> searchDepartments(String searchString, Long franchise) {
        Map<Long, String> finalMap = null;
        List<Long> existingDeptIds = hrService.retrieveDepartmentIdsForExistingWorkflows(franchise);
        if (!CollectionUtils.isEmpty(existingDeptIds)) {
            Search search = SearchFactory.getSearch(UMDepartment.class);
            if (searchString != null) {
                search.addFilterILike(UMUserManagementConstants.DepartmentFields.DEPT_NAME, "%" + searchString + "%");
            }
            search.addFilterIn(UMUserManagementConstants.DepartmentFields.COMPANY, getCompnies(franchise));
            search.addFilterIn(UMUserManagementConstants.DepartmentFields.ID, existingDeptIds);

            search.addField(UMUserManagementConstants.DepartmentFields.ID);
            search.addField(UMUserManagementConstants.DepartmentFields.DEPT_NAME);
            List<UMDepartment> finalDepts = commonDao.search(search);
            if (!CollectionUtils.isEmpty(finalDepts)) {
                finalMap = new HashMap<>();
                for (UMDepartment dept : finalDepts) {
                    finalMap.put(dept.getId(), dept.getDeptName());
                }
            }
        }
        return finalMap;
    }

    public Map<Long, String> searchDepartmentsWithoutWorkFlow(String searchString, Long franchise) {
        Map<Long, String> finalMap = null;
        Search search = SearchFactory.getSearch(UMDepartment.class);
        search.addFilterILike(UMUserManagementConstants.DepartmentFields.DEPT_NAME, "%" + searchString + "%");
        search.addFilterIn(UMUserManagementConstants.DepartmentFields.COMPANY, getCompnies(franchise));
        search.addField(UMUserManagementConstants.DepartmentFields.ID);
        search.addField(UMUserManagementConstants.DepartmentFields.DEPT_NAME);
        List<UMDepartment> finalDepts = commonDao.search(search);
        if (!CollectionUtils.isEmpty(finalDepts)) {
            finalMap = new HashMap<>();
            for (UMDepartment dept : finalDepts) {
                finalMap.put(dept.getId(), dept.getDeptName());
            }
        }
        return finalMap;
    }

    public void changePassword(Long id, Long franchise, String newPassword, String userId) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(UMUserManagementConstants.UserObjects.ID, id);
        search.addFilterEqual(COMPANY, franchise);
        UMUser user = (UMUser) commonDao.searchUnique(search);
        if (user != null) {
            user.setUserId(userId);
            user.setPassword(basicPasswordEncryptor.encryptPassword(newPassword));
        }
    }

    public List<Long> searchUsersByName(String searchString, Long franchise) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        if (searchString != null) {
            search.addFilterOr(Filter.ilike(UMUserManagementConstants.UserObjects.FIRST_NAME, "%" + searchString + "%"),
                    Filter.ilike(UMUserManagementConstants.UserObjects.MIDDLE_NAME, "%" + searchString + "%"),
                    Filter.ilike(UMUserManagementConstants.UserObjects.LAST_NAME, "%" + searchString + "%"),
                    Filter.ilike(UMUserManagementConstants.UserObjects.USER_ID, "%" + searchString + "%"),
                    Filter.ilike(UMUserManagementConstants.UserObjects.USER_CODE, "%" + searchString + "%"));
        }
        search.addFilterEqual(COMPANY, franchise);
        search.addField(UMUserManagementConstants.UserObjects.ID);
        return commonDao.search(search);
    }

    public Set<Long> searchUsersByFeatureName(List<String> featureNames, Long franchise) {
        Set<Long> userSet = null;
        Search search = SearchFactory.getActiveSearch(UMFeature.class);
        search.addFilterIn(UMUserManagementConstants.FeatureFields.NAME, featureNames);
        if (franchise != null) {
            search.addFilterIn(COMPANY, getCompnies(franchise));
        }
        search.addField(UMUserManagementConstants.FeatureFields.ID);
        List<Long> featureIdList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(featureIdList)) {
            search = SearchFactory.getActiveSearch(UMRoleFeature.class);
            search.addFilterIn(UMUserManagementConstants.RoleFeatureFields.FEATURE_ID, featureIdList);
            search.addField(UMUserManagementConstants.RoleFeatureFields.ROLE_ID);
            if (franchise != null) {
                search.addFilterIn(COMPANY, getCompnies(franchise));
            }
            List<Long> roleList = commonDao.search(search);
            if (!CollectionUtils.isEmpty(roleList)) {
                search = SearchFactory.getActiveSearch(UMUserRole.class);
                search.addFilterIn(UMUserManagementConstants.UserRoleObjects.ROLE_ID, roleList);
                search.addField(UMUserManagementConstants.UserRoleObjects.USER_ID);
                List<Long> usersList = commonDao.search(search);
                if (!CollectionUtils.isEmpty(usersList)) {
                    userSet = new HashSet<>(usersList);
                }
            }
        }
        return userSet;
    }

    public List<Long> searchUsersByShift(Long shiftId, Long franchise) {
        List<Long> userSet = null;
        if (shiftId != null && franchise != null) {
            Search search = SearchFactory.getActiveSearch(UMUser.class);
            search.addFilterEqual(UMUserManagementConstants.UserObjects.CUSTOM4, shiftId);
            search.addFilterEqual(UMUserManagementConstants.UserObjects.COMPANY_ID, franchise);
            search.addField(UMUserManagementConstants.UserObjects.ID);
            userSet = commonDao.search(search);
            return userSet;
        }
        return userSet;
    }

    public List<Long> retrieveAllUsersByFranchise(Long franchise) {
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(COMPANY, franchise);
        search.addField(UMUserManagementConstants.UserObjects.ID);
        return commonDao.search(search);
    }

    public Set<Long> retrieveApproversByWorkflow(Long workflowId, Integer level) {
        final String SEPARATOR = ":";
        List<HkWorkflowApproverEntity> approverEntities = hrService.retrieveWorkflowApproversByWorkflowId(workflowId, level, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(approverEntities)) {
            List<String> recepientCodes = new LinkedList<>();
            for (HkWorkflowApproverEntity approverEntity : approverEntities) {
                StringBuilder recepientCode = new StringBuilder();
                recepientCode.append(approverEntity.getHkWorkflowApproverEntityPK().getReferenceInstance());
                recepientCode.append(SEPARATOR);
                recepientCode.append(approverEntity.getHkWorkflowApproverEntityPK().getReferenceType());
                recepientCodes.add(recepientCode.toString());
            }
            //  Remaining to be implemented
            return this.retrieveRecipientIds(recepientCodes);
        } else {
            return null;
        }
    }

    public List<UMRole> copyRolesFromFranchise(Long sourceFranchise, Long destinationFranchise, Long modifiedBy) {
        System.out.println("Copt roles" + sourceFranchise + "dest--" + destinationFranchise + "--od" + modifiedBy);
        Search search = SearchFactory.getActiveSearch(UMRole.class);
        search.addFilterEqual(COMPANY, sourceFranchise);
        List<String> roleNameList = new ArrayList<>();
        List<UMRole> roles = commonDao.search(search);
        List<UMRole> rolesToCreate = new LinkedList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            for (UMRole role : roles) {
                if (!role.getName().equalsIgnoreCase("HK Admin")) {
                    UMRole mRole = new UMRole(null, role.getName(), role.getCreatedBy(), new Date(), role.getIsActive(), role.getIsArchive(), role.getPrecedence());
                    mRole.setModifiedBy(role.getModifiedBy());
                    mRole.setModifiedOn(new Date());
                    mRole.setDescription(role.getDescription());
                    mRole.setCompany(destinationFranchise);
                    Set<UMRoleFeature> umRoleFeatureSet = role.getUMRoleFeatureSet();
                    Set<UMRoleFeature> umRoleFeatureSetNew = new HashSet<>();
                    if (!CollectionUtils.isEmpty(umRoleFeatureSet)) {
                        for (UMRoleFeature uMRoleFeature : umRoleFeatureSet) {
                            UMRoleFeature roleFeature = new UMRoleFeature(null, uMRoleFeature.getAllowToCreate(), uMRoleFeature.getAllowToDelete(), uMRoleFeature.getAllowToUpdate(), uMRoleFeature.getIsActive(), uMRoleFeature.getIsArchive());
                            roleFeature.setCompany(destinationFranchise);
                            roleFeature.setFeature(uMRoleFeature.getFeature());
                            roleFeature.setFeatureId(uMRoleFeature.getFeatureId());
                            roleFeature.setRole(mRole);
                            roleFeature.setRoleId(mRole.getId());
                            umRoleFeatureSetNew.add(roleFeature);
                        }
                    }
                    mRole.setUMRoleFeatureSet(umRoleFeatureSetNew);
                    rolesToCreate.add(mRole);
                    roleNameList.add(mRole.getName());
                }
            }
            commonDao.saveAll(rolesToCreate);
            this.createAllLocaleForEntity(roleNameList, "Designation", modifiedBy, destinationFranchise);
        }
        return rolesToCreate;
    }

    /**
     * includes count of child department
     *
     * @param departmentId
     * @return
     */
    public int retrieveUserCountOfDepartment(Long departmentId) {
        int count = 0;
        Search search = SearchFactory.getActiveSearch(UMDepartment.class);
        search.addFilterEqual(UMDepartmentDao.PARENT + "." + UMDepartmentDao.ID, departmentId);
        search.addField(UMDepartmentDao.ID);
        List<Long> depIdList = commonDao.search(search);
        if (depIdList == null) {
            depIdList = new ArrayList<>();
        }
        depIdList.add(departmentId);
        if (!CollectionUtils.isEmpty(depIdList)) {
            search = SearchFactory.getActiveSearch(UMUser.class);
            search.addFilterIn(UMUserDao.DEPARTMENT, depIdList);
            List<UMUser> users = commonDao.search(search);
            if (!CollectionUtils.isEmpty(users)) {
                count = users.size();
            }
        }
        return count;
    }

    public Map<Long, Integer> retrieveUserCountsByDepartment(Long company) {
        Map<Long, Integer> resultMap = new HashMap<>();
        Search search = SearchFactory.getActiveSearch(UMDepartment.class);
        search.addFilterIn(COMPANY, getCompnies(company));
        search.addField(UMDepartmentDao.ID);
        List<Long> depIdList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(depIdList)) {
            search = SearchFactory.getActiveSearch(UMUser.class);
            search.addFilterIn(UMUserDao.DEPARTMENT, depIdList);
            List<UMUser> users = commonDao.search(search);
            if (!CollectionUtils.isEmpty(users)) {
                for (UMUser user : users) {
                    Integer userCount = resultMap.get(user.getDepartment());
                    if (userCount == null) {
                        userCount = 0;
                    }
                    userCount++;
                    resultMap.put(user.getDepartment(), userCount);
                }
            }
        }
        return resultMap;
    }

    public List<Long> retrieveUsersByRoleName(String roleName, Long companyId) {
        List<Long> users = new ArrayList<>();
        Search search = SearchFactory.getActiveSearch(UMRole.class);
        search.addFilterIn(UMRoleDao.COMPANY, getCompnies(companyId));
        search.addFilterEqual(UMRoleDao.NAME, roleName);
        search.addField(UMRoleDao.ID);
        List<Long> roleList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(roleList)) {
            Search search1 = SearchFactory.getActiveSearch(UMUserRole.class);
            search1.addFilterIn(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleList);
            search1.addField(UMUserRoleDao.UM_USER_ROLE_PK);
            List<UMUserRolePK> userRolePKList = commonDao.search(search1);
            if (!CollectionUtils.isEmpty(userRolePKList)) {
                for (UMUserRolePK uMUserRolePK : userRolePKList) {
                    users.add(uMUserRolePK.getUserobj());
                }
            }
        }

        return users;
    }

    public int retrieveUserCountByRole(Long role) {
        Search search = SearchFactory.getActiveSearch(UMUserRole.class);
        search.addFilterEqual(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, role);
        search.addField(UMUserRoleDao.UM_USER_ROLE_PK);
        List<UMUserRolePK> userRolePKList = commonDao.search(search);
        int userCount = 0;
        if (!CollectionUtils.isEmpty(userRolePKList)) {
            userCount = userRolePKList.size();
        }
        return userCount;
    }

    public Map<Long, Integer> retrieveUserCountsByRole(Long company) {
        Map<Long, Integer> resultMap = new HashMap<>();
        Search search = SearchFactory.getActiveSearch(UMRole.class);
        search.addFilterIn(COMPANY, getCompnies(company));
        search.addField(UMRoleDao.ID);
        List<Long> roleIdList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(roleIdList)) {
            search = SearchFactory.getActiveSearch(UMUserRole.class);
            search.addFilterIn(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleIdList);
            search.addField(UMUserRoleDao.UM_USER_ROLE_PK);
            List<UMUserRolePK> userRolePKList = commonDao.search(search);
            if (!CollectionUtils.isEmpty(userRolePKList)) {
                for (UMUserRolePK userRolePK : userRolePKList) {
                    Integer userCount = resultMap.get(userRolePK.getRole());
                    if (userCount == null) {
                        userCount = 0;
                    }
                    userCount++;
                    resultMap.put(userRolePK.getRole(), userCount);
                }
            }
        }
        return resultMap;
    }

    public Map<Long, UMFeature> retrieveFeaturesByFeatureIds(List<Long> featureIds, String type) {
        List<UMFeature> featureList = null;
        Map<Long, UMFeature> mapOfFeatureIdWithFeatures = null;
        if (!CollectionUtils.isEmpty(featureIds)) {
            Map<String, Object> criteriaMap = new HashMap<>();

            criteriaMap.put(UMFeatureDao.ID, featureIds);
            criteriaMap.put(UMFeatureDao.IS_ACTIVE, true);
            criteriaMap.put(UMFeatureDao.IS_ARCHIVE, false);
            if (!StringUtils.isEmpty(type)) {
                criteriaMap.put(UMFeatureDao.MENU_TYPE, type);
            }
            List<String> requires = new ArrayList<>();
            requires.add(UMFeatureDao.UM_FEATURE_CHILD_SET);

            try {
                featureList = featureService.retrieveFeatures(null, criteriaMap, null, null, requires);
            } catch (GenericDatabaseException ex) {
                java.util.logging.Logger.getLogger(WebApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!CollectionUtils.isEmpty(featureList)) {
                mapOfFeatureIdWithFeatures = new HashMap<>();
                for (UMFeature feature : featureList) {
                    mapOfFeatureIdWithFeatures.put(feature.getId(), feature);
                }
            }

        }
        return mapOfFeatureIdWithFeatures;
    }

    public UMRole createRole(String roleName, int precedence, List<String> featureNameList, Long company) {
        UMRole roleEntity = new UMRole(null, roleName, 1L, new Date(), true, false, precedence);
        roleEntity.setCompany(company);
        roleEntity.setDescription(roleName + " role, created by system on deployment.");

        if (!CollectionUtils.isEmpty(featureNameList)) {
            Map<String, Object> criteriaMap = new HashMap<>();
            criteriaMap.put(UMFeatureDao.NAME, featureNameList);
            criteriaMap.put(UMFeatureDao.IS_ACTIVE, true);
            criteriaMap.put(UMFeatureDao.IS_ARCHIVE, false);

            List<String> requires = new ArrayList<>();
            requires.add(UMFeatureDao.UM_FEATURE_CHILD_SET);

            Set<UMRoleFeature> umRoleFeatureSet = new HashSet<>();
            List<UMFeature> featureList = null;
            try {
                featureList = featureService.retrieveFeatures(null, criteriaMap, null, null, requires);
            } catch (GenericDatabaseException ex) {
                java.util.logging.Logger.getLogger(WebApplicationConfig.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!CollectionUtils.isEmpty(featureList)) {
                Collection<UMFeature> updatedFeatureList = this.recursiveFeatureList(featureList, new HashMap<Long, UMFeature>()).values();
                for (UMFeature featureEntity : updatedFeatureList) {
                    UMRoleFeature roleFeatureEntity = new UMRoleFeature(null, true, true, true, true, false);
                    roleFeatureEntity.setCompany(company);
                    roleFeatureEntity.setDescription("Created by system for " + roleName + " role.");
                    roleFeatureEntity.setFeatureId(featureEntity.getId());
                    roleFeatureEntity.setFeature(featureEntity);
                    roleFeatureEntity.setRole(roleEntity);
                    umRoleFeatureSet.add(roleFeatureEntity);
                }
                roleEntity.setUMRoleFeatureSet(umRoleFeatureSet);
            }
        }

        Long roleId = roleService.createRole(roleEntity);
        roleEntity.setId(roleId);
        return roleEntity;
    }

    public UMRole createRole(String roleName, Integer precedence, Long companyId) {
        System.out.println("Role name...." + roleName);
        UMRole roleEntity = null;
        if (CollectionUtils.isEmpty(this.retrieveRolesByCompanyByStatus(companyId, roleName, Boolean.TRUE, false, precedence))) {
            roleEntity = new UMRole(null, roleName, 1L, new Date(), true, false, precedence);
            roleEntity.setCompany(companyId);
            roleEntity.setDescription(roleName + " role, created by system on deployment.");
            // Code added by Shifa Salheen on 11 August for assigning default dpeartment to hkadmin and franchise admin 
            if (roleName.equals(HkSystemConstantUtil.ROLE.HK_ADMIN) || roleName.equals(HkSystemConstantUtil.ROLE.FRANCHISE_ADMIN)) {
                System.out.println("inside this");
                List<UMDepartment> deptList = this.retrieveDefaultDepartment(0l);
                if (!CollectionUtils.isEmpty(deptList)) {
                    UMDepartment defaultDept = deptList.get(0);
                    // Set default department for hkAdmin
                    roleEntity.setCustom1(defaultDept.getId());
                }
            }
            List<UMFeature> featureList = this.retrieveFeatures(precedence);
            if (!CollectionUtils.isEmpty(featureList)) {
                Set<UMRoleFeature> umRoleFeatureSet = new HashSet<>();
                for (UMFeature featureEntity : featureList) {
                    if (StringUtils.hasText(featureEntity.getMenuType()) && (featureEntity.getMenuType().equalsIgnoreCase(HkSystemConstantUtil.FeatureTypes.MENU) || featureEntity.getMenuType().equalsIgnoreCase(HkSystemConstantUtil.FeatureTypes.MENU_ITEM) || featureEntity.getMenuType().equalsIgnoreCase(HkSystemConstantUtil.FeatureTypes.EXTRA_ITEM))) {
                        UMRoleFeature roleFeatureEntity = new UMRoleFeature(null, true, true, true, true, false);
                        roleFeatureEntity.setCompany(companyId);
                        roleFeatureEntity.setDescription("Created by system for " + roleName + " role.");
                        roleFeatureEntity.setFeatureId(featureEntity.getId());
                        roleFeatureEntity.setFeature(featureEntity);
                        roleFeatureEntity.setRole(roleEntity);
                        umRoleFeatureSet.add(roleFeatureEntity);
                    }
                }
                roleEntity.setUMRoleFeatureSet(umRoleFeatureSet);

            }

            roleService.createRole(roleEntity);
        }
        return roleEntity;
    }

    public UMRole retrieveRole(String roleName, Long companyId, boolean fetchRoleFeatures, int precedence) {
        UMRole role = null;

        List<UMRole> roles = this.retrieveRolesByCompanyByStatus(companyId, roleName, Boolean.TRUE, fetchRoleFeatures, precedence);
        if (!CollectionUtils.isEmpty(roles)) {
            role = roles.get(0);
        }

        return role;
    }

    private Map<Long, UMFeature> recursiveFeatureList(Collection<UMFeature> featureList, Map<Long, UMFeature> outputFeatureMap) {
        for (UMFeature featureEntity : featureList) {
            if (!outputFeatureMap.containsKey(featureEntity.getId())) {
                outputFeatureMap.put(featureEntity.getId(), featureEntity);
                if (!CollectionUtils.isEmpty(featureEntity.getuMFeatureChildSet())) {
                    recursiveFeatureList(featureEntity.getuMFeatureChildSet(), outputFeatureMap);
                }
            }
        }
        return outputFeatureMap;
    }

    public UMUser createUser(String firstName, String middleName, String lastName, String userId, String password, UMRole roleEntity) {
        UMUser umUser = new UMUser();
        umUser.setCompany(0L);
        umUser.setCreatedBy(1L);
        umUser.setCreatedOn(new Date());
        umUser.setFirstName(firstName);
        umUser.setMiddleName(middleName);
        umUser.setLastName(lastName);
        umUser.setIsActive(true);
        umUser.setIsArchive(false);
        umUser.setUserId(userId);
        umUser.setUserCode(userId);
        umUser.setPreferredLanguage(HkSystemConstantUtil.I18_LANGUAGE.ENGLISH);
        umUser.setPassword(basicPasswordEncryptor.encryptPassword(password));

        UMUserContact contactEntity = new UMUserContact();
        contactEntity.setFirstName(firstName);
        contactEntity.setMiddleName(middleName);
        contactEntity.setLastName(lastName);
        contactEntity.setCompany(0L);
        contactEntity.setCreatedBy(1L);
        contactEntity.setCreatedOn(new Date());
        umUser.setContact(contactEntity);

        Set<UMUserRole> userRoleSet = new HashSet<>();
        UMUserRole userRoleEntity = new UMUserRole(new UMUserRolePK());
        userRoleEntity.getuMUserRolePK().setRole(roleEntity.getId());
        userRoleEntity.setuMUser(umUser);
        userRoleSet.add(userRoleEntity);
        umUser.setUMUserRoleSet(userRoleSet);

        try {
            userService.createUser(umUser);
        } catch (UMUserManagementException ex) {
            java.util.logging.Logger.getLogger(WebApplicationInitializerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        return umUser;
    }

    /*
     Method to assign role to User
     */
    public boolean assignRoleToUser(UMUser uMUser, UMRole uMRole, Boolean isActive) throws UMUserManagementException {
        boolean result = false;
        if (uMUser != null && uMRole != null) {
            UMUserRole userRoleEntity = new UMUserRole(new UMUserRolePK(uMUser.getId(), uMRole.getId()));
            userRoleEntity.setuMUser(uMUser);
            if (isActive) {
                userRoleEntity.setIsActive(true);
                userRoleEntity.setIsArchive(false);
            } else {
                userRoleEntity.setIsActive(false);
                userRoleEntity.setIsArchive(true);
            }
            userService.createAllUserRole(Arrays.asList(userRoleEntity));
            result = true;
        }
        return result;
    }

    /*
     This method returns map of user id and usercode-firstname lastname for the franchise
     */
    public Map<Long, UMUser> retrieveUsersByFranchise(Long franchise) {
        Map<Long, UMUser> userIdCodeNameMap = null;
        Search search = SearchFactory.getActiveSearch(UMUser.class);
        search.addFilterEqual(COMPANY, franchise);
        search.addField(UMUserManagementConstants.UserObjects.ID);
        search.addField(UMUserManagementConstants.UserObjects.USER_CODE);
        search.addField(UMUserManagementConstants.UserObjects.FIRST_NAME);
        search.addField(UMUserManagementConstants.UserObjects.LAST_NAME);
        search.addField(UMUserManagementConstants.UserObjects.DEPARTMENT_ID);
        List<UMUser> users = commonDao.search(search);
        if (!CollectionUtils.isEmpty(users)) {
            userIdCodeNameMap = new LinkedHashMap<>();
            for (UMUser uMUser : users) {
                userIdCodeNameMap.put(uMUser.getId(), uMUser);
            }

        }

        return userIdCodeNameMap;
    }

    public Map<Long, String> retrieveDepartmentsByFranchise(Long franchise) {
        Map<Long, String> deptIdName = null;
        Search search = SearchFactory.getActiveSearch(UMDepartment.class);
        search.addFilterIn(COMPANY, getCompnies(franchise));
        search.addField(UMUserManagementConstants.DepartmentFields.ID);
        search.addField(UMUserManagementConstants.DepartmentFields.DEPT_NAME);
        List<UMDepartment> depts = commonDao.search(search);
        if (!CollectionUtils.isEmpty(depts)) {
            deptIdName = new LinkedHashMap<>();
            for (UMDepartment uMDepartment : depts) {
                deptIdName.put(uMDepartment.getId(), uMDepartment.getDeptName());
            }
        }

        return deptIdName;
    }

    /**
     * Retrieve users by id.<br/>
     * Projection applied
     *
     */
    public List<UMUser> retrieveUsers(List<Long> ids, boolean onlyMinInfo) throws GenericDatabaseException {
        Map<String, Object> equal = new HashMap<>();
        List<String> projections = null;
        List<String> requires = null;
        equal.put(UMUserDao.IS_ACTIVE, true);
        if (onlyMinInfo) {
            projections = new ArrayList<>();
            projections.add(UMUserDao.ID);
            projections.add(UMUserDao.USER_ID);
        } else {
            projections = new ArrayList<>();
            projections.add(UMUserDao.ID);
            projections.add(UMUserDao.USER_ID);
            projections.add(UMUserDao.COMPANY);
            projections.add(UMUserDao.PASSWORD);
            projections.add(UMUserDao.PREFERRED_LANGUAGE);
            projections.add(UMUserDao.JOINING_DATE);
            projections.add(UMUserDao.EMAIL_ADDRESS);
            projections.add(UMUserDao.EXPIRED_ON);
            projections.add(UMUserDao.STATUS);
            projections.add(UMUserDao.IS_ACTIVE);
            projections.add(UMUserDao.USER_CODE);

            requires = new ArrayList<>();
            requires.add(UMUserDao.USER_ROLE_COLLECTION);
        }
        Map<String, Object> idmap = new HashMap<>();
        idmap.put(UMUserDao.ID, ids);
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        criterias.put(GenericDao.QueryOperators.IN, idmap);
        List<UMUser> retrieveUsers = userService.retrieveUsers(projections, criterias, requires);
        return retrieveUsers;
    }

    public Map<Long, UMCompany> retrieveActiveFranchises(boolean onlyMinimumInfo) {
        Search search = SearchFactory.getActiveSearch(UMCompany.class);
        if (onlyMinimumInfo) {
            search.addField(UMCompanyDao.ID);
            search.addField(UMCompanyDao.NAME);
        }
        List<UMCompany> franchises = commonDao.search(search);
        Map<Long, UMCompany> franchiseMap = null;
        if (!CollectionUtils.isEmpty(franchises)) {
            franchiseMap = new LinkedHashMap<>();
            for (UMCompany franchise : franchises) {
                franchiseMap.put(franchise.getId(), franchise);
            }
        }
        return franchiseMap;
    }

    public void createLocaleForEntity(String text, String entity, Long modifiedBy, Long companyId) {
        I18nLabelEntity label = new I18nLabelEntity();
        label.setText(text);
        label.setEnvironment("w");
        label.setLastModifiedBy(modifiedBy);
        String finalKey = text.trim().replace(" ", "");
        String get = HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get(entity.toLowerCase());
        label.setLabelPK(new I18nLabelPKEntity(finalKey, null, null, LabelType.CONTENT.toString(), get, companyId));
        i18nService.addLabelAsync(label);
    }

    public void createAllLocaleForEntity(List<String> texts, String entity, Long modifiedBy, Long companyId) {
        List<I18nLabelEntity> i18nLabelEntitys = new ArrayList<>();
        String get = HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get(entity.toLowerCase());
        for (String text : texts) {
            I18nLabelEntity label = new I18nLabelEntity();
            label.setText(text);
            label.setEnvironment("w");
            label.setLastModifiedBy(modifiedBy);
            String finalKey = text.trim().replace(" ", "");
            label.setLabelPK(new I18nLabelPKEntity(finalKey, null, null, LabelType.CONTENT.toString(), get, companyId));
            i18nLabelEntitys.add(label);
        }
        i18nService.addBulkLabels(i18nLabelEntitys);
    }

    public void deleteLocaleForEntity(String text, String entity, String labelType, Long modifiedBy, Long companyId) {
        System.out.println("text : " + text);
        System.out.println("entity : " + entity);
        String finalKey = text.trim().replace(" ", "");
        String get = HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get(entity.toLowerCase());
        List<I18nLabelEntity> i18nLabelEntitys = i18nService.getLabelByKeyAndEntityAndTypeAndCompanyId(get, finalKey, labelType, companyId);
        System.out.println("i18nLabelEntitys : " + i18nLabelEntitys);
        if (!CollectionUtils.isEmpty(i18nLabelEntitys)) {
            i18nService.deleteLabel(i18nLabelEntitys);
        }

    }

    public void deleteAllLocaleForEntity(List<String> texts, String entity, String labelType, Long modifiedBy, Long companyId) {
        System.out.println("text : " + texts);
        System.out.println("entity : " + entity);
        List<String> keys = new ArrayList<>();
        for (String text : texts) {
            String finalKey = text.trim().replace(" ", "");
            keys.add(finalKey);
        }
        String get = HkSystemConstantUtil.I18N_CONTENT_TYPE_MAP.get(entity.toLowerCase());
        List<I18nLabelEntity> i18nLabelEntitys = i18nService.retrieveLabelByKeyAndEntityAndTypeAndCompanyId(get, keys, labelType, companyId);
        System.out.println("i18nLabelEntitys : " + i18nLabelEntitys);
        if (!CollectionUtils.isEmpty(i18nLabelEntitys)) {
            i18nService.deleteLabel(i18nLabelEntitys);
        }
    }

    public Map<Long, String> retrieveUserNamesByIds(List<Long> ids) throws GenericDatabaseException {
        Map<Long, String> userMap = null;
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMUserDao.IS_ACTIVE, true);
        List<String> projections = new ArrayList<>();
        projections.add(UMUserDao.ID);
        projections.add(UMUserDao.FIRST_NAME);
        projections.add(UMUserDao.LAST_NAME);
        Map<String, Object> idmap = new HashMap<>();
        idmap.put(UMUserDao.ID, ids);
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        criterias.put(GenericDao.QueryOperators.IN, idmap);
        List<UMUser> retrieveUsers = userService.retrieveUsers(projections, criterias, null);
        if (!CollectionUtils.isEmpty(retrieveUsers)) {
            userMap = new HashMap<>();
            for (UMUser uMUser : retrieveUsers) {
                if (StringUtils.hasText(uMUser.getLastName())) {
                    userMap.put(uMUser.getId(), uMUser.getFirstName() + " " + uMUser.getLastName());
                } else {
                    userMap.put(uMUser.getId(), uMUser.getFirstName());
                }
            }
        }
        return userMap;
    }

    public List<Long> searchUsersByShift(Long shiftId) {
        List<Long> userSet = null;

        if (shiftId != null) {
            Search search = SearchFactory.getActiveSearch(UMUser.class);
            search.addFilterEqual(UMUserManagementConstants.UserObjects.CUSTOM4, shiftId);

            search.addField(UMUserManagementConstants.UserObjects.ID);
            userSet = commonDao.search(search);
            return userSet;
        }
        return userSet;
    }

    public List<UMUser> retrieveUsersForInvitees(List<Long> ids) throws GenericDatabaseException {
        Map<String, Object> equal = new HashMap<>();
        List<String> projections = null;
        equal.put(UMUserDao.IS_ACTIVE, true);

        Map<String, Object> idmap = new HashMap<>();
        idmap.put(UMUserDao.ID, ids);

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        criterias.put(GenericDao.QueryOperators.IN, idmap);
        List<String> fetches = new ArrayList<>();
        fetches.add(UMUserManagementConstants.UserObjects.USER_CONTACT);
        fetches.add(UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.ADDRESS_SET);

        List<UMUser> retrieveUsers = userService.retrieveUsers(projections, criterias, fetches);
        return retrieveUsers;
    }

    public List<UMFeature> retrieveAllFeaturesByListOfMenuType(List<String> listOfMenuTypes, Long companyId, Boolean isActive, String searchparam) {
        List<UMFeature> featureList = null;
        Search search = new Search(UMFeature.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(IS_ACTIVE, isActive);
        search.addFilterIn(COMPANY, getCompnies(companyId));
        search.addFilterIn(UMFeatureDetail.MENU_TYPE, listOfMenuTypes);
        if (searchparam != null) {
            search.addFilterILike(UMFeatureDetail.MENU_LABEL, searchparam + "%");
        }
        featureList = commonDao.search(search);
        return featureList;
    }

    public Map<Long, String> retrieveFeatureNamesByIds(List<Long> ids, Boolean nameRequiredInsteadOfLabel) throws GenericDatabaseException {
        Map<Long, String> featureMap = null;
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMFeatureDao.IS_ACTIVE, true);
        List<String> projections = new ArrayList<>();
        projections.add(UMFeatureDao.ID);
        if (!nameRequiredInsteadOfLabel) {
            projections.add(UMFeatureDao.MENU_LABEL);
        } else {
            projections.add(UMFeatureDao.NAME);
        }
        Map<String, Object> idmap = new HashMap<>();
        idmap.put(UMFeatureDao.ID, ids);
        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        criterias.put(GenericDao.QueryOperators.IN, idmap);
        List<UMFeature> retrieveFeatures = featureService.retrieveFeatures(projections, criterias, null);
        if (!CollectionUtils.isEmpty(retrieveFeatures)) {
            featureMap = new HashMap<>();
            for (UMFeature uMFeature : retrieveFeatures) {
                if (!nameRequiredInsteadOfLabel && StringUtils.hasText(uMFeature.getMenuLabel())) {
                    featureMap.put(uMFeature.getId(), uMFeature.getMenuLabel());
                } else if (StringUtils.hasText(uMFeature.getName())) {
                    featureMap.put(uMFeature.getId(), uMFeature.getName());
                }
            }
        }
        return featureMap;
    }

    public void setTheme(Long userId, String folderName) {
        try {
            UMUser retrieveUserById = userService.retrieveUserById(userId, null);
            retrieveUserById.getContact().setFacebookPage(folderName);
            userService.updateUser(retrieveUserById);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, String> retrieveUIFieldNameWithComponentTypes(List<String> UIFieldList) {
        return fieldService.retrieveUIFieldNameWithComponentTypes(UIFieldList);
    }

    public BasicBSONObject makeBSONObject(Map<String, Object> val, Map<String, String> dbTypeMap, String featureName, long companyId, Object instanseId) {
        if (!CollectionUtils.isEmpty(val)) {
            BasicBSONObject basicBSONObject = new BasicBSONObject();
            for (Map.Entry<String, Object> entry : val.entrySet()) {
                String colName = entry.getKey();
                Object value = entry.getValue();
                String dbType = dbTypeMap.get(colName);
                if (colName.contains(HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                    dbType = "String";
                }
                if (dbType != null && value != null && !"".equals(value.toString())) {
                    switch (dbType) {
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
//                            if (value instanceof Integer) {
                            basicBSONObject.put(colName, new DateTime(value.toString()));
//                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                try {
//                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()));
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
                                } catch (ParseException ex) {

                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()));
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
                                } catch (ParseException ex) {
                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                            basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                            basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                            basicBSONObject.put(colName, Long.parseLong(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
                            basicBSONObject.put(colName, value.toString());
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
                            List<String> Array = new ArrayList<>();
                            Array.add(value.toString());
                            basicBSONObject.put(colName, (ArrayList) value);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
                            basicBSONObject.put(colName, (ArrayList<Long>) value);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.IMAGE:
                            String imageName;
                            System.out.println("In update called --> " + value.toString());
                            if (value instanceof String) {
                                imageName = value.toString();
                            } else {
                                ArrayList imageArray = (ArrayList) value;
                                imageName = imageArray.get(0).toString();
                            }
                            String oldImageName = imageName;
                            if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                try {
                                    imageName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, imageName);
                                    System.out.println("In wrapper name is : " + imageName);
                                    FolderManagement.copyFilesCustom(oldImageName, imageName, instanseId, true);
                                } catch (IOException ex) {

                                }
                            }
                            basicBSONObject.put(colName, imageName);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.FILE:
                            ArrayList fileNameArray = (ArrayList) value;
                            ArrayList newFileNames = new ArrayList();
                            for (Object fileNameObj : fileNameArray) {
                                String fileName = fileNameObj.toString();
                                String oldFileName = fileName;
                                if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                    try {
                                        fileName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, fileName);
                                        FolderManagement.copyFilesCustom(oldFileName, fileName, instanseId, false);
                                    } catch (IOException ex) {

                                    }
                                }
                                newFileNames.add(fileName);
                            }
                            basicBSONObject.put(colName, newFileNames);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN:
                            basicBSONObject.put(colName, Boolean.parseBoolean(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.OBJECT_ID:
                            basicBSONObject.put(colName, value.toString());
                            break;

                    }
                }
            }
            return basicBSONObject;
        }
        return null;
    }

    public UMFeature retrieveFeatureByIdForCustom(Long featureId) {
        UMFeature feature = null;
        List<UMFeature> featureList = null;
        Search search = new Search(UMFeature.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(UMFeatureDetail.ID, featureId);
        featureList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(featureList)) {
            feature = featureList.get(0);
        }
        return feature;
    }

    public UMFeature retrieveFeatureByFeatureName(String featureName) {
        UMFeature feature = null;
        List<UMFeature> featureList = null;
        Search search = new Search(UMFeature.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(UMFeatureDetail.FEATURE_NAME, featureName);
        featureList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(featureList)) {
            feature = featureList.get(0);
        }
        return feature;
    }

    public Map<Long, String> retrieveFeatureMap(Boolean nameRequiredInsteadOfLabel) {
        System.out.println("Feature Map");
        Map<Long, String> featureMap = null;
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMFeatureDao.IS_ACTIVE, true);
        List<String> projections = new ArrayList<>();
        projections.add(UMFeatureDao.ID);
        if (!nameRequiredInsteadOfLabel) {
            projections.add(UMFeatureDao.MENU_LABEL);
        } else {
            projections.add(UMFeatureDao.NAME);
        }

        Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
        criterias.put(GenericDao.QueryOperators.EQUAL, equal);
        List<UMFeature> retrieveFeatures = null;
        try {
            retrieveFeatures = featureService.retrieveFeatures(projections, criterias, null);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(UserManagementServiceWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!CollectionUtils.isEmpty(retrieveFeatures)) {
            featureMap = new HashMap<>();
            for (UMFeature uMFeature : retrieveFeatures) {
                if (!nameRequiredInsteadOfLabel && StringUtils.hasText(uMFeature.getMenuLabel())) {
                    featureMap.put(uMFeature.getId(), uMFeature.getMenuLabel());
                } else if (StringUtils.hasText(uMFeature.getName())) {
                    featureMap.put(uMFeature.getId(), uMFeature.getName());
                }
            }
        }
        return featureMap;
    }

    public List<UMUser> retrieveUsersByRoleId(List<Long> roleId, String search, Long companyId) {
        List<UMUser> users = null;
        try {
            Map<String, Object> in1 = new HashMap<>();
            in1.put(UMUserRoleDao.UM_ROLE_IN_UM_USER_ROLE_PK, roleId);
            List<String> require = new ArrayList<>();
//            require.add(UMUserRoleDao.UM_USER);
            Map<GenericDao.QueryOperators, Object> criteria = new HashMap<>();
            criteria.put(GenericDao.QueryOperators.IN, in1);

            List<UMUserRole> retrieveUserRoleByRoleId = roleService.retrieveUserRoles(null, criteria, require);
            List<Long> userIds = null;
            if (!CollectionUtils.isEmpty(retrieveUserRoleByRoleId)) {
                userIds = new ArrayList<>();
                for (UMUserRole uMUserRole : retrieveUserRoleByRoleId) {
                    userIds.add(uMUserRole.getuMUserRolePK().getUserobj());
                }
                if (!CollectionUtils.isEmpty(userIds)) {
                    Map<String, Object> equal = new HashMap<>();
                    equal.put(UMUserDao.COMPANY, companyId);
                    equal.put(UMUserDao.IS_ACTIVE, true);
                    List<String> projections = new ArrayList<>();
                    projections.add(UMUserDao.FIRST_NAME);
                    projections.add(UMUserDao.LAST_NAME);
                    projections.add(UMUserDao.USER_CODE);
                    projections.add(UMUserDao.ID);
                    List<String> match = new ArrayList<>();
                    match.add(UMUserDao.FIRST_NAME);
                    match.add(UMUserDao.LAST_NAME);
                    match.add(UMUserDao.USER_CODE);
                    Map<List<String>, Object> matchname = new HashMap<>();
                    if (!StringUtils.isEmpty(search)) {
                        matchname.put(match, search);
                    }
                    Map<String, Object> in = new HashMap<>();
                    in.put(UMUserDao.ID, userIds);

                    Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                    criterias.put(GenericDao.QueryOperators.EQUAL, equal);
                    if (!StringUtils.isEmpty(search) && !CollectionUtils.isEmpty(matchname)) {
                        criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, matchname);
                    }
                    criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMUserDao.FIRST_NAME);
//                    criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
                    criterias.put(GenericDao.QueryOperators.IN, in);

                    users = userService.retrieveUsers(projections, criterias, null);
                }
            }

            return users;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles by id", ex);
            return null;
        }
    }

    public List<UMCompany> reteievePendingCompanies() throws GenericDatabaseException {
        Map<String, Object> equal = new HashMap<>();
        equal.put(UMCompanyDao.DESCRIPTION, HkSystemConstantUtil.SETUP_PENDING);
        Map<QueryOperators, Object> criteria = new HashMap<>();
        criteria.put(QueryOperators.EQUAL, equal);
        return companyService.retrieveCompanies(null, criteria, null);
    }

    public List<UMDepartment> retrieveAllDepartments(List<Long> deptIds) {
        List<UMDepartment> departments = new ArrayList<>();
        Map<QueryOperators, Object> criterias = new HashMap<>();
        Map<String, Object> equalMap = new HashMap<>();
        equalMap.put(UMDepartmentDao.IS_ACTIVE, true);
        equalMap.put(UMDepartmentDao.IS_ARCHIVE, false);

        Map<String, Object> inMap = new HashMap<>();
        inMap.put(UMDepartmentDao.ID, deptIds);
        criterias.put(QueryOperators.IN, inMap);
        criterias.put(QueryOperators.EQUAL, equalMap);

        List<String> requires = new ArrayList<>();
        requires.add(UMDepartmentDao.PARENT);
        requires.add(UMDepartmentDao.UM_DEPARTMENT_SET);

        try {
            departments = departmentService.retrieveDepartments(null, criterias, requires);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(UMDepartmentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departments;
    }

    public List<UMUserRole> retrieveUserRolesByUserId(Long userId, boolean isRequired, Boolean inActive) {
        try {
            Map<String, Object> equal = new HashMap<>();
            equal.put(UMUserRoleDao.UM_USER_ROLE_PK + ".userobj", userId);
            equal.put(UMUserRoleDao.IS_ARCHIVE, inActive);
            equal.put(UMUserRoleDao.IS_ACTIVE, !inActive);
            List<String> require = new ArrayList<>();
            if (isRequired) {
                require.add(UMUserRoleDao.UM_USER);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);

            List<UMUserRole> retrieveUserRoleByRoleId = roleService.retrieveUserRoles(null, criterias, require);
            return retrieveUserRoleByRoleId;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles by id", ex);
            return null;
        }
    }

    public Boolean checkIfDepartmentIsInDesignation(Long depId, Long companyId) {
        Boolean checkIfDepExist = false;
        List<UMRole> userRoles = null;

        Map<QueryOperators, Object> criteriasMap = new HashMap<>();

        Map<String, Object> queryEquals = new HashMap<>();
        queryEquals.put(UMRoleDao.IS_ACTIVE, Boolean.TRUE);

        Map<String, Object> queryIn = new HashMap<>();
        queryIn.put(UMRoleDao.COMPANY, getCompnies(companyId));
        queryEquals.put(UMRoleDao.CUSTOM1, depId);

        criteriasMap.put(QueryOperators.EQUAL, queryEquals);
        criteriasMap.put(QueryOperators.IN, queryIn);
        criteriasMap.put(QueryOperators.ORDER_ASC, UMRoleDao.CREATED_ON);

        try {
            userRoles = roleService.retrieveRoles(null, criteriasMap, null);
            if (!CollectionUtils.isEmpty(userRoles)) {
                checkIfDepExist = true;
            }
        } catch (GenericDatabaseException e) {
            LOGGER.error("Error retriving Roles", e);
        }
        return checkIfDepExist;

    }

    public Map<Long, String> retrieveMapOfDesgIdWithName(List<Long> desgIds) {

        Map<Long, String> mapOfDesgIdWithName = null;
        List<UMRole> designationByIds = this.retrieveDesignationByIds(desgIds);
        if (!CollectionUtils.isEmpty(designationByIds)) {
            mapOfDesgIdWithName = new HashMap<>();
            for (UMRole role : designationByIds) {
                mapOfDesgIdWithName.put(role.getId(), role.getName());
            }
        }
        return mapOfDesgIdWithName;
    }

    public List<UMFeature> retrieveAllDiamondFeatures(Long companyId) {
        try {
            Map<String, Object> in = new HashMap<>();
            if (companyId != null) {
                in.put(UMFeatureDao.COMPANY, getCompnies(companyId));
            }

            Map<String, Object> equal = new HashMap<>();
            equal.put(UMFeatureDao.IS_ACTIVE, Boolean.TRUE);
            equal.put(UMFeatureDao.MENU_TYPE, HkSystemConstantUtil.DiamondFeatureTypes.MENU);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, in);
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMFeatureDao.MENU_LABEL);
            List<UMFeature> uMFeatures = featureService.retrieveFeatures(null, criterias, null);
            return uMFeatures;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving features", ex);
            return null;
        }
    }

    public List<UMRole> retrieveChildRolesByCompany(Long companyId, String role, String parentRole, Boolean isActive, boolean fetchRoleFeatures) {
        try {
            Map<String, Object> equal = new HashMap<>();
            if (companyId != null && !companyId.equals(0l)) {
                equal.put(UMRoleDao.COMPANY, getCompnies(companyId));
            }
            if (parentRole != null) {
                equal.put(UMRoleDao.CUSTOM2, parentRole);
            }
            equal.put(UMRoleDao.IS_ACTIVE, isActive);
            List<String> requires = null;
            if (fetchRoleFeatures) {
                requires = new ArrayList<>();
                requires.add(UMRoleDao.UM_ROLE_FEATURE_SET);
            }
            List<String> projections = new ArrayList<>();
            projections.add(UMRoleDao.NAME);
            projections.add(UMRoleDao.ID);
            Map<String, Object> in = new HashMap<>();
            if (role != null) {
                in.put(UMRoleDao.NAME, role);
            }
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.EQUAL, equal);
            criterias.put(GenericDao.QueryOperators.IGNORE_CASE_LIKE_MATCH_ANYWHERE, in);
            criterias.put(GenericDao.QueryOperators.ORDER_ASC, UMRoleDao.NAME);
//            criterias.put(GenericDao.QueryOperators.MAX_RESULT, HkSystemConstantUtil.MAX_RESULT);
            List<UMRole> roles = roleService.retrieveRoles(projections, criterias, requires);
            return roles;
        } catch (GenericDatabaseException ex) {
            LOGGER.error("Messaging->Error retrieving Roles", ex);
            return null;
        }
    }

    public UMUser getUserbyUserName(String username) {
        return userService.getUserbyUserName(username, false, false, false, false, false, true);
    }

    public UMUser retrieveUserById(Long id) {
        Search search = SearchFactory.getSearch(UMUser.class);
        search.addFilterEqual(UMUserDao.ID, id);

        search.addFetches(UMUserManagementConstants.UserObjects.USER_CONTACT,
                UMUserManagementConstants.UserObjects.USER_IP_ASSOCIATION_SET,
                UMUserManagementConstants.UserObjects.USER_ROLE_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT_SET,
                UMUserManagementConstants.UserObjects.USER_FEATURE_SET,
                UMUserManagementConstants.UserObjects.USER_GROUP_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT_SET + "." + UMUserManagementConstants.UserContactObjects.INSURANCE_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT_SET + "." + UMUserManagementConstants.UserContactObjects.DOCUMENT_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.ADDRESS_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.CONTACT_USER_OBJ,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.DOCUMENT_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.EDUCATION_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.EXP_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.INSURANCE_SET,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.USER_OBJ,
                UMUserManagementConstants.UserObjects.USER_CONTACT + "." + UMUserManagementConstants.UserContactObjects.USER_SET);

        UMUser user = (UMUser) commonDao.searchUnique(search);

        return user;
    }
}
