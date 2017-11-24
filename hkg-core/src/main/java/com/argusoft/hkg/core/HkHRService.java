/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkAssetIssueEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkEventRegistrationFieldEntity;
import com.argusoft.hkg.model.HkEventRegistrationFieldValueEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkShiftRuleEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.usermanagement.common.model.UMUser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Includes modules like Event & Registration form, Holiday, Leave Settings,
 * Leave Workflow, Shift etc.
 *
 * @author Mital, Tejas
 */
public interface HkHRService {

    /*
     * Methods for Holiday start.............
     */
    /**
     * Saves holiday. Same as saveOrUpdate. Creates new entity if it doesn't
     * already exist, updates if exist.
     *
     * @param holidayEntity Holiday Entity to be saved.
     * @param notifyUserList The list of users who need to notified by
     * notification.
     */
    public void saveHoliday(HkHolidayEntity holidayEntity, List<Long> notifyUserList);

    /**
     * This method retrieves the holiday by given id.
     *
     * @param holidayId The id of the holiday.
     * @return Returns the holiday object.
     */
    public HkHolidayEntity retrieveHolidayById(Long holidayId);

    /**
     * Removes given Holiday Entity. Here, it'll be deactivated and saved in
     * database.
     *
     * @param holidayEntity Holiday Entity to be removed.
     * @param notifyUserList
     */
    public void removeHoliday(HkHolidayEntity holidayEntity, List<Long> notifyUserList);

    /**
     * Removes Holiday Entity with given id. Here, the entity will be
     * deactivated and saved in database.
     *
     * @param holidayId Id of the holiday entity that needs to be removed.
     * @param notifyUserList
     */
    public void removeHoliday(Long holidayId, List<Long> notifyUserList);

    /**
     * Saves all the holiday entities. Same as saveOrUpdate().
     *
     * @param holidayEntities List of entities to be saved/updated in database.
     */
    public void saveAllHolidays(List<HkHolidayEntity> holidayEntities);

    /**
     * Retrieves all the holidays.
     *
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return List of holidays.
     */
    public List<HkHolidayEntity> retrieveAllHolidays(Long franchise, Boolean archiveStatus);

    /**
     * Search based on the title of the holiday.
     *
     * @param holidayTitle Search string to match the holiday title.
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return List of holidays.
     */
    public List<String> searchPreviousYearsHolidaysByTitle(String holidayTitle, Long franchise, Boolean archiveStatus);

    /**
     * Retrieves Holidays by the given parameters.
     *
     * @param holidayTitle Title of Holiday, null if none.
     * @param startDate Start date of the holiday, null if none.
     * @param endDate End date of the holiday, null if none.
     * @param searchOnlyCurrentYear True if result list should contain only
     * current year's holidays, false otherwise.
     * @param franchise Id of Franchise. If less than or equal to 0, holidays of
     * franchise with 0 will be returned.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return List of holidays.
     */
    public List<HkHolidayEntity> retrieveHolidaysByCriteria(String holidayTitle, Date startDate, Date endDate, boolean searchOnlyCurrentYear, Long franchise, Boolean archiveStatus);

    /**
     * This method checks for the given date if it's a holiday.
     *
     * @param searchDate The date for which the holiday needs to be checked.
     * @param franchise The id of the franchise.
     * @return Returns true if the given date is a holiday, false otherwise.
     */
    public boolean isHoliday(Date searchDate, Long franchise);

    public List<Date> retrieveDateListExcludingHoliday(Date fromDate, Date toDate, Long franchise);

    /**
     * This method returns the list of holidays based on given search string.
     * Search criteria is: holiday reason and the date of holiday.
     *
     * @param searchString The search string given by user.
     * @param franchise The id of the franchise.
     * @return Returns the list of holidays that match the given string.
     */
    public List<HkHolidayEntity> searchHolidays(String searchString, Long franchise);

    /*
     * Methods for workflow start.............
     */
    /**
     * This method creates the workflow. Set all the fields including lastLevel
     * and the approver set properly for all the entities.
     *
     * @param workflow
     * @param deptName
     * @param notifyUserList
     */
    public void createWorkflow(HkWorkflowEntity workflow, String deptName, List<Long> notifyUserList);

    /**
     * This method creates the workflows. Set all the fields including lastLevel
     * and the approver set properly for all the entities.
     *
     * @param workflowEntityArr
     * @param deptNames
     * @param notifyUserArr
     */
    public void createWorkflow(HkWorkflowEntity[] workflowEntityArr, String[] deptNames, List<Long>[] notifyUserArr);

    /**
     * This method updates the workflow entities. Pass the new approver set
     * separately.
     *
     * @param workflow The entity to be updated.
     * @param approversSet The set of the approvers. Pass only active approvers
     * irrespective of old or new.
     * @param departmentName
     * @param notifyUserList
     */
    public void updateWorkflow(HkWorkflowEntity workflow, Set<HkWorkflowApproverEntity> approversSet, String departmentName, List<Long> notifyUserList);

    /**
     * This method archives the workflow with given workflow id. All the related
     * approver entities will also be archived.
     *
     * @param workflowId The id of the workflow that is to be removed.
     * @param removedBy The id of the user who is removing this object.
     */
    public void removeWorkflow(Long workflowId, Long removedBy);

    /**
     * This method retrieves the ids of the departments for which the workflow
     * exists. List of department ids will be returned. Then the call can be
     * made to UM to retrieve names of the departments.
     *
     * @param franchise The id of the franchise.
     * @return Returns the ids of the departments for which workflow exists.
     */
    public List<Long> retrieveDepartmentIdsForExistingWorkflows(Long franchise);

    /**
     * This method retrieves the workflow entity for the given id.
     *
     * @param workflowId The id of the workflow.
     * @param includeApproverDetails True if approver details needed to be set
     * in the result list, false otherwise.
     * @return Returns the workflow entity for the given workflow id.
     */
    public HkWorkflowEntity retrieveWorkflowById(Long workflowId, boolean includeApproverDetails);

    /**
     * This method retrieves the approver list for given workflow id.
     *
     * @param workflowId The id of the workflow entity.
     * @param level Level of the workflow entity
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of workflow approvers.
     */
    public List<HkWorkflowApproverEntity> retrieveWorkflowApproversByWorkflowId(Long workflowId, Integer level, Boolean archiveStatus);

    /**
     * This method retrieves the workflow for the given department. Note: if
     * department id passed as null, this method returns the default workflow.
     *
     * @param departmentId The id of the department. If null, default workflow
     * is returned.
     * @param franchise The id of the franchise.
     * @param includeApproverDetails True if approver details needed to be set
     * in the result list, false otherwise.
     * @return Returns the Workflow entity with given department id.
     */
    public HkWorkflowEntity retrieveWorkflowForDepartment(Long departmentId, Long franchise, boolean includeApproverDetails);

    public List<HkWorkflowApproverEntity> retrieveWorkFlowApprover(Long user, Long department);

    /**
     * This method add records for issuing assets to department or employee.
     * Managed and non-managed both assets will be handle here.
     *
     * @param assetIssueEntities assets needs to be issued
     * @return Returns the success/failure of issue assets
     */
    public boolean issueAssets(List<HkAssetIssueEntity> assetIssueEntities, String empCode, String empName, String assignerCode, String assignerName, String departmentName, List<Long> notifyUserList);

    /*
     Methods for Event start.............
     */
    /**
     * This method is used to create event. Set hkEventRecipientEntitySet and
     * hkEventRegistrationFieldEntitySet approperiately.
     *
     * @param eventEntity The event object that needs to be created.
     * @param notifyUserMap map of users to be notified
     */
    public void createEvent(HkEventEntity eventEntity, Map<String, List<Long>> notifyUserMap);

    /**
     * This method is used to update the existing events. The new set of users
     * is passed to check if they still can register for this event or not. If
     * the set doesn't contain the user ids of the users who registered
     * previously, old registrations will be archived and will be removed from
     * the count.
     *
     * @param eventEntity The event entity that needs to be updated.
     * @param newUsersSet The set of the users who are eligible to register for
     * this event.
     * @param notifyUserList
     * @param isFormChanged
     */
    public void updateEvent(HkEventEntity eventEntity, Set<Long> newUsersSet, List<Long> notifyUserList, boolean isFormChanged);

    /**
     * This method updates the event list, no other update operation is done
     * this method.
     *
     * @param eventList The list of events to be updated.
     * @param notifyUserList
     */
    public void updateEvents(List<HkEventEntity> eventList, Set<Long> notifyUserList);

    /**
     * This method removes the event. It just sets the event's archive status to
     * true.
     *
     * @param eventId The id of the event.
     * @param removedBy The id of the user who is removing the event.
     */
    public void removeEvent(Long eventId, Long removedBy);

    /**
     * This method archives the completed events (as per current UI).
     *
     * @param eventId The id of the event.
     * @param archivedBy The id of the user who is archiving the event.
     */
    public void archiveEvent(Long eventId, Long archivedBy);

    /**
     * This method is used for searching the events by name, date, category,
     * status.
     *
     * @param searchStr The search string for seaching the event.
     * @return Returns the list of events that match anywhere in the given
     * criteria.
     */
    public List<HkEventEntity> searchEvents(String searchStr, Long franchise);

    /**
     * This method is used to retrieve events by given criteria.
     *
     * @param statusList The list of statues that are required.
     * @param categoryId The id of the category.
     * @param archiveStatus True if archived records are required, false if
     * active records are required. Null if both should be added in result.
     * @param includeRecipients true if recipients details are required, false
     * otherwise.
     * @param includeRegFields true if registration fields are required, false
     * otherwise.
     * @param includeReg true if existing registration are required
     * @return Returns the list of events that match the given criteria.
     */
    public List<HkEventEntity> retrieveEventsByCriteria(List<String> statusList, Long categoryId, Boolean archiveStatus, boolean includeRecipients, boolean includeRegFields, boolean includeReg);

    /**
     * This method retrieves the registration form names of the previous events.
     *
     * @param franchise The id of the franchise.
     * @return Returns the map of event ids (Key) and form name (value).
     */
    public Map<Long, String> retrieveEventFormNames(Long franchise);

    /**
     * This method retrieves the list of registration fields for given event.
     * Used for "configure registration form", "preview the invitation card".
     *
     * @param eventId The id of the event.
     * @return Returns the list of EventRegistrationField entities for given
     * event.
     */
    public List<HkEventRegistrationFieldEntity> retrieveRegistrationFieldsByEventId(Long eventId);

    /**
     * This method retrieves the list of upcoming events. Used to show on event
     * pages. Only required fields are returned so if any other field required
     * for this list, it must be added in method implementation.
     *
     * @param categoryId The id of the category.
     * @param departmentId
     * @param franchiseId The id of franchise.
     * @param userId
     * @param haveAddEditRights
     * @return Returns the list of Events. The list is sorted in ascending order
     * of most recent event.
     */
    public List<HkEventEntity> retrieveUpcomingEvents(Long categoryId, Long departmentId, Long userId, Long franchiseId, boolean haveAddEditRights);

    /**
     * This method retrieves the list of completed events. Archived events won't
     * get displayed here. Sorted list in descending order of end date and end
     * time. 4 results returned only, as per FS. Only required fields returned
     * so if any other field required for this list, it must be added in method
     * implementation.
     *
     * @param categoryId The id of the category.
     * @param departmentId
     * @param franchiseId The id of the franchise.
     * @param userId
     * @param haveAddEditRights
     * @return Returns the list of Events. The list is sorted in descending
     * order of event date, last completed shown first.
     */
    public List<HkEventEntity> retrieveCompletedEvents(Long categoryId, Long departmentId, Long userId, Long franchiseId, boolean haveAddEditRights);

    /**
     * This method retrieves the active (created, upcoming, currently active)
     * events. Used for main page where categories shown with the count of
     * active and upcoming events.
     *
     * @param franchise The id of the franchise.
     * @return Returns the map where key is the category object and value is the
     * number of active events in it.
     */
    public Map<HkCategoryEntity, Integer> retrieveActiveEventsCount(Long franchise);

    /**
     * This method retrieve the event object by given id.
     *
     * @param eventId The id of the event.
     * @param includeInvitees true if invitees' date is required, false
     * otherwise.
     * @param includeRegFields true if registration fields are required, false
     * otherwise.
     * @return Returns the Event object which matches given id.
     */
    public HkEventEntity retrieveEventById(Long eventId, boolean includeInvitees, boolean includeRegFields);

    /**
     * This method retrieves Users' Event Registration objects (The users who
     * have registered for the given event).
     *
     * @param eventId The id of the event.
     * @param archiveStatus True if archived users needed, false if active users
     * are needed, null if both required.
     * @return Returns the list of User Event Registration entities.
     */
    public List<HkEventRegistrationEntity> retrieveUserEventRegistrationEntities(Long eventId, Long userId, Boolean archiveStatus);

    public List<HkEventRegistrationFieldValueEntity> retrieveEventRegistrationFieldValues(Long eventId, Long userId);

    /**
     * This method checks if the event name is same as any active event.
     *
     * @param eventTitle The title of the event.
     * @param franchise The id of the franchise.
     * @param skipEventId The id of the event that needs to be searched.
     * @return Returns true if event name is same as some active event, false
     * otherwise.
     */
    public boolean doesEventNameExist(String eventTitle, Long franchise, Long skipEventId);

    public void registerForEvent(HkEventRegistrationEntity eventRegistrationEntity, Map<Long, String> fieldMap);

    public void editEventRegistration(HkEventRegistrationEntity eventRegistrationEntity, Map<Long, String> fieldMap);

    public void cancelEventRegistration(Long eventId, Long userId);

    /* Shift Management */
    /**
     * Create a shift
     *
     * @param hkShiftEntity
     * @return
     * @author Tejas
     */
    public void createShift(HkShiftEntity hkShiftEntity, String departmentName, List<Long> notifyUserList, boolean isTemporaryShift, Long parentShiftId);

    /**
     * get all root level shifts with related temp shifts. This method can be
     * used to build the shift tree
     *
     * @param franshise
     * @return list of top level shifts.
     * @author Tejas : Changes done by Mital
     */
    public HkShiftEntity retrieveDefaultShift(long franshise);

    /**
     * update Shift
     *
     * @param hkShiftEntity
     * @return
     * @author Tejas
     */
    public void updateShift(HkShiftEntity hkShiftEntity, List<Long> notifyUserList);

    /**
     * remove Shift
     *
     * @param shiftId
     * @param hkShiftEntity
     * @return
     * @author Tejas
     */
    public void removeShift(long shiftId, Long removedBy, List<Long> notifyUserList);

    /**
     * get all root level shifts with related temp shifts. This method can be
     * used to build the shift tree
     *
     * @param franshise
     * @return list of top level shifts.
     * @author Tejas
     */
    public List<HkShiftEntity> retrieveShifts(Long franshise);

    /**
     *
     * @param franchise
     * @param id
     * @return
     */
    public HkShiftEntity retrieveShiftById(long franchise, Long id);

    /**
     *
     * @param franchise
     * @param searchString search string to search shifts for.
     * @return list of Shift entities whose name starts with searchString.
     * @author Tejas
     */
    public List<HkShiftEntity> searchShifts(long franchise, String searchString);

    /**
     *
     * @param shiftRuleEntity
     * @param shiftId
     * @author Tejas
     */
    public void addShiftRule(HkShiftRuleEntity shiftRuleEntity, long shiftId);

    /**
     * Remove an existing shift rule.
     *
     * @param shiftRuleEntity
     * @param shiftId
     * @author Tejas
     */
    public void removeShiftRule(HkShiftRuleEntity shiftRuleEntity, long shiftId);

    /**
     * Creates a default shift for franchise.
     *
     * @param hkShiftEntity
     */
    public void createDefaultShift(HkShiftEntity hkShiftEntity, List<Long> notifyUserList);

    /**
     * This method retrieves the shift ids and titles by given department. If no
     * shift found for given department, default shift will be returned.
     * Exclusively used in Employee module. Can be used at other places with
     * same requirement.
     *
     * @param franchise The id of the franchise.
     * @param deptId The id of the department.
     * @return Returns the map of shift ids and shift names.
     */
    public Map<Long, String> retrieveShiftsByDepartment(Long franchise, Long deptId);

    /**
     *
     * @param franchise
     * @param searchString search string to search shifts for.
     * @param departmentIds list of departmentIds
     * @return list of Shift entities whose name starts with searchString or
     * have departments specified by departmentIds
     * @author Tejas
     */
    List<HkShiftEntity> searchShifts(long franchise, String searchString,
            List<Long> departmentIds);

    public HkShiftEntity retrieveShiftById(long franchise, Long id, boolean isShiftDeptNeeded, boolean isShiftDetailsNeeded, boolean isTempShiftsNeeded, boolean isRuleEntityNeeded);

    public HkShiftEntity retrieveCurrentShiftForUserOperation(Long shiftId, Date onDate);

    public List<HkShiftEntity> inactiveTemporaryShift(Date relieveToDate);

    public List<HkShiftEntity> retrieveShiftByIds(List<Long> shiftIds,Long franchise);
    
    public Map<Long,Set<HkShiftEntity>> retrieveShiftByDeptIds(List<Long> deptIds,Long franchise);
    
    public void sendRotateShiftNotification(List<UMUser> userList, HkShiftEntity shiftEntity);
}
