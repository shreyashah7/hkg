/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.taskmanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkTaskEntity;
import com.argusoft.hkg.model.HkTaskRecipientDtlEntity;
import com.argusoft.hkg.model.HkTaskRecipientEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskDataBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDataBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDetailDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.reportbuilder.core.bean.RbFieldDataBean;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMGroupContact;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author kuldeep
 */
@Service
public class TaskTransformerBean {

    @Autowired
    HkEmployeeService employeeService;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HKCategoryService categoryService;
    @Autowired
    UMUserService userService;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    final String SEPARATOR = ":";
    final String SELF = "Self";
    private static final String CATEGORY_ID = "categoryId";
    private static final String RECEIVED_TASK_IDS = "receivedTasksIds";
    private static final String CREATED_TASK_IDS = "createdTasksIds";
    private static final String TASK_ID = "taskId";
    private static final String REPETITION_COUNT = "repetitionCount";

    /**
     * static class with TaskSorting parameters on which task sorting is needed.
     */
    public static class TaskSortingFields {

        private static final String LAST_MODIFIED_ON = "lastModifiedOn";
        private static final String TASK_LAST_MODIFIED_ON = "task.lastModifiedOn";
        private static final String ON_DATE = "onDate";
        private static final String COMPLETED_ON = "completedOn";
    }

    /**
     * To create new task
     *
     * @param taskDataBean
     * @return Id of created task
     */
    public Long createTask(TaskDataBean taskDataBean) {
        HkTaskEntity taskEntity = new HkTaskEntity();
        String assignerName = null;
        Set<Long> recipientIdSet = new HashSet<>();
        taskEntity = convertTaskDataBeanToTaskEntity(taskDataBean, taskEntity, false);
        Set<HkTaskRecipientEntity> taskRecipientEntitySet = convertTaskRecipientsToTaskRecipientEntity(taskDataBean.getTaskRecipients(), recipientIdSet, null);
        taskEntity.setHkTaskRecipientEntitySet(taskRecipientEntitySet);
        taskEntity.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
        String assignerCode = loginDataBean.getUserCode();
        if (!loginDataBean.getLastName().isEmpty()) {
            assignerName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
        } else {
            assignerName = loginDataBean.getFirstName();
        }
//        taskEntity.setLastUpdate(HkSystemConstantUtil.TaskLastUpdate.DUE);
        userManagementServiceWrapper.createLocaleForEntity(taskEntity.getTaskName(), "Task", loginDataBean.getId(), loginDataBean.getCompanyId());
        employeeService.createTask(taskEntity, recipientIdSet, assignerCode, assignerName, loginDataBean.getId());
//for custom field
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(taskEntity.getId(), taskDataBean.getTaskCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : taskDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, taskDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.TASK.toString(), loginDataBean.getCompanyId(), taskEntity.getId());
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        Long saveOrUpdate = customFieldSevice.saveOrUpdate(taskEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.TASK, loginDataBean.getCompanyId(), map);

        return taskEntity.getId();
    }

    /**
     * To retrieve taskAssignerList from which task is assigned.
     *
     * @return TaskAssigner names
     */
    public List<SelectItem> retrieveTaskAssignerList() {

        List<Long> assignerIds = employeeService.retrieveTaskAssignerIds(loginDataBean.getId(), loginDataBean.getCompanyId(), Boolean.FALSE);
        List<String> assignerIdList = new ArrayList<>();
        List<SelectItem> assignerNamesList = new ArrayList<>();
        if (assignerIds != null && !assignerIds.isEmpty()) {
            for (Long assignerId : assignerIds) {
                assignerIdList.add(assignerId + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
            }
            Map<String, String> assignerNamesMap = userManagementServiceWrapper.retrieveRecipientNames(assignerIdList);
            for (Long assignerId : assignerIds) {
                if (assignerId.equals(loginDataBean.getId())) {
                    assignerNamesList.add(new SelectItem(assignerId, SELF));
                } else {
                    assignerNamesList.add(new SelectItem(assignerId, assignerNamesMap.get(assignerId + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)));
                }
            }
        }
        return assignerNamesList;
    }

    /**
     * To update task
     *
     * @param taskDataBean
     * @return returns true if task is updated successfully otherwise false
     */
    public boolean updateTask(TaskDataBean taskDataBean) {
        try {
            HkTaskEntity taskEntity = employeeService.retrieveTaskById(taskDataBean.getId(), true, false);
            Set<Long> recipientIdSet = new HashSet<>();
            String assignerName = null;
            taskEntity = convertTaskDataBeanToTaskEntity(taskDataBean, taskEntity, true);
            Set<HkTaskRecipientEntity> taskRecipientEntitySet = convertTaskRecipientsToTaskRecipientEntity(taskDataBean.getTaskRecipients(), recipientIdSet, taskEntity.getId());
            String assignerCode = loginDataBean.getUserCode();
            if (!loginDataBean.getLastName().isEmpty()) {
                assignerName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
            } else {
                assignerName = loginDataBean.getFirstName();
            }
            userManagementServiceWrapper.createLocaleForEntity(taskEntity.getTaskName(), "Task", loginDataBean.getId(), loginDataBean.getCompanyId());
            employeeService.updateTask(taskEntity, taskRecipientEntitySet, recipientIdSet, assignerCode, assignerName, loginDataBean.getId());
            Map<Long, Map<String, Object>> val = new HashMap<>();
            val.put(taskDataBean.getId(), taskDataBean.getTaskCustom());
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(taskDataBean.getDbType())) {
                for (Map.Entry<String, String> entrySet : taskDataBean.getDbType().entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }
            Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            //Pass this map to makecustomfieldService
            List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, taskDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.TASK.toString(), loginDataBean.getCompanyId(), taskDataBean.getId());
            //After that make Map of Section and there customfield
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
            //Pass this map to customFieldSevice.saveOrUpdate method.
            Long saveOrUpdate = customFieldSevice.saveOrUpdate(taskDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.TASK, loginDataBean.getCompanyId(), map);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * To cancel repeated task
     *
     * @param repetitionDetailMap It contains taskId, current repetitionCount
     */
    public void cancelRepeatedTask(Map<String, String> repetitionDetailMap) {
        LOGGER.debug("----------------" + repetitionDetailMap);
        if (repetitionDetailMap != null && !repetitionDetailMap.isEmpty()) {
            Long taskId = null;
            String assignerName = null;
            int repetitionCount = 0;
            if (repetitionDetailMap.containsKey(TASK_ID) && repetitionDetailMap.containsKey(REPETITION_COUNT)) {
                taskId = Long.parseLong(repetitionDetailMap.get(TASK_ID));
                repetitionCount = Integer.parseInt(repetitionDetailMap.get(REPETITION_COUNT));
                String assignerCode = loginDataBean.getUserCode();
                if (!loginDataBean.getLastName().isEmpty()) {
                    assignerName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
                } else {
                    assignerName = loginDataBean.getFirstName();
                }
                LOGGER.debug("-------------task id---------" + taskId + "----repcount-------" + repetitionCount);
                employeeService.cancelRepeatedTask(taskId, repetitionCount, assignerCode, assignerName, loginDataBean.getCompanyId());
            }
        }

    }

    /**
     * To update task categories of created or recevied tasks
     *
     * @param taskIdAndCategoryMap
     */
    public void updateTaskCategories(Map<String, List<Long>> taskIdAndCategoryMap) {
        if (taskIdAndCategoryMap != null && !taskIdAndCategoryMap.isEmpty()) {
            if (taskIdAndCategoryMap.containsKey(CATEGORY_ID)) {
                Long categoryId = taskIdAndCategoryMap.get(CATEGORY_ID).get(0);
                List<Long> createdTasksIds = null;
                List<Long> receivedTasksIds = null;
                if (taskIdAndCategoryMap.containsKey(CREATED_TASK_IDS)) {
                    createdTasksIds = taskIdAndCategoryMap.get(CREATED_TASK_IDS);
                }
                if (taskIdAndCategoryMap.containsKey(RECEIVED_TASK_IDS)) {
                    receivedTasksIds = taskIdAndCategoryMap.get(RECEIVED_TASK_IDS);
                }
                employeeService.changeCategoryOfTasks(receivedTasksIds, createdTasksIds, categoryId, loginDataBean.getCompanyId());
            }
        }

    }

    /**
     * To update list of task recipient details
     *
     * @param taskRecipientDetailDataBeanList
     */
    public void updateTaskRecipientDetailList(List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList) {
        List<HkTaskRecipientDtlEntity> taskRecipientDtlEntityList = new ArrayList<>();
        if (taskRecipientDetailDataBeanList != null && !taskRecipientDetailDataBeanList.isEmpty()) {
            for (TaskRecipientDetailDataBean taskRecipientDetailDataBean : taskRecipientDetailDataBeanList) {
                HkTaskRecipientDtlEntity taskRecipientDtlEntity = new HkTaskRecipientDtlEntity();
                taskRecipientDtlEntity = convertTaskRecipientDetailDataBeanToTaskRecipientDtlEntity(taskRecipientDetailDataBean, taskRecipientDtlEntity);
                LOGGER.debug("-----------------category---" + taskRecipientDtlEntity.getCategory());
                taskRecipientDtlEntityList.add(taskRecipientDtlEntity);
            }
            employeeService.updateTaskRecipientDtlInfos(taskRecipientDtlEntityList);
        }
    }

    /**
     * To retrieve taskSugesstions for auto complete while assigning new task
     *
     * @return
     */
    public List<SelectItem> retrieveTaskSuggestions() {

        List<String> taskSuggestions = employeeService.searchTaskNames(null, loginDataBean.getId(), loginDataBean.getCompanyId());
        List<SelectItem> taskSuggestionList = new ArrayList<>();
        if (taskSuggestions != null && !taskSuggestions.isEmpty()) {
            for (String taskSuggestion : taskSuggestions) {
                SelectItem task = new SelectItem(taskSuggestion, taskSuggestion);
                taskSuggestionList.add(task);
            }
        }
        return taskSuggestionList;
    }

    /**
     * To mark edited task as attended.
     *
     * @param taskRecipientDetailId id of task recipient
     */
    public void attendTask(Long taskRecipientDetailId) {
        HkTaskRecipientDtlEntity taskRecipientDtlEntity = employeeService.retrieveTaskRecipientDtlById(taskRecipientDetailId);
        taskRecipientDtlEntity.setAttendedOn(Calendar.getInstance().getTime());
        String assigneeName = null;
        String assigneeCode = loginDataBean.getUserCode();
        if (!loginDataBean.getLastName().isEmpty()) {
            assigneeName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
        } else {
            assigneeName = loginDataBean.getFirstName();
        }
        employeeService.updateTaskRecipientDtl(taskRecipientDtlEntity, assigneeCode, assigneeName);
    }

    /**
     * To mark task as completed.
     *
     * @param taskRecipientDetailId id of task recipient
     */
    public void completeTask(Long taskRecipientDetailId) {
        HkTaskRecipientDtlEntity taskRecipientDtlEntity = employeeService.retrieveTaskRecipientDtlById(taskRecipientDetailId);
        taskRecipientDtlEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED);
        taskRecipientDtlEntity.setCompletedOn(Calendar.getInstance().getTime());
        String assigneeName = null;
        String assigneeCode = loginDataBean.getUserCode();
        if (!loginDataBean.getLastName().isEmpty()) {
            assigneeName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
        } else {
            assigneeName = loginDataBean.getFirstName();
        }
        employeeService.updateTaskRecipientDtl(taskRecipientDtlEntity, assigneeCode, assigneeName);
    }

    /**
     * To remove task(Archieves task)
     *
     * @param taskId Id of task to be removed
     */
    public void removeTask(Long taskId) {
        String assignerCode = loginDataBean.getUserCode();
        String assignerName = null;
        if (!loginDataBean.getLastName().isEmpty()) {
            assignerName = loginDataBean.getFirstName() + " " + loginDataBean.getLastName();
        } else {
            assignerName = loginDataBean.getFirstName();
        }
        employeeService.removeTask(taskId, assignerCode, assignerName, null);
        TaskDataBean taskDataBean = this.retrieveTaskById(taskId);
        userManagementServiceWrapper.deleteLocaleForEntity(taskDataBean.getTaskName(), "Task", "CONTENT", loginDataBean.getId(), loginDataBean.getCompanyId());
    }

    /**
     * To retrieve task categories
     *
     * @return category with category name,category id and category count
     */
    public List<SelectItem> retrieveTaskCategories() {
        List<HkCategoryEntity> categoryEntityList = categoryService.retrieveAllCategories(loginDataBean.getCompanyId(), CategoryType.TASK);
        List<SelectItem> taskCategoryList = new ArrayList<>();
        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(null, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY, loginDataBean.getCompanyId());
        if (categoryEntityList != null && !categoryEntityList.isEmpty()) {
            Map<Long, Integer> categoryCountMap = employeeService.retrievePendingTaskCountGroupedByCategory(loginDataBean.getId(), loginDataBean.getCompanyId(), Boolean.FALSE);
            for (HkCategoryEntity categoryEntity : categoryEntityList) {
                String categoryCount = "0";
                if (categoryCountMap.get(categoryEntity.getId()) != null) {
                    categoryCount = categoryCountMap.get(categoryEntity.getId()).toString();
                }
                SelectItem selectItem = new SelectItem(categoryEntity.getId(), categoryEntity.getCategoryTitle(), categoryCount);
                if (retrieveDocumentByInstanceIds != null) {
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionWiseMap = retrieveDocumentByInstanceIds.get(categoryEntity.getId());
                    if (sectionWiseMap != null) {
                        List<Map<Long, Map<String, Object>>> maps = sectionWiseMap.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                        if (maps != null) {
                            for (Map<Long, Map<String, Object>> map : maps) {
                                selectItem.setCategoryCustom(map.get(categoryEntity.getId()));
                            }
                        }
                    }
                }
                if (selectItem.getCategoryCustom() == null) {
                    selectItem.setCategoryCustom(new HashMap<String, Object>());
                }
                taskCategoryList.add(selectItem);
            }
        }

        return taskCategoryList;
    }

    /**
     * To retrieve task by id for edit task
     *
     * @param taskId
     * @return TaskDataBean with TaskRecipientDtlDataBean and
     * TaskReciientDataBean
     */
    public TaskDataBean retrieveTaskById(Long taskId) {
        HkTaskEntity taskEntity = employeeService.retrieveTaskById(taskId, true, true);
        List<HkTaskEntity> taskEntityList = new ArrayList<>();
        taskEntityList.add(taskEntity);
        TaskDataBean taskDataBean = convertTaskEntityListToTaskDataBeanList(taskEntityList).get(0);
        Set<HkTaskRecipientEntity> taskRecipientEntitySet = taskEntity.getHkTaskRecipientEntitySet();
        List<TaskRecipientDataBean> taskRecipientDataBeanList = new ArrayList<>();
        if (taskRecipientEntitySet != null && !taskRecipientEntitySet.isEmpty()) {
            List<String> recipientCodes = new ArrayList<>();
            for (HkTaskRecipientEntity hkTaskRecipientEntity : taskRecipientEntitySet) {
                recipientCodes.add(hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceInstance() + SEPARATOR + hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceType());
            }
            Map<String, String> recipientNames = userManagementServiceWrapper.retrieveRecipientNames(recipientCodes);

            for (HkTaskRecipientEntity hkTaskRecipientEntity : taskRecipientEntitySet) {
                TaskRecipientDataBean taskRecipientDataBean = new TaskRecipientDataBean();
                taskRecipientDataBean = convertTaskRecipientEntityToTaskRecipientDataBean(hkTaskRecipientEntity, taskRecipientDataBean, recipientNames, SEPARATOR);
                taskRecipientDataBeanList.add(taskRecipientDataBean);
            }
        }
        taskDataBean.setTaskRecipientDataBeanList(taskRecipientDataBeanList);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(taskId, HkSystemConstantUtil.FeatureNameForCustomField.TASK, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            if (retrieveDocumentByInstanceId != null) {
                List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        taskDataBean.setTaskCustom(map.get(taskId));
                    }
                }
            }
        }
        if (taskDataBean.getTaskCustom() == null) {
            taskDataBean.setTaskCustom(new HashMap<String, Object>());
        }
        return taskDataBean;
    }

    //Tasks for user
    /**
     * To retrieve recent task assigned to logged in user
     *
     * @return
     */
    public List<TaskDataBean> retrieveRecentTasksForUser() {
        int recentDays = -HkSystemConstantUtil.RECENT_DAYS;
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, recentDays);
        Date nDaysAgo = cal.getTime();
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = new LinkedList<>();
        taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, nDaysAgo, null, loginDataBean.getId(), null, Arrays.asList(HkSystemConstantUtil.TaskStatus.DUE), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, true);
        List<TaskDataBean> taskDataBeans = convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
        if (!CollectionUtils.isEmpty(taskDataBeans)) {
            Collections.sort(taskDataBeans, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    TaskDataBean oldfield = (TaskDataBean) o1;
                    TaskDataBean newfield = (TaskDataBean) o2;
                    if (newfield.getTaskRecipientDetailDataBeanList() == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList() == null) ? 0 : -1;
                    } else if (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) ? 0 : -1;
                    }
                    if (oldfield.getTaskRecipientDetailDataBeanList() == null) {
                        return 1;
                    } else if (oldfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return 1;
                    }
                    return newfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate().compareTo(oldfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate());
                }
            });
        }
        return taskDataBeans;
    }

    /**
     * To retrieve all task that are assigned to logged in user
     *
     * @return
     */
    public List<TaskDataBean> retrieveAllTasksForUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = new LinkedList<>();
        taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, null, null, loginDataBean.getId(), null, null, null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, true);
        List<TaskDataBean> taskDataBeans = convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
        if (!CollectionUtils.isEmpty(taskDataBeans)) {
            Collections.sort(taskDataBeans, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    TaskDataBean oldfield = (TaskDataBean) o1;
                    TaskDataBean newfield = (TaskDataBean) o2;
                    if (newfield.getTaskRecipientDetailDataBeanList() == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList() == null) ? 0 : -1;
                    } else if (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) ? 0 : -1;
                    }
                    if (oldfield.getTaskRecipientDetailDataBeanList() == null) {
                        return 1;
                    } else if (oldfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return 1;
                    }
                    return newfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate().compareTo(oldfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate());
                }
            });
        }
        return taskDataBeans;
    }

    /**
     * To retrieve due today task that are assigned to logged in user
     *
     * @return
     */
    public List<TaskDataBean> retrieveDueTodayTasksForUser() {
        //Sorted by asc ondate Oldest assigned first
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = new LinkedList<>();
        taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(HkSystemFunctionUtil.getDateWithoutTimeStamp(new Date()), null, null, loginDataBean.getId(), null, Arrays.asList(HkSystemConstantUtil.TaskStatus.DUE), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, false);
        return convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
    }

    /**
     * To retrieve completed that are assigned to logged in user
     *
     * @return
     */
    public List<TaskDataBean> retrieveCompletedTasksForUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = new LinkedList<>();
        taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, null, null, loginDataBean.getId(), null, Arrays.asList(HkSystemConstantUtil.TaskStatus.COMPLETED), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.COMPLETED_ON, true);
        return convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
    }

    /**
     * To retrieve received tasks that are assigned to logged in user
     *
     * @return
     */
    public List<TaskDataBean> retrieveReceivedTasksForUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = new LinkedList<>();
        taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, null, null, loginDataBean.getId(), null, Arrays.asList(HkSystemConstantUtil.TaskStatus.DUE), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, true);
        List<TaskDataBean> taskDataBeans = convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
        if (!CollectionUtils.isEmpty(taskDataBeans)) {
            Collections.sort(taskDataBeans, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    TaskDataBean oldfield = (TaskDataBean) o1;
                    TaskDataBean newfield = (TaskDataBean) o2;
                    if (newfield.getTaskRecipientDetailDataBeanList() == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList() == null) ? 0 : -1;
                    } else if (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return (newfield.getTaskRecipientDetailDataBeanList().get(0) == null) ? 0 : -1;
                    }
                    if (oldfield.getTaskRecipientDetailDataBeanList() == null) {
                        return 1;
                    } else if (oldfield.getTaskRecipientDetailDataBeanList().get(0) == null) {
                        return 1;
                    }
                    return newfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate().compareTo(oldfield.getTaskRecipientDetailDataBeanList().get(0).getOnDate());
                }
            });
        }
        return taskDataBeans;
    }

    /**
     * To retrieve tasks that are assigned to logged in user by particular
     * assigner.
     *
     * @param assignerId
     * @return
     */
    public List<TaskDataBean> retrieveTasksForUserByAssigner(Long assignerId) {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, null, null, loginDataBean.getId(), assignerId, Arrays.asList(HkSystemConstantUtil.TaskStatus.DUE), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, true);
        return convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
    }

    /**
     * To retrieve tasks that are assigned to logged in user by category id of
     * task.
     *
     * @param categoryId
     * @return
     */
    public List<TaskDataBean> retrieveTasksForUserBycategory(Long categoryId) {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskRecipientDtlEntity> taskRecipientDtlList = employeeService.retrieveTaskRecipientDtlsByCriteria(null, null, null, loginDataBean.getId(), null, null, categoryId, null, loginDataBean.getCompanyId(), Boolean.FALSE, TaskSortingFields.TASK_LAST_MODIFIED_ON, true);
        return convertTaskRecipientDtlEntityListToTaskDataBeanList(taskRecipientDtlList);
    }

    //Tasks assigned by user
    /**
     * To retrieve all tasks assigned by logged in user to another users.
     *
     * @return
     */
    public List<TaskDataBean> retrieveAllTasksByUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(null, null, null, loginDataBean.getId(), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, true);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve recent tasks assigned by logged in user to another users.
     *
     * @return
     */
    public List<TaskDataBean> retrieveRecentTasksByUser() {
        int recentDays = -HkSystemConstantUtil.RECENT_DAYS;
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, recentDays);
        Date nDaysAgo = cal.getTime();
        //Sorted by desc ondate(Most recent first)
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(null, nDaysAgo, null, loginDataBean.getId(), HkSystemConstantUtil.TaskStatus.DUE, null, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, true);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve due today tasks assigned by logged in user to another users.
     *
     * @return
     */
    public List<TaskDataBean> retrieveDueTodayTasksByUser() {
        //Sorted by asc ondate Oldest assigned first
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(HkSystemFunctionUtil.getDateWithoutTimeStamp(new Date()), null, null, loginDataBean.getId(), HkSystemConstantUtil.TaskStatus.DUE, null, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, false);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve completed tasks assigned by logged in user to another users.
     *
     * @return
     */
    public List<TaskDataBean> retrieveCompletedTasksAssignedByUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(null, null, null, loginDataBean.getId(), HkSystemConstantUtil.TaskStatus.COMPLETED, null, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, true);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve tasks assigned by logged in user to another users.
     *
     * @return
     */
    public List<TaskDataBean> retrieveTasksAssignedByUser() {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(null, null, null, loginDataBean.getId(), null, null, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, true);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve tasks assigned by logged in user to another users by
     * category.
     *
     * @param categoryId
     * @return
     */
    public List<TaskDataBean> retrieveTasksByUserByCategory(Long categoryId) {
        //Sorted by desc ondate(Most recent first)
        List<HkTaskEntity> taskEntityList = employeeService.retrieveTasksByCriteria(null, null, null, loginDataBean.getId(), null, categoryId, loginDataBean.getCompanyId(), Boolean.FALSE, true, TaskSortingFields.LAST_MODIFIED_ON, true);
        return convertTaskEntityListToTaskDataBeanList(taskEntityList);
    }

    /**
     * To retrieve task recipient list by taskid
     *
     * @param taskId
     * @return
     */
    public List<TaskRecipientDataBean> retrieveTaskRecipientListByTaskId(Long taskId) {
        List<HkTaskRecipientEntity> taskRecipientEntityList = employeeService.retrieveTaskRecipientsByTaskId(taskId, Boolean.FALSE);
        List<TaskRecipientDataBean> taskRecipientDataBeanList = new ArrayList<>();
        if (taskRecipientEntityList != null && !taskRecipientEntityList.isEmpty()) {

            List<String> recipientCodes = new ArrayList<>();
            for (HkTaskRecipientEntity hkTaskRecipientEntity : taskRecipientEntityList) {
                recipientCodes.add(hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceInstance() + SEPARATOR + hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceType());
            }
            Map<String, String> recipientNames = userManagementServiceWrapper.retrieveRecipientNames(recipientCodes);

            for (HkTaskRecipientEntity hkTaskRecipientEntity : taskRecipientEntityList) {
                TaskRecipientDataBean taskRecipientDataBean = new TaskRecipientDataBean();
                taskRecipientDataBean = convertTaskRecipientEntityToTaskRecipientDataBean(hkTaskRecipientEntity, taskRecipientDataBean, recipientNames, SEPARATOR);
                taskRecipientDataBeanList.add(taskRecipientDataBean);
            }
        }
        return taskRecipientDataBeanList;
    }

    /**
     * To convert TaskRecipientDtlEntity to TaskDataBeanList. Used for
     * retrieving task for logged in user.
     *
     * @param taskRecipientDtlEntityList List of taskRecipientDtlEntity
     * @return TaskDataBean with TaskRecipientDtlDataBean
     */
    public List<TaskDataBean> convertTaskRecipientDtlEntityListToTaskDataBeanList(List<HkTaskRecipientDtlEntity> taskRecipientDtlEntityList) {
        List<TaskDataBean> taskDataBeanList = new LinkedList<>();
        if (taskRecipientDtlEntityList != null && !taskRecipientDtlEntityList.isEmpty()) {
            List<String> assignedByIdList = new ArrayList<>();
            Long userId = null;
            for (HkTaskRecipientDtlEntity taskRecipientDtlEntity : taskRecipientDtlEntityList) {
                if (!taskRecipientDtlEntity.getIsArchive()) {
                    userId = taskRecipientDtlEntity.getTask().getCreatedBy();
                    assignedByIdList.add(taskRecipientDtlEntity.getTask().getCreatedBy() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                }

            }
            UMUser user = userService.retrieveUserById(userId, loginDataBean.getCompanyId());
            Map<String, String> assignedByNamesMap = userManagementServiceWrapper.retrieveRecipientNames(assignedByIdList);
            for (HkTaskRecipientDtlEntity taskRecipientDtlEntity : taskRecipientDtlEntityList) {
                if (!taskRecipientDtlEntity.getIsArchive()) {
                    TaskDataBean taskDataBean = new TaskDataBean();
                    taskDataBean = converTaskEntityToTaskDataBean(taskRecipientDtlEntity.getTask(), taskDataBean, assignedByNamesMap);
                    if (!StringUtils.isEmpty(taskDataBean.getAssignedBy())) {
                        String[] split = (taskDataBean.getAssignedBy()).split(SEPARATOR);
                        String replace = split[0].replace(split[0], user.getUserCode());
//                        taskDataBean.setAssignedBy(assignedByNamesMap.get(userId + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
                    }
                    List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList = new ArrayList();
                    TaskRecipientDetailDataBean taskRecipientDetailDataBean = new TaskRecipientDetailDataBean();
                    taskRecipientDetailDataBean = convertTaskRecipeintDtlEntityToTaskRecipientDetailDataBean(taskRecipientDtlEntity, taskRecipientDetailDataBean, null);
//                    if ((!(taskRecipientDtlEntity.getTask().getCreatedOn().equals(taskRecipientDtlEntity.getTask().getLastModifiedOn()))) && (taskRecipientDtlEntity.getAttendedOn() == null || taskRecipientDtlEntity.getAttendedOn().compareTo(taskRecipientDtlEntity.getTask().getLastModifiedOn()) < 0)) {
//                        taskRecipientDetailDataBean.setTaskEdited(true);
//                    }
                    
                    //In all the condition for creation as well as updation if user has not attended then it should come bold.
                    if ((taskRecipientDtlEntity.getAttendedOn() == null || taskRecipientDtlEntity.getAttendedOn().compareTo(taskRecipientDtlEntity.getTask().getLastModifiedOn()) < 0)) {
                        taskRecipientDetailDataBean.setTaskEdited(true);
                    }
                    taskRecipientDetailDataBean.setUpdatedOn(taskRecipientDtlEntity.getTask().getLastModifiedOn());
                    taskRecipientDetailDataBeanList.add(taskRecipientDetailDataBean);
                    if (!StringUtils.isEmpty(taskDataBean.getRecipientNames())) {
                        String recipients = taskDataBean.getRecipientNames();
                        recipients = recipients + "," + taskRecipientDetailDataBean.getUserName();
                        taskDataBean.setRecipientNames(recipients);
                    } else {
                        taskDataBean.setRecipientNames(taskRecipientDetailDataBean.getUserName());
                    }
                    taskDataBean.setTaskRecipientDetailDataBeanList(taskRecipientDetailDataBeanList);
                    taskDataBeanList.add(taskDataBean);
                }

            }
        }
        return taskDataBeanList;
    }

    /**
     * To remove task from user's list which is completed or cancelled.
     *
     * @param taskRecipientDetailId
     */
    public boolean removeTaskOfUserFromList(Long taskRecipientDetailId) {
        HkTaskRecipientDtlEntity taskRecipientDtlEntity = employeeService.retrieveTaskRecipientDtlById(taskRecipientDetailId);
        if (taskRecipientDtlEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.CANCELLED)) {
            taskRecipientDtlEntity.setStatus(HkSystemConstantUtil.TaskStatus.CANCELLED_ARCHIVED);
            employeeService.updateTaskRecipientDtl(taskRecipientDtlEntity, null, null);
            return true;
        } else if (taskRecipientDtlEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.COMPLETED)) {
            taskRecipientDtlEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED);
            employeeService.updateTaskRecipientDtl(taskRecipientDtlEntity, null, null);
            return true;
        }
        return false;
    }

    /**
     * to convert TaskEntityList to TaskDataBean. Used for retrieving task
     * assigned by logged in user to another user
     *
     * @param taskEntityList List of taskEntity
     * @return List of taskDataBean with TaskRecipientDtlDataBean
     */
    public List<TaskDataBean> convertTaskEntityListToTaskDataBeanList(List<HkTaskEntity> taskEntityList) {
        List<TaskDataBean> taskDataBeanList = new ArrayList<>();
        if (taskEntityList != null && !taskEntityList.isEmpty()) {
            //List of user names
            List<String> userNamesList = new ArrayList<>();
            for (HkTaskEntity taskEntity : taskEntityList) {
                userNamesList.add(taskEntity.getCreatedBy() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                Set<HkTaskRecipientDtlEntity> taskRecipientDtlEntitys = taskEntity.getHkTaskRecipientDtlEntitySet();
                if (taskRecipientDtlEntitys != null && !taskRecipientDtlEntitys.isEmpty()) {
                    for (HkTaskRecipientDtlEntity taskRecipientDtlEntity : taskRecipientDtlEntitys) {
                        if (!taskRecipientDtlEntity.getIsArchive()) {
                            userNamesList.add(taskRecipientDtlEntity.getUserId() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                        }
                    }
                }
            }
            Map<String, String> userNamesMap = userManagementServiceWrapper.retrieveRecipientNames(userNamesList);
            for (HkTaskEntity taskEntity : taskEntityList) {
                TaskDataBean taskDataBean = new TaskDataBean();
                Map<Integer, List<TaskRecipientDetailDataBean>> taskRepititionMap = new HashMap<>();

                taskDataBean = converTaskEntityToTaskDataBean(taskEntity, taskDataBean, userNamesMap);

                //Set task recipients
                Set<HkTaskRecipientDtlEntity> taskRecipientDtlEntitys = taskEntity.getHkTaskRecipientDtlEntitySet();
                List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList = new ArrayList();
                if (taskRecipientDtlEntitys != null && !taskRecipientDtlEntitys.isEmpty()) {
                    for (HkTaskRecipientDtlEntity hkTaskRecipientDtlEntity : taskRecipientDtlEntitys) {
                        if (!hkTaskRecipientDtlEntity.getIsArchive()) {
                            TaskRecipientDetailDataBean taskRecipientDetailDataBean = new TaskRecipientDetailDataBean();
                            taskRecipientDetailDataBean = convertTaskRecipeintDtlEntityToTaskRecipientDetailDataBean(hkTaskRecipientDtlEntity, taskRecipientDetailDataBean, userNamesMap);
                            taskRecipientDetailDataBean.setUpdatedOn(taskEntity.getLastModifiedOn());
                            taskRecipientDetailDataBeanList.add(taskRecipientDetailDataBean);
//                        if (!hkTaskRecipientDtlEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.CANCELLED)) {
                            if (taskRepititionMap.containsKey(taskRecipientDetailDataBean.getRepetitionCount())) {
                                taskRepititionMap.get(taskRecipientDetailDataBean.getRepetitionCount()).add(taskRecipientDetailDataBean);
                            } else {
                                List<TaskRecipientDetailDataBean> repititionTaskRecipientDetailDataBeanList = new ArrayList<>();
                                repititionTaskRecipientDetailDataBeanList.add(taskRecipientDetailDataBean);
                                taskRepititionMap.put(taskRecipientDetailDataBean.getRepetitionCount(), repititionTaskRecipientDetailDataBeanList);
                            }
                            if (!StringUtils.isEmpty(taskDataBean.getRecipientNames())) {
                                String recipients = taskDataBean.getRecipientNames();
                                recipients = recipients + "," + taskRecipientDetailDataBean.getUserName();
                                taskDataBean.setRecipientNames(recipients);
                            } else {
                                taskDataBean.setRecipientNames(taskRecipientDetailDataBean.getUserName());
                            }
                        }

//                        }
                    }
                }
                taskDataBean.setTaskRecipientDetailDataBeanList(taskRecipientDetailDataBeanList);
                taskDataBean.setRepititionTaskRecipeintDetailMap(taskRepititionMap);
                taskDataBeanList.add(taskDataBean);
            }
        }
        return taskDataBeanList;
    }

    /**
     * This method is used for autocomplete recipients suggestion of userid's
     * while assigning new task.
     *
     * @param user searchString
     * @param isActive isActive flag
     * @return List of users
     */
    public List<UMUser> retrieveUsersByStatus(String user, Boolean isActive) {
        List<UMUser> users = userManagementServiceWrapper.retrieveUsersByCompanyByStatus(loginDataBean.getCompanyId(), user, isActive, true, loginDataBean.getId());
        return users;
    }

    /**
     * To convert taskDataBeanToTaskEntity while creating of editing of task
     *
     * @param taskDataBean TaskDataBean to be converted
     * @param taskEntity TaskEntity to be returned
     * @param isEditTask edit task flag. If not edit task then set
     * createdBy,createdOn etc..
     * @return
     */
    public HkTaskEntity convertTaskDataBeanToTaskEntity(TaskDataBean taskDataBean, HkTaskEntity taskEntity, boolean isEditTask) {

        if (!isEditTask) {
            taskEntity.setCreatedBy(loginDataBean.getId());
            taskEntity.setCreatedOn(Calendar.getInstance().getTime());
            taskEntity.setFranchise(loginDataBean.getCompanyId());
            taskEntity.setIsArchive(false);
        }
        taskEntity.setLastModifiedOn(Calendar.getInstance().getTime());
        taskEntity.setTaskName(taskDataBean.getTaskName());
        if (taskDataBean.getTaskCategory() != null) {
            taskEntity.setTaskCategory(taskDataBean.getTaskCategory());
        } else {
            taskEntity.setTaskCategory(0);
        }
        taskEntity.setDueDt(HkSystemFunctionUtil.convertToServerDate(taskDataBean.getDueDate(), loginDataBean.getClientRawOffsetInMin()));
        taskEntity.setIsRepetative(taskDataBean.isRepeatTask());
        if (taskEntity.getIsRepetative()) {
            taskEntity.setRepeatativeMode(taskDataBean.getRepeatativeMode());
            if (taskEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
                taskEntity.setWeeklyOnDays(taskDataBean.getWeeklyOnDays());
                taskEntity.setMonthlyOnDay(null);

            } else if (taskEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
                taskEntity.setMonthlyOnDay(taskDataBean.getMonthlyOnDay());
                taskEntity.setWeeklyOnDays(null);
            } else {
                taskEntity.setMonthlyOnDay(null);
                taskEntity.setWeeklyOnDays(null);
            }
            taskEntity.setEndRepeatMode(taskDataBean.getEndRepeatMode());
            if (taskEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)) {
                taskEntity.setEndDate(HkSystemFunctionUtil.convertToServerDate(taskDataBean.getEndDate(), loginDataBean.getClientRawOffsetInMin()));
                taskEntity.setAfterUnits(null);
            } else {
                taskEntity.setAfterUnits(taskDataBean.getAfterUnits());
                taskEntity.setEndDate(null);
            }
        } else {
            //Set all values to null for updating object
            taskEntity.setRepeatativeMode(null);
            taskEntity.setWeeklyOnDays(null);
            taskEntity.setMonthlyOnDay(null);
            taskEntity.setEndRepeatMode(null);
            taskEntity.setEndDate(null);
            taskEntity.setAfterUnits(null);

        }
        taskEntity.setStatus(HkSystemFunctionUtil.getKeyByValue(HkSystemConstantUtil.TASK_STATUS_MAP, taskDataBean.getStatus()));
//        taskEntity.setLastUpdate(HkSystemFunctionUtil.getKeyByValue(HkSystemConstantUtil.TASK_LAST_UPDATE_MAP, taskDataBean.getLastUpdate()));
        return taskEntity;
    }

    /**
     * To convert taskRecipients to TaskRecipientEntity. Used while creating or
     * editing of task
     *
     * @param taskRecipients TaskRecipients in form of "1:E","2:D" etc.
     * @param recipientIdSet Set of recipientId that is to be filled for
     * creation of task
     * @param taskId id of task. Pass null in case of creation of task
     * @return Set of taskRecipientEntity for particular task.
     */
    public Set<HkTaskRecipientEntity> convertTaskRecipientsToTaskRecipientEntity(String[] taskRecipients, Set recipientIdSet, Long taskId) {
        Set<HkTaskRecipientEntity> taskRecipientSet = new HashSet<>();
        if (taskRecipients != null && !taskRecipients.equals("")) {
            for (String recipient : taskRecipients) {
                String[] recipientDetail = recipient.split(":");
                Long recipientId = new Long(recipientDetail[0]);
                String recipientType = recipientDetail[1];
                HkTaskRecipientEntity taskRecipient = null;
                if (taskId != null) {
                    taskRecipient = new HkTaskRecipientEntity(taskId, recipientType, recipientId);
                } else {
                    taskRecipient = new HkTaskRecipientEntity(0l, recipientType, recipientId);
                }

                if (recipientType.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
                    recipientIdSet.add(recipientId);
                } else if (recipientType.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
                    List<UMUser> usersByDepartmentId = userManagementServiceWrapper.retrieveUsersByCompanyByDepartment(loginDataBean.getCompanyId(), recipientId, true, loginDataBean.getId());
                    if (!CollectionUtils.isEmpty(usersByDepartmentId)) {
                        for (UMUser uMUser : usersByDepartmentId) {
                            recipientIdSet.add(uMUser.getId());
                        }
                    }
                } else if (recipientType.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.GROUP)) {
                    List<UMGroupContact> groupContacts = userManagementServiceWrapper.retrieveGroupContacts(recipientId, true);
                    if (!CollectionUtils.isEmpty(groupContacts)) {
                        for (UMGroupContact groupContact : groupContacts) {
                            recipientIdSet.add(groupContact.getUMUserContact().getUserobj().getId());
                        }
                    }
                } else if (recipientType.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.ACTIVITY)) {
                } else if (recipientType.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
                    List<UMUserRole> retrieveUserRoleByRoleId = userManagementServiceWrapper.retrieveUserRolesByRoleId(recipientId, true);
                    if (!CollectionUtils.isEmpty(retrieveUserRoleByRoleId)) {
                        for (UMUserRole uMUserRole : retrieveUserRoleByRoleId) {
                            recipientIdSet.add(uMUserRole.getuMUser().getId());
                        }
                    }
                }

                taskRecipient.setIsArchive(false);
                taskRecipientSet.add(taskRecipient);
            }
        }
        return taskRecipientSet;
    }

    /**
     * To convert TaskRecipeintDetailDataBean to TaskRecipientDtlEntity. Used
     * for creating and editing of task
     *
     * @param taskRecipientDetailDataBean TaskRecipeintDetailDataBean to be
     * converted
     * @param taskRecipientDtlEntity TaskRecipientDtlEntity to be returned
     * @return
     */
    public HkTaskRecipientDtlEntity convertTaskRecipientDetailDataBeanToTaskRecipientDtlEntity(TaskRecipientDetailDataBean taskRecipientDetailDataBean, HkTaskRecipientDtlEntity taskRecipientDtlEntity) {
        taskRecipientDtlEntity.setAttendedOn(taskRecipientDetailDataBean.getAttendedOn());
        taskRecipientDtlEntity.setCompletedOn(taskRecipientDetailDataBean.getCompletedOn());
        taskRecipientDtlEntity.setDueDate(HkSystemFunctionUtil.convertToServerDate(taskRecipientDetailDataBean.getDueDate(), loginDataBean.getClientRawOffsetInMin()));
//        taskRecipientDtlEntity.setOnDate(taskRecipientDetailDataBean.getOnDate());
        taskRecipientDtlEntity.setStatus(taskRecipientDetailDataBean.getStatus());
        return taskRecipientDtlEntity;
    }

    /**
     * To convert TaskEntity to TaskDataBean while retrieving of tasks.
     *
     * @param taskEntity TaskEntity to be converted.
     * @param taskDataBean TaskDataBean to be returned.
     * @param assignedByNamesMap Map of task assigner names.
     * @return
     */
    public TaskDataBean converTaskEntityToTaskDataBean(HkTaskEntity taskEntity, TaskDataBean taskDataBean, Map<String, String> assignedByNamesMap) {
        taskDataBean.setId(taskEntity.getId());
        taskDataBean.setAfterUnits(taskEntity.getAfterUnits());
        taskDataBean.setAssignedById(taskEntity.getCreatedBy());
        taskDataBean.setAssignedBy(assignedByNamesMap.get(taskEntity.getCreatedBy() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
        taskDataBean.setDueDate(HkSystemFunctionUtil.convertToClientDate(taskEntity.getDueDt(), loginDataBean.getClientRawOffsetInMin()));
        taskDataBean.setEndDate(HkSystemFunctionUtil.convertToClientDate(taskEntity.getEndDate(), loginDataBean.getClientRawOffsetInMin()));
        taskDataBean.setEndRepeatMode(taskEntity.getEndRepeatMode());
        taskDataBean.setMonthlyOnDay(taskEntity.getMonthlyOnDay());
        taskDataBean.setRepeatTask(taskEntity.getIsRepetative());
        taskDataBean.setRepeatativeMode(taskEntity.getRepeatativeMode());
        taskDataBean.setStatus(HkSystemConstantUtil.TASK_STATUS_MAP.get(taskEntity.getStatus()));
        taskDataBean.setTaskCategory(taskEntity.getTaskCategory());
        taskDataBean.setTaskName(taskEntity.getTaskName());
        taskDataBean.setWeeklyOnDays(taskEntity.getWeeklyOnDays());
        taskDataBean.setRepetitionCount(taskEntity.getRepetitionCount());
        return taskDataBean;
    }

    /**
     * To convert TaskRecipientEntity to TaskRecipientDataBean while retrieval
     * of task
     *
     * @param taskRecipientEntity TaskRecipientEntity to be converted
     * @param taskRecipientDataBean TaskRecipientDataBean to be returned
     * @param recipientNames Map of task recipient names.
     * @param SEPERATOR Seperator used in recipient name map
     * @return
     */
    public TaskRecipientDataBean convertTaskRecipientEntityToTaskRecipientDataBean(HkTaskRecipientEntity taskRecipientEntity, TaskRecipientDataBean taskRecipientDataBean, Map<String, String> recipientNames, String SEPERATOR) {
        taskRecipientDataBean.setRecipientInstance(taskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceInstance());
        taskRecipientDataBean.setRecipientType(taskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceType());
        taskRecipientDataBean.setRecipientValue(recipientNames.get(taskRecipientDataBean.getRecipientInstance() + SEPERATOR + taskRecipientDataBean.getRecipientType()));
        return taskRecipientDataBean;
    }

    /**
     * To Convert TaskRecipientDtlEntity to TaskRecipientDetailDataBean used for
     * retrieval of task
     *
     * @param taskRecipientDtlEntity HkTaskRecipientDtlEntity to be converted
     * @param taskRecipientDetailDataBean TaskRecipientDetailDataBean to be
     * retrieved.
     * @param userNamesMap Map of task recipient names.
     * @return
     */
    public TaskRecipientDetailDataBean convertTaskRecipeintDtlEntityToTaskRecipientDetailDataBean(HkTaskRecipientDtlEntity taskRecipientDtlEntity, TaskRecipientDetailDataBean taskRecipientDetailDataBean, Map<String, String> userNamesMap) {
        taskRecipientDetailDataBean.setId(taskRecipientDtlEntity.getId());
        taskRecipientDetailDataBean.setAttendedOn(taskRecipientDtlEntity.getAttendedOn());
        taskRecipientDetailDataBean.setCompletedOn(taskRecipientDtlEntity.getCompletedOn());
        taskRecipientDetailDataBean.setDueDate(HkSystemFunctionUtil.convertToClientDate(taskRecipientDtlEntity.getDueDate(), loginDataBean.getClientRawOffsetInMin()));
        taskRecipientDetailDataBean.setOnDate(HkSystemFunctionUtil.convertToClientDate(taskRecipientDtlEntity.getOnDate(), loginDataBean.getClientRawOffsetInMin()));
        taskRecipientDetailDataBean.setStatus(HkSystemConstantUtil.TASK_STATUS_MAP.get(taskRecipientDtlEntity.getStatus()));
        taskRecipientDetailDataBean.setTask(taskRecipientDtlEntity.getTask().getId());
        taskRecipientDetailDataBean.setUserId(taskRecipientDtlEntity.getUserId());
        taskRecipientDetailDataBean.setRepetitionCount(taskRecipientDtlEntity.getRepetitionCount());
//        Commented by kvithlani
//        IF login user's category needed then do this
//        if (loginDataBean.getId().longValue() == taskRecipientDtlEntity.getUserId()) {
//            taskRecipientDetailDataBean.setRecipientCategory(taskRecipientDtlEntity.getTask().getTaskCategory());
//        } else {
        taskRecipientDetailDataBean.setRecipientCategory(taskRecipientDtlEntity.getCategory());
//        }

        if (userNamesMap != null && !userNamesMap.isEmpty()) {
            taskRecipientDetailDataBean.setUserName(userNamesMap.get(taskRecipientDtlEntity.getUserId() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
        }
        return taskRecipientDetailDataBean;

    }

    public List<TaskDataBean> searchTask(String searchString) {
        Long companyId = loginDataBean.getCompanyId();
        Long id = loginDataBean.getId();
        searchString = searchString.trim();
        List<Long> ids = null;
        String referenceType = null;
        List<Long> searchUsersByName = null;
        String searchText = searchString.substring(2).trim();
        if (searchString.toUpperCase().startsWith('@' + HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
            referenceType = HkSystemConstantUtil.RecipientCodeType.DEPARTMENT;
            ids = new ArrayList<>();
            List<UMDepartment> searchDepartmentByName = userManagementServiceWrapper.searchDepartmentByName(searchText, companyId);
            if (!CollectionUtils.isEmpty(searchDepartmentByName)) {
                for (UMDepartment uMDepartment : searchDepartmentByName) {
                    ids.add(uMDepartment.getId());
                }
            }
        } else if (searchString.toUpperCase().startsWith('@' + HkSystemConstantUtil.RecipientCodeType.ACTIVITY)) {
            referenceType = HkSystemConstantUtil.RecipientCodeType.ACTIVITY;
            ids = new ArrayList<>();
//            Map<Long, String> searchActivities = userManagementServiceWrapper.searchUsers(searchText, companyId);
//            if (!CollectionUtils.isEmpty(searchActivities)) {
//                for (Map.Entry<Long, String> entry : searchActivities.entrySet()) {
//                    ids.add(entry.getKey());
//                }
//            }
        } else if (searchString.toUpperCase().startsWith('@' + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
            referenceType = HkSystemConstantUtil.RecipientCodeType.EMPLOYEE;
            ids = userManagementServiceWrapper.searchUsersByName(searchText, companyId);
        } else if (searchString.toUpperCase().startsWith('@' + HkSystemConstantUtil.RecipientCodeType.GROUP)) {
            referenceType = HkSystemConstantUtil.RecipientCodeType.GROUP;
            ids = new ArrayList<>();
//            Map<Long, String> searchUsers = userManagementServiceWrapper.searchUsers(searchText, companyId);
//            if (!CollectionUtils.isEmpty(searchUsers)) {
//                for (Map.Entry<Long, String> entry : searchUsers.entrySet()) {
//                    ids.add(entry.getKey());
//                }
//            }
        } else if (searchString.toUpperCase().startsWith('@' + HkSystemConstantUtil.RecipientCodeType.DESIGNATION)) {
            referenceType = HkSystemConstantUtil.RecipientCodeType.DESIGNATION;
            ids = new ArrayList<>();
            List<UMRole> searchDesignationByName = userManagementServiceWrapper.retrieveDesignations(searchText, companyId);
            if (!CollectionUtils.isEmpty(searchDesignationByName)) {
                for (UMRole umrole : searchDesignationByName) {
                    ids.add(umrole.getId());
                }
            }
        } else {
            searchUsersByName = userManagementServiceWrapper.searchUsersByName(searchString, companyId);
        }
        if (referenceType != null) {
            searchString = null;
        }

        List<HkTaskEntity> searchTasks = employeeService.searchTasks(searchString, searchUsersByName, referenceType, ids, id, companyId);
        List<TaskDataBean> taskDataBeanList = null;
        if (!CollectionUtils.isEmpty(searchTasks)) {
            taskDataBeanList = new ArrayList<>();
            //List of user names
            List<String> userNamesList = new ArrayList<>();
            for (HkTaskEntity taskEntity : searchTasks) {
                userNamesList.add(taskEntity.getCreatedBy() + SEPARATOR + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                Set<HkTaskRecipientEntity> taskRecipientEntitys = taskEntity.getHkTaskRecipientEntitySet();
                if (taskRecipientEntitys != null && !taskRecipientEntitys.isEmpty()) {
                    for (HkTaskRecipientEntity taskRecipientEntity : taskRecipientEntitys) {
                        if (!taskRecipientEntity.getIsArchive()) {
                            userNamesList.add(taskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceInstance() + SEPARATOR + taskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceType());
                        }
                    }
                }
            }
            Map<String, String> userNamesMap = userManagementServiceWrapper.retrieveRecipientNames(userNamesList);
            for (HkTaskEntity taskEntity : searchTasks) {
                TaskDataBean taskDataBean = new TaskDataBean();
                taskDataBean.setId(taskEntity.getId());
                taskDataBean.setTaskName(taskEntity.getTaskName());
                taskDataBean.setDueDate(HkSystemFunctionUtil.convertToClientDate(taskEntity.getDueDt(), loginDataBean.getClientRawOffsetInMin()));
                StringBuilder recipientName = new StringBuilder();
                //Set task recipients
                Set<HkTaskRecipientEntity> taskRecipientEntitys = taskEntity.getHkTaskRecipientEntitySet();
                if (taskRecipientEntitys != null && !taskRecipientEntitys.isEmpty()) {
                    for (HkTaskRecipientEntity hkTaskRecipientEntity : taskRecipientEntitys) {
                        if (userNamesMap != null && !userNamesMap.isEmpty() && !hkTaskRecipientEntity.getIsArchive()) {
                            String get = userNamesMap.get(hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceInstance() + SEPARATOR + hkTaskRecipientEntity.getHkTaskRecipientEntityPK().getReferenceType());
                            if (recipientName.toString().length() > 0) {
                                recipientName.append(",");
                            }
                            recipientName.append(get);
                        }
                    }
                }
                taskDataBean.setLastUpdate(recipientName.toString());
                taskDataBean.setStatus(taskEntity.getStatus());
                taskDataBeanList.add(taskDataBean);
            }
        }
        return taskDataBeanList;
    }
}
