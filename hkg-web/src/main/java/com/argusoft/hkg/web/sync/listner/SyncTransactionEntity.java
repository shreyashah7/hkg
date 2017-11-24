/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.listner;

import java.util.Date;
import java.util.List;

/**
 *
 * @author brijesh
 */
public class SyncTransactionEntity {

    private Long id;
    private Long transactionId;
    private boolean isSql;
    private boolean isAcknowledge = false;
    private Date timestamp;
    private List<SyncRecordedEntity> hkRecordedEntitys;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIsSql() {
        return isSql;
    }

    public void setIsSql(boolean isSql) {
        this.isSql = isSql;
    }

    public boolean isIsAcknowledge() {
        return isAcknowledge;
    }

    public void setIsAcknowledge(boolean isAcknowledge) {
        this.isAcknowledge = isAcknowledge;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public List<SyncRecordedEntity> getHkRecordedEntitys() {
        return hkRecordedEntitys;
    }

    public void setHkRecordedEntitys(List<SyncRecordedEntity> hkRecordedEntitys) {
        this.hkRecordedEntitys = hkRecordedEntitys;
    }

    @Override
    public String toString() {
        return "HkTransactionEntity{" + "id=" + id + ", transactionId=" + transactionId + ", isSql=" + isSql + ", isAcknowledge=" + isAcknowledge + ", timestamp=" + timestamp + ", hkRecordedEntitys=" + hkRecordedEntitys + '}';
    }

}
