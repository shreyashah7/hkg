/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core;

import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import com.argusoft.sync.center.model.HkNotificationConfigrationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author akta
 */
public interface HkNotificationDocumentService {

    public int retrieveUnseenNotificationCount(Long userId, Long franchise);

    public List<HkNotificationRecipientDocument> retrieveNotificationsForPopup(Long userId, Long franchise);

    public void markUserNotificationsAsSeen(Long userId, List<String> notificationIds, Long franchise);

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
    public List<HkNotificationRecipientDocument> retrieveNotificationsForUser(Long userId, Boolean isSeen, Date afterDate, Long franchise);

    /**
     * This method removes the notification from user's page. Object will be set
     * as inactive only, but not archived.
     *
     * @param userId The id of the user.
     * @param notificationId The id of notification to be removed.
     * @param franchise The id of the franchise.
     */
    public void removeUserNotification(Long userId, String notificationId, Long franchise);

    public HkNotificationDocument createNotification(String notificationType, String instanceType, Map<String, Object> valuesMap, Long instanceId, Long franchise);

    public void sendNotification(HkNotificationDocument notification, Collection<Long> userList);
    
    public List<HkNotificationConfigrationRecipientDocument> retrieveNotificationConfigurationByNode(Long node);
    
    public void sendNotification(HkNotificationDocument notification, List<Long> userList);

    /**
     *
     * @param recipient
     * @param archiveStatus
     * @param hasPriority
     * @return
     */
    public List<HkMessageRecipientDtlDocument> retrievePriorityNotifications(Long recipient, Boolean archiveStatus, Boolean hasPriority);

    /**
     * This method retrieves the unattended message count to show on display.
     *
     * @param recipient
     * @param hasPriority
     * @param archiveStatus
     * @return Returns the count of total unattended messages.
     */
    public int retrieveUnattendedMessageCount(Long recipient, Boolean hasPriority, Boolean archiveStatus);
}
