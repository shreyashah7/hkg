package com.argusoft.hkg.web.goalmanagement.databeans;

import com.argusoft.hkg.web.usermanagement.databeans.RuleSetDataBean;
import java.util.Map;

/**
 * Databean to store GoalTemplate Information....
 *
 * @author rajkumar
 */
public class GoalTemplateDataBean {

    private Long id;
    private String name;
    private String description;
    private Integer period;
    private String type;
    private Long copyOf;
    private RuleSetDataBean ruleList;
    private boolean isGenVal;
    private String genvaltype;
    private String genvalvalue;
    private boolean isValGoalAchieved;
    private String valgoalachievedtype;
    private String valgoalachievedvalue;
    private boolean isValGoalNotAchieved;
    private String valgoalnotachievedtype;
    private String valgoalnotachievedvalue;
    private Long for_service;
    private Long for_designation;
    private Long for_department;
    private Long franchise;
    private String status;
    private String nameOfAssociation;
    private Map<String, Object> goalCustom;
    private Map<String, String> dbType;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public RuleSetDataBean getRuleList() {
        return ruleList;
    }

    public void setRuleList(RuleSetDataBean ruleList) {
        this.ruleList = ruleList;
    }

    public boolean isIsGenVal() {
        return isGenVal;
    }

    public void setIsGenVal(boolean isGenVal) {
        this.isGenVal = isGenVal;
    }

    public String getGenvaltype() {
        return genvaltype;
    }

    public void setGenvaltype(String genvaltype) {
        this.genvaltype = genvaltype;
    }

    public String getGenvalvalue() {
        return genvalvalue;
    }

    public void setGenvalvalue(String genvalvalue) {
        this.genvalvalue = genvalvalue;
    }

    public boolean isIsValGoalAchieved() {
        return isValGoalAchieved;
    }

    public void setIsValGoalAchieved(boolean isValGoalAchieved) {
        this.isValGoalAchieved = isValGoalAchieved;
    }

    public String getValgoalachievedtype() {
        return valgoalachievedtype;
    }

    public void setValgoalachievedtype(String valgoalachievedtype) {
        this.valgoalachievedtype = valgoalachievedtype;
    }

    public String getValgoalachievedvalue() {
        return valgoalachievedvalue;
    }

    public void setValgoalachievedvalue(String valgoalachievedvalue) {
        this.valgoalachievedvalue = valgoalachievedvalue;
    }

    public boolean isIsValGoalNotAchieved() {
        return isValGoalNotAchieved;
    }

    public void setIsValGoalNotAchieved(boolean isValGoalNotAchieved) {
        this.isValGoalNotAchieved = isValGoalNotAchieved;
    }

    public String getValgoalnotachievedtype() {
        return valgoalnotachievedtype;
    }

    public void setValgoalnotachievedtype(String valgoalnotachievedtype) {
        this.valgoalnotachievedtype = valgoalnotachievedtype;
    }

    public String getValgoalnotachievedvalue() {
        return valgoalnotachievedvalue;
    }

    public void setValgoalnotachievedvalue(String valgoalnotachievedvalue) {
        this.valgoalnotachievedvalue = valgoalnotachievedvalue;
    }

    public Long getFor_service() {
        return for_service;
    }

    public void setFor_service(Long for_service) {
        this.for_service = for_service;
    }

    public Long getFor_designation() {
        return for_designation;
    }

    public void setFor_designation(Long for_designation) {
        this.for_designation = for_designation;
    }

    public Long getFor_department() {
        return for_department;
    }

    public void setFor_department(Long for_department) {
        this.for_department = for_department;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCopyOf() {
        return copyOf;
    }

    public void setCopyOf(Long copyOf) {
        this.copyOf = copyOf;
    }

    public String getNameOfAssociation() {
        return nameOfAssociation;
    }

    public void setNameOfAssociation(String nameOfAssociation) {
        this.nameOfAssociation = nameOfAssociation;
    }

    public Map<String, Object> getGoalCustom() {
        return goalCustom;
    }

    public void setGoalCustom(Map<String, Object> goalCustom) {
        this.goalCustom = goalCustom;
    }

    public Map<String, String> getDbType() {
        return dbType;
    }

    public void setDbType(Map<String, String> dbType) {
        this.dbType = dbType;
    }

    @Override
    public String toString() {
        return "GoalTemplateDataBean{" + "id=" + id + ", name=" + name + ", description=" + description + ", period=" + period + ", type=" + type + ", copyOf=" + copyOf + ", ruleList=" + ruleList + ", isGenVal=" + isGenVal + ", genvaltype=" + genvaltype + ", genvalvalue=" + genvalvalue + ", isValGoalAchieved=" + isValGoalAchieved + ", valgoalachievedtype=" + valgoalachievedtype + ", valgoalachievedvalue=" + valgoalachievedvalue + ", isValGoalNotAchieved=" + isValGoalNotAchieved + ", valgoalnotachievedtype=" + valgoalnotachievedtype + ", valgoalnotachievedvalue=" + valgoalnotachievedvalue + ", for_service=" + for_service + ", for_designation=" + for_designation + ", for_department=" + for_department + ", franchise=" + franchise + ", status=" + status + ", nameOfAssociation=" + nameOfAssociation + ", goalCustom=" + goalCustom + ", dbType=" + dbType + '}';
    }

}
