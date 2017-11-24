/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_system_task_info")
public class HkTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "task_name", nullable = false, length = 1000)
    private String taskName;
    @Column(name = "due_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDt;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "actual_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndDate;
    @Basic(optional = false)
    @Column(name = "is_repetative", nullable = false)
    private boolean isRepetative;
    @Column(name = "repeatative_mode", length = 5)
    private String repeatativeMode;
    @Column(name = "weekly_on_days", length = 50)
    private String weeklyOnDays;
    @Column(name = "monthly_on_day")
    private Integer monthlyOnDay;
    @Column(name = "end_repeat_mode", length = 5)
    private String endRepeatMode;
    @Column(name = "after_units")
    private Integer afterUnits;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "last_modified_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedOn;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(nullable = false)
    private long franchise;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    //  Shows the overall status of task
    private String status;
    @Column(name = "task_category")
    private long taskCategory;
    @Column(name="parent")
    private long parent;
    @Column(name="repetition_cnt")
    private Integer repetitionCount;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    @OrderBy("onDate desc")
    private Set<HkTaskRecipientDtlEntity> hkTaskRecipientDtlEntitySet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hkTaskEntity")
    private Set<HkTaskRecipientEntity> hkTaskRecipientEntitySet;

    public HkTaskEntity() {
    }

    public HkTaskEntity(Long id) {
        this.id = id;
    }

    public HkTaskEntity(Long id, String taskName, boolean isRepetative, long createdBy, Date createdOn, Date lastModifiedOn, boolean isArchive, long franchise, String status, long taskCategory) {
        this.id = id;
        this.taskName = taskName;
        this.isRepetative = isRepetative;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastModifiedOn = lastModifiedOn;
        this.isArchive = isArchive;
        this.franchise = franchise;
        this.status = status;
        this.taskCategory = taskCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDueDt() {
        return dueDt;
    }

    public void setDueDt(Date dueDt) {
        this.dueDt = dueDt;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public boolean getIsRepetative() {
        return isRepetative;
    }

    public void setIsRepetative(boolean isRepetative) {
        this.isRepetative = isRepetative;
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

    public Integer getAfterUnits() {
        return afterUnits;
    }

    public void setAfterUnits(Integer afterUnits) {
        this.afterUnits = afterUnits;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(long taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public Set<HkTaskRecipientDtlEntity> getHkTaskRecipientDtlEntitySet() {
        return hkTaskRecipientDtlEntitySet;
    }

    public void setHkTaskRecipientDtlEntitySet(Set<HkTaskRecipientDtlEntity> hkTaskRecipientDtlEntitySet) {
        this.hkTaskRecipientDtlEntitySet = hkTaskRecipientDtlEntitySet;
    }

    public Set<HkTaskRecipientEntity> getHkTaskRecipientEntitySet() {
        return hkTaskRecipientEntitySet;
    }

    public void setHkTaskRecipientEntitySet(Set<HkTaskRecipientEntity> hkTaskRecipientEntitySet) {
        this.hkTaskRecipientEntitySet = hkTaskRecipientEntitySet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkTaskEntity)) {
            return false;
        }
        HkTaskEntity other = (HkTaskEntity) object;
        if ((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkTaskEntity[ id=" + id + " ]";
    }
    
}
