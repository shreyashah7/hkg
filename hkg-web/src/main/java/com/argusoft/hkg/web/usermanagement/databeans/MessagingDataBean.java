/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.databeans;

import java.util.Date;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author piyush
 */
public class MessagingDataBean {

    private Long id;
    private String messageType;
    @NotNull(message="Message not entered..")
    @Size(max=500,message="Message size exceeded 500..")
    private String messageBody;
    private boolean isTemplate;
    private long franchise;
    private boolean isArchive;
    @NotNull(message="Recipient not entered..")
    private String nameRecipient;
    private String nameRecipientIds;
    private String shortMessage;
    private String createdBy;
    private Date createdOn;
    private Long messageObj;
    private boolean hasPriority;
    private boolean isAttended;
    private String status;
    private String copyMessage;
    private Map<String, Object> messageCustom;// for custom field data
    private Map<String, String> messageDbType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsAttended() {
        return isAttended;
    }

    public void setIsAttended(boolean isAttended) {
        this.isAttended = isAttended;
    }

    public String getCopyMessage() {
        return copyMessage;
    }

    public void setCopyMessage(String copyMessage) {
        this.copyMessage = copyMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessagingDataBean() {
    }

    public String getNameRecipientIds() {
        return nameRecipientIds;
    }

    public void setNameRecipientIds(String nameRecipientIds) {
        this.nameRecipientIds = nameRecipientIds;
    }

    public boolean isHasPriority() {
        return hasPriority;
    }

    public void setHasPriority(boolean hasPriority) {
        this.hasPriority = hasPriority;
    }

    public Long getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(Long messageObj) {
        this.messageObj = messageObj;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getNameRecipient() {
        return nameRecipient;
    }

    public void setNameRecipient(String nameRecipient) {
        this.nameRecipient = nameRecipient;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public long getFranchise() {
        return franchise;
    }

    public void setFranchise(long franchise) {
        this.franchise = franchise;
    }

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public Map<String, Object> getMessageCustom() {
        return messageCustom;
    }

    public void setMessageCustom(Map<String, Object> messageCustom) {
        this.messageCustom = messageCustom;
    }

    public Map<String, String> getMessageDbType() {
        return messageDbType;
    }

    public void setMessageDbType(Map<String, String> messageDbType) {
        this.messageDbType = messageDbType;
    }

    @Override
    public String toString() {
        return "MessagingDataBean{" + "id=" + id + ", messageType=" + messageType + ", messageBody=" + messageBody + ", isTemplate=" + isTemplate + ", franchise=" + franchise + ", isArchive=" + isArchive + ", nameRecipient=" + nameRecipient + ", nameRecipientIds=" + nameRecipientIds + ", shortMessage=" + shortMessage + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", messageObj=" + messageObj + ", hasPriority=" + hasPriority + ", isAttended=" + isAttended + ", status=" + status + ", copyMessage=" + copyMessage + ", messageCustom=" + messageCustom + ", messageDbType=" + messageDbType + '}';
    }
}
