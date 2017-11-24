package com.argusoft.hkg.web.usermanagement.databeans;

import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDataBean;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rajkumar
 */
public class NotificationConfigurationDataBean {

    private Long id;
    private String name;
    private String desc;
    private String repeatativeMode;
    private String weeklyOnDays;
    private Integer monthlyOnDay;
    private String endRepeatMode;
    private Date endDate;
    private Integer afterUnits;
    private String[] taskRecipients;
    private String status;
    private Integer repetitionCount;
    private RuleSetDataBean ruleSet;
    private String basedOn;
    private boolean interfaceEType;
    private String interfaceETypeText;
    private boolean interfaceWType;
    private String interfaceWTypeText;
//    private boolean interfaceSType;
//    private String interfaceSTypeText;
    private String apply;
    private Date atTime;
    private List<TaskRecipientDataBean> taskRecipientDataBeanList;
    private Map<String, Object> taskCustom;
    private Map<String, String> dbType;
    private Long activity;
    private Long service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRepeatativeMode() {
        return repeatativeMode;
    }

    public void setRepeatativeMode(String repeatativeMode) {
        this.repeatativeMode = repeatativeMode;
    }

    public String getWeeklyOnDays() {
        return weeklyOnDays;
    }

    public void setWeeklyOnDays(String weeklyOnDays) {
        this.weeklyOnDays = weeklyOnDays;
    }

    public Integer getMonthlyOnDay() {
        return monthlyOnDay;
    }

    public void setMonthlyOnDay(Integer monthlyOnDay) {
        this.monthlyOnDay = monthlyOnDay;
    }

    public String getEndRepeatMode() {
        return endRepeatMode;
    }

    public void setEndRepeatMode(String endRepeatMode) {
        this.endRepeatMode = endRepeatMode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getAfterUnits() {
        return afterUnits;
    }

    public void setAfterUnits(Integer afterUnits) {
        this.afterUnits = afterUnits;
    }

    public String[] getTaskRecipients() {
        return taskRecipients;
    }

    public void setTaskRecipients(String[] taskRecipients) {
        this.taskRecipients = taskRecipients;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public RuleSetDataBean getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSetDataBean ruleSet) {
        this.ruleSet = ruleSet;
    }

    public String getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(String basedOn) {
        this.basedOn = basedOn;
    }

    public boolean isInterfaceEType() {
        return interfaceEType;
    }

    public void setInterfaceEType(boolean interfaceEType) {
        this.interfaceEType = interfaceEType;
    }

    public String getInterfaceETypeText() {
        return interfaceETypeText;
    }

    public void setInterfaceETypeText(String interfaceETypeText) {
        this.interfaceETypeText = interfaceETypeText;
    }

    public boolean isInterfaceWType() {
        return interfaceWType;
    }

    public void setInterfaceWType(boolean interfaceWType) {
        this.interfaceWType = interfaceWType;
    }

    public String getInterfaceWTypeText() {
        return interfaceWTypeText;
    }

    public void setInterfaceWTypeText(String interfaceWTypeText) {
        this.interfaceWTypeText = interfaceWTypeText;
    }

//    public boolean isInterfaceSType() {
//        return interfaceSType;
//    }
//
//    public void setInterfaceSType(boolean interfaceSType) {
//        this.interfaceSType = interfaceSType;
//    }
//
//    public String getInterfaceSTypeText() {
//        return interfaceSTypeText;
//    }
//
//    public void setInterfaceSTypeText(String interfaceSTypeText) {
//        this.interfaceSTypeText = interfaceSTypeText;
//    }
    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public Date getAtTime() {
        return atTime;
    }

    public void setAtTime(Date atTime) {
        this.atTime = atTime;
    }

    public List<TaskRecipientDataBean> getTaskRecipientDataBeanList() {
        return taskRecipientDataBeanList;
    }

    public void setTaskRecipientDataBeanList(List<TaskRecipientDataBean> taskRecipientDataBeanList) {
        this.taskRecipientDataBeanList = taskRecipientDataBeanList;
    }

    public Map<String, Object> getTaskCustom() {
        return taskCustom;
    }

    public void setTaskCustom(Map<String, Object> taskCustom) {
        this.taskCustom = taskCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    public Long getActivity() {
        return activity;
    }

    public void setActivity(Long activity) {
        this.activity = activity;
    }

    public Long getService() {
        return service;
    }

    public void setService(Long service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "NotificationConfigurationDataBean{" + "id=" + id + ", name=" + name + ", desc=" + desc + ", repeatativeMode=" + repeatativeMode + ", weeklyOnDays=" + weeklyOnDays + ", monthlyOnDay=" + monthlyOnDay + ", endRepeatMode=" + endRepeatMode + ", endDate=" + endDate + ", afterUnits=" + afterUnits + ", taskRecipients=" + taskRecipients + ", status=" + status + ", repetitionCount=" + repetitionCount + ", ruleSet=" + ruleSet + ", basedOn=" + basedOn + ", interfaceEType=" + interfaceEType + ", interfaceETypeText=" + interfaceETypeText + ", interfaceWType=" + interfaceWType + ", interfaceWTypeText=" + interfaceWTypeText + ", apply=" + apply + ", atTime=" + atTime + ", taskRecipientDataBeanList=" + taskRecipientDataBeanList + ", taskCustom=" + taskCustom + ", dbType=" + dbType + ", activity=" + activity + ", service=" + service + '}';
    }

}
