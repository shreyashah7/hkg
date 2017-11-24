/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sync.center.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author shruti
 */
@Document(collection = "transactionlog")
public class SyncTransactionLogDocument {

    @Id
    private String id;
    private Long transactionId;
    private String receiverJid;
    private String senderJid;
    private Date sentDate;
    private String chatThreadId;
//    private Map<String, List<Map<String, Object>>> entityIdMap;
    private String messageFile;
    private Long lastReadChar;
    private Boolean isSent;
    private String status;
    @DBRef
    private List<MetadataDocument> entityMetadataList;
    private int noOfRetry;
    private String errorFile;
    private boolean isAckMsg;

    public SyncTransactionLogDocument() {
        this.isAckMsg = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverJid() {
        return receiverJid;
    }

    public void setReceiverJid(String receiverJid) {
        this.receiverJid = receiverJid;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getChatThreadId() {
        return chatThreadId;
    }

    public void setChatThreadId(String chatThreadId) {
        this.chatThreadId = chatThreadId;
    }

//    public Map<String, List<Map<String, Object>>> getEntityIdMap() {
//        return entityIdMap;
//    }
//
//    /**
//     * This map is used to store entity and its id values.
//      *
//     * @param entityIdMap
//     */
//    public void setEntityIdMap(Map<String, List<Map<String, Object>>> entityIdMap) {
//        this.entityIdMap = entityIdMap;
//    }
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessageFile() {
        return messageFile;
    }

    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public Long getLastReadChar() {
        return lastReadChar;
    }

    public void setLastReadChar(Long lastReadChar) {
        this.lastReadChar = lastReadChar;
    }

    public String getSenderJid() {
        return senderJid;
    }

    public void setSenderJid(String senderJid) {
        this.senderJid = senderJid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MetadataDocument> getEntityMetadataList() {
        return entityMetadataList;
    }

    public void setEntityMetadataList(List<MetadataDocument> entityMetadataList) {
        this.entityMetadataList = entityMetadataList;
    }

    public int getNoOfRetry() {
        return noOfRetry;
    }

    public void setNoOfRetry(int noOfRetry) {
        this.noOfRetry = noOfRetry;
    }

    @Override
    public String toString() {
        return "SyncTransactionLogDocument{" + "id=" + id + ", transactionId=" + transactionId + ", receiverJid=" + receiverJid + ", sentDate=" + sentDate + ", chatThreadId=" + chatThreadId + ", messageFile=" + messageFile + ", lastReadChar=" + lastReadChar + ", isSent=" + isSent + '}';
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

    /**
     *
     * @return
     */
    public boolean getIsAckMsg() {
        return isAckMsg;
    }

    public void setIsAckMsg(boolean isAckMsg) {
        this.isAckMsg = isAckMsg;
    }

}
