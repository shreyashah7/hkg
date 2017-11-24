/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.database.HkMessageDao;
import com.argusoft.hkg.database.HkMessageRecipientDao;
import com.argusoft.hkg.database.HkMessageRecipientDtlDao;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import com.argusoft.hkg.model.HkMessageRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntityPK;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation class of HkNotificationService.
 *
 * @author Mital
 */
@Service
@Transactional
public class HkNotificationServiceImpl extends HkCoreService implements HkNotificationService {

    @Autowired
    private HkMessageDao hkMessageDao;
    @Autowired
    private HkMessageRecipientDtlDao hkMessageRecipientDtlDao;
    @Autowired
    private HkMessageRecipientDao hkMessageRecipientDao;

    public static final String FRANCHISE = "franchise";
    public static final String IS_ARCHIVE = "isArchive";

    public static class HkMessageRecipientDtlField {

        public static final String MESSAGE_TO = "hkMessageRecipientDtlPK.messageTo";
        public static final String REPLIED_ON = "repliedOn";
        public static final String HAS_PRIORITY = "hasPriority";
        public static final String MESSAGE_FROM = "messageFrom";
        public static final String CREATED_ON = "hkMessage.createdOn";
        public static final String MESSAGE = "hkMessage";
        public static final String IS_ATTENDED = "isAttended";
    }

    public static class HkMessageField {

        public static final String MESSAGE_ID = "id";
        public static final String MESSAGE_BODY = "messageBody";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String MESSAGE_RECIPIENT_LIST = "hkMessageRecipientList";
        public static final String MESSAGE_RECIPIENT_DTL_LIST = "hkMessageRecipientDtlList";
        public static final String HAS_PRIORITY = "hasPriority";
    }

    public static class HkNotificationRecipientField {

        public static final String NOTIFICATION_ID = "hkNotificationRecipientEntityPK.notification";
        public static final String FRANCHISE = "hkNotificationEntity.franchise";
        public static final String ON_DATE = "hkNotificationEntity.onDate";
        public static final String IS_SEEN = "isSeen";
        public static final String STATUS = "status";
        public static final String FOR_USER = "hkNotificationRecipientEntityPK.forUser";
        public static final String LAST_MODIFIED_ON = "lastModifiedOn";
        public static final String NOTIFICATION_CONFIGURATION_TYPE = "hkNotificationEntity.notificationConfigurationType";
        public static final String MAIL_STATUS = "hkNotificationEntity.mailStatus";
    }

    public static class HkNotificationConfigurationField {

        public static final String ID = "id";
        public static final String STATUS = "status";
        public static final String FRANCHISE = "franchise";
        public static final String NOTIFICATION_NAME = "notificationName";
        public static final String SMS_MESSAGE = "smsMessage";
        public static final String WEB_MESSAGE = "webMessage";
        public static final String EMAIL_MESSAGE = "emailMessage";
        public static final String NOTIFICATION_TYPE = "notificationType";
        
    }

    public static class HkNotificationConfigrationRecipientEntityFields {

        public static final String CONFIGURATION_ID = "hkNotificationConfigrationRecipientEntityPK.notificationConfiguration";
        public static final String IS_ARCHIVE = "isArchive";
    }

    @Override
    public void sendMessage(HkMessageEntity hkMessage, Set<Long> recipients) {
        hkMessage.setStatus(HkSystemConstantUtil.ACTIVE);
        commonDao.save(hkMessage);

        List<HkMessageRecipientEntity> hkMessageRecipients = hkMessage.getHkMessageRecipientList();
        for (HkMessageRecipientEntity msgRecipient : hkMessageRecipients) {
            msgRecipient.getHkMessageRecipentPK().setMessageObj(hkMessage.getId());
        }
        commonDao.save(hkMessageRecipients.toArray());

        List<HkMessageRecipientDtlEntity> hkMessageRecipientDtlList = new ArrayList<>();
        for (Long recipient : recipients) {
            HkMessageRecipientDtlEntity hkMsgRecipientDtl = new HkMessageRecipientDtlEntity();

            hkMsgRecipientDtl.setHkMessageRecipientDtlPK(new HkMessageRecipientDtlEntityPK(hkMessage.getId(), recipient));
            hkMsgRecipientDtl.setMessageFrom(hkMessage.getCreatedBy());
            hkMsgRecipientDtl.setHasPriority(hkMessage.getHasPriority());
            hkMsgRecipientDtl.setIsAttended(false);
            hkMsgRecipientDtl.setStatus(HkSystemConstantUtil.MessageStatus.PENDING);
            hkMsgRecipientDtl.setIsArchive(false);
            hkMessageRecipientDtlList.add(hkMsgRecipientDtl);
        }
        commonDao.save(hkMessageRecipientDtlList.toArray());
    }

    @Override

    public HkMessageEntity retrieveMessageById(Long hkMsgId, boolean includeRecipientReferences) {
        Search search = SearchFactory.getSearch(HkMessageEntity.class);
        search.addFilterEqual(HkMessageField.MESSAGE_ID, hkMsgId);
        if (includeRecipientReferences) {
            search.addFetch(HkMessageField.MESSAGE_RECIPIENT_LIST)
                    .setDistinct(true);
        }
        List<HkMessageEntity> list = (List<HkMessageEntity>) commonDao.search(search);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public HkMessageRecipientDtlEntity retrieveMessageRecipientDetailById(HkMessageRecipientDtlEntityPK hkMsgRecipientDtlId) {
        return hkMessageRecipientDtlDao.retrieveById(hkMsgRecipientDtlId);
    }

    @Override
    public List<HkMessageEntity> searchMessageTemplates(String searchMessage, Long sender, Long franchise, Boolean archiveStatus) {
        List<HkMessageEntity> hkMessages = hkMessageDao.searchMessageTemplates(searchMessage, sender, franchise, archiveStatus);
        Map<Long, HkMessageEntity> messagesMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(hkMessages)) {
            for (HkMessageEntity hkMsg : hkMessages) {
                //  Set null for list so db calls can be saved in future while calling getter method
                hkMsg.setHkMessageRecipientList(null);
                messagesMap.put(hkMsg.getId(), hkMsg);
            }

            //  Doing this as fetch mode is Lazy.. :(
            List<HkMessageRecipientEntity> recipientList = hkMessageRecipientDao.retrieveMessageRecipientDtlsByMessageId(messagesMap.keySet(), null);
            for (HkMessageRecipientEntity recipient : recipientList) {
                //  Get recipients list from current hkmessage object
                List<HkMessageRecipientEntity> msgRecipients = messagesMap.get(recipient.getHkMessageRecipentPK().getMessageObj()).getHkMessageRecipientList();
                //  if it's empty, create new one
                if (CollectionUtils.isEmpty(msgRecipients)) {
                    msgRecipients = new ArrayList<>();
                }
                //  Add the current recipient to the list
                msgRecipients.add(recipient);
                //  Set it to the relevant hkmessage object's list
                messagesMap.get(recipient.getHkMessageRecipentPK().getMessageObj()).setHkMessageRecipientList(msgRecipients);
            }
        }
        return new ArrayList<>(messagesMap.values());
    }

    @Override
    public List<HkMessageEntity> searchMessages(String searchStr, Long userId, List<Long> recipients, Long franchise) {
        Criteria criteria = commonDao.getCurrentSession().createCriteria(HkMessageEntity.class);
        criteria.createAlias(HkMessageField.MESSAGE_RECIPIENT_DTL_LIST, "recipientDtl")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        //  All the messages must be either created by user or received by user
        criteria.add(Restrictions.or(
                Restrictions.and(
                        Restrictions.eq("recipientDtl." + HkMessageRecipientDtlField.MESSAGE_FROM, userId),
                        Restrictions.eq(IS_ARCHIVE, false)),
                Restrictions.and(
                        Restrictions.eq("recipientDtl." + HkMessageRecipientDtlField.MESSAGE_TO, userId),
                        Restrictions.eq("recipientDtl." + IS_ARCHIVE, false))
        ));
        criteria.add(Restrictions.eq(FRANCHISE, franchise));
        Criterion dateCri = Restrictions.or(
                Restrictions.ilike(HkMessageField.MESSAGE_BODY, searchStr, MatchMode.ANYWHERE),
                getDateRestrictions(searchStr, "created_on"));

        if (!CollectionUtils.isEmpty(recipients)) {
            criteria.add(Restrictions.or(
                    //  either the message should be sent by user and received by these users and messages must not be removed by me
                    Restrictions.and(
                            Restrictions.in("recipientDtl." + HkMessageRecipientDtlField.MESSAGE_TO, recipients),
                            Restrictions.eq(IS_ARCHIVE, false)),
                    //  or the message should be sent to me by these users and messages must not be removed by me
                    Restrictions.and(
                            Restrictions.in("recipientDtl." + HkMessageRecipientDtlField.MESSAGE_FROM, recipients),
                            Restrictions.eq("recipientDtl." + IS_ARCHIVE, false)),
                    dateCri
            ));
        } else {
            //  either the message body matches or the date matches
            criteria.add(dateCri);
        }

        criteria.setFetchMode(HkMessageField.MESSAGE_RECIPIENT_LIST, FetchMode.JOIN);

        List<HkMessageEntity> resultList = criteria.list();
        if (!CollectionUtils.isEmpty(resultList)) {
            for (HkMessageEntity message : resultList) {
                List<HkMessageRecipientEntity> hkMessageRecipientList = message.getHkMessageRecipientList();
                List<HkMessageRecipientEntity> visited = new ArrayList<>();
                for (HkMessageRecipientEntity hkMessageRecipientEntity : hkMessageRecipientList) {
                    if (!visited.contains(hkMessageRecipientEntity)) {
                        visited.add(hkMessageRecipientEntity);
                    }
                }
                message.setHkMessageRecipientList(visited);
                if (!CollectionUtils.isEmpty(message.getHkMessageRecipientDtlList())) {
                    message.setHkMessageRecipientDtlList(new ArrayList<>(message.getHkMessageRecipientDtlList()));
                } else {
                    message.setHkMessageRecipientDtlList(new ArrayList<HkMessageRecipientDtlEntity>());
                }
            }
        }
        return resultList;
    }

    @Override
    public int retrieveUnattendedMessageCount(Long recipient, Boolean hasPriority, Boolean archiveStatus) {
        if (recipient != null) {
            Search search = SearchFactory.getSearch(HkMessageRecipientDtlEntity.class);
            if (hasPriority != null) {
                search.addFilterEqual(HkMessageRecipientDtlField.HAS_PRIORITY, hasPriority);
            }
            search.addFilterEqual(HkMessageRecipientDtlField.MESSAGE_TO, recipient);
            if (archiveStatus != null) {
                search.addFilterEqual(IS_ARCHIVE, archiveStatus);
            }
            search.addFilterEqual(HkMessageRecipientDtlField.IS_ATTENDED, false);
            return commonDao.count(search);
        } else {
            return 0;
        }
    }

    @Override
    public List<HkMessageRecipientDtlEntity> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority) {
        return hkMessageRecipientDtlDao.retrievePriorityNotifications(recipient, archiveStatus, hasPriority);
    }

    @Override
    public Map<List<HkMessageEntity>, List<Boolean>> retrieveInboxMessages(Long userId, Boolean hasPriority, Date fromDate, Date toDate, Boolean archiveStatus, Long franchise, boolean includeRecipientReferences) {
        Search search = new Search(HkMessageRecipientDtlEntity.class);
        if (userId != null) {
            search.addFilterEqual(HkMessageRecipientDtlField.MESSAGE_TO, userId);
        }

        if (hasPriority != null) {
            search.addFilterEqual(HkMessageField.HAS_PRIORITY, hasPriority);
        }

        if (fromDate != null && toDate != null) {
            search.addFilterAnd(Filter.greaterOrEqual(HkMessageRecipientDtlField.CREATED_ON, fromDate), Filter.lessOrEqual(HkMessageRecipientDtlField.CREATED_ON, toDate));
        }

        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        if (franchise != null) {
            search.addFilterEqual(HkMessageRecipientDtlField.MESSAGE + "." + FRANCHISE, franchise);
        }

        if (includeRecipientReferences) {
            search.addFetch(HkMessageRecipientDtlField.MESSAGE + "." + HkMessageField.MESSAGE_RECIPIENT_LIST)
                    .setDistinct(true);
        }

        List<HkMessageRecipientDtlEntity> recipientDtlList = commonDao.search(search);
        List<HkMessageEntity> messageList = null;
        Map<List<HkMessageEntity>, List<Boolean>> map = new HashMap<>();
        List<Boolean> booleans = null;
        if (!CollectionUtils.isEmpty(recipientDtlList)) {
            messageList = new ArrayList<>();
            booleans = new ArrayList<>();
            for (HkMessageRecipientDtlEntity recipientDtl : recipientDtlList) {
                messageList.add(recipientDtl.getHkMessage());
                booleans.add(recipientDtl.getIsAttended());
            }
        }
        map.put(messageList, booleans);
        return map;
    }

    @Override
    public List<HkMessageEntity> retrieveSentMessages(Long userId, Boolean hasPriority, Date fromDate, Date toDate, Boolean archiveStatus, Long franchise, boolean includeRecipientReferences) {
        Search search = new Search(HkMessageEntity.class);
        if (userId != null) {
            search.addFilterEqual(HkMessageField.CREATED_BY, userId);
        }

        if (hasPriority != null) {
            search.addFilterEqual(HkMessageField.HAS_PRIORITY, hasPriority);
        }

        if (fromDate != null && toDate != null) {
            search.addFilterAnd(Filter.greaterOrEqual(HkMessageField.CREATED_ON, fromDate), Filter.lessOrEqual(HkMessageField.CREATED_ON, toDate));
        }

        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        if (franchise != null) {
            search.addFilterEqual(FRANCHISE, franchise);
        }

        if (includeRecipientReferences) {
            search.addFetch(HkMessageField.MESSAGE_RECIPIENT_LIST)
                    .setDistinct(true);
        }

        return commonDao.search(search);
    }

    @Override
    public void updateHkMessageRecipientDtlEntity(HkMessageRecipientDtlEntity hkMsgRecipientDtl) {
        hkMessageRecipientDtlDao.update(hkMsgRecipientDtl);
    }

    @Override
    public void removeMessage(HkMessageEntity hkMessage) {
        hkMessage.setIsArchive(true);
        hkMessageDao.saveOrUpdate(hkMessage);
    }

    @Override
    public void removeMessage(Long hkMessageId) {
        HkMessageEntity hkMessage = hkMessageDao.retrieveById(hkMessageId);
        this.removeMessage(hkMessage);
    }

    @Override
    public void removeMessageTemplate(Long messageId) {
        HkMessageEntity hkMessage = hkMessageDao.retrieveById(messageId);
        hkMessage.setStatus(HkSystemConstantUtil.INACTIVE);
        hkMessageDao.update(hkMessage);
    }

    @Override
    @Async
    public void sendNotification(HkNotificationEntity notification, Collection<Long> userList) {
        notification.setId(UUID.randomUUID().toString());
        notification.setIsArchive(false);
        if (notification.getNotificationConfigurationType() == null) {
            notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.WEB);
        }
        if (notification.getMailStatus() == null) {
            notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.NOT_REQUIRED);
        }
        Set<HkNotificationRecipientEntity> notRecipientSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userList)) {
            for (Long userId : userList) {
                HkNotificationRecipientEntity recipient = new HkNotificationRecipientEntity(notification.getId(), userId);
                recipient.setHkNotificationEntity(notification);
                recipient.setIsArchive(false);
                recipient.setIsSeen(false);
                recipient.setLastModifiedOn(new Date());
                recipient.setStatus(HkSystemConstantUtil.ACTIVE);
                notRecipientSet.add(recipient);
            }
        }
        notification.setHkNotificationRecipientEntitySet(notRecipientSet);
        commonDao.save(notification);
    }

    @Override
    @Async
    public void sendNotifications(Map<HkNotificationEntity, List<Long>> notificationMap) {
        if (!CollectionUtils.isEmpty(notificationMap)) {
            for (HkNotificationEntity notification : notificationMap.keySet()) {
                notification.setIsArchive(false);
                notification.setId(UUID.randomUUID().toString());
                if (notification.getNotificationConfigurationType() == null) {
                    notification.setNotificationConfigurationType(HkSystemConstantUtil.NotificationSendType.WEB);
                }
                if (notification.getMailStatus() == null) {
                    notification.setMailStatus(HkSystemConstantUtil.NotificationEmailStatus.NOT_REQUIRED);
                }
                Set<HkNotificationRecipientEntity> notRecipientSet = new HashSet<>();
                List<Long> userList = notificationMap.get(notification);
                if (!CollectionUtils.isEmpty(userList)) {
                    for (Long userId : userList) {
                        HkNotificationRecipientEntity recipient = new HkNotificationRecipientEntity(notification.getId(), userId);
                        recipient.setHkNotificationEntity(notification);
                        recipient.setIsArchive(false);
                        recipient.setIsSeen(false);
                        recipient.setLastModifiedOn(new Date());
                        recipient.setStatus(HkSystemConstantUtil.ACTIVE);
                        notRecipientSet.add(recipient);
                    }
                }
                notification.setHkNotificationRecipientEntitySet(notRecipientSet);
            }
            commonDao.saveAll(notificationMap.keySet());
        }
    }

    @Override
    public int retrieveUnseenNotificationCount(Long userId, Long franchise) {
        if (userId != null && franchise != null) {
            Search search = SearchFactory.getSearch(HkNotificationRecipientEntity.class);
            search.addFilterEqual(HkNotificationRecipientField.FOR_USER, userId);
            search.addFilterEqual(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE, HkSystemConstantUtil.NotificationSendType.WEB);
            search.addFilterEqual(HkNotificationRecipientField.IS_SEEN, false);
            if (franchise != null) {
                search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
            }
            return commonDao.count(search);
        } else {
            return 0;
        }
    }

    @Override
    public List<HkNotificationRecipientEntity> retrieveNotificationsForUser(Long userId, Boolean isSeen, Date afterDate, Long franchise) {
        Search search = SearchFactory.getSearch(HkNotificationRecipientEntity.class);
        search.addFilterEqual(HkNotificationRecipientField.FOR_USER, userId);
        search.addFilterLessOrEqual(HkNotificationRecipientField.ON_DATE, new Date());
        search.addFilterEqual(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE, HkSystemConstantUtil.NotificationSendType.WEB);
        if (isSeen != null) {
            search.addFilterEqual(HkNotificationRecipientField.IS_SEEN, isSeen);
        }
        if (afterDate != null) {
            search.addFilterGreaterOrEqual(HkNotificationRecipientField.ON_DATE, afterDate);
        }
        if (franchise != null) {
            search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
        }
        search.addFilterEqual(HkNotificationRecipientField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addSortAsc(HkNotificationRecipientField.IS_SEEN);
        search.addSortDesc(HkNotificationRecipientField.ON_DATE);

        List<HkNotificationRecipientEntity> resultList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(resultList)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Gson gson = new Gson();
            for (HkNotificationRecipientEntity recipient : resultList) {
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
    public List<HkNotificationRecipientEntity> retrieveNotificationsForPopup(Long userId, Long franchise) {
        Search search = SearchFactory.getSearch(HkNotificationRecipientEntity.class);
        search.addFilterEqual(HkNotificationRecipientField.FOR_USER, userId);
        search.addFilterEqual(HkNotificationRecipientField.IS_SEEN, false);
        search.addFilterEqual(HkNotificationRecipientField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterEqual(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE, HkSystemConstantUtil.NotificationSendType.WEB);
        search.addFilterLessOrEqual(HkNotificationRecipientField.ON_DATE, new Date());
        if (franchise != null) {
            search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
        }

        search.addSortAsc(HkNotificationRecipientField.IS_SEEN);
        search.addSortDesc(HkNotificationRecipientField.ON_DATE);

        //  if count of above criteria is less than 10, remove is seen criteria and return 10 results
        if (commonDao.count(search) < 10) {
            search.removeFilter(Filter.equal(HkNotificationRecipientField.IS_SEEN, false));
        }
        search.setMaxResults(10);
        //  if count is greater than 10 of unread notifications, no need to set any limit to results.
        List<HkNotificationRecipientEntity> resultList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(resultList)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Gson gson = new Gson();
            for (HkNotificationRecipientEntity recipient : resultList) {
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
    public void markUserNotificationsAsSeen(Long userId, List<String> notificationIds, Long franchise) {
        Search search = SearchFactory.getSearch(HkNotificationRecipientEntity.class);
        search.addFilterEqual(HkNotificationRecipientField.FOR_USER, userId);
        search.addFilterIn(HkNotificationRecipientField.NOTIFICATION_ID, notificationIds);
        search.addFilterEqual(HkNotificationRecipientField.IS_SEEN, false);
        if (franchise != null) {
            search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
        }
        List<HkNotificationRecipientEntity> notificationList = commonDao.search(search);
        if (!CollectionUtils.isEmpty(notificationList)) {
            for (HkNotificationRecipientEntity notification : notificationList) {
                notification.setIsSeen(true);
                notification.setSeenOn(new Date());
                notification.setLastModifiedOn(new Date());
            }
        }
    }

    @Override
    public void removeUserNotification(Long userId, String notificationId, Long franchise) {
        Search search = SearchFactory.getSearch(HkNotificationRecipientEntity.class);
        search.addFilterEqual(HkNotificationRecipientField.FOR_USER, userId);
        search.addFilterEqual(HkNotificationRecipientField.NOTIFICATION_ID, notificationId);
        if (franchise != null) {
            search.addFilterEqual(HkNotificationRecipientField.FRANCHISE, franchise);
        }
        HkNotificationRecipientEntity notification = (HkNotificationRecipientEntity) commonDao.searchUnique(search);
        notification.setStatus(HkSystemConstantUtil.INACTIVE);
        notification.setLastModifiedOn(new Date());
    }

    @Override
    public Long createNotificationConfiguration(HkNotificationConfigurationEntity configurationEntity) {
        commonDao.save(configurationEntity);
        return configurationEntity.getId();
    }

    @Override
    public List<HkNotificationConfigurationEntity> retrieveAllActiveNotificationConfiguration(Long companyId,String notificationType) {
        Search search = new Search(HkNotificationConfigurationEntity.class);
        if (companyId != null) {
            search.addFilterEqual(HkNotificationConfigurationField.FRANCHISE, companyId);
        }
        search.addFilterEqual(HkNotificationConfigurationField.STATUS, HkSystemConstantUtil.ACTIVE);
        if(!StringUtils.isEmpty(notificationType)){
            search.addFilterEqual(HkNotificationConfigurationField.NOTIFICATION_TYPE, notificationType);
        }

        return commonDao.search(search);
    }

    @Override
    public void createNotificationConfigurationRecipients(List<HkNotificationConfigrationRecipientEntity> recipientEntitys) {
        commonDao.saveAll(recipientEntitys);
    }

    @Override
    public Map<Long, List<HkNotificationConfigrationRecipientEntity>> retrieveActiveNotificationConfigurationRecipients(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            Search search = new Search(HkNotificationConfigrationRecipientEntity.class);

            search.addFilterIn(HkNotificationConfigrationRecipientEntityFields.CONFIGURATION_ID, ids);
            search.addFilterEqual(HkNotificationConfigrationRecipientEntityFields.IS_ARCHIVE, Boolean.FALSE);
            List<HkNotificationConfigrationRecipientEntity> result = (List<HkNotificationConfigrationRecipientEntity>) commonDao.search(search);
            Map<Long, List<HkNotificationConfigrationRecipientEntity>> resultMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(result)) {
                for (HkNotificationConfigrationRecipientEntity recipientEntity : result) {
                    if (resultMap.get(recipientEntity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration()) == null) {
                        resultMap.put(recipientEntity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration(), new ArrayList<HkNotificationConfigrationRecipientEntity>());
                    }

                    resultMap.get(recipientEntity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration()).add(recipientEntity);
                }
            }

            return resultMap;
        }
        return null;
    }

    @Override
    public List<HkNotificationConfigrationRecipientEntity> retrieveActiveRecipientsByConfiguration(Long configurationId) {
        List<HkNotificationConfigrationRecipientEntity> result = new ArrayList<>();
        Search search = new Search(HkNotificationConfigrationRecipientEntity.class);

        search.addFilterEqual(HkNotificationConfigrationRecipientEntityFields.IS_ARCHIVE, false);
        search.addFilterEqual(HkNotificationConfigrationRecipientEntityFields.CONFIGURATION_ID, configurationId);

        result = commonDao.search(search);
        return result;
    }

    @Override
    public void updateNotificationConfiguration(HkNotificationConfigurationEntity configurationEntity, List<HkNotificationConfigrationRecipientEntity> configrationRecipientEntitys) {
        commonDao.save(configurationEntity);
        List<HkNotificationConfigrationRecipientEntity> activeRecipients = this.retrieveActiveRecipientsByConfiguration(configurationEntity.getId());

        if (!CollectionUtils.isEmpty(activeRecipients) && !CollectionUtils.isEmpty(configrationRecipientEntitys)) {
            Iterator<HkNotificationConfigrationRecipientEntity> activeIterator = activeRecipients.iterator();
            while (activeIterator.hasNext()) {

                Iterator<HkNotificationConfigrationRecipientEntity> newIterator = configrationRecipientEntitys.iterator();
                HkNotificationConfigrationRecipientEntity activeEntity = activeIterator.next();

                while (newIterator.hasNext()) {
                    HkNotificationConfigrationRecipientEntity newEntity = newIterator.next();

                    if (activeEntity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration() == newEntity.getHkNotificationConfigrationRecipientEntityPK().getNotificationConfiguration()
                            && activeEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance() == newEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance()
                            && activeEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType().equals(newEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType())) {
                        newIterator.remove();
                        activeIterator.remove();
                    }

                }
            }
        }
        List<HkNotificationConfigrationRecipientEntity> entitiesToDelete = new ArrayList<>();
        for (HkNotificationConfigrationRecipientEntity hkNotificationConfigrationRecipientEntity : activeRecipients) {
            hkNotificationConfigrationRecipientEntity.setIsArchive(true);
            entitiesToDelete.add(hkNotificationConfigrationRecipientEntity);
        }
        List<HkNotificationConfigrationRecipientEntity> finalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entitiesToDelete)) {
            finalList.addAll(entitiesToDelete);
        }
        if (!CollectionUtils.isEmpty(configrationRecipientEntitys)) {
            finalList.addAll(configrationRecipientEntitys);
        }
        if (!CollectionUtils.isEmpty(finalList)) {
            commonDao.saveAll(finalList);
        }
    }

    @Override
    public List<HkNotificationConfigurationEntity> retrieveNotificationBySearchText(String searchText, Long companyId) {
        Search search = null;
        if (StringUtils.hasText(searchText) && searchText.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            search = new Search(HkNotificationConfigurationEntity.class);

            search.addFilterEqual(HkNotificationConfigurationField.FRANCHISE, companyId);

            StringBuilder notificationName = new StringBuilder("%");
            notificationName.append(searchText);
            notificationName.append("%");
            search.addFilterILike(HkNotificationConfigurationField.NOTIFICATION_NAME, notificationName.toString());
        }

        if (search != null) {
            search.addField(HkNotificationConfigurationField.ID);
            search.addField(HkNotificationConfigurationField.NOTIFICATION_NAME);
            search.addField(HkNotificationConfigurationField.EMAIL_MESSAGE);
            search.addField(HkNotificationConfigurationField.SMS_MESSAGE);
            search.addField(HkNotificationConfigurationField.WEB_MESSAGE);
            return commonDao.search(search);
        }
        return null;
    }

    @Override
    public HkNotificationConfigurationEntity retrieveNotificationCnfigurationById(Long Id) {
        return commonDao.find(HkNotificationConfigurationEntity.class, Id);
    }

    @Override
    public void updateNotificationRecipient(HkNotificationRecipientEntity entity) {
        commonDao.save(entity);
    }

    @Override
    public HkNotificationRecipientEntity retrieveNotificationRecEntity(HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK) {
        return commonDao.find(HkNotificationRecipientEntity.class, hkNotificationRecipientEntityPK);
    }

    @Override
    public void saveNotificationConfigurations(List<HkNotificationConfigurationEntity> notificationListToUpdate) {
        if (!CollectionUtils.isEmpty(notificationListToUpdate)) {
            commonDao.saveAll(notificationListToUpdate);
        }
    }

    @Override
    public List<HkNotificationRecipientEntity> retrievePendingEmailNotificationsTillDate(Date toDay) {
        Search search = new Search(HkNotificationRecipientEntity.class);

        search.addFilterEqual(HkNotificationRecipientField.MAIL_STATUS, HkSystemConstantUtil.NotificationEmailStatus.PENDING);
        search.addFilterEqual(HkNotificationRecipientField.NOTIFICATION_CONFIGURATION_TYPE, HkSystemConstantUtil.NotificationSendType.EMAIL);
        search.addFilterLessOrEqual(HkNotificationRecipientField.ON_DATE, toDay);

        return commonDao.search(search);
    }

    @Override
    public void updateNotifications(List<HkNotificationEntity> notificationsToUpdate) {
        if (!CollectionUtils.isEmpty(notificationsToUpdate)) {
            commonDao.saveAll(notificationsToUpdate);
        }
    }

}
