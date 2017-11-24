package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncCenterUserRoleDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author harshit
 */
@Service
public class HkUserServiceImpl implements HkUserService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    /**
     * String firstName.
     */
    public static final String FIRST_NAME = "firstName";
    /**
     * String middleName.
     */
    public static final String MIDDLE_NAME = "middleName";
    /**
     * String lastName.
     */
    public static final String LAST_NAME = "lastName";
    public static final String USER_CODE = "userCode";
    public static final String ID = "_id";
    public static final String IS_ACTIVE = "isActive";
    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String COMPANY_ID = "company";
    public static final String FRANCHISE = "franchiseId";
    public static final String DEPARTMENT = "departmentId";
    public static final String ROLE = "roleId";
    static final String IS_ARCHIVE = "isArchive";

    static class HkDepartmentDocumentField {

        static final String DEPT_NAME = "deptName";
        static final String ID = "_id";
        static final String IS_ACTIVE = "isActive";
        static final String PARENT = "parentId";
    }

    static class HkDesignationDocumentField {

        static final String DESIG_NAME = "name";
        static final String PRECEDENCE = "precedence";
        static final String DEPARTMENT = "department";
        static final String IS_ACTIVE = "isActive";
    }

    static class UmComapnyDocumentField {

        static final String IS_ARCHIVE = "isArchive";
        static final String COMPANY_NAME = "name";
        static final String COMPANY_ID = "id";
    }

    static class SyncUserField {

        static final String DEPARTMENT_ID = "departmentId";
        static final String IS_ACTIVE = "isActive";
        static final String ID = "id";
    }

    static class SyncUserRoleField {

        static final String USER_ID = "userId";
        static final String ROLE_ID = "roleId";
        static final String IS_ARCHIVE = "isArchive";
    }

    @Override
    public Map<String, String> retrieveRecipientNames(List<String> recipientCodes) {
        //System.out.println("In view coming to this method..........................");
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
                //System.out.println("Employee ids :: " + employeeIds);
                Query query = new Query();
                query.fields().include(FIRST_NAME);
                query.fields().include(LAST_NAME);
                query.fields().include(USER_CODE);
                query.fields().include(ID);

                query.addCriteria(Criteria.where(ID).in(employeeIds));

                //Criterias
                query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));

                List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
                if (!CollectionUtils.isEmpty(users)) {
                    for (SyncCenterUserDocument uMUser : users) {
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
            }

            if (!CollectionUtils.isEmpty(departmentIds)) {
                //System.out.println("in department..." + departmentIds);
                Query departmentQuery = new Query();
                departmentQuery.fields().include(HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME);
                departmentQuery.fields().include(ID);

                //Criterias
                departmentQuery.addCriteria(Criteria.where(ID).in(departmentIds));
                departmentQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME));

                List<Criteria> criterias = new ArrayList<>();
                criterias.add(Criteria.where(ID).in(departmentIds));
                List<HkDepartmentDocument> departments = mongoGenericDao.getMongoTemplate().find(departmentQuery, HkDepartmentDocument.class);
//                    List<HkDepartmentDocument> departments = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);

                if (!CollectionUtils.isEmpty(departments)) {
                    for (HkDepartmentDocument uMDepartment : departments) {
                        StringBuilder key = new StringBuilder(uMDepartment.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
                        recipientCodeNameMap.put(key.toString(), uMDepartment.getDeptName());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(otherFranchisedepartmentIds)) {
                Query otherFranDeQuery = new Query();
                otherFranDeQuery.fields().include(HkDepartmentDocumentField.DEPT_NAME);
                otherFranDeQuery.fields().include(ID);

                otherFranDeQuery.addCriteria(Criteria.where(ID).is(otherFranchisedepartmentIds));
                otherFranDeQuery.with(new Sort(Sort.Direction.ASC, HkDepartmentDocumentField.DEPT_NAME));

                List<HkDepartmentDocument> departments = mongoGenericDao.getMongoTemplate().find(otherFranDeQuery, HkDepartmentDocument.class);
                SyncCenterFranchiseDocument centerFranchiseDocument = mongoGenericDao.getMongoTemplate().findById(1, SyncCenterFranchiseDocument.class);
                if (centerFranchiseDocument != null) {
                    for (HkDepartmentDocument departmentDoc : departments) {
                        StringBuilder key = new StringBuilder(departmentDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT);
                        StringBuilder nameBuilder = new StringBuilder(departmentDoc.getDeptName());
                        nameBuilder.append(" - ");
                        nameBuilder.append(centerFranchiseDocument.getName());
                        recipientCodeNameMap.put(key.toString(), nameBuilder.toString());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(designationIds)) {

                Query desigQuery = new Query();
                desigQuery.addCriteria(Criteria.where(ID).in(designationIds));
                desigQuery.with(new Sort(Sort.Direction.ASC, HkDesignationDocumentField.DESIG_NAME));

                List<UmDesignationDocument> designations = mongoGenericDao.getMongoTemplate().find(desigQuery, UmDesignationDocument.class);
                if (!CollectionUtils.isEmpty(designations)) {
                    for (UmDesignationDocument desigDoc : designations) {
                        StringBuilder key = new StringBuilder(desigDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.DESIGNATION);
                        recipientCodeNameMap.put(key.toString(), desigDoc.getName());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(franchiseIds)) {

                Query franQuery = new Query();
                franQuery.addCriteria(Criteria.where(ID).in(franchiseIds));
                franQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.UmComapnyDocumentField.COMPANY_NAME));

                List<UmCompanyDocument> companies = mongoGenericDao.getMongoTemplate().find(franQuery, UmCompanyDocument.class);
                if (!CollectionUtils.isEmpty(companies)) {
                    for (UmCompanyDocument companyDoc : companies) {
                        StringBuilder key = new StringBuilder(companyDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.FRANCHISE);
                        recipientCodeNameMap.put(key.toString(), companyDoc.getName());
                    }
                }
            }
//            }

        }
        return recipientCodeNameMap;
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByActivityNodeDesignation(Long roleId, String user) {
        List<SyncCenterUserDocument> userList = new ArrayList<>();
        if (roleId != null) {

            userList = this.retrieveUsersByRoleId(Arrays.asList(roleId), user);
        }
        return userList;
    }

    public List<SyncCenterUserDocument> retrieveUsersByRoleId(List<Long> roleId, String user) {

        List<SyncCenterUserDocument> users = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("roleId").in(roleId));
        criterias.add(Criteria.where("isArchive").in(Boolean.FALSE));
        criterias.add(Criteria.where("isActive").in(Boolean.TRUE));
        if (user != null) {
            Criteria criteria = new Criteria().orOperator(
                    (Criteria.where(FIRST_NAME).regex(user, "i")),
                    (Criteria.where(LAST_NAME).regex(user, "i")),
                    (Criteria.where(USER_CODE).regex(user, "i")));
            criterias.add(criteria);
        }

        users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        return users;
    }

    public List<SyncCenterUserDocument> retrieveUsersByIds(List<Long> userIds) {
        List<SyncCenterUserDocument> centerUserDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userIds)) {
            criterias.add(Criteria.where(ID).in(userIds));
            return centerUserDocuments = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        }
        return centerUserDocuments;
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByCompany(Long companyId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        query.fields().include(USER_ID);
        query.fields().include(PASSWORD);
        query.fields().include(ID);
        if (!isLoggedInUserRequired) {
            query.addCriteria(Criteria.where(ID).ne(loggedInId));
        }

        List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public Set<Long> retrieveRecipientIds(List<String> recipientCodes) {
        Set<Long> recipientIdsSet = new HashSet<>();

        final String SEPARATOR = ":";
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            List<Long> employeeIds = new LinkedList<>();
            List<Long> departmentIds = new LinkedList<>();
            List<Long> designationIds = new LinkedList<>();

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
                            departmentIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DESIGNATION:
                            designationIds.add(id);
                            break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(employeeIds)) {
                recipientIdsSet.addAll(employeeIds);
            }

            if (!CollectionUtils.isEmpty(departmentIds)) {
                List<Criteria> criterias = new ArrayList<>();
                criterias.add(Criteria.where(SyncUserField.DEPARTMENT_ID).in(departmentIds));
                List<SyncCenterUserDocument> users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);

                if (!CollectionUtils.isEmpty(users)) {
                    for (SyncCenterUserDocument uMUser : users) {
                        recipientIdsSet.add(uMUser.getId());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(designationIds)) {
                List<Criteria> criterias = new ArrayList<>();
                criterias.add(Criteria.where(SyncUserRoleField.IS_ARCHIVE).is(false));
                criterias.add(Criteria.where(SyncUserRoleField.ROLE_ID).in(designationIds));

                List<SyncCenterUserRoleDocument> userRoles = mongoGenericDao.findByCriteria(criterias, SyncCenterUserRoleDocument.class);
                if (!CollectionUtils.isEmpty(userRoles)) {
                    for (SyncCenterUserRoleDocument uMUserRole : userRoles) {
                        recipientIdsSet.add(uMUserRole.getUserId());
                    }
                }

            }
        }
        return recipientIdsSet;
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsers(List<Long> ids) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(SyncUserField.IS_ACTIVE).is(true));
        criterias.add(Criteria.where(SyncUserField.ID).in(ids));

        return mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
    }

    public List<UmDesignationDocument> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive, Integer precedence) {
        Query desigQuery = new Query();
        desigQuery.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        if (role != null) {
            desigQuery.addCriteria(Criteria.where(HkDesignationDocumentField.DESIG_NAME).regex(role, "i"));
        }
        if (precedence != null) {
            desigQuery.addCriteria(Criteria.where(HkDesignationDocumentField.PRECEDENCE).gte(precedence));
        }
        desigQuery.with(new Sort(Sort.Direction.ASC, HkDesignationDocumentField.DESIG_NAME));
//        desigQuery.limit(HkSystemConstantUtil.MAX_RESULT);
        List<UmDesignationDocument> desigDocs = mongoGenericDao.getMongoTemplate().find(desigQuery, UmDesignationDocument.class);
        return desigDocs;
    }

    public List<HkDepartmentDocument> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        Query departmentQuery = new Query();
        departmentQuery.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        if (department != null) {
            departmentQuery.addCriteria(Criteria.where(HkDepartmentDocumentField.DEPT_NAME).regex(department, "i"));
        }
        departmentQuery.with(new Sort(Sort.Direction.ASC, HkDepartmentDocumentField.DEPT_NAME));
//        departmentQuery.limit(HkSystemConstantUtil.MAX_RESULT);
        List<HkDepartmentDocument> departments = mongoGenericDao.getMongoTemplate().find(departmentQuery, HkDepartmentDocument.class);
        return departments;
    }

    public List<SyncCenterUserDocument> retrieveUsersByCriteria(String user, Long companyId, Boolean isActive, List<Long> userIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        query.fields().include(FIRST_NAME);
        query.fields().include(LAST_NAME);
        query.fields().include(USER_CODE);
        query.fields().include(ID);

//        query.limit(HkSystemConstantUtil.MAX_RESULT);
        if (user != null) {
            Criteria criteria = new Criteria().orOperator(
                    (Criteria.where(FIRST_NAME).regex(user, "i")),
                    (Criteria.where(LAST_NAME).regex(user, "i")),
                    (Criteria.where(USER_CODE).regex(user, "i")));
            query.addCriteria(criteria);
        }
        if (companyId != null) {
            query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            query.addCriteria(Criteria.where(ID).in(userIds));
        }
        query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));
        List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
        return users;
    }

    public List<SyncCenterUserDocument> retrieveUsersByCompanyByStatus(String user, Long companyId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        query.fields().include(FIRST_NAME);
        query.fields().include(LAST_NAME);
        query.fields().include(USER_CODE);
        query.fields().include(ID);

//        query.limit(HkSystemConstantUtil.MAX_RESULT);
        if (user != null) {
            Criteria criteria = new Criteria().orOperator(
                    (Criteria.where(FIRST_NAME).regex(user, "i")),
                    (Criteria.where(LAST_NAME).regex(user, "i")),
                    (Criteria.where(USER_CODE).regex(user, "i")));
            query.addCriteria(criteria);
        }
        if (companyId != null) {
            query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        }
        query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));
        if (!isLoggedInUserRequired) {
            query.addCriteria(Criteria.where(ID).ne(loggedInId));
        }
        List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByDepartmentIds(Long companyId, String user, List<Long> depIds, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        query.fields().include(FIRST_NAME);
        query.fields().include(LAST_NAME);
        query.fields().include(USER_CODE);
        query.fields().include(DEPARTMENT);
        query.fields().include(ID);

//        query.limit(HkSystemConstantUtil.MAX_RESULT);
        if (user != null) {
            Criteria criteria = new Criteria().orOperator(
                    (Criteria.where(FIRST_NAME).regex(user, "i")),
                    (Criteria.where(LAST_NAME).regex(user, "i")),
                    (Criteria.where(USER_CODE).regex(user, "i")));
            query.addCriteria(criteria);
        }
        if (companyId != null) {
            query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        }
        if (!CollectionUtils.isEmpty(depIds)) {
            query.addCriteria(Criteria.where(DEPARTMENT).in(depIds));
        }
        query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));
        if (!isLoggedInUserRequired) {
            query.addCriteria(Criteria.where(ID).ne(loggedInId));
        }
        List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByRoleIds(Long companyId, String user, List<Long> roleIds, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(IS_ACTIVE).is(isActive));
        query.fields().include(FIRST_NAME);
        query.fields().include(LAST_NAME);
        query.fields().include(USER_CODE);
        query.fields().include(ID);

//        query.limit(HkSystemConstantUtil.MAX_RESULT);
        if (user != null) {
            Criteria criteria = new Criteria().orOperator(
                    (Criteria.where(FIRST_NAME).regex(user, "i")),
                    (Criteria.where(LAST_NAME).regex(user, "i")),
                    (Criteria.where(USER_CODE).regex(user, "i")));
            query.addCriteria(criteria);
        }
        if (companyId != null) {
            query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        }
        if (!CollectionUtils.isEmpty(roleIds)) {
            query.addCriteria(Criteria.where(ROLE).in(roleIds));
        }
        query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));
        if (!isLoggedInUserRequired) {
            query.addCriteria(Criteria.where(ID).ne(loggedInId));
        }
        List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public List<UmCompanyDocument> searchCompanyByName(String companyName) {
        Query companyQuery = new Query();
        companyQuery.addCriteria(Criteria.where(UmComapnyDocumentField.IS_ARCHIVE).is(false));
        if (companyName != null) {
            companyQuery.addCriteria(Criteria.where(UmComapnyDocumentField.COMPANY_NAME).regex(companyName, "i"));
        }
        companyQuery.with(new Sort(Sort.Direction.ASC, UmComapnyDocumentField.COMPANY_NAME));
//        companyQuery.limit(HkSystemConstantUtil.MAX_RESULT);
        List<UmCompanyDocument> companyDocs = mongoGenericDao.getMongoTemplate().find(companyQuery, UmCompanyDocument.class);
        return companyDocs;
    }

    @Override
    public UmCompanyDocument retrieveFranchiseById(Long franchiseId) {
        UmCompanyDocument umCompanyDocument = new UmCompanyDocument();
        List<Criteria> criterias = new ArrayList<>();
        if (franchiseId != null) {
            criterias.add(Criteria.where(ID).in(franchiseId));
            umCompanyDocument = (UmCompanyDocument) mongoGenericDao.findOneByCriteria(criterias, UmCompanyDocument.class);
        }
        return umCompanyDocument;
    }

    @Override
    public List<UmCompanyDocument> retrieveFranchises() {
        List<UmCompanyDocument> umCompanyDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(IS_ARCHIVE).in(Boolean.FALSE));
        umCompanyDocuments = mongoGenericDao.findByCriteria(criterias, UmCompanyDocument.class);
        return umCompanyDocuments;
    }

    @Override
    public List<HkDepartmentDocument> retrieveDepartmentsByIds(List<Long> ids) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkDepartmentDocumentField.IS_ACTIVE).is(true));
        criterias.add(Criteria.where(HkDepartmentDocumentField.ID).in(ids));

        return mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);
    }

    @Override
    public List<SyncCenterUserDocument> retrieveUsersByCompanyDesigDept(Long companyId, List<Long> roles, Long depatmentId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId) {
        List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where(IS_ACTIVE).is(isActive));
        if (companyId != null) {
            criterias.add(Criteria.where(COMPANY_ID).is(companyId));
        }
        if (!CollectionUtils.isEmpty(roles)) {
            criterias.add(Criteria.where(ROLE).in(roles));
        }
        if (depatmentId != null) {
            criterias.add(Criteria.where(DEPARTMENT).in(depatmentId));
        }

        if (!isLoggedInUserRequired) {
            criterias.add(Criteria.where(ID).ne(loggedInId));
        }
        List<SyncCenterUserDocument> users = mongoGenericDao.findByCriteria(criterias, SyncCenterUserDocument.class);
        return users;
    }

    @Override
    public List<HkDepartmentDocument> retrieveDepartmentsForTreeView(Long department) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkDepartmentDocument> result = new LinkedList<>();
        criterias.add(Criteria.where(HkDepartmentDocumentField.ID).is(department));
        List<HkDepartmentDocument> hkDepartmentDocumentList = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);
        while (!CollectionUtils.isEmpty(hkDepartmentDocumentList)) {
            result.addAll(hkDepartmentDocumentList);
            List<Long> ids = new ArrayList<>();
            for (HkDepartmentDocument hkDepartmentDocument : hkDepartmentDocumentList) {
                ids.add(hkDepartmentDocument.getId());
            }
            criterias = new ArrayList<>();
            criterias.add(Criteria.where(HkDepartmentDocumentField.PARENT).in(ids));
            hkDepartmentDocumentList = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);
        }

        return result;
    }

    @Override
    public Map<Long, HkDepartmentDocument> retrieveDepartmentMapByIds(List<Long> departmentIds) {
        Map<Long, HkDepartmentDocument> departmentMap = null;
        if (!CollectionUtils.isEmpty(departmentIds)) {
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(ID).in(departmentIds));

            List<HkDepartmentDocument> departments = null;
            departments = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);

            if (!CollectionUtils.isEmpty(departments)) {
                departmentMap = new LinkedHashMap<>();
                for (HkDepartmentDocument department : departments) {
                    departmentMap.put(department.getId(), department);
                }
            }
        }
        return departmentMap;
    }

    @Override
    public List<Long> retrieveAllParentDepartments(Long dept) {
        List<Long> result = new ArrayList<>();
        HkDepartmentDocument depts = (HkDepartmentDocument) mongoGenericDao.retrieveById(dept, HkDepartmentDocument.class);
        if (depts != null) {
            while (depts.getParentId() != null) {
                result.add(depts.getParentId());
                depts = (HkDepartmentDocument) mongoGenericDao.retrieveById(depts.getParentId(), HkDepartmentDocument.class);
            }
        }
        return result;
    }

    @Override
    public List<UmDesignationDocument> retrieveDesignationsByDepartment(Long depId) {
        List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where(HkDesignationDocumentField.DEPARTMENT).in(depId));
        criterias.add(Criteria.where(HkDesignationDocumentField.IS_ACTIVE).in(true));

        return mongoGenericDao.findByCriteria(criterias, UmDesignationDocument.class);
    }
}
