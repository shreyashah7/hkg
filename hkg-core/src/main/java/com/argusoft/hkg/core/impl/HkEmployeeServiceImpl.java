/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.model.HkLeaveApprovalEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkTaskEntity;
import com.argusoft.hkg.model.HkTaskRecipientDtlEntity;
import com.argusoft.hkg.model.HkTaskRecipientEntity;
import com.argusoft.hkg.model.HkUserAttendanceEntity;
import com.argusoft.hkg.model.HkUserAttendanceEntityPK;
import com.argusoft.hkg.model.HkUserOperationEntity;
import com.argusoft.hkg.model.HkUserOperationEntityPK;
import com.argusoft.hkg.model.HkUserWorkHistoryEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.hkg.core.util.HkUserOperationEnum;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation class for HkEmployeeService.
 *
 * @author Mital
 */
@Service
@Transactional
public class HkEmployeeServiceImpl extends HkCoreService implements HkEmployeeService {

    @Autowired
    private HkHRService hkHrService;
    @Autowired
    private HkFoundationService hkFoundationService;
    @Autowired
    HkNotificationService notificationService;

    private static final String FRANCHISE = "franchise";
    private static final String IS_ARCHIVE = "isArchive";

    public static class HkTaskField {

        private static final String ID = "id";
        private static final String TASK_NAME = "taskName";
        private static final String STATUS = "status";
        private static final String IS_REPETITIVE = "isRepetative";
        private static final String DUE_DATE = "dueDt";
        private static final String CATEGORY = "taskCategory";
        private static final String CREATED_BY = "createdBy";
        private static final String CREATED_ON = "createdOn";
        private static final String TASK_RECIPIENT_DTL_ENTITY_SET = "hkTaskRecipientDtlEntitySet";
        private static final String TASK_RECIPIENT_ENTITY_SET = "hkTaskRecipientEntitySet";
    }

    private static class HkTaskRecipientField {

        private static final String TASK_ID = "hkTaskRecipientEntityPK.task";
        private static final String REFERENCE_TYPE = "hkTaskRecipientEntityPK.referenceType";
        private static final String REFERENCE_INSTANCE = "hkTaskRecipientEntityPK.referenceInstance";
    }

    public static class HkTaskRecipientDtlField {

        private static final String DUE_DATE = "dueDate";
        private static final String ON_DATE = "onDate";
        private static final String STATUS = "status";
        private static final String USER_ID = "userId";
        private static final String ID = "id";
        private static final String REPETITION_COUNT = "repetitionCount";
        private static final String TASK_NAME = "task.taskName";
        private static final String TASK_ID = "task.id";
        private static final String TASK = "task";
        private static final String FRANCHISE = "task.franchise";
        private static final String ASSIGNER = "task.createdBy";
        private static final String CATEGORY = "category";
    }

    private static class HkLeaveField {

        private static final String FROM_DATE = "frmDt";
        private static final String TO_DATE = "toDt";
        private static final String ID = "id";
        private static final String FOR_USER = "forUser";
        private static final String STATUS = "status";
        private static final String LEAVE_REASON = "leaveReason";
    }

    private static class HkLeaveApprovalField {

        private static final String LEAVE_REQUEST = "leaveRequest";
        private static final String LEAVE_ID = "leaveRequest.id";
        private static final String STATUS = "status";
        private static final String WORKFLOW = "workflow";
        private static final String LEVEL = "level";
        private static final String ATTENDED_BY = "attendedBy";
        private static final String LEAVE_REASON = "leaveRequest.leaveReason";
        private static final String FOR_USER = "leaveRequest.forUser";
        private static final String LEAVE_FRANCHISE = "leaveRequest." + FRANCHISE;
    }

    private static class HkUserWorkHistoryField {

        private static final String USER_ID = "userId";
    }

    private static class HkUserOperationField {

        private static final String ON_TIME = "onTime";
        private static final String FRANCHISE = "franchise";
        private static final String USER_ID = "hkUserOperationEntityPK.userId";
        private static final String USER_OPERATION = "hkUserOperationEntityPK.eventCode";
        private static final String IS_ATTENDED = "isAttended";
    }

    private static class HkUserAttendanceField {

        private static final String ON_DATE = "hkUserAttendanceEntityPK.onDate";
        private static final String FRANCHISE = "franchise";

    }

    @Override
    public void createTask(HkTaskEntity taskEntity, Set<Long> recipientIds, String assignerCode, String assignerName, Long createdBy) {
        taskEntity.setRepetitionCount(0);
        commonDao.save(taskEntity);

        if (!CollectionUtils.isEmpty(taskEntity.getHkTaskRecipientEntitySet())) {
            //  if recipients are there, add their details in table

            Set<HkTaskRecipientEntity> recipientSet = taskEntity.getHkTaskRecipientEntitySet();
            for (HkTaskRecipientEntity recipient : recipientSet) {
                recipient.getHkTaskRecipientEntityPK().setTask(taskEntity.getId());
            }
            commonDao.save(recipientSet.toArray());

            if (!CollectionUtils.isEmpty(recipientIds)) {
                List<HkTaskRecipientDtlEntity> recipientDtlList = new ArrayList<>();
                for (Long recipientId : recipientIds) {
                    if (recipientId.equals(createdBy)) {
                        HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                        recipientDtlObj.setDueDate(taskEntity.getDueDt());
                        recipientDtlObj.setIsArchive(false);
                        recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                        recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                        recipientDtlObj.setTask(taskEntity);
                        recipientDtlObj.setUserId(recipientId);
                        recipientDtlObj.setRepetitionCount(0);
                        recipientDtlObj.setCategory(taskEntity.getTaskCategory());
                        recipientDtlList.add(recipientDtlObj);
                    } else {
                        HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                        recipientDtlObj.setDueDate(taskEntity.getDueDt());
                        recipientDtlObj.setIsArchive(false);
                        recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                        recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                        recipientDtlObj.setTask(taskEntity);
                        recipientDtlObj.setUserId(recipientId);
                        recipientDtlObj.setRepetitionCount(0);
                        recipientDtlList.add(recipientDtlObj);
                    }

                }
                commonDao.saveAll(recipientDtlList);

                //  Send notification to all the recipients
                Map<String, Object> valuesMap = new HashMap<>();
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, assignerCode);
                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, assignerName);
                HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                        HkSystemConstantUtil.NotificationInstanceType.ADD_TASK, valuesMap, taskEntity.getId(), taskEntity.getFranchise());
                notificationService.sendNotification(notification, recipientIds);
            }
        }
    }

    @Override
    public void updateTask(HkTaskEntity taskEntity, Set<HkTaskRecipientEntity> taskRecipientsSet, Set<Long> recipientIds, String assignerCode, String assignerName, Long createdBy) {

        boolean isTaskCancelled = false;
        //  if task is cancelled, cancel it for recipient dtls (users) also
        if (taskEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.CANCELLED) && taskEntity.getIsRepetative()) {
            Search search = new Search(HkTaskRecipientDtlEntity.class);
            //  Get details of same repetition count to archive them
            search.addFilterEqual(HkTaskRecipientDtlField.TASK_ID, taskEntity.getId());
            search.addFilterEqual(HkTaskRecipientDtlField.REPETITION_COUNT, taskEntity.getRepetitionCount());
            search.addFilterEqual(IS_ARCHIVE, false);
            List<HkTaskRecipientDtlEntity> taskRecipientList = commonDao.search(search);

            if (!CollectionUtils.isEmpty(taskRecipientList)) {
                for (HkTaskRecipientDtlEntity recipientDtl : taskRecipientList) {
                    //  if the task is due, then only update the status to cancelled. no need to update the status of completed tasks.
                    if (recipientDtl.getStatus().equals(HkSystemConstantUtil.TaskStatus.DUE)) {
                        recipientDtl.setStatus(HkSystemConstantUtil.TaskStatus.CANCELLED);
                    }
                }
            }
            isTaskCancelled = true;
        } else {
            List<HkTaskRecipientEntity> oldTaskRecipientList = this.retrieveTaskRecipientsByTaskId(taskEntity.getId(), null);
            if (!CollectionUtils.isEmpty(oldTaskRecipientList)) {
                for (HkTaskRecipientEntity recipient : oldTaskRecipientList) {
                    recipient.setIsArchive(true);
                }
            }

            if (!CollectionUtils.isEmpty(taskRecipientsSet)) {
                for (HkTaskRecipientEntity recipient : taskRecipientsSet) {
                    recipient.setIsArchive(false);
                }
                taskEntity.setHkTaskRecipientEntitySet(taskRecipientsSet);
            }
            List<HkTaskRecipientDtlEntity> oldDtlEntitys = this.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), null);
            if (!CollectionUtils.isEmpty(oldDtlEntitys)) {
                List<HkTaskRecipientDtlEntity> oldRecipientDtlsWithSameCount = new ArrayList<>();
//                System.out.println("oldDtlEntitys :" + oldDtlEntitys);
                for (HkTaskRecipientDtlEntity hkTaskRecipientDtlEntity : oldDtlEntitys) {
                    if (hkTaskRecipientDtlEntity.getRepetitionCount().equals(taskEntity.getRepetitionCount())) {
                        oldRecipientDtlsWithSameCount.add(hkTaskRecipientDtlEntity);
                    }
                }
//                System.out.println("oldRecipientDtlsWithSameCount :" + oldRecipientDtlsWithSameCount);
                if (!CollectionUtils.isEmpty(oldRecipientDtlsWithSameCount)) {
                    List<Long> oldRecipientIds = new ArrayList<>();
                    List<Long> notCommonOldRecipientIds = new ArrayList<>(oldRecipientIds);
                    List<Long> notCommonNewRecipientIds = new ArrayList<>(recipientIds);
                    for (HkTaskRecipientDtlEntity oldRecipientDtlsWithSameCount1 : oldRecipientDtlsWithSameCount) {
                        oldRecipientIds.add(oldRecipientDtlsWithSameCount1.getUserId());
                    }
                    if (!CollectionUtils.isEmpty(oldRecipientIds) && !CollectionUtils.isEmpty(recipientIds)) {
                        notCommonOldRecipientIds.addAll(oldRecipientIds);
                        notCommonOldRecipientIds.removeAll(recipientIds);
                        notCommonNewRecipientIds.removeAll(oldRecipientIds);
                    }
                    if (!CollectionUtils.isEmpty(notCommonOldRecipientIds)) {
//                        System.out.println("notCommonOldRecipientIds :" + notCommonOldRecipientIds);
                        for (Long notCommonOldRecipientId : notCommonOldRecipientIds) {
                            for (HkTaskRecipientDtlEntity oldRecipientDtlsWithSameCount1 : oldRecipientDtlsWithSameCount) {
                                if (notCommonOldRecipientId.equals(oldRecipientDtlsWithSameCount1.getUserId())) {
                                    oldRecipientDtlsWithSameCount1.setIsArchive(true);
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(notCommonNewRecipientIds)) {
//                        System.out.println("notCommonNewRecipientIds :" + notCommonNewRecipientIds);
                        Set<HkTaskRecipientDtlEntity> recipientDtlList = new HashSet<>();
                        for (Long recipientId : notCommonNewRecipientIds) {
                            if (recipientId.equals(createdBy)) {
                                HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                                recipientDtlObj.setDueDate(taskEntity.getDueDt());
                                recipientDtlObj.setIsArchive(false);
                                recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                                recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                                recipientDtlObj.setTask(taskEntity);
                                recipientDtlObj.setUserId(recipientId);
                                recipientDtlObj.setRepetitionCount(taskEntity.getRepetitionCount());
                                recipientDtlObj.setCategory(taskEntity.getTaskCategory());
                                recipientDtlList.add(recipientDtlObj);
                            } else {
                                HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                                recipientDtlObj.setDueDate(taskEntity.getDueDt());
                                recipientDtlObj.setIsArchive(false);
                                recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                                recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                                recipientDtlObj.setTask(taskEntity);
                                recipientDtlObj.setUserId(recipientId);
                                recipientDtlObj.setRepetitionCount(taskEntity.getRepetitionCount());
                                recipientDtlList.add(recipientDtlObj);
                            }
                        }
                        taskEntity.setHkTaskRecipientDtlEntitySet(recipientDtlList);
                    }
                } else {
                    if (!CollectionUtils.isEmpty(recipientIds)) {
                        Set<HkTaskRecipientDtlEntity> recipientDtlList = new HashSet<>();
                        for (Long recipientId : recipientIds) {
                            if (recipientId.equals(createdBy)) {
                                HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                                recipientDtlObj.setDueDate(taskEntity.getDueDt());
                                recipientDtlObj.setIsArchive(false);
                                recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                                recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                                recipientDtlObj.setTask(taskEntity);
                                recipientDtlObj.setUserId(recipientId);
                                recipientDtlObj.setRepetitionCount(taskEntity.getRepetitionCount());
                                recipientDtlObj.setCategory(taskEntity.getTaskCategory());
                                recipientDtlList.add(recipientDtlObj);
                            } else {
                                HkTaskRecipientDtlEntity recipientDtlObj = new HkTaskRecipientDtlEntity();
                                recipientDtlObj.setDueDate(taskEntity.getDueDt());
                                recipientDtlObj.setIsArchive(false);
                                recipientDtlObj.setOnDate(taskEntity.getCreatedOn());
                                recipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                                recipientDtlObj.setTask(taskEntity);
                                recipientDtlObj.setUserId(recipientId);
                                recipientDtlObj.setRepetitionCount(taskEntity.getRepetitionCount());
                                recipientDtlList.add(recipientDtlObj);
                            }
                        }
                        taskEntity.setHkTaskRecipientDtlEntitySet(recipientDtlList);

                    }
                }
            }
        }

        //  Send notification to all the recipients
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, assignerCode);
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, assignerName);
        HkNotificationEntity notification;
        if (isTaskCancelled) {
            notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                    HkSystemConstantUtil.NotificationInstanceType.TASK_CANCELLED, valuesMap, taskEntity.getId(), taskEntity.getFranchise());
        } else {
            notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                    HkSystemConstantUtil.NotificationInstanceType.UPDATE_TASK, valuesMap, taskEntity.getId(), taskEntity.getFranchise());
        }
        notificationService.sendNotification(notification, recipientIds);

        commonDao.getCurrentSession().merge(taskEntity);
    }

    @Override
    public void removeTask(HkTaskEntity taskEntity, String assignerCode, String assignerName) {
        Set<Long> recipientIds = new HashSet<>();
        //  Get recipientInfo objects and remove them
        if (!CollectionUtils.isEmpty(taskEntity.getHkTaskRecipientEntitySet())) {
            for (HkTaskRecipientEntity recipient : taskEntity.getHkTaskRecipientEntitySet()) {
                recipient.setIsArchive(true);
            }

            //  it's possible that in the given recipient set, there would be no person to do this task so let's check for nullity
            if (!CollectionUtils.isEmpty(taskEntity.getHkTaskRecipientDtlEntitySet())) {
                //  Get recipientDtl objects and remove them
                for (HkTaskRecipientDtlEntity recipientDtl : taskEntity.getHkTaskRecipientDtlEntitySet()) {
                    if (!recipientDtl.getIsArchive()) {
                        recipientDtl.setIsArchive(true);
                        recipientIds.add(recipientDtl.getUserId());
                    }
                }
            }
        }

        taskEntity.setLastModifiedOn(new Date());
        taskEntity.setIsArchive(true);
        commonDao.save(taskEntity);
        if (!CollectionUtils.isEmpty(recipientIds)) {
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, assignerCode);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, assignerName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                    HkSystemConstantUtil.NotificationInstanceType.TASK_REMOVED, valuesMap, taskEntity.getId(), taskEntity.getFranchise());
            notificationService.sendNotification(notification, recipientIds);
        }
    }

    @Override
    public void removeTask(Long taskId, String assignerCode, String assignerName, Long franchise) {
        Search search = SearchFactory.getSearch(HkTaskEntity.class);
        search.addFilterEqual(HkTaskField.ID, taskId);
        search.addFilterEqual(FRANCHISE, franchise);
        HkTaskEntity taskEntity = (HkTaskEntity) commonDao.searchUnique(search);
        if (taskEntity != null) {
            taskEntity.setLastModifiedOn(new Date());
            this.removeTask(taskEntity, assignerCode, assignerName);
        }
    }

    @Override
    public List<HkTaskEntity> searchTasks(String searchStr, List<Long> recipientIds, String referenceType, List<Long> referenceIds, Long userId, Long franchise) {
        List<HkTaskEntity> resultList = null;

        Criteria criteria = commonDao.getCurrentSession().createCriteria(HkTaskEntity.class);
        criteria.add(Restrictions.eq(FRANCHISE, franchise));

        if (referenceType != null && !CollectionUtils.isEmpty(referenceIds)) {
            criteria.createAlias(HkTaskField.TASK_RECIPIENT_ENTITY_SET, "recipient")
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            criteria.add(Restrictions.and(Restrictions.eq("recipient." + HkTaskRecipientField.REFERENCE_TYPE, referenceType),
                    Restrictions.in("recipient." + HkTaskRecipientField.REFERENCE_INSTANCE, referenceIds)));
            criteria.add(Restrictions.eq(HkTaskField.CREATED_BY, userId));
            criteria.setFetchMode(HkTaskField.TASK_RECIPIENT_DTL_ENTITY_SET, FetchMode.JOIN);
            resultList = criteria.list();
            if (!CollectionUtils.isEmpty(resultList)) {
                for (HkTaskEntity task : resultList) {
                    task.setHkTaskRecipientEntitySet(new HashSet<>(task.getHkTaskRecipientEntitySet()));
                }
            }
        } else {
            //  if no reference type passed
            criteria.createAlias(HkTaskField.TASK_RECIPIENT_DTL_ENTITY_SET, "recipientDtl")
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            Criterion titleDateCriterion = Restrictions.or(
                    Restrictions.ilike(HkTaskField.TASK_NAME, searchStr, MatchMode.ANYWHERE),
                    getDateRestrictions(searchStr, "due_date"),
                    getDateRestrictions(searchStr, "due_dt"));

            if (!CollectionUtils.isEmpty(recipientIds)) {
                Criterion assigneeCri = Restrictions.or(
                        Restrictions.in("recipientDtl." + HkTaskRecipientDtlField.USER_ID, recipientIds),
                        Restrictions.in(HkTaskField.CREATED_BY, recipientIds));
                criteria.add(Restrictions.or(titleDateCriterion, assigneeCri));
            } else {
                criteria.add(titleDateCriterion);
            }

            criteria.add(Restrictions.or(
                    //  if this task is assigned to me, it must not be archive
                    Restrictions.and(
                            Restrictions.eq("recipientDtl." + HkTaskRecipientDtlField.USER_ID, userId),
                            Restrictions.eq("recipientDtl." + IS_ARCHIVE, false),
                            Restrictions.ne("recipientDtl." + HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.CANCELLED_ARCHIVED),
                            Restrictions.ne("recipientDtl." + HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED)),
                    //  if this task is created by me, it must not be archive
                    Restrictions.and(
                            Restrictions.eq(HkTaskField.CREATED_BY, userId),
                            Restrictions.eq(IS_ARCHIVE, false),
                            Restrictions.ne(HkTaskField.STATUS, HkSystemConstantUtil.TaskStatus.CANCELLED_ARCHIVED),
                            Restrictions.ne(HkTaskField.STATUS, HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED))
            ));
//        criteria.setFetchMode("recipientDtl", FetchMode.SELECT);      //  not working :(
            criteria.setFetchMode(HkTaskField.TASK_RECIPIENT_ENTITY_SET, FetchMode.JOIN);

            resultList = criteria.list();
            if (!CollectionUtils.isEmpty(resultList)) {
                for (HkTaskEntity task : resultList) {
                    if (!CollectionUtils.isEmpty(task.getHkTaskRecipientDtlEntitySet())) {
                        task.setHkTaskRecipientDtlEntitySet(new HashSet<>(task.getHkTaskRecipientDtlEntitySet()));
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<String> searchTaskNames(String searchName, Long userId, Long franchise) {
        Search search = new Search(HkTaskEntity.class);
        if (searchName != null) {
            search.addFilterILike(HkTaskField.TASK_NAME, "%" + searchName + "%");
        }
        if (userId != null) {
            search.addFilterEqual(HkTaskField.CREATED_BY, userId);
        }
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        search.addField(HkTaskField.TASK_NAME);
        return commonDao.search(search);
    }

    @Override
    public void cancelRepeatedTask(Long taskId, int repetitionCount, String assignerCode, String assignerName, Long franchise) {
        Search search = new Search(HkTaskRecipientDtlEntity.class);
        search.addFilterEqual(HkTaskRecipientDtlField.TASK_ID, taskId);
        search.addFilterEqual(HkTaskRecipientDtlField.REPETITION_COUNT, repetitionCount);
        search.addFilterEqual(HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.DUE);
        if (franchise != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.FRANCHISE, franchise);
        }
        search.addFilterEqual(IS_ARCHIVE, false);
        List<HkTaskRecipientDtlEntity> recipientDtlList = commonDao.search(search);

        if (!CollectionUtils.isEmpty(recipientDtlList)) {
            Set<Long> recipientIds = new HashSet<>();
            for (HkTaskRecipientDtlEntity recipientDtl : recipientDtlList) {
                recipientDtl.setStatus(HkSystemConstantUtil.TaskStatus.CANCELLED);
                recipientIds.add(recipientDtl.getUserId());
            }

            //  Send notification to relevant users
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, assignerCode);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, assignerName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                    HkSystemConstantUtil.NotificationInstanceType.TASK_CANCELLED, valuesMap, taskId, franchise);
            notificationService.sendNotification(notification, recipientIds);
        }

        HkTaskEntity taskEntity = commonDao.find(HkTaskEntity.class, taskId);
        //  if current repetition count is same as given count, means current instance is being cancelled
        //  so change the status of the task to cancel
        if (taskEntity.getRepetitionCount().equals(repetitionCount)) {
            taskEntity.setStatus(HkSystemConstantUtil.TaskStatus.CANCELLED);
        } else {
            this.updateCompletedTask(taskEntity);
        }
    }

    @Override
    public List<HkTaskRecipientEntity> retrieveTaskRecipientsByTaskId(Long taskId, Boolean archiveStatus) {
        Search search = new Search(HkTaskRecipientEntity.class);
        search.addFilterEqual(HkTaskRecipientField.TASK_ID, taskId);
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        return commonDao.search(search);
    }

    @Override
    public List<HkTaskRecipientDtlEntity> retrieveTaskRecipientDtlsByTaskId(Long taskId, Boolean archiveStatus) {
        Search search = new Search(HkTaskRecipientDtlEntity.class);
        search.addFilterEqual(HkTaskRecipientDtlField.TASK_ID, taskId);
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        return commonDao.search(search);
    }

    @Override
    public void updateTaskRecipientDtl(HkTaskRecipientDtlEntity taskRecipientDtlEntity, String assigneeCode, String assigneeName) {
        HkTaskRecipientDtlEntity oldRecipientDetailObj = this.retrieveTaskRecipientDtlById(taskRecipientDtlEntity.getId());
        boolean wasTaskCompletedEarlier = oldRecipientDetailObj.getStatus().equals(HkSystemConstantUtil.TaskStatus.COMPLETED);
        commonDao.getCurrentSession().merge(taskRecipientDtlEntity);
        if (!wasTaskCompletedEarlier && taskRecipientDtlEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.COMPLETED)) {
            //  if task was not completed earlier, and if now it's finished, send the notification

            //  check if the main task can be marked as finished now
            this.updateCompletedTask(taskRecipientDtlEntity.getTask());

            //  Send notification to assigner
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, assigneeCode);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, assigneeName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.TASK,
                    HkSystemConstantUtil.NotificationInstanceType.TASK_COMPLETED, valuesMap, taskRecipientDtlEntity.getTask().getId(), taskRecipientDtlEntity.getTask().getFranchise());
            notificationService.sendNotification(notification, Arrays.asList(taskRecipientDtlEntity.getTask().getCreatedBy()));
        }
    }

    private void updateCompletedTask(HkTaskEntity taskEntity) {
        //  If the task is completed, retrieve other task instances to check if this task is overall finished or not
        Search search = new Search(HkTaskRecipientDtlEntity.class);
        //  Get details of task recipient dtls with same task id and due status
        search.addFilterEqual(HkTaskRecipientDtlField.TASK_ID, taskEntity.getId());
        search.addFilterEqual(HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.DUE);
        search.addFilterEqual(IS_ARCHIVE, false);
        int resultCount = commonDao.count(search);
        if (resultCount <= 0) {
            //  no due tasks found so finish this task now
            taskEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED);
            taskEntity.setActualEndDate(new Date());
            commonDao.getCurrentSession().merge(taskEntity);
        }
    }

    @Override
    public HkTaskEntity retrieveTaskById(Long taskId, boolean includeRecipients, boolean includeRecipientDetails) {
        HkTaskEntity taskEntity = commonDao.find(HkTaskEntity.class, taskId);

        //  Set other details as per requirement
        if (taskEntity != null) {
            if (includeRecipients) {
                taskEntity.setHkTaskRecipientEntitySet(new HashSet<>(this.retrieveTaskRecipientsByTaskId(taskId, false)));
            }
            if (includeRecipientDetails) {
                taskEntity.setHkTaskRecipientDtlEntitySet(new HashSet<>(this.retrieveTaskRecipientDtlsByTaskId(taskId, false)));
            }
        }

        //  return the result
        return taskEntity;
    }

    @Override
    public List<HkTaskRecipientDtlEntity> retrieveTaskRecipientDtlsByCriteria(Date dueDate, Date assignedStartDate, Date assignedEndDate, Long assignee, Long assigner, List<String> statusList, Long categoryId, Long repetitionCount, Long franchise, Boolean archiveStatus, String sortProperty, boolean isDesc) {
        Search search = new Search(HkTaskRecipientDtlEntity.class);

        //  Check for assigned date - used for "Recent tasks" list
        if (assignedStartDate != null) {
            search.addFilterGreaterOrEqual(HkTaskRecipientDtlField.ON_DATE, assignedStartDate);
        }
        if (assignedEndDate != null) {
            search.addFilterLessOrEqual(HkTaskRecipientDtlField.ON_DATE, assignedEndDate);
        }

        //  Check for due date - Used for "Due today" list
        if (dueDate != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.DUE_DATE, dueDate);
        }

        //  Used for my tasks
        if (assignee != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.USER_ID, assignee);
        }

        //  Used for tasks assigned by me or other
        if (assigner != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.ASSIGNER, assigner);
        }

        //  Due, Cancelled or Completed - Usually used for seeing completed tasks or due tasks
        if (!CollectionUtils.isEmpty(statusList)) {
            search.addFilterIn(HkTaskRecipientDtlField.STATUS, statusList);
        } else {
            search.addFilterNotIn(HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED);
        }

        if (categoryId != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.CATEGORY, categoryId);
        }

        if (repetitionCount != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.REPETITION_COUNT, repetitionCount);
        }

        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        if (sortProperty != null) {
            search.addSort(sortProperty, isDesc);
        }

        //  return the result
        return commonDao.search(search);
    }

    @Override
    public List<HkTaskEntity> retrieveTasksByCriteria(Date dueDate, Date createdStartDate, Date createdEndDate, Long assigner, String status, Long categoryId, Long franchise, Boolean archiveStatus, boolean includeRecipientDetails, String sortProperty, boolean isDesc) {
        Search search = new Search(HkTaskEntity.class);

        //  Check for assigned date - used for "Recent tasks" list
        if (createdStartDate != null) {
            search.addFilterGreaterOrEqual(HkTaskField.CREATED_ON, createdStartDate);
        }
        if (createdEndDate != null) {
            search.addFilterLessOrEqual(HkTaskField.CREATED_ON, createdEndDate);
        }

        //  Check for due date - Used for "Due today" list
        if (dueDate != null) {
            search.addFilterEqual(HkTaskField.DUE_DATE, dueDate);
        }

        //  Used for tasks assigned by me or other
        if (assigner != null) {
            search.addFilterEqual(HkTaskField.CREATED_BY, assigner);
        }

        //  Due, Cancelled or Completed - Usually used for seeing completed tasks or due tasks
        if (status != null) {
            search.addFilterEqual(HkTaskField.STATUS, status);
        }

        if (categoryId != null) {
            search.addFilterEqual(HkTaskField.CATEGORY, categoryId);
        }

        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        if (includeRecipientDetails) {
            search.addFetch(HkTaskField.TASK_RECIPIENT_DTL_ENTITY_SET)
                    .setDistinct(true);
        }
        if (sortProperty != null) {
            search.addSort(sortProperty, isDesc);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkTaskEntity> retrieveTasksByCriteria(Boolean isRepetitive, Boolean isCompleted, Long franchise, Boolean archiveStatus) {
        Search search = new Search(HkTaskEntity.class);

        if (isRepetitive != null) {
            search.addFilterEqual(HkTaskField.IS_REPETITIVE, isRepetitive);
        }

        if (isCompleted != null) {
            if (isCompleted) {
                //  if only finished tasks needed, status should be completed
                search.addFilterEqual(HkTaskField.STATUS, HkSystemConstantUtil.TaskStatus.COMPLETED);
            } else {
                //  if only non-finished tasks needed, status should not be completed
                search.addFilterNotEqual(HkTaskField.STATUS, HkSystemConstantUtil.TaskStatus.COMPLETED);
            }
        }

        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        //  return the result
        return commonDao.search(search);
    }

    @Override
    public List<HkTaskEntity> retrieveTasksByStatuses(List<String> statusList, Long franchise, Boolean archiveStatus) {
        Search search = new Search(HkTaskEntity.class);
        if (!CollectionUtils.isEmpty(statusList)) {
            search.addFilterIn(HkTaskField.STATUS, statusList);
        }
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        search.addFetch(HkTaskField.TASK_RECIPIENT_DTL_ENTITY_SET)
                .setDistinct(true);
        return commonDao.search(search);
    }

    @Override
    public void updateTaskInfos(List<HkTaskEntity> taskListToUpdate) {
        commonDao.save(taskListToUpdate.toArray());
    }

    @Override
    public void updateTaskRecipientDtlInfos(List<HkTaskRecipientDtlEntity> taskRecipientDtlListToUpdate) {
        commonDao.save(taskRecipientDtlListToUpdate.toArray());
    }

    @Override
    public HkTaskRecipientDtlEntity retrieveTaskRecipientDtlById(Long recipientDtlId) {
        return commonDao.find(HkTaskRecipientDtlEntity.class, recipientDtlId);
    }

    @Override
    public void changeCategoryOfTasks(List<Long> receivedTaskIds, List<Long> createdTaskIds, Long newCategoryId, Long franchise) {
        if (!CollectionUtils.isEmpty(receivedTaskIds)) {
            Search search = SearchFactory.getSearch(HkTaskRecipientDtlEntity.class);
            search.addFilterIn(HkTaskRecipientDtlField.ID, receivedTaskIds);
            search.addFilterEqual(HkTaskRecipientDtlField.FRANCHISE, franchise);
            List<HkTaskRecipientDtlEntity> receivedTasks = commonDao.search(search);
            if (!CollectionUtils.isEmpty(receivedTasks)) {
                for (HkTaskRecipientDtlEntity receivedTask : receivedTasks) {
                    receivedTask.setCategory(newCategoryId);
                }
            }
        }
        if (!CollectionUtils.isEmpty(createdTaskIds)) {
            Search search = SearchFactory.getSearch(HkTaskEntity.class);
            search.addFilterIn(HkTaskField.ID, createdTaskIds);
            search.addFilterEqual(FRANCHISE, franchise);
            List<HkTaskEntity> createdTasks = commonDao.search(search);
            if (!CollectionUtils.isEmpty(createdTasks)) {
                for (HkTaskEntity myTask : createdTasks) {
                    myTask.setTaskCategory(newCategoryId);
                }
            }
        }
    }

    @Override
    public Map<Long, Integer> retrievePendingTaskCountGroupedByCategory(Long assignee, Long franchise, Boolean archiveStatus) {
        Search search = new Search(HkTaskRecipientDtlEntity.class);
        if (assignee != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.USER_ID, assignee);
        }
        search.addFilterEqual(HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.DUE);
        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        search.addField(HkTaskRecipientDtlField.CATEGORY);

        List<Long> categoryIdList = commonDao.search(search);
        Map<Long, Integer> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(categoryIdList)) {
            for (Long categoryId : categoryIdList) {
                Integer taskNos = resultMap.get(categoryId);
                if (taskNos == null) {
                    taskNos = 0;
                }
                taskNos += 1;
                resultMap.put(categoryId, taskNos);
            }
        }

        //  Search for the main task that are created by this user
        search = new Search(HkTaskEntity.class);
        if (assignee != null) {
            search.addFilterEqual(HkTaskField.CREATED_BY, assignee);
        }
        search.addFilterEqual(HkTaskField.STATUS, HkSystemConstantUtil.TaskStatus.DUE);
        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        search.addField(HkTaskField.CATEGORY);

        categoryIdList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(categoryIdList)) {
            for (Long categoryId : categoryIdList) {
                Integer taskNos = resultMap.get(categoryId);
                if (taskNos == null) {
                    taskNos = 0;
                }
                taskNos += 1;
                resultMap.put(categoryId, taskNos);
            }
        }

        return resultMap;
    }

    @Override
    public List<Long> retrieveTaskAssignerIds(Long assignee, Long franchise, Boolean archiveStatus) {
        Search search = new Search(HkTaskRecipientDtlEntity.class);
        if (assignee != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.USER_ID, assignee);
        }
        search.addFilterEqual(HkTaskRecipientDtlField.STATUS, HkSystemConstantUtil.TaskStatus.DUE);
        //  Check for franchise and archiveStatus fields
        if (franchise != null) {
            search.addFilterEqual(HkTaskRecipientDtlField.FRANCHISE, franchise);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        search.addField(HkTaskRecipientDtlField.TASK);
        search.addField(HkTaskRecipientDtlField.ON_DATE);
        search.addSortAsc(HkTaskRecipientDtlField.ON_DATE);

        List<HkTaskRecipientDtlEntity> recipientDtlList = commonDao.search(search);
        List<Long> assignersList = null;
        if (!CollectionUtils.isEmpty(recipientDtlList)) {
            assignersList = new LinkedList<>();
            for (HkTaskRecipientDtlEntity recipient : recipientDtlList) {
                if (!assignersList.contains(recipient.getTask().getCreatedBy())) {
                    assignersList.add(recipient.getTask().getCreatedBy());
                }
            }
        }

        return assignersList;
    }

    /*
     Leave methods start.........
     */
    @Override
    public void applyLeave(HkLeaveEntity leaveEntity, Long deptId, Long shiftId, String empCode, String empName, List<Long> notifyUserList) {
        //  Count total leave days
        Float totalLeaveDays = this.countTotalLeaveDays(leaveEntity, shiftId);
        leaveEntity.setTotalDays(totalLeaveDays);
        commonDao.save(leaveEntity);

        // Get the workflow id for the given department
        Long workflowId = this.retrieveWorkflowIdForDepartment(deptId, leaveEntity.getFranchise());

        if (workflowId != null) {
            //  Create approval entity if workflow exist for related department
            HkLeaveApprovalEntity leaveApprovalEntity = new HkLeaveApprovalEntity();
            leaveApprovalEntity.setLeaveRequest(leaveEntity);
            leaveApprovalEntity.setLevel(1);
            leaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
            leaveApprovalEntity.setWorkflow(workflowId);
            leaveApprovalEntity.setOnDate(new Date());
            commonDao.save(leaveApprovalEntity);

            //  Send notification
            Date frmDt = leaveEntity.getFrmDt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            String fromDate = simpleDateFormat.format(frmDt);
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, empCode);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, empName);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_DAYS, leaveEntity.getTotalDays());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_START_DATE, fromDate);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.LEAVE,
                    HkSystemConstantUtil.NotificationInstanceType.LEAVE_APPLIED, valuesMap, leaveEntity.getId(), leaveEntity.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        }
    }

    private Float countTotalLeaveDays(HkLeaveEntity leaveEntity, Long shiftId) {
        Float totalLeaveDays = 0F;
        //  This needs shift calculation, so do it later Remaining ***********************************
        Date frmDt = leaveEntity.getFrmDt();
        Date toDt = leaveEntity.getToDt();
        List<Date> retrieveDateWithOutHoliday = hkHrService.retrieveDateListExcludingHoliday(frmDt, toDt, leaveEntity.getFranchise());
        //System.out.println("retrieveDateWithOutHoliday :" + retrieveDateWithOutHoliday);
        if (!CollectionUtils.isEmpty(retrieveDateWithOutHoliday)) {
            Map<String, List<Date>> weekDayList = new HashMap<>();
            Calendar c = Calendar.getInstance();
            for (Date date : retrieveDateWithOutHoliday) {
                c.setTime(date);
                int get = c.get(Calendar.DAY_OF_WEEK);
                List<Date> get1 = weekDayList.get(String.valueOf(get));
                if (get1 == null) {
                    get1 = new ArrayList<>();
                }
                get1.add(date);
                weekDayList.put(String.valueOf(get), get1);
            }
            //System.out.println("weekDayList :" + weekDayList);
            HkShiftEntity retrieveShiftById = hkHrService.retrieveShiftById(leaveEntity.getFranchise(), shiftId);
            if (retrieveShiftById != null) {
                String weekDays = retrieveShiftById.getWeekDays();
                String[] split = weekDays.split(",");
                List<Date> temp = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    List<Date> get = weekDayList.get(split[i]);
                    if (get != null) {
                        temp.addAll(get);
                    }
                }
                //System.out.println("temp :" + temp);
                Integer shiftDurationMin = 0;
                Set<HkShiftDtlEntity> hkShiftDtlEntitySet = retrieveShiftById.getHkShiftDtlEntitySet();
                for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
                    if (hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                        shiftDurationMin = hkShiftDtlEntity.getShiftDurationMin();
                        if (shiftDurationMin != null && shiftDurationMin != 0) {
                            for (Date date : temp) {
                                Date shiftStartTime = setDateAndTime(date, hkShiftDtlEntity.getStrtTime());
                                Date shiftEndTime = setDateAndTime(date, hkShiftDtlEntity.getEndTime());
                                float perDayMin = 0;
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                boolean startEqual = fmt.format(date).equals(fmt.format(frmDt));
                                boolean endEqual = fmt.format(date).equals(fmt.format(toDt));
                                //System.out.println("frmDt :" + frmDt);
                                //System.out.println("toDt :" + toDt);
                                boolean bothOnSameDay = fmt.format(frmDt).equals(fmt.format(toDt));
                                if (bothOnSameDay) {
                                    if (toDt.before(shiftStartTime) || shiftEndTime.before(frmDt)) {
                                    } else if (frmDt.equals(shiftStartTime) && toDt.equals(shiftEndTime)) {
                                        perDayMin = shiftDurationMin;
                                    } else if (frmDt.before(shiftStartTime) && shiftEndTime.before(toDt)) {
                                        perDayMin = shiftDurationMin;
                                    } else {
                                        long diff = toDt.getTime() - frmDt.getTime();
                                        long min = diff / (60 * 1000);
                                        perDayMin = new Integer(String.valueOf(min));
                                    }
                                } else {
                                    if (startEqual) {
                                        if (frmDt.equals(shiftStartTime)) {
                                            perDayMin = shiftDurationMin;
                                        } else if (frmDt.before(shiftStartTime)) {
                                            perDayMin = shiftDurationMin;
                                        } else {
                                            long diff = shiftEndTime.getTime() - frmDt.getTime();
                                            long min = diff / (60 * 1000);
                                            perDayMin = new Integer(String.valueOf(min));
                                        }
                                    }
                                    if (endEqual) {
                                        if (toDt.equals(shiftEndTime)) {
                                            perDayMin = shiftDurationMin;
                                        } else if (shiftEndTime.before(toDt)) {
                                            perDayMin = shiftDurationMin;
                                        } else {
                                            long diff = toDt.getTime() - shiftStartTime.getTime();
                                            long min = diff / (60 * 1000);
                                            perDayMin = new Integer(String.valueOf(min));
                                        }
                                    }
                                    if (!endEqual && !startEqual) {
                                        if (shiftStartTime.before(frmDt) && toDt.before(shiftEndTime)) {
                                            long diffStart = shiftEndTime.getTime() - frmDt.getTime();
                                            long diffEnd = toDt.getTime() - shiftStartTime.getTime();
                                            long totalDiff = diffEnd + diffStart;
                                            long min = totalDiff / (60 * 1000);
                                            perDayMin = new Integer(String.valueOf(min));
                                        } else {
                                            perDayMin = shiftDurationMin;
                                        }

                                    }
                                }
                                totalLeaveDays = totalLeaveDays + (perDayMin / shiftDurationMin);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return totalLeaveDays;
    }

    public Date setDateAndTime(Date date, Date timeDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, timeDate.getHours());
        calendar.set(Calendar.MINUTE, timeDate.getMinutes());
        calendar.set(Calendar.SECOND, timeDate.getSeconds());
        return calendar.getTime();
    }

    private Long retrieveWorkflowIdForDepartment(Long deptId, Long franchise) {
        HkWorkflowEntity workflowEntity = hkHrService.retrieveWorkflowForDepartment(deptId, franchise, false);
        if (workflowEntity != null) {
            return workflowEntity.getId();
        }
        return null;
    }

    @Override
    public void updateLeave(HkLeaveEntity leaveEntity, String empCode, String empName, Long shiftId, List<Long> notifyUserList) {
        //  if the status of the leave is pending, there is no need to do any change in workflow as the user will see the request of cancellation now
        //  if the status of the leave is approved, retrieve last level of the workflow who had approved this leave request
        if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
            HkLeaveApprovalEntity leaveApprovalEntity = this.retrieveLeaveApprovalEntitiesByCriteria(leaveEntity.getId(), null, Arrays.asList(HkSystemConstantUtil.LeaveStatus.APPROVED), null, null).get(0);
            HkLeaveApprovalEntity newLeaveApprovalEntity = new HkLeaveApprovalEntity();
            newLeaveApprovalEntity.setLeaveRequest(leaveEntity);
            newLeaveApprovalEntity.setLevel(leaveApprovalEntity.getLevel());
            newLeaveApprovalEntity.setOnDate(new Date());
            newLeaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
            newLeaveApprovalEntity.setWorkflow(leaveApprovalEntity.getWorkflow());
            commonDao.save(newLeaveApprovalEntity);

            //  Set status of leave to pending
            leaveEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
        }
        if (shiftId != null) {
            Float countTotalLeaveDays = this.countTotalLeaveDays(leaveEntity, shiftId);
            leaveEntity.setTotalDays(countTotalLeaveDays);
        }

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, empCode);
        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, empName);
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.LEAVE,
                HkSystemConstantUtil.NotificationInstanceType.UPDATE_LEAVE, valuesMap, leaveEntity.getId(), leaveEntity.getFranchise());
        notificationService.sendNotification(notification, notifyUserList);

        commonDao.save(leaveEntity);
    }

    @Override
    public void cancelLeave(Long leaveId, String empCode, String empName, List<Long> notifyUserList) {
        HkLeaveEntity leaveEntity = commonDao.find(HkLeaveEntity.class, leaveId);
        //  if the status of the leave is pending, there is no need to do any change in workflow as the user will see the request of cancellation now
        //  if the status of the leave is approved, retrieve last level of the workflow who had approved this leave request
        if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
            HkLeaveApprovalEntity leaveApprovalEntity = this.retrieveLeaveApprovalEntitiesByCriteria(leaveEntity.getId(), null, Arrays.asList(HkSystemConstantUtil.LeaveStatus.APPROVED), null, null).get(0);
            HkLeaveApprovalEntity newLeaveApprovalEntity = new HkLeaveApprovalEntity();
            newLeaveApprovalEntity.setLeaveRequest(leaveEntity);
            newLeaveApprovalEntity.setLevel(leaveApprovalEntity.getLevel());
            newLeaveApprovalEntity.setOnDate(new Date());
            newLeaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
            newLeaveApprovalEntity.setWorkflow(leaveApprovalEntity.getWorkflow());
            leaveApprovalEntity.setStatus(HkSystemConstantUtil.INACTIVE);
            commonDao.save(newLeaveApprovalEntity);
        } else if (leaveEntity.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING)) {
            //   if leave approval is pending, cancel the pending request as the user does not need to see if the pending request was cancelled
            HkLeaveApprovalEntity leaveApprovalEntity = this.retrieveLeaveApprovalEntitiesByCriteria(leaveId, null, Arrays.asList(HkSystemConstantUtil.LeaveStatus.PENDING), null, null).get(0);
            leaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.CANCELED);
        }

        leaveEntity.setStatus(HkSystemConstantUtil.LeaveStatus.CANCELED);

        commonDao.save(leaveEntity);

        //  Send notification
        Map<String, Object> valuesMap = new HashMap<>();

        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, empCode);

        valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, empName);
        HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.LEAVE,
                HkSystemConstantUtil.NotificationInstanceType.CANCEL_LEAVE, valuesMap, leaveEntity.getId(), leaveEntity.getFranchise());

        notificationService.sendNotification(notification, notifyUserList);
    }

    @Override
    public void cancelApprovedLeave(Long leaveId, Long cancelledBy, String remarks) {

        if (leaveId != null) {
            List<HkLeaveApprovalEntity> leaveApprovalEntities = this.retrieveLeaveApprovalEntitiesByCriteria(leaveId, 1, null, null, null);
            if (!CollectionUtils.isEmpty(leaveApprovalEntities)) {
                HkLeaveApprovalEntity leaveApprovalEntity = leaveApprovalEntities.get(0);
                if (leaveApprovalEntity != null) {
                    if (StringUtils.hasText(remarks)) {
                        leaveApprovalEntity.setRemarks(leaveApprovalEntity.getRemarks() + remarks);
                    }

                    leaveApprovalEntity.setAttendedBy(cancelledBy);
                    leaveApprovalEntity.setAttendedOn(new Date());
                    leaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.APPROVED_CANCELED);
                    //  **********************notification
                }
            }
        }
    }

    @Override
    public void archiveLeave(Long leaveId) {
        HkLeaveEntity leaveEntity = commonDao.find(HkLeaveEntity.class, leaveId);
        leaveEntity.setIsArchive(true);
        commonDao.save(leaveEntity);
    }

    @Override
    public List<HkLeaveEntity> searchMyLeaves(String searchStr, Long userId, Long franchise) {
        List<Long> matchedReasonIds = hkFoundationService.searchMasterValues(franchise, HkSystemConstantUtil.MasterCode.LEAVE_REASON, searchStr);

        Criteria criteria = commonDao.getCurrentSession().createCriteria(HkLeaveEntity.class);

        Criterion reasonIdCri = null;

        if (!CollectionUtils.isEmpty(matchedReasonIds)) {
            reasonIdCri = Restrictions.in(HkLeaveField.LEAVE_REASON, matchedReasonIds);
        }
        Criterion dateCri = Restrictions.or(
                getDateRestrictions(searchStr, "frm_dt"),
                getDateRestrictions(searchStr, "to_dt"));
        if (reasonIdCri
                != null) {
            criteria.add(Restrictions.or(reasonIdCri, dateCri));
        } else {
            criteria.add(dateCri);
        }

        //  The user who is searching for his own leaves
        criteria.add(Restrictions.eq(HkLeaveField.FOR_USER, userId));
        criteria.add(Restrictions.eq(FRANCHISE, franchise));
        criteria.add(Restrictions.eq(IS_ARCHIVE, false));

        List<HkLeaveEntity> resultList = criteria.list();
        return resultList;
    }

    @Override
    public List<HkLeaveApprovalEntity> searchLeavesForApprover(String searchStr, List<Long> applierIds, Long deptId, Long userId, Long franchise) {
        List<HkWorkflowApproverEntity> workflowApproverEntitys = hkHrService.retrieveWorkFlowApprover(userId, deptId);
        List<Long> workFlowIds = null;
        if (!CollectionUtils.isEmpty(workflowApproverEntitys)) {
            workFlowIds = new LinkedList<>();
            for (HkWorkflowApproverEntity hkWorkflowApproverEntity : workflowApproverEntitys) {
                workFlowIds.add(hkWorkflowApproverEntity.getHkWorkflowApproverEntityPK().getWorkflow());
            }
        }
        List<Long> matchedReasonIds = hkFoundationService.searchMasterValues(franchise, HkSystemConstantUtil.MasterCode.LEAVE_REASON, searchStr);

        Criteria leaveApprovalCriteria = commonDao.getCurrentSession().createCriteria(HkLeaveApprovalEntity.class);
        Criterion reasonIdCri = null;
        Criterion applierCri = null;

        Criteria leaveCriteria = leaveApprovalCriteria.createCriteria(HkLeaveApprovalField.LEAVE_REQUEST);
        Criterion dateCri = Restrictions.or(
                getDateRestrictions(searchStr, "frm_dt"),
                getDateRestrictions(searchStr, "to_dt"));

        if (!CollectionUtils.isEmpty(matchedReasonIds)) {
            reasonIdCri = Restrictions.in(HkLeaveField.LEAVE_REASON, matchedReasonIds);
        }

        if (!CollectionUtils.isEmpty(applierIds)) {
            applierCri = Restrictions.in(HkLeaveField.FOR_USER, applierIds);
        }

        if (!CollectionUtils.isEmpty(applierIds)
                && reasonIdCri != null) {
            leaveCriteria.add(Restrictions.or(reasonIdCri, dateCri, applierCri));
        } else if (CollectionUtils.isEmpty(applierIds)
                && reasonIdCri != null) {
            leaveCriteria.add(Restrictions.or(dateCri, reasonIdCri));
        } else if (reasonIdCri
                == null && !CollectionUtils.isEmpty(applierIds)) {
            leaveCriteria.add(Restrictions.or(dateCri, applierCri));
        } else {
            leaveCriteria.add(dateCri);
        }

        leaveCriteria.add(Restrictions.eq(FRANCHISE, franchise));

        //  If the leave is pending and is from same workflow as the user is, add it in search results or if the leave is attended by this user
        if (!CollectionUtils.isEmpty(workFlowIds)) {
            leaveApprovalCriteria.add(Restrictions.or(
                    Restrictions.and(
                            Restrictions.in(HkLeaveApprovalField.WORKFLOW, workFlowIds),
                            Restrictions.eq(HkLeaveApprovalField.STATUS, HkSystemConstantUtil.LeaveStatus.PENDING)),
                    Restrictions.eq(HkLeaveApprovalField.ATTENDED_BY, userId)));

            leaveApprovalCriteria.add(Restrictions.not(Restrictions.in(HkLeaveApprovalField.STATUS, Arrays.asList(HkSystemConstantUtil.INACTIVE, HkSystemConstantUtil.LeaveStatus.CANCELED, HkSystemConstantUtil.LeaveStatus.APPROVED_CANCELED))));

            List<HkLeaveApprovalEntity> resultList = leaveApprovalCriteria.list();
            return resultList;
        } else {
            return null;
        }
    }

    @Override
    public List<Long> isUserOnLeave(Long userId, Date fromDate, Date toDate, List<String> statusList, Long franchise) {
        Search search = new Search(HkLeaveEntity.class
        );
        System.out.println("user id" + userId);
        System.out.println("from date" + fromDate);
        System.out.println("to date" + toDate);
        System.out.println("statusList" + statusList);
        System.out.println("franchise" + franchise);
        search.addFilterEqual(HkLeaveField.FOR_USER, userId);

        //  if given leave date is greater than from date and less than to date
        if (fromDate != null && toDate
                != null) {
            search.addFilterAnd(Filter.lessOrEqual(HkLeaveField.FROM_DATE, toDate),
                    Filter.greaterOrEqual(HkLeaveField.TO_DATE, fromDate));
        }

        search.addFilterIn(HkLeaveField.STATUS, statusList);

        if (franchise
                != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }

        search.addFilterEqual(IS_ARCHIVE,
                false);
        search.addField(HkLeaveField.ID);

        return commonDao.search(search);
    }

    @Override
    public HkLeaveEntity
            retrieveLeaveById(Long leaveId, boolean includeLeaveApprovals) {
        HkLeaveEntity leaveEntity = commonDao.find(HkLeaveEntity.class, leaveId);
        if (leaveEntity != null && includeLeaveApprovals) {
            leaveEntity.setHkLeaveApprovalEntitySet(leaveEntity.getHkLeaveApprovalEntitySet());
        }
        return leaveEntity;
    }

    @Override
    public List<HkLeaveEntity> retrieveLeavesByCriteria(Long userId, List<String> statusList, Long franchise, Boolean archiveStatus) {
        Search search = new Search(HkLeaveEntity.class
        );
        if (userId
                != null) {
            search.addFilterEqual(HkLeaveField.FOR_USER, userId);
        }
        if (statusList
                != null) {
            search.addFilterIn(HkLeaveField.STATUS, statusList);
        }
        if (franchise
                != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }
        if (archiveStatus
                != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        return commonDao.search(search);
    }

    @Override
    public List<HkLeaveApprovalEntity> retrieveLeaveApprovalEntitiesByCriteria(Long leaveId, Integer level, List<String> statusList, Long deptId, Long franchise) {
        Search search = new Search(HkLeaveApprovalEntity.class);
        if (leaveId != null) {
            search.addFilterEqual(HkLeaveApprovalField.LEAVE_ID, leaveId);
        }

        if (level != null) {
            search.addFilterEqual(HkLeaveApprovalField.LEVEL, level);
        }

        if (!CollectionUtils.isEmpty(statusList)) {
            search.addFilterIn(HkLeaveApprovalField.STATUS, statusList);
        }

        search.addFilterNotEqual(HkLeaveApprovalField.STATUS, HkSystemConstantUtil.INACTIVE);

        // Get the workflow id for the given department
        if (deptId != null && franchise != null) {
            Long workflowId = this.retrieveWorkflowIdForDepartment(deptId, franchise);

            if (workflowId != null) {
                search.addFilterEqual(HkLeaveApprovalField.WORKFLOW, workflowId);
            }
        }

        return commonDao.search(search);
    }

    @Override
    public void respondLeave(Long leaveId, Long responseBy, String remarks, String status, String empCode, String empName, String approverName, List<Long> notifyUserList) {
        List<HkLeaveApprovalEntity> leaveApprovalList = this.retrieveLeaveApprovalEntitiesByCriteria(leaveId, null, Arrays.asList(HkSystemConstantUtil.LeaveStatus.PENDING), null, null);
        HkLeaveApprovalEntity leaveApprovalEntity;
        if (!CollectionUtils.isEmpty(leaveApprovalList)) {
            //  Change status of existing leave approval entity
            leaveApprovalEntity = leaveApprovalList.get(0);
            leaveApprovalEntity.setAttendedBy(responseBy);
            leaveApprovalEntity.setAttendedOn(new Date());
            leaveApprovalEntity.setRemarks(remarks);
            leaveApprovalEntity.setStatus(status);
            commonDao.save(leaveApprovalEntity);

            HkLeaveEntity leaveEntity = leaveApprovalEntity.getLeaveRequest();
            // If status is approved, update leave entity and no need to go further in workflow
            //Reverse Logic based on client requirement MM : 30-Jun-2015
            if (status.equals(HkSystemConstantUtil.LeaveStatus.DISAPPROVED)) {
                leaveEntity.setStatus(status);
                commonDao.save(leaveEntity);
            } else if (status.equals(HkSystemConstantUtil.LeaveStatus.APPROVED)) {
                //  Retrieve related workflow
                HkWorkflowEntity workflowEntity = hkHrService.retrieveWorkflowById(leaveApprovalEntity.getWorkflow(), false);
                //  If the level of this approval request was the last, update leave with the final status
                if (workflowEntity.getLastLevel().equals(leaveApprovalEntity.getLevel())) {
                    leaveEntity.setStatus(status);
                    commonDao.save(leaveEntity);
                } else {
                    //  if that was not the last level and the leave is disapproved,
                    //  generate other request for workflow's next level
                    HkLeaveApprovalEntity nextLeaveApprovalEntity = new HkLeaveApprovalEntity();
                    nextLeaveApprovalEntity.setLeaveRequest(leaveEntity);
                    nextLeaveApprovalEntity.setLevel(leaveApprovalEntity.getLevel() + 1);
                    nextLeaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
                    nextLeaveApprovalEntity.setWorkflow(leaveApprovalEntity.getWorkflow());
                    nextLeaveApprovalEntity.setOnDate(new Date());
                    commonDao.save(nextLeaveApprovalEntity);
                }
            } else if (status.equals(HkSystemConstantUtil.LeaveStatus.COMMENTED)) {
                //  Create new approval entity for same level if only comment was given by the approver
                HkLeaveApprovalEntity newLeaveApprovalEntity = new HkLeaveApprovalEntity();
                newLeaveApprovalEntity.setLeaveRequest(leaveEntity);
                newLeaveApprovalEntity.setLevel(leaveApprovalEntity.getLevel());
                newLeaveApprovalEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
                newLeaveApprovalEntity.setWorkflow(leaveApprovalEntity.getWorkflow());
                newLeaveApprovalEntity.setOnDate(new Date());
                commonDao.save(newLeaveApprovalEntity);
            }

            //  Send notification to the applier
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_DAYS, leaveEntity.getTotalDays());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_STATUS, HkSystemConstantUtil.LEAVE_STATUS_MAP.get(leaveEntity.getStatus()));
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_START_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + leaveEntity.getFrmDt().getTime());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.APPROVER_NAME, approverName);
            HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.LEAVE,
                    HkSystemConstantUtil.NotificationInstanceType.LEAVE_RESPONSE_MINE, valuesMap, leaveEntity.getId(), leaveEntity.getFranchise());
            notificationService.sendNotification(notification, Arrays.asList(leaveEntity.getForUser()));

            //  Send notification to all the hierarchical users
            valuesMap = new HashMap<>();
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_DAYS, leaveEntity.getTotalDays());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_STATUS, HkSystemConstantUtil.LEAVE_STATUS_MAP.get(leaveEntity.getStatus()));
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.LEAVE_START_DATE, HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR + leaveEntity.getFrmDt().getTime());
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.APPROVER_NAME, approverName);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_NAME, empName);
            valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.EMP_CODE, empCode);
            notification = createNotification(HkSystemConstantUtil.NotificationType.LEAVE,
                    HkSystemConstantUtil.NotificationInstanceType.LEAVE_RESPONSE_OTHER, valuesMap, leaveEntity.getId(), leaveEntity.getFranchise());
            notificationService.sendNotification(notification, notifyUserList);
        }
    }

    @Override
    public void createUserOperation(HkUserOperationEnum userOperation, Date onTime, Long userId, String comments, Long addedByUser, Long franchise) {

        if (userId != null) {
            //shiftDtl - add private method for fetching shift detail for the user. Use HkUserWorkHistoryEntity and HkShiftDtlEntity for retrieval
            //Save User Operation
            //Prepared HkUserOperationEntity Object using above parameters
            HkUserOperationEntity userOperationEntity = new HkUserOperationEntity();
            HkUserOperationEntityPK userOperationEntityPK = new HkUserOperationEntityPK();
            userOperationEntityPK.setCreatedOn(new Date());
            userOperationEntityPK.setEventCode(userOperation.value());
            userOperationEntityPK.setUserId(userId);
            userOperationEntity.setOnTime(onTime);
            userOperationEntity.setHkUserOperationEntityPK(userOperationEntityPK);
            userOperationEntity.setFranchise(franchise);
            //Default Value for Status is A, CreatedOn = Current Date Time, isArchive = false, is_attended = false, lastModifiedOn = Current Date Time
            userOperationEntity.setIsArchive(Boolean.FALSE);
            userOperationEntity.setIsAttended(false);
            userOperationEntity.setLastModifiedOn(new Date());
            userOperationEntity.setStatus(HkSystemConstantUtil.ACTIVE);
            if (addedByUser != null) {
                userOperationEntity.setLastModifiedBy(addedByUser);
            }
            if (comments != null) {
                userOperationEntity.setComments(comments);
            }
            //retrieved userWorkHistoryEntity entity based on userId 
            HkUserWorkHistoryEntity userWorkHistoryEntity = this.retrieveShiftForUserFromUserWorkHistory(userId);
            if (userWorkHistoryEntity != null) {
                //get the shift id 
                long shiftId = userWorkHistoryEntity.getShift();
                //get the object of main shift entity based on shiftId
                HkShiftEntity shift = hkHrService.retrieveCurrentShiftForUserOperation(shiftId, new Date());
                if (shift != null) {
                    //get all the shift details set
                    Set<HkShiftDtlEntity> hkShiftDtlEntitySet = shift.getHkShiftDtlEntitySet();
                    if (hkShiftDtlEntitySet != null && !hkShiftDtlEntitySet.isEmpty()) {
                        //check whether it's a Main shift or a break shift
                        for (HkShiftDtlEntity hkShiftDtlEntity : hkShiftDtlEntitySet) {
                            if (hkShiftDtlEntity.getSlotType().equalsIgnoreCase(HkSystemConstantUtil.SHIFT_TYPE.MAIN_TIME)) {
                                userOperationEntity.setShiftDtl(hkShiftDtlEntity.getId());
                            }
                        }
                    }
                }
            }
            commonDao.save(userOperationEntity);
        }
    }

    @Override
    public List<HkUserOperationEntity> retrieveUserOperations(Date fromDate, Date toDate, Long userId, Long franchise) {
        //To change body of generated methods, choose Tools | Templates.
        //Follow JavaDoc comments for retrival logic
        Search search = new Search(HkUserOperationEntity.class
        );
        if (fromDate != null && toDate
                != null) {
            search.addFilterAnd(Filter.greaterOrEqual(HkUserOperationField.ON_TIME, fromDate),
                    Filter.lessOrEqual(HkUserOperationField.ON_TIME, toDate));
        }
        if (franchise
                != null) {
            search.addFilterEqual(HkUserOperationField.FRANCHISE, franchise);
        }
        if (userId
                != null) {
            search.addFilterEqual(HkUserOperationField.USER_ID, userId);
        }
        if (franchise == null && userId
                == null) {
            return null;
        }

        return commonDao.search(search);
    }

    @Override
    public void calculateUserAttendance() {
        //Retrieve all records from HkUserOperationEntity for which isAttended field is false
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Search search = new Search(HkUserOperationEntity.class
        );
        search.addFilterEqual(HkUserOperationField.IS_ATTENDED,
                false);
        search.addSort(Sort.asc(HkUserOperationField.ON_TIME));
        List<HkUserOperationEntity> userOperationEntitys = commonDao.search(search);
        //Arrange data userwise, also collect all require shift_dtl ids 
        Map<Long, List<HkUserOperationEntity>> userWiseUserOpertationMap = null;
        List<Long> shiftDtlIds = null;
        Map<Long, Map<Date, Long>> userShiftMap = null;
        Map<Long, Long> userFrenchiseMap = null;

        if (!CollectionUtils.isEmpty(userOperationEntitys)) {
            userShiftMap = new HashMap<>();
            userWiseUserOpertationMap = new HashMap<>();
            userFrenchiseMap = new HashMap<>();
            shiftDtlIds = new ArrayList<>();
            for (HkUserOperationEntity hkUserOperationEntity : userOperationEntitys) {
                long userId = hkUserOperationEntity.getHkUserOperationEntityPK().getUserId();
                shiftDtlIds.add(hkUserOperationEntity.getShiftDtl());
                userFrenchiseMap.put(userId, hkUserOperationEntity.getFranchise());
                Map<Date, Long> dateWiseShiftMap = userShiftMap.get(userId);
                if (dateWiseShiftMap == null) {
                    dateWiseShiftMap = new LinkedHashMap<>();
                }
                Date onTime = HkSystemFunctionUtil.getDateWithoutTimeStamp(hkUserOperationEntity.getOnTime());
                if (!dateWiseShiftMap.containsKey(onTime)) {
                    dateWiseShiftMap.put(onTime, hkUserOperationEntity.getShiftDtl());
                    userShiftMap.put(userId, dateWiseShiftMap);
                }
                List<HkUserOperationEntity> tempUserOperationEntitys = userWiseUserOpertationMap.get(userId);
                if (tempUserOperationEntitys == null) {
                    tempUserOperationEntitys = new ArrayList<>();
                }
                tempUserOperationEntitys.add(hkUserOperationEntity);
                userWiseUserOpertationMap.put(userId, tempUserOperationEntitys);
                if (hkUserOperationEntity.getOnTime().before(minDate.getTime())) {
                    minDate.setTime(hkUserOperationEntity.getOnTime());
                }
                if (hkUserOperationEntity.getOnTime().after(maxDate.getTime())) {
                    maxDate.setTime(hkUserOperationEntity.getOnTime());
                }
            }
        }

        minDate.setTime(HkSystemFunctionUtil.getDateWithoutTimeStamp(minDate.getTime()));
        maxDate.setTime(HkSystemFunctionUtil.getDateWithoutTimeStamp(maxDate.getTime()));

        //Retrieve Leave and Holiday details too
//        List<HkHolidayEntity> holidayEntitys = hkHrService.retrieveAllHolidays(franchise, Boolean.FALSE);
//        List<String> statusList = new ArrayList<>();
//        statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
//        statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
//        List<HkLeaveEntity> leaveEntitys = this.retrieveLeavesByCriteria(null, statusList, franchise, Boolean.FALSE);
        //Retrieve all shift_dtls and prepare map based on that
        search = SearchFactory.getSearch(HkShiftDtlEntity.class);

        search.addFilterIn(
                "id", shiftDtlIds);
        List<HkShiftDtlEntity> shiftDtls = commonDao.search(search);
        Map<Long, HkShiftDtlEntity> shiftDtlMap = null;
        if (shiftDtls
                != null && !shiftDtls.isEmpty()) {
            shiftDtlMap = new HashMap<>();
            for (HkShiftDtlEntity hkShiftDtlEntity : shiftDtls) {
                shiftDtlMap.put(hkShiftDtlEntity.getId(), hkShiftDtlEntity);
            }
        }

        if (!CollectionUtils.isEmpty(shiftDtlMap)
                && !CollectionUtils.isEmpty(userShiftMap) && !CollectionUtils.isEmpty(userWiseUserOpertationMap)) {
            List<HkUserAttendanceEntity> userAttendanceEntitys = new LinkedList<>();
            List<HkUserOperationEntity> editUserOperationEntitys = new LinkedList<>();
            for (Map.Entry<Long, List<HkUserOperationEntity>> entry : userWiseUserOpertationMap.entrySet()) {
                Long userId = entry.getKey();
                if (userId != null) {
                    Map<Date, Long> dateWiseShiftDtl = userShiftMap.get(userId);
                    if (dateWiseShiftDtl != null) {
                        List<HkUserOperationEntity> userOperationList = entry.getValue();

                        Calendar onDate = minDate;
                        while (onDate.before(maxDate) || onDate.equals(maxDate)) {

                            Long shtfDtl = dateWiseShiftDtl.get(onDate.getTime());
                            if (shtfDtl != null) {
                                HkShiftDtlEntity shiftDtlEntity = shiftDtlMap.get(shtfDtl);

                                if (shiftDtlEntity != null) {
                                    Calendar calShiftStartTime = Calendar.getInstance();
                                    calShiftStartTime.setTime(HkSystemFunctionUtil.mergeDateAndTime(onDate.getTime(), shiftDtlEntity.getStrtTime()));

                                    Calendar calShiftEndTime = Calendar.getInstance();
                                    calShiftEndTime.setTime(HkSystemFunctionUtil.mergeDateAndTime(onDate.getTime(), shiftDtlEntity.getEndTime()));
                                    if (calShiftEndTime.before(calShiftStartTime)) {
                                        calShiftEndTime.add(Calendar.DATE, 1);
                                    }

                                    calShiftStartTime.add(Calendar.MINUTE, (shiftDtlEntity.getShiftDurationMin() * -1));
                                    calShiftEndTime.add(Calendar.MINUTE, shiftDtlEntity.getShiftDurationMin());

                                    Long logInTime = 0l;
                                    Long punchTime = 0l;
                                    Long startTimeLogIn = 0l;
                                    Long startTimePunchIn = 0l;

                                    String status = HkSystemConstantUtil.AttendanceStatus.PRESENT;
                                    List<HkUserOperationEntity> tempUserOperationEntitys = new ArrayList<>();
                                    if (!CollectionUtils.isEmpty(userOperationList)) {
                                        boolean isException = false;
                                        for (HkUserOperationEntity hkUserOperationEntity : userOperationList) {
                                            if (calShiftStartTime.before(hkUserOperationEntity.getOnTime()) && calShiftEndTime.after(hkUserOperationEntity.getOnTime())) {
                                                tempUserOperationEntitys.add(hkUserOperationEntity);
                                                int eventCode = hkUserOperationEntity.getHkUserOperationEntityPK().getEventCode();
                                                long operationTime = hkUserOperationEntity.getOnTime().getTime();
                                                if (eventCode == HkUserOperationEnum.PUNCH_IN.value()) {
                                                    if (startTimePunchIn == 0) {
                                                        startTimePunchIn = operationTime;
                                                    } else {
                                                        isException = true;
                                                    }
                                                }
                                                if (eventCode == HkUserOperationEnum.LOGIN.value()) {
                                                    if (startTimeLogIn == 0) {
                                                        startTimeLogIn = operationTime;
                                                    } else {
                                                        isException = true;
                                                    }
                                                }
                                                if (eventCode == HkUserOperationEnum.PUNCH_OUT.value()) {
                                                    if (startTimePunchIn == 0) {
                                                        isException = true;
                                                    } else {
                                                        punchTime = punchTime + (operationTime - startTimePunchIn) / 60000;
                                                    }
                                                }

                                                if (eventCode == HkUserOperationEnum.LOGOUT.value()) {
                                                    if (startTimeLogIn == 0) {
                                                        isException = true;
                                                    } else {
                                                        logInTime = logInTime + (operationTime - startTimeLogIn) / 60000;
                                                    }
                                                }
                                                if (isException) {
                                                    status = HkSystemConstantUtil.AttendanceStatus.EXCEPTION;
                                                    logInTime = 0l;
                                                    punchTime = 0l;
                                                    break;
                                                }
                                            }
                                            if (!isException) {
                                                editUserOperationEntitys.addAll(tempUserOperationEntitys);
                                            }
                                            float shiftHours = ((float) shiftDtlEntity.getShiftDurationMin()) / 60f;
                                            userAttendanceEntitys.add(this.makeHkUserAttandanceEntity(userId, onDate.getTime(), status, userFrenchiseMap.get(userId), ((float) logInTime / 60f), shiftHours, ((float) punchTime / 60f)));
                                        }
                                    }
                                }
                                onDate.add(Calendar.DATE, 1);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(userAttendanceEntitys)) {
                commonDao.saveAll(userAttendanceEntitys);
            }
            if (!CollectionUtils.isEmpty(editUserOperationEntitys)) {
                List<HkUserOperationEntity> toBeEditUserOperations = new ArrayList<>();
                for (HkUserOperationEntity hkUserOperationEntity : editUserOperationEntitys) {
                    hkUserOperationEntity.setIsAttended(true);
                    toBeEditUserOperations.add(hkUserOperationEntity);
                }
                commonDao.saveAll(toBeEditUserOperations);
            }
        }

        //Based on the shift-dtl associated with the Login or Punchin(if login is not available) calculate the working hrs for that day and store that in shift_hours field
        //Based on HkUserOperation data, leave and holiday status field remaining fields
        //If HkUserOperation data for the specific date is not complete then just keep shift_hrs and keep office and login hrs as 0. For those HkUserOperation data don't change the is_attended flag
        //If data is proper then change is_attended flag of HkUserOperation to true
    }

    private HkUserAttendanceEntity makeHkUserAttandanceEntity(long userId, Date onDate, String attendanceType, Long franchise, float loginHours, float shiftHours, float officeHours) {
        HkUserAttendanceEntity hkUserAttendanceEntity = new HkUserAttendanceEntity();
        HkUserAttendanceEntityPK hkUserAttendanceEntityPK = new HkUserAttendanceEntityPK();
        hkUserAttendanceEntityPK.setUserId(userId);
        hkUserAttendanceEntityPK.setOnDate(onDate);
        hkUserAttendanceEntity.setHkUserAttendanceEntityPK(hkUserAttendanceEntityPK);
        hkUserAttendanceEntity.setFranchise(franchise);
        hkUserAttendanceEntity.setIsArchive(false);
        hkUserAttendanceEntity.setLoginHours(loginHours);
        hkUserAttendanceEntity.setShiftHours(shiftHours);
        hkUserAttendanceEntity.setOfficeHours(officeHours);
        return hkUserAttendanceEntity;
    }

    @Override
    public List<HkUserAttendanceEntity> retrieveUserAttendance(Date fromDate, Date toDate, Long franchise) {
        //To change body of generated methods, choose Tools | Templates.
        //Follow JavaDoc comments for retrival logic
        Search search = new Search(HkUserAttendanceEntity.class
        );
        if (fromDate != null && toDate
                != null) {
            search.addFilterAnd(Filter.greaterOrEqual(HkUserAttendanceField.ON_DATE, fromDate),
                    Filter.lessOrEqual(HkUserAttendanceField.ON_DATE, toDate));
        }

        search.addFilterEqual(HkUserAttendanceField.FRANCHISE, franchise);
        if (franchise
                == null) {
            return null;
        }

        return commonDao.search(search);
    }

    @Override
    public
            void updateUserOperation(HkUserOperationEnum oldUserOperation, Date oldOnTime, HkUserOperationEnum newUserOperation, Date newOnTime, Long userId, String comments, Long modifiedByUser, Long franchise) {
        //To change body of generated methods, choose Tools | Templates.
        //Based On oldUserOperation and oldOnTime field fetch old data from the system. 
        //Add proper comments and lastmodifiedby info and change status to Delete(D)
        //Create new record for HkUserOperationEntity based on oldData and newUserOperation and newOnTime field
        Search search = SearchFactory.getSearch(HkUserOperationEntity.class
        );
        search.addFilterEqual(HkUserOperationField.ON_TIME, oldOnTime);

        search.addFilterEqual(HkUserOperationField.USER_OPERATION, oldUserOperation.value());
        HkUserOperationEntity hkUserOperationEntity = (HkUserOperationEntity) commonDao.searchUnique(search);
        if (hkUserOperationEntity
                != null) {
            hkUserOperationEntity.setStatus(HkSystemConstantUtil.ARCHIVED);
            if (comments != null) {
                hkUserOperationEntity.setComments(comments);
            }
            if (modifiedByUser != null) {
                hkUserOperationEntity.setLastModifiedBy(modifiedByUser);
            }
            hkUserOperationEntity.setLastModifiedOn(new Date());
            commonDao.save(hkUserOperationEntity);
        }

        this.createUserOperation(newUserOperation, newOnTime, userId,
                null, modifiedByUser, franchise);
    }

    @Override
    public HkUserWorkHistoryEntity
            retrieveShiftForUserFromUserWorkHistory(Long userId) {
        Search search = SearchFactory.getSearch(HkUserWorkHistoryEntity.class
        );
        search.addFilterEqual(HkUserWorkHistoryField.USER_ID, userId);
        return (HkUserWorkHistoryEntity) commonDao.searchUnique(search);
    }

    @Override
    public List<HkLeaveApprovalEntity> retrieveLeavesForApprover(List<String> statusList, Long userId, Long deptId) {
        if (userId != null || deptId != null) {
            Search search = new Search(HkLeaveApprovalEntity.class);
            if (!CollectionUtils.isEmpty(statusList)) {
                search.addFilterIn(HkLeaveApprovalField.STATUS, statusList);
            }
            List<Long> workflowIds = null;
            List<HkWorkflowApproverEntity> workflowApproverEntitys = hkHrService.retrieveWorkFlowApprover(userId, deptId);

            if (!CollectionUtils.isEmpty(workflowApproverEntitys)) {
                workflowIds = new LinkedList<>();
                Filter[] filters = new Filter[workflowApproverEntitys.size()];
                int counter = 0;
                for (HkWorkflowApproverEntity hkWorkflowApproverEntity : workflowApproverEntitys) {
//                    workflowIds.add(hkWorkflowApproverEntity.getHkWorkflowApproverEntityPK().getWorkflow());
                    filters[counter++] = Filter.and(Filter.in(HkLeaveApprovalField.WORKFLOW, hkWorkflowApproverEntity.getHkWorkflowApproverEntityPK().getWorkflow()
                    ), Filter.equal(HkLeaveApprovalField.LEVEL, hkWorkflowApproverEntity.getLevel()));

                }
                search.addFilterOr(filters);
            } else {
                return null;
            }

            return commonDao.search(search);
        } else {
            return null;
        }
    }

    @Override
    public HkLeaveApprovalEntity retrieveHkLeaveApprovalEntityById(Long id) {
        return commonDao.find(HkLeaveApprovalEntity.class, id);
    }

    @Override
    public void updateHkLeaveApprovalEntity(HkLeaveApprovalEntity leaveApprovalEntity) {
        commonDao.save(leaveApprovalEntity);
    }
}
