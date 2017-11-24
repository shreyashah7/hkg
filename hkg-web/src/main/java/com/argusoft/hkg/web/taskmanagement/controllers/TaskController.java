/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.taskmanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.assets.transformers.CategoryTransformerBean;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskDataBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDataBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDetailDataBean;
import com.argusoft.hkg.web.taskmanagement.transformers.TaskTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.MessagingTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kuldeep
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<TaskDataBean, Long> {

    @Autowired
    TaskTransformerBean taskTransformerBean;
    @Autowired
    CategoryTransformerBean categoryTransformerBean;
    @Autowired
    MessagingTransformerBean messagingTransformerBean;
    private static final String TASK_LIST = "taskList";
    private static final String TASK_RECIPIENT_LIST = "taskRecipientList";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public enum TaskFilters {

        RECENT,
        DUETODAY,
        COMPLETED,
        ALL,
        RECEIVED,
        ASSIGNED
    }

    /**
     *
     * @return
     */
    @Override
    public ResponseEntity<List<TaskDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method retrieve task by task id. Used for edit task.
     *
     * @param taskId Id to task to be retrieved
     * @return returrn TaskDataBean with TaskRecipienntDataBeanList and
     * TaskRecipientDetailDataBeanList
     */
    @Override
    public ResponseEntity<TaskDataBean> retrieveById(@RequestBody PrimaryKey<Long> taskId) {
        return new ResponseEntity<>(taskTransformerBean.retrieveTaskById(taskId.getPrimaryKey()), ResponseCode.SUCCESS, null, null);
    }

    /**
     * This method can be used to update task
     *
     * @param taskDataBean TaskDataBean with TaskRecipienntDataBeanList and
     * TaskRecipientDetailDataBeanList
     * @return returns null
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody TaskDataBean taskDataBean) {
        if (taskTransformerBean.updateTask(taskDataBean)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task updated successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Task could not be updated. Try again.", null);
        }
    }

    /**
     * This method creates new task.
     *
     * @param taskDataBean TaskDataBean with TaskRecipientDetailList
     * @return returns null
     */
    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@RequestBody TaskDataBean taskDataBean) {
        if (taskTransformerBean.createTask(taskDataBean) != null) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task created successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
        }
    }

    /**
     *
     * @param primaryKey
     * @return
     */
    @Override
    public ResponseEntity<TaskDataBean> deleteById(@RequestBody PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * To retrieve task category list for smartlist and also for select category
     *
     * @return list of SelectItem with id of category name of category
     */
//    @RequestMapping(value = "/retrievetaskcategories", method = RequestMethod.GET)
//    public ResponseEntity<List<SelectItem>> retrieveTaskCategories() {
//        return new ResponseEntity<>(taskTransformerBean.retrieveTaskCategories(), ResponseCode.SUCCESS, "", null);
//    }
    @RequestMapping(value = "/retrievetaskcategories", method = RequestMethod.GET)
    public List<CategoryDataBean> retrieveTaskCategory() {
        return categoryTransformerBean.retrieveCategoryList(CategoryType.TASK, false);
    }

    /**
     * Retrieves all tasks for user and by user as per filter string.
     *
     * @param filter filter values can be
     * "recent","duetoday","completed","all","received","assigned". This tasks
     * can be viewed from smartlist.
     * @return returns Map consiting of keys "taskForUser" and "taskByUser".
     * "taskForUser" indicates task assigned to user and "taskByUser" indicates
     * task assiged by user
     */
    @RequestMapping(value = "/retrievealltasks", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<TaskDataBean>>> retrieveAllTasks(@RequestBody String filter) {
        Map<String, List<TaskDataBean>> tasksMap = new HashMap<>();
        TaskFilters taskFilter = TaskFilters.valueOf(filter.toUpperCase());
        switch (taskFilter) {
            case RECENT:
                tasksMap.put("tasksForUser", taskTransformerBean.retrieveRecentTasksForUser());
                tasksMap.put("tasksByUser", taskTransformerBean.retrieveRecentTasksByUser());
                break;
            case DUETODAY:
                tasksMap.put("tasksForUser", taskTransformerBean.retrieveDueTodayTasksForUser());
                tasksMap.put("tasksByUser", taskTransformerBean.retrieveDueTodayTasksByUser());
                break;
            case COMPLETED:
                tasksMap.put("tasksForUser", taskTransformerBean.retrieveCompletedTasksForUser());
                tasksMap.put("tasksByUser", taskTransformerBean.retrieveCompletedTasksAssignedByUser());
                break;
            case ALL:
                tasksMap.put("tasksForUser", taskTransformerBean.retrieveAllTasksForUser());
                tasksMap.put("tasksByUser", taskTransformerBean.retrieveAllTasksByUser());
                break;
            case RECEIVED:
                tasksMap.put("tasksForUser", taskTransformerBean.retrieveReceivedTasksForUser());
                tasksMap.put("tasksByUser", null);
                break;
            case ASSIGNED:
                tasksMap.put("tasksForUser", null);
                tasksMap.put("tasksByUser", taskTransformerBean.retrieveTasksAssignedByUser());
                break;

        }
        return new ResponseEntity<>(tasksMap, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Retrieves task by category id. Used for smartlist selection of retrieving
     * task by category
     *
     * @param categoryId Specify category id for which task list is needed
     * @return returns Map consiting of keys "taskForUser" and "taskByUser".
     * "taskForUser" indicates task assigned to user and "taskByUser" indicates
     * task assiged by user
     */
    @RequestMapping(value = "/retrievetasksbycategory", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<TaskDataBean>>> retrieveTasksByCategory(@RequestBody Long categoryId) {
        Map<String, List<TaskDataBean>> tasksMap = new HashMap<>();
        tasksMap.put("tasksForUser", taskTransformerBean.retrieveTasksForUserBycategory(categoryId));
        tasksMap.put("tasksByUser", taskTransformerBean.retrieveTasksByUserByCategory(categoryId));
        return new ResponseEntity<>(tasksMap, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Method used to mark task as completed. Marks status of task to complete.
     *
     * @param taskRecipientDetailId id of taskrecipeintdetail as user can only
     * mark his task as completed.
     * @return null
     */
    @RequestMapping(value = "/attendtask", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> attendTask(@RequestBody Long taskRecipientDetailId) {
        taskTransformerBean.attendTask(taskRecipientDetailId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Method used to mark task as completed. Marks status of task to complete.
     *
     * @param taskRecipientDetailId id of taskrecipeintdetail as user can only
     * mark his task as completed.
     * @return null
     */
    @RequestMapping(value = "/completetask", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> completeTask(@RequestBody Long taskRecipientDetailId) {
        taskTransformerBean.completeTask(taskRecipientDetailId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/removetask", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> removeTask(@RequestBody Long taskId) {
        taskTransformerBean.removeTask(taskId);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task removed successfully", null);
    }

    /**
     * This method retrieves user for task assignment for
     * autocompletesuggestions of task recipient.
     *
     * @param user search string from which it searches user from db.
     * @return returns SelectItem consisting of id of user and his username
     */
    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {

        List<UMUser> umUsers = taskTransformerBean.retrieveUsersByStatus(user, Boolean.TRUE);
        List<SelectItem> hkSelectItems = messagingTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    /**
     * Creates new task category.
     *
     * @param categoryDataBean CategoryDataBean
     * @return null
     */
    @RequestMapping(value = "/createtaskcategory", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> createTaskCategory(@RequestBody CategoryDataBean categoryDataBean) {
        if (categoryTransformerBean.doesCategoryNameExist(categoryDataBean.getDisplayName(), CategoryType.TASK, null)) {
            if(categoryDataBean.getDisplayName().length() > 20){
                categoryDataBean.setDisplayName(categoryDataBean.getDisplayName().substring(0, 19) + "..");
            }
            return new ResponseEntity<>(null, ResponseCode.WARNING, categoryDataBean.getDisplayName() + " already taken.", null, false);
        } else if (categoryTransformerBean.createCategory(categoryDataBean, CategoryType.TASK)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Category added successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Could not save details, please try again.", null);
        }
    }

    /**
     * To edit task category.
     *
     * @param categoryDataBean Category databean with category id
     * @return null
     */
    @RequestMapping(value = "/edittaskcategory", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> editTaskCategory(@RequestBody CategoryDataBean categoryDataBean) {
        if (categoryTransformerBean.doesCategoryNameExist(categoryDataBean.getDisplayName(), CategoryType.TASK, categoryDataBean.getId())) {
            return new ResponseEntity<>(null, ResponseCode.WARNING, categoryDataBean.getDisplayName() + " already taken.", null, false);
        } else if (categoryTransformerBean.updateCategory(categoryDataBean, CategoryType.TASK)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Category updated successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Category could not be updated. Try again.", null);
        }
    }

    /**
     * To retrieve task suggestion when creating new task
     *
     * @return SelectItem with suggestion string
     */
    @RequestMapping(value = "/retrievetasksuggestions", method = RequestMethod.GET)
    public ResponseEntity<List<SelectItem>> retrieveTaskSuggestions() {
        return new ResponseEntity<>(taskTransformerBean.retrieveTaskSuggestions(), ResponseCode.SUCCESS, "", null);
    }

    /**
     * To retrieve task recipient details about completion of task.
     *
     * @param taskId Id of task for which recipient detail is needed.
     * @return List of taskRecipientDataBean for displaying completion status of
     * task
     */
    @RequestMapping(value = "/retrievetaskrecipients", method = RequestMethod.POST)
    public ResponseEntity<List<TaskRecipientDataBean>> retrieveTaskRecipients(@RequestBody Long taskId) {
        return new ResponseEntity<>(taskTransformerBean.retrieveTaskRecipientListByTaskId(taskId), ResponseCode.SUCCESS, "", null);
    }

    /**
     * To retrieve task assginer names for logged in user. This is used for
     * smartlist for People category
     *
     * @return Returns SelectItem with assginerNames and id.
     */
    @RequestMapping(value = "/retrievetaskassignernames", method = RequestMethod.GET)
    public ResponseEntity<List<SelectItem>> retrieveTaskAssignerNames() {
        return new ResponseEntity<>(taskTransformerBean.retrieveTaskAssignerList(), ResponseCode.SUCCESS, null, null);
    }

    /**
     * To retrieve tasks assgined by particular assigner by assignerId
     *
     * @param assignerId Id of task assigner
     * @return List of taskDataBean
     */
    @RequestMapping(value = "/retrievetasksbyassigner", method = RequestMethod.POST)
    public ResponseEntity<List<TaskDataBean>> retrieveTasksByAssigner(@RequestBody Long assignerId) {
        return new ResponseEntity<>(taskTransformerBean.retrieveTasksForUserByAssigner(assignerId), ResponseCode.SUCCESS, "", null);
    }

    /**
     * To remove task from list that is displayed to user
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/removetaskfromlist", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> removeTaskOfUserFromList(@RequestBody Long taskId) {
        if (taskTransformerBean.removeTaskOfUserFromList(taskId)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task removed successfully", null);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Only completed or cancelled task can be removed.", null);
        }
    }

//    @RequestMapping(value = "/updatetasks", method = RequestMethod.POST)
//    public ResponseEntity<PrimaryKey<Long>> updateTaskList(@RequestBody List<TaskDataBean> taskDataBeanList) {
//        taskTransformerBean.updateTaskList(taskDataBeanList);
//        return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null);
//    }
    /**
     * To update task categories of received and created tasks.
     *
     * @param taskIdAndCategoryMap
     * @return
     */
    @RequestMapping(value = "/updatetaskcategories", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateTaskCategories(@RequestBody Map<String, List<Long>> taskIdAndCategoryMap) {
        taskTransformerBean.updateTaskCategories(taskIdAndCategoryMap);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task category updated successfully", null);
    }

    @RequestMapping(value = "/updatetaskrecipients", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateTaskRecipientDetailList(@RequestBody List<TaskRecipientDetailDataBean> taskRecipientDetailDataBeanList) {
        taskTransformerBean.updateTaskRecipientDetailList(taskRecipientDetailDataBeanList);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/searchTask", method = RequestMethod.GET, produces = {"application/json"})
    public List<TaskDataBean> searchTask(@RequestParam("q") String searchString) throws GenericDatabaseException {
        List<TaskDataBean> searchTask = taskTransformerBean.searchTask(searchString);
        return searchTask;
    }

    @RequestMapping(value = "/cancelrepeatedtask", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> cancelRepeatedTask(@RequestBody Map<String, String> repetitionDetailMap) {
        taskTransformerBean.cancelRepeatedTask(repetitionDetailMap);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Task repeatition cancelled successfully", null);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/addCustomDataToCategoryDataBean", method = RequestMethod.POST)
    public CategoryDataBean addCustomDataToCategoryDataBean(@RequestBody CategoryDataBean categoryDataBean) {
        return categoryTransformerBean.addCustomDataToCategoryDataBean(categoryDataBean);
    }
}
