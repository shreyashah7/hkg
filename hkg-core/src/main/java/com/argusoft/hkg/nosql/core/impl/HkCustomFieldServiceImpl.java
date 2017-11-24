/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.FeatureNameForCustomField;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.SectionNameForCustomField;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import static com.argusoft.hkg.nosql.core.impl.HkUserServiceImpl.FIRST_NAME;
import static com.argusoft.hkg.nosql.core.impl.HkUserServiceImpl.ID;
import static com.argusoft.hkg.nosql.core.impl.HkUserServiceImpl.LAST_NAME;
import static com.argusoft.hkg.nosql.core.impl.HkUserServiceImpl.USER_CODE;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkApplyLeaveDocument;
import com.argusoft.hkg.nosql.model.HkAssetDocument;
import com.argusoft.hkg.nosql.model.HkCategoryDocument;
import com.argusoft.hkg.nosql.model.HkEventDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkFranchiseDocument;
import com.argusoft.hkg.nosql.model.HkGoalDocument;
import com.argusoft.hkg.nosql.model.HkHolidayDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkIssueAssetDocument;
import com.argusoft.hkg.nosql.model.HkIssueDocument;
import com.argusoft.hkg.nosql.model.HkLeaveWorkflowDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkMessageDocument;
import com.argusoft.hkg.nosql.model.HkNotificationDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.nosql.model.HkPurchaseDocument;
import com.argusoft.hkg.nosql.model.HkRespondLeaveDocument;
import com.argusoft.hkg.nosql.model.HkSellDocument;
import com.argusoft.hkg.nosql.model.HkShiftDocument;
import com.argusoft.hkg.nosql.model.HkSubEntityExceptionDocument;
import com.argusoft.hkg.nosql.model.HkSubFormFieldDocument;
import com.argusoft.hkg.nosql.model.HkSubFormValueDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.hkg.nosql.model.HkTaskDocument;
import com.argusoft.hkg.nosql.model.HkTransferDocument;
import com.argusoft.hkg.nosql.model.HkUserDocument;
import com.argusoft.hkg.nosql.model.SectionDocument;
import com.argusoft.sync.center.model.CenterCompanyFeatureSectionDocument;
import com.argusoft.sync.center.model.HkDepartmentDocument;
import com.argusoft.sync.center.model.HkFeatureSectionDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkReferenceRateDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import com.argusoft.sync.center.model.SyncCenterFranchiseDocument;
import com.argusoft.sync.center.model.SyncCenterUserDocument;
import com.argusoft.sync.center.model.SyncFileTransferDocument;
import com.argusoft.sync.center.model.UmCompanyDocument;
import com.argusoft.sync.center.model.UmDesignationDocument;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author dhwani
 */
@Service
public class HkCustomFieldServiceImpl implements HkCustomFieldService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Autowired
    HkFoundationDocumentService foundationDocumentService;

    @Autowired
    HkFeatureServiceImpl hkFeatureService;

    public static final String IS_ARCHIVE = "isArchive";
    public static final String IS_ACTIVE = "isActive";
    private static final String FRANCHISE = "franchise";
    private static final String FIELD_VALUE = "fieldValue";

    private static class InvoiceDocument {

        private static final String OBJECT_ID = "_id";
    }

    private static class ParcelDocument {

        private static final String OBJECT_ID = "_id";
        private static final String INVOICE_ID = "invoice";
    }

    private static class LotDocument {

        private static final String OBJECT_ID = "_id";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
    }

    private static class PacketDocument {

        private static final String OBJECT_ID = "_id";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
        private static final String LOT_ID = "lot";
    }

    private static class SubLotDocument {

        private static final String OBJECT_ID = "_id";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
    }

    private static final class HkCurrencyReferenceRateFields {

        public static final String IS_ACTIVE = "isActive";
        public static final String UPDATED_ON = "lastModifiedOn";
        public static final String COMPANY = "franchise";
        public static final String CURRENCY = "code";
    }

    public Map<String, String> retrieveRoundOffValueForField(Long franchise, Long featureId, String componentType) {
        Map<String, String> roundOffWithDbField = null;
        List<HkFieldDocument> fieldEntities = null;
        List<Criteria> criterias = new ArrayList();
        criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        criterias.add(Criteria.where(HkFieldEntityField.COMPONENT_TYPE).is(componentType));
        criterias.add(Criteria.where(FRANCHISE).is(franchise));
        criterias.add(Criteria.where(HkFieldEntityField.FEATURE).is(featureId));
        fieldEntities = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(fieldEntities)) {
            roundOffWithDbField = new HashMap();
            for (HkFieldDocument fieldEntity : fieldEntities) {

                String[] validationsArr = fieldEntity.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                String roundVal = null;
                for (String validationValue : validationsArr) {
                    if (validationValue.contains("\"roundOff\":")) {
                        String[] roundOffArray = validationValue.split(":");
                        roundVal = roundOffArray[1];
                        roundOffWithDbField.put(fieldEntity.getDbFieldName(), roundVal);
                    }
                }
            }
        }
        return roundOffWithDbField;
    }

    @Override
    public Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFeatureFieldPermissionForSearch(Long featureId, List<Long> designationIds) {
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> sectionFieldMap = new HashMap<>();
        List<Criteria> featureFieldPermCriteria = new ArrayList<>();
        Set<Long> sectionIds = new HashSet<>();
        if (featureId != null) {
            featureFieldPermCriteria.add(Criteria.where(HkFeatureFieldPermissionField.FEATURE).is(featureId));
        }
        if (!CollectionUtils.isEmpty(designationIds)) {
            featureFieldPermCriteria.add(Criteria.where(HkFeatureFieldPermissionField.DESIGNATION).in(designationIds));
        }
        featureFieldPermCriteria.add(Criteria.where(HkFeatureFieldPermissionField.SEARCH_FLAG).is(true));
        featureFieldPermCriteria.add(Criteria.where(HkFeatureFieldPermissionField.IS_ARCHIVE).is(false));
        List<HkFeatureFieldPermissionDocument> fieldList = mongoGenericDao.findByCriteria(featureFieldPermCriteria, HkFeatureFieldPermissionDocument.class
        );
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (HkFeatureFieldPermissionDocument field : fieldList) {
                List<HkFeatureFieldPermissionDocument> sectionFieldsSet = sectionFieldMap.get(field.getHkFieldEntity().getSection());
                if (sectionFieldsSet == null) {
                    sectionFieldsSet = new ArrayList<>();
                    sectionFieldMap.put(field.getHkFieldEntity().getSection(), sectionFieldsSet);
                }
                sectionFieldsSet.add(field);
                if (field.getHkFieldEntity().getSection() != null) {
                    sectionIds.add(field.getHkFieldEntity().getSection().getId());
                }
            }

            List<Criteria> featureSectionCriteria = new ArrayList<>();
            featureSectionCriteria.add(Criteria.where(HkFeatureSectionField.IS_ARCHIVE).is(false));
            featureSectionCriteria.add(Criteria.where(HkFeatureSectionField.FEATURE).is(featureId));
            if (!CollectionUtils.isEmpty(sectionIds)) {
                featureSectionCriteria.add(Criteria.where(HkFeatureSectionField.SECTION_ID).nin(sectionIds));
            }
            List<HkFeatureSectionDocument> featureSectionList = mongoGenericDao.findByCriteria(featureSectionCriteria, HkFeatureSectionDocument.class);
            if (!CollectionUtils.isEmpty(featureSectionList)) {
                for (HkFeatureSectionDocument featureSection : featureSectionList) {
                    if (featureSection.getSection() != null) {
                        sectionFieldMap.put(featureSection.getSection(), null);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(sectionFieldMap.keySet())) {
                for (HkSectionDocument section : sectionFieldMap.keySet()) {
                    if (!CollectionUtils.isEmpty(sectionFieldMap.get(section))) {
                        List<HkFeatureFieldPermissionDocument> get = sectionFieldMap.get(section);
                        List<HkFieldDocument> entitys = new ArrayList<>();
                        for (HkFeatureFieldPermissionDocument hkFieldEntity : get) {
                            HkFieldDocument hkFieldEntity1 = hkFieldEntity.getHkFieldEntity();
                            entitys.add(hkFieldEntity1);
                        }
                        fillValuePattern(entitys);
                    }
                }
            }
        }
        return sectionFieldMap;
    }

    @Override
    public List<HkFeatureFieldPermissionDocument> retrieveFeatureFieldPermissions(Long featureId, List<Long> designationIds) {
        List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments;
        List<Criteria> criterias = new ArrayList<>();

        if (featureId != null) {
            criterias.add(Criteria.where(HkFeatureFieldPermissionField.FEATURE).is(featureId));
        }
        if (!CollectionUtils.isEmpty(designationIds)) {
            criterias.add(Criteria.where(HkFeatureFieldPermissionField.DESIGNATION).in(designationIds));
        }
        criterias.add(Criteria.where(HkFeatureFieldPermissionField.IS_ARCHIVE).is(false));
        criterias.add(new Criteria()
                .orOperator(Criteria.where(HkFeatureFieldPermissionField.PARENT_VIEW_FLAG).is(Boolean.TRUE),
                        Criteria.where(HkFeatureFieldPermissionField.EDITABLE_FLAG).is(Boolean.TRUE),
                        Criteria.where(HkFeatureFieldPermissionField.SEARCH_FLAG).is(Boolean.TRUE),
                        Criteria.where(HkFeatureFieldPermissionField.READ_ONLY_FLAG).is(Boolean.TRUE)));
        featureFieldPermissionDocuments = mongoGenericDao.findByCriteria(criterias, HkFeatureFieldPermissionDocument.class);
        return featureFieldPermissionDocuments;
    }

    //------------------------------- Field Document service fields start-------------------------------------//
    private static class HkFeatureFieldPermissionField {

        public static final String FEATURE = "feature";
        public static final String SEARCH_FLAG = "searchFlag";
        public static final String DESIGNATION = "designation";
        public static final String PARENT_VIEW_FLAG = "parentViewFlag";
        public static final String EDITABLE_FLAG = "editableFlag";
        public static final String READ_ONLY_FLAG = "readonlyFlag";
        public static final String FIELD = "hkFeatureFieldPermissionEntityPK.field";
        public static final String IS_ARCHIVE = "isArchive";
    }

    private static class HkFeatureSectionField {

        public static final String FEATURE = "feature";
        public static final String SECTION = "section";
        public static final String SECTION_ID = "sectionId";
        public static final String IS_ARCHIVE = "isArchive";
    }

    private static class HkFieldEntityField {

        public static final String FEATURE = "feature";
        public static final String STATUS = "status";
        public static final String IS_CUSTOM_FIELD = "isCustomField";
        public static final String SECTION = "section";
        public static final String SECTION_ID = "section.id";
        public static final String ID = "id";
        public static final String FIELD_LABEL = "fieldLabel";
        public static final String DB_FIELD_NAME = "dbBaseName";
        public static final String FIELD_NAME = "dbFieldName";
        public static final String FRANCHISE = "franchise";
        public static final String SEQ_NO = "seqNo";
        public static final String NEW_DB_FIELD_NAME = "dbFieldName";
        public static final String FIELD_TYPE = "fieldType";
        public static final String ACTIVE_STATUS = "A";
        public static final String INACTIVE_STATUS = "I";
        public static final String IS_DEPENDANT = "isDependant";
        public static final String IS_EDITABLE = "isEditable";
        public static final String COMPONENT_TYPE = "componentType";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String FORMULA_VALUE = "formulaValue";

    }

    private static class HkSubFormFieldEntityField {

        public static final String parentField = "parentField";
        public static final String parentFieldId = "parentField.$id";
        public static final String isArchive = "isArchive";
        public static final String id = "id";
        public static final String subFieldName = "subFieldName";
        public static final String FRANCHISE = "franchise";
        public static final String IS_DROPLIST_FIELD = "isDroplistField";
    }

    //------------------------------- Field Document service fields end-------------------------------------//
    //------------------------------- Sub form service fields starts----------------------------------------//
    private static final class SubFormFields {

        private static final String INSTANCE_ID = "instanceId";
        private static final String FRANCHISE_ID = "franchiseId";
        private static final String ID = "id";
        private static final String IS_ARCHIVE = "isArchive";
    }

    private static final class AutogeneratedFields {

        private static final String OBJECT_ID = "id";
    }

    //------------------------------- Field Document service fields end-------------------------------------//
    //------------------------------- Sub form service fields starts----------------------------------------//
    private static final class SubEntityExceptionFields {

        private static final String INSTANCE_ID = "instanceId";
        private static final String FIELD_ID = "fieldId";

    }
    //------------------------------- Sub form service fields starts----------------------------------------//

    private static final class FeatureFieldPermissionFields {

        private static final String FIELD_ID = "fieldId";
        private static final String FRANCHISE = "franchise";
        private static final String DESIGNATION = "designation";
        private static final String SECTION_CODE = "sectionCode";
        private static final String FEATURE = "feature";

    }

    @Override
    public List<GenericDocument> retrieveDocument(Long instanceId, FeatureNameForCustomField feature, long companyId) {
        List<Criteria> criterias = new ArrayList<Criteria>();

        if (instanceId != null) {
            criterias.add(Criteria.where("instanceId").is(instanceId));
        }
        criterias.add(Criteria.where("franchiseId").is(companyId));

        List<GenericDocument> documents = null;
        if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.USER)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkUserDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT)) {
            documents = mongoGenericDao.findByCriteria(criterias, com.argusoft.hkg.nosql.model.HkDepartmentDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkHolidayDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkApplyLeaveDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkFranchiseDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkLeaveWorkflowDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.EVENT)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkEventDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.TASK)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkTaskDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.SHIFT)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkShiftDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.ASSET)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkAssetDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkCategoryDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.NOTIFICATION)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkNotificationDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.GOAL)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkGoalDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkMessageDocument.class);
        }

        return documents;
    }

    @Override
    public List<CustomField> makeCustomField(Map<Long, Map<String, Object>> val, Map<String, String> dbTypeMap, Map<String, String> uiFieldListWithComponentType, String featureName, long companyId, Object instanseId) {
        if (!CollectionUtils.isEmpty(val)) {
            List<CustomField> customFields = new ArrayList<>();
            for (Map.Entry<Long, Map<String, Object>> entry : val.entrySet()) {
                Long customFieldId = entry.getKey();
                Map<String, Object> colValueMap = entry.getValue();
                if (!CollectionUtils.isEmpty(colValueMap)) {
                    BasicBSONObject basicBSONObject = makeBSONObject(colValueMap, dbTypeMap, uiFieldListWithComponentType, featureName, companyId, instanseId, Boolean.FALSE, null, null);
                    CustomField customField = new CustomField(customFieldId, basicBSONObject);
                    customFields.add(customField);
                }
            }
            return customFields;
        }
        return null;
    }

    @Override
    public Long saveOrUpdate(Long instanceId, FeatureNameForCustomField featureName, long companyId, Map<SectionNameForCustomField, List<CustomField>> sectionCustomFieldMap) {
        if (featureName != null) {
            List<GenericDocument> documents = retrieveDocument(instanceId, featureName, companyId);
            GenericDocument document = null;
            if (CollectionUtils.isEmpty(documents)) {
                if (featureName.equals(FeatureNameForCustomField.USER)) {
                    document = new HkUserDocument();
                } else if (featureName.equals(FeatureNameForCustomField.DEPARTMENT)) {
                    document = new com.argusoft.hkg.nosql.model.HkDepartmentDocument();
                } else if (featureName.equals(FeatureNameForCustomField.FRANCHISE)) {
                    document = new HkFranchiseDocument();
                } else if (featureName.equals(FeatureNameForCustomField.LEAVEWORKFLOW)) {
                    document = new HkLeaveWorkflowDocument();
                } else if (featureName.equals(FeatureNameForCustomField.ASSET)) {
                    document = new HkAssetDocument();
                } else if (featureName.equals(FeatureNameForCustomField.CATEGORY)) {
                    document = new HkCategoryDocument();
                } else if (featureName.equals(FeatureNameForCustomField.HOLIDAY)) {
                    document = new HkHolidayDocument();
                } else if (featureName.equals(FeatureNameForCustomField.LEAVE)) {
                    document = new HkApplyLeaveDocument();
                } else if (featureName.equals(FeatureNameForCustomField.RESPONDLEAVE)) {
                    document = new HkRespondLeaveDocument();
                } else if (featureName.equals(FeatureNameForCustomField.EVENT)) {
                    document = new HkEventDocument();
                } else if (featureName.equals(FeatureNameForCustomField.TASK)) {
                    document = new HkTaskDocument();
                } else if (featureName.equals(FeatureNameForCustomField.SHIFT)) {
                    document = new HkShiftDocument();
                } else if (featureName.equals(FeatureNameForCustomField.NOTIFICATION)) {
                    document = new HkNotificationDocument();
                } else if (featureName.equals(FeatureNameForCustomField.GOAL)) {
                    document = new HkGoalDocument();
                } else if (featureName.equals(FeatureNameForCustomField.MESSAGE)) {
                    document = new HkMessageDocument();
                }

            } else {
                document = documents.get(0);
            }
//            //System.out.println("Doc=" + document);
            document.setInstanceId(instanceId);
            // document.setFeatureName(featureName);
            document.setFranchiseId(companyId);
            if (!CollectionUtils.isEmpty(sectionCustomFieldMap)) {
                List<SectionDocument> sectionDocumentList = new ArrayList<>();
                BasicBSONObject bSONObject = new BasicBSONObject();
                for (Map.Entry<SectionNameForCustomField, List<CustomField>> entry : sectionCustomFieldMap.entrySet()) {
                    SectionDocument sectionDoc = new SectionDocument();
                    SectionNameForCustomField sectionName = entry.getKey();
                    List<CustomField> customFieldList = entry.getValue();
                    if (sectionName.equals(HkSystemConstantUtil.SectionNameForCustomField.GENERAL)) {
                        for (CustomField customField : customFieldList) {
                            bSONObject.putAll(customField.getFieldValue().toMap());
                        }
                    } else {
                        sectionDoc.setSectionName(sectionName);
                        sectionDoc.setCustomFields(customFieldList);
                        sectionDocumentList.add(sectionDoc);
                    }
                }
                document.setFieldValue(bSONObject);
                document.setSectionList(sectionDocumentList);
            }
            mongoGenericDao.update(document);
        }
        return null;
    }

    @Override
    public Map<Long, Map<SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds(List<Long> instanceIds, FeatureNameForCustomField feature, long companyId
    ) {
        List<Criteria> criterias = new ArrayList<>();
        List<GenericDocument> documents = null;
        Map<Long, Map<SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> instanceSectionCustomFieldMap = null;
        Map<SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionCustomFieldMap = null;
        if (instanceIds != null) {
            criterias.add(Criteria.where("instanceId").in(instanceIds));
        }
        criterias.add(Criteria.where("franchiseId").is(companyId));
        if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.USER)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkUserDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.DEPARTMENT)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.FRANCHISE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkFranchiseDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVEWORKFLOW)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkLeaveWorkflowDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.ASSET)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkAssetDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkCategoryDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.HOLIDAY)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkHolidayDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.LEAVE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkApplyLeaveDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.EVENT)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkEventDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.TASK)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkTaskDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.SHIFT)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkShiftDocument.class);
        } else if (feature != null && feature.equals(HkSystemConstantUtil.FeatureNameForCustomField.MESSAGE)) {
            documents = mongoGenericDao.findByCriteria(criterias, HkMessageDocument.class);
        }

        if (!CollectionUtils.isEmpty(documents)) {
            //System.out.println("documents :" + documents);
            instanceSectionCustomFieldMap = new HashMap<>();
            for (GenericDocument genericDocument : documents) {
                if (!CollectionUtils.isEmpty(genericDocument.getSectionList())) {
                    sectionCustomFieldMap = new HashMap<>();
                    for (SectionDocument sectionDocument : genericDocument.getSectionList()) {
                        List<Map<Long, Map<String, Object>>> customFieldMapList = new ArrayList<>();
                        Map<Long, Map<String, Object>> idFieldMap = new HashMap<>();
                        if (!CollectionUtils.isEmpty(sectionDocument.getCustomFields())) {
                            for (CustomField customField : sectionDocument.getCustomFields()) {
                                idFieldMap.put(customField.getId(), customField.getFieldValue().toMap());
                            }
                            customFieldMapList.add(idFieldMap);
                            sectionCustomFieldMap.put(sectionDocument.getSectionName(), customFieldMapList);
                        }
                    }
                    instanceSectionCustomFieldMap.put(genericDocument.getInstanceId(), sectionCustomFieldMap);
                }
            }
            return instanceSectionCustomFieldMap;
        }
        return null;
    }

    @Override
    public BasicBSONObject makeBSONObject(Map<String, Object> val, Map<String, String> dbTypeMap, Map<String, String> uiFieldListWithComponentType, String featureName, long companyId, Object instanseId, Boolean updateFlag, Map<String, Object> fieldValues, Map<String, String> autoGeneratedLabelMap) {
        if (!CollectionUtils.isEmpty(val)) {
//            List<String> uiFieldList = new ArrayList<>();
//            for (Map.Entry<String, String> entrySet : dbTypeMap.entrySet()) {
//                uiFieldList.add(entrySet.getKey());
//
//            }
            if (!CollectionUtils.isEmpty(uiFieldListWithComponentType)) {
                // Map<String, String> uiFieldMap = this.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN;
                for (Map.Entry<String, String> uiField : uiFieldListWithComponentType.entrySet()) {
                    if (uiField.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN) || uiField.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.USER_MULTISELECT)
                            || uiField.getValue().equals(pointerComponentType)) {
                        if (dbTypeMap.containsKey(uiField.getKey())) {
                            dbTypeMap.put(uiField.getKey(), HkSystemConstantUtil.CustomField.DbFieldType.ARRAY);
                        }

                        if (val.containsKey(uiField.getKey())) {
                            String value = val.get(uiField.getKey()).toString();
                            List<String> values = new ArrayList<>();
                            String[] valueArray = value.replace("\"", "").split(",");
                            for (String v : valueArray) {
                                values.add(v.replace("\"", ""));
                            }

                            val.put(uiField.getKey(), values);
                        }
                    }
                    if (uiField.getValue().equals(HkSystemConstantUtil.CustomField.ComponentType.AUTO_GENERATED)) {
                        if (val.containsKey(uiField.getKey()) && !CollectionUtils.isEmpty(autoGeneratedLabelMap)) {
                            String value = val.get(uiField.getKey()).toString();
                            if (autoGeneratedLabelMap.containsKey("beforeLabel") && autoGeneratedLabelMap.get("beforeLabel") != null && autoGeneratedLabelMap.containsKey("afterLabel") && autoGeneratedLabelMap.get("afterLabel") != null) {
                                String beforeLabel = autoGeneratedLabelMap.get("beforeLabel");
                                String afterLabel = autoGeneratedLabelMap.get("afterLabel");
                                value = beforeLabel + value + afterLabel;
                            } else if (autoGeneratedLabelMap.containsKey("beforeLabel") && autoGeneratedLabelMap.get("beforeLabel") != null) {
                                String beforeLabel = autoGeneratedLabelMap.get("beforeLabel");
                                value = beforeLabel + value;
                            } else if (autoGeneratedLabelMap.containsKey("afterLabel") && autoGeneratedLabelMap.get("afterLabel") != null) {
                                String afterLabel = autoGeneratedLabelMap.get("afterLabel");
                                value = value + afterLabel;
                            }
                            val.put(uiField.getKey(), value);
                        }
                    }

                }
            }
            BasicBSONObject basicBSONObject = new BasicBSONObject();
            if (!updateFlag) {

                for (Map.Entry<String, Object> entry : val.entrySet()) {
                    String colName = entry.getKey();
                    Object value = entry.getValue();
                    String dbType = dbTypeMap.get(colName);
                    if (colName.contains(HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                        dbType = "String";
                    }
                    //System.out.println("Col namew:::::::::" + colName + "---------" + "dbtype" + dbType);
                    if (dbType != null && value != null && !"".equals(value.toString())) {
                        switch (dbType) {
                            case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
//                            if (value instanceof Integer) {
                                DateTime dateTime = new DateTime(value.toString());
                                basicBSONObject.put(colName, dateTime.toDate());
//                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                try {
//                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                                if (value instanceof Integer) {
                                    dateTime = new DateTime(value.toString());
                                    basicBSONObject.put(colName, new DateTime(dateTime.toDate()));
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                    try {
                                        basicBSONObject.put(colName, sdf.parse(value.toString()));
                                    } catch (ParseException ex) {
                                        Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                                if (value instanceof Integer) {
                                    basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
                                } else {
                                    SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                    try {
                                        basicBSONObject.put(colName, sdf.parse(value.toString()));
                                    } catch (ParseException ex) {
                                        Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                                basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                                basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                                basicBSONObject.put(colName, Long.parseLong(value.toString()));
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
                                basicBSONObject.put(colName, value.toString());
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
                                List<String> Array = new ArrayList<>();
                                Array.add(value.toString());
                                basicBSONObject.put(colName, (ArrayList) value);
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
                                basicBSONObject.put(colName, (ArrayList<Long>) value);
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.IMAGE:
                                String imageName;
                                if (value instanceof String) {
                                    imageName = value.toString();
                                } else {
                                    ArrayList imageArray = (ArrayList) value;
                                    imageName = imageArray.get(0).toString();
                                }
                                String oldImageName = imageName;
                                if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                    try {
                                        imageName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, imageName);
                                        Boolean isNewFile = FolderManagement.isNewFile(oldImageName);
                                        FolderManagement.copyFilesCustom(oldImageName, imageName, instanseId, true);
                                        if (isNewFile) {
//                                            saveOrUpdateFileTransferDocument(imageName, companyId);
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                System.out.println("image name to be set" + imageName);
                                basicBSONObject.put(colName, imageName);
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.FILE:
                                ArrayList fileNameArray = (ArrayList) value;
                                ArrayList newFileNames = new ArrayList();
                                for (Object fileNameObj : fileNameArray) {
                                    String fileName = fileNameObj.toString();
                                    String oldFileName = fileName;
                                    if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                        try {
                                            fileName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, fileName);
                                            Boolean isNewFile = FolderManagement.isNewFile(oldFileName);
                                            FolderManagement.copyFilesCustom(oldFileName, fileName, instanseId, false);
                                            if (isNewFile) {
//                                                saveOrUpdateFileTransferDocument(fileName, companyId);
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    newFileNames.add(fileName);
                                }
                                basicBSONObject.put(colName, newFileNames);
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN:
                                basicBSONObject.put(colName, Boolean.parseBoolean(value.toString()));
                                break;
                            case HkSystemConstantUtil.CustomField.DbFieldType.OBJECT_ID:
                                basicBSONObject.put(colName, value.toString());
                                break;

                        }
                    }
                }
                return basicBSONObject;

            } else {
                if (!CollectionUtils.isEmpty(fieldValues)) {
                    List<String> blankValuesKeys = new ArrayList<>();

                    for (Map.Entry<String, Object> entry : val.entrySet()) {
                        String colName = entry.getKey();
                        Object value = entry.getValue();

                        Boolean isBlankArray = Boolean.FALSE;
                        if (value instanceof ArrayList) {
                            List<Object> list = (ArrayList) value;
                            if (CollectionUtils.isEmpty(list) || list.size() == 0) {
                                isBlankArray = Boolean.TRUE;
                            } else {
                                for (Object object : list) {
                                    if (!StringUtils.hasText(object.toString())) {
                                        isBlankArray = Boolean.TRUE;
                                    }
                                }
                            }

                        }
                        String dbType = dbTypeMap.get(colName);
                        if (colName.contains(HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                            dbType = "String";
                        }
                        //System.out.println("Col namew:::::::::" + colName + "---------" + "dbtype" + dbType);
                        if (dbType != null && value != null && !"".equals(value.toString()) && !isBlankArray) {
                            switch (dbType) {
                                case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
//                            if (value instanceof Integer) {
                                    DateTime dateTime = new DateTime(value.toString());
                                    basicBSONObject.put(colName, dateTime.toDate());
//                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                try {
//                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                                    if (value instanceof Integer) {
                                        dateTime = new DateTime(value.toString());
                                        basicBSONObject.put(colName, new DateTime(dateTime.toDate()));
                                    } else {
                                        SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                        try {
                                            basicBSONObject.put(colName, sdf.parse(value.toString()));
                                        } catch (ParseException ex) {
                                            Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                                    if (value instanceof Integer) {
                                        basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
                                    } else {
                                        SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                        try {
                                            basicBSONObject.put(colName, sdf.parse(value.toString()));
                                        } catch (ParseException ex) {
                                            Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                                    basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                                    basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                                    basicBSONObject.put(colName, Long.parseLong(value.toString()));
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
                                    basicBSONObject.put(colName, value.toString());
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
                                    List<String> Array = new ArrayList<>();
                                    Array.add(value.toString());
                                    basicBSONObject.put(colName, (ArrayList) value);
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
                                    basicBSONObject.put(colName, (ArrayList<Long>) value);
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.IMAGE:
                                    String imageName;
                                    if (value instanceof String) {
                                        imageName = value.toString();
                                    } else {
                                        ArrayList imageArray = (ArrayList) value;
                                        imageName = imageArray.get(0).toString();
                                    }
                                    String oldImageName = imageName;
                                    if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                        try {
                                            imageName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, imageName);
                                            Boolean isNewFile = FolderManagement.isNewFile(oldImageName);
                                            FolderManagement.copyFilesCustom(oldImageName, imageName, instanseId, true);
                                            if (isNewFile) {
//                                                saveOrUpdateFileTransferDocument(imageName, companyId);
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    basicBSONObject.put(colName, imageName);
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.FILE:
                                    ArrayList fileNameArray = (ArrayList) value;
                                    ArrayList newFileNames = new ArrayList();
                                    for (Object fileNameObj : fileNameArray) {
                                        String fileName = fileNameObj.toString();
                                        String oldFileName = fileName;
                                        if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                            try {
                                                fileName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, fileName);
                                                Boolean isNewFile = FolderManagement.isNewFile(oldFileName);
                                                FolderManagement.copyFilesCustom(oldFileName, fileName, instanseId, false);
                                                if (isNewFile) {
//                                                    saveOrUpdateFileTransferDocument(fileName, companyId);
                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        newFileNames.add(fileName);
                                    }
                                    basicBSONObject.put(colName, newFileNames);
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN:
                                    basicBSONObject.put(colName, Boolean.parseBoolean(value.toString()));
                                    break;
                                case HkSystemConstantUtil.CustomField.DbFieldType.OBJECT_ID:
                                    basicBSONObject.put(colName, value.toString());
                                    break;

                            }
                        } else {
                            blankValuesKeys.add(colName);
//                            System.out.println("col name : " + colName);
                        }
                    }
//                    System.out.println("Basic bson size ::: " + basicBSONObject.toMap().size());
//                    System.out.println("field value size before ::: " + fieldValues.size());
                    fieldValues.putAll(basicBSONObject.toMap());
//                    System.out.println("field value size after ::: " + fieldValues.size());
                    if (!CollectionUtils.isEmpty(blankValuesKeys)) {
//                        System.out.println("Blank values size :: " + blankValuesKeys.size());
                        for (String string : blankValuesKeys) {
//                            System.out.println("B;lank of :: " + string);
                            if (fieldValues.containsKey(string)) {
                                fieldValues.remove(string);
                            }
                        }
//                        System.out.println("After removal size is : " + fieldValues.size());
                    }

                }
                return new BasicBSONObject(fieldValues);
            }

        }
        return null;
    }

    @Override
    public Long saveAll(List<Long> instanceId, FeatureNameForCustomField featureName, long companyId, Map<SectionNameForCustomField, List<CustomField>> sectionCustomFieldMap) {
        if (featureName != null) {
            GenericDocument document = null;
            List<GenericDocument> genericDocuments = null;
            if (instanceId != null) {
                genericDocuments = new ArrayList<>();
                for (Long id : instanceId) {
                    if (featureName.equals(FeatureNameForCustomField.ISSUEASSET)) {
                        document = new HkIssueAssetDocument();
                    }
                    document.setInstanceId(id);
                    document.setFeatureName(featureName);
                    document.setFranchiseId(companyId);
                    if (!CollectionUtils.isEmpty(sectionCustomFieldMap)) {
                        List<SectionDocument> sectionDocumentList = new ArrayList<>();
                        for (Map.Entry<SectionNameForCustomField, List<CustomField>> entry : sectionCustomFieldMap.entrySet()) {
                            SectionDocument sectionDoc = new SectionDocument();
                            SectionNameForCustomField sectionName = entry.getKey();
                            List<CustomField> customFieldList = entry.getValue();
                            sectionDoc.setSectionName(sectionName);
                            sectionDoc.setCustomFields(customFieldList);
                            sectionDocumentList.add(sectionDoc);
                        }
                        document.setSectionList(sectionDocumentList);
                    }
                    genericDocuments.add(document);
                }
                mongoGenericDao.createAll(genericDocuments);
            }
        }
        return null;
    }

    @Override
    public CenterCompanyFeatureSectionDocument
            retrieveCenterCompanyFeatureSectionDocumentById(Long id) {
        return mongoGenericDao.getMongoTemplate().findById(id, CenterCompanyFeatureSectionDocument.class
        );
    }
    //------------------------------- Custom field service ends -------------------------------------//
    //------------------------------- Field Document service start-------------------------------------//

    @Override
    public Map<Long, List<String>> searchFields(String searchStr, Map<Long, String> featureIdNameMap) {
//        Search search = null;
        List<HkFieldDocument> fieldList = new ArrayList<>();
//        if (StringUtils.hasText(searchStr) && searchStr.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
//            if (searchStr.toUpperCase().contains(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE)) {
//                searchStr = searchStr.substring(searchStr.toUpperCase().indexOf(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE) + HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE.length()).trim();
//                List<UMFeature> featureList = this.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY, HkSystemConstantUtil.FeatureTypes.MENU), null, Boolean.TRUE, searchStr);
//                List<Long> featureIds = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(featureList)) {
//                    for (UMFeature feature : featureList) {
//                        featureIds.add(feature.getId());
//                    }
//                }
//                if (!CollectionUtils.isEmpty(featureList)) {
//                    //System.out.println("Feature else");
//                    search = SearchFactory.getSearch(HkFieldEntity.class);
//                    search.addFilterIn(HkFieldServiceImpl.UMFeatureDetail.FEATURE_ID, featureIds);
//                    search.addFilterEqual(HkFieldServiceImpl.HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
//                    search.addFilterEqual(HkFieldServiceImpl.HkFieldEntityField.IS_EDITABLE, Boolean.TRUE);
//                    fieldList = commonDAO.search(search);
//                }
//            } else {
//                //System.out.println("Main else");
//                search = new Search(HkFieldEntity.class);
//                search.addFilterILike(HkFieldServiceImpl.HkFieldEntityField.FIELD_LABEL, searchStr + "%");
//                search.addFilterEqual(IS_ARCHIVE, false);
//                search.addFilterEqual(HkFieldServiceImpl.HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
//                search.addFilterEqual(HkFieldServiceImpl.HkFieldEntityField.IS_EDITABLE, Boolean.TRUE);
//                search.addFilterEqual(HkFieldServiceImpl.HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
//
//                fieldList = commonDAO.search(search);
//            }
//        }
//        Map<Long, List<String>> finalMap = null;
//        if (!CollectionUtils.isEmpty(fieldList)) {
//            finalMap = new HashMap<>();
//            for (HkFieldEntity field : fieldList) {
//                List<String> mapFieldList = finalMap.get(field.getId());
//                if (mapFieldList == null) {
//                    mapFieldList = new ArrayList<>();
//                }
//                mapFieldList.add(field.getFieldLabel() + "," + field.getComponentType()
//                        + " , " + (field.getSection() == null ? HkSystemConstantUtil.DEFAULT_SECTION : field.getSection().getSectionName()) + " , " + featureIdNameMap.get(field.getFeature()));
//                finalMap.put(field.getId(), mapFieldList);
//            }
//        }
        return null;
    }

    private Collection<HkFieldDocument> fillValuePattern(Collection<HkFieldDocument> fieldList) {
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (HkFieldDocument field : fieldList) {
                List<String> list = new ArrayList<>();
                list.add(field.getId() + "");
                List<HkMasterValueDocument> fieldMaster = foundationDocumentService.retrieveMasterValueByCode(list);
                for (HkMasterValueDocument masterValue : fieldMaster) {
                    if (field.getFieldValues() == null) {
                        field.setFieldValues(masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                    } else {
                        field.setFieldValues(field.getFieldValues() + "," + masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                    }
                }
            }
        }
        return fieldList;
    }

    @Override
    public BasicBSONObject makeBSONObject(Map<String, Object> val, Map<String, String> dbTypeMap, String featureName, long companyId, Object instanseId, Map<String, String> autoGeneratedLabelMap) {
        if (!CollectionUtils.isEmpty(val)) {
            BasicBSONObject basicBSONObject = new BasicBSONObject();
            for (Map.Entry<String, Object> uiField : val.entrySet()) {
                String dbFieldName = uiField.getKey();
                String[] split = dbFieldName.split("\\$");
                if (split[1].equals(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(HkSystemConstantUtil.CustomField.ComponentType.AUTO_GENERATED))) {
                    if (!CollectionUtils.isEmpty(autoGeneratedLabelMap)) {
                        String value = uiField.getValue().toString();
                        if (autoGeneratedLabelMap.containsKey("beforeLabel") && autoGeneratedLabelMap.get("beforeLabel") != null && autoGeneratedLabelMap.containsKey("afterLabel") && autoGeneratedLabelMap.get("afterLabel") != null) {
                            String beforeLabel = autoGeneratedLabelMap.get("beforeLabel");
                            String afterLabel = autoGeneratedLabelMap.get("afterLabel");
                            value = beforeLabel + value + afterLabel;
                        } else if (autoGeneratedLabelMap.containsKey("beforeLabel") && autoGeneratedLabelMap.get("beforeLabel") != null) {
                            String beforeLabel = autoGeneratedLabelMap.get("beforeLabel");
                            value = beforeLabel + value;
                        } else if (autoGeneratedLabelMap.containsKey("afterLabel") && autoGeneratedLabelMap.get("afterLabel") != null) {
                            String afterLabel = autoGeneratedLabelMap.get("afterLabel");
                            value = value + afterLabel;
                        }
                        uiField.setValue(value);
                    }
                }
            }
            for (Map.Entry<String, Object> entry : val.entrySet()) {
                String colName = entry.getKey();
                Object value = entry.getValue();
                String dbType = dbTypeMap.get(colName);
                if (colName.contains(HkSystemConstantUtil.CURRENCY_CODE_CUSTOM)) {
                    dbType = "String";
                }
                if (dbType != null && value != null && !"".equals(value.toString())) {
                    switch (dbType) {
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE:
//                            if (value instanceof Integer) {
                            basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
//                            } else {
//                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_FORMAT);
//                                try {
//                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
//                                } catch (ParseException ex) {
//                                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DATE_TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_DATE_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
                                } catch (ParseException ex) {

                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.TIME:
                            if (value instanceof Integer) {
                                basicBSONObject.put(colName, new DateTime(value.toString()).toDate());
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat(HkSystemConstantUtil.CUSTOM_FIELD_TIME_FORMAT);
                                try {
                                    basicBSONObject.put(colName, sdf.parse(value.toString()));
                                } catch (ParseException ex) {
                                }
                            }
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.DOUBLE:
                            basicBSONObject.put(colName, Double.parseDouble(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.INTEGER:
                            basicBSONObject.put(colName, Integer.parseInt(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.LONG:
                            basicBSONObject.put(colName, Long.parseLong(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING:
                            basicBSONObject.put(colName, value.toString());
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.STRING_ARRAY:
                            List<String> Array = new ArrayList<>();
                            Array.add(value.toString());
                            basicBSONObject.put(colName, (ArrayList) value);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.ARRAY:
                            basicBSONObject.put(colName, (ArrayList<Long>) value);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.IMAGE:
                            String imageName;
                            //System.out.println("In update called --> " + value.toString());
                            if (value instanceof String) {
                                imageName = value.toString();
                            } else {
                                ArrayList imageArray = (ArrayList) value;
                                imageName = imageArray.get(0).toString();
                            }
                            String oldImageName = imageName;
                            if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                try {
                                    imageName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, imageName);
                                    //System.out.println("In wrapper name is : " + imageName);
                                    Boolean isNewFile = FolderManagement.isNewFile(oldImageName);
                                    FolderManagement.copyFilesCustom(oldImageName, imageName, instanseId, true);
                                    if (isNewFile) {
//                                        saveOrUpdateFileTransferDocument(imageName, companyId);
                                    }
                                } catch (IOException ex) {

                                }
                            }
                            basicBSONObject.put(colName, imageName);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.FILE:
                            ArrayList fileNameArray = (ArrayList) value;
                            ArrayList newFileNames = new ArrayList();
                            for (Object fileNameObj : fileNameArray) {
                                String fileName = fileNameObj.toString();
                                String oldFileName = fileName;
                                if (!value.toString().contains(FolderManagement.UNIQUE_SEPARATOR)) {
                                    try {
                                        fileName = FolderManagement.getTempFileNameCustom(companyId, featureName, null, instanseId, fileName);
                                        Boolean isNewFile = FolderManagement.isNewFile(oldFileName);
                                        FolderManagement.copyFilesCustom(oldFileName, fileName, instanseId, false);
                                        if (isNewFile) {
//                                            saveOrUpdateFileTransferDocument(fileName, companyId);
                                        }
                                    } catch (IOException ex) {

                                    }
                                }
                                newFileNames.add(fileName);
                            }
                            basicBSONObject.put(colName, newFileNames);
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.BOOLEAN:
                            basicBSONObject.put(colName, Boolean.parseBoolean(value.toString()));
                            break;
                        case HkSystemConstantUtil.CustomField.DbFieldType.OBJECT_ID:
                            basicBSONObject.put(colName, value.toString());
                            break;

                    }
                }
            }
            return basicBSONObject;
        }
        return null;
    }

    @MongoTransaction
    private void saveOrUpdateFileTransferDocument(String fileName, Long company) {
        SyncFileTransferDocument syncFileTransferDocument = new SyncFileTransferDocument();
        syncFileTransferDocument.setCompany(company);
        syncFileTransferDocument.setFileName(fileName);
        syncFileTransferDocument.setLastModifiedOn(new Date());
        mongoGenericDao.update(syncFileTransferDocument);
    }

//    @Override
//    public Map<String, String> retrieveCustomUIFieldNameWithComponentTypes(Long featureId, Long companyId) {
//        Map<String, String> uiFieldMap = null;
//        List<HkFieldDocument> fieldDocuments = null;
//        Map<Long, String> fieldIdWithPointerId = null;
//        List<Long> pointerIdsList = null;
//        if (featureId != null && companyId != null) {
//            List<Criteria> criterias = new ArrayList<>();
//            criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
//            criterias.add(Criteria.where(HkFieldEntityField.FEATURE).is(featureId));
//            criterias.add(Criteria.where(HkFieldEntityField.FRANCHISE).is(companyId));
//            criterias.add(Criteria.where(HkFieldEntityField.IS_CUSTOM_FIELD).is(true));
//            fieldDocuments = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
//        }
//        if (!CollectionUtils.isEmpty(fieldDocuments)) {
//            fieldIdWithPointerId = new HashMap<>();
//            pointerIdsList = new ArrayList<>();
//            for (HkFieldDocument hkFieldDocument : fieldDocuments) {
//                String[] validationsArr = hkFieldDocument.getValidationPattern().replace("{", "")
//                        .replace("}", "")
//                        .split(",");
//                String pointerId = "";
//                for (String validationValue : validationsArr) {
//                    if (validationValue.contains("\"pointer\":")) {
//                        String[] pointerArray = validationValue.split(":");
//                        pointerId = pointerArray[1].replace("\"", "");
//                        fieldIdWithPointerId.put(hkFieldDocument.getId(), pointerId);
//                        pointerIdsList.add(Long.parseLong(pointerId));
//                    }
//                }
//            }
//        }
//        if (!CollectionUtils.isEmpty(fieldDocuments)) {
//            uiFieldMap = new HashMap<>();
//            for (HkFieldDocument hkFieldDocument : fieldDocuments) {
//                String pointerValue = "";
//                if (fieldIdWithPointerId.containsKey(hkFieldDocument.getId())) {
//                    pointerValue = fieldIdWithPointerId.get(hkFieldDocument.getId());
//                }
//                if (hkFieldDocument.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
//                    Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldEntity = this.mapOfFieldIdAndHkFieldEntity(pointerIdsList);
//                    if (mapOfFieldIdAndHkFieldEntity.containsKey(Long.parseLong(pointerValue))) {
//                        String componentType = mapOfFieldIdAndHkFieldEntity.get(Long.parseLong(pointerValue)).getComponentType();
//                        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + componentType;
//                        uiFieldMap.put(hkFieldDocument.getUiFieldName(), pointerComponentType);
//                    }
//                } else {
//                    uiFieldMap.put(hkFieldDocument.getUiFieldName(), hkFieldDocument.getComponentType());
//                }
//            }
//        }
//        return uiFieldMap;
//    }
    @Override
    public Map<String, String> retrieveUIFieldNameWithComponentTypes(List<String> dbFieldNameList) {
        Map<String, String> uiFieldMap = null;
        Map<String, String> componentCodeMap = new HashMap<>(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP);
        Map<String, String> codeMap = null;
        if (componentCodeMap != null && !componentCodeMap.isEmpty()) {
            codeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : componentCodeMap.entrySet()) {
                codeMap.put(entry.getValue(), entry.getKey());

            }
        }
        if (!CollectionUtils.isEmpty(dbFieldNameList)) {
            uiFieldMap = new HashMap<>();
            for (String dbField : dbFieldNameList) {
                System.out.println("dbField: " + dbField);
                String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                if (split != null && split.length >= 1) {
                    uiFieldMap.put(dbField, codeMap.get(split[1]));
                }

            }
        }
        return uiFieldMap;
    }

    private Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldEntity(List<Long> fieldIds) {
        Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldEntity = null;
        List<HkFieldDocument> fieldList;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
        if (!CollectionUtils.isEmpty(fieldIds)) {
            criterias.add(Criteria.where(HkFieldEntityField.ID).in(fieldIds));
        }
        fieldList = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class
        );
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfFieldIdAndHkFieldEntity = new HashMap<>();
            for (HkFieldDocument field : fieldList) {
                mapOfFieldIdAndHkFieldEntity.put(field.getId(), field);
            }
        }
        return mapOfFieldIdAndHkFieldEntity;
    }

    @Override
    public Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldDocument(Long companyId) {
        Map<Long, HkFieldDocument> mapOfFieldIdAndDocument = null;
        List<HkFieldDocument> fieldList;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
//        if (companyId != null) {
//            criterias.add(Criteria.where(FRANCHISE).is(companyId));
//        }
        fieldList = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfFieldIdAndDocument = new HashMap<>();
            for (HkFieldDocument field : fieldList) {
                mapOfFieldIdAndDocument.put(field.getId(), field);
            }
        }
        return mapOfFieldIdAndDocument;
    }

    @Override
    public List<HkSubFormFieldDocument> retrieveListOfSubEntitiesAssociatedWithFieldId(Long hkFieldDocId, long companyId) {
        //System.out.println("hkfieldDoc is..." + hkFieldDocId);
        List<Criteria> listSubFormDocCriterias = new ArrayList<>();
        listSubFormDocCriterias.add(Criteria.where(HkSubFormFieldEntityField.isArchive).is(Boolean.FALSE));
        if (hkFieldDocId != null) {
            listSubFormDocCriterias.add(Criteria.where(HkSubFormFieldEntityField.parentFieldId).is(hkFieldDocId));
        }
        List<HkSubFormFieldDocument> subFormEntitiesList = mongoGenericDao.findByCriteria(listSubFormDocCriterias, HkSubFormFieldDocument.class
        );
        return subFormEntitiesList;
    }

    @Override
    public HkFieldDocument
            retrieveCustomFieldByFieldId(Long fieldId) {
        return mongoGenericDao.getMongoTemplate().findById(fieldId, HkFieldDocument.class
        );
    }

    @Override
    public Set<Long> retrieveFeaturesForExistingFields(Boolean isCustomField, Long companyId) {
        Query query = new Query();
        if (isCustomField != null) {
            query.addCriteria(Criteria.where(HkFieldEntityField.IS_CUSTOM_FIELD).is(isCustomField));
        }
        query.addCriteria(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
        query.fields().include(HkFieldEntityField.FEATURE);
        List<HkFieldDocument> list = mongoGenericDao.getMongoTemplate().find(query, HkFieldDocument.class
        );
        Set<Long> fieldSet = new HashSet<>();
        for (HkFieldDocument fieldDoc : list) {
            fieldSet.add(fieldDoc.getFeature());
        }
        return fieldSet;
    }

    @Override
    public Map<String, HkFieldDocument> retrieveMapOfDBFieldNameWithEntity(List<String> dbFieldName, Long companyId) {
        Map<String, HkFieldDocument> dbFieldMapWithFieldEntity = null;
        List<Criteria> listCrit = new ArrayList<>();
//        if (companyId != null) {
//            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
//        }
        if (!CollectionUtils.isEmpty(dbFieldName)) {
            listCrit.add(Criteria.where(HkFieldEntityField.FIELD_NAME).in(dbFieldName));

        }
        List<HkFieldDocument> hkFieldEntities = mongoGenericDao.findByCriteria(listCrit, HkFieldDocument.class
        );
        Logger.getLogger(HkCustomFieldServiceImpl.class
                .getName()).info("Entities....................." + hkFieldEntities.size());
        if (!CollectionUtils.isEmpty(hkFieldEntities)) {
            dbFieldMapWithFieldEntity = new HashMap<>();
            for (HkFieldDocument hkFieldEntity : hkFieldEntities) {
                dbFieldMapWithFieldEntity.put(hkFieldEntity.getDbFieldName(), hkFieldEntity);
            }
        }
        return dbFieldMapWithFieldEntity;
    }

    @Override
    public Map<String, HkFieldDocument> retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(Long companyId, String featureName, String componentType) {
        List<Criteria> criterialist = new ArrayList<>();
        Map<String, HkFieldDocument> fieldEntityMap = null;
        if (featureName != null) {
            criterialist.add(Criteria.where(HkFieldEntityField.FORMULA_VALUE).regex(featureName));
        }
        if (componentType != null) {
            criterialist.add(Criteria.where(HkFieldEntityField.COMPONENT_TYPE).is(componentType));
        }
        List<HkFieldDocument> hkFieldEntities = mongoGenericDao.findByCriteria(criterialist, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(hkFieldEntities)) {
            fieldEntityMap = new HashMap<>();
            for (HkFieldDocument hkFieldEntity : hkFieldEntities) {
                fieldEntityMap.put(hkFieldEntity.getDbFieldName(), hkFieldEntity);
            }
        }
        return fieldEntityMap;
    }

    @Override
    public List<HkFieldDocument> retrieveAllFormulaList(Long featureId, Long franchiseId, String featureType, String componentType) {
        System.out.println("componnet type:::" + componentType);
        List<HkFieldDocument> fieldListByFeatureAndComponentType = retrieveFieldByFeatureAndComponentType(franchiseId, null, componentType, Boolean.FALSE);
      
        return fieldListByFeatureAndComponentType;
        
//        System.out.println("List of field size" + fieldListByFeatureAndComponentType.size());
//        List<String> invoiceList = new ArrayList<>();
//        List<String> parcelList = new ArrayList<>();
//        List<String> lotList = new ArrayList<>();
//        List<String> packetList = new ArrayList<>();
//        List<String> planList = new ArrayList<>();
//        List<String> sellList = new ArrayList<>();
//        List<String> transferList = new ArrayList<>();
//        List<String> issueList = new ArrayList<>();
//        List<String> subLotList = new ArrayList<>();
//        int countInvalid = 0;
//        Map<String, String> validFormulaMap = null;
//
//        if (!CollectionUtils.isEmpty(fieldListByFeatureAndComponentType)) {
//            validFormulaMap = new HashMap<>();
//            for (HkFieldDocument field : fieldListByFeatureAndComponentType) {
//                String formulaValue = field.getFormulaValue();
//                String[] formulaArray = formulaValue.split("\\|");
//                //System.out.println("In core formula Array" + formulaArray);
//                for (String formula : formulaArray) {
//                    if (formula.contains(HkSystemConstantUtil.Feature.INVOICE)) {
//                        invoiceList.add(formula.replace(HkSystemConstantUtil.Feature.INVOICE + ".", ""));
//                        //System.out.println("InvoiceList" + invoiceList);
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.PARCEL)) {
//                        parcelList.add(formula.replace(HkSystemConstantUtil.Feature.PARCEL + ".", ""));
//                        //System.out.println("Parcel List" + parcelList);
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.LOT)) {
//                        lotList.add(formula.replace(HkSystemConstantUtil.Feature.LOT + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.PACKET)) {
//                        packetList.add(formula.replace(HkSystemConstantUtil.Feature.PACKET + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.PLAN)) {
//                        planList.add(formula.replace(HkSystemConstantUtil.Feature.PLAN + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.SELL)) {
//                        sellList.add(formula.replace(HkSystemConstantUtil.Feature.SELL + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.TRANSFER)) {
//                        transferList.add(formula.replace(HkSystemConstantUtil.Feature.TRANSFER + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.ISSUE)) {
//                        issueList.add(formula.replace(HkSystemConstantUtil.Feature.ISSUE + ".", ""));
//                    }
//                    if (formula.contains(HkSystemConstantUtil.Feature.SUB_LOT)) {
//                        subLotList.add(formula.replace(HkSystemConstantUtil.Feature.SUB_LOT + ".", ""));
//                    }
//                    if (subLotList.size() > 0) {
//                        System.out.println("sublot list is not empty");
//                    }
//
//                }
//                switch (featureType) {
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT:
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
////                                        validFormula += "true";
//                                    } else {
//                                        countInvalid++;
////                                        validFormula += "false";
//                                    }
//
//                                } else {
//                                    countInvalid++;
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL:
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                    } else {
//                                        countInvalid++;
//                                    }
//
//                                } else {
//                                    countInvalid++;
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
////                                        validFormula += "true";
//                                    } else {
//                                        countInvalid++;
////                                        validFormula += "false";
//                                    }
//
//                                } else {
//                                    countInvalid++;
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET:
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//                                        countInvalid++;
//
//                                    } else {
//
//                                    }
//
//                                } else {
//
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN:
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PLAN:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//                                        countInvalid++;
//
//                                    } else {
//
//                                    }
//
//                                } else {
//
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(planList)) {
//                            for (String plan_formula : planList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(plan_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.SELL_STOCK:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//                                        countInvalid++;
//
//                                    } else {
//
//                                    }
//
//                                } else {
//
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(sellList)) {
//                            for (String sell_formula : sellList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(sell_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.TRANSFER_STOCK:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//                                        countInvalid++;
//
//                                    } else {
//
//                                    }
//
//                                } else {
//
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(transferList)) {
//                            for (String transfer_formula : transferList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(transfer_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ISSUE_STOCK:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//                                        countInvalid++;
//
//                                    } else {
//
//                                    }
//
//                                } else {
//
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(issueList)) {
//                            for (String issue_formula : issueList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(issue_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//
//                    // Code added for sublot on 20 October 2015
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT:
//                        if (!CollectionUtils.isEmpty(subLotList)) {
//                            for (String sublot_formula : subLotList) {
//                                System.out.println("sublot form" + sublot_formula);
//                                int indexOf = Arrays.asList(formulaArray).indexOf(sublot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        System.out.println("inside ayaaaaaaaa");
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "false";
//                                    }
//
//                                } else {
////                                    validFormula += "false";
//                                }
//                            }
//                        }
//                        System.out.println("count validddd" + countInvalid);
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//                        System.out.println("valid frm amp" + validFormulaMap);
//                        break;
//
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_INVOICE:
//                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE:
//                        if (!CollectionUtils.isEmpty(invoiceList)) {
//                            for (String inv_formula : invoiceList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                        countInvalid++;
//                                    } else {
////                                        validFormula += "true";
//                                    }
//
//                                } else {
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(parcelList)) {
//                            for (String par_formula : parcelList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                    } else {
//                                        countInvalid++;
//                                    }
//
//                                } else {
//                                    countInvalid++;
////                                    validFormula += "true";
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(lotList)) {
//                            for (String lot_formula : lotList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                    } else {
//                                        countInvalid++;
//                                    }
//
//                                } else {
//                                    countInvalid++;
//                                }
//                            }
//                        }
//                        if (!CollectionUtils.isEmpty(packetList)) {
//                            for (String packet_formula : packetList) {
//                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
//                                if (indexOf >= 4) {
//                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
//                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
//
//                                    } else {
//                                        countInvalid++;
//                                    }
//
//                                } else {
//                                    countInvalid++;
//                                }
//                            }
//                        }
//                        System.out.println("count valid"+countInvalid);
//                        if (countInvalid > 1) {
//                        } else {
//                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
//                        }
//
//                        break;
//
//                }
//            }
//
//        }
//        return validFormulaMap;
    }

    @Override
    public List<HkFieldDocument> retrieveFieldByFeatureAndComponentType(Long companyId, Long featureId, String componentType, Boolean isArchive) {
        System.out.println("Company Id" + companyId);
        System.out.println("Feature Id" + featureId);
        System.out.println("Component Type..." + componentType);
        System.out.println("Is Archive" + isArchive);
        List<HkFieldDocument> fieldEntityList = new ArrayList<>();
        List<Criteria> criteriaList = new ArrayList<>();
        if (componentType != null) {
            criteriaList.add(Criteria.where(HkFieldEntityField.COMPONENT_TYPE).is(componentType));
        }
        if (isArchive != null) {
            criteriaList.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(isArchive));
        }
        if (featureId != null) {
            criteriaList.add(Criteria.where(HkFieldEntityField.FEATURE).is(featureId));
        }
        fieldEntityList = mongoGenericDao.findByCriteria(criteriaList, HkFieldDocument.class);
        return fieldEntityList;

    }

    @Override
    public Map<Long, HkSubFormFieldDocument> retrieveMapOfFieldIdAndItsDropListSubDocument() {
        Map<Long, HkSubFormFieldDocument> mapOfFieldIdWithDropListDocument = null;
        List<Criteria> listSubFormDocCriterias = new ArrayList<>();
        listSubFormDocCriterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(Boolean.FALSE));
        listSubFormDocCriterias.add(Criteria.where(HkSubFormFieldEntityField.IS_DROPLIST_FIELD).is(Boolean.TRUE));

        List<HkSubFormFieldDocument> subFormEntitiesList = mongoGenericDao.findByCriteria(listSubFormDocCriterias, HkSubFormFieldDocument.class);
        if (!CollectionUtils.isEmpty(subFormEntitiesList)) {
            mapOfFieldIdWithDropListDocument = new HashMap<>();
            for (HkSubFormFieldDocument subFormDoc : subFormEntitiesList) {
                mapOfFieldIdWithDropListDocument.put(subFormDoc.getParentField().getId(), subFormDoc);
            }

        }
        return mapOfFieldIdWithDropListDocument;
    }

    @Override
    public List<HkFieldDocument> retrievePointerEntities() {
        List<HkFieldDocument> fieldDocument = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkFieldEntityField.COMPONENT_TYPE).is(HkSystemConstantUtil.CustomField.ComponentType.POINTER));
        fieldDocument = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        return fieldDocument;
    }

    @Override
    public Map<Long, String> mapOfPointerIdAndItsComponentType() {
        Map<Long, String> pointerIdWithComponentType = null;
        List<HkFieldDocument> pointerEntities = this.retrievePointerEntities();
        List<Long> pointerIds = null;
        Map<Long, Long> fieldIdAndPointerId = new HashMap<>();
        if (!CollectionUtils.isEmpty(pointerEntities)) {
            pointerIds = new ArrayList<>();
        }
        for (HkFieldDocument hkFieldDocument : pointerEntities) {
            String[] validationsArr = hkFieldDocument.getValidationPattern().replace("{", "")
                    .replace("}", "")
                    .split(",");
            String pointerId = "";
            for (String validationValue : validationsArr) {
                if (validationValue.contains("\"pointer\":")) {
                    String[] pointerArray = validationValue.split(":");
                    pointerId = pointerArray[1].replace("\"", "");
                    pointerIds.add(Long.parseLong(pointerId));
                }
            }
            fieldIdAndPointerId.put(Long.parseLong(pointerId), hkFieldDocument.getId());
        }
        Map<Long, HkFieldDocument> mapOfFieldIdAndHkFieldEntity = this.mapOfFieldIdAndHkFieldEntity(pointerIds);
        if (mapOfFieldIdAndHkFieldEntity != null && !mapOfFieldIdAndHkFieldEntity.isEmpty()) {
            pointerIdWithComponentType = new HashMap<>();
            for (Map.Entry<Long, HkFieldDocument> fieldWithEntity : mapOfFieldIdAndHkFieldEntity.entrySet()) {
                Long key = fieldWithEntity.getKey();
                HkFieldDocument value = fieldWithEntity.getValue();
                if (fieldIdAndPointerId.containsKey(fieldWithEntity.getKey())) {
                    pointerIdWithComponentType.put(fieldIdAndPointerId.get(fieldWithEntity.getKey()), fieldWithEntity.getValue().getComponentType());
                }

            }
        }
        return pointerIdWithComponentType;
    }

    @Override
    public Map<String, String> mapOfLabelAndDbFieldWithFeature(Long companyId) {
        List<Criteria> criterias = new ArrayList<>();
        Map<Long, String> mapOfFeatureIdWithFeatureName = hkFeatureService.mapOfFeatureIdWithFeatureName();
        Map<String, String> mapOfLableAndDbFieldWithFeature = null;
        List<HkFieldDocument> fieldList = null;
        criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(Boolean.FALSE));
        criterias.add(Criteria.where(FRANCHISE).is(companyId));
        fieldList = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfLableAndDbFieldWithFeature = new HashMap<>();
            for (HkFieldDocument fieldDoc : fieldList) {
                if (mapOfFeatureIdWithFeatureName.containsKey(fieldDoc.getFeature())) {
                    String featureName = mapOfFeatureIdWithFeatureName.get(fieldDoc.getFeature());
                    mapOfLableAndDbFieldWithFeature.put(featureName + "." + fieldDoc.getFieldLabel(), featureName + "." + fieldDoc.getDbFieldName());
                }
            }

        }
        return mapOfLableAndDbFieldWithFeature;
    }

    //------------------------------- Field Document service ends-------------------------------------//
    //------------------------------- Subform service start-------------------------------------//
    @Override
    @MongoTransaction

    public void saveSubFormValues(List<HkSubFormValueDocument> subFormValueDocuments) {
        if (!CollectionUtils.isEmpty(subFormValueDocuments)) {
            for (HkSubFormValueDocument hkSubFormValueDocument : subFormValueDocuments) {
                mongoGenericDao.update(hkSubFormValueDocument);
            }
        }
    }

    @Override
    public List<HkSubFormValueDocument> retrieveSubFormValueByInstance(Long instanceId, Boolean isArchive) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkSubFormValueDocument> documents = null;
        if (instanceId != null) {
            criterias.add(Criteria.where(SubFormFields.INSTANCE_ID).is(instanceId));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(SubFormFields.IS_ARCHIVE).is(isArchive));
        }

        documents = mongoGenericDao.findByCriteria(criterias, HkSubFormValueDocument.class);
        if (documents == null) {
            return Collections.emptyList();
        } else {
            return documents;
        }
    }

    @Override
    public HkSubFormValueDocument retrieveSubFormValueById(ObjectId id) {
        HkSubFormValueDocument document = (HkSubFormValueDocument) mongoGenericDao.retrieveById(id, HkSubFormValueDocument.class);
        return document;
    }

    @Override
    public List<HkSubFormValueDocument> retrieveSubFormValueByListOfInstanceIds(List<Long> instanceIds, Boolean isArchive) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkSubFormValueDocument> documents = null;
        if (instanceIds != null) {
            criterias.add(Criteria.where(SubFormFields.INSTANCE_ID).in(instanceIds));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(SubFormFields.IS_ARCHIVE).is(isArchive));
        }

        documents = mongoGenericDao.findByCriteria(criterias, HkSubFormValueDocument.class);
        if (documents == null) {
            return Collections.emptyList();
        } else {
            return documents;
        }
    }

    @Override
    public List<HkSubFormValueDocument> retrieveSubFormValueByFranchise(Long franchiseId, Boolean isArchive) {
        List<Criteria> criterias = new ArrayList<>();
        List<HkSubFormValueDocument> documents = null;
        if (franchiseId != null) {
            criterias.add(Criteria.where(SubFormFields.FRANCHISE_ID).is(franchiseId));
        }
        if (isArchive != null) {
            criterias.add(Criteria.where(SubFormFields.IS_ARCHIVE).is(isArchive));
        }

        documents = mongoGenericDao.findByCriteria(criterias, HkSubFormValueDocument.class);
        if (documents == null) {
            return Collections.emptyList();
        } else {
            return documents;
        }
    }

    @Override
    public Map<ObjectId, HkSubFormValueDocument> mapOfObjectIdsWithDocuments(List<ObjectId> objectIds) {
        Map<ObjectId, HkSubFormValueDocument> mapOfObjectIdsWithDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        List<HkSubFormValueDocument> documents = null;
        criterias.add(Criteria.where(SubFormFields.ID).in(objectIds));
        documents = mongoGenericDao.findByCriteria(criterias, HkSubFormValueDocument.class);
        if (!CollectionUtils.isEmpty(documents)) {
            mapOfObjectIdsWithDocuments = new HashMap<>();
            for (HkSubFormValueDocument document : documents) {
                mapOfObjectIdsWithDocuments.put(document.getId(), document);

            }
        }
        return mapOfObjectIdsWithDocuments;
    }

    //------------------------------- Subform service ends-------------------------------------//
    @Override
    public Map<String, String> mapOfDbFieldWithCurrencyCode(Long companyId) {
        List<HkFieldDocument> fieldEntities = null;
        Map<String, String> currencyCodeMap = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        criterias.add(Criteria.where(HkFieldEntityField.COMPONENT_TYPE).is(HkSystemConstantUtil.CustomField.ComponentType.CURRENCY));
        criterias.add(Criteria.where("franchise").is(companyId));
        fieldEntities = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(fieldEntities)) {
            currencyCodeMap = new HashMap<>();
            for (HkFieldDocument fieldEntity : fieldEntities) {

                String[] validationsArr = fieldEntity.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                String currencyCode = null;
                for (String validationValue : validationsArr) {
                    if (validationValue.contains("\"currencyCode\":")) {
                        String[] currencyArray = validationValue.split(":");
                        currencyCode = currencyArray[1];
                        currencyCodeMap.put(fieldEntity.getDbFieldName(), currencyCode);
                    }
                }
            }
        }
        return currencyCodeMap;
    }

    @Override
    public Map<String, Object> retrieveAutogeneratedFields(String dbFieldName, String search, Integer limit, Integer offset) {
        Map<String, Object> result = new HashMap<>();
        Set<String> autogeneratedFields = new HashSet<>();
        List<String> paginatedList = new ArrayList<>();

        Query query = new Query();
        query.fields().include("fieldValue." + dbFieldName);
        query.addCriteria(Criteria.where("fieldValue." + dbFieldName).regex(search));
        query.addCriteria(Criteria.where("isArchive").is(Boolean.FALSE));
        query.with(new Sort(Sort.Direction.DESC, "createdOn"));

        switch (dbFieldName) {
            case HkSystemConstantUtil.AutoNumber.INVOICE_ID:
                List<HkInvoiceDocument> invoiceDocuments = mongoGenericDao.getMongoTemplate().find(query, HkInvoiceDocument.class);
                if (!CollectionUtils.isEmpty(invoiceDocuments)) {
                    for (HkInvoiceDocument invoiceDocument : invoiceDocuments) {
                        autogeneratedFields.add(invoiceDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.PARCEL_ID:
                List<HkParcelDocument> parcelDocuments = mongoGenericDao.getMongoTemplate().find(query, HkParcelDocument.class);
                if (!CollectionUtils.isEmpty(parcelDocuments)) {
                    for (HkParcelDocument parcelDocument : parcelDocuments) {
                        autogeneratedFields.add(parcelDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.LOT_ID:
            case HkSystemConstantUtil.AutoNumber.LOT_SLIP_ID:
                List<HkLotDocument> lotDocuments = mongoGenericDao.getMongoTemplate().find(query, HkLotDocument.class);
                if (!CollectionUtils.isEmpty(lotDocuments)) {
                    for (HkLotDocument lotDocument : lotDocuments) {
                        autogeneratedFields.add(lotDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.PACKET_ID:
            case HkSystemConstantUtil.AutoNumber.PACKET_SLIP_ID:
                List<HkPacketDocument> packetDocuments = mongoGenericDao.getMongoTemplate().find(query, HkPacketDocument.class);
                if (!CollectionUtils.isEmpty(packetDocuments)) {
                    for (HkPacketDocument packetDocument : packetDocuments) {
                        autogeneratedFields.add(packetDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.SELL_ID:
                List<HkSellDocument> sellDocuments = mongoGenericDao.getMongoTemplate().find(query, HkSellDocument.class);
                if (!CollectionUtils.isEmpty(sellDocuments)) {
                    for (HkSellDocument sellDocument : sellDocuments) {
                        autogeneratedFields.add(sellDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.TRANSFER_ID:
                List<HkTransferDocument> transferDocuments = mongoGenericDao.getMongoTemplate().find(query, HkTransferDocument.class);
                if (!CollectionUtils.isEmpty(transferDocuments)) {
                    for (HkTransferDocument transferDocument : transferDocuments) {
                        autogeneratedFields.add(transferDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.PLAN_ID:
                List<HkPlanDocument> planDocuments = mongoGenericDao.getMongoTemplate().find(query, HkPlanDocument.class);
                if (!CollectionUtils.isEmpty(planDocuments)) {
                    for (HkPlanDocument planDocument : planDocuments) {
                        autogeneratedFields.add(planDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
            case HkSystemConstantUtil.AutoNumber.ROUGH_ID:
                List<HkPurchaseDocument> purchaseDocuments = mongoGenericDao.getMongoTemplate().find(query, HkPurchaseDocument.class);
                if (!CollectionUtils.isEmpty(purchaseDocuments)) {
                    for (HkPurchaseDocument purchaseDocument : purchaseDocuments) {
                        autogeneratedFields.add(purchaseDocument.getFieldValue().get(dbFieldName).toString());
                    }
                }
                break;
        }
        if (!CollectionUtils.isEmpty(autogeneratedFields)) {
            List<String> toalFields = new ArrayList<>(autogeneratedFields);
            for (int i = offset; i < (offset + limit); i++) {
                if (i < toalFields.size()) {
                    paginatedList.add(toalFields.get(i));
                }
            }
        }
        result.put("total", autogeneratedFields.size());
        result.put("records", paginatedList);
        return result;
    }

    @Override
    @MongoTransaction
    public void saveValueExceptions(List<HkSubEntityExceptionDocument> hkSubEntityExceptionDocuments) {
        if (!CollectionUtils.isEmpty(hkSubEntityExceptionDocuments)) {
            for (HkSubEntityExceptionDocument hkSubEntityExceptionDocument : hkSubEntityExceptionDocuments) {
                mongoGenericDao.update(hkSubEntityExceptionDocument);
            }

        }
    }

    public List<HkSubEntityExceptionDocument> retrieveValueExceptions(Long instanceId, Long companyId) {
        List<Criteria> criterias = new LinkedList<>();
        if (instanceId != null) {
            criterias.add(Criteria.where(SubEntityExceptionFields.INSTANCE_ID).is(instanceId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(FRANCHISE).is(companyId));
        }
        criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        return mongoGenericDao.findByCriteria(criterias, HkSubEntityExceptionDocument.class);

    }

    @Override
    public Boolean checkUniqueness(String fieldModel, String featureName, Object value, Long companyId) {
        if (!StringUtils.isEmpty(featureName) && !StringUtils.isEmpty(fieldModel) && value != null) {
            List<Criteria> criterias = new LinkedList<>();
            criterias.add(Criteria.where(FIELD_VALUE + '.' + fieldModel).is(value));
            criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
            if (companyId != null) {
                criterias.add(Criteria.where("franchiseId").in(Arrays.asList(0l, companyId)));
            }

            switch (featureName) {
                case HkSystemConstantUtil.Feature.INVOICE:
                    List<HkInvoiceDocument> invoiceDocuments = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
                    if (CollectionUtils.isEmpty(invoiceDocuments)) {
                        return true;
                    } else if (invoiceDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.PARCEL:
                    List<HkParcelDocument> parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class);
                    if (CollectionUtils.isEmpty(parcelDocuments)) {
                        return true;
                    } else if (parcelDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.LOT:
                    List<HkLotDocument> lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class);
                    if (CollectionUtils.isEmpty(lotDocuments)) {
                        return true;
                    } else if (lotDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.PACKET:
                    List<HkPacketDocument> packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class);
                    if (CollectionUtils.isEmpty(packetDocuments)) {
                        return true;
                    } else if (packetDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.ISSUE:
                    List<HkIssueDocument> issueDocuments = mongoGenericDao.findByCriteria(criterias, HkIssueDocument.class);
                    if (CollectionUtils.isEmpty(issueDocuments)) {
                        return true;
                    } else if (issueDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.PLAN:
                    List<HkPlanDocument> planDocuments = mongoGenericDao.findByCriteria(criterias, HkPlanDocument.class);
                    if (CollectionUtils.isEmpty(planDocuments)) {
                        return true;
                    } else if (planDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.SELL:
                    List<HkSellDocument> sellDocuments = mongoGenericDao.findByCriteria(criterias, HkSellDocument.class);
                    if (CollectionUtils.isEmpty(sellDocuments)) {
                        return true;
                    } else if (sellDocuments.size() == 1) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.TRANSFER:
                    List<HkTransferDocument> transferDocuments = mongoGenericDao.findByCriteria(criterias, HkTransferDocument.class);
                    if (CollectionUtils.isEmpty(transferDocuments)) {
                        return true;
                    } else if (transferDocuments.size() == 1) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public Boolean checkUniquenessForFields(Map<String, String> mapOfFields, Map<String, String> idRelatedFields, String modelValue, String modelName, String featureName, Long companyId) {
        if (!StringUtils.isEmpty(featureName)
                && !StringUtils.isEmpty(modelName) && modelValue != null) {
            List<Criteria> criterias = new LinkedList<>();
            // Criteria for the self model
            criterias.add(Criteria.where(FIELD_VALUE + '.' + modelName).is(modelValue));
            criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
            if (companyId != null) {
                criterias.add(Criteria.where("franchiseId").in(Arrays.asList(0l, companyId)));
            }
            // Criterias for all the dependant Fields
            if (mapOfFields != null && !mapOfFields.isEmpty()) {
                for (Map.Entry<String, String> dependantFieldsMap : mapOfFields.entrySet()) {
                    criterias.add(Criteria.where(FIELD_VALUE + '.' + dependantFieldsMap.getKey()).is(dependantFieldsMap.getValue()));

                }

            }
            switch (featureName) {
                case HkSystemConstantUtil.Feature.INVOICE:
                    if (idRelatedFields != null && !idRelatedFields.isEmpty()) {
                        if (idRelatedFields.containsKey("invoiceId")) {
                            criterias.add(Criteria.where(InvoiceDocument.OBJECT_ID).is(new ObjectId(idRelatedFields.get("invoiceId"))));
                        }
                    }
                    List<HkInvoiceDocument> invoiceDocuments = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class);
                    if (CollectionUtils.isEmpty(invoiceDocuments)) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.PARCEL:
                    if (idRelatedFields != null && !idRelatedFields.isEmpty()) {
                        if (idRelatedFields.containsKey("invoiceId")) {
                            criterias.add(Criteria.where(ParcelDocument.INVOICE_ID).is(new ObjectId(idRelatedFields.get("invoiceId"))));
                        }
                        if (idRelatedFields.containsKey("parcelId")) {
                            criterias.add(Criteria.where(ParcelDocument.OBJECT_ID).is(new ObjectId(idRelatedFields.get("parcelId"))));
                        }
                    }
                    List<HkParcelDocument> parcelDocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class);
                    if (CollectionUtils.isEmpty(parcelDocuments)) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.LOT:
                    if (idRelatedFields != null && !idRelatedFields.isEmpty()) {
                        if (idRelatedFields.containsKey("invoiceId")) {
                            criterias.add(Criteria.where(LotDocument.INVOICE_ID).is(new ObjectId(idRelatedFields.get("invoiceId"))));
                        }
                        if (idRelatedFields.containsKey("parcelId")) {
                            criterias.add(Criteria.where(LotDocument.PARCEL_ID).is(new ObjectId(idRelatedFields.get("parcelId"))));
                        }
                        if (idRelatedFields.containsKey("lotId")) {
                            criterias.add(Criteria.where(LotDocument.OBJECT_ID).is(new ObjectId(idRelatedFields.get("lotId"))));
                        }
                    }
                    List<HkLotDocument> lotDocuments = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class);
                    if (CollectionUtils.isEmpty(lotDocuments)) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.PACKET:
                    if (idRelatedFields != null && !idRelatedFields.isEmpty()) {
                        if (idRelatedFields.containsKey("invoiceId")) {
                            criterias.add(Criteria.where(PacketDocument.INVOICE_ID).is(new ObjectId(idRelatedFields.get("invoiceId"))));
                        }
                        if (idRelatedFields.containsKey("parcelId")) {
                            criterias.add(Criteria.where(PacketDocument.PARCEL_ID).is(new ObjectId(idRelatedFields.get("parcelId"))));
                        }
                        if (idRelatedFields.containsKey("lotId")) {
                            criterias.add(Criteria.where(PacketDocument.LOT_ID).is(new ObjectId(idRelatedFields.get("lotId"))));
                        }
                        if (idRelatedFields.containsKey("packetId")) {
                            criterias.add(Criteria.where(LotDocument.OBJECT_ID).is(new ObjectId(idRelatedFields.get("packetId"))));
                        }
                    }
                    List<HkPacketDocument> packetDocuments = mongoGenericDao.findByCriteria(criterias, HkPacketDocument.class);
                    if (CollectionUtils.isEmpty(packetDocuments)) {
                        return true;
                    }
                    break;
                case HkSystemConstantUtil.Feature.SUB_LOT:
                    if (idRelatedFields != null && !idRelatedFields.isEmpty()) {
                        if (idRelatedFields.containsKey("invoiceId")) {
                            criterias.add(Criteria.where(SubLotDocument.INVOICE_ID).is(new ObjectId(idRelatedFields.get("invoiceId"))));
                        }
                        if (idRelatedFields.containsKey("parcelId")) {
                            criterias.add(Criteria.where(SubLotDocument.PARCEL_ID).is(new ObjectId(idRelatedFields.get("parcelId"))));
                        }
                    }
                    List<HkSubLotDocument> subLotDocuments = mongoGenericDao.findByCriteria(criterias, HkSubLotDocument.class);
                    if (CollectionUtils.isEmpty(subLotDocuments)) {
                        return true;
                    }
                    break;

            }
        }

        return false;
    }

    @Override
    public List<HkSubEntityExceptionDocument> retrieveValueExceptionsByCriteria(Set<Long> dependentOnFieldId, Long companyId) {
        List<Criteria> criterias = new LinkedList<>();
        if (dependentOnFieldId != null) {
            criterias.add(Criteria.where(SubEntityExceptionFields.FIELD_ID).in(dependentOnFieldId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(FRANCHISE).is(companyId));
        }
        criterias.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));

        return mongoGenericDao.findByCriteria(criterias, HkSubEntityExceptionDocument.class
        );

    }

    private List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null && !companyId.equals(0l)) {
            companyIds.add(companyId);
        }
        return companyIds;
    }

    @Override
    public Map<Long, String> mapOfFieldidAndDbFieldName(Long companyId) {
        Map<Long, String> mapOfFieldIdAndDbField = null;
        List<HkFieldDocument> fieldList;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
        if (companyId != null) {
            criterias.add(Criteria.where(FRANCHISE).in(getCompnies(companyId)));

        }
        fieldList = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class
        );
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfFieldIdAndDbField = new HashMap<>();
            for (HkFieldDocument field : fieldList) {
                mapOfFieldIdAndDbField.put(field.getId(), field.getDbFieldName());
            }
        }
        return mapOfFieldIdAndDbField;
    }

    @Override
    public List<HkFeatureFieldPermissionDocument> retrieveFeatureFieldPermissionsByFieldIds(List<Long> fieldIds) {
        List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
        if (!CollectionUtils.isEmpty(fieldIds)) {
            criterias.add(Criteria.where(FeatureFieldPermissionFields.FIELD_ID).in(fieldIds));
            featureFieldPermissionDocuments = mongoGenericDao.findByCriteria(criterias, HkFeatureFieldPermissionDocument.class);
        }
        return featureFieldPermissionDocuments;
    }

    @Override
    public void copyFeatureFieldPermission(Long sourceFranchise, Long destinationFranchise) {
        List<HkFeatureFieldPermissionDocument> oldFieldPermissions = null;
        List<Criteria> criterias = new ArrayList<>();
        if (sourceFranchise != null && destinationFranchise != null) {
            criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
            criterias.add(Criteria.where(FeatureFieldPermissionFields.FRANCHISE).is(sourceFranchise));
            oldFieldPermissions = mongoGenericDao.findByCriteria(criterias, HkFeatureFieldPermissionDocument.class);
        }

        if (!CollectionUtils.isEmpty(oldFieldPermissions)) {
            for (HkFeatureFieldPermissionDocument hkFeatureFieldPermissionEntity : oldFieldPermissions) {
                try {
                    HkFeatureFieldPermissionDocument permissionEntity = hkFeatureFieldPermissionEntity.clone();
                    String oldId = hkFeatureFieldPermissionEntity.getId();
                    String[] split = oldId.split("-");
                    split[0] = destinationFranchise.toString();
                    String newId = "";
                    for (int i = 0; i < split.length; i++) {
                        if (newId.equals("")) {
                            newId = newId + split[i];
                        } else {
                            newId = newId + "-" + split[i];
                        }
                    }
                    permissionEntity.setId(newId);
                    permissionEntity.setFranchise(destinationFranchise);
                    permissionEntity.setLastModifiedOn(new Date());
                    mongoGenericDao.update(permissionEntity);

                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(HkCustomFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public List<HkFeatureFieldPermissionDocument> retrieveFeatureFieldPermissionsByDesignationsFeatureAndSection(List<Long> designationIds, List<Long> featureIds, String sectionCode, Long franchise) {
        List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = null;
        List<Criteria> criterias = new ArrayList<>();
        if (franchise != null && !CollectionUtils.isEmpty(designationIds) && !CollectionUtils.isEmpty(featureIds)) {
            criterias.add(Criteria.where(HkFieldEntityField.IS_ARCHIVE).is(false));
            criterias.add(Criteria.where(FeatureFieldPermissionFields.DESIGNATION).in(designationIds));
            if (StringUtils.hasText(sectionCode)) {
                criterias.add(Criteria.where(FeatureFieldPermissionFields.SECTION_CODE).is(sectionCode));
            }
            criterias.add(Criteria.where(FeatureFieldPermissionFields.FEATURE).in(featureIds));
            criterias.add(Criteria.where(FeatureFieldPermissionFields.FRANCHISE).is(franchise));
            featureFieldPermissionDocuments = mongoGenericDao.findByCriteria(criterias, HkFeatureFieldPermissionDocument.class);
        }
        return featureFieldPermissionDocuments;
    }

    @Override
    public HkReferenceRateDocument retrieveActiveCurrencyRateByCode(String currencyCode) {
        if (StringUtils.hasText(currencyCode)) {
            List<Criteria> criterias = new ArrayList<>();
            criterias.add(Criteria.where(HkCurrencyReferenceRateFields.IS_ACTIVE).is(Boolean.TRUE));
            criterias.add(Criteria.where(HkCurrencyReferenceRateFields.CURRENCY).is(currencyCode));
            return (HkReferenceRateDocument) mongoGenericDao.findOneByCriteria(criterias, HkReferenceRateDocument.class);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> retrieveRecipientNames(List<String> recipientCodes) {
        Map<String, String> recipientCodeNameMap = null;
        final String SEPARATOR = ":";
        if (!CollectionUtils.isEmpty(recipientCodes)) {
            recipientCodeNameMap = new LinkedHashMap<>();
            List<Long> employeeIds = new LinkedList<>();
            List<Long> departmentIds = new LinkedList<>();
            List<Long> otherFranchisedepartmentIds = new LinkedList<>();
            List<Long> groupIds = new LinkedList<>();
            List<Long> activityIds = new LinkedList<>();
            List<Long> designationIds = new LinkedList<>();
            List<Long> franchiseIds = new LinkedList<>();

            for (String recipientCode : recipientCodes) {
                StringTokenizer tokenizer = new StringTokenizer(recipientCode, SEPARATOR);
                if (tokenizer.countTokens() == 2) {
                    Long id = new Long(tokenizer.nextToken());
                    String type = tokenizer.nextToken();
                    switch (type) {
                        case HkSystemConstantUtil.RecipientCodeType.EMPLOYEE:
                            employeeIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT:
                            otherFranchisedepartmentIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DEPARTMENT:
                            departmentIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.DESIGNATION:
                            designationIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.ACTIVITY:
                            activityIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.FRANCHISE:
                            franchiseIds.add(id);
                            break;
                        case HkSystemConstantUtil.RecipientCodeType.GROUP:
                            groupIds.add(id);
                            break;
                    }
                }
            }

            if (!CollectionUtils.isEmpty(employeeIds)) {
                Query query = new Query();
                query.fields().include(FIRST_NAME);
                query.fields().include(LAST_NAME);
                query.fields().include(USER_CODE);
                query.fields().include(ID);

                query.addCriteria(Criteria.where(ID).in(employeeIds));

                //Criterias
                query.with(new Sort(Sort.Direction.ASC, FIRST_NAME));

                List<SyncCenterUserDocument> users = mongoGenericDao.getMongoTemplate().find(query, SyncCenterUserDocument.class);
                if (!CollectionUtils.isEmpty(users)) {
                    for (SyncCenterUserDocument uMUser : users) {
                        StringBuilder name = new StringBuilder();
                        if (StringUtils.hasText(uMUser.getUserCode())) {
                            name.append(uMUser.getUserCode());
                            name.append(" - ");
                        }
                        name.append(uMUser.getFirstName());
                        if (StringUtils.hasText(uMUser.getLastName())) {
                            name.append(" ");
                            name.append(uMUser.getLastName());
                        }
                        StringBuilder key = new StringBuilder(uMUser.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE);
                        recipientCodeNameMap.put(key.toString(), name.toString());
                    }

                }
            }

            if (!CollectionUtils.isEmpty(departmentIds)) {
                Query departmentQuery = new Query();
                departmentQuery.fields().include(HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME);
                departmentQuery.fields().include(ID);

                //Criterias
                departmentQuery.addCriteria(Criteria.where(ID).in(departmentIds));
                departmentQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME));

                List<Criteria> criterias = new ArrayList<>();
                criterias.add(Criteria.where(ID).in(departmentIds));
                List<HkDepartmentDocument> departments = mongoGenericDao.getMongoTemplate().find(departmentQuery, HkDepartmentDocument.class);
//                    List<HkDepartmentDocument> departments = mongoGenericDao.findByCriteria(criterias, HkDepartmentDocument.class);

                if (!CollectionUtils.isEmpty(departments)) {
                    for (HkDepartmentDocument uMDepartment : departments) {
                        StringBuilder key = new StringBuilder(uMDepartment.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT);
                        recipientCodeNameMap.put(key.toString(), uMDepartment.getDeptName());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(otherFranchisedepartmentIds)) {
                Query otherFranDeQuery = new Query();
                otherFranDeQuery.fields().include(HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME);
                otherFranDeQuery.fields().include(ID);

                otherFranDeQuery.addCriteria(Criteria.where(ID).is(otherFranchisedepartmentIds));
                otherFranDeQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.HkDepartmentDocumentField.DEPT_NAME));

                List<HkDepartmentDocument> departments = mongoGenericDao.getMongoTemplate().find(otherFranDeQuery, HkDepartmentDocument.class);
                SyncCenterFranchiseDocument centerFranchiseDocument = mongoGenericDao.getMongoTemplate().findById(1, SyncCenterFranchiseDocument.class);
                if (centerFranchiseDocument != null) {
                    for (HkDepartmentDocument departmentDoc : departments) {
                        StringBuilder key = new StringBuilder(departmentDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.FRANCHISE_DEPARTMENT);
                        StringBuilder nameBuilder = new StringBuilder(departmentDoc.getDeptName());
                        nameBuilder.append(" - ");
                        nameBuilder.append(centerFranchiseDocument.getName());
                        recipientCodeNameMap.put(key.toString(), nameBuilder.toString());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(designationIds)) {

                Query desigQuery = new Query();
                desigQuery.addCriteria(Criteria.where(ID).in(designationIds));
                desigQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.HkDesignationDocumentField.DESIG_NAME));

                List<UmDesignationDocument> designations = mongoGenericDao.getMongoTemplate().find(desigQuery, UmDesignationDocument.class);
                if (!CollectionUtils.isEmpty(designations)) {
                    for (UmDesignationDocument desigDoc : designations) {
                        StringBuilder key = new StringBuilder(desigDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.DESIGNATION);
                        recipientCodeNameMap.put(key.toString(), desigDoc.getName());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(franchiseIds)) {

                Query franQuery = new Query();
                franQuery.addCriteria(Criteria.where(ID).in(franchiseIds));
                franQuery.with(new Sort(Sort.Direction.ASC, HkUserServiceImpl.UmComapnyDocumentField.COMPANY_NAME));

                List<UmCompanyDocument> companies = mongoGenericDao.getMongoTemplate().find(franQuery, UmCompanyDocument.class);
                if (!CollectionUtils.isEmpty(companies)) {
                    for (UmCompanyDocument companyDoc : companies) {
                        StringBuilder key = new StringBuilder(companyDoc.getId().toString());
                        key.append(SEPARATOR);
                        key.append(HkSystemConstantUtil.RecipientCodeType.FRANCHISE);
                        recipientCodeNameMap.put(key.toString(), companyDoc.getName());
                    }
                }
            }
//            }

        }
        return recipientCodeNameMap;
    }
}
