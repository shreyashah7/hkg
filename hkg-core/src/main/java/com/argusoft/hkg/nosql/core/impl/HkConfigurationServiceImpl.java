/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.core.mongotransaction.MongoTransaction;
import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.HkAssociatedDeptDocument;
import com.argusoft.hkg.nosql.model.HkAssociatedDesigDocument;
import com.argusoft.hkg.nosql.model.HkCriteriaSetDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.nosql.model.HkFeatureFieldPermissionDocument;
import com.argusoft.hkg.nosql.model.HkRoleFeatureModifierDocument;
import com.argusoft.sync.center.model.HkFeatureSectionDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import com.argusoft.sync.center.model.HkMasterValueDocument;
import com.argusoft.sync.center.model.HkSectionDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shifa
 */
@Service
public class HkConfigurationServiceImpl implements HkConfigurationService {

    @Autowired
    MongoGenericDao mongoGenericDao;
    @Autowired
    HkFoundationDocumentServiceImpl foundationDocumentService;
    @Autowired
    HkCustomFieldService customFieldService;
    public static final String DEP_ID = "department";
    public static final String DEP_NAME = "departmentName";
    public static final String ASSOCIATED_DEPARTMENTS = "associatedDepartments";
//    public static final String ASSOCIATED_DESIGNATIONS = "associatedDesignations";
    public static final String STOCK_ROOM = "stockRoom";
    public static final String DEFAULT_DEPARTMENT = "defaultDepartment";
    public static final String IS_ARCHIVE = "isArchive";
    public static String CRITERIA_SET_FOR_DEP = "criteriaRuleList";
    public static String COMPANY_ID = "franchise";
    public static String DESIGNATION = "designation";
    public static String FEATURE = "feature";

    private static class HkFeatureSectionField {

        public static final String FEATURE = "feature";
        public static final String SECTION = "section";
        public static final String SECTION_ID = "sectionId";
        public static final String IS_ARCHIVE = "isArchive";
    }

    @Override
    public Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> retrieveFeatureFieldPermissionForSearchFromConfigDocument(Long deptId, Long featureId, List<Long> designationIds, Long companyId) {
        System.out.println("in core search method...");
        Map<HkSectionDocument, List<HkFeatureFieldPermissionDocument>> sectionFieldMap = new HashMap<>();
        List<HkFeatureFieldPermissionDocument> listOfPermissionDocuments = this.retrieveListOfPermissionDocuments(designationIds, featureId, Boolean.TRUE, companyId, "GEN");
        System.out.println("listOfPermissionDocuments :"+listOfPermissionDocuments);
        List<Criteria> featureFieldPermCriteria = new ArrayList<>();
        Set<Long> sectionIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(listOfPermissionDocuments)) {
            for (HkFeatureFieldPermissionDocument field : listOfPermissionDocuments) {
                System.out.println("fieldId:::"+field.getId());
                System.out.println("fieldentity:::"+field.getHkFieldEntity());
                System.out.println("field:::"+field.toString());
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

    public static class RuleDocumentClass {

        public static final String RULE_NUMBER = "ruleNumber";
        public static final String COMPANY_ID = "franchiseId";
    }

    @Override
    public void retrieveConfigDocumentsForRemovingDepartment(Long depId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<HkDepartmentConfigDocument> retrieveAssociatedDesignationsFieldForDocuments() {
        Query query = new Query();
        query.fields().include(ASSOCIATED_DEPARTMENTS);
        query.fields().include(DEP_ID);

        List<HkDepartmentConfigDocument> departmentConfigDocs = mongoGenericDao.getMongoTemplate().find(query, HkDepartmentConfigDocument.class);
        return departmentConfigDocs;
    }

    @Override
    @MongoTransaction
    public Long createConfiguration(HkDepartmentConfigDocument hkDepartmentConfigDocument) {
        mongoGenericDao.update(hkDepartmentConfigDocument);
        return hkDepartmentConfigDocument.getDepartment();
    }

    @Override
    public HkDepartmentConfigDocument retrieveDocumentByDepartmentId(Long depId, Long companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DEP_ID).is(depId));
        query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        return mongoGenericDao.getMongoTemplate().findOne(query, HkDepartmentConfigDocument.class);
    }

//    @Override
//    public Map<Long, List<HkFeatureFieldPermissionDocument>> retrieveMapOfDesignationIdWithListOfpermissionDocument(Long depId, Long companyId) {
//        HkDepartmentConfigDocument departmentConfigDocument = this.retrieveDocumentByDepartmentId(depId, companyId);
//        Map<Long, List<HkFeatureFieldPermissionDocument>> mapOfDesgIdWithPermissionList = null;
//        if (departmentConfigDocument != null) {
//
//            List<HkAssociatedDesigDocument> associatedDesignationsList = departmentConfigDocument.getAssociatedDesignations();
//
//            if (!CollectionUtils.isEmpty(associatedDesignationsList)) {
//                mapOfDesgIdWithPermissionList = new HashMap<>();
//                for (HkAssociatedDesigDocument associatedDesignation : associatedDesignationsList) {
//                    List<String> featureFieldPermissionIds = null;
//                    List<HkFeatureFieldPermissionDocument> featureFieldPermissionsForAssDesgList = associatedDesignation.getFeatureFieldPermissions();
//                    if (!CollectionUtils.isEmpty(featureFieldPermissionsForAssDesgList)) {
//                        featureFieldPermissionIds = new ArrayList<>();
//                        for (HkFeatureFieldPermissionDocument featureFieldPermissionsForAssDesg : featureFieldPermissionsForAssDesgList) {
//                            featureFieldPermissionIds.add(featureFieldPermissionsForAssDesg.getId());
//                        }
//                    }
//                    mapOfDesgIdWithPermissionList.put(associatedDesignation.getDesignation(), associatedDesignation.getFeatureFieldPermissions());
//                }
//            }
//        }
//        return mapOfDesgIdWithPermissionList;
//
//    }

    @Override
    public List<HkDepartmentConfigDocument> searchConfigurationsByDepartmentName(String query) {
        List<HkDepartmentConfigDocument> hkDepartmentConfigDocList = null;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where(DEP_NAME).regex(query, "i"));
        criteriaList.add(Criteria.where(IS_ARCHIVE).in(Boolean.FALSE));
        hkDepartmentConfigDocList = mongoGenericDao.findByCriteria(criteriaList, HkDepartmentConfigDocument.class);
        return hkDepartmentConfigDocList;
    }
    
    @Override
    public List<Long> retrieveAvailableDepartmentConfigurations(Long companyId) {
        List<Long> depIds = new ArrayList<>();
        Query query = new Query();
        query.fields().include(DEP_ID);
        query.addCriteria(Criteria.where(COMPANY_ID).is(companyId));
        query.addCriteria(Criteria.where(IS_ARCHIVE).is(false));
        List<HkDepartmentConfigDocument> departmentConfigDocs = mongoGenericDao.getMongoTemplate().find(query, HkDepartmentConfigDocument.class);
        if (!CollectionUtils.isEmpty(departmentConfigDocs)) {
            for (HkDepartmentConfigDocument configDocument : departmentConfigDocs) {
                depIds.add(configDocument.getDepartment());
            }
        }
        return depIds;
    }

    @Override
    public List<HkDepartmentConfigDocument> retrieveConfigDocumentsForRemovingDepartment() {
        Query query = new Query();
        query.fields().include(ASSOCIATED_DEPARTMENTS);
        query.fields().include(DEP_ID);
        query.fields().include(STOCK_ROOM);
        query.fields().include(DEFAULT_DEPARTMENT);
        List<HkDepartmentConfigDocument> departmentConfigDocs = mongoGenericDao.getMongoTemplate().find(query, HkDepartmentConfigDocument.class);
        return departmentConfigDocs;
    }

    @Override
    public Map<Long, HkAssociatedDeptDocument> retrieveMapOfAssociatedDepartmentsForDept(Long deptId) {
        Query query = new Query();
        query.fields().include(ASSOCIATED_DEPARTMENTS);
        query.addCriteria(Criteria.where(DEP_ID).is(deptId));
        Map<Long, HkAssociatedDeptDocument> mapOfAssociatedDeptIdWithDoc = null;
        System.out.println(query);
        List<HkDepartmentConfigDocument> deptConfigDocs = mongoGenericDao.getMongoTemplate().find(query, HkDepartmentConfigDocument.class);
        if (!CollectionUtils.isEmpty(deptConfigDocs)) {
            mapOfAssociatedDeptIdWithDoc = new HashMap<>();
            for (HkDepartmentConfigDocument deptConfigDoc : deptConfigDocs) {
                List<HkAssociatedDeptDocument> associatedDepartments = deptConfigDoc.getAssociatedDepartments();
                if (!CollectionUtils.isEmpty(associatedDepartments)) {
                    for (HkAssociatedDeptDocument associatedDepartment : associatedDepartments) {
                        mapOfAssociatedDeptIdWithDoc.put(associatedDepartment.getDepartment(), associatedDepartment);
                    }
                }
            }

        }
        return mapOfAssociatedDeptIdWithDoc;
    }

    @MongoTransaction
    @Override
    public void createOrUpdateHkPermissionDocumentsForDesignation(List<HkFeatureFieldPermissionDocument> fieldPermissionDocs) {
        if(!CollectionUtils.isEmpty(fieldPermissionDocs)){
            for (HkFeatureFieldPermissionDocument fieldPermissionDoc : fieldPermissionDocs) {
                mongoGenericDao.update(fieldPermissionDoc);
            }
        }
    }

    @Override
    public Map<Long, HkCriteriaSetDocument> mapOfretrieveRuleListForDepartment(Long depId) {

        System.out.println("in core....");
        Query query = new Query();
        query.fields().include(CRITERIA_SET_FOR_DEP);
        query.addCriteria(Criteria.where(DEP_ID).is(depId));
        Map<Long, HkCriteriaSetDocument> mapOfRuleIdWithDoc = null;
        System.out.println(query);
        List<HkDepartmentConfigDocument> deptConfigDocs = mongoGenericDao.getMongoTemplate().find(query, HkDepartmentConfigDocument.class);
        if (!CollectionUtils.isEmpty(deptConfigDocs)) {
            mapOfRuleIdWithDoc = new HashMap<>();
            System.out.println("doc not empty");
            for (HkDepartmentConfigDocument deptConfigDoc : deptConfigDocs) {
                List<HkCriteriaSetDocument> criteraiRules = deptConfigDoc.getCriteriaRuleList();
                if (!CollectionUtils.isEmpty(criteraiRules)) {
                    for (HkCriteriaSetDocument rule : criteraiRules) {
                        mapOfRuleIdWithDoc.put(rule.getRuleNumber(), rule);
                    }
                }
            }

        }
        return mapOfRuleIdWithDoc;
    }

    @Override
    public Long retrieveMaxCountOfRule(Long companyId) {
        Long nextRuleNumber = 0L;
        HkCriteriaSetDocument lastRuleDocument = null;
        Query query = new Query();
        query.addCriteria(Criteria.where(RuleDocumentClass.COMPANY_ID).is(companyId));
        query.with(new Sort(Sort.Direction.DESC, RuleDocumentClass.RULE_NUMBER));
        lastRuleDocument = mongoGenericDao.getMongoTemplate().findOne(query, HkCriteriaSetDocument.class);

        if (lastRuleDocument != null) {
            nextRuleNumber = lastRuleDocument.getRuleNumber();
        }
//        nextRuleNumber += 1;
        return nextRuleNumber;
    }

    @Override
    public void updateAllConfigDocuments(List<HkDepartmentConfigDocument> configDocs) {
        if (!CollectionUtils.isEmpty(configDocs)) {
            for (HkDepartmentConfigDocument configDoc : configDocs) {
                mongoGenericDao.update(configDoc);
            }
        }
    }

    @Override
    public List<HkDepartmentConfigDocument> retrieveAllDepartmentConfigurationDocumentsForProcessFlow(Long companyId) {
        List<HkDepartmentConfigDocument> hkDepartmentConfigDocList = null;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        criteriaList.add(Criteria.where(COMPANY_ID).is(companyId));
        hkDepartmentConfigDocList = mongoGenericDao.findByCriteria(criteriaList, HkDepartmentConfigDocument.class);
        return hkDepartmentConfigDocList;
    }

    @Override
    public List<HkFeatureFieldPermissionDocument> retrieveListOfPermissionDocuments(List<Long> roleIds, Long featureId, Boolean isSearch, Long companyId, String sectionCode) {
        List<HkFeatureFieldPermissionDocument> totalFieldPermissionDocuments = null;

        totalFieldPermissionDocuments = new ArrayList<>();
        List<HkFeatureFieldPermissionDocument> featureFieldPermissionsForAssDesgList = customFieldService.retrieveFeatureFieldPermissionsByDesignationsFeatureAndSection(roleIds, Arrays.asList(featureId), sectionCode, companyId);
        if (!CollectionUtils.isEmpty(featureFieldPermissionsForAssDesgList)) {
            System.out.println("documents retrieveed....");
            for (HkFeatureFieldPermissionDocument permissionDoc : featureFieldPermissionsForAssDesgList) {
                if (permissionDoc != null) {
                    System.out.println("permission doc  not empty");
                    Boolean checkCondition = false;
                    if (isSearch) {
                        System.out.println("permission f" + permissionDoc);
                        if (Boolean.valueOf(permissionDoc.getSearchFlag().toString())) {
                            checkCondition = true;
                        } else {
                            checkCondition = false;
                        }
                    } else {
                        checkCondition = true;
                    }
                    if (permissionDoc.getFeature().equals(featureId) && checkCondition && !permissionDoc.isIsArchive()) {

                        if (!CollectionUtils.isEmpty(totalFieldPermissionDocuments)) {
                            if (!totalFieldPermissionDocuments.contains(permissionDoc)) {
                                totalFieldPermissionDocuments.add(permissionDoc);
                            }
                        } else {
                            totalFieldPermissionDocuments.add(permissionDoc);
                        }
                    }
                }
            }
        }

        return totalFieldPermissionDocuments;

    }

    @Override
    @MongoTransaction
    public void saveRoleFeatureModifiers(List<HkRoleFeatureModifierDocument> roleFeatureModifierDocuments) {
        if (!CollectionUtils.isEmpty(roleFeatureModifierDocuments)) {
            List<HkRoleFeatureModifierDocument> toBeAddedDocuments = new ArrayList<>();
            for (HkRoleFeatureModifierDocument modifier : roleFeatureModifierDocuments) {
                if (modifier.getId() != null) {
                    mongoGenericDao.update(modifier);
                } else {
                    toBeAddedDocuments.add(modifier);
                }
            }
            if (!CollectionUtils.isEmpty(toBeAddedDocuments)) {
                mongoGenericDao.createAll(toBeAddedDocuments);
            }
        }
    }

    @Override
    public List<HkRoleFeatureModifierDocument> retrieveModifiersByDesignations(List<Long> designationIds, Long franchiseId, Long featureId) {
        List<HkRoleFeatureModifierDocument> modifiers = null;
        List<Criteria> criteriaList = new ArrayList<>();
        if (featureId != null) {
            criteriaList.add(Criteria.where(FEATURE).is(featureId));
        }
        if (franchiseId != null) {
            criteriaList.add(Criteria.where(COMPANY_ID).is(franchiseId));
        }
        criteriaList.add(Criteria.where(IS_ARCHIVE).is(Boolean.FALSE));
        if (!CollectionUtils.isEmpty(designationIds)) {
            criteriaList.add(Criteria.where(DESIGNATION).in(designationIds));
        }
        modifiers = mongoGenericDao.findByCriteria(criteriaList, HkRoleFeatureModifierDocument.class);
        return modifiers;
    }
        }
