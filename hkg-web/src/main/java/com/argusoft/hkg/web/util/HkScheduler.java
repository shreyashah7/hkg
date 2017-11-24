/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import com.argusoft.email.common.core.EmailService;
import com.argusoft.email.common.core.serviceimpl.EmailServiceImpl;
import com.argusoft.email.common.model.Attachment;
import com.argusoft.email.common.model.Email;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkEventEntity;
import com.argusoft.hkg.model.HkEventRecipientEntity;
import com.argusoft.hkg.model.HkEventRegistrationEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkShiftDtlEntity;
import com.argusoft.hkg.model.HkShiftEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkTaskEntity;
import com.argusoft.hkg.model.HkTaskRecipientDtlEntity;
import com.argusoft.hkg.model.HkTaskRecipientEntity;
import com.argusoft.hkg.model.HkUserWorkHistoryEntity;
import com.argusoft.hkg.nosql.core.HkGoalService;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.hkg.web.reportbuilder.transformers.SchedulerReportTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.FranchiseTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.reportbuilder.core.RbReportService;
import com.argusoft.reportbuilder.core.bean.MasterReportDataBean;
import com.argusoft.reportbuilder.model.RbEmailReportConfigurationEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntity;
import com.argusoft.reportbuilder.model.RbEmailReportStatusEntityPK;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserRole;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * This class is used to write the business logic for the scheduled tasks.
 *
 * @author Mital
 */
@Component
public class HkScheduler {

    @Autowired
    private HkEmployeeService employeeService;
    @Autowired
    private HkHRService hrService;
    @Autowired
    private UserManagementServiceWrapper umServiceWrapper;
    @Autowired
    private HkFoundationService foundationService;
    @Autowired
    UMDepartmentService departmentService;
    @Autowired
    UMUserService userService;
    @Autowired
    private HkGoalService goalService;
    @Autowired
    private HkNotificationService notificationService;
    @Autowired
    EmailService emailService;
    @Autowired
    private RbReportService reportService;

    @Autowired
    FranchiseTransformerBean franchiseTransformerBean;
    @Autowired
    SchedulerReportTransformerBean schedulerReportTransformer;

    private static final Long FRANCHISE = 0L;
    private static Calendar lastRunCal;

    /**
     * Run the scheduler everyday at 00:00
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 24 * 60000 * 60)
    public synchronized void scheduledTasks() {
        System.out.println("--------Scheduler starts-----------");
        lastRunCal = Calendar.getInstance();
        //  Get the last run date for this schedular
        HkSystemConfigurationEntity schedulerConfig = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.SCHEDULER_LAST_RUN, FRANCHISE);
        if (schedulerConfig == null) {
            //  if no value found with given key, save it now
            schedulerConfig = new HkSystemConfigurationEntity(HkSystemConstantUtil.FranchiseConfiguration.SCHEDULER_LAST_RUN, FRANCHISE);
            schedulerConfig.setIsArchive(false);
            //  Save the time, modifiedBy field can also be used to get the last run time of scheduler
            schedulerConfig.setKeyValue(Long.toString(lastRunCal.getTimeInMillis()));
            schedulerConfig.setModifiedBy(0);
            schedulerConfig.setModifiedOn(lastRunCal.getTime());
            foundationService.saveSystemConfigurations(Arrays.asList(schedulerConfig));

//        lastRunCal.setTimeInMillis(new Date(2014 - 1900, 7, 13).getTime());
            lastRunCal.add(Calendar.DATE, -1);
        } else {
            lastRunCal.setTime(schedulerConfig.getModifiedOn());
        }
        lastRunCal.set(Calendar.HOUR_OF_DAY, 0);
        lastRunCal.set(Calendar.MINUTE, 0);
        lastRunCal.set(Calendar.SECOND, 0);
        lastRunCal.set(Calendar.MILLISECOND, 0);

        try {
            this.generateRepetitiveTask();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.archiveTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.deactivateRelievedUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.updateEventStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.inactiveTemporaryShift();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.automaticShiftRotationOfEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        schedulerConfig.setModifiedOn(new Date());
        foundationService.saveSystemConfigurations(Arrays.asList(schedulerConfig));
        System.out.println("--------Scheduler ends-----------");
    }

    /**
     * This method generates the repetitive tasks as per their given schedule.
     *
     */
    public void generateRepetitiveTask() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        //  if the last run date was today, return the statement because for the
        //  new tasks that are generated today, no repetitive task needs to be
        //  generated as one will already be there for today and for the old 
        //  tasks, repetitive tasks are already generated.
        if (lastRunCal.equals(todayCal)) {
            return;
        }

        //  The list of tasks that needs to be updated, thus avoiding the update call in for loop from here
        List<HkTaskEntity> taskListToUpdate = new LinkedList<>();
        List<HkTaskRecipientDtlEntity> taskRecipientDtlListToUpdate = new LinkedList<>();

        //  Let's retrieve all the tasks first
        List<HkTaskEntity> allTaskList = employeeService.retrieveTasksByCriteria(true, null, null, Boolean.FALSE);
//        System.out.println("allTaskList :" + allTaskList);
//        System.out.println("================== all tasks are " + allTaskList);
        for (HkTaskEntity task : allTaskList) {
            //  Retrieve last task generation date
            Calendar taskGenCal = Calendar.getInstance();
            taskGenCal.setTimeInMillis(lastRunCal.getTimeInMillis());
            //  Add one day in last run schedular and get new date to generate the task
            taskGenCal.add(Calendar.DATE, 1);   //  Now we'll generate tasks for this date

//            System.out.println("task.getDueDt() is " + task.getDueDt());
//            System.out.println("taskgencal is " + taskGenCal.getTime());
            //  run the loop for all the days, starting from the last run date till today's date
            while (taskGenCal.before(todayCal) || taskGenCal.equals(todayCal)) {
//                System.out.println("::::::::::::: task is " + task);

                //  if it's not a holiday today, and the main task due date is gone, tasks will be generated
//                System.out.println("------------ hrService.isHoliday(new Date(taskGenCal.getTimeInMillis()), franchise) " + hrService.isHoliday(new Date(taskGenCal.getTimeInMillis()), franchise));
//                System.out.println("=========== task.getDueDt().before(taskGenCal.getTime()) " + task.getDueDt().before(taskGenCal.getTime()));
                if (!hrService.isHoliday(new Date(taskGenCal.getTimeInMillis()), task.getFranchise())
                        && task.getDueDt().before(taskGenCal.getTime())) {

                    //  Check if the task should be generated today or not
                    Date dueDateForTask = this.checkTaskGeneration(task, taskGenCal);
//                    System.out.println("due date we got is " + dueDateForTask);

                    //  The function is written such that it'll return the due date if task needs to be generated, otherwise it'll return null
                    if (dueDateForTask != null) {
                        //  Now task can be generated.......
                        //  if task repetitive mode is after some iterations, add 1
                        Integer taskRepetitionCount = null;
//                        if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                        taskRepetitionCount = task.getRepetitionCount() == null ? 0 : task.getRepetitionCount();
                        task.setRepetitionCount(++taskRepetitionCount);
                        System.out.println("++++++++++++++ nw rep cnt is " + task.getRepetitionCount());
                        taskListToUpdate.add(task);
//                        }

                        //  Retrieve all users first
                        List<HkTaskRecipientEntity> recipientList = employeeService.retrieveTaskRecipientsByTaskId(task.getId(), false);
                        if (!CollectionUtils.isEmpty(recipientList)) {
                            List<String> recipientCodes = new ArrayList<>();
                            for (HkTaskRecipientEntity recipient : recipientList) {
                                recipientCodes.add(recipient.getHkTaskRecipientEntityPK().getReferenceInstance() + ":" + recipient.getHkTaskRecipientEntityPK().getReferenceType());
                            }
                            Set<Long> recipientUserIds = umServiceWrapper.retrieveRecipientIds(recipientCodes);

                            if (!CollectionUtils.isEmpty(recipientUserIds)) {
                                for (Long userId : recipientUserIds) {
                                    List<Long> userOnLeaveList = employeeService.isUserOnLeave(userId, taskGenCal.getTime(), taskGenCal.getTime(), Arrays.asList(HkSystemConstantUtil.LeaveStatus.APPROVED), task.getFranchise());
                                    //  if user on leave list is got null, means user is not on leave
                                    if (CollectionUtils.isEmpty(userOnLeaveList)) {
                                        HkTaskRecipientDtlEntity taskRecipientDtlObj = new HkTaskRecipientDtlEntity();
                                        taskRecipientDtlObj.setIsArchive(false);
                                        taskRecipientDtlObj.setOnDate(taskGenCal.getTime());
                                        taskRecipientDtlObj.setStatus(HkSystemConstantUtil.TaskStatus.DUE);
                                        taskRecipientDtlObj.setTask(task);
                                        taskRecipientDtlObj.setUserId(userId);
                                        //  Set the due date which was found earlier in other function
                                        taskRecipientDtlObj.setDueDate(dueDateForTask);
//                                    if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                                        taskRecipientDtlObj.setRepetitionCount(taskRepetitionCount);
//                                    }
                                        taskRecipientDtlListToUpdate.add(taskRecipientDtlObj);
                                    }
                                }
                            }
                        }
//                        //  if task repetitive mode is after some iterations, add 1
//                        if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
//                            Integer taskRepetitionCount = task.getRepetitionCount() == null ? 0 : task.getRepetitionCount();
//                            task.setRepetitionCount(taskRepetitionCount + 1);
//                            System.out.println("++++++++++++++ nw rep cnt is " + task.getRepetitionCount());
//                            taskListToUpdate.add(task);
//                        }
                    }
                }
                //  Add one day in last run schedular and get new date to generate the task
                taskGenCal.add(Calendar.DATE, 1);   //  Now we'll generate tasks for this date
            }
        }

        //  Update task entities
        employeeService.updateTaskInfos(taskListToUpdate);
        employeeService.updateTaskRecipientDtlInfos(taskRecipientDtlListToUpdate);

        //  update the last task generation date in database *************** remaining
    }

    /**
     * This method checks if the task needs to be generated to the given date.
     * If yes, it'll return the due date for the task else it'll return null.
     *
     * @param task The task object for which the task generation needs to be
     * checked.
     * @param taskGenCal The date on which the checking needs to be done whether
     * the task should be generated or not.
     * @return Returns the due date of the task if it should be generated on
     * given date, returns null if the task should not be generated on given
     * date.
     */
    private Date checkTaskGeneration(HkTaskEntity task, Calendar taskGenCal) {
        System.out.println("task :" + task);
        boolean result = false;
        Calendar dueDate = Calendar.getInstance();
        //  Set the duedate to current date first
        dueDate.setTimeInMillis(taskGenCal.getTimeInMillis());
        System.out.println("taskGenCal :" + taskGenCal.getTime());
        //  if repeatation mode is after x repetitions and if the after units are over i.e. all the repetations are generated,
        //  if rep mode is after x days and if 
        //  if rep mode is on date and if the date is gone,
        //  return null
//        System.out.println("task.getAfterUnits() is " + task.getAfterUnits());
//        System.out.println("task.getRepetitionCount() is " + task.getRepetitionCount());
        //  Task should not be weekly, and if end repeat mode is after x reps and after units is <= actual rep count
        if ((!task.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)
                && task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                && task.getAfterUnits() <= task.getRepetitionCount())
                || (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)
                && task.getEndDate().before(taskGenCal.getTime()))) {
            return null;
        }
        if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
            int passedDays = (int) ((taskGenCal.getTime().getTime() - task.getCreatedOn().getTime()) / (1000 * 60 * 60 * 24));
//            System.out.println("passeddays are " + passedDays);
            if (passedDays >= task.getAfterUnits()) {
                return null;
            }
        }
        if (task.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.DAILY)) {
            result = true;
            //  Due date would be same for all the daily tasks
            dueDate.setTimeInMillis(taskGenCal.getTimeInMillis());
            System.out.println("we found daily................");

        } else if (task.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
            String[] onDays = task.getWeeklyOnDays().split("\\" + HkSystemConstantUtil.SEPARATOR_PI);
            System.out.println("onDays :" + onDays.toString());
            int countCheck = 0;
            if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                Date lstModifiedOn = task.getLastModifiedOn();
                Calendar calTemp = Calendar.getInstance();
                calTemp.setTime(lstModifiedOn);
                calTemp.set(Calendar.HOUR_OF_DAY, 0);
                calTemp.set(Calendar.MINUTE, 0);
                calTemp.set(Calendar.SECOND, 0);
                calTemp.set(Calendar.MILLISECOND, 0);
                if (calTemp.getTime().equals(task.getDueDt())) {
//                    System.out.println("start and due date equal----");
//                    System.out.println("createdOn :::" + lstModifiedOn);
                    Calendar c = Calendar.getInstance();
                    c.setTime(lstModifiedOn);
                    Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//                    System.out.println("dayOfWeek :::" + dayOfWeek);
                    int checkCountOfWeek = 0;
                    for (int count = 0; count < onDays.length; count++) {
                        if (onDays[count].equals(dayOfWeek.toString())) {
                            checkCountOfWeek++;
                        }
                    }
//                    System.out.println("checkCountOfWeek : " + checkCountOfWeek);
                    if (checkCountOfWeek > 0) {
                        countCheck = (task.getAfterUnits() * onDays.length) - 1;
                    } else {
                        countCheck = (task.getAfterUnits() * onDays.length);
                    }
                } else if (calTemp.getTime().before(task.getDueDt())) {
//                    System.out.println("calTemp.getTime() :"+calTemp.getTime());
//                    System.out.println("task.getDueDt() in else if :"+task.getDueDt());
                    int longDueDateDiff = 0;
                    Calendar calObj = Calendar.getInstance();
                    calObj.setTime(lstModifiedOn);
                    int countVarObj = 0;
                    while (calObj.getTime().before(task.getDueDt()) || calObj.getTime().equals(task.getDueDt())) {
                        if (countVarObj != 0) {
                            calObj.add(Calendar.DATE, 1);
                        }
                        countVarObj++;
//                        System.out.println("calObj :"+calObj.getTime());
                        Integer dayOfWeek = calObj.get(Calendar.DAY_OF_WEEK);
//                        System.out.println("dayOfWeek in while :::" + dayOfWeek);
                        for (int count = 0; count < onDays.length; count++) {
                            if (onDays[count].equals(dayOfWeek.toString())) {
                                longDueDateDiff++;
                            }
                        }
                    }
//                    System.out.println("longDueDateDiff :"+longDueDateDiff);
                    if (longDueDateDiff > 0) {
                        countCheck = (task.getAfterUnits() * onDays.length) - longDueDateDiff;
                    } else {
                        countCheck = (task.getAfterUnits() * onDays.length);
                    }

                } else {
                    countCheck = (task.getAfterUnits() * onDays.length);
                }
            }
            System.out.println("countCheck : " + countCheck);
            if (task.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                    && (countCheck) <= task.getRepetitionCount()) {
                //  Return null if,
                //  task repeat mode is after x repetitions and,
                //  if task after units * total days is less or equal to the repetition count that is done
                //  e.g. after units = 3, no. of days selected = 3 so, (3*3 = 9) <= 9 then return null, no more repetitions needed
                return null;
            }
            for (int count = 0; count < onDays.length; count++) {
                if (onDays[count].equals(Integer.toString(taskGenCal.get(Calendar.DAY_OF_WEEK)))) {
                    int diff = 0;
                    if (count + 1 == onDays.length) {
                        diff = 7 - Integer.valueOf(onDays[count]) + Integer.valueOf(onDays[0]) - 1;
                        if (diff >= 7) {
                            diff = diff - 7;
                        }
                    } else {
                        diff = Integer.valueOf(onDays[count + 1]) - Integer.valueOf(onDays[count]) - 1;
                        if (diff < 0) {
                            diff = 7 + diff;
                        }
                    }
                    dueDate.add(Calendar.DATE, diff);
                    System.out.println("dueDate :" + dueDate.getTime());
                    result = true;
                    break;
                }
            }
            //  logic: e.g.- weekdays = {4,7}, onDate=25/2/2014 i.e. Thursday (Day=4) today
            //  so as per (after) this loop, nextDay = 6
            //  previousDay = 4
            //  so days to be added = 6-4=2
            //  thus, due date set would be, 25+2 = 27/2/2014 i.e. Saturday (Day=7)

        } else if (task.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
            Integer monthDate = task.getMonthlyOnDay();
            if (taskGenCal.get(Calendar.DATE) == monthDate) {
                //  if task gen date is same as the given month date, result true
                result = true;
            }
//            else if (monthDate == 31 //  if given date is 31 and today's date is 30 and month is 4,6,9,11 then result = true
//                    && (taskGenCal.get(Calendar.DATE) == 30)
//                    && (taskGenCal.get(Calendar.MONTH) == Calendar.APRIL
//                    || taskGenCal.get(Calendar.MONTH) == Calendar.JUNE
//                    || taskGenCal.get(Calendar.MONTH) == Calendar.SEPTEMBER
//                    || taskGenCal.get(Calendar.MONTH) == Calendar.NOVEMBER)) {
//                result = true;
//            } else if (taskGenCal.get(Calendar.MONTH) == Calendar.FEBRUARY //  if today's month is february
//                    && monthDate > 28 && taskGenCal.get(Calendar.DATE) >= 28) {
//                if (taskGenCal.get(Calendar.YEAR) % 4 != 0 //  if it's not a leap year and date is 28, result = true where given month date > 28
//                        || (taskGenCal.get(Calendar.YEAR) % 4 == 0 && taskGenCal.get(Calendar.DATE) == 29)) {   //  if leap year, and date 29, and monthdate > 28, result = true
//                    result = true;
//                }
//            }
            //  java will handle, so just add one month and subtract one day from it
            if (result) {
                dueDate.add(Calendar.MONTH, 1);
                System.out.println("dueDate before after adding month :" + dueDate.getTime());
                if (monthDate == 31) {
                    if (dueDate.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                        dueDate.add(Calendar.MONTH, 1);
                        dueDate.set(Calendar.DATE, 30);
                        System.out.println("dueDate after adding date & after adding month :" + dueDate.getTime());
                    } else if (dueDate.get(Calendar.MONTH) == Calendar.APRIL || dueDate.get(Calendar.MONTH) == Calendar.JUNE
                            || dueDate.get(Calendar.MONTH) == Calendar.SEPTEMBER
                            || dueDate.get(Calendar.MONTH) == Calendar.NOVEMBER) {
                        dueDate.add(Calendar.MONTH, 1);
                        dueDate.set(Calendar.DATE, 30);
                    }
                } else if (monthDate == 30) {
                    if (dueDate.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                        dueDate.add(Calendar.MONTH, 1);
                        dueDate.set(Calendar.DATE, 29);
                    }
                } else if ((dueDate.get(Calendar.YEAR) % 4 != 0) && monthDate == 29) {
                    if (dueDate.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                        dueDate.add(Calendar.MONTH, 1);
                        dueDate.set(Calendar.DATE, 28);
                    }
                } else {
                    dueDate.add(Calendar.DATE, -1);
                }

            }
        }

        System.out.println("===============result is " + result);
        if (result) {
            return dueDate.getTime();
        }

        return null;
    }

    /**
     * FS View Tasks: The completed and cancelled tasks will be archived
     * automatically after n days. (where n will be configured by the admin)
     */
    public void archiveTasks() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        //  if the last run date was today, return the statement because for the
        //  new tasks that are generated today, no repetitive task needs to be
        //  generated as one will already be there for today and for the old 
        //  tasks, repetitive tasks are already generated.
        if (lastRunCal.equals(todayCal)) {
            return;
        }

        Integer taskArchiveDays = 60;
        //  Retrieve the no. of days to archive the completed tasks
        HkSystemConfigurationEntity sysConfig = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.TASK_ARCHIVE_PERIOD, FRANCHISE);
        if (sysConfig != null) {
            taskArchiveDays = sysConfig.getKeyValue() == null ? taskArchiveDays : Integer.parseInt(sysConfig.getKeyValue());
        } else {
            //  if no value found with given key, save it now
            sysConfig = new HkSystemConfigurationEntity(HkSystemConstantUtil.FranchiseConfiguration.TASK_ARCHIVE_PERIOD, FRANCHISE);
            sysConfig.setIsArchive(false);
            sysConfig.setKeyValue(taskArchiveDays.toString());
            sysConfig.setModifiedBy(0);
            sysConfig.setModifiedOn(new Date());
            foundationService.saveSystemConfigurations(Arrays.asList(sysConfig));
        }
        //  Retrieve all the configuration of archive period key for all franchise.
        List<HkSystemConfigurationEntity> hkSystemConfigurationEntitys = foundationService.retrieveAllSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.TASK_ARCHIVE_PERIOD);
        Map<Long, String> franchiseArchivePeriodMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(hkSystemConfigurationEntitys)) {
            for (HkSystemConfigurationEntity hkSystemConfigurationEntity : hkSystemConfigurationEntitys) {
                franchiseArchivePeriodMap.put(hkSystemConfigurationEntity.getHkSystemConfigurationEntityPK().getFranchise(), hkSystemConfigurationEntity.getKeyValue());
            }
        }
        //  Retrieve all the completed and cancelled tasks
        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.TaskStatus.CANCELLED);
        statusList.add(HkSystemConstantUtil.TaskStatus.COMPLETED);
        List<HkTaskEntity> hkTaskEntitys = employeeService.retrieveTasksByStatuses(statusList, null, Boolean.FALSE);
        Iterator<HkTaskEntity> itr = hkTaskEntitys.iterator();
        while (itr.hasNext()) {
            HkTaskEntity hkTaskEntity = itr.next();
            if (hkTaskEntity.getEndDate() != null) {
//                if (hkTaskEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.COMPLETED)) {
                Boolean dueTaskFlag = false;
                if (!CollectionUtils.isEmpty(hkTaskEntity.getHkTaskRecipientDtlEntitySet())) {
                    for (HkTaskRecipientDtlEntity recipientDtlEntity : hkTaskEntity.getHkTaskRecipientDtlEntitySet()) {
                        if (recipientDtlEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.DUE)) {
                            dueTaskFlag = true;
                            break;
                        }
                    }
                    if (dueTaskFlag) {
                        itr.remove();
                    }
                }
//                }
            }
        }
        //Archive the tasks passed by archive period of that specific franchise.
        for (Map.Entry<Long, String> entry : franchiseArchivePeriodMap.entrySet()) {
            Long franchise = entry.getKey();
            String archivePeriod = entry.getValue();
            for (HkTaskEntity hkTaskEntity : hkTaskEntitys) {
                if (hkTaskEntity.getFranchise() == franchise) {
                    if (hkTaskEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.COMPLETED)) {
                        if (hkTaskEntity.getActualEndDate() != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(hkTaskEntity.getActualEndDate());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            Date actualEndate = cal.getTime();
                            long diff = todayCal.getTime().getTime() - actualEndate.getTime();
                            long diffDays = diff / (24 * 60 * 60 * 1000);
                            if (diffDays > Long.parseLong(archivePeriod)) {
                                hkTaskEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED);
                                if (!CollectionUtils.isEmpty(hkTaskEntity.getHkTaskRecipientDtlEntitySet())) {
                                    for (HkTaskRecipientDtlEntity recipientDtlEntity : hkTaskEntity.getHkTaskRecipientDtlEntitySet()) {
                                        recipientDtlEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED);
                                    }
                                }
                            }
                        }
                    } else if (hkTaskEntity.getStatus().equals(HkSystemConstantUtil.TaskStatus.CANCELLED)) {
                        if (hkTaskEntity.getLastModifiedOn() != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(hkTaskEntity.getLastModifiedOn());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            Date actualEndate = cal.getTime();
                            long diff = todayCal.getTime().getTime() - actualEndate.getTime();
                            long diffDays = diff / (24 * 60 * 60 * 1000);
                            if (diffDays > Long.parseLong(archivePeriod)) {
                                hkTaskEntity.setStatus(HkSystemConstantUtil.TaskStatus.CANCELLED_ARCHIVED);
                                if (!CollectionUtils.isEmpty(hkTaskEntity.getHkTaskRecipientDtlEntitySet())) {
                                    for (HkTaskRecipientDtlEntity recipientDtlEntity : hkTaskEntity.getHkTaskRecipientDtlEntitySet()) {
                                        recipientDtlEntity.setStatus(HkSystemConstantUtil.TaskStatus.COMPLETED_ARCHIVED);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            employeeService.updateTaskInfos(hkTaskEntitys);

        }

    }

    /**
     * This method will change the status of the events. For e.g. Created =>
     * Upcoming, Upcoming => Active, Active => Completed.
     */
    public void updateEventStatus() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR, 0);
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        todayCal.set(Calendar.MINUTE, 0);

        List<String> statusList = new ArrayList<>();
        statusList.add(HkSystemConstantUtil.EventStatus.ONGOING);
        statusList.add(HkSystemConstantUtil.EventStatus.CREATED);
        statusList.add(HkSystemConstantUtil.EventStatus.UPCOMING);
        List<HkEventEntity> eventList = hrService.retrieveEventsByCriteria(statusList, null, Boolean.FALSE, true, false, true);
        if (!CollectionUtils.isEmpty(eventList)) {
            for (HkEventEntity event : eventList) {
                boolean sendNotification = false;
                boolean ignoreRegisteredUsers = false;
//                System.out.println("todayCal.getTime()---"+todayCal.getTime());
                //  if event's end date is before today's date, means the event is completed
                if (event.getToDt().before(todayCal.getTime())) {
                    event.setStatus(HkSystemConstantUtil.EventStatus.COMPLETED);
                } else if ((event.getFrmDt().before(todayCal.getTime()) || event.getFrmDt().getTime() == (todayCal.getTime().getTime()))
                        && (event.getToDt().after(todayCal.getTime()) || event.getToDt().getTime() == (todayCal.getTime().getTime()))) {
                    //  if today's date is between event from date and event to date, means it's active event
                    //  and if the event's status is not active
                    event.setStatus(HkSystemConstantUtil.EventStatus.ONGOING);
                } else if ((event.getPublishedOn().getTime() == (todayCal.getTime().getTime()) || event.getPublishedOn().before(todayCal.getTime()))) {
                    //  if publish date is today's date or if it's already passed, and if it's status is not upcoming,
                    //  change status to upcoming
                    event.setStatus(HkSystemConstantUtil.EventStatus.UPCOMING);
                    sendNotification = true;
                }

                //For online registration, Notification is sent to Invitees who have not filled Registration forms before 2 days.
                if (event.getRegistrationType() != null && event.getRegistrationType().equalsIgnoreCase(HkSystemConstantUtil.EventRegistrationStatus.ONLINE)
                        && (event.getRegistrationReminderSent() == null || !event.getRegistrationReminderSent()) && event.getRegistrationLastDt() != null) {
                    DateTime startDate = new DateTime(event.getRegistrationLastDt());
                    DateTime endDate = new DateTime();

                    Days daysDiff = Days.daysBetween(startDate, endDate);
                    if (daysDiff.getDays() < 3) {
                        sendNotification = true;
                        ignoreRegisteredUsers = true;
                        event.setRegistrationReminderSent(Boolean.TRUE);
                    }
                }

                Set<Long> notifyUsers = null;
                if (sendNotification && CollectionUtils.isEmpty(event.getHkEventRecipientEntitySet())) {
                    List<String> recepientCodes = new LinkedList<>();
                    final String SEPARATOR = ":";
                    for (HkEventRecipientEntity eventRecipientEntity : event.getHkEventRecipientEntitySet()) {
                        if (!eventRecipientEntity.getIsArchive()) {
                            StringBuilder recepientCode = new StringBuilder();
                            recepientCode.append(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceInstance());
                            recepientCode.append(SEPARATOR);
                            recepientCode.append(eventRecipientEntity.getHkEventRecipientEntityPK().getReferenceType());
                            recepientCodes.add(recepientCode.toString());
                        }
                    }

                    notifyUsers = umServiceWrapper.retrieveRecipientIds(recepientCodes);

                    if (ignoreRegisteredUsers && !CollectionUtils.isEmpty(event.getHkEventRegistrationEntitySet())) {
                        for (HkEventRegistrationEntity eventRegistrationEntity : event.getHkEventRegistrationEntitySet()) {
                            if (!eventRegistrationEntity.getIsArchive()) {
                                notifyUsers.remove(eventRegistrationEntity.getHkEventRegistrationEntityPK().getUserId());
                            }
                        }
                    }
                }
                System.out.println("event :" + event);
                hrService.updateEvents(Arrays.asList(event), notifyUsers);
            }

        }
    }

    /**
     * This method will archive the users whose relieve date is passed.
     */
    public void deactivateRelievedUsers() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        umServiceWrapper.relieveResignedUsers(todayCal.getTime());
//        System.out.println("total users deactivated: " + deactivatedUsers);
    }

//    @Scheduled(fixedRate = 8 * 60 * 60000)
    public void caculateAttandance() {
        employeeService.calculateUserAttendance();
    }

    public void inactiveTemporaryShift() throws GenericDatabaseException {

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        List<HkShiftEntity> inactivedShifts = hrService.inactiveTemporaryShift(todayCal.getTime());

        if (!CollectionUtils.isEmpty(inactivedShifts)) {
            for (HkShiftEntity hkShiftEntity : inactivedShifts) {

                Set<Long> searchUsersByFeatureName = umServiceWrapper.searchUsersByFeatureName(Arrays.asList(HkSystemConstantUtil.Feature.MANAGE_SHIFT), hkShiftEntity.getFranchise());
                List<Long> notify = new ArrayList<>();
                if (!CollectionUtils.isEmpty(searchUsersByFeatureName)) {
                    notify.addAll(searchUsersByFeatureName);
                }

                List<Long> searchUsersByShift = umServiceWrapper.searchUsersByShift(hkShiftEntity.getTemporaryShiftFor().getId());
                notify.addAll(searchUsersByShift);
                hrService.removeShift(hkShiftEntity.getId(), null, notify);
                umServiceWrapper.deleteLocaleForEntity(hkShiftEntity.getShiftTitle(), "SHIFT", "CONTENT", hkShiftEntity.getLastModifiedBy(), hkShiftEntity.getFranchise());
            }
        }
//        System.out.println("total shift deactivated: " + deactivated);
    }

    /**
     * Runs every 15 minutes and activates all the pending franchises
     *
     * @throws GenericDatabaseException
     */
    @Scheduled(fixedRate = 900000)
    public void activatePendingFranchise() throws GenericDatabaseException, UMUserManagementException {
        System.out.println("---------activatePendingFranchise");
        franchiseTransformerBean.activateFranchise();
        System.out.println("---------activatePendingFranchise ends");
    }

    /**
     * This method rotate the shift's of the departments who's difference of
     * days from the lastRotatedOn matches with the value of shift rotation days
     * in given department.
     *
     * @throws GenericDatabaseException
     */
    public void automaticShiftRotationOfEmployees() throws GenericDatabaseException {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        //Retrieveing all the departments of all the franchise.
        List<UMDepartment> departments = umServiceWrapper.retrieveAllDepartments(null);

        Map<Long, UMDepartment> idDeptObj = new HashMap<>();
        Set<Long> departmentIdsWithChild = new HashSet<>();
        if (!CollectionUtils.isEmpty(departments)) {

            //taking the copy of all the departmemts originally retrieved.
            List<UMDepartment> originalDepartments = new LinkedList<>();
            originalDepartments.addAll(departments);

            List<UMDepartment> tempDeptList = new LinkedList<>();

            Calendar taskGenCal = Calendar.getInstance();
            taskGenCal.setTimeInMillis(lastRunCal.getTimeInMillis());
            //  Add one day in last run schedular and get new date to check for the rotation
            taskGenCal.add(Calendar.DATE, 1);
            System.out.println("taskGenCal :" + taskGenCal.getTime());

            //  run the loop for all the days, starting from the last run date till today's date
            while (taskGenCal.before(todayCal) || taskGenCal.equals(todayCal)) {

                //Looping the departments retrieved
                for (UMDepartment department : departments) {

                    //checking whether it contains the value of shift rotation days.
                    if (department.getCustom1() != null) {

                        //Checking whether that departments has any child departments.
                        Set<UMDepartment> childDepartment = department.getuMDepartmentSet();
                        if (!CollectionUtils.isEmpty(childDepartment)) {
//                            System.out.println("childDepartment :" + childDepartment);

                            for (UMDepartment childDept : childDepartment) {

                                //If the departments contains child departments and
                                //if it contains the value of shift rotation days field then without changing it add to departmentIdsWithChild list.
                                if (childDept.getCustom1() != null) {
                                    departmentIdsWithChild.add(childDept.getId());
                                } else {

                                    //It it doesnt contains the value of shift rotation days then set its value same as parent department
                                    //along with the last rotation on fields.
                                    childDept.setCustom1(department.getCustom1());
                                    childDept.setCustom2(department.getCustom2());
                                    departmentIdsWithChild.add(childDept.getId());
                                }
                            }
                        }
                        departmentIdsWithChild.add(department.getId());
                    }

                    //Preparing a map of dept id and obj of dept.
                    idDeptObj.put(department.getId(), department);
                }

                //Retrieving a map of dept id along with the list of shift associated with that departments.
                Map<Long, Set<HkShiftEntity>> shiftDeptMap = hrService.retrieveShiftByDeptIds(new LinkedList<>(departmentIdsWithChild), null);
//                System.out.println("shiftDeptMap :" + shiftDeptMap);

                if (!CollectionUtils.isEmpty(shiftDeptMap)) {

                    //Iterating that map and checking for the shifts inside that department.
                    for (Map.Entry<Long, Set<HkShiftEntity>> entrySet : shiftDeptMap.entrySet()) {
                        Long deptId = entrySet.getKey();
                        if (idDeptObj.containsKey(deptId)) {
                            UMDepartment department = idDeptObj.get(deptId);
//                            System.out.println("department :" + department);
                            if (department.getCustom1() != null && department.getCustom2() != null) {
//                                System.out.println("department.getCustom1() : " + department.getCustom1());

                                //Preparing a calendar instance of the lastRotatedOn field for comparing
                                Calendar lastRotatedOn = Calendar.getInstance();
                                lastRotatedOn.setTime(department.getCustom2());
                                lastRotatedOn.set(Calendar.HOUR, 0);
                                lastRotatedOn.set(Calendar.HOUR_OF_DAY, 0);
                                lastRotatedOn.set(Calendar.MINUTE, 0);
                                lastRotatedOn.set(Calendar.SECOND, 0);
                                lastRotatedOn.set(Calendar.MILLISECOND, 0);

                                //Calculating the difference between lastRotatedOn and the taskGenCal date to check whether to rotate the shift.
                                //Here if the scheduler will not run on specific days it will loop all the missed days and calculate the difference.
                                Days daysDiff = Days.daysBetween(new DateTime(lastRotatedOn.getTime()), new DateTime(taskGenCal.getTime()));
                                int diffDays = daysDiff.getDays();
//                                System.out.println("diff days ----" + diffDays);
                                //If the difference matches with shift rotation day count then we need to rotate the shifts of all those departments
                                if (diffDays == department.getCustom1()) {
                                    Set<HkShiftEntity> shiftEntitys = entrySet.getValue();
                                    List<HkShiftEntity> hkShiftEntitys = new LinkedList<>(shiftEntitys);

                                    //Sorting the shifts of particular department based on the start time of the shift so that
                                    //we get the correct order of the shifts of the day.
                                    //E.g if the shifts are like 13 to 15,20 to 22 and 17 to 19 the after ths we will get 13 to 15,17 to 19 and 20 to 22.
//                                    
                                    if (!CollectionUtils.isEmpty(hkShiftEntitys) && hkShiftEntitys.size() > 1) {
                                        hkShiftEntitys = this.sortShiftEntities(hkShiftEntitys);
//                                        System.out.println("hkShiftEntitys after :" + hkShiftEntitys);

                                        Set<Long> shiftIds = new LinkedHashSet<>();
                                        for (HkShiftEntity hkShiftEntity : hkShiftEntitys) {
                                            shiftIds.add(hkShiftEntity.getId());
                                        }

                                        //Retrieveing users from the shifts associated with dept and preparing map of dept id and list of its shifts
                                        Map<Long, List<UMUser>> shiftUsersMap = this.retrieveUsersByShiftIds(new LinkedList(shiftIds), deptId);

                                        if (!CollectionUtils.isEmpty(shiftUsersMap)) {
                                            System.out.println("shiftUsersMap :" + shiftUsersMap);

                                            //If the map is not empty then rotate the shift.
                                            Map<Long, List<UMUser>> afterRotationMap = this.rotatingShifts(shiftUsersMap, entrySet.getKey());

                                            //Summing up all the users into one list.
                                            List<UMUser> uMUsers = new ArrayList<>();
                                            for (Map.Entry<Long, List<UMUser>> entrySet1 : afterRotationMap.entrySet()) {
                                                List<UMUser> value = entrySet1.getValue();
                                                if (!CollectionUtils.isEmpty(value)) {
                                                    uMUsers.addAll(value);
                                                }
                                            }

                                            //Updating the users of that department and making the entry of them into HkUserWorkHistoryEntity table.
                                            if (!CollectionUtils.isEmpty(uMUsers)) {
                                                userService.updateAllUsers(uMUsers);
                                                List<HkUserWorkHistoryEntity> workHistoryEntitys = new ArrayList<>();
                                                for (UMUser user : uMUsers) {

                                                    HkUserWorkHistoryEntity userWorkHistoryEntity = employeeService.retrieveShiftForUserFromUserWorkHistory(user.getId());
                                                    if (userWorkHistoryEntity != null) {
                                                        //get the existing work history entity
                                                        userWorkHistoryEntity.setEffectedTo(new Date());
                                                        userWorkHistoryEntity.setIsArchive(true);
                                                        workHistoryEntitys.add(userWorkHistoryEntity);
                                                    } else {
                                                        HkUserWorkHistoryEntity userHistory = new HkUserWorkHistoryEntity();
                                                        userHistory.setDepartment(user.getDepartment());
                                                        userHistory.setDesignation(umServiceWrapper.retrieveDesignationStrForHistory(user));
                                                        userHistory.setEffectedFrm(new Date());
                                                        userHistory.setIsArchive(false);
                                                        userHistory.setReportsTo(user.getCustom2());
                                                        userHistory.setShift(user.getCustom4());
                                                        userHistory.setUserId(user.getId());
                                                        workHistoryEntitys.add(userHistory);
                                                    }
                                                }
                                                umServiceWrapper.createOrUpdateWorkHistory(workHistoryEntitys);
                                            }

                                            //Preparing the list of departments who's rotations has taken place.
                                            tempDeptList.add(department);
                                        } else {
                                            System.out.println("no shifting---" + shiftUsersMap);
                                        }
                                    }
                                } else if (diffDays == (department.getCustom1() - 1)) {
                                    Set<HkShiftEntity> shiftEntitys = entrySet.getValue();
                                    List<HkShiftEntity> hkShiftEntitys = new LinkedList<>(shiftEntitys);

                                    //Sorting the shifts of particular department based on the start time of the shift so that
                                    //we get the correct order of the shifts of the day.
                                    //E.g if the shifts are like 13 to 15,20 to 22 and 17 to 19 the after ths we will get 13 to 15,17 to 19 and 20 to 22.
//                                    
                                    if (!CollectionUtils.isEmpty(hkShiftEntitys) && hkShiftEntitys.size() > 1) {
                                        hkShiftEntitys = this.sortShiftEntities(hkShiftEntitys);
//                                        System.out.println("hkShiftEntitys after :" + hkShiftEntitys);

                                        Set<Long> shiftIds = new LinkedHashSet<>();
                                        for (HkShiftEntity hkShiftEntity : hkShiftEntitys) {
                                            shiftIds.add(hkShiftEntity.getId());
                                        }
                                        Map<Long, List<UMUser>> shiftUsersMap = this.retrieveUsersByShiftIds(new LinkedList(shiftIds), deptId);

                                        if (!CollectionUtils.isEmpty(shiftUsersMap)) {
                                            System.out.println("shiftUsersMap :" + shiftUsersMap);

                                            Map<Long, List<UMUser>> afterRotationMap = this.rotatingShifts(shiftUsersMap, entrySet.getKey());

                                            //Summing up all the users into one list.
                                            List<UMUser> uMUsers = new ArrayList<>();
                                            Map<Long, HkShiftEntity> map = new HashMap<>();
                                            if (!CollectionUtils.isEmpty(afterRotationMap)) {
                                                Set<Long> shiftLongId = afterRotationMap.keySet();
                                                if (!CollectionUtils.isEmpty(shiftLongId)) {

                                                    //Preparing the map of shift id and its obj for further use
                                                    List<HkShiftEntity> shiftEntitysList = hrService.retrieveShiftByIds(new ArrayList(shiftLongId), null);
                                                    if (!CollectionUtils.isEmpty(shiftEntitysList)) {
                                                        for (HkShiftEntity shiftObj : shiftEntitysList) {
                                                            map.put(shiftObj.getId(), shiftObj);
                                                        }
                                                    }
                                                }
                                                for (Map.Entry<Long, List<UMUser>> entrySet1 : afterRotationMap.entrySet()) {
                                                    List<UMUser> value = entrySet1.getValue();
                                                    HkShiftEntity entity = map.get(entrySet.getKey());
                                                    if (!CollectionUtils.isEmpty(value)) {
                                                        uMUsers.addAll(value);
                                                    }
                                                    //Send notification to the users whose shift is going to changed by tomorrow.
                                                    hrService.sendRotateShiftNotification(uMUsers, entity);
                                                }
                                            }

                                        } else {
                                            System.out.println("no notifications---");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //If departments found in the list
                    if (!CollectionUtils.isEmpty(tempDeptList)) {
                        List<UMDepartment> updateDeptList = new ArrayList<>();
                        for (UMDepartment tempDept : tempDeptList) {

                            //Comapring with the original department list as we have changed its field after retrieving from the table.
                            for (UMDepartment originalDepartment : originalDepartments) {
                                if (tempDept.getId().equals(originalDepartment.getId()) && originalDepartment.getCustom1() != null) {

                                    //If it matches then changing the lastRotatedOn date to generation of task date after the last run.
                                    originalDepartment.setCustom2(taskGenCal.getTime());

                                    //Preparing the updated department list from the original.
                                    updateDeptList.add(originalDepartment);
                                }
                            }
                        }

                        //Updating the departement's last rotated on field
                        departmentService.updateAllDepartment(updateDeptList);
                    }
                }

                //  Add one day in last run schedular and get new date to generate the task
                taskGenCal.add(Calendar.DATE, 1);
            }

        }
    }

    public Map<Long, List<UMUser>> rotatingShifts(Map<Long, List<UMUser>> shiftUsersMap, Long deptId) {
        //New map to store the result after rotation of the users in the shift
        Map<Long, List<UMUser>> afterRotationMap = new LinkedHashMap<>();

        if (!CollectionUtils.isEmpty(shiftUsersMap)) {
            System.out.println("shiftUsersMap :" + shiftUsersMap);

            //Fetching the first value from the map.
            //means getting the first shift and their users from the map.
            Long firstShiftId = (Long) shiftUsersMap.keySet().toArray()[0];
            List<UMUser> firstUserShiftSet = shiftUsersMap.get(firstShiftId);

            //As the key is in the oredered form we dnt need to change the key only users will be shifted.
            //So keeping the first shifts users blank.
            afterRotationMap.put(firstShiftId, null);
            for (int i = 1; i < shiftUsersMap.size(); i++) {

                //retrieveing the second shift along with their users.
                Long secondShiftId = (Long) shiftUsersMap.keySet().toArray()[i];
                List<UMUser> secondUserShiftSet = shiftUsersMap.get(secondShiftId);

                //Now the first shift's users need to be shifted to second shift.
                //Then changing the shift field of that users by second shift.
                if (!CollectionUtils.isEmpty(firstUserShiftSet)) {
                    for (UMUser firstUser : firstUserShiftSet) {
                        firstUser.setCustom4(secondShiftId);
                    }
                }

                //Setting the list of first users in front of second shift meaning they are shifted.
                afterRotationMap.put(secondShiftId, firstUserShiftSet);

                //Initalizing the set again and then keeping the second users list into first again and iterating again.
                firstUserShiftSet = new ArrayList<>();
                if (!CollectionUtils.isEmpty(secondUserShiftSet)) {
                    firstUserShiftSet.addAll(secondUserShiftSet);
                }
            }

            //Setting the last shift's user to first shift users.
            if (!CollectionUtils.isEmpty(firstUserShiftSet)) {
                for (UMUser firstUser : firstUserShiftSet) {
                    firstUser.setCustom4(firstShiftId);
                }
            }

            afterRotationMap.put(firstShiftId, firstUserShiftSet);
            System.out.println("afterRotationMap :" + afterRotationMap);

        }
        return afterRotationMap;
    }

    public List<HkShiftEntity> sortShiftEntities(List<HkShiftEntity> hkShiftEntitys) {
        if (!CollectionUtils.isEmpty(hkShiftEntitys) && hkShiftEntitys.size() > 1) {
            Collections.sort(hkShiftEntitys, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    HkShiftEntity m1 = (HkShiftEntity) o1;
                    HkShiftEntity m2 = (HkShiftEntity) o2;
                    List<HkShiftDtlEntity> dtlEntitys1 = new ArrayList<>(m1.getHkShiftDtlEntitySet());
                    List<HkShiftDtlEntity> dtlEntitys2 = new ArrayList<>(m2.getHkShiftDtlEntitySet());
                    Date strtTime1 = dtlEntitys1.get(0).getStrtTime();
                    Calendar cmpDate1 = Calendar.getInstance();
                    cmpDate1.set(Calendar.HOUR, strtTime1.getHours());
                    cmpDate1.set(Calendar.MINUTE, strtTime1.getMinutes());
                    cmpDate1.set(Calendar.SECOND, 0);

                    Date strtTime2 = dtlEntitys2.get(0).getStrtTime();
                    Calendar cmpDate2 = Calendar.getInstance();
                    cmpDate2.set(Calendar.HOUR, strtTime2.getHours());
                    cmpDate2.set(Calendar.MINUTE, strtTime2.getMinutes());
                    cmpDate2.set(Calendar.SECOND, 0);

                    return cmpDate1.compareTo(cmpDate2);
                }
            });
        }
        return hkShiftEntitys;
    }

    public Map<Long, List<UMUser>> retrieveUsersByShiftIds(List<Long> shiftIds, Long deptId) {
        List<UMUser> users = new LinkedList<>();

        //Retrieving the users of the shift for the rotation.
        users = umServiceWrapper.retrieveUsersByShiftByDepartment(null, new LinkedList(shiftIds), Arrays.asList(deptId), true, null);

        //Preparing the map of shift id and the respective users of the shift.
        Map<Long, List<UMUser>> shiftUsersMap = new LinkedHashMap<>();
        System.out.println("users :" + users);
        for (Long shift : shiftIds) {
            for (UMUser user : users) {
                if (user.getCustom4().equals(shift)) {
                    List<UMUser> shiftWiseUserList = new LinkedList<>();
                    if (shiftUsersMap.containsKey(shift)) {
                        shiftWiseUserList = shiftUsersMap.get(shift);
                    }
                    shiftWiseUserList.add(user);
                    shiftUsersMap.put(shift, shiftWiseUserList);
                }
            }
            if (!shiftUsersMap.containsKey(shift)) {
                shiftUsersMap.put(shift, null);
            }
        }
        return shiftUsersMap;
    }

    //Cron format:: 1-Seconds 2-Minutes 3-Hours 4-Day-of-Month 5-Month 6-Day-of-Week 7-Year (optional field)
    //Runs every day at 12:00AM
    @Scheduled(cron = "0 0 0 * * ?")
    public void createUserGoal() throws CloneNotSupportedException, ScriptException {
        System.out.println("In cron createUserGoal().....");
        List<HkGoalTemplateEntity> activetemplates = foundationService.retrieveGoalTemplates(null, HkSystemConstantUtil.ACTIVE);

        if (!CollectionUtils.isEmpty(activetemplates)) {
            List<Long> activetemplateIds = new ArrayList<>();
            Map<Long, HkGoalTemplateEntity> idToEntityMap = new HashMap<>();

            List<HkGoalTemplateEntity> goalStatusToCreate = new ArrayList<>(activetemplates);

            for (HkGoalTemplateEntity hkGoalTemplateEntity : activetemplates) {
                activetemplateIds.add(hkGoalTemplateEntity.getId());
                idToEntityMap.put(hkGoalTemplateEntity.getId(), hkGoalTemplateEntity);
            }
            List<HkUserGoalStatusDocument> userGoalStatuses = goalService.retrieveUserGoalStatusByGoalTemplateIds(activetemplateIds, Arrays.asList(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING, HkSystemConstantUtil.UserGoalTemplateStatus.SUBMITTED));
            List<HkUserGoalStatusDocument> toModifyuserGoalStatuses = new ArrayList<>();

            Iterator<HkGoalTemplateEntity> iterator = goalStatusToCreate.iterator();
            Map<String, HkUserGoalStatusDocument> mapOfUserGoals = new HashMap<>();

            while (iterator.hasNext()) {
                HkGoalTemplateEntity goalTemplate = iterator.next();

                if (goalTemplate.getForDesignation() != null) {
                    Long designation = goalTemplate.getForDesignation();
                    List<UMUserRole> userRoles = umServiceWrapper.retrieveUserRolesByRoleId(designation, true);

                    if (!CollectionUtils.isEmpty(userRoles)) {
                        for (UMUserRole uMUserRole : userRoles) {
                            Calendar cal = Calendar.getInstance();
//                            cal.set(Calendar.AM_PM, Calendar.AM);
                            cal.set(Calendar.HOUR, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);

                            HkUserGoalStatusDocument userGoalStatusDocument = new HkUserGoalStatusDocument();

                            userGoalStatusDocument.setActivityGroup(null);
                            userGoalStatusDocument.setActivityNode(null);
                            userGoalStatusDocument.setCreatedOn(new Date());
                            userGoalStatusDocument.setForUser(uMUserRole.getUserId());
                            userGoalStatusDocument.setFranchiseId(goalTemplate.getFranchise());
                            userGoalStatusDocument.setFromDate(cal.getTime());
                            userGoalStatusDocument.setRealizedCount(0);

                            cal.add(Calendar.DATE, goalTemplate.getPeriod());
                            cal.set(Calendar.HOUR, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
//                            cal.set(Calendar.AM_PM, Calendar.PM);

                            userGoalStatusDocument.setToDate(cal.getTime());
                            userGoalStatusDocument.setGoalAchieved(Boolean.FALSE);
                            userGoalStatusDocument.setGoalTemplate(goalTemplate.getId());
                            userGoalStatusDocument.setGoalType(goalTemplate.getTemplateType());
                            userGoalStatusDocument.setLastModifiedOn(new Date());
//                                userGoalStatusDocument.setMinTarget(goalTemplate.get);
                            userGoalStatusDocument.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING);

                            mapOfUserGoals.put("" + goalTemplate.getId() + "~!" + uMUserRole.getUserId(), userGoalStatusDocument);
                        }
                    } else {
                        System.out.println("User role is empty in designation");
                    }
                } else if (goalTemplate.getForDepartment() != null) {
                    List<UMUser> usersInDept = umServiceWrapper.retrieveUsersByCompanyByDepartment(null, goalTemplate.getForDepartment(), true, null);
                    if (!CollectionUtils.isEmpty(usersInDept)) {
                        for (UMUser user : usersInDept) {
                            Calendar cal = Calendar.getInstance();
//                            cal.set(Calendar.AM_PM, Calendar.AM);
                            cal.set(Calendar.HOUR, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);

                            HkUserGoalStatusDocument userGoalStatusDocument = new HkUserGoalStatusDocument();

                            userGoalStatusDocument.setActivityGroup(null);
                            userGoalStatusDocument.setActivityNode(null);
                            userGoalStatusDocument.setCreatedOn(new Date());
                            userGoalStatusDocument.setForUser(user.getId());
                            userGoalStatusDocument.setFranchiseId(goalTemplate.getFranchise());
                            userGoalStatusDocument.setFromDate(cal.getTime());
                            userGoalStatusDocument.setRealizedCount(0);

                            cal.add(Calendar.DATE, goalTemplate.getPeriod());
                            cal.set(Calendar.HOUR, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
//                            cal.set(Calendar.AM_PM, Calendar.PM);

                            userGoalStatusDocument.setToDate(cal.getTime());
                            userGoalStatusDocument.setGoalAchieved(Boolean.FALSE);
                            userGoalStatusDocument.setGoalTemplate(goalTemplate.getId());
                            userGoalStatusDocument.setGoalType(goalTemplate.getTemplateType());
                            userGoalStatusDocument.setLastModifiedOn(new Date());
//                                userGoalStatusDocument.setMinTarget(goalTemplate.get);
                            userGoalStatusDocument.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING);

//                                documentsToCreate.add(userGoalStatusDocument);
                            mapOfUserGoals.put("" + goalTemplate.getId() + "~!" + user.getId(), userGoalStatusDocument);
                        }
                    } else {
                        System.out.println("No users in dept");
                    }                
                }
                if (!CollectionUtils.isEmpty(userGoalStatuses)) {
                    for (HkUserGoalStatusDocument hkUserGoalStatusDocument : userGoalStatuses) {
                        if (hkUserGoalStatusDocument.getGoalTemplate().equals(goalTemplate.getId())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(mapOfUserGoals)) {
                for (HkUserGoalStatusDocument hkUserGoalStatusDocument : userGoalStatuses) {
                    if (mapOfUserGoals.get("" + hkUserGoalStatusDocument.getGoalTemplate() + "~!" + hkUserGoalStatusDocument.getForUser()) != null) {
                        toModifyuserGoalStatuses.add(hkUserGoalStatusDocument);
                        mapOfUserGoals.remove("" + hkUserGoalStatusDocument.getGoalTemplate() + "~!" + hkUserGoalStatusDocument.getForUser());
                    }
                }
                List<HkUserGoalStatusDocument> vals = new ArrayList<>(mapOfUserGoals.values());
                if (!CollectionUtils.isEmpty(vals)) {
                    goalService.saveUserGoalStatusByGoalTemplates(vals);
                }
            }
            //Modify Existing user goal templates
            List<HkUserGoalStatusDocument> newModifiedDocuments = new ArrayList<>();
            System.out.println("userGoalStatuses::::" + toModifyuserGoalStatuses);
            if (!CollectionUtils.isEmpty(toModifyuserGoalStatuses)) {
                Calendar cal = Calendar.getInstance();
                for (HkUserGoalStatusDocument hkUserGoalStatusDocument : toModifyuserGoalStatuses) {
                    if ((cal.getTime().compareTo(hkUserGoalStatusDocument.getToDate()) > 0 && hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING)) || cal.getTime().compareTo(hkUserGoalStatusDocument.getToDate()) > 0 && hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED)) {

                        if (cal.getTime().compareTo(hkUserGoalStatusDocument.getToDate()) > 0 && hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING)) {
                            hkUserGoalStatusDocument.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.NOT_ATTENDED);
                        } else if (cal.getTime().compareTo(hkUserGoalStatusDocument.getToDate()) > 0 && hkUserGoalStatusDocument.getStatus().equals(HkSystemConstantUtil.UserGoalTemplateStatus.ATTENDED)) {
                            hkUserGoalStatusDocument.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.COMPLETED);
                        }

                        HkUserGoalStatusDocument userGoalStatusDocument = hkUserGoalStatusDocument.clone();
                        System.out.println("hkUserGoalStatusDocument::::" + hkUserGoalStatusDocument);
                        if (hkUserGoalStatusDocument.getActivityNode() != null) {
                            HkGoalTemplateEntity goalTempl = idToEntityMap.get(hkUserGoalStatusDocument.getGoalTemplate());
                            //System.out.println("goalTempl:::" + goalTempl);
                            if (goalTempl != null) {
                                //System.out.println("hkUserGoalStatusDocument.getGoalAchieved()::::" + hkUserGoalStatusDocument.getGoalAchieved());
                                if (hkUserGoalStatusDocument.getGoalAchieved()) {
                                    //System.out.println("goalTempl.getSuccessValue()::::" + goalTempl.getSuccessValue());
                                    String[] data = goalTempl.getSuccessValue().split(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE);
                                    //System.out.println("data[1]::::" + data[1]);
                                    if (data.length == 2) {
                                        String replace = data[1];
                                        //System.out.println("data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_GOAL):::::" + data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_VALUE));
                                        if (data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_VALUE)) {
                                            replace = data[1].replace("$" + HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_VALUE + "$", hkUserGoalStatusDocument.getMaxTarget() != null ? hkUserGoalStatusDocument.getMaxTarget().toString() : hkUserGoalStatusDocument.getMinTarget().toString());
                                        }
                                        if (data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_ACHIEVED_VALUE)) {
                                            replace = data[1].replace("$" + HkSystemConstantUtil.GoalTemplateModifiers.PREV_ACHIEVED_VALUE + "$", hkUserGoalStatusDocument.getRealizedCount() == null ? "0" : hkUserGoalStatusDocument.getRealizedCount().toString());
                                        }
                                        //System.out.println("data[1] after replace:::::" + replace);
                                        ScriptEngineManager mgr = new ScriptEngineManager();
                                        ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                        //System.out.println("data[0]::::" + data[0]);
                                        if (data[0].equals("equal to")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()));
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        } else if (data[0].equals("less than")) {
                                            userGoalStatusDocument.setMinTarget(0);
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()) - 1);
                                        } else if (data[0].equals("less than equal to")) {
                                            userGoalStatusDocument.setMinTarget(0);
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        } else if (data[0].equals("greater than")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()) + 1);
                                        } else if (data[0].equals("greater than equal to")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        }
                                        //System.out.println("userGoalStatusDocument.getMinTarget==" + userGoalStatusDocument.getMinTarget());
                                        //System.out.println("userGoalStatusDocument.getMaxTarget==" + userGoalStatusDocument.getMaxTarget());
                                    }
                                } else {
                                    //System.out.println("goalTempl.getFailureValue()::::" + goalTempl.getFailureValue());
                                    String[] data = goalTempl.getFailureValue().split(HkSystemConstantUtil.SEPARATOR_FOR_GOAL_TEMPLATE);
                                    //System.out.println("data::::" + data);
                                    String replace = data[1];
                                    if (data.length == 2) {
                                        if (data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_VALUE)) {
                                            replace = data[1].replace("$" + HkSystemConstantUtil.GoalTemplateModifiers.PREV_SET_VALUE + "$", hkUserGoalStatusDocument.getMaxTarget() == null ? hkUserGoalStatusDocument.getMaxTarget().toString() : hkUserGoalStatusDocument.getMinTarget().toString());
                                        }
                                        if (data[1].contains(HkSystemConstantUtil.GoalTemplateModifiers.PREV_ACHIEVED_VALUE)) {
                                            replace = data[1].replace("$" + HkSystemConstantUtil.GoalTemplateModifiers.PREV_ACHIEVED_VALUE + "$", hkUserGoalStatusDocument.getRealizedCount() == null ? "0" : hkUserGoalStatusDocument.getRealizedCount().toString());
                                        }
                                        //System.out.println("data[1] after replace:::::" + replace);
                                        ScriptEngineManager mgr = new ScriptEngineManager();
                                        ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                        //System.out.println("data[0]::::" + data[0]);
                                        if (data[0].equals("equal to")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()));
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        } else if (data[0].equals("less than")) {
                                            userGoalStatusDocument.setMinTarget(0);
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()) - 1);
                                        } else if (data[0].equals("less than equal to")) {
                                            userGoalStatusDocument.setMinTarget(0);
                                            userGoalStatusDocument.setMaxTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        } else if (data[0].equals("greater than")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()) + 1);
                                        } else if (data[0].equals("greater than equal to")) {
                                            userGoalStatusDocument.setMinTarget(Integer.parseInt(engine.eval(replace).toString()));
                                        }
                                        //System.out.println("userGoalStatusDocument.getMinTarget==" + userGoalStatusDocument.getMinTarget());
                                        //System.out.println("userGoalStatusDocument.getMaxTarget==" + userGoalStatusDocument.getMaxTarget());
                                    }
                                }
                            }
                        }
                        userGoalStatusDocument.setId(null);
                        userGoalStatusDocument.setCreatedOn(new Date());
                        userGoalStatusDocument.setStatus(HkSystemConstantUtil.UserGoalTemplateStatus.PENDING);
                        userGoalStatusDocument.setLastModifiedOn(new Date());
                        userGoalStatusDocument.setGoalAchieved(false);
                        userGoalStatusDocument.setPreviousGoalStatusDocumentId(hkUserGoalStatusDocument.getId());
                        Calendar calInstance = Calendar.getInstance();
                        userGoalStatusDocument.setRealizedCount(0);
                        calInstance.setTime(hkUserGoalStatusDocument.getToDate());

                        //If saturday add 2 in add, otherwise add one
                        if (calInstance.get(Calendar.DAY_OF_WEEK) == 7) {
                            calInstance.add(Calendar.DATE, 2);
                        } else {
                            calInstance.add(Calendar.DATE, 1);
                        }
//                        calInstance.set(Calendar.AM_PM, Calendar.AM);
                        calInstance.set(Calendar.HOUR, 0);
                        calInstance.set(Calendar.MINUTE, 0);
                        calInstance.set(Calendar.SECOND, 0);
                        calInstance.set(Calendar.MILLISECOND, 0);
                        userGoalStatusDocument.setFromDate(calInstance.getTime());

                        calInstance.add(Calendar.DATE, idToEntityMap.get(hkUserGoalStatusDocument.getGoalTemplate()).getPeriod());
                        calInstance.set(Calendar.HOUR, 23);
                        calInstance.set(Calendar.MINUTE, 59);
                        calInstance.set(Calendar.SECOND, 59);
//                        calInstance.set(Calendar.AM_PM, Calendar.PM);
                        userGoalStatusDocument.setToDate(calInstance.getTime());

                        newModifiedDocuments.add(userGoalStatusDocument);
                        newModifiedDocuments.add(hkUserGoalStatusDocument);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(newModifiedDocuments)) {
                goalService.saveUserGoalStatusByGoalTemplates(newModifiedDocuments);
            }
        } else {
            System.out.println("Active goal templates is empty");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 180000)
    public void generateTimeBasedNotification() throws GenericDatabaseException {
        System.out.println("-------------In Notification scheduler------------------");
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        lastRunCal = Calendar.getInstance();
        //  Get the last run date for this schedular
        List<HkSystemConfigurationEntity> schedulerConfig = foundationService.retrieveAllSystemConfigurationByKey(HkSystemConstantUtil.FranchiseConfiguration.NOTIFICATION_SCHEDULER_LAST_RUN);
        if (CollectionUtils.isEmpty(schedulerConfig)) {
            //  if no value found with given key, save it now
            HkSystemConfigurationEntity schedulerConfig1 = new HkSystemConfigurationEntity(HkSystemConstantUtil.FranchiseConfiguration.NOTIFICATION_SCHEDULER_LAST_RUN, FRANCHISE);
            schedulerConfig1.setIsArchive(false);
            //  Save the time, modifiedBy field can also be used to get the last run time of scheduler
            schedulerConfig1.setKeyValue(Long.toString(lastRunCal.getTimeInMillis()));
            schedulerConfig1.setModifiedBy(0);
            schedulerConfig1.setModifiedOn(lastRunCal.getTime());
            schedulerConfig.add(schedulerConfig1);
            foundationService.saveSystemConfigurations(schedulerConfig);

            lastRunCal.add(Calendar.DATE, -1);
        } else {
            lastRunCal.setTime(schedulerConfig.get(0).getModifiedOn());
        }
        lastRunCal.set(Calendar.HOUR_OF_DAY, 0);
        lastRunCal.set(Calendar.MINUTE, 0);
        lastRunCal.set(Calendar.SECOND, 0);
        lastRunCal.set(Calendar.MILLISECOND, 0);
        /* Check if notification to generate */
        List<HkNotificationConfigurationEntity> activeEmailConfiguration = notificationService.retrieveAllActiveNotificationConfiguration(null, HkSystemConstantUtil.NotificationConfigurationType.TIME_BASED);
        if (!CollectionUtils.isEmpty(activeEmailConfiguration)) {

            List<HkNotificationConfigurationEntity> notificationListToUpdate = new LinkedList<>();

            for (HkNotificationConfigurationEntity hkNotificationConfigurationEntity : activeEmailConfiguration) {
                Calendar notificationCal = Calendar.getInstance();
                notificationCal.setTimeInMillis(lastRunCal.getTimeInMillis());
                //  Add one day in last run schedular and get new date to generate the task
                notificationCal.add(Calendar.DATE, 1);   //  Now we'll generate tasks for this date

                //  run the loop for all the days, starting from the last run date till today's date
                while (notificationCal.before(todayCal) || notificationCal.equals(todayCal)) {
                    Boolean toGenerate = this.checkNotificationGeneration(hkNotificationConfigurationEntity, notificationCal);

                    if (toGenerate) {
                        List<HkNotificationConfigrationRecipientEntity> notificationRecipients = notificationService.retrieveActiveRecipientsByConfiguration(hkNotificationConfigurationEntity.getId());

                        if (!CollectionUtils.isEmpty(notificationRecipients)) {
                            List<String> recipientCodes = new ArrayList<>();
                            for (HkNotificationConfigrationRecipientEntity recipient : notificationRecipients) {
                                recipientCodes.add(recipient.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance() + ":" + recipient.getHkNotificationConfigrationRecipientEntityPK().getReferenceType());
                            }
                            Set<Long> recipientUserIds = umServiceWrapper.retrieveRecipientIds(recipientCodes);
                            Map<String, Object> valuesMap = new HashMap<>();
                            if (!StringUtils.isEmpty(hkNotificationConfigurationEntity.getWebMessage())) {

                                Integer notificationCnt = null;
                                notificationCnt = hkNotificationConfigurationEntity.getRepetitionCnt() == null ? 0 : hkNotificationConfigurationEntity.getRepetitionCnt();
                                hkNotificationConfigurationEntity.setRepetitionCnt(++notificationCnt);
                                notificationListToUpdate.add(hkNotificationConfigurationEntity);

                                valuesMap.put(HkSystemConstantUtil.NotificationMessageParam.NOTIFICATION_DESC, hkNotificationConfigurationEntity.getWebMessage());

                                HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.NOTIFICATION,
                                        HkSystemConstantUtil.NotificationInstanceType.NOTIFICATION, valuesMap, hkNotificationConfigurationEntity.getId(), hkNotificationConfigurationEntity.getFranchise());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(hkNotificationConfigurationEntity.getAtTime());
                                Calendar clone = (Calendar) notificationCal.clone();
                                clone.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                                clone.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                                clone.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                                notification.setOnDate(clone.getTime());
                                notification.setNotificationConfiguration(hkNotificationConfigurationEntity.getId());
                                notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.WEB);
                                notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.NOT_REQUIRED);

                                notificationService.sendNotification(notification, recipientUserIds);
                            }
                            if (!StringUtils.isEmpty(hkNotificationConfigurationEntity.getEmailMessage())) {
//                                    List<UMUser> recepients = umServiceWrapper.retrieveUsers(new ArrayList<>(recipientUserIds), false);
//                                    if (!CollectionUtils.isEmpty(recepients)) {
//                                        List<String> recs = new ArrayList<>();
//
//                                        for (UMUser uMUser : recepients) {
//                                            if (!StringUtils.isEmpty(uMUser.getEmailAddress())) {
//                                                recs.add(uMUser.getEmailAddress());
//                                            }
//                                        }
//                                        if (!CollectionUtils.isEmpty(recs)) {
//                                            this.sendEmail("Notification", hkNotificationConfigurationEntity.getEmailMessage(), Arrays.copyOf(recs.toArray(), recs.toArray().length, String[].class));
//                                        }
//                                    }
                                HkNotificationEntity notification = createNotification(HkSystemConstantUtil.NotificationType.NOTIFICATION,
                                        HkSystemConstantUtil.NotificationInstanceType.NOTIFICATION, null, hkNotificationConfigurationEntity.getId(), hkNotificationConfigurationEntity.getFranchise());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(hkNotificationConfigurationEntity.getAtTime());
                                Calendar clone = (Calendar) notificationCal.clone();
                                clone.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                                clone.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                                clone.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                                notification.setOnDate(clone.getTime());
                                notification.setDescription(hkNotificationConfigurationEntity.getEmailMessage());
                                notification.setNotificationConfiguration(hkNotificationConfigurationEntity.getId());
                                notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.EMAIL);
                                notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.PENDING);

                                notificationService.sendNotification(notification, recipientUserIds);
                            }
                        }
                    }
                    notificationCal.add(Calendar.DATE, 1);
                }
            }

            if (!CollectionUtils.isEmpty(notificationListToUpdate)) {
                notificationService.saveNotificationConfigurations(notificationListToUpdate);
            }
        }

        /* End of check */
        schedulerConfig.get(0).setModifiedOn(new Date());
        foundationService.saveSystemConfigurations(schedulerConfig);
        System.out.println("-------------End of Notification scheduler------------------");
    }

    public Boolean checkNotificationGeneration(HkNotificationConfigurationEntity notificationConfigurationEntity, Calendar notificationCal) {
        boolean result = false;
        Calendar dueDate = Calendar.getInstance();
        //  Set the duedate to current date first
        dueDate.setTimeInMillis(notificationCal.getTimeInMillis());
        //  if repeatation mode is after x repetitions and if the after units are over i.e. all the repetations are generated,
        //  if rep mode is after x days and if 
        //  if rep mode is on date and if the date is gone,
        //  return null
        //  Task should not be weekly, and if end repeat mode is after x reps and after units is <= actual rep count
        if ((!notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)
                && notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                && notificationConfigurationEntity.getAfterUnits() <= notificationConfigurationEntity.getRepetitionCnt())
                || (notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)
                && notificationConfigurationEntity.getEndDate().before(notificationCal.getTime()))) {
            return result;
        }
        if (notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
            int passedDays = (int) ((notificationCal.getTime().getTime() - notificationConfigurationEntity.getCreatedOn().getTime()) / (1000 * 60 * 60 * 24));
            if (passedDays >= notificationConfigurationEntity.getAfterUnits()) {
                return result;
            }
        }
        if (notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.DAILY)) {
            result = true;
        } else if (notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
            String[] onDays = notificationConfigurationEntity.getWeeklyOnDays().split("\\" + HkSystemConstantUtil.SEPARATOR_PI);
            int countCheck = 0;
            if (notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                Date lstModifiedOn = notificationConfigurationEntity.getLastModifiedOn();
                Calendar calTemp = Calendar.getInstance();
                calTemp.setTime(lstModifiedOn);
                calTemp.set(Calendar.HOUR_OF_DAY, 0);
                calTemp.set(Calendar.MINUTE, 0);
                calTemp.set(Calendar.SECOND, 0);
                calTemp.set(Calendar.MILLISECOND, 0);
                Calendar c = Calendar.getInstance();
                c.setTime(lstModifiedOn);
                Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                int checkCountOfWeek = 0;
                for (int count = 0; count < onDays.length; count++) {
                    if (onDays[count].equals(dayOfWeek.toString())) {
                        checkCountOfWeek++;
                    }
                }

                if (checkCountOfWeek > 0) {
                    countCheck = (notificationConfigurationEntity.getAfterUnits() * onDays.length) - 1;
                } else {
                    countCheck = (notificationConfigurationEntity.getAfterUnits() * onDays.length);
                }
            }

            if (notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                    && (countCheck) <= notificationConfigurationEntity.getRepetitionCnt()) {
                return false;
            }
            for (int count = 0; count < onDays.length; count++) {
                if (onDays[count].equals(Integer.toString(notificationCal.get(Calendar.DAY_OF_WEEK)))) {
                    int diff = 0;
                    if (count + 1 == onDays.length) {
                        diff = 7 - Integer.valueOf(onDays[count]) + Integer.valueOf(onDays[0]) - 1;
                        if (diff >= 7) {
                            diff = diff - 7;
                        }
                    } else {
                        diff = Integer.valueOf(onDays[count + 1]) - Integer.valueOf(onDays[count]) - 1;
                        if (diff < 0) {
                            diff = 7 + diff;
                        }
                    }
                    dueDate.add(Calendar.DATE, diff);
                    result = true;
                    break;
                }
            }
            //  logic: e.g.- weekdays = {4,7}, onDate=25/2/2014 i.e. Thursday (Day=4) today
            //  so as per (after) this loop, nextDay = 6
            //  previousDay = 4
            //  so days to be added = 6-4=2
            //  thus, due date set would be, 25+2 = 27/2/2014 i.e. Saturday (Day=7)

        } else if (notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
            Integer monthDate = notificationConfigurationEntity.getMonthlyOnDay();
            if (notificationCal.get(Calendar.DATE) == monthDate) {
                //  if notificationConfigurationEntity gen date is same as the given month date, result true
                result = true;
            } else if (monthDate == 31 //  if given date is 31 and today's date is 30 and month is 4,6,9,11 then result = true
                    && (notificationCal.get(Calendar.DATE) == 30)
                    && (notificationCal.get(Calendar.MONTH) == Calendar.APRIL
                    || notificationCal.get(Calendar.MONTH) == Calendar.JUNE
                    || notificationCal.get(Calendar.MONTH) == Calendar.SEPTEMBER
                    || notificationCal.get(Calendar.MONTH) == Calendar.NOVEMBER)) {
                result = true;
            } else if (notificationCal.get(Calendar.MONTH) == Calendar.FEBRUARY //  if today's month is february
                    && monthDate > 28 && notificationCal.get(Calendar.DATE) >= 28) {
                if (notificationCal.get(Calendar.YEAR) % 4 != 0 //  if it's not a leap year and date is 28, result = true where given month date > 28
                        || (notificationCal.get(Calendar.YEAR) % 4 == 0 && notificationCal.get(Calendar.DATE) == 29)) {   //  if leap year, and date 29, and monthdate > 28, result = true
                    result = true;
                }
            }
        }

        return result;
    }

    public HkNotificationEntity createNotification(String notificationType, String instanceType, Map<String, Object> valuesMap, Long instanceId, Long franchise) {
        String description = " ";
        if (!CollectionUtils.isEmpty(valuesMap)) {
            Gson gson = new Gson();
            description = gson.toJson(valuesMap);
        }
        HkNotificationEntity notification = new HkNotificationEntity(null, notificationType, new Date(), description, franchise, false);
        notification.setInstanceId(instanceId);
        notification.setInstanceType(instanceType);
        return notification;
    }

    public void sendEmail(String mailSubject, String message, String[] toList, Attachment[] attachmentArray) {

        Email email = new Email();
        if (toList != null && toList.length > 0) {
            email.setTo(toList);

            email.setSubject(mailSubject);
            StringBuilder msg = new StringBuilder();
            msg.append("<html xmlns='http://www.w3.org/1999/xhtml'>");
            msg.append("<head>");
            msg.append("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1' />");
            msg.append("<title>HKG Service Exception</title>");
            msg.append("</head>");
            msg.append("<body>");
            msg.append("<div style='margin:0 auto;color:#00008B; font-family: Arial, Helvetica, sans-serif; font-size:12px;' > <hr style='border-bottom:solid 1px #00008B; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
            msg.append("<h3>");
            msg.append(message);

            msg.append("</h3>");
            msg.append("<hr style='border-bottom:solid 1px #00008B; border-top:none; border-left:none; border-right:none; margin-bottom:5px;'/>");
            msg.append("<br/><div>With Best Wishes, <br/>Team HKG.</div>");
            msg.append("</body>");
            msg.append("</html>");

            email.setMessageBody(msg.toString());

            email.setConnectionType("DEFAULT");
            email.setAttachment(attachmentArray);

            if (emailService == null) {
                //System.out.println("Email Service null...");
                emailService = new EmailServiceImpl();
            } else {
                //System.out.println("Email Service not null...");
            }

            emailService.sendMail(email);
        }
    }

    //Runs every 5 mins
//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(fixedRate = 300000)
    public void sendEmailNotification() throws GenericDatabaseException {
        System.out.println("-------------In sendEmailNotification scheduler-------------");

        Calendar toDay = Calendar.getInstance();
        List<HkNotificationRecipientEntity> pendingEmailNotifications = notificationService.retrievePendingEmailNotificationsTillDate(toDay.getTime());
        if (!CollectionUtils.isEmpty(pendingEmailNotifications)) {
            Map<HkNotificationEntity, List<HkNotificationRecipientEntity>> mapOfNotifications = new HashMap<>();

            for (HkNotificationRecipientEntity hkNotificationRecipientEntity : pendingEmailNotifications) {
                if (mapOfNotifications.get(hkNotificationRecipientEntity.getHkNotificationEntity()) == null) {
                    mapOfNotifications.put(hkNotificationRecipientEntity.getHkNotificationEntity(), new ArrayList<>());
                }
                mapOfNotifications.get(hkNotificationRecipientEntity.getHkNotificationEntity()).add(hkNotificationRecipientEntity);
            }

            if (!CollectionUtils.isEmpty(mapOfNotifications)) {
                List<HkNotificationEntity> notificationsToUpdate = new ArrayList<>();
                for (Map.Entry<HkNotificationEntity, List<HkNotificationRecipientEntity>> entry : mapOfNotifications.entrySet()) {
                    List<Long> recipientUserIds = new ArrayList<>();
                    for (HkNotificationRecipientEntity hkNotificationRecipientEntity : entry.getValue()) {
                        recipientUserIds.add(hkNotificationRecipientEntity.getHkNotificationRecipientEntityPK().getForUser());
                    }
                    List<UMUser> recepients = umServiceWrapper.retrieveUsers(recipientUserIds, false);
                    if (!CollectionUtils.isEmpty(recepients)) {
                        List<String> recs = new ArrayList<>();

                        for (UMUser uMUser : recepients) {
                            if (!StringUtils.isEmpty(uMUser.getEmailAddress())) {
                                recs.add(uMUser.getEmailAddress());
                            }
                        }
                        if (!CollectionUtils.isEmpty(recs)) {
                            this.sendEmail("Notification", entry.getKey().getDescription(), Arrays.copyOf(recs.toArray(), recs.toArray().length, String[].class), null);
                        }
                    }
                    HkNotificationEntity key = entry.getKey();
                    key.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.SENT);

                    notificationsToUpdate.add(key);
                }

                if (!CollectionUtils.isEmpty(notificationsToUpdate)) {
                    notificationService.updateNotifications(notificationsToUpdate);
                }
            }
        }
        System.out.println("-------------End of sendEmailNotification scheduler-------------");
    }

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 60000)
    public void generateTimeBasedReportEmailNotification() throws GenericDatabaseException {
        System.out.println("-------------In Report Email Notification scheduler------------------");

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        /* Check if reports need to be generated */
        List<RbEmailReportConfigurationEntity> allEmailConfigurations = reportService.retrieveAllEmailConfigurations(false);
        List<RbEmailReportStatusEntity> reportEmailStatusEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allEmailConfigurations)) {
            for (RbEmailReportConfigurationEntity emailConfiguration : allEmailConfigurations) {
                if (emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                        || emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
                    if (emailConfiguration.getRepetitionCnt() == null) {
                        emailConfiguration.setRepetitionCnt(0);
                    }
                }
                Boolean isGenerate = this.checkReportEmailGeneration(emailConfiguration, todayCal);
                if (isGenerate) {
                    RbEmailReportStatusEntity emailStatusEntity = new RbEmailReportStatusEntity();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(emailConfiguration.getAtTime());
                    Calendar clone = (Calendar) todayCal.clone();
                    clone.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                    clone.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                    clone.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                    clone.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
                    RbEmailReportStatusEntityPK emailStatusPk = new RbEmailReportStatusEntityPK(emailConfiguration.getId(), clone.getTime(), emailConfiguration.getCreatedBy());
                    emailStatusEntity.setRbEmailReportStatusEntityPK(emailStatusPk);
                    emailStatusEntity.setEmailSent(false);
                    emailStatusEntity.setGeneratedOn(new Date());
                    emailStatusEntity.setStatus(HkSystemConstantUtil.ReportEmailStatus.PENDING);
                    reportEmailStatusEntities.add(emailStatusEntity);
                }
            }
        }
        if (!CollectionUtils.isEmpty(reportEmailStatusEntities)) {
            reportService.saveEmailStatusEntities(reportEmailStatusEntities);
        }

        /* End of check */
        System.out.println("-------------End of report email Notification scheduler------------------");
    }

    public Boolean checkReportEmailGeneration(RbEmailReportConfigurationEntity emailNotificationConfigurationEntity, Calendar notificationCal) {
        boolean result = false;
        Calendar dueDate = Calendar.getInstance();
        //  Set the duedate to current date first
        dueDate.setTimeInMillis(notificationCal.getTimeInMillis());
        //  if repeatation mode is after x repetitions and if the after units are over i.e. all the repetations are generated,
        //  if rep mode is after x days and if 
        //  if rep mode is on date and if the date is gone,
        //  return null
        //  Task should not be weekly, and if end repeat mode is after x reps and after units is <= actual rep count
        if ((!emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)
                && emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                && emailNotificationConfigurationEntity.getAfterUnits() <= emailNotificationConfigurationEntity.getRepetitionCnt())
                || (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)
                && emailNotificationConfigurationEntity.getEndDate().before(notificationCal.getTime()))) {
            return result;
        }
        if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
            int passedDays = (int) ((notificationCal.getTime().getTime() - emailNotificationConfigurationEntity.getCreatedOn().getTime()) / (1000 * 60 * 60 * 24));
            if (passedDays >= emailNotificationConfigurationEntity.getAfterUnits()) {
                return result;
            }
        }
        if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.DAILY)) {
            result = true;
        } else if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
            String[] onDays = emailNotificationConfigurationEntity.getWeeklyOnDays().split("\\" + HkSystemConstantUtil.SEPARATOR_PI);
            int countCheck = 0;
            if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)) {
                Date lstModifiedOn = emailNotificationConfigurationEntity.getLastModifiedOn();
                Calendar calTemp = Calendar.getInstance();
                calTemp.setTime(lstModifiedOn);
                calTemp.set(Calendar.HOUR_OF_DAY, 0);
                calTemp.set(Calendar.MINUTE, 0);
                calTemp.set(Calendar.SECOND, 0);
                calTemp.set(Calendar.MILLISECOND, 0);
                Calendar c = Calendar.getInstance();
                c.setTime(lstModifiedOn);
                Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                int checkCountOfWeek = 0;
                for (int count = 0; count < onDays.length; count++) {
                    if (onDays[count].equals(dayOfWeek.toString())) {
                        checkCountOfWeek++;
                    }
                }

                if (checkCountOfWeek > 0) {
                    countCheck = (emailNotificationConfigurationEntity.getAfterUnits() * onDays.length) - 1;
                } else {
                    countCheck = (emailNotificationConfigurationEntity.getAfterUnits() * onDays.length);
                }
            }

            if (emailNotificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                    && (countCheck) <= emailNotificationConfigurationEntity.getRepetitionCnt()) {
                return false;
            }
            for (int count = 0; count < onDays.length; count++) {
                if (onDays[count].equals(Integer.toString(notificationCal.get(Calendar.DAY_OF_WEEK)))) {
                    int diff = 0;
                    if (count + 1 == onDays.length) {
                        diff = 7 - Integer.valueOf(onDays[count]) + Integer.valueOf(onDays[0]) - 1;
                        if (diff >= 7) {
                            diff = diff - 7;
                        }
                    } else {
                        diff = Integer.valueOf(onDays[count + 1]) - Integer.valueOf(onDays[count]) - 1;
                        if (diff < 0) {
                            diff = 7 + diff;
                        }
                    }
                    dueDate.add(Calendar.DATE, diff);
                    result = true;
                    break;
                }
            }
            //  logic: e.g.- weekdays = {4,7}, onDate=25/2/2014 i.e. Thursday (Day=4) today
            //  so as per (after) this loop, nextDay = 6
            //  previousDay = 4
            //  so days to be added = 6-4=2
            //  thus, due date set would be, 25+2 = 27/2/2014 i.e. Saturday (Day=7)

        } else if (emailNotificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
            Integer monthDate = emailNotificationConfigurationEntity.getMonthlyOnDay();
            if (notificationCal.get(Calendar.DATE) == monthDate) {
                //  if notificationConfigurationEntity gen date is same as the given month date, result true
                result = true;
            } else if (monthDate == 31 //  if given date is 31 and today's date is 30 and month is 4,6,9,11 then result = true
                    && (notificationCal.get(Calendar.DATE) == 30)
                    && (notificationCal.get(Calendar.MONTH) == Calendar.APRIL
                    || notificationCal.get(Calendar.MONTH) == Calendar.JUNE
                    || notificationCal.get(Calendar.MONTH) == Calendar.SEPTEMBER
                    || notificationCal.get(Calendar.MONTH) == Calendar.NOVEMBER)) {
                result = true;
            } else if (notificationCal.get(Calendar.MONTH) == Calendar.FEBRUARY //  if today's month is february
                    && monthDate > 28 && notificationCal.get(Calendar.DATE) >= 28) {
                if (notificationCal.get(Calendar.YEAR) % 4 != 0 //  if it's not a leap year and date is 28, result = true where given month date > 28
                        || (notificationCal.get(Calendar.YEAR) % 4 == 0 && notificationCal.get(Calendar.DATE) == 29)) {   //  if leap year, and date 29, and monthdate > 28, result = true
                    result = true;
                }
            }
        }

        return result;
    }

    //Runs every 5 mins
    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(fixedRate = 60000)
    public void sendReportEmail() throws GenericDatabaseException {

        System.out.println("-------------In sendReportEmail scheduler-------------");
        Calendar toDay = Calendar.getInstance();
        List<RbEmailReportStatusEntity> pendingReportEmailStatusList = reportService.retrievePendingReportEmailStatusTillDate(toDay.getTime());
        if (!CollectionUtils.isEmpty(pendingReportEmailStatusList)) {
            List<RbEmailReportStatusEntity> emailStatusToBeUpdated = new ArrayList<>();
            List<RbEmailReportConfigurationEntity> emailConfigurationsToBeUpdated = new ArrayList<>();
            for (RbEmailReportStatusEntity emailReportStatus : pendingReportEmailStatusList) {
                Date onDate = emailReportStatus.getRbEmailReportStatusEntityPK().getOnTime();
                Date today = new Date();
                //If on date is within 12 hours of current time.
                if (((today.getTime() - onDate.getTime()) / (1000 * 60 * 60)) < 12) {
                    Long recipientId = emailReportStatus.getRbEmailReportStatusEntityPK().getForUser();
                    Long emailConfigurationId = emailReportStatus.getRbEmailReportStatusEntityPK().getEmailReportConfiguration();
                    List<String> requires = new ArrayList<>();
                    requires.add("uMUserRoleSet");
                    requires.add("uMUserFeatureSet");
                    requires.add("uMUserContactSet");
                    requires.add("contact");
                    requires.add("uMUserIpAssociationSet");
                    requires.add("uMUserGroupSet");
                    UMUser user = userService.retrieveUserById(recipientId, requires);
                    RbEmailReportConfigurationEntity emailConfiguration = reportService.retrieveEmailConfigurationById(emailConfigurationId);
                    schedulerReportTransformer.initUserDataBean(user);
                    MasterReportDataBean retrievedReport = schedulerReportTransformer.retrieveReportById(emailConfiguration.getReport().getId());
                    byte[] pdfByteData = null;
                    byte[] xlsByteData = null;
                    try {
                        if (emailConfiguration.isPdfAttachment()) {
                            pdfByteData = schedulerReportTransformer.generateDataForReport(retrievedReport, ".pdf", null, null);
                        }
                        if (emailConfiguration.isXlsAttachment()) {
                            xlsByteData = schedulerReportTransformer.generateDataForReport(retrievedReport, ".xls", null, null);
                        }
                    } catch (SQLException | JSONException | ParseException | DRException | JRException ex) {
                        Logger.getLogger(HkScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(HkScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(HkScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    List<Attachment> attachmentList = new ArrayList<>();
                    if (pdfByteData != null) {
                        Attachment attachment = new Attachment();
                        attachment.setFileName(retrievedReport.getReportName() + ".pdf");
                        attachment.setFileContent(pdfByteData);
                        attachmentList.add(attachment);
                    }
                    if (xlsByteData != null) {
                        Attachment attachment = new Attachment();
                        attachment.setFileName(retrievedReport.getReportName() + ".xls");
                        attachment.setFileContent(xlsByteData);
                        attachmentList.add(attachment);
                    }

                    List<String> toList = new ArrayList<>();
                    if (user.getEmailAddress() != null && !StringUtils.isEmpty(user.getEmailAddress())) {
                        toList.add(user.getEmailAddress());
                    }
                    this.sendEmail("Report - " + retrievedReport.getReportName(), retrievedReport.getDescription(), Arrays.copyOf(toList.toArray(), toList.toArray().length, String[].class), Arrays.copyOf(attachmentList.toArray(), attachmentList.toArray().length, Attachment[].class));
                    emailReportStatus.setEmailSent(true);
                    emailReportStatus.setStatus(HkSystemConstantUtil.ReportEmailStatus.SENT);
                    emailStatusToBeUpdated.add(emailReportStatus);
                    //Update email configuration count.
                    if (emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_REPETITIONS)
                            || emailConfiguration.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.AFTER_X_DAYS)) {
                        if (emailConfiguration.getRepetitionCnt() == null) {
                            emailConfiguration.setRepetitionCnt(0);
                        }
                        emailConfiguration.setRepetitionCnt(emailConfiguration.getRepetitionCnt() + 1);
                        emailConfigurationsToBeUpdated.add(emailConfiguration);
                    }
                } else {
                    //Set status to expired.
                    emailReportStatus.setStatus(HkSystemConstantUtil.ReportEmailStatus.EXPIRED);
                    emailStatusToBeUpdated.add(emailReportStatus);
                }
            }
            reportService.saveEmailStatusEntities(emailStatusToBeUpdated);
            if (!CollectionUtils.isEmpty(emailConfigurationsToBeUpdated)) {
                reportService.saveEmailConfigurationEntities(emailConfigurationsToBeUpdated);
            }
        }
        System.out.println("-------------End of sendReportEmail scheduler-------------");
    }

}
