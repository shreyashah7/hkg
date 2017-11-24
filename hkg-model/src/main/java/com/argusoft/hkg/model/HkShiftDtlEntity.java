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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_shift_dtl")
@NamedQueries({
    @NamedQuery(name = "HkShiftDtlEntity.findAll", query = "SELECT h FROM HkShiftDtlEntity h")})
public class HkShiftDtlEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "slot_type", length = 5)
    private String slotType;
    @Column(name = "slot_title", length = 100)
    private String slotTitle;
    @Basic(optional = false)
    @Column(name = "strt_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date strtTime;
    @Basic(optional = false)
    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "effected_frm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectedFrm;
    @Column(name = "effected_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectedEnd;
    @Basic(optional = false)
    @Column(name = "week_days", nullable = false, length = 100)
    private String weekDays;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @JoinColumn(name = "shift", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private HkShiftEntity shift;
    @Column(name = "shift_duration_min")
    private Integer shiftDurationMin;

    public HkShiftDtlEntity() {
    }

    public HkShiftDtlEntity(Long id) {
        this.id = id;
    }

    public HkShiftDtlEntity(Long id, Date strtTime, Date endTime, String weekDays, long createdBy, Date createdOn, boolean isArchive) {
        this.id = id;
        this.strtTime = strtTime;
        this.endTime = endTime;
        this.weekDays = weekDays;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.isArchive = isArchive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getSlotTitle() {
        return slotTitle;
    }

    public void setSlotTitle(String slotTitle) {
        this.slotTitle = slotTitle;
    }

    public Date getStrtTime() {
        return strtTime;
    }

    public void setStrtTime(Date strtTime) {
        this.strtTime = strtTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEffectedFrm() {
        return effectedFrm;
    }

    public void setEffectedFrm(Date effectedFrm) {
        this.effectedFrm = effectedFrm;
    }

    public Date getEffectedEnd() {
        return effectedEnd;
    }

    public void setEffectedEnd(Date effectedEnd) {
        this.effectedEnd = effectedEnd;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
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

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkShiftEntity getShift() {
        return shift;
    }

    public void setShift(HkShiftEntity shift) {
        this.shift = shift;
    }

    public Integer getShiftDurationMin() {
        return shiftDurationMin;
    }

    public void setShiftDurationMin(Integer shiftDurationMin) {
        this.shiftDurationMin = shiftDurationMin;
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
        if (!(object instanceof HkShiftDtlEntity)) {
            return false;
        }
        HkShiftDtlEntity other = (HkShiftDtlEntity) object;
        if (this.id == null && other.id == null) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean isIdenticalTo(HkShiftDtlEntity object) {

        if (id == null) {
            if (object.id != null) {
                return false;
            }
        } else if (!id.equals(object.id)) {
            return false;
        }

        if (createdBy != object.createdBy) {
            return false;
        }
        if (createdOn == null) {
            if (object.createdOn != null) {
                return false;
            }
        } else if (!createdOn.equals(object.createdOn)) {
            return false;
        }
        if (effectedEnd == null) {
            if (object.effectedEnd != null) {
                return false;
            }
        } else if (!effectedEnd.equals(object.effectedEnd)) {
            return false;
        }
        if (effectedFrm == null) {
            if (object.effectedFrm != null) {
                return false;
            }
        } else if (!effectedFrm.equals(object.effectedFrm)) {
            return false;
        }
        if (endTime == null) {
            if (object.endTime != null) {
                return false;
            }
        } else if (!endTime.equals(object.endTime)) {
            return false;
        }
        if (isArchive != object.isArchive) {
            return false;
        }
        if (shift == null) {
            if (object.shift != null) {
                return false;
            }
        } else if (!shift.equals(object.shift)) {
            return false;
        }
        if (slotTitle == null) {
            if (object.slotTitle != null) {
                return false;
            }
        } else if (!slotTitle.equals(object.slotTitle)) {
            return false;
        }
        if (slotType == null) {
            if (object.slotType != null) {
                return false;
            }
        } else if (!slotType.equals(object.slotType)) {
            return false;
        }
        if (strtTime == null) {
            if (object.strtTime != null) {
                return false;
            }
        } else if (!strtTime.equals(object.strtTime)) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftDtlEntity[ id=" + id + " ]";
    }

}
