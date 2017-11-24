/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;

import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;

/**
 *
 * @author shreya
 */
public class UserGradeSuggestionDataBean {

    private String packetObjectId;
    private String packetId;
    private Long userId;
    private String userName;
    private Integer currentStock;
    private Integer newStock;
    private Integer totalStock;
    private Long grade;
    private String gradeName;
    private Long goingToGrade;
    private String goingToGradeName;
    private Long actualUserId;
    private Boolean presence;
    private Integer leaveCount;
    private List<SelectItem> leaveData;

    public String getPacketObjectId() {
        return packetObjectId;
    }

    public void setPacketObjectId(String packetObjectId) {
        this.packetObjectId = packetObjectId;
    }

    public Long getActualUserId() {
        return actualUserId;
    }

    public void setActualUserId(Long actualUserId) {
        this.actualUserId = actualUserId;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGoingToGradeName() {
        return goingToGradeName;
    }

    public void setGoingToGradeName(String goingToGradeName) {
        this.goingToGradeName = goingToGradeName;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public List<SelectItem> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(List<SelectItem> leaveData) {
        this.leaveData = leaveData;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    @Override
    public String toString() {
        return "UserGradeSuggestionDataBean{" + "packetObjectId=" + packetObjectId + ", packetId=" + packetId + ", userId=" + userId + ", userName=" + userName + ", currentStock=" + currentStock + ", newStock=" + newStock + ", totalStock=" + totalStock + ", grade=" + grade + ", gradeName=" + gradeName + ", goingToGrade=" + goingToGrade + ", goingToGradeName=" + goingToGradeName + ", actualUserId=" + actualUserId + ", presence=" + presence + ", leaveCount=" + leaveCount + ", leaveData=" + leaveData + '}';
    }

}
