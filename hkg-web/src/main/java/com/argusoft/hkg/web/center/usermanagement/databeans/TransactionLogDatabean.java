/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.databeans;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shruti
 */
public class TransactionLogDatabean {
    private String id;
    private Long transactionId;
    private String receiverJid;
    private String senderJid;
    private Date sentDate;
    private String chatThreadId;
    private String messageFile;
    private Long lastReadChar;
    private Boolean isSent;
    private String status;

    private List<MetadataDatabean> entityMetadataList;
    private int noOfRetry;
    private String errorFile;
    String metadataString;
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
     * @return the transactionId
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the receiverJid
     */
    public String getReceiverJid() {
        return receiverJid;
    }

    /**
     * @param receiverJid the receiverJid to set
     */
    public void setReceiverJid(String receiverJid) {
        this.receiverJid = receiverJid;
    }

    /**
     * @return the senderJid
     */
    public String getSenderJid() {
        return senderJid;
    }

    /**
     * @param senderJid the senderJid to set
     */
    public void setSenderJid(String senderJid) {
        this.senderJid = senderJid;
    }

    /**
     * @return the sentDate
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * @param sentDate the sentDate to set
     */
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    /**
     * @return the chatThreadId
     */
    public String getChatThreadId() {
        return chatThreadId;
    }

    /**
     * @param chatThreadId the chatThreadId to set
     */
    public void setChatThreadId(String chatThreadId) {
        this.chatThreadId = chatThreadId;
    }

    /**
     * @return the messageFile
     */
    public String getMessageFile() {
        return messageFile;
    }

    /**
     * @param messageFile the messageFile to set
     */
    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }

    /**
     * @return the lastReadChar
     */
    public Long getLastReadChar() {
        return lastReadChar;
    }

    /**
     * @param lastReadChar the lastReadChar to set
     */
    public void setLastReadChar(Long lastReadChar) {
        this.lastReadChar = lastReadChar;
    }

    /**
     * @return the isSent
     */
    public Boolean getIsSent() {
        return isSent;
    }

    /**
     * @param isSent the isSent to set
     */
    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
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
     * @return the entityMetadataList
     */
    public List<MetadataDatabean> getEntityMetadataList() {
        return entityMetadataList;
    }

    /**
     * @param entityMetadataList the entityMetadataList to set
     */
    public void setEntityMetadataList(List<MetadataDatabean> entityMetadataList) {
        this.entityMetadataList = entityMetadataList;
    }

    /**
     * @return the noOfRetry
     */
    public int getNoOfRetry() {
        return noOfRetry;
    }

    /**
     * @param noOfRetry the noOfRetry to set
     */
    public void setNoOfRetry(int noOfRetry) {
        this.noOfRetry = noOfRetry;
    }

    /**
     * @return the errorFile
     */
    public String getErrorFile() {
        return errorFile;
    }

    /**
     * @param errorFile the errorFile to set
     */
    public void setErrorFile(String errorFile) {
        this.errorFile = errorFile;
    }
}
