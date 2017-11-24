/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.center.core.SyncTransactionLogService;
import com.argusoft.sync.center.model.SyncTransactionLogDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncTransactionLogServiceImpl implements SyncTransactionLogService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SyncTransactionLogServiceImpl.class);
    private static final String TRANSACTION = "transactionId";
    private static final String RECEIVER = "receiverJid";
    private static final String CHAT_THREAD_ID = "chatThreadId";
    private static final String IS_SENT = "isSent";
    private static final String SENDER_JID = "senderJid";
    private static final String SENT_DATE = "sentDate";
    private static final String STATUS = "status";
    private static final String NO_OF_RETRY = "noOfRetry";
    private static final String IS_ACK_MSG = "isAckMsg";

    /**
     *
     */
    public static class TransactionStatus {

        public static final String DELIVERED = "Delivered";
        public static final String SENT = "Sent";
        public static final String XMPP_DOWN = "XmppDown";
        public static final String IN_PROCESS = "InProces";
        public static final String ERROR = "Error";
        public static final String ERROR_R = "Error(R)";
    }
    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public void save(SyncTransactionLogDocument transactionLogDocument) {
        mongoGenericDao.update(transactionLogDocument);
        LOGGER.debug("saved transactionLogDocument transaction id   " + transactionLogDocument);
    }

    @Override
    public SyncTransactionLogDocument retrieve(Long transactionId, String receiverJid) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(new Criteria().andOperator(Criteria.where(TRANSACTION).is(transactionId), (Criteria.where(RECEIVER).is(receiverJid))));
        SyncTransactionLogDocument syncTransactionLogDocument = (SyncTransactionLogDocument) mongoGenericDao.findOneByCriteria(criterias, SyncTransactionLogDocument.class);
        
        LOGGER.debug("end, syncTransactionLogDocument=" + syncTransactionLogDocument + ", transactionId" + transactionId + ", receiverJid" + receiverJid);

        return syncTransactionLogDocument;

    }

    @Override
    public void delete(Long transactionId, String receiverJid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("transactionId").is(transactionId).andOperator(Criteria.where("receiverJid").is(receiverJid)));
        mongoGenericDao.getMongoTemplate().findAndRemove(query, SyncTransactionLogDocument.class);
    }

    @Override
    public SyncTransactionLogDocument retrieveByChatThreadAndReceiverJid(String chatThreadId, String receiverJid) {
        List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where(CHAT_THREAD_ID).is(chatThreadId).andOperator(Criteria.where(RECEIVER).is(receiverJid)));

        return (SyncTransactionLogDocument) mongoGenericDao.findOneByCriteria(criterias, SyncTransactionLogDocument.class);
    }

    @Override
    public List<SyncTransactionLogDocument> retrievePendingTransactions(String receiverJid) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(new Criteria().andOperator(Criteria.where(IS_SENT).is(Boolean.FALSE), (Criteria.where(RECEIVER).is(receiverJid))));
        return mongoGenericDao.findByCriteria(criterias, SyncTransactionLogDocument.class);
    }

    @Override
    public List<SyncTransactionLogDocument> retrievePendingTransactions() {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(IS_SENT).is(Boolean.FALSE));
        return mongoGenericDao.findByCriteria(criterias, SyncTransactionLogDocument.class);
    }

    @Override
    public List<SyncTransactionLogDocument> retrieveTransactionsByCriteria(String status, String company, Date startDate, Date endDate) {
        List<Criteria> criterias = new ArrayList<>();
        Criteria criteria = where(IS_ACK_MSG).is(false);
        if (!StringUtils.isEmpty(status)) {
            criteria = where(STATUS).is(status);
        }
        if (startDate != null && endDate != null) {
            if (criteria != null) {
                criteria.andOperator(Criteria.where(SENT_DATE).gte(startDate), (where(SENT_DATE).lte(endDate)));
            } else {
              
//                criteria = andOperator(Criteria.where(SENT_DATE).gte(startDate), (where(SENT_DATE).lte(endDate)));
                criteria = new Criteria().andOperator(Criteria.where(SENT_DATE).gte(startDate), (where(SENT_DATE).lte(endDate)));
                    }

        }
        LOGGER.debug("company: " + company);
        if (!StringUtils.isEmpty(company)) {
            if (criteria != null) {
                criteria.orOperator(where(RECEIVER).is(company), where(SENDER_JID).is(company));
            } else {
                criteria = new Criteria().orOperator(where(RECEIVER).is(company), where(SENDER_JID).is(company));
            }

        }
        criterias.add(criteria);
        return mongoGenericDao.findByCriteria(criterias, SyncTransactionLogDocument.class);
    }
}
