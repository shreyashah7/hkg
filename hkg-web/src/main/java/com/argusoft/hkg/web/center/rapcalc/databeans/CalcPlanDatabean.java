/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.databeans;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class CalcPlanDatabean {

    private String id;
    private String lot;
    private String packet;
    private Long createdBy;
    private Date createdOn;
    private Long lastModifiedBy;
    private String status;
    private String tag;
    private Double amount;
    private Double difference;
    private Double planTotal;
    private Double mixAmount;
    private Boolean isActive;
    private Boolean isArchive;
    private int sequence;
    private Date rDate;
    private Map<String, Object> planCustom;
    private Map<String, String> lotDbType;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the lot
     */
    public String getLot() {
        return lot;
    }

    /**
     * @param lot the lot to set
     */
    public void setLot(String lot) {
        this.lot = lot;
    }

    /**
     * @return the packet
     */
    public String getPacket() {
        return packet;
    }

    /**
     * @param packet the packet to set
     */
    public void setPacket(String packet) {
        this.packet = packet;
    }

    /**
     * @return the createdBy
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the lastModifiedBy
     */
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the difference
     */
    public Double getDifference() {
        return difference;
    }

    /**
     * @param difference the difference to set
     */
    public void setDifference(Double difference) {
        this.difference = difference;
    }

    /**
     * @return the PlanTotal
     */
    public Double getPlanTotal() {
        return planTotal;
    }

    /**
     * @param PlanTotal the PlanTotal to set
     */
    public void setPlanTotal(Double PlanTotal) {
        this.planTotal = PlanTotal;
    }

    /**
     * @return the mixAmount
     */
    public Double getMixAmount() {
        return mixAmount;
    }

    /**
     * @param mixAmount the mixAmount to set
     */
    public void setMixAmount(Double mixAmount) {
        this.mixAmount = mixAmount;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isArchive
     */
    public Boolean getIsArchive() {
        return isArchive;
    }

    /**
     * @param isArchive the isArchive to set
     */
    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }

    /**
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the rDate
     */
    public Date getrDate() {
        return rDate;
    }

    /**
     * @param rDate the rDate to set
     */
    public void setrDate(Date rDate) {
        this.rDate = rDate;
    }

    /**
     * @return the planCustom
     */
    public Map<String, Object> getPlanCustom() {
        return planCustom;
    }

    /**
     * @param planCustom the planCustom to set
     */
    public void setPlanCustom(Map<String, Object> planCustom) {
        this.planCustom = planCustom;
    }

    /**
     * @return the lotDbType
     */
    public Map<String, String> getLotDbType() {
        return lotDbType;
    }

    /**
     * @param lotDbType the lotDbType to set
     */
    public void setLotDbType(Map<String, String> lotDbType) {
        this.lotDbType = lotDbType;
    }

    @Override
    public String toString() {
        return "CalcPlanDatabean{" + "id=" + id + ", lot=" + lot + ", packet=" + packet + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", lastModifiedBy=" + lastModifiedBy + ", status=" + status + ", tag=" + tag + ", amount=" + amount + ", difference=" + difference + ", planTotal=" + planTotal + ", mixAmount=" + mixAmount + ", isActive=" + isActive + ", isArchive=" + isArchive + ", sequence=" + sequence + ", rDate=" + rDate + ", planCustom=" + planCustom + ", lotDbType=" + lotDbType + '}';
    }

}
