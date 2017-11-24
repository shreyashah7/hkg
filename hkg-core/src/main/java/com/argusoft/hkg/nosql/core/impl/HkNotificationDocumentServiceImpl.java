/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import com.argusoft.sync.center.model.HkNotificationConfigrationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationConfigurationDocument;
import com.argusoft.sync.center.model.HkNotificationDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author akta
 */
@Service
public class HkNotificationDocumentServiceImpl implements com.argusoft.hkg.nosql.core.HkNotificationDocumentService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static class HkNotificationConfigurationRecepientField {

        public static final String NOTIFICATION_CONFIGURATION = "hkNotificationConfigurationDocument.$id";
    }

    public static class HkNotificationConfigurationField {

        public static final String ACTIVITY_NODE = "activityNode";
    }

    public static class HkNotificationRecipientField {

        public static final String NOTIFICATION_ID = "hkNotificationRecipientEntityPK.notification";
        public static final String FRANCHISE = "hkNotificationEntity.franchise";
        public static final String ON_DATE = "hkNotificationEntity.onDate";
        public static final String IS_SEEN = "isSeen";
        public static final String STATUS = "status";
        public static final String FOR_USER = "hkNotificationRecipientEntityPK.forUser";
        public static final String LAST_MODIFIED_ON = "lastModifiedOn";
        public static final String SEEN_ON = "seenOn";
        public static final String NOTIFICATION_CONFIGURATION_TYPE = "hkNotificationEntity.notificationConfigurationType";
    }

    public static class HkMessageRecipientDtlField {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String HAS_PRIORITY = "hasPriority";
        private static final String MESSAGE_TO = "hkMessageRecipientDtlPK.messageTo";
        private static final String IS_ATTENDED = "isAttended";
        private static final String MESSAGE_FROM = "messageFrom";
        private static final String STATUS = "status";
        private static final String MESSAGE_OBJ = "hkMessageRecipientDtlPK.messageObj";
    }

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public int retrieveUnseenNotificationCount(Long userId, Long franchise) {
        List<Criteria> criterias = new ArrayList<>();
//        if (franchise != null) {
//            criterias.add(Criteria.where(HkNotificationRecipientField.FRANCHISE).is(franchise));
//        }
        criterias.add(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
        criterias.add(Criteria.where(HkNotificationRecipientField.IS_SEEN).is(false));
        criterias.add(Criteria.where(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE).is(HkSystemConstantUtil.NotificationSendType.WEB));
        return mongoGenericDao.count(criterias, HkNotificationRecipientDocument.class).intValue();
    }

    @Override
    public List<HkNotificationRecipientDocument> retrieveNotificationsForPopup(Long userId, Long franchise) {
        Query query = new Query();
        query.addCriteria(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
        query.addCriteria(Criteria.where(HkNotificationRecipientField.IS_SEEN).is(false));
        query.addCriteria(Criteria.where(HkNotificationRecipientField.STATUS).is(HkSystemConstantUtil.ACTIVE));
        query.addCriteria(Criteria.where(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE).is(HkSystemConstantUtil.NotificationSendType.WEB));
        query.addCriteria(Criteria.where(HkNotificationRecipientField.ON_DATE).lte(new Date()));

        //As in center side only franchise specific notifications will be there no need to use company id
//        if (franchise != null) {
//            query.addCriteria(Criteria.where(HkNotificationRecipientField.FRANCHISE).is(franchise));
//        }
        query.with(new Sort(Sort.Direction.ASC, HkNotificationRecipientField.IS_SEEN));
        query.with(new Sort(Sort.Direction.DESC, HkNotificationRecipientField.ON_DATE));

        //  if count of above criteria is less than 10, remove is seen criteria and return 10 results
        if (mongoGenericDao.getMongoTemplate().count(query, HkNotificationRecipientDocument.class) < 10) {
//            LOGGER.debug("mongoGenericDao.getMongoTemplate().count(query, HkNotificationRecipientDocument.class)=" + mongoGenericDao.getMongoTemplate().count(query, HkNotificationRecipientDocument.class));
            query = new Query();
            query.addCriteria(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
            query.addCriteria(Criteria.where(HkNotificationRecipientField.STATUS).is(HkSystemConstantUtil.ACTIVE));
//As in center side only franchise specific notifications will be there no need to use company id
//            if (franchise != null) {
//                query.addCriteria(Criteria.where(HkNotificationRecipientField.FRANCHISE).is(franchise));
//            }
            query.with(new Sort(Sort.Direction.ASC, HkNotificationRecipientField.IS_SEEN));
            query.with(new Sort(Sort.Direction.DESC, HkNotificationRecipientField.ON_DATE));
        }
        query.limit(10);
        //  if count is greater than 10 of unread notifications, no need to set any limit to results.
        List<HkNotificationRecipientDocument> resultList = mongoGenericDao.getMongoTemplate().find(query, HkNotificationRecipientDocument.class);
        if (!CollectionUtils.isEmpty(resultList)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Gson gson = new Gson();
            for (HkNotificationRecipientDocument recipient : resultList) {
                Map<String, String> descriptionParams = gson.fromJson(recipient.getHkNotificationEntity().getDescription(), type);
                if (!CollectionUtils.isEmpty(descriptionParams)) {
                    for (String key : descriptionParams.keySet()) {
                        String paramValue = descriptionParams.get(key);
                        if (paramValue.startsWith(HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR)) {
                            paramValue = HkSystemConstantUtil.dateFormatter.format(
                                    new Date(new Long(paramValue.substring(HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR.length()))));
                            descriptionParams.put(key, paramValue);
                        }
                    }
                    recipient.getHkNotificationEntity().setDescription(gson.toJson(descriptionParams));
                }
            }
        }
        return resultList;
    }

    @Override
    @MongoTransaction
    public void markUserNotificationsAsSeen(Long userId, List<String> notificationIds, Long franchise) {

//        Query query = new Query();
//        query.addCriteria(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
//        query.addCriteria(Criteria.where(HkNotificationRecipientField.NOTIFICATION_ID).in(notificationIds));
//        query.addCriteria(Criteria.where(HkNotificationRecipientField.IS_SEEN).is(false));
//        Update update = new Update();
//        update.set(HkNotificationRecipientField.IS_SEEN, true);
//        update.set(HkNotificationRecipientField.SEEN_ON, new Date());
//        mongoGenericDao.getMongoTemplate().updateMulti(query, update, HkNotificationRecipientDocument.class);
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
        criterias.add(Criteria.where(HkNotificationRecipientField.NOTIFICATION_ID).in(notificationIds));
        criterias.add(Criteria.where(HkNotificationRecipientField.IS_SEEN).is(false));
        List<HkNotificationRecipientDocument> resultList = mongoGenericDao.findByCriteria(criterias, HkNotificationRecipientDocument.class);
        for (HkNotificationRecipientDocument notificationRecDoct : resultList) {
            notificationRecDoct.setIsSeen(true);
            notificationRecDoct.setSeenOn(new Date());
            notificationRecDoct.setLastModifiedOn(new Date());
            mongoGenericDao.update(notificationRecDoct);
        }
//        mongoGenericDao.createAll(resultList);
    }

    @Override
    public List<HkNotificationRecipientDocument> retrieveNotificationsForUser(Long userId, Boolean isSeen, Date afterDate, Long franchise) {
        Query query = new Query();
        query.addCriteria(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
        query.addCriteria(Criteria.where(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE).is(HkSystemConstantUtil.NotificationSendType.WEB));
        if (isSeen != null) {
            query.addCriteria(Criteria.where(HkNotificationRecipientField.IS_SEEN).is(isSeen));
        }
        if (afterDate != null) {
            query.addCriteria(Criteria.where(HkNotificationRecipientField.ON_DATE).gte(afterDate).lte(new Date()));
        } else {
            query.addCriteria(Criteria.where(HkNotificationRecipientField.ON_DATE).lte(new Date()));
        }
        //As in center side only franchise specific notifications will be there no need to use company id
//        if (franchise != null) {
//             query.addCriteria(Criteria.where(HkNotificationRecipientField.ON_DATE).is(afterDate));
//            search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
//        }
        query.addCriteria(Criteria.where(HkNotificationRecipientField.STATUS).is(HkSystemConstantUtil.ACTIVE));
        query.with(new Sort(Sort.Direction.ASC, HkNotificationRecipientField.IS_SEEN));
        query.with(new Sort(Sort.Direction.DESC, HkNotificationRecipientField.ON_DATE));

        List<HkNotificationRecipientDocument> resultList = mongoGenericDao.getMongoTemplate().find(query, HkNotificationRecipientDocument.class);
        if (!CollectionUtils.isEmpty(resultList)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Gson gson = new Gson();
            for (HkNotificationRecipientDocument recipient : resultList) {
                Map<String, String> descriptionParams = gson.fromJson(recipient.getHkNotificationEntity().getDescription(), type);
                if (!CollectionUtils.isEmpty(descriptionParams)) {
                    for (String key : descriptionParams.keySet()) {
                        String paramValue = descriptionParams.get(key);
                        if (paramValue.startsWith(HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR)) {
                            paramValue = HkSystemConstantUtil.dateFormatter.format(
                                    new Date(new Long(paramValue.substring(HkSystemConstantUtil.NOTIFICATION_DATE_SEPARATOR.length()))));
                            descriptionParams.put(key, paramValue);
                        }
                    }
                    recipient.getHkNotificationEntity().setDescription(gson.toJson(descriptionParams));
                }
            }
        }
        return resultList;
    }

    @Override
    @MongoTransaction
    public void removeUserNotification(Long userId, String notificationId, Long franchise) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkNotificationRecipientField.FOR_USER).is(userId));
        criterias.add(Criteria.where(HkNotificationRecipientField.NOTIFICATION_ID).is(notificationId));

        //this constraint is not needed as center side notifications will only from single franchise
//        if (franchise != null) {
//             criterias.add(Criteria.where(HkNotificationRecipientField.FRANCHISE).is(franchise));
//        }
        HkNotificationRecipientDocument notificationRecDocument = (HkNotificationRecipientDocument) mongoGenericDao.findOneByCriteria(criterias, HkNotificationRecipientDocument.class);
        notificationRecDocument.setStatus(HkSystemConstantUtil.INACTIVE);
        notificationRecDocument.setLastModifiedOn(new Date());
        mongoGenericDao.update(notificationRecDocument);
    }

    public HkNotificationDocument createNotification(String notificationType, String instanceType, Map<String, Object> valuesMap, Long instanceId, Long franchise) {
        String description = " ";
        if (!CollectionUtils.isEmpty(valuesMap)) {
            Gson gson = new Gson();
            description = gson.toJson(valuesMap);
        }
        HkNotificationDocument notification = new HkNotificationDocument(null, notificationType, new Date(), description, franchise, false);
        notification.setInstanceId(instanceId);
        notification.setInstanceType(instanceType);
        return notification;
    }

    @Override
    @Async
    @MongoTransaction
    public void sendNotification(HkNotificationDocument notification, Collection<Long> userList) {
        notification.setId(UUID.randomUUID().toString());
        notification.setIsArchive(false);
        if (notification.getNotificationConfigurationType() == null) {
            notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.WEB);
        }
        if (notification.getMailStatus() == null) {
            notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.NOT_REQUIRED);
        }
        Set<HkNotificationRecipientDocument> notRecipientSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userList)) {
            for (Long userId : userList) {
                HkNotificationRecipientDocument recipient = new HkNotificationRecipientDocument(UUID.randomUUID().toString(), userId);
                recipient.setHkNotificationEntity(notification);
                recipient.setIsArchive(false);
                recipient.setIsSeen(false);
                recipient.setLastModifiedOn(new Date());
                recipient.setStatus(HkSystemConstantUtil.ACTIVE);
                notRecipientSet.add(recipient);
            }
        }
        mongoGenericDao.createAll(new ArrayList(notRecipientSet));
        mongoGenericDao.update(notification);
    }

    @Override
    public List<HkNotificationConfigrationRecipientDocument> retrieveNotificationConfigurationByNode(Long node) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkNotificationConfigurationField.ACTIVITY_NODE).is(node));
        List<HkNotificationConfigurationDocument> listOfNotification = mongoGenericDao.findByCriteria(criterias, HkNotificationConfigurationDocument.class);
        if (!CollectionUtils.isEmpty(listOfNotification)) {
            List<Long> notificationIds = new ArrayList<>();
            for (HkNotificationConfigurationDocument listOfNotification1 : listOfNotification) {
                notificationIds.add(listOfNotification1.getId());
            }
            if (!CollectionUtils.isEmpty(notificationIds)) {
                criterias = new ArrayList<>();
                criterias.add(Criteria.where(HkNotificationConfigurationRecepientField.NOTIFICATION_CONFIGURATION).in(notificationIds));
                return mongoGenericDao.findByCriteria(criterias, HkNotificationConfigrationRecipientDocument.class);
            }
        }
        return null;
    }

    @Override
    @Async
    @MongoTransaction
    public void sendNotification(HkNotificationDocument notification, List<Long> userList) {
        notification.setId(UUID.randomUUID().toString());
        notification.setIsArchive(false);
        if (notification.getNotificationConfigurationType() == null) {
            notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.WEB);
        }
        if (notification.getMailStatus() == null) {
            notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.NOT_REQUIRED);
        }
        Set<HkNotificationRecipientDocument> notRecipientSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userList)) {
            for (Long userId : userList) {
                HkNotificationRecipientDocument recipient = new HkNotificationRecipientDocument(UUID.randomUUID().toString(), userId);
                recipient.setHkNotificationEntity(notification);
                recipient.setIsArchive(false);
                recipient.setIsSeen(false);
                recipient.setLastModifiedOn(new Date());
                recipient.setStatus(HkSystemConstantUtil.ACTIVE);
                notRecipientSet.add(recipient);
            }
            List<HkNotificationRecipientDocument> recipientDocuments = new ArrayList<>(notRecipientSet);
            mongoGenericDao.createAll(recipientDocuments);
        }

        mongoGenericDao.create(notification);
    }

    @Override
    public List<HkMessageRecipientDtlDocument> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority) {
        List<Criteria> criteriaList = new LinkedList<>();
        if (hasPriority != null) {
            criteriaList.add(Criteria.where(HkMessageRecipientDtlField.HAS_PRIORITY).is(hasPriority));
        }
        criteriaList.add(Criteria.where(HkMessageRecipientDtlField.STATUS).is(HkSystemConstantUtil.MessageStatus.PENDING));
        if (recipient != null) {

//  if recipient is the receiver whom the message is sent and if he has not seen the message yet.
            //  if recipient had sent the message earlier and now he has got the reply which he hasn't seen yet i.e. status = R.
            criteriaList.add(new Criteria().orOperator(new Criteria().andOperator(
                    Criteria.where(HkMessageRecipientDtlField.MESSAGE_TO).is(recipient), Criteria.where(HkMessageRecipientDtlField.IS_ATTENDED).is(Boolean.FALSE)),
                    new Criteria().andOperator(
                            Criteria.where(HkMessageRecipientDtlField.MESSAGE_FROM).is(recipient), Criteria.where(HkMessageRecipientDtlField.STATUS).is(HkSystemConstantUtil.MessageStatus.REPLIED))
            ));
        }

        if (archiveStatus != null) {
            criteriaList.add(Criteria.where(HkMessageRecipientDtlField.IS_ARCHIVE).is(archiveStatus));
        }

        return mongoGenericDao.findByCriteria(criteriaList, HkMessageRecipientDtlDocument.class);
    }

    @Override
    public int retrieveUnattendedMessageCount(Long recipient, Boolean hasPriority, Boolean archiveStatus) {
        if (recipient != null) {
            List<Criteria> criteriaList = new ArrayList<>();
            if (hasPriority != null) {
                criteriaList.add(Criteria.where(HkMessageRecipientDtlField.HAS_PRIORITY).is(hasPriority));
            }
            criteriaList.add(Criteria.where(HkMessageRecipientDtlField.MESSAGE_TO).is(recipient));
            if (archiveStatus != null) {
                criteriaList.add(Criteria.where(HkMessageRecipientDtlField.IS_ARCHIVE).is(archiveStatus));
            }
            criteriaList.add(Criteria.where(HkMessageRecipientDtlField.IS_ATTENDED).is(false));
            return mongoGenericDao.count(criteriaList, HkMessageRecipientDtlDocument.class).intValue();
        } else {
            return 0;
        }
    }

}
