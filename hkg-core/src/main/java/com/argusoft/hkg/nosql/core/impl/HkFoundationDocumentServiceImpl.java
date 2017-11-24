/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.nosql.core.HkFoundationDocumentService;
import com.argusoft.hkg.nosql.model.HkValueExceptionDocument;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.sync.center.model.HkAssetDocument;
import com.argusoft.sync.center.model.HkCaratRangeDocument;
import com.argusoft.sync.center.model.HkCategoryDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkSystemConfigurationDocument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author akta
 */
@Service
public class HkFoundationDocumentServiceImpl implements HkFoundationDocumentService {

    @Autowired
    private MongoGenericDao mongoGenericDao;

    @Override
    public HkMasterDocument retrieveMaster(String masterCode, short userPrecedence, Long companyId) {
        List<Criteria> criterias = new LinkedList<>();
        criterias.add(where(MASTER.CODE).is(masterCode));
        criterias.add(where(MASTER.PRECEDENCE).gte(userPrecedence));
        HkMasterDocument hkMasterDocument = (HkMasterDocument) mongoGenericDao.findOneByCriteria(criterias, HkMasterDocument.class);
        hkMasterDocument.setHkValueEntityList(retrieveMasterValueByCode(hkMasterDocument.getCode()));
        return hkMasterDocument;
    }

    public static class MasterValueDocField {

        static final String CODE = "code";
        static final String VALUE_ID = "id";
        static final String VALUE_NAME = "valueName";
        public static final String SHORTCUT_CODE = "shortcutCode";
        public static final String COMPANY = "franchise";
        public static final String IS_ARCHIVED = "isArchive";
    }

    private static final class HkCaratDoc {

        public static final String ID = "id";
        public static final String MIN_VALUE = "minValue";
        public static final String MAX_VALUE = "maxValue";
        public static final String STATUS = "status";
        public static final String FRANCHISE = "franchise";

    }

    private static class Fields {

        public static final String PREFIX = "categoryPrefix";
        public static final String TYPE = "categoryType";
        public static final String FRANCHISE = "franchise";
    }

    private static final class Asset {

        public static final String COMPANY = "franchise";
        public static final String CATEGORY = "category.id";
        public static final String CATEGORY_OBJ = "category";
        public static final String SERIALNUMBER = "serialNumber";
        public static final String ASSET_TYPE = "assetType";
        public static final String ASSET_NAME = "assetName";
        public static final String REMAINING_UNITS = "remainingUnits";
        public static final String STATUS = "status";
        public static final String ID = "id";
    }

    private static final class FieldDocument {

        public static final String DB_FIELD_NAME = "dbFieldName";
        public static final String ID = "id";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String FEATURE = "feature";
    }

    public static class MASTER implements com.argusoft.hkg.core.util.Fields {

        public static final String NAME = "masterName";
        public static final String CODE = "code";
        public static final String VALUES = "hkValueEntityList";
        public static final String PRECEDENCE = "precedence";
        public static final String MASTER_TYPE = "masterType";
        public static final String COMPANY = "franchise";

        public static final String OFTEN_USED = "isOftenUsed";
        public static final String SHORTCUT_CODE = "shortcutCode";

    }

    public static class ValueException implements com.argusoft.hkg.core.util.Fields {

        public static final String INSTANCE_ID = "instanceId";
        public static final String FIELD_ID = "fieldId";
        public static final String MASTER_TYPE = "masterType";
        public static final String COMPANY = "franchise";
        public static final String SELECTED_VALUE_ID = "dependsOnValueList";

    }
    static final String IS_ARCHIVE = "isArchive";

    @Override
    public List<HkMasterValueDocument> retrieveMasterValueByCode(List<String> code) {
        if (!CollectionUtils.isEmpty(code)) {
            List<Criteria> listMasterCriteria = new ArrayList<>();
            listMasterCriteria.add(Criteria.where(MasterValueDocField.CODE).in(code));
            listMasterCriteria.add(Criteria.where(IS_ARCHIVE).is(false));
            return mongoGenericDao.findByCriteria(listMasterCriteria, HkMasterValueDocument.class);
        }
        return null;
    }

    public List<HkMasterValueDocument> retrieveMasterValueByCode(String code) {
        List<Criteria> listMasterCriteria = new ArrayList<>();
        listMasterCriteria.add(Criteria.where(MasterValueDocField.CODE).is(code));
        return mongoGenericDao.findByCriteria(listMasterCriteria, HkMasterValueDocument.class);
    }

    @Override
    public List<HkSystemConfigurationDocument> retrieveSystemConfigurationByFranchise(Long franchise) {
        //franchise is ignored as this is center side code
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where(IS_ARCHIVE).is(false));
        return mongoGenericDao.findByCriteria(criteria, HkSystemConfigurationDocument.class);
    }

    @Override
    public HkMasterValueDocument retrieveValueEntityById(Long valueId) {
        HkMasterValueDocument document = (HkMasterValueDocument) mongoGenericDao.retrieveById(valueId, HkMasterValueDocument.class);
        return document;
    }

    @Override
    public List<HkMasterValueDocument> retrieveValueEntities(List<Long> valueIds) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where(MasterValueDocField.VALUE_ID).in(valueIds));
        return mongoGenericDao.findByCriteria(criterias, HkMasterValueDocument.class);
    }

    @Override
    public List<HkCaratRangeDocument> retrieveCaratRangeByFranchiseAndStatus(Long franchise, List<String> status, List<Long> caratId) {
        List<Criteria> criteria = new LinkedList<>();

        criteria.add(Criteria.where(HkCaratDoc.FRANCHISE).is(franchise));
        if (!CollectionUtils.isEmpty(status)) {
            criteria.add(Criteria.where(HkCaratDoc.STATUS).in(status));
        }
        if (!CollectionUtils.isEmpty(caratId)) {
            criteria.add(Criteria.where(HkCaratDoc.ID).in(caratId));
        }

        return mongoGenericDao.findByCriteria(criteria, HkCaratRangeDocument.class);
    }

    @Override
    public HkCategoryDocument retrieveAssetCategoryByPrefix(String categoryPrefix, Long franchise, CategoryType categoryType) {
        List<Criteria> criteria = new LinkedList<>();
        criteria.add(Criteria.where(Fields.FRANCHISE).is(franchise));
        if (StringUtils.hasText(categoryPrefix)) {
            criteria.add(Criteria.where(Fields.PREFIX).is(categoryPrefix));
        }
        criteria.add(Criteria.where(Fields.TYPE).is(categoryType));
        return (HkCategoryDocument) mongoGenericDao.findOneByCriteria(criteria, HkCategoryDocument.class);
    }

    @Override
    public List<HkAssetDocument> retrieveAssets(Long category, Long companyId, boolean forIssue) {
        List<Criteria> criteria = new LinkedList<>();
        if (companyId != null) {
            criteria.add(Criteria.where(Asset.COMPANY).is(companyId));
        }
        if (forIssue) {
            criteria.add(new Criteria().orOperator(new Criteria().andOperator(
                    Criteria.where(Asset.ASSET_TYPE).is(HkSystemConstantUtil.ASSET_TYPE.MANAGED),
                    Criteria.where(Asset.STATUS).is(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE)),
                    new Criteria().andOperator(
                            Criteria.where(Asset.ASSET_TYPE).is(HkSystemConstantUtil.ASSET_TYPE.NON_MANAGED),
                            Criteria.where(Asset.REMAINING_UNITS).gt(0))));
        }
        Criteria.where(Asset.ASSET_TYPE).is(HkSystemConstantUtil.ASSET_TYPE.NON_MANAGED);
        return mongoGenericDao.findByCriteria(criteria, HkAssetDocument.class);
    }

    @Override
    public List<HkMasterDocument> retrieveMasters(long companyId, short userPrecedence, String type, Boolean isArchive) {
        List<Criteria> criterias = new LinkedList<>();
        if (type != null) {
            criterias.add(Criteria.where(MASTER.MASTER_TYPE).is(type));
        }
        criterias.add(Criteria.where(MASTER.COMPANY).in(HkSystemFunctionUtil.getCompnies(companyId)));
        if (isArchive != null) {
            criterias.add(Criteria.where(MASTER.IS_ARCHIVED).is(isArchive));
        }
        criterias.add(Criteria.where(MASTER.PRECEDENCE).gte(userPrecedence));
        return mongoGenericDao.findByCriteria(criterias, HkMasterDocument.class);
    }

    @Override
    public List<HkMasterValueDocument> retrieveMasterValueByCodeForCustomMasters(String code, String searchQuery) {
        boolean isShortcutCode;
        Integer scode = null;
        try {
            scode = Integer.parseInt(searchQuery);
            // is an integer!
            isShortcutCode = true;
        } catch (NumberFormatException e) {
            // not an integer!
            isShortcutCode = false;
        }
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, MASTER.OFTEN_USED));
        query.with(new Sort(Sort.Direction.ASC, MASTER.SHORTCUT_CODE));
        query.addCriteria(Criteria.where(MasterValueDocField.CODE).is(code));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(false));
        if (!StringUtils.isEmpty(searchQuery)) {
            System.out.println("Is shortcut" + isShortcutCode);
            if (isShortcutCode) {
                query.addCriteria(Criteria.where(MasterValueDocField.SHORTCUT_CODE).is(scode));
            } else {
                query.addCriteria(Criteria.where(MasterValueDocField.VALUE_NAME).regex(searchQuery, "i"));
            }
        }

        return mongoGenericDao.getMongoTemplate().find(query, HkMasterValueDocument.class);
    }

    @Override
    @MongoTransaction
    public void saveValueExceptions(List<HkValueExceptionDocument> hkValueExceptionDocuments) {
        if (!CollectionUtils.isEmpty(hkValueExceptionDocuments)) {
            for (HkValueExceptionDocument hkValueExceptionDocument : hkValueExceptionDocuments) {
                mongoGenericDao.update(hkValueExceptionDocument);
            }

        }
    }

    public List<HkValueExceptionDocument> retrieveValueExceptions(Long instanceId, Long companyId) {
        List<Criteria> criterias = new LinkedList<>();
        if (instanceId != null) {
            criterias.add(Criteria.where(ValueException.INSTANCE_ID).is(instanceId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(MASTER.COMPANY).is(companyId));
        }
        criterias.add(Criteria.where(MASTER.IS_ARCHIVED).is(Boolean.FALSE));
        return mongoGenericDao.findByCriteria(criterias, HkValueExceptionDocument.class);

    }

    @Override
    public List<HkValueExceptionDocument> retrieveValueExceptionsByCriteriaWithValue(Long dependentOnFieldId, Long companyId, Long selectedValueId) {

        List<Criteria> criterias = new LinkedList<>();
        if (dependentOnFieldId != null) {
            criterias.add(Criteria.where(ValueException.FIELD_ID).is(dependentOnFieldId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(MASTER.COMPANY).is(companyId));
        }
        if (selectedValueId != null) {
            criterias.add(Criteria.where(ValueException.SELECTED_VALUE_ID).in(selectedValueId));
        }

        criterias.add(Criteria.where(MASTER.IS_ARCHIVED).is(Boolean.FALSE));
        return mongoGenericDao.findByCriteria(criterias, HkValueExceptionDocument.class);
    }

    @Override
    public List<HkValueExceptionDocument> retrieveValueExceptionsByCriteria(Set<Long> dependentOnFieldId, Long companyId) {
        List<Criteria> criterias = new LinkedList<>();
        if (dependentOnFieldId != null) {
            criterias.add(Criteria.where(ValueException.FIELD_ID).in(dependentOnFieldId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(MASTER.COMPANY).is(companyId));
        }

        criterias.add(Criteria.where(MASTER.IS_ARCHIVED).is(Boolean.FALSE));
        return mongoGenericDao.findByCriteria(criterias, HkValueExceptionDocument.class);
    }

    @Override
    public Map<Long, HkMasterValueDocument> retrieveValueEntititiesByCriteria(Long companyId, List<Long> valueId
    ) {
        Map<Long, HkMasterValueDocument> valueEntityByCriteriaMap = null;
        List<Criteria> criterias = new LinkedList<>();
        if (!CollectionUtils.isEmpty(valueId)) {
            criterias.add(Criteria.where(MasterValueDocField.VALUE_ID).in(valueId));
        }
        if (companyId != null) {
            criterias.add(Criteria.where(MasterValueDocField.COMPANY).is(companyId));
        }
        criterias.add(Criteria.where(MasterValueDocField.IS_ARCHIVED).is(Boolean.FALSE));

        List<HkMasterValueDocument> valueDocList = mongoGenericDao.findByCriteria(criterias, HkMasterValueDocument.class);
        if (!CollectionUtils.isEmpty(valueDocList)) {
            valueEntityByCriteriaMap = new HashMap<>();
            for (HkMasterValueDocument valueDoc : valueDocList) {
                valueEntityByCriteriaMap.put(valueDoc.getId(), valueDoc);
            }

        }
        return valueEntityByCriteriaMap;
    }

    @Override
    public Map<Long, HkMasterValueDocument> retrieveMapOfIdAndValueEntity(Long companyId
    ) {
        Map<Long, HkMasterValueDocument> map = new HashMap<>();
        List<Criteria> criterias = new LinkedList<>();
        if (companyId != null) {
            criterias.add(Criteria.where(MasterValueDocField.COMPANY).is(companyId));
        }
        List<HkMasterValueDocument> hkValueEntitys = mongoGenericDao.findByCriteria(criterias, HkMasterValueDocument.class);
        if (!CollectionUtils.isEmpty(hkValueEntitys)) {
            for (HkMasterValueDocument hkValueEntity : hkValueEntitys) {
                map.put(hkValueEntity.getId(), hkValueEntity);
            }
        }
        return map;
    }

    @Override
    public Map<String, List<HkMasterValueDocument>> mapOfKeyIdWithValueEntities(Long companyId
    ) {
        Map<String, List<HkMasterValueDocument>> map = new HashMap<>();
        List<Criteria> criterias = new LinkedList<>();
        if (companyId != null) {
            criterias.add(Criteria.where(MasterValueDocField.COMPANY).is(companyId));
        }
        List<HkMasterValueDocument> hkValueEntitys = mongoGenericDao.findByCriteria(criterias, HkMasterValueDocument.class);
        if (!CollectionUtils.isEmpty(hkValueEntitys)) {
            List<HkMasterValueDocument> valEntities = null;
            for (HkMasterValueDocument hkValueEntity : hkValueEntitys) {
                if (map.containsKey(hkValueEntity.getCode())) {
                    valEntities = map.get(hkValueEntity.getCode());

                } else {
                    valEntities = new ArrayList<>();
                }
                valEntities.add(hkValueEntity);
                map.put(hkValueEntity.getCode(), valEntities);
            }
        }
        return map;
    }

    @Override
    public Map< String, String> retrieveCustomfieldMasterMappingForCalc(List<Long> featureList) {
        LinkedList<Criteria> criterias = new LinkedList();
        Criteria criteria = new Criteria().andOperator(where(FieldDocument.FEATURE).in(featureList), where(IS_ARCHIVE).is(false));
        criterias.add(criteria);
        List<HkFieldDocument> fieldList = mongoGenericDao.findByCriteria(criterias, HkFieldDocument.class);
        if (!CollectionUtils.isEmpty(fieldList)) {
            Map<String, String> fieldMap = new HashMap<>();
            for (HkFieldDocument field : fieldList) {
                fieldMap.put(field.getDbFieldName(), field.getId().toString());
            }
            return fieldMap;
        }
        return null;
    }
}
