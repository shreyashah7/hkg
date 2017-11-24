package com.argusoft.hkg.web.sync.mongotransaction;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.sync.xmppclient.SyncXmppClient;
import com.argusoft.hkg.web.center.util.ApplicationUtil;
import com.argusoft.hkg.web.sync.listner.SyncTransactionEntity;
import com.argusoft.hkg.web.sync.listner.SyncTransactionLog;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.lang.reflect.Method;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoDbUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;

/**
 * Aspect class to intercept request to any method that have used
 * SyncMongoTransaction annotation
 *
 * @author rajkumar
 */
@Service
@Aspect
public class SyncMongoTransactionAspect {

    private static final Logger log = LoggerFactory.getLogger(SyncMongoTransactionAspect.class);

    static int index = 1;
    static Long franchiseId;

    @Autowired
    private MongoGenericDao mongoGenericDao;
    @Autowired
    private SyncXmppClient xmppClient;
    @Autowired
    ApplicationUtil applicationUtil;

    @PostConstruct
    public void init() {
        if (applicationUtil.getCenterFranchiseDataBean() == null) {
            franchiseId = 0l;
        } else {
            franchiseId = applicationUtil.getCenterFranchiseDataBean().getFranchiseId();
        }
    }

    public static class TransactionContext {

        private static final ThreadLocal<Object> contextHolder = new ThreadLocal<>();

        /**
         * This method returns the JSON request object value and clears the
         * context upon first retrieval
         * @return
         */
        public static Object get() {
            return contextHolder.get();
        }

        public static void set(Object value) {
            if (value != null) {
                contextHolder.set(value);
            }
        }

        public static void remove() {
            contextHolder.remove();
        }
    }

    @Around("@annotation(com.argusoft.generic.core.mongotransaction.MongoTransaction)")
    public Object performTransaction(ProceedingJoinPoint pjp) throws Throwable {
        long transactionid = new Long(new Date().getTime() + "" + franchiseId + "" + index);
//        log.info("~~~~~~~~Start Transaction~~~~~~~~~~~~~ : " + transactionid);

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        MongoTransaction tx = method.getAnnotation(MongoTransaction.class);
        SyncTransactionLog.initializePerTransactionRecordedEntities(transactionid);
        TransactionContext.set(transactionid);
        index++;
        beginMongoTransaction(tx);
        Object proceed = null;
        try {
            proceed = pjp.proceed();
            commitMongoTransaction();
            if (!SyncTransactionLog.isEmptyHkRecordedEntityList((Long) TransactionContext.get())) {
                SyncTransactionEntity hkTransactionEntity = new SyncTransactionEntity();
                hkTransactionEntity.setTransactionId((Long) TransactionContext.get());
                hkTransactionEntity.setIsSql(false);
                hkTransactionEntity.setIsAcknowledge(false);
                hkTransactionEntity.setHkRecordedEntitys(SyncTransactionLog.getPerTransactionRecordedEntities().get((Long) TransactionContext.get()));
                xmppClient.sendMessage(hkTransactionEntity, null);
            }
        } catch (Exception e) {
            rollbackMongoTransaction();
            e.printStackTrace();
            throw e;
        } finally {

            SyncTransactionLog.flushMap((Long) TransactionContext.get());
//            log.debug("---transactionId----log transsaction after successfull commit-----\n" + TransactionContext.get() + "\n" + SyncTransactionLog.getPerTransactionRecordedEntities());
//            log.info("~~~~~~~~End Transaction~~~~~~~~~~~~~");
            TransactionContext.remove();

            return proceed;
        }

    }

    private void beginMongoTransaction(MongoTransaction tx) throws TransactionException {
        BasicDBObjectBuilder builder = new BasicDBObjectBuilder().add("beginTransaction", Boolean.TRUE);

        switch (tx.isolation().value()) {
            case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
                builder.append("isolation", "readUncommitted");
                break;
            case TransactionDefinition.ISOLATION_SERIALIZABLE:
                builder.append("isolation", "serializable");
                break;
            case TransactionDefinition.ISOLATION_DEFAULT:
                builder.append("isolation", "mvcc");
                break;
            default:
                throw new InvalidIsolationLevelException("the requested isolation level " + tx.isolation().value()
                        + " is not supported by tokuMx.");
        }
        DBObject command = builder.get();
        CommandResult result = null;
        DB mongoDB = null;
        try {
            mongoDB = mongoGenericDao.getMongoTemplate().getDb();
            mongoDB.requestStart();
//            if (requestEnsureConnection) {
//                mongoDB.requestEnsureConnection();
//            }
            result = mongoDB.command(command);
        } catch (RuntimeException ex) {
            MongoDbUtils.closeDB(mongoDB);
            // use MongoExceptionTranslator?
            throw new TransactionSystemException("tokuMx.doBegin: unexpected system exception: " + ex.getMessage(), ex);
        }
        String error = result.getErrorMessage();
        if (error != null) {
            MongoDbUtils.closeDB(mongoDB);
            throw new CannotCreateTransactionException("execution of " + command.toString() + " failed: " + error);
        }
        log.trace("tokuMx.doBegin: {}", command);
    }

    private void commitMongoTransaction() throws TransactionException {
        DBObject command = new BasicDBObject("commitTransaction", Boolean.TRUE);
        CommandResult result = null;
        DB mongoDB = null;
        try {
            mongoDB = mongoGenericDao.getMongoTemplate().getDb();
            result = mongoDB.command(command);
        } catch (RuntimeException ex) {
            // use MongoExceptionTranslator?
            throw new TransactionSystemException("tokuMx.doCommit: unexpected system exception: " + ex.getMessage(), ex);
        } finally {
            MongoDbUtils.closeDB(mongoDB);
        }
        String error = result.getErrorMessage();
        if (error != null) {
            throw new TransactionSystemException("tokuMx.doCommit: execution of " + command.toString() + " failed: " + error);
        }
        log.trace("tokuMx.doCommit: {}", command);
    }

    private void rollbackMongoTransaction() throws TransactionException {
        DBObject command = new BasicDBObject("rollbackTransaction", Boolean.TRUE);
        CommandResult result = null;
        DB mongoDB = null;
        try {
            mongoDB = mongoGenericDao.getMongoTemplate().getDb();
            result = mongoDB.command(command);
        } catch (RuntimeException ex) {
            // use MongoExceptionTranslator?
            throw new TransactionSystemException("tokuMx.doRollback: unexpected system exception: " + ex.getMessage(), ex);
        } finally {
            MongoDbUtils.closeDB(mongoDB);
        }
        String error = result.getErrorMessage();
        if (error != null) {
            throw new TransactionSystemException("tokuMx.doRollback: execution of " + command.toString() + " failed: "
                    + error);
        }
        log.trace("tokuMx.doRollback: {}", command);
    }
}
