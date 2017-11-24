/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.sync.listner;

import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.sync.xmpp.util.SyncEntityDocumentMapper;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.sync.center.model.AuditTrail;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author brijesh
 */
public class SyncSqlInterceptor extends EmptyInterceptor {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncSqlInterceptor.class);
    private static final long serialVersionUID = 1L;
    @Autowired
    private SessionFactory sessionFactory;
    private final Gson gson;
    @Autowired
    private SyncXmppClient xmppClient;
    @Autowired
    private SyncEntityDocumentMapper entityDocumentMapper;

    @Autowired
    private HkUMSyncService uMSyncService;

//    @Autowired
//    private LoginDataBean loginDataBean;

    public SyncSqlInterceptor() {
        this.gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                if (field.getAnnotation(OneToMany.class) != null || field.getAnnotation(OneToOne.class) != null || field.getAnnotation(ManyToMany.class) != null || field.getAnnotation(ManyToOne.class) != null) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> arg0) {
                return false;
            }
        }).create();

    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
//        LOGGER.debug("---afterTransactionBegin---sessionFactory Object----tx object--" + tx.hashCode());
        SyncTransactionLog.initializePerTransactionRecordedEntities((long) tx.hashCode());
    }

    @Override
    public boolean onFlushDirty(Object o, Serializable srlzbl, Object[] os, Object[] os1, String[] strings, org.hibernate.type.Type[] types) throws CallbackException {

//            LOGGER.debug("--onFlushDirty:----------" + o);
//            List<SyncTransformerInterface> transformerInterfaceList = entityDocumentMapper.getTransformerInstance(o.getClass());
        if (entityDocumentMapper.containsEntityMapping(o.getClass())) {
//                Object documentObject = transformerInterface.convertEntityToDocument(o);
//                if (transformerInterface.getSyncTransferType() != SyncTransferType.NONE && SyncTransactionLog.getPerTransactionRecordedEntities().containsKey(sessionFactory.getCurrentSession().getTransaction().hashCode())) {
            if (SyncTransactionLog.getPerTransactionRecordedEntities().containsKey((long) sessionFactory.getCurrentSession().getTransaction().hashCode())) {
                SyncRecordedEntity hkRecordedEntity = new SyncRecordedEntity();
                hkRecordedEntity.setEntity(o);
//                    hkRecordedEntity.setEntityName(documentObject.getClass().getName());
                hkRecordedEntity.setIsDirty(true);
                SyncTransactionLog.getPerTransactionRecordedEntities().get((long) sessionFactory.getCurrentSession().getTransaction().hashCode()).add(hkRecordedEntity);

            } else {
//                    LOGGER.debug("--onFlushDirty:----transaction id is not find in catalog------");
            }
        }
        uMSyncService.saveOrUpdateDocument(new AuditTrail(gson.toJson(o), new Date(), o.getClass().getName(), false));
        return false;
    }

    @Override
    public boolean onSave(Object o, Serializable srlzbl, Object[] os, String[] strings, org.hibernate.type.Type[] types) throws CallbackException {

//            LOGGER.debug("---onSave---" + o.getClass().getSimpleName());
//            List<SyncTransformerInterface> transformerInterface = entityDocumentMapper.getTransformerInstance(o.getClass());
        if (entityDocumentMapper.containsEntityMapping(o.getClass())) {

//                LOGGER.debug("transformerInterface found==" + o.getClass().getSimpleName());
//                Object documentObject = transformerInterface.convertEntityToDocument(o);
//                LOGGER.debug("transformerInterface.getSyncTransferType() != SyncTransferType.NONE  " + (transformerInterface.getSyncTransferType() != SyncTransferType.NONE));
//                if (transformerInterface.getSyncTransferType() != SyncTransferType.NONE && SyncTransactionLog.getPerTransactionRecordedEntities().containsKey(sessionFactory.getCurrentSession().getTransaction().hashCode())) {
            if (SyncTransactionLog.getPerTransactionRecordedEntities().containsKey((long) sessionFactory.getCurrentSession().getTransaction().hashCode())) {
//                    LOGGER.debug("getPerTransactionRecordedEntities found==" + o.getClass().getSimpleName());
                SyncRecordedEntity hkRecordedEntity = new SyncRecordedEntity();
                hkRecordedEntity.setEntity(o);
//                    hkRecordedEntity.setEntityName(documentObject.getClass().getName());
                hkRecordedEntity.setIsDirty(false);
                SyncTransactionLog.getPerTransactionRecordedEntities().get((long) sessionFactory.getCurrentSession().getTransaction().hashCode()).add(hkRecordedEntity);
            } else {
                LOGGER.info("--onSave:----No transaction id found in catalog------");

            }
        }
        uMSyncService.saveOrUpdateDocument(new AuditTrail(gson.toJson(o), new Date(), o.getClass().getName(), false));
        return false;
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {

        if (tx.wasCommitted()) {
//            LOGGER.debug("!SyncTransactionLog.isEmptyHkRecordedEntityList((long) tx.hashCode())=" + !SyncTransactionLog.isEmptyHkRecordedEntityList((long) tx.hashCode()));
            if (!SyncTransactionLog.isEmptyHkRecordedEntityList((long) tx.hashCode())) {
                try {
                    SyncTransactionEntity hkTransactionEntity = new SyncTransactionEntity();
                    hkTransactionEntity.setTransactionId((long) tx.hashCode());
                    hkTransactionEntity.setIsSql(true);
                    hkTransactionEntity.setIsAcknowledge(false);
                    hkTransactionEntity.setHkRecordedEntitys(SyncTransactionLog.getPerTransactionRecordedEntities().get((long) tx.hashCode()));
                    xmppClient.sendMessage(hkTransactionEntity, null);
                } catch (XMPPException | SmackException.NotConnectedException ex) {
                    Logger.getLogger(SyncSqlInterceptor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        SyncTransactionLog.flushMap((long) tx.hashCode());
//        LOGGER.debug("----transactionId--catalog----" + (long) tx.hashCode() + "-----------" + SyncTransactionLog.getPerTransactionRecordedEntities()
//        );

    }

    @Override
    public void onCollectionRecreate(Object o, Serializable srlzbl) throws CallbackException {
//        LOGGER.debug("-------------onCollectionRecreate:------------- ");
    }

    @Override
    public void postFlush(Iterator itrtr) throws CallbackException {
//        LOGGER.debug("-------------postFlusCh:------------- " + itrtr);
    }

    @Override
    public void beforeTransactionCompletion(Transaction t) {
//        LOGGER.debug("-------------beforeTransactionCompletion:------------- " + t.getLocalStatus().name());
    }

    @Override
    public boolean onLoad(Object o, Serializable srlzbl, Object[] os, String[] strings, org.hibernate.type.Type[] types) throws CallbackException {
//        LOGGER.debug("-------------onLoad:------------- " + o);
        return false;
    }

    @Override
    public void onDelete(Object o, Serializable srlzbl, Object[] os, String[] strings, org.hibernate.type.Type[] types) throws CallbackException {
//        LOGGER.debug("-------------onDelete:------------- ");
    }

    @Override
    public void onCollectionRemove(Object o, Serializable srlzbl) throws CallbackException {
//        LOGGER.debug("-------------onCollectionRemove:------------- ");
    }

    @Override
    public void onCollectionUpdate(Object o, Serializable srlzbl) throws CallbackException {
//        LOGGER.debug("-------------onCollectionUpdate:------------- ");
    }

    @Override
    public void preFlush(Iterator itrtr) throws CallbackException {
//        LOGGER.debug("-------------preFlush:------------- ");
    }

//    @Override
//    public void setBeanFactory(BeanFactory bf) throws BeansException {
//        this.beanFactory = bf;
//    }
}
