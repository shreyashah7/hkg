/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.database.HkHolidayDao;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkAssetIssueEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRecipientEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntityPK;
import com.argusoft.hkg.model.HkEventRegistrationFieldEntity;
import com.argusoft.hkg.model.HkEventRegistrationFieldValueEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkShiftDepartmentEntity;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkShiftRuleEntity;
import com.argusoft.hkg.model.HkShiftRuleEntityPK;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.usermanagement.common.constants.UMUserManagementConstants;
import com.argusoft.usermanagement.common.database.UMUserDao;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation class for HkHRService.
 *
 * @author Mital, Tejas
 */
@Service
@Transactional
public class HkHRServiceImpl extends HkCoreService implements HkHRService {

    /**
     * DAOs
     */
    @Autowired
    HkHolidayDao holidayDao;

    @Autowired
    HkFoundationService foundationService;
    @Autowired
    HKCategoryService categoryService;
    @Autowired
    HkNotificationService notificationService;
    @Autowired
    UMUserService userService;

    private static final String IS_ARCHIVE = "isArchive";
    private static final String FRANCHISE = "franchise";

    private static class HkHolidayField {

        public static final String TITLE = "holidayTitle";
        public static final String START_DATE = "startDt";
        public static final String END_DATE = "endDt";
    }

    private static class HkWorkflowField {

        private static final String DEPARTMENT = "department";
    }

    private static class HkWorkflowApproverField {

        private static final String WORKFLOW_ID = "hkWorkflowApproverEntityPK.workflow";
        private static final String REFERENCE_TYPE = "hkWorkflowApproverEntityPK.referenceType";
        private static final String REFERENCE_INSTANCE = "hkWorkflowApproverEntityPK.referenceInstance";
        private static final String LEVEL = "level";
    }

    private static class HkEventRegistrationFieldField {

        private static final String EVENT_ID = "event.id";
    }

    private static class HkEventRecipientField {

        private static final String EVENT_ID = "hkEventRecipientEntityPK.event";
        private static final String REFERENCE_TYPE = "hkEventRecipientEntityPK.referenceType";
        private static final String REFERENCE_INSTANCE = "hkEventRecipientEntityPK.referenceInstance";
        private static final String EVENT = "hkEventEntity";
    }

    private static class HkEventRegistrationField {

        private static final String USER_ID = "hkEventRegistrationEntityPK.userId";
        private static final String EVENT_ID = "hkEventRegistrationEntityPK.event";
    }

    private static class HkEventRegistrationFieldValueField {

        private static final String FIELD_ID = "hkEventRegistrationFieldValueEntityPK.registrationField";
        private static final String USER_ID = "hkEventRegistrationFieldValueEntityPK.userId";
        private static final String EVENT_ID = "event";
    }

    private static class HkEventField {

        private static final String EVENT_ID = "id";
        private static final String EVENT_TITLE = "eventTitle";
        private static final String BANNER_IMAGE_NAME = "bannerImageName";
        private static final String FROM_DATE = "frmDt";
        private static final String TO_DATE = "toDt";
        private static final String END_TIME = "endTime";
        private static final String STATUS = "status";
        private static final String REGISTRATION_FORM_NAME = "registrationFormName";
        private static final String CATEGORY_ID = "category.id";
        private static final String CATEGORY_TITLE = "category.categoryTitle";
        private static final String REGISTRATION_LAST_DATE = "registrationLastDt";
        private static final String CATEGORY = "category";
        private static final String PUBLISH_ON = "publishedOn";
        private static final String REGISTRATION_COUNT = "registrationCount";
        private static final String ADULT_COUNT = "adultCount";
        private static final String CHILD_COUNT = "childCount";
        private static final String GUEST_COUNT = "guestCount";
        private static final String EVENT_RECIPIENT_ENTITY_SET = "hkEventRecipientEntitySet";
        private static final String EVENT_REG_FIELD_SET = "hkEventRegistrationFieldEntitySet";
        private static final String EVENT_REG_SET = "hkEventRegistrationEntitySet";
        private static final String FOLDER_NAME = "folderName";
    }

    private static class HkShiftField {

        private static final String SHIFT_ID = "id";
        private static final String TITLE = "shiftTitle";
        private static final String TEMP_SHIFTS_SET = "hkShiftEntitySet";
        private static final String OVRRIDE_SHIFTS_SET = "hkOverrideShiftEntitySet";
        private static final String SHIFT_DETAILS_SET = "hkShiftDtlEntitySet";
        private static final String SHIFT_DEPTS_SET = "hkShiftDepartmentEntitySet";
        private static final String TEMP_SHIFT_FOR = "temporaryShiftFor";
        private static final String OVERRIDE_SHIFT_FOR = "overrideShiftFor";
        private static final String IS_DEFAULT = "isDefault";
        private static final String RULE_ENTITY_SET = "hkShiftRuleEntitySet";
        private static final String STATUS = "status";
        private static final String FROM_DATE = "frmDt";
        private static final String TO_DATE = "toDt";
        public static final String FRANCHISE = "franchise";
        public static final String END_DATE = "toDt";

    }

    private static class HkShiftDepartmentField {

        private static final String HK_SHIFT_ENTITY = "hkShiftEntity";
        private static final String HK_SHIFT_DEPARTMENT_ENTITY_PK = "hkShiftDepartmentEntityPK";
        private static final String HK_SHIFT_DEPARTMENT_ENTITY = HK_SHIFT_ENTITY + ".hkShiftDepartmentEntitySet";
        private static final String HK_SHIFT_DETAILS = HK_SHIFT_ENTITY + ".hkShiftDtlEntitySet";
        private static final String DEPARTMENT = HK_SHIFT_DEPARTMENT_ENTITY_PK + ".department";
        private static final String SHIFT_ID = HK_SHIFT_DEPARTMENT_ENTITY_PK + ".shift";
        private static final String SHIFT_FRANCHISE = HK_SHIFT_ENTITY + "." + FRANCHISE;
    }

    private static class HkShiftDetailField {

        private static final String IS_ARCHIVE = "isArchive";
    }

    /**
     * HOLIDAY
     */
    @Override
    public void saveHoliday(HkHolidayEntity holidayEntity, List<Long> notifyUserList) {
        String notInstanceType = HkSystemConstantUtil.NotificationInstanceType.UPDATE_HOLIDAY;
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.HOLIDAY_NAME, holidayEntity.getHolidayTitle());
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.HOLIDAY_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + holidayEntity.getStartDt().getTime());
        if (holidayEntity.getId() == null) {
            notInstanceType = HkSystemConstantUtil.NotificationInstanceType.ADD_HOLIDAY;
            commonDao.save(holidayEntity);
        } else {
            HkHolidayEntity oldHoliday = this.retrieveHolidayById(holidayEntity.getId());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.HOLIDAY_OLD_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + oldHoliday.getStartDt().getTime());
            commonDao.getCurrentSession().merge(holidayEntity);
        }

        //  Send notification
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.HOLIDAY,
                notInstanceType, valuesMap, holidayEntity.getId(), holidayEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);
    }

    @Override
    public HkHolidayEntity retrieveHolidayById(Long holidayId) {
        HkHolidayEntity holiday = commonDao.find(HkHolidayEntity.class, holidayId);
        if (holiday != null && holiday.getIsArchive()) {
            holiday = null;
        }
        return holiday;
    }

    @Override
    public void removeHoliday(HkHolidayEntity holidayEntity, List<Long> notifyUserList) {
        holidayEntity.setIsActive(false);
        holidayEntity.setIsArchive(true);
        holidayDao.saveOrUpdate(holidayEntity);

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.HOLIDAY_NAME, holidayEntity.getHolidayTitle());
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.HOLIDAY_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + holidayEntity.getStartDt().getTime());
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.HOLIDAY, HkSystemConstantUtil.NotificationInstanceType.REMOVE_HOLIDAY, valuesMap, holidayEntity.getId(), holidayEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);
    }

    @Override
    public void removeHoliday(Long holidayId, List<Long> notifyUserList) {
        HkHolidayEntity holidayEntity = holidayDao.retrieveById(holidayId);
        this.removeHoliday(holidayEntity, notifyUserList);
    }

    @Override
    public void saveAllHolidays(List<HkHolidayEntity> holidayEntities) {
        holidayDao.saveOrUpdateAll(holidayEntities);
    }

    @Override
    public List<HkHolidayEntity> retrieveAllHolidays(Long franchise, Boolean archiveStatus) {
        List<HkHolidayEntity> holidayList = holidayDao.retrieveAllHolidays(franchise, archiveStatus);
        return holidayList;
    }

    @Override
    public List<String> searchPreviousYearsHolidaysByTitle(String holidayTitle, Long franchise, Boolean archiveStatus) {
        return holidayDao.searchPreviousYearsHolidaysByTitle(holidayTitle, franchise, archiveStatus);
    }

    @Override
    public List<HkHolidayEntity> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Long franchise, Boolean archiveStatus) {
        List<HkHolidayEntity> holidayEntities = holidayDao.retrieveHolidaysByCriteria(holidayTitle, startDate, endDate, searchOnlyCurrentYear, franchise, archiveStatus);
        return holidayEntities;
    }

    @Override
    public boolean isHoliday(Date searchDate, Long franchise) {
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(searchDate);
        dateCal.set(Calendar.HOUR_OF_DAY, 0);
        dateCal.set(Calendar.MINUTE, 0);
        dateCal.set(Calendar.SECOND, 0);
        dateCal.set(Calendar.MILLISECOND, 0);

        List<HkHolidayEntity> holidayList = retrieveHolidaysByCriteria(null, dateCal.getTime(), dateCal.getTime(), true, franchise, Boolean.FALSE);
        //  if list is not empty, means holiday exists for that date and so return true else return false
        return !CollectionUtils.isEmpty(holidayList);
    }

    public Date clearDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public List<Date> retrieveDateListExcludingHoliday(Date fromDate, Date toDate, Long franchise) {
        final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
        List<Date> dateList = new LinkedList<>();
        List<HkHolidayEntity> retrieveHolidaysByCriteria = retrieveHolidaysByCriteria(null, null, toDate, true, franchise, Boolean.FALSE);
        List<Date> holidays = new ArrayList<>();
        if (!CollectionUtils.isEmpty(retrieveHolidaysByCriteria)) {
            for (HkHolidayEntity hkHolidayEntity : retrieveHolidaysByCriteria) {
                for (long t = hkHolidayEntity.getStartDt().getTime(); t < hkHolidayEntity.getEndDt().getTime(); t += DAY_IN_MILLIS) {
                    Date d = clearDate(new Date(t));
                    holidays.add(d);
                }
                holidays.add(hkHolidayEntity.getEndDt());
            }
        }
        for (long t = fromDate.getTime(); t < toDate.getTime(); t += DAY_IN_MILLIS) {
            Date d = clearDate(new Date(t));
            if (!holidays.contains(d)) {
                dateList.add(d);
            }
        }
        return dateList;
    }

    /**
     * Search criteria - Holiday name, from date, to date.
     *
     * @param searchString
     * @param franchise
     * @return
     */
    @Override
    public List<HkHolidayEntity> searchHolidays(String searchString, Long franchise) {
        Criteria criteria = commonDao.getCurrentSession().createCriteria(HkHolidayEntity.class);
        criteria.add(Restrictions.or(Restrictions.ilike(HkHolidayField.TITLE, "%" + searchString + "%"),
                getDateRestrictions(searchString, "start_dt"),
                getDateRestrictions(searchString, "end_dt")));
        criteria.add(Restrictions.eq(FRANCHISE, franchise));
        criteria.add(Restrictions.eq(IS_ARCHIVE, false));
        return criteria.list();
    }

    @Override
    public void createWorkflow(HkWorkflowEntity workflow, String deptName, List<Long> notifyUserList) {
        commonDao.save(workflow);

        if (!CollectionUtils.isEmpty(workflow.getHkWorkflowApproverEntitySet())) {
            for (HkWorkflowApproverEntity approver : workflow.getHkWorkflowApproverEntitySet()) {
                approver.getHkWorkflowApproverEntityPK().setWorkflow(workflow.getId());
            }
            commonDao.save(workflow.getHkWorkflowApproverEntitySet().toArray());
        }

        //  Send notification
        if (deptName != null) {
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DEPARTMENT_NAME, deptName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.WORKFLOW,
                    HkSystemConstantUtil.NotificationInstanceType.ADD_WORKFLOW, valuesMap, workflow.getId(), workflow.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        }
    }

    @Override
    public void createWorkflow(HkWorkflowEntity[] workflowEntityArr, String[] deptNames, List<Long>[] notifyUserArr) {
        commonDao.save(workflowEntityArr);

        List<HkWorkflowApproverEntity> approverList = new ArrayList<>();
        for (HkWorkflowEntity workflow : workflowEntityArr) {
            if (!CollectionUtils.isEmpty(workflow.getHkWorkflowApproverEntitySet())) {
                for (HkWorkflowApproverEntity approver : workflow.getHkWorkflowApproverEntitySet()) {
                    HkWorkflowApproverEntity approverEntity = new HkWorkflowApproverEntity(workflow.getId(), approver.getHkWorkflowApproverEntityPK().getReferenceType(), approver.getHkWorkflowApproverEntityPK().getReferenceInstance());
                    approverEntity.setIsArchive(false);
                    approverEntity.setLevel(approver.getLevel());
                    approverList.add(approverEntity);
                }
            }
        }
        commonDao.save(approverList.toArray());

        //  Send notifications
        if (deptNames != null && deptNames.length > 0) {
            Map<HkNotificationEntity, List<Long>> notificationMap = new HashMap<>();
            for (int count = 0; count < deptNames.length; count++) {
                Map<String, Object> valuesMap = new HashMap<>();
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DEPARTMENT_NAME, deptNames[count]);
                HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.WORKFLOW,
                        HkSystemConstantUtil.NotificationInstanceType.ADD_WORKFLOW,
                        valuesMap, workflowEntityArr[count].getId(), workflowEntityArr[count].getFranchise());
                notificationMap.put(notification, notifyUserArr[count]);
            }
            notificationService.sendNotifications(notificationMap);
        }
    }

    @Override
    public void updateWorkflow(HkWorkflowEntity workflow, Set<HkWorkflowApproverEntity> finalApproversSet, String departmentName, List<Long> notifyUserList) {
        commonDao.save(workflow);
        Search search = new Search(HkWorkflowApproverEntity.class);
        search.addFilterEqual(HkWorkflowApproverField.WORKFLOW_ID, workflow.getId());
        search.addFilterEqual(IS_ARCHIVE, false);
        List<HkWorkflowApproverEntity> oldApproverList = commonDao.search(search);
        Map<HkWorkflowApproverEntity, HkWorkflowApproverEntity> map = new HashMap<>();
        for (HkWorkflowApproverEntity hkWorkflowApproverEntity : finalApproversSet) {
            map.put(hkWorkflowApproverEntity, hkWorkflowApproverEntity);
        }
        for (HkWorkflowApproverEntity oldApprover : oldApproverList) {
            if (!finalApproversSet.contains(oldApprover)) {
                oldApprover.setIsArchive(true);
            } else {
                oldApprover.setLevel(map.get(oldApprover).getLevel());
                finalApproversSet.remove(oldApprover);
            }
        }
        if (!CollectionUtils.isEmpty(finalApproversSet)) {
            commonDao.save(finalApproversSet.toArray());
        }

        //  Send notification
        if (departmentName != null) {
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DEPARTMENT_NAME, departmentName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.WORKFLOW, HkSystemConstantUtil.NotificationInstanceType.UPDATE_WORKFLOW, valuesMap, workflow.getId(), workflow.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        }
    }

    @Override
    public void removeWorkflow(Long workflowId, Long removedBy) {
        HkWorkflowEntity workflowEntity = commonDao.find(HkWorkflowEntity.class, workflowId);
        workflowEntity.setIsArchive(true);
        workflowEntity.setLastModifiedBy(removedBy);
        workflowEntity.setLastModifiedOn(new Date());
        commonDao.save(workflowEntity);

        //  Remove approvers
        Set<HkWorkflowApproverEntity> workflowApproverSet = workflowEntity.getHkWorkflowApproverEntitySet();
        if (!CollectionUtils.isEmpty(workflowApproverSet)) {
            for (HkWorkflowApproverEntity approver : workflowApproverSet) {
                approver.setIsArchive(true);
            }
            commonDao.save(workflowApproverSet.toArray());
        }
    }

    @Override
    public List<Long> retrieveDepartmentIdsForExistingWorkflows(Long franchise) {
        Search search = new Search(HkWorkflowEntity.class);
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addField(HkWorkflowField.DEPARTMENT);

        return commonDao.search(search);
    }

    @Override
    public HkWorkflowEntity retrieveWorkflowById(Long workflowId, boolean includeApproverDetails) {
        HkWorkflowEntity workflowEntity = commonDao.find(HkWorkflowEntity.class, workflowId);
        if (includeApproverDetails) {
            workflowEntity.setHkWorkflowApproverEntitySet(new HashSet<>(this.retrieveWorkflowApproversByWorkflowId(workflowId, null, workflowEntity.getIsArchive())));
        }
        return workflowEntity;
    }

    @Override
    public List<HkWorkflowApproverEntity> retrieveWorkflowApproversByWorkflowId(Long workflowId, Integer level, Boolean archiveStatus) {
        Search search = new Search(HkWorkflowApproverEntity.class);
        search.addFilterEqual(HkWorkflowApproverField.WORKFLOW_ID, workflowId);
        if (level != null) {
            search.addFilterEqual(HkWorkflowApproverField.LEVEL, level);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        return commonDao.search(search);
    }

    @Override
    public HkWorkflowEntity retrieveWorkflowForDepartment(Long departmentId, Long franchise, boolean includeApproverDetails) {
        Search search = new Search(HkWorkflowEntity.class);
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (departmentId == null) {
            //  if department id is null, fetch the default workflow
            search.addFilterNull(HkWorkflowField.DEPARTMENT);
        } else {
            //  Check if the department id matches and also retrieve the default workflow
            search.addFilterOr(Filter.equal(HkWorkflowField.DEPARTMENT, departmentId),
                    Filter.isNull(HkWorkflowField.DEPARTMENT));
        }
        search.addFilterEqual(IS_ARCHIVE, false);
        List<HkWorkflowEntity> workflowList = commonDao.search(search);

        HkWorkflowEntity workflow = null;
        if (!CollectionUtils.isEmpty(workflowList)) {
            if (workflowList.size() == 1) {
                //  if one object found, means no workflow exist for given department, return the default one which is found
                workflow = workflowList.get(0);
            } else {
                //  if more than one workflow found, means workflow for given department exist, return it
                for (HkWorkflowEntity workflowEntity : workflowList) {
                    if (workflowEntity.getDepartment() != null && workflowEntity.getDepartment().equals(departmentId)) {
                        workflow = workflowEntity;
                        break;
                    }
                }
            }
        }

        if (workflow != null && includeApproverDetails) {
            workflow.setHkWorkflowApproverEntitySet(new HashSet<>(this.retrieveWorkflowApproversByWorkflowId(workflow.getId(), null, workflow.getIsArchive())));
        }

        return workflow;
    }

    @Override
    public List<HkWorkflowApproverEntity> retrieveWorkFlowApprover(Long user, Long department) {
        Search search = SearchFactory.getSearch(HkWorkflowApproverEntity.class);

        search.addFilterOr(Filter.and(Filter.equal(HkWorkflowApproverField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.EMPLOYEE),
                Filter.equal(HkWorkflowApproverField.REFERENCE_INSTANCE, user)),
                Filter.and(Filter.equal(HkWorkflowApproverField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT),
                        Filter.equal(HkWorkflowApproverField.REFERENCE_INSTANCE, department)));
        return commonDao.search(search);
    }

    @Override
    public boolean issueAssets(List<HkAssetIssueEntity> assetIssueEntities, String empCode, String empName, String assignerCode, String assignerName, String departmentName, List<Long> notifyUserList) {
        if (!CollectionUtils.isEmpty(assetIssueEntities)) {
            Map<HkNotificationEntity, List<Long>> notificationMap = new HashMap<>();
            commonDao.saveAll(assetIssueEntities);
            for (HkAssetIssueEntity assetIssueEntity : assetIssueEntities) {
                if (assetIssueEntity.getAsset() != null && assetIssueEntity.getAsset().getId() != null) {
                    HkAssetEntity assetEntity = foundationService.retrieveAsset(assetIssueEntity.getAsset().getId());
                    if (assetEntity != null) {
                        assetEntity.setAssetLastIssued(assetIssueEntity);
                        //  Create notification and add to the map
                        Map<String, Object> valuesMap = new HashMap<>();
                        String instanceType = null;
                        if (!StringUtils.isEmpty(assetEntity.getAssetType())) {
                            if (assetEntity.getAssetType().equalsIgnoreCase(HkSystemConstantUtil.ASSET_TYPE.MANAGED)) {
                                assetEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.ISSUED);
                                if (empName != null) {
                                    instanceType = HkSystemConstantUtil.NotificationInstanceType.ISSUE_ASSET_MANAGED_EMP;
                                }
                                if (departmentName != null) {
                                    instanceType = HkSystemConstantUtil.NotificationInstanceType.ISSUE_ASSET_MANAGED_DPT;
                                }
                                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.MODEL_NUMBER, assetEntity.getModelNumber());
                            } else {

                                Integer remainingUnits = assetEntity.getRemainingUnits();
                                if (remainingUnits != null && assetIssueEntity.getIssuedUnits() != null && remainingUnits - assetIssueEntity.getIssuedUnits() > 0) {
                                    assetEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE);
                                    assetEntity.setRemainingUnits(remainingUnits - assetIssueEntity.getIssuedUnits());
                                } else {
                                    assetEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.ISSUED);
                                    assetEntity.setRemainingUnits(0);
                                }
                                if (empName != null) {
                                    instanceType = HkSystemConstantUtil.NotificationInstanceType.ISSUE_ASSET_NON_MANAGED_EMP;
                                }
                                if (departmentName != null) {
                                    instanceType = HkSystemConstantUtil.NotificationInstanceType.ISSUE_ASSET_NON_MANAGED_DPT;
                                }
                                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.NO_OF_UNITS, assetIssueEntity.getIssuedUnits());
                            }

                            //  Put notification to map
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, empCode);
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, empName);
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.ASSIGNER_CODE, assignerCode);
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.ASSIGNER_NAME, assignerName);
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DEPARTMENT_NAME, departmentName);
                            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.ASSET_NAME, assetEntity.getAssetName());

                            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.ASSET,
                                    instanceType, valuesMap, assetIssueEntity.getId(), assetIssueEntity.getFranchise());
                            notificationMap.put(notification, notifyUserList);
                        }
                    }
                    assetIssueEntity.setAsset(assetEntity);
                }
            }

            //  Send all notifications together
            notificationService.sendNotifications(notificationMap);
            return true;
        }
        return false;
    }

    /*
     * Methods for Event start.............
     */
    @Override
    public void createEvent(HkEventEntity eventEntity, Map<String, List<Long>> notifyUserMap) {
//        if (eventEntity.getCategory().getId() == null) {
//            categoryService.createCategory(eventEntity.getCategory(), CategoryType.EVENT);
//        }
        Calendar todayCal = Calendar.getInstance();
        Set<HkEventRecipientEntity> recipientSet = eventEntity.getHkEventRecipientEntitySet();
        Set<HkEventRegistrationFieldEntity> registrationFieldSet = eventEntity.getHkEventRegistrationFieldEntitySet();

        eventEntity.setAdultCount(0);
        eventEntity.setChildCount(0);
        eventEntity.setGuestCount(0);
        eventEntity.setRegistrationCount(0);
        eventEntity.setNotAttendingCount(0);

//        eventEntity.setHkEventRecipientEntitySet(null);
//        eventEntity.setHkEventRegistrationFieldEntitySet(null);
        if (eventEntity.getFrmDt().equals(todayCal.getTime()) || eventEntity.getFrmDt().before(todayCal.getTime())) {
            eventEntity.setStatus(HkSystemConstantUtil.EventStatus.ONGOING);
        } else if (eventEntity.getPublishedOn().before(todayCal.getTime())) {
            //  if publish date today's date, status = upcoming
            eventEntity.setStatus(HkSystemConstantUtil.EventStatus.UPCOMING);
        } else {
            //  if publish date is future's date, status = created
            eventEntity.setStatus(HkSystemConstantUtil.EventStatus.CREATED);
        }

        commonDao.save(eventEntity);

//        if (!CollectionUtils.isEmpty(recipientSet)) {
//            for (HkEventRecipientEntity recipient : recipientSet) {
//                recipient.getHkEventRecipientEntityPK().setEvent(eventEntity.getId());
//            }
//            commonDao.save(recipientSet.toArray());
//        }
        if (!CollectionUtils.isEmpty(registrationFieldSet)) {
            for (HkEventRegistrationFieldEntity regField : registrationFieldSet) {
                regField.setEvent(eventEntity);
            }
            commonDao.save(registrationFieldSet.toArray());
        }

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();
        List<Long> notifyUserList = new ArrayList<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_TITLE, eventEntity.getEventTitle());
        HkNotificationEntity notification = null;
        if (eventEntity.getPublishedOn().before(todayCal.getTime()) || eventEntity.getPublishedOn().equals(todayCal.getTime())) {
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_PUBLISH_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + eventEntity.getPublishedOn().getTime());
            notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.PUBLISH_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
            if (!CollectionUtils.isEmpty(notifyUserMap) && !CollectionUtils.isEmpty(notifyUserMap.get("invitee"))) {
                for (Long inviteeList : notifyUserMap.get("invitee")) {
                    notifyUserList.add(inviteeList);
                }
            }
            if (!CollectionUtils.isEmpty(notifyUserMap) && !CollectionUtils.isEmpty(notifyUserMap.get("haveFeature"))) {
                for (Long haveFeatureList : notifyUserMap.get("haveFeature")) {
                    notifyUserList.add(haveFeatureList);
                }
            }
            if (!CollectionUtils.isEmpty(notifyUserList)) {
                notificationService.sendNotification(notification, new HashSet<>(notifyUserList));
            }
            valuesMap.clear();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_TITLE, eventEntity.getEventTitle());
            notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.ADD_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
            if (!CollectionUtils.isEmpty(notifyUserMap) && !CollectionUtils.isEmpty(notifyUserMap.get("haveFeature"))) {
                notificationService.sendNotification(notification, new HashSet<>(notifyUserMap.get("haveFeature")));
            }

        } else {
            notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.ADD_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
            if (!CollectionUtils.isEmpty(notifyUserMap) && notifyUserMap.containsKey("haveFeature") && !CollectionUtils.isEmpty(notifyUserMap.get("haveFeature"))) {
                notificationService.sendNotification(notification, new HashSet<>(notifyUserMap.get("haveFeature")));
            }
        }
    }

    @Override
    public void updateEvent(HkEventEntity eventEntity, Set<Long> newUsersSet, List<Long> notifyUserList, boolean isFormChanged) {
//        if (eventEntity.getCategory().getId() == null) {
//            categoryService.createCategory(eventEntity.getCategory(), CategoryType.EVENT);
//        }
        Set<HkEventRecipientEntity> newRecipientSet = eventEntity.getHkEventRecipientEntitySet();
        Set<HkEventRegistrationFieldEntity> newRegFieldSet = eventEntity.getHkEventRegistrationFieldEntitySet();

        //  Archive old fields
        Search search = SearchFactory.getSearch(HkEventRecipientEntity.class);
        search.addFilterEqual(HkEventRecipientField.EVENT_ID, eventEntity.getId());
        List<HkEventRecipientEntity> oldRecipientList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(oldRecipientList)) {
            for (HkEventRecipientEntity recipient : oldRecipientList) {
                recipient.setIsArchive(true);
            }
        }
        if (!CollectionUtils.isEmpty(newRecipientSet)) {
            for (HkEventRecipientEntity recipient : newRecipientSet) {
                recipient.setIsArchive(false);
                recipient.getHkEventRecipientEntityPK().setEvent(eventEntity.getId());
                recipient.setHkEventEntity(eventEntity);
            }
        }

        //  Archive old recipients
        search = SearchFactory.getSearch(HkEventRegistrationFieldEntity.class);
        search.addFilterEqual(HkEventRegistrationFieldField.EVENT_ID, eventEntity.getId());
        List<HkEventRegistrationFieldEntity> oldFieldList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(oldFieldList)) {
            for (HkEventRegistrationFieldEntity reg : oldFieldList) {
                reg.setIsArchive(true);
            }
        }
        if (!CollectionUtils.isEmpty(newRegFieldSet)) {
            for (HkEventRegistrationFieldEntity reg : newRegFieldSet) {
                reg.setIsArchive(false);
                reg.setEvent(eventEntity);
            }
        }

        boolean eventPublished = false;
        Calendar todayCal = Calendar.getInstance();
        //  if event's publish on date is before current date and it's status is "Created", publish it now
        if (eventEntity.getPublishedOn().before(todayCal.getTime())
                && eventEntity.getStatus().equals(HkSystemConstantUtil.EventStatus.CREATED)) {
            eventEntity.setStatus(HkSystemConstantUtil.EventStatus.UPCOMING);
            //  Event is getting published so send the notification......
            eventPublished = true;

        } else if (eventEntity.getPublishedOn().after(todayCal.getTime())
                && eventEntity.getStatus().equals(HkSystemConstantUtil.EventStatus.UPCOMING)) {
            //  if publish date is future's date and status is "upcoming", change it to "created"
            eventEntity.setStatus(HkSystemConstantUtil.EventStatus.CREATED);
        }

        List<HkEventRegistrationEntity> userRegistrationList = this.retrieveUserEventRegistrationEntities(eventEntity.getId(), null, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(userRegistrationList)) {
            for (HkEventRegistrationEntity userReg : userRegistrationList) {
                //  if new user set does not contain existing registered user, archive this object

                if (newUsersSet != null && !newUsersSet.isEmpty() && !newUsersSet.contains(userReg.getHkEventRegistrationEntityPK().getUserId())) {
                    userReg.setIsArchive(true); //  object should be merged in session, no need to update it

                    //  deduct the total no. of registrations done by this user
                    eventEntity.setAdultCount(eventEntity.getAdultCount() - userReg.getAdultCount());
                    eventEntity.setChildCount(eventEntity.getChildCount() - userReg.getChildCount());
                    eventEntity.setGuestCount(eventEntity.getGuestCount() - userReg.getGuestCount());
                    eventEntity.setRegistrationCount(eventEntity.getRegistrationCount()
                            - (userReg.getAdultCount() + userReg.getChildCount() + userReg.getGuestCount()));
                }
            }
        }
        commonDao.getCurrentSession().merge(eventEntity);

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();
        HkNotificationEntity notification = null;
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_TITLE, eventEntity.getEventTitle());
        if (eventPublished) {
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_PUBLISH_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + eventEntity.getPublishedOn().getTime());
            notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.PUBLISH_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
        } else {
            if (isFormChanged) {
                notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.UPDATE_EVENT_REG_FORM, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
            } else {
                notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.UPDATE_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
            }
        }
        if (!CollectionUtils.isEmpty(notifyUserList)) {
            notificationService.sendNotification(notification, new HashSet<>(notifyUserList));
        }
    }

    @Override
    public void updateEvents(List<HkEventEntity> eventList, Set<Long> notifyUserList) {
        commonDao.saveAll(eventList);
        if (!CollectionUtils.isEmpty(notifyUserList)) {
            for (HkEventEntity eventEntity : eventList) {
                Map<String, Object> valuesMap = new HashMap<>();
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_TITLE, eventEntity.getEventTitle());
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EVENT_PUBLISH_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + eventEntity.getPublishedOn().getTime());
                HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.EVENT, HkSystemConstantUtil.NotificationInstanceType.PUBLISH_EVENT, valuesMap, eventEntity.getId(), eventEntity.getFranchise());
                if (!CollectionUtils.isEmpty(notifyUserList)) {
                    notificationService.sendNotification(notification, new HashSet<>(notifyUserList));
                }
            }
        }
    }

    @Override
    public void removeEvent(Long eventId, Long removedBy) {
        HkEventEntity event = commonDao.find(HkEventEntity.class, eventId);
        event.setIsArchive(true);
        event.setLastModifiedBy(removedBy);
        event.setLastModifiedOn(new Date());
        commonDao.save(event);
    }

    @Override
    public void archiveEvent(Long eventId, Long archivedBy) {
        HkEventEntity event = commonDao.find(HkEventEntity.class, eventId);
        event.setStatus(HkSystemConstantUtil.EventStatus.COMPLETED_ARCHIVED);
        event.setLastModifiedBy(archivedBy);
        event.setLastModifiedOn(new Date());
        commonDao.save(event);
    }

    @Override
    public List<HkEventEntity> searchEvents(String searchStr, Long franchise) {
//        Map<Long, String> finalMap = null;
        List<HkEventEntity> resultEvents = null;
        if (StringUtils.hasText(searchStr) && searchStr.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            boolean isCategory = searchStr.toUpperCase().startsWith(HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY.toUpperCase());

            Criteria criteria = commonDao.getCurrentSession().createCriteria(HkEventEntity.class);
            criteria.add(Restrictions.eq(IS_ARCHIVE, false));
            if (isCategory) {
                searchStr = searchStr.substring(searchStr.toUpperCase().indexOf(HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY) + HkSystemConstantUtil.ASSET_SEARCH_CODE.CATEGORY.length()).trim();
                List<HkCategoryEntity> categories = categoryService.retrieveCategoryBasedonSearchCriteria(searchStr, franchise, CategoryType.EVENT);
                if (!CollectionUtils.isEmpty(categories)) {
                    List<Long> catIds = new ArrayList<>();
                    for (HkCategoryEntity category : categories) {
                        catIds.add(category.getId());
                    }
                    criteria.add(Restrictions.in(HkEventField.CATEGORY_ID, catIds));
                    resultEvents = criteria.list();
                }
            } else {
                List<String> statusList = new ArrayList<>();
                for (String statusKey : HkSystemConstantUtil.EVENT_STATUS_MAP.keySet()) {
                    if (HkSystemConstantUtil.EVENT_STATUS_MAP.get(statusKey).toLowerCase().contains(searchStr.toLowerCase())) {
                        statusList.add(statusKey);
                    }
                }
                if (!CollectionUtils.isEmpty(statusList)) {
                    criteria.add(Restrictions.or(
                            Restrictions.ilike(HkEventField.EVENT_TITLE, "%" + searchStr + "%"),
                            Restrictions.in(HkEventField.STATUS, statusList),
                            getDateRestrictions(searchStr, "frm_dt"),
                            getDateRestrictions(searchStr, "to_dt")));
                } else {
                    criteria.add(Restrictions.or(
                            Restrictions.ilike(HkEventField.EVENT_TITLE, "%" + searchStr + "%"),
                            getDateRestrictions(searchStr, "frm_dt"),
                            getDateRestrictions(searchStr, "to_dt")));
                }
                resultEvents = criteria.list();
            }
        }

        return resultEvents;
    }

    @Override
    public List<HkEventEntity> retrieveEventsByCriteria(List<String> statusList, Long categoryId, Boolean archiveStatus, boolean includeRecipients, boolean includeRegFields, boolean includeReg) {
        Search search = new Search(HkEventEntity.class);
        if (!CollectionUtils.isEmpty(statusList)) {
            search.addFilterIn(HkEventField.STATUS, statusList);
        }
        if (categoryId != null) {
            search.addFilterEqual(HkEventField.CATEGORY_ID, categoryId);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        if (includeRecipients) {
            search.addFetch(HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);
        }
        if (includeReg) {
            search.addFetch(HkEventField.EVENT_REG_SET)
                    .setDistinct(true);
        }
        if (includeRegFields) {
            search.addFetch(HkEventField.EVENT_REG_FIELD_SET)
                    .setDistinct(true);
        }

        return commonDao.search(search);
    }

    @Override
    public Map<Long, String> retrieveEventFormNames(Long franchise) {
        Map<Long, String> resultMap = null;
        Search search = new Search(HkEventEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(FRANCHISE, franchise);
        search.addField(HkEventField.EVENT_ID);
        search.addField(HkEventField.REGISTRATION_FORM_NAME);
        search.addField(HkEventField.FROM_DATE);
        List<HkEventEntity> eventList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(eventList)) {
            resultMap = new HashMap<>();
            for (HkEventEntity event : eventList) {
                if(!StringUtils.isEmpty(event.getRegistrationFormName())){
                    resultMap.put(event.getId(), event.getRegistrationFormName() + " - " + new SimpleDateFormat("dd/MM/YYYY").format(event.getFrmDt()));
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<HkEventRegistrationFieldEntity> retrieveRegistrationFieldsByEventId(Long eventId) {
        Search search = new Search(HkEventRegistrationFieldEntity.class);
        search.addFilterEqual(HkEventRegistrationFieldField.EVENT_ID, eventId);
        search.addFilterEqual(IS_ARCHIVE, false);
        return commonDao.search(search);
    }

    @Override
    public List<HkEventEntity> retrieveUpcomingEvents(Long categoryId, Long departmentId, Long userId, Long franchiseId, boolean haveAddEditRights) {
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.CREATED);
        statusList.add(HkSystemConstantUtil.EventStatus.UPCOMING);
        statusList.add(HkSystemConstantUtil.EventStatus.ONGOING);

        if (departmentId == null && userId == null) {
            Search search = new Search(HkEventEntity.class);

            search.addFetch(HkEventField.CATEGORY);
            search.addFetch(HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);

            if (categoryId != null) {
                search.addFilterEqual(HkEventField.CATEGORY_ID, categoryId);
            }

            //  Fetch only active events            
            search.addFilterIn(HkEventField.STATUS, statusList);

            if (franchiseId != null) {
                search.addFilterEqual(FRANCHISE, franchiseId);
            }
            search.addFilterEqual(IS_ARCHIVE, false);
            //  Most recent event to show first
            search.addSortAsc(HkEventField.FROM_DATE);

            return commonDao.search(search);
        } else {
            List<HkEventEntity> eventList = null;

            Search search = SearchFactory.getSearch(HkEventRecipientEntity.class);
            search.addFetch(HkEventRecipientField.EVENT);
            search.addFetch(HkEventRecipientField.EVENT + "." + HkEventField.CATEGORY);
            search.addFetch(HkEventRecipientField.EVENT + "." + HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);

            if (departmentId == null) {
                departmentId = 0l;
            }
            if (userId == null) {
                userId = 0l;
            }
            if (haveAddEditRights && franchiseId != null) {
                search.addFilterOr(Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.EMPLOYEE),
                        Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, userId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.equal(HkEventRecipientField.EVENT + "." + FRANCHISE, franchiseId));
            } else {
                search.addFilterOr(Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.EMPLOYEE),
                        Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, userId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)));
            }

            if (categoryId != null) {
                search.addFilterEqual(HkEventRecipientField.EVENT + "." + HkEventField.CATEGORY_ID, categoryId);
            }

            search.addFilterIn(HkEventRecipientField.EVENT + "." + HkEventField.STATUS, statusList);

            search.addFilterEqual(HkEventRecipientField.EVENT + "." + IS_ARCHIVE, false);

            //  Most recent event to show first
            search.addSortAsc(HkEventRecipientField.EVENT + "." + HkEventField.FROM_DATE);
            List<HkEventRecipientEntity> eventRecipientFields = commonDao.search(search);

            if (!CollectionUtils.isEmpty(eventRecipientFields)) {
                eventList = new LinkedList<>();
                for (HkEventRecipientEntity eventRecipientEntity : eventRecipientFields) {
                    if (!eventList.contains(eventRecipientEntity.getHkEventEntity())) {
                        eventList.add(eventRecipientEntity.getHkEventEntity());
                    }
                }
            }
            return eventList;
        }
    }

    @Override
    public List<HkEventEntity> retrieveCompletedEvents(Long categoryId, Long departmentId, Long userId, Long franchiseId, boolean haveAddEditRights) {
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.COMPLETED);
        if (departmentId == null && userId == null) {
            Search search = new Search(HkEventEntity.class);

            search.addFetch(HkEventField.CATEGORY);
            search.addFetch(HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);

            if (categoryId != null) {
                search.addFilterEqual(HkEventField.CATEGORY_ID, categoryId);
            }

            //  Fetch only active events            
            search.addFilterIn(HkEventField.STATUS, statusList);

            if (franchiseId != null) {
                search.addFilterEqual(FRANCHISE, franchiseId);
            }
            search.addFilterEqual(IS_ARCHIVE, false);
            //  Most recent event to show first
            search.addSortDesc(HkEventField.TO_DATE)
                    .addSortDesc(HkEventField.END_TIME);
            search.setMaxResults(4);

            return commonDao.search(search);
        } else {
            List<HkEventEntity> eventList = null;

            Search search = SearchFactory.getSearch(HkEventRecipientEntity.class);
            search.addFetch(HkEventRecipientField.EVENT);
            search.addFetch(HkEventRecipientField.EVENT + "." + HkEventField.CATEGORY);
            search.addFetch(HkEventRecipientField.EVENT + "." + HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);

            if (departmentId == null) {
                departmentId = 0l;
            }
            if (userId == null) {
                userId = 0l;
            }
            if (haveAddEditRights && franchiseId != null) {
                search.addFilterOr(Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.EMPLOYEE),
                        Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, userId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.equal(HkEventRecipientField.EVENT + "." + FRANCHISE, franchiseId));
            } else {
                search.addFilterOr(Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.EMPLOYEE),
                        Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, userId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)),
                        Filter.and(Filter.equal(HkEventRecipientField.REFERENCE_TYPE, HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT),
                                Filter.equal(HkEventRecipientField.REFERENCE_INSTANCE, departmentId)));
            }

            if (categoryId != null) {
                search.addFilterEqual(HkEventRecipientField.EVENT + "." + HkEventField.CATEGORY_ID, categoryId);
            }

            search.addFilterIn(HkEventRecipientField.EVENT + "." + HkEventField.STATUS, statusList);

            search.addFilterEqual(HkEventRecipientField.EVENT + "." + IS_ARCHIVE, false);

            //  Most recent event to show first
            search.addSortDesc(HkEventRecipientField.EVENT + "." + HkEventField.TO_DATE)
                    .addSortDesc(HkEventRecipientField.EVENT + "." + HkEventField.END_TIME);
            List<HkEventRecipientEntity> eventRecipientFields = commonDao.search(search);
            search.setMaxResults(40);

            if (!CollectionUtils.isEmpty(eventRecipientFields)) {
                eventList = new LinkedList<>();
                for (HkEventRecipientEntity eventRecipientEntity : eventRecipientFields) {
                    if (!eventList.contains(eventRecipientEntity.getHkEventEntity())) {
                        eventList.add(eventRecipientEntity.getHkEventEntity());
                    }
                    if (eventList.size() == 4) {
                        break;
                    }
                }
            }
            return eventList;
        }
    }

    @Override
    public Map<HkCategoryEntity, Integer> retrieveActiveEventsCount(Long franchise) {
        Map<HkCategoryEntity, Integer> eventCountMap = new HashMap<>();

        List<HkCategoryEntity> categories = categoryService.retrieveAllCategories(franchise, CategoryType.EVENT);
        if (!CollectionUtils.isEmpty(categories)) {
            for (HkCategoryEntity hkCategoryEntity : categories) {
                eventCountMap.put(hkCategoryEntity, 0);
                HkCategoryEntity parentCat = hkCategoryEntity.getParent();
                int parentEventNum = 0;
                while (parentCat != null) {
                    if (eventCountMap.containsKey(parentCat)) {
                        parentEventNum = eventCountMap.get(parentCat);
                    }
                    eventCountMap.put(parentCat, parentEventNum);
                    parentCat = parentCat.getParent();
                }
            }
        }

        Search search = new Search(HkEventEntity.class);

        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        search.addFilterEqual(IS_ARCHIVE, false);

        search.addFetch(HkEventField.CATEGORY);

        List<HkEventEntity> resultEvents = commonDao.search(search);
        if (!CollectionUtils.isEmpty(resultEvents)) {
            for (HkEventEntity event : resultEvents) {
                HkCategoryEntity category = event.getCategory();
                if (!category.getIsArchive()) {
                    int eventNum = 0;
                    if (eventCountMap.containsKey(category)) {
                        eventNum = eventCountMap.get(category);
                    }
                    //  if event is active, put the count
                    if (event.getStatus().equals(HkSystemConstantUtil.EventStatus.CREATED)
                            || event.getStatus().equals(HkSystemConstantUtil.EventStatus.ONGOING)
                            || event.getStatus().equals(HkSystemConstantUtil.EventStatus.UPCOMING)) {
                        eventNum += 1;
                    }
                    HkCategoryEntity parentCat = category.getParent();
                    int parentEventNum = 0;
                    while (parentCat != null) {
                        if (eventCountMap.containsKey(parentCat)) {
                            parentEventNum = eventCountMap.get(parentCat);
                        }
                        eventCountMap.put(parentCat, parentEventNum);
                        parentCat = parentCat.getParent();
                    }
                    eventCountMap.put(category, eventNum);
                }
            }
        }

        return eventCountMap;
    }

    @Override
    public HkEventEntity retrieveEventById(Long eventId, boolean includeInvitees, boolean includeRegFields) {
        Search search = SearchFactory.getSearch(HkEventEntity.class);
        search.addFilterEqual(HkEventField.EVENT_ID, eventId);
        if (includeInvitees) {
            search.addFetch(HkEventField.EVENT_RECIPIENT_ENTITY_SET)
                    .setDistinct(true);
        }
        if (includeRegFields) {
            search.addFetch(HkEventField.EVENT_REG_FIELD_SET)
                    .setDistinct(true);
        }
        HkEventEntity eventEntity = (HkEventEntity) commonDao.searchUnique(search);
        if (includeInvitees && !CollectionUtils.isEmpty(eventEntity.getHkEventRecipientEntitySet())) {
            Iterator<HkEventRecipientEntity> itr = eventEntity.getHkEventRecipientEntitySet().iterator();
            while (itr.hasNext()) {
                HkEventRecipientEntity recipient = itr.next();
                if (recipient.getIsArchive()) {
                    itr.remove();
                }
            }
        }
        if (includeRegFields && !CollectionUtils.isEmpty(eventEntity.getHkEventRegistrationFieldEntitySet())) {
            Iterator<HkEventRegistrationFieldEntity> itr = eventEntity.getHkEventRegistrationFieldEntitySet().iterator();
            while (itr.hasNext()) {
                HkEventRegistrationFieldEntity regField = itr.next();
                if (regField.getIsArchive()) {
                    itr.remove();
                }
            }
        }

        return eventEntity;
    }

    @Override
    public List<HkEventRegistrationEntity> retrieveUserEventRegistrationEntities(Long eventId, Long userId, Boolean archiveStatus) {
        Search search = new Search(HkEventRegistrationEntity.class);
        if (eventId != null) {
            search.addFilterEqual(HkEventRegistrationField.EVENT_ID, eventId);
        }
        if (userId != null) {
            search.addFilterEqual(HkEventRegistrationField.USER_ID, userId);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        return commonDao.search(search);
    }

    @Override
    public List<HkEventRegistrationFieldValueEntity> retrieveEventRegistrationFieldValues(Long eventId, Long userId) {
        Search search = SearchFactory.getSearch(HkEventRegistrationFieldValueEntity.class);
        search.addFilterEqual(HkEventRegistrationFieldValueField.EVENT_ID, eventId);
        search.addFilterEqual(HkEventRegistrationFieldValueField.USER_ID, userId);
        return commonDao.search(search);
    }

    @Override
    public boolean doesEventNameExist(String eventTitle, Long franchise, Long skipEventId) {
        Search search = SearchFactory.getSearch(HkEventEntity.class);
        search.addFilterILike(HkEventField.EVENT_TITLE, eventTitle);
        //  Fetch only active event count
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.CREATED);
        statusList.add(HkSystemConstantUtil.EventStatus.UPCOMING);
        statusList.add(HkSystemConstantUtil.EventStatus.ONGOING);
        search.addFilterIn(HkEventField.STATUS, statusList);
        if (skipEventId != null) {
            search.addFilterNotEqual(HkEventField.EVENT_ID, skipEventId);
        }

        search.addFilterIn(FRANCHISE, getCompnies(franchise));

        return commonDao.count(search) > 0;
    }

    @Override
    public void registerForEvent(HkEventRegistrationEntity eventRegistrationEntity, Map<Long, String> fieldMap) {
        HkEventEntity eventEntity = this.retrieveEventById(eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent(), false, false);
        eventRegistrationEntity.setIsArchive(false);
        eventRegistrationEntity.setLastModifiedOn(new Date());
        commonDao.save(eventRegistrationEntity);

        if (eventRegistrationEntity.getStatus() == null) {
            eventRegistrationEntity.setStatus(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING);
        }

        if (eventRegistrationEntity.getStatus().equals(HkSystemConstantUtil.EventUserRegistrationStatus.ATTENDING)) {
            if (!CollectionUtils.isEmpty(fieldMap)) {
                List<HkEventRegistrationFieldValueEntity> fieldValueEntityList = new ArrayList<>();
                for (Long fieldId : fieldMap.keySet()) {
                    HkEventRegistrationFieldValueEntity fieldValueEntity = new HkEventRegistrationFieldValueEntity(fieldId, eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                    fieldValueEntity.setEvent(eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
                    fieldValueEntity.setFieldValue(fieldMap.get(fieldId));
                    fieldValueEntity.setIsArchive(false);
                    fieldValueEntityList.add(fieldValueEntity);
                }
                commonDao.saveAll(fieldValueEntityList);
            }

            //  Change the count in event info
            eventEntity.setAdultCount(eventEntity.getAdultCount() + eventRegistrationEntity.getAdultCount());
            eventEntity.setChildCount(eventEntity.getChildCount() + eventRegistrationEntity.getChildCount());
            eventEntity.setGuestCount(eventEntity.getGuestCount() + eventRegistrationEntity.getGuestCount());
            eventEntity.setRegistrationCount(eventEntity.getRegistrationCount() + eventRegistrationEntity.getAdultCount()
                    + eventRegistrationEntity.getChildCount() + eventRegistrationEntity.getGuestCount());
        } else {
            if (eventEntity.getNotAttendingCount() == null) {
                eventEntity.setNotAttendingCount(0);
            }
            eventEntity.setNotAttendingCount(eventEntity.getNotAttendingCount() + 1);
        }
        commonDao.getCurrentSession().merge(eventEntity);
    }

    @Override
    public void editEventRegistration(HkEventRegistrationEntity eventRegistrationEntity, Map<Long, String> fieldMap) {
        //  Get the old reg entity and deduct those counts from main event
        Search search = SearchFactory.getSearch(HkEventRegistrationEntity.class);
        search.addFilterEqual(HkEventRegistrationField.EVENT_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
        search.addFilterEqual(HkEventRegistrationField.USER_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
        HkEventRegistrationEntity oldReg = (HkEventRegistrationEntity) commonDao.searchUnique(search);
        HkEventEntity eventEntity = oldReg.getHkEventEntity();

        eventEntity.setAdultCount(eventEntity.getAdultCount() - oldReg.getAdultCount());
        eventEntity.setChildCount(eventEntity.getChildCount() - oldReg.getChildCount());
        eventEntity.setGuestCount(eventEntity.getGuestCount() - oldReg.getGuestCount());
        eventEntity.setRegistrationCount(eventEntity.getRegistrationCount() - (oldReg.getAdultCount()
                + oldReg.getChildCount() + oldReg.getGuestCount()));

        //  find all the old field values for this user and event and update those
        search = new Search(HkEventRegistrationFieldValueEntity.class);
        search.addFilterEqual(HkEventRegistrationFieldValueField.EVENT_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
        search.addFilterEqual(HkEventRegistrationFieldValueField.USER_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
        List<HkEventRegistrationFieldValueEntity> regOldFieldValues = commonDao.search(search);
        if (!CollectionUtils.isEmpty(regOldFieldValues)) {
            for (HkEventRegistrationFieldValueEntity fieldValueEntity : regOldFieldValues) {
                if (fieldMap.containsKey(fieldValueEntity.getHkEventRegistrationFieldValueEntityPK().getRegistrationField())) {
                    fieldValueEntity.setIsArchive(false);
                    fieldValueEntity.setFieldValue(fieldMap.get(fieldValueEntity.getHkEventRegistrationFieldValueEntityPK().getRegistrationField()));
                    fieldMap.remove(fieldValueEntity.getHkEventRegistrationFieldValueEntityPK().getRegistrationField());
                } else {
                    fieldValueEntity.setIsArchive(true);
                }
            }
        }

        //  save new field values now
        if (!CollectionUtils.isEmpty(fieldMap)) {
            List<HkEventRegistrationFieldValueEntity> fieldValueEntityList = new ArrayList<>();
            for (Long fieldId : fieldMap.keySet()) {
                HkEventRegistrationFieldValueEntity fieldValueEntity = new HkEventRegistrationFieldValueEntity(fieldId, eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                fieldValueEntity.setEvent(eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
                fieldValueEntity.setFieldValue(fieldMap.get(fieldId));
                fieldValueEntity.setIsArchive(false);
                fieldValueEntityList.add(fieldValueEntity);
            }
            commonDao.saveAll(fieldValueEntityList);
        }

        eventRegistrationEntity.setLastModifiedOn(new Date());
        commonDao.getCurrentSession().merge(eventRegistrationEntity);

        eventEntity.setAdultCount(eventEntity.getAdultCount() + eventRegistrationEntity.getAdultCount());
        eventEntity.setChildCount(eventEntity.getChildCount() + eventRegistrationEntity.getChildCount());
        eventEntity.setGuestCount(eventEntity.getGuestCount() + eventRegistrationEntity.getGuestCount());
        eventEntity.setRegistrationCount(eventEntity.getRegistrationCount() + eventRegistrationEntity.getAdultCount()
                + eventRegistrationEntity.getChildCount() + eventRegistrationEntity.getGuestCount());
        commonDao.getCurrentSession().merge(eventEntity);
    }

    @Override
    public void cancelEventRegistration(Long eventId, Long userId) {
        HkEventRegistrationEntity eventRegistrationEntity = commonDao.find(HkEventRegistrationEntity.class, new HkEventRegistrationEntityPK(eventId, userId));
        eventRegistrationEntity.setIsArchive(true);
        eventRegistrationEntity.setLastModifiedOn(new Date());
        eventRegistrationEntity.setStatus(HkSystemConstantUtil.INACTIVE);

        //  find all the old field values for this user and event and archive those
        Search search = new Search(HkEventRegistrationFieldValueEntity.class);
        search.addFilterEqual(HkEventRegistrationFieldValueField.EVENT_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getEvent());
        search.addFilterEqual(HkEventRegistrationFieldValueField.USER_ID, eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
        List<HkEventRegistrationFieldValueEntity> regFieldValues = commonDao.search(search);
        if (!CollectionUtils.isEmpty(regFieldValues)) {
            for (HkEventRegistrationFieldValueEntity fieldValueEntity : regFieldValues) {
                fieldValueEntity.setIsArchive(true);
            }
        }

        HkEventEntity eventEntity = this.retrieveEventById(eventId, false, false);
        eventEntity.setAdultCount(eventEntity.getAdultCount() - eventRegistrationEntity.getAdultCount());
        eventEntity.setChildCount(eventEntity.getChildCount() - eventRegistrationEntity.getChildCount());
        eventEntity.setGuestCount(eventEntity.getGuestCount() - eventRegistrationEntity.getGuestCount());
        eventEntity.setRegistrationCount(eventEntity.getRegistrationCount() - (eventRegistrationEntity.getAdultCount()
                + eventRegistrationEntity.getChildCount() + eventRegistrationEntity.getGuestCount()));
    }

    @Override
    public void createShift(HkShiftEntity hkShiftEntity, String departmentName, List<Long> notifyUserList, boolean isTemporaryShift, Long parentShiftId) {
        hkShiftEntity.setTemporaryShiftFor(null);
        hkShiftEntity.setCreatedOn(new Date());
        this.countTotalTimeInShift(hkShiftEntity);
        Set<HkShiftDepartmentEntity> shiftDepartments = hkShiftEntity.getHkShiftDepartmentEntitySet();
        hkShiftEntity.setHkShiftDepartmentEntitySet(null);
        commonDao.save(hkShiftEntity);

        //  To create temporary shift
        if (isTemporaryShift) {
            HkShiftEntity parentShift = commonDao.find(HkShiftEntity.class, parentShiftId);
            hkShiftEntity.setTemporaryShiftFor(parentShift);
            if (parentShift.getHkShiftEntitySet() == null) {
                parentShift.setHkShiftEntitySet(new HashSet<HkShiftEntity>());
            }
            parentShift.getHkShiftEntitySet().add(hkShiftEntity);

            //  Check if this temporary shift has rules or not
            Set<HkShiftRuleEntity> hkShiftRuleEntitySet = hkShiftEntity.getHkShiftRuleEntitySet();
            if (!CollectionUtils.isEmpty(hkShiftRuleEntitySet)) {
                for (HkShiftRuleEntity hsre : hkShiftRuleEntitySet) {
                    hsre.getHkShiftRuleEntityPK().setShift(hkShiftEntity.getId());
                    hsre.getHkShiftRuleEntityPK().setRuleType(hsre.getHkShiftRuleEntityPK().getRuleType() + hkShiftEntity.getId().toString());
                }
            }

        } else {
            for (HkShiftDepartmentEntity hkShiftDepartmentEntity : shiftDepartments) {
                hkShiftDepartmentEntity.getHkShiftDepartmentEntityPK().setShift(hkShiftEntity.getId());
            }
            hkShiftEntity.setHkShiftDepartmentEntitySet(shiftDepartments);
        }
        //  Send notification to assigner
        if (!hkShiftEntity.getIsDefault()) {
            HkNotificationEntity notification = new HkNotificationEntity();
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_NAME, hkShiftEntity.getShiftTitle());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.DEPARTMENT_NAME, departmentName);
            notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
                    HkSystemConstantUtil.NotificationInstanceType.ADD_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        } else {
            HkNotificationEntity notification = new HkNotificationEntity();
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_NAME, hkShiftEntity.getShiftTitle());
            notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
                    HkSystemConstantUtil.NotificationInstanceType.ADD_DEFAULT_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        }
//        else {
//            Date strtTime = new Date();
//            Date endTime = new Date();
//            Date today = new Date();
//            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
//            for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
//                if (hkShiftDtlEntity.getShift().equals(hkShiftEntity) && hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
//                    strtTime = hkShiftDtlEntity.getStrtTime();
//                    endTime = hkShiftDtlEntity.getEndTime();
//                }
//            }
//            SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
//            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
//            String formattedStartTime = formatTime.format(strtTime);
//            String formattedEndTime = formatTime.format(endTime);
//            String formattedToday = formatDate.format(today);
//            Map<String, Object> valuesMap = new HashMap<>();
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_DATE, formattedToday);
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_TIME, formattedStartTime);
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_END_TIME, formattedEndTime);
//            notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
//                    HkSystemConstantUtil.NotificationInstanceType.PUBLISH_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
//        }
//        notificationService.sendNotification(notification, notifyUserList);
    }

    private void countTotalTimeInShift(HkShiftEntity hkShiftEntity) {
        long totalBreakTimeDiff = 0;
        if (!CollectionUtils.isEmpty(hkShiftEntity.getHkShiftDtlEntitySet())) {
            for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftEntity.getHkShiftDtlEntitySet()) {
                if (!hkShiftDtlEntity.getIsArchive()) {
                    if (hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.BREAK_TIME)) {
                        Date shiftStartTime = setDateAndTime(new Date(), hkShiftDtlEntity.getStrtTime());
                        Date shiftEndTime = setDateAndTime(new Date(), hkShiftDtlEntity.getEndTime());
                        totalBreakTimeDiff = totalBreakTimeDiff + (shiftEndTime.getTime() - shiftStartTime.getTime());
                    }
                }
            }
            for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftEntity.getHkShiftDtlEntitySet()) {
                if (!hkShiftDtlEntity.getIsArchive()) {
                    if (hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                        Date shiftStartTime = setDateAndTime(new Date(), hkShiftDtlEntity.getStrtTime());
                        Date shiftEndTime = setDateAndTime(new Date(), hkShiftDtlEntity.getEndTime());
                        long totalMainTimeDiff = shiftEndTime.getTime() - shiftStartTime.getTime();
                        long shiftDurationMin = (totalMainTimeDiff - totalBreakTimeDiff) / (60 * 1000);
                        hkShiftDtlEntity.setShiftDurationMin(new Integer(String.valueOf(shiftDurationMin)));
                        break;
                    }
                }
            }
        }
    }

    public Date setDateAndTime(Date date, Date timeDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, timeDate.getHours());
        calendar.set(Calendar.MINUTE, timeDate.getMinutes());
        calendar.set(Calendar.SECOND, timeDate.getSeconds());
        return calendar.getTime();
    }

    @Override
    public void createDefaultShift(HkShiftEntity hkShiftEntity, List<Long> notifyUserList) {
        hkShiftEntity.setTemporaryShiftFor(null);
        hkShiftEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        hkShiftEntity.setIsDefault(true);
        commonDao.save(hkShiftEntity);
        Date strtTime = new Date();
        Date endTime = new Date();
        Date today = new Date();
        Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
        for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
            if (hkShiftDtlEntity.getShift().equals(hkShiftEntity) && hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                strtTime = hkShiftDtlEntity.getStrtTime();
                endTime = hkShiftDtlEntity.getEndTime();
            }
        }
//        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
//        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
//        String formattedStartTime = formatTime.format(strtTime);
//        String formattedEndTime = formatTime.format(endTime);
//        String formattedToday = formatDate.format(today);
//        Map<String, Object> valuesMap = new HashMap<>();
//        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_DATE, formattedToday);
//        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_TIME, formattedStartTime);
//        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_END_TIME, formattedEndTime);
//        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
//                HkSystemConstantUtil.NotificationInstanceType.PUBLISH_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
//        notificationService.sendNotification(notification, notifyUserList);
        HkNotificationEntity notification = new HkNotificationEntity();
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_NAME, hkShiftEntity.getShiftTitle());
        notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
                HkSystemConstantUtil.NotificationInstanceType.ADD_DEFAULT_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);
    }

    @Override
    public void updateShift(HkShiftEntity hkShiftEntity, List<Long> notifyUserList) {
        Search search = new Search(HkShiftEntity.class);
        search.addFetches(HkShiftField.TEMP_SHIFTS_SET, HkShiftField.SHIFT_DETAILS_SET,
                HkShiftField.SHIFT_DEPTS_SET, HkShiftField.RULE_ENTITY_SET);
        search.addFilterEqual(HkShiftField.SHIFT_ID, hkShiftEntity.getId());
        HkShiftEntity originalShift = (HkShiftEntity) commonDao.searchUnique(search);
        boolean createNewShift = false;

        //  if the shift is not default
        //  if the shift is not temporary
        if (hkShiftEntity.getTemporaryShiftFor() == null) {
            //  Change the shift department entities
            Set<HkShiftDepartmentEntity> originalShiftDepartments = originalShift.getHkShiftDepartmentEntitySet();
            if (!CollectionUtils.isEmpty(originalShiftDepartments)) {
                for (HkShiftDepartmentEntity shiftDeptEntity : originalShiftDepartments) {
                    shiftDeptEntity.setIsArchive(true);
                    shiftDeptEntity.setLastModifiedOn(new Date());
                }
            }

            //  This set is got from ui, it does not have shift id
            Set<HkShiftDepartmentEntity> newShiftDeptSet = hkShiftEntity.getHkShiftDepartmentEntitySet();
            if (!CollectionUtils.isEmpty(newShiftDeptSet)) {
                for (HkShiftDepartmentEntity shiftDeptEntity : newShiftDeptSet) {
                    shiftDeptEntity.getHkShiftDepartmentEntityPK().setShift(hkShiftEntity.getId());
                    shiftDeptEntity.setHkShiftEntity(hkShiftEntity);
                    shiftDeptEntity.setIsArchive(false);
                    shiftDeptEntity.setLastModifiedOn(new Date());
                }
            }
        } else {
            //  Check for rules..
            Set<HkShiftRuleEntity> newRuleSet = hkShiftEntity.getHkShiftRuleEntitySet();
            Set<HkShiftRuleEntity> originalRuleSet = originalShift.getHkShiftRuleEntitySet();
            Map<String, HkShiftRuleEntity> newRuleMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(newRuleSet)) {
                for (HkShiftRuleEntity shiftRule : newRuleSet) {
                    newRuleMap.put(shiftRule.getHkShiftRuleEntityPK().getRuleType(), shiftRule);
                }
                if (!createNewShift && ((hkShiftEntity.getFrmDt() != null && hkShiftEntity.getFrmDt().getTime() != originalShift.getFrmDt().getTime()) || (hkShiftEntity.getToDt() != null && hkShiftEntity.getToDt().getTime() != originalShift.getToDt().getTime()))) {
                    createNewShift = true;
                }
                if (!CollectionUtils.isEmpty(originalRuleSet) && !createNewShift) {
                    for (HkShiftRuleEntity originalShiftRule : originalRuleSet) {
                        if (!originalShiftRule.getIsArchive()) {
                            HkShiftRuleEntity newShiftRule = newRuleMap.get(originalShiftRule.getHkShiftRuleEntityPK().getRuleType());
                            if (!createNewShift && newShiftRule != null && newShiftRule.getHkShiftRuleEntityPK() != null && newShiftRule.getHkShiftRuleEntityPK().getRuleType() != null) {
                                if (!createNewShift && (!originalShiftRule.getHkShiftRuleEntityPK().getRuleType().contains(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE)
                                        && !newShiftRule.getHkShiftRuleEntityPK().getRuleType().contains(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE) && (!originalShiftRule.getDayCnt().equals(newShiftRule.getDayCnt())
                                        || !originalShiftRule.getEventAction().equals(newShiftRule.getEventAction())
                                        || !originalShiftRule.getEventInstance().equals(newShiftRule.getEventInstance())
                                        || !originalShiftRule.getEventType().equals(newShiftRule.getEventType())))) {

                                    createNewShift = true;
                                    break;
                                }
                            } else {
                                createNewShift = true;
                                break;
                            }
                        }
                    }
                }

                //  if shift from date or to date does not match, create new shift
//                //  if new shift doesn't have rule and old one has the rule, remove those
//                if (!hkShiftEntity.getHasRule()) {
//                    if (originalShift.getHasRule()) {
//                        for (HkShiftRuleEntity shiftRule : originalRuleSet) {
//                            shiftRule.setIsArchive(true);
//                        }
//                    }
//                    //  if new shift doesn't have rule and if old also didn't have any, no need to do anything
//                } else if (originalShift.getHasRule()) {    //  if new shift has rule and original also has rule
//                    if (!CollectionUtils.isEmpty(originalRuleSet) && !CollectionUtils.isEmpty(newRuleSet)) {
//                        Map<String, HkShiftRuleEntity> originalRuleMap = new HashMap<>();
//
//                        for (HkShiftRuleEntity shiftRule : originalRuleSet) {
//                            if (!shiftRule.getIsArchive()) {
//                                originalRuleMap.put(shiftRule.getEventAction(), shiftRule);
//                            }
//                        }
//
//                        for (HkShiftRuleEntity newShiftRule : newRuleSet) {
//                            HkShiftRuleEntity oldShiftRule = originalRuleMap.get(newShiftRule.getEventAction());
//                            if (!oldShiftRule.getEventInstance().equals(newShiftRule.getEventInstance())
//                                    || !oldShiftRule.getDayCnt().equals(newShiftRule.getDayCnt())
//                                    || !oldShiftRule.getEventType().equals(newShiftRule.getEventType())) {
//                                //  if any of the data doesn't match, archive that entry
//                                oldShiftRule.setIsArchive(true);
//                            } else {
//                                //  if all data matches, no need to add new data
//                                newShiftRule = null;
//                            }
//                        }
//                    }
//                }
            }
        }
        if (!createNewShift) {
            //Check if shift Details were changed..
            Set<HkShiftDtlEntity> originalShiftDtls = originalShift.getHkShiftDtlEntitySet();
            Set<HkShiftDtlEntity> shiftDtls = hkShiftEntity.getHkShiftDtlEntitySet();
            if (!CollectionUtils.isEmpty(originalShiftDtls)) {
                Map<String, HkShiftDtlEntity> originalDtlMap = new HashMap<>();

                for (HkShiftDtlEntity shiftDtl : originalShiftDtls) {
                    if (!shiftDtl.getIsArchive()) {
                        originalDtlMap.put(shiftDtl.getSlotTitle(), shiftDtl);
                    }
                }

                //  Iterate through the new shift details to see which are the final shift details
                for (HkShiftDtlEntity newShiftDtl : shiftDtls) {
                    //  if effected from is null, means it's newly added, set the date
                    if (newShiftDtl.getEffectedFrm() == null) {
                        newShiftDtl.setEffectedFrm(new Date());
                    }

                    //  Get the shift detail from the original shift detail map
                    //  if no shift detail found with new title or if any of the data doesn't match, archive that entry (to be done below)
                    HkShiftDtlEntity oldShiftDtl = originalDtlMap.get(newShiftDtl.getSlotTitle());
                    if (oldShiftDtl != null
                            && (oldShiftDtl.getSlotType().equals(newShiftDtl.getSlotType())
                            && oldShiftDtl.getStrtTime().equals(newShiftDtl.getStrtTime())
                            && oldShiftDtl.getEndTime().equals(newShiftDtl.getEndTime())
                            && oldShiftDtl.getWeekDays().equals(newShiftDtl.getWeekDays()))) {
                        //  Remove that detail object from the original map
                        originalDtlMap.remove(newShiftDtl.getSlotTitle());
                        //  if all data matches, no need to add new data
                        newShiftDtl = null;
                    } else {
                        newShiftDtl.setId(null);
                    }
                }

                if (!CollectionUtils.isEmpty(originalDtlMap)) {
                    //  These are the entries that do not match anywhere in new data
                    for (HkShiftDtlEntity oldShiftDtl : originalDtlMap.values()) {
                        oldShiftDtl.setEffectedEnd(new Date());
                        oldShiftDtl.setIsArchive(true);
                    }
                }
            }
            hkShiftEntity.setIsDefault(originalShift.getIsDefault());
            this.countTotalTimeInShift(hkShiftEntity);
            commonDao.getCurrentSession().merge(originalShift);
            commonDao.getCurrentSession().merge(hkShiftEntity);
        } else {
            hkShiftEntity.setIsDefault(originalShift.getIsDefault());
            commonDao.getCurrentSession().evict(originalShift);
            hkShiftEntity.setId(null);
            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = hkShiftEntity.getHkShiftDtlEntitySet();
            Set<HkShiftRuleEntity> ruleEntitys = hkShiftEntity.getHkShiftRuleEntitySet();
            if (!CollectionUtils.isEmpty(ruleEntitys)) {
                for (HkShiftRuleEntity hkShiftRuleEntity : ruleEntitys) {
                    if (hkShiftRuleEntity.getHkShiftRuleEntityPK().getRuleType().contains(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE)) {
                        HkShiftRuleEntityPK hkShiftRuleEntityPK = new HkShiftRuleEntityPK();
                        hkShiftRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.DATE_RANGE);
                        hkShiftRuleEntity.setHkShiftRuleEntityPK(hkShiftRuleEntityPK);
                        hkShiftRuleEntity.setHkShiftEntity(hkShiftEntity);
                    } else if (hkShiftRuleEntity.getHkShiftRuleEntityPK().getRuleType().contains(HkSystemConstantUtil.ShiftRuleType.BEGINS)) {
                        HkShiftRuleEntityPK hkShiftRuleEntityPK = new HkShiftRuleEntityPK();
                        hkShiftRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.BEGINS);
                        hkShiftRuleEntity.setHkShiftRuleEntityPK(hkShiftRuleEntityPK);
                        hkShiftRuleEntity.setHkShiftEntity(hkShiftEntity);
                    } else if (hkShiftRuleEntity.getHkShiftRuleEntityPK().getRuleType().contains(HkSystemConstantUtil.ShiftRuleType.ENDS)) {
                        HkShiftRuleEntityPK hkShiftRuleEntityPK = new HkShiftRuleEntityPK();
                        hkShiftRuleEntityPK.setRuleType(HkSystemConstantUtil.ShiftRuleType.ENDS);
                        hkShiftRuleEntity.setHkShiftRuleEntityPK(hkShiftRuleEntityPK);
                        hkShiftRuleEntity.setHkShiftEntity(hkShiftEntity);
                    }
                }
            }
            for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
                hkShiftDtlEntity.setShift(hkShiftEntity);
                hkShiftDtlEntity.setId(null);
            }
            this.removeShift(originalShift.getId(), hkShiftEntity.getLastModifiedBy(), notifyUserList);
            this.createShift(hkShiftEntity, null, null, hkShiftEntity.getTemporaryShiftFor() != null,
                    hkShiftEntity.getTemporaryShiftFor().getId());
        }

        //  Send notification to assigner
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_NAME, originalShift.getShiftTitle());
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
                HkSystemConstantUtil.NotificationInstanceType.UPDATE_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);
//        } else {
//            Date strtTime = new Date();
//            Date endTime = new Date();
//            Date today = new Date();
//            Set<HkShiftDtlEntity> defaultShiftSet = hkShiftEntity.getHkShiftDtlEntitySet();
//            for (HkShiftDtlEntity hkShiftDtlEntity : defaultShiftSet) {
//                if (hkShiftDtlEntity.getShift().equals(hkShiftEntity) && hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
//                    strtTime = hkShiftDtlEntity.getStrtTime();
//                    endTime = hkShiftDtlEntity.getEndTime();
//                }
//            }
//            SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
//            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yy");
//            String formattedStartTime = formatTime.format(strtTime);
//            String formattedEndTime = formatTime.format(endTime);
//            String formattedToday = formatDate.format(today);
//            Map<String, Object> valuesMap = new HashMap<>();
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_DATE, formattedToday);
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_TIME, formattedStartTime);
//            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_END_TIME, formattedEndTime);
//            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
//                    HkSystemConstantUtil.NotificationInstanceType.PUBLISH_SHIFT, valuesMap, hkShiftEntity.getId(), hkShiftEntity.getFranchise());
//            notificationService.sendNotification(notification, notifyUserList);
//        }

    }

    @Override
    public void removeShift(long shiftId, Long removedBy, List<Long> notifyUserList
    ) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFetches(HkShiftField.TEMP_SHIFTS_SET, HkShiftField.SHIFT_DETAILS_SET,
                HkShiftField.SHIFT_DEPTS_SET, HkShiftField.RULE_ENTITY_SET,HkShiftField.OVRRIDE_SHIFTS_SET);
        HkShiftEntity shiftEntity = commonDao.find(HkShiftEntity.class, shiftId);
        shiftEntity.setStatus(HkSystemConstantUtil.INACTIVE);
        if (removedBy != null) {
            shiftEntity.setLastModifiedBy(removedBy);
        }
        shiftEntity.setLastModifiedOn(new Date());

        this.removeShiftRelatedEntries(shiftEntity);

        //  Remove the temporary shifts related to the main shift
        String shiftName = shiftEntity.getShiftTitle();
        Set<HkShiftEntity> tempShiftSet = shiftEntity.getHkShiftEntitySet();
        if (!CollectionUtils.isEmpty(tempShiftSet)) {
            for (HkShiftEntity tempShiftEntity : tempShiftSet) {
                tempShiftEntity.setStatus(HkSystemConstantUtil.INACTIVE);
                tempShiftEntity.setLastModifiedBy(removedBy);
                tempShiftEntity.setLastModifiedOn(new Date());
//                shiftName += tempShiftEntity.getShiftTitle() + ",";
                this.removeShiftRelatedEntries(tempShiftEntity);
            }
//            shiftName = shiftName.substring(0, shiftName.length() - 1);
        }
        Set<HkShiftEntity> ovrrideShiftSet = shiftEntity.getHkOverrideShiftEntitySet();
        if (!CollectionUtils.isEmpty(ovrrideShiftSet)) {
            for (HkShiftEntity ovrShiftEntity : ovrrideShiftSet) {
                ovrShiftEntity.setStatus(HkSystemConstantUtil.INACTIVE);
                ovrShiftEntity.setLastModifiedBy(removedBy);
                ovrShiftEntity.setLastModifiedOn(new Date());
//                shiftName += tempShiftEntity.getShiftTitle() + ",";
                this.removeShiftRelatedEntries(ovrShiftEntity);
            }
//            shiftName = shiftName.substring(0, shiftName.length() - 1);
        }
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_NAME, shiftName);
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.SHIFT,
                HkSystemConstantUtil.NotificationInstanceType.UPDATE_SHIFT, valuesMap, shiftEntity.getId(), shiftEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);
    }

    private void removeShiftRelatedEntries(HkShiftEntity shiftEntity) {
        //  Remove shift details, shift departments and rules
        Set<HkShiftDtlEntity> shiftDtlSet = shiftEntity.getHkShiftDtlEntitySet();
        if (!CollectionUtils.isEmpty(shiftDtlSet)) {
            for (HkShiftDtlEntity shiftDtlEntity : shiftDtlSet) {
                shiftDtlEntity.setIsArchive(true);
            }
        }

        //  Remove shift departments
        Set<HkShiftDepartmentEntity> shiftDeptSet = shiftEntity.getHkShiftDepartmentEntitySet();
        if (!CollectionUtils.isEmpty(shiftDeptSet)) {
            for (HkShiftDepartmentEntity shiftDeptEntity : shiftDeptSet) {
                shiftDeptEntity.setIsArchive(true);
                shiftDeptEntity.setLastModifiedOn(new Date());
            }
        }

        Set<HkShiftRuleEntity> shiftRuleSet = shiftEntity.getHkShiftRuleEntitySet();
        if (!CollectionUtils.isEmpty(shiftRuleSet)) {
            for (HkShiftRuleEntity shiftRuleEntity : shiftRuleSet) {
                shiftRuleEntity.setIsArchive(true);
            }
        }
    }

    @Override
    public List<HkShiftEntity> retrieveShifts(Long franchise) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.setDistinct(true);
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        search.addFilterNull(HkShiftField.TEMP_SHIFT_FOR);
        search.addFilterNull(HkShiftField.OVERRIDE_SHIFT_FOR);
        search.addFilterEqual(HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
        search.addFetch(HkShiftField.TEMP_SHIFTS_SET);
        search.addFetch(HkShiftField.OVRRIDE_SHIFTS_SET);
        search.addFetch(HkShiftField.RULE_ENTITY_SET);
        List<HkShiftEntity> shiftList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(shiftList)) {
            for (HkShiftEntity shiftEntity : shiftList) {
                this.removeArchivedShiftObjs(shiftEntity);
            }
        }
        return shiftList;
    }

    private void removeArchivedShiftObjs(HkShiftEntity shiftEntity) {
        if (shiftEntity != null) {
            //  if the shift is not default and if it has some departments, remove archived ones
            if (!shiftEntity.getIsDefault() && !CollectionUtils.isEmpty(shiftEntity.getHkShiftDepartmentEntitySet())) {
                Iterator<HkShiftDepartmentEntity> itr = shiftEntity.getHkShiftDepartmentEntitySet().iterator();
                while (itr.hasNext()) {
                    HkShiftDepartmentEntity shiftDept = itr.next();
                    if (shiftDept.getIsArchive()) {
                        itr.remove();
                    }
                }
            }

            //  if the shift has detail set, remove archived ones
            if (!CollectionUtils.isEmpty(shiftEntity.getHkShiftDtlEntitySet())) {
                Iterator<HkShiftDtlEntity> itr = shiftEntity.getHkShiftDtlEntitySet().iterator();
                while (itr.hasNext()) {
                    HkShiftDtlEntity shiftDtl = itr.next();
                    if (shiftDtl.getIsArchive()) {
                        itr.remove();
                    }
                }
            }

            //  if the shift rules exist, remove archived ones
            if (!CollectionUtils.isEmpty(shiftEntity.getHkShiftRuleEntitySet())) {
                Iterator<HkShiftRuleEntity> itr = shiftEntity.getHkShiftRuleEntitySet().iterator();
                while (itr.hasNext()) {
                    HkShiftRuleEntity shiftRule = itr.next();
                    if (shiftRule.getIsArchive()) {
                        itr.remove();
                    }
                }
            }
        }
    }

    @Override
    public HkShiftEntity retrieveShiftById(long franchise, Long id) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFilterEqual(HkShiftField.FRANCHISE, franchise);
        search.addFilterEqual(HkShiftField.SHIFT_ID, id);
        search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        search.setDistinct(true);
        search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
        search.addFetch(HkShiftField.TEMP_SHIFTS_SET);
        search.addFetch(HkShiftField.RULE_ENTITY_SET);
        search.addFetch(HkShiftField.TEMP_SHIFT_FOR.concat(".").concat(HkShiftField.SHIFT_DEPTS_SET));
        search.addFetch(HkShiftField.OVERRIDE_SHIFT_FOR.concat(".").concat(HkShiftField.SHIFT_DEPTS_SET));
        HkShiftEntity shiftEntity = (HkShiftEntity) commonDao.searchUnique(search);
        this.removeArchivedShiftObjs(shiftEntity);
        return shiftEntity;
    }

    @Override
    public List<HkShiftEntity> retrieveShiftByIds(List<Long> shiftIds, Long franchise) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        if (!CollectionUtils.isEmpty(shiftIds)) {
            search.addFilterIn(HkShiftField.SHIFT_ID, shiftIds);
        }
        if (franchise != null) {
            search.addFilterEqual(HkShiftField.FRANCHISE, franchise);
        }
        search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
        search.addFetch(HkShiftField.TEMP_SHIFTS_SET);
        search.addFetch(HkShiftField.RULE_ENTITY_SET);
        search.addFetch(HkShiftField.TEMP_SHIFT_FOR.concat(".").concat(HkShiftField.SHIFT_DEPTS_SET));
        List<HkShiftEntity> hkShiftEntitys = commonDao.search(search);
        if (!CollectionUtils.isEmpty(hkShiftEntitys)) {
            for (HkShiftEntity hkShiftEntity : hkShiftEntitys) {
                this.removeArchivedShiftObjs(hkShiftEntity);
            }
        }
        return hkShiftEntitys;
    }

    @Override
    public void sendRotateShiftNotification(List<UMUser> userList, HkShiftEntity shiftEntity) {
        if (!CollectionUtils.isEmpty(userList)) {
            Set<HkShiftDtlEntity> hkShiftDtlEntitySet = shiftEntity.getHkShiftDtlEntitySet();
            if (!CollectionUtils.isEmpty(hkShiftDtlEntitySet)) {
                List<HkShiftDtlEntity> entitys = new ArrayList(hkShiftDtlEntitySet);
                HkShiftDtlEntity hkShiftDtlEntity = entitys.get(0);
                String strtTime = Integer.toString(hkShiftDtlEntity.getStrtTime().getHours()) + ':' + Integer.toString(hkShiftDtlEntity.getStrtTime().getMinutes());
                String endTime = Integer.toString(hkShiftDtlEntity.getEndTime().getHours()) + ':' + Integer.toString(hkShiftDtlEntity.getEndTime().getMinutes());
                userList.stream().forEach((user) -> {
                    HkNotificationEntity notification = new HkNotificationEntity();
                    Map<String, Object> valuesMap = new HashMap<>();
                    valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_START_TIME, strtTime);
                    valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.SHIFT_END_TIME, endTime);
                    notification = createNotification(HkSystemConstantUtil.NotificationType.EMPLOYEE,
                            HkSystemConstantUtil.NotificationInstanceType.ROTATE_SHIFT, valuesMap, user.getId(), user.getCompany());
                    notificationService.sendNotification(notification, Arrays.asList(user.getId()));
                });
            }
        }
    }

    @Override
    public List<HkShiftEntity> searchShifts(long franchise, String searchString) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFilterEqual(FRANCHISE, franchise);
        search.setDistinct(true);
        search.addFilterEqual(HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterILike(HkShiftField.TITLE, "%" + searchString + "%");
        search.addFetches(HkShiftField.SHIFT_DEPTS_SET, HkShiftField.SHIFT_DETAILS_SET, HkShiftField.TEMP_SHIFTS_SET, HkShiftField.TEMP_SHIFT_FOR, HkShiftField.TEMP_SHIFT_FOR.concat(".").concat(HkShiftField.SHIFT_DEPTS_SET));
        return commonDao.search(search);
    }

    @Override
    public List<HkShiftEntity> searchShifts(long franchise, String searchText, List<Long> departmentIds) {
        Search searchDepts = SearchFactory.getSearch(HkShiftDepartmentEntity.class);
        searchDepts.addFilterEqual(IS_ARCHIVE, "false");
        searchDepts.addFilterIn("hkShiftDepartmentEntityPK.department", departmentIds);
        searchDepts.addFilterEqual(HkShiftDepartmentField.HK_SHIFT_ENTITY + "." + HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        searchDepts.addField("hkShiftEntity");
        return commonDao.search(searchDepts);
    }

    @Override
    public void addShiftRule(HkShiftRuleEntity shiftRuleEntity, long shiftId) {
        HkShiftEntity shift = commonDao.find(HkShiftEntity.class, shiftId);
        if (shift.getHkShiftRuleEntitySet() == null) {
            shift.setHkShiftRuleEntitySet(new HashSet<HkShiftRuleEntity>());
        }
        shift.getHkShiftRuleEntitySet().add(shiftRuleEntity);
    }

    @Override
    public void removeShiftRule(HkShiftRuleEntity shiftRuleEntity, long shiftId) {
        commonDao.find(HkShiftEntity.class, shiftId).getHkShiftRuleEntitySet().remove(shiftRuleEntity);
    }

    @Override
    public HkShiftEntity retrieveDefaultShift(long franshise) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFilterNull(HkShiftField.TEMP_SHIFT_FOR);
        search.addFetch(HkShiftField.TEMP_SHIFTS_SET);
        search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
        search.addFilterEqual(HkShiftField.IS_DEFAULT, true);
        search.addFetch(HkShiftField.RULE_ENTITY_SET);
        search.addFilterEqual(HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterEqual(FRANCHISE, franshise);
        HkShiftEntity shiftEntity = (HkShiftEntity) commonDao.searchUnique(search);

        this.removeArchivedShiftObjs(shiftEntity);
        return shiftEntity;
    }

    @Override
    public Map<Long, String> retrieveShiftsByDepartment(Long franchise, Long deptId) {
        Map<Long, String> shiftDeptMap = new HashMap<>();
        Search search = SearchFactory.getSearch(HkShiftDepartmentEntity.class);
        search.addFilterEqual(HkShiftDepartmentField.DEPARTMENT, deptId);
        search.addFilterEqual(HkShiftDepartmentField.SHIFT_FRANCHISE, franchise);
        search.addFilterEqual(HkShiftDepartmentField.HK_SHIFT_ENTITY + "." + HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterNull(HkShiftDepartmentField.HK_SHIFT_ENTITY + "." + HkShiftField.TEMP_SHIFT_FOR);

        search.addFetch(HkShiftDepartmentField.HK_SHIFT_ENTITY);

        List<HkShiftDepartmentEntity> shiftDeptList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(shiftDeptList)) {
            for (HkShiftDepartmentEntity shiftDept : shiftDeptList) {
                shiftDeptMap.put(shiftDept.getHkShiftEntity().getId(), shiftDept.getHkShiftEntity().getShiftTitle());
            }
        } else {
            HkShiftEntity defaultShift = retrieveDefaultShift(franchise);
            if (defaultShift != null) {
                shiftDeptMap.put(defaultShift.getId(), defaultShift.getShiftTitle());
            }
        }
        return shiftDeptMap;
    }

    @Override
    public HkShiftEntity retrieveShiftById(long franchise, Long id, boolean isShiftDeptNeeded, boolean isShiftDetailsNeeded, boolean isTempShiftsNeeded, boolean isRuleEntityNeeded) {
        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFilterEqual(HkShiftField.SHIFT_ID, id);
        search.addFilterEqual(HkShiftField.FRANCHISE, franchise);
        if (isShiftDeptNeeded) {
            search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        }
        search.setDistinct(true);
        if (isShiftDetailsNeeded) {
            search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
        }
        if (isTempShiftsNeeded) {
            search.addFetch(HkShiftField.TEMP_SHIFTS_SET);
        }
        if (isRuleEntityNeeded) {
            search.addFetch(HkShiftField.RULE_ENTITY_SET);
        }
        HkShiftEntity shiftEntity = (HkShiftEntity) commonDao.searchUnique(search);
        this.removeArchivedShiftObjs(shiftEntity);
        return shiftEntity;
    }

    @Override
    public HkShiftEntity retrieveCurrentShiftForUserOperation(Long shiftId, Date onDate) {
        if (shiftId == null) {
            return null;
        } else {
            Search search = SearchFactory.getSearch(HkShiftEntity.class);
            search.addFilterOr(Filter.equal(HkShiftField.SHIFT_ID, shiftId),
                    Filter.and(Filter.equal(HkShiftField.TEMP_SHIFT_FOR + "." + HkShiftField.SHIFT_ID, shiftId), Filter.lessOrEqual(HkShiftField.FROM_DATE, onDate), Filter.greaterOrEqual(HkShiftField.TO_DATE, onDate)),
                    Filter.and(Filter.equal(HkShiftField.OVERRIDE_SHIFT_FOR + "." + HkShiftField.SHIFT_ID, shiftId), Filter.equal(HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE)));
            search.setDistinct(true);
            search.addFetch(HkShiftField.SHIFT_DETAILS_SET);
            List<HkShiftEntity> hkShiftList = commonDao.search(search);
            if (!CollectionUtils.isEmpty(hkShiftList)) {
                HkShiftEntity hkShiftEntity = null;
                if (hkShiftList.size() == 1) {
                    hkShiftEntity = hkShiftList.get(0);
                } else {
                    for (HkShiftEntity tempHkShiftEntity : hkShiftList) {
                        if (tempHkShiftEntity.getTemporaryShiftFor() != null) {
                            hkShiftEntity = tempHkShiftEntity;
                        }
                    }
                    if (hkShiftEntity == null) {
                        for (HkShiftEntity overrideShift : hkShiftList) {
                            //and compare day of onDate with the days available in Override Shift. If that match then only we have to use that shift
                            if (overrideShift.getOverrideShiftFor()!= null) {
                                if (StringUtils.hasText(overrideShift.getWeekDays())) {
                                    Calendar cal = Calendar.getInstance();
                                    Integer val = cal.get(Calendar.DAY_OF_WEEK);
                                    if (overrideShift.getWeekDays().contains(val.toString())) {
                                        hkShiftEntity = overrideShift;
                                    }
                                }
                            }
                        }
                    }
                }
                return hkShiftEntity;
            } else {
                return null;
            }
        }
    }

    @Override
    public List<HkShiftEntity> inactiveTemporaryShift(Date today) {

        Search search = SearchFactory.getSearch(HkShiftEntity.class);
        search.addFilterEqual(HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFetch(HkShiftField.SHIFT_DEPTS_SET);
        search.addFetch(HkShiftField.TEMP_SHIFT_FOR.concat(".").concat(HkShiftField.SHIFT_DEPTS_SET));
        search.addFilterNotNull(HkShiftField.TEMP_SHIFT_FOR);
        search.setDistinct(true);
        search.addFilterLessOrEqual(HkShiftField.END_DATE, today);
        return commonDao.search(search);
    }

    public Set<Long> retrieveRecipientIds(List<String> recipientCodes) throws GenericDatabaseException {
        Set<Long> recipientIdsSet = new HashSet<>();
        final String SEPARATOR = ":";
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            List<Long> departmentIds = new LinkedList<>();
            for (String recipientCode : recipientCodes) {
                StringTokenizer tokenizer = new StringTokenizer(recipientCode, SEPARATOR);
                if (tokenizer.countTokens() == 2) {
                    Long id = new Long(tokenizer.nextToken());
                    String type = tokenizer.nextToken();
                    switch (type) {
                        case HkSystemConstantUtil.RecipientCodeType.DEPARTMENT:
                            departmentIds.add(id);
                            break;
                    }
                }
            }
            if (!CollectionUtils.isEmpty(departmentIds)) {
                List<String> projections = new ArrayList<>();
                projections.add(UMUserDao.ID);
                Map<String, Object> departmentIdMap = new HashMap<>();
                departmentIdMap.put(UMUserDao.DEPARTMENT, departmentIds);
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                criterias.put(GenericDao.QueryOperators.IN, departmentIdMap);
                List<UMUser> users = userService.retrieveUsers(projections, criterias, null);
                if (!CollectionUtils.isEmpty(users)) {
                    for (UMUser uMUser : users) {
                        recipientIdsSet.add(uMUser.getId());
                    }
                }
            }

        }
        return recipientIdsSet;
    }

    public Map<Long, Set<HkShiftEntity>> retrieveShiftByDeptIds(List<Long> deptIds, Long franchise) {
        Map<Long, Set<HkShiftEntity>> shiftDeptMap = new HashMap<>();
        Search search = SearchFactory.getSearch(HkShiftDepartmentEntity.class);
        if (!CollectionUtils.isEmpty(deptIds)) {
            search.addFilterIn(HkShiftDepartmentField.DEPARTMENT, deptIds);
        }
        if (franchise != null) {
            search.addFilterEqual(HkShiftDepartmentField.SHIFT_FRANCHISE, franchise);
        }
        search.addFilterEqual(HkShiftDepartmentField.HK_SHIFT_ENTITY + "." + HkShiftField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterNull(HkShiftDepartmentField.HK_SHIFT_ENTITY + "." + HkShiftField.TEMP_SHIFT_FOR);

        search.addFetch(HkShiftDepartmentField.HK_SHIFT_ENTITY);
        search.addFetch(HkShiftDepartmentField.HK_SHIFT_DETAILS);
        search.addFetch(HkShiftDepartmentField.HK_SHIFT_DEPARTMENT_ENTITY);

        List<HkShiftDepartmentEntity> shiftDeptList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(shiftDeptList)) {
            for (HkShiftDepartmentEntity shiftDept : shiftDeptList) {
                Set<HkShiftEntity> hkShiftEntitySet = new LinkedHashSet<>();
                if (shiftDeptMap.containsKey(shiftDept.getHkShiftDepartmentEntityPK().getDepartment())) {
                    hkShiftEntitySet = shiftDeptMap.get(shiftDept.getHkShiftDepartmentEntityPK().getDepartment());
                }
                hkShiftEntitySet.add(shiftDept.getHkShiftEntity());
                shiftDeptMap.put(shiftDept.getHkShiftDepartmentEntityPK().getDepartment(), hkShiftEntitySet);
            }
        }
        return shiftDeptMap;
    }
}
