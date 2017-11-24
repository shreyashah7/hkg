/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class to test methods of HkNotificationService.
 *
 * @author Mital
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkNotificationServiceImplTest {

    @Resource
    private HkNotificationService hkNotificationService;

    private HkMessageEntity hkMessage;
    private HkNotificationEntity hkNotification;
    private Long recipient = 1L;
    private Set<Long> recipients;

    public HkNotificationServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        hkMessage = new HkMessageEntity();
        hkMessage.setCreatedBy(1L);
        hkMessage.setCreatedOn(new Date());
        hkMessage.setFranchise(0L);
        hkMessage.setHasPriority(false);
        hkMessage.setMessageBody("Meeting at 12:30 PM.");
        hkMessage.setIsArchive(false);

        recipients = new HashSet<>();
        recipients.add(recipient);

        List<HkMessageRecipientEntity> hkMessageRecipientList = new ArrayList<>();
        hkMessageRecipientList.add(new HkMessageRecipientEntity(null, "D", 10L));
        hkMessage.setHkMessageRecipientList(hkMessageRecipientList);
        String des = "{\"MSTR_NM\":\"Caste\"}";
        hkNotification = new HkNotificationEntity(null, HkSystemConstantUtil.NotificationType.HOLIDAY, new Date(), des, 1L, false);
        hkNotification.setInstanceId(1L);
        hkNotification.setInstanceType(HkSystemConstantUtil.NotificationInstanceType.ADD_HOLIDAY);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sendMessage method, of class HkNotificationService.
     */
    @Test
    public void testSendMessage() {
        hkNotificationService.sendMessage(hkMessage, recipients);
    }

    /**
     * Test of searchMessageTemplate method of class HkNotificationService.
     */
   // @Test
    public void testSearchMessageTemplates() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        String searchMessage = "at 12";
        Long sender = 1L;
        Long franchise = 0L;
        Boolean archiveStatus = false;
        List<HkMessageEntity> result = hkNotificationService.searchMessageTemplates(searchMessage, sender, franchise, archiveStatus);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of removeMessageTemplate method of class HkNotificationService.
     */
    @Test
    public void testRemoveMessageTemplate() {
        hkNotificationService.sendMessage(hkMessage, recipients);
        hkNotificationService.removeMessageTemplate(hkMessage.getId());

    }

    /**
     * Test of removeUnattendedMessageCount method of class
     * HkNotificationService.
     */
    @Test
    public void testRemoveUnattendedMessageCount() {
        hkNotificationService.sendMessage(hkMessage, recipients);
        int i = hkNotificationService.retrieveUnattendedMessageCount(recipient, Boolean.FALSE, Boolean.FALSE);
        assertTrue("Unattended Message count retrieved successfully", i == 1);
    }

    /**
     * Test of searchMessage method, of class HkNotificationService.
     */
    @Test
    public void testSearchMessages() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        List<HkMessageEntity> result = hkNotificationService.searchMessages("at", hkMessage.getCreatedBy(), null, 0L);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of updateHkMessageRecipientDtl method, of class
     * HkNotificationService.
     */
    @Test
    public void testUpdateHkMessageRecipientDtlEntity() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        HkMessageRecipientDtlEntity hkMsgRecipientDtl = hkNotificationService.retrieveMessageRecipientDetailById(new HkMessageRecipientDtlEntityPK(hkMessage.getId(), recipient));
        hkMsgRecipientDtl.setIsAttended(true);
        hkMsgRecipientDtl.setAttendedOn(new Date());
        hkMsgRecipientDtl.setRepliedOn(new Date());
        hkMsgRecipientDtl.setRepliedMessageBody("Okay");
        hkNotificationService.updateHkMessageRecipientDtlEntity(hkMsgRecipientDtl);

        HkMessageRecipientDtlEntity resultDtlObj = hkNotificationService.retrieveMessageRecipientDetailById(new HkMessageRecipientDtlEntityPK(hkMessage.getId(), recipient));
        assertEquals("Okay", resultDtlObj.getRepliedMessageBody());
    }

    /**
     * Test of removeMessage method, of class HkNotificationService.
     */
    @Test
    public void testRemoveMessage_HkMessageEntity() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        hkNotificationService.removeMessage(hkMessage);

        HkMessageEntity resultObj = hkNotificationService.retrieveMessageById(hkMessage.getId(), false);
        assertNull("Removed successfully.. ", resultObj);
    }

    /**
     * Test of removeMessage method, of class HkNotificationService.
     */
    @Test
    public void testRemoveMessage_Long() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        hkNotificationService.removeMessage(hkMessage.getId());

        HkMessageEntity resultObj = hkNotificationService.retrieveMessageById(hkMessage.getId(), false);
        assertNull("Removed successfully.. ", resultObj);
    }

    /**
     * Test of retrievePriorityNotifications method, of class
     * HkNotificationService.
     */
    @Test
    public void testRetrievePriorityNotifications() {
        hkMessage.setHasPriority(true);
        hkNotificationService.sendMessage(hkMessage, recipients);

        List<HkMessageRecipientDtlEntity> result = hkNotificationService.retrievePriorityNotifications(recipient, Boolean.FALSE, true);
        assertNotNull(result);
        for (HkMessageRecipientDtlEntity recipientDtl : result) {
            assertFalse(recipientDtl.getIsAttended());
            assertTrue(recipientDtl.getHasPriority());
        }
    }

    /**
     * Test of retrieveMessageById method, of class HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveMessageById() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        HkMessageEntity result = hkNotificationService.retrieveMessageById(hkMessage.getId(), false);
        assertEquals(hkMessage, result);
    }

    /**
     * Test of retrieveInboxMessages method, of class HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveInboxMessages() {
        hkNotificationService.sendMessage(hkMessage, recipients);
        Map<List<HkMessageEntity>, List<Boolean>> retrieveInboxMessages = hkNotificationService.retrieveInboxMessages(recipient, false, null, null, false, hkMessage.getFranchise(), true);
        List<HkMessageEntity> result = new ArrayList<>();
        for (Map.Entry<List<HkMessageEntity>, List<Boolean>> entry : retrieveInboxMessages.entrySet()) {
            result = entry.getKey();
            break;
        }
        assertTrue(result.size() > 0);
    }

    /**
     * Test of retrieveSentMessages method, of class HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveSentMessages() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        List<HkMessageEntity> result = hkNotificationService.retrieveSentMessages(hkMessage.getCreatedBy(), false, null, null, false, hkMessage.getFranchise(), true);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of retrieveMessageRecipientDetailById method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveMessageRecipientDetailById() {
        hkNotificationService.sendMessage(hkMessage, recipients);

        HkMessageRecipientDtlEntity resultDtlObj = hkNotificationService.retrieveMessageRecipientDetailById(new HkMessageRecipientDtlEntityPK(hkMessage.getId(), recipient));
        assertNotNull("Message Recipient Detail retrieved successfully ", resultDtlObj);

    }

    /**
     * Test of sendNotification method, of class HkNotificationServiceImpl.
     */
    @Test
    public void testSendNotification() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        assertNotNull(hkNotification.getId());
    }

    /**
     * Test of sendNotifications method, of class HkNotificationServiceImpl.
     */
    @Test
    public void testSendNotifications() {
        ArrayList<Long> recipient = new ArrayList<>(recipients);
        Map<HkNotificationEntity, List<Long>> notificationMap = new HashMap<>();
        notificationMap.put(hkNotification, recipient);
        hkNotificationService.sendNotifications(notificationMap);
        assertNotNull(hkNotification.getId());
    }

    /**
     * Test of retrieveUnseenNotificationCount method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveUnseenNotificationCount() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        int result = hkNotificationService.retrieveUnseenNotificationCount(recipient, hkNotification.getFranchise());
        assertTrue(result > 0);
    }

    /**
     * Test of retrieveNotificationsForUser method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveNotificationsForUser() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        List<HkNotificationRecipientEntity> result = hkNotificationService.retrieveNotificationsForUser(recipient, null, null, hkNotification.getFranchise());
        assertTrue(result.size() > 0);
    }

    /**
     * Test of retrieveNotificationsForPopup method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testRetrieveNotificationsForPopup() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        List<HkNotificationRecipientEntity> result = hkNotificationService.retrieveNotificationsForPopup(recipient, hkNotification.getFranchise());
        assertTrue(result.size() > 0);
    }

    /**
     * Test of markUserNotificationsAsSeen method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testMarkUserNotificationsAsSeen() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        hkNotificationService.markUserNotificationsAsSeen(recipient, Arrays.asList(hkNotification.getId()), hkNotification.getFranchise());
    }

    /**
     * Test of removeUserNotification method, of class
     * HkNotificationServiceImpl.
     */
    @Test
    public void testRemoveUserNotification() {
        hkNotificationService.sendNotification(hkNotification, new ArrayList<>(recipients));
        hkNotificationService.removeUserNotification(recipient, hkNotification.getId(), hkNotification.getFranchise());
    }
}
