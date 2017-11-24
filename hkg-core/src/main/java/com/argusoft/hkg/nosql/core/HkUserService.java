/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author harshit
 */
public interface HkUserService {

    public List<SyncCenterUserDocument> retrieveUsersByActivityNodeDesignation(Long roleId, String user);

    public Map<String, String> retrieveRecipientNames(List<String> recipientCodes);

    public List<SyncCenterUserDocument> retrieveUsersByIds(List<Long> userIds);

    public List<SyncCenterUserDocument> retrieveUsersByCompany(Long companyId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId);

    public Set<Long> retrieveRecipientIds(List<String> recipientCodes);

    public List<SyncCenterUserDocument> retrieveUsers(List<Long> ids);

    public List<UmDesignationDocument> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive,Integer precedence);

    public List<HkDepartmentDocument> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive);

    public List<HkDepartmentDocument> retrieveDepartmentsByIds(List<Long> ids);

    public List<SyncCenterUserDocument> retrieveUsersByCriteria(String user, Long companyId, Boolean isActive, List<Long> userIds);

    public List<SyncCenterUserDocument> retrieveUsersByCompanyByStatus(String user, Long companyId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId);

    public List<UmCompanyDocument> searchCompanyByName(String companyName);

    public UmCompanyDocument retrieveFranchiseById(Long franchiseId);

    public List<SyncCenterUserDocument> retrieveUsersByDepartmentIds(Long companyId, String user, List<Long> depIds, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId);

    public List<SyncCenterUserDocument> retrieveUsersByRoleIds(Long companyId, String user, List<Long> roleIds, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId);

    public List<UmCompanyDocument> retrieveFranchises();

    public List<SyncCenterUserDocument> retrieveUsersByCompanyDesigDept(Long companyId, List<Long> roles, Long depatmentId, Boolean isActive, boolean isLoggedInUserRequired, Long loggedInId);

    public List<HkDepartmentDocument> retrieveDepartmentsForTreeView(Long department);
    
    public Map<Long, HkDepartmentDocument> retrieveDepartmentMapByIds(List<Long> departmentIds);
    
    public List<Long> retrieveAllParentDepartments(Long dept);
    
    public List<UmDesignationDocument> retrieveDesignationsByDepartment(Long depId);
}
