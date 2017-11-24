/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Mayank
 */
@Entity
@Table(name = "hk_shift_rule_info")
@NamedQueries({
    @NamedQuery(name = "HkShiftRuleEntity.findAll", query = "SELECT h FROM HkShiftRuleEntity h")})
public class HkShiftRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HkShiftRuleEntityPK hkShiftRuleEntityPK;
    @Column(name = "day_cnt")
    private Integer dayCnt;
    @Column(name = "event_action", length = 5)
    private String eventAction;
    @Column(name = "event_type", length = 5)
    private String eventType;
    @Column(name = "event_instance")
    private BigInteger eventInstance;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;
    @JoinColumn(name = "shift", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private HkShiftEntity hkShiftEntity;

    public HkShiftRuleEntity() {
    }

    public HkShiftRuleEntity(HkShiftRuleEntityPK hkShiftRuleEntityPK) {
        this.hkShiftRuleEntityPK = hkShiftRuleEntityPK;
    }

    public HkShiftRuleEntity(HkShiftRuleEntityPK hkShiftRuleEntityPK, boolean isArchive) {
        this.hkShiftRuleEntityPK = hkShiftRuleEntityPK;
        this.isArchive = isArchive;
    }

    public HkShiftRuleEntity(long shift, String ruleType) {
        this.hkShiftRuleEntityPK = new HkShiftRuleEntityPK(shift, ruleType);
    }

    public HkShiftRuleEntityPK getHkShiftRuleEntityPK() {
        return hkShiftRuleEntityPK;
    }

    public void setHkShiftRuleEntityPK(HkShiftRuleEntityPK hkShiftRuleEntityPK) {
        this.hkShiftRuleEntityPK = hkShiftRuleEntityPK;
    }

    public Integer getDayCnt() {
        return dayCnt;
    }

    public void setDayCnt(Integer dayCnt) {
        this.dayCnt = dayCnt;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public BigInteger getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(BigInteger eventInstance) {
        this.eventInstance = eventInstance;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public HkShiftEntity getHkShiftEntity() {
        return hkShiftEntity;
    }

    public void setHkShiftEntity(HkShiftEntity hkShiftEntity) {
        this.hkShiftEntity = hkShiftEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hkShiftRuleEntityPK != null ? hkShiftRuleEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkShiftRuleEntity)) {
            return false;
        }
        HkShiftRuleEntity other = (HkShiftRuleEntity) object;
        if ((this.hkShiftRuleEntityPK == null && other.hkShiftRuleEntityPK != null) || (this.hkShiftRuleEntityPK != null && !this.hkShiftRuleEntityPK.equals(other.hkShiftRuleEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftRuleEntity[ hkShiftRuleEntityPK=" + hkShiftRuleEntityPK + " ]";
    }
    
}
