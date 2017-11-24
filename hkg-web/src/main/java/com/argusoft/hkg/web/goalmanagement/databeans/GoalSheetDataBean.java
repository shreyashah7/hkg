package com.argusoft.hkg.web.goalmanagement.databeans;

import java.util.Date;

/**
 *
 * @author rajkumar
 */
public class GoalSheetDataBean {

    private String id;
    private String goalName;
    private Date fromDate;
    private Date toDate;
    private Integer minTarget;
    private Integer maxTarget;
    private Integer target;
    private Boolean goalAchieved;
    private Long user;
    private String goalType;
    private String activityGroup;
    private String activityNode;
    private String department;
    private String designation;
    private String status;
    private Integer realizedCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getMinTarget() {
        return minTarget;
    }

    public void setMinTarget(Integer minTarget) {
        this.minTarget = minTarget;
    }

    public Integer getMaxTarget() {
        return maxTarget;
    }

    public void setMaxTarget(Integer maxTarget) {
        this.maxTarget = maxTarget;
    }

    public Boolean isGoalAchieved() {
        return goalAchieved;
    }

    public void setGoalAchieved(Boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getActivityGroup() {
        return activityGroup;
    }

    public void setActivityGroup(String activityGroup) {
        this.activityGroup = activityGroup;
    }

    public String getActivityNode() {
        return activityNode;
    }

    public void setActivityNode(String activityNode) {
        this.activityNode = activityNode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRealizedCount() {
        return realizedCount;
    }

    public void setRealizedCount(Integer realizedCount) {
        this.realizedCount = realizedCount;
    }

    @Override
    public String toString() {
        return "GoalSheetDataBean{" + "id=" + id + ", goalName=" + goalName + ", fromDate=" + fromDate + ", toDate=" + toDate + ", minTarget=" + minTarget + ", maxTarget=" + maxTarget + ", target=" + target + ", goalAchieved=" + goalAchieved + ", user=" + user + ", goalType=" + goalType + ", activityGroup=" + activityGroup + ", activityNode=" + activityNode + ", department=" + department + ", designation=" + designation + ", status=" + status + ", realizedCount=" + realizedCount + '}';
    }


}
