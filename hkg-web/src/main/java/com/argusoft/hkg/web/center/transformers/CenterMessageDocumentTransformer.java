/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkNotificationDocumentService;
import com.argusoft.hkg.nosql.core.HkUserService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.MessagingDataBean;
import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author akta
 */
@Service
public class CenterMessageDocumentTransformer {

    @Autowired
    HkNotificationDocumentService notificationDocumentService;

    @Autowired
    HkUserService hkUserService;

    @Autowired
    CenterCustomFieldTransformer customFieldTransformerBean;

    @Autowired
    LoginDataBean loginDataBean;

    public List<MessagingDataBean> retrieveAllMessagesByUser(Long userId, Boolean isArchive, boolean Priority) {
        List<MessagingDataBean> hkMessagingDataBeans = new ArrayList<>();
        List<HkMessageRecipientDtlDocument> retrievePriorityNotifications = notificationDocumentService.retrievePriorityNotifications(userId, isArchive, Priority);
        if (!CollectionUtils.isEmpty(retrievePriorityNotifications)) {
            List<String> recipientCodes = new ArrayList<>();
            for (HkMessageRecipientDtlDocument hkMessageRecipientDtl : retrievePriorityNotifications) {
                recipientCodes.add(hkMessageRecipientDtl.getMessageFrom() + ":" + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
            }
            Map<String, String> retrieveRecipientNames = hkUserService.retrieveRecipientNames(recipientCodes);
            for (HkMessageRecipientDtlDocument hkMessageRecipientDtl : retrievePriorityNotifications) {
                MessagingDataBean messagingDataBean = new MessagingDataBean();
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId
                        = customFieldTransformerBean.retrieveDocumentByInstanceId(hkMessageRecipientDtl.getHkMessage().getId(), HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE, loginDataBean.getCompanyId());
                if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
                    if (retrieveDocumentByInstanceId != null) {
                        List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                        if (maps != null) {
                            for (Map<Long, Map<String, Object>> map : maps) {
                                messagingDataBean.setMessageCustom(map.get(hkMessageRecipientDtl.getHkMessage().getId()));
                            }
                        }
                    }
                }
                convertMessageRecipientDTLToMessagingDataBean(hkMessageRecipientDtl, messagingDataBean);
                messagingDataBean.setCreatedBy(retrieveRecipientNames.get(hkMessageRecipientDtl.getMessageFrom() + ":" + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
                hkMessagingDataBeans.add(messagingDataBean);
            }
            Collections.sort(hkMessagingDataBeans, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    MessagingDataBean obj1 = (MessagingDataBean) o1;
                    MessagingDataBean obj2 = (MessagingDataBean) o2;
                    return obj2.getCreatedOn().compareTo(obj1.getCreatedOn());
                }
            });
        }

        return hkMessagingDataBeans;
    }

    private MessagingDataBean convertMessageRecipientDTLToMessagingDataBean(HkMessageRecipientDtlDocument hkMessageRecipientDtl, MessagingDataBean messagingDataBean) {
        messagingDataBean.setId(hkMessageRecipientDtl.getHkMessageRecipientDtlPK().getMessageTo());
        messagingDataBean.setMessageObj(hkMessageRecipientDtl.getHkMessage().getId());
//        String msg = null;
        messagingDataBean.setStatus(hkMessageRecipientDtl.getStatus());
//        if (hkMessageRecipientDtl.getRepliedOn() == null) {
//            msg = hkMessageRecipientDtl.getHkMessage().getMessageBody();
//            messagingDataBean.setCreatedOn(hkMessageRecipientDtl.getHkMessage().getCreatedOn());
//        } else {
//            msg = hkMessageRecipientDtl.getRepliedMessageBody();
//            messagingDataBean.setCreatedOn(hkMessageRecipientDtl.getRepliedOn());
//        }
        messagingDataBean.setCreatedOn(hkMessageRecipientDtl.getHkMessage().getCreatedOn());
        messagingDataBean.setMessageBody(hkMessageRecipientDtl.getHkMessage().getMessageBody());
//        if (msg.length() > 25) {
//            messagingDataBean.setShortMessage(msg.substring(0, 24));
//        } else {
//            messagingDataBean.setShortMessage(msg);
//        }
        messagingDataBean.setIsAttended(hkMessageRecipientDtl.isIsAttended());

        return messagingDataBean;
    }

    public int retrieveunattendedMessageCount(Long userId, Boolean isArchive, boolean Priority) {
        int retrieveUnattendedMessageCount = notificationDocumentService.retrieveUnattendedMessageCount(userId, Boolean.FALSE, Boolean.FALSE);
        return retrieveUnattendedMessageCount;
    }
}
