/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkGoalTemplateEntity;
import com.argusoft.hkg.model.HkHolidayEntity;
import com.argusoft.hkg.model.HkLeaveEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkMessageEntity;
import com.argusoft.hkg.model.HkMessageRecipientDtlEntity;
import com.argusoft.hkg.model.HkNotificationConfigrationRecipientEntity;
import com.argusoft.hkg.model.HkNotificationConfigurationEntity;
import com.argusoft.hkg.model.HkNotificationRecipientEntity;
import com.argusoft.hkg.model.HkPriceListEntity;
import com.argusoft.hkg.model.HkReferenceRateEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkSystemConfigurationEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPersonCapabilityDocument;
import com.argusoft.hkg.nosql.model.HkPlanAccessPermissionDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkSubEntityExceptionDocument;
import com.argusoft.hkg.nosql.model.HkSubFormFieldDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.nosql.model.HkUserGoalStatusDocument;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.web.center.sync.transformers.HkAssetTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkCaratRangeTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkCategoryTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkCustomFieldTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkFeatureFieldPermissionTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkFeatureSectionTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkFieldTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkGenericDocumentTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkGoalTemplateTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkHolidayTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkLeaveTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkLocaleTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkMasterTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkMessageRecipientDtlTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkMessageTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkNotificationConfigrationRecipientTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkNotificationConfigurationTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkNotificationDocumentTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkPriceListEntityTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkReferenceRateTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkRuleTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkSectionTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkSubFormFieldTransformer;
import com.argusoft.hkg.web.center.sync.transformers.HkSystemConfigurationTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterRoleFeatureTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncCenterUserTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncFileTransferDocumentTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncMasterValueTransformer;
import com.argusoft.hkg.web.center.sync.transformers.SyncVcardTransformer;
import com.argusoft.hkg.web.center.sync.transformers.UmCompanyTransformer;
import com.argusoft.hkg.web.center.sync.transformers.UmDepartmentTransformer;
import com.argusoft.hkg.web.center.sync.transformers.UmDesignationTransformer;
import com.argusoft.hkg.web.center.sync.transformers.UmFeatureTransformer;
import com.argusoft.hkg.web.sync.xmpp.transformers.HkNotificationTransformer;
import com.argusoft.hkg.web.sync.xmpp.transformers.SyncMenuTransformer;
import com.argusoft.hkg.web.sync.xmpp.transformers.UmUserTransformer;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.sync.center.model.CenterCustomFieldDocument;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.HkFeatureSectionDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkGoalTemplateDocument;
import com.argusoft.sync.center.model.HkHolidayDocument;
import com.argusoft.sync.center.model.HkLeaveDocument;
import com.argusoft.sync.center.model.HkLocaleDocument;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkMessageDocument;
import com.argusoft.sync.center.model.HkMessageRecipientDtlDocument;
import com.argusoft.sync.center.model.HkNotificationConfigrationRecipientDocument;
import com.argusoft.sync.center.model.HkNotificationConfigurationDocument;
import com.argusoft.sync.center.model.HkNotificationRecipientDocument;
import com.argusoft.sync.center.model.HkPriceListDocument;
import com.argusoft.sync.center.model.HkReferenceRateDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import com.argusoft.sync.center.model.SyncCenterFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterRoleFeatureDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncFileTransferDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import com.argusoft.sync.center.model.VcardDocument;
import com.argusoft.usermanagement.common.model.UMCompany;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author shruti
 */
@Service
public class SyncEntityDocumentMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncEntityDocumentMapper.class);
    @Autowired
    private WebApplicationContext applicationContext;

    private final Map<Class<?>, List<Class<? extends SyncTransformerInterface>>> entityTransformerMap = new HashMap<>();
    private final Map<Class<?>, Class<? extends SyncTransformerInterface>> documentTransformerMap = new HashMap<>();

    @PostConstruct
    public void init() {

        LOGGER.debug("EntityDocumentMapper Created");

//        entityTransformerMap.put(ActActivityFlowVersionDocument.class, ActActivityFlowVersionTransformer.class);
//        entityTransformerMap.put(ActActivityFlowNodeEntity.class, ActActivityFlowNodeTransformer.class);
        List<Class<? extends SyncTransformerInterface>> listTransformers = new LinkedList<>();
        listTransformers.add(UmDesignationTransformer.class);
        listTransformers.add(SyncMenuTransformer.class);
        entityTransformerMap.put(UMRole.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(UmFeatureTransformer.class);
        listTransformers.add(SyncMenuTransformer.class);
//        entityTransformerMap.put(UMCompany.class, SyncMenuTransformer.class);
        entityTransformerMap.put(UMFeature.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(UmUserTransformer.class);
        entityTransformerMap.put(UMUser.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(UmCompanyTransformer.class);
        entityTransformerMap.put(UMCompany.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(UmDepartmentTransformer.class);
        entityTransformerMap.put(UMDepartment.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkSystemConfigurationTransformer.class);
        entityTransformerMap.put(HkSystemConfigurationEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkCaratRangeTransformer.class);
        entityTransformerMap.put(HkCaratRangeEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(SyncMasterValueTransformer.class);
        entityTransformerMap.put(HkValueEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkNotificationTransformer.class);
        entityTransformerMap.put(HkNotificationRecipientEntity.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(HkLeaveTransformer.class);
        entityTransformerMap.put(HkLeaveEntity.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(HkPriceListEntityTransformer.class);
        entityTransformerMap.put(HkPriceListEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkLocaleTransformer.class);
        entityTransformerMap.put(I18nLabelEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
//        listTransformers.add(HkCustomFieldTransformer.class);
        listTransformers.add(HkFieldTransformer.class);
        entityTransformerMap.put(HkFieldEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkCustomFieldTransformer.class);
        entityTransformerMap.put(CenterCustomFieldDocument.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkNotificationTransformer.class);
        entityTransformerMap.put(HkNotificationRecipientEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkRuleTransformer.class);
        entityTransformerMap.put(HkRuleSetDocument.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkNotificationDocumentTransformer.class);
        entityTransformerMap.put(HkNotificationRecipientDocument.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(UmFeatureTransformer.class);
        entityTransformerMap.put(SyncCenterFeatureDocument.class, listTransformers);
//        listTransformers = new LinkedList<>();
//        listTransformers.add(HkFeatureFieldPermissionTransformer.class);

        listTransformers = new LinkedList<>();
        listTransformers.add(HkFeatureSectionTransformer.class);
        entityTransformerMap.put(HkFeatureSectionEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkSectionTransformer.class);
        entityTransformerMap.put(HkSectionEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkNotificationConfigurationTransformer.class);
        entityTransformerMap.put(HkNotificationConfigurationEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkNotificationConfigrationRecipientTransformer.class);
        entityTransformerMap.put(HkNotificationConfigrationRecipientEntity.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(HkSubFormFieldTransformer.class);
        entityTransformerMap.put(HkSubFormFieldEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkMessageRecipientDtlTransformer.class);
        entityTransformerMap.put(HkMessageRecipientDtlEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkMessageTransformer.class);
        entityTransformerMap.put(HkMessageEntity.class, listTransformers);
//        listTransformers = new LinkedList<>();
//        listTransformers.add(SyncFileTransferDocumentTransformer.class);
//        entityTransformerMap.put(SyncFileTransferDocument.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkGenericDocumentTransformer.class);
        entityTransformerMap.put(HkPersonCapabilityDocument.class, listTransformers);
        entityTransformerMap.put(HkSellDocument.class, listTransformers);
        entityTransformerMap.put(HkTransferDocument.class, listTransformers);
        entityTransformerMap.put(HkUserGoalStatusDocument.class, listTransformers);
        entityTransformerMap.put(HkPlanAccessPermissionDocument.class, listTransformers);
        entityTransformerMap.put(HkSubFormValueDocument.class, listTransformers);
        entityTransformerMap.put(HkValueExceptionDocument.class, listTransformers);
        entityTransformerMap.put(HkSubEntityExceptionDocument.class, listTransformers);
        entityTransformerMap.put(HkIssueDocument.class, listTransformers);
        entityTransformerMap.put(HkInvoiceDocument.class, listTransformers);
        entityTransformerMap.put(HkPlanDocument.class, listTransformers);
        entityTransformerMap.put(HkPacketDocument.class, listTransformers);
        entityTransformerMap.put(HkParcelDocument.class, listTransformers);
        entityTransformerMap.put(HkLotDocument.class, listTransformers);
        entityTransformerMap.put(HkDepartmentConfigDocument.class, listTransformers);
        entityTransformerMap.put(HkPurchaseDocument.class, listTransformers);
        entityTransformerMap.put(HkFieldDocument.class, listTransformers);
        entityTransformerMap.put(HkSectionDocument.class, listTransformers);
        entityTransformerMap.put(HkCriteriaSetDocument.class, listTransformers);
        entityTransformerMap.put(HkRoleFeatureModifierDocument.class, listTransformers);
        entityTransformerMap.put(HkSubLotDocument.class, listTransformers);
        entityTransformerMap.put(HkCalcBasPriceDocument.class, listTransformers);
        entityTransformerMap.put(HkCalcFourCDiscountDocument.class, listTransformers);
        entityTransformerMap.put(HkCalcRateDetailDocument.class, listTransformers);
        entityTransformerMap.put(HkCalcMasterDocument.class, listTransformers);

        listTransformers = new LinkedList<>();
        listTransformers.add(HkCategoryTransformer.class);
        entityTransformerMap.put(HkCategoryEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkAssetTransformer.class);
        entityTransformerMap.put(HkAssetEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkMasterTransformer.class);
        entityTransformerMap.put(HkMasterEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkGoalTemplateTransformer.class);
        entityTransformerMap.put(HkGoalTemplateEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(SyncVcardTransformer.class);
        entityTransformerMap.put(VcardDocument.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkHolidayTransformer.class);
        entityTransformerMap.put(HkHolidayEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkReferenceRateTransformer.class);
        entityTransformerMap.put(HkReferenceRateEntity.class, listTransformers);
        listTransformers = new LinkedList<>();
        listTransformers.add(HkFeatureFieldPermissionTransformer.class);
        entityTransformerMap.put(HkFeatureFieldPermissionDocument.class, listTransformers);
//        listTransformers = new LinkedList<>();
//        listTransformers.add(UmUserIpAssociationTransformer.class);
//        entityTransformerMap.put(UMUserIpAssociation.class, listTransformers);

        documentTransformerMap.put(SyncCenterRoleFeatureDocument.class, SyncCenterRoleFeatureTransformer.class);
        documentTransformerMap.put(SyncCenterUserDocument.class, SyncCenterUserTransformer.class);
        documentTransformerMap.put(HkDepartmentDocument.class, UmDepartmentTransformer.class);
        documentTransformerMap.put(HkCaratRangeDocument.class, HkCaratRangeTransformer.class);
        documentTransformerMap.put(UmCompanyDocument.class, UmCompanyTransformer.class);
        documentTransformerMap.put(HkMasterValueDocument.class, SyncMasterValueTransformer.class);
        documentTransformerMap.put(HkLeaveDocument.class, HkLeaveTransformer.class);
        documentTransformerMap.put(UmDesignationDocument.class, UmDesignationTransformer.class);

        documentTransformerMap.put(HkIssueDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkInvoiceDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkPlanDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkPacketDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkLotDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkParcelDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkValueExceptionDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkSubEntityExceptionDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkPriceListDocument.class, HkPriceListEntityTransformer.class);
//        documentTransformerMap.put(HkPriceListDetailDocument.class, HkPriceListDetailEntityTransformer.class);
        documentTransformerMap.put(HkLocaleDocument.class, HkLocaleTransformer.class);
        documentTransformerMap.put(CenterCustomFieldDocument.class, HkCustomFieldTransformer.class);
        documentTransformerMap.put(HkSubFormValueDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkFieldDocument.class, HkFieldTransformer.class);
        documentTransformerMap.put(HkSystemConfigurationDocument.class, HkSystemConfigurationTransformer.class);
        documentTransformerMap.put(HkNotificationRecipientDocument.class, HkNotificationDocumentTransformer.class);
        documentTransformerMap.put(HkRuleSetDocument.class, HkRuleTransformer.class);
        documentTransformerMap.put(SyncCenterFeatureDocument.class, UmFeatureTransformer.class);
        documentTransformerMap.put(HkNotificationRecipientEntity.class, HkNotificationTransformer.class);
        documentTransformerMap.put(HkFeatureFieldPermissionDocument.class, HkFeatureFieldPermissionTransformer.class);
        documentTransformerMap.put(HkFeatureSectionDocument.class, HkFeatureSectionTransformer.class);
        documentTransformerMap.put(HkSectionDocument.class, HkSectionTransformer.class);
        documentTransformerMap.put(HkNotificationConfigurationDocument.class, HkNotificationConfigurationTransformer.class);
        documentTransformerMap.put(HkNotificationConfigrationRecipientDocument.class, HkNotificationConfigrationRecipientTransformer.class);
        documentTransformerMap.put(HkSubFormFieldDocument.class, HkSubFormFieldTransformer.class);
        documentTransformerMap.put(SyncFileTransferDocument.class, SyncFileTransferDocumentTransformer.class);
        documentTransformerMap.put(HkMessageRecipientDtlDocument.class, HkMessageRecipientDtlTransformer.class);
        documentTransformerMap.put(HkMessageDocument.class, HkMessageTransformer.class);
        documentTransformerMap.put(HkPersonCapabilityDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkSellDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkTransferDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkUserGoalStatusDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkDepartmentConfigDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkPurchaseDocument.class, HkGenericDocumentTransformer.class);

        documentTransformerMap.put(HkCategoryDocument.class, HkCategoryTransformer.class);
        documentTransformerMap.put(HkAssetDocument.class, HkAssetTransformer.class);
        documentTransformerMap.put(HkMasterDocument.class, HkMasterTransformer.class);
        documentTransformerMap.put(HkPlanAccessPermissionDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkGoalTemplateDocument.class, HkGoalTemplateTransformer.class);
        documentTransformerMap.put(VcardDocument.class, SyncVcardTransformer.class);
        documentTransformerMap.put(HkHolidayDocument.class, HkHolidayTransformer.class);
        documentTransformerMap.put(HkCriteriaSetDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkRoleFeatureModifierDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkSubLotDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkReferenceRateDocument.class, HkReferenceRateTransformer.class);
        documentTransformerMap.put(HkCalcBasPriceDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkCalcFourCDiscountDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkCalcRateDetailDocument.class, HkGenericDocumentTransformer.class);
        documentTransformerMap.put(HkCalcMasterDocument.class, HkGenericDocumentTransformer.class);
    }

    public Map<Class<?>, List<Class<? extends SyncTransformerInterface>>> getEntityTransformerMap() {
        return entityTransformerMap;
    }

    public List<Class<? extends SyncTransformerInterface>> getTransformerClass(Class<?> entityClass) {
        return entityTransformerMap.get(entityClass);
    }

    public List<SyncTransformerInterface> getTransformerInstance(Class<?> entityClass) throws InstantiationException, IllegalAccessException {
        List<Class<? extends SyncTransformerInterface>> transformerClass = entityTransformerMap.get(entityClass);
//        LOGGER.debug("application context: " + applicationContext);
//        LOGGER.debug("(transformerClass != null) ? applicationContext.getBean(transformerClass) : null " + ((transformerClass != null) ? applicationContext.getBean(transformerClass) : null));
        LOGGER.debug(entityClass.getSimpleName() + " entityTransformerMap , transformer: " + transformerClass);
        if (transformerClass != null) {
            List<SyncTransformerInterface> listSyncTransIntrface = new ArrayList<>();
            for (Class<? extends SyncTransformerInterface> transformerClas : transformerClass) {
                listSyncTransIntrface.add(applicationContext.getBean(transformerClas));
            }
            return listSyncTransIntrface;
        }
        return null;
    }

    public boolean containsEntityMapping(Class<?> entityclass) {
        return entityTransformerMap.containsKey(entityclass);
    }

    public void addEntityMapping(Class<?> entityClass, Class<? extends SyncTransformerInterface> transformerClass) {
        List<Class<? extends SyncTransformerInterface>> transformerClassList = entityTransformerMap.get(entityClass);
        if (transformerClassList == null) {
            transformerClassList = new ArrayList<>();
        }
        transformerClassList.add(transformerClass);
        entityTransformerMap.put(entityClass, transformerClassList);
    }

    public void addAllEntityMapping(Map<Class<?>, List<Class<? extends SyncTransformerInterface>>> transformerMap) {
        entityTransformerMap.putAll(transformerMap);
    }

    public Map<Class<?>, Class<? extends SyncTransformerInterface>> getDocumentTransformerMap() {
        return documentTransformerMap;
    }

    public Class<? extends SyncTransformerInterface> getDocumentTransformerClass(Class<?> entityClass) {
        return documentTransformerMap.get(entityClass);
    }

    public SyncTransformerInterface getDocumentTransformerInstance(Class<?> entityClass) throws InstantiationException, IllegalAccessException {
        Class<? extends SyncTransformerInterface> transformerClass = documentTransformerMap.get(entityClass);
//        LOGGER.debug(entityClass.getSimpleName() + " , transformer: " + transformerClass);
//        LOGGER.debug("application context: " + applicationContext);
//        LOGGER.debug("(transformerClass != null) ? applicationContext.getBean(transformerClass) : null " + ((transformerClass != null) ? applicationContext.getBean(transformerClass) : null));
        return (transformerClass != null) ? applicationContext.getBean(transformerClass) : null;
    }

    public boolean containsDocumentMapping(Class<?> entityclass) {
        return documentTransformerMap.containsKey(entityclass);
    }

    public void addDocumentMapping(Class<?> entityClass, Class<? extends SyncTransformerInterface> transformerClass) {
        documentTransformerMap.put(entityClass, transformerClass);
    }

    public void addAllDocumentMapping(Map<Class<?>, Class<? extends SyncTransformerInterface>> transformerMap) {
        documentTransformerMap.putAll(transformerMap);
    }

}
