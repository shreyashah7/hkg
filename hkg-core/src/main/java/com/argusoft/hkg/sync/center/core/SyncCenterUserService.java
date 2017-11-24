/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.SyncCenterUserDocument;
import java.util.List;

/**
 *
 * @author shruti
 */
public interface SyncCenterUserService {

    /**
     * Long id, Primary key of user object.
     */
    public static final String ID = "id";
    /**
     * String user_id, username of the user.
     */
    public static final String USER_ID = "userId";
    /**
     * String password, password of the user. Mostly encrypted.
     */
    public static final String PASSWORD = "password";
    /**
     * String type.
     */
    public static final String TYPE = "type";
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
    /**
     * String emailAddress.
     */
    public static final String EMAIL_ADDRESS = "emailAddress";
    /**
     * String mobileNumber.
     */
    public static final String MOBILE_NUMBER = "mobileNumber";
    /**
     * long createdBy, id of the user who created this record.
     */
    public static final String CREATED_BY = "createdBy";
    /**
     * Date createdOn, date on which this record is created.
     */
    public static final String CREATED_ON = "createdOn";
    /**
     * Long modifiedBy, id of the user who has modified this record last.
     */
    public static final String MODIFIED_BY = "modifiedBy";
    /**
     * Date modifiedOn, date on which this record is last modified.
     */
    public static final String MODIFIED_ON = "modifiedOn";
    /**
     * Date expriedOn.
     */
    public static final String EXPIRED_ON = "expiredOn";
    /**
     * boolean isActive, specifies whether this record is active or not.
     */
    public static final String IS_ACTIVE = "isActive";
    /**
     * boolean isArchive, specifies whether this record is in system or not.
     */
    public static final String IS_ARCHIVE = "isArchive";
    /**
     * String status.
     */
    public static final String STATUS = "status";
    /**
     * Date lastLoginOn, last log in date of user.
     */
    public static final String LAST_LOGIN_ON = "lastLoginOn";
    /**
     * Long custom1, can be use for any extra purpose depending on system
     * requirement.
     */
    public static final String CUSTOM1 = "custom1";
    /**
     * String custom2, can be use for any extra purpose depending on system
     * requirement.
     */
    public static final String CUSTOM2 = "custom2";
    /**
     * Date custom3, can be use for any extra purpose depending on system
     * requirement.
     */
    public static final String CUSTOM3 = "custom3";
    /**
     * Long custom4, can be use for any extra purpose depending on system
     * requirement.
     */
    public static final String CUSTOM4 = "custom4";
    /**
     * Long company, reference to um_company_info table. FK not provided,
     * handled by code.
     */
    public static final String COMPANY = "company";
    /**
     * Long department, reference to um_dept_mst table. Fk not provided handled
     * by code.
     */
    public static final String DEPARTMENT = "department";
    /**
     * String userCode, normally used to store domain related user_code or
     * something.
     */
    public static final String USER_CODE = "userCode";
    /**
     * Long linkedWithUser, self reference handled by code.
     */
    public static final String LINKED_WITH_USER = "linkedWithUser";
    /**
     * Date joiningDate, specifies joining date.
     */
    public static final String JOINING_DATE = "joiningDate";
    /**
     * Long preferedLanguage, reference to um_contact_language_knwn_dtl.
     */
    public static final String PREFERRED_LANGUAGE = "preferredLanguage";
    /**
     * UMUserContact contact, reference to um_user_contact table.
     */
    public static final String CONTACT = "contact";
    /**
     * Set<UMUserRole> uMUserRoleSet, Set of the association table which defines
     * relationship between um_system_user and um_system_role.
     */
    public static final String USER_ROLE_COLLECTION = "uMUserRoleSet";

    public SyncCenterUserDocument getUserbyUserName(String userName, boolean isUserRoleRequired, Boolean isActive);

    /**
     * This method is used to create/update User
     *
     * @author shruti
     * @param document object of SyncCenterUserDocument
     * @return id of document
     */
    public Long saveOrUpdateUser(SyncCenterUserDocument document);

    /**
     * @Author dhwani
     * @param roleId List of roles
     * @param search search string for users (matches anywhere in
     * firstName,LastName or userCode)
     * @param companyId id of company
     * @return List of SyncCenterUserDocument
     */
    public List<SyncCenterUserDocument> retrieveUsersByRoleId(List<Long> roleId, String search, Long companyId);

    /**
     * @Author Shruti
     * @param userId user id
     * @return SyncCenterUserDocument
     */
    public SyncCenterUserDocument retrieveUsersById(Long userId);

    /**
     * @Author Gautam
     * @param userIds userIds of users
     * @return List of SyncCenterUserDocument
     */
    public List<SyncCenterUserDocument> retrieveUsersByIds(List<Long> userIds);

    /**
     * @Author dhwani
     * @param companyId id of company
     * @param departmentIds ids of department for fetching users
     * @return List of SyncCenterUserDocument
     */
    public List<SyncCenterUserDocument> retrieveUsersByDepartmentIds(List<Long> departmentIds, Long companyId);

}
