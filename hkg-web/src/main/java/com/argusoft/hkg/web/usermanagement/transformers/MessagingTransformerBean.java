/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntityPK;
import com.argusoft.hkg.model.HkMessageRecipientEntity;
import com.argusoft.hkg.model.HkMessageRecipientEntityPK;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.MessagingDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMGroupService;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import com.argusoft.usermanagement.common.model.UMUserGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author mansi
 */
@Service
public class MessagingTransformerBean {

    @Autowired
    UMGroupService userGroupService;
    @Autowired
    UMUserService userService;
    @Autowired
    UMRoleService roleService;
    @Autowired
    UMDepartmentService departmentService;
    @Autowired
    HkNotificationService notificationService;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkFoundationService foundationService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    public MessagingTransformerBean() {
    }

    public List<UMUser> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive) {
        List<UMUser> users = userManagementServiceWrapper.retrieveUsersByCompanyByStatus(companyId, user, isActive, false, loginDataBean.getId());
        return users;
    }

    public List<UMRole> retrieveRolesByCompanyByStatus(Long companyId, String role, Boolean isActive) {
        List<UMRole> roles = userManagementServiceWrapper.retrieveRolesByCompanyByStatus(companyId, role, isActive, false,loginDataBean.getPrecedence());
        return roles;
    }

    public List<UMDepartment> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        List<UMDepartment> departments = userManagementServiceWrapper.retrieveDepartmentsByCompanyByStatus(companyId, department, isActive);
        return departments;
    }

    public List<SelectItem> searchDepartmentsOfOtherCompany(String query, Boolean isActive) {
        List<SelectItem> hkSelectItems = null;
        List<UMDepartment> departments = userManagementServiceWrapper.searchDepartmentsOfOtherCompany(loginDataBean.getCompanyId(), query, isActive);
        if (!CollectionUtils.isEmpty(departments)) {
            Map<Long, UMCompany> franchiseMap = userManagementServiceWrapper.retrieveActiveFranchises(true);
            if (!CollectionUtils.isEmpty(franchiseMap)) {
                hkSelectItems = new LinkedList<>();
                for (UMDepartment uMDepartment : departments) {
                    StringBuilder nameBuilder = new StringBuilder(uMDepartment.getDeptName());
                    if (uMDepartment.getCompany() != null && franchiseMap.containsKey(uMDepartment.getCompany())) {
                        nameBuilder.append(" - ");
                        nameBuilder.append(franchiseMap.get(uMDepartment.getCompany()).getName());
                    }
                    hkSelectItems.add(new SelectItem(uMDepartment.getId(), nameBuilder.toString(), HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT));
                }
            }
        }

        return hkSelectItems;
    }

    public List<UMUserGroup> retrieveGroupsByCompanyByStatus(Long companyId, String group, Boolean isActive) {
        List<UMUserGroup> groups = userManagementServiceWrapper.retrieveGroupsByCompanyByStatus(companyId, group, isActive);
        return groups;
    }

    public List<UMUser> retrieveActivitiesByCompanyByStatus(Long companyId, String activity, Boolean isActive) {
        List<UMUser> users = userManagementServiceWrapper.retrieveActivitiesByCompanyByStatus(companyId, activity, isActive);
        return users;
    }

    public MessagingDataBean convertMessageToMessagingDataBean(HkMessageEntity hkMessage, MessagingDataBean hkMessagingDataBean, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId) {
        if (hkMessagingDataBean == null) {
            hkMessagingDataBean = new MessagingDataBean();
        }
        hkMessagingDataBean.setMessageObj(hkMessage.getId());
        hkMessagingDataBean.setIsArchive(hkMessage.getIsArchive());
        hkMessagingDataBean.setCreatedOn(hkMessage.getCreatedOn());
        hkMessagingDataBean.setMessageBody(hkMessage.getMessageBody());
        hkMessagingDataBean.setCopyMessage(hkMessage.getCopyMessage());
        hkMessagingDataBean.setId(hkMessage.getId());
        if (hkMessage.getMessageBody().length() > 25) {
            hkMessagingDataBean.setShortMessage(hkMessage.getMessageBody().substring(0, 24));
        } else {
            hkMessagingDataBean.setShortMessage(hkMessage.getMessageBody());
        }
        hkMessagingDataBean.setHasPriority(hkMessage.getHasPriority());
        hkMessagingDataBean.setIsAttended(false);
        if (retrieveDocumentByInstanceId != null) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    hkMessagingDataBean.setMessageCustom(map.get(hkMessage.getId()));
                }
            }
        }
        return hkMessagingDataBean;
    }

    public HkMessageEntity convertMessagingDataBeanToMessage(MessagingDataBean messagingDataBean, HkMessageEntity message) {
        if (message == null) {
            message = new HkMessageEntity();
            message.setCreatedBy(loginDataBean.getId());
            message.setCreatedOn(new Date());
            message.setIsArchive(false);
            message.setFranchise(messagingDataBean.getFranchise());
        } else {
            message.setIsArchive(messagingDataBean.isIsArchive());
        }
        message.setMessageBody(messagingDataBean.getMessageBody());
        String copyMessage = messagingDataBean.getMessageBody().replaceAll("\\s+", "");
        message.setHasPriority(messagingDataBean.isHasPriority());
        message.setCopyMessage(copyMessage);
        return message;
    }

    public String saveMessage(MessagingDataBean hkMessagingDataBean, Long userId) throws UMUserManagementException {

        String nameRecipient = hkMessagingDataBean.getNameRecipient();//eg. 6:G,3:E,2:R
        HkMessageRecipientEntity messageRecipiet = null;
        List<HkMessageRecipientEntity> hkMessageRecipient = new ArrayList<>();
        String[] split = nameRecipient.split(",");
        String response = HkSystemConstantUtil.SUCCESS;
        try {
            List<String> recipientCodes = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                String string = split[i];
                String[] split1 = string.split(":");
                Long id = new Long(split1[0]);
                messageRecipiet = new HkMessageRecipientEntity();
                messageRecipiet.setHkMessageRecipentPK(new HkMessageRecipientEntityPK(null, split1[1], id));
                messageRecipiet.setIsArchive(false);
                hkMessageRecipient.add(messageRecipiet);
                recipientCodes.add(string);
            }
            Set<Long> retrieveRecipientIds = userManagementServiceWrapper.retrieveRecipientIds(recipientCodes, true);
            if (!CollectionUtils.isEmpty(retrieveRecipientIds)) {
                retrieveRecipientIds.remove(loginDataBean.getId());
            }
            HkMessageEntity message = null;
            message = convertMessagingDataBeanToMessage(hkMessagingDataBean, message);
            message.setHkMessageRecipientList(hkMessageRecipient);
            notificationService.sendMessage(message, retrieveRecipientIds);
            createOrUpdateCustomField(message.getId(), hkMessagingDataBean);
        } catch (Exception e) {
            response = HkSystemConstantUtil.FAILURE;
        }
        return response;
    }

    public void createOrUpdateCustomField(Long id, MessagingDataBean hkMessagingDataBean) {
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(id, hkMessagingDataBean.getMessageCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(hkMessagingDataBean.getMessageDbType())) {
            for (Map.Entry<String, String> entrySet : hkMessagingDataBean.getMessageDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        List<CustomField> messageCustomField = customFieldSevice.makeCustomField(val, hkMessagingDataBean.getMessageDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE.toString(), loginDataBean.getCompanyId(), id);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, messageCustomField);
        customFieldSevice.saveOrUpdate(id, HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE, loginDataBean.getCompanyId(), map);
    }

    public List<MessagingDataBean> retrieveAllMessagesByUser(Long userId, Boolean isArchive, boolean Priority) {
        List<MessagingDataBean> hkMessagingDataBeans = new ArrayList<>();
        List<HkMessageRecipientDtlEntity> retrievePriorityNotifications = notificationService.retrievePriorityNotifications(userId, isArchive, Priority);
        if (!CollectionUtils.isEmpty(retrievePriorityNotifications)) {
            List<String> recipientCodes = new ArrayList<>();
            for (HkMessageRecipientDtlEntity hkMessageRecipientDtl : retrievePriorityNotifications) {
                recipientCodes.add(hkMessageRecipientDtl.getMessageFrom() + ":" + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
            }
            Map<String, String> retrieveRecipientNames = userManagementServiceWrapper.retrieveRecipientNames(recipientCodes);
            for (HkMessageRecipientDtlEntity hkMessageRecipientDtl : retrievePriorityNotifications) {
                MessagingDataBean messagingDataBean = new MessagingDataBean();
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(hkMessageRecipientDtl.getHkMessage().getId(), HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE, loginDataBean.getCompanyId());
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

    public int retrieveunattendedMessageCount(Long userId, Boolean isArchive, boolean Priority) {
        int retrieveUnattendedMessageCount = notificationService.retrieveUnattendedMessageCount(userId, Boolean.FALSE, Boolean.FALSE);
        return retrieveUnattendedMessageCount;
    }

    public List<MessagingDataBean> retrieveAllMessagesByUserByType(Long userId, Long companyId, Boolean isArchive, String type) throws GenericDatabaseException {

        List<MessagingDataBean> hkMessagingDataBeans = new ArrayList<>();
        List<HkMessageEntity> finalList = new ArrayList<>();
        List<Boolean> attendedList = new ArrayList<>();
        if (type != null) {
            if (type.equalsIgnoreCase("Inbox")) {
                Map<List<HkMessageEntity>, List<Boolean>> retrieveInboxMessages = notificationService.retrieveInboxMessages(userId, null, null, null, isArchive, companyId, true);
                for (Map.Entry<List<HkMessageEntity>, List<Boolean>> entry : retrieveInboxMessages.entrySet()) {
                    finalList = entry.getKey();
                    attendedList = entry.getValue();
                    break;
                }
            } else if (type.equalsIgnoreCase("Sent")) {
                finalList = notificationService.retrieveSentMessages(userId, null, null, null, isArchive, companyId, true);
            } else {
                Map<List<HkMessageEntity>, List<Boolean>> retrieveInboxMessages = notificationService.retrieveInboxMessages(userId, Boolean.TRUE, null, null, isArchive, companyId, true);
                for (Map.Entry<List<HkMessageEntity>, List<Boolean>> entry : retrieveInboxMessages.entrySet()) {
                    finalList = entry.getKey();
                    attendedList = entry.getValue();
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(finalList)) {
                List<String> ids = new ArrayList<>();
                for (HkMessageEntity message : finalList) {
                    ids.add(message.getCreatedBy() + ":" + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                }
                Map<String, String> retrieveRecipientNames = userManagementServiceWrapper.retrieveRecipientNames(ids);
                List<String> recipientCodesDtl = new ArrayList<>();
                Map<Long, List<String>> msgMap = new HashMap<>();
                for (HkMessageEntity hkMessage : finalList) {
                    List<HkMessageRecipientEntity> hkMessageRecipientList = hkMessage.getHkMessageRecipientList();
                    if (!CollectionUtils.isEmpty(hkMessageRecipientList)) {
                        List<String> temp = new ArrayList<>();
                        for (HkMessageRecipientEntity hkMessageRecipient : hkMessageRecipientList) {
                            recipientCodesDtl.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
                            temp.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
                        }
                        msgMap.put(hkMessage.getId(), temp);
                    }
                }
                Map<String, String> retrieveRecipientNamesDtl = userManagementServiceWrapper.retrieveRecipientNames(recipientCodesDtl);
                int i = 0;
                for (HkMessageEntity hkMessageEntity : finalList) {
                    MessagingDataBean messagingDataBean = new MessagingDataBean();
                    StringBuilder builder = new StringBuilder(String.valueOf(hkMessageEntity.getCreatedBy()));
                    builder.append(":").append(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                    String get = retrieveRecipientNames.get(builder.toString());
                    Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(hkMessageEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE, companyId);
                    if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
                        convertMessageToMessagingDataBean(hkMessageEntity, messagingDataBean, retrieveDocumentByInstanceId);
                    } else {
                        convertMessageToMessagingDataBean(hkMessageEntity, messagingDataBean, null);
                    }
                    messagingDataBean.setMessageType(type);
                    messagingDataBean.setCreatedBy(get);
                    try {
                        if (type.equalsIgnoreCase("Inbox") || type.equalsIgnoreCase("Priority")) {
                            messagingDataBean.setIsAttended(attendedList.get(i));
                        } else {
                            messagingDataBean.setIsAttended(true);
                            setRecipientNameInDataBean(hkMessageEntity, messagingDataBean, retrieveRecipientNamesDtl, msgMap);
                        }
                    } catch (GenericDatabaseException ex) {
                        Logger.getLogger(MessagingTransformerBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    hkMessagingDataBeans.add(messagingDataBean);
                    i++;
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
        }
        return hkMessagingDataBeans;
    }

    private MessagingDataBean convertMessageRecipientDTLToMessagingDataBean(HkMessageRecipientDtlEntity hkMessageRecipientDtl, MessagingDataBean messagingDataBean) {
        messagingDataBean.setId(hkMessageRecipientDtl.getHkMessageRecipientDtlPK().getMessageTo());
        messagingDataBean.setMessageObj(hkMessageRecipientDtl.getHkMessage().getId());
        messagingDataBean.setHasPriority(hkMessageRecipientDtl.getHkMessage().getHasPriority());
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
        messagingDataBean.setIsAttended(hkMessageRecipientDtl.getIsAttended());

        return messagingDataBean;
    }

    public String archiveMessageRecipientDtlEntity(MessagingDataBean hkMessagingDataBean, Long userId) {

        String result = HkSystemConstantUtil.FAILURE;
        try {
            notificationService.removeMessage(hkMessagingDataBean.getMessageObj());
            HkMessageRecipientDtlEntity retrieveMessageRecipientDetailById = notificationService.retrieveMessageRecipientDetailById(new HkMessageRecipientDtlEntityPK(hkMessagingDataBean.getMessageObj(), hkMessagingDataBean.getId()));
            retrieveMessageRecipientDetailById.setIsArchive(true);
            notificationService.updateHkMessageRecipientDtlEntity(retrieveMessageRecipientDetailById);
            result = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void markAsClosed(MessagingDataBean hkMessagingDataBean) {
        HkMessageRecipientDtlEntityPK hkMessageRecipientDtlEntityPK = new HkMessageRecipientDtlEntityPK(hkMessagingDataBean.getMessageObj(), loginDataBean.getId());
        HkMessageRecipientDtlEntity retrieveMessageRecipientDetailById = notificationService.retrieveMessageRecipientDetailById(hkMessageRecipientDtlEntityPK);
        retrieveMessageRecipientDetailById.setIsAttended(true);
        retrieveMessageRecipientDetailById.setAttendedOn(new Date());
        retrieveMessageRecipientDetailById.setStatus(HkSystemConstantUtil.MessageStatus.CLOSED);
        notificationService.updateHkMessageRecipientDtlEntity(retrieveMessageRecipientDetailById);
    }

    public List<MessagingDataBean> retrieveMessagesByCompanyByStatus(Long userId, Long companyId, String msgSearchString, Boolean isArchive, boolean recipientRequired) throws GenericDatabaseException {
        if (msgSearchString != null) {
            String copyMessage = msgSearchString.replaceAll("\\s+", "");
            msgSearchString = copyMessage;
        }
        List<HkMessageEntity> messages = notificationService.searchMessageTemplates(msgSearchString, userId, companyId, isArchive);
        Integer count = 0;
        Integer checkCount = 0;
        List<HkMessageEntity> newMessageList = null;
        List<HkMessageEntity> newMsgList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messages)) {
            Set<HkMessageEntity> messageSet = new TreeSet(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    HkMessageEntity oldMessageEntity = (HkMessageEntity) o1;
                    HkMessageEntity newMessageEntity = (HkMessageEntity) o2;
                    if (oldMessageEntity.getCopyMessage().equalsIgnoreCase(newMessageEntity.getCopyMessage())) {
                        return 0;
                    }
                    return 1;
                }
            });
            messageSet.addAll(messages);
            newMsgList.addAll(messageSet);
            newMessageList = new LinkedList<>();
            if (!CollectionUtils.isEmpty(newMsgList)) {
                HkSystemConfigurationEntity systemConfiguration = foundationService.retrieveSystemConfigurationByKey(HkSystemConstantUtil.MAX_MESSAGE_SAVE_LENGTH, loginDataBean.getCompanyId());
                if (systemConfiguration.getKeyValue() != null) {
                    String value = systemConfiguration.getKeyValue();
                    Integer maxValue = Integer.parseInt(value);
                    Collections.sort(newMsgList, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            HkMessageEntity oldMessageEntity = (HkMessageEntity) o1;
                            HkMessageEntity newMessageEntity = (HkMessageEntity) o2;
                            return newMessageEntity.getCreatedOn().compareTo(oldMessageEntity.getCreatedOn());
                        }
                    });
                    for (HkMessageEntity hkMessageEntity : newMsgList) {
                        if (!count.equals(maxValue)) {
                            newMessageList.add(hkMessageEntity);
                            count++;
                        } else {
                            checkCount = 1;
                            break;
                        }
                    }
                } else {
                    newMessageList.addAll(newMsgList);
                }
            }
        }
        List<MessagingDataBean> dataBeans = convertSearchMessageToBean(newMessageList, userId, recipientRequired);
        return dataBeans;
    }

    public List<MessagingDataBean> convertSearchMessageToBean(List<HkMessageEntity> messages, Long userId, boolean recipientRequired) {
        List<MessagingDataBean> dataBeans = null;

        if (!CollectionUtils.isEmpty(messages)) {
            dataBeans = new ArrayList<>();
            MessagingDataBean hkMessagingDataBean = null;
            List<String> ids = new ArrayList<>();
            for (HkMessageEntity message : messages) {
                ids.add(message.getCreatedBy() + ":" + HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
            }
            Map<String, String> retrieveRecipientNames = userManagementServiceWrapper.retrieveRecipientNames(ids);
            Long id = loginDataBean.getId();
            List<String> recipientCodesDtl = new ArrayList<>();
            Map<Long, List<String>> msgMap = new HashMap<>();
            Map<String, String> retrieveRecipientNamesDtl = new HashMap<>();
            if (recipientRequired) {
                for (HkMessageEntity hkMessage : messages) {
                    List<HkMessageRecipientEntity> hkMessageRecipientList = hkMessage.getHkMessageRecipientList();
                    if (!CollectionUtils.isEmpty(hkMessageRecipientList)) {
                        List<String> temp = new ArrayList<>();
                        for (HkMessageRecipientEntity hkMessageRecipient : hkMessageRecipientList) {
                            recipientCodesDtl.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
                            temp.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
                        }
                        msgMap.put(hkMessage.getId(), temp);
                    }
                }
                retrieveRecipientNamesDtl = userManagementServiceWrapper.retrieveRecipientNames(recipientCodesDtl);
            }
            for (HkMessageEntity hkMessage : messages) {
                hkMessagingDataBean = new MessagingDataBean();
                this.convertMessageToMessagingDataBean(hkMessage, hkMessagingDataBean, null);
                if (userId == null) {
                    if (id.equals(hkMessage.getCreatedBy())) {
                        hkMessagingDataBean.setMessageType("Sent");
                    } else {
                        hkMessagingDataBean.setMessageType("Inbox");
                    }
                }
                StringBuilder builder = new StringBuilder(String.valueOf(hkMessage.getCreatedBy()));
                builder.append(":").append(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                String get = retrieveRecipientNames.get(builder.toString());
                hkMessagingDataBean.setCreatedBy(get);
                if (recipientRequired) {
                    try {
                        setRecipientNameInDataBean(hkMessage, hkMessagingDataBean, retrieveRecipientNamesDtl, msgMap);

                    } catch (GenericDatabaseException ex) {
                        Logger.getLogger(MessagingTransformerBean.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                dataBeans.add(hkMessagingDataBean);
            }
        }
        return dataBeans;
    }

    public void setRecipientNameInDataBean(HkMessageEntity hkMessage, MessagingDataBean hkMessagingDataBean, Map<String, String> retrieveRecipientNames, Map<Long, List<String>> msgMap) throws GenericDatabaseException {
        StringBuilder nameRecipientBuilder = new StringBuilder();
        StringBuilder nameRecipientIdBuilder = new StringBuilder();
        List<String> recipientCodes = msgMap.get(hkMessage.getId());
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            for (String id : recipientCodes) {
                String name = retrieveRecipientNames.get(id);
                if (nameRecipientIdBuilder.toString().length() > 0) {
                    nameRecipientIdBuilder.append(",");
                }
                nameRecipientIdBuilder.append(id);
                if (nameRecipientBuilder.toString().length() > 0) {
                    nameRecipientBuilder.append("\n");
                }
                String[] split = id.split(":");
                if (!split[1].equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
                    nameRecipientBuilder.append("@").append(split[1]).append(" ");
                }
                nameRecipientBuilder.append(name);
            }
        }
        hkMessagingDataBean.setNameRecipient(nameRecipientBuilder.toString());
        hkMessagingDataBean.setNameRecipientIds(nameRecipientIdBuilder.toString());
    }

    public String archiveTemplate(Long id) {
        String result = HkSystemConstantUtil.FAILURE;
        try {
            notificationService.removeMessageTemplate(id);
            result = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String archiveMessage(List<MessagingDataBean> messages) {

        String result = HkSystemConstantUtil.FAILURE;
        try {
            Long id = loginDataBean.getId();
            for (MessagingDataBean hkMessagingDataBean : messages) {
                if (hkMessagingDataBean.getMessageType().equalsIgnoreCase("Sent")) {
                    notificationService.removeMessage(hkMessagingDataBean.getMessageObj());
                } else {
                    HkMessageRecipientDtlEntity retrieveMessageRecipientDetailById = notificationService.retrieveMessageRecipientDetailById(new HkMessageRecipientDtlEntityPK(hkMessagingDataBean.getMessageObj(), id));
                    if (retrieveMessageRecipientDetailById != null) {
                        retrieveMessageRecipientDetailById.setIsArchive(true);
                        notificationService.updateHkMessageRecipientDtlEntity(retrieveMessageRecipientDetailById);
                    }
                }
            }
//            for (Long long1 : messages) {
//                notificationService.removeMessage(long1);
//            }
            result = HkSystemConstantUtil.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<MessagingDataBean> searchByMessage(String searchString) throws GenericDatabaseException {
        Long companyId = loginDataBean.getCompanyId();
        List<MessagingDataBean> convertSearchMessageToBean = null;
        Long id = loginDataBean.getId();
        List<Long> searchUsersByName = userManagementServiceWrapper.searchUsersByName(searchString, companyId);
        List<HkMessageEntity> searchMessages = notificationService.searchMessages(searchString, id, searchUsersByName, companyId);
        convertSearchMessageToBean = convertSearchMessageToBean(searchMessages, null, true);
        if (CollectionUtils.isEmpty(convertSearchMessageToBean)) {
            return new ArrayList<>();
        } else {
            return convertSearchMessageToBean;
        }

    }

    public MessagingDataBean retrieveById(Long primaryKey) throws GenericDatabaseException {
        HkMessageEntity retrievedMessage = notificationService.retrieveMessageById(primaryKey, true);
        MessagingDataBean messagingDataBean = new MessagingDataBean();
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(retrievedMessage.getId(), HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            messagingDataBean = convertMessageToMessagingDataBean(retrievedMessage, messagingDataBean, retrieveDocumentByInstanceId);
        } else {
            messagingDataBean = convertMessageToMessagingDataBean(retrievedMessage, messagingDataBean, null);
        }
        List<String> recipientCodesDtl = new ArrayList<>();
        Map<Long, List<String>> msgMap = new HashMap<>();
        List<HkMessageRecipientEntity> hkMessageRecipientList = retrievedMessage.getHkMessageRecipientList();
        if (!CollectionUtils.isEmpty(hkMessageRecipientList)) {
            List<String> temp = new ArrayList<>();
            for (HkMessageRecipientEntity hkMessageRecipient : hkMessageRecipientList) {
                recipientCodesDtl.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
                temp.add(hkMessageRecipient.getHkMessageRecipentPK().getReferenceInstance() + ":" + hkMessageRecipient.getHkMessageRecipentPK().getReferenceType());
            }
            msgMap.put(retrievedMessage.getId(), temp);
        }
        Map<String, String> retrieveRecipientNamesDtl = userManagementServiceWrapper.retrieveRecipientNames(recipientCodesDtl);
        setRecipientNameInDataBean(retrievedMessage, messagingDataBean, retrieveRecipientNamesDtl, msgMap);
        return messagingDataBean;
    }

    public List<SelectItem> getSelectItemListFromUserList(List<UMUser> umUsers) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUsers)) {
            for (UMUser uMUser : umUsers) {
                StringBuilder name = new StringBuilder();
                if (uMUser.getUserCode() != null) {
                    name.append(uMUser.getUserCode()).append(" - ");
                }
                name.append(uMUser.getFirstName());
                if (uMUser.getLastName() != null) {
                    name.append(" ").append(uMUser.getLastName());
                }
                hkSelectItems.add(new SelectItem(uMUser.getId(),
                        name.toString(),
                        HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
            }
        }
        return hkSelectItems;
    }
}
