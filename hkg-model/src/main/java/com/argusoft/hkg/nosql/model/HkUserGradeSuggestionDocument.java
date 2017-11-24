/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.hkg.nosql.model;

import org.bson.types.ObjectId;

/**
 *
 * @author dhwani
 */

public class HkUserGradeSuggestionDocument {

    private ObjectId packetId;
    private Long userId;
    private Integer currentStock;
    private Integer newStock;
    private Integer totalStock;
    private Long Grade;
    private Long goingToGrade;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoingToGrade() {
        return goingToGrade;
    }

    public void setGoingToGrade(Long goingToGrade) {
        this.goingToGrade = goingToGrade;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getNewStock() {
        return newStock;
    }

    public void setNewStock(Integer newStock) {
        this.newStock = newStock;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Long getGrade() {
        return Grade;
    }

    public void setGrade(Long Grade) {
        this.Grade = Grade;
    }

    public ObjectId getPacketId() {
        return packetId;
    }

    public void setPacketId(ObjectId packetId) {
        this.packetId = packetId;
    }

    @Override
    public String toString() {
        return "HkUserGradeSuggestionDocument{" + "packetId=" + packetId + ", userId=" + userId + ", currentStock=" + currentStock + ", newStock=" + newStock + ", totalStock=" + totalStock + ", Grade=" + Grade + ", goingToGrade=" + goingToGrade + '}';
    }
            
            
}
