/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;


import java.util.Date;

/**
 *
 * @author shreya
 */
public class TrackStockActivityDataBean {
    
    private String activityName;
    private Long activityId;
    private Long nodeId;
    private String serviceName;
    private String assignedToName;
    private Long assignedToId;
    private Date assignedOn;
    private Date completedOn;
    private Date terminatedOn;
    private String status;
    private Boolean isReRouted;
    private Long activityVersion;
    private Integer lotOrPacketCount;

    public TrackStockActivityDataBean() {
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Long getActivityVersion() {
        return activityVersion;
    }

    public void setActivityVersion(Long activityVersion) {
        this.activityVersion = activityVersion;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }

    public Date getAssignedOn() {
        return assignedOn;
    }

    public void setAssignedOn(Date assignedOn) {
        this.assignedOn = assignedOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Date getTerminatedOn() {
        return terminatedOn;
    }

    public void setTerminatedOn(Date terminatedOn) {
        this.terminatedOn = terminatedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsReRouted() {
        return isReRouted;
    }

    public void setIsReRouted(Boolean isReRouted) {
        this.isReRouted = isReRouted;
    }

    public Integer getLotOrPacketCount() {
        return lotOrPacketCount;
    }

    public void setLotOrPacketCount(Integer lotOrPacketCount) {
        this.lotOrPacketCount = lotOrPacketCount;
    }

    @Override
    public String toString() {
        return "TrackStockActivityDataBean{" + "activityName=" + activityName + ", activityId=" + activityId + ", nodeId=" + nodeId + ", serviceName=" + serviceName + ", assignedToName=" + assignedToName + ", assignedToId=" + assignedToId + ", assignedOn=" + assignedOn + ", completedOn=" + completedOn + ", terminatedOn=" + terminatedOn + ", status=" + status + ", isReRouted=" + isReRouted + ", activityVersion=" + activityVersion + ", lotOrPacketCount=" + lotOrPacketCount + '}';
    }
    
}
