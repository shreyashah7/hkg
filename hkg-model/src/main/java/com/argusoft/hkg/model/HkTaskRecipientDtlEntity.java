/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_task_recipient_dtl")
public class HkTaskRecipientDtlEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "on_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date onDate;
    @Basic(optional = false)
    @Column(name = "due_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name="repetition_count")
    private Integer repetitionCount;
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(length = 1000)
    private String remarks;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @Basic(optional = false)
    @Column(nullable = false, length = 5)
    private String status;
    @Column(name = "attended_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attendedOn;
    @Column(name = "completed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;
    @Column(name = "category")
    private long category;
    @JoinColumn(name = "task", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private HkTaskEntity task;

    public HkTaskRecipientDtlEntity() {
    }

    public HkTaskRecipientDtlEntity(Long id) {
        this.id = id;
    }

    public HkTaskRecipientDtlEntity(Long id, Date dueDate, long userId, boolean isArchive, String status) {
        this.id = id;
        this.dueDate = dueDate;
        this.userId = userId;
        this.isArchive = isArchive;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAttendedOn() {
        return attendedOn;
    }

    public void setAttendedOn(Date attendedOn) {
        this.attendedOn = attendedOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public HkTaskEntity getTask() {
        return task;
    }

    public void setTask(HkTaskEntity task) {
        this.task = task;
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
        if (!(object instanceof HkTaskRecipientDtlEntity)) {
            return false;
        }
        HkTaskRecipientDtlEntity other = (HkTaskRecipientDtlEntity) object;
        if ((this.id == null && other.id == null) || (this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkTaskRecipientDtlEntity[ id=" + id + " ]";
    }

}
