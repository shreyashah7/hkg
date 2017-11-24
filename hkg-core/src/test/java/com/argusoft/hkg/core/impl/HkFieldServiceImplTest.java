/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.core.impl;

import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkSectionEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for HkFieldService class.
 *
 * @author Mital
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class HkFieldServiceImplTest {

    @Resource
    private HkFieldService fieldService;
    @Resource
    private HkCustomFieldService customFieldService;
    private HkSectionEntity sectionEntity;
    private HkFieldEntity fieldEntity;
    private Set<Long> featureSet;
    private long companyId = 1l;

    public HkFieldServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        sectionEntity = new HkSectionEntity();
        sectionEntity.setCreatedOn(new Date());
        sectionEntity.setSectionName("Section A");
        sectionEntity.setIsArchive(false);

        featureSet = new HashSet<>();
        featureSet.add(1L);
        featureSet.add(2L);

        fieldEntity = new HkFieldEntity();
        fieldEntity.setCreatedBy(1L);
        fieldEntity.setCreatedOn(new Date());
        fieldEntity.setComponentType(HkSystemConstantUtil.CustomField.ComponentType.NUMBER);
        fieldEntity.setFranchise(1L);
        fieldEntity.setFeature(1L);
        fieldEntity.setFieldLabel("Shape");
        fieldEntity.setIsArchive(false);
        fieldEntity.setIsCustomField(true);
        fieldEntity.setLastModifiedBy(1L);
        fieldEntity.setLastModifiedOn(new Date());
        fieldEntity.setValidationPattern("{\"defaultValue\":\"Circle\",\"isRequired\":true}");
        fieldEntity.setFieldValues("Round, Circle, Square");
        fieldEntity.setStatus("A");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createSection method, of class HkFieldService.
     */
    @Test
    public void testCreateSection() {
//        System.out.println("createSection");
        fieldService.createSection(sectionEntity, featureSet);

        HkSectionEntity resultSectionEntity = fieldService.retrieveSectionById(sectionEntity.getId(), false);
        assertEquals(sectionEntity, resultSectionEntity);
    }

    /**
     * Test of updateSection method, of class HkFieldService.
     */
    @Test
    public void testUpdateSection() {
//        System.out.println("updateSection");
        fieldService.createSection(sectionEntity, featureSet);

        String newSectionName = "Section B";
        sectionEntity.setSectionName(newSectionName);
        featureSet.remove(2L);
        featureSet.add(3L);
        fieldService.updateSection(sectionEntity, featureSet);

        HkSectionEntity newSectionEntity = fieldService.retrieveSectionById(sectionEntity.getId(), false);
        assertEquals(newSectionName, newSectionEntity.getSectionName());
    }

    /**
     * Test of removeSection method, of class HkFieldService.
     */
    @Test
    public void testRemoveSection_HkSectionEntity() {
//        System.out.println("removeSection");
        fieldService.createSection(sectionEntity, featureSet);

        fieldService.removeSection(sectionEntity);

        HkSectionEntity resultSection = fieldService.retrieveSectionById(sectionEntity.getId(), true);
        assertEquals(true, resultSection.getIsArchive());
    }

    /**
     * Test of removeSection method, of class HkFieldService.
     */
    @Test
    public void testRemoveSection_Long() {
//        System.out.println("removeSection");
        fieldService.createSection(sectionEntity, featureSet);

        fieldService.removeSection(sectionEntity.getId());

        HkSectionEntity resultSection = fieldService.retrieveSectionById(sectionEntity.getId(), true);
        assertEquals(true, resultSection.getIsArchive());
    }

    /**
     * Test of retrieveSectionById method, of class HkFieldService.
     */
    @Test
    public void testRetrieveSectionById() {
//        System.out.println("retrieveSectionById");
        fieldService.createSection(sectionEntity, featureSet);

        HkSectionEntity result = fieldService.retrieveSectionById(sectionEntity.getId(), false);
        assertEquals(sectionEntity, result);
    }

    /**
     * Test of searchFields method, of class HkFieldService.
     */
    @Test
    public void testSearchFields() {
//        System.out.println("searchFields");

        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);
        Map<Long, String> map = new HashMap<>();
        map.put(1l, "Employee");
        Map<Long, List<String>> resultFieldMap = fieldService.searchFields(fieldEntity.getFieldLabel(), map,companyId);
        //assertTrue(resultFieldMap.size() > 0);

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of saveFields method, of class HkFieldService.
     */
    @Test
    public void testSaveFields() {
//        System.out.println("saveFields");

        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        List<Long> fieldIds = new ArrayList<>();
        fieldIds.add(fieldEntity.getId());
        HkFieldEntity resultField = fieldService.retrieveFieldsByIds(fieldIds, false).get(0);
        assertEquals(fieldEntity, resultField);

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of removeFields method, of class HkFieldService.
     */
//    @Test
    public void testRemoveFields_HkFieldEntity() {
//        System.out.println("removeFields");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        fieldService.removeFields(fieldList);

        List<Long> fieldIds = new ArrayList<>();
        fieldIds.add(fieldEntity.getId());
        HkFieldEntity resultField = fieldService.retrieveFieldsByIds(fieldIds, false).get(0);
        assertEquals(HkSystemConstantUtil.INACTIVE, resultField.getStatus());
    }

    /**
     * Test of removeFields method, of class HkFieldService.
     */
//    @Test
    public void testRemoveFields_Long() {
//        System.out.println("removeFields");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        List<Long> fieldIds = new ArrayList<>();
        fieldIds.add(fieldEntity.getId());
        fieldService.removeFields(fieldIds, 1L);

        HkFieldEntity resultField = fieldService.retrieveFieldsByIds(fieldIds, false).get(0);
        assertEquals(HkSystemConstantUtil.INACTIVE, resultField.getStatus());

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of retrieveFieldsByCriteria method, of class HkFieldService.
     */
    @Test
    public void testRetrieveFieldsByCriteria() {
//        System.out.println("retrieveFieldsByCriteria");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        Long featureId = fieldEntity.getFeature();
        Long sectionId = null;
        String status = HkSystemConstantUtil.ACTIVE;
        List<HkFieldEntity> result = fieldService.retrieveFieldsByCriteria(featureId, sectionId, true, status, false, false, companyId);
        assertTrue(result.size() > 0);

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of isFieldLableExistent method, of class HkFieldService.
     */
    @Test
    public void testIsFieldLabelExistent() {
//        System.out.println("isFieldLabelExistent");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        boolean result = fieldService.isFieldLabelExistent(fieldEntity.getFieldLabel(), (fieldEntity.getSection() == null ? null : fieldEntity.getSection().getId()));
        assertEquals(true, result);

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of retrieveFeaturesForExistingFields method, of class
     * HkFieldService.
     */
    @Test
    public void testRetrieveFeaturesForExistingFields() {
//        System.out.println("retrieveFeaturesForExistingFields");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        Set<Long> result = fieldService.retrieveFeaturesForExistingFields(true, 1l);
        assertTrue(result.size() > 0);

        fieldService.removeFields(fieldList);
    }

    /**
     * Test of retrieveSectionsByFeatureId method, of class HkFieldService.
     */
    @Test
    public void testRetrieveSectionsByFeatureId() {
//        System.out.println("retrieveSectionsByFeatureId");
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);

        Map<HkSectionEntity, List<HkFieldEntity>> resultMap = fieldService.retrieveSectionsByFeatureId(fieldEntity.getFeature(), 1l, true, false);
        assertTrue(resultMap.keySet().size() > 0);
        fieldService.removeFields(fieldList);
    }

    @Test
    public void retrieveAllFieldsByCompanyId() {
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);
        List<HkFieldEntity> allfieldList = fieldService.retrieveAllFieldsByCompanyId(1l);

        assertNotNull("retrieve all fields", allfieldList);

    }

    @Test
    public void createAllFields() {
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.createAllFields(fieldList);
        assertNotNull("All fields", fieldList);
    }

    @Test
    public void retrieveFieldsByFeatures() {
        List<HkFieldEntity> fieldList = new ArrayList<>();
        fieldList.add(fieldEntity);
        fieldService.saveFields(1l, null, "abc", fieldList, true, companyId);
        List<Long> featureIds = new ArrayList<>();
        featureIds.addAll(featureSet);
        List<HkFieldEntity> retrieveFieldsByFeatures = fieldService.retrieveFieldsByFeatures(featureIds, true, companyId);
        System.out.println("retrieveFieldsByFeatures--" + retrieveFieldsByFeatures.size());
        assertNotNull(retrieveFieldsByFeatures);

    }
}
