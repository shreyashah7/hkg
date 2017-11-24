/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkFranchiseSetupEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.model.HkSubFormFieldEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.core.util.MasterType;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation class for HkFieldService.
 *
 * @author Mital
 */
@Service
@Transactional
public class HkFieldServiceImpl implements HkFieldService {

    @Autowired
    private CommonDAO commonDAO;
    @Autowired
    private HkFoundationService hkFoundationService;
    @Autowired
    HkCustomFieldService customfieldService;
    @Autowired
    UMRoleService roleService;
    @Autowired
    UMFeatureService featureService;
    @Autowired
    private MongoGenericDao mongoGenericDao;

    private static final String IS_ARCHIVE = "isArchive";
    private static final String FRANCHISE = "franchise";
    public static final String UIFIELDNAME = "uiFieldName";
    public static final String IS_ACTIVE = "isActive";
    public static final String COMPANY = "company";

    public static class UMFeatureDetail {

        private static final String MENU_LABEL = "menuLabel";
        private static final String MENU_TYPE = "menuType";
        private static final String FEATURE_ID = "feature";
    }

    public static class HkFranchiseSetupDetails {

        private static final String STATUS = "status";
        private static final String FRANCHISE = "hkFranchiseSetupEntityPK.franchise";
        private static final String SETUP_ITEM = "hkFranchiseSetupEntityPK.setupItem";
    }

    /**
     * Entity used in naming convention here just for convenience else don't
     * append "Entity" in such names.
     */
    static class HkFieldEntityField {

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
        public static final String LABEL = "fieldLabel";
        public static final String CREATED_ON = "createdOn";

    }

    private static class HkStaticServiceFieldEntity {

        public static final String DESIGNATION = "designation";
    }

    private static class HkSubFormFieldEntityField {

        public static final String parentField = "parentField";
        public static final String parentFieldId = "parentField.id";
        public static final String parentFieldFeature = "parentField.feature";
        public static final String parentFieldIsArchive = "parentField.isArchive";
        public static final String isArchive = "isArchive";
        public static final String id = "id";
        public static final String subFieldName = "subFieldName";
        public static final String subFieldLabel = "subFieldLabel";
        public static final String STATUS = "status";
        public static final String FRANCHISE = "franchise";
        public static final String IS_ARCHIVE = "isArchive";
    }

    private static class HkFeatureSectionField {

        public static final String FEATURE = "feature";
        public static final String SECTION = "section";
        public static final String SECTION_ID = "sectionId";
    }

    private static class HkFeatureFieldPermissionField {

        public static final String FEATURE = "feature";
        public static final String SEARCH_FLAG = "searchFlag";
        public static final String DESIGNATION = "designation";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String PARENT_VIEW_FLAG = "parentViewFlag";
        public static final String EDITABLE_FLAG = "editableFlag";
        public static final String READ_ONLY_FLAG = "readonlyFlag";
        public static final String FIELD = "hkFeatureFieldPermissionEntityPK.field";
    }

    @Override
    public void createSection(HkSectionEntity sectionEntity, Set<Long> featureSet) {
        commonDAO.save(sectionEntity);

        if (!CollectionUtils.isEmpty(featureSet)) {
            List<HkFeatureSectionEntity> featureSectionList = new ArrayList<>();
            for (Long featureId : featureSet) {
                HkFeatureSectionEntity featureSectionEntity = new HkFeatureSectionEntity();
                featureSectionEntity.setCreatedOn(sectionEntity.getCreatedOn());
                featureSectionEntity.setFeature(featureId);
                featureSectionEntity.setSection(sectionEntity);
                featureSectionEntity.setIsViewOnly(false);
                featureSectionEntity.setIsArchive(false);
                featureSectionList.add(featureSectionEntity);
            }
            commonDAO.save(featureSectionList.toArray());
        }
    }

    @Override
    public HkFieldEntity retrieveCustomFieldByFieldId(Long fieldId) {
        return commonDAO.find(HkFieldEntity.class, fieldId);
    }

    @Override
    public void removeFieldById(Long fieldId) {
        // For archiving the value from HkFieldEntity table
        HkFieldEntity hkFieldEntity = commonDAO.find(HkFieldEntity.class, fieldId);
        hkFieldEntity.setIsArchive(true);
        hkFieldEntity.setStatus(HkFieldEntityField.INACTIVE_STATUS);
        commonDAO.save(hkFieldEntity);

        // archiving the key stored in master table when customfield is removed
        HkMasterEntity masterEntity = commonDAO.find(HkMasterEntity.class, fieldId.toString());
        if (masterEntity != null) {
            masterEntity.setIsArchive(true);
            commonDAO.save(masterEntity);
        }
        // archiving the entries in valueMaster table of corresponding customfield
        List<HkValueEntity> hkValueEntitys = hkFoundationService.retrieveMasterValuesOfSameKeyCodeByCustomFieldId(fieldId);
        List<HkValueEntity> newhkValueEntitys = null;
        if (!CollectionUtils.isEmpty(hkValueEntitys)) {
            newhkValueEntitys = new ArrayList<>();
            for (HkValueEntity hkValueEntity : hkValueEntitys) {
                hkValueEntity.setIsActive(Boolean.FALSE);
                hkValueEntity.setIsArchive(Boolean.TRUE);
                newhkValueEntitys.add(hkValueEntity);
            }
            commonDAO.save(newhkValueEntitys.toArray());
        }

        List<HkFeatureFieldPermissionDocument> featureFieldPermissionDocuments = customfieldService.retrieveFeatureFieldPermissionsByFieldIds(Arrays.asList(fieldId));
        if (!CollectionUtils.isEmpty(featureFieldPermissionDocuments)) {
            for (HkFeatureFieldPermissionDocument hkfeatureFieldPermission : featureFieldPermissionDocuments) {
                hkfeatureFieldPermission.setIsArchive(Boolean.TRUE);
                mongoGenericDao.update(hkfeatureFieldPermission);
            }

        }
    }

    @Override
    public void createSections(Map<Long, List<HkSectionEntity>> map) {
        List<HkSectionEntity> hkSectionEntitys = new ArrayList();
        List<Long> featureIds = new ArrayList<>();
        for (Map.Entry<Long, List<HkSectionEntity>> entry : map.entrySet()) {
//            List<HkSectionEntity> sectionEntitys = new ArrayList<>();
            Long id = entry.getKey();
            hkSectionEntitys.addAll(entry.getValue());

        }
        commonDAO.saveAll(hkSectionEntitys);
        for (Map.Entry<Long, List<HkSectionEntity>> entry : map.entrySet()) {
            Long long1 = entry.getKey();
            List<HkSectionEntity> sectionEntityList = entry.getValue();
            List<HkFeatureSectionEntity> featureSectionList = new ArrayList<>();
            for (HkSectionEntity hkSectionEntity : sectionEntityList) {
                HkFeatureSectionEntity featureSectionEntity = new HkFeatureSectionEntity();
                featureSectionEntity.setCreatedOn(hkSectionEntity.getCreatedOn());
                featureSectionEntity.setFeature(long1);
                featureSectionEntity.setSection(hkSectionEntity);
                featureSectionEntity.setIsViewOnly(false);
                featureSectionEntity.setIsArchive(false);
                featureSectionList.add(featureSectionEntity);
            }
            commonDAO.save(featureSectionList.toArray());
        }
    }

    @Override
    public void updateSection(HkSectionEntity sectionEntity, Set<Long> allFeatureSet) {
        List<HkFeatureSectionEntity> featureSectionListToUpdate = new ArrayList<>();
        List<HkFeatureSectionEntity> featureSectionList = this.retrieveFeatureSectionEntities(null, sectionEntity.getId(), null);

        //  Check for all existing features for this section
        if (!CollectionUtils.isEmpty(featureSectionList)) {
            for (HkFeatureSectionEntity featureSectionEntity : featureSectionList) {
                //  if new feature set doesn't contain this feature, it should be removed
                if (allFeatureSet == null || !allFeatureSet.contains(featureSectionEntity.getFeature())) {
                    featureSectionEntity.setIsArchive(true);
                    featureSectionListToUpdate.add(featureSectionEntity);
                } else {
                    //  if it contains, it means this already exists so no need to create it, just remove it from the set.
                    allFeatureSet.remove(featureSectionEntity.getFeature());
                    //  make it active in case it's already deleted before
                    featureSectionEntity.setIsArchive(false);
                    featureSectionListToUpdate.add(featureSectionEntity);
                }
            }
        }

        if (!CollectionUtils.isEmpty(allFeatureSet)) {
            for (Long featureId : allFeatureSet) {
                HkFeatureSectionEntity featureSectionEntity = new HkFeatureSectionEntity();
                featureSectionEntity.setCreatedOn(sectionEntity.getCreatedOn());
                featureSectionEntity.setFeature(featureId);
                featureSectionEntity.setSection(sectionEntity);
                featureSectionEntity.setIsViewOnly(false);
                featureSectionEntity.setIsArchive(false);
                featureSectionListToUpdate.add(featureSectionEntity);
            }
        }

        if (!CollectionUtils.isEmpty(featureSectionListToUpdate)) {
            commonDAO.save(featureSectionListToUpdate.toArray());
        }

        commonDAO.save(sectionEntity);
    }

    @Override
    public void updateSections(List<HkSectionEntity> sectionEntitys) {
        if (!CollectionUtils.isEmpty(sectionEntitys)) {
            commonDAO.saveAll(sectionEntitys);
        }
    }

    @Override
    public void removeSection(HkSectionEntity sectionEntity) {
        sectionEntity.setIsArchive(true);
        commonDAO.save(sectionEntity);

        //  Archive feature section relationships
        List<HkFeatureSectionEntity> featureSectionList = this.retrieveFeatureSectionEntities(null, sectionEntity.getId(), false);
        if (!CollectionUtils.isEmpty(featureSectionList)) {
            for (HkFeatureSectionEntity featureSectionEntity : featureSectionList) {
                featureSectionEntity.setIsArchive(true);
            }
            commonDAO.save(featureSectionList.toArray());
        }
    }

    @Override
    public void removeSections(List<HkSectionEntity> sectionEntitys) {
        if (!CollectionUtils.isEmpty(sectionEntitys)) {
            List<Long> sectionIds = new ArrayList<>();
            for (HkSectionEntity sectionEntity : sectionEntitys) {
                sectionEntity.setIsArchive(true);
                sectionIds.add(sectionEntity.getId());
            }
            commonDAO.saveAll(sectionEntitys);

            //  Archive feature section relationships
            List<HkFeatureSectionEntity> featureSectionList = this.retrieveFeatureSectionEntities(null, sectionIds, false);
            if (!CollectionUtils.isEmpty(featureSectionList)) {
                for (HkFeatureSectionEntity featureSectionEntity : featureSectionList) {
                    featureSectionEntity.setIsArchive(true);
                }
                commonDAO.save(featureSectionList.toArray());
            }
        }

    }

    @Override
    public void removeSection(Long sectionId) {
        HkSectionEntity sectionEntity = commonDAO.find(HkSectionEntity.class, sectionId);
        this.removeSection(sectionEntity);
    }

    private List<HkFeatureSectionEntity> retrieveFeatureSectionEntities(Long featureId, Long sectionId, Boolean archiveStatus) {
        Search search = new Search(HkFeatureSectionEntity.class);

        if (featureId != null) {
            search.addFilterEqual(HkFeatureSectionField.FEATURE, featureId);
        }
        if (sectionId != null) {
            search.addFilterEqual(HkFeatureSectionField.SECTION_ID, sectionId);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        return commonDAO.search(search);
    }

    private List<HkFeatureSectionEntity> retrieveFeatureSectionEntities(List<Long> featureIds, List<Long> sectionIds, Boolean archiveStatus) {
        Search search = new Search(HkFeatureSectionEntity.class);

        if (featureIds != null) {
            search.addFilterIn(HkFeatureSectionField.FEATURE, featureIds);
        }
        if (sectionIds != null) {
            search.addFilterIn(HkFeatureSectionField.SECTION_ID, sectionIds);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }

        return commonDAO.search(search);
    }

    @Override
    public List<HkFeatureSectionEntity> retrieveFeatureSectionEntitiesByFeatureIds(List<Long> featureIds) {
        Search search = new Search(HkFeatureSectionEntity.class);
        if (!CollectionUtils.isEmpty(featureIds)) {
            search.addFilterIn(HkFeatureSectionField.FEATURE, featureIds);
        }
        search.addFilterEqual(IS_ARCHIVE, false);
        return commonDAO.search(search);
    }

    @Override
    public HkSectionEntity retrieveSectionById(Long sectionId, boolean includeFeatures) {
        HkSectionEntity sectionEntity = commonDAO.find(HkSectionEntity.class, sectionId);

        if (includeFeatures) {
            List<HkFeatureSectionEntity> featureSectionList = this.retrieveFeatureSectionEntities(null, sectionId, false);
            sectionEntity.setHkFeatureSectionRelationEntitySet(new HashSet<>(featureSectionList));
        }

        return sectionEntity;
    }

    @Override
    public Map<HkSectionEntity, List<HkFieldEntity>> retrieveSectionsByFeatureId(Long featureId, Long companyId, Boolean isCustomField, boolean setValuePattern) {
        Map<HkSectionEntity, List<HkFieldEntity>> sectionFieldMap = new HashMap<>();

        Set<Long> sectionIds = new HashSet<>();
        //  Get the fields first and set the id of sections.
        Search search = new Search(HkFieldEntity.class);
        search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        if (isCustomField != null) {
            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, isCustomField);
        }
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addSortAsc(HkFieldEntityField.SEQ_NO);
        if (companyId != null) {
            search.addFilterIn(FRANCHISE, companyId, 0);
        }
        List<HkFieldEntity> fieldList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (HkFieldEntity field : fieldList) {
                List<HkFieldEntity> sectionFieldsSet = sectionFieldMap.get(field.getSection());
                if (sectionFieldsSet == null) {
                    sectionFieldsSet = new ArrayList<>();
                    sectionFieldMap.put(field.getSection(), sectionFieldsSet);
                }
                sectionFieldsSet.add(field);
                if (field.getSection() != null) {
                    sectionIds.add(field.getSection().getId());
                }
            }
        }

        //  Now fetch the sections of which, fields may not exist but section would be there.
        search = new Search(HkFeatureSectionEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(HkFeatureSectionField.FEATURE, featureId);
        if (!CollectionUtils.isEmpty(sectionIds)) {
            search.addFilterNotIn(HkFeatureSectionField.SECTION_ID, sectionIds);
        }
        search.addFetch(HkFeatureSectionField.SECTION);
        List<HkFeatureSectionEntity> featureSectionList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(featureSectionList)) {
            for (HkFeatureSectionEntity featureSection : featureSectionList) {
                sectionFieldMap.put(featureSection.getSection(), null);
            }
        }

        if (setValuePattern && !CollectionUtils.isEmpty(sectionFieldMap.keySet())) {
            for (HkSectionEntity section : sectionFieldMap.keySet()) {
                if (!CollectionUtils.isEmpty(sectionFieldMap.get(section))) {
                    this.fillValuePattern(sectionFieldMap.get(section));
                }
            }
        }

        return sectionFieldMap;
    }

    @Override
    public Map<Long, String> retrieveMapOfFieldIdAndValueMasterDetailAssociated() {
        Map<Long, String> mapOfFieldIdWithValues = null;
        Map<String, HkMasterEntity> masterEntityMap = new HashMap<>();
        List<String> masterCodes = new ArrayList<>();
        Search search = new Search(HkFieldEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addSortAsc(HkFieldEntityField.SEQ_NO);
        List<HkFieldEntity> fieldList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfFieldIdWithValues = new HashMap<>();
            for (HkFieldEntity field : fieldList) {
                masterCodes.add(field.getId() + "");
            }
            masterEntityMap = hkFoundationService.retrieveMaster(masterCodes, (short) 0);
            for (HkFieldEntity field : fieldList) {
                HkMasterEntity fieldMaster = masterEntityMap.get(field.getId() + "");
                if (fieldMaster != null && fieldMaster.getHkValueEntityList() != null) {
                    for (HkValueEntity masterValue : fieldMaster.getHkValueEntityList()) {
                        if (field.getFieldValues() == null) {
                            field.setFieldValues(masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                        } else {
                            field.setFieldValues(field.getFieldValues() + "," + masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                        }
                    }
                }
                if (field.getFieldValues() != null) {
                    mapOfFieldIdWithValues.put(field.getId(), field.getFieldValues());
                }
            }
        }
        return mapOfFieldIdWithValues;
    }

    @Override
    public Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> retrieveCompanyFeatureSectionsMap(List<Long> featureIds, List<Long> companyIds) {
        Map<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> companyAndFeatureSectionFieldMap = new HashMap<>();
        List<String> masterCodes = new ArrayList<>();
        Map<String, HkMasterEntity> masterEntityMap = null;

        Search search = new Search(HkFieldEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterIn(HkFieldEntityField.FEATURE, featureIds);
        search.addSortAsc(HkFieldEntityField.SEQ_NO);

        List<HkFieldEntity> fieldList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (HkFieldEntity field : fieldList) {
                masterCodes.add(field.getId() + "");

                Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> featureSectionMap = companyAndFeatureSectionFieldMap.get(field.getFranchise());
                if (featureSectionMap == null) {
                    featureSectionMap = new HashMap<>();
                    companyAndFeatureSectionFieldMap.put(field.getFranchise(), featureSectionMap);
                }
                Map<HkSectionEntity, List<HkFieldEntity>> sectionMap = featureSectionMap.get(field.getFeature());
                if (sectionMap == null) {
                    sectionMap = new HashMap<>();
                    featureSectionMap.put(field.getFeature(), sectionMap);
                }
                List<HkFieldEntity> hkFieldEntitys = sectionMap.get(field.getSection());
                if (hkFieldEntitys == null) {
                    hkFieldEntitys = new ArrayList<>();
                    sectionMap.put(field.getSection(), hkFieldEntitys);
                }
                hkFieldEntitys.add(field);
            }
            masterEntityMap = hkFoundationService.retrieveMaster(masterCodes, (short) 0);
        }

        if (!CollectionUtils.isEmpty(companyAndFeatureSectionFieldMap.keySet()) && masterEntityMap != null) {
            for (Map.Entry<Long, Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>>> entrySet : companyAndFeatureSectionFieldMap.entrySet()) {
                Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> featureSectionMap = entrySet.getValue();
                for (Map.Entry<Long, Map<HkSectionEntity, List<HkFieldEntity>>> entrySet1 : featureSectionMap.entrySet()) {
                    Map<HkSectionEntity, List<HkFieldEntity>> sectionMap = entrySet1.getValue();
                    for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entrySet2 : sectionMap.entrySet()) {
                        List<HkFieldEntity> hkFieldEntitys = entrySet2.getValue();
                        for (HkFieldEntity hkFieldEntity : hkFieldEntitys) {
                            HkMasterEntity fieldMaster = masterEntityMap.get(hkFieldEntity.getId() + "");
                            if (fieldMaster != null && fieldMaster.getHkValueEntityList() != null) {
                                for (HkValueEntity masterValue : fieldMaster.getHkValueEntityList()) {
                                    if (hkFieldEntity.getFieldValues() == null) {
                                        hkFieldEntity.setFieldValues(masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                                    } else {
                                        hkFieldEntity.setFieldValues(hkFieldEntity.getFieldValues() + "," + masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> companyOSectionMap = companyAndFeatureSectionFieldMap.get(0l);
        if (!CollectionUtils.isEmpty(companyOSectionMap)) {
            if (!CollectionUtils.isEmpty(companyIds)) {
                for (Long companyId : companyIds) {
                    Map<Long, Map<HkSectionEntity, List<HkFieldEntity>>> featureSectionMap = companyAndFeatureSectionFieldMap.get(companyId);
                    if (featureSectionMap == null) {
                        companyAndFeatureSectionFieldMap.put(companyId, companyOSectionMap);
                    } else {
                        for (Map.Entry<Long, Map<HkSectionEntity, List<HkFieldEntity>>> entrySet : companyOSectionMap.entrySet()) {
                            Long key = entrySet.getKey();
                            Map<HkSectionEntity, List<HkFieldEntity>> company0SectionMap = entrySet.getValue();
                            Map<HkSectionEntity, List<HkFieldEntity>> sectionMap = featureSectionMap.get(key);
                            if (sectionMap == null) {
                                sectionMap = company0SectionMap;
                                featureSectionMap.put(key, sectionMap);
                            } else {
                                for (Map.Entry<HkSectionEntity, List<HkFieldEntity>> entrySet1 : company0SectionMap.entrySet()) {
                                    HkSectionEntity key1 = entrySet1.getKey();
                                    List<HkFieldEntity> comapny0HkFieldEntitys = entrySet1.getValue();
                                    List<HkFieldEntity> hkFieldEntitys = sectionMap.get(key1);
                                    if (hkFieldEntitys == null) {
                                        hkFieldEntitys = comapny0HkFieldEntitys;
                                        sectionMap.put(key1, hkFieldEntitys);
                                    } else {
                                        hkFieldEntitys.addAll(comapny0HkFieldEntitys);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return companyAndFeatureSectionFieldMap;
    }

    public List<UMFeature> retrieveAllFeaturesByListOfMenuType(List<String> listOfMenuTypes, Long companyId, Boolean isActive, String searchparam) {
        List<UMFeature> featureList = new ArrayList<>();
        Search search = new Search(UMFeature.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterEqual(IS_ACTIVE, isActive);
        if (companyId != null) {
            search.addFilterIn(COMPANY, getCompnies(companyId));
        }
        search.addFilterIn(UMFeatureDetail.MENU_TYPE, listOfMenuTypes);
        if (searchparam != null) {
            search.addFilterILike(UMFeatureDetail.MENU_LABEL, searchparam + "%");
        }
        featureList = commonDAO.search(search);
        return featureList;
    }

// The method edited by Shifa Salheen on 22 December 2014 as the search is now on the basis of field Id and it should return feature name as well as section name
    @Override
    public Map<Long, List<String>> searchFields(String searchStr, Map<Long, String> featureIdNameMap, Long companyId) {
        Search search;
        List<HkFieldEntity> fieldList = null;
        Map<Long, List<String>> finalMap = null;
        if (StringUtils.hasText(searchStr) && searchStr.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            if (searchStr.toUpperCase().contains(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE)) {
                searchStr = searchStr.substring(searchStr.toUpperCase().indexOf(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE) + HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE.length()).trim();
                List<UMFeature> featureList = this.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY, HkSystemConstantUtil.FeatureTypes.MENU), null, Boolean.TRUE, searchStr);
                List<Long> featureIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(featureList)) {
                    for (UMFeature feature : featureList) {
                        featureIds.add(feature.getId());
                    }
                }
                if (!CollectionUtils.isEmpty(featureList)) {
                    search = SearchFactory.getSearch(HkFieldEntity.class);
                    search.addFilterIn(UMFeatureDetail.FEATURE_ID, featureIds);
                    search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
                    fieldList = commonDAO.search(search);
                }
            } else {
                search = new Search(HkFieldEntity.class);
                search.addFilterILike(HkFieldEntityField.FIELD_LABEL, searchStr + "%");
                search.addFilterEqual(IS_ARCHIVE, false);
                search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
                search.addFilterEqual(HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
                fieldList = commonDAO.search(search);
            }
            if (!CollectionUtils.isEmpty(fieldList)) {
                finalMap = new HashMap<>();
                for (HkFieldEntity field : fieldList) {
                    List<String> mapFieldList = finalMap.get(field.getId());
                    if (mapFieldList == null) {
                        mapFieldList = new ArrayList<>();
                    }
                    mapFieldList.add(field.getFieldLabel() + "," + field.getComponentType()
                            + " , " + (field.getSection() == null ? HkSystemConstantUtil.DEFAULT_SECTION : field.getSection().getSectionName()) + " , " + featureIdNameMap.get(field.getFeature()));
                    finalMap.put(field.getId(), mapFieldList);
                }
            }
        }

        return finalMap;
    }

    @Override
    public Map<String, List<String>> searchSubEntityFields(String searchStr, Map<Long, String> featureIdNameMap, Long companyId) {
        Search search;
        List<HkSubFormFieldEntity> subFieldList = null;
        Map<String, List<String>> finalMap = null;
        if (StringUtils.hasText(searchStr) && searchStr.length() >= HkSystemConstantUtil.MIN_SEARCH_LENGTH) {
            if (searchStr.toUpperCase().contains(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE)) {
                searchStr = searchStr.substring(searchStr.toUpperCase().indexOf(HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE) + HkSystemConstantUtil.CUSTOMFIELD_SEARCH_CODE.FEATURE.length()).trim();
                List<UMFeature> featureList = this.retrieveAllFeaturesByListOfMenuType(Arrays.asList(HkSystemConstantUtil.FeatureTypes.ENTITY, HkSystemConstantUtil.FeatureTypes.MENU), null, Boolean.TRUE, searchStr);
                List<Long> featureIds = new ArrayList<>();
                if (!CollectionUtils.isEmpty(featureList)) {
                    for (UMFeature feature : featureList) {
                        featureIds.add(feature.getId());
                    }
                }
                if (!CollectionUtils.isEmpty(featureList)) {
                    search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
                    search.addFilterIn(HkSubFormFieldEntityField.parentFieldFeature, featureIds);
                    search.addFilterEqual(IS_ARCHIVE, false);
                    search.addFilterEqual(HkSubFormFieldEntityField.parentFieldIsArchive, false);
                    subFieldList = commonDAO.search(search);
                }
            } else {
                search = new Search(HkSubFormFieldEntity.class);
                search.addFilterILike(HkSubFormFieldEntityField.subFieldLabel, searchStr + "%");
                search.addFilterEqual(IS_ARCHIVE, false);
                search.addFilterEqual(HkSubFormFieldEntityField.parentFieldIsArchive, false);
                search.addFilterEqual(HkSubFormFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
                subFieldList = commonDAO.search(search);
            }
            if (!CollectionUtils.isEmpty(subFieldList)) {
                finalMap = new HashMap<>();
                for (HkSubFormFieldEntity field : subFieldList) {
                    List<String> mapFieldList = finalMap.get(field.getId().toString());
                    if (mapFieldList == null) {
                        mapFieldList = new ArrayList<>();
                    }
                    mapFieldList.add((field.getParentField().getFieldLabel()) + " , " + featureIdNameMap.get(field.getParentField().getFeature()));
                    finalMap.put(field.getParentField().getId() + "-" + field.getParentField().getFeature(), mapFieldList);
                }
            }
        }

        return finalMap;
    }
// Deprecated method

    @Override
    public Map<String, String> createUniqueKeyForField(List<HkFieldEntity> fieldList, Long companyId) {
        Map<String, String> uniqueDbFieldMap = null;
        if (!CollectionUtils.isEmpty(fieldList)) {
            uniqueDbFieldMap = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : fieldList) {
                String spaceRemovedLabel = hkFieldEntity.getFieldLabel().replaceAll(" ", "_").toLowerCase();
                String dotRemovedLabel = spaceRemovedLabel.replaceAll("\\.", "_").toLowerCase();
                hkFieldEntity.setDbFieldName(dotRemovedLabel);

                uniqueDbFieldMap.put(hkFieldEntity.getFieldLabel(), dotRemovedLabel);
            }
        }
        return uniqueDbFieldMap;
    }
// method added by Shifa on 23 December

    private Map<String, String> createUniqueKeyForField(HkFieldEntity hkFieldEntity, Long companyId) {
        Map<String, String> uniqueDbFieldMap = null;
        if (hkFieldEntity != null) {
            uniqueDbFieldMap = new HashMap<>();
            String spaceRemovedLabel = hkFieldEntity.getFieldLabel().replaceAll(" ", "_").toLowerCase();
            String dotRemovedLabel = spaceRemovedLabel.replaceAll("\\.", "_").toLowerCase();
            hkFieldEntity.setDbFieldName(dotRemovedLabel);
            String[] splitLabel = dotRemovedLabel.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
            List<String> dbList = this.retrieveDbFieldNamesWithoutSeperatorByCompanyId(companyId);
            String uniqueDbFieldName = uniqueDbFieldName(splitLabel[0], dbList);
            uniqueDbFieldMap.put(hkFieldEntity.getFieldLabel(), uniqueDbFieldName);

        }
        return uniqueDbFieldMap;

    }
// Recursive Method to find unique dbfield name

    private String uniqueDbFieldName(String dbFieldName, List<String> dbList) {
        int count = 0;

        if (!CollectionUtils.isEmpty(dbList)) {
            for (String dbName : dbList) {
                if (dbName.equals(dbFieldName)) {
                    count++;
                }
            }
        }
        String newDbFieldName = dbFieldName;
        if (count > 0) {
            newDbFieldName = dbFieldName + count;
            return uniqueDbFieldName(newDbFieldName, dbList);
        } else {
            return newDbFieldName;
        }
    }

    @Override
    public List<String> retrieveDbFieldNamesWithoutSeperatorByCompanyId(Long companyId) {
        List<HkFieldEntity> allCustomFieldsByCompanyId = this.retrieveAllCustomFieldsByCompanyId(companyId);
        List<String> dbFieldNamesWithoutSeperatorList = null;
        if (!CollectionUtils.isEmpty(allCustomFieldsByCompanyId)) {
            dbFieldNamesWithoutSeperatorList = new ArrayList<>();
            for (HkFieldEntity fields : allCustomFieldsByCompanyId) {
                String[] split = fields.getDbFieldName().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                dbFieldNamesWithoutSeperatorList.add(split[0]);
            }
        }
        return dbFieldNamesWithoutSeperatorList;
    }

    @Override
    public int uniqueSubEntityName(String dbSubEntityName, Long companyId, List<Long> uniqueSubEntityName) {
        int count = 0;
        Search search = new Search(HkSubFormFieldEntity.class);
        search.addFilterILike(HkSubFormFieldEntityField.subFieldName, "%" + dbSubEntityName + "%");
        search.addFilter(Filter.equal(HkSubFormFieldEntityField.FRANCHISE, companyId));
        search.addFilter(Filter.equal(HkSubFormFieldEntityField.IS_ARCHIVE, Boolean.FALSE));
        search.addFilterNotIn(HkSubFormFieldEntityField.id, uniqueSubEntityName);
        count = commonDAO.count(search);
        return count;

    }

    @Override
    public void saveFields(Long featureId, Long sectionId, String documentName, List<HkFieldEntity> fieldList, boolean isCustomFields, long companyId) {
        Map<Long, HkFieldEntity> oldFieldMap = new HashMap<>();
        if (isCustomFields) {
            if (sectionId == null) {
                sectionId = HkSystemConstantUtil.DEFAULT_SECTION_ID;
            }
            List<HkFieldEntity> oldFieldList = this.retrieveFieldsByCriteria(featureId, sectionId, Boolean.TRUE, HkSystemConstantUtil.ACTIVE, Boolean.FALSE, false, companyId);
            if (!CollectionUtils.isEmpty(oldFieldList)) {
                for (HkFieldEntity field : oldFieldList) {
                    if (CollectionUtils.isEmpty(fieldList) || !fieldList.contains(field)) {
                        field.setStatus(HkSystemConstantUtil.INACTIVE);
                        field.setIsArchive(true);
                    } else {
                        //  if field needs to be updated, update the relavant fields of object
                        oldFieldMap.put(field.getId(), field);
                    }
                }
            }
        } else {
            //For fixed fields
            if (sectionId == null) {
                sectionId = HkSystemConstantUtil.DEFAULT_SECTION_ID;
            }
            List<HkFieldEntity> oldFieldList = this.retrieveFieldsByCriteria(featureId, sectionId, Boolean.FALSE, HkSystemConstantUtil.ACTIVE, Boolean.FALSE, false, companyId);
            if (!CollectionUtils.isEmpty(oldFieldList)) {
                for (HkFieldEntity field : oldFieldList) {
                    if (CollectionUtils.isEmpty(fieldList) || !fieldList.contains(field)) {
                        field.setStatus(HkSystemConstantUtil.INACTIVE);
                        field.setIsArchive(true);
                    } else {
                        //  if field needs to be updated, update the relavant fields of object
                        oldFieldMap.put(field.getId(), field);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(fieldList)) {
            Iterator<HkFieldEntity> fieldItr = fieldList.iterator();
            while (fieldItr.hasNext()) {
                HkFieldEntity field = fieldItr.next();
                if (field.getId() == null) {
                    //  Creating fields logic
                    if (field.getIsCustomField()) {
                        //  Table name for storing these fields' values
                        field.setDbBaseName(documentName);
                        field.setDbBaseType(HkSystemConstantUtil.DBBaseType.MONGO_DB);
                        // Changed the code as it was not working by Shifa on 9 December 2014.
                        //  e.g. First Name will be stored as first_name
//                        field.setDbFieldName(field.getFieldLabel().replaceAll(" ", "_").toLowerCase());
//                        field.setDbFieldName(field.getDbFieldName().replaceAll("\\.", "_").toLowerCase());
//                        int i = 0;
//                        while (!checkDbFieldNameUnique(field.getDbFieldName(), featureId)) {
//                            System.out.println("check db field nae");
//                            field.setDbFieldName(field.getDbFieldName() + i++);
//                            System.out.println("DBFIELD...." + field.getDbFieldName());
//                        }
                        Map<String, String> labelAndDbFieldMap = createUniqueKeyForField(fieldList, companyId);
                        if (labelAndDbFieldMap.containsKey(field.getFieldLabel())) {
                            field.setDbFieldName(labelAndDbFieldMap.get(field.getFieldLabel()));
                            field.setUiFieldName(labelAndDbFieldMap.get(field.getFieldLabel()));
                        } else {
                            field.setDbFieldName(field.getDbFieldName());
                            field.setUiFieldName(field.getDbFieldName());
                        }
                        field.setFieldType(HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(field.getComponentType()));
                    }
                    field.setStatus(HkSystemConstantUtil.ACTIVE);

                } else {
                    //  updating fields logic, would be merged in session above, so remove it from this list
                    fieldItr.remove();
                    //  Set new field values for the old fields
                    HkFieldEntity oldField = oldFieldMap.get(field.getId());
                    if (oldField != null) {
                        oldField.setValidationPattern(field.getValidationPattern());
                        oldField.setFieldLabel(field.getFieldLabel());
                        oldField.setAssociatedCurrency(field.getAssociatedCurrency());
                        oldField.setSeqNo(field.getSeqNo());
                        oldField.setComponentType(field.getComponentType());
                        oldField.setFieldType(field.getFieldType());
                        oldField.setDbFieldName(field.getDbFieldName());
                        oldField.setDbBaseName(field.getDbBaseName());
                        oldField.setUiFieldName(field.getUiFieldName());
                        oldField.setFranchise(field.getFranchise());
                        oldField.setIsPrivate(field.getIsPrivate());
                        oldField.setFormulaValue(field.getFormulaValue());
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(fieldList)) {
            //  Save the fields so we get the ids of the fields.
            commonDAO.save(fieldList.toArray());

            for (HkFieldEntity field : fieldList) {
                // add values for the fields
                if (field.getFieldValues() != null) {
                    String defaultValue = "";
                    //  Replace all curly braces and split by comma
                    String[] validationsArr = field.getValidationPattern().replace("{", "")
                            .replace("}", "")
                            .split(",");

                    //  Find the default value
                    HkValueEntity defaultValueEntity = null;
                    for (String validationValue : validationsArr) {
                        if (validationValue.contains("\"defaultValue\":")) {
                            defaultValue = validationValue.replaceAll("\"defaultValue\":\"", "");
                            //  Last double quote would be remainign so remove it
                            if (defaultValue.length() > 0) {
                                defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
                            }
                            break;
                        }
                    }

                    HkMasterEntity masterEntity = new HkMasterEntity(field.getId().toString(), field.getFieldLabel(), (short) 3, false, field.getCreatedOn(), field.getCreatedBy(), field.getCreatedBy(), field.getCreatedOn(), true);
                    masterEntity.setDescription("Master for " + field.getFieldLabel() + " generated by System.");
                    masterEntity.setIsArchive(false);
                    masterEntity.setFranchise(field.getFranchise());
                    masterEntity.setMasterType(MasterType.CUSTOM.value());

                    List<HkValueEntity> valueEntityList = new ArrayList<>();
                    String[] values = field.getFieldValues().split(",");
                    for (int count = 0; count < values.length; count++) {
                        HkValueEntity valueEntity = new HkValueEntity(null, count + 1, values[count].trim(), false, field.getFranchise(), true, false, field.getCreatedBy(), field.getCreatedOn(), field.getCreatedBy(), field.getCreatedOn(), masterEntity);
                        valueEntity.setIsArchive(false);
                        valueEntity.setTranslatedValueName(values[count]);
                        valueEntityList.add(valueEntity);
                        if (defaultValue.length() > 0 && values[count].trim().equals(defaultValue)) {
                            defaultValueEntity = valueEntity;
                        }
                    }
                    masterEntity.setHkValueEntityList(valueEntityList);
                    //  Let this call be here, this is transactional so it remains same whether we call single insert or bulk insert
                    //  Moreover, id of master value is needed below to set for default value
                    hkFoundationService.createMaster(masterEntity);

                    //  if default value is present, set it
                    if (defaultValueEntity != null) {
                        //  Replace the value with id of master
                        field.setValidationPattern(field.getValidationPattern().replaceFirst("\"defaultValue\":\"[^\"]*\"", "\"defaultValue\":" + defaultValueEntity.getId() + ""));
                    }
                }
            }
        }
    }

    @Override
    public void saveField(Long featureId, Long sectionId, String documentName, HkFieldEntity customField, boolean isCustomFields, long companyId) {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = this.retrieveMapOfFieldIdAndHkFieldEntity();

        Boolean isCreate;
        if (customField.getId() != null) {
            isCreate = false;
        } else {
            isCreate = true;
        }
        if (isCustomFields) {
            if (sectionId == null) {
                sectionId = HkSystemConstantUtil.DEFAULT_SECTION_ID;
            }
            HkFieldEntity hkFieldEntity = new HkFieldEntity();
            if (customField.getId() == null) {
                // create logic
                if (customField.getIsCustomField()) {
                    customField.setDbBaseName(documentName);
                    customField.setDbBaseType(HkSystemConstantUtil.DBBaseType.MONGO_DB);
                    // this uses recursive method which is private
                    Map<String, String> labelAndDbFieldMap = createUniqueKeyForField(customField, companyId);
                    if (customField.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                        String[] validationsArr = customField.getValidationPattern().replace("{", "")
                                .replace("}", "")
                                .split(",");
                        String pointerId = "";
                        for (String validationValue : validationsArr) {
                            if (validationValue.contains("\"pointer\":")) {
                                String[] pointerArray = validationValue.split(":");
                                pointerId = pointerArray[1].replace("\"", "");
                            }
                        }
                        String pointerComponentType = null;
                        if (mapOfFieldIdAndHkFieldEntity.containsKey(Long.parseLong(pointerId))) {
                            pointerComponentType = mapOfFieldIdAndHkFieldEntity.get(Long.parseLong(pointerId)).getComponentType();
                            customField.setFieldType(HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(pointerComponentType));
                        }

                        if (labelAndDbFieldMap.containsKey(customField.getFieldLabel())) {

                            customField.setDbFieldName(labelAndDbFieldMap.get(customField.getFieldLabel())
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(pointerComponentType)
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(pointerComponentType)
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()));
                            customField.setUiFieldName(labelAndDbFieldMap.get(customField.getFieldLabel())
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(pointerComponentType)
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(pointerComponentType)
                                    + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL
                                    + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()));
                        } else {
                            customField.setDbFieldName(customField.getDbFieldName() + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(pointerComponentType) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(pointerComponentType) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()));
                            customField.setUiFieldName(customField.getDbFieldName() + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(pointerComponentType) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(pointerComponentType) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()));

                        }

                    } else {
                        if (labelAndDbFieldMap.containsKey(customField.getFieldLabel())) {

                            customField.setDbFieldName(labelAndDbFieldMap.get(customField.getFieldLabel()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(customField.getComponentType()));
                            customField.setUiFieldName(labelAndDbFieldMap.get(customField.getFieldLabel()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(customField.getComponentType()));
                        } else {
                            customField.setDbFieldName(customField.getDbFieldName() + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(customField.getComponentType()));
                            customField.setUiFieldName(customField.getDbFieldName() + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customField.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(customField.getComponentType()));

                        }
                    }
                    customField.setIsEditable(true);
                    if (customField.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {

                    } else {
                        customField.setFieldType(HkSystemConstantUtil.CustomField.FIELD_TYPE_MAP.get(customField.getComponentType()));
                    }
                    customField.setStatus(HkSystemConstantUtil.ACTIVE);
                    commonDAO.save(customField);
                }
            } else {
                // update logic
                hkFieldEntity = this.retrieveCustomFieldByFieldId(customField.getId());
                hkFieldEntity.setValidationPattern(customField.getValidationPattern());
                if (customField.getFormulaValue() != null) {
                    hkFieldEntity.setFormulaValue(customField.getFormulaValue());
                }
                hkFieldEntity.setFieldLabel(customField.getFieldLabel());
                hkFieldEntity.setAssociatedCurrency(customField.getAssociatedCurrency());
                hkFieldEntity.setSeqNo(customField.getSeqNo());
                hkFieldEntity.setLastModifiedOn(new Date());
                hkFieldEntity.setLastModifiedBy(customField.getLastModifiedBy());
                hkFieldEntity.setIsPrivate(customField.getIsPrivate());
                if (customField.getHkSubFormFields() != null) {
                    hkFieldEntity.setHkSubFormFields(customField.getHkSubFormFields());
                }
                commonDAO.save(hkFieldEntity);
            }

            // add values for the fields
            if (customField.getFieldValues() != null) {
                String defaultValue = "";
                //  Replace all curly braces and split by comma
                String[] validationsArr = customField.getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                //  Find the default value
                HkValueEntity defaultValueEntity = null;
                String defaultValueForMultiSelect = "";
                Long masterCodeEdit = null;
                List<HkValueEntity> defaultValueEntityList = new ArrayList<>();
                String[] splitValue = null;
                for (String validationValue : validationsArr) {
                    if (validationValue.contains("\"defaultValue\":")) {
                        if (customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
                            defaultValue = validationValue.replaceAll("\"defaultValue\":\"", "");
                            //  Last double quote would be remainign so remove it
                            if (defaultValue.length() > 0) {
                                defaultValue = defaultValue.substring(0, defaultValue.length() - 1);
                            }
                            break;
                        }

                        if (customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                            // for multiselect another logic as we will need multiple default values
                            String[] defaultValueArrayForMultiSelect = validationValue.split(":");
                            defaultValueForMultiSelect = defaultValueArrayForMultiSelect[1];
                            if (defaultValueForMultiSelect != null) {
                                // splitvalue stores the multiple default values in array by separting using the separator
                                splitValue = defaultValueForMultiSelect.replace("\"", "").split("~!");

                            }
                        }
                    }
                    if (validationValue.contains("\"mastercode\":")) {
                        String[] masterCodeArray = validationValue.split(":");
                        masterCodeEdit = Long.parseLong(masterCodeArray[1]);
                    }
                }
                String masterCode = null;
                if (isCreate) {
//                    if (defaultValueForMultiSelect != null) {
//                        // splitvalue stores the multiple default values in array by separting using the separator
//                        splitValue = defaultValueForMultiSelect.replace("\"", "").split("~!");
//
//                    }
                    // Masters should be created at create time only
                    HkMasterEntity masterEntity = new HkMasterEntity(customField.getId().toString(), customField.getFieldLabel(), (short) 3, false, customField.getCreatedOn(), customField.getCreatedBy(), customField.getCreatedBy(), customField.getCreatedOn(), true);
                    masterEntity.setDescription("Master for " + customField.getFieldLabel() + " generated by System.");
                    masterEntity.setIsArchive(false);
                    masterEntity.setFranchise(customField.getFranchise());
                    masterEntity.setMasterType(MasterType.CUSTOM.value());

                    List<HkValueEntity> valueEntityList = new ArrayList<>();

                    String[] values = customField.getFieldValues().split(",");
                    for (int count = 0; count < values.length; count++) {
                        HkValueEntity valueEntity = new HkValueEntity(null, count + 1, values[count].trim(), false, customField.getFranchise(), true, false, customField.getCreatedBy(), customField.getCreatedOn(), customField.getCreatedBy(), customField.getCreatedOn(), masterEntity);
                        // Check If Custom Field is Private
                        System.out.println("Is private..." + customField.getIsPrivate());
                        if (customField.getIsPrivate() != null && customField.getIsPrivate().equals(Boolean.TRUE)) {
                            System.out.println("in here ");
                            // this case when custom field is private,So franchise and created by franchise would be of login user franchise
                            valueEntity.setFranchise(companyId);
                            valueEntity.setCreatedByFranchise(companyId);
                        } else {
                            System.out.println("Shared");
                            // This case  when custom fiels is shared,So franchise would be of 0 and created by franchise would be login user franchise
                            valueEntity.setFranchise(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
                            valueEntity.setCreatedByFranchise(companyId);

                        }

                        valueEntity.setIsArchive(false);
                        valueEntity.setTranslatedValueName(values[count]);
                        valueEntityList.add(valueEntity);

                        if (defaultValue.length() > 0 && values[count].trim().equals(defaultValue) && !(customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN))) {
                            defaultValueEntity = valueEntity;

                        }
                        if (customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                            // multiple value entities as default value contains multiple values
                            if (splitValue != null) {
                                if (Arrays.asList(splitValue).contains(values[count])) {
                                    defaultValueEntityList.add(valueEntity);
                                }
                            }
                        }
                    }
                    masterEntity.setHkValueEntityList(valueEntityList);
                    //  Let this call be here, this is transactional so it remains same whether we call single insert or bulk insert
                    //  Moreover, id of master value is needed below to set for default value
                    hkFoundationService.createMaster(masterEntity);
                    masterCode = masterEntity.getCode();
                    String validationPattern = customField.getValidationPattern().replace("{", "")
                            .replace("}", "");
                    if (validationPattern != null && validationPattern.length() > 0) {
                        customField.setValidationPattern("{" + validationPattern.concat(",").concat("\"mastercode\":" + masterCode + "") + "}");

                    } else {
                        customField.setValidationPattern("{" + validationPattern.concat("\"mastercode\":" + masterCode + "") + "}");

                    }
                } else {

                    List<HkValueEntity> allValueEntities = hkFoundationService.retrieveMasterValuesOfSameKeyCodeByCustomFieldId(masterCodeEdit);
                    if (!CollectionUtils.isEmpty(allValueEntities)) {
                        for (HkValueEntity allValueEntity : allValueEntities) {
                            if (splitValue != null) {
                                if (Arrays.asList(splitValue).contains(allValueEntity.getValueName())) {
                                    defaultValueEntityList.add(allValueEntity);

                                }
                            }
                            if (defaultValue.length() > 0 && allValueEntity.getValueName().equals(defaultValue) && !(customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN))) {
                                defaultValueEntity = allValueEntity;

                            }

                            // Custom Field Private or Shared case
                            if (customField.getIsPrivate() != null && customField.getIsPrivate().equals(Boolean.TRUE)) {
                                // if we are changing from false to true...i.e. from shared to private...Then copy createdByFranchise to franchise
                                allValueEntity.setFranchise(allValueEntity.getCreatedByFranchise());

                            } else {
                                // If we are changing from true to false...i.e. from private to shared...
                                allValueEntity.setFranchise(HkSystemConstantUtil.ZERO_LEVEL_FRANCHISE);
                            }

                        }
                        // Update Value Entities
                        hkFoundationService.createAllMasterValues(allValueEntities);
                    }
                }
                //  if default value is present, set it
                if (defaultValueEntity != null || !CollectionUtils.isEmpty(defaultValueEntityList)) {
                    //  Replace the value with id of master
                    if (!customField.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.MULTISELECT_DROPDOWN)) {
                        if (isCreate) {
                            customField.setValidationPattern(customField.getValidationPattern().replaceFirst("\"defaultValue\":\"[^\"]*\"", "\"defaultValue\":" + defaultValueEntity.getId() + ""));
                        } else {
                            hkFieldEntity.setValidationPattern(customField.getValidationPattern().replaceFirst("\"defaultValue\":\"[^\"]*\"", "\"defaultValue\":" + defaultValueEntity.getId() + ""));

                        }
                    } else {
                        List<Long> defaultIds = null;
                        if (!CollectionUtils.isEmpty(defaultValueEntityList)) {
                            defaultIds = new ArrayList<>();
                            for (HkValueEntity hkdefaultValue : defaultValueEntityList) {
                                // list to store ids
                                defaultIds.add(hkdefaultValue.getId());
                            }
                        }
                        String defaultValueString = defaultIds.toString().replace("[", "\"")
                                .replace("]", "\"");
                        defaultValueString = defaultValueString.replaceAll(",", "&");
                        if (isCreate) {
                            customField.setValidationPattern(customField.getValidationPattern().replaceFirst("\"defaultValue\":\"[^\"]*\"", "\"defaultValue\":" + defaultValueString + ""));
                        } else {
                            hkFieldEntity.setValidationPattern(customField.getValidationPattern().replaceFirst("\"defaultValue\":\"[^\"]*\"", "\"defaultValue\":" + defaultValueString + ""));
                        }
                    }
                }
            }

        }

    }

    private boolean checkDbFieldNameUnique(String dbFieldName, long featureId) {
        if (StringUtils.hasText(dbFieldName)) {
            Search s = new Search(HkFieldEntity.class);
            s.addFilter(Filter.equal(HkFieldEntityField.DB_FIELD_NAME, dbFieldName));
            s.addFilter(Filter.equal(HkFieldEntityField.FEATURE, featureId));
            return commonDAO.count(s) <= 0;
        }
        return false;
    }

    @Override
    public void removeFields(List<HkFieldEntity> fieldList) {
        for (HkFieldEntity fieldEntity : fieldList) {
            fieldEntity.setStatus(HkSystemConstantUtil.INACTIVE);
            fieldEntity.setIsArchive(true);
        }
        commonDAO.save(fieldList.toArray());
    }

    @Override
    public void removeFields(List<Long> fieldIds, Long removedBy) {
        List<HkFieldEntity> fieldEntityList = this.retrieveFieldsByIds(fieldIds, false);
        if (!CollectionUtils.isEmpty(fieldIds)) {
            for (HkFieldEntity fieldEntity : fieldEntityList) {
                fieldEntity.setLastModifiedBy(removedBy);
                fieldEntity.setLastModifiedOn(new Date());
                fieldEntity.setStatus(HkSystemConstantUtil.INACTIVE);
                fieldEntity.setIsArchive(true);
            }
            commonDAO.save(fieldEntityList.toArray());
        }
    }

    @Override
    public List<HkFieldEntity> retrieveFieldsByIds(List<Long> fieldIds, boolean setValuePattern) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        if (!CollectionUtils.isEmpty(fieldIds)) {
            search.addFilterIn(HkFieldEntityField.ID, fieldIds);
        }
        List<HkFieldEntity> resultFields = commonDAO.search(search);
        if (setValuePattern) {
            this.fillValuePattern(resultFields);
        }
        return resultFields;
    }

    private Collection<HkFieldEntity> fillValuePattern(Collection<HkFieldEntity> fieldList) {
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (HkFieldEntity field : fieldList) {
                HkMasterEntity fieldMaster = hkFoundationService.retrieveMaster(field.getId() + "", (short) 0);
                if (fieldMaster != null && fieldMaster.getHkValueEntityList() != null) {
                    for (HkValueEntity masterValue : fieldMaster.getHkValueEntityList()) {
                        if (field.getFieldValues() == null) {
                            field.setFieldValues(masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                        } else {
                            field.setFieldValues(field.getFieldValues() + "," + masterValue.getId() + HkSystemConstantUtil.SEPARATOR_PI + masterValue.getValueName());
                        }
                    }
                }
            }
        }
        return fieldList;
    }

    @Override
    public List<HkFieldEntity> retrieveFieldsByCriteria(Long featureId, Long sectionId, Boolean isCustomField, String status, Boolean archiveStatus, boolean setValuePattern, long companyId) {
        Search search = new Search(HkFieldEntity.class);

        if (featureId != null) {
            search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        }
        if (sectionId != null) {
            if (sectionId == HkSystemConstantUtil.DEFAULT_SECTION_ID) {
                search.addFilterNull(HkFieldEntityField.SECTION);
            } else {
                search.addFilterEqual(HkFieldEntityField.SECTION_ID, sectionId);
            }
        }
        if (isCustomField != null) {
            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, isCustomField);
        }
        if (status != null) {
            search.addFilterEqual(HkFieldEntityField.STATUS, status);
        }
        if (archiveStatus != null) {
            search.addFilterEqual(IS_ARCHIVE, archiveStatus);
        }
        search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));

        List<HkFieldEntity> resultFields = commonDAO.search(search);
        if (setValuePattern) {
            this.fillValuePattern(resultFields);
        }
        return resultFields;
    }

    @Override
    public List<HkFieldEntity> retrieveFieldsByFeatures(List<Long> featureIds, Boolean isCustomField, long companyId) {
        List<HkFieldEntity> resultFields = null;
        Search search = SearchFactory.getSearch(HkFieldEntity.class);

        if (!CollectionUtils.isEmpty(featureIds)) {
            search.addFilterIn(HkFieldEntityField.FEATURE, featureIds);

            if (isCustomField != null) {
                search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, isCustomField);
            }

            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));

            search.addFilterEqual(HkFieldEntityField.IS_ARCHIVE, false);

            resultFields = commonDAO.search(search);
        }
        return resultFields;
    }

    @Override
    public boolean isFieldLabelExistent(String fieldLabel, Long sectionId) {
        Search search = new Search(HkFieldEntity.class);

        if (fieldLabel != null) {
            search.addFilterEqual(HkFieldEntityField.FIELD_LABEL, fieldLabel);
        }
        if (sectionId != null) {
            if (sectionId == HkSystemConstantUtil.DEFAULT_SECTION_ID) {
                search.addFilterNull(HkFieldEntityField.SECTION);
            } else {
                search.addFilterEqual(HkFieldEntityField.SECTION, sectionId);
            }
        }
        search.addFilterEqual(HkFieldEntityField.STATUS, HkSystemConstantUtil.ACTIVE);
        search.addFilterEqual(IS_ARCHIVE, false);

        List<HkFieldEntity> resultList = commonDAO.search(search);
        return !CollectionUtils.isEmpty(resultList);
    }

    @Override
    public Set<Long> retrieveFeaturesForExistingFields(Boolean isCustomField, Long companyId) {
        Search search = new Search(HkFieldEntity.class);
        if (isCustomField != null) {
            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, isCustomField);
        }
        search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addField(HkFieldEntityField.FEATURE);
        return new HashSet<>(commonDAO.search(search));
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
    public List<HkFieldEntity> retrieveAllFieldsByCompanyId(Long companyId) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        return commonDAO.search(search);
    }

    @Override
    public List<HkFieldEntity> retrieveAllCustomFieldsByCompanyId(Long companyId) {
        Search search = new Search(HkFieldEntity.class);
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
//            search.addFilterEqual(HkFieldEntityField.IS_ARCHIVE, Boolean.FALSE);
        }
        return commonDAO.search(search);
    }

    @Override
    public void createAllFields(List<HkFieldEntity> hkFieldEntityList) {
        if (hkFieldEntityList != null) {
            commonDAO.saveAll(hkFieldEntityList);
        }
    }

    @Override
    public List<HkFieldEntity> retrieveAllFieldsByFeatureId(Long featureId, String componentType) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        search.addFilterEqual(IS_ARCHIVE, false);
        if (componentType != null) {
            search.addFilterEqual(HkFieldEntityField.COMPONENT_TYPE, componentType);
        }
        return commonDAO.search(search);

    }

    @Override
    public Map<Long, List<HkFieldEntity>> retrieveFeatureWiseCustomFieldList(List<String> listOfFieldTypes, List<String> componentTypes) {
        Map<Long, List<HkFieldEntity>> featureWithCustomFieldMap = null;
        List<HkFieldEntity> customFieldList;
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        if (!CollectionUtils.isEmpty(listOfFieldTypes)) {
            search.addFilterIn(HkFieldEntityField.FIELD_TYPE, listOfFieldTypes);
        }
        if (!CollectionUtils.isEmpty(componentTypes)) {
            search.addFilterIn(HkFieldEntityField.COMPONENT_TYPE, componentTypes);
        }
        List<HkFieldEntity> hkFieldEntityList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(hkFieldEntityList)) {
            featureWithCustomFieldMap = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : hkFieldEntityList) {
                if (featureWithCustomFieldMap.containsKey(hkFieldEntity.getFeature())) {
                    customFieldList = featureWithCustomFieldMap.get(hkFieldEntity.getFeature());
                    customFieldList.add(hkFieldEntity);
                } else {
                    customFieldList = new ArrayList<>();
                    customFieldList.add(hkFieldEntity);
                }
                featureWithCustomFieldMap.put(hkFieldEntity.getFeature(), customFieldList);

            }
        }
        return featureWithCustomFieldMap;
    }

    @Override
    public void createFields(List<HkFieldEntity> fieldEntitys) {
        commonDAO.saveAll(fieldEntitys);
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            List<HkMasterEntity> masterEntitys = new ArrayList<>();
            for (HkFieldEntity customField : fieldEntitys) {

                if (customField.getIsCustomField()) {
//                System.out.println("Component type : "+customField.getComponentType());
                    if (customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.DROPDOWN)) {
                        String validationPattern = customField.getValidationPattern();
                        Type validationPatternObjType = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map<String, Object> validationPatternObj = (new Gson()).fromJson(validationPattern, validationPatternObjType);
                        if (!HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE.startsWith(customField.getDbFieldName()) && !HkSystemConstantUtil.LotStaticFieldName.CARATE_RANGE.startsWith(customField.getDbFieldName())
                                && !HkSystemConstantUtil.PacketStaticFieldName.CARATE_RANGE.startsWith(customField.getDbFieldName()) && !HkSystemConstantUtil.PlanStaticFieldName.MACHINE_TO_PROCESS.startsWith(customField.getDbFieldName())) {
                            HkMasterEntity masterEntity = new HkMasterEntity(customField.getId().toString(), customField.getFieldLabel(), (short) 3, false, customField.getCreatedOn(), customField.getCreatedBy(), customField.getCreatedBy(), customField.getCreatedOn(), true);
                            masterEntity.setDescription("Master for " + customField.getFieldLabel() + " generated by System.");
                            masterEntity.setIsActive(true);
                            masterEntity.setIsArchive(false);
                            masterEntity.setFranchise(customField.getFranchise());
                            masterEntity.setMasterType(MasterType.CUSTOM.value());
                            masterEntitys.add(masterEntity);

                            if (!validationPatternObj.containsKey("mastercode")) {
                                validationPatternObj.put("mastercode", customField.getId());
                            }
                            validationPattern = (new Gson()).toJson(validationPatternObj, validationPatternObjType);
                            customField.setValidationPattern(validationPattern);
                        }
                    } else if (customField.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                        String validationPattern = customField.getValidationPattern();
                        Type validationPatternObjType = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        Map<String, Object> validationPatternObj = (new Gson()).fromJson(validationPattern, validationPatternObjType);
                        if (validationPatternObj.containsKey("pointerTo")) {
                            //Static fields of pointer type.
                            String pointerDbFieldName = validationPatternObj.get("pointerTo").toString();
                            HkFieldEntity pointerToFieldEntity = null;
                            for (HkFieldEntity entity : fieldEntitys) {
                                if (entity.getDbFieldName().equals(pointerDbFieldName)) {
                                    pointerToFieldEntity = entity;
                                    break;
                                }
                            }
                            if (pointerToFieldEntity != null) {
                                Long featureId = pointerToFieldEntity.getFeature();
                                UMFeature featureEntity = null;
                                String featureName = null;
                                try {
                                    featureEntity = featureService.retrieveFeatureById(featureId, null);
                                } catch (GenericDatabaseException ex) {
                                    Logger.getLogger(HkFieldServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (featureEntity != null) {
                                    featureName = featureEntity.getMenuLabel();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", pointerToFieldEntity.getId().toString());
                                    map.put("text", featureName + "." + pointerDbFieldName);
                                    List<Object> list = new ArrayList<>();
                                    list.add(map);
                                    validationPatternObj.put("pointerOption", list);
                                }
                                validationPatternObj.remove("pointerTo");
                                validationPatternObj.put(HkSystemConstantUtil.CustomField.Validation.POINTER, pointerToFieldEntity.getId().toString());

                                validationPattern = (new Gson()).toJson(validationPatternObj, validationPatternObjType);
                                customField.setValidationPattern(validationPattern);
                            }
                        }
                    }
                }
            }
            if (masterEntitys.size() > 0) {
                hkFoundationService.createAllMasters(masterEntitys);
            }
        }
    }

    public void retrieveFiledsByDbFieldName(List<String> dbFieldName) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        search.addFilterIn(HkFieldEntityField.DB_FIELD_NAME, dbFieldName);
        List<HkFieldEntity> fieldEntitys = commonDAO.search(search);
        Map<String, String> modelToLabel = null;
        if (!CollectionUtils.isEmpty(fieldEntitys)) {
            modelToLabel = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : fieldEntitys) {
                modelToLabel.put(hkFieldEntity.getDbFieldName(), hkFieldEntity.getFieldLabel());
            }
        }
    }

    @Override
    public List<HkFieldEntity> retrieveAllDependantFieldsByCompanyId(Long companyId) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        search.addFilterEqual(HkFieldEntityField.IS_DEPENDANT, Boolean.TRUE);
        return commonDAO.search(search);

    }

    @Override
    public Set<HkSubFormFieldEntity> retrieveSetOfSubEntitiesFromFieldId(HkFieldEntity hkFieldEntity) {
        Search search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
        search.addFilterEqual(HkSubFormFieldEntityField.isArchive, Boolean.FALSE);
        search.addFilterEqual(HkSubFormFieldEntityField.parentField, hkFieldEntity);
        List<HkSubFormFieldEntity> subFormEntitiesList = commonDAO.search(search);
        Set<HkSubFormFieldEntity> setOfSubEntities = new HashSet<>(subFormEntitiesList);
        return setOfSubEntities;
    }

    @Override
    public List<String> retrieveAllSubEntitiesDbFieldName(Long companyId) {
        List<String> subDbFieldList = new ArrayList<>();
        Search search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
        search.addFilterEqual(HkSubFormFieldEntityField.isArchive, Boolean.FALSE);

        List<HkSubFormFieldEntity> subFormEntitiesList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(subFormEntitiesList)) {
            for (HkSubFormFieldEntity subList : subFormEntitiesList) {
                subDbFieldList.add(subList.getSubFieldName());
            }
        }
        return subDbFieldList;
    }

    @Override
    public List<HkSubFormFieldEntity> retrieveSetOfSubEntitiesByListOfFieldIds(List<Long> fieldIds) {
        Search search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
        search.addFilterEqual(HkSubFormFieldEntityField.isArchive, Boolean.FALSE);
        if (!CollectionUtils.isEmpty(fieldIds)) {
            search.addFilterIn(HkSubFormFieldEntityField.parentFieldId, fieldIds);
        }
        List<HkSubFormFieldEntity> subFormEntitiesList = commonDAO.search(search);
        return subFormEntitiesList;
    }

    @Override
    public List<HkSubFormFieldEntity> retrieveListOfSubEntitiesAssociatedWithFieldId(HkFieldEntity hkFieldEntity) {
        Search search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
        search.addFilterEqual(HkSubFormFieldEntityField.isArchive, Boolean.FALSE);
        if (hkFieldEntity != null) {
            search.addFilterEqual(HkSubFormFieldEntityField.parentField, hkFieldEntity);
        }
        List<HkSubFormFieldEntity> subFormEntitiesList = commonDAO.search(search);
        return subFormEntitiesList;
    }

    @Override
    public List<HkSubFormFieldEntity> retrieveSubFormEntityById(List<Long> hkSubFormFieldId) {
        Search search = SearchFactory.getSearch(HkSubFormFieldEntity.class);
        search.addFilterIn(HkSubFormFieldEntityField.id, hkSubFormFieldId);
        List<HkSubFormFieldEntity> subfieldEntitys = commonDAO.search(search);
        return subfieldEntitys;
    }

    @Override
    public void saveSubEntityField(List<HkSubFormFieldEntity> hkSubFormFieldEntity) {
        commonDAO.saveAll(hkSubFormFieldEntity);
    }

    @Override
    public Map<String, HkFieldEntity> retrieveMapOfDBFieldNameWithEntity(List<String> dbFieldName, Long companyId) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        Map<String, HkFieldEntity> dbFieldMapWithFieldEntity = null;
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        if (!CollectionUtils.isEmpty(dbFieldName)) {
            search.addFilterIn(HkFieldEntityField.FIELD_NAME, dbFieldName);
        }
        List<HkFieldEntity> hkFieldEntities = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(hkFieldEntities)) {
            dbFieldMapWithFieldEntity = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : hkFieldEntities) {
                dbFieldMapWithFieldEntity.put(hkFieldEntity.getDbFieldName(), hkFieldEntity);
            }
        }
        return dbFieldMapWithFieldEntity;
    }

    @Override
    public List<HkFieldEntity> retrieveFieldByFeatureAndComponentType(Long companyId, Long featureId, String componentType, Boolean isArchive) {

        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        List<HkFieldEntity> fieldEntityList = new ArrayList<>();
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        if (componentType != null) {
            search.addFilterEqual(HkFieldEntityField.COMPONENT_TYPE, componentType);
        }
        if (isArchive != null) {
            search.addFilterEqual(HkFieldEntityField.IS_ARCHIVE, isArchive);
        }
        if (featureId != null) {
            search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        }
        fieldEntityList = commonDAO.search(search);
        return fieldEntityList;

    }

    @Override
    public Map<String, HkFieldEntity> retrieveMapOfDbFieldNameForFeatureInvolvedInFormula(Long companyId, String featureName, String componentType) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        Map<String, HkFieldEntity> fieldEntityMap = null;
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        if (featureName != null) {
            search.addFilterLike(HkFieldEntityField.FORMULA_VALUE, "%" + featureName + "%");
        }
        if (componentType != null) {
            search.addFilterEqual(HkFieldEntityField.COMPONENT_TYPE, componentType);
        }
        List<HkFieldEntity> hkFieldEntities = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(hkFieldEntities)) {
            fieldEntityMap = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : hkFieldEntities) {
                fieldEntityMap.put(hkFieldEntity.getDbFieldName(), hkFieldEntity);
            }
        }
        return fieldEntityMap;
    }

    @Override
    public Map<Long, HkFieldEntity> retrieveMapOfFieldIdAndHkFieldEntity() {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        Map<Long, HkFieldEntity> fieldEntityMap = null;

        List<HkFieldEntity> hkFieldEntities = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(hkFieldEntities)) {
            fieldEntityMap = new HashMap<>();
            for (HkFieldEntity hkFieldEntity : hkFieldEntities) {
                fieldEntityMap.put(hkFieldEntity.getId(), hkFieldEntity);
            }
        }
        return fieldEntityMap;

    }

    @Override
    public Map<String, String> retrieveUIFieldNameWithComponentTypes(List<String> dbFieldNameList) {
        Map<String, String> componentCodeMap = new HashMap<>(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP);
        Map<String, String> codeMap = null;
        if (componentCodeMap != null && !componentCodeMap.isEmpty()) {
            codeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : componentCodeMap.entrySet()) {
                codeMap.put(entry.getValue(), entry.getKey());

            }
        }
        Map<String, String> uiFieldMap = null;
        if (!CollectionUtils.isEmpty(dbFieldNameList)) {
            uiFieldMap = new HashMap<>();
            for (String dbField : dbFieldNameList) {

                String[] split = dbField.split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                if (split != null && split.length >= 1) {
                    uiFieldMap.put(dbField, codeMap.get(split[1]));
                }

            }
        }
        return uiFieldMap;
    }

//    @Override
//    public Map<String, String> retrieveCustomUIFieldNameWithComponentTypes(Long featureId, Long companyId) {
//        Map<String, String> uiFieldMap = null;
//        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = null;
//        List<HkFieldEntity> fieldList = null;
//        if (featureId != null && companyId != null) {
//            Search search = new Search(HkFieldEntity.class);
//            search.addFilterEqual(IS_ARCHIVE, false);
//            search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
//            search.addFilterEqual(HkFieldEntityField.FRANCHISE, companyId);
//            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, true);
//            fieldList = commonDAO.search(search);
//        }
//        Map<Long, String> fieldIdWithPointerId = null;
//        List<Long> pointerIdsList = null;
//        if (!CollectionUtils.isEmpty(fieldList)) {
//            fieldIdWithPointerId = new HashMap<>();
//            pointerIdsList = new ArrayList<>();
//            for (HkFieldEntity hkfield : fieldList) {
//                String[] validationsArr = hkfield.getValidationPattern().replace("{", "")
//                        .replace("}", "")
//                        .split(",");
//                String pointerId = "";
//                for (String validationValue : validationsArr) {
//                    if (validationValue.contains("\"pointer\":")) {
//                        String[] pointerArray = validationValue.split(":");
//                        pointerId = pointerArray[1].replace("\"", "");
//                        fieldIdWithPointerId.put(hkfield.getId(), pointerId);
//                        pointerIdsList.add(Long.parseLong(pointerId));
//                    }
//                }
//            }
//        }
//        if (!CollectionUtils.isEmpty(pointerIdsList)) {
//            mapOfFieldIdAndHkFieldEntity = this.mapOfFieldIdAndHkFieldEntity(pointerIdsList);
//        }
//        if (!CollectionUtils.isEmpty(fieldList)) {
//            uiFieldMap = new HashMap<>();
//            for (HkFieldEntity fieldList1 : fieldList) {
//                String pointerValue = "";
//                if (fieldIdWithPointerId.containsKey(fieldList1.getId())) {
//                    pointerValue = fieldIdWithPointerId.get(fieldList1.getId());
//                }
//                if (fieldList1.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
//                    if (mapOfFieldIdAndHkFieldEntity.containsKey(Long.parseLong(pointerValue))) {
//                        String componentType = mapOfFieldIdAndHkFieldEntity.get(Long.parseLong(pointerValue)).getComponentType();
//                        String pointerComponentType = HkSystemConstantUtil.CustomField.ComponentType.POINTER + "@*" + componentType;
//                        uiFieldMap.put(fieldList1.getUiFieldName(), pointerComponentType);
//                    }
//                } else {
//                    uiFieldMap.put(fieldList1.getUiFieldName(), fieldList1.getComponentType());
//                }
//            }
//        }
//        return uiFieldMap;
//    }
    private Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity(List<Long> fieldIds) {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = null;
        List<HkFieldEntity> fieldList = new ArrayList<>();
        Search search = new Search(HkFieldEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addFilterIn(HkFieldEntityField.ID, fieldIds);
        fieldList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(fieldList)) {
            mapOfFieldIdAndHkFieldEntity = new HashMap<>();
            for (HkFieldEntity field : fieldList) {
                mapOfFieldIdAndHkFieldEntity.put(field.getId(), field);
            }
        }
        return mapOfFieldIdAndHkFieldEntity;
    }

    @Override
    public Map<String, String> retrieveFeatureNameWithDbFieldListForFormula(Long featureId, Long franchiseId, String featureType, String componentType) {
        List<HkFieldEntity> fieldListByFeatureAndComponentType = this.retrieveFieldByFeatureAndComponentType(franchiseId, featureId, componentType, Boolean.FALSE);
        List<String> invoiceList = new ArrayList<>();
        List<String> parcelList = new ArrayList<>();
        List<String> lotList = new ArrayList<>();
        List<String> packetList = new ArrayList<>();
        int countInvalid = 0;
        Map<String, String> validFormulaMap = null;
        if (!CollectionUtils.isEmpty(fieldListByFeatureAndComponentType)) {
            validFormulaMap = new HashMap<>();
            for (HkFieldEntity field : fieldListByFeatureAndComponentType) {

                String formulaValue = field.getFormulaValue();

                String[] formulaArray = formulaValue.split("|");
                for (String formula : formulaArray) {
                    if (formula.contains(HkSystemConstantUtil.Feature.INVOICE)) {
                        invoiceList.add(formula.replace(HkSystemConstantUtil.Feature.INVOICE + ".", ""));
                    }
                    if (formula.contains(HkSystemConstantUtil.Feature.PARCEL)) {
                        parcelList.add(formula.replace(HkSystemConstantUtil.Feature.PARCEL + ".", ""));
                    }
                    if (formula.contains(HkSystemConstantUtil.Feature.LOT)) {
                        lotList.add(formula.replace(HkSystemConstantUtil.Feature.LOT + ".", ""));
                    }
                    if (formula.contains(HkSystemConstantUtil.Feature.PACKET)) {
                        packetList.add(formula.replace(HkSystemConstantUtil.Feature.PACKET + ".", ""));
                    }

                }
                switch (featureType) {
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT:
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT:
                        if (!CollectionUtils.isEmpty(invoiceList)) {
                            for (String inv_formula : invoiceList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelList)) {
                            for (String par_formula : parcelList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotList)) {
                            for (String lot_formula : lotList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(packetList)) {
                            for (String packet_formula : packetList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

//                                        validFormula += "true";
                                    } else {
                                        countInvalid++;
//                                        validFormula += "false";
                                    }

                                } else {
                                    countInvalid++;
//                                    validFormula += "false";
                                }
                            }
                        }
                        if (countInvalid > 1) {
                        } else {
                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
                        }

                        break;
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL:
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL:
                        if (!CollectionUtils.isEmpty(invoiceList)) {
                            for (String inv_formula : invoiceList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelList)) {
                            for (String par_formula : parcelList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotList)) {
                            for (String lot_formula : lotList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                    } else {
                                        countInvalid++;
                                    }

                                } else {
                                    countInvalid++;
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(packetList)) {
                            for (String packet_formula : packetList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

//                                        validFormula += "true";
                                    } else {
                                        countInvalid++;
//                                        validFormula += "false";
                                    }

                                } else {
                                    countInvalid++;
//                                    validFormula += "false";
                                }
                            }
                        }
                        if (countInvalid > 1) {
                        } else {
                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
                        }

                        break;
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET:
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET:
                        if (!CollectionUtils.isEmpty(invoiceList)) {
                            for (String inv_formula : invoiceList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelList)) {
                            for (String par_formula : parcelList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotList)) {
                            for (String lot_formula : lotList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                        countInvalid++;

                                    } else {

                                    }

                                } else {

                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(packetList)) {
                            for (String packet_formula : packetList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "false";
                                    }

                                } else {
//                                    validFormula += "false";
                                }
                            }
                        }
                        if (countInvalid > 1) {
                        } else {
                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
                        }

                        break;
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_INVOICE:
                    case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE:
                        if (!CollectionUtils.isEmpty(invoiceList)) {
                            for (String inv_formula : invoiceList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        countInvalid++;
                                    } else {
//                                        validFormula += "true";
                                    }

                                } else {
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(parcelList)) {
                            for (String par_formula : parcelList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                    } else {
                                        countInvalid++;
                                    }

                                } else {
                                    countInvalid++;
//                                    validFormula += "true";
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(lotList)) {
                            for (String lot_formula : lotList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                    } else {
                                        countInvalid++;
                                    }

                                } else {
                                    countInvalid++;
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(packetList)) {
                            for (String packet_formula : packetList) {
                                int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                if (indexOf >= 4) {
                                    if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                            || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                    } else {
                                        countInvalid++;
                                    }

                                } else {
                                    countInvalid++;
                                }
                            }
                        }
                        if (countInvalid > 1) {
                        } else {
                            validFormulaMap.put(field.getDbFieldName(), formulaValue);
                        }

                        break;

                }
            }

        }
        return validFormulaMap;
    }

    @Override
    public HkFranchiseSetupEntity retrieveFranchiseSetupByFranchiseSetupItem(Long franchise, String setupItem) {
        Search search = new Search(HkFranchiseSetupEntity.class);
        search.addFilterEqual(HkFranchiseSetupDetails.FRANCHISE, franchise);
        search.addFilterLike(HkFranchiseSetupDetails.SETUP_ITEM, setupItem);
        HkFranchiseSetupEntity result = (HkFranchiseSetupEntity) commonDAO.searchUnique(search);

        return result;
    }

    @Override
    public void createOrUpdateFranchiseSetup(HkFranchiseSetupEntity franchiseSetupEntity) {
        commonDAO.save(franchiseSetupEntity);
    }

    @Override
    public Map<String, Long> sectionNameIdMap() {
        Map<String, Long> sectionNameIdMap = null;
        Search search = SearchFactory.getSearch(HkSectionEntity.class);
        List<HkSectionEntity> sectionList = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(sectionList)) {
            sectionNameIdMap = new HashMap<>();
            for (HkSectionEntity section : sectionList) {
                sectionNameIdMap.put(section.getSectionName(), section.getId());
            }
        }
        return sectionNameIdMap;
    }

    @Override
    public Map<String, List<String>> featureWithSectionDetails() {
        Map<String, List<String>> featureWithSectionMap = new HashMap<>();
        List<String> assetList = new ArrayList<>();
        assetList.add(HkSystemConstantUtil.Section.ISSUE_ASSET);
        assetList.add(HkSystemConstantUtil.Section.GENERAL_SECTION_DISPLAY);
        featureWithSectionMap.put(HkSystemConstantUtil.Feature.MANAGE_ASSET, assetList);
        List<String> employeeList = new ArrayList<>();
        employeeList.add(HkSystemConstantUtil.Section.GENERAL_SECTION_DISPLAY);
        employeeList.add(HkSystemConstantUtil.Section.PERSONAL);
        employeeList.add(HkSystemConstantUtil.Section.CONTACT);
        employeeList.add(HkSystemConstantUtil.Section.EDUCATION);
        employeeList.add(HkSystemConstantUtil.Section.EXPERIENCE);
        employeeList.add(HkSystemConstantUtil.Section.POLICY);
        employeeList.add(HkSystemConstantUtil.Section.HKGWORK);
        employeeList.add(HkSystemConstantUtil.Section.OTHER);
        employeeList.add(HkSystemConstantUtil.Section.IDENTIFICATION);
        employeeList.add(HkSystemConstantUtil.Section.FAMILY);

        featureWithSectionMap.put(HkSystemConstantUtil.Feature.MANAGE_EMPLOYEE, employeeList);
        List<String> eventList = new ArrayList<>();
        eventList.add(HkSystemConstantUtil.Section.GENERAL_SECTION_DISPLAY);
        eventList.add(HkSystemConstantUtil.Section.REGISTRATION);
        eventList.add(HkSystemConstantUtil.Section.INVITATIONCARD);
        eventList.add(HkSystemConstantUtil.Section.CATEGORY);
        featureWithSectionMap.put(HkSystemConstantUtil.Feature.MANAGE_EVENT, eventList);
        return featureWithSectionMap;
    }

    @Override
    public List<HkFieldEntity> retrieveFeatureOrSectionRelatedCustomFields(Boolean isSection, Long sectionId, Long featureId, Long companyId) {
        List<HkFieldEntity> featureOrSectionCustomFields = new ArrayList<>();
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        if (isSection) {
            if (sectionId != null) {
                if (sectionId != -1L) {
                    search.addFilterEqual(HkFieldEntityField.SECTION_ID, sectionId);

                } else {
                    // sectionId=-1 
                    // for fetching general section records As we are not storing general section id in db ..

                    search.addFilterEmpty(HkFieldEntityField.SECTION_ID);
                }

            }
            if (featureId != null) {
                search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
            }
            search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
        } else {
            if (featureId != null) {
                search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
                search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
            }
        }
        search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        featureOrSectionCustomFields = commonDAO.search(search);
        return featureOrSectionCustomFields;
    }

    @Override
    public void updateSequenceNumber(Map<Long, String> sequenceNumberWithFieldId) {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = this.retrieveMapOfFieldIdAndHkFieldEntity();
        List<HkFieldEntity> fieldEntityList = null;
        if (mapOfFieldIdAndHkFieldEntity != null && !mapOfFieldIdAndHkFieldEntity.isEmpty() && sequenceNumberWithFieldId != null && !sequenceNumberWithFieldId.isEmpty()) {
            fieldEntityList = new ArrayList<>();
            for (Map.Entry<Long, String> sequenceNumber : sequenceNumberWithFieldId.entrySet()) {
                if (mapOfFieldIdAndHkFieldEntity.containsKey(sequenceNumber.getKey())) {
                    HkFieldEntity fieldEntity = mapOfFieldIdAndHkFieldEntity.get(sequenceNumber.getKey());
                    if (sequenceNumber.getValue() != null) {
                        fieldEntity.setSeqNo(Integer.parseInt(sequenceNumber.getValue()));
                    }
                    fieldEntityList.add(fieldEntity);
                }

            }
            commonDAO.saveAll(fieldEntityList);
        }
    }

    @Override
    public int retrieveNumberOfLabelsForFeature(String label, Long companyId, Long featureId, Boolean isUpdate, Long fieldId) {
        int count = 0;
        Search search = new Search(HkFieldEntity.class);
        if (featureId != null) {
            search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        }
        if (label != null) {
            search.addFilterEqual(HkFieldEntityField.LABEL, label);
        }
        if (isUpdate != null && isUpdate == true) {
            if (fieldId != null) {
                search.addFilterNotEqual(HkFieldEntityField.ID, fieldId);
            }
        }
        search.addFilterEqual(IS_ARCHIVE, Boolean.FALSE);
        search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        count = commonDAO.count(search);
        return count;
    }

    @Override
    public Map<Long, String> retrieveCustomFieldsWithConstraintsValue() {
        Map<Long, HkFieldEntity> mapOfFieldIdAndHkFieldEntity = this.retrieveMapOfFieldIdAndHkFieldEntity();
        Map<Long, String> fieldsWithConstraintValue = null;
//        Search search = new Search(HkFieldEntity.class);
//        search.addFilterEqual(IS_ARCHIVE, Boolean.FALSE);
//        search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
//        List<HkFieldEntity> fieldEntities = commonDAO.search(search);
        if (!CollectionUtils.isEmpty(mapOfFieldIdAndHkFieldEntity)) {
            fieldsWithConstraintValue = new HashMap<>();
            for (Map.Entry<Long, HkFieldEntity> fieldEntity : mapOfFieldIdAndHkFieldEntity.entrySet()) {

                String[] validationsArr = fieldEntity.getValue().getValidationPattern().replace("{", "")
                        .replace("}", "")
                        .split(",");
                String constraintValue = null;
                String pointerValue = null;
                for (String validationValue : validationsArr) {
                    if (validationValue.contains("\"constraint\":")) {
                        String[] constraintArray = validationValue.split(":");
                        constraintValue = constraintArray[1];
                        fieldsWithConstraintValue.put(fieldEntity.getKey(), constraintValue);
                    }
                    // Copy the constraint for the pointer
                    if (validationValue.contains("\"pointer\":")) {
                        String[] pointerArray = validationValue.split(":");
                        // Fetch the pointer id
                        pointerValue = pointerArray[1].replace("\"", "");
                        if (mapOfFieldIdAndHkFieldEntity.containsKey(Long.parseLong(pointerValue))) {
                            // Fetch the validation pattern of the field to which it is pointing
                            String pointerValidationPattern = mapOfFieldIdAndHkFieldEntity.get(Long.parseLong(pointerValue)).getValidationPattern();
                            String[] pointerValidationArr = pointerValidationPattern.replace("{", "")
                                    .replace("}", "")
                                    .split(",");
                            for (String pointerValidationVal : pointerValidationArr) {
                                if (pointerValidationVal.contains("\"constraint\":")) {
                                    String[] pointArr = pointerValidationVal.split(":");
                                    String pointerConstraintValue = pointArr[1];
                                    fieldsWithConstraintValue.put(fieldEntity.getKey(), pointerConstraintValue);

                                }
                            }
                        }

                    }
                }

            }
        }
        return fieldsWithConstraintValue;
    }

    @Override
    public List<HkFieldEntity> retrieveCustomFieldEntitysByComponentTypes(List<String> componentTypes, Long companyId, List<Long> featureIds) {
        Search search = new Search(HkFieldEntity.class);
        List<HkFieldEntity> fieldEntitys = new ArrayList<>();
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        if (!CollectionUtils.isEmpty(componentTypes)) {
            search.addFilterIn(HkFieldEntityField.COMPONENT_TYPE, componentTypes);
        }
        search.addFilterEqual(IS_ARCHIVE, Boolean.FALSE);
        search.addFilterEqual(HkFieldEntityField.IS_CUSTOM_FIELD, Boolean.TRUE);
        if (!CollectionUtils.isEmpty(featureIds)) {
            search.addFilterIn(HkFieldEntityField.FEATURE, featureIds);
        }
        fieldEntitys = commonDAO.search(search);
        return fieldEntitys;
    }

    public List<HkFieldEntity> retrieveFieldByFieldLabel(String label, Long featureId) {
        Search search = new Search(HkFieldEntity.class);
        search.addFilterEqual(HkFieldEntityField.FIELD_LABEL, label);
        search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        search.addFilterEqual(IS_ARCHIVE, false);
        search.addSort(HkFieldEntityField.CREATED_ON, true);
        return commonDAO.search(search);
    }

    @Override
    public List<HkFieldEntity> retrieveCustomFieldByFeatureAndComponentTypes(Long companyId, Long featureId, List<String> componentTypes, String searchParam) {
        Search search = SearchFactory.getSearch(HkFieldEntity.class);
        search.addFilterEqual(IS_ARCHIVE, false);

        if (!CollectionUtils.isEmpty(componentTypes)) {
            search.addFilterIn(HkFieldEntityField.COMPONENT_TYPE, componentTypes);
        }
        if (featureId != null) {
            search.addFilterEqual(HkFieldEntityField.FEATURE, featureId);
        }
        if (companyId != null) {
            search.addFilterIn(HkFieldEntityField.FRANCHISE, getCompnies(companyId));
        }
        if (searchParam != null) {
            search.addFilterILike(HkFieldEntityField.FIELD_LABEL, searchParam + "%");
        }
        List<HkFieldEntity> hkFieldEntityList = commonDAO.search(search);

        return hkFieldEntityList;
    }
}
