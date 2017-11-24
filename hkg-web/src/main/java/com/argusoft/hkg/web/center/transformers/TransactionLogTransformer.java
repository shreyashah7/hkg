/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncTransactionLogService;
import com.argusoft.hkg.sync.center.core.impl.SyncTransactionLogServiceImpl;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.center.usermanagement.databeans.MetadataDatabean;
import com.argusoft.hkg.web.center.usermanagement.databeans.TransactionLogDatabean;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.sync.listner.SyncRecordedEntity;
import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import com.argusoft.hkg.web.sync.xmpp.transformers.MasterTransactionLogTransformer;
import com.argusoft.sync.center.model.SyncCenterXmppUserDocument;
import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class TransactionLogTransformer {

    @Autowired
    private MetadataTransformer metadataTransformer;

    @Autowired
    private SyncTransactionLogService transactionLogService;
    @Autowired
    private ApplicationUtil applicationUtil;
    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private SyncXmppClient syncXmppClient;

    public TransactionLogDatabean convertTransactionLogDocumentToTransactionLogDataBean(SyncTransactionLogDocument logDocument, TransactionLogDatabean logDatabean) {
        if (logDatabean == null) {
            logDatabean = new TransactionLogDatabean();
        }
        logDatabean.setChatThreadId(logDocument.getChatThreadId());
        logDatabean.setErrorFile(logDocument.getErrorFile());
        logDatabean.setId(logDocument.getId());
        logDatabean.setIsSent(logDocument.getIsSent());
        logDatabean.setLastReadChar(logDocument.getLastReadChar());
        logDatabean.setMessageFile(logDocument.getMessageFile());
        logDatabean.setNoOfRetry(logDocument.getNoOfRetry());
        logDatabean.setReceiverJid(logDocument.getReceiverJid());
        logDatabean.setSenderJid(logDocument.getSenderJid());
        logDatabean.setSentDate(logDocument.getSentDate());
        logDatabean.setStatus(logDocument.getStatus());
        logDatabean.setTransactionId(logDocument.getTransactionId());
        logDatabean.setEntityMetadataList(metadataTransformer.convertMetadataDocumentListToDataBeanList(logDocument.getEntityMetadataList()));
        return logDatabean;
    }

    public List<TransactionLogDatabean> getAllTransactionLog() {
        List<? extends Object> retrieveAllDocuments = hkUMSyncService.retrieveAllDocuments(SyncTransactionLogDocument.class);
        if (retrieveAllDocuments != null) {
            return convertTransactionLogDocumentListToDatabeanList((List<SyncTransactionLogDocument>) retrieveAllDocuments);
        }
        return null;
    }

    public List<TransactionLogDatabean> getTransactionLogByCriteria(String status, String company, Date startDate, Date endDate) {

        List<? extends Object> retrieveAllDocuments = transactionLogService.retrieveTransactionsByCriteria(status, company, startDate, endDate);
        if (retrieveAllDocuments != null) {
            return convertTransactionLogDocumentListToDatabeanList((List<SyncTransactionLogDocument>) retrieveAllDocuments);
        }
        return null;
    }

    public List<TransactionLogDatabean> convertTransactionLogDocumentListToDatabeanList(List<SyncTransactionLogDocument> documents) {
        List<TransactionLogDatabean> databeans = null;
        if (documents != null) {
            databeans = new LinkedList<>();
            for (SyncTransactionLogDocument document : documents) {
                databeans.add(convertTransactionLogDocumentToTransactionLogDataBean(document, null));
            }
        }
        return databeans;
    }

    public List<String> getstatusList() {
        List<String> statuses = new LinkedList<>();
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.DELIVERED);
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.ERROR);
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.ERROR_R);
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.IN_PROCESS);
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.SENT);
        statuses.add(SyncTransactionLogServiceImpl.TransactionStatus.XMPP_DOWN);
        return statuses;
    }

    public List<String> getFranchiseList() {
        SyncCenterXmppUserDocument xmppUserDocument = (SyncCenterXmppUserDocument) hkUMSyncService.getDocumentById(1l, SyncCenterXmppUserDocument.class);
        List<String> items = new LinkedList<>();
        if (xmppUserDocument != null) {
            items.add(xmppUserDocument.getUserName() + "@" + WebApplicationInitializerConfig.XMPP_DOMAIN);
        }
        return items;
    }

    public void resend(List<TransactionLogDatabean> logDatabeans) {
        for (TransactionLogDatabean logDatabean : logDatabeans) {
            List<SyncRecordedEntity> recordedEntitys = new LinkedList<>();
            for (MetadataDatabean metadataDatabean : logDatabean.getEntityMetadataList()) {
                Object object = null;
                               if (metadataDatabean.isIsSqlEntity()) {

                } else {
//                    metadataDocument = metadataTransformer.convertMetadataDatabeanToMetadataDocument(metadataDatabean, null);
                    object = hkUMSyncService.retrieveObjectsFromMetadata(new ObjectId(metadataDatabean.getId()));
                    System.out.println("document ojectttt " + object);
                }
                SyncRecordedEntity recordedEntity = new SyncRecordedEntity();
                recordedEntity.setEntity(object);
                if (object != null) {
                    recordedEntity.setEntityName(object.getClass().getName());
                }
//                recordedEntity.setMetadataDocument(metadataDocument);
                recordedEntitys.add(recordedEntity);
            }
            SyncTransactionEntity transactionEntity = new SyncTransactionEntity();
            transactionEntity.setHkRecordedEntitys(recordedEntitys);
            transactionEntity.setTransactionId(logDatabean.getTransactionId());
            try {
                syncXmppClient.sendMessage(transactionEntity, logDatabean.getReceiverJid());
            } catch (XMPPException ex) {
                Logger.getLogger(MasterTransactionLogTransformer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SmackException.NotConnectedException ex) {
                Logger.getLogger(MasterTransactionLogTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
