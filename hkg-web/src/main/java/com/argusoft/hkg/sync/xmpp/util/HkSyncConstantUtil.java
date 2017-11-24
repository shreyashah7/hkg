/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntityPK;
import com.argusoft.hkg.model.HkPriceListDetailEntity;
import com.argusoft.hkg.model.HkPriceListDetailEntityPK;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntityPK;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.sync.center.model.HkNotificationDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocumentPK;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shruti
 */
public class HkSyncConstantUtil {

    public static final Map<Class<?>, Map<String, String>> ENTITY_FIELD_MAP = new HashMap<>();
    public static final Map<Class<?>, String> DOCUMENT_COLLECTION_MAP = new HashMap<>();

    static {
        Map<String, String> departmentFieldMap = new HashMap<>();
        departmentFieldMap.put("id", "id");
        departmentFieldMap.put("deptName", "deptName");
        departmentFieldMap.put("lastModifiedOn", "lastModifiedOn");
        departmentFieldMap.put("isActive", "isActive");
        departmentFieldMap.put("parentId", "parentId");
        ENTITY_FIELD_MAP.put(UMDepartment.class, departmentFieldMap);

        Map<String, String> notificationRecipientMap = new HashMap<>();
        notificationRecipientMap.put("isSeen", "isSeen");
        notificationRecipientMap.put("seenOn", "seenOn");
        notificationRecipientMap.put("status", "status");
        notificationRecipientMap.put("isArchive", "isArchive");
        notificationRecipientMap.put("lastModifiedOn", "lastModifiedOn");
//        notificationRecipientMap.put("hkNotificationEntity", "hkNotificationEntity");
        ENTITY_FIELD_MAP.put(HkNotificationRecipientEntity.class, notificationRecipientMap);
        ENTITY_FIELD_MAP.put(HkNotificationRecipientDocument.class, notificationRecipientMap);

        Map<String, String> notificationRecipientPkMap = new HashMap<>();
        notificationRecipientPkMap.put("notification", "notification");
        notificationRecipientPkMap.put("forUser", "forUser");
        ENTITY_FIELD_MAP.put(HkNotificationRecipientEntityPK.class, notificationRecipientPkMap);
        ENTITY_FIELD_MAP.put(HkNotificationRecipientDocumentPK.class, notificationRecipientPkMap);

        Map<String, String> notificationMap = new HashMap<>();
        notificationMap.put("id", "id");
        notificationMap.put("notificationType", "notificationType");
        notificationMap.put("onDate", "onDate");
        notificationMap.put("description", "description");
        notificationMap.put("instanceType", "instanceType");
        notificationMap.put("instanceId", "instanceId");
        notificationMap.put("isArchive", "isArchive");
        notificationMap.put("notificationConfigurationType", "notificationConfigurationType");
        notificationMap.put("notificationConfiguration", "notificationConfiguration");
        notificationMap.put("mailStatus", "mailStatus");
        ENTITY_FIELD_MAP.put(HkNotificationEntity.class, notificationMap);
        ENTITY_FIELD_MAP.put(HkNotificationDocument.class, notificationMap);

        Map<String, String> masterValueFieldMap = new HashMap<>();
        masterValueFieldMap.put("id", "id");
        masterValueFieldMap.put("shortcutCode", "shortcutCode");
        masterValueFieldMap.put("valueName", "valueName");
        masterValueFieldMap.put("translatedValueName", "translatedValueName");
        masterValueFieldMap.put("isActive", "isActive");
        masterValueFieldMap.put("isArchive", "isArchive");
        masterValueFieldMap.put("lastModifiedOn", "lastModifiedOn");
        masterValueFieldMap.put("isOftenUsed", "isOftenUsed");
        ENTITY_FIELD_MAP.put(HkValueEntity.class, masterValueFieldMap);

        //---HkSystemConfigurationEntity
        Map<String, String> configFieldMap = new HashMap<>();
        configFieldMap.put("isArchive", "isArchive");
        configFieldMap.put("keyValue", "keyValue");
        configFieldMap.put("modifiedBy", "modifiedBy");
        configFieldMap.put("modifiedOn", "modifiedOn");
        ENTITY_FIELD_MAP.put(HkSystemConfigurationEntity.class, configFieldMap);
        configFieldMap = new HashMap<>();
        configFieldMap.put("systemKey", "id");
        ENTITY_FIELD_MAP.put(HkSystemConfigurationEntityPK.class, configFieldMap);

        Map<String, String> roleFieldMap = new HashMap<>();
        roleFieldMap.put("id", "id");
        roleFieldMap.put("precedence", "precedence");
        roleFieldMap.put("isArchive", "isArchive");
        roleFieldMap.put("modifiedOn", "modifiedOn");
        roleFieldMap.put("name", "name");
        roleFieldMap.put("isActive", "isActive");
        roleFieldMap.put("custom1", "department");
        ENTITY_FIELD_MAP.put(UMRole.class, roleFieldMap);

        // HkLeaveEntity
        Map<String, String> leaveFieldMap = new HashMap<>();
        leaveFieldMap.put("id", "id");
        leaveFieldMap.put("leaveReason", "leaveReason");
        leaveFieldMap.put("description", "description");
        leaveFieldMap.put("forUser", "forUser");
        leaveFieldMap.put("frmDt", "frmDt");
        leaveFieldMap.put("toDt", "toDt");
        leaveFieldMap.put("totalDays", "totalDays");
        leaveFieldMap.put("status", "status");
        leaveFieldMap.put("finalRemarks", "finalRemarks");
        leaveFieldMap.put("isArchive", "isArchive");
        leaveFieldMap.put("createdBy", "createdBy");
        leaveFieldMap.put("createdOn", "createdOn");
        leaveFieldMap.put("franchise", "franchise");

        ENTITY_FIELD_MAP.put(HkLeaveEntity.class, leaveFieldMap);

        Map<String, String> fieldEntityMap = new HashMap<>();
        fieldEntityMap.put("id", "id");
        fieldEntityMap.put("fieldLabel", "fieldLabel");
        fieldEntityMap.put("fieldType", "fieldType");
        fieldEntityMap.put("componentType", "componentType");
        fieldEntityMap.put("uiFieldName", "uiFieldName");
        fieldEntityMap.put("dbFieldName", "dbFieldName");
        fieldEntityMap.put("isCustomField", "isCustomField");
        fieldEntityMap.put("isEditable", "isEditable");
        fieldEntityMap.put("isDependant", "isDependant");
        fieldEntityMap.put("dbBaseName", "dbBaseName");
        fieldEntityMap.put("dbBaseType", "dbBaseType");
        fieldEntityMap.put("feature", "feature");
        fieldEntityMap.put("associatedCurrency", "associatedCurrency");
        fieldEntityMap.put("status", "status");
        fieldEntityMap.put("isArchive", "isArchive");
        fieldEntityMap.put("franchise", "franchise");
        fieldEntityMap.put("seqNo", "seqNo");
        fieldEntityMap.put("createdBy", "createdBy");
        fieldEntityMap.put("createdOn", "createdOn");
        fieldEntityMap.put("lastModifiedBy", "lastModifiedBy");
        fieldEntityMap.put("lastModifiedOn", "lastModifiedOn");
//        fieldEntityMap.put("section", "section");
        fieldEntityMap.put("validationPattern", "validationPattern");
        fieldEntityMap.put("formulaValue", "formulaValue");
        fieldEntityMap.put("fieldValues", "fieldValues");
        ENTITY_FIELD_MAP.put(HkFieldEntity.class, fieldEntityMap);

        Map<String, String> priceDetailFieldMap = new HashMap<>();
        priceDetailFieldMap.put("color", "color");
        priceDetailFieldMap.put("clarity", "clarity");
        priceDetailFieldMap.put("cut", "cut");
        priceDetailFieldMap.put("caratRange", "caratRange");
        priceDetailFieldMap.put("fluorescence", "fluorescence");
        priceDetailFieldMap.put("originalPrice", "originalPrice");
        priceDetailFieldMap.put("discount", "discount");
        priceDetailFieldMap.put("hkgPrice", "hkgPrice");
        priceDetailFieldMap.put("isArchive", "isArchive");
        ENTITY_FIELD_MAP.put(HkPriceListDetailEntity.class, priceDetailFieldMap);

        Map<String, String> priceListDetailEntityPKMap = new HashMap<>();
        priceListDetailEntityPKMap.put("priceList", "priceList");
        priceListDetailEntityPKMap.put("sequenceNumber", "sequenceNumber");
        ENTITY_FIELD_MAP.put(HkPriceListDetailEntityPK.class, priceListDetailEntityPKMap);

        Map<String, String> featureSection = new HashMap<>();
        featureSection.put("id", "id");
        featureSection.put("feature", "feature");
        featureSection.put("isViewOnly", "isViewOnly");
        featureSection.put("isArchive", "isArchive");
        featureSection.put("createdOn", "createdOn");
        featureSection.put("sectionId", "sectionId");
        ENTITY_FIELD_MAP.put(HkFeatureSectionEntity.class, featureSection);

        Map<String, String> notificConfigMap = new HashMap<>();
        notificConfigMap.put("id", "id");
        notificConfigMap.put("notificationName", "notificationName");
        notificConfigMap.put("description", "description");
        notificConfigMap.put("notificationType", "notificationType");
        notificConfigMap.put("associatedRule", "associatedRule");
        notificConfigMap.put("atTime", "atTime");
        notificConfigMap.put("actualEndDate", "actualEndDate");
        notificConfigMap.put("afterUnits", "afterUnits");
        notificConfigMap.put("endDate", "endDate");
        notificConfigMap.put("endRepeatMode", "endRepeatMode");
        notificConfigMap.put("monthlyOnDay", "monthlyOnDay");
        notificConfigMap.put("repeatativeMode", "repeatativeMode");
        notificConfigMap.put("repetitionCnt", "repetitionCnt");
        notificConfigMap.put("weeklyOnDays", "weeklyOnDays");
        notificConfigMap.put("webMessage", "webMessage");
        notificConfigMap.put("smsMessage", "smsMessage");
        notificConfigMap.put("emailMessage", "emailMessage");
        notificConfigMap.put("status", "status");
        notificConfigMap.put("createdOn", "createdOn");
        notificConfigMap.put("lastModifiedBy", "lastModifiedBy");
        notificConfigMap.put("lastModifiedOn", "lastModifiedOn");
        notificConfigMap.put("activityGroup", "activityGroup");
        notificConfigMap.put("activityNode", "activityNode");
        ENTITY_FIELD_MAP.put(HkNotificationConfigurationEntity.class, notificConfigMap);

        Map<String, String> subFormFieldMap = new HashMap<>();
        subFormFieldMap.put("id", "id");
        subFormFieldMap.put("componentType", "componentType");
        subFormFieldMap.put("subFieldName", "subFieldName");
        subFormFieldMap.put("subFieldLabel", "subFieldLabel");
        subFormFieldMap.put("subFieldType", "subFieldType");
        subFormFieldMap.put("franchise", "franchise");
        subFormFieldMap.put("isArchive", "isArchive");
        subFormFieldMap.put("lastModifiedBy", "lastModifiedBy");
        subFormFieldMap.put("lastModifiedOn", "lastModifiedOn");
        subFormFieldMap.put("status", "status");
        subFormFieldMap.put("validationPattern", "validationPattern");
        subFormFieldMap.put("isDroplistField", "isDroplistField");
        subFormFieldMap.put("sequenceNo", "sequenceNo");
        ENTITY_FIELD_MAP.put(HkSubFormFieldEntity.class, subFormFieldMap);
    }

}
