/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Mayank
 */
@Embeddable
public class HkShiftRuleEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(nullable = false)
    private long shift;
    @Basic(optional = false)
    @Column(name = "rule_type", nullable = false, length = 5)
    private String ruleType;

    public HkShiftRuleEntityPK() {
    }

    public HkShiftRuleEntityPK(long shift, String ruleType) {
        this.shift = shift;
        this.ruleType = ruleType;
    }

    public long getShift() {
        return shift;
    }

    public void setShift(long shift) {
        this.shift = shift;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) shift;
        hash += (ruleType != null ? ruleType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HkShiftRuleEntityPK)) {
            return false;
        }
        HkShiftRuleEntityPK other = (HkShiftRuleEntityPK) object;
        if (this.shift != other.shift) {
            return false;
        }
        if ((this.ruleType == null && other.ruleType != null) || (this.ruleType != null && !this.ruleType.equals(other.ruleType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argusoft.hkg.model.HkShiftRuleEntityPK[ shift=" + shift + ", ruleType=" + ruleType + " ]";
    }
    
}
