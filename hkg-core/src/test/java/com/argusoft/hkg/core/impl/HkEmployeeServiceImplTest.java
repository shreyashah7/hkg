/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkLeaveApprovalEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkTaskEntity;
import com.argusoft.hkg.model.HkTaskRecipientDtlEntity;
import com.argusoft.hkg.model.HkTaskRecipientEntity;
import com.argusoft.hkg.model.HkUserOperationEntity;
import com.argusoft.hkg.model.HkWorkflowApproverEntity;
import com.argusoft.hkg.model.HkWorkflowEntity;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.core.util.HkUserOperationEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Test class for HkEmployeeService.
 *
 * @author Mital
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkEmployeeServiceImplTest {

    @Resource
    private HkEmployeeService employeeService;
    @Resource
    private HKCategoryService categoryService;
    @Resource
    private HkHRService hrService;
    private HkTaskEntity taskEntity;
    private HkCategoryEntity categoryEntity;
    private final Long assignee1 = 1L;
    private Set<Long> taskRecipientSet = new HashSet<>();

    /*
     Entities
     */
    private HkLeaveEntity leaveEntity;
    private HkTaskRecipientEntity taskRecipient;
    private HkWorkflowEntity workflowEntity;
    private Set<HkWorkflowApproverEntity> workflowApproverSet;

    public HkEmployeeServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        taskRecipientSet.add(assignee1);

        categoryEntity = new HkCategoryEntity();
        categoryEntity.setCategoryPrefix("Tk");
        categoryEntity.setCategoryTitle("Meetings");
        categoryEntity.setCategoryType("Task");
        categoryEntity.setCreatedBy(1L);
        categoryEntity.setCreatedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        categoryEntity.setFranchise(0L);
        categoryEntity.setIsActive(true);
        categoryEntity.setIsArchive(false);
        categoryEntity.setLastModifiedBy(1L);
        categoryEntity.setLastModifiedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

        taskEntity = new HkTaskEntity();
        taskEntity.setCreatedBy(1L);
        taskEntity.setCreatedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        taskEntity.setDueDt(new java.sql.Date(2014 - 1900, 11, 12));
        taskEntity.setFranchise(0L);
        taskEntity.setIsArchive(false);
        taskEntity.setIsRepetative(false);
        taskEntity.setLastModifiedOn(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        taskEntity.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
        taskEntity.setTaskName("Wrap up the event registration today.");
        if (taskEntity.getId() != null) {
            taskRecipient = new HkTaskRecipientEntity(taskEntity.getId(), "E", assignee1);
        } else {
            taskRecipient = new HkTaskRecipientEntity(0l, "E", assignee1);
        }
        List<HkTaskRecipientEntity> taskRecipientList = new ArrayList<>();
        taskRecipient.setIsArchive(false);
        taskRecipientList.add(taskRecipient);
        taskEntity.setHkTaskRecipientEntitySet(new HashSet<>(taskRecipientList));

        //  Set up leave entity object
        leaveEntity = new HkLeaveEntity();
        leaveEntity.setCreatedBy(1L);
        leaveEntity.setCreatedOn(new Date());
        leaveEntity.setDescription("Going on vacation");
        leaveEntity.setForUser(1L);
        leaveEntity.setFranchise(1L);
        leaveEntity.setFrmDt(new Date());
        leaveEntity.setIsArchive(false);
        leaveEntity.setLeaveReason(1L);
        leaveEntity.setStatus(HkSystemConstantUtil.LeaveStatus.PENDING);
        leaveEntity.setToDt(new Date());
        leaveEntity.setTotalDays(1.0f);

        //  Set workflow entity, used for leave
        workflowEntity = new HkWorkflowEntity(null, false, 1L, new Date(), 1L, new Date(), 1L);
        workflowEntity.setLastLevel(1);
        workflowEntity.setDepartment(1L);

        workflowApproverSet = new HashSet<>();
        HkWorkflowApproverEntity approver = new HkWorkflowApproverEntity(0L, HkSystemConstantUtil.RecipientCodeType.DEPARTMENT, 2L);
        approver.setLevel(1);
        approver.setIsArchive(false);
        workflowApproverSet.add(approver);
        workflowEntity.setHkWorkflowApproverEntitySet(workflowApproverSet);
        
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createTask method, of class HkEmployeeService.
     */
    @Test
    public void testCreateTask() {
        categoryService.createCategory(categoryEntity, CategoryType.TASK);
        taskEntity.setTaskCategory(categoryEntity.getId());
        taskEntity.setDueDt(new Date(2014 - 1900, 5, 5));
        taskEntity.setRepeatativeMode(HkSystemConstantUtil.TaskRepetitiveMode.DAILY);
        taskEntity.setEndRepeatMode(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS);
        taskEntity.setAfterUnits(5);
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);
        HkTaskEntity resultTaskEntity = employeeService.retrieveTaskById(taskEntity.getId(), false, false);
        assertEquals(taskEntity, resultTaskEntity);
    }

    /**
     * Test of updateTask method, of class HkEmployeeService.
     */
    @Test
    public void testUpdateTask() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);
        taskEntity.setDueDt(new Date(2014 - 1900, 8, 8));
        taskEntity.setHkTaskRecipientEntitySet(null);
        Set<HkTaskRecipientEntity> newRecipientSet = new HashSet<>();
        newRecipientSet.add(new HkTaskRecipientEntity(taskEntity.getId(), "E", 12L));
        employeeService.updateTask(taskEntity, newRecipientSet, new HashSet<>(Arrays.asList(12L)), null, null,3l);
        HkTaskEntity resultEntity = employeeService.retrieveTaskById(taskEntity.getId(), true, false);
        assertEquals(taskEntity.getDueDt(), resultEntity.getDueDt());
        assertTrue("Task Updated Successfully",resultEntity.getHkTaskRecipientEntitySet().size() > 0);
    }
//
//    /**
//     * Test of removeTask method, of class HkEmployeeService.
//     */

    @Test
    public void testRemoveTask_HkTaskEntity() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        //  Remove it
        taskEntity = employeeService.retrieveTaskById(taskEntity.getId(), true, true);
        employeeService.removeTask(taskEntity, null, null);

        HkTaskEntity resultTask = employeeService.retrieveTaskById(taskEntity.getId(), true, true);
        assertEquals(true, resultTask.getIsArchive());
        for (HkTaskRecipientEntity recipient : resultTask.getHkTaskRecipientEntitySet()) {
            assertEquals("Task Recipients Removed Successfully", true, recipient.getIsArchive());
        }

        for (HkTaskRecipientDtlEntity recipientDtl : resultTask.getHkTaskRecipientDtlEntitySet()) {
            assertEquals("Task Recipients Details Removed Successfully", true, recipientDtl.getIsArchive());
        }
    }
//
//    /**
//     * Test of removeTask method, of class HkEmployeeService.
//     */

    @Test
    public void testRemoveTask_Long() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        //  Remove it
        employeeService.removeTask(taskEntity.getId(), null, null, taskEntity.getFranchise());

        HkTaskEntity resultTask = employeeService.retrieveTaskById(taskEntity.getId(), false, false);
        assertEquals("Task Removed successfully..", true, resultTask.getIsArchive());
    }
//
//    /**
//     * Test of searchTaskNames method, of class HkEmployeeService.
//     */

    @Test
    public void testSearchTaskNames() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<String> resultNames = employeeService.searchTaskNames(taskEntity.getTaskName(), taskEntity.getCreatedBy(), taskEntity.getFranchise());
        assertTrue("Searched successfully..", resultNames.size() > 0);
    }
//
//    /**
//     * Test of retrieveTaskRecipientsByTaskId method, of class
//     * HkEmployeeService.
//     */

    @Test
    public void testRetrieveTaskRecipientsByTaskId() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);
        List<HkTaskRecipientEntity> resultList = employeeService.retrieveTaskRecipientsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientEntity taskRecipient : resultList) {
            assertEquals("Task Retrieved..." + taskEntity.getId(), taskEntity.getId(), (Long) taskRecipient.getHkTaskRecipientEntityPK().getTask());
        }
    }
//
//    /**
//     * Test of retrieveTaskRecipientDtlsByTaskId method, of class
//     * HkEmployeeService.
//     */

    @Test
    public void testRetrieveTaskRecipientDtlsByTaskId() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<HkTaskRecipientDtlEntity> resultList = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientDtlEntity recipientDtl : resultList) {
            assertEquals(taskEntity.getId(), recipientDtl.getTask().getId());
        }
    }
//
//    /**
//     * Test of updateTaskRecipientDtl method, of class HkEmployeeService.
//     */

    @Test
    public void testUpdateTaskRecipientDtl() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<HkTaskRecipientDtlEntity> resultList = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientDtlEntity recipientDtl : resultList) {
            recipientDtl.setCompletedOn(new Date());
            recipientDtl.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED);
            employeeService.updateTaskRecipientDtl(recipientDtl, null, null);
        }
        List<HkTaskRecipientDtlEntity> retrievedTask = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientDtlEntity recipientDtl : retrievedTask) {
            assertEquals(HkSystemConstantUtil.TaskStatus.COMPLETED, recipientDtl.getStatus());
        }

    }
//
//    /**
//     * Test of cancelRepeatedTask method, of class HkEmployeeServiceImpl.
//     */

    @Test
    public void testCancelRepeatedTask() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);
        employeeService.cancelRepeatedTask(taskEntity.getId(), 0, null, null, taskEntity.getFranchise());
        List<HkTaskRecipientDtlEntity> recipientList = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        if (!CollectionUtils.isEmpty(recipientList)) {
            for (HkTaskRecipientDtlEntity recipient : recipientList) {
                if (recipient.getRepetitionCount().equals(0)) {
                    assertTrue(recipient.getStatus().equals(HkSystemConstantUtil.TaskStatus.CANCELLED));
                }
            }
        }
    }
//
//    /**
//     * Test of retrieveTaskById method, of class HkEmployeeService.
//     */

    @Test
    public void testRetrieveTaskById() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        Long taskId = taskEntity.getId();
        HkTaskEntity resultTask = employeeService.retrieveTaskById(taskId, false, false);
        assertEquals(taskEntity, resultTask);
    }
//
//    /**
//     * Test of retrieveTaskRecipientDtlsByCriteria method, of class
//     * HkEmployeeService.
//     */

    @Test
    public void testRetrieveTaskRecipientDtlsByCriteria() {
        Calendar todayCal = Calendar.getInstance();
        taskEntity.setDueDt(new Date(todayCal.getTimeInMillis()));
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        //  For "Due today" list
        Date dueDate = new Date(todayCal.getTimeInMillis());
        Date assignedStartDate = null;
        Date assignedEndDate = null;
        Long assignee = 1L;
        Long assigner = 1L;
        String status = HkSystemConstantUtil.TaskStatus.DUE;
        Long franchise = 0L;
        Boolean archiveStatus = false;

        List<HkTaskRecipientDtlEntity> resultList = employeeService.retrieveTaskRecipientDtlsByCriteria(dueDate, assignedStartDate, assignedEndDate, assignee, assigner, Arrays.asList(status), taskEntity.getTaskCategory(), null, franchise, archiveStatus, null, false);
        for (HkTaskRecipientDtlEntity taskRecipientDtl : resultList) {
            assertEquals(dueDate, taskRecipientDtl.getDueDate());
        }
    }
//
//    /**
//     * Test of retrieveTasksByCriteria method, of class HkEmployeeService.
//     */

    @Test
    public void testRetrieveTasksByCriteria_8args() {
        Calendar todayCal = Calendar.getInstance();
        taskEntity.setDueDt(new Date(todayCal.getTimeInMillis()));
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        //  For "Due today" list
        Date dueDate = new Date(todayCal.getTimeInMillis());
        Date createdStartDate = null;
        Date createdEndDate = null;
        Long assigner = 1L;
        String status = HkSystemConstantUtil.TaskStatus.DUE;
        Long franchise = 0L;
        Boolean archiveStatus = false;

        List<HkTaskEntity> resultList = employeeService.retrieveTasksByCriteria(dueDate, createdStartDate, createdEndDate, assigner, status, taskEntity.getTaskCategory(), franchise, archiveStatus, false, null, false);
        for (HkTaskEntity task : resultList) {
            assertEquals(dueDate, task.getDueDt());
        }
    }
//
//    /**
//     * Test of retrieveTasksByCriteria method, of class HkEmployeeService.
//     */

    @Test
    public void testRetrieveTasksByCriteria_4args() {
        taskEntity.setIsRepetative(true);
        taskEntity.setRepeatativeMode(HkSystemConstantUtil.TaskRepetitiveMode.DAILY);
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        Boolean isRepetitive = true;
        Boolean isCompleted = false;
        Long franchise = 0L;
        Boolean archiveStatus = false;
        List<HkTaskEntity> resultList = employeeService.retrieveTasksByCriteria(isRepetitive, isCompleted, franchise, archiveStatus);
        for (HkTaskEntity task : resultList) {
            assertNotSame(HkSystemConstantUtil.TaskStatus.COMPLETED, task.getStatus());
            assertEquals(isRepetitive, task.getIsRepetative());
        }
    }
//
//    /**
//     * Test of updateTaskInfos method, of class HkEmployeeService.
//     */

    @Test
    public void testUpdateTaskInfos() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<HkTaskEntity> taskListToUpdate = new ArrayList<>();
        taskEntity.setIsRepetative(true);
        taskEntity.setRepeatativeMode(HkSystemConstantUtil.TaskRepetitiveMode.DAILY);
        taskListToUpdate.add(taskEntity);
        employeeService.updateTaskInfos(taskListToUpdate);

        HkTaskEntity resultTask = employeeService.retrieveTaskById(taskEntity.getId(), false, false);
        assertEquals(taskEntity.getRepeatativeMode(), resultTask.getRepeatativeMode());
    }
//
//    /**
//     * Test of updateTaskRecipientDtlInfos method, of class HkEmployeeService.
//     */

    @Test
    public void testUpdateTaskRecipientDtlInfos() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<HkTaskRecipientDtlEntity> taskRecipientDtlListToUpdate = new ArrayList<>();
        List<HkTaskRecipientDtlEntity> resultList = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientDtlEntity recipientDtl : resultList) {
            recipientDtl.setCompletedOn(new Date());
            recipientDtl.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED);
            employeeService.updateTaskRecipientDtl(recipientDtl, null, null);
            taskRecipientDtlListToUpdate.add(recipientDtl);
        }
        employeeService.updateTaskRecipientDtlInfos(taskRecipientDtlListToUpdate);

        List<HkTaskRecipientDtlEntity> retrievedTask = employeeService.retrieveTaskRecipientDtlsByTaskId(taskEntity.getId(), false);
        for (HkTaskRecipientDtlEntity recipientDtl : retrievedTask) {
            assertEquals(HkSystemConstantUtil.TaskStatus.COMPLETED, recipientDtl.getStatus());
        }

    }
//
//    /**
//     * Test of retrievePendingTaskCountGroupedByCategory method, of class
//     * HkEmployeeService.
//     */
    @Test
    public void testRetrievePendingTaskCountGroupedByCategory() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        Map<Long, Integer> resultMap = employeeService.retrievePendingTaskCountGroupedByCategory(assignee1, 0L, Boolean.FALSE);
        for (Long cateogryId : resultMap.keySet()) {
            assertTrue(resultMap.get(cateogryId) >= 1);
        }
    }

    /**
     * Test of retrieveTaskAssignerIds method, of class HkEmployeeService.
     */
    @Test
    public void testRetrieveTaskAssignerIds() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);
        Set<Long> assignerIds = new HashSet<>();
        assignerIds.add(taskEntity.getCreatedBy());
        List<Long> resultIds = employeeService.retrieveTaskAssignerIds(assignee1, 0L, Boolean.FALSE);
        assertTrue(assignerIds.size() <= resultIds.size());
    }

    /**
     * Test of applyLeave method, of class HkEmployeeService.
     */
    @Test
    public void testApplyLeave() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, 1L, null, null, null, null);
        HkLeaveEntity resultLeaveEntity = employeeService.retrieveLeaveById(leaveEntity.getId(), false);
        assertEquals(leaveEntity, resultLeaveEntity);
    }

    /**
     * Test of updateLeave method, of class HkEmployeeService.
     */
    @Test
    public void testUpdateLeave() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        leaveEntity.setFinalRemarks("Respond leave request");
        employeeService.updateLeave(leaveEntity, null, null, null, null);

        HkLeaveEntity resultLeaveEntity = employeeService.retrieveLeaveById(leaveEntity.getId(), false);
        assertEquals(HkSystemConstantUtil.LeaveStatus.PENDING, resultLeaveEntity.getStatus());
    }

    /**
     * Test of isUserOnLeave method, of class HkEmployeeService.
     */
    @Test
    public void testIsUserOnLeave() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        Long userId = leaveEntity.getForUser();
        Date fromDate = leaveEntity.getFrmDt();
        Date toDate = leaveEntity.getToDt();
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
        statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
        Long franchise = 1L;
        List<Long> resultIdList = employeeService.isUserOnLeave(userId, fromDate, toDate, statusList, franchise);
        assertTrue(resultIdList.size() > 0);
    }

    /**
     * Test of retrieveLeavesByCriteria method, of class HkEmployeeService.
     */
    @Test
    public void testRetrieveLeavesByCriteria() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        Long userId = leaveEntity.getForUser();
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.LeaveStatus.PENDING);
        statusList.add(HkSystemConstantUtil.LeaveStatus.APPROVED);
        Long franchise = 1L;
        List<HkLeaveEntity> resultList = employeeService.retrieveLeavesByCriteria(userId, statusList, franchise, false);
        assertTrue(resultList.size() > 0);
        for (HkLeaveEntity leave : resultList) {
            assertTrue(leave.getStatus().equals(HkSystemConstantUtil.LeaveStatus.PENDING) || leave.getStatus().equals(HkSystemConstantUtil.LeaveStatus.APPROVED));
            assertEquals(userId.longValue(), leave.getForUser());
    }
    }

    /**
     * Test of retrieveLeaveApprovalEntitiesByCriteria method, of class
     * HkEmployeeService.
     */
    @Test
    public void testRetrieveLeaveApprovalEntitiesByCriteria() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        List<HkLeaveApprovalEntity> resultList = employeeService.retrieveLeaveApprovalEntitiesByCriteria(leaveEntity.getId(), null, Arrays.asList(HkSystemConstantUtil.LeaveStatus.PENDING), workflowEntity.getDepartment(), 1L);
        assertTrue(resultList.size() > 0);
        for (HkLeaveApprovalEntity leaveApproval : resultList) {
            assertEquals(HkSystemConstantUtil.LeaveStatus.PENDING, leaveApproval.getStatus());
    }
    }

    /**
     * Test of respondLeave method, of class HkEmployeeService.
     */
    @Test
    public void testRespondLeave() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        Long responseBy = 3L;
        String remarks = "Okay";
        String status = HkSystemConstantUtil.LeaveStatus.APPROVED;
        employeeService.respondLeave(leaveEntity.getId(), responseBy, remarks, status, null, null, null, null);

        List<HkLeaveApprovalEntity> resultList = employeeService.retrieveLeaveApprovalEntitiesByCriteria(leaveEntity.getId(), null, Arrays.asList(status), null, null);
        assertTrue(resultList.size() > 0);
        for (HkLeaveApprovalEntity leaveApproval : resultList) {
            assertEquals(status, leaveApproval.getStatus());
        }
    }

    /**
     * Test of searchMyLeaves method, of class HkEmployeeService.
     */
    @Test
    public void testSearchMyLeaves() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        List<HkLeaveEntity> resultLeaves = employeeService.searchMyLeaves(Integer.toString(leaveEntity.getToDt().getDate()), leaveEntity.getForUser(), leaveEntity.getFranchise());
        assertTrue(resultLeaves.size() > 0);
    }

    /**
     * Test of searchLeavesForApprover method, of class HkEmployeeServiceImpl.
     */
    @Test
    public void testSearchLeavesForApprover() {
        hrService.createWorkflow(workflowEntity, null, null);
        employeeService.applyLeave(leaveEntity, workflowEntity.getDepartment(), null, null, null, null);

        List<HkLeaveApprovalEntity> resultLeaves = employeeService.searchLeavesForApprover("", null, 2l, null, leaveEntity.getFranchise());
        assertTrue(resultLeaves.size() > 0);
    }

    /**
     * Test of searchTasks method, of class HkEmployeeServiceImpl.
     */
    @Test
    public void testSearchTasks() {
        employeeService.createTask(taskEntity, taskRecipientSet, null, null,3l);

        List<HkTaskEntity> result = employeeService.searchTasks(taskEntity.getTaskName(), null, null, null, taskEntity.getCreatedBy(), taskEntity.getFranchise());
        assertTrue(result.size() > 0);
    }

//    @Test
    public void createUserOperation() {
        employeeService.createUserOperation(HkUserOperationEnum.LOGIN, new Date(), 1l, null, 1l, 1l);
    }

//    @Test
    public void retrieveUserOperation() {
        Date onDate = new Date();
        employeeService.createUserOperation(HkUserOperationEnum.LOGIN, onDate, 1l, null, 1l, 1l);
        List<HkUserOperationEntity> retrieveUserOperations = employeeService.retrieveUserOperations(onDate, onDate, 1l, 1l);
    }

    //@Test
    public void updateUserOperation() {
//    public void updateUserOperation(HkUserOperationEnum oldUserOperation, Date oldOnTime, HkUserOperationEnum newUserOperation, Date newOnTime, Long userId, String comments, Long modifiedByUser, Long franchise);
        Date onDate = new Date();
        employeeService.createUserOperation(HkUserOperationEnum.LOGIN, onDate, 1l, null, 1l, 1l);
        List<HkUserOperationEntity> retrieveUserOperations = employeeService.retrieveUserOperations(onDate, onDate, 1l, 1l);
        HkUserOperationEntity hkUserOperationEntity = retrieveUserOperations.get(0);
        employeeService.updateUserOperation(HkUserOperationEnum.LOGIN, hkUserOperationEntity.getOnTime(), HkUserOperationEnum.LOGOUT, onDate, 1l, "new updated", 1l, 1l);

    }

}
