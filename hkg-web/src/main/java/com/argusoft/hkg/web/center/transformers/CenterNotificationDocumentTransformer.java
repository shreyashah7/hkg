/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.nosql.core.HkNotificationDocumentService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationRecipientDataBean;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author akta
 */
@Service
public class CenterNotificationDocumentTransformer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HkNotificationDocumentService notificationDocumentService;
    @Autowired
    private LoginDataBean centerLoginDataBean;

    public Map<String, Integer> retrieveNotificationCount(Long userId, Long companyId) {
//        issueService.testTrans();

        Map<String, Integer> notificationCount = new HashMap<>();
        notificationCount.put("count", notificationDocumentService.retrieveUnseenNotificationCount(userId, companyId));
        return notificationCount;
    }

    public List<NotificationRecipientDataBean> retrieveNotificationsPopUp(Long userId, Long companyId) {
        List<HkNotificationRecipientDocument> hkNotificationDocumentRecipientEntitysPopUp = notificationDocumentService.retrieveNotificationsForPopup(userId, companyId);
        List<NotificationRecipientDataBean> notificationRecipientDataBeansPopUp = new ArrayList<>();
        List<String> notificationIds = new ArrayList<>();
        for (HkNotificationRecipientDocument hkNotificationDocumentRecipientDocument : hkNotificationDocumentRecipientEntitysPopUp) {
            NotificationRecipientDataBean notificationRecipientDataBean = new NotificationRecipientDataBean();
            convertNotificationRecipientDocumentToNotificationRecipientDataBean(hkNotificationDocumentRecipientDocument, notificationRecipientDataBean);
            notificationIds.add(hkNotificationDocumentRecipientDocument.getHkNotificationRecipientEntityPK().getNotification());
            notificationRecipientDataBeansPopUp.add(notificationRecipientDataBean);
        }
        notificationDocumentService.markUserNotificationsAsSeen(centerLoginDataBean.getId(), notificationIds, centerLoginDataBean.getCompanyId());
        return notificationRecipientDataBeansPopUp;
    }

    public NotificationRecipientDataBean convertNotificationRecipientDocumentToNotificationRecipientDataBean(HkNotificationRecipientDocument notificationRecipientDocument, NotificationRecipientDataBean notificationRecipientDataBean) {
        if (notificationRecipientDataBean == null) {
            notificationRecipientDataBean = new NotificationRecipientDataBean();
        }
        NotificationDataBean notificationDataBean = new NotificationDataBean();
        notificationRecipientDataBean.setIsArchive(notificationRecipientDocument.getIsArchive());
        notificationRecipientDataBean.setLastModifiedOn(new Date());
        notificationRecipientDataBean.setIsSeen(notificationRecipientDocument.getIsSeen());
        notificationRecipientDataBean.setNotification(notificationRecipientDocument.getHkNotificationRecipientEntityPK().getNotification());
        notificationRecipientDataBean.setForUser(notificationRecipientDocument.getHkNotificationRecipientEntityPK().getForUser());
        notificationRecipientDataBean.setSeenOn(notificationRecipientDocument.getSeenOn());
        notificationRecipientDataBean.setStatus(notificationRecipientDocument.getStatus());
        notificationDataBean.setDescription(notificationRecipientDocument.getHkNotificationEntity().getDescription());
        notificationDataBean.setId(notificationRecipientDocument.getHkNotificationEntity().getId());
        notificationDataBean.setFranchise(notificationRecipientDocument.getHkNotificationEntity().getFranchise());
        notificationDataBean.setInstanceId(notificationRecipientDocument.getHkNotificationEntity().getInstanceId());
        notificationDataBean.setInstanceType(notificationRecipientDocument.getHkNotificationEntity().getInstanceType());
        notificationDataBean.setIsArchive(notificationRecipientDocument.getIsArchive());
        notificationDataBean.setNotificationtype(notificationRecipientDocument.getHkNotificationEntity().getNotificationType());
        notificationDataBean.setOnDate(notificationRecipientDocument.getHkNotificationEntity().getOnDate());
        notificationRecipientDataBean.setNotificationDataBean(notificationDataBean);
        return notificationRecipientDataBean;
    }

    public List<NotificationRecipientDataBean> retrieveNotifications(Long userId, Boolean isSeen, Date afterDate, Long companyId) {
        List<HkNotificationRecipientDocument> notificationRecDocList = notificationDocumentService.retrieveNotificationsForUser(userId, isSeen, afterDate, companyId);
        List<String> notificationIds = new ArrayList<>();
        List<NotificationRecipientDataBean> notificationRecipientDataBeans = new ArrayList<>();
        for (HkNotificationRecipientDocument notificationRecDocument : notificationRecDocList) {
            NotificationRecipientDataBean notificationRecipientDataBean = new NotificationRecipientDataBean();
            convertNotificationRecipientDocumentToNotificationRecipientDataBean(notificationRecDocument, notificationRecipientDataBean);
            notificationRecipientDataBeans.add(notificationRecipientDataBean);
            notificationIds.add(notificationRecDocument.getHkNotificationRecipientEntityPK().getNotification());
        }
        notificationDocumentService.markUserNotificationsAsSeen(centerLoginDataBean.getId(), notificationIds, centerLoginDataBean.getCompanyId());
        return notificationRecipientDataBeans;
    }

    public void removeNotifications(String notificationId) {
        notificationDocumentService.removeUserNotification(centerLoginDataBean.getId(), notificationId, centerLoginDataBean.getCompanyId());
    }
}
