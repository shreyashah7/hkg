/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkLeaveApprovalEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkTaskEntity;
import com.argusoft.hkg.model.HkTaskRecipientDtlEntity;
import com.argusoft.hkg.model.HkTaskRecipientEntity;
import com.argusoft.hkg.model.HkUserAttendanceEntity;
import com.argusoft.hkg.model.HkUserOperationEntity;
import com.argusoft.hkg.model.HkUserWorkHistoryEntity;
import com.argusoft.hkg.core.util.HkUserOperationEnum;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service class for Employee related activities. Includes Task, Leave
 * (Approval, Request), Goals, Register to Event etc.
 *
 * @author Mital
 */
public interface HkEmployeeService {

    /**
     * This class creates the task entity and related entities. Set the
     * taskRecipientEntity list with given reference ids and types. Used for
     * creating the tasks.
     *
     * @param taskEntity The object that needs to be created.
     * @param recipientIds The ids of the recipients.
     */
    public void createTask(HkTaskEntity taskEntity, Set<Long> recipientIds, String assignerCode, String assignerName,Long createdBy);

    /**
     * This method updates the task entity and related changes. Changes will
     * always reflect from the next day so today if some user has this task
     * assigned, he'll be able to do this task but no new task will be generated
     * from this task if he's removed from recipients. Set the new
     * taskRecipientEntity list in the new param instead of taskEntity object.
     * Used for updating the tasks.
     *
     * @param taskEntity The object that needs to be updated.
     * @param taskRecipientsSet The set of new recipients, including old active
     * recipients.
     */
    public void updateTask(HkTaskEntity taskEntity, Set<HkTaskRecipientEntity> taskRecipientsSet, Set<Long> recipientIds, String assignerCode, String assignerName,Long createdBy);

    /**
     * This method removes the task and related entities from other tables. Used
     * for deleting some task. When task is removed, all the recipient info
     * objects will be removed as well as the recipient detail objects will also
     * be removed so the recipients will no longer see this task in their list.
     *
     * @param taskEntity The object that needs to be removed.
     */
    public void removeTask(HkTaskEntity taskEntity, String assignerCode, String assignerName);

    /**
     * This method removes the task and related entities from other tables. Used
     * for deleting some task. When task is removed, all the recipient info
     * objects will be removed as well as the recipient detail objects will also
     * be removed so the recipients will no longer see this task in their list.
     *
     * @param taskId Id of the task entity object.
     */
    public void removeTask(Long taskId, String assignerCode, String assignerName, Long franchise);

    /**
     * Used for search component.
     *
     * @param searchStr
     * @param recipientIds
     * @param referenceType
     * @param referenceIds
     * @param userId
     * @param franchise
     * @return
     */
    public List<HkTaskEntity> searchTasks(String searchStr, List<Long> recipientIds, String referenceType, List<Long> referenceIds, Long userId, Long franchise);

    /**
     * This method searches for the task name which matches given searchName.
     *
     * @param searchName The task name that needs to be matched.
     * @param userId The id of the user (creator of the task).
     * @param franchise The id of the franchise.
     * @return Returns the list of task names that match the given criteria.
     */
    public List<String> searchTaskNames(String searchName, Long userId, Long franchise);

    public void cancelRepeatedTask(Long taskId, int repetitionCount, String assignerCode, String assignerName, Long franchise);

    /**
     * This method retrieves the recipient entities by given task id.
     *
     * @param taskId The id of the task for which recipients are needed.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of task recipient entities.
     */
    public List<HkTaskRecipientEntity> retrieveTaskRecipientsByTaskId(Long taskId, Boolean archiveStatus);

    /**
     * This method retrieves the list of all the recipients.
     *
     * @param taskId The id of the task for which the recipient details are
     * needed to be fetched.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of the task recipient detail objects.
     */
    public List<HkTaskRecipientDtlEntity> retrieveTaskRecipientDtlsByTaskId(Long taskId, Boolean archiveStatus);

    /**
     * This method updates the taskRecipientDtl object. Used when task is marked
     * as completed, or canceled.
     *
     * @param taskRecipientDtlEntity The object that is to be updated.
     */
    public void updateTaskRecipientDtl(HkTaskRecipientDtlEntity taskRecipientDtlEntity, String assigneeCode, String assigneeName);

    /**
     * This method retrieves the task by given id.
     *
     * @param taskId The id of the task to be retrieved.
     * @param includeRecipients True if set of references needed to be set in
     * the returned task object, false otherwise.
     * @param includeRecipientDetails True if set of recipient detail objects is
     * needed to be set in the returned task object, false otherwise.
     * @return Returns the matched taskEntity object.
     */
    public HkTaskEntity retrieveTaskById(Long taskId, boolean includeRecipients, boolean includeRecipientDetails);

    /**
     * This method retrieves the recipient detail objects. Can be used for smart
     * lists like Recent tasks, All tasks, Due today, Completed Tasks.
     *
     * @param dueDate The date for which the task is due.
     * @param assignedStartDate The start date of the task when it was assigned.
     * @param assignedEndDate The end date of the task when it was assigned.
     * @param assignee The user to which the task is assigned.
     * @param assigner The user who has created the main task.
     * @param status The status of the task.
     * @param categoryId The id of the category.
     * @param repetitionCount The no. of repetition.
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @param sortProperty Property name for which sorting needs to be done. See
     * HkTaskRecipientDtlField for values.
     * @param isDesc True if descending order is needed, false otherwise.
     * @return Returns the list of recipientDtl objects.
     */
    public List<HkTaskRecipientDtlEntity> retrieveTaskRecipientDtlsByCriteria(Date dueDate, Date assignedStartDate, Date assignedEndDate, Long assignee, Long assigner, List<String> statusList, Long categoryId, Long repetitionCount, Long franchise, Boolean archiveStatus, String sortProperty, boolean isDesc);

    /**
     * This method retrieves the recipient detail objects. Can be used for smart
     * lists like Recent tasks, All tasks, Due today, Completed Tasks - to show
     * the tasks assigned by the given user.
     *
     * @param dueDate The date for which the task is due.
     * @param createdStartDate The start date of the task when it was assigned.
     * @param createdEndDate The end date of the task when it was assigned.
     * @param assigner The user who has created the main task.
     * @param status The status of the task.
     * @param categoryId The id of the category.
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @param includeRecipientDetails True if recipient details required, false
     * otherwise.
     * @param sortProperty Property name for which sorting needs to be done. See
     * HkTaskField for values.
     * @param isDesc True if descending order is needed, false otherwise.
     * @return Returns the list of recipientDtl objects.
     */
    public List<HkTaskEntity> retrieveTasksByCriteria(Date dueDate, Date createdStartDate, Date createdEndDate, Long assigner, String status, Long categoryId, Long franchise, Boolean archiveStatus, boolean includeRecipientDetails, String sortProperty, boolean isDesc);

    /**
     * This method retrieves the list of tasks by given criteria.
     *
     * @param isRepetitive True if the tasks to be fetched should be repetitive,
     * false otherwise. Used for scheduler while generating tasks.
     * @param isCompleted True if the tasks to be fetched should be completed
     * (status=C), false otherwise. When passed null, all the tasks irrespective
     * of the status of task will be returned.
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of task entities.
     */
    public List<HkTaskEntity> retrieveTasksByCriteria(Boolean isRepetitive, Boolean isCompleted, Long franchise, Boolean archiveStatus);

    /**
     *This method retrieves all the tasks from the given status list.
     * @param statusList List of status for the task
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return
     */
    public List<HkTaskEntity> retrieveTasksByStatuses(List<String> statusList,Long franchise, Boolean archiveStatus);

    /**
     * This method updates the task entities. No other changes will be done in
     * other related tables. Used in scheduler.
     *
     * @param taskListToUpdate The list of tasks to be updated.
     */
    public void updateTaskInfos(List<HkTaskEntity> taskListToUpdate);

    /**
     * This method updates the task recipient dtl objects. No other changes in
     * related tables will be done. Used in scheduler.
     *
     * @param taskRecipientDtlListToUpdate The list of task recipient dtls to be
     * updated.
     */
    public void updateTaskRecipientDtlInfos(List<HkTaskRecipientDtlEntity> taskRecipientDtlListToUpdate);

    /**
     * This method retrieves the taskRecipientDtl object by given id.
     *
     * @param recipientDtlId The id of the taskRecipientDtl object.
     * @return Returns the object with given id.
     */
    public HkTaskRecipientDtlEntity retrieveTaskRecipientDtlById(Long recipientDtlId);

    /**
     * This method changes the category of the given task ids.
     *
     * @param receivedTaskIds The task ids that are given by others to this
     * user.
     * @param createdTaskIds The task ids which are created by this user.
     * @param newCategoryId The id of new category that is to be assigned now.
     * @param franchise The id of the franchise.
     */
    public void changeCategoryOfTasks(List<Long> receivedTaskIds, List<Long> createdTaskIds, Long newCategoryId, Long franchise);

    /**
     * This method returns the map where key=categoryId and value=number of due
     * tasks
     *
     * @param assignee The id of the assignee who is given the tasks.
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the map of category ids and the number of tasks in that
     * category
     */
    public Map<Long, Integer> retrievePendingTaskCountGroupedByCategory(Long assignee, Long franchise, Boolean archiveStatus);

    /**
     * This method returns the list of the assigners who have assigned some task
     * to given assignee and the task is due
     *
     * @param assignee The id of the assignee who is given the task.
     * @param franchise The id of the franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of assigner ids in sorted form of descending
     * order of task assigned date.
     */
    public List<Long> retrieveTaskAssignerIds(Long assignee, Long franchise, Boolean archiveStatus);

    /**
     * This method is used for applying leave. Retrieve appropriate workflow
     * from user's department and pass it as argument for passing it in
     * hierarchy.
     *
     * @param leaveEntity The leave object to be applied.
     * @param deptId The id of user's department.
     */
    public void applyLeave(HkLeaveEntity leaveEntity, Long deptId, Long shiftId, String userCode, String userName, List<Long> notifyUserList);

    /**
     * This method is used for updating leave entity for normal fields.
     *
     * @param leaveEntity The leave entity that is to be updated.
     */
    public void updateLeave(HkLeaveEntity leaveEntity, String empCode, String empName, Long shiftId, List<Long> notifyUserList);

    /**
     * This method cancels the existing leave request. If it was approved
     * before, it'll send the notification to the approvers' list, if it was
     * pending and was in queue for response, the leave will not be asked for
     * approval again for cancellation.
     *
     * @param leaveId The id of the leave.
     */
    public void cancelLeave(Long leaveId, String empCode, String empName, List<Long> notifyUserList);

    /**
     * This method cancels the approved leave by the approver.
     *
     * @param leaveId The id of the leave.
     * @param cancelledBy The id of the approver.
     * @param remarks
     */
    public void cancelApprovedLeave(Long leaveId, Long cancelledBy, String remarks);

    /**
     * This method archives the leave with given id.
     *
     * @param leaveId The id of the leave.
     */
    public void archiveLeave(Long leaveId);

    /**
     * This method retrieves the leaves for the search component on My leaves
     * page. Search criteria: Reason and date (from date and to date)
     *
     * @param searchStr The search string that is entered by the user.
     * @param userId The id of the user whose leaves are to be fetched.
     * @param franchise The id of user's franchise.
     * @return Returns the list of leaves which match the criteria.
     */
    public List<HkLeaveEntity> searchMyLeaves(String searchStr, Long userId, Long franchise);

    /**
     * This method retrieves the leave approval entities for the search
     * component on Approval list page. Search criteria: employee, reason and
     * date.
     *
     * @param searchStr The search string that is entered by the user.
     * @param applierIds The id of the appliers whose names match the given
     * string.
     * @param deptId The id of user's department.
     * @param userId The id of the approver / responder whose responded or
     * pending request leaves are to be fetched.
     * @param franchise The id of the user's franchise.
     * @return Returns the list of approval objects.
     */
    public List<HkLeaveApprovalEntity> searchLeavesForApprover(String searchStr, List<Long> applierIds, Long deptId, Long userId, Long franchise);

    /**
     * This method is used to check if the user is on leave for the given time.
     * Pass the status list as {Pending, Approved} if checking for user while
     * applying. Pass the list as {Approved} while allocating the stock to user.
     *
     * @param userId The id of the user whose leave status needs to be checked.
     * @param fromDate The start date for which leave status needs to be
     * checked.
     * @param toDate The end date for which leave status needs to be checked.
     * @param statusList The list of statuses. e.g. - while applying, to check
     * the leave status for given leave date, statusList should be {Pending,
     * Approved}
     * @param franchise The id of the franchise.
     * @return Returns list of leave ids that match the given date and user has
     * applied for given dates. Null/empty list returned if no leave found for
     * given date.
     */
    public List<Long> isUserOnLeave(Long userId, Date fromDate, Date toDate, List<String> statusList, Long franchise);

    /**
     * This method retrieves the leave by given id.
     *
     * @param leaveId The id of the leave.
     * @param includeLeaveApprovals True if approvals entities required, False
     * otherwise.
     * @return Returns the list of leave entity with given leaveId.
     */
    public HkLeaveEntity retrieveLeaveById(Long leaveId, boolean includeLeaveApprovals);

    /**
     * This method is used to retrieve the leaves by the given criteria.
     * Important: archiveStatus in leave is used for showing list on user's own
     * leave list page. So pass null if this method is being used for reports.
     * Used to retrieve user's own leaves' list.
     *
     * @param userId The id of the user whose leaves need to be fetched.
     * @param statusList The list of status of the leave.
     * @param franchise The id of the franchise.
     * @param archiveStatus Important: This status in leave is used for showing
     * list on user's own leave list page. So pass null if this method is being
     * used for reports. If used for showing user's own leaves, pass True if
     * archived leaves required, False if non-archived leaves required, null if
     * all combined required.
     * @return Returns the list of leave entities.
     */
    public List<HkLeaveEntity> retrieveLeavesByCriteria(Long userId, List<String> statusList, Long franchise, Boolean archiveStatus);

    /**
     * This method is used to retrieve leave approval entities. Used for
     * retrieving the leaves for the user to respond.
     *
     * @param leaveId The id of the leave.
     * @param level
     * @param status The status of the leave.
     * @param deptId The id of the department.
     * @param franchise The id of the franchise.
     * @return Returns the list of leave approval entities.
     */
    public List<HkLeaveApprovalEntity> retrieveLeaveApprovalEntitiesByCriteria(Long leaveId, Integer level, List<String> status, Long deptId, Long franchise);

    /**
     * This method is used to give response for the given leave id. Used to
     * respond the leave - Approve, Disapprove, Comment - Pass the status
     * appropriately.
     *
     * @param leaveId The id of the leave.
     * @param responseBy The id of the user who is responding the leave.
     * @param remarks The remarks the user has put while responding.
     * @param status The status of the responder - Approved, Disapproved,
     * Commented.
     * @param empCode
     * @param empName
     * @param approverName
     * @param notifyUserList
     */
    public void respondLeave(Long leaveId, Long responseBy, String remarks, String status, String empCode, String empName, String approverName, List<Long> notifyUserList);

    /**
     * This method is used to store system operations performed by the User like
     * Login, Logout. Except comments and addByUser all fields are mandatory.
     *
     * @param userOperation Type of User operation
     * @param onTime Date and time of that operation
     * @param userId The id of the User.
     * @param comments comments if added manually
     * @param addedByUser The id of user who added the entry manually
     * @param franchise The id of the franchise.
     */
    public void createUserOperation(HkUserOperationEnum userOperation, Date onTime, Long userId, String comments, Long addedByUser, Long franchise);

    /**
     * This method is used to retrieve system operations performed by the Users
     * between specified date
     *
     * @param fromDate Date from which the operation data should be retrieved.
     * Mandatory if toDate is present.
     * @param toDate Date till which the operation data should be retrieved.
     * Mandatory if fromDate is present.
     * @param userId The id of the User. Mandatory if franchise not present.
     * @param franchise The id of the franchise. Mandatory if userId not
     * present.
     *
     * @return list of UserOperation entities.
     */
    public List<HkUserOperationEntity> retrieveUserOperations(Date fromDate, Date toDate, Long userId, Long franchise);

    /**
     * This method is used to calculate attendance of system users. Its for
     * backend processing only,
     */
    public void calculateUserAttendance();

    /**
     * This method is used to retrieve attendance for the franchise.
     *
     * @param fromDate Date from which the attendance data should be retrieved.
     * Mandatory if toDate is present.
     * @param toDate Date till which the attendance data should be retrieved.
     * Mandatory if fromDate is present.
     * @param franchise The id of the franchise. This field is mandatory.
     *
     * @return list of UserAttendance entities.
     */
    public List<HkUserAttendanceEntity> retrieveUserAttendance(Date fromDate, Date toDate, Long franchise);

    /**
     * This method is used to store system operations performed by the User like
     * Login, Logout. Except comments all fields are mandatory
     *
     * @param oldUserOperation Previous type of User operation
     * @param oldOnTime Previous date and time of that operation
     * @param newUserOperation New type of User operation
     * @param newOnTime New date and time of that operation
     * @param userId The id of the User.
     * @param comments Comments from the user who made the update request
     * @param modifiedByUser User who made this update request
     * @param franchise The id of the franchise.
     */
    public void updateUserOperation(HkUserOperationEnum oldUserOperation, Date oldOnTime, HkUserOperationEnum newUserOperation, Date newOnTime, Long userId, String comments, Long modifiedByUser, Long franchise);

    /**
     * This method is used to retrieve leave approval entities. Used for
     * retrieving the leaves for the user to respond.
     *
     * @param statusList The status of the leave.
     * @param userId The id of user
     * @param deptId The id of the department.
     * @return Returns the list of leave approval entities.
     */
    public List<HkLeaveApprovalEntity> retrieveLeavesForApprover(List<String> statusList, Long userId, Long deptId);

    public HkLeaveApprovalEntity retrieveHkLeaveApprovalEntityById(Long id);

    public void updateHkLeaveApprovalEntity(HkLeaveApprovalEntity leaveApprovalEntity);
    
    public HkUserWorkHistoryEntity
            retrieveShiftForUserFromUserWorkHistory(Long userId);
}
