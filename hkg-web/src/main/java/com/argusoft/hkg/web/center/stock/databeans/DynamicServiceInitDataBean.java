/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.databeans;


import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author siddharth
 */
public class DynamicServiceInitDataBean {

    private Long id;
    private Long nodeId;
    private String nodeName;
    private Long groupId;
    private String groupName;
    private String modifier;
    private Long diamondsInQueue;
    private List<SelectItem> selectItems;
    private List<DynamicServiceInitDataBean> dynamicServiceInitDataBeans;
    private List<String> mandatoryFields;
    private Map<Long, List<String>> nodeAndWorkAllocationIds;
    private Map<Long, Integer> nodeIdPlansToBeSubmitted;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Long getDiamondsInQueue() {
        return diamondsInQueue;
    }

    public void setDiamondsInQueue(Long diamondsInQueue) {
        this.diamondsInQueue = diamondsInQueue;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public List<DynamicServiceInitDataBean> getDynamicServiceInitDataBeans() {
        return dynamicServiceInitDataBeans;
    }

    public void setDynamicServiceInitDataBeans(List<DynamicServiceInitDataBean> dynamicServiceInitDataBeans) {
        this.dynamicServiceInitDataBeans = dynamicServiceInitDataBeans;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public void setMandatoryFields(List<String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public Map<Long, List<String>> getNodeAndWorkAllocationIds() {
        return nodeAndWorkAllocationIds;
    }

    public void setNodeAndWorkAllocationIds(Map<Long, List<String>> nodeAndWorkAllocationIds) {
        this.nodeAndWorkAllocationIds = nodeAndWorkAllocationIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Long, Integer> getNodeIdPlansToBeSubmitted() {
        return nodeIdPlansToBeSubmitted;
    }

    public void setNodeIdPlansToBeSubmitted(Map<Long, Integer> nodeIdPlansToBeSubmitted) {
        this.nodeIdPlansToBeSubmitted = nodeIdPlansToBeSubmitted;
    }

    @Override
    public String toString() {
        return "DynamicServiceInitDataBean{" + "nodeId=" + nodeId + ", nodeName=" + nodeName + ", groupId=" + groupId + ", groupName=" + groupName + ", modifier=" + modifier + ", diamondsInQueue=" + diamondsInQueue + ", selectItems=" + selectItems + ", dynamicServiceInitDataBeans=" + dynamicServiceInitDataBeans + ", mandatoryFields=" + mandatoryFields + ", nodeAndWorkAllocationIds=" + nodeAndWorkAllocationIds + '}';
    }

}
