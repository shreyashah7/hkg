/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import java.util.Date;
import java.util.List;

/**
 *
 * @author shruti
 */
public interface SyncTransactionLogService {

    public void save(SyncTransactionLogDocument transactionLogDocument);

    public SyncTransactionLogDocument retrieve(Long transactionId, String receiverJid);

    public SyncTransactionLogDocument retrieveByChatThreadAndReceiverJid(String chatThreadId, String receiverJid);

    public void delete(Long transactionId, String receiverJid);

    public List<SyncTransactionLogDocument> retrievePendingTransactions(String receiverJid);

    public List<SyncTransactionLogDocument> retrievePendingTransactions();
    public List<SyncTransactionLogDocument> retrieveTransactionsByCriteria(String status, String company, Date startDate, Date endDate);
}
