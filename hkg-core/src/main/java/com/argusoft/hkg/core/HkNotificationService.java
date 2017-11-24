/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core;

import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntityPK;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.scheduling.annotation.Async;

/**
 * Service class for modules like Messages, Notifications and other
 * communications.
 *
 * @author Mital
 */
public interface HkNotificationService {

    /**
     * Method is implemented in HkCoreService.
     *
     * @param notificationType
     * @param instanceType
     * @param valuesMap
     * @param instanceId
     * @param franchise
     * @return
     */
    public HkNotificationEntity createNotification(String notificationType, String instanceType, Map<String, Object> valuesMap, Long instanceId, Long franchise);

    /**
     * This method sends the message. Set the hkMessageRecipientList and send it
     * along with this object to store the details of sender and recipients.
     * Also send the Set of users (so that all users get the message once) who
     * would get this message.
     *
     * @param hkMessage The object that will save the details of message,
     * including the list of recipients.
     * @param recipients Set of userIds who should get the message.
     */
    public void sendMessage(HkMessageEntity hkMessage, Set<Long> recipients);

    /**
     * This method retrieves the Message.
     *
     * @param hkMsgId Id of the Message that is to be retrieved.
     * @return Returns the HkMessageEntity object.
     */
    public HkMessageEntity retrieveMessageById(Long hkMsgId, boolean includeRecipientReferences);

    /**
     * This method retrieves the MessageRecipientDetail object.
     *
     * @param hkMsgRecipientDtlId Id of the HkMessageRecipientDtlEntity object
     * that is to be retrieved.
     * @return Returns the HkMessageRecipientDtl object.
     */
    public HkMessageRecipientDtlEntity retrieveMessageRecipientDetailById(HkMessageRecipientDtlEntityPK hkMsgRecipientDtlId);

    /**
     * This method searches for the message templates sent by the sender
     * previously. Used for auto completion of message body while composing.
     *
     * @param searchMessage The message body that needs to be matched.
     * @param sender The id of the user who is sending the message.
     * @param franchise Id of franchise.
     * @param archiveStatus False if active i.e. non-archived records required,
     * True if archive i.e. deleted record to be retrieved. When null, returns
     * all records.
     * @return Returns the list of HkMessageEntity objects.
     */
    public List<HkMessageEntity> searchMessageTemplates(String searchMessage, Long sender, Long franchise, Boolean archiveStatus);

    /**
     * This method is used for search component.
     *
     * @param searchStr
     * @param userId
     * @param recipients
     * @param franchise
     * @return
     */
    public List<HkMessageEntity> searchMessages(String searchStr, Long userId, List<Long> recipients, Long franchise);

    /**
     *
     * @param recipient
     * @param archiveStatus
     * @return
     */
    public List<HkMessageRecipientDtlEntity> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority);

    /**
     * This method is used to show the messages received by the given user.
     *
     * @param userId The id of the recipient.
     * @param hasPriority true if priority messages need to be retrieved, false
     * if that need to be excluded, null if this value is to be ignored.
     * @param fromDate Start date range for messages.
     * @param toDate End date range for messages.
     * @param archiveStatus true if archived messages required, false if not.
     * Null if both required.
     * @param franchise The id of the franchise.
     * @param includeRecipientReferences true if recipient references needed
     * with messages, false if not required.
     * @return Returns the list of messages.
     */
    public Map<List<HkMessageEntity>, List<Boolean>> retrieveInboxMessages(Long userId, Boolean hasPriority, Date fromDate, Date toDate, Boolean archiveStatus, Long franchise, boolean includeRecipientReferences);

    /**
     * This method is used to show the messages sent by the given user.
     *
     * @param userId The id of the recipient.
     * @param hasPriority true if priority messages need to be retrieved, false
     * if that need to be excluded, null if this value is to be ignored.
     * @param fromDate Start date range for messages.
     * @param toDate End date range for messages.
     * @param archiveStatus true if archived messages required, false if not.
     * Null if both required.
     * @param franchise The id of the franchise.
     * @param includeRecipientReferences true if recipient references needed
     * with messages, false if not required.
     * @return Returns the list of messages.
     */
    public List<HkMessageEntity> retrieveSentMessages(Long userId, Boolean hasPriority, Date fromDate, Date toDate, Boolean archiveStatus, Long franchise, boolean includeRecipientReferences);

    /**
     * This method updates the HkMessageRecipientDtl object. Used when, #1
     * recipient reads the message (update isAttended = true, attendedOn, status
     * = P) #2 recipient replies the message (update isAttended = true if first
     * time reading, attendedOn if first time seeing, repliedMessageBody,
     * repliedOn, status = R) #3 reply is read (status = C)
     *
     * @param hkMsgRecipientDtl The object that needs to be updated.
     */
    public void updateHkMessageRecipientDtlEntity(HkMessageRecipientDtlEntity hkMsgRecipientDtl);

    /**
     * This method removes HkMessageEntity object. Used when sender removes some
     * message from given suggestions.
     *
     * @param hkMessage The object that is to be deleted.
     */
    public void removeMessage(HkMessageEntity hkMessage);

    /**
     * This method removes HkMessageEntity object. Used when sender removes some
     * message from given suggestions.
     *
     * @param hkMessageId Id of the object that needs to be deleted.
     */
    public void removeMessage(Long hkMessageId);

    /**
     * This method removes the message template i.e. status=D so the message
     * won't get retrieved next time user retrieves templates by
     * searchMessageTemplates.
     *
     * @param messageId The id of the message entity.
     */
    public void removeMessageTemplate(Long messageId);

    /**
     * This method retrieves the unattended message count to show on display.
     *
     * @param recipient
     * @param hasPriority
     * @return Returns the count of total unattended messages.
     */
    public int retrieveUnattendedMessageCount(Long recipient, Boolean hasPriority, Boolean archiveStatus);

    /**
     * This method sends the notification to given users.
     *
     * @param notification The notification object to be sent.
     * @param userList The list of user ids.
     */
    @Async
    public void sendNotification(HkNotificationEntity notification, Collection<Long> userList);

    /**
     * This method is used to send notifications all together.
     *
     * @param notificationMap
     */
    @Async
    public void sendNotifications(Map<HkNotificationEntity, List<Long>> notificationMap);

    /**
     * This method retrieves the unseen notifications count to show on display.
     *
     * @param userId The id of the logged in user.
     * @param franchise The id of the franchise of user.
     * @return Returns the count of total unseen i.e. new notifications.
     */
    public int retrieveUnseenNotificationCount(Long userId, Long franchise);

    /**
     * This method retrieves the notifications for user by given criteria.
     *
     * @param userId The id of the user.
     * @param isSeen True if seen notifications are to be retrieved, false if
     * unseen, null otherwise.
     * @param afterDate The date after which the notifications were sent to this
     * user.
     * @param franchise The id of the franchise.
     * @return Returns the list of notifications that match the given criteria
     * in descending order of notification date.
     */
    public List<HkNotificationRecipientEntity> retrieveNotificationsForUser(Long userId, Boolean isSeen, Date afterDate, Long franchise);

    /**
     * This method is used for retrieving notifications in popup. By default,
     * all the unseen notifications will be retrieved. If size of unseen
     * notifications is less than 30, then total last 30 notifications will be
     * returned.
     *
     * @param userId The id of the user.
     * @param franchise The id of the franchise.
     * @return Returns the list of notifications.
     */
    public List<HkNotificationRecipientEntity> retrieveNotificationsForPopup(Long userId, Long franchise);

    /**
     * This method marks the given notifications as seen.
     *
     * @param userId The of the user.
     * @param notificationIds The notification ids that are to be marked as
     * seen.
     * @param franchise The of the franchise.
     */
    public void markUserNotificationsAsSeen(Long userId, List<String> notificationIds, Long franchise);

    /**
     * This method removes the notification from user's page. Object will be set
     * as inactive only, but not archived.
     *
     * @param userId The id of the user.
     * @param notificationId The id of notification to be removed.
     * @param franchise The id of the franchise.
     */
    public void removeUserNotification(Long userId, String notificationId, Long franchise);

    /**
     * create notification configuration
     *
     * @param configurationEntity
     * @return ID of created configuration
     */
    public Long createNotificationConfiguration(HkNotificationConfigurationEntity configurationEntity);

    /**
     * create notification recipients
     *
     * @param recipientEntitys list of recipients
     */
    public void createNotificationConfigurationRecipients(List<HkNotificationConfigrationRecipientEntity> recipientEntitys);

    /**
     * retrieve all active notification configurations for franchise
     *
     * @param companyId franchise ID
     * @return list of active notification configurations
     */
    public List<HkNotificationConfigurationEntity> retrieveAllActiveNotificationConfiguration(Long companyId,String notificationType);

    /**
     * retrieve active notification configuration recipients by configuration
     * ids
     *
     * @param ids of notification confiurations
     * @return
     */
    public Map<Long, List<HkNotificationConfigrationRecipientEntity>> retrieveActiveNotificationConfigurationRecipients(List<Long> ids);

    /**
     * retrieve active recipients by configuration ID
     *
     * @param configurationId of configuration
     * @return List of all active recipients
     */
    public List<HkNotificationConfigrationRecipientEntity> retrieveActiveRecipientsByConfiguration(Long configurationId);

    /**
     * Update notification configuration
     *
     * @param configurationEntity configuration entity
     * @param configrationRecipientEntitys related recipients
     */
    public void updateNotificationConfiguration(HkNotificationConfigurationEntity configurationEntity, List<HkNotificationConfigrationRecipientEntity> configrationRecipientEntitys);

    /**
     * Search for Notification based on search text
     *
     * @param searchText text to search in name
     * @param companyId franchise ID
     * @return List of notifications matched
     */
    public List<HkNotificationConfigurationEntity> retrieveNotificationBySearchText(String searchText, Long companyId);

    /**
     * Retrieve notification configuration by ID
     *
     * @param Id of notification configuration
     * @return Notification configuration matched with ID
     */
    public HkNotificationConfigurationEntity retrieveNotificationCnfigurationById(Long Id);

    public void updateNotificationRecipient(HkNotificationRecipientEntity entity);

    public HkNotificationRecipientEntity retrieveNotificationRecEntity(HkNotificationRecipientEntityPK hkNotificationRecipientEntityPK);

    public void saveNotificationConfigurations(List<HkNotificationConfigurationEntity> notificationListToUpdate);

    public List<HkNotificationRecipientEntity> retrievePendingEmailNotificationsTillDate(Date toDay);

    public void updateNotifications(List<HkNotificationEntity> notificationsToUpdate);
}
