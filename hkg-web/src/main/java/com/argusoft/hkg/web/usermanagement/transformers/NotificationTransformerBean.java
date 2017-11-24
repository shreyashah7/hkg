/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkNotificationService;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntityPK;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRuleService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.taskmanagement.databeans.TaskRecipientDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationConfigurationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.NotificationRecipientDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleDatabean;
import com.argusoft.hkg.web.usermanagement.databeans.RuleSetDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author jyoti
 */
@Service
public class NotificationTransformerBean {

    final String SEPARATOR = ":";
    @Autowired
    HkNotificationService hkNotificationService;

    @Autowired
    LoginDataBean loginDataBean;

    @Autowired
    RuleManagementTransformerBean ruleManagementTransformerBean;

    @Autowired
    HkRuleService ruleService;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    HkCustomFieldService customFieldSevice;

    @Autowired
    HkFieldService fieldService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    @Async
    public void markNotificationAsSeen(Long userId, List<String> notificationIds, Long companyId) {
        hkNotificationService.markUserNotificationsAsSeen(userId, notificationIds, companyId);
    }

    public Map<String, Integer> retrieveNotificationCount(Long userId, Long companyId) {
//        issueService.testTrans();

        Map<String, Integer> notificationCount = new HashMap<>();
        notificationCount.put("count", hkNotificationService.retrieveUnseenNotificationCount(userId, companyId));
        return notificationCount;
    }

    public List<NotificationRecipientDataBean> retrieveNotifications(Long userId, Boolean isSeen, Date afterDate, Long companyId) {
        List<HkNotificationRecipientEntity> hkNotificationRecipientEntity = hkNotificationService.retrieveNotificationsForUser(userId, isSeen, afterDate, companyId);
        List<NotificationRecipientDataBean> notificationRecipientDataBeans = new ArrayList<>();
        return convertNotificationRecipientEntityToNotificationRecipientDataBean(hkNotificationRecipientEntity, notificationRecipientDataBeans);

    }

    public List<NotificationRecipientDataBean> retrieveNotificationsPopUp(Long userId, Long companyId) {
        List<HkNotificationRecipientEntity> hkNotificationRecipientEntitysPopUp = hkNotificationService.retrieveNotificationsForPopup(userId, companyId);
        List<NotificationRecipientDataBean> notificationRecipientDataBeansPopUp = new ArrayList<>();
        return convertNotificationRecipientEntityToNotificationRecipientDataBean(hkNotificationRecipientEntitysPopUp, notificationRecipientDataBeansPopUp);
    }

    public List<NotificationRecipientDataBean> convertNotificationRecipientEntityToNotificationRecipientDataBean(List<HkNotificationRecipientEntity> hkNotificationRecipientEntitys, List<NotificationRecipientDataBean> notificationRecipientDataBeans) {
        if (notificationRecipientDataBeans == null) {
            notificationRecipientDataBeans = new ArrayList<>();
        }
        List<String> notificationIds = new ArrayList<>();
        for (HkNotificationRecipientEntity recipientEntity : hkNotificationRecipientEntitys) {
            NotificationRecipientDataBean notificationRecipientDataBean = new NotificationRecipientDataBean();
            NotificationDataBean notificationDataBean = new NotificationDataBean();
            notificationRecipientDataBean.setIsArchive(recipientEntity.getIsArchive());
            notificationRecipientDataBean.setLastModifiedOn(new Date());
            notificationRecipientDataBean.setIsSeen(recipientEntity.getIsSeen());
            notificationRecipientDataBean.setNotification(recipientEntity.getHkNotificationRecipientEntityPK().getNotification());
            notificationRecipientDataBean.setForUser(recipientEntity.getHkNotificationRecipientEntityPK().getForUser());
            notificationRecipientDataBean.setSeenOn(recipientEntity.getSeenOn());
            notificationRecipientDataBean.setStatus(recipientEntity.getStatus());
            notificationDataBean.setDescription(recipientEntity.getHkNotificationEntity().getDescription());
            notificationDataBean.setId(recipientEntity.getHkNotificationEntity().getId());
            notificationDataBean.setFranchise(recipientEntity.getHkNotificationEntity().getFranchise());
            notificationDataBean.setInstanceId(recipientEntity.getHkNotificationEntity().getInstanceId());
            notificationDataBean.setInstanceType(recipientEntity.getHkNotificationEntity().getInstanceType());

            notificationDataBean.setIsArchive(recipientEntity.getIsArchive());
            notificationDataBean.setNotificationtype(recipientEntity.getHkNotificationEntity().getNotificationType());
            notificationDataBean.setOnDate(recipientEntity.getHkNotificationEntity().getOnDate());
            notificationRecipientDataBean.setNotificationDataBean(notificationDataBean);
            notificationIds.add(notificationDataBean.getId());
            notificationRecipientDataBeans.add(notificationRecipientDataBean);
        }
        markNotificationAsSeen(loginDataBean.getId(), notificationIds, loginDataBean.getCompanyId());
        return notificationRecipientDataBeans;

    }

    public void removeNotifications(String notificationId) {
        this.hkNotificationService.removeUserNotification(loginDataBean.getId(), notificationId, loginDataBean.getCompanyId());
    }

    public void createNotificationConfiguration(NotificationConfigurationDataBean notificationDataBean) {
        HkNotificationConfigurationEntity notificationEntity = this.convertNotificationConfigurationDataBeanToEntity(notificationDataBean, null);
        HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
        
        if (notificationDataBean.getRuleSet() != null) {
            RuleSetDataBean ruleSet = notificationDataBean.getRuleSet();
            if(!CollectionUtils.isEmpty(ruleSet.getRules())){
                for (RuleDatabean rule : ruleSet.getRules()) {
                    rule.setIsActive(true);
                    rule.setIsArchive(false);
                }
            }
            ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.CREATE_OPERATION, notificationDataBean.getRuleSet(), ruleSetDocument);
            ruleSetDocument.setId(null);
            ruleSetDocument.setFranchise(loginDataBean.getCompanyId());
            ruleSetDocument.setLastModifiedOn(new Date());
            ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
            ruleService.saveOrUpdateRuleSet(ruleSetDocument);
            notificationEntity.setAssociatedRule(ruleSetDocument.getId().toString());
        }

        hkNotificationService.createNotificationConfiguration(notificationEntity);
        List<HkNotificationConfigrationRecipientEntity> recipients = new ArrayList<>();
        if (notificationDataBean.getTaskRecipients() != null) {
            for (String recipient : notificationDataBean.getTaskRecipients()) {
                HkNotificationConfigrationRecipientEntity recipientEntity = new HkNotificationConfigrationRecipientEntity();
                String[] vals = recipient.split(":");
                recipientEntity.setHkNotificationConfigrationRecipientEntityPK(new HkNotificationConfigrationRecipientEntityPK(notificationEntity.getId(), Long.parseLong(vals[0]), vals[1]));
                recipientEntity.setIsArchive(Boolean.FALSE);

                recipients.add(recipientEntity);
            }
        }
        hkNotificationService.createNotificationConfigurationRecipients(recipients);

        //Create custom field
        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(notificationEntity.getId(), notificationDataBean.getTaskCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notificationDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : notificationDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, notificationDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION.toString(), loginDataBean.getCompanyId(), notificationEntity.getId());
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        customFieldSevice.saveOrUpdate(notificationEntity.getId(), HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION, loginDataBean.getCompanyId(), map);

    }

    public HkNotificationConfigurationEntity convertNotificationConfigurationDataBeanToEntity(NotificationConfigurationDataBean notificationDataBean, HkNotificationConfigurationEntity notificationConfigurationEntity) {
        if (notificationConfigurationEntity == null) {
            notificationConfigurationEntity = new HkNotificationConfigurationEntity();
        }

        notificationConfigurationEntity.setNotificationName(notificationDataBean.getName());
        if (notificationDataBean.getBasedOn().equals("1")) {
            notificationConfigurationEntity.setNotificationType(HkSystemConstantUtil.NotificationConfigurationType.RULE_BASED);
        } else if (notificationDataBean.getBasedOn().equals("2")) {
            notificationConfigurationEntity.setNotificationType(HkSystemConstantUtil.NotificationConfigurationType.TIME_BASED);
        }
        notificationConfigurationEntity.setId(notificationDataBean.getId());
        notificationConfigurationEntity.setDescription(notificationDataBean.getDesc());
        notificationConfigurationEntity.setCreatedBy(loginDataBean.getId());
        notificationConfigurationEntity.setCreatedOn(new Date());
        notificationConfigurationEntity.setLastModifiedBy(loginDataBean.getId());
        notificationConfigurationEntity.setLastModifiedOn(new Date());
        notificationConfigurationEntity.setFranchise(loginDataBean.getCompanyId());
        if (notificationDataBean.getBasedOn().equals("2")) {
            notificationConfigurationEntity.setRepeatativeMode(notificationDataBean.getRepeatativeMode());
            if (notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
                notificationConfigurationEntity.setWeeklyOnDays(notificationDataBean.getWeeklyOnDays());
                notificationConfigurationEntity.setMonthlyOnDay(null);

            } else if (notificationConfigurationEntity.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
                notificationConfigurationEntity.setMonthlyOnDay(notificationDataBean.getMonthlyOnDay());
                notificationConfigurationEntity.setWeeklyOnDays(null);
            } else {
                notificationConfigurationEntity.setMonthlyOnDay(null);
                notificationConfigurationEntity.setWeeklyOnDays(null);
            }
            notificationConfigurationEntity.setEndRepeatMode(notificationDataBean.getEndRepeatMode());
            if (notificationConfigurationEntity.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)) {
                notificationConfigurationEntity.setEndDate(HkSystemFunctionUtil.convertToServerDate(notificationDataBean.getEndDate(), loginDataBean.getClientRawOffsetInMin()));
                notificationConfigurationEntity.setAfterUnits(null);
            } else {
                notificationConfigurationEntity.setAfterUnits(notificationDataBean.getAfterUnits());
                notificationConfigurationEntity.setEndDate(null);
            }
        } else {
            notificationConfigurationEntity.setRepeatativeMode(null);
            notificationConfigurationEntity.setWeeklyOnDays(null);
            notificationConfigurationEntity.setMonthlyOnDay(null);
            notificationConfigurationEntity.setEndRepeatMode(null);
            notificationConfigurationEntity.setEndDate(null);
            notificationConfigurationEntity.setAfterUnits(null);
        }
        if (notificationDataBean.isInterfaceEType()) {
            notificationConfigurationEntity.setEmailMessage(notificationDataBean.getInterfaceETypeText());
        }
        if (notificationDataBean.isInterfaceWType()) {
            notificationConfigurationEntity.setWebMessage(notificationDataBean.getInterfaceWTypeText());
        }
//        if (notificationDataBean.isInterfaceSType()) {
//            notificationConfigurationEntity.setSmsMessage(notificationDataBean.getInterfaceSTypeText());
//        }
        notificationConfigurationEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        notificationConfigurationEntity.setAtTime(notificationDataBean.getAtTime());
        notificationConfigurationEntity.setActivityGroup(notificationDataBean.getActivity());
        notificationConfigurationEntity.setActivityNode(notificationDataBean.getService());
        notificationConfigurationEntity.setAtTime(notificationDataBean.getAtTime());
        return notificationConfigurationEntity;
    }

    public List<NotificationConfigurationDataBean> retrieveallNotificationConfigurations() {
        List<HkNotificationConfigurationEntity> activeNotifications = hkNotificationService.retrieveAllActiveNotificationConfiguration(loginDataBean.getCompanyId(),null);
        List<Long> ids = new ArrayList<>();
        List<NotificationConfigurationDataBean> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(activeNotifications)) {
            for (HkNotificationConfigurationEntity hkNotificationConfigurationEntity : activeNotifications) {
                NotificationConfigurationDataBean notificationDataBean = this.convertNotificationConfigurationEntityToDataBean(hkNotificationConfigurationEntity, null);
                ids.add(hkNotificationConfigurationEntity.getId());

                result.add(notificationDataBean);
            }
            Map<Long, List<HkNotificationConfigrationRecipientEntity>> mapOfRecipients = hkNotificationService.retrieveActiveNotificationConfigurationRecipients(ids);
            List<NotificationConfigurationDataBean> result1 = new ArrayList<>();
            
            for (NotificationConfigurationDataBean notificationConfigurationDataBean : result) {
                if (mapOfRecipients.get(notificationConfigurationDataBean.getId()) != null) {
                    List<HkNotificationConfigrationRecipientEntity> listOfRec = mapOfRecipients.get(notificationConfigurationDataBean.getId());
                    List<String> arrOfRec = new ArrayList<>();
                    for (HkNotificationConfigrationRecipientEntity hkNotificationConfigrationRecipientEntity : listOfRec) {
                        String rec = hkNotificationConfigrationRecipientEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance() + ":" + hkNotificationConfigrationRecipientEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType();
                        arrOfRec.add(rec);
                    }
                    notificationConfigurationDataBean.setTaskRecipients(Arrays.copyOf(arrOfRec.toArray(), arrOfRec.toArray().length, String[].class));
                }
                Map<String, String> recipientNames = new HashMap<>();
                if(!StringUtils.isEmpty(notificationConfigurationDataBean.getTaskRecipients())){
                    recipientNames = userManagementServiceWrapper.retrieveRecipientNames(Arrays.asList(notificationConfigurationDataBean.getTaskRecipients()));
                }
                List<TaskRecipientDataBean> taskRecipientDataBeanList = new ArrayList<>();
                for (String recName : notificationConfigurationDataBean.getTaskRecipients()) {
                    TaskRecipientDataBean taskRecipientDataBean = new TaskRecipientDataBean();
                    taskRecipientDataBean = this.convertTaskRecipientEntityToTaskRecipientDataBean(recName, taskRecipientDataBean, recipientNames, SEPARATOR);
                    taskRecipientDataBeanList.add(taskRecipientDataBean);
                }
                notificationConfigurationDataBean.setTaskRecipientDataBeanList(taskRecipientDataBeanList);
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(notificationConfigurationDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION, loginDataBean.getCompanyId());
                if (!CollectionUtils.isEmpty(customFields)) {
                    if (customFields != null) {
                        List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                        if (maps != null) {
                            for (Map<Long, Map<String, Object>> map : maps) {
                                notificationConfigurationDataBean.setTaskCustom(map.get(notificationConfigurationDataBean.getId()));
                            }
                        }
                    }
                }
                if (notificationConfigurationDataBean.getTaskCustom() == null) {
                    notificationConfigurationDataBean.setTaskCustom(new HashMap<String, Object>());
                }
                result1.add(notificationConfigurationDataBean);
            }
            return result1;
        }
        return null;
    }

    public TaskRecipientDataBean convertTaskRecipientEntityToTaskRecipientDataBean(String recName, TaskRecipientDataBean taskRecipientDataBean, Map<String, String> recipientNames, String SEPERATOR) {
        taskRecipientDataBean.setRecipientInstance(Long.parseLong(recName.split(":")[0]));
        taskRecipientDataBean.setRecipientType(recName.split(":")[1]);
        taskRecipientDataBean.setRecipientValue(recipientNames.get(taskRecipientDataBean.getRecipientInstance() + SEPERATOR + taskRecipientDataBean.getRecipientType()));
        return taskRecipientDataBean;
    }

    public NotificationConfigurationDataBean convertNotificationConfigurationEntityToDataBean(HkNotificationConfigurationEntity notificationConfigurationEntity, NotificationConfigurationDataBean notificationDataBean) {
        if (notificationDataBean == null) {
            notificationDataBean = new NotificationConfigurationDataBean();
        }

        notificationDataBean.setId(notificationConfigurationEntity.getId());
        notificationDataBean.setName(notificationConfigurationEntity.getNotificationName());
        notificationDataBean.setDesc(notificationConfigurationEntity.getDescription());
        notificationDataBean.setAtTime(notificationConfigurationEntity.getAtTime());
        if (notificationConfigurationEntity.getNotificationType().equals(HkSystemConstantUtil.NotificationConfigurationType.RULE_BASED)) {
            notificationDataBean.setBasedOn("1");
        } else if (notificationConfigurationEntity.getNotificationType().equals(HkSystemConstantUtil.NotificationConfigurationType.TIME_BASED)) {
            notificationDataBean.setBasedOn("2");
        }
        if (notificationConfigurationEntity.getAssociatedRule() != null) {
            ObjectId objectId = new ObjectId(notificationConfigurationEntity.getAssociatedRule());
            RuleSetDataBean ruleList = ruleManagementTransformerBean.retrieveRuleById(objectId, loginDataBean.getCompanyId());
            notificationDataBean.setRuleSet(ruleList);
        }
        if (notificationConfigurationEntity.getWebMessage() != null) {
            notificationDataBean.setInterfaceWType(true);
            notificationDataBean.setInterfaceWTypeText(notificationConfigurationEntity.getWebMessage());
        }
//        if (notificationConfigurationEntity.getSmsMessage() != null) {
//            notificationDataBean.setInterfaceSType(true);
//            notificationDataBean.setInterfaceSTypeText(notificationConfigurationEntity.getSmsMessage());
//        }
        if (notificationConfigurationEntity.getEmailMessage() != null) {
            notificationDataBean.setInterfaceEType(true);
            notificationDataBean.setInterfaceETypeText(notificationConfigurationEntity.getEmailMessage());
        }
        if (notificationConfigurationEntity.getRepeatativeMode() != null) {
            notificationDataBean.setRepeatativeMode(notificationConfigurationEntity.getRepeatativeMode());
            if (notificationDataBean.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.WEEKLY)) {
                notificationDataBean.setWeeklyOnDays(notificationConfigurationEntity.getWeeklyOnDays());

            } else if (notificationDataBean.getRepeatativeMode().equals(HkSystemConstantUtil.TaskRepetitiveMode.MONTHLY)) {
                notificationDataBean.setMonthlyOnDay(notificationConfigurationEntity.getMonthlyOnDay());

            }
        }
        if (notificationConfigurationEntity.getEndRepeatMode() != null) {
            notificationDataBean.setEndRepeatMode(notificationConfigurationEntity.getEndRepeatMode());
            if (notificationDataBean.getEndRepeatMode().equals(HkSystemConstantUtil.TaskEndRepeatMode.ON_DATE)) {
                notificationDataBean.setEndDate(notificationConfigurationEntity.getEndDate());
            } else {
                notificationDataBean.setAfterUnits(notificationConfigurationEntity.getAfterUnits());
            }
        }
        notificationDataBean.setStatus(notificationConfigurationEntity.getStatus());
        notificationDataBean.setActivity(notificationConfigurationEntity.getActivityGroup());
        notificationDataBean.setService(notificationConfigurationEntity.getActivityNode());
        notificationDataBean.setStatus(notificationConfigurationEntity.getStatus());
        return notificationDataBean;
    }

    public void updateNotificationConfiguration(NotificationConfigurationDataBean notificationDataBean) {
        HkNotificationConfigurationEntity configurationEntity = this.convertNotificationConfigurationDataBeanToEntity(notificationDataBean, null);
        if (notificationDataBean.getBasedOn().equals("2")) {
            configurationEntity.setAssociatedRule(null);
        }
        HkRuleSetDocument ruleSetDocument = new HkRuleSetDocument();
        if (notificationDataBean.getRuleSet() != null) {
            
            ruleSetDocument = ruleManagementTransformerBean.convertRuleSetDatabeanToRuleSetDocument(HkSystemConstantUtil.UPDATE_OPERATION, notificationDataBean.getRuleSet(), ruleSetDocument);
            ruleSetDocument.setId(new ObjectId(notificationDataBean.getRuleSet().getId()));
            ruleSetDocument.setFranchise(loginDataBean.getCompanyId());
            ruleSetDocument.setLastModifiedOn(new Date());
            ruleSetDocument.setLastModifiedBy(loginDataBean.getId());
            ruleService.saveOrUpdateRuleSet(ruleSetDocument);
            configurationEntity.setAssociatedRule(ruleSetDocument.getId().toString());
        }

        List<HkNotificationConfigrationRecipientEntity> recipients = new ArrayList<>();
        if (notificationDataBean.getTaskRecipients() != null) {
            for (String recipient : notificationDataBean.getTaskRecipients()) {
                HkNotificationConfigrationRecipientEntity recipientEntity = new HkNotificationConfigrationRecipientEntity();
                String[] vals = recipient.split(":");
                recipientEntity.setHkNotificationConfigrationRecipientEntityPK(new HkNotificationConfigrationRecipientEntityPK(configurationEntity.getId(), Long.parseLong(vals[0]), vals[1]));
                recipientEntity.setIsArchive(Boolean.FALSE);

                recipients.add(recipientEntity);
            }
        }

        hkNotificationService.updateNotificationConfiguration(configurationEntity, recipients);

        Map<Long, Map<String, Object>> val = new HashMap<>();
        val.put(notificationDataBean.getId(), notificationDataBean.getTaskCustom());
        List<String> uiFieldList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notificationDataBean.getDbType())) {
            for (Map.Entry<String, String> entrySet : notificationDataBean.getDbType().entrySet()) {
                uiFieldList.add(entrySet.getKey());
            }
        }
        Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
        //Pass this map to makecustomfieldService
        List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, notificationDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION.toString(), loginDataBean.getCompanyId(), notificationDataBean.getId());
        //After that make Map of Section and there customfield
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
        map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
        //Pass this map to customFieldSevice.saveOrUpdate method.
        customFieldSevice.saveOrUpdate(notificationDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION, loginDataBean.getCompanyId(), map);

    }

    public List<NotificationConfigurationDataBean> searchNotificationConfiguration(String searchText) {
        List<HkNotificationConfigurationEntity> searchedNotifications = hkNotificationService.retrieveNotificationBySearchText(searchText, loginDataBean.getCompanyId());
        List<NotificationConfigurationDataBean> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(searchedNotifications)) {
            for (HkNotificationConfigurationEntity hkNotificationConfigurationEntity : searchedNotifications) {
                NotificationConfigurationDataBean notificationConfigurationDataBean = new NotificationConfigurationDataBean();

                notificationConfigurationDataBean.setId(hkNotificationConfigurationEntity.getId());
                notificationConfigurationDataBean.setName(hkNotificationConfigurationEntity.getNotificationName());
                if (hkNotificationConfigurationEntity.getWebMessage() != null) {
                    notificationConfigurationDataBean.setInterfaceWType(true);
                    notificationConfigurationDataBean.setInterfaceWTypeText(hkNotificationConfigurationEntity.getWebMessage());
                }
//                if (hkNotificationConfigurationEntity.getSmsMessage() != null) {
//                    notificationConfigurationDataBean.setInterfaceSType(true);
//                    notificationConfigurationDataBean.setInterfaceSTypeText(hkNotificationConfigurationEntity.getSmsMessage());
//                }
                if (hkNotificationConfigurationEntity.getEmailMessage() != null) {
                    notificationConfigurationDataBean.setInterfaceEType(true);
                    notificationConfigurationDataBean.setInterfaceETypeText(hkNotificationConfigurationEntity.getEmailMessage());
                }
                result.add(notificationConfigurationDataBean);
            }
            return result;
        }
        return null;
    }

    public NotificationConfigurationDataBean retrieveNotificationConfigurationById(Long id) {
        HkNotificationConfigurationEntity notification = hkNotificationService.retrieveNotificationCnfigurationById(id);
        NotificationConfigurationDataBean result = this.convertNotificationConfigurationEntityToDataBean(notification, null);
        Map<Long, List<HkNotificationConfigrationRecipientEntity>> mapOfRecipients = hkNotificationService.retrieveActiveNotificationConfigurationRecipients(Arrays.asList(notification.getId()));

        if (mapOfRecipients.get(result.getId()) != null) {
            List<HkNotificationConfigrationRecipientEntity> listOfRec = mapOfRecipients.get(result.getId());
            List<String> arrOfRec = new ArrayList<>();
            for (HkNotificationConfigrationRecipientEntity hkNotificationConfigrationRecipientEntity : listOfRec) {
                String rec = hkNotificationConfigrationRecipientEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceInstance() + ":" + hkNotificationConfigrationRecipientEntity.getHkNotificationConfigrationRecipientEntityPK().getReferenceType();
                arrOfRec.add(rec);
            }
            result.setTaskRecipients(Arrays.copyOf(arrOfRec.toArray(), arrOfRec.toArray().length, String[].class));
        }
        Map<String, String> recipientNames = userManagementServiceWrapper.retrieveRecipientNames(Arrays.asList(result.getTaskRecipients()));
        List<TaskRecipientDataBean> taskRecipientDataBeanList = new ArrayList<>();
        for (String recName : result.getTaskRecipients()) {
            TaskRecipientDataBean taskRecipientDataBean = new TaskRecipientDataBean();
            taskRecipientDataBean = this.convertTaskRecipientEntityToTaskRecipientDataBean(recName, taskRecipientDataBean, recipientNames, SEPARATOR);
            taskRecipientDataBeanList.add(taskRecipientDataBean);
        }
        result.setTaskRecipientDataBeanList(taskRecipientDataBeanList);
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> customFields = customFieldTransformerBean.retrieveDocumentByInstanceId(result.getId(), HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(customFields)) {
            if (customFields != null) {
                List<Map<Long, Map<String, Object>>> maps = customFields.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        result.setTaskCustom(map.get(result.getId()));
                    }
                }
            }
        }
        if (result.getTaskCustom() == null) {
            result.setTaskCustom(new HashMap<String, Object>());
        }
        return result;
    }
}
