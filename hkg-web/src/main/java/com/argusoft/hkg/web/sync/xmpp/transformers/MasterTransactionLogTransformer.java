/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.xmpp.transformers;

import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import com.argusoft.hkg.sync.SyncGeneralService;
import com.argusoft.hkg.sync.SyncXmppUserService;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.center.core.SyncTransactionLogService;
import com.argusoft.hkg.sync.center.core.impl.SyncTransactionLogServiceImpl;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.center.transformers.MetadataTransformer;
import com.argusoft.hkg.web.center.usermanagement.databeans.MetadataDatabean;
import com.argusoft.hkg.web.center.usermanagement.databeans.TransactionLogDatabean;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.sync.listner.SyncRecordedEntity;
import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import com.argusoft.sync.center.model.MetadataDocument;
import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
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
public class MasterTransactionLogTransformer {

    @Autowired
    private MetadataTransformer metadataTransformer;
    @Autowired
    private SyncXmppClient xmppClient;
    @Autowired
    private SyncTransactionLogService transactionLogService;

    @Autowired
    private HkUMSyncService hkUMSyncService;
    @Autowired
    private SyncGeneralService generalService;
    @Autowired
    private SyncXmppUserService xmppUserService;

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
    public void resend(List<TransactionLogDatabean> logDatabeans) {
        for (TransactionLogDatabean logDatabean : logDatabeans) {
            List<SyncRecordedEntity> recordedEntitys = new LinkedList<>();
            for (MetadataDatabean metadataDatabean : logDatabean.getEntityMetadataList()) {
                Object object = null;
                MetadataDocument metadataDocument = null;
                if (metadataDatabean.isIsSqlEntity()) {
                    metadataDocument = metadataTransformer.convertMetadataDatabeanToMetadataDocument(metadataDatabean, null);
                    object = generalService.retrueveObjectsFromMetadata(metadataDocument);
                    System.out.println("objectttttttttt " + object);
                } else {
//                    metadataDocument = metadataTransformer.convertMetadataDatabeanToMetadataDocument(metadataDatabean, null);
                    object = hkUMSyncService.retrieveObjectsFromMetadata(new ObjectId(metadataDatabean.getId()));
                    System.out.println("document ojectttt " + object);
                }
                SyncRecordedEntity recordedEntity = new SyncRecordedEntity();
                recordedEntity.setEntity(object);
                recordedEntity.setEntityName(object.getClass().getName());
//                recordedEntity.setMetadataDocument(metadataDocument);
                recordedEntitys.add(recordedEntity);
            }
            SyncTransactionEntity transactionEntity = new SyncTransactionEntity();
            transactionEntity.setHkRecordedEntitys(recordedEntitys);
            transactionEntity.setTransactionId(logDatabean.getTransactionId());
            try {
                xmppClient.sendMessage(transactionEntity, logDatabean.getReceiverJid());
            } catch (XMPPException ex) {
                Logger.getLogger(MasterTransactionLogTransformer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SmackException.NotConnectedException ex) {
                Logger.getLogger(MasterTransactionLogTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        List<SyncXmppUser> retrieveAllActiveUsers = xmppUserService.retrieveAllActiveUsers();
        List<String> items = new LinkedList<>();
        for (SyncXmppUser xmppUser : retrieveAllActiveUsers) {
            items.add(xmppUser.getUserName() + "@" + WebApplicationInitializerConfig.XMPP_DOMAIN);
        }

        return items;
    }
    public void downloadDocument(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename =" + fileName.substring(fileName.indexOf("#") + 1, fileName.length()));
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
//            System.out.println("The total file name ....." + pathFromImageName);
            Path pathVarible = Paths.get(pathFromImageName);
            byte[] data = Files.readAllBytes(pathVarible);
            response.getOutputStream().write(data);
        } catch (IOException e) {
        }
    }
}
