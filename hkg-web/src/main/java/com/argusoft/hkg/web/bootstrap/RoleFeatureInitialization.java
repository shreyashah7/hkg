/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.bootstrap;

import com.argusoft.generic.database.common.GenericDao;
import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.model.HkFeatureSectionEntity;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.web.usermanagement.databeans.FieldDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.usermanagement.common.constants.UMUserManagementConstants;
import com.argusoft.usermanagement.common.core.UMFeatureService;
import com.argusoft.usermanagement.common.core.UMRoleService;
import com.argusoft.usermanagement.common.database.UMFeatureDao;
import com.argusoft.usermanagement.common.model.UMFeature;
import com.argusoft.usermanagement.common.model.UMRole;
import com.argusoft.usermanagement.common.model.UMRoleFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author anita
 */
@Service
public class RoleFeatureInitialization {

    @Autowired
    UMFeatureService featureService;

    @Autowired
    UMRoleService roleService;

    @Autowired
    HkFieldService fieldService;

//    @Autowired
    @Autowired
    UserManagementServiceWrapper serviceWrapper;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RoleFeatureInitialization.class);

    public void UpdateFeatureDetailUsingXls() {
        try {
            ClassLoader cLoader = this.getClass().getClassLoader();
            InputStream inpstr = cLoader.getResourceAsStream("featuresDetail.xlsx");
            Map<String, UMFeature> features = new LinkedHashMap<>();
            List<String> featureNames = new ArrayList<>();
            List<String> featureParentNames = new ArrayList<>();
            List<String> featuresNeedToRemove = new LinkedList<>();
            Map featureParentMapping = new HashMap();
            List<Long> featuresToUpdate = new LinkedList<>();
            List<UMFeature> finalFeatureList = new LinkedList();

            Workbook wb = WorkbookFactory.create(inpstr);

            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);

                if (sheet != null) {
                    int i = 0;
                    for (Row row : sheet) {
                        if (i != 0) {
                            String webserviceUrl = null;
                            if (row.getCell(2) != null && !StringUtils.isEmpty(row.getCell(2).getStringCellValue())) {
                                UMFeature feature = new UMFeature();
                                if (row.getCell(0) != null && !StringUtils.isEmpty(row.getCell(0).toString())) {
                                }
                                if (row.getCell(2) != null && !StringUtils.isEmpty(row.getCell(2).getStringCellValue())) {
                                    feature.setName(row.getCell(2).getStringCellValue().trim());
                                    if (featureNames.contains(row.getCell(2).getStringCellValue())) {
                                        continue;
                                    }
                                    featureNames.add(row.getCell(2).getStringCellValue());
                                }

                                if (row.getCell(3) != null && !StringUtils.isEmpty(row.getCell(3).getStringCellValue())) {
                                    featureParentNames.add(row.getCell(3).getStringCellValue());
                                    featureParentMapping.put(row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue());
                                }
                                if (row.getCell(1) != null && !StringUtils.isEmpty(row.getCell(1).getStringCellValue())) {
                                    feature.setMenuLabel(row.getCell(1).getStringCellValue().trim());
                                }
                                if (row.getCell(4) != null && !StringUtils.isEmpty(row.getCell(4).getStringCellValue())) {
                                    feature.setMenuType(row.getCell(4).getStringCellValue().trim());
                                }
                                if (row.getCell(5) != null && !StringUtils.isEmpty(row.getCell(5).getStringCellValue())) {
                                    feature.setDescription(row.getCell(5).getStringCellValue().trim());
                                }
                                if (row.getCell(6) != null && !StringUtils.isEmpty(row.getCell(6).getStringCellValue())) {
                                    feature.setFeatureUrl(row.getCell(6).getStringCellValue().replaceAll("[\\n\\t ]", ""));
                                }
                                if (row.getCell(7) != null && !StringUtils.isEmpty(row.getCell(7).getStringCellValue())) {
                                    webserviceUrl = row.getCell(7).getStringCellValue();
                                    feature.setWebserviceUrl(webserviceUrl.replaceAll("[\\n\\t ]", ""));
                                }
                                if (row.getCell(8) != null && !StringUtils.isEmpty(row.getCell(8).getStringCellValue())) {
                                    if (webserviceUrl != null && !StringUtils.isEmpty(webserviceUrl)) {
                                        webserviceUrl = webserviceUrl + "," + row.getCell(8).getStringCellValue();
                                    } else {
                                        webserviceUrl = row.getCell(8).getStringCellValue();
                                    }
                                    feature.setWebserviceUrl(webserviceUrl.replaceAll("[\\n\\t ]", ""));
                                }
                                if (row.getCell(9) != null) {
                                    Double seqNo = row.getCell(9).getNumericCellValue();
                                    feature.setSeqNo(seqNo.intValue());
                                }
                                if (row.getCell(10) != null) {
                                    Double precedence = row.getCell(10).getNumericCellValue();
                                    feature.setPrecedence(precedence.intValue());

                                }
                                if (row.getCell(11) != null) {
                                    feature.setMenuCategory(row.getCell(11).getStringCellValue().trim());

                                }
                                features.put(feature.getName(), feature);
                            }
                        }
                        i++;
                    }

                    if (!CollectionUtils.isEmpty(featureNames)) {
                        List<UMFeature> existsingFeatures = serviceWrapper.retrieveAllFeature(null);
                        if (!CollectionUtils.isEmpty(existsingFeatures)) {
                            for (UMFeature existingUMFeature : existsingFeatures) {
                                if (features.containsKey(existingUMFeature.getName())) {
                                    UMFeature umFeature = features.get(existingUMFeature.getName());
                                    umFeature.setId(existingUMFeature.getId());
                                    umFeature.setSeqNo(existingUMFeature.getSeqNo());
                                    features.put(existingUMFeature.getName(), umFeature);
                                    if (!toStringUMFeature(umFeature).equals(toStringUMFeature(existingUMFeature))) {
                                        featuresToUpdate.add(existingUMFeature.getId());
                                    }
                                } else {
                                    featuresNeedToRemove.add(existingUMFeature.getName());
                                }
                            }

                        }
                    }
                    if (!CollectionUtils.isEmpty(featuresNeedToRemove)) {
                        List<UMFeature> existsingFeatures = serviceWrapper.retrieveFeatureByName(featuresNeedToRemove);
                        List<Long> featureIds = new LinkedList<>();
                        for (UMFeature existingUMFeature : existsingFeatures) {
                            existingUMFeature.setIsArchive(true);
                            existingUMFeature.setIsActive(false);
                            featureIds.add(existingUMFeature.getId());
                        }
                        featureService.updateAllFeature(existsingFeatures);
                        serviceWrapper.removeRoleFeatures(featureIds);
                    }

                    Map<String, UMFeature> parentIdMap = new HashMap<>();
                    Map<String, UMFeature> parentIdInSystemMap = new HashMap<>();
                    for (UMFeature uMFeature : features.values()) {

                        for (String name : featureParentNames) {
                            if (uMFeature.getName().equalsIgnoreCase(name)) {
                                parentIdMap.put(uMFeature.getName(), uMFeature);
                            }
                        }
                    }

                    if (!CollectionUtils.isEmpty(features)) {
                        parentIdInSystemMap = serviceWrapper.getAllParentByFeatureName(featureParentNames);

                        for (UMFeature uMFeature : features.values()) {
                            List<String> texts = new ArrayList<>();
                            if (uMFeature != null && (uMFeature.getId() == null || featuresToUpdate.contains(uMFeature.getId()))) {
                                uMFeature.setCreatedBy(1L);
                                uMFeature.setCreatedOn(new Date());
                                uMFeature.setModifiedBy(1L);
                                uMFeature.setCompany(0L);
                                uMFeature.setModifiedOn(new Date());
                                uMFeature.setIsActive(Boolean.TRUE);
                                uMFeature.setIsArchive(Boolean.FALSE);
                                if (featureParentMapping.get(uMFeature.getName()) != null) {
                                    if (parentIdMap.get(featureParentMapping.get(uMFeature.getName())) != null) {
                                        uMFeature.setParent(parentIdMap.get(featureParentMapping.get(uMFeature.getName())));
                                    } else if (parentIdInSystemMap.get(featureParentMapping.get(uMFeature.getName())) != null) {
                                        uMFeature.setParent(parentIdInSystemMap.get(featureParentMapping.get(uMFeature.getName())));
                                    }
                                }
                                texts.add(uMFeature.getMenuLabel());
                                finalFeatureList.add(uMFeature);
                            }
                            if (!CollectionUtils.isEmpty(texts)) {
                                serviceWrapper.createAllLocaleForEntity(texts, "Feature", 1l, 0l);
                            }
                        }
                        if (!CollectionUtils.isEmpty(finalFeatureList)) {
                            featureService.createAllFeature(finalFeatureList);
                        }

                        authenticationRoleFeatureIntialization();
                    }
                }
            }
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void authenticationRoleFeatureIntialization() {
        Boolean fullyAuthRoleExists = roleService.isRoleExist(UMUserManagementConstants.IS_AUTHENTICATED_FULLY, null, null, null);
        Boolean anonymousAuthRoleExits = roleService.isRoleExist(UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY, null, null, null);

        if (!fullyAuthRoleExists) {
            UMRole authenticationFullyRole = new UMRole();
            authenticationFullyRole.setCompany(0L);
            authenticationFullyRole.setCreatedBy(-1L);
            authenticationFullyRole.setCreatedOn(new Date());
            authenticationFullyRole.setDescription("Role containing fully authenticated features.");
            authenticationFullyRole.setIsActive(Boolean.TRUE);
            authenticationFullyRole.setIsArchive(Boolean.FALSE);
            authenticationFullyRole.setName(UMUserManagementConstants.IS_AUTHENTICATED_FULLY);
            authenticationFullyRole.setPrecedence(-1);

            roleService.createRole(authenticationFullyRole);

            UMRoleFeature authenticationFullyRoleFeature = new UMRoleFeature();
            authenticationFullyRoleFeature.setAllowToCreate(Boolean.FALSE);
            authenticationFullyRoleFeature.setAllowToDelete(Boolean.FALSE);
            authenticationFullyRoleFeature.setAllowToUpdate(Boolean.FALSE);
            authenticationFullyRoleFeature.setCompany(0L);
            authenticationFullyRoleFeature.setDescription("Fully authenticated role and features mapping.");
            authenticationFullyRoleFeature.setIsActive(Boolean.TRUE);
            authenticationFullyRoleFeature.setIsArchive(Boolean.FALSE);
            List<UMFeature> features = featureService.retrieveFeatureByName(UMUserManagementConstants.IS_AUTHENTICATED_FULLY);
            if (features.size() == 1) {
                authenticationFullyRoleFeature.setFeature(features.get(0));
                authenticationFullyRoleFeature.setRole(authenticationFullyRole);
                roleService.createRoleFeature(authenticationFullyRoleFeature);
            }

        }
        if (!anonymousAuthRoleExits) {
            UMRole authenticationFullyRole = new UMRole();
            authenticationFullyRole.setCompany(0L);
            authenticationFullyRole.setCreatedBy(-1L);
            authenticationFullyRole.setCreatedOn(new Date());
            authenticationFullyRole.setDescription("Role containing anonymous authenticated features.");
            authenticationFullyRole.setIsActive(Boolean.TRUE);
            authenticationFullyRole.setIsArchive(Boolean.FALSE);
            authenticationFullyRole.setName(UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY);
            authenticationFullyRole.setPrecedence(-1);

            roleService.createRole(authenticationFullyRole);

            UMRoleFeature authenticationFullyRoleFeature = new UMRoleFeature();
            authenticationFullyRoleFeature.setAllowToCreate(Boolean.FALSE);
            authenticationFullyRoleFeature.setAllowToDelete(Boolean.FALSE);
            authenticationFullyRoleFeature.setAllowToUpdate(Boolean.FALSE);
            authenticationFullyRoleFeature.setCompany(0L);
            authenticationFullyRoleFeature.setDescription("Anonymous authenticated role and features mapping.");
            authenticationFullyRoleFeature.setIsActive(Boolean.TRUE);
            authenticationFullyRoleFeature.setIsArchive(Boolean.FALSE);
            List<UMFeature> features = featureService.retrieveFeatureByName(UMUserManagementConstants.IS_AUTHENTICATED_ANONYMOUSLY);
            if (features.size() == 1) {
                authenticationFullyRoleFeature.setFeature(features.get(0));
                authenticationFullyRoleFeature.setRole(authenticationFullyRole);
                roleService.createRoleFeature(authenticationFullyRoleFeature);
            }
        }

    }

    public void createFeatureSectionField() {
        try {
            ClassLoader cLoader = this.getClass().getClassLoader();
            InputStream inpstr = cLoader.getResourceAsStream("featuresDetail.xlsx");

            Workbook wb = WorkbookFactory.create(inpstr);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(3);
                if (sheet != null) {
                    int i = 0;
                    Map<String, Map<String, List<FieldDataBean>>> featureSectionFieldMap = new HashMap<>();
                    Map<String, List<FieldDataBean>> featureWithoutSectionMap = new HashMap<>();
                    Map<String, List<HkSectionEntity>> diamondFeatureSectionMap = new HashMap<>();
                    String curfeatureName = null;
                    String curSectionName = null;
                    for (Row row : sheet) {
                        if (i != 0) {

                            if (row.getCell(1) != null && !StringUtils.isEmpty(row.getCell(1).getStringCellValue())) {
                                curfeatureName = row.getCell(1).getStringCellValue();
                                //If not a dummy section for diamond
                                if (row.getCell(14) == null || StringUtils.isEmpty(row.getCell(14).getStringCellValue())) {
                                    featureSectionFieldMap.put(curfeatureName, new HashMap<String, List<FieldDataBean>>());
                                }

                            }
                            if (row.getCell(14) == null || StringUtils.isEmpty(row.getCell(14).getStringCellValue())) {
                                Cell sectionName = row.getCell(2);
                                if (sectionName != null && !StringUtils.isEmpty(sectionName.getStringCellValue()) && !sectionName.getStringCellValue().equalsIgnoreCase(HkSystemConstantUtil.DEFAULT_SECTION)) {
                                    Map<String, List<FieldDataBean>> sectionMap = featureSectionFieldMap.get(curfeatureName);
                                    curSectionName = row.getCell(2).getStringCellValue();
                                    if (sectionMap.get(curSectionName) == null) {
                                        sectionMap.put(curSectionName, new ArrayList<>());
                                    }
                                    List<FieldDataBean> fieldDataBeans = sectionMap.get(curSectionName);
                                    FieldDataBean fieldDataBean = new FieldDataBean();
                                    fieldDataBean.setFieldLabel(row.getCell(3).getStringCellValue());
                                    fieldDataBean.setFieldType(row.getCell(4).getStringCellValue());
                                    fieldDataBean.setComponentType(row.getCell(5).getStringCellValue());
                                    fieldDataBean.setUiFieldName(row.getCell(6).getStringCellValue());
                                    fieldDataBean.setDbBaseName(row.getCell(7).getStringCellValue());
                                    fieldDataBean.setValidationPattern(row.getCell(8).getStringCellValue());
                                    fieldDataBean.setDbFieldName(row.getCell(9).getStringCellValue());
                                    if (row.getCell(10) != null) {
                                        fieldDataBean.setDbBaseType(row.getCell(10).getStringCellValue());
                                    }
                                    if (row.getCell(11) != null) {
                                        if (row.getCell(11).getStringCellValue().equalsIgnoreCase("T")) {
                                            fieldDataBean.setIsCustom(Boolean.TRUE);
                                        } else {
                                            fieldDataBean.setIsCustom(Boolean.FALSE);
                                        }
                                    }
                                    if (row.getCell(12) != null) {
                                        if (row.getCell(12).getStringCellValue().equalsIgnoreCase("T")) {
                                            fieldDataBean.setIsEditable(Boolean.TRUE);
                                        } else {
                                            fieldDataBean.setIsEditable(Boolean.FALSE);
                                        }
                                    }
                                    if (row.getCell(13) != null) {
                                        fieldDataBean.setFormulaValue(row.getCell(13).getStringCellValue());
                                    }
                                    fieldDataBeans.add(fieldDataBean);
                                } else {
                                    //System.out.println("in else if &&&&&&&&&&&&&&& curr section null" + curfeatureName);
                                    List<FieldDataBean> fieldDataBeans = null;
                                    if (!featureWithoutSectionMap.containsKey(curfeatureName)) {
                                        fieldDataBeans = new ArrayList<>();
                                    } else {
                                        fieldDataBeans = featureWithoutSectionMap.get(curfeatureName);
                                    }
                                    FieldDataBean fieldDataBean = new FieldDataBean();
                                    if (row.getCell(3) != null) {
                                        fieldDataBean.setFieldLabel(row.getCell(3).getStringCellValue());
                                    }
                                    if (row.getCell(4) != null) {
                                        fieldDataBean.setFieldType(row.getCell(4).getStringCellValue());
                                    }
                                    if (row.getCell(5) != null) {
                                        fieldDataBean.setComponentType(row.getCell(5).getStringCellValue());
                                    }
                                    if (row.getCell(6) != null) {
                                        fieldDataBean.setUiFieldName(row.getCell(6).getStringCellValue());
                                    }
                                    if (row.getCell(7) != null) {
                                        fieldDataBean.setDbBaseName(row.getCell(7).getStringCellValue());
                                    }
                                    if (row.getCell(8) != null) {
                                        if (row.getCell(8).getStringCellValue().equalsIgnoreCase("p1")) {
                                            fieldDataBean.setValidationPattern("{}");
                                        } else {
                                            fieldDataBean.setValidationPattern(row.getCell(8).getStringCellValue());
                                        }
                                    }
                                    if (row.getCell(9) != null) {
                                        fieldDataBean.setDbFieldName(row.getCell(9).getStringCellValue());
                                    }
                                    if (row.getCell(10) != null) {
                                        fieldDataBean.setDbBaseType(row.getCell(10).getStringCellValue());
                                    }
                                    if (row.getCell(11) != null) {
                                        if (row.getCell(11).getStringCellValue().equalsIgnoreCase("T")) {
                                            fieldDataBean.setIsCustom(Boolean.TRUE);
                                        } else {
                                            fieldDataBean.setIsCustom(Boolean.FALSE);
                                        }
                                    }
                                    if (row.getCell(12) != null) {
                                        if (row.getCell(12).getStringCellValue().equalsIgnoreCase("T")) {
                                            fieldDataBean.setIsEditable(Boolean.TRUE);
                                        } else {
                                            fieldDataBean.setIsEditable(Boolean.FALSE);
                                        }
                                    }
                                    if (row.getCell(13) != null) {
                                        fieldDataBean.setFormulaValue(row.getCell(13).getStringCellValue());
                                    }
                                    fieldDataBeans.add(fieldDataBean);
                                    featureWithoutSectionMap.put(curfeatureName, fieldDataBeans);

                                }

                            } else {
                                //Add diamond sections.
                                if (!diamondFeatureSectionMap.containsKey(curfeatureName)) {
                                    diamondFeatureSectionMap.put(curfeatureName, new ArrayList<>());
                                }
                                //Construct section entity.
                                HkSectionEntity sectionEntity = new HkSectionEntity();
                                sectionEntity.setSectionCode(row.getCell(14).getStringCellValue());
                                sectionEntity.setSectionName(row.getCell(2).getStringCellValue());
                                diamondFeatureSectionMap.get(curfeatureName).add(sectionEntity);
                            }
                        }
                        i++;
                    }
                    this.createDiamondSections(diamondFeatureSectionMap);
                    this.createSectionAndField(featureSectionFieldMap);
                    if (featureWithoutSectionMap.size() > 0) {
                        //System.out.println("in if without section ****************************************");
                        createNewFields(featureWithoutSectionMap);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createDiamondSections(Map<String, List<HkSectionEntity>> diamondFeatureSectionMap) {
        if (!CollectionUtils.isEmpty(diamondFeatureSectionMap)) {
            Map<String, Map<String, Object>> sectionCodeToDetailsMap = new HashMap<>();

            try {
                Map<String, Object> in = new HashMap<>();
                in.put(UMFeatureDao.NAME, new ArrayList<>(diamondFeatureSectionMap.keySet()));
                Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
                criterias.put(GenericDao.QueryOperators.IN, in);
                List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);
                Map<String, Long> featureNameIdMap = new HashMap<>();
                Map<Long, String> featureIdToNameMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(features)) {
                    List<Long> featureIds = new ArrayList<>();
                    for (UMFeature uMFeature : features) {
                        featureIds.add(uMFeature.getId());
                        featureNameIdMap.put(uMFeature.getName(), uMFeature.getId());
                        featureIdToNameMap.put(uMFeature.getId(), uMFeature.getName());
                    }

                    for (Map.Entry<String, List<HkSectionEntity>> entry : diamondFeatureSectionMap.entrySet()) {
                        for (HkSectionEntity section : entry.getValue()) {
                            Map<String, Object> sectionDetailsMap = new HashMap<>();
                            sectionDetailsMap.put("feature", featureNameIdMap.get(entry.getKey()));
                            sectionDetailsMap.put("sectionName", section.getSectionName());
                            sectionCodeToDetailsMap.put(section.getSectionCode(), sectionDetailsMap);
                        }
                    }

                    List<HkFeatureSectionEntity> retrieveFeatureSectionEntitiesByFeatureIds = fieldService.retrieveFeatureSectionEntitiesByFeatureIds(featureIds);
                    Map<Long, List<HkSectionEntity>> sectionToBeCreated = new HashMap<>();
                    List<HkSectionEntity> sectionsToBeRemoved = new ArrayList<>();
                    List<HkSectionEntity> sectionsToBeUpdated = new ArrayList<>();

                    if (!CollectionUtils.isEmpty(retrieveFeatureSectionEntitiesByFeatureIds)) {
                        for (HkFeatureSectionEntity featureSectionEntity : retrieveFeatureSectionEntitiesByFeatureIds) {
                            HkSectionEntity section = featureSectionEntity.getSection();
                            if (!sectionCodeToDetailsMap.keySet().contains(section.getSectionCode())) {
                                //Remove section if not exist in sheet.
                                sectionsToBeRemoved.add(section);
                            } else {
                                String sectionName = sectionCodeToDetailsMap.get(section.getSectionCode()).get("sectionName").toString();
                                if (!sectionName.equalsIgnoreCase(section.getSectionName())) {
                                    section.setSectionName(sectionName);
                                    sectionsToBeUpdated.add(section);
                                }
                                sectionCodeToDetailsMap.remove(section.getSectionCode());
                            }
                        }
                        fieldService.removeSections(sectionsToBeRemoved);
                        fieldService.updateSections(sectionsToBeUpdated);
                    }

                    if (!CollectionUtils.isEmpty(sectionCodeToDetailsMap)) {

                        for (Map.Entry<String, Map<String, Object>> entry : sectionCodeToDetailsMap.entrySet()) {
                            String sectionCode = entry.getKey();
                            Map<String, Object> sectionDetails = entry.getValue();
                            Long featureId = Long.parseLong(sectionDetails.get("feature").toString());
                            HkSectionEntity hkSectionEntity = new HkSectionEntity();
                            hkSectionEntity.setCreatedOn(new Date());
                            hkSectionEntity.setIsArchive(false);
                            hkSectionEntity.setSectionName(sectionDetails.get("sectionName").toString());
                            hkSectionEntity.setSectionCode(sectionCode);
                            if (sectionToBeCreated.get(featureId) == null) {
                                sectionToBeCreated.put(featureId, new ArrayList<>());
                            }
                            sectionToBeCreated.get(featureId).add(hkSectionEntity);
                        }

                    }
                    fieldService.createSections(sectionToBeCreated);
                }
            } catch (GenericDatabaseException ex) {
                Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Remove all diamond sections if there are not any in sheet.
            List<HkFeatureSectionEntity> retrieveFeatureSectionEntities = fieldService.retrieveFeatureSectionEntitiesByFeatureIds(null);
            List<HkSectionEntity> sectionsToBeRemoved = new ArrayList<>();
            if (!CollectionUtils.isEmpty(retrieveFeatureSectionEntities)) {
                for (HkFeatureSectionEntity featureSectionEntity : retrieveFeatureSectionEntities) {
                    if (featureSectionEntity.getSection().getSectionCode() != null && !StringUtils.isEmpty(featureSectionEntity.getSection().getSectionCode())) {
                        sectionsToBeRemoved.add(featureSectionEntity.getSection());
                    }
                }
            }
            fieldService.removeSections(sectionsToBeRemoved);
        }
    }

    public void createNewFields(Map<String, List<FieldDataBean>> featureWithoutSectionMap) {
        //System.out.println("Here ---------------------------------------------------------->>" + featureWithoutSectionMap.size());
        Map<String, String> deployTimePointerMaps = new HashMap<String, String>(HkSystemConstantUtil.DEPLOY_TIME_POINTERS_MAP);
        List<String> featureNames = new ArrayList<>();
        Map<Long, String> featureIdToNameMap = new HashMap<Long, String>();
        Map<String, Long> featureNameToIdMap = new HashMap<String, Long>();
        for (Map.Entry<String, List<FieldDataBean>> entry : featureWithoutSectionMap.entrySet()) {
            String featureName = entry.getKey();
            featureNames.add(featureName);
        }
        try {
            Map<String, Object> in = new HashMap<>();
            in.put(UMFeatureDao.NAME, featureNames);

            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, in);
            List<String> projections = new ArrayList<>();
            projections.add(UMFeatureDao.ID);
            projections.add(UMFeatureDao.NAME);
            List<UMFeature> features = featureService.retrieveFeatures(projections, criterias, projections);
            List<Long> featureIds = null;
            if (!CollectionUtils.isEmpty(features)) {
                featureIds = new ArrayList<>();
                for (UMFeature uMFeature : features) {
                    featureIds.add(uMFeature.getId());
                    featureIdToNameMap.put(uMFeature.getId(), uMFeature.getName());
                    featureNameToIdMap.put(uMFeature.getName(), uMFeature.getId());
                }

                //System.out.println("Feature ids : " + featureIds);
                List<HkFieldEntity> fieldEntitys = fieldService.retrieveFieldsByFeatures(featureIds, null, 0l);
                if (!CollectionUtils.isEmpty(fieldEntitys)) {
                    //System.out.println("Not null ");
                    Map<Long, List<HkFieldEntity>> dbMap = new HashMap<>();
                    List<HkFieldEntity> hkFieldEntitys = null;
                    for (HkFieldEntity hkFieldEntity : fieldEntitys) {
                        if (hkFieldEntity.getSection() == null) {
                            if (!dbMap.containsKey(hkFieldEntity.getFeature())) {
                                hkFieldEntitys = new ArrayList<>();
                                hkFieldEntitys.add(hkFieldEntity);
                                dbMap.put(hkFieldEntity.getFeature(), hkFieldEntitys);
                            } else {
                                dbMap.get(hkFieldEntity.getFeature()).add(hkFieldEntity);
                            }
                        }
                    }
                    List<HkFieldEntity> fieldsToBeAdded = new ArrayList<>();
                    List<Long> dbFeatureIds = new ArrayList<>();
                    for (Map.Entry<Long, List<HkFieldEntity>> entry : dbMap.entrySet()) {
                        Long featureId = entry.getKey();
                        dbFeatureIds.add(featureId);
                        List<HkFieldEntity> list = entry.getValue();
                        if (!CollectionUtils.isEmpty(list)) {
                            Map<String, HkFieldEntity> dbFieldNameEntity = new HashMap<>();
                            for (HkFieldEntity hkFieldEntity : list) {
                                if (hkFieldEntity.getSection() == null) {
                                    dbFieldNameEntity.put(hkFieldEntity.getDbFieldName(), hkFieldEntity);
                                }
                            }

                            List<FieldDataBean> fieldDataBeans = featureWithoutSectionMap.get(featureIdToNameMap.get(featureId));
                            List<HkFieldEntity> compareFieldDbList = new ArrayList<>();
                            for (FieldDataBean fieldDataBean : fieldDataBeans) {
                                HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                                if (hkFieldEntity.getIsCustomField()) {
//                                    hkFieldEntity.setFranchise(1L);
//                                }
                                hkFieldEntity.setFeature(featureId);
                                compareFieldDbList.add(hkFieldEntity);
                            }
                            if (!CollectionUtils.isEmpty(compareFieldDbList)) {
                                for (HkFieldEntity hkFieldEntity : compareFieldDbList) {
                                    Boolean isPointerSatisfied = false;
                                    if (hkFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                                        if (deployTimePointerMaps.get(hkFieldEntity.getDbFieldName()) != null) {

                                            if (!dbFieldNameEntity.containsKey(deployTimePointerMaps.get(hkFieldEntity.getDbFieldName()))) {
                                                isPointerSatisfied = true;
                                            } else {
                                                isPointerSatisfied = false;
                                            }

//                                            for (Map.Entry<String, String> deployMap : deployTimePointerMaps.entrySet()) {
//                                                if (!dbFieldNameEntity.containsKey(deployMap.getValue())) {
//                                                    isPointerSatisfied = true;
//                                                } else {
//                                                    isPointerSatisfied = false;
//                                                }
//
//                                            }
                                        } else {
                                            isPointerSatisfied = true;
                                        }
                                    } else {
                                        isPointerSatisfied = true;
                                    }
                                    String dbAppendedField = hkFieldEntity.getDbFieldName() + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(hkFieldEntity.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + hkFieldEntity.getFieldType();

                                    //Modify validation pattern if exists
                                    String SheetValidationPattern = hkFieldEntity.getValidationPattern();
                                    HkFieldEntity dbFieldEntityTemp = null;
                                    if (dbFieldNameEntity.containsKey(hkFieldEntity.getDbFieldName())) {
                                        dbFieldEntityTemp = dbFieldNameEntity.get(hkFieldEntity.getDbFieldName());
                                    } else if (dbFieldNameEntity.containsKey(dbAppendedField)) {
                                        dbFieldEntityTemp = dbFieldNameEntity.get(dbAppendedField);
                                    }
                                    if (dbFieldEntityTemp != null && dbFieldEntityTemp.getValidationPattern() != null) {
                                        String dbValidationPattern = dbFieldEntityTemp.getValidationPattern();
                                        Type validationPatternObjType = new TypeToken<Map<String, Object>>() {
                                        }.getType();
                                        Map<String, Object> validationPatternObj = (new Gson()).fromJson(dbValidationPattern, validationPatternObjType);
                                        Map<String, Object> sheetValidationPatternObj = (new Gson()).fromJson(SheetValidationPattern, validationPatternObjType);
                                        if (!CollectionUtils.isEmpty(sheetValidationPatternObj)) {
                                            if (!CollectionUtils.isEmpty(validationPatternObj)) {
                                                for (String key : sheetValidationPatternObj.keySet()) {
                                                    if (!validationPatternObj.containsKey(key)) {
                                                        validationPatternObj.put(key, sheetValidationPatternObj.get(key));
                                                    }
                                                }
                                            } else {
                                                validationPatternObj = new HashMap<>(sheetValidationPatternObj);
                                            }
                                        }
                                        SheetValidationPattern = (new Gson()).toJson(validationPatternObj, validationPatternObjType);
                                    }
                                    hkFieldEntity.setValidationPattern(SheetValidationPattern);

                                    if (!dbFieldNameEntity.containsKey(hkFieldEntity.getDbFieldName()) && !dbFieldNameEntity.containsKey(dbAppendedField) && isPointerSatisfied) {
                                        fieldsToBeAdded.add(hkFieldEntity);
                                    } else {

                                        //Update Existing fields
                                        HkFieldEntity dbFieldEntity = dbFieldNameEntity.get(hkFieldEntity.getDbFieldName());
                                        if (dbFieldEntity == null) {
                                            if (hkFieldEntity.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                                                // For pointer we have appended in different way
                                                dbFieldEntity = dbFieldNameEntity.get(deployTimePointerMaps.get(hkFieldEntity.getDbFieldName()));
                                            } else {
                                                dbFieldEntity = dbFieldNameEntity.get(dbAppendedField);
                                            }
                                        }

                                        if (!toStringField(dbFieldEntity).equals(toStringField(hkFieldEntity))) {
                                            hkFieldEntity.setId(dbFieldEntity.getId());
                                            hkFieldEntity.setCreatedBy(dbFieldEntity.getCreatedBy());
                                            hkFieldEntity.setCreatedOn(dbFieldEntity.getCreatedOn());
                                            fieldsToBeAdded.add(hkFieldEntity);
                                        }

                                    }
                                }
//                            for (HkFieldEntity hkFieldEntity : list) {
//                                if (!compareFieldDbList.contains(hkFieldEntity)) {
//                                    fieldsToBeRemoved.add(hkFieldEntity);
//                                }
//                            }
                            }
                            if (!CollectionUtils.isEmpty(dbFieldNameEntity)) {
                                for (String dbFieldName : dbFieldNameEntity.keySet()) {
                                    boolean isExists = false;
                                    for (HkFieldEntity hkFieldEntity : compareFieldDbList) {
                                        if (!StringUtils.isEmpty(dbFieldName) && dbFieldName.equals(hkFieldEntity.getDbFieldName())) {
                                            isExists = true;
                                            break;
                                        }
                                    }
                                    if (!isExists && dbFieldNameEntity.get(dbFieldName).getSection() == null && !dbFieldNameEntity.get(dbFieldName).getIsCustomField()) {
                                        //Archive Non-existent fields
                                        HkFieldEntity dbFieldEntity = dbFieldNameEntity.get(dbFieldName);
                                        dbFieldEntity.setIsArchive(Boolean.TRUE);
                                        fieldsToBeAdded.add(dbFieldEntity);
                                    }
                                }
                            }

                        }
//                    if (!CollectionUtils.isEmpty(fieldsToBeAdded) && !CollectionUtils.isEmpty(fieldsToBeRemoved)) {
//                        for (HkFieldEntity hkFieldEntity : fieldsToBeRemoved) {
//                            hkFieldEntity.setIsArchive(true);
//                            fieldsToBeAdded.add(hkFieldEntity);
//                        }
//                    }

                    }
                    //If no field exist for a feature in database but in feature sheet. Add all the new fields
                    for (Long feature : featureIds) {
                        if (!dbFeatureIds.contains(feature)) {
                            List<FieldDataBean> fieldDataBeans = featureWithoutSectionMap.get(featureIdToNameMap.get(feature));
                            for (FieldDataBean fieldDataBean : fieldDataBeans) {
                                HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                                if (hkFieldEntity.getIsCustomField()) {
//                                    hkFieldEntity.setFranchise(1L);
//                                }
                                hkFieldEntity.setFeature(feature);
                                fieldsToBeAdded.add(hkFieldEntity);
                            }
                        }
                    }
//                    System.out.println("Size ::::::::::::::::::::::::: " + fieldsToBeAdded.size());
                    if (fieldsToBeAdded.size() > 0) {
                        fieldService.createFields(fieldsToBeAdded);
                    }
                    //call create fields method 
                } else {
                    List<HkFieldEntity> hkFieldEntitys = new ArrayList<>();
                    for (Map.Entry<String, List<FieldDataBean>> entry : featureWithoutSectionMap.entrySet()) {
                        String feature = entry.getKey();
                        //System.out.println("feature name is ++++++++++++++++++++++++++ " + feature);
                        List<FieldDataBean> list = entry.getValue();
                        //System.out.println("inn this size is =======>>> " + list.size());
                        for (FieldDataBean fieldDataBean : list) {
                            HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                            if (hkFieldEntity.getIsCustomField()) {
//                                hkFieldEntity.setFranchise(1L);
//                            }
                            hkFieldEntity.setFeature(featureNameToIdMap.get(feature));
                            hkFieldEntitys.add(hkFieldEntity);
                        }
                    }
                    fieldService.createFields(hkFieldEntitys);
//                    System.out.println("in  else list size is :::::::::::::::::::::::::::::::;; " + hkFieldEntitys.size());
                }
            }
        } catch (GenericDatabaseException ex) {
            Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void createSectionAndField(Map<String, Map<String, List<FieldDataBean>>> featureSectionFieldMap) {
        List<String> featureNames = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<FieldDataBean>>> entry : featureSectionFieldMap.entrySet()) {
            String string = entry.getKey();
            featureNames.add(string);
        }

        try {
            Map<String, Object> in = new HashMap<>();
            in.put(UMFeatureDao.NAME, featureNames);
            Map<GenericDao.QueryOperators, Object> criterias = new HashMap<>();
            criterias.put(GenericDao.QueryOperators.IN, in);
            List<UMFeature> features = featureService.retrieveFeatures(null, criterias, null);
            Map<String, Long> featureNameIdMap = new HashMap<>();
            Map<Long, String> featureIdToNameMap = new HashMap<>();
            List<Long> fieldsToBeUpdated = new LinkedList<>();
            if (!CollectionUtils.isEmpty(features)) {
                List<Long> featureIds = new ArrayList<>();
                for (UMFeature uMFeature : features) {
                    featureIds.add(uMFeature.getId());
                    featureNameIdMap.put(uMFeature.getName(), uMFeature.getId());
                    featureIdToNameMap.put(uMFeature.getId(), uMFeature.getName());
                }
                List<HkFeatureSectionEntity> retrieveFeatureSectionEntitiesByFeatureIds = fieldService.retrieveFeatureSectionEntitiesByFeatureIds(featureIds);
                Map<Long, List<HkSectionEntity>> mapToBeSent = new HashMap<>();
                if (!CollectionUtils.isEmpty(retrieveFeatureSectionEntitiesByFeatureIds)) {
                    Map<String, Map<String, List<HkFieldEntity>>> featureSectionFieldMapDB = new HashMap<>();
                    Map<String, List<String>> featureSectionMapFromDB = new HashMap<>();
                    for (HkFeatureSectionEntity hkFeatureSectionEntity : retrieveFeatureSectionEntitiesByFeatureIds) {
                        List<String> sectionsInDB = featureSectionMapFromDB.get(featureIdToNameMap.get(hkFeatureSectionEntity.getFeature()));
                        if (sectionsInDB == null) {
                            sectionsInDB = new ArrayList<>();
                            featureSectionMapFromDB.put(featureIdToNameMap.get(hkFeatureSectionEntity.getFeature()), sectionsInDB);
                        }
                        sectionsInDB.add(hkFeatureSectionEntity.getSection().getSectionName());

                        List<HkFieldEntity> fieldEntityList = fieldService.retrieveFieldsByCriteria(hkFeatureSectionEntity.getFeature(), hkFeatureSectionEntity.getSectionId(), false, null, null, false, 0l);
                        Map<String, List<HkFieldEntity>> sectionFieldMap = new HashMap<>();
                        if (fieldEntityList != null) {
                            sectionFieldMap.put(hkFeatureSectionEntity.getSection().getSectionName(), fieldEntityList);
                        }
                        if (featureSectionFieldMapDB.get(featureIdToNameMap.get(hkFeatureSectionEntity.getFeature())) == null) {
                            featureSectionFieldMapDB.put(featureIdToNameMap.get(hkFeatureSectionEntity.getFeature()), new HashMap<>());
                        }
                        featureSectionFieldMapDB.get(featureIdToNameMap.get(hkFeatureSectionEntity.getFeature())).putAll(sectionFieldMap);
                    }

                    Map<String, List<String>> featureSectionMapFromUI = new HashMap<>();

                    for (Map.Entry<String, Map<String, List<FieldDataBean>>> entry : featureSectionFieldMap.entrySet()) {
                        String featureName = entry.getKey();
                        List<String> sectionNames = new ArrayList<>();
                        Map<String, List<FieldDataBean>> sectionMap = featureSectionFieldMap.get(featureName);
                        for (Map.Entry<String, List<FieldDataBean>> entry1 : sectionMap.entrySet()) {
                            String sectionName = entry1.getKey();
                            sectionNames.add(sectionName);
                        }
                        featureSectionMapFromUI.put(featureName, sectionNames);
                    }
                    for (String s : featureSectionMapFromUI.keySet()) {
                        List<String> duplicateFeatureSection = new ArrayList<>();
                        if (featureSectionMapFromDB.get(s) != null) {
                            for (String string : featureSectionMapFromDB.get(s)) {
                                if (featureSectionMapFromUI.get(s).contains(string)) {
                                    duplicateFeatureSection.add(string);
                                }
                            }
                            featureSectionMapFromUI.get(s).removeAll(duplicateFeatureSection);
                        }
                    }

                    for (Map.Entry<String, Map<String, List<HkFieldEntity>>> entry : featureSectionFieldMapDB.entrySet()) {
                        String featureName = entry.getKey();
                        Map<String, List<HkFieldEntity>> sectionFieldMapDB = entry.getValue();
                        Map<String, List<FieldDataBean>> sectionFieldMap = featureSectionFieldMap.get(featureName);
                        List<HkFieldEntity> fieldsToBeRemoved = new ArrayList<>();
                        if (sectionFieldMapDB != null && sectionFieldMap != null) {
                            Set<String> nonExistentSections = new HashSet<>(sectionFieldMapDB.keySet());
                            for (Map.Entry<String, List<FieldDataBean>> sectionFieldEntry : sectionFieldMap.entrySet()) {
                                String sectionName = sectionFieldEntry.getKey();
                                List<FieldDataBean> fieldDataBeanList = sectionFieldEntry.getValue();
                                List<HkFieldEntity> fieldEntityDB = sectionFieldMapDB.get(sectionName);
                                if (!CollectionUtils.isEmpty(fieldEntityDB)) {
                                    nonExistentSections.remove(sectionName);
                                    for (HkFieldEntity fieldEntity : fieldEntityDB) {
                                        boolean isFieldExists = false;
                                        for (FieldDataBean fieldDataBean : fieldDataBeanList) {
                                            if (fieldEntity.getFieldLabel().equals(fieldDataBean.getFieldLabel())) {
                                                fieldDataBean.setId(fieldEntity.getId());
                                                isFieldExists = true;
                                                if (!toStringField(fieldEntity).equals(toStringField(convertFieldDataBeanToEntity(fieldDataBean)))) {
                                                    fieldsToBeUpdated.add(fieldDataBean.getId());
                                                }
                                                break;
                                            }
                                        }
                                        if (!isFieldExists) {
                                            if (!fieldEntity.getIsCustomField()) {
                                                fieldsToBeRemoved.add(fieldEntity);
                                            }
                                        }
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(nonExistentSections)) {
                                for (String sectionName : nonExistentSections) {
                                    List<HkFieldEntity> fieldEntitySet = sectionFieldMapDB.get(sectionName);
                                    if (!CollectionUtils.isEmpty(fieldEntitySet)) {
                                        fieldsToBeRemoved.addAll(fieldEntitySet);
                                    }
                                    for (HkFeatureSectionEntity hkFeatureSectionEntity : retrieveFeatureSectionEntitiesByFeatureIds) {
                                        if ((hkFeatureSectionEntity.getFeature() == featureNameIdMap.get(featureName))
                                                && hkFeatureSectionEntity.getSection().getSectionName().equals(sectionName)) {
                                            fieldService.removeSection(hkFeatureSectionEntity.getSectionId());
                                        }
                                    }
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(fieldsToBeRemoved)) {
                            fieldService.removeFields(fieldsToBeRemoved);
                        }
                    }
                    //Add new sections as well as fields
                    if (!featureSectionMapFromUI.isEmpty()) {

                        for (Map.Entry<String, List<String>> entry : featureSectionMapFromUI.entrySet()) {
                            List<HkSectionEntity> hkSectionEntitys = new ArrayList<>();
                            String string = entry.getKey();
                            List<String> list = entry.getValue();
                            for (String sectionName : list) {
                                HkSectionEntity hkSectionEntity = new HkSectionEntity();
                                hkSectionEntity.setCreatedOn(new Date());
                                hkSectionEntity.setIsArchive(false);
                                hkSectionEntity.setSectionName(sectionName);
                                Set<HkFieldEntity> fieldEntitySet = new HashSet<>();
                                if (featureSectionFieldMap.get(string).get(sectionName) != null) {
                                    for (FieldDataBean fieldDataBean : featureSectionFieldMap.get(string).get(sectionName)) {
                                        HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                                        if (hkFieldEntity.getIsCustomField()) {
//                                            hkFieldEntity.setFranchise(1L);
//                                        }
                                        hkFieldEntity.setFeature(featureNameIdMap.get(string));
                                        hkFieldEntity.setSection(hkSectionEntity);
                                        fieldEntitySet.add(hkFieldEntity);
                                    }
                                }
                                hkSectionEntity.setHkFieldEntitySet(fieldEntitySet);
                                hkSectionEntitys.add(hkSectionEntity);
                            }
                            serviceWrapper.createAllLocaleForEntity(list, "Section", 1l, 1l);
                            Long featureId = featureNameIdMap.get(string);
                            mapToBeSent.put(featureId, hkSectionEntitys);

                        }
                        if (!mapToBeSent.isEmpty()) {
                            fieldService.createSections(mapToBeSent);
                        }

                    }
                    //Check if Fields only changed / added in an existing section and add only fields
                    for (Map.Entry<String, Map<String, List<FieldDataBean>>> entry : featureSectionFieldMap.entrySet()) {
                        String featureName = entry.getKey();
                        List<HkSectionEntity> hkSectionEntitys = new ArrayList<>();
                        Map<String, List<FieldDataBean>> sectionMap = featureSectionFieldMap.get(featureName);
                        Long featureId = featureNameIdMap.get(featureName);
                        for (Map.Entry<String, List<FieldDataBean>> entry1 : sectionMap.entrySet()) {
                            String sectionName = entry1.getKey();
                            List<String> existingSections = new ArrayList<>();
                            if (!featureSectionMapFromUI.isEmpty()) {
                                existingSections = featureSectionMapFromUI.get(featureName);
                            }
                            List<HkFieldEntity> fieldEntitySet = new ArrayList<>();
                            Long sectionId = null;
                            if (existingSections != null && !existingSections.contains(sectionName)) {
                                for (FieldDataBean fieldDataBean : entry1.getValue()) {

                                    HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                                    if (hkFieldEntity.getIsCustomField()) {
//                                        hkFieldEntity.setFranchise(1L);
//                                    }
                                    hkFieldEntity.setFeature(featureNameIdMap.get(featureName));
                                    hkFieldEntity.setSection(null);
                                    if (!CollectionUtils.isEmpty(retrieveFeatureSectionEntitiesByFeatureIds)) {
                                        for (HkFeatureSectionEntity featureSectionEntity : retrieveFeatureSectionEntitiesByFeatureIds) {
                                            if (featureSectionEntity.getFeature() == featureId && featureSectionEntity.getSection().getSectionName().equals(sectionName)) {
                                                hkFieldEntity.setSection(featureSectionEntity.getSection());
                                            }
                                        }
                                    }
                                    if (fieldDataBean.getId() != null) {
                                        hkFieldEntity.setId(fieldDataBean.getId());
                                    }
                                    sectionId = hkFieldEntity.getSection().getId();
                                    if (fieldsToBeUpdated.contains(hkFieldEntity.getId())) {
                                        fieldEntitySet.add(hkFieldEntity);
                                    }
                                }
                            }                          
                            if (!CollectionUtils.isEmpty(fieldEntitySet)) {
                                fieldService.saveFields(featureId, sectionId, null, fieldEntitySet, false, 0l);
                            }

                        }

                    }

                } else {
                    for (Map.Entry<String, Map<String, List<FieldDataBean>>> entry : featureSectionFieldMap.entrySet()) {
                        String string = entry.getKey();
                        List<HkSectionEntity> hkSectionEntitys = new ArrayList<>();
                        Map<String, List<FieldDataBean>> sectionMap = featureSectionFieldMap.get(string);
                        List<String> texts = new ArrayList<>();
                        for (Map.Entry<String, List<FieldDataBean>> entry1 : sectionMap.entrySet()) {
                            String sectionName = entry1.getKey();
                            texts.add(sectionName);
                            HkSectionEntity hkSectionEntity = new HkSectionEntity();
                            hkSectionEntity.setCreatedOn(new Date());
                            hkSectionEntity.setIsArchive(false);
                            hkSectionEntity.setSectionName(sectionName);
                            Set<HkFieldEntity> fieldEntitySet = new HashSet<>();
                            for (FieldDataBean fieldDataBean : entry1.getValue()) {
                                HkFieldEntity hkFieldEntity = this.convertFieldDataBeanToEntity(fieldDataBean);
//                                if (hkFieldEntity.getIsCustomField()) {
//                                    hkFieldEntity.setFranchise(1L);
//                                }
                                hkFieldEntity.setFeature(featureNameIdMap.get(string));
                                hkFieldEntity.setSection(hkSectionEntity);
                                fieldEntitySet.add(hkFieldEntity);
                            }
                            hkSectionEntity.setHkFieldEntitySet(fieldEntitySet);
                            hkSectionEntitys.add(hkSectionEntity);
                        }
                        serviceWrapper.createAllLocaleForEntity(texts, "Section", 1l, 1l);

                        Long featureId = featureNameIdMap.get(string);
                        mapToBeSent.put(featureId, hkSectionEntitys);

                    }
                    fieldService.createSections(mapToBeSent);
                }
            }

        } catch (GenericDatabaseException ex) {
            Logger.getLogger(RoleFeatureInitialization.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private HkFieldEntity convertFieldDataBeanToEntity(FieldDataBean fieldDataBean) {
        HkFieldEntity hkFieldEntity = new HkFieldEntity();
        if (fieldDataBean != null) {
            hkFieldEntity.setComponentType(fieldDataBean.getComponentType());
            hkFieldEntity.setCreatedBy(1l);
            hkFieldEntity.setCreatedOn(new Date());
            hkFieldEntity.setDbBaseName(fieldDataBean.getDbBaseName());
            hkFieldEntity.setDbFieldName(fieldDataBean.getDbFieldName());
            hkFieldEntity.setFieldLabel(fieldDataBean.getFieldLabel());
            hkFieldEntity.setFieldType(fieldDataBean.getFieldType());
            hkFieldEntity.setFranchise(0L);
            hkFieldEntity.setIsArchive(Boolean.FALSE);
            hkFieldEntity.setIsCustomField(Boolean.FALSE);
            hkFieldEntity.setIsPrivate(Boolean.FALSE);
            hkFieldEntity.setValidationPattern(fieldDataBean.getValidationPattern());
            hkFieldEntity.setUiFieldName(fieldDataBean.getUiFieldName());
            hkFieldEntity.setStatus("A");
            hkFieldEntity.setSeqNo(1);
            hkFieldEntity.setLastModifiedBy(1l);
            hkFieldEntity.setLastModifiedOn(new Date());
            if (fieldDataBean.isIsCustom() != null) {
                hkFieldEntity.setIsCustomField(fieldDataBean.isIsCustom());
            }
            if (fieldDataBean.isIsEditable() != null) {
                hkFieldEntity.setIsEditable(fieldDataBean.isIsEditable());
            }
            if (fieldDataBean.getDbBaseType() != null) {
                hkFieldEntity.setDbBaseType(fieldDataBean.getDbBaseType());
            }
            if (fieldDataBean.getFormulaValue() != null) {
                hkFieldEntity.setFormulaValue(fieldDataBean.getFormulaValue());
            }
        }
        return hkFieldEntity;
    }

    // Method added by Shifa Salheen for appending component type and field type in dbfieldlabel
    public void appendComponentTypeInDbFieldName() {
        List<HkFieldEntity> allCustomFields = fieldService.retrieveAllFieldsByCompanyId(null);
        List<HkFieldEntity> fieldListToBeUpdated = null;
        if (!CollectionUtils.isEmpty(allCustomFields)) {
            Map<String, String> deployTimePointerMap = new HashMap<String, String>(HkSystemConstantUtil.DEPLOY_TIME_POINTERS_MAP);

            fieldListToBeUpdated = new ArrayList<>();
            for (HkFieldEntity customfield : allCustomFields) {
                // Change the custom fields where component type and field type has not been appended
                if (customfield.getComponentType().equals(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
                    if (deployTimePointerMap != null) {

                        if (deployTimePointerMap.containsKey(customfield.getDbFieldName())) {
                            String appendedNamePointer = deployTimePointerMap.get(customfield.getDbFieldName());
                            customfield.setDbFieldName(appendedNamePointer);
                            customfield.setUiFieldName(appendedNamePointer);
                            fieldListToBeUpdated.add(customfield);
                        }
                    }
                } else {
                    if (!customfield.getUiFieldName().contains(HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL) && customfield.getIsCustomField()) {
                        String dbFieldName = customfield.getUiFieldName();
                        customfield.setDbFieldName(dbFieldName + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customfield.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + customfield.getFieldType());
                        customfield.setUiFieldName(dbFieldName + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP.get(customfield.getComponentType()) + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL + customfield.getFieldType());

                        fieldListToBeUpdated.add(customfield);
                    }
                }
                if (!customfield.getIsCustomField()) {
                    if (customfield.getUiFieldName().contains(HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL) || customfield.getDbFieldName().contains(HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL)) {
                        String dbField = customfield.getDbFieldName();
                        if (dbField.contains(HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL)) {
                            String[] dbFieldArray = dbField.split(HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                            dbField = dbFieldArray[0];
                            customfield.setDbFieldName(dbField);
                            customfield.setUiFieldName(dbField);
                        }
                    }
                    fieldListToBeUpdated.add(customfield);
                }

            }
            fieldService.createAllFields(fieldListToBeUpdated);
        }
    }

    private String toStringUMFeature(UMFeature feature) {
        StringBuilder featureString = new StringBuilder(feature.getName());
        featureString.append("##");
        featureString.append(feature.getMenuLabel());
        featureString.append("##");
        featureString.append(feature.getMenuType());
        featureString.append("##");
        featureString.append(feature.getDescription());
        featureString.append("##");
        featureString.append(feature.getFeatureUrl());
        featureString.append("##");
        featureString.append(feature.getWebserviceUrl());
        featureString.append("##");
        featureString.append(feature.getSeqNo());
        featureString.append("##");
        featureString.append(feature.getPrecedence());
        featureString.append("##");
        featureString.append(feature.getMenuCategory());

        return featureString.toString();
    }

    private String toStringField(HkFieldEntity fieldEntity) {
        StringBuilder fieldString = new StringBuilder(fieldEntity.getFieldLabel());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getComponentType());
        fieldString.append("##");
        if (StringUtils.hasText(fieldEntity.getUiFieldName())) {
            fieldString.append((new StringTokenizer(fieldEntity.getUiFieldName(), HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL).nextToken()));
            fieldString.append("##");
        }
        fieldString.append(fieldEntity.getDbBaseName());
        fieldString.append("##");
        if (StringUtils.hasText(fieldEntity.getComponentType()) && !fieldEntity.getComponentType().equalsIgnoreCase(HkSystemConstantUtil.CustomField.ComponentType.POINTER)) {
            fieldString.append(fieldEntity.getValidationPattern());
            fieldString.append("##");
        }
        if (StringUtils.hasText(fieldEntity.getDbFieldName())) {
            fieldString.append((new StringTokenizer(fieldEntity.getDbFieldName(), HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL).nextToken()));
            fieldString.append("##");
        }
        fieldString.append(fieldEntity.getDbBaseType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getIsCustomField());
        fieldString.append("##");
        fieldString.append(fieldEntity.getIsEditable());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFormulaValue());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());
        fieldString.append("##");
        fieldString.append(fieldEntity.getFieldType());

        return fieldString.toString();
    }

}
